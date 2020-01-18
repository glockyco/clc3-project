package clc3.indexer;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.WildcardQuery;
import org.apache.maven.index.*;
import org.apache.maven.index.context.IndexingContext;
import org.apache.maven.index.search.grouping.GAGrouping;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RepositoryIndexer {

    private final Indexer indexer;
    private final Configuration configuration;

    public RepositoryIndexer(Indexer indexer, Configuration configuration) {
        this.indexer = indexer;
        this.configuration = configuration;
    }

    public ArtifactInfo[] getArtifactInfos() throws IOException {
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

        final WildcardQuery query = new WildcardQuery(new Term("u", "*|NA|jar"));
        final Grouping grouping = new GAGrouping();
        final GroupedSearchRequest request = new GroupedSearchRequest(query, grouping, context);

        final GroupedSearchResponse response = indexer.searchGrouped(request);

        final Map<String, ArtifactInfoGroup> results = response.getResults();

        ArtifactInfo[] artifactInfos = results.values().stream()
                // only include newest versions of the artifacts
                .map(group -> group.getArtifactInfos().iterator().next())
                // only include artifacts for which sources are available
                .filter(artifact -> artifact.getSourcesExists() == ArtifactAvailability.PRESENT)
                // TODO: include only "full" versions (no alpha/beta/rc etc.)
                .toArray(ArtifactInfo[]::new);

        this.indexer.closeIndexingContext(context, false);

        return artifactInfos;
    }
}
