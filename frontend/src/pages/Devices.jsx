import { useEffect, useState } from 'react'
import '../styles/Devices.css'

function Devices() {
    const [devices, setDevices] = useState([]);
    const [groups, setGroups] = useState([]);

    useEffect(() => {
        console.log("get data from backend");
        //
        // setDevices(result from backend)
    }, [])


    return (
        <div className="devices-page">
            <h1>Device Management</h1>
            <h2>Manage displays and device groups</h2>

            <DeviceGroups groups={groups} setGroups={setGroups} devices={devices} />
            
            <DeviceList devices={devices} setDevices={setDevices} />
        </div>
    )
}

// Device Groups Section
function DeviceGroups({ groups, setGroups, devices }) {
    const [showModal, setShowModal] = useState(false);

    function handleDelete(id) {
        setGroups(groups.filter(group => group.id !== id));
    }

    return (
        <div className="subpage">
            <div className="section-header">
                <h1>Device Groups</h1>
                <button onClick={() => setShowModal(true)}>New Group</button>
            </div>

            {groups.map(group => (
                <GroupCard key={group.id} group={group} onDelete={handleDelete} />
            ))}

            {showModal && (
                <NewGroupModal
                    onClose={() => setShowModal(false)}
                    onAdd={(newGroup) => {
                        setGroups([...groups, newGroup]);
                        setShowModal(false);
                    }}
                    devices={devices}
                />
            )}
        </div>
    )
}

function GroupCard({ group, onDelete }) {
    return (
        <div className="card">
            <div className="card-header">
                <h3>{group.name}</h3>
                <div className="card-UI">
                    <button>Edit</button>
                    <button onClick={() => onDelete(group.id)}>Delete</button>
                </div>
            </div>
            <p>{group.deviceIds.length} device{group.deviceIds.length !== 1 ? 's' : ''}</p>
        </div>
    )
}

// Devices Section
function DeviceList({ devices, setDevices }){
    const [showModal, setShowModal] = useState(false);
    const [editingDevice, setEditingDevice] = useState(null);
    
    function handleDelete(id) {
        setDevices(devices.filter(device => device.id !== id));
    }

    function handleEdit(device) {
        setEditingDevice(device);
    }

    function handleEditSave(updatedDevice) {
        setDevices(devices.map(d => d.id === updatedDevice.id ? updatedDevice : d));
        setEditingDevice(null);
    }
    
    return (
        <div className="subpage">
            <div className="section-header">
                <h1>Devices</h1>
                <button onClick={() => setShowModal(true)}>Add Device</button>
            </div>

            {devices.map(device => (<DeviceCard key={device.id} device={device} onDelete={handleDelete} onEdit={handleEdit}/>))}
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
                <NewDeviceModal
                    onClose={() => setEditingDevice(null)}
                    onAdd={handleEditSave}
                    existingDevice={editingDevice}
                />
            )}

        </div>
    )
}

function DeviceCard({ device, onDelete, onEdit }) {
    return (
        <div className="card">
            <div className="card-header">
                <h3>{device.name}</h3>
                <div className="card-UI">
                    {/* <button>View</button> */}
                    <button onClick={() => onEdit(device)}>Edit</button>
                    <button onClick={() => onDelete(device.id)}>Delete</button>
                </div>
            </div>
            <p>Pairing ID: {device.pairingId}</p>
        </div>
    );
}

function NewGroupModal({ onClose, onAdd, devices }) {
    const [name, setName] = useState('');
    const [selectedIds, setSelectedIds] = useState([]);

    function toggleDevice(id) {
        setSelectedIds(prev =>
            prev.includes(id) ? prev.filter(d => d !== id) : [...prev, id]
        );
    }

    function handleSubmit() {
        if (!name) return;
        onAdd({ id: Date.now(), name, deviceIds: selectedIds });
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

                <label>Devices</label>
                {devices.length === 0 ? (
                    <p style={{ color: '#888' }}>No devices added yet.</p>
                ) : (
                    devices.map(device => (
                        <div key={device.id} style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                            <input
                                type="checkbox"
                                checked={selectedIds.includes(device.id)}
                                onChange={() => toggleDevice(device.id)}
                            />
                            <span>{device.name}</span>
                        </div>
                    ))
                )}

                <div className="modal-buttons">
                    <button onClick={onClose}>Cancel</button>
                    <button onClick={handleSubmit}>Create</button>
                </div>
            </div>
        </div>
    );
}

function NewDeviceModal({ onClose, onAdd, existingDevice }) {
    const [name, setName] = useState(existingDevice?.name || '');
    const [pairingId, setPairingId] = useState(existingDevice?.pairingId || '');

    function handleSubmit() {
        if (!name || !pairingId) return;
        onAdd({ id: existingDevice?.id ?? Date.now(), name, pairingId });
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={e => e.stopPropagation()}>
                <h2>{existingDevice ? 'Edit Device' : 'Add Device'}</h2>
                <label>Device Name</label>

                <input
                    value={name}
                    onChange={e => setName(e.target.value)}
                    placeholder="Device name"
                />
                
                <label>Pairing ID</label>
                <input
                    value={pairingId}
                    onChange={e => setPairingId(e.target.value)}
                    placeholder="Pairing ID"
                />
                
                <div className="modal-buttons">
                    <button onClick={onClose}>Cancel</button>
                    <button onClick={handleSubmit}>{existingDevice ? 'Save' : 'Add'}</button>
                </div>
            </div>
        </div>
    );
}

export default Devices