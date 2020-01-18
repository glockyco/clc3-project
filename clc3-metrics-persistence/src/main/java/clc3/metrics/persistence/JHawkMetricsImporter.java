package clc3.metrics.persistence;

import clc3.common.artifact.ArtifactFilesFactory;
import clc3.common.artifact.LocalArtifactFiles;
import clc3.cosmos.entities.*;
import clc3.cosmos.repositories.ClazzRepository;
import clc3.cosmos.repositories.MethodRepository;
import clc3.cosmos.repositories.PakkageRepository;
import clc3.cosmos.repositories.ProjectRepository;
import clc3.metrics.persistence.jhawk.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLFilterImpl;

import javax.xml.bind.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class JHawkMetricsImporter {

    private final ArtifactFilesFactory factory;

    private final ProjectRepository projectRepository;
    private final PakkageRepository pakkageRepository;
    private final ClazzRepository clazzRepository;
    private final MethodRepository methodRepository;

    public JHawkMetricsImporter(
        ArtifactFilesFactory factory,

        ProjectRepository projectRepository,
        PakkageRepository pakkageRepository,
        ClazzRepository clazzRepository,
        MethodRepository methodRepository) {

        this.factory = factory;

        this.projectRepository = projectRepository;
        this.pakkageRepository = pakkageRepository;
        this.clazzRepository = clazzRepository;
        this.methodRepository = methodRepository;
    }

    @SneakyThrows
    public void importMetrics(Project project) {
        SystemType system = this.unmarshallMetrics(project);
        this.importSystemMetrics(project, system);
    }

    private SystemType unmarshallMetrics(Project project) throws JAXBException, IOException, SAXException, ParserConfigurationException {
        LocalArtifactFiles files = this.factory.createLocal(project);

        File metricsXml = new File(files.getMetricsXml());

        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);

        // Create the XMLFilter
        XMLFilter filter = new NamespaceFilter("jhawk");

        // Set the parent XMLReader on the XMLFilter
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        filter.setParent(xr);

        // Set UnmarshallerHandler as ContentHandler on XMLFilter
        Unmarshaller unmarshaller = context.createUnmarshaller();
        UnmarshallerHandler unmarshallerHandler = unmarshaller.getUnmarshallerHandler();
        filter.setContentHandler(unmarshallerHandler);

        // Parse the XMLs
        InputSource xml = new InputSource(metricsXml.getAbsolutePath());
        filter.parse(xml);

        JAXBElement<?> element = (JAXBElement<?>) unmarshallerHandler.getResult();

        return (SystemType) element.getValue();
    }

    protected void importSystemMetrics(Project project, SystemType s) {
        LOCType metrics = s.getLOC();

        project.setMetrics(new ProjectMetrics(
            metrics.getBL().doubleValue(),
            metrics.getCI().doubleValue(),
            metrics.getCO().doubleValue(),
            metrics.getTL().doubleValue()
        ));

        this.projectRepository.upsertItem(project);

        for (PackageType p : s.getPackages().getPackage()) {
            this.importPackageMetrics(project, p);
        }
    }

    private void importPackageMetrics(Project project, PackageType p) {
        PackageMetricsType metrics = p.getMetrics();

        Pakkage pakkage = new Pakkage(UUID.randomUUID().toString(), p.getName(), project.getId(), new PakkageMetrics(
            metrics.getAbstractness(),
            metrics.getAvcc(),
            metrics.getCumulativeNumberOfCommentLines().doubleValue(),
            metrics.getCumulativeNumberOfComments().doubleValue(),
            metrics.getDistance(),
            metrics.getFanin().doubleValue(),
            metrics.getFanout().doubleValue(),
            metrics.getHalsteadCumulativeBugs(),
            metrics.getHalsteadCumulativeLength().doubleValue(),
            metrics.getHalsteadCumulativeVolume(),
            metrics.getHalsteadEffort(),
            metrics.getInstability(),
            metrics.getLoc().doubleValue(),
            metrics.getMaintainabilityIndex(),
            metrics.getMaintainabilityIndexNC(),
            metrics.getMaxcc().doubleValue(),
            metrics.getNumberOfClasses().doubleValue(),
            metrics.getNumberOfMethods().doubleValue(),
            metrics.getNumberOfStatements().doubleValue(),
            metrics.getRVF().doubleValue(),
            metrics.getTcc().doubleValue()
        ));

        this.pakkageRepository.upsertItem(pakkage);

        for (ClassType c : p.getClasses().getClazz()) {
            this.importClassMetrics(pakkage, c);
        }
    }

    private void importClassMetrics(Pakkage pakkage, ClassType c) {
        ClassMetricsType metrics = c.getMetrics();

        Clazz clazz = new Clazz(UUID.randomUUID().toString(), c.getClassName(), pakkage.getId(), new ClazzMetrics(
            metrics.getAvcc(),
            metrics.getCbo().doubleValue(),
            metrics.getCoh(),
            metrics.getCumulativeNumberOfCommentLines().doubleValue(),
            metrics.getCumulativeNumberOfComments().doubleValue(),
            metrics.getDit().doubleValue(),
            metrics.getFanIn().doubleValue(),
            metrics.getFanOut().doubleValue(),
            metrics.getHalsteadCumulativeBugs(),
            metrics.getHalsteadCumulativeLength().doubleValue(),
            metrics.getHalsteadCumulativeVolume(),
            metrics.getHalsteadEffort(),
            metrics.getLcom(),
            metrics.getLcom2(),
            metrics.getLoc().doubleValue(),
            metrics.getMaintainabilityIndex(),
            metrics.getMaintainabilityIndexNC(),
            metrics.getMaxcc().doubleValue(),
            metrics.getMessagePassingCoupling().doubleValue(),
            metrics.getNumberOfCommands().doubleValue(),
            metrics.getNumberOfMethods().doubleValue(),
            metrics.getNumberOfQueries().doubleValue(),
            metrics.getNumberOfStatements().doubleValue(),
            metrics.getNumberOfSubclasses().doubleValue(),
            metrics.getNumberOfSuperclasses().doubleValue(),
            metrics.getResponseForClass().doubleValue(),
            metrics.getReuseRatio(),
            metrics.getREVF(),
            metrics.getSix(),
            metrics.getSpecializationRatio(),
            metrics.getTcc().doubleValue(),
            metrics.getUnweightedClassSize().doubleValue()
        ));

        this.clazzRepository.upsertItem(clazz);

        for (MethodType m : c.getMethods().getMethod()) {
            this.importMethodMetrics(clazz, m);
        }
    }

    private void importMethodMetrics(Clazz clazz, MethodType m) {
        MethodMetricsType metrics = m.getMetrics();

        Method method = new Method(UUID.randomUUID().toString(), m.getName(), clazz.getId(), new MethodMetrics(
            metrics.getCyclomaticComplexity().doubleValue(),
            metrics.getHalsteadBugs(),
            metrics.getHalsteadDifficulty(),
            metrics.getHalsteadEffort(),
            metrics.getHalsteadLength().doubleValue(),
            metrics.getHalsteadVocabulary().doubleValue(),
            metrics.getHalsteadVolume(),
            metrics.getLoc().doubleValue(),
            metrics.getMaxDepthOfNesting().doubleValue(),
            metrics.getNumberOfArguments().doubleValue(),
            metrics.getNumberOfCasts().doubleValue(),
            metrics.getNumberOfCommentLines().doubleValue(),
            metrics.getNumberOfComments().doubleValue(),
            metrics.getNumberOfExpressions().doubleValue(),
            metrics.getNumberOfLoops().doubleValue(),
            metrics.getNumberOfOperands().doubleValue(),
            metrics.getNumberOfOperators().doubleValue(),
            metrics.getNumberOfStatements().doubleValue(),
            metrics.getNumberOfVariableDeclarations().doubleValue(),
            metrics.getNumberOfVariableReferences().doubleValue(),
            metrics.getTotalNesting().doubleValue()
        ));

        this.methodRepository.upsertItem(method);
    }
}

class NamespaceFilter extends XMLFilterImpl {

    private final String namespace;

    public NamespaceFilter(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(namespace, localName, qName);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        super.startElement(namespace, localName, qName, atts);
    }
}
