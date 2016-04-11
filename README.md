#why choose mybatis autocode factory

1，生成的model 实体上字段加上javadoc，为表字段的备注。

2，生成的model 部分数据类型需要修改，如数据库设计字段为tinyint，生成的java代码字段类型为byte
但是model的类型设计为Integer在编码中会更加方便。

3，生成的mapper，model代码继承公共的类实现公共的接口。如model重写toString(),clone()方法，mapper都继承公共的mapper通过泛型来传入具体对象。

4，生成的mapper和model分为2部分，一部分为基础类，一部分为扩展类。

6，每个人都用的自己的生成工具，生成的代码格式不一样，公司内很难代码结构风格化统一，完美解决此问题。
