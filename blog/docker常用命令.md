---
title: docker常用命令
date: 2018-08-09 20:56:39
tags: docker
categories: 持续集成
---

### 1.启动

使用以下docker run命令即可新建并启动一个容器，该命令是最常用的命令，它有很多选项，下面将列举一些常用的选项。
<!--more-->
-d选项：表示后台运行

-P选项：随机端口映射

-p选项：指定端口映射，有以下四种格式

-- ip:hostPort:containerPort <br>
-- ip::containerPort<br>
-- hostPort:containerPort <br>
-- containerPort<br>
--net选项：指定网络模式，该选项有以下可选参数：<br>
--net=bridge:默认选项，表示连接到默认的网桥。<br>
--net=host:容器使用宿主机的网络。<br>
--net=container:NAME-or-ID：告诉 Docker让新建的容器使用已有容器的网络配置。<br>
--net=none：不配置该容器的网络，用户可自定义网络配置。<br>

例子
```
docker run -d -p 91:80 nginx
```
这样就能启动一个 Nginx容器。在本例中，为 docker run添加了两个参数，含义如下：
-d 后台运行
-p 宿主机端口:容器端口 #开放容器端口到宿主机端口


###  2.列出容器
用 docker ps命令即可列出运行中的容器
```
# docker ps
```
 
如需列出所有容器（包括已停止的容器），可使用-a参数。该列表包含了7列，含义如下
- CONTAINER_ID：表示容器 ID。
- IMAGE:表示镜像名称。
- COMMAND：表示启动容器时运行的命令。
- CREATED：表示容器的创建时间。 
- STATUS：表示容器运行的状态。UP表示运行中， Exited表示已停止。 
- PORTS:表示容器对外的端口号。 
- NAMES:表示容器名称。该名称默认由 Docker自动生成，也可使用 docker run命令的--name选项自行指定。

### 3.停止容器
使用 docker stop命令，即可停止容器
```
 docker stop f0b1c8ab3633
```

其中f0b1c8ab3633是容器 ID,当然也可使用 docker stop容器名称来停止指定容器

### 4.强制停止容器
可使用 docker kill命令发送 SIGKILL信号来强制停止容器
```
 docker kill f0b1c8ab3633
```

### 5.启动已停止的容器
使用docker run命令，即可新建并启动一个容器。对于已停止的容器，可使用 docker start命令来启动
```
 docker start f0b1c8ab3633
```

### 6.查看容器所有信息
```
 docker inspect f0b1c8ab3633
```
### 7.查看容器日志
```
 docker container logs f0b1c8ab3633
```
### 8.查看容器里的进程
```
 docker top f0b1c8ab3633
```
### 9.进入容器

使用docker container exec命令用于进入一个正在运行的docker容器。如果docker run命令运行容器的时候，没有使用-it参数，就要用这个命令进入容器。一旦进入了容器，就可以在容器的 Shell 执行命令了,ctrl+D 上推出
```
 docker  exec -it container /bin/bash
```
例子
```
docker  exec -it  42b7dd6a8bb5  /bin/bash
```

### 10.删除容器

使用 docker rm命令即可删除指定容器
```
# docker rm f0b1c8ab3633
```
该命令只能删除已停止的容器，如需删除正在运行的容器，可使用-f参数
