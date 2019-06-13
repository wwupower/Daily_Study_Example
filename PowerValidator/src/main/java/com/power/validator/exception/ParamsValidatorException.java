

package com.power.validator.exception;


/**
 * @author wwupower
 * @Title: 参数异常
 * @history 2019年05月14日
 * @since JDK1.8
 */
public class ParamsValidatorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;

    /**
     * 异常处理类无参构造函数
     */
    public ParamsValidatorException() {
        super();
    }

    /**
     * 异常处理类传入异常ex构造函数
     *
     * @param ex
     */
    public ParamsValidatorException(Exception ex) {
        super(ex);
    }

    /**
     * 异常处理类传入message构造函数
     *
     * @param message
     */
    public ParamsValidatorException(String message) {
        super(message);
    }

    /**
     * 异常处理类传入错误message和code的构造函数
     *
     * @param message
     * @param code
     */
    public ParamsValidatorException(String message, String code) {
        super(message);
        this.code = code;
    }

    /**
     * 异常处理类传入错误message和异常的构造函数
     *
     * @param message
     * @param cause
     */
    public ParamsValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamsValidatorException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }
}
