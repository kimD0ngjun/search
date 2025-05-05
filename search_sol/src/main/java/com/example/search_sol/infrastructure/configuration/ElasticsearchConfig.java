package com.example.search_sol.infrastructure.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

//    @Value("${elasticsearch.host}")
//    private String host;
//
//    @Value("${elasticsearch.port}")
//    private Integer port;
//
//    @Value("${elasticsearch.username}")
//    private String username;
//
//    @Value("${elasticsearch.password}")
//    private String password;
//
//    @Value("${elasticsearch.encodedApiKey}")
//    private String encodedApiKey;
//
//    @Value("${elasticsearch.fingerprint}")
//    private String fingerprint;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // 나중에 https 및 비밀번호 설정 대비해서 참조하기
        // https://velog.io/@pooh6195/Elasticsearch%EC%97%90-Java-Low-Level-REST-Client%EB%A5%BC-%ED%86%B5%ED%95%B4-%EC%9A%94%EC%B2%AD-%EB%B3%B4%EB%82%B4%EA%B8%B0
        // https://velog.io/@pooh6195/Elasticsearch%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%9E%90%EB%8F%99-%EC%99%84%EC%84%B1-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%A8-%EB%A7%8C%EB%93%A4%EA%B8%B0-Elasticsearch-java-low-level-client-with-Spring

//        CredentialsProvider credentials = new BasicCredentialsProvider();
//        // 엘라스틱 서치 username(elastic), password
//        credentials.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
//        RestClient build = RestClient.builder(new HttpHost(host, port, "https"))
//                .setDefaultHeaders(new Header[]{
//                        new BasicHeader("Authorization", "ApiKey " + encodedApiKey)    // 헤더에 API Key 추가
//                })
//                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder
//                        .setSSLContext(TransportUtils.sslContextFromCaFingerprint(fingerprint)) // 인증서 추가
//                        .setDefaultCredentialsProvider(credentials)    // credential 추가
//                )
//                .build();

        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http")
        ).build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper()
        );

        return new ElasticsearchClient(transport);
    }
}
