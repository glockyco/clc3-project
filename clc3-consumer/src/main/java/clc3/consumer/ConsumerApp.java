package clc3.consumer;

import akka.actor.ActorSystem;
import akka.stream.alpakka.azure.storagequeue.javadsl.AzureQueueDeleteSink;
import akka.stream.alpakka.azure.storagequeue.javadsl.AzureQueueSource;
import akka.stream.javadsl.Sink;
import clc3.cosmos.entities.Project;
import clc3.download.ArtifactDownloader;
import clc3.metrics.calculation.JHawkMetricsCalculator;
import clc3.metrics.persistence.JHawkMetricsImporter;
import clc3.queue.TaskQueue;
import clc3.unpacking.ArtifactUnpacker;
import com.google.gson.Gson;
import com.microsoft.azure.storage.queue.CloudQueue;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;

@SpringBootApplication
public class ConsumerApp implements CommandLineRunner {

    private final ArtifactDownloader downloader;
    private final ArtifactUnpacker unpacker;
    private final JHawkMetricsCalculator calculator;
    private final JHawkMetricsImporter importer;
    private final CloudQueue queue;
    private final Gson gson;

    public ConsumerApp(
        ArtifactDownloader downloader,
        ArtifactUnpacker unpacker,
        JHawkMetricsCalculator calculator,
        JHawkMetricsImporter importer,
        TaskQueue queue,
        Gson gson
    ) {
        this.downloader = downloader;
        this.unpacker = unpacker;
        this.calculator = calculator;
        this.importer = importer;
        this.queue = queue.get();
        this.gson = gson;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args).close();
    }

    @Override
    public void run(String... args) throws Exception {
        ActorSystem system = ActorSystem.create();

        int count = 100;
        long start = System.currentTimeMillis();

        AzureQueueSource.create(() -> this.queue)
            .take(count)
            .map(message -> {
                Project project = this.gson.fromJson(message.getMessageContentAsString(), Project.class);

                try {
                    this.downloadProject(project);
                    this.unpackProject(project);
                    this.calculateMetricsOfProject(project);
                    this.persistMetricsOfProject(project);

                    System.out.println(Instant.now() + " Done: " + project.getName());
                } catch (Exception e) {
                    System.out.println(Instant.now() + " Error: " + project.getName());
                    e.printStackTrace();
                }

                return message;
            })
            .runWith(AzureQueueDeleteSink.create(() -> this.queue), system)
            .toCompletableFuture()
            .get();

        long end = System.currentTimeMillis();

        System.out.println(
                "Start: " + start
            + ", End: " + end
            + ", Delta: " + (end - start)
            + ", Projects: " + count
            + ", Per Project: " + ((end - start) / (float)count));

        System.exit(0);
    }

    private Project downloadProject(Project project) {
        System.out.println("Downloading: " + project.getName());
        this.downloader.downloadProject(project);
        return project;
    }

    private Project unpackProject(Project project) {
        System.out.println("Unpacking: " + project.getName());
        this.unpacker.unpackProject(project);
        return project;
    }

    private Project calculateMetricsOfProject(Project project) {
        System.out.println("Calculating Metrics: " + project.getName());
        this.calculator.calculateMetrics(project);
        return project;
    }

    private Project persistMetricsOfProject(Project project) {
        System.out.println("Persisting Metrics: " + project.getName());
        this.importer.importMetrics(project);
        return project;
    }
}
