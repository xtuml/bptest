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
package org.xtuml.bp.core.test;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Menu;
import org.junit.Test;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.ui.Selection;
import org.xtuml.bp.core.util.UIUtil;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.CanvasTestUtils;
import org.xtuml.bp.test.common.UITestingUtilities;
import org.xtuml.bp.ui.explorer.ExplorerView;
import org.xtuml.bp.ui.graphics.editor.GraphicalEditor;

public class UIConfigurationTests extends BaseTest {
	/**
	 * The editor upon which these tests operate.
	 */
	private static GraphicalEditor editor = null;

	/**
	 * The package upon which tests tests operate.
	 */
	private static Package_c pkgUnderTest = null;
	
	private static String modelName = "ComponentContextMenuTests";
	
	@Override
	protected void initialSetup() throws Exception {
		m_sys = getSystemModel(modelName);
		modelRoot = Ooaofooa.getInstance("/" + modelName + "/models/" + modelName + "/Component Context Menu Test/Component Context Menu Test.xtuml");
		pkgUnderTest = Package_c.getOneEP_PKGOnR1405(m_sys,
				candidate -> ((Package_c) candidate).getName().equals("Component Context Menu Testing"));
		editor = UITestingUtilities.getGraphicalEditorFor(pkgUnderTest, false);
	}
	
	/* Canvas and model explorer tests 
	 */
    @Test
	public void testCanvasAndMEDisablement() {
    	// The first column is the name of the tool under test in disablement flag form. The
    	// second column is the CME path.
    	String [][] tools = {
    			{"Actor", "New::Interaction::Actor"},
    			{"InteractionComponent", "New::Interaction::Component"},
    			{"UserDataType", "New::Types::User DataType"},
    			{"Exception", "New::Exceptions::Exception"},
    			{"ExternalEntity", "New::External::External Entity"},
    			{"Class", "New::Classes::Class"},
    			{"Component", "New::Components::Component"},
    			{"AcceptEventAction", "New::Activity::Accept Event Action"},
    			{"UseCase", "New::Use Case::Use Case"},
    			{"Function", "New::Function"},
    			};
    	
    	for (String[] tool : tools) {
            CanvasTestUtils.openPackageCanvasEditor(pkgUnderTest);

            checkMECMEForTool(tool[1], true);
    		
            checkCanvasCMEForTool(tool[1], true);
	    	
	    	// Now turn off the tool and re-check
	    	System.setProperty("bridgepoint." + tool[0], "disabled");
	    	UIUtil.dispatchAll();

            checkMECMEForTool(tool[1], false);
    		
            checkCanvasCMEForTool(tool[1], false);

	    	// Turn the tool back on
	    	System.setProperty("bridgepoint." + tool[0], "enabled");
	    	UIUtil.dispatchAll();
    	}
    }
	
	/* Model Explorer only tests
	 */
    @Test
	public void testMEExclusiveDisablement() {
    	// The first column is the name of the tool under test in disablement flag form. The
    	// second column is the CME path.
    	String [][] tools = {
    			{"ExportMASLProject", "Export MASL Project"},
    			{"ExportMASLDomains", "Export MASL Domains"},
    			{"GenerateFunctionsFromList", "BridgePoint Utilities::Generate Functions From List..."},
    			};
    	
    	for (String[] tool : tools) {
            CanvasTestUtils.openPackageCanvasEditor(pkgUnderTest);

            checkMECMEForTool(tool[1], true);
    		
	    	// Now turn off the tool and re-check
	    	System.setProperty("bridgepoint." + tool[0], "disabled");
	    	UIUtil.dispatchAll();

            checkMECMEForTool(tool[1], false);
    		
	    	// Turn the tool back on
	    	System.setProperty("bridgepoint." + tool[0], "enabled");
	    	UIUtil.dispatchAll();
    	}
    }
    
    private void checkMECMEForTool(String toolPath, boolean expected) {
        Menu menu = null;
		m_bp_tree.refresh();
        TreeViewer viewer = ExplorerView.getExplorerTreeViewer();
        viewer.expandAll();
        StructuredSelection sel = new StructuredSelection(pkgUnderTest);
        viewer.setSelection(sel);
		Selection.getInstance().setSelection(sel);
		UIUtil.dispatchAll();
		viewer.getTree();
		UIUtil.dispatchAll();
		menu = viewer.getTree().getMenu();
		if (expected) {
			assertTrue("Unable to find menu entry: " + toolPath, UITestingUtilities.checkItemStatusInContextMenuByPath(menu, toolPath, false));
		} else {
			assertFalse("Found menu entry when we should not have: " + toolPath, UITestingUtilities.checkItemStatusInContextMenuByPath(menu, toolPath, false));
		}
    }
    
    private void checkCanvasCMEForTool(String toolPath, boolean expected) {
    	UITestingUtilities.clearGraphicalSelection();
    	UITestingUtilities.addElementToGraphicalSelection(pkgUnderTest);
    	UIUtil.dispatchAll();
    	Menu menu = editor.getCanvas().getMenu();
		if (expected) {
			assertTrue("Unable to find menu entry: " + toolPath, UITestingUtilities.checkItemStatusInContextMenuByPath(menu, toolPath, false));
		} else {
			assertFalse("Found menu entry when we should not have: " + toolPath, UITestingUtilities.checkItemStatusInContextMenuByPath(menu, toolPath, false));
		}
    }
}

