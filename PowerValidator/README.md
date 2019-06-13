### 自定义方法和实体校验器

### 背景

Spring 本身就集成方法级别的参数校验，实体校验一般使用 `hibernate-validator` 脚本，这两个都是基于`java` 对象校验（` validation` ）-`JSR303`规范
来实现的。使用也是比较方便，具体使用可以看下的 [使用demo](https://github.com/wwupower/Daily_Study_Example/tree/master/springboot-validation)
,自己写的这个工具主要是自己又一个项目，非Spring项目的，一个非Spring的轻量开发的，目前需要和第三个做数据接口，涉及一些参数校验，`hibernate-validator` 仅仅
对实体进行校验，因此索性自己写下自己的校验，并非遵循 `JSR303`,目的只要能快速校验必须的参数，如非空、时间格式、正则表达式、长度限制等等。

### 主要目标

1.能支持动态扩展
2.支持方法级别校验
3.支持实体校验








	
