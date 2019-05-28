
package org.xtuml.bp.ui.explorer.test;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtuml.bp.core.EnumerationDataType_c;
import org.xtuml.bp.core.ModelClass_c;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.ui.Selection;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.ExplorerUtil;
import org.xtuml.bp.test.common.OrderedRunner;
import org.xtuml.bp.test.common.UITestingUtilities;

@RunWith(OrderedRunner.class)
public class BuildProjectMenuTest extends BaseTest {

	private static String modelName = "";

	public BuildProjectMenuTest() throws Exception {
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
	public void testBuildProjectMicrowaveOvenOnProject() throws Exception {

		modelName = "MicrowaveOven";

		loadProject(modelName);
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(m_sys);
		checkForBuildProjectCME();
	}

	@Test
	public void testBuildProjectMicrowaveOvenOnDatatype() throws Exception { 
		ExplorerUtil.expandAll(); 
		BaseTest.dispatchEvents(0); 
		String name = "tube_wattage";
		TreeItem findItem = ExplorerUtil.findItem(null, name);
		assertTrue("Expected to find the " + name + " type, but didn't.",
				findItem.getData() instanceof EnumerationDataType_c);
		EnumerationDataType_c ele = (EnumerationDataType_c) findItem.getData();
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(ele);
		checkForBuildProjectCME();
	}
	
	@Test
	public void testBuildProjectMicrowaveOvenOnClassAndPackage() throws Exception { 
		ExplorerUtil.expandAll(); 
		BaseTest.dispatchEvents(0); 
		String name = "Door";
		TreeItem findItem = ExplorerUtil.findItem(null, name);
		assertTrue("Expected to find the " + name + " type, but didn't.",
				findItem.getData() instanceof ModelClass_c);
		ModelClass_c ele = (ModelClass_c) findItem.getData();
		Selection.getInstance().clear();
		Selection.getInstance().addToSelection(ele);

		name = "Test Subsystem";
		findItem = ExplorerUtil.findItem(null, name);
		assertTrue("Expected to find the " + name + " type, but didn't.",
				findItem.getData() instanceof Package_c);
		Package_c ele2 = (Package_c) findItem.getData();
		Selection.getInstance().addToSelection(ele2);
		checkForBuildProjectCME();
	}

	public void checkForBuildProjectCME() throws InterruptedException {
		// get the explorer context menu for the selection
		Menu menu = getExplorerView().getTreeViewer().getControl().getMenu();

		MenuItem[] items = UITestingUtilities.getMenuItems(menu, "");
		MenuItem buildItem = null;
		for (MenuItem itemIter : items) {
			if (itemIter.getText().equals("Build Project")) {
				buildItem = itemIter;
				break;
			}
		}
		assertTrue("The \"Build Project\" menu item was not available.", buildItem != null);
	}
	
}
