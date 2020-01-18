package clc3.cosmos.repositories;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

@Repository
public class MethodRepository {

    private CosmosContainer container;

    @SneakyThrows
    public MethodRepository(CosmosDatabase database) {
        this.container = database.createContainerIfNotExists("methods", "/name", 400).getContainer();
    }

    @SneakyThrows
    public void upsertItem(Object item) {
        this.container.upsertItem(item);
    }
}
