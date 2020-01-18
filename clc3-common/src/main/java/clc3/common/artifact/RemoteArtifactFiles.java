package clc3.common.artifact;

import clc3.cosmos.entities.Project;
import org.apache.maven.index.artifact.Gav;
import org.apache.maven.index.artifact.M2GavCalculator;

import java.io.File;

public class RemoteArtifactFiles extends AbstractArtifactFiles {

    private static final String REPOSITORY_PATH = "https://repo1.maven.org/maven2";

    private static M2GavCalculator calculator = new M2GavCalculator();

    public RemoteArtifactFiles(Project project) {
        super(project);
    }

    @Override
    protected String buildBinariesJar(Project project) {
        Gav gav = new Gav(
            project.getGroupId(),
            project.getArtifactId(),
            project.getVersion(),
            null,
            "jar",
            null,
            null,
            null,
            false,
            null,
            false,
            null);

        return REPOSITORY_PATH + calculator.gavToPath(gav).replace(File.separatorChar, '/');
    }

    @Override
    protected String buildSourcesJar(Project project) {
        Gav gav = new Gav(
            project.getGroupId(),
            project.getArtifactId(),
            project.getVersion(),
            "sources",
            "jar",
            null,
            null,
            null,
            false,
            null,
            false,
            null);

        return REPOSITORY_PATH + calculator.gavToPath(gav).replace(File.separatorChar, '/');
    }
}
