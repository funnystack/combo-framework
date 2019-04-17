#why choose mybatis autocode factory

1，生成的model实体上字段加上javadoc，为数据库表字段的备注。

2，当数据库设计字段为tinyint时，生成的model实体的java类型为Integer,而不是byte，boolean,省去修改类型成integer的时间。

3，生成的model都继承BaseEntity，实现了序列化接口，克隆接口。

4，生成的dao文件默认继承BaseMapper，实现了增删改查原子操作，不需要在dao文件再写这些通用方法。

5，生成的dao的xml文件有2份，一份是基础的增删改查的子操作的Entity.xml，一个是写业务方法的xml文件，sql语句与java的dao接口对应即可。

6，使用统一的代码工具可以有助于代码风格的统一。


1，新建maven项目

2，将项目的packing改成pom形式<此步非常重要>


4，引入插件，并配置
```xml
<plugin>
    <groupId>com.funny</groupId>
    <artifactId>autocode-plugin</artifactId>
    <version>1.0.3-SNAPSHOT</version>
    <configuration>
        <jdbcDriver>com.mysql.jdbc.Driver</jdbcDriver>
        <jdbcURL>jdbc:mysql://127.0.0.1:3306/data?useUnicode=true&amp;characterEncoding=UTF-8</jdbcURL>
        <jdbcUserId>root</jdbcUserId>
        <jdbcPassword>123456</jdbcPassword>

        <domainPackage>com.funny.autocode.model.entity</domainPackage>
        <domainProject>ttx-domain</domainProject>
    
        <daoPackage>com.funny.autocode.dao.stat</daoPackage>
        <daoProject>ttx-domain</daoProject>
        <tableNames>chat</tableNames>
    </configuration>
</plugin>
```

5，运行
```
mvn mall-autocode:generate
```