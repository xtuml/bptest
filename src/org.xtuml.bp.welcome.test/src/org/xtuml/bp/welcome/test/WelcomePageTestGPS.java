package org.xtuml.bp.welcome.test;
//=====================================================================
//
// File: WelcomePageTestGPS.java
//
//=====================================================================

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.xtuml.bp.core.ExternalEntity_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.SystemModel_c;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.common.PersistableModelComponent;
import org.xtuml.bp.core.common.PersistenceManager;
import org.xtuml.bp.test.TestUtil;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.OrderedRunner;
import org.xtuml.bp.test.common.TestingUtilities;
import org.xtuml.bp.ui.explorer.ExplorerView;
import org.xtuml.bp.utilities.ui.TreeUtilities;
import org.xtuml.bp.welcome.gettingstarted.SampleProjectGettingStartedAction;

import junit.framework.TestCase;
@RunWith(OrderedRunner.class)
public class WelcomePageTestGPS extends TestCase {

    private static Map<String,IProject> projects;
    private static IViewPart g_view = null;

    private final String[] projectNames = { "GPS_Watch", "Location", "HeartRateMonitor", "Tracking", "UI" };

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Override
    public void setUp() {
        while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
    }

    @Test
    public void testProjectCreation() {
        runGPSOALGettingStartedAction();

        TestUtil.selectButtonInDialog(3000, "Yes");
        runGPSOALGettingStartedAction();

        verifyProjectCreated();
    }

    @Test
    public void testNoProjectOverwrite() {
        IFile dummyFile = projects.get(projectNames[0]).getFile("dummyFile");
        IFile readme = projects.get(projectNames[0]).getFile("README.md");
        try {
            dummyFile.create(readme.getContents(), IResource.REPLACE, null);
        } catch (CoreException ce) {
            fail("Failed to create dummy file.");
        }
        if (!dummyFile.exists()) {
            fail("Failed to create the dummy file.");
        }
        TestUtil.selectButtonInDialog(2000, "No");
        runGPSOALGettingStartedAction();

        // We said not to overwrite, so the dummy file should still be there
        assertTrue("The project was overwritten when it shouldn't have been.", dummyFile.exists());
    }

    @Test
    public void testProjectOverwrite() throws Exception {
        IFile dummyFile = projects.get(projectNames[0]).getFile("dummyFile");

        // Make sure the marker file is there.
        assertTrue("The dummy file for testing doesn't exist.", dummyFile.exists());

        TestUtil.selectButtonInDialog(1000, "Yes");
        runGPSOALGettingStartedAction();

        // We said to overwrite, so the dummy file should not be there
        assertFalse("The project was not overwritten when it should have been.", dummyFile.exists());

        verifyProjectCreated();
    }

    @Test
    public void testImportLoadPersistAndBuild()  throws Exception {
        // This test is disabled for command line runs
        // See https://support.onefact.net/issues/9414
        if(BaseTest.isCLITestRun()) {
            return;
        }
        int numImports = 3;
        for (int i = 0; i < numImports; i++) {
            System.out.println("Import number: " + String.valueOf(i+1));
            TestUtil.selectButtonInDialog(1000, "Yes");
            runGPSOALGettingStartedAction();

            verifyProjectCreated();

            final IProject project = getProject(projectNames[0]);

            buildProject(project);

            checkForErrors();

            // load and persist
            PersistableModelComponent pmc = PersistenceManager.getRootComponent(project);
            pmc.loadComponentAndChildren(new NullProgressMonitor());
            pmc.persistSelfAndChildren();

            checkForErrors();

            TestingUtilities.deleteProject(projectNames[0]);
        }

    }

