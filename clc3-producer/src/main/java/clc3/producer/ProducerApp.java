package clc3.producer;

import akka.actor.ActorSystem;
import akka.stream.alpakka.azure.storagequeue.javadsl.AzureQueueSink;
import akka.stream.javadsl.Source;
import clc3.cosmos.CosmosConfiguration;
import clc3.cosmos.entities.Project;
import clc3.indexer.IndexerConfiguration;
import clc3.indexer.RepositoryIndexUpdater;
import clc3.indexer.RepositoryIndexer;
import clc3.queue.QueueConfiguration;
import clc3.queue.TaskQueue;
import com.google.gson.Gson;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueMessage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.UUID;

@SpringBootApplication
@Import({CosmosConfiguration.class, IndexerConfiguration.class, QueueConfiguration.class})
public class ProducerApp implements CommandLineRunner {

    private final RepositoryIndexUpdater updater;
    private final RepositoryIndexer indexer;
    private final CloudQueue queue;
    private final Gson gson;

    public ProducerApp(RepositoryIndexUpdater updater, RepositoryIndexer indexer, TaskQueue queue, Gson gson) {
        this.updater = updater;
        this.indexer = indexer;
        this.queue = queue.get();
        this.gson = gson;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProducerApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        this.updater.updateIndex();

        ActorSystem system = ActorSystem.create();

        Source.from(Arrays.asList(this.indexer.getArtifactInfos()))
            //.take(10)
            .map(artifactInfo -> {
                String id = UUID.randomUUID().toString();
                String name = artifactInfo.getUinfo();
                String groupId = artifactInfo.getGroupId();
                String artifactId = artifactInfo.getArtifactId();
                String version = artifactInfo.getVersion();

                return new Project(id, name, groupId, artifactId, version, null);
            })
            .map(project -> { System.out.println("Adding to Queue: " + project.getName()); return project; })
            .map(project -> new CloudQueueMessage(this.gson.toJson(project)))
            .runWith(AzureQueueSink.create(() -> this.queue), system);
    }
}
