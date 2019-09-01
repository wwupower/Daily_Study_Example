package com.power.validator;
import com.power.validator.annotation.Length;
import com.power.validator.annotation.Max;
import com.power.validator.annotation.Min;
import com.power.validator.annotation.Pattern;

/**
 * @author wwupower
 * @Title: DemoVO
 * @history 2019年06月13日
 * @since JDK1.8
 */
public class DemoVO {
    @Length(min = 3L,max = 10L)
    String length;

    @Pattern(regexp = "^[a-zA-Z_]\\w{4,19}$", msg = "用户名必须以字母下划线开头，可由字母数字下划线组成")
    String reg;

    @Min(value = 2)
    String min;

    @Max(value = 1)
    String max;


    public String getLength() {
        return length;
    }

    public DemoVO setLength(String length) {
        this.length = length;
        return this;
    }

    public String getReg() {
        return reg;
    }

    public DemoVO setReg(String reg) {
        this.reg = reg;
        return this;
    }

    public String getMin() {
        return min;
    }

    public DemoVO setMin(String min) {
        this.min = min;
        return this;
    }

    public String getMax() {
        return max;
    }

    public DemoVO setMax(String max) {
        this.max = max;
        return this;
    }

}
