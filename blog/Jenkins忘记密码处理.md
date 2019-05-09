---
title: Jenkins忘记密码处理
date: 2018-08-16 20:48:19
tags: jenkins
categories: 持续集成
---


由于之前的同事离职，最近使用的jenkins进行项目构建集成时候发现忘记之前的账户了，为此把忘记密码的处理简单记录如下：
<!--more-->
修改用户命名；找到对应的目录
```
修改 JENKINS_HOME/users/user.conf  user.conf 就是忘记密码对应的用户
针对 yum install jenkins 安装的Jenkins，它的HOME目录位于 /var/lib/jenkins/ 
```

> 修改用户

```
/var/lib/jenkins/users
```

> 备份 切换到用户目录如

```
# cd admin
```
> 修改密码

修改为密码111111：
```
 <passwordHash>#jbcrypt:$2a$10$DdaWzN64JgUtLdvxWIflcuQu2fgrrMSAMabF5TSrGK5nXitqK9ZMS</passwordHash>
```

> 重启

```
service jenkins restart
```