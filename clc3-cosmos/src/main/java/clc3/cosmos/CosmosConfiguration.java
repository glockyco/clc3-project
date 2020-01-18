package clc3.cosmos;

import clc3.credentials.Credentials;
import com.azure.cosmos.*;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"clc3.cosmos"})
public class CosmosConfiguration {

    @Bean
    public ConnectionPolicy policy() {
        return ConnectionPolicy.getDefaultPolicy();
    }

    @Bean
    public CosmosClient cosmosClient(ConnectionPolicy policy) {
        return new CosmosClientBuilder()
            .setEndpoint(Credentials.COSMOS_DB_ENDPOINT)
            .setKey(Credentials.COSMOS_DB_KEY)
            .setConnectionPolicy(policy)
            .setConsistencyLevel(ConsistencyLevel.EVENTUAL)
            .buildClient();
    }

    @Bean
    @SneakyThrows
    public CosmosDatabase cosmosDatabase(CosmosClient client) {
        return client.createDatabaseIfNotExists(Credentials.COSMOS_DB_DATABASE).getDatabase();
    }
}
