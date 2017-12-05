//=====================================================================
//
//File:      $RCSfile: CombineSplitReferentialsTestGenerics.java,v $
//Version:   $Revision: 1.5 $
//Modified:  $Date: 2013/05/10 04:30:24 $
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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtuml.bp.core.Attribute_c;
import org.xtuml.bp.core.ModelClass_c;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.UserDataType_c;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.common.TransactionManager;
import org.xtuml.bp.core.ui.CombineWithOnO_ATTRAction;
import org.xtuml.bp.core.ui.CombineWithOnO_ATTRWizardPage1;
import org.xtuml.bp.core.ui.Selection;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.CanvasTestUtils;
import org.xtuml.bp.test.common.OrderedRunner;
import org.xtuml.bp.ui.canvas.Cl_c;
import org.xtuml.bp.ui.canvas.test.CanvasTest;
import org.xtuml.bp.ui.graphics.editor.GraphicalEditor;
import org.xtuml.bp.ui.graphics.editor.ModelEditor;

@RunWith(OrderedRunner.class)
public class CombineSplitReferentialsTestGenerics extends CanvasTest {

	String test_id = null;
	private static boolean generateResults = false;
	private static boolean initialized = false;
	private static Selection selection = Selection.getInstance();

	public CombineSplitReferentialsTestGenerics(){
		super(null, null);
	}

	protected String getResultName() {
		return "CombineSplitReferentials" + "_" + test_id;
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		if (!initialized) {
			loadProject("CombineSplitReferentialsTest");
			initialized = true;
		}
		Display d = Display.getCurrent();
		while (d.readAndDispatch());
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	public void setGenerateResults() {
		try {
			generateResults = true;
			setUp();
			testSelectNonReferentialAttribute();
			testClassWithOneReferentialAttribute();
			testReferentialAttributesWithDiffBaseTypes();
			testTwoReferentialAttributesWithSameBaseTypes();
			testTwoReferentialAttributesSameBaseTypesOneDifferent();
			testThreeReferentials();
			testCombineTwoCombinedRefs();
			testCombineRefWithIDRef();
		} catch (Exception e) {
			System.out.println("Exception encountered by test result creator: "
					+ e);
		}

	}

	public void openTestPKGDiagram(String title) {
		Package_c uut = Package_c.PackageInstance(modelRoot,
				new Package_by_name_c(title));
		CanvasTestUtils.openCanvasEditor(uut);
	}
	
	@Test
	public void testSelectNonReferentialAttribute() {
		test_id = "1";

		openTestPKGDiagram("TestCombineSplitReferentials");
		ModelClass_c mc = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("testNonRefAttr"));
		Attribute_c uut = Attribute_c.getOneO_ATTROnR102(mc);
		assertFalse(uut.Actionfilter("can", "combine"));
		assertFalse(uut.Actionfilter("can", "split ref"));
	}

	@Test
	public void testClassWithOneReferentialAttribute() {
		// only 1 referential attribute in class
		test_id = "2";

		openTestPKGDiagram("TestCombineSplitReferentials");
		ModelClass_c mc = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("testOneRefAttr"));
		Attribute_c ref_attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("attr1"));
		assertFalse(ref_attr.Actionfilter("can", "combine"));
		assertFalse(ref_attr.Actionfilter("can", "split ref"));
	}

	@Test
	public void testReferentialAttributesWithDiffBaseTypes() {
		test_id = "3";

		openTestPKGDiagram("TestCombineSplitReferentials");
		ModelClass_c mc = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("testRefAttrsDiffBaseType"));
		Attribute_c ref_attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("attr1"));
		assertTrue(ref_attr.Actionfilter("can", "combine"));
		assertFalse(ref_attr.Actionfilter("can", "split ref"));
	}

	@Test
	public void testTwoReferentialAttributesWithSameBaseTypes() {
		openTestPKGDiagram("TestCombineSplitReferentials");
		ModelClass_c mc = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("testTwoRefAttr"));
		Attribute_c[] ref_attrs = Attribute_c.getManyO_ATTRsOnR102(mc);
		Attribute_c ref_attr1 = ref_attrs[0];
		assertTrue(ref_attr1.Actionfilter("can", "combine"));
		assertFalse(ref_attr1.Actionfilter("can", "split ref"));

		Cl_c.Clearselection();
		selection.addToSelection(ref_attr1);

		Action a = new Action() {
		};
		CombineWithOnO_ATTRAction cwooa = new CombineWithOnO_ATTRAction();
		cwooa.setActivePart(a, PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart());
		IStructuredSelection structuredSelection = (IStructuredSelection) selection
				.getSelection();
		WizardDialog wd = cwooa.O_ATTR_CombineWith(structuredSelection);
		CombineWithOnO_ATTRWizardPage1 page = (CombineWithOnO_ATTRWizardPage1) wd
				.getCurrentPage();
		String[] items = page.Combine_withCombo.getItems();
		assertEquals(1, items.length);
		assertEquals("attr1", items[0]);
		page.Combine_withCombo.select(0);
		IWizard w = page.getWizard();
		w.performFinish();
		wd.close();
		performTest("4");

	}

	@Test
	public void testTwoReferentialAttributesSameBaseTypesOneDifferent() {
		openTestPKGDiagram("TestCombineSplitReferentials");
		ModelClass_c mc = ModelClass_c
				.ModelClassInstance(modelRoot, new ModelClass_by_name_c(
						"testTwoRefAttrSameBaseOneRefAttrDiff"));
		Attribute_c[] ref_attrs = Attribute_c.getManyO_ATTRsOnR102(mc);
		Attribute_c ref_attr1 = ref_attrs[1];
		assertTrue(ref_attr1.Actionfilter("can", "combine"));
		assertFalse(ref_attr1.Actionfilter("can", "split ref"));

		Cl_c.Clearselection();
		selection.addToSelection(ref_attr1);

		Action a = new Action() {
		};
		CombineWithOnO_ATTRAction cwooa = new CombineWithOnO_ATTRAction();
		cwooa.setActivePart(a, PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart());
		IStructuredSelection structuredSelection = (IStructuredSelection) selection
				.getSelection();
		WizardDialog wd = cwooa.O_ATTR_CombineWith(structuredSelection);
		CombineWithOnO_ATTRWizardPage1 page = (CombineWithOnO_ATTRWizardPage1) wd
				.getCurrentPage();
		String[] items = page.Combine_withCombo.getItems();
		assertEquals(2, items.length);
		int comboSelection = 0;
		if (items[0].equals("attr1")) {
		    assertEquals("attr2", items[1]);
		} else {
			assertEquals("attr1", items[1]);
			comboSelection = 1;
		}
		page.Combine_withCombo.select(comboSelection);
		IWizard w = page.getWizard();
		w.performCancel();
		wd.close();
		performTest("6");
	}

	@Test
	public void testThreeReferentials() {
		openTestPKGDiagram("TestCombineSplitReferentials");
		ModelClass_c mc = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("testThreeRefAttrs"));
		Attribute_c[] ref_attrs = Attribute_c.getManyO_ATTRsOnR102(mc);
		Attribute_c ref_attr1 = ref_attrs[0];
		assertTrue(ref_attr1.Actionfilter("can", "combine"));
		assertFalse(ref_attr1.Actionfilter("can", "split ref"));

		Cl_c.Clearselection();
		selection.addToSelection(ref_attr1);

		Action a = new Action() {
		};
		CombineWithOnO_ATTRAction cwooa = new CombineWithOnO_ATTRAction();
		cwooa.setActivePart(a, PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart());
		IStructuredSelection structuredSelection = (IStructuredSelection) selection
				.getSelection();
		WizardDialog wd = cwooa.O_ATTR_CombineWith(structuredSelection);
		CombineWithOnO_ATTRWizardPage1 page = (CombineWithOnO_ATTRWizardPage1) wd
				.getCurrentPage();
		String[] items = page.Combine_withCombo.getItems();
		assertEquals(2, items.length);
		assertEquals("attr1", items[0]);
		assertEquals("attr1", items[1]);
		page.Combine_withCombo.select(0);
		IWizard w = page.getWizard();
		w.performFinish();
		wd.close();
		performTest("8");

		assertTrue(ref_attr1.Actionfilter("can", "combine"));

		Cl_c.Clearselection();
		selection.addToSelection(ref_attr1);

		a = new Action() {
		};
		cwooa = new CombineWithOnO_ATTRAction();
		cwooa.setActivePart(a, PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart());
		structuredSelection = (IStructuredSelection) selection.getSelection();
		wd = cwooa.O_ATTR_CombineWith(structuredSelection);
		page = (CombineWithOnO_ATTRWizardPage1) wd.getCurrentPage();
		items = page.Combine_withCombo.getItems();
		assertEquals(1, items.length);
		assertEquals("attr1", items[0]);
		page.Combine_withCombo.select(0);
		w = page.getWizard();
		w.performFinish();

		performTest("9");

		assertFalse(ref_attr1.Actionfilter("can", "combine"));

	}

	@Test
	public void testCombineTwoCombinedRefs() {
		openTestPKGDiagram("TestCombineSplitReferentials");
		ModelClass_c mc = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("testTwoCombinedRefAttrs"));
		Attribute_c[] ref_attrs = Attribute_c.getManyO_ATTRsOnR102(mc);
		Attribute_c ref_attr1 = ref_attrs[0];
		assertTrue(ref_attr1.Actionfilter("can", "combine"));
		assertTrue(ref_attr1.Actionfilter("can", "split ref"));

		Cl_c.Clearselection();
		selection.addToSelection(ref_attr1);

		Action a = new Action() {
		};
		CombineWithOnO_ATTRAction cwooa = new CombineWithOnO_ATTRAction();
		cwooa.setActivePart(a, PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart());
		IStructuredSelection structuredSelection = (IStructuredSelection) selection
				.getSelection();
		WizardDialog wd = cwooa.O_ATTR_CombineWith(structuredSelection);
		CombineWithOnO_ATTRWizardPage1 page = (CombineWithOnO_ATTRWizardPage1) wd
				.getCurrentPage();
		String[] items = page.Combine_withCombo.getItems();
		assertEquals(1, items.length);
		assertEquals("attr2", items[0]);
		page.Combine_withCombo.select(0);
		IWizard w = page.getWizard();
		w.performFinish();
		wd.close();
		performTest("12");

	}

	@Test
	public void testCombineRefWithIDRef() {
		openTestPKGDiagram("TestCombineSplitReferentials");
		ModelClass_c mc = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("testCombineRefWithIDRef"));
		Attribute_c[] ref_attrs = Attribute_c.getManyO_ATTRsOnR102(mc);
		Attribute_c ref_attr2 = ref_attrs[1];
		assertTrue(ref_attr2.Actionfilter("can", "combine"));
		assertFalse(ref_attr2.Actionfilter("can", "split ref"));

		Cl_c.Clearselection();
		selection.addToSelection(ref_attr2);

		Action a = new Action() {
		};
		CombineWithOnO_ATTRAction cwooa = new CombineWithOnO_ATTRAction();
		cwooa.setActivePart(a, PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart());
		IStructuredSelection structuredSelection = (IStructuredSelection) selection
				.getSelection();
		WizardDialog wd = cwooa.O_ATTR_CombineWith(structuredSelection);
		CombineWithOnO_ATTRWizardPage1 page = (CombineWithOnO_ATTRWizardPage1) wd
				.getCurrentPage();
		String[] items = page.Combine_withCombo.getItems();
		assertEquals(1, items.length);
		assertEquals("attr2", items[0]);
		page.Combine_withCombo.select(0);
		IWizard w = page.getWizard();
		w.performFinish();
		wd.close();
		performTest("13");
	}

	@Test
	public void testCombineBaseIDWithRef() {
		// Combine base identifier with referential of same type starting with the base. Verify that
		// non-identifier base attributes are excluded as well as referentials of differing type.
	    test_id = "14";

		openTestPKGDiagram("brewing");
		ModelClass_c mc = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("fermentor"));
		Attribute_c attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("fermentor_id"));
		assertTrue(attr.Actionfilter("can", "combine"));
		assertFalse(attr.Actionfilter("can", "split ref"));

		Cl_c.Clearselection();
		selection.addToSelection(attr);

		Action a = new Action() {
		};
		CombineWithOnO_ATTRAction cwooa = new CombineWithOnO_ATTRAction();
		cwooa.setActivePart(a, PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart());
		IStructuredSelection structuredSelection = (IStructuredSelection) selection
				.getSelection();
		WizardDialog wd = cwooa.O_ATTR_CombineWith(structuredSelection);
		CombineWithOnO_ATTRWizardPage1 page = (CombineWithOnO_ATTRWizardPage1) wd
				.getCurrentPage();
		String[] items = page.Combine_withCombo.getItems();
		assertEquals(1, items.length);
		assertEquals("vessel_id", items[0]);
		page.Combine_withCombo.select(0);
		IWizard w = page.getWizard();
		w.performFinish();
		wd.close();
		
		// Now that we've combined:
		// - make sure vessel_id is gone
		// - make sure the propagated ID fermentor_id was unaffected 
		//   by checking it's still valid on the conditioning class
		attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("vessel_id"));
		assertTrue("Combination of fermentation_id and vessel_id failed.",
				attr == null);
		ModelClass_c mc2 = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("conditioning"));
		attr = Attribute_c.getOneO_ATTROnR102(mc2,
				new Attribute_by_name_c("fermentor_id"));
		assertTrue("Combination of fermentation_id and vessel_id had undesired fallout.",
				attr != null);
		
		performTest(test_id);
