项目采用四层架构：
view            视图层
controller      控制层         接收前端的输入，并调用service层
service         业务层         处理业务逻辑，并调用dao层
dao             持久层         操作数据库 Mybatis


接口隔离原则：  扩展性
dao接口和dao的实现类
service接口和service的实现类


pojo    一张表对应的一个类
vo      显示在视图层
例子：数据库查出时间在页面的展示
db---->pojo(123456789)----->vo---->2018-10-17 21：22：30

