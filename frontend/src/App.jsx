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

export default App;
