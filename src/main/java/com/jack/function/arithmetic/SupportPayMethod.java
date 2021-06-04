package com.jack.function.arithmetic;

/**
 * @author liangchen
 * @date 2021/6/4
 */
public class SupportPayMethod {

    private boolean useCoupon;

    private boolean cash;

    private boolean weixin;

    public boolean isUseCoupon() {
        return useCoupon;
    }

    public void setUseCoupon(boolean useCoupon) {
        this.useCoupon = useCoupon;
    }

    public boolean isCash() {
        return cash;
    }

    public void setCash(boolean cash) {
        this.cash = cash;
    }

    public boolean isWeixin() {
        return weixin;
    }

    public void setWeixin(boolean weixin) {
        this.weixin = weixin;
    }

    @Override
    public String toString() {
        return "SupportPayMethod{" +
                "useCoupon=" + useCoupon +
                ", cash=" + cash +
                ", weixin=" + weixin +
                '}';
    }
}
