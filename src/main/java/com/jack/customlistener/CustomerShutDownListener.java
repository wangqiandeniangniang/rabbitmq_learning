package com.jack.customlistener;

import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 自定义关闭监听器
 * @author liangchen
 * @date 2021/6/4
 */
public class CustomerShutDownListener implements ShutdownListener {
    @Override
    public void shutdownCompleted(ShutdownSignalException cause) {
        //我们有可能在关闭时候释放一些其他资源什么
        //或者增加告警日志都是可以的
        System.out.println("CustomerShutDownListener:shutdownCompleted");
    }
}
