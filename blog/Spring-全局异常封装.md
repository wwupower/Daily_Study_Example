---
title: Spring 全局异常封装
date: 2018-07-23 21:39:39
tags: 异常Spring
categories: spring
---

### 说明

Spring 框架里，统一的异常捕获是很重要的，比如我们可以出现异常短信或者邮件通知相关的维护人员；能够及时知道程序出问题；
<!--more-->
实现controller的异常捕获并统一响应给客户端一般由这三种方式去实现；

- 1.try{}catch(Exception){} 把所有的异常都捕获，并根据异常返回给客户端；

- 2.使用@ControllerAdvice方式

- 3.实现HandlerExceptionResolver接口的方式；

### 具体实现方式

> 第一种方式

使用第一种情况的的话，这种方式是比较直接，但是不好的是带来有太多的冗余，不利于阅读；
实现代码如下：
```java
    /**
     * 获取用户详细信息
     * @param param
     * @return
     */
    @RequestMapping(value="/getuserinfo/{id}",method = RequestMethod.POST)
    public ResData getUserInfo(@RequestBody UserInfo user){
        ResData resData = new ResData();
        //
        try{
            
        }catch(Exception){
            //
            resData.setCode(-1);
            resData.setMsg("系统繁忙！");
            Logger.error("...")
        }
        return resData;
    }
```


> 第二种方式:

这是基于API接口统一的异常处理，纯微服务Restful接口的话，推荐使用此方式，

只需加上@ControllerAdvice注解；可以统一封装已知或未知的异常类型，如某业务模块下异常的类型，并设置对于的返回内容；

代码如下：

```java
/**
 * @Title:  <p>
 *          实现全局的 Controller 层的异常处理 这是基于Restful的方式，
 *          </p>
 * @author  wwupower
 * @since   JDK1.8
 * @history 2018年4月20日
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 统一全局异常返回码
     */
    public static final  Integer GLOHAL_ERROR_CODE = -1;

    /**
     * 统一的正常访问的状态吗
     */
    public static final  Integer GLOHAL_SUCCESS_CODE = 0;

    /**
     * 处理未知异常
     * @param ex
     * @return
     */
    @ExceptionHandler()
    @ResponseBody
    public AjaxJson allExceptionHandler(Exception ex){
        return setErrorMsg(ex);
    }

    /**
     * 处理所有业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    AjaxJson handleBusinessException(BaseException ex) {
        return setErrorMsg(ex);
    }
    /**
     * 处理所有业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler(PlatformException.class)
    @ResponseBody
    AjaxJson handleBusinessException(PlatformException ex){
        return setErrorMsg(ex);
    }

    /**
     * controller 异常消息铺货
     * @param throwable
     * @return
     */
    public AjaxJson setErrorMsg(Throwable throwable){
        AjaxJson json = new AjaxJson();
        json.setCode(GLOHAL_ERROR_CODE);
        if(StringUtil.isEmpty(throwable.getMessage())){
            json.setMsg("系统繁忙，稍后再试");
        }
        LoggerUtil.error(this.getClass(),throwable.getMessage(),throwable);
        return json;
    }

}
```

> 第三种

这种方式需要实现HandlerExceptionResolver接口，并返回一个ModelAndView页面，但是我们开发过程中，不仅仅是普通的web页面请求，还有Ajax请求的，Ajax错误请求返回一个页面很是很不科学，因此做了简单的封装，普通项目推荐使用此方式，具体代码实现如下:

```java
/**
 * @Title: <p>
 *          实现全局的 Controller 层的异常处理 这是基于Restful和普通web跳转的统一异常处理；
 *          </p>
 * @author  wwupower
 * @since   JDK1.8
 * @history 2018年4月20日
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    /**
     * 统一异常拦截
     * @param request
     * @param response
     * @param object
     * @param exception
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object object, Exception exception) {
        // 判断是否ajax请求
        if (!isAjaxRequst(request)) {
            //  系统异常 此处应分系统异常和业务异常；
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("success", false);
            map.put("msg", exception.getMessage());
            //对于非ajax请求，我们都统一跳转到error.jsp页面
            LoggerUtil.error(this.getClass(),exception.getMessage(),exception);
            return new ModelAndView("error/error", map);
        } else {
            // 如果是ajax请求，JSON格式返回
            AjaxJson json = new AjaxJson();
            try {
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                //获取错误码
                try {
                    BaseException baseException = (BaseException) exception;
                    if(StringUtils.isNotEmpty(baseException.getCode())) {
                        json.setCode(Integer.parseInt(baseException.getCode()));
                    }
                } catch (ClassCastException e){
                    json.setCode(ApiGlobalException.GLOHAL_ERROR_CODE);
                } catch (NumberFormatException e){
                    json.setCode(ApiGlobalException.GLOHAL_ERROR_CODE);
                }
                if (StringUtil.isNotEmpty(exception.getMessage())) {
                    json.setMsg(exception.getMessage());
                }
                writer.write(JsonHelper.parseObject(json));
                writer.flush();
                writer.close();
                LoggerUtil.error(this.getClass(),exception.getMessage(),exception);
                return null;
            } catch (IOException e) {
                LoggerUtil.error(this.getClass(), "show exception error:{}", e.getMessage());
                //异常中的异常返回到错误页面
                return null;
            }
        }
    }

    /**
     * 判断是否是Ajax请求
     * @param request
     * @return
     */
    public boolean isAjaxRequst(HttpServletRequest request){
        return request.getHeader("accept").indexOf("application/json") > -1
                || (request.getHeader("X-Requested-With") != null && request.getHeader(
                "X-Requested-With").indexOf("XMLHttpRequest") > -1);
    }
}


```

在Spring MVC中，所有用于处理在请求映射和请求处理过程中抛出的异常的类，都要实现HandlerExceptionResolver接口。AbstractHandlerExceptionResolver实现该接口和Orderd接口，是HandlerExceptionResolver类的实现的基类。ResponseStatusExceptionResolver等具体的异常处理类均在AbstractHandlerExceptionResolver之上，实现了具体的异常处理方式。一个基于Spring MVC的Web应用程序中，可以存在多个实现了HandlerExceptionResolver的异常处理类，他们的执行顺序，由其order属性决定, order值越小，越是优先执行, 在执行到第一个返回不是null的ModelAndView的Resolver时


参考资料

http://www.cnblogs.com/xinzhao/p/4902295.html




 
 


