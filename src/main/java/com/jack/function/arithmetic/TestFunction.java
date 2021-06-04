package com.jack.function.arithmetic;

/**
 * @author liangchen
 * @date 2021/6/4
 */
public class TestFunction {
    public static void main(String[] args) {
        // 具体我们定义支持cash,coupon支付
        PayConfig build = PayConfigs.builder().useCash().useCoupon().build();
        SupportPayMethod supportPayMethod = new SupportPayMethod();
        build.execute(supportPayMethod);
        System.out.println(supportPayMethod);
    }
}
