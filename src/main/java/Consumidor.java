import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;


public class Consumidor {
    private static final String TASK_QUEUE_NAME  = "task_queue";

    private static void doWork(String task) {
        for (char ch: task.toCharArray ()) {
            if (ch == '.') {
                try {
                    Thread.sleep (1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("mqadmin");
        connectionFactory.setPassword("Admin123XX_");

        Connection connection = connectionFactory.newConnection();
        Channel canal = connection.createChannel();

        canal.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        int prefetchCount = 1;
        canal.basicQos(prefetchCount);

        System.out.println("Consumidor executando...");
        DeliverCallback callback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println ("[x] Received '" + message + "'");
            try {
                doWork (message);
            } finally {
                System.out.println ("[x] Done");
                canal.basicAck(delivery.getEnvelope(). getDeliveryTag(), false);
            }
        };
        boolean autoAck = false;
        canal.basicConsume (TASK_QUEUE_NAME, autoAck, callback, consumerTag -> {});

    }
}


