import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/login.css";

// Mock user for dummy authentication
const mockUser = {
  username: "admin",
  password: "1234",
};

/**
 * Authenticate against mock data.
 *
 * FUTURE: Replace the body of this function with a real API call:
 *
 *   const response = await fetch("http://localhost:5000/api/login", {
 *     method: "POST",
 *     headers: { "Content-Type": "application/json" },
 *     body: JSON.stringify({ username, password }),
 *   });
 *
 *   if (!response.ok) {
 *     const err = await response.json();
 *     return { success: false, message: err.message };
 *   }
 *
 *   const data = await response.json();
 *
 *   // Store JWT token for subsequent requests
 *   // localStorage.setItem("token", data.token);
 *
 *   return { success: true };
 */
async function loginUser(username, password) {
  // Simulate network delay
  await new Promise((resolve) => setTimeout(resolve, 600));

  if (username === mockUser.username && password === mockUser.password) {
    return { success: true };
  }
  return { success: false, message: "Invalid username or password" };
}

function Login() {
  const navigate = useNavigate();

  // Form state
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  // Both fields must be non-empty to enable the button
  const isDisabled = !username.trim() || !password.trim() || loading;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    // Input validation
    if (!username.trim() || !password.trim()) {
      setError("Please fill in all fields");
      return;
    }

    setLoading(true);

    try {
      const result = await loginUser(username.trim(), password.trim());

      if (result.success) {
        // FUTURE: Store auth token / update auth context before navigating
        navigate("/dashboard");
      } else {
        setError(result.message);
      }
    } catch {
      setError("Something went wrong. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-wrapper">
      <div className="login-tagline">
        &gt; Digital Signage<span className="login-tagline-cursor">_</span>
      </div>
      <div className="login-card">
        <h1 className="login-title">Welcome</h1>

        <form onSubmit={handleSubmit} noValidate>
          <label htmlFor="username">Username</label>
          <input
            id="username"
            type="text"
            placeholder="Enter your username"
            autoComplete="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />

          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            placeholder="Enter your password"
            autoComplete="current-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          {/* Error message */}
          {error && (
            <p className="login-error" role="alert">
              {error}
            </p>
          )}

          <button type="submit" disabled={isDisabled}>
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
