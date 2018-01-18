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

		String[] expectedAttrs = {"attr1"};
		doCombineDialog(ref_attr1, expectedAttrs, 0, true);
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

		// Note that if this fails it is likely due to the attributes being in
		// a different order in the dialog 
		String[] expectedAttrs = {"attr2", "attr1"};
		doCombineDialog(ref_attr1, expectedAttrs, 0, true);
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

		String[] expectedAttrs = {"attr1", "attr1"};
		doCombineDialog(ref_attr1, expectedAttrs, 0, true);
		performTest("8");

		assertTrue(ref_attr1.Actionfilter("can", "combine"));

		String[] expectedAttrs2 = {"attr1"};
		doCombineDialog(ref_attr1, expectedAttrs2, 0, true);
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
 
		String[] expectedAttrs = {"attr2"};
		doCombineDialog(ref_attr1, expectedAttrs, 0, true);
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

		String[] expectedAttrs = {"attr2"};
		doCombineDialog(ref_attr2, expectedAttrs, 0, true);
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
		Attribute_c attr_v = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("vessel_id"));
		String vessel_desc = attr_v.getDescrip();
		Attribute_c attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("fermentor_id"));
		String fermentor_desc = attr.getDescrip();
		assertTrue(attr.Actionfilter("can", "combine"));
		assertFalse(attr.Actionfilter("can", "split ref"));

		String[] expectedAttrs = {"vessel_id"};
		doCombineDialog(attr, expectedAttrs, 0, true);
		
		// Now that we've combined:
		// - make sure vessel_id is gone
		// - make sure the propagated ID fermentor_id was unaffected 
		//   by checking it's still valid on the conditioning class
		attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("fermentor_id"));
		String new_desc = attr.getDescrip();
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

		// Test that descriptions are concatenated
		String combined_desc = fermentor_desc + "\n\n" + vessel_desc;
		assertTrue("Failed to combine attribute descriptions.",
				new_desc.equals(combined_desc) );

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
	    test_id = "15";

		openTestPKGDiagram("brewing");
		ModelClass_c mc = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("fermentor"));
		Attribute_c attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("vessel_id"));
		String vessel_desc = attr.getDescrip();
		Attribute_c attr_f = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("fermentor_id"));
		String fermentor_desc = attr_f.getDescrip();
		assertTrue(attr.Actionfilter("can", "combine"));
		assertFalse(attr.Actionfilter("can", "split ref"));

		String[] expectedAttrs = {"fermentor_id"};
		doCombineDialog(attr, expectedAttrs, 0, true);

		// Now that we've combined:
		// - make sure vessel_id is gone
		// - make sure the propagated ID fermentor_id was unaffected 
		//   by checking it's still valid on the conditioning class
		attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("fermentor_id"));
		String new_desc = attr.getDescrip();
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

		// Test that descriptions are concatenated
		String combined_desc = fermentor_desc + "\n\n" + vessel_desc;
		assertTrue("Failed to combine attribute descriptions.",
				new_desc.equals(combined_desc) );

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

	@Test
	public void testCantCombineRefsFromSameAssoc() {
		test_id = "18";

		// Test case where the class only contains two ref attributes and they
		// both came in via the same assoc (because they are part of the same
		// identifier in the referred to class). In this case we should not see
		// combine or split available on the CME on either attribute.
		openTestPKGDiagram("dog_test");
		ModelClass_c mc = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("skb"));
		Attribute_c ref_attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("name"));
		assertFalse(ref_attr.Actionfilter("can", "combine"));
		assertFalse(ref_attr.Actionfilter("can", "split ref"));
		ref_attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("n"));
		assertFalse(ref_attr.Actionfilter("can", "combine"));
		assertFalse(ref_attr.Actionfilter("can", "split ref"));

		// Test case where the class contains other attributes in addition to 
		// the two referentials from the same association.  In this case, we 
		// should see combine on the cme.  When we activate combine, the list of
		// combination targets should include the other valid attributes but not
		// the one from the same association.
		mc = ModelClass_c.ModelClassInstance(modelRoot,
				new ModelClass_by_name_c("Dog"));
		ref_attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("owner_doname"));
		assertTrue(ref_attr.Actionfilter("can", "combine"));
		String[] expectedAttrs = {"dog_name", "egb_id"};
		doCombineDialog(ref_attr, expectedAttrs, 0, false);
		ref_attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("n"));
		assertTrue(ref_attr.Actionfilter("can", "combine"));
		doCombineDialog(ref_attr, expectedAttrs, 0, false);
		ref_attr = Attribute_c.getOneO_ATTROnR102(mc,
				new Attribute_by_name_c("egb_id"));
		assertTrue(ref_attr.Actionfilter("can", "combine"));
		String[] expectedAttrs3 = {"dog_name", "owner_doname", "n"};
		doCombineDialog(ref_attr, expectedAttrs3, 0, false);
	}

	private void performTest(String test_num) {
		test_id = test_num;
		GraphicalEditor ce = ((ModelEditor) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor())
				.getGraphicalEditor();
		validateOrGenerateResults(ce, generateResults);
	}
	
	private void doCombineDialog(Attribute_c attr, String[] expectedAttrs, int selectedItem, boolean doFinish) {
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
		assertEquals(expectedAttrs.length, items.length);
		for (int i = 0; i < expectedAttrs.length; ++i) {
		    assertEquals(expectedAttrs[i], items[i]);
		}
		page.Combine_withCombo.select(selectedItem);
		IWizard w = page.getWizard();
		if (doFinish) {
		    w.performFinish();
		} else {
			w.performCancel();
		}
		wd.close();
	}
}
