import { useEffect, useState } from 'react'
import '../styles/Devices.css'

const BASE_URL = import.meta.env.VITE_API_URL;

// ─── API helpers ────────────────────────────────────────────

async function apiFetch(path, options = {}) {
    const res = await fetch(`${BASE_URL}${path}`, {
        headers: { 'Content-Type': 'application/json' },
        ...options,
    });
    
    let json;
    try {
        json = await res.json();
    } catch {
        throw new Error('Server returned an unexpected response.');
    }
    
    if (!res.ok) throw new Error(json.message || 'Request failed');
    return json.data;
}

const api = {
    getDevices: () => apiFetch('/api/devices'),
    deleteDevice: (id) => apiFetch(`/api/devices/${id}`, { method: 'DELETE' }),
    getLayouts: () => apiFetch('/api/layouts'),

    getGroups: () => apiFetch('/api/device-groups'),
    createGroup: (name) => apiFetch('/api/device-groups', {
        method: 'POST',
        body: JSON.stringify({ name, description: '' }),
    }),
    updateGroup: (id, name, layoutId) => apiFetch(`/api/device-groups/${id}`, {
        method: 'PUT',
        body: JSON.stringify({ name, description: '', layoutId }),
    }),
    deleteGroup: (id) => apiFetch(`/api/device-groups/${id}`, { method: 'DELETE' }),

    assignDeviceToGroup: (deviceId, deviceGroupId) =>
        apiFetch(`/api/devices/${deviceId}/group`, {
            method: 'PATCH',
            body: JSON.stringify({ deviceGroupId }),
        }),
};

function Devices() {
    const [devices, setDevices] = useState([]);
    const [groups, setGroups] = useState([]);
    const [layouts, setLayouts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function loadData() {
            try {
                const [deviceData, groupData, layoutData] = await Promise.all([
                    api.getDevices(),
                    api.getGroups(),
                    api.getLayouts(),
                ]);

                const normalizedDevices = deviceData.map(d => {
                    const group = groupData.find(g => g.id === d.deviceGroupId);
                    return {
                        id: d.id,
                        name: d.name,
                        pairingId: d.pairingId,
                        isOnline: d.status === 'ONLINE',
                        deviceGroupId: d.deviceGroupId,
                        layoutId: group?.layout?.id ?? d.layoutId ?? null,
                    };
                });

                const normalizedGroups = groupData.map(g => ({
                    id: g.id,
                    name: g.name,
                    layoutId: g.layout?.id ?? null,
                    deviceIds: normalizedDevices
                        .filter(d => d.deviceGroupId === g.id)
                        .map(d => d.id),
                }));

                setDevices(normalizedDevices);
                setGroups(normalizedGroups);
                setLayouts(layoutData);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        }
        loadData();
    }, []);

    if (loading) return <div className="devices-page"><p>Loading...</p></div>;
    if (error) return <div className="devices-page"><p style={{ color: 'red' }}>Error: {error}</p></div>;

    return (
        <div className="devices-page">
            <h1>Device Management</h1>
            <h2>Manage displays and device groups</h2>
            <DeviceList devices={devices} setDevices={setDevices} groups={groups} setGroups={setGroups} layouts={layouts} />
            <DeviceGroups groups={groups} setGroups={setGroups} devices={devices} layouts={layouts} />
        </div>
    );
}

