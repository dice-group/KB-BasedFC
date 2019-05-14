import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class RPCClient {

	private Connection connection;
    private Channel channel;
    private static final String EXCHANGE_NAME = "nameko-rpc";
    private String routingKey = "kstream.stream";
    private String replyQueueName;

    public RPCClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
        replyQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(replyQueueName, EXCHANGE_NAME, replyQueueName);
    }

    public String call(String message) throws IOException, TimeoutException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .contentType("application/json")
                .contentEncoding("utf-8")
                .replyTo(replyQueueName)
                .build();

        System.out.println("Triple sent to the queue!");
        channel.basicPublish(EXCHANGE_NAME, routingKey, props, message.getBytes("UTF-8"));
        

        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        System.out.println("Processing...");
        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body, "UTF-8"));
                }
            }
        });

        return response.take();
    }
    
    public void close() throws IOException {
        connection.close();
    }
}