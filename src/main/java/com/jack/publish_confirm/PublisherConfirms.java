package com.jack.publish_confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BooleanSupplier;

public class PublisherConfirms {

    static final int MESSAGE_COUNT = 50_000;
    static Connection createConnection()throws  Exception{
        final ConnectionFactory cf = new ConnectionFactory();
        cf.setHost("localhost");
        cf.setUsername("guest");
        cf.setPassword("guest");
        return cf.newConnection();
    }

    public static void main(String[] args) throws Exception {
        publishMessagesIndividually();
        publicMessagesInBatch();
        handlePublishConfirmsAsynchronously();
    }

    private static void handlePublishConfirmsAsynchronously() throws Exception {
        try (Connection connection = createConnection()) {
            Channel ch = connection.createChannel();

            String queue = UUID.randomUUID().toString();
            ch.queueDeclare(queue, false, false, true, null);

            ch.confirmSelect();

            final ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

            ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple)->{
                if(multiple){
                    final ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(sequenceNumber, true);
                    confirmed.clear();
                }else {
                    outstandingConfirms.remove(sequenceNumber);
                }
            };
            ch.addConfirmListener(cleanOutstandingConfirms, (sequenceNumber, multiple) -> {
                final String body = outstandingConfirms.get(sequenceNumber);
                System.err.format(
                        "Message with body %s has been nack-ed. Sequence number: %d, multiple: %b%n",
                        body, sequenceNumber, multiple
                );
                cleanOutstandingConfirms.handle(sequenceNumber, multiple);
            });
            long start = System.nanoTime();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String body = String.valueOf(i);
                outstandingConfirms.put(ch.getNextPublishSeqNo(), body);
                ch.basicPublish("", queue, null, body.getBytes());
            }

            if (!waitUntil(Duration.ofSeconds(60), () -> outstandingConfirms.isEmpty())) {
                throw new IllegalStateException("All messages could not be confirmed in 60 seconds");
            }

            long end = System.nanoTime();
            System.out.format("Published %,d messages and handled confirms asynchronously in %,d ms%n", MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());

        }
    }

    private static void publicMessagesInBatch() throws Exception {
        try (Connection connection = createConnection()) {
            final Channel channel = connection.createChannel();
            String queue = UUID.randomUUID().toString();
            channel.queueDeclare(queue,false, false, true, null);
            channel.confirmSelect();

            int batchSize = 100;
            int outstandingMessageCount = 0;
            long start = System.nanoTime();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                final String body = String.valueOf(i);
                channel.basicPublish("", queue, null, body.getBytes(StandardCharsets.UTF_8));
                outstandingMessageCount ++ ;
                if (outstandingMessageCount == batchSize) {
                    channel.waitForConfirmsOrDie(5_000);
                    outstandingMessageCount = 0;
                }
            }
            if (outstandingMessageCount > 0) {
                channel.waitForConfirmsOrDie(5_000);
            }
            long end = System.nanoTime();
            System.out.format("Published %,d messages in batch in %,d ms%n", MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());

        }
    }

    private static void publishMessagesIndividually() throws Exception {
        try(Connection connection = createConnection()){
            final Channel channel = connection.createChannel();
            String queue = UUID.randomUUID().toString();
            channel.queueDeclare(queue,false, false, true, null);
            channel.confirmSelect();
            final long start = System.nanoTime();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                final String body = String.valueOf(i);
                channel.basicPublish("", queue, null , body.getBytes(StandardCharsets.UTF_8));
                channel.waitForConfirmsOrDie(5_000);
            }
            long end = System.nanoTime();
            System.out.format("Published %,d messages individually in %,d ms%n", MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
        }

    }
    static boolean waitUntil(Duration timeout, BooleanSupplier condition) throws InterruptedException {
        int waited = 0;
        while (!condition.getAsBoolean() && waited < timeout.toMillis()) {
            Thread.sleep(100L);
            waited = +100;
        }
        return condition.getAsBoolean();
    }
}
