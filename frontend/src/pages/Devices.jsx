function Devices() {
    return (
        <div>
            <h1>Device Management</h1>
            <p>Manage displays and device groups</p>

            <DeviceGroups />
            <DeviceList />
        </div>
    )
}

function DeviceGroups() {
    return(
        <div>
            <h1>Device Groups</h1>
        </div>
    )
}

function DeviceList(){
    return (
        <div>
            <h1>Devices</h1>
        </div>
    )
}


export default Devices