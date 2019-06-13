

package com.power.validator.valid;

import cn.hutool.core.util.StrUtil;
import com.power.validator.annotation.Pattern;


/**
 * @author wwupower
 * @Title: 正则表达式校验
 * @history 2019年06月13日
 * @since JDK1.8
 */

public class PatternValitdator implements BaseValidator<Pattern, String> {

    @Override
    public ValidatorResult valid(String value, Pattern pattern) {
        if (StrUtil.isEmpty(value)) {
            return new ValidatorResult().setSuccess(true).setMsg(pattern.msg());
        }
        boolean matcheResult = value.matches(pattern.regexp());
        return new ValidatorResult().setSuccess(matcheResult).setMsg(pattern.msg());
    }

}
