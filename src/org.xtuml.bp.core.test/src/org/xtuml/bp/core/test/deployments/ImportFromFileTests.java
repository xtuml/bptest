package org.xtuml.bp.core.test.deployments;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtuml.bp.core.DataType_c;
import org.xtuml.bp.core.Deployment_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.TerminatorServiceParameter_c;
import org.xtuml.bp.core.TerminatorService_c;
import org.xtuml.bp.core.Terminator_c;
import org.xtuml.bp.core.ui.Selection;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.OrderedRunner;

@RunWith(OrderedRunner.class)
public class ImportFromFileTests extends BaseTest {

    private static final String[] PROJECT_NAMES = { "DeploymentsTests" };
    private static final String MOD_FILE = "/DeploymentsDomains/masl/DeploymentsDomain1/DeploymentsDomain1.mod";
    private static final String INT_FILE = "/DeploymentsDomains/masl/DeploymentsDomain1/DeploymentsDomain1.int";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        for (String project : PROJECT_NAMES) {
            loadProject(project);
        }
        modelRoot = Ooaofooa
                .getInstance("/DeploymentsTests/models/DeploymentsTests/DeploymentsTests/DeploymentsTests.xtuml");
        m_sys = modelRoot.getRoot();
        setupExecutables();
    }

    @Test
    public void testImportFromIntFile() throws Exception {
        testImportFromFile(INT_FILE);
    }

    @Test
    public void testImportFromModFile() throws Exception {
        testImportFromFile(MOD_FILE);
    }

    private void testImportFromFile(String filename) throws Exception {

        // get the deployment
        Deployment_c deployment = Deployment_c.DeploymentInstance(modelRoot);
        assertNotNull(deployment);

        // check that the file exists
        File testFile = new File(getTestModelRespositoryLocation() + filename);
        assertTrue("Cannot access test file.", testFile.exists());
        
        // trigger the import action
        Selection.getInstance().clear();
        Selection.getInstance().addToSelection(deployment);
        ImportTerminatorsFromFileOnD_DEPLAction action = new ImportTerminatorsFromFileOnD_DEPLAction(
                Arrays.asList(new String[] { getTestModelRespositoryLocation() + filename }).iterator());
        action.run(null);

        // check the resulting instances
        Terminator_c providedTerm = Terminator_c.getOneD_TERMOnR1650(deployment,
                (selected) -> "DeploymentsDomain1".equals(((Terminator_c) selected).getName()));
        assertTrue("Provided terminator missing.", null != providedTerm && providedTerm.getProvider());
        TerminatorService_c provSvc = TerminatorService_c.getOneD_TSVCOnR1651(providedTerm,
                (selected) -> "public_service1".equals(((TerminatorService_c) selected).getName()));
        assertNotNull("Provided terminator service missing.", provSvc);
        TerminatorServiceParameter_c provSvcParam = TerminatorServiceParameter_c.getOneD_TSPARMOnR1652(provSvc,
                (selected) -> "real_param".equals(((TerminatorServiceParameter_c) selected).getName()));
        assertNotNull("Provided terminator service parameter missing.", provSvcParam);
        DataType_c s_dt = DataType_c.getOneS_DTOnR1653(provSvcParam);
        assertTrue("Incorrect parameter type.", null != s_dt && "real".equals(s_dt.getName()));
        Terminator_c requiredTerm = Terminator_c.getOneD_TERMOnR1650(deployment,
                (selected) -> "DeploymentsDomain1::term1".equals(((Terminator_c) selected).getName()));
        assertTrue("Required terminator missing.", null != requiredTerm && !requiredTerm.getProvider());
        TerminatorService_c reqSvc = TerminatorService_c.getOneD_TSVCOnR1651(requiredTerm,
                (selected) -> "term_service1".equals(((TerminatorService_c) selected).getName()));
        assertNotNull("Required terminator service missing.", reqSvc);
        TerminatorServiceParameter_c reqSvcParam = TerminatorServiceParameter_c.getOneD_TSPARMOnR1652(reqSvc,
                (selected) -> "enum_param".equals(((TerminatorServiceParameter_c) selected).getName()));
        assertNotNull("Required terminator service parameter missing.", reqSvcParam);
        s_dt = DataType_c.getOneS_DTOnR1653(reqSvcParam);
        assertTrue("Incorrect parameter type.", null != s_dt && "DeploymentsDomain1::MyEnum".equals(s_dt.getName()));

    }

    private void setupExecutables() throws Exception {
        final File mcDir = new File(System.getProperty("eclipse.home.location").replaceFirst("file:", "") + "/tools/mc");
        if (!mcDir.exists()) {
            assertTrue("Could not create necessary directories.", mcDir.mkdirs());
        }
        final File binDir = new File(mcDir, "bin");
        if (!binDir.exists()) {
            final String mcRepoPath = System.getenv("XTUML_DEVELOPMENT_REPOSITORY") + File.separator + "../mc";
            assertTrue("Cannot find mc repository.", !"".equals(mcRepoPath));
            File mcBinDir = new File(mcRepoPath + File.separator + "bin");
            assertTrue("Cannot find mc bin directory.", mcBinDir.exists());
            File antlrJar = new File(mcBinDir, "antlr-3.5.2-complete.jar");
            URL antlrURL = new URL("http://central.maven.org/maven2/org/antlr/antlr-complete/3.5.2/antlr-complete-3.5.2.jar");
            ReadableByteChannel rbc = Channels.newChannel(antlrURL.openStream());
            FileOutputStream fos = new FileOutputStream(antlrJar);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            Files.createSymbolicLink(binDir.toPath(), mcBinDir.toPath());
        }
    }

}
