package clc3.download;

import clc3.common.artifact.ArtifactFiles;
import clc3.common.artifact.ArtifactFilesFactory;
import clc3.cosmos.entities.Project;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

@Component
public class ArtifactDownloader {

    private final ArtifactFilesFactory factory;

    public ArtifactDownloader(ArtifactFilesFactory factory) {
        this.factory = factory;
    }

    public void downloadProject(Project project) {
        ArtifactFiles remoteFiles = this.factory.createRemote(project);
        ArtifactFiles localFiles = this.factory.createLocal(project);

        File binariesJar = new File(localFiles.getBinariesJar());
        File sourcesJar = new File(localFiles.getSourcesJar());

        binariesJar.getParentFile().mkdirs();
        sourcesJar.getParentFile().mkdirs();

        try {
            if (!binariesJar.exists()) {
                this.transfer(remoteFiles.getBinariesJar(), binariesJar.getPath());
            }
            if (!sourcesJar.exists()) {
                this.transfer(remoteFiles.getSourcesJar(), sourcesJar.getPath());
            }
        } catch (IOException e) {
            binariesJar.delete();
            sourcesJar.delete();

            throw new DownloadException(e);
        }
    }

    private void transfer(String source, String destination) throws IOException {
        URL url = new URL(source);

        try (ReadableByteChannel inputChannel = Channels.newChannel(url.openStream());
             FileOutputStream outputStream = new FileOutputStream(destination);
             FileChannel outputChannel = outputStream.getChannel()) {

            outputChannel.transferFrom(inputChannel, 0, Long.MAX_VALUE);
        }
    }
}
