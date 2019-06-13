

package com.power.validator.valid;

import cn.hutool.core.util.StrUtil;
import com.power.validator.annotation.Max;


/**
 * @author wwupower
 * @Title: 最大长度校验
 * @history 2019年06月13日
 * @since JDK1.8
 */

public class MaxValitdator implements BaseValidator<Max, String> {

    @Override
    public ValidatorResult valid(String value, Max max) {
        if (StrUtil.isEmpty(value)) {
            return new ValidatorResult().setSuccess(true).setMsg(max.msg());
        }
        if(value.length()>max.value()){
            return new ValidatorResult().setSuccess(false).setMsg(max.msg());
        }
        return new ValidatorResult().setSuccess(true).setMsg(max.msg());
    }

}
