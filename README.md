# kcode 热身赛

## 赛题描述

在工程实习过程中开发同学经常需要关注一个方法或接口的调用次数和处理时间，通常请求量衡量标准有QPS, 响应时间的衡量标准有P99, P50, AVG, MAX等分别对应99分位响应时间，中位数时间，平均响应时间和最长耗时。

## 题目内容

实现一个计算QPS,P99, P50, AVG, MAX的程序，要求包含的功能如下：        
- 实现一个接收打点数据的接口，输入数据格式下面会给出
- 实现一个查询一个方法在特定秒的QPS,P99,P50,AVG,MAX

## 输入数据集格式说明（单行）：

>调用方法时间(ms),方法名称,方法耗时(ms)

>逗号分隔，每部分无空格

示例
```html
1589761895123,getUserName,103
1589761895122,getUserName,1034
1589761895124,getUserName,203
1589761895233,setUserName,132
1589761895376,setUserName,151
```

## 查询方式
>输入：方法名称+调用方法时间(s)

>注意时间单位秒，逗号分隔，每部分无空格

>结果：QPS,P99,P50,AVG,MAX

>结果中的5个值都是以毫秒为单位，如遇小数，向上取整
  

示例
```html
输入:
1589761895,getUserName 

结果:
2,103,103,203,1034
```

## 操作说明

- 输入数据时间戳为毫秒，发生在第 0~1 秒之间的监控数据，属于第 0 秒监控
- 登录 https://kcode-git.kuaishou.com/ 可以看到自己的参赛项目(项目名称为 team_xxxxx)
- 克隆本项目(https://kcode-git.kuaishou.com/kcode/kcode-warm-up)之后，关注 4 个文件：
    - KcodeQuestion
    - KcodeMain
    - pom.xml
    - .gitignore
- 将上述 4 个文件拷贝到自己的参赛 git 项目中，例如，team_xxxxx
- pom.xml 中指定了打包插件(无需修改)
- KcodeQuestion 中需要选手实现两个 prepare() 和 getResult() 两个方法，其中
    - prepare() 方法用来接受输入数据集
    - getResult() 方法是由kcode评测系统调用，是评测程序正确性的一部分，请按照题目要求返回正确数据
- KcodeMain 用于选手本地测试，在参赛项目中无需提交

## 要求和限制
- 题目语言限定为 Java 
- 不允许引入任何外部依赖，JavaSE 8 包含的lib除外
- 排名靠前的代码会被review，如发现大量copy代码或引入不符合要求的三方库，将扣分或取消成绩
- 禁止在代码中输出任何日志输出或是控制台输出，否则无成绩
- 上传与题目无关的代码视为作弊，视为作弊，无成绩

## 评测环境&启动参数
- JDK 版本： xxxx
- GC collector : 
- jvm内存设置：-Xms4g -Xmx4g
- 评测机器硬件信息（docker）：
    - 操作系统 CentOS 7.3 64位
    - CPU	16核
    - 内存	32GB
    - 机型：腾讯云CVM 计算型C3
    
   
## 评测标准&排名
- 系统会默认拉取每个参赛队伍git项目的master代码作为评测程序执行
- 输入数据总条数为 N，接收打点数据的接口总耗时为 T1
- 执行查询 M 次，总耗时 T2
- 以 T1 + T2 和为成绩，时间越短成绩越好
- 执行查询结果需要全部正确，否则视为评测不通过

## 参考项目
https://kcode-git.kuaishou.com/kcode/kcode-warm-up

## 本地测试数据集

为方便下载，将数据集切分为 8 个 100 MB的文件：

http://static.yximgs.com/kos/nlav10305/warmup-test/warmup-test.data.z01
http://static.yximgs.com/kos/nlav10305/warmup-test/warmup-test.data.z02
http://static.yximgs.com/kos/nlav10305/warmup-test/warmup-test.data.z03
http://static.yximgs.com/kos/nlav10305/warmup-test/warmup-test.data.z04
http://static.yximgs.com/kos/nlav10305/warmup-test/warmup-test.data.z05
http://static.yximgs.com/kos/nlav10305/warmup-test/warmup-test.data.z06
http://static.yximgs.com/kos/nlav10305/warmup-test/warmup-test.data.z07
http://static.yximgs.com/kos/nlav10305/warmup-test/warmup-test.data.zip

测试数据集对应的结果：

http://static.yximgs.com/kos/nlav10305/warmup-test/result-test.data