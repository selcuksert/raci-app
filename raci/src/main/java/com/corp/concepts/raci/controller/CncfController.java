package com.corp.concepts.raci.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.corp.concepts.raci.model.CncfData;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/cncf")
public class CncfController {

	private WebClient webClient;

	public CncfController() {
		ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 10000)).build();
		webClient = WebClient.builder().exchangeStrategies(exchangeStrategies)
				.baseUrl("https://landscape.cncf.io/data.json")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
	}

	@GetMapping("/data")
	public Flux<CncfData> getCncfData() {
		return webClient.get().retrieve().bodyToFlux(CncfData.class)
				.sort((data1, data2) -> data1.getName().toUpperCase().compareTo(data2.getName().toUpperCase()));
	}
}
