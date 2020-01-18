package clc3.cosmos.repositories;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

@Repository
public class PakkageRepository {

    private CosmosContainer container;

    @SneakyThrows
    public PakkageRepository(CosmosDatabase database) {
        this.container = database.createContainerIfNotExists("pakkages", "/name", 400).getContainer();
    }

    @SneakyThrows
    public void upsertItem(Object item) {
        this.container.upsertItem(item);
    }
}
