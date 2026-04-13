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

function DeviceGroups() {
    return(
        <div className="subpage">
            <h1>Device Groups</h1>
            
            <GroupCards />
        </div>
    )
}

function GroupCards() {
    return(
        <div className="groupcard">
            <h3>Campus Center</h3>
            <p>2 devices | Layout: Campus Center Default</p>
        </div>
    )
}

function DeviceList(){
    return (
        <div className="subpage">
            <div className="thing">
                <h1>Devices</h1>
                <button>Add Device</button>
            </div>
            

            <DeviceCards />
        </div>
    )
}

function DeviceCards() {
    return(
        <div className="devicecard">
            <h3>Devices</h3>

            
        </div>
    )
}

export default Devices