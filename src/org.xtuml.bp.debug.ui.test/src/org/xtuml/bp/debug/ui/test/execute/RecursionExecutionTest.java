//========================================================================
//
//File:      RecursionExecutionTest.java
//
//Copyright 2005-2013 Mentor Graphics Corporation. All rights reserved.
//
//========================================================================
//This document contains information proprietary and confidential to
//Mentor Graphics Corp., and is not for external distribution.
//========================================================================
package org.xtuml.bp.debug.ui.test.execute;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
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
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.ui.Selection;
import org.xtuml.bp.core.ui.perspective.BridgePointPerspective;
import org.xtuml.bp.debug.ui.launch.BPDebugUtils;
import org.xtuml.bp.debug.ui.test.DebugUITestUtilities;
import org.xtuml.bp.test.TestUtil;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.OrderedRunner;
import org.xtuml.bp.test.common.TestingUtilities;
import org.xtuml.bp.test.common.UITestingUtilities;

@RunWith(OrderedRunner.class)
public class RecursionExecutionTest extends BaseTest {
	private static String projectName = "135_dts0100895768";

	static private boolean initialized = false;

	public RecursionExecutionTest() throws Exception {
		super(null, null);
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		if (!initialized){
			loadProject(projectName);
			
			final IProject project = ResourcesPlugin.getWorkspace().getRoot()
			.getProject(projectName);
			

			project.getName();
			long maxTime = 2000;
			long startTime = System.currentTimeMillis();
			while(m_sys == null && System.currentTimeMillis() - startTime < maxTime) {
				while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
				m_sys = getSystemModel(project.getName());
			}
			m_sys = getSystemModel(project.getName());

			m_sys = SystemModel_c.SystemModelInstance(Ooaofooa
					.getDefaultInstance(), new ClassQueryInterface_c() {

				public boolean evaluate(Object candidate) {
					return ((SystemModel_c) candidate).getName().equals(
							project.getName());
				}

			});

			CorePlugin.enableParseAllOnResourceChange();

			initialized = true;
		}
	}
	
	@After
	public void tearDown() throws Exception {
		// terminate all launches
		DebugUITestUtilities.terminateAllProcesses(m_sys);
		// clear the any console output
		DebugUITestUtilities.clearConsoleOutput();
		DebugUITestUtilities.clearDebugView();
		// remove all breakpoints
		DebugUITestUtilities.removeAllBreakpoints();
		// wait for display events to complete
		TestingUtilities.processDisplayEvents();
		
		TestingUtilities.waitForThread("Verifier (" + projectName + ")");	
	}
	
	
	@Test
	public void testDeleteInstanceInRecursion() {
		Package_c pkg = Package_c.getOneEP_PKGOnR1405(m_sys, candidate -> {
			return ((Package_c) candidate).getName().equals("Model");
		});
		long waitTime = 2000;
		long startTime = System.currentTimeMillis();
		while(pkg == null && System.currentTimeMillis() - startTime < waitTime) {
			pkg = Package_c.getOneEP_PKGOnR1405(m_sys, candidate -> {
				return ((Package_c) candidate).getName().equals("Model");
			});
		}
		
		Function_c testFunc = Function_c.FunctionInstance(pkg.getModelRoot(),
				new Function_by_name_c("testDeleteInRecursion"));
		assertNotNull(testFunc);
		
		Selection.getInstance().setSelection(new StructuredSelection(m_sys));
		runVerifier();
		
		for (int i=0; i < 5 ; i++){
			BPDebugUtils.executeElement(testFunc);
			DebugUITestUtilities.waitForExecution();
			DebugUITestUtilities.waitForBPThreads(m_sys);
			DebugUITestUtilities.waitForExecution();
			BaseTest.dispatchEvents();
		}

		String actualConsoleText = DebugUITestUtilities.getConsoleText();
		String expectedConsoleText = "User invoked function: testDeleteInRecursion" + System.getProperty("line.separator") + "LogReal:  6.0   Instance to delete  " + System.getProperty("line.separator") + "LogInfo:  All instances have been deleted" + System.getProperty("line.separator") + "User invoked function: testDeleteInRecursion" + System.getProperty("line.separator") + "LogReal:  5.0   Instance to delete  " + System.getProperty("line.separator") + "LogInfo:  All instances have been deleted" + System.getProperty("line.separator") + "User invoked function: testDeleteInRecursion" + System.getProperty("line.separator") + "LogReal:  4.0   Instance to delete  " + System.getProperty("line.separator") + "LogInfo:  All instances have been deleted" + System.getProperty("line.separator") + "User invoked function: testDeleteInRecursion" + System.getProperty("line.separator") + "LogReal:  3.0   Instance to delete  " + System.getProperty("line.separator") + "LogInfo:  All instances have been deleted" + System.getProperty("line.separator") + "User invoked function: testDeleteInRecursion" + System.getProperty("line.separator") + "LogReal:  2.0   Instance to delete  " + System.getProperty("line.separator") + "LogInfo:  All instances have been deleted" + System.getProperty("line.separator");	

		assertEquals(expectedConsoleText , actualConsoleText);
		
	}
	

    private void runVerifier() {
    	Package_c[] pkgs = Package_c.getManyEP_PKGsOnR1401(m_sys);
    	Component_c[] models = Component_c.getManyC_CsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(pkgs));
    	openPerspectiveAndView("org.xtuml.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);    	  
    	Selection.getInstance().setSelection(new StructuredSelection(models));
    	Menu menu = m_bp_tree.getControl().getMenu();
    	assertTrue(
    			"The Launch Verifier action was not present for a component.",
    			UITestingUtilities.checkItemStatusInContextMenu(menu,
    					"Launch Verifier", "", false));
    	MenuItem launchVerifierItem = DebugUITestUtilities.getLaunchVerifierItem(menu);
    	assertNotNull(launchVerifierItem);
    	ComponentInstance_c[] engines = ComponentInstance_c.ComponentInstanceInstances(models[0].getModelRoot());
    	assertTrue("Unexpected test state, there should be no Component Instances.", engines.length == 0);
    	TestUtil.debugToDialog(200);
    	launchVerifierItem.notifyListeners(SWT.Selection, null);
    	TestingUtilities.processDisplayEvents();

    	menu = m_bp_tree.getControl().getMenu();
    	assertFalse(
    			"The Launch Verifier action was present for an unassigned imported component.",
    			UITestingUtilities.menuItemExists(menu, "", "Launch Verifier"));
  	  
	}

}
