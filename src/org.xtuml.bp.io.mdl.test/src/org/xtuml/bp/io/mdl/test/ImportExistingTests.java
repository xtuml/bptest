package org.xtuml.bp.io.mdl.test;
//=============================================================================
//
// File:     ImportExistingTests.java
//
// This test imports existing projects into the workspace without copying
// the files into the workspace. This is a regression test for issue #9556
//
//=============================================================================

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroPart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtuml.bp.core.ui.perspective.BridgePointPerspective;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.OrderedRunner;
import org.xtuml.bp.ui.explorer.ExplorerView;
import org.xtuml.bp.utilities.ui.ProjectUtilities;
import org.xtuml.bp.utilities.ui.TreeUtilities;

import junit.framework.TestCase;

@RunWith(OrderedRunner.class)
public class ImportExistingTests extends TestCase {
    
    private static final String PROJECT_NAME = "GPS_Watch";
    private static final String PROJECT_PATH = "applications/gps/GPS/GPS_Watch";
    
    @Test
    public void testProjectCreationNoImportIntoworkspace() throws Exception {
        
        String repository_location = BaseTest.getTestModelRespositoryLocation() + "/../";
        ProjectUtilities.importExistingProject( repository_location + PROJECT_PATH );
        BaseTest.dispatchEvents();

        ProjectUtilities.openxtUMLPerspective();
        closeWelcomePage();
        
        verifyProjectCreated( PROJECT_NAME );
        
        ResourcesPlugin.getWorkspace().getRoot().getProject( PROJECT_NAME ).refreshLocal( IResource.DEPTH_INFINITE, null );
        BaseTest.dispatchEvents();

        checkForErrors( PROJECT_NAME );

    }

    private void verifyProjectCreated( String projectName ) {
        // Check that project exists in the workspace
        // and that it is indeed an xtUML project
        boolean projectExists = false;
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject( projectName );
        projectExists = project.exists();
        assertTrue("Project: " + projectName + " does not exist.", projectExists);
        projectExists = project.isOpen();
        assertTrue("Project: " + projectName + " is not open.", projectExists);
    }

    private void checkForErrors( String projectName ) {

        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject( projectName );

        // Check the problems view
        selectView( project, "org.eclipse.ui.views.ProblemView" );

        // Check the explorer view for orphaned elements
        ExplorerView view = null;
        try {
            view = (ExplorerView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(BridgePointPerspective.ID_MGC_BP_EXPLORER);
            BaseTest.dispatchEvents();
        } catch (PartInitException e) {}
        view.getTreeViewer().refresh();
        while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
        view.getTreeViewer().expandAll();
        while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
        view.getTreeViewer().refresh();
        while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
        TreeItem topItem = null;
        for ( TreeItem i : view.getTreeViewer().getTree().getItems() ) {
        	if ( PROJECT_NAME.equals( i.getText() ) ) { 
        		topItem = i;
        	}
        }
        assertNotNull( topItem );
        TreeItem[] orphaned = TreeUtilities.getOrphanedElementsFromTree(topItem);
        if (orphaned.length > 0) {
            String elements = TreeUtilities.getTextResultForOrphanedElementList(orphaned);
            assertTrue("Orphaned elements are present: " + elements, false);
        }
        while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
        // this is a regression test for issue 9556
        assertTrue("Expected tree items are missing from the Model Explorer View", topItem.getItemCount()==3);
    }

    private IViewPart g_view = null;
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

    private void closeWelcomePage() {
        // close welcome page
        // Check to see if the xtUML perspective contains an intro part
        // This prevents a NPE in the case where no welcome page exists
        boolean foundIntroPart = false;
        IViewReference views[] = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
        for(int i = 0; i < views.length; i ++) {
            if(views[i].getPartName().equals("Welcome")) { //$NON-NLS-1$
                foundIntroPart = true;
            }
        }
        if(foundIntroPart == true) {
            IIntroPart introPart = PlatformUI.getWorkbench().getIntroManager().getIntro();
            if(introPart != null) {
                PlatformUI.getWorkbench().getIntroManager().closeIntro(introPart);
            }
            while(Display.getCurrent().readAndDispatch());
        }
    }

}
