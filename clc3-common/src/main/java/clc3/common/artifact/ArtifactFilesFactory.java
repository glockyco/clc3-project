package clc3.common.artifact;

import clc3.cosmos.entities.Project;
import org.springframework.stereotype.Component;

@Component
public class ArtifactFilesFactory {

    public LocalArtifactFiles createLocal(Project project) {
        return new LocalArtifactFiles(project);
    }

    public RemoteArtifactFiles createRemote(Project project) {
        return new RemoteArtifactFiles(project);
    }
}
