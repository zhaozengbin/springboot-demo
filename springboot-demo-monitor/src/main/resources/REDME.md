
## 项目名：spring-demo-examples

## 机器IP：127.0.0.1

## 报警阈值：10

## 异常方法：

***
### 方法名：**testException**

### 异常次数：13

### 出现时间：2021-02-24 11:45:06

### 方法类路径：

com.zzb.examples.controller.monitor.ExamplesMonitorController

### 参数信息：

<ol><li>. 2</li>
</ol>
### 追踪信息：

<ol><li>. com.zzb.examples.service.impl.ExceptionServiceImpl.getException(ExceptionServiceImpl.java:13)</li>
<li>. com.zzb.examples.controller.monitor.ExamplesMonitorController.testException(ExamplesMonitorController.java:17)</li>
<li>. com.zzb.monitor.chain.aop.ProjectTreeAspect.around(ProjectTreeAspect.java:36)</li>
</ol>
### 链路： 

>com.zzb.examples.controller.monitor.ExamplesMonitorController.testException(java.lang.Integer)[39ms]
>>	com.zzb.examples.service.impl.ExceptionServiceImpl.getException([I)[20ms]




***