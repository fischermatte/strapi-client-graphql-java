package dev.fischermatte.strapi.client.graphql;

import com.graphql_java_generator.exception.GraphQLRequestExecutionException;
import com.graphql_java_generator.exception.GraphQLRequestPreparationException;
import dev.fischermatte.strapi.client.graphql.api.util.QueryExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {


    public static void main(String[] args) throws GraphQLRequestPreparationException, GraphQLRequestExecutionException {
        var ctx = SpringApplication.run(Application.class, args);
        var queryExecutor = ctx.getBean(QueryExecutor.class);
        var objectResponse = queryExecutor.getEmailTemplatesResponseBuilder().withQueryResponseDef("").build();
        var response = queryExecutor.emailTemplatesWithBindValues(objectResponse, null, null,null,null, null, null);

    }


}
