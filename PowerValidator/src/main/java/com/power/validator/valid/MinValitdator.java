

package com.power.validator.valid;

import com.power.validator.annotation.Min;
import cn.hutool.core.util.StrUtil;
/**
 * @author wwupower
 * @Title: 最大长度校验
 * @history 2019年06月13日
 * @since JDK1.8
 */
public class MinValitdator implements BaseValidator<Min, String> {

    @Override
    public ValidatorResult valid(String value, Min min) {
        if (StrUtil.isEmpty(value)) {
            return new ValidatorResult().setSuccess(true).setMsg(min.msg());
        }
        if (value.length() < min.value()) {
            return new ValidatorResult().setSuccess(false).setMsg(min.msg());
        }
        return new ValidatorResult().setSuccess(true).setMsg(min.msg());
    }

}
