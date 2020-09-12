package com.corp.concepts.raci.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.corp.concepts.raci.model.CncfData;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/cncf")
@Slf4j
public class CncfController {

	@Value("${custom.property.cncf.url}")
	private String cncfUrl;

	private WebClient webClient;

	@PostConstruct
	private void configureWebClient() {
		log.debug("CNCF URL: {}", cncfUrl);
		ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 10000)).build();
		webClient = WebClient.builder().baseUrl(cncfUrl).exchangeStrategies(exchangeStrategies)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
	}

	@GetMapping("/data")
	public Flux<CncfData> getCncfData() {
		return webClient.get().retrieve().bodyToFlux(CncfData.class).map(data -> {
			data.setLandscape(data.getLandscape().split("/")[1]);
			return data;
		}).sort((data1, data2) -> data1.getName().toUpperCase().compareTo(data2.getName().toUpperCase()));
	}
}
