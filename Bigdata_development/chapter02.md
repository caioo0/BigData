# Hadoop环境构建


## Hadoop背景
Hadoop从2.x开始，就开始分化了。逐渐演变成：HDFS，YARN，MapReduce三大应用模块，这三个
应用模块分别的能力和作用是：

```md
1. HDFS:  分布式文件系统，用来接解决海量大文件的存储问题
2. MapReduce: 一套通用的用来解决海量大文件计算的编程模型API
3. YARN: 资源调度/管理系统
```

其中需要主要的是：这三者之间的关系。彼此独立，又相互依赖。使用`MapReduce`的分布式编程API编写分布式计算应用程序，读取存储在`HDFS`上的海量大文件进行计算，由`YARN`提供计算资源。`HDFS`和`YARN`可以独立运行。主要表现在：

```md
1. 使用MapReduce编写的应用程序也可以运行在其他资源调度系统之上；
2. 使用其他编程模型编写的应用程序，比如storm,Spark,Flink等也可运行在YARN集群上。
```

所以称Hadoop是一个分布式的成熟解决方案。
所以其实安装Hadoop,其实安装HDFS和YARN两个集群。HDFS和YARN都是一个一主多从的集群。

HDFS 集群：

```md
一个NameNode主节点/管理节点。
多个DataNode从节点/工作节点。
```

YARN 集群：

```md
一个ResourceManager主节点/管理节点
多个NodeManager从节点/工作节点
```

### 版本选择

现在 Hadoop 经历四个大版本：

```md
Hadoop-0.x # 古老的Hadoop，连YARN都没有，现在应该没有任何企业还在使用这么古老的Hadoop了
hadoop-1.x # 基本淘汰的Hadoop版本。不用考虑
hadoop-2.x # 现阶段主流的使用版本。比如Hadoop-2.6.5， hadoop-2.7.7， hadoop-2.8.5
hadoop-3.x # 目前较新的Hadoop版本，提供了很多新特性，但是升级的企业还是比较少。
```

根据以上的说明和比较，根据我的了解，选择使用 Hadoop-2.7.7

### 集群规划

说到集群规划，那么我们需要了解一下关于Hadoop集群的几种模式。

```md
伪分布式
分布式
高可用
联邦集群
```

主要有这么四种。当然企业中搭建的 Hadoop 集群都是高可用的分布式集群！

所以这里讲的Hadoop集群的规划主要针对Hadoop分布式集群来进行说明。

一台服务器：伪分布式


| 节点名称 | Hadoop                                  | YARN                             |
| ---------- | ----------------------------------------- | ---------------------------------- |
| bigdate2 | NameNode + DataNode + SecondaryNamenode | ResouceManager+<br />NodeManager |

三台服务器：


| 节点名称 | Hadoop                       | YARN                                   |
| ---------- | ------------------------------ | ---------------------------------------- |
| bigdate2 | NameNode + DataNode 主节点   | NodeManager                            |
| bigdate3 | DataNode + SecondaryNamenode | NodeManager                            |
| bigdate4 | DataNode                     | ResouceManager主节点+<br />NodeManager |

四台服务器：


| 节点名称 | Hadoop                      | YARN                                |
| ---------- | ----------------------------- | ------------------------------------- |
| bigdate2 | NameNode   | NodeManager                         |
| bigdate3 | DataNode + SecondaryNamenode | NodeManager                         |
| bigdate4 | DataNode                    | NodeManager |
| bigdate4 | DataNode                    | ResouceManager|

如果有更多的服务器，那么可以按需分配集群角色。实际企业中，一般会将单独性能较好的机器作为集群的主节点。我们在这儿把HDFS和YARN集群的主节点分开安装的不同的节点，主要是为了降低个节点的压力。毕竟我们使用的是虚拟机呀。当然，也可以调整把他们安装在同一个节点。

## 集群安装 

刚才讲集群规划的时候，提到了四种安装模式。

```md
伪分布式 # 主要用来测试学习，搭建快速 
分布式 # 正儿八经的分布式集群。用来了解集群的工作机制，用于正儿八经的企业生产 
高可用 # 现阶段流行的主要集群模式。因为避免Hadoop的SPOF问题。提高集群的可用性。 
联邦集群 # 一些超大型企业可以使用的模式。主要为了应对超大规模的数据量对集群的主节点造 成的压力。
```

### 单机版环境搭建

<nav>
<a href="#一前置条件">一、前置条件</a><br/>
<a href="#二配置-SSH-免密登录">二、配置 SSH 免密登录</a><br/>
<a href="#三HadoopHDFS环境搭建">三、Hadoop(HDFS)环境搭建</a><br/>
<a href="#四HadoopYARN环境搭建">四、Hadoop(YARN)环境搭建</a><br/>
</nav>


## 一、前置条件

Hadoop 的运行依赖 JDK，需要预先安装;

# Linux下JDK的安装

> **系统环境**：centos 7.6
>
> **JDK 版本**：jdk 1.8.0_20

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

