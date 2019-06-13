

package com.power.validator.valid;

import cn.hutool.core.util.StrUtil;
import com.power.validator.annotation.NotEmpty;

/**
 * @author wwupower
 * @Title: 不允许为空校验器
 * @history 2019年05月16日
 * @since JDK1.8
 */
public class NotEmptyValidator implements BaseValidator<NotEmpty, String> {


    @Override
    public ValidatorResult valid(String value, NotEmpty notEmpty) {
        if (StrUtil.isEmpty(value)) {
            return new ValidatorResult().setSuccess(false).setMsg(notEmpty.msg());
        }
        return new ValidatorResult().setSuccess(true).setMsg(notEmpty.msg());
    }
}
