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

            

            <DeviceGroups groups={groups} />
            
            <DeviceList devices={devices} setDevices={setDevices} />
        </div>
    )
}

// Device Groups Section
function DeviceGroups({ groups }) {
    const [showModal, setShowModal] = useState(false);
    
    return(
        <div className="subpage">
            <div className="section-header">
                <h1>Device Groups</h1>
                <button onClick={() => setShowModal(true)}>New Group</button>
            </div>

            {groups.map(group => (<GroupCard key={group.id} group={group} />))}

            {showModal && (<NewGroupModal onClose={() => setShowModal(false)} />)}

        </div>
    )
}

function GroupCards() {
    return(
        <div className="card">
            <div className="section-header">
                <h3>Campus Center</h3>

                <div className="card-UI">
                    <button>Edit</button>
                    <button>Delete</button>
                </div>
            </div>
            
            {/* Card Information */}
            <p>2 devices | Layout: Campus Center Default</p>
        </div>
    )
}

// Devices Section
function DeviceList({ devices, setDevices }){
    const [showModal, setShowModal] = useState(false);
    
    function handleDelete(id) {
        setDevices(devices.filter(device => device.id !== id));
    }
    
    return (
        <div className="subpage">
            <div className="section-header">
                <h1>Devices</h1>
                <button onClick={() => setShowModal(true)}>Add Device</button>
            </div>

            {devices.map(device => (<DeviceCard key={device.id} device={device} onDelete={handleDelete}/>))}
            {showModal && (
                <NewDeviceModal
                    onClose={() => setShowModal(false)}
                    onAdd={(newDevice) => {
                        setDevices([...devices, newDevice]);
                        setShowModal(false);
                    }}
                />
            )}

        </div>
    )
}

function DeviceCard({ device, onDelete }) {
    return (
        <div className="card">
            <div className="card-header">
                <h3>{device.name}</h3>
                <div className="card-UI">
                    <button>View</button>
                    <button>Edit</button>
                    <button onClick={() => onDelete(device.id)}>Delete</button>
                </div>
            </div>
            <p>Pairing ID: {device.pairingId}</p>
        </div>
    );
}

function NewGroupModal({ onClose }) {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');

    function handleSubmit() {

        console.log({ name, description });
        onClose();
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

                <label>Description</label>
                <input
                    value={description}
                    onChange={e => setDescription(e.target.value)}
                    placeholder="Description"
                />

                <div className="modal-buttons">
                    <button onClick={onClose}>Cancel</button>
                    <button onClick={handleSubmit}>Create</button>
                </div>
            </div>
        </div>
    );
}

function NewDeviceModal({ onClose, onAdd }) {
    const [name, setName] = useState('');
    const [pairingId, setPairingId] = useState('');

    function handleSubmit() {
        if (!name || !pairingId) return;
        onAdd({ id: Date.now(), name, pairingId });
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={e => e.stopPropagation()}>
                <h2>Add Device</h2>
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
                    <button onClick={handleSubmit}>Add</button>
                </div>
            </div>
        </div>
    );
}

export default Devices