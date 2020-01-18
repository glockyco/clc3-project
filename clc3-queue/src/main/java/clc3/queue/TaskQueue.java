package clc3.queue;

import com.microsoft.azure.storage.queue.CloudQueue;
import org.springframework.stereotype.Component;

@Component
public class TaskQueue {

    private CloudQueue queue;

    public TaskQueue(CloudQueue queue) {
        this.queue = queue;
    }

    public CloudQueue get() {
        return this.queue;
    }
}
