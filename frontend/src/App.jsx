import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Canvas from "./pages/Canvas";

// FUTURE: Import an AuthProvider here to wrap routes with auth context
// import { AuthProvider } from "./context/AuthContext";

function App() {
  return (
    // FUTURE: Wrap BrowserRouter with <AuthProvider> for global auth state
    <BrowserRouter>
      <Routes>
        {/* Public route — login page */}
        <Route path="/" element={<Login />} />

        {/* Protected route — canvas page */}
        {/* FUTURE: Wrap with a <ProtectedRoute> component that checks auth state */}
        <Route path="/canvas" element={<Canvas />} />
      </Routes>
    </BrowserRouter>
  );
}

// export default App;
// import { BrowserRouter, Routes, Route } from 'react-router-dom'
// import Content from './pages/Content'
// import Dashboard from './pages/Dashboard'
// import Devices from './pages/Devices'
// import Layouts from './pages/Layouts'
// import Login from './pages/Login'

// function App() {
//     return (
//         <BrowserRouter>
//             <Routes>
//                 {/* Routes to login page first */}
//                 <Route path="/" element={<Login />} />
                
//                 <Route path="/dashboard" element={<Dashboard />} />
                
//                 <Route path="/content" element={<Content />} />
//                 <Route path="/devices" element={<Devices />} />
//                 <Route path="/layouts" element={<Layouts />} />
                
//             </Routes>
//         </BrowserRouter>
//     );
// }



// export default App
