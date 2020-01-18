package clc3.indexer;

import org.apache.maven.index.Indexer;
import org.apache.maven.index.context.IndexingContext;
import org.apache.maven.index.updater.*;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.events.TransferEvent;
import org.apache.maven.wagon.events.TransferListener;
import org.apache.maven.wagon.observers.AbstractTransferListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class RepositoryIndexUpdater {

    private final Indexer indexer;
    private final IndexUpdater indexUpdater;
    private final Configuration configuration;
    private final Wagon wagon;

    public RepositoryIndexUpdater(
        Indexer indexer,
        IndexUpdater indexUpdater,
        Configuration configuration,
        Wagon wagon
    ) {
        this.indexer = indexer;
        this.indexUpdater = indexUpdater;
        this.configuration = configuration;
        this.wagon = wagon;
    }

    public void updateIndex() throws IOException {
        final IndexingContext context = indexer.createIndexingContext(
                this.configuration.getId(),
                this.configuration.getRepositoryId(),
                this.configuration.getRepository(),
                this.configuration.getIndexDirectory(),
                this.configuration.getRepositoryUrl(),
                this.configuration.getIndexUpdateUrl(),
                this.configuration.getSearchable(),
                this.configuration.getReclaim(),
                this.configuration.getIndexers());

        System.out.println("Updating Index...");
        System.out.println("This might take a while on first run, so please be patient!");

        TransferListener listener = new AbstractTransferListener() {
            public void transferStarted(TransferEvent transferEvent) {
                System.out.print("  Downloading " + transferEvent.getResource().getName());
            }

            public void transferProgress(TransferEvent transferEvent, byte[] buffer, int length) {
            }

            public void transferCompleted(TransferEvent transferEvent) {
                System.out.println(" - Done");
            }
        };

        Date centralContextCurrentTimestamp = context.getTimestamp();

        ResourceFetcher resourceFetcher = new WagonHelper.WagonFetcher(this.wagon, listener, null, null);
        IndexUpdateRequest updateRequest = new IndexUpdateRequest(context, resourceFetcher);
        IndexUpdateResult updateResult = indexUpdater.fetchAndUpdateIndex(updateRequest);

        this.indexer.closeIndexingContext(context, false);

        if (updateResult.isFullUpdate()) {
            System.out.println("Full update happened!");
        } else if (updateResult.getTimestamp().equals(centralContextCurrentTimestamp)) {
            System.out.println("No update needed, index is up to date!");
        } else {
            System.out.println("Incremental update happened, change covered " + centralContextCurrentTimestamp + " - " + updateResult.getTimestamp() + " period.");
        }

        System.out.println();
    }

}
