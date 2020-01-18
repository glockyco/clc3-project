package clc3.queue;

import clc3.credentials.Credentials;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.StorageCredentialsAccountAndKey;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"clc3.queue"})
public class QueueConfiguration {

    @Bean
    @SneakyThrows
    public CloudStorageAccount cloudStorageAccount() {
        StorageCredentials credentials = new StorageCredentialsAccountAndKey(Credentials.STORAGE_ACCOUNT, Credentials.STORAGE_KEY);
        return new CloudStorageAccount(credentials);
    }

    @Bean
    public CloudQueueClient cloudQueueClient(CloudStorageAccount account) {
        return account.createCloudQueueClient();
    }

    @Bean
    @SneakyThrows
    public CloudQueue cloudQueue(CloudQueueClient client) {
        CloudQueue queue = client.getQueueReference("tasks");
        queue.createIfNotExists();
        return queue;
    }
}
