package clc3.cosmos.repositories;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepository {

    private CosmosContainer container;

    @SneakyThrows
    public ProjectRepository(CosmosDatabase database) {
        this.container = database.createContainerIfNotExists("projects", "/name", 400).getContainer();
    }

    @SneakyThrows
    public void upsertItem(Object item) {
        this.container.upsertItem(item);
    }
}
