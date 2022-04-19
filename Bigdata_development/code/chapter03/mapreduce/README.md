
## 查看最高气温的mapper类：

* 1. 打包jar复制到文件夹：share/hadoop/mapreduce/mapreduce03-1.0-SNAPSHOT.jar
* 2. 执行代码：bin/hadoop jar share/hadoop/mapreduce/mapreduce03-1.0-SNAPSHOT.jar com.Max.temp/MaxTemperature input/sample.txt output2
* 3. hadoop fs -cat output2/part-r-00000
* sample.txt
```
* 0067011990999991950051507004+68750+023550FM-12+038299999V0203301N00671220001CN9999999N9+00001+99999999999
* 0043011990999991950051512004+68750+023550FM-12+038299999V0203201N00671220001CN9999999N9+00221+99999999999
* 0043011990999991950051518004+68750+023550FM-12+038299999V0203201N00261220001CN9999999N9-00111+99999999999
* 0043012650999991949032412004+62300+010750FM-12+048599999V0202701N00461220001CN0500001N9+01111+99999999999
* 0043012650999991949032418004+62300+010750FM-12+048599999V0202701N00461220001CN0500001N9+00781+99999999999
```
结果：
```shell
[root@hadoop5 hadoop-2.7.7]# hadoop fs -cat output2/part-r-00000
1949    111
1950    22

```

## 官方wordcount 

1. 打包jar复制到文件夹：share/hadoop/mapreduce/mapreduce0-wordcount.jar
2. 复制hello.txt到dfs文件夹：`hadoop fs -put hello.txt input`
hello.txt
```text
atguigu atguigu
ss ss
cls cls
jiao
banzhang
xue
hadoop
```
3. 执行代码：hadoop jar share/hadoop/mapreduce/mapreduce-wordcount.jar com.mapreduce.WordcountDriver input/hello.txt output3
4. hadoop fs -cat output3/*
结果：
```
[root@hadoop5 hadoop-2.7.7]# hadoop fs -cat output3/*
atguigu 2
banzhang        1
cls     2
hadoop  1
jiao    1
ss      2
xue     1
```




