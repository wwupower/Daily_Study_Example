---
title: “使用Atlas实现mysql读写分离 实现数据库负载均衡”
date: 2018-07-04 21:22:39
tags: 数据库
categories: mysql

---

### 一、简介


Atlas 是由 Qihoo 360公司 Web 平台部基础架构团队开发维护的一个基于 MySQL 协议的数据中间层项目。它在 MySQL 官方推出的 MySQL-Proxy 0.8.2 版本的基础上，修改了大量bug，添加了很多功能特性。目前该项目在360公司内部得到了广泛应用，很多MySQL业务已经接入了Atlas平台，每天承载的读写请求数达几十亿条。同时，有超过50家公司在生产环境中部署了Atlas，超过800人已加入了我们的开发者交流群，并且这些数字还在不断增加。
<!--more-->
主要功能：

1.读写分离

2.从库负载均衡

3.IP 过滤

4.自动分表

5.DBA 可平滑上下线 DB

6.自动摘除宕机的 DB

### 二、Atlas 相对于官方 MySQL-Proxy 的优势

---

1.将主流程中所有Lua代码用C重写，Lua仅用于管理接口

2.重写网络模型、线程模型

3.实现了真正意义上的连接池

4.优化了锁机制，性能提高数十倍

### 三、安装与配置

---

#### 3.1注意事项

 (1).Atlas只能安装运行在64位的系统上。
 
(2).Centos 5.X安装 Atlas-XX.el5.x86_64.rpm，Centos 6.X安装Atlas-XX.el6.x86_64.rpm。

(3).如果执行sudo rpm –i Atlas-XX.el6.x86_64.rpm，提示类似：“file /usr/local/mysql-proxy/bin/encrypt from install of Atlas-2.0.1-1.x86_64 conflicts with file from package Atlas-1.0.3-1.x86_64”错误，则表示该系统之前已经安装过Atlas-1.0.3-1.x86_64，需要执行：sudo rpm –e Atlas-1.0.3-1.x86_64，将之前安装的Atlas删除掉，再执行sudo rpm –i Atlas-XX.el6.x86_64.rpm安装新版本的Atlas。

(4).后端mysql版本应大于5.1，建议使用Mysql 5.6

Atlas 的安装是非常简单的，安装包的下载地址https://github.com/Qihoo360/Atlas/releases；


#### 3.2 上传安装

把安装包上传到/usr/local/mysql-proxy,然后执行安装命令；
```
sudo rpm –i Atlas-XX.el6.x86_64.rpm
```
![安装](/img/mysql/20171008201450434.png)

#### 3.3 配置

由于之前已经搭建好一主多从的环境了，master ip：192.168.119.129，slave ip：192.168.119.128；
假如你还没这样的环境可以参考我的这篇文章[《mysql 一主多从配置说明》 ],那么我们现在开始来配置Atlas的读写分离；

在/usr/local/conf 目录下找到test.cnf

```
#Atlas后端连接的MySQL主库的IP和端口，可设置多项，用逗号分隔
proxy-backend-addresses = 127.0.0.1:3306

#Atlas后端连接的MySQL从库的IP和端口，@后面的数字代表权重，用来作负载均衡，若省略则默认为1，可设置多项，用逗号分隔
proxy-read-only-backend-addresses =192.168.119.128:3306@1

#用户名与其对应的加密过的MySQL密码，密码使用PREFIX/bin目录下的加密程序encrypt加密，下行的user1和user2为示例，将其替换为你的MySQL的用>户名和加密密码！
pwds = root:DAJnl8cVzy8=

#SQL日志的开关，可设置为OFF、ON、REALTIME，OFF代表不记录SQL日志，ON代表记录SQL日志，REALTIME代表记录SQL日志且实时写入磁盘，默认为OFF 这里为了查询效果我把SQL记录到日志里
sql-log = REALTIME

#实例名称，用于同一台机器上多个Atlas实例间的区分
#instance = test
```
好了，现在配置完成，我们开始启动服务；

#### 3.4 启动和关闭服务

```
#  cd /usr/local/mysql-proxy/bin
#  ./mysql-proxyd test start        # 启动
#  ./mysql-proxyd test stop         # 关闭
#  ./mysql-proxyd test status       # 查看状态
#  ./mysql-proxyd test restart      # 重启
```

我们先用客户连接试试；

点击“连接测试” “连接成功”就表示是通过Atlas代理连接数据库的

#### 3.5 检验读写分离
实现目标，我们通过代理的查询是不是去查slave库的，插入更新是不是在master库的。

验证方法，通过代理连接的数据库，通过查询、插入、更新数据；然后看看Atlas的sql日志；

```
INSERT INTO t_test_user(age,name) VALUES('20','hello Atlas');
update t_test_user SET age='25' WHERE id='10';
SELECT * from t_test_user;
```
开始查看sql日志

![这里写图片描述](http://img.blog.csdn.net/20171008205057870?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvd3d1UG93ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

更多配置请参考
https://github.com/Qihoo360/Atlas/wiki

《参照资料》
https://github.com/Qihoo360/Atlas