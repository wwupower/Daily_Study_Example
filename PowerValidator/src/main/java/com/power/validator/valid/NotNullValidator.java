

package com.power.validator.valid;

import cn.hutool.core.util.StrUtil;
import com.power.validator.annotation.NotNull;

/**
 * @author wwupower
 * @Title: 不允许为空校验器
 * @history 2019年05月16日
 * @since JDK1.8
 */
public class NotNullValidator implements BaseValidator<NotNull, Object> {

    @Override
    public ValidatorResult valid(Object value, NotNull notNull) {
        if (value == null) {
            return new ValidatorResult().setSuccess(false).setMsg(notNull.msg());
        }
        if (value instanceof String) {
            if (StrUtil.isEmpty(value+"")) {
                return new ValidatorResult().setSuccess(false).setMsg(notNull.msg());
            }
        }
        return new ValidatorResult().setSuccess(true);
    }
}
