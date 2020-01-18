package clc3.unpacking;

import clc3.common.artifact.ArtifactFilesFactory;
import clc3.common.artifact.LocalArtifactFiles;
import clc3.cosmos.entities.Project;
import org.apache.commons.exec.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ArtifactUnpacker {

    private final ArtifactFilesFactory factory;

    public ArtifactUnpacker(ArtifactFilesFactory factory) {
        this.factory = factory;
    }

    public void unpackProject(Project project) {
        LocalArtifactFiles files = this.factory.createLocal(project);

        File binariesDirectory = new File(files.getBinariesDirectory());
        File sourcesDirectory = new File(files.getSourcesDirectory());

        binariesDirectory.mkdirs();
        sourcesDirectory.mkdirs();

        try {
            this.unpack(files.getBinariesJar(), files.getBinariesDirectory());
            this.unpack(files.getSourcesJar(), files.getSourcesDirectory());
        } catch (IOException | InterruptedException e) {
            binariesDirectory.delete();
            sourcesDirectory.delete();

            throw new UnpackingException(e);
        }
    }

    private void unpack(String source, String target) throws IOException, InterruptedException {
        if (OS.isFamilyWindows()) {
            this.unpackWindows(source, target);
        } else {
            this.unpackUnix(source, target);
        }
    }

    private void unpackWindows(String source, String target) throws IOException, InterruptedException {
        File sourceFile = new File(source);
        File targetDirectory = new File(target);

        targetDirectory.mkdirs();

        String cdTemplate = "cd %s";
        String jarTemplate = "jar xf %s";

        String cdCommand = String.format(cdTemplate, targetDirectory.getAbsolutePath());
        String jarCommand = String.format(jarTemplate, sourceFile.getAbsolutePath());

        String baseCommand = cdCommand + " & " + jarCommand;
        String command = "cmd.exe /c " + baseCommand;

        CommandLine cmd = CommandLine.parse(command);

        DefaultExecutor exec = new DefaultExecutor();
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        exec.execute(cmd, resultHandler);
        resultHandler.waitFor();

        if (resultHandler.getException() != null) {
            System.out.println(resultHandler.getException());
        }
    }

    private void unpackUnix(String source, String target) throws IOException, InterruptedException {
        File sourceFile = new File(source);
        File targetDirectory = new File(target);

        targetDirectory.mkdirs();

        String template = "unzip %s -d %s";
        String command = String.format(template, sourceFile.getAbsolutePath(), targetDirectory.getAbsolutePath());

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
