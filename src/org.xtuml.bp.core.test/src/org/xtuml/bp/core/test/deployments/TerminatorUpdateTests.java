package org.xtuml.bp.core.test.deployments;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtuml.bp.core.Deployment_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.OrderedRunner;

@RunWith(OrderedRunner.class)
public class TerminatorUpdateTests extends BaseTest {

    private static final String[] PROJECT_NAMES = { "DeploymentsTests" };
    private static final String BASE_INT_FILE = "/DeploymentsDomains/masl/DeploymentsDomain1/DeploymentsDomain1.int";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        for (String project : PROJECT_NAMES) {
            loadProject(project);
        }
        modelRoot = Ooaofooa
                .getInstance("/DeploymentsTests/models/DeploymentsTests/DeploymentsTests/DeploymentsTests.xtuml");
        m_sys = modelRoot.getRoot();
    }

    @Test
    public void testUpdate1() throws Exception {

        // get the deployment
        Deployment_c deployment = Deployment_c.DeploymentInstance(modelRoot);
        assertNotNull(deployment);

        // trigger the import action
        deployment.Importfromfile(getTestModelRespositoryLocation() + BASE_INT_FILE);

    }

}
