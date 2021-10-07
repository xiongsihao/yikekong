# 项目背景

## 1.物联网行业分析

### 1.1 什么是物联网

**物联网**（英文：*Internet of Things*，缩写：*IoT*）起源于传媒领域，是信息科技产业的第三次革命。物联网是指通过信息传感设备，按约定的协议，将任何物体与网络相连接，物体通过信息传播媒介进行信息交换和通信，以实现智能化识别、定位、跟踪、监管等功能。 

在物联网应用中有三项关键技术，分别是感知层、网络传输层和应用层。 

**中国式物联网定义：**

最简洁明了的定义：物联网(Internet of Things)是一个基于互联网、传统电信网等信息承载体，让所有能够被独立寻址的普通物理对象实现互联互通的网络。它具有普通对象设备化、自治终端互联化和普适服务智能化3个重要特征。

![](http://cdn.xiongsihao.com/ykk-1-1.jpg)

上图中出现了四个概念：

**两化融合**是信息化和工业化的高层次的深度结合， 是指以信息化带动工业化、以工业化促进信息化，走新型工业化道路；两化融合的核心就是信息化支撑，追求可持续发展模式。

**M2M**全称Machine to Machine，是指数据从一台终端传送到另一台终端，也就是机器与机器的对话。

射频识别（RFID）是 Radio Frequency Identification 的缩写。其原理为阅读器与标签之间进行非接触式的数据通信，达到识别目标的目的。RFID 的应用非常广泛，典型应用有动物晶片、汽车晶片防盗器、门禁管制、停车场管制、生产线自动化、物料管理。

**传感网**是传感器网络的简称，传感器网络是集[计算机](https://wiki.mbalib.com/wiki/%E8%AE%A1%E7%AE%97%E6%9C%BA)、[通信](https://wiki.mbalib.com/wiki/%E9%80%9A%E4%BF%A1)、网络、智能计算、传感器、[嵌入式系统](https://wiki.mbalib.com/wiki/%E5%B5%8C%E5%85%A5%E5%BC%8F%E7%B3%BB%E7%BB%9F)、微电子等多个领域交叉综合的新兴学科，它将大量的多种类传感器节点(集传感、采集、处理、收发于一体)组成自治的网络，实现对物理世界的动态智能协同感知。

从上图中可以看出，物联网涵盖了上边所提到的四大领域。

**“一句式”理解物联网**

把所有物品通过信息传感设备与互联网连接起来，进行信息交换，即物物相息，以实现智能化识别和管理。

**历史溯源**

物联网这个概念，中国在1999年提出来的时候叫传感网。中科院早在1999年就启动了传感网的研究和开发。与其它国家相比，我国的技术研发水平处于世界前列，具有同发优势和重大影响力。
2005年11月27日，在突尼斯举行的信息社会峰会上，国际电信联盟（ITU）发布了《ITU互联网报告2005：物联网》，正式提出了物联网的概念。
2009年8月24日，中国移动总裁王建宙在台湾公开演讲中，也提到了物联网这个概念。
工信部总工程师朱宏任在中国工业运行2009年夏季报告会上表示，物联网是个新概念，到2009年为止还没有一个约定俗成的，大家公认的概念。他说，总的来说，“物联网”是指各类传感器和现有的“互联网”相互衔接的一种新技术。
物联网是在计算机互联网的基础上，利用RFID、无线数据通信等技术，构造一个覆盖世界上万事万物的“Internet of Things”。在这个网络中，物品(商品)能够彼此进行“交流”，而无需人的干预。其实质是利用射频自动识别(RFID)技术，通过计算机互联网实现物品(商品)的自动识别和信息的互联与共享。
物联网概念的问世，打破了之前的传统思维。过去的思路一直是将物理基础设施和IT基础设施分开，一方面是机场、公路、建筑物，另一方面是数据中心，个人电脑、宽带等。而在物联网时代,钢筋混凝土、电缆将与芯片、宽带整合为统一的基础设施，在此意义上，基础设施更像是一块新的地球。故也有业内人士认为物联网与智能电网均是智慧地球的有机构成部分。

### 1.2 物联网应用领域

1、智能家居
智能家居是利用先进的计算机技术，运用智能硬件（氦氪wifi、Zigbee、蓝牙、NB-iot等），物联网技术，通讯技术，将与家具生活的各种子系统有机的结合起来，通过统筹管理，让家居生活更舒适，方便，有效，与安全。智能家居主要包括智能音箱、智能灯、智能插座、智能锁、智能恒温器、扫地机器人等。

2、智慧交通
智慧交通，是将物联网、互联网、云计算为代表的智能传感技术、信息网络技术、通信传输技术和数据处理技术等有效地集成，并应用到整个交通系统中，在更大的时空范围内发挥作用的综合交通体系 [2]  。智慧交通是以智慧路网、智慧出行、智慧装备、智慧物流、智慧管理为重要内容，以信息技术高度集成、信息资源综合运用为主要特征的大交通发展新模式。依托迪蒙科技在云计算、物联网、大数据、金融科技等领域的丰富开发经验和雄厚的技术积累，历时3年倾力打造的中国目前首家 一款集网约专车、智慧停车、汽车租赁、汽车金融，以及其他智慧出行领域创新商业模式于一体的高端智慧交通整体解决方案 [3]  。

4、智能电网
智能电网是在传统电网的基础上构建起来的集传感、通信、计算、决策与控制为一体的综合数物复合系统，通过获取电网各层节点资源和设备的运行状态，进行分层次的控制管理和电力调配，实现能量流、信息流和业务流的高度一体化，提高电力系统运行稳定性，以达到最大限度地提高设备效利用率，提高安全可靠性，节能减排，提高用户供电质量，提高可再生能源的利用效率。

4、智慧城市
智慧城市就是运用信息和通信技术手段感测、分析、整合城市运行核心系统的各项关键信息，从而对包括民生、环保、公共安全、城市服务、工商业活动在内的各种需求做出智能响应。其实质是利用先进的信息技术，实现城市智慧式管理和运行，进而为城市中的人创造更美好的生活，促进城市的和谐、可持续成长。
随着人类社会的不断发展，未来城市将承载越来越多的人口。目前，我国正处于城镇化加速发展的时期，部分地区“城市病”问题日益严峻。为解决城市发展难题，实现城市可持续发展，建设智慧城市已成为当今世界城市发展不可逆转的历史潮流。
智慧城市的建设在国内外许多地区已经展开，并取得了一系列成果，国内的如智慧上海、智慧双流；国外如新加坡的“智慧国计划”、韩国的“U-City计划”等 。

5、其它领域：智能汽车、智能建筑、智能水务、智能商业、智能工业、平安城市、智能农业、智能安防、智能医疗等。

### 1.3 物联网发展现状

消费级IOT蓬勃发展，仍处初级阶段 

物联网通过相关设备将物与物、人与人进行联网。

（1）规模：全球物联网产业规模自 2008 年500亿美元增长至 2018 年仅 1510  亿美元，年均复合增速达 11.7%。我国物联网产业规模2017年达 11500亿元，自 2011 年起进一步加速，2009-2017  年均复合增速达 26.9%，我国物联网发展速度较全球平均水平更快。

（2）渗透：全球物联网行业渗透率 2013、2017 分别达  12%、29%，提升一倍多，预计2020年有超过 65%企业和组织将应用物联网产品和方案。近年来，我国物联网市场规模不断扩大，2012年的  3650 亿元增长到 2017 年的 11605 亿元，年复合增长率高达 25%。 

# 亿可控需求分析

### 2.1 需求概述

​      亿可控作为一个中台，对设备运行状况进行实时在线监测、预警，不做业务相关的功能。

![](http://cdn.xiongsihao.com/202110071722_771.png)

​	核心功能列表：

​	（1）报文数据采集与指标解析 ：整个系统的数据来源是通过接收设备发送过来的报文消息，在系统中定义主题和消息内容字段的指标数据为过滤条件，从而对消息进行收集和分析。

​	（2）报警监控  ： 通过和系统中定义的各种告警级别数据进行对比，一旦发现触发到告警级别的消息，就会通过和告警关联配置的webhook来将告警信息透传到其它系统

​    （3）GPS定位监控 ：采集每台设备的GPS定位，并提供设备位置查询功能。

​    （4）数据看板 :   提供丰富的自定义数据看板。

### 2.2 业务架构图

![](http://cdn.xiongsihao.com/202110071723_82.png)

从上图可以看到，整个系统从业务上分为6大功能模块：图形监控模块、数据详情展示模块、看板管理模块、设备管理模块、报警管理模块、系统管理模块。

### 2.3 核心业务描述

![](http://cdn.xiongsihao.com/202110071731_510.png)

产品原型地址：

https://app.mockplus.cn/run/prototype/yYVLQlJ-YN6/JhE4uVilt/4nw_LQ8n7

### 3.1 亿可控系统架构

整个系统的技术架构图如下：

![](http://cdn.xiongsihao.com/202110071725_355.png)

预制数据将放入MySQL里进行存储，设备上报的指标数据包括告警数据将存入influxDB中，设备的地理位置信息数据存入到ES中以便后期搜索。为了提高系统的运行稳定性，有些频繁访问的数据储存在redis中，因为考虑到设备上报的数据是非常频繁的，如果单单只依靠MySQL数据库的话，会很容易将MySQL服务器的CPU的占用率搞到100%，从而会引发整个系统的崩溃无法使用。

一些基本的配置放入到了consul的配置中心，考虑到系统的横向扩展能力，将整个系统基于Consul做注册中心来搭组建一个微服务。

注：influxDB数据库特点：只可插入与查询，不可做更新和删除操作;

### 3.2 数据库设计

mysql数据库有5个表：

**管理员表tb_admin**

| 列名       | 数据类型    | 说明                                          |
| ---------- | ----------- | --------------------------------------------- |
| id         | int         | 表主键id，自增                                |
| login_name | varchar(50) | 登录账号                                      |
| password   | varchar(60) | 密码                                          |
| type       | tinyint     | 类型 1:超级管理员 2:普通用户 目前作为保留字段 |
| board      | varchar(50) | 看板列表                                      |

**指标配置表tb_quota**

| 列名            | 数据类型      | 说明                                   |
| --------------- | ------------- | -------------------------------------- |
| id              | int           | 表主键id                               |
| name            | varchar(50)   | 指标名称                               |
| unit            | varchar(20)   | 指标单位                               |
| subject         | varchar(50)   | 报文主题                               |
| value_key       | varchar(50)   | 指标值字段                             |
| sn_key          | varchar(50)   | 设备识别码字段                         |
| webhook         | varchar(1000) | web钩子                                |
| value_type      | varchar(10)   | 指标字段类型，Double、Inteter、Boolean |
| reference_value | varchar(100)  | 参考值                                 |

**报警配置表tb_alarm**

| 列名      | 数据类型      | 说明                     |
| --------- | ------------- | ------------------------ |
| id        | int           | 表主键id，自增           |
| name      | varchar(50)   | 报警指标名称             |
| quota_id  | int           | 关联指标名称             |
| operator  | varchar(10)   | 运算符                   |
| threshold | int           | 报警阈值                 |
| level     | int           | 报警级别 1：一般 2：严重 |
| cycle     | int           | 沉默周期(以分钟为单位)   |
| webhook   | varchar(1000) | web钩子地址              |

**面板配置表tb_board**

| 列名     | 数据类型     | 说明           |
| -------- | ------------ | -------------- |
| id       | int          | 表主键id，自增 |
| admin_id | int          | 管理员id       |
| name     | varchar(50)  | 看板名称       |
| quota    | varchar(100) | 指标           |
| device   | varchar(100) | 设备           |
| system   | tinyint      | 是否是系统看板 |
| disable  | tinyint      | 是否不显示     |

**GPS配置表tb_gps**

| 列名       | 数据类型    | 说明                   |
| ---------- | ----------- | ---------------------- |
| id         | bigint      | 表主键id               |
| subject    | varchar(50) | 报文主题               |
| sn_key     | varchar(50) | 设备识别码字段         |
| type       | tinyint     | 类型（单字段、双字段） |
| value_key  | varchar(50) | 经纬度字段             |
| separation | varchar(10) | 经纬度分隔符           |
| longitude  | varchar(20) | 经度字段               |
| latitude   | varchar(20) | 维度字段               |