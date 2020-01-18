package clc3.common.artifact;

import clc3.cosmos.entities.Project;

public abstract class AbstractArtifactFiles implements ArtifactFiles {

    private String binariesJar;
    private String sourcesJar;

    protected abstract String buildBinariesJar(Project project);
    protected abstract String buildSourcesJar(Project project);

    public AbstractArtifactFiles(Project project) {
        this.binariesJar = this.buildBinariesJar(project);
        this.sourcesJar = this.buildSourcesJar(project);
    }

    @Override
    public String getBinariesJar() {
        return this.binariesJar;
    }

    @Override
    public String getSourcesJar() {
        return this.sourcesJar;
    }

}
