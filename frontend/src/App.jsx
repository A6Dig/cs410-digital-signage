import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Navbar from './components/navbar'
import Content from './pages/Content'
import Dashboard from './pages/Dashboard'
import Devices from './pages/Devices'
import Layouts from './pages/Layouts'
import Login from './pages/Login'
import Canvas from './pages/Canvas'

function App() {
    return (
        <BrowserRouter>
            <div style={{ display: 'flex', height: '100svh', overflow: 'hidden' }}>
                <Navbar />
                <div style={{ flex: 1, overflow: 'hidden' }}>
                    <Routes>
                        {/* Routes to login page first */}
                        <Route path="/" element={<Login />} />
                        <Route path="/dashboard" element={<Dashboard />} />
                        <Route path="/content" element={<Content />} />
                        <Route path="/devices" element={<Devices />} />
                        <Route path="/layouts" element={<Layouts />} />
                        <Route path="/canvas" element={<Canvas />} />
                    </Routes>
                </div>
                </div>
        </BrowserRouter>
    );
}

export default App
