---
title: maven集成tomcat7插件使用及部署
date: 2018-08-30 12:59:25
tags: maven
categories: 持续集成
---
在项目开发中，由于每次测试环境需要手动去部署，特别麻烦，在想有没有在本地发布之后，测试环境自动就部署好，好在maven可以集成tomcat7插件，我们可以利用这个插件帮我们自动部署；下面步骤记录的集成过程；

### 一、tomcat 配置
`tomcat` 环境下，我用的是tomcat8，我们先配 `tomcat` 用户角色,找到 `/conf` 目录下的 `tomcat-users.xml` 文件
<!--more-->
```xml
<?xml version="1.0" encoding="UTF-8"?>
 
<tomcat-users xmlns="http://tomcat.apache.org/xml"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://tomcat.apache.org/xml tomcat-users.xsd"
              version="1.0">
<role rolename="manager"/>
<role rolename="manager-gui"/>
<role rolename="admin"/>
<role rolename="admin-gui"/>
<role rolename="manager-script"/>
<user username="tomcat" password="tomcat" roles="admin-gui,admin,manager-gui,manager,manager-script"/>
</tomcat-users>
```

顺便说明下 tomcat 的角色说明
以下是 `Tomcat Manager` 4种角色的大致介绍(下面 URL 中的*为通配符)：
```xml
    manager-gui
    允许访问html接口(即URL路径为/manager/html/*)
    manager-script
    允许访问纯文本接口(即URL路径为/manager/text/*)
    manager-jmx
    允许访问JMX代理接口(即URL路径为/manager/jmxproxy/*)
    manager-status
    允许访问Tomcat只读状态页面(即URL路径为/manager/status/*)
```

tomcat8下还需要注意配置`/webapps/manager/META-INF/context.xml`目录下的文件，否则依然拒绝访问，配置如下
原来的：
```xml
<Context antiResourceLocking="false" privileged="true" >
  <Valve className="org.apache.catalina.valves.RemoteAddrValve"
         allow="127\.\d+\.\d+\.\d+|::1|0:0:0:0:0:0:0:1" />
  <Manager sessionAttributeValueClassNameFilter="java\.lang\.(?:Boolean|Integer|Long|Number|String)|org\.apache\.catalina\.filters\.CsrfPreventionFilter\$LruCache(?:\$1)?|java\.util\.(?:Linked)?HashMap"/>
</Context>

```
改成 去掉IP地址的访问限制
```xml
<Context antiResourceLocking="false" privileged="true" >
  <Manager sessionAttributeValueClassNameFilter="java\.lang\.(?:Boolean|Integer|Long|Number|String)|org\.apache\.catalina\.filters\.CsrfPreventionFilter\$LruCache(?:\$1)?|java\.util\.(?:Linked)?HashMap"/>
</Context>

```

### 二、maven集成插件 



在pom.xml添加插件 `tomcat7` 插件
```xml
    <?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.power</groupId>
  <artifactId>test</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>war</packaging>
  <properties>
    <COMPILE.JDK>1.8</COMPILE.JDK>
    <COMPILE.ENCODING>UTF-8</COMPILE.ENCODING>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <finalName>test</finalName>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.1</version>
        <configuration>
          <source>${COMPILE.JDK}</source>
          <target>${COMPILE.JDK}</target>
          <!-- 指明编译源代码时使用的字符编码，
                maven编译的时候默认使用的GBK编码，
              通过encoding属性设置字符编码，
              告诉maven这个项目使用UTF-8来编译 -->
          <encoding>utf8</encoding>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-war-plugin</artifactId>
        <configuration>
          <failOnMissingWebXml>false</failOnMissingWebXml>
        </configuration>
      </plugin>

      <plugin>
        <groupId>org.apache.tomcat.maven</groupId>
        <artifactId>tomcat7-maven-plugin</artifactId>
        <configuration>
          <!--deploy begin-->
          <url>http://127.0.0.1:8080/manager/text</url>
          <server>tomcat</server>
          <username>tomcat</username>
          <password>tomcat</password>
          <path>/test</path>
          <uriEncoding>UTF-8</uriEncoding>
          <!--deploy end-->
        </configuration>
      </plugin>

    </plugins>
  </build>
</project>

```
可以 `maven` 的 `settings.xml` 配置服务如下：

```xml

<servers>
	<server>
        <id>tomcat8</id>
        <username>tomcat</username>
        <password>tomcat</password>
    </server>
</servers>
```

然后再 `pom.xml` 文件配置如下

```xml
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <configuration>
        <!--deploy begin-->
        <url>http://127.0.0.1:8080/manager/text</url>
        <server>tomcat8</server> <!-- 此处的名字必须和setting.xml中配置的ID一致 -->
        <path>/test</path>
        <uriEncoding>UTF-8</uriEncoding>
        <!--deploy end-->
    </configuration>
</plugin>
```

### 三、发布

cd 到当前项目目录下，执行 `mvn clean tomcat7:deploy`,或者你可以用工具执行，我用是idea，配置如下：

<img src='http://blog.wwudev.cn/img/maven/tomcat-7-deploy.png' width='100%' />

最后访问 ` localhost:8080/test `就可以了

如果已经发布的话那么再次发布会失败，那么可以使用这样的命令 `mvn clean tomcat7:redeploy`

