package com.kafka.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.kafka.stream.binding.BindingConfig;
import com.kafka.stream.model.PageViewEvent;


@SpringBootApplication
@EnableBinding(BindingConfig.class)
public class KafkaSpringCloudStreamApplication {

		public static void main(String[] args) {
				SpringApplication.run(KafkaSpringCloudStreamApplication.class, args);
		}

		//this component will send the messages to the topic using a scheduler
		@Component
		public static class PageViewEventSource implements ApplicationRunner {

				private final MessageChannel pageViewsOut;
				private final Log log = LogFactory.getLog(getClass());

				public PageViewEventSource(BindingConfig binding) {
						this.pageViewsOut = binding.pageViewsOut();
				}

				@Override
				public void run(ApplicationArguments args) throws Exception {
						List<String> names = Arrays.asList("sachin", "ramesh", "sourav", "honai", "john");
						List<String> pages = Arrays.asList("blog", "sitemap", "initializr", "news", "about");
						Runnable runnable = () -> {
								String rPage = pages.get(new Random().nextInt(pages.size()));
								String rName = pages.get(new Random().nextInt(names.size()));
								//PageViewEvent pageViewEvent = new PageViewEvent(rName, rPage, Math.random() > .5 ? 10 : 1000);
								PageViewEvent pageViewEvent = new PageViewEvent();
								pageViewEvent.setPage(rPage);
								pageViewEvent.setUserId(rName);
								Message<PageViewEvent> message = MessageBuilder
									.withPayload(pageViewEvent)
									.setHeader(KafkaHeaders.MESSAGE_KEY, pageViewEvent.getUserId().getBytes())
									.build();
								try {
										this.pageViewsOut.send(message);
										log.info("message sent " + message.toString());
								}
								catch (Exception e) {
										log.error(e);
								}
						};
						Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable, 1, 1, TimeUnit.SECONDS);
				}
		}


		@Component
		public static class PageCountSink {

				private final Log log = LogFactory.getLog(getClass());

				@StreamListener
				public void pageCount(@Input((BindingConfig.PAGE_COUNT_IN)) KTable<String, Long> counts) {
						counts
							.toStream()
							.foreach((key, value) -> log.info(key + "=" + value));
				}
		}

}
	
	