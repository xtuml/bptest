package org.xtuml.bp.core.test.deployments;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.service.prefs.Preferences;
import org.xtuml.bp.core.Component_c;
import org.xtuml.bp.core.DataType_c;
import org.xtuml.bp.core.Deployment_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.TerminatorServiceParameter_c;
import org.xtuml.bp.core.TerminatorService_c;
import org.xtuml.bp.core.Terminator_c;
import org.xtuml.bp.core.common.PersistenceManager;
import org.xtuml.bp.core.ui.Selection;
import org.xtuml.bp.core.ui.actions.ImportTerminatorsFromComponentOnD_DEPLAction;
import org.xtuml.bp.core.ui.dialogs.ElementSelectionDialog;
import org.xtuml.bp.core.ui.preferences.BridgePointProjectPreferences;
import org.xtuml.bp.core.ui.preferences.BridgePointProjectReferencesPreferences;
import org.xtuml.bp.test.TestUtil;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.FailableRunnable;
import org.xtuml.bp.test.common.OrderedRunner;

@RunWith(OrderedRunner.class)
public class ImportFromComponentTests extends BaseTest {

    private static final String[] PROJECT_NAMES = { "DeploymentsTests", "DeploymentsDomains" };

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
    public void testImportFromComponent() throws Exception {

        // get the deployment
        Deployment_c deployment = Deployment_c.DeploymentInstance(modelRoot);
        assertNotNull(deployment);

        // enable IPRs
        Preferences projectPrefs = new ProjectScope((IProject) m_sys.getAdapter(IProject.class))
                .getNode(BridgePointProjectPreferences.BP_PROJECT_PREFERENCES_ID);
        projectPrefs.putBoolean(BridgePointProjectReferencesPreferences.BP_PROJECT_REFERENCES_ID, true);

        // trigger the import action
        Selection.getInstance().clear();
        Selection.getInstance().addToSelection(deployment);
        Shell[] existingShells = PlatformUI.getWorkbench().getDisplay().getShells();
        FailableRunnable runnable = TestUtil.chooseItemInDialog(300, "DeploymentsDomain1", existingShells);
        TestUtil.okElementSelectionDialog(runnable, existingShells);
        ImportTerminatorsFromComponentOnD_DEPLAction action = new ImportTerminatorsFromComponentOnD_DEPLAction();
        action.run(null);
        if (!runnable.getFailure().equals("")) {
            fail(runnable.getFailure());
        }

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

        // Delete the source project
        TestUtil.deleteProject((IProject) getSystemModel(PROJECT_NAMES[1]).getAdapter(IProject.class));

        // re-check the instances to make sure nothing got changed
        providedTerm = Terminator_c.getOneD_TERMOnR1650(deployment,
                (selected) -> "DeploymentsDomain1".equals(((Terminator_c) selected).getName()));
        assertTrue("Provided terminator missing.", null != providedTerm && providedTerm.getProvider());
        provSvc = TerminatorService_c.getOneD_TSVCOnR1651(providedTerm,
                (selected) -> "public_service1".equals(((TerminatorService_c) selected).getName()));
        assertNotNull("Provided terminator service missing.", provSvc);
        provSvcParam = TerminatorServiceParameter_c.getOneD_TSPARMOnR1652(provSvc,
                (selected) -> "real_param".equals(((TerminatorServiceParameter_c) selected).getName()));
        assertNotNull("Provided terminator service parameter missing.", provSvcParam);
        s_dt = DataType_c.getOneS_DTOnR1653(provSvcParam);
        assertTrue("Incorrect parameter type.", null != s_dt && "real".equals(s_dt.getName()));
        requiredTerm = Terminator_c.getOneD_TERMOnR1650(deployment,
                (selected) -> "DeploymentsDomain1::term1".equals(((Terminator_c) selected).getName()));
        assertTrue("Required terminator missing.", null != requiredTerm && !requiredTerm.getProvider());
        reqSvc = TerminatorService_c.getOneD_TSVCOnR1651(requiredTerm,
                (selected) -> "term_service1".equals(((TerminatorService_c) selected).getName()));
        assertNotNull("Required terminator service missing.", reqSvc);
        reqSvcParam = TerminatorServiceParameter_c.getOneD_TSPARMOnR1652(reqSvc,
                (selected) -> "enum_param".equals(((TerminatorServiceParameter_c) selected).getName()));
        assertNotNull("Required terminator service parameter missing.", reqSvcParam);
        s_dt = DataType_c.getOneS_DTOnR1653(reqSvcParam);
        assertTrue("Incorrect parameter type.", null != s_dt && "DeploymentsDomain1::MyEnum".equals(s_dt.getName()));

    }

