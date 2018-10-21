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


md5+salt(盐值)

递归查询
    id    名称       parentid
    1    电子产品      0
    2    手机          1
    3     家电         1
    4     彩电         3
    5     冰箱         3
    6     小米手机     2
    7     小米8        6
    8      mi8-32G     7
    查询电子产品的所有类别?categoryId=1
    step1:查询出点子产品类
    step2: 查询电子产品的子类 List<categoryid> 2,3
    step3:查询categorid=2类别的子类   List   6
    step4:查询categoryid=3类别的子类  List 4,5

