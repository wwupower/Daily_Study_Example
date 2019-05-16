/******************************************************************************
 * Copyright (C) 2017 ShenZhen Powerdata Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳博安达开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author wwupower
 * @Title: DemoService
 * @history 2019年05月16日
 * @since JDK1.8
 */
@Service
@Validated
public class DemoService {

    public void demo(@NotNull String aa, @NotNull(message = "bb不能为空") String bb){
        System.out.println(aa+bb);
    }
}
