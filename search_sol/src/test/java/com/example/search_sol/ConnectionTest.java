package com.example.search_sol;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ConnectionTest {

    @Autowired
    private RestClient restClient;

    @Test
    void elasticsearchShouldBeAvailable() throws IOException {
        Response response = restClient.performRequest(new Request("GET", "/"));
        assertEquals(200, response.getStatusLine().getStatusCode());
    }
}
