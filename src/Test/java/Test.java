import com.neuedu.utils.BigDecimalUtil;

import java.math.BigDecimal;

public class Test {
    public static void main(String[]args){

        //System.out.println(0.05+0.01);

        BigDecimal bigDecimal1 = new BigDecimal("0.02");//一定调用字符串类型的构造函数
        BigDecimal bigDecimal2 = new BigDecimal("0.02");//一定调用字符串类型的构造函数
        bigDecimal1 = bigDecimal1.add(bigDecimal2);
        System.out.println(bigDecimal1);

        //BigDecimal bigDecimal = BigDecimalUtil.add(0.5,0.3);
        BigDecimal bigDecimal = BigDecimalUtil.divi(10.0,3.0);
        System.out.println(bigDecimal);

    }


}
