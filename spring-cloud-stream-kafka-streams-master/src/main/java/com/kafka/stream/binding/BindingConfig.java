package com.kafka.stream.binding;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import com.kafka.stream.model.PageViewEvent;

/**
 * 
 * Binding class which is specify the outbound and inbound channel to where messages should be published and consumed
 *
 */
public interface BindingConfig {
	
	String PAGE_VIEWS_OUT = "pvout";
	String PAGE_VIEWS_IN = "pvin";
	String PAGE_COUNT_MV = "pcmv";
	String PAGE_COUNT_OUT = "pcout";
	String PAGE_COUNT_IN = "pcin";

	// page views
	@Input(PAGE_VIEWS_IN)
	KStream<String, PageViewEvent> pageViewsIn();

	@Output(PAGE_VIEWS_OUT)
	MessageChannel pageViewsOut();

	// page cocunts
	@Output(PAGE_COUNT_OUT)
	KStream<String, Long> pageCountOut();

	@Input(PAGE_COUNT_IN)
	KTable<String, Long> pageCountIn();

}