// Device Groups Section
function DeviceGroups({ groups, setGroups, devices, layouts }) {
    const [showModal, setShowModal] = useState(false);
    const [editingGroup, setEditingGroup] = useState(null);

    async function handleDelete(id) {
        try {
            await api.deleteGroup(id);
            setGroups(groups.filter(g => g.id !== id));
        } catch (err) {
            alert('Failed to delete group: ' + err.message);
        }
    }

    async function handleAdd(newGroup) {
        try {
            const created = await api.createGroup(newGroup.name);
            await Promise.all(
                newGroup.deviceIds.map(deviceId =>
                    api.assignDeviceToGroup(deviceId, created.id)
                )
            );
            setGroups([...groups, { id: created.id, name: created.name, deviceIds: newGroup.deviceIds }]);
            setShowModal(false);
        } catch (err) {
            alert('Failed to create group: ' + err.message);
        }
    }

    return (
        <div className="subpage">
            <div className="section-header">
                <h1>Device Groups</h1>
                <button onClick={() => setShowModal(true)}>New Group</button>
            </div>

            {groups.map(group => (
                <GroupCard key={group.id} group={group} onDelete={handleDelete} onEdit={setEditingGroup} devices={devices} layouts={layouts} />
            ))}

            {showModal && (
                <NewGroupModal
                    onClose={() => setShowModal(false)}
                    onAdd={handleAdd}
                />
            )}

            {editingGroup && (
                <EditGroupModal
                    group={editingGroup}
                    layouts={layouts}
                    onClose={() => setEditingGroup(null)}
                    onSave={(updatedGroup) => {
                        setGroups(groups.map(g => g.id === updatedGroup.id ? updatedGroup : g));
                        setEditingGroup(null);
                    }}
                />
            )}
        </div>
    );
}

