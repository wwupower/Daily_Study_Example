

package com.power.validator.valid;

import cn.hutool.core.util.StrUtil;
import com.power.validator.annotation.Length;


/**
 * @author wwupower
 * @Title: 长度校验
 * @history 2019年06月13日
 * @since JDK1.8
 */

public class LengthValitdator implements BaseValidator<Length, String> {

    @Override
    public ValidatorResult valid(String value, Length length) {
        if (StrUtil.isEmpty(value)) {
            return new ValidatorResult().setSuccess(true).setMsg(length.msg());
        }
        if(value.length()>length.max()){
            return new ValidatorResult().setSuccess(false).setMsg(length.msg());
        }
        if(value.length()<length.min()){
            return new ValidatorResult().setSuccess(false).setMsg(length.msg());
        }
        return new ValidatorResult().setSuccess(true).setMsg(length.msg());
    }

}
