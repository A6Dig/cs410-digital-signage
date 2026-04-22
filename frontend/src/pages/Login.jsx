import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login as loginRequest } from "../api/authService";
import "../styles/login.css";

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
      const user = await loginRequest(username.trim(), password.trim());
      // PENDING BACKEND SUPPORT: no /login endpoint exists yet, so the
      // authService resolves the user via GET /api/users without verifying
      // the password. Persist the returned user so other pages can read it.
      localStorage.setItem("currentUser", JSON.stringify(user));
      navigate("/canvas");
    } catch (err) {
      setError(err.message || "Something went wrong. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-wrapper">
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

