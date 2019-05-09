---
title: docker运行tomcat
date: 2018-09-01 10:50:00
tags: docker
categories: 持续集成
---

### 通过镜像构建

这段时间在看docker，并尝试在docker上运行tomcat，下面的记录的笔记：

<!--more-->
#### 1.搜索tomcat镜像
```
docker search tomcat
```
#### 2.下载tomcat镜像

```
docker pull tomcat
```

####3.查看镜像

```
docker images|grep tomcat
```

#### 4.运行tomcat

```
docker run -d -p 8088:8080 tomcat

docker run  --name tomcat -d -p 8088:8080 tomcat

```

#### 5.配置映射本机目录

```
# 复制docker目录当本主机
docker  cp wangwu-tomcat:/usr/local/tomcat/webapps /home/tomcat/

#配置文件和发布目录映射
docker run --name wangwu-tomcat -d -v /home/tomcat/webapps:/usr/local/tomcat/webapps/ -p 8088:8080 -v /home/tomcat/conf:/usr/local/tomcat/conf -v /home/tomcat/logs:/usr/local/tomcat/logs tomcat 
```

-p 8080:8080：将容器的8080端口映射到主机的8080端口

-v $PWD/test:/usr/local/tomcat/webapps/test：将主机中当前目录下的test挂载到容器的/test

#### 6.查看容器启动情况
```
docker ps | grep tomcat
```
#### 7.重启|关闭tomcat

```
docker stop wangwu-tomcat
docker restart wangwu-tomcat
```
