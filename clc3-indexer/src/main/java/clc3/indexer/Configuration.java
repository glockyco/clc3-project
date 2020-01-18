package clc3.indexer;

import org.apache.maven.index.context.IndexCreator;
import org.apache.maven.index.creator.JarFileContentsIndexCreator;
import org.apache.maven.index.creator.MavenPluginArtifactInfoIndexCreator;
import org.apache.maven.index.creator.MinimalArtifactInfoIndexCreator;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class Configuration {

    private final String id = "central-context";

    private final String repositoryId = "central";

    private final File repository = new File("target/central-cache");

    private final File indexDirectory = new File("target/central-index");

    private final String repositoryUrl = "https://repo1.maven.org/maven2";

    private final String indexUpdateUrl = null;

    private final boolean searchable = true;

    private final boolean reclaim = true;

    private final List<IndexCreator> indexers = new ArrayList<IndexCreator>() {
        {
            add(new MinimalArtifactInfoIndexCreator());
            add(new JarFileContentsIndexCreator());
            add(new MavenPluginArtifactInfoIndexCreator());
        }
    };

    public Configuration() {
    }

    public String getId() {
        return id;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public File getRepository() {
        return repository;
    }

    public File getIndexDirectory() {
        return indexDirectory;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public String getIndexUpdateUrl() {
        return indexUpdateUrl;
    }

    public boolean getSearchable() {
        return this.searchable;
    }

    public boolean getReclaim() {
        return this.reclaim;
    }

    public List<IndexCreator> getIndexers() {
        return indexers;
    }

}
