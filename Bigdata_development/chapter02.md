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



