package clc3.common.artifact;

import clc3.cosmos.entities.Project;

public class LocalArtifactFiles extends AbstractArtifactFiles {

    private static final String JARS_DIRECTORY = "target/project-jars/";
    private static final String FILES_DIRECTORY = "target/project-files/";
    private static final String METRICS_DIRECTORY = "target/project-metrics/";

    private String binariesDirectory;
    private String sourcesDirectory;

    private String metricsXml;

    public LocalArtifactFiles(Project project) {
        super(project);

        this.binariesDirectory = this.buildBinariesDirectory(project);
        this.sourcesDirectory = this.buildSourcesDirectory(project);

        this.metricsXml = this.buildMetricsXml(project);
    }

    public String getBinariesDirectory() {
        return this.binariesDirectory;
    }

    public String getSourcesDirectory() {
        return this.sourcesDirectory;
    }

    public String getMetricsXml() {
        return this.metricsXml;
    }

    @Override
    protected String buildBinariesJar(Project project) {
        return JARS_DIRECTORY + "binaries/" + this.buildName(project) + ".jar";
    }

    @Override
    protected String buildSourcesJar(Project project) {
        return JARS_DIRECTORY + "sources/" + this.buildName(project) + ".jar";
    }

    private String buildBinariesDirectory(Project project) {
        return FILES_DIRECTORY + "binaries/" + this.buildName(project) + "/";
    }

    private String buildSourcesDirectory(Project project) {
        return FILES_DIRECTORY + "sources/" + this.buildName(project) + "/";
    }

    private String buildMetricsXml(Project project) {
        return METRICS_DIRECTORY + this.buildName(project) + ".xml";
    }

    private String buildName(Project project) {
        return "__" + project.getGroupId() + "__" + project.getArtifactId() + "__" + project.getVersion() + "__";
    }
}
