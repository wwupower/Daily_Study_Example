---
title: git项目开发一些问题
date: 2018-09-18 11:25:20
tags: git
categories: 持续集成
---
### 一、Git 开发流程

<!--more-->

<img src="http://blog.wwudev.cn/img/git/日常开发.png" width=100% />

#### master 分支
1. `Master` 分支应该始终和生产环境保持一致。
2. 由于 `master` 和生产代码是一致的，所以没有人包括技术负责人能在 `master` 上直接开发。
3. 真正的开发代码应当写在其他分支上。

#### Release(发布) 分支
1. 当项目开始时，第一件事情就是创建发布分支。发布分支是基于 `master` 分支创建而来。
2. 所有与本项目相关的代码都在发布分支中，这个分支也是一个以 `release/` 开头的普通分支。
比如这次的发布分支名为 `release/fb`。
3. 可能有多个项目都基于同一份代码运行，因此对于每一个项目来说都需要创建一个独立的发布分支。假设现在还有一个项目正在并行运行，那就得为这个项目创建一个单独的发布分支比如 `release/messenger`。
4. 需要单独的发布分支的原因是：多个并行项目是基于同一份代码运行的，但是项目之间不能有冲突。

#### Feature(功能分支) branch
1. 对于应用中的每一个功能都应该创建一个独立的功能分支，这会确保这些功能能被单独构建。
功能分支也和其他分支一样，只是以 `feature/` 开头。
2.如登录模块分支就以 `feature/login` 这样的分支

#### 功能分支合并

通常一个功能开发完成之后，我们需要把当前分支合并到发布分支上，我们必须经过代码走查才能合并到分支，合并我们需要 `pull requst`



### 二、一些常用的操作

#### 2.1 如何添加忽略提交文件 
创建忽略文件 `.gitignore`,编辑内容如下：
```
.idea
*.iml
*.ipr
*.iws
out
.classpath
.project
.settings/
```
需要注意的是，修改忽略文件需要把文件移除缓存区，如：将指定目录及子目录移除出暂存区 `git rm --cached target -r`,再把文件添加到缓存区 `add`,这样修改的忽略文件才有效果

#### 2.2.如何创建功能开发分支
```
$ git branch dev1 #基于当前分支创建功能分支dev1

$ git branch --set-upstream-to=origin/release #关联远程分支
$ git push origin dev1:dev1 #把本地分支推送到远程分支
```

#### 2.3.合并分支
```
git merge dev
```
git merge 命令用于合并指定分支到当前分支。

#### 2.4.删除分支
```
$ git branch -d dev
```

#### 2.5.查看分支图
```
git log --graph
```


### 三、一些常见问题

#### 3.1.合并分支冲突了怎么搞？
比如User1修改了在feture/login分支上修改了login文件，但是user2在feture/order也修改了文件，user1先把代码feture/login合并到release分支上，当user2再把代码合并到release上的时候，就是产生冲突。<br>
```
Auto-merging src/main/webapp/index.jsp
CONFLICT (content): Merge conflict in src/main/webapp/index.jsp
Automatic merge failed; fix conflicts and then commit the result.
```
那么要如何去解决这个问题呢?

一般两种方式区解决

> 1.合并代码人解决冲突；

```
git status #查看冲突文件
git diff #查看对比文件
```
手动合并代码完整之后

```
git commit -am '合并分支解决冲突并提交'
git push
```

> 2.开发功能开支人解决，把最新的代码合并到功能分支，并解决冲突，目前我们是采用这种方式

```
git merge release
```
冲突之后可看到

<img src="http://blog.wwudev.cn/img/git/git分支合并冲突.png" width=100% />

把release分支的最新代码合并到本地功能分支，解决好冲突之后，再提交到远程，然后再发起合并请求。这样就不会产生冲突了。
解决好之后：

<img src="http://blog.wwudev.cn/img/git/git合并正常.png" width=100% />

#### 3.2 我提交错了，想撤回怎么撤

场景1：当你改乱了工作区某个文件的内容，想直接丢弃工作区的修改时，用命令git `checkout -- file`。

场景2：当你不但改乱了工作区某个文件的内容，还添加到了暂存区时，想丢弃修改，分两步，第一步用命令 `git reset HEAD file` ，就回到了场景1，第二步按场景1操作。

场景3：已经提交了不合适的修改到版本库时，想要撤销本次提交，使用 `git reset --hard HEAD^` 。上一个版本就是HEAD^，上上一个版本就是HEAD^^，当然往上100个版本写100个^比较容易数不过来，所以写成HEAD~100。

场景4：如果你回退版本后又后悔了，想恢复最后那个版本怎么办，通过 `git reset --hard commit_id` 命令可以搞定，注意这里的 `commit_id` 是版本号，只要记得版本号，你想切换到哪个版本都行，如果你忘记了刚才最后一个的版本号，可以通过 `git reflog` 来查看，这里我们记得最后那次版本号为b520a36，执行 `git reset --hard b520a36`












