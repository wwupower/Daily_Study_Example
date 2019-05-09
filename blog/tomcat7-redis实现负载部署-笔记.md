---
title: “nginx+tomcat7+redis 实现负载部署-笔记”
date: 2018-12-23 21:22:39
tags: 数据库
categories: mysql
---
### 1.资源下载

https://pan.baidu.com/s/1Egh0cHXc2yeAcfzEM_3iwA

### 2.目的
主要为了实现用户无感知的更新和重启服务，技术实现主要基于nginx+tomcat7+redis来实现；原理如下图：
<img src="https://note.youdao.com/yws/public/resource/1d1ab52f19aa3a5e550cba386fd3a018/xmlnote/04EEDED6308C4128AE1E78E6F93015FE/10397">

### 3.配置步骤说明

> 3.1 redis 安装

本文Redis下载地址: https://github.com/MSOpenTech/redis/releases 本次使用Redis版本用的是Redis3.2.100

> window下redis服务的安装步骤如下：

本次使用默认服务<br>
1.进入Redis安装包目录，注册服务
```
redis-server.exe --service-install redis.windows.conf --loglevel verbose

```
备注：通过以上面命令，会在window  Service列表出现”Redis”服务，但此服务不是启动状态，需要调下面命令启动服务。
```
    启动服务：redis-server.exe
``` 
```
    客户端调用: redis-cli.exe -h 127.0.0.1 -p 6379
```   

```
    停止服务：redis-server.exe  --service-stop
    卸载服务: redis-server.exe  --service-uninstall
```

> 3.2.tomcat7配置

把三个jar拷贝到tomcat的lib目录下：<br>
commons-pool2-2.4.2.jar<br>
jedis-2.9.0.jar<br>
tomcat-redis-session-manager-2.0.0.jar<br>

在tomcat的\conf目录下的context.xml 添加如下内容：

```xml
<Valve className="com.orangefunction.tomcat.redissessions.RedisSessionHandlerValve" />
    <Manager className="com.orangefunction.tomcat.redissessions.RedisSessionManager"
       host="localhost"
       port="6379"
       database="0"
       maxInactiveInterval="60" />
```

设置不同的tomcat的访问端口；案例分别用 8080 9090 9091三个端口

> 3.3 nginx 配置

在nginx-1.8.1\conf目录下的nginx.conf文件添加如下的配置
```
 upstream localhost{
	  server localhost:9091 down;
	  server localhost:8080 weight=1;
	  server localhost:9090 weight=1 max_fails=1 fail_timeout=2s;
	  #server localhost:9091 backup;
	  
	}
	
    server {
        listen       8090;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location ^~ /{
			proxy_pass http://localhost;
			proxy_set_header Host $http_host;
			proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;  
			proxy_set_header X-Real-IP $remote_addr;  
			proxy_set_header Request-Url $request_uri;
			proxy_connect_timeout       1;
			proxy_read_timeout          1;
			proxy_send_timeout          1;
		}
		
        #error_page  404              /404.html;
        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
```


