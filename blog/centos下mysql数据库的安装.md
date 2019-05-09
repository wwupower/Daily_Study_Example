---
title: centos下mysql数据库的安装
date: 2017-10-06 22:51:06
tags: mysql安装
categories: mysql
---



### 1、下载安装包
https://dev.mysql.com/downloads/mysql/5.5.html#downloads
![这里写图片描述](http://img.blog.csdn.net/20171006095843819?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvd3d1UG93ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
<!--more-->
### 2、上传安装包到 centos
使用用具xshell ，由于系统刚装完，先安装上传下载组件；
```
yum install lrzsz
```
查找这两个文件是否存在，存在则说明之前有个安装，删除掉即可。还有可能是系统自带的，先删掉；
```
 ll /etc/my.cnf
 ll /etc/init.d/mysql*
```

### 3.安装依赖包异步 IO(libaio)
```
 yum search libaio      
 yum install libaio     #安装libaio
```
![这里写图片描述](http://img.blog.csdn.net/20171006105150898?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvd3d1UG93ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


### 4.解压安装包
```
# cd 到 mysoft/pack 目录下
tar -zxcf  mysql-5.5.57-linux-glibc2.12-x86_64.tar.gz
#创建软连接
[root@localhost mysoft]# ln -s /mysoft/pack/mysql-5.5.57-linux-glibc2.12-x86_64 mysql
```

### 5.创建用户和用户权限
```
[root@localhost mysoft]# ll
总用量 0
lrwxrwxrwx. 1 root root 48 10月  6 11:32 mysql -> /mysoft/pack/mysql-5.5.57-linux-glibc2.12-x86_64
drwxr-xr-x. 3 root root 99 10月  6 11:30 pack
#创建mysql用户组
[root@localhost mysoft]# groupadd mysql 
#把用户mysql添加到mysql用户组， -r系统用户 -g所属组 -s /bin/false 用户不可登录
[root@localhost mysoft]# useradd -r -g mysql -s /bin/false mysql
[root@localhost mysoft]# cd mysql
#改变文件的所有权 -R表示改变一个目录和其下文件（或者子目录）的所有权设置 
[root@localhost mysql]# chown -R mysql ./  #mysql下的所有目录和文件交给mysql用户
[root@localhost mysql]# chgrp -R mysql ./  #mysql下的所有目录和文件交给mysql组
[root@localhost mysql]# ll
总用量 56
drwxr-xr-x.  2 mysql mysql  4096 10月  6 11:30 bin
-rw-r--r--.  1 mysql mysql 17987 6月   5 14:38 COPYING
drwxr-xr-x.  3 mysql mysql    18 10月  6 11:30 data
drwxr-xr-x.  2 mysql mysql    55 10月  6 11:30 docs
drwxr-xr-x.  3 mysql mysql  4096 10月  6 11:30 include
-rw-r--r--.  1 mysql mysql   301 6月   5 14:38 INSTALL-BINARY
drwxr-xr-x.  3 mysql mysql  4096 10月  6 11:30 lib
drwxr-xr-x.  4 mysql mysql    30 10月  6 11:30 man
drwxr-xr-x. 10 mysql mysql  4096 10月  6 11:30 mysql-test
-rw-r--r--.  1 mysql mysql  2496 6月   5 14:38 README
drwxr-xr-x.  2 mysql mysql    30 10月  6 11:30 scripts
drwxr-xr-x. 27 mysql mysql  4096 10月  6 11:30 share
drwxr-xr-x.  4 mysql mysql  4096 10月  6 11:30 sql-bench
drwxr-xr-x.  2 mysql mysql  4096 10月  6 11:30 support-files
```
### 6.拷贝 mysql 配置文件
```
[root@localhost mysql]# cd support-fils
[root@localhost support-files]# cp my-medium.cnf /etc/my.cnf
[root@localhost support-files]# cp mysql.server /etc/init.d/mysql
```

###7.修改 my.cnf
```
vim /etc/my.cnf
#修改mysql指向，添加这两个目录；
basedir=/mysoft/mysql
datadir=/mysoft/mysql/data
```
![这里写图片描述](http://img.blog.csdn.net/20171006140445130?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvd3d1UG93ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

### 8.安装 mysql
```
[root@localhost mysql]# cd scripts
[root@localhost scripts]# ./mysql_install_db --user=mysql --basedir=/mysoft/mysql datadir=/mysoft/mysql/data
```
安装成功效果
![这里写图片描述](http://img.blog.csdn.net/20171006140704113?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvd3d1UG93ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

### 9.添加环境变量
```
[root@localhost scripts]# vim /etc/profile
#添加环境变量
export PATH=$PATH:/mysoft/mysql/bin
[root@localhost scripts]# source /etc/profile #生效
```

###10.初始化用户名密码
```
[root@localhost bin]# ./mysql_secure_installation
```
###11.授权远程登录
```
mysql -uroot -p
```
```sql
mysql> use mysql
mysql> update user set host='%' where user='root' and host ='localhost';
mysql> flush pivileges;
```
### 12.关闭防火墙
```
[root@localhost bin]# systemctl stop firewalld.service
```
### 13.使用工具连接
![这里写图片描述](http://img.blog.csdn.net/20171006141536272?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvd3d1UG93ZXI=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)