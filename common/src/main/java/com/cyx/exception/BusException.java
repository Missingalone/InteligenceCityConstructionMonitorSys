package com.cyx.exception;

import com.cyx.Enums.ResultCodeEnum;

public class BusException extends RuntimeException {

    private final int code;

    public BusException(ResultCodeEnum resultCode) {
        this(resultCode.getCode(), resultCode.getMessage());
    }

    public BusException(String message) {
        this(ResultCodeEnum.BAD_REQUEST.getCode(), message);
    }

    public BusException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
