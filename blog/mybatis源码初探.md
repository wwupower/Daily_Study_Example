---
title: mybatis源码初探
date: 2018-08-24 00:18:41
tags: mybatis
categories: 源码阅读
---


 ### 一、Mybatis 简单项目搭建
 
 pom.xml maven 依赖
 <!--more-->
 ```xml
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.4.4</version>
</dependency>
 ```
### 二、配置mybatis配置文件

 ```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <typeAliases>
        <typeAlias type="com.power.mybatis.pojo.User" alias="user"></typeAlias>
    </typeAliases>
    <plugins>
        <!--监控 sql 埋点 分页-->
        <plugin interceptor="com.power.mybatis.plugin.SqlPrintInterceptor"></plugin>
    </plugins>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/power-demo"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>

    <mappers>
      <!--  <mapper resource="mybatis/UserMapper2.xml"/>-->
      <!--  <mapper class="com.power.mybatis.mapper.UserMapper"></mapper>-->
        <package name="com.power.mybatis.mapper"></package>
       <!-- <mapper class="com.power.mapper.UserMapper"></mapper>-->
      <!--  <package name="com.power.mapper"></package>-->
    </mappers>
</configuration>
 ```

 ### 三、编写测试
 
 ```java

@Slf4j
public class MybatisTest {


    // <mapper resource="UserMapper.xml"/>
    @Test
    public void test() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = sqlSession.selectOne("com.power.mapper.UserMapper.selectUser", 1);
        log.info("user:{}", user);

    }

    /**
     * <mapper class="com.power.mybatis.mapper.UserMapper"></mapper>
     * <package name="com.power.mybatis.mapper"></package>
     * 注解的方式
     *
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectUser(1);
        log.info("user:{}", user);

    }

}
 ```
### 四、源码阅读

环境搭建好了之后，就开始Debug源码走，下面是基于注解的方式debug记录
 
 > 基于注解方式源码阅读 mapper 配置  `<mapper class="com.power.mybatis.mapper.UserMapper"></mapper>`


 - 1.读取mybatis-config.xml 并解析 返回org.apache.ibatis.session.SqlSessionFactory
```
  > org.apache.ibatis.session.SqlSessionFactoryBuilder
    > org.apache.ibatis.builder.xml.XMLConfigBuilder().parse() 读取/mybatis-config.xml并解析 返回Configuration
     > org.apache.ibatis.session.Configuration
       > org.apache.ibatis.session.SqlSessionFactoryBuilder
         > org.apache.ibatis.session.SqlSessionFactoryBuilder.build(org.apache.ibatis.session.Configuration)
            > org.apache.ibatis.session.defaults.DefaultSqlSessionFactory 调用默认DefaultSqlSessionFactory 构造器
```
- 2 获取到 `org.apache.ibatis.session.SqlSessionFactory` 之后执行 `openSession`
```
   > org.apache.ibatis.session.defaults.DefaultSqlSessionFactory.openSession()
        > org.apache.ibatis.session.defaults.DefaultSqlSessionFactory.openSessionFromDataSource()
        > org.apache.ibatis.transaction.TransactionFactory.newTransaction(environment.getDataSource(), level, autoCommit) 返回Transaction 事务
            > org.apache.ibatis.session.Configuration.newExecutor(org.apache.ibatis.transaction.Transaction, org.apache.ibatis.session.ExecutorType)
                > org.apache.ibatis.executor.SimpleExecutor
                    > org.apache.ibatis.executor.CachingExecutor
                        >org.apache.ibatis.plugin.InterceptorChain.pluginAll
```
 - 3 获取 `UserMaper` 并执行 `selectUser()` <br>
```
    > org.apache.ibatis.session.SqlSession.getMapper()
        > com.jiagouedu.mybatis.mapper.UserMapper.selectUser
            > org.apache.ibatis.binding.MapperProxy.invoke 实现InvocationHandler 使用jdk代理
                   >org.apache.ibatis.binding.MapperMethod.execute(org.apache.ibatis.session.SqlSession)
                        >org.apache.ibatis.session.defaults.DefaultSqlSession.selectOne(java.lang.String, java.lang.Object)
                              >org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(java.lang.String, java.lang.Object)
                                 > org.apache.ibatis.session.Configuration.getMappedStatement(java.lang.String)
                                    > org.apache.ibatis.executor.CachingExecutor.query(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler)
                                        > org.apache.ibatis.mapping.MappedStatement.getBoundSql(org.apache.ibatis.mapping.MappedStatement)
			                                > org.apache.ibatis.executor.CachingExecutor.createCacheKey()
			                                        缓存KEY 结构  id +sql+limit+offsetxxx：396405528:1543508020:com.power.mybatis.mapper.UserMapper.selectUser:0:2147483647:select id,username,age,phone,`desc` from user where id=?:1:development
                                                	> org.apache.ibatis.executor.BaseExecutor.query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql)
                                                	    >org.apache.ibatis.executor.BaseExecutor.queryFromDatabase()
                                                          > org.apache.ibatis.executor.BaseExecutor.doQuery
                                                            >org.apache.ibatis.executor.resultset.ResultSetHandler.handleResultSets
                                                                 >org.apache.ibatis.executor.resultset.DefaultResultSetHandler
```

### 五、执行流程图

<img src="http://blog.wwudev.cn/img/mybatis/mybatis-flow.png" width="100%">


### 六、总结

关键作用
名称 | 作用
---|---
Configuration                  | 管理mysql-config.xml 全局配置关系类 
SqlSessionFactory                |            Session 管理工厂接口
Session                       |     是一个面向用户（程序员）的接口。SqlSession                        中提 供了很多操作数据库的方法  
Executor  | 执行器是一个接口（基本执行器、缓存执行器） 作用：SqlSession  内部通过执行器操作数据库 
MappedStatement  | 对操作数据库存储封装，包括  sql 语句、输入输出参数  
StatementHandler     |         具体操作数据库相关的handler 接口  
ResultSetHandler     |       具体操作数据库返回结果的handler 接口  

其中根据断点发现mybatis缓存特点：

每当我们使用 MyBatis 开启一次和数据库的会话，MyBatis 会创建出一个SqlSession 对象表 示一次数据库会话。  在对数据库的一次会话中，我们有可能会反复地执行完全相同的查询 语句，如果不采取一些措施的话，每一次查询都会查询一次数据库,而我们在极短的时间内做了完全相同的查询，那么它们的结果极有可能完全相同，由于查询一次数据库的代价很大， 这有可能造成很大的资源浪费。          

为了解决这一问题，减少资源的浪费，MyBatis 会在表示会话的 SqlSession 对象中建立一个 简单的缓存，将每次查询到的结果结果缓存起来，当下次查询的时候，如果判断先前有个完 全一样的查询，会直接从缓存中直接将结果取出，返回给用户，不需要再进行一次数据库查 
询了。  
cache key: id +sql+limit+offsetxxx  

mybatis 缓存参考

https://blog.csdn.net/u012373815/article/details/47069223
http://www.cnblogs.com/xdp-gacl/p/4270403.html


