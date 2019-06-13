

package com.example.demo.vo;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author wwupower
 * @Title: ValitadeTest
 * @history 2019年05月16日
 * @since JDK1.8
 */
public class ValitadeTest {

    public static void main(String[] args) {
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Demo2 demo2 = new Demo2();
        demo2.setAge("111");
        demo2.setHigh(150);
        demo2.setLength("ABCDE");
        demo2.setList(new ArrayList<String>(){{add("111");add("222");add("333");}});
        Set<ConstraintViolation<Demo2>> violationSet = validator.validate(demo2);
        for (ConstraintViolation<Demo2> model : violationSet) {
            System.out.println("----------"+model.getMessage());
        }
    }
}
