1. 初始大数据leader,怎么部署大数据平台，步骤为：

   

- 数据量评估（至少需要6台，内存至少128G，需要高配）
  - 第一台：NameNode(Active)
  - 第二台：NameNode(standBy)
  - 第三台到第六台：DataNode

思考：为什么 Namenode和DataNode为什么不放在同一个节点？

解答：

1. Namenode：响应客户端请求[参考原理](https://cloud.tencent.com/developer/article/1659296) ，只请求响应，不负责I/O处理(容易宕机)
2. namenode： cpu密集型 ， datanode io密集型。
3. 



​       