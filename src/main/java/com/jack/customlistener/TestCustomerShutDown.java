package com.jack.customlistener;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author liangchen
 * @date 2021/6/3
 */
public class TestCustomerShutDown {

    private final static String V_HOST = "test_host";


    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost(V_HOST);
        Connection connection = factory.newConnection();
        connection.addShutdownListener(new CustomerShutDownListener());
        System.out.println("before close connection.isOpen() is " +   connection.isOpen());
        connection.close();
        System.out.println("after close connection.isOpen() is " +   connection.isOpen());


    }
}
