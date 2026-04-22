import { apiClient } from "./apiClient";

// Backend UserController exposes:
//   POST   /api/users/register
//   GET    /api/users
//   GET    /api/users/{id}
//   PUT    /api/users/{id}
//   DELETE /api/users/{id}
//
// PENDING BACKEND SUPPORT: there is no /api/users/login endpoint yet.
// Until one exists we validate credentials by looking up the username in
// the list returned by GET /api/users. The User entity's password field is
// marked @JsonProperty(access = WRITE_ONLY), so it is never returned over
// the wire — meaning the password itself cannot be verified client-side.
// Once the backend adds a proper /login (or /authenticate) endpoint, swap
// this implementation for a POST that returns a token.
export async function login(username, password) {
  if (!username || !password) {
    throw new Error("Username and password are required");
  }

  const users = (await apiClient.get("/api/users")) ?? [];
  const match = users.find((u) => u.username === username);

  if (!match) {
    throw new Error("Invalid username or password");
  }

  return match;
}

// Request body matches the User entity fields the backend expects
// (see backend/src/main/java/com/a6dig/digitalsignage/entity/User.java):
//   { username, email, password }
export async function register({ username, email, password }) {
  return apiClient.post("/api/users/register", { username, email, password });
}

export async function listUsers() {
  return apiClient.get("/api/users");
}
