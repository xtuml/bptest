//=====================================================================
//
//File:      $RCSfile$
//Version:   $Revision$
//Modified:  $Date$
//
//(c) Copyright 2008-2014 by Mentor Graphics Corp. All rights reserved.
//
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
package org.xtuml.bp.debug.engine;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtuml.bp.core.ComponentInstance_c;
import org.xtuml.bp.core.Component_c;
import org.xtuml.bp.core.CorePlugin;
import org.xtuml.bp.core.Function_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.PackageableElement_c;
import org.xtuml.bp.core.SystemModel_c;
import org.xtuml.bp.core.Transition_c;
import org.xtuml.bp.core.common.BridgePointPreferencesStore;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.common.PersistenceManager;
import org.xtuml.bp.core.ui.Selection;
import org.xtuml.bp.core.ui.perspective.BridgePointPerspective;
import org.xtuml.bp.debug.ui.launch.BPDebugUtils;
import org.xtuml.bp.debug.ui.test.DebugUITestUtilities;
import org.xtuml.bp.test.TestUtil;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.OrderedRunner;
import org.xtuml.bp.test.common.TestingUtilities;
import org.xtuml.bp.ui.text.activity.ActivityEditor;

@RunWith(OrderedRunner.class)
public class VerifierTransitionActionTests extends BaseTest {

	private static String projectName = "VerifierTransitionActionTest";

	private boolean initialized = false;

	public VerifierTransitionActionTests() throws Exception {
		super(projectName, null);
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		if (!initialized) {
			CorePlugin.disableParseAllOnResourceChange();

			// set perspective switch dialog on launch
			DebugUIPlugin.getDefault().getPluginPreferences().setValue(
					IDebugUIConstants.PLUGIN_ID + ".switch_to_perspective",
					"always");

			CorePlugin
					.getDefault()
					.getPluginPreferences()
					.setDefault(
							BridgePointPreferencesStore.ALLOW_IMPLICIT_COMPONENT_ADDRESSING,
							true);

			CorePlugin.getDefault().getPluginPreferences().setValue(
			  	      BridgePointPreferencesStore.SHOW_EVENT_PARAMETERS, true);

			// initialize test model
			final IProject project = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(projectName);

			loadProject(projectName);

			m_sys = SystemModel_c.SystemModelInstance(Ooaofooa
					.getDefaultInstance(), new ClassQueryInterface_c() {

				public boolean evaluate(Object candidate) {
					return ((SystemModel_c) candidate).getName().equals(
							project.getName());
				}

			});

			PersistenceManager.getDefaultInstance().loadProjects(List.of(project), new NullProgressMonitor());

			CorePlugin.enableParseAllOnResourceChange();

			TestingUtilities.allowJobCompletion();
			while (!ResourcesPlugin.getWorkspace().getRoot().isSynchronized(
					IProject.DEPTH_INFINITE)) {
				ResourcesPlugin.getWorkspace().getRoot().refreshLocal(
						IProject.DEPTH_INFINITE, new NullProgressMonitor());
				while (PlatformUI.getWorkbench().getDisplay().readAndDispatch())
					;
			}

			Ooaofooa.setPersistEnabled(true);

			initialized = true;

		}
	}

	@After
	public void tearDown() throws Exception {
		DebugUITestUtilities.stopSession(m_sys, projectName);
	}

	@Test
	public void testISMTransitionAction() {
		checkTransitionActionBreakPoint("Idle::UUT1: Perform Test 1",
                                        "ISM_BP_Transition_Action_test.result");
	}
	@Test
	public void testCSMTransitionAction() {
		checkTransitionActionBreakPoint("Test Complete",
                                        "CSM_BP_Transition_Action_test.result");
	}
	private void checkTransitionActionBreakPoint(final String transName, String expResultFile) {
		Package_c dom = Package_c.getOneEP_PKGOnR1401(m_sys, new ClassQueryInterface_c() {
			
			@Override
			public boolean evaluate(Object candidate) {
				return ((Package_c) candidate).getName().equals("TransitionActionTest");
			}
		});
		assertNotNull(dom);

		// launch the domain
		DebugUITestUtilities.setLogActivityAndLaunchForElement(dom,
				m_bp_tree.getControl().getMenu(), m_sys.getName());
        // select the transition to be tested
		Transition_c trans = Transition_c.TransitionInstance(dom.getModelRoot(), new ClassQueryInterface_c(){
			public boolean evaluate(Object candidate) {
				return ((Transition_c)candidate).Get_label().contains(transName);
			}
		});
        assertNotNull(trans);
        Selection.getInstance().clear();
        Selection.getInstance().addToSelection(trans);
        
		// set a breakpoint at line 2
		ActivityEditor editor = DebugUITestUtilities
		.openActivityEditorForSelectedElement();

        DebugUITestUtilities.setBreakpointAtLine(editor, 2);

		Function_c setup = Function_c.getOneS_SYNCOnR8001(PackageableElement_c
				.getManyPE_PEsOnR8000(Package_c
						.getManyEP_PKGsOnR1405(m_sys)),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((Function_c) candidate).getName().equals(
								"setup");
					}

				});
		assertNotNull(setup);

		openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.executeElement(setup);
		
		DebugUITestUtilities.waitForExecution();

		Function_c test = Function_c.getOneS_SYNCOnR8001(PackageableElement_c
				.getManyPE_PEsOnR8000(Package_c
						.getManyEP_PKGsOnR1405(m_sys)),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((Function_c) candidate).getName().equals(
								"test");
					}

				});
		assertNotNull(test);
		
		BPDebugUtils.executeElement(test);

		
		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(Component_c
						.getOneC_COnR8001(PackageableElement_c
								.getManyPE_PEsOnR8000(dom)));
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/verifier/" + expResultFile);
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}
}