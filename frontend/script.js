// ===== Mock User Database =====
const users = [
  { username: "admin", password: "1234" },
  { username: "user", password: "password" }
];

// ===== DOM Elements =====
const loginForm = document.getElementById("login-form");
const usernameInput = document.getElementById("username");
const passwordInput = document.getElementById("password");
const loginBtn = document.getElementById("login-btn");
const errorMessage = document.getElementById("error-message");

// ===== Login Function =====
// Structured for easy replacement with a real API call
async function loginUser(username, password) {
  // CURRENT: Mock authentication against local JSON data
  // Simulate network delay for realistic UX
  await new Promise((resolve) => setTimeout(resolve, 800));

  const matchedUser = users.find(
    (u) => u.username === username && u.password === password
  );

  if (matchedUser) {
    return { success: true };
  }
  return { success: false, message: "Invalid username or password" };

  // FUTURE: Replace the above with a real API call:
  //
  // const response = await fetch("http://localhost:5000/api/login", {
  //   method: "POST",
  //   headers: { "Content-Type": "application/json" },
  //   body: JSON.stringify({ username, password })
  // });
  // return await response.json();
}

// ===== Form Submit Handler =====
loginForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  // Clear previous error message
  errorMessage.textContent = "";

  const username = usernameInput.value.trim();
  const password = passwordInput.value.trim();

  // Input validation — check for empty fields
  if (!username || !password) {
    errorMessage.textContent = "Please fill in all fields";
    return;
  }

  // Disable button and show loading state
  loginBtn.disabled = true;
  loginBtn.textContent = "Logging in...";

  try {
    const result = await loginUser(username, password);

    if (result.success) {
      // Redirect on successful login
      window.location.href = "/dashboard.html";
    } else {
      errorMessage.textContent = result.message;
    }
  } catch (error) {
    errorMessage.textContent = "Something went wrong. Please try again.";
  } finally {
    // Re-enable button and restore label
    loginBtn.disabled = false;
    loginBtn.textContent = "Login";
  }
});
