package com.example.gateway;

import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GatewayApplicationTests {

	@LocalServerPort
	private int port;
	private WebTestClient client;

	@Before
	public void setup() {
		client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
	}

	@Test
	public void rateLimiterTest() {
		WebTestClient authClient = client.mutate()
				.filter(ExchangeFilterFunctions.basicAuthentication("lillard", "lillard666"))
				.build();

		IntStream.range(0, 100).parallel().forEach(it -> {
			FluxExchangeResult<String> res = authClient.get().uri("/rl/customers").exchange().returnResult(String.class);
			System.out.println(String.format("status code: %s, body: %s.", res.getStatus(), res.getResponseBody().blockFirst()));
		});
	}

}
