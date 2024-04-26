package com.cloudy.springazureeventgrid;

import com.azure.core.util.BinaryData;
import com.azure.messaging.eventgrid.EventGridEvent;
import com.azure.messaging.eventgrid.EventGridPublisherClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.function.Consumer;

@SpringBootApplication
public class SpringazureeventgridApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringazureeventgridApplication.class);
	public static final String EVENT_TYPE_NEW_ACC_CREATED = "NewAccountCreated";
	public static final String SUBJECT = "New Account";

	@Autowired
	EventGridPublisherClient<EventGridEvent> client;

	@Bean
	public Consumer<Message<String>> consume() {
		return message -> {
			List<EventGridEvent> eventData = EventGridEvent.fromString(message.getPayload());
			eventData.forEach(event -> {
				LOGGER.info("New event received: '{}'", event.getData());
			});
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringazureeventgridApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String str = "FirstName: Papa, LastName: Johns";
		EventGridEvent event = new EventGridEvent(SUBJECT, EVENT_TYPE_NEW_ACC_CREATED, BinaryData.fromObject(str), "0.1");

		client.sendEvent(event);
		LOGGER.info("New event published: '{}'", event.getData());
	}
}
