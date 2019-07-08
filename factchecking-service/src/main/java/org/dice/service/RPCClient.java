package org.dice.service;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class RPCClient {

	private Connection connection;
	private Channel channel;
	private static final String EXCHANGE_NAME = "nameko-rpc";
	private String routingKey = "";
	private String replyQueueName;
	public static String RABBITMQ_HOSTNAME = System.getenv("RABBITMQ_HOSTNAME");
	private static final Logger LOGGER = Logger.getLogger(RPCClient.class.getName());

	/*
	 * Establishes new connection to RabbitMQ Server, creates a channel with Topic exchange,
	 * binds a reply queue to the created channel
	 */
	public RPCClient() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		
		if(RABBITMQ_HOSTNAME == null)
			factory.setHost("localhost");
		else
			factory.setHost(RABBITMQ_HOSTNAME);

		connection = factory.newConnection();
		channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
		replyQueueName = channel.queueDeclare().getQueue();

		channel.queueBind(replyQueueName, EXCHANGE_NAME, replyQueueName);        

	}

	/*
	 * Defines properties for the message being published,
	 * handles the response sent to the binded reply queue
	 */
	public String call(String message) throws IOException, TimeoutException, InterruptedException {
		final String corrId = UUID.randomUUID().toString();

		AMQP.BasicProperties props = new AMQP.BasicProperties
				.Builder()
				.correlationId(corrId)
				.contentType("application/json")
				.contentEncoding("utf-8")
				.replyTo(replyQueueName)
				.build();

		LOGGER.info("Triple sent to the queue!");
		channel.basicPublish(EXCHANGE_NAME, routingKey, props, message.getBytes("UTF-8"));


		final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

		LOGGER.info("Processing...");
		channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				if (properties.getCorrelationId().equals(corrId)) {
					response.offer(new String(body, "UTF-8"));
				}
			}
		});

		return response.poll(30000, TimeUnit.MILLISECONDS);
	}

	public void close() throws IOException {
		connection.close();
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
}
