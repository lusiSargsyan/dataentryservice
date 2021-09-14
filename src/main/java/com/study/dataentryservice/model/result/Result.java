package com.study.dataentryservice.model.result;

import com.study.dataentryservice.exception.DatabaseException;
import com.study.dataentryservice.exception.ValidationException;
import com.study.dataentryservice.helper.LogHelper;
import org.json.JSONObject;

import java.util.Objects;

public class Result {

    private final ResultCode resultCode;
    private String message;

    private Result(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    private Result(ResultCode resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public static String successResultString() {
        return new Result(ResultCode.SUCCESS).toString();
    }

    public static Result successResult() {
        return new Result(ResultCode.SUCCESS);
    }

    public static Result successResult(String message) {
        return new Result(ResultCode.SUCCESS, message);
    }
    public static String errorResultString(Throwable e) {
        return errorResult(e).toString();
    }

    public static Result errorResult(Throwable e) {
        LogHelper.logError(e);
        if (e instanceof DatabaseException) {
            return new Result(ResultCode.INVALID_STRUCTURE);
        } else if (e instanceof ValidationException) {
            return new Result(ResultCode.INVALID_INPUT, e.getMessage());
        } else {
            return new Result(ResultCode.UNEXPECTED_ERROR);
        }
    }

    @Override
    public String toString() {
        JSONObject result = new JSONObject();
        result.put("result", resultCode);
        if (Objects.nonNull(message)) {
            result.put("message", message);
        }
        return result.toString();
    }
}
