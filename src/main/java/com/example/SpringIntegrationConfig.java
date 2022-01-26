package com.example;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;

@Configuration
@EnableIntegration
public class SpringIntegrationConfig {

	@Bean
	@InboundChannelAdapter(value = "fileInputChannel",poller=@Poller(fixedDelay="1000"))
	public FileReadingMessageSource fileReadingMessageSource() {
		//we can also filter particular file to read
		CompositeFileListFilter<File> filter=new CompositeFileListFilter<>();
		filter.addFilter(new SimplePatternFileListFilter("*.jpg"));
		FileReadingMessageSource reader = new FileReadingMessageSource();
		reader.setDirectory(new File("D:\\integration"));
		reader.setFilter(filter);
		return reader;
	}
	
	@Bean
	@ServiceActivator(inputChannel="fileInputChannel")//service activator checks input channel if found anything it will read
	public FileWritingMessageHandler fileWritingMessageHandler()
	{
		FileWritingMessageHandler writer=new FileWritingMessageHandler(new File("D:\\destination"));
		writer.setAutoCreateDirectory(true);
		writer.setExpectReply(false);
		return writer;
	}
}
