# Apache Sqoop介绍及数据迁移

`partqurt`  `snappy` `orc`  `Kettle`  `tez` `CHD->implala` `HDP -> hive` `K8S`

- 本节目标

> Apache Sqoop常用命令使用
> 掌握使用Sqoop完成从RDB到HDFS的数据迁移
> 掌握使用Sqoop完成从RDB到Hive的数据迁移
> 掌握使用Sqoop完成从RDB到HBase的数据迁移

## 11.1 Sqoop

### 11.1.1 简介

- Sqoop 是一个用于再Hadoop和关系数据库，或者商业服务器之间的数据传输的工具
- RDB导入数据到HDFS,或者从HDFS导出数据到RDB（使用MapReduce导入和导出数据，提供并行操作和容错）
- 使用者
  - 应用开发人员
  - 系统管理员
  - 数据库管理员
- Sqoop底层就是mapreduce任务，但是只有map,没有reduce.

> 更多信息参见[sqoop 用户手册 v1.4.6](https://sqoop.apache.org/docs/1.4.6/SqoopUserGuide.html)

### 11.1.2 Sqoop 安装

安装sqoop的前提是已经具备Java和Hadoop的环境。


**第一步：下载并解压**

1. 下载地址：[http://mirrors.hust.edu.cn/apache/sqoop/1.4.7/](http://mirrors.hust.edu.cn/apache/sqoop/1.4.7/)[sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz ](http://mirrors.hust.edu.cn/apache/sqoop/1.4.7/sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz)
2. 上传安装包`sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz`到虚拟机中
3. 解压sqoop安装包到指定目录，如：
```shell
tar -zxvf sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz -C ../install/
```

**第二步：修改配置文件sqoop-env.sh**

进入conf文件夹 `cp sqoop-env-template.sh sqoop-env.sh`

修改sqoop-env.sh

配置hadoop，hbase，hive，zookeeper等的安装位置
```
cd /usr/local/services/sqoop/conf/
cp sqoop-env-template.sh sqoop-env.sh
vi sqoop-env.sh

export HADOOP_COMMON_HOME=/root/install/hadoop-2.7.7 
export HADOOP_MAPRED_HOME=/root/install/hadoop-2.7.7 
export HADOOP_CONF_DIR=/root/install/hadoop-2.7.7/etc/hadoop
export HIVE_CONF_DIR=/root/install/apache-hive-2.3.6-bin/conf 
export HIVE_HOME=/root/install/apache-hive-2.3.6-bin 
export HBASE_HOME=/root/install/hbase-1.2.12 
export ZOOCFGDIR=/root/install/zookeeper-3.4.14/conf 
export ZOOKEEPER_HOME=/root/install/zookeeper-3.4.14 

```

**第三步：修改configure-sqoop文件**

`cd /usr/local/services/sqoop/bin/`

`vi configure-sqoop`

注释掉以下代码

```shell
#if [ -z "${HCAT_HOME}" ]; then
#  if [ -d "/usr/lib/hive-hcatalog" ]; then
#    HCAT_HOME=/usr/lib/hive-hcatalog
#  elif [ -d "/usr/lib/hcatalog" ]; then
#    HCAT_HOME=/usr/lib/hcatalog
#  else
#    HCAT_HOME=${SQOOP_HOME}/../hive-hcatalog
#    if [ ! -d ${HCAT_HOME} ]; then
#       HCAT_HOME=${SQOOP_HOME}/../hcatalog
#    fi
#  fi
#fi
#if [ -z "${ACCUMULO_HOME}" ]; then
#  if [ -d "/usr/lib/accumulo" ]; then
#    ACCUMULO_HOME=/usr/lib/accumulo
#  else
#    ACCUMULO_HOME=${SQOOP_HOME}/../accumulo
#  fi
#fi
 
## Moved to be a runtime check in sqoop.
#if [ ! -d "${HCAT_HOME}" ]; then
#  echo "Warning: $HCAT_HOME does not exist! HCatalog jobs will fail."
#  echo 'Please set $HCAT_HOME to the root of your HCatalog installation.'
#fi
 
#if [ ! -d "${ACCUMULO_HOME}" ]; then
#  echo "Warning: $ACCUMULO_HOME does not exist! Accumulo imports will fail."
#  echo 'Please set $ACCUMULO_HOME to the root of your Accumulo installation.'
#fi
```

**第四步：加入mysql驱动包到sqoop-1.4.7/lib目录下**

（1）oracle：将ojdbc6.jar 放入/usr/local/services/sqoop/lib/下

`cp ojdbc6.jar /usr/local/services/sqoop/lib/`

（2）mysql：将mysql-connector-java-5.1.34.jar 放入/usr/local/services/sqoop/lib/下

`cp mysql-connector-java-5.1.34.jar /usr/local/services/sqoop/lib/`


**第五步：配置环境变量**

`sudo vim /etc/profile`添加以下内容：
```shell
export SQOOP_HOME=/usr/local/sqoop-1.4.7
export PATH=SQOOPHOME/bin:SQOOPHOME/bin:PATH
 ```


**第六步：验证sqoop是否安装成功**

`sqoop version`

- 列出MySQL数据有哪些数据库：


**第七步：启动sqoop测试**

```
sqoop list-databases \

--connect jdbc:mysql://hadoop5:3306/ \

--username root \

--password hadoop
```
- 列出MySQL中的某个数据库有哪些数据表：
```
sqoop list-tables \

--connect jdbc:mysql://hadoop5:3306/shopping_db \

--username root \

--password hadoop
```

## 11.2 从RDB的表到HDFS

## 11.2.1 Mysql数据导入到HDFS

“导入工具” 导入单个表从RDBMS到HDFS。表中的每一行被视为HDFS的记录。所有记录 都存储为文本文件的文本数据（或者`Avro`、`sequence`、`textfile`、`Parquet`文件等二进制数据）

### 列举出所有的数据库

**命令行查看帮助**

```md
bin/sqoop list‐databases ‐‐help
```

**列出windows主机所有的数据库**

```md
bin/sqoop list‐databases ‐‐connect jdbc:mysql://192.168.1.7:3306/ ‐‐ username root ‐‐password root
```

**查看某一个数据库下面的所有数据表**

```
bin/sqoop list‐tables ‐‐connect jdbc:mysql://192.168.1.7:3306/userdb ‐‐ username root ‐‐password root
```

如果出现连接拒绝，则在windows的mysql的数据库中执行以下命令: 开启windows的远程连接权限

```md
GRANT ALL PRIVILEGES ON . TO 'root'@'%' IDENTIFIED BY 'yourpassword' 
WITH GRANT OPTION; 
FLUSH PRIVILEGES;
```

```md
\# mysql数据库的表导出到HDFS 

sqoop import --connect jdbc:mysql://localhost/sqoop_db --driver 

com.mysql.jdbc.Driver --table customers --username root --password hadoop --

target-dir /sqoop/db/customers --m 1 

\# connect -> 连接数据库的url 

\# driver -> mysql的驱动包 

\# table -> 指定要导出的表 

\# target-dir -> HDFS的目标目录 

\# m -> mapper的个数 

\# --sqoop-import is an alias of sqoop import

\# 导入数据通过where进行过滤 

sqoop import \

--connect jdbc:mysql://localhost/sqoop_db \

--driver com.mysql.jdbc.Driver \

--table orders \

--where "order_date > '2014-06-10'" \

--username root \

--password hadoop \

--delete-target-dir \

--target-dir /sqoop/db/orders \

--m 3 

\#--where 添加过滤条件 

\#--target-dir 删除HDFS目标目录

\#导入指定的column 

sqoop import \

--connect jdbc:mysql://localhost/sqoop_db \

--driver com.mysql.jdbc.Driver \

--table products \

--columns "product_id,product_name,product_price" \

--username root \

--password hadoop \

--delete-target-dir \

--target-dir /sqoop/db/products \

--m 4 

\#columns -> 指定column列表

\# 将查询的结果集导入到HDFS and \$CONDITIONS 

sqoop import \

--connect jdbc:mysql://localhost/sqoop_db \

--driver com.mysql.jdbc.Driver \

--query "select * from categories where category_id > 50 and \$CONDITIONS" \

--username root \

--password hadoop \

--split-by category_id \

--delete-target-dir \

--target-dir /sqoop/db/categories \

--m 3
```

- --query -> all the queries should end up with $CONDITIONS，which is used by sqoop internally to distribute record ranges to all mappers.(所有查询都应以$ CONDITIONS结束，sqoop在内部使用它来 将记录范围分配给所有mapper).
- --split-by host -> the host column is used to split work units. (切分工作单元).
- $CONDITIONS：该变量记录了 split-by指定的column，通过该字段和mapper number 来分割数据.
- Note：查询结果为一个数据集，sqoop根据split-by值的范围把数据分为若干份(mapper数量决定)， 然后把slipt-by的值给$CONDITIONS变量。比如说是ID，范围1~1500，那么每个mapper处理500条数 据

## 11.5 参考资料

1. https://cloud.tencent.com/developer/column/82426/tag-0
2. https://zhuanlan.zhihu.com/p/451943553
3. https://baijiahao.baidu.com/s?id=1721040222341192801&wfr=spider&for=pc