function GroupCard({ group, onDelete, onEdit, devices, layouts }) {
    const assignedDevices = devices.filter(d => group.deviceIds.includes(d.id));
    const assignedLayout = layouts.find(l => l.id === group.layoutId);

    return (
        <div className="card">
            <div className="card-header">
                <h3>{group.name}</h3>
                <div className="card-UI">
                    <button onClick={() => onEdit(group)}>Edit</button>
                    <button onClick={() => onDelete(group.id)}>Delete</button>
                </div>
            </div>
            <p>
                {group.deviceIds.length} device{group.deviceIds.length !== 1 ? 's' : ''}
                {' · '}
                {assignedLayout ? assignedLayout.name : 'No layout assigned'}
            </p>
            <div style={{ marginTop: '8px', display: 'flex', flexDirection: 'column', gap: '4px' }}>
                {assignedDevices.length === 0 ? (
                    <p style={{ color: '#aaa', fontSize: '0.85rem' }}>No devices assigned.</p>
                ) : (
                    assignedDevices.map(device => (
                        <div key={device.id} style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.85rem' }}>
                            <StatusBadge isOnline={device.isOnline} />
                            <span>{device.name}</span>
                            <span style={{ color: '#aaa' }}>— Pairing ID: {device.pairingId}</span>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}

// Devices Section
function DeviceList({ devices, setDevices, groups, setGroups, layouts }) {
    const [showModal, setShowModal] = useState(false);
    const [editingDevice, setEditingDevice] = useState(null);

    async function handleDelete(id) {
        try {
            await api.deleteDevice(id);
            setDevices(devices.filter(d => d.id !== id));
        } catch (err) {
            alert('Failed to delete device: ' + err.message);
        }
    }

    function handleEditSave(updatedDevice) {
        setDevices(devices.map(d => d.id === updatedDevice.id ? updatedDevice : d));

        setGroups(groups.map(g => {
            if (g.id === updatedDevice.deviceGroupId) {
                return { ...g, deviceIds: [...g.deviceIds.filter(id => id !== updatedDevice.id), updatedDevice.id] };
            } else {
                return { ...g, deviceIds: g.deviceIds.filter(id => id !== updatedDevice.id) };
            }
        }));

        setEditingDevice(null);
    }

    return (
        <div className="subpage">
            <div className="section-header">
                <h1>Devices</h1>
                <button onClick={() => setShowModal(true)}>Add Device</button>
            </div>

            {devices.map(device => (
                <DeviceCard
                    key={device.id}
                    device={device}
                    onDelete={handleDelete}
                    onEdit={setEditingDevice}
                    layouts={layouts}
                />
            ))}

            {showModal && (
                <NewDeviceModal
                    onClose={() => setShowModal(false)}
                    onAdd={(newDevice) => {
                        setDevices([...devices, newDevice]);
                        setShowModal(false);
                    }}
                />
            )}

            {editingDevice && (
                <EditDeviceModal
                    device={editingDevice}
                    groups={groups}
                    onClose={() => setEditingDevice(null)}
                    onSave={handleEditSave}
                />
            )}
        </div>
    );
}

function StatusBadge({ isOnline }) {
    const styles = {
        display: 'inline-flex',
        alignItems: 'center',
        gap: '6px',
        padding: '4px 12px',
        borderRadius: '6px',
        fontWeight: '600',
        fontSize: '0.85rem',
        color: 'white',
        backgroundColor: isOnline ? '#22c55e' : '#dc2626',
    };

    return (
        <span style={styles}>
            {isOnline ? '✓' : '✕'} {isOnline ? 'Online' : 'Offline'}
        </span>
    );
}

function DeviceCard({ device, onDelete, onEdit, layouts }) {
    const assignedLayout = layouts.find(l => l.id === device.layoutId);

    return (
        <div className="card">
            <div className="card-header">
                <h3>{device.name}</h3>
                <div className="card-UI">
                    {/* <button>View</button> */}
                    <StatusBadge isOnline={device.isOnline} />
                    <button onClick={() => onEdit(device)}>Edit</button>
                    <button onClick={() => onDelete(device.id)}>Delete</button>
                </div>
            </div>
            <p>Pairing ID: {device.pairingId}</p>
            <p>Layout: {assignedLayout ? assignedLayout.name : 'No layout assigned'}</p>
        </div>
    );
}

function NewGroupModal({ onClose, onAdd }) {
    const [name, setName] = useState('');

    function handleSubmit() {
        if (!name) return;
        onAdd({ name, deviceIds: [] });
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={e => e.stopPropagation()}>
                <h2>New Device Group</h2>

                <label>Name</label>
                <input
                    value={name}
                    onChange={e => setName(e.target.value)}
                    placeholder="Group name"
                />

                <div className="modal-buttons">
                    <button onClick={onClose}>Cancel</button>
                    <button onClick={handleSubmit}>Create</button>
                </div>
            </div>
        </div>
    );
}

function EditGroupModal({ group, layouts, onClose, onSave }) {
    const [name, setName] = useState(group.name);
    const [selectedLayoutId, setSelectedLayoutId] = useState(group.layoutId ?? null);

    async function handleSubmit() {
        if (!name) return;
        try {
            await api.updateGroup(group.id, name, selectedLayoutId);
            onSave({ ...group, name, layoutId: selectedLayoutId });
        } catch (err) {
            alert('Failed to update group: ' + err.message);
        }
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={e => e.stopPropagation()}>
                <h2>Edit Group</h2>

                <label>Name</label>
                <input
                    value={name}
                    onChange={e => setName(e.target.value)}
                    placeholder="Group name"
                />

                <label style={{ marginTop: '12px', display: 'block' }}>Layout</label>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop: '8px' }}>
                    {layouts.length === 0 ? (
                        <p style={{ color: '#aaa', fontSize: '0.85rem' }}>No layouts available.</p>
                    ) : (
                        layouts.map(layout => {
                            const isSelected = selectedLayoutId === layout.id;
                            return (
                                <div
                                    key={layout.id}
                                    onClick={() => setSelectedLayoutId(layout.id)}
                                    style={{
                                        padding: '8px 12px',
                                        borderRadius: '6px',
                                        cursor: 'pointer',
                                        background: isSelected ? '#3b3b3b' : '#f5f5f5',
                                        color: isSelected ? 'white' : 'black',
                                        fontWeight: isSelected ? '600' : 'normal',
                                        border: isSelected ? '2px solid #111' : '2px solid transparent',
                                    }}
                                >
                                    {layout.name}
                                </div>
                            );
                        })
                    )}
                </div>

                <div className="modal-buttons">
                    <button onClick={onClose}>Cancel</button>
                    <button onClick={handleSubmit}>Save</button>
                </div>
            </div>
        </div>
    );
}

function NewDeviceModal({ onClose, onAdd, existingDevice }) {
    const [pairingId, setPairingId] = useState('');
    const [name, setName] = useState(existingDevice?.name || '');
    const [error, setError] = useState(null);

    async function handleSubmit() {
        if (!pairingId || !name) return;
        try {
            const verifyData = await apiFetch('/api/devices/verify-register', {
                method: 'POST',
                body: JSON.stringify({ pairingId: Number(pairingId) }),
            });

            const deviceId = verifyData.id;

            await apiFetch(`/api/devices/${deviceId}/pair`, {
                method: 'PATCH',
                body: JSON.stringify({ pairingId: Number(pairingId), paired: true }),
            });

            onAdd({
                id: deviceId,
                name,
                pairingId,
                isOnline: false,
                deviceGroupId: null,
            });
        } catch (err) {
            setError(err.message || 'Could not reach the server.');
        }
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={e => e.stopPropagation()}>
                <h2>{existingDevice ? 'Edit Device' : 'Pair Device'}</h2>

                <label>Device Name</label>
                <input
                    value={name}
                    onChange={e => setName(e.target.value)}
                    placeholder="e.g. Lobby Screen"
                />

                <label>Pairing ID</label>
                <input
                    value={existingDevice ? existingDevice.pairingId : pairingId}
                    onChange={e => setPairingId(e.target.value)}
                    placeholder="e.g. 4821"
                    readOnly={!!existingDevice}
                    style={existingDevice ? { background: '#f0f0f0', cursor: 'not-allowed' } : {}}
                />

                {error && <p style={{ color: 'red', fontSize: '0.85rem' }}>{error}</p>}

                <div className="modal-buttons">
                    <button onClick={onClose}>Cancel</button>
                    <button onClick={handleSubmit}>{existingDevice ? 'Save' : 'Pair'}</button>
                </div>
            </div>
        </div>
    );
}

function EditDeviceModal({ device, groups, onClose, onSave }) {
    const [selectedGroupId, setSelectedGroupId] = useState(device.deviceGroupId ?? null);

    async function handleSubmit() {
        if (selectedGroupId === device.deviceGroupId) {
            onClose();
            return;
        }
        try {
            await api.assignDeviceToGroup(device.id, selectedGroupId);
            onSave({ ...device, deviceGroupId: selectedGroupId });
        } catch (err) {
            alert('Failed to update device group: ' + err.message);
        }
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={e => e.stopPropagation()}>
                <h2>Edit Device — {device.name}</h2>

                <label>Assign to Group</label>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop: '8px' }}>
                    {groups.length === 0 ? (
                        <p style={{ color: '#aaa', fontSize: '0.85rem' }}>No groups available.</p>
                    ) : (
                        groups.map(group => {
                            const isSelected = selectedGroupId === group.id;
                            return (
                                <div
                                    key={group.id}
                                    onClick={() => setSelectedGroupId(group.id)}
                                    style={{
                                        padding: '8px 12px',
                                        borderRadius: '6px',
                                        cursor: 'pointer',
                                        background: isSelected ? '#3b3b3b' : '#f5f5f5',
                                        color: isSelected ? 'white' : 'black',
                                        fontWeight: isSelected ? '600' : 'normal',
                                        border: isSelected ? '2px solid #111' : '2px solid transparent',
                                    }}
                                >
                                    {group.name}
                                    <span style={{ fontSize: '0.8rem', marginLeft: '8px', opacity: 0.6 }}>
                                        {group.deviceIds.length} device{group.deviceIds.length !== 1 ? 's' : ''}
                                    </span>
                                </div>
                            );
                        })
                    )}
                </div>

                <div className="modal-buttons">
                    <button onClick={onClose}>Cancel</button>
                    <button onClick={handleSubmit}>Save</button>
                </div>
            </div>
        </div>
    );
}

export default Devices