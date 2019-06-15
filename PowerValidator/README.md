
### 自定义方法和实体校验器

### 1.背景
Spring 本身就集成方法级别的参数校验，实体校验一般使用 `hibernate-validator` 脚本，这两个都是基于`java` 对象校验（` validation` ）-`JSR303`规范来实现的。使用也是比较方便，具体使用可以看下的 [使用demo](https://github.com/wwupower/Daily_Study_Example/tree/master/springboot-validation)
,自己写的这个工具主要是有一个项目，非Spring项目的，一个非Spring的轻量开发的，目前需要给第三个做数据接口，涉及一些参数校验，`hibernate-validator` 仅仅对实体进行校验，因此写下自己的校验，并非遵循 `JSR303`,目的只要能快速校验必须的参数，如非空、时间格式、正则表达式、长度限制等等，也不做分组等支持！

### 主要目标
1.能支持动态扩展
2.支持方法级别校验
3.支持实体校验

### 使用方法

> 1.独立使用

独立使用demo，我自己在非Spring的项目也是使用aop的方式，在service的方法之前检查参数！下面是单独使用的列子：

```java
   /**
    * @author wwupower
    * @Title: ValidatorTest
    * @history 2019年06月13日
    * @since JDK1.8
    */
   public class ValidatorTest {
   
       public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
           //初始化参数校验工厂 并添加自定义校验器
           ParamsValidatorFactory paramsValidatorFactory = new ParamsValidatorFactory("com.power.validator.valid.NotEmptyValidator");
           //设置的快速检查模式
           ParamsValidator paramsValidator = paramsValidatorFactory.createParamsValidator().setFastModel(false);
           //单个实体对象检验
   //        List<ValidatorResult> validatorResults = paramsValidator.validEntity(new DemoVO().setLength("1").setReg("ac_ssasa").setMax("11212121"), DemoVO.class);
   //        validatorResults.forEach(validatorResult -> {
   //            System.out.println(validatorResult.getMsg());
   //        });
           DemoService demoService = new DemoService();
           //使用代理
           Method method = ReflectUtil.getMethod(demoService.getClass(), "findDemos", String.class, DemoVO.class);
           method.invoke(demoService, "aa", new DemoVO().setLength("1").setReg("ac_ssasa").setMax("11212121"));
           //方法级别校验
           List<ValidatorResult> validatorMenthodrResults = paramsValidator.validMethod(method.getParameters(),
                   "",new DemoVO().setLength("122323").setReg("ac_ssasa").setMax("1"));
           System.out.println("---方法级别校验-");
           validatorMenthodrResults.forEach(validatorResult -> {
               System.out.println(validatorResult.getMsg());
           });
       }
   
   }

```
> 2.集成Spring

假如你想在Spring使用的话，在Spring.xml添加注入
```xml
    <beans:bean id="paramsValidatorFactory"
        class="com.power.validator.ParamsValidatorFactory">
        <!--   <beans:property
            name="userDefindVlidatorClasses"
            value="com.power.validator.valid.NotEmptyValidator"/>-->
    </beans:bean>
    <beans:bean id="paramsValidato"
        factory-bean="paramsValidatorFactory"
        factory-method="createParamsValidator">
        <beans:property name="fastModel" value="false"/>
    </beans:bean>
```

然后在通过 `Aop`，在 `service` 的方法执行前检查，然后统一异常捕获抛出！
```java
/**
 * 参数检查切面类
 */
@Aspect
@Component
public class ParamVerifyAspect {

    @Pointcut("execution(* com.power.api.v1..*.controller..*.*(..))")
    private void pointCutControllerMethod() {
    }

    @Pointcut("execution(* com.power.v1..*.service..*.*(..))")
    private void pointServiceCutMethod() {
    }

    /**
     * 调用前扫描
     *
     * @param joinPoint
     * @throws Exception
     */
    @Before("pointServiceCutMethod()")
    public void paramServiceCheck(JoinPoint joinPoint) throws Exception {
        ParamsValidator paramsValidator = (ParamsValidator) Toolkit.getSpringBean(ParamsValidator.class);
        Object[] args = joinPoint.getArgs();
        //获取方法参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        List<ValidatorResult> validatorResults = paramsValidator.validMethod(parameters, args);
        if(CollectionUtils.isNotEmpty(validatorResults)){
            throw new ParamsValidatorException(paramsValidator.getErrorMsg(validatorResults));
        }
    }

    /**
     * 调用前扫描
     *
     * @param joinPoint
     * @throws Exception
     */
    @Before("pointCutControllerMethod()")
    public void paramCheck(JoinPoint joinPoint) throws Exception {
        //ParamsVerifyUtil.getInstance().verifyParams(joinPoint);
        //获取方法参数
       /* ParamsValidator paramsValidator = (ParamsValidator) Toolkit.getSpringBean(ParamsValidator.class);
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        paramsValidator.validMethod(parameters, args);*/
    }

}

```


### 自定义扩展

先创建自己的注解如：
```java
/**
 * @author wwupower
 * @Title: 长度校验
 * @history 2019年06月13日
 * @since JDK1.8
 */

public class LengthValitdator implements BaseValidator<Length, String> {

    @Override
    public ValidatorResult valid(String value, Length length) {
        if (StrUtil.isEmpty(value)) {
            return new ValidatorResult().setSuccess(true).setMsg(length.msg());
        }
        if(value.length()>length.max()){
            return new ValidatorResult().setSuccess(false).setMsg(length.msg());
        }
        if(value.length()<length.min()){
            return new ValidatorResult().setSuccess(false).setMsg(length.msg());
        }
        return new ValidatorResult().setSuccess(true).setMsg(length.msg());
    }
}

```
> 校验器
```java
/**
 * @author wwupower
 * @Title: 长度校验
 * @history 2019年06月13日
 * @since JDK1.8
 */

public class LengthValitdator implements BaseValidator<Length, String> {

    @Override
    public ValidatorResult valid(String value, Length length) {
        if (StrUtil.isEmpty(value)) {
            return new ValidatorResult().setSuccess(true).setMsg(length.msg());
        }
        if(value.length()>length.max()){
            return new ValidatorResult().setSuccess(false).setMsg(length.msg());
        }
        if(value.length()<length.min()){
            return new ValidatorResult().setSuccess(false).setMsg(length.msg());
        }
        return new ValidatorResult().setSuccess(true).setMsg(length.msg());
    }

}

```
> 在工厂创建时候添加自己的校验器
```java
 //初始化参数校验工厂 并添加自定义校验器
   ParamsValidatorFactory paramsValidatorFactory = new ParamsValidatorFactory("com.power.validator.valid.LengthValitdator");
   //设置的快速检查模式
   ParamsValidator paramsValidator = paramsValidatorFactory.createParamsValidator().setFastModel(false);
```

> 使用用注解
```java
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

```
[代码已上传到GitHub](https://github.com/wwupower/Daily_Study_Example/tree/master/PowerValidator)





	
