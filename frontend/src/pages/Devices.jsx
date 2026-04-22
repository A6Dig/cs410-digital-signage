import { useEffect, useState } from 'react'
import '../styles/Devices.css'

function Devices() {
    const [devices, setDevices] = useState("");

    useEffect(() => {
        console.log("get data from backend");
        //
        // setDevices(result from backend)
    }, [])


    return (
        <div className="devices-page">
            <h1>Device Management</h1>
            <h2>Manage displays and device groups</h2>

            <DeviceGroups />
            
            <DeviceList />
        </div>
    )
}

// Device Groups Section
function DeviceGroups() {
    return(
        <div className="subpage">
            <div className="section-header">
                <h1>Device Groups</h1>
                <button>New Group</button>
            </div>
            
            <GroupCards />
        </div>
    )
}

function GroupCards() {
    return(
        <div className="groupcard">
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
function DeviceList(){
    return (
        <div className="subpage">
            <div className="section-header">
                <h1>Devices</h1>
                <button>Add Device</button>
            </div>
            

            <DeviceCards />
        </div>
    )
}

function DeviceCards() {
    return(
        <div className="card">
            <div className="card-header">
                <h3>CC Main Lobby</h3>

                <div className="card-UI">
                    <p>Online</p>

                    <div>
                        <button>View</button>
                        <button>Edit</button>
                        <button>Delete</button>
                    </div>
                </div>
            </div>

            {/* Card information */}
            <p>Campus Center - Main Lobby</p>
            <p>Group: Campus Center</p>
            <p>Assigned Layout: Campus Center Default</p>
            
        </div>
        
    )
}

export default Devices