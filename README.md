## 说明

dbpool-spring-boot-starter，通过自动切换数据源的方式，提供客户端上的主从自动切换的简单解决方案。

## 基本使用

### 引入依赖
```
<dependency>
    <groupId>cn.fishermartyn.inf</groupId>
    <artifactId>dbpool-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 开启自动配置

在Spring 配置类中增加@EnableDbPool注解

```
@Configuration
@EnableDbPool
public class AppConfig {
    //...
}
```
### 配置数据源
针对不同的配置环境，修改application-{env}.properties，例如application-dev.properties，增加相关配置，举例如下：


配置  | 说明
------------- | -------------
dbpool.url  | 数据库连接配置，如果有从库，使用逗号分开，可以支持多从库
dbpool.username  | 数据库用户名，与url一一对应
dbpool.password | 数据库密码，与url一一对应
dbpool.auto-change-data-source | 是否开启自动切换数据源，默认true，写操作自动写主库；读操作自动读从库

例如：

```
dbpool.url=jdbc:mysql://192.168.11.1:3306/demo,jdbc:mysql://192.168.11.1:3307/demo
dbpool.username=root,root
dbpool.password=dev#pass,test#pass
dbpool.initial-size=2
dbpool.auto-change-data-source=true
```


### 强制切换数据源
遇到例如强制读主库的case，可以利用DbTypeHolder强制切换主从库，例如：

```
DbTypeHolder.setMaster();
//your code
DbTypeHolder.setSlave();
//your code
```

## 基本原理
1. 通过mybaits plugin根据statement判断sql语句
2. 通过DMSRoutingDataSource根据ThreadLocal的DbTypeHolder来进行db切换。

## 使用demo
TODO
