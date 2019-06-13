

package com.power.validator.valid;

import cn.hutool.core.util.StrUtil;
import com.power.validator.annotation.DateStr;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author wwupower
 * @Title: 不允许为空校验器
 * @history 2019年05月16日
 * @since JDK1.8
 */
public class DateStrValidator implements BaseValidator<DateStr, String> {

    @Override
    public ValidatorResult valid(String value, DateStr dateStr) {
        if(StrUtil.isEmpty(value)){
            return new ValidatorResult().setSuccess(true);
        }
        String[] dateFormats = dateStr.format();
        if (dateFormats == null && dateFormats.length == 0) {
            dateFormats = new String[]{"yyyy-MM-dd HH:mm:ss"};
        }
        boolean isDateStr = false;
        String dateStrMes = "";
        for (String dateFormat : dateFormats) {
            if(value.length()!=dateFormat.length()){
                dateStrMes = getExceptionStr(dateFormat,dateStr);
                return new ValidatorResult().setSuccess(isDateStr).setMsg(dateStrMes);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(StrUtil.isEmpty(dateFormat) ? "yyyy-MM-dd HH:mm:ss"
                    : dateFormat);
            try {
                // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
                simpleDateFormat.setLenient(false);
                simpleDateFormat.parse(value);
                isDateStr = true;
                break;
            } catch (ParseException e) {
                dateStrMes = getExceptionStr(dateFormat,dateStr);
            }
        }
        if (isDateStr) {
            return new ValidatorResult().setSuccess(isDateStr);
        }
        return new ValidatorResult().setSuccess(isDateStr).setMsg(dateStrMes);
    }

    public String getExceptionStr(String dateFormat, DateStr dateStr){
        String dateStrMes = dateStr.msg();
        if("请传入符合时间格式的字符串,如：yyyy-MM-dd HH:mm:ss".equals(dateStrMes)){
            dateStrMes = dateStr.msg().replace("yyyy-MM-dd HH:mm:ss",dateFormat);
        }
        return  dateStrMes;
    }

}
