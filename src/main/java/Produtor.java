import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Produtor {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("mqadmin");
        connectionFactory.setPassword("Admin123XX_");
        System.out.println("Produtor executando...");

        try (
                Connection connection = connectionFactory.newConnection();
                Channel canal = connection.createChannel();
        ) {
            String message = "Message from: Marcela Barreto de Oliveira Kramer (20221370019)";

            canal.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            canal.basicPublish("", TASK_QUEUE_NAME, false, false, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println ("[x] Sent '" + message + "'");
        }
    }
}