    @Test
    public void testSmartPreBuild() throws Exception {
        // This test is disabled for command line runs
        // See https://support.onefact.net/issues/9414
        if(BaseTest.isCLITestRun()) {
            return;
        }
        // This test builds the project several times, testing that the exported
        // <project>.sql file from pre-builder is updated when needed and left
        // unmodified by the build (re-export skipped) when an update is not needed.
        TestUtil.selectButtonInDialog(1000, "Yes");
        runGPSOALGettingStartedAction();

        verifyProjectCreated();

        final IProject project = getProject(projectNames[0]);

        // First build
        buildProject(project);
        checkForErrors();
        long firstPrebuildOutputTimestamp = getPrebuildOutputTimestamp();

        // Second build.  Wait a while, then build again without touching the
        // model data.  The pre-builder should not re-export.
        TestingUtilities.allowJobCompletion();
        buildProject(project);
        checkForErrors();
        long secondPrebuildOutputTimestamp = getPrebuildOutputTimestamp();
        assertTrue("The pre-build re-exported the model data.  It should not have done this.", firstPrebuildOutputTimestamp == secondPrebuildOutputTimestamp);

        // Third build.  Wait a while, touch the model data by adding text to a package
        // description, then build again. The pre-builder should re-export.
        TestingUtilities.allowJobCompletion();
        SystemModel_c system = SystemModel_c.SystemModelInstance( Ooaofooa.getDefaultInstance(), new ClassQueryInterface_c() {
            @Override
            public boolean evaluate(Object candidate) {
                return ((SystemModel_c) candidate).getName().equals(projectNames[0]);
            }
        });
        Package_c pkg = Package_c.getOneEP_PKGOnR1401(system);
        assertNotNull( pkg );
        pkg.setDescrip( pkg.getDescrip() + "\nHello, World!" );
        pkg.getPersistableComponent().persist();
        TestingUtilities.allowJobCompletion();
        buildProject(project);
        checkForErrors();
        long thirdPrebuildOutputTimestamp = getPrebuildOutputTimestamp();
        assertTrue("The pre-build did not re-export the model data.  It should have done this.", thirdPrebuildOutputTimestamp > secondPrebuildOutputTimestamp);
    }

    @Test
    public void testExternalEntityDefaults() {
        TestUtil.selectButtonInDialog(1000, "Yes");
        runGPSOALGettingStartedAction();

        verifyProjectCreated();

        SystemModel_c system = SystemModel_c.SystemModelInstance( Ooaofooa.getDefaultInstance(), new ClassQueryInterface_c() {
            @Override
            public boolean evaluate(Object candidate) {
                return ((SystemModel_c) candidate).getName().equals(projectNames[0]);
            }
        });

        assertNotNull(system);
        system.getPersistableComponent().loadComponentAndChildren(new NullProgressMonitor());

        Ooaofooa[] instancesUnderSystem = Ooaofooa.getInstancesUnderSystem(projectNames[0]);
        for (Ooaofooa root : instancesUnderSystem) {
            ExternalEntity_c[] ees = ExternalEntity_c.ExternalEntityInstances(root);
            for (ExternalEntity_c ee : ees) {
                if (!ee.getIsrealized()) {
                    fail("External Entity: " + ee.getName() + " was not configured with the default isRealized = true");
                }
            }
        }
    }

    private long getPrebuildOutputTimestamp() throws CoreException {
        long prebuildOutputTimestamp = Long.MAX_VALUE;
        IPath genPath = new Path("gen" + File.separator + "code_generation" + File.separator);
        IFolder genFolder = projects.get(projectNames[0]).getFolder(genPath);
        genFolder.refreshLocal(IResource.DEPTH_ONE, null);
        if (genFolder.exists() && genFolder.members().length != 0) {
            for (IResource res : genFolder.members()) {
                if (res.getName().equals( projectNames[0] + ".sql")) {
                    prebuildOutputTimestamp = res.getLocalTimeStamp();
                }
            }
        }
        else {
            fail("The pre-builder did not create the expected output.");
        }

        return prebuildOutputTimestamp;
    }

    private void checkForErrors() {
        // Check the problems view
        g_view = selectView(projects.get(projectNames[0]), "org.eclipse.ui.views.ProblemView");

        // Check the explorer view for orphaned elements
        ExplorerView view = null;
        try {
            view = (ExplorerView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.xtuml.bp.ui.explorer.ExplorerView");
        } catch (PartInitException e) {}
        view.getTreeViewer().refresh();
        while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
        view.getTreeViewer().expandAll();
        while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
        TreeItem topItem = view.getTreeViewer().getTree().getTopItem();
        TreeItem[] orphaned = TreeUtilities.getOrphanedElementsFromTree(topItem);
        if (orphaned.length > 0) {
            String elements = TreeUtilities.getTextResultForOrphanedElementList(orphaned);
            assertTrue("Orphaned elements are present: " + elements, false);
        }
        // this is a regression test for issue 9556
        assertTrue("Expected tree items are missing from the Model Explorer View", topItem.getItemCount()==3);
    }

    private void buildProject(final IProject project) throws Exception {
        g_view = selectView(project, "org.eclipse.ui.views.ResourceNavigator");
        g_view.getSite().getSelectionProvider().setSelection(new StructuredSelection(project));
        Runnable r = new Runnable() {
            public void run() {
                try {
                    project.build(IncrementalProjectBuilder.FULL_BUILD, null);
                } catch (Exception e) {
                    fail("Failed to build the project. " + e.getMessage());
                }
            }
        };
        r.run();

        TestingUtilities.allowJobCompletion();
    }

    private IProject getProject(String name) {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
        assertTrue( project.exists() );
        return project;
    }

    private IViewPart selectView(final IProject project, final String viewName) {
        g_view = null;
        Runnable r = new Runnable() {
            public void run() {
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try {
                    g_view = page.showView(viewName);
                } catch (PartInitException e) {
                    fail("Failed to open the " + viewName + " view");
                }
            }
        };
        r.run();
        assertTrue("Unable to select view: " + viewName, g_view != null);
        return g_view;
    }

    private void runGPSOALGettingStartedAction() {
        runGPSOALGettingStartedAction(true, "");
    }

    private void runGPSOALGettingStartedAction(boolean importIntoWorkspace, String tempFolder) {
        String singleFileModel = "zip";
        String model = "GPS_Watch_OAL,GPS_Watch,HeartRateMonitor,Location,Tracking,UI";
        if (!importIntoWorkspace) {
            singleFileModel = tempFolder;
        }
        // create and run new instances of GettingStarted for the GPS Watch model
        SampleProjectGettingStartedAction action = new SampleProjectGettingStartedAction();
        Properties props = new Properties();
        props.put("model", model);
        props.put("SingleFileModel", singleFileModel);
        props.put("ImportIntoWorkspace", (importIntoWorkspace ? "true" : "false"));
        props.put("LaunchGettingStartedHelp", "false"); // We do not test this and it just spawns lots of windows we do not use in test
        props.put("readmePath","GPS_Watch/README.md");
        props.put("exes","GPS_Watch/Debug_Linux/GPS_Watch,GPS_Watch/Debug_Mac/GPS_Watch,GPS_Watch/Debug_Windows/GPS_Watch.exe");
        action.run(null, props);
        TestingUtilities.allowJobCompletion();
    }

    private boolean projectReady(String projectName) {
        // Check that project exists in the workspace
        // and that it is indeed an xtUML project
        boolean projectExists = false;
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        projectExists = project.exists();
        assertTrue("Project: " + projectName + " does not exist.", projectExists);
        projectExists = project.isOpen();
        assertTrue("Project: " + projectName + " is not open.", projectExists);
        projects.put( projectName, project );
        return projectExists;
    }

    private void verifyProjectCreated() {
    	projects = new HashMap<>();
    	boolean projects_ready = true;
        for ( String projectName : projectNames ) {
            projects_ready = projects_ready && projectReady(projectName);
        }
    }

}
