package org.xtuml.bp.core.test.ui;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.service.prefs.Preferences;
import org.xtuml.bp.core.ui.Selection;
import org.xtuml.bp.core.ui.preferences.BridgePointProjectPreferences;
import org.xtuml.bp.core.ui.preferences.BridgePointProjectReferencesPreferences;
import org.xtuml.bp.test.TestUtil;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.OrderedRunner;
import org.xtuml.bp.test.common.UITestingUtilities;

//========================================================================
// Licensed under the Apache License, Version 2.0 (the "License"); you may not 
// use this file except in compliance with the License.  You may obtain a copy 
// of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   See the 
// License for the specific language governing permissions and limitations under
// the License.
//========================================================================

@RunWith(OrderedRunner.class)
public class DocGenTest extends BaseTest {

	private static String modelName = "";
    private String[] projSpecificExpectedFiles;

    private String[] expectedOutputFiles =  {
	       "doc/images/DataType.gif",
	       "doc/techpub.css",
	       "doc/doc.xml",
	       "doc/doc.html"
	};

	public DocGenTest() throws Exception {
		super(null, null);
	}

    @Override
    @Before
	public void setUp() throws Exception {
        super.setUp();
    }

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDocumentGenerationMicrowaveOven() throws Exception {

	    modelName = "MicrowaveOven";
        projSpecificExpectedFiles = new String[]{ "doc/images/MicrowaveOven-MicrowaveOven-Package Diagram.png" };

	    loadProject(modelName);
	    runDocGenAndCheckResults();
	}

	@Test
	public void testDocumentGenerationInterProjectRealizedClass() throws Exception {
        // This test uses a test model IPR on.
        // We load the library project, then the referring project we'll
        // actually generate code for.
        modelName = "RealizedClassOnwer";  // Yes, the test model name has this typo in it!
	    loadProject(modelName);
        modelName = "InterProjectRealizedClass";
	    loadProject(modelName);
        
        projSpecificExpectedFiles = new String[]{ 
                "doc/images/RealizedClassOnwer-System-Package Diagram.png",
                "doc/images/InterProjectRealizedClass-Top Package-Package Diagram.png"
                };

        runDocGenAndCheckResults();
    }

	@Test
	public void testDocumentGenerationInterProjectRealizedClass_RTO_Off() throws Exception {
        // This test runs DocGen on an IPR project but with the "Emit RTO project data"
        // preference turned off.  Thus, the resulting docs do not have info from
        // the referred-to projects included.
        modelName = "InterProjectRealizedClass";
        
        projSpecificExpectedFiles = new String[]{ 
                "doc/images/InterProjectRealizedClass-Top Package-Package Diagram.png"
                };

        // Turn off the project preference to emit RTO project data
        IProject selectedProject = ResourcesPlugin.getWorkspace().getRoot().getProject(modelName);
        if (selectedProject != null) {
            IScopeContext projectScope = new ProjectScope(selectedProject);
            Preferences projectNode = projectScope.getNode(BridgePointProjectPreferences.BP_PROJECT_PREFERENCES_ID);
            projectNode.putBoolean(BridgePointProjectReferencesPreferences.BP_PROJECT_EMITRTODATA_ID, false);
        }
        
        runDocGenAndCheckResults("_RTO_Off");
    }

    public void runDocGenAndCheckResults() throws InterruptedException {
        runDocGenAndCheckResults("");
    }
    
    public void runDocGenAndCheckResults(final String expectedResultsNameModifier) throws InterruptedException {

        Menu menu = null;

        // initialize test model
        final IProject project = ResourcesPlugin.getWorkspace().getRoot()
                .getProject(modelName);

        // add the element to the core selection and use the explorer
        // context menu
        Selection.getInstance().clear();
        Selection.getInstance().addToSelection(m_sys);
        menu = getExplorerView().getTreeViewer().getControl().getMenu();

        MenuItem[] items = UITestingUtilities.getMenuItems(menu, "BridgePoint Utilities");
        MenuItem docItem = null;
        for ( MenuItem itemIter : items ) {
            if ( itemIter.getText().equals("Create Documentation")) {
            	docItem = itemIter;
            	break;
            }
        }
        assertTrue(
                "The \"Create Documentation\" menu item was not available for testing.",
                docItem != null);
        UITestingUtilities.activateMenuItem(docItem);

        BaseTest.dispatchEvents();
        
        Thread.sleep(4000);

        // Spot check existence of a few files that are output from various
        // pieces of the doc gen flow
        for (int i = 0; i < expectedOutputFiles.length; i++) {
            IFile file = project.getFile(expectedOutputFiles[i]);
            assertTrue("Expected file: " + file.getName() + " does not exist.",
                    file.exists());
        }        
        for (int i = 0; i < projSpecificExpectedFiles.length; i++) {
            IFile file = project.getFile(projSpecificExpectedFiles[i]);
            assertTrue("Expected file: " + file.getName() + " does not exist.",
                    file.exists());
        }        
        
        // Diff contents of the key output files
        //  - doc.xml : this file has a workspace specific path that is replaced out before comparing
        //  - doc.html
        File expectedResults = new File(m_workspace_path + "/expected_results/DocGen/" + modelName + expectedResultsNameModifier + "_doc.xml");
        String expected_results = TestUtil.getTextFileContents(expectedResults);
        IFile ifile = project.getFile("doc/doc.xml");
        File actualResults = ifile.getRawLocation().makeAbsolute().toFile();
        String actual_results = TestUtil.getTextFileContents(actualResults);
        actual_results = actual_results.replaceFirst("\'.*docbookx.dtd\'", "JUNIT REPLACED - DTD PATH");
        assertEquals(expected_results, actual_results);

        // We must modify the compare data to remove autogenerated IDs
        expectedResults = new File(m_workspace_path + "/expected_results/DocGen/" + modelName + expectedResultsNameModifier + "_doc.html");
        expected_results = TestUtil.getTextFileContents(expectedResults);
        expected_results = expected_results.replaceAll("idm[0-9]+", "");
        ifile = project.getFile("doc/doc.html");
        actualResults = ifile.getRawLocation().makeAbsolute().toFile();
        actual_results = TestUtil.getTextFileContents(actualResults);
        actual_results = actual_results.replaceAll("idm[0-9]+", "");
        assertEquals(expected_results, actual_results);

    }

}
