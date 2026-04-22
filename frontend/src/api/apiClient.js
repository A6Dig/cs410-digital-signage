// Base URL for the Spring Boot backend.
// Port is 8080 per backend/src/main/resources/application-example.properties.
// Override via VITE_API_BASE_URL if the backend runs elsewhere.
const BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

// Every backend controller wraps its payload in an APIResponse envelope:
//   { status, message, data, errors }
// This helper unwraps `data` on success and throws on failure.
async function request(path, { method = "GET", body, headers = {} } = {}) {
  const res = await fetch(`${BASE_URL}${path}`, {
    method,
    headers: {
      "Content-Type": "application/json",
      ...headers,
    },
    body: body != null ? JSON.stringify(body) : undefined,
  });

  let payload = null;
  const text = await res.text();
  if (text) {
    try {
      payload = JSON.parse(text);
    } catch {
      payload = null;
    }
  }

  if (!res.ok) {
    const message =
      payload?.message || `Request failed with status ${res.status}`;
    const error = new Error(message);
    error.status = res.status;
    error.payload = payload;
    throw error;
  }

  return payload?.data ?? null;
}

export const apiClient = {
  get: (path) => request(path),
  post: (path, body) => request(path, { method: "POST", body }),
  put: (path, body) => request(path, { method: "PUT", body }),
  delete: (path, body) => request(path, { method: "DELETE", body }),
};

export { BASE_URL };
