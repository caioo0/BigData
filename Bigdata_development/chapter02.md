# Hadoop环境构建



## 单机版环境搭建

<nav>
<a href="#一前置条件">一、前置条件</a><br/>
<a href="#二配置-SSH-免密登录">二、配置 SSH 免密登录</a><br/>
<a href="#三HadoopHDFS环境搭建">三、Hadoop(HDFS)环境搭建</a><br/>
<a href="#四HadoopYARN环境搭建">四、Hadoop(YARN)环境搭建</a><br/>
</nav>


## 一、前置条件

Hadoop 的运行依赖 JDK，需要预先安装;

# Linux下JDK的安装

>**系统环境**：centos 7.6
>
>**JDK 版本**：jdk 1.8.0_20



### 1. 下载并解压

在[官网](https://www.oracle.com/technetwork/java/javase/downloads/index.html) 下载所需版本的 JDK，这里我下载的版本为[JDK 1.8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) ,下载后进行解压：

```shell
[root@ java]# tar -zxvf jdk-8u201-linux-x64.tar.gz
```



### 2. 设置环境变量

```shell
[root@ java]# vi /etc/profile
```

添加如下配置：

```shell
export JAVA_HOME=/usr/java/jdk1.8.0_201  
export JRE_HOME=${JAVA_HOME}/jre  
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib  
export PATH=${JAVA_HOME}/bin:$PATH
```

执行 `source` 命令，使得配置立即生效：

```shell
[root@ java]# source /etc/profile
```



### 3. 检查是否安装成功

```shell
[root@ java]# java -version
```

显示出对应的版本信息则代表安装成功。

```shell
java version "1.8.0_201"
Java(TM) SE Runtime Environment (build 1.8.0_201-b09)
Java HotSpot(TM) 64-Bit Server VM (build 25.201-b09, mixed mode)

```


## 二、配置免密登录

Hadoop 组件之间需要基于 SSH 进行通讯。

#### 2.1 配置映射

配置 ip 地址和主机名映射：

```shell
vim /etc/hosts
# 文件末尾增加
192.168.43.202  hadoop001
```

### 2.2  生成公私钥

执行下面命令行生成公匙和私匙：

```
ssh-keygen -t rsa
```

### 3.3 授权

进入 `~/.ssh` 目录下，查看生成的公匙和私匙，并将公匙写入到授权文件：

```shell
[root@@hadoop001 sbin]#  cd ~/.ssh
[root@@hadoop001 .ssh]# ll
-rw-------. 1 root root 1675 3 月  15 09:48 id_rsa
-rw-r--r--. 1 root root  388 3 月  15 09:48 id_rsa.pub
```

```shell
# 写入公匙到授权文件
[root@hadoop001 .ssh]# cat id_rsa.pub >> authorized_keys
[root@hadoop001 .ssh]# chmod 600 authorized_keys
```



## 三、Hadoop(HDFS)环境搭建



