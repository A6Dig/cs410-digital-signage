import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Content from './pages/Content'
import Dashboard from './pages/Dashboard'
import Devices from './pages/Devices'
import Layouts from './pages/Layouts'
import Login from './pages/Login'

function App() {
    return (
        <BrowserRouter>
            <Routes>
                {/* Routes to login page first */}
                <Route path="/" element={<Login />} />
                
                <Route path="/dashboard" element={<Dashboard />} />
                
                <Route path="/content" element={<Content />} />
                <Route path="/devices" element={<Devices />} />
                <Route path="/layouts" element={<Layouts />} />
                
            </Routes>
        </BrowserRouter>
    );
}



export default App