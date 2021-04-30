package com.sport.training.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.codec.http.HttpResponseStatus;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class RestClientConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestClientConfig.class);
	
	@Value("${app.uri}")
	private String uri;
	
	@Bean
    public WebClient webClient() {
    	return  WebClient.builder()
    			.clientConnector(new ReactorClientHttpConnector(httpClient))
    			.baseUrl(uri)
    			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    			.filter(logRequest()) 
    			.build();
    }
    
    private HttpClient httpClient = HttpClient.create()
//            .tcpConfiguration(client ->
//                    client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
//                    .doOnConnected(conn -> conn
//                            .addHandlerLast(new ReadTimeoutHandler(10))
//                            .addHandlerLast(new WriteTimeoutHandler(10))))
            .followRedirect((req, res) -> {
       //         System.out.println(res.responseHeaders().get("Location"));
                return HttpResponseStatus.FOUND.equals(res.status());
            });
    
 // This method returns filter function which will log request data
    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
        	LOGGER.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> LOGGER.debug("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

}
