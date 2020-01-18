package clc3.metrics.calculation;

import clc3.common.artifact.ArtifactFilesFactory;
import clc3.common.artifact.LocalArtifactFiles;
import clc3.cosmos.entities.Project;
import lombok.SneakyThrows;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class JHawkMetricsCalculator {

    private final ArtifactFilesFactory factory;

    private ProcessBuilder processBuilder = new ProcessBuilder();

    public JHawkMetricsCalculator(ArtifactFilesFactory factory) {
        this.factory = factory;
    }

    @SneakyThrows
    public void calculateMetrics(Project project) {
        LocalArtifactFiles files = this.factory.createLocal(project);
        new File(files.getMetricsXml()).getParentFile().mkdirs();
        this.calculate(files.getSourcesDirectory(), files.getMetricsXml());
    }

    private void calculate(String source, String target) throws IOException, InterruptedException {
        File sourceDirectory = new File(source);
        File targetFile = new File(target);

        String template = "java -jar jhawk-command-line/JHawkCommandLine.jar -p jhawk-command-line/jhawk.properties -f .*\\.java -r -l pcm -s %s -x %s";
        String command = String.format(template, sourceDirectory.getAbsolutePath(), targetFile.getAbsolutePath().replaceAll("\\.xml$", ""));

        CommandLine cmd = CommandLine.parse(command);

        DefaultExecutor exec = new DefaultExecutor();
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        exec.execute(cmd, resultHandler);
        resultHandler.waitFor();

        if (resultHandler.getException() != null) {
            System.out.println(resultHandler.getException());
        }
    }
}
