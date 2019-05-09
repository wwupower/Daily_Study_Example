---
title: git基本概念和核心命令使用
date: 2018-08-01 20:10:07
tags: git
categories: 持续集成
---

## 诞生背景
事实是，在2002年以前，世界各地的志愿者把源代码文件通过diff的方式发给Linus，然后由Linus本人通过手工方式合并代码！
<!--more-->
你也许会想，为什么Linus不把Linux代码放到版本控制系统里呢？不是有CVS、SVN这些免费的版本控制系统吗？因为Linus坚定地反对CVS和SVN，这些集中式的版本控制系统不但速度慢，而且必须联网才能使用。有一些商用的版本控制系统，虽然比CVS、SVN好用，但那是付费的，和Linux的开源精神不符。

不过，到了2002年，Linux系统已经发展了十年了，代码库之大让Linus很难继续通过手工方式管理了，社区的弟兄们也对这种方式表达了强烈不满，于是Linus选择了一个商业的版本控制系统BitKeeper，BitKeeper的东家BitMover公司出于人道主义精神，授权Linux社区免费使用这个版本控制系统。

安定团结的大好局面在2005年就被打破了，原因是Linux社区牛人聚集，不免沾染了一些梁山好汉的江湖习气。开发Samba的Andrew试图破解BitKeeper的协议（这么干的其实也不只他一个），被BitMover公司发现了（监控工作做得不错！），于是BitMover公司怒了，要收回Linux社区的免费使用权。

Linus可以向BitMover公司道个歉，保证以后严格管教弟兄们，嗯，这是不可能的。实际情况是这样的：

Linus花了两周时间自己用C写了一个分布式版本控制系统，这就是Git！一个月之内，Linux系统的源码已经由Git管理了！牛是怎么定义的呢？大家可以体会一下。

Git迅速成为最流行的分布式版本控制系统，尤其是2008年，GitHub网站上线了，它为开源项目免费提供Git存储，无数开源项目开始迁移至GitHub，包括jQuery，PHP，Ruby等等。

## 概念

git是一个分布式的版本管理系统

## git和svn的区别

- 分布式和集中式
- 储存方式  git 基于key/value的方式存储文件
- 使用方式  SVN 只需要 commint 而 GIT 需要 add、commint、push 三个步骤

##  git核心命令和使用

### 初始化提交

```shell
#基于远程仓库克隆至本地
git clone <remote_url>

#当前目录初始化为git 本地仓库
git init  <directory>
#添加指定文件至暂存区
git add <fileName>
#添加指定目录至暂存区
git add <directory>
#添加所有
git add -A
#将指定目录及子目录移除出暂存区
git rm --cached target -r
#添加勿略配置文件 .gitignore
本地提交
#提交至本地仓库
git commit file -m '提交评论'
#快捷提交至本地仓库
git commit -am '快添加与提交'
$ git push origin test:master           // 提交本地test分支作为远程的master分支
$ git push origin dev:dev              // 提交本地dev分支作为远程的dev分支
等价于 $ git push origin dev


```
### 分支

```shell
#查看当前分支
git branch [-avv]
#基于当前分支新建分支
git branch <branch name>
#基于提交新建分支
git branch <branch name> <commit id>

$ git branch -d {dev}

#切换分支
git checkout <branch name>
#合并分支
git merge <merge target>
#解决冲突，如果因冲突导致自动合并失败，此时 status 为mergeing 状态.
#需要手动修改后重新提交（commit） 

```
### 远程仓库

```shell
#添加远程地址
git remote add origin http:xxx.xxx
#删除远程地址
git remote remove origin 
#上传新分支至远程
git push --set-upstream origin master  #origin别名 master远程分支名
#将本地分支与远程建立关联
git branch --track --set-upstream-to=origin/dev dev

```


### tag 管理

```
#查看当前
git tag
#创建分支
git tag <tag name> <branch name>
#删除分支
git tag -d <tag name>
```

### 日志

```
#查看当前分支下所有提交日志
git log
#查看当前分支下所有提交日志
git log {branch}
# 单行显示日志
git log --oneline
# 比较两个版本的区别
git log master..dev

#以图表的方式显示提交合并网络
git log --pretty=format:'%h %s' --graph

```

参考资料

https://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000

git内部文件储存原理
https://blog.csdn.net/wangbaolongaa/article/details/52039447












