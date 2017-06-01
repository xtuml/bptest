//=====================================================================
//
//File:      $RCSfile: ModificationValidationTestsGenerics.java,v $
//Version:   $Revision: 1.6 $
//Modified:  $Date: 2013/05/10 04:30:25 $
//
//(c) Copyright 2004-2014 by Mentor Graphics Corp. All rights reserved.
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

package org.xtuml.bp.core.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.PlatformUI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtuml.bp.core.Function_c;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.ui.DeleteAction;
import org.xtuml.bp.core.ui.Selection;
import org.xtuml.bp.test.TestUtil;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.CanvasTestUtils;
import org.xtuml.bp.test.common.ExplorerUtil;
import org.xtuml.bp.test.common.OrderedRunner;
import org.xtuml.bp.ui.canvas.Shape_c;
import org.xtuml.bp.ui.canvas.test.CanvasTestUtilities;
import org.xtuml.bp.ui.graphics.editor.GraphicalEditor;
import org.xtuml.bp.ui.text.activity.ActivityEditor;
import org.xtuml.bp.utilities.ui.CanvasUtilities;

@RunWith(OrderedRunner.class)
public class ModificationValidationTestsGenerics extends BaseTest {

	public ModificationValidationTestsGenerics(){
		super(null, null);
	}

	static private boolean firstTime = true;
	static private boolean initialized = false;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		if (firstTime) {
			//switchPerspective("org.xtuml.bp.core.perspective");
			if (!initialized) {
				loadProject("ModificationValidationTests");
				BaseTest.waitFor(300);
				initialized = true;
			}
			firstTime = false;
		}

	}


	@Test
	public void testRenameOfReadonlyComponent() {
		Package_c ee = Package_c.PackageInstance(modelRoot,
				new Package_by_name_c("External Entities"));
		TestUtil.changeFileReadonlyStatus(true, ee.getFile());
		ExplorerUtil.getTreeViewer().refresh();
		ExplorerUtil.selectMEInModelExplorer(ee.getPersistableComponent()
				.getFullPath());
		TestUtil.noToDialog(200);
		ExplorerUtil.renameItem("ee");
		while (PlatformUI.getWorkbench().getDisplay().readAndDispatch());
		assertTrue("Component was renamed after user answered no.", !ee
				.getName().equals("ee"));
		TestUtil.yesToDialog(200);
		ExplorerUtil.renameItem("ee");
		while (PlatformUI.getWorkbench().getDisplay().readAndDispatch());
		assertTrue("Component was not renamed after user answered yes.", ee
				.getName().equals("ee"));
	}

	@Test
	public void testModificationOfReadonlyActivity() {
		Package_c fpk = Package_c.PackageInstance(modelRoot,
				new Package_by_name_c("Test FPK"));
		TestUtil.changeFileReadonlyStatus(true, fpk.getFile());
		Function_c function = Function_c.FunctionInstance(modelRoot);
		CanvasTestUtilities.openActivityEditor(function);
		ActivityEditor ae = (ActivityEditor) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		TestUtil.noToDialog(200);

		Event e = new Event();
		e.character = 'a';

		ae.getTextWidget().notifyListeners(SWT.KeyDown, e);

		while (PlatformUI.getWorkbench().getDisplay().readAndDispatch());

		assertTrue("Modification to activity was allowed on readonly file", ae
				.getTextWidget().getCharCount() == 0);

		ae.close(false);

		while (PlatformUI.getWorkbench().getDisplay().readAndDispatch());

		CanvasTestUtilities.openActivityEditor(function);

		ae = (ActivityEditor) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		TestUtil.yesToDialog(200);

		ae.getTextWidget().notifyListeners(SWT.KeyDown, e);

		while (PlatformUI.getWorkbench().getDisplay().readAndDispatch());

		assertTrue(
				"Modification was not allowed after user made file writable.",
				ae.getTextWidget().getCharCount() == 1);

		ae.close(false);

	}
}
