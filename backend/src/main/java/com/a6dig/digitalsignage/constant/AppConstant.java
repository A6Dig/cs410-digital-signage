package com.a6dig.digitalsignage.constant;

public final class AppConstant {
    public static final class SystemConstant {
        public static final String DOMAIN_TYPE_AD_CONTENT = "Ad Content";
        public static final String DOMAIN_TYPE_MODULE = "Module";
    }
    public static final class ExceptionMessage {
        public static final String LAYOUT_NOT_FOUND = "Layout not found.";
        public static final String LAYOUT_INVALID_LAYOUT_ID = "Invalid layout ID provided. Layout with this ID doesn't exist.";
        public static final String LAYOUT_SLOT_NOT_FOUND = "Layout slot not found.";
        public static final String LAYOUT_VALIDATION_FAILED = "Layout validation failed.";
        public static final String LAYOUT_SLOT_VALIDATION_FAILED = "Layout slot validation failed.";

        public static final String LAYOUT_SLOT_INVALID_LAYOUT_SLOT = "Invalid layout slot provided.";

        public static final String INTERNAL_SERVER_ERROR = "Internal server error";
        public static final String UNEXPECTED_ERROR = "Unexpected error occurred.";

        public static final String INVALID_JSON = "Invalid JSON format";
        public static final String INVALID_JSON_UNABLE_TO_CONVERT_TO_STRING = "Unable to convert to string.";
        public static final String INVALID_JSON_UNABLE_TO_CONVERT_TO_JSON = "Unable to convert to JSON.";

        public static final class Domain {
            public static final String NOT_FOUND = "Type doesn't exist";
            public static final String TYPE_NOT_PROVIDED = "Type not provided";
            public static final String TYPE_CANNOT_BE_EMPTY = "Type cannot be empty or null.";

            public static String domainCodeDoesNotExist(String code) {
                return String.format("%s doesn't exist.", code);
            }



            public static String domainCodeDoesNotBelongToTheType(String code, String type) {
                return String.format("%s doesn't belong to %s.", code, type);
            }

        }


        public static final class Module {
            public static final String NOT_FOUND = "Module not found";
            public static final String ID_NOT_PROVIDED = "Null or empty id provided";
            public static final String INVALID_DATA_PROVIDED = "Invalid data";
            public static String idDoesNotExist(Long id) {
                return "Module with id " + id + " doesn't exist";
            }
        }
        public static final class AdCollection {
            public static final String NOT_FOUND = "Ad collection not found";
            public static final String ID_NOT_PROVIDED = "Null or empty id provided";
            public static final String INVALID_DATA_PROVIDED = "Invalid data";
            public static String idDoesNotExist(Long id) {
                return "Ad collection with id " + id + " doesn't exist";
            }
        }

        public static final class AdContent {
            public static final String NOT_FOUND = "Ad content not found";
            public static final String ID_NOT_PROVIDED = "Null or empty id provided";
            public static final String INVALID_DATA_PROVIDED = "Invalid data";
            public static String idDoesNotExist(Long id) {
                return "Ad content with id " + id + " doesn't exist";
            }
        }

        public static final class Device {
            public static final String NOT_FOUND = "Device not found";
            public static final String ID_NOT_PROVIDED = "Null or empty id provided";
            public static final String INVALID_DATA_PROVIDED = "Invalid data";
            public static String idDoesNotExist(Long id) {
                return "Device with id " + id + " doesn't exist";
            }
        }

        public static final class DeviceGroup {
            public static final String NOT_FOUND = "Device group not found";
        }

        public static final class User {
            public static final String NOT_FOUND = "User not found";
            public static final String INVALID_DATA_PROVIDED = "Invalid user data";
            public static String idDoesNotExist(Long id) {
                return "User with id " + id + " doesn't exist";
            }
        }



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
        public static final String LAYOUT_SLOT_DELETED_SELECTED = "Selected layout slots have been deleted.";


        public static final class Module {
            public static final String CREATED = "Module created successfully";
            public static final String UPDATED = "Module updated successfully";
            public static final String DELETED = "Module deleted successfully";
            public static final String DELETED_ALL = "Modules deleted successfully";
        }

        public static final class AdCollection {
            public static final String CREATED = "Ad collection created successfully";
            public static final String UPDATED = "Ad collection updated successfully";
            public static final String DELETED = "Ad collection deleted successfully";
            public static final String DELETED_ALL = "Ad collections deleted successfully";

        }

        public static final class AdContent {
            public static final String CREATED = "Ad content created successfully";
            public static final String UPDATED = "Ad content updated successfully";
            public static final String DELETED = "Ad content deleted successfully";
            public static final String DELETED_ALL = "Ad contents deleted successfully";

        }

        public static final class User {
            public static final String CREATED = "User created successfully";
            public static final String UPDATED = "User updated successfully";
            public static final String DELETED = "User deleted successfully";
            public static final String DELETED_ALL = "Users deleted successfully";
        }

        public static final class Device {
            public static final String CREATED = "Device created successfully";
            public static final String UPDATED = "Device updated successfully";
            public static final String DELETED = "Device deleted successfully";
            public static final String DELETED_ALL = "Devices deleted successfully";
        }

        public static final class DeviceGroup {
            public static final String CREATED = "Device group created successfully";
            public static final String UPDATED = "Device group updated successfully";
            public static final String DELETED = "Device group deleted successfully";
            public static final String DELETED_ALL = "Device groups deleted successfully";
        }


        public static String allLayoutSlotsOfTheLayoutDeleted(Long id) {
            return "All layout slots of layout with id " + id + " has been deleted.";
        }
    }
}
