

package com.power.validator.valid;

/**
 * @author wwupower
 * @Title: 校验结果
 * @history 2019年05月17日
 * @since JDK1.8
 */
public class ValidatorResult {

    private boolean isSuccess;
    private String msg;

    public boolean isSuccess() {
        return isSuccess;
    }

    public ValidatorResult setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ValidatorResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
