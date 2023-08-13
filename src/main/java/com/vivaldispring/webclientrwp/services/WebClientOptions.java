package com.vivaldispring.webclientrwp.services;

import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class WebClientOptions {


    private static final Logger logger = LogManager.getLogger(WebClientOptions.class);
    final int size = 16 * 1024 * 1024;
    final ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
            .build();

    /**
     * The response timeout is the time we wait to receive a response after sending a request.
     * Set to 1 second
     * @return WebClient
     */
    public WebClient WebClientWithResponseTimeOut() {

        // The response timeout is the time we wait to receive a response after sending a request.
        HttpClient client = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(1));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(client))
                .exchangeStrategies(strategies)
                .build();


    }

    /**
     * The connection timeout is a period within which a connection between a client and a server must be established.
     *
     * @return WebClient
     */
    public WebClient WebClientWithConnectionTimeOut() {

        // The connection timeout is a period within which a connection between a client and a server must be established.
        HttpClient client2 = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(client2))
                .build();

    }

    /**
     * We can configure the keep-alive option, which will send TCP check probes when the connection is idle
     * @return WebClient
     */
    public WebClient WebClientWithConnectionIdle() {

        // we can configure the keep-alive option, which will send TCP check probes when the connection is idle
        HttpClient client3 = HttpClient.create()
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(EpollChannelOption.TCP_KEEPIDLE, 300)
                .option(EpollChannelOption.TCP_KEEPINTVL, 60)
                .option(EpollChannelOption.TCP_KEEPCNT, 8);


        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(client3))
                .build();


    }

    /**
     *
     * @return WebClient
     */
    public WebClient WebClientWithReadWriteTimeOut() {

        // create WebClient...

        HttpClient client4 = HttpClient.create()
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(10)));

        ClientHttpConnector connector = new ReactorClientHttpConnector(client4);

        return WebClient.builder()
                .baseUrl("https://countriesnow.space/api/v0.1/countriespopulation/cities")
                .clientConnector(connector)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();


    }
}
