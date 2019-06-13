

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
