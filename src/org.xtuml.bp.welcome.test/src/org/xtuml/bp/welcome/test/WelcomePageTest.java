package org.xtuml.bp.welcome.test;
//=====================================================================
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
//=====================================================================

import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.PlatformUI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtuml.bp.core.ExternalEntity_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.SystemModel_c;
import org.xtuml.bp.core.XtUMLNature;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.test.TestUtil;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.OrderedRunner;
import org.xtuml.bp.test.common.TestingUtilities;
import org.xtuml.bp.utilities.ui.ProjectUtilities;
import org.xtuml.bp.welcome.gettingstarted.GettingStartedLiveHelpAction;
import org.xtuml.bp.welcome.gettingstarted.SampleProjectGettingStartedAction;
@RunWith(OrderedRunner.class)
public class WelcomePageTest extends BaseTest {

	private IProject project;
	private final String ProjectName = "MicrowaveOven";

	private String[] expectedXtUMLFiles = {".externalToolBuilders/Model Compiler.launch",
	"models/MicrowaveOven/MicrowaveOven.xtuml"};

	private String[] expectedFiles =expectedXtUMLFiles;
	
	private String markingFolder = "gen/";
	private String codeGenFolder = "code_generation/";

	private String[] MC3020Files = {
            markingFolder + "datatype.mark",
            markingFolder + "system.mark",
            markingFolder + "class.mark",
            markingFolder + "domain.mark"
            };
	
	public WelcomePageTest() {
		super();
	}

	@Override
	public void setUp() {
		while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
	}
	
	public void runGettingStartedAction() {
		// create and run new instances of GettingStartedAction
		GettingStartedLiveHelpAction gsAction = new GettingStartedLiveHelpAction();
		TestUtil.yesToDialog(5000);
		gsAction.run();
	}

	public boolean projectReady(String projectName) {
		// Check that project exists in the workspace
		// and that it is indeed an xtUML project
		boolean projectExists = false;
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				projectName);
		projectExists = project.exists();
		assertTrue("Project: " + projectName + " does not exist.",
				projectExists);
		projectExists = project.isOpen();
		assertTrue("Project: " + projectName + " is not open.", projectExists);
		return projectExists;
	}

	public void isxtUMLProject(IProject project) {
		assertTrue("Project: " + project.getName()
				+ " is not an xtUML project.", XtUMLNature.hasNature(project));
	}

	public void containsProjectMembers() {
		/*
		 * verify that the project contains all of the necessary xtUML, Edge,
		 * and 3020 files
		 */
		for (int i = 0; i < expectedFiles.length; i++) {
			IFile file = project.getFile(expectedFiles[i]);
			assertTrue("Expected file: " + file.getName() + " does not exist.",
					file.exists());
		}
		for (int i = 0; i < MC3020Files.length; i++) {
			IFile file = project.getFile(MC3020Files[i]);
			assertTrue("Expected file: " + file.getName() + " does not exist.",
					file.exists());
		}
	}

	public void verifyProjectCreated() {
		boolean projectExists = projectReady("MicrowaveOven");
		if (projectExists)
			containsProjectMembers();
	}
	@Test
	public void testProjectCreation() {
		runGettingStartedAction();
		runGettingStartedAction();
		verifyProjectCreated();
	}
	@Test
	public void testExternalEntityDefaultsTemplateProject() {
		// get handle to help shell in order to display after completing below actions
		SampleProjectGettingStartedAction action = new SampleProjectGettingStartedAction();
		Properties props = new Properties();
		props.put("model", "TemplateProject");
		props.put("SingleFileModel", "false");
		action.run(null, props);
		
		SystemModel_c system = SystemModel_c.SystemModelInstance(
				Ooaofooa.getDefaultInstance(), new ClassQueryInterface_c() {

					@Override
					public boolean evaluate(Object candidate) {
						return ((SystemModel_c) candidate).getName().equals(
								"TemplateProject");
					}
				});

		assertNotNull(system);
		system.getPersistableComponent().loadComponentAndChildren(
				new NullProgressMonitor());

		Ooaofooa[] instancesUnderSystem = Ooaofooa
				.getInstancesUnderSystem("TemplateProject");
		for (Ooaofooa root : instancesUnderSystem) {
			ExternalEntity_c[] ees = ExternalEntity_c
					.ExternalEntityInstances(root);
			for (ExternalEntity_c ee : ees) {
				if (!ee.getIsrealized()) {
					fail("External Entity: "
							+ ee.getName()
							+ " was not configured with the default isRealized = true");
				}
			}
		}
	}

	@Test
	public void testBuildProjectWithNoGenFolder() {
        // Verify that <project>.sql file from pre-builder is created when no gen/ folder
		// exists in the project

        TestUtil.selectButtonInDialog(1000, "Yes");
		runGettingStartedAction();
        TestingUtilities.allowJobCompletion();

        verifyProjectCreated();

        final IProject project = ProjectUtilities.getProject(ProjectName);

        // delete the gen/ folder
        IFile genDir = project.getFile(markingFolder);
        assertTrue("Expected directory: " + genDir.getName() + " exists and it should not.", !genDir.exists());
        
        // build
        try {
            ProjectUtilities.buildProject(project);
		} catch (Exception e) {
			fail("Failed to build the project. " + e.getMessage()); //$NON-NLS-1$
		}
        
        // make sure pre-builder output exists
        IFile file = project.getFile(markingFolder + codeGenFolder + ProjectName + ".sql");
        assertTrue("Expected file: " + file.getName() + " does not exist.", file.exists());
	}
	
	@Test
	public void testBuildProjectWithNoCodeGenFolder() {
        // Verify that <project>.sql file from pre-builder is created when no gen/code_generation/ folder
		// exists in the project

        TestUtil.selectButtonInDialog(1000, "Yes");
		runGettingStartedAction();
        TestingUtilities.allowJobCompletion();

        verifyProjectCreated();

        final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(ProjectName);

        // delete the gen/code_generation/ folder
        IFile codeGenDir = project.getFile(markingFolder + codeGenFolder);
        assertTrue("Expected directory: " + codeGenDir.getName() + " exists and it should not.", !codeGenDir.exists());
        
        // build
        try {
            ProjectUtilities.buildProject(project);
		} catch (Exception e) {
			fail("Failed to build the project. " + e.getMessage()); //$NON-NLS-1$
		}
        
        // make sure pre-builder output exists
        IFile file = project.getFile(markingFolder + codeGenFolder + ProjectName + ".sql");
        assertTrue("Expected file: " + file.getName() + " does not exist.", file.exists());
	}
}
