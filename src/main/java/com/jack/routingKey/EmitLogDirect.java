package com.jack.routingKey;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel()){
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            String severity = getSeverity(args);
            String message = getMessage(args);
            channel.basicPublish(EXCHANGE_NAME, severity, null , message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] sent '" + message + "'");
        }
    }

    private static String getMessage(String[] args) {
        if (args.length < 2) {
            return "Hello World!";
        }
        return joinStrings(args, " ", 1);
    }


    private static String joinStrings(String[] args, String delimiter, int startIndex) {
        final int length = args.length;
        if (length == 0) return "";
        if (length <= startIndex) return "";
        final StringBuilder words = new StringBuilder(args[startIndex]);
        for (int i = startIndex + 1; i < length; i++) {
            words.append(delimiter).append(args[i]);
        }
        return words.toString();
    }

    private static String getSeverity(String[] args) {
        if (args.length < 1) {
            return "info";
        }
        return args[0];
    }
}
