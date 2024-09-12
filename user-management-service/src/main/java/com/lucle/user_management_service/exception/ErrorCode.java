package com.lucle.user_management_service.exception;

public enum ErrorCode {
    // key(value) value = cac thuoc tinh cua lop enum
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception"),
    INVALID_KEY(1001, "Invalid message key"),
    USER_EXISTED(1002, "User existed"),
    USERNAME_INVALID(1003, "Username must be at least 4 characters"),
    PASSWORD_INVALID(1004, "Password must be at least 6 characters")
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
