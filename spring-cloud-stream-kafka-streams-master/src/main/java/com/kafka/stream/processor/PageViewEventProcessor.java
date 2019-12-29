package com.kafka.stream.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.kafka.stream.binding.BindingConfig;
import com.kafka.stream.model.PageViewEvent;

/**
 * 
 * Class which will receive kafka streams from the channel
 * this is a listener which will continuously receive all the messages published to that channel
 *
 */
@Component
public class PageViewEventProcessor {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@StreamListener
	@SendTo(BindingConfig.PAGE_COUNT_OUT)
	public KStream<String, Long> process(@Input(BindingConfig.PAGE_VIEWS_IN) KStream<String, PageViewEvent> events) {
		
		log.info("Received " + events);
		log.info("Received " + events.toString());
		KStream<String, Long> count=	 events
				.filter((key, value) -> value.getDuration() > 10)
				.map((key, value) -> new KeyValue<>(value.getPage(), "0"))
				.groupByKey()
				.count(Materialized.as(BindingConfig.PAGE_COUNT_MV))
				.toStream();
		
		events.foreach((k,v)->System.out.println("key : " + k + " value : " + v));		
		log.info("Received count" + count);
		return count;
	}

}
