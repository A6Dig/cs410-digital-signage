package com.a6dig.digitalsignage.constant;

public final class AppConstant {
    public static final class ExceptionMessage {
        public static final String LAYOUT_NOT_FOUND = "Layout not found.";
        public static final String LAYOUT_INVALID_LAYOUT_ID = "Invalid layout ID provided. Layout with this ID doesn't exist.";
        public static final String LAYOUT_SLOT_NOT_FOUND = "Layout slot not found.";
        public static final String LAYOUT_VALIDATION_FAILED = "Layout validation failed.";

        public static final String LAYOUT_SLOT_INVALID_LAYOUT_SLOT = "Invalid layout slot provided.";

        public static final String INTERNAL_SERVER_ERROR = "Internal server error";
        public static final String UNEXPECTED_ERROR = "Unexpected error occurred.";

        public static String layoutIdDoesNotExist(Long id) {
            return "Layout with id " + id + " doesn't exist";
        }
        public static String layoutSlotIdDoesNotBelongToTheLayout(Long slotId, String layoutName) {
            return "Layout slot with id " + slotId + " doesn't belong to the layout " + layoutName;
        }
        public static String layoutSlotIdDoesNotExist(Long id) {
            return "Layout slot with id " + id + " doesn't exist";
        }
    }


    public static final class SuccessMessage {
        public static final String LAYOUT_CREATED = "Layout created successfully.";
        public static final String LAYOUT_UPDATED = "Layout updated successfully.";
        public static final String LAYOUT_DELETED = "Layout deleted successfully.";
        public static final String LAYOUT_ALL_DELETED = "Layouts deleted successfully.";
    }
}
