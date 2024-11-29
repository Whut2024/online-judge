package com.whut.onlinejudge.backgrounddoor.exception;

import com.whut.onlinejudge.backgrounddoor.common.ErrorCode;
import lombok.Getter;

/**
 * 自定义异常类
 *
 whut-online-judge
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

}

