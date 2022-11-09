package dev.fischermatte.strapi.client.graphql;

import com.graphql_java_generator.client.GraphQLConfiguration;
import com.graphql_java_generator.exception.GraphQLRequestExecutionException;
import com.graphql_java_generator.exception.GraphQLRequestPreparationException;
import dev.fischermatte.strapi.client.graphql.api.util.QueryExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@SpringBootApplication
public class Application {

    @Value("${strapi.api.token}")
    private String strapiApiToken;


    public static void main(String[] args) throws GraphQLRequestPreparationException, GraphQLRequestExecutionException {
        var ctx = SpringApplication.run(Application.class, args);
        var queryExecutor = ctx.getBean(QueryExecutor.class);
        var objectResponse = queryExecutor.getEmailTemplatesResponseBuilder()
                .withQueryResponseDef("""
                        {
                            data {
                              id
                              attributes {
                                message
                                subject
                                email_type
                              }
                            }
                        }                        
                        """).build();

        var response = queryExecutor
                .emailTemplatesWithBindValues(objectResponse, null, null,null,null, null, null);
        log.debug(response.getData().get(0).toString());
    }


    @Bean
    public WebClient webClient(String graphqlEndpoint,
                               @Autowired(required = false) CodecCustomizer defaultCodecCustomizer,
                               @Autowired(required = false) @Qualifier("httpClient") HttpClient httpClient,
                               @Autowired(required = false) @Qualifier("serverOAuth2AuthorizedClientExchangeFilterFunction") ServerOAuth2AuthorizedClientExchangeFilterFunction serverOAuth2AuthorizedClientExchangeFilterFunction) {
        return GraphQLConfiguration.getWebClient(graphqlEndpoint, defaultCodecCustomizer,
                httpClient, serverOAuth2AuthorizedClientExchangeFilterFunction);
    }



    @Bean
    public HttpClient httpClient() {
        return HttpClient.create().wiretap(true).headers(entries -> entries.add("Authorization", "Bearer " + this.strapiApiToken));
    }


}