/* TODO - test that descriptions are concatenated
*/

		// Undo the combination
		// undo the paste
		TransactionManager.getSingleton().getUndoAction().run();
		BaseTest.dispatchEvents(0);
		attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("vessel_id"));
		assertTrue("Undo did not restore original attributes that were combined.",
				attr != null);
	}

	@Test
	public void testCombineRefWithBaseID() {
		// Combine base identifier with referential of same type starting with the base. Verify that
		// non-identifier base attributes are excluded as well as referentials of differing type.
//		2) fermentor.vessel_id - combine with available and fermentor_id only option, after
//		  combination left with fermentor_id
	    test_id = "15";

		openTestPKGDiagram("brewing");
		ModelClass_c mc = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("fermentor"));
		Attribute_c attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("vessel_id"));
		assertTrue(attr.Actionfilter("can", "combine"));
		assertFalse(attr.Actionfilter("can", "split ref"));

		Cl_c.Clearselection();
		selection.addToSelection(attr);

		Action a = new Action() {
		};
		CombineWithOnO_ATTRAction cwooa = new CombineWithOnO_ATTRAction();
		cwooa.setActivePart(a, PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart());
		IStructuredSelection structuredSelection = (IStructuredSelection) selection
				.getSelection();
		WizardDialog wd = cwooa.O_ATTR_CombineWith(structuredSelection);
		CombineWithOnO_ATTRWizardPage1 page = (CombineWithOnO_ATTRWizardPage1) wd
				.getCurrentPage();
		String[] items = page.Combine_withCombo.getItems();
		assertEquals(1, items.length);
		assertEquals("fermentor_id", items[0]);
		page.Combine_withCombo.select(0);
		IWizard w = page.getWizard();
		w.performFinish();
		wd.close();
		// Now that we've combined:
		// - make sure vessel_id is gone
		// - make sure the propagated ID fermentor_id was unaffected 
		//   by checking it's still valid on the conditioning class
		attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("vessel_id"));
		assertTrue("Combination of fermentation_id and vessel_id failed.",
				attr == null);
		ModelClass_c mc2 = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("conditioning"));
		attr = Attribute_c.getOneO_ATTROnR102(mc2,
				new Attribute_by_name_c("fermentor_id"));
		assertTrue("Combination of fermentation_id and vessel_id had undesired fallout.",
				attr != null);
		
		performTest(test_id);
