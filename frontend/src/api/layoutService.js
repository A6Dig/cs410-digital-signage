import { apiClient } from "./apiClient";

// Backend LayoutController — /api/layouts
// Request shape (LayoutRequestDto extends LayoutDtoBase):
//   { name: string, cols: int, rows: int, slots: LayoutSlotRequestDto[] }
// Slot shape (LayoutSlotRequestDto / ...UpdateDto):
//   { id?: long, moduleId: long, colPos, rowPos, colSpan, rowSpan, zIndex }
// Response shape (LayoutResponseDto):
//   { id, name, cols, rows, slots: LayoutSlotResponseDto[], createdAt, updatedAt }
export const layoutService = {
  list: () => apiClient.get("/api/layouts"),
  getById: (id) => apiClient.get(`/api/layouts/${id}`),
  create: (layout) => apiClient.post("/api/layouts", layout),
  update: (id, layout) => apiClient.put(`/api/layouts/${id}`, layout),
  remove: (id) => apiClient.delete(`/api/layouts/${id}`),
  removeAll: () => apiClient.delete("/api/layouts"),
};