    @Test
    public void testChooserMultiSelect() throws Exception {

        // get the deployment
        Deployment_c deployment = Deployment_c.DeploymentInstance(modelRoot);
        assertNotNull(deployment);

        // enable IPRs
        Preferences projectPrefs = new ProjectScope((IProject) m_sys.getAdapter(IProject.class))
                .getNode(BridgePointProjectPreferences.BP_PROJECT_PREFERENCES_ID);
        projectPrefs.putBoolean(BridgePointProjectReferencesPreferences.BP_PROJECT_REFERENCES_ID, true);

        // trigger the element selection dialog
        Selection.getInstance().clear();
        Selection.getInstance().addToSelection(deployment);
        Shell[] existingShells = PlatformUI.getWorkbench().getDisplay().getShells();
        FailableRunnable runnable = TestUtil.chooseItemInDialog(300, null,
                new String[] { "DeploymentsDomain1", "DeploymentsDomain2" }, false, false, existingShells);
        TestUtil.okElementSelectionDialog(runnable, existingShells);
		PersistenceManager.getDefaultInstance().loadProjects(
				List.of(deployment.getPersistableComponent().getFile().getProject()), new NullProgressMonitor());
        Component_c[] elements = ImportTerminatorsFromComponentOnD_DEPLAction.getElements(deployment);
        ElementSelectionDialog dialog = new ElementSelectionDialog(
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), elements, "component", true, null,
                false, null);
        dialog.setBlockOnOpen(true);
        dialog.setTitle("Import Terminators From Component Selection");
        int result = dialog.open();
        assertEquals("Dialog not OK'd", Window.OK, result);
        Component_c[] selectedComponents = Arrays.copyOf(dialog.getResult(), dialog.getResult().length,
                Component_c[].class);
        assertEquals("Two components not selected.", 2, null == selectedComponents ? 0 : selectedComponents.length);
        if (!runnable.getFailure().equals("")) {
            fail(runnable.getFailure());
        }

    }

    @Test
    public void testIPRDialog() throws Exception {

        // get the deployment
        Deployment_c deployment = Deployment_c.DeploymentInstance(modelRoot);
        assertNotNull(deployment);

        // disable IPRs
        Preferences projectPrefs = new ProjectScope((IProject) m_sys.getAdapter(IProject.class))
                .getNode(BridgePointProjectPreferences.BP_PROJECT_PREFERENCES_ID);
        projectPrefs.putBoolean(BridgePointProjectReferencesPreferences.BP_PROJECT_REFERENCES_ID, false);

        // trigger the import action, say no to IPR dialog
        Selection.getInstance().clear();
        Selection.getInstance().addToSelection(deployment);
        Shell[] existingShells = PlatformUI.getWorkbench().getDisplay().getShells();
        TestUtil.noToDialog(150);
        FailableRunnable runnable = TestUtil.chooseItemInDialog(300, null,
                new String[] { "DeploymentsDomain1", "DeploymentsDomain2" }, true, false, existingShells);
        TestUtil.cancelElementSelectionDialog(0, runnable, existingShells);
        ImportTerminatorsFromComponentOnD_DEPLAction action = new ImportTerminatorsFromComponentOnD_DEPLAction();
        action.run(null);
        assertEquals("IPR dialog not found.",
                "No components found in visibility. Enable inter-project model references and try again?",
                TestUtil.dialogText);
        assertEquals("Unexpected component found.",
                "Could not locate the expected item(s) in the selection dialog (DeploymentsDomain1, DeploymentsDomain2).",
                runnable.getFailure());

        // trigger again, this time say yes
        Selection.getInstance().clear();
        Selection.getInstance().addToSelection(deployment);
        existingShells = PlatformUI.getWorkbench().getDisplay().getShells();
        TestUtil.yesToDialog(150);
        runnable = TestUtil.chooseItemInDialog(300, null, new String[] { "DeploymentsDomain1", "DeploymentsDomain2" },
                true, false, existingShells);
        TestUtil.cancelElementSelectionDialog(0, runnable, existingShells);
        action = new ImportTerminatorsFromComponentOnD_DEPLAction();
        action.run(null);
        assertEquals("IPR dialog not found.",
                "No components found in visibility. Enable inter-project model references and try again?",
                TestUtil.dialogText);
        assertEquals("Components not found.", "", runnable.getFailure());

        // assure IPRs are enabled
        assertTrue("IPRs not enabled",
                projectPrefs.getBoolean(BridgePointProjectReferencesPreferences.BP_PROJECT_REFERENCES_ID, false));
    }

}
