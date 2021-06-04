package com.jack.function.arithmetic;


/**
 * 是否支持一些支付方式
 * @author liangchen
 * @date 2021/6/4
 */
public abstract class PayConfigs {

    // 使用优惠券
    public static final PayConfig USE_COUPON = pay ->  pay.setUseCoupon(true);

    // 使用现金
    public static final PayConfig USE_CASH = pay -> pay.setCash(true);

    // 使用微信
    public static final PayConfig USE_WEIXIN = pay -> pay.setWeixin(true);



    public static Builder  builder(){
       return new Builder();
    }

    public static class Builder {

        // 定义空
        private PayConfig configurator = pay -> {

        };


        public Builder useCoupon(){
            configurator = configurator.andThen(USE_COUPON);
            return this;
        }


        public Builder useCash(){
            configurator = configurator.andThen(USE_CASH);
            return this;
        }

        public Builder useWeixin(){
            configurator = configurator.andThen(USE_WEIXIN);
            return this;
        }

        public PayConfig build() {
            return configurator;
        }
    }

}