/* TODO - test that descriptions are concatenated
*/

		// Undo the combination
		// undo the paste
		TransactionManager.getSingleton().getUndoAction().run();
		BaseTest.dispatchEvents(0);
		attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("vessel_id"));
		assertTrue("Undo did not restore original attributes that were combined.",
				attr != null);
	}

	@Test
	public void testRefWithNoCombinationPossibilities() {
		test_id = "16";

		openTestPKGDiagram("brewing");
		ModelClass_c mc = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("fermentor"));
		Attribute_c ref_attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("name"));
		assertFalse(ref_attr.Actionfilter("can", "combine"));
		assertFalse(ref_attr.Actionfilter("can", "split ref"));
	}

	@Test
	public void testBaseWithNoCombinationPossibilities() {
		test_id = "17";

		openTestPKGDiagram("brewing");
		ModelClass_c mc = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("fermentor"));
		Attribute_c ref_attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("label"));
		assertFalse(ref_attr.Actionfilter("can", "combine"));
		assertFalse(ref_attr.Actionfilter("can", "split ref"));
	}

	private void performTest(String test_num) {
		test_id = test_num;
		GraphicalEditor ce = ((ModelEditor) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor())
				.getGraphicalEditor();
		generateResults = true;
		validateOrGenerateResults(ce, generateResults);
	}
}
