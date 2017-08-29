package org.xtuml.bp.core.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFileState;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.widgets.MenuItem;
import org.junit.Test;
import org.xtuml.bp.core.Association_c;
import org.xtuml.bp.core.ClassAsAssociatedOneSide_c;
import org.xtuml.bp.core.ClassAsAssociatedOtherSide_c;
import org.xtuml.bp.core.ClassAsLink_c;
import org.xtuml.bp.core.ClassAsSimpleFormalizer_c;
import org.xtuml.bp.core.ClassAsSimpleParticipant_c;
import org.xtuml.bp.core.LinkedAssociation_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.PackageableElement_c;
import org.xtuml.bp.core.SimpleAssociation_c;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.common.NonRootModelElement;
import org.xtuml.bp.core.util.UIUtil;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.TestingUtilities;
import org.xtuml.bp.test.common.UITestingUtilities;
import org.xtuml.bp.ui.graphics.editor.GraphicalEditor;

import junit.framework.TestCase;

public class AssociationCardinalityMenuTests extends BaseTest {
	
	String modelName = "AssociationEditingCardinalityMenu";
	static Package_c pkg;
	static GraphicalEditor editor;
	
	@Override
	protected void initialSetup() throws Exception {
		TestingUtilities.importTestingProjectIntoWorkspace(modelName);
		UIUtil.dispatchAll();
		m_sys = getSystemModel(modelName);
		modelRoot = Ooaofooa.getInstance("/" + modelName + "/models/" + modelName + "/AssociationEditing/AssociationEditing.xtuml");
		pkg = Package_c.getOneEP_PKGOnR1401(m_sys,
				candidate -> ((Package_c) candidate).getName().equals("AssociationEditing"));
		editor = UITestingUtilities.getGraphicalEditorFor(pkg, true);
	}
	
	@Test
	public void testCardinalityPartOneUnformalized() {
		Association_c association = getAssociation(2);
		selectElement(association);
		testContextMenu("Cardinality::B::0..1", true, () -> {
			verifyRule(association, true, 1, 0);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::B::1", true, () -> {
			verifyRule(association, true, 0, 0);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::B::1..*", true, () -> {
			verifyRule(association, true, 0, 1);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::B::*", true, () -> {
			verifyRule(association, true, 1, 1);
			checkPersistence(true);
		});
	}
	
	@Test
	public void testCardinalityPartTwoUnformalized() {
		Association_c association = getAssociation(2);
		selectElement(association);
		testContextMenu("Cardinality::D::0..1", true, () -> {
			verifyRule(association, false, 1, 0);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::D::1", true, () -> {
			verifyRule(association, false, 0, 0);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::D::1..*", true, () -> {
			verifyRule(association, false, 0, 1);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::D::*", true, () -> {
			verifyRule(association, false, 1, 1);
			checkPersistence(true);
		});		
	}
	
	@Test
	public void testCardinalityPartOneFormalized() {
		Association_c association = getAssociation(7);
		selectElement(association);
		testContextMenu("Cardinality::E.'one'::0..1", true, () -> {
			verifyRule(association, true, 1, 0);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::E.'one'::1", true, () -> {
			verifyRule(association, true, 0, 0);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::E.'one'::1..*", true, () -> {
			verifyRule(association, true, 0, 1);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::E.'one'::*", true, () -> {
			verifyRule(association, true, 1, 1);
			checkPersistence(true);
		});		
	}
	
	@Test
	public void testCardinalityFormOneFormalized() {
		Association_c association = getAssociation(7);
		selectElement(association);
		testContextMenu("Cardinality::E.'other'::0..1", true, () -> {
			verifyRule(association, false, 1, 0);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::E.'other'::1", true, () -> {
			verifyRule(association, false, 0, 0);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::E.'other'::1..*", true, () -> {
			verifyRule(association, false, 0, 1);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::E.'other'::*", true, () -> {
			verifyRule(association, false, 1, 1);
			checkPersistence(true);
		});		
	}
	
	@Test
	public void testCardinalityPartOneLinkedFormalized() {
		Association_c association = getAssociation(1);
		selectElement(association);
		testContextMenu("Cardinality::A::0..1", true, () -> {
			verifyRule(association, true, 1, 0);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::A::1", true, () -> {
			verifyRule(association, true, 0, 0);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::A::1..*", true, () -> {
			verifyRule(association, true, 0, 1);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::A::*", true, () -> {
			verifyRule(association, true, 1, 1);
			checkPersistence(true);
		});				
	}
	
	@Test
	public void testCardinalityPartTwoLinkedFormalized() {
		Association_c association = getAssociation(1);
		selectElement(association);
		testContextMenu("Cardinality::B::0..1", true, () -> {
			verifyRule(association, false, 1, 0);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::B::1", true, () -> {
			verifyRule(association, false, 0, 0);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::B::1..*", true, () -> {
			verifyRule(association, false, 0, 1);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::B::*", true, () -> {
			verifyRule(association, false, 1, 1);
			checkPersistence(true);
		});						
	}
	
	@Test
	public void testCardinalityMultipleSelection() {
		selectElement(getAssociation(1), getAssociation(2));
		testContextMenu("Cardinality", false, null);
	}
	
	@Test
	public void testCardinalityNonAssociation() {
		selectElement(getModelClass("A"));
		testContextMenu("Cardinality", false, null);
	}
	
	@Test
	public void testCardinalityCasLinked() {
		ClassAsLink_c cas = ClassAsLink_c.getOneR_ASSROnR211(LinkedAssociation_c.getManyR_ASSOCsOnR206(getAssociation(1)));
		selectElement(cas);
		testContextMenu("Cardinality::C::{*}", true, () -> {
			verifyRule(cas, true, 0, 1);
			checkPersistence(true);
		});
		testContextMenu("Cardinality::C:: ", true, () -> {
			verifyRule(cas, true, 3, 0);
			checkPersistence(true);
		});						
	}
	
	@Test
	public void testCardinalityFilteringOneSideSimpleOneUnconditional() {
		selectElement(getAssociation(12));
		checkMenuItems(new String[] {"0..1", "1..*", "*"}, "Cardinality::FilterA");
	}
	
	@Test
	public void testCardinalityFilteringOneSideSimpleOneConditional() {
		selectElement(getAssociation(9));
		checkMenuItems(new String[] {"1", "1..*", "*"}, "Cardinality::FilterA");		
	}
	
	@Test
	public void testCardinalityFilteringOneSideSimpleManyUnconditional() {
		selectElement(getAssociation(10));
		checkMenuItems(new String[] {"1", "0..1", "*"}, "Cardinality::FilterA");		
	}
	
	@Test
	public void testCardinalityFilteringOneSideSimpleManyConditional() {
		selectElement(getAssociation(11));
		checkMenuItems(new String[] {"1", "0..1", "1..*"}, "Cardinality::FilterA");
	}

	@Test
	public void testCardinalityFilteringOtherSideSimpleOneUnconditional() {
		selectElement(getAssociation(12));
		checkMenuItems(new String[] {"0..1", "1..*", "*"}, "Cardinality::FilterB");		
	}
	
	@Test
	public void testCardinalityFilteringOtherSideSimpleOneConditional() {
		selectElement(getAssociation(9));
		checkMenuItems(new String[] {"1", "1..*", "*"}, "Cardinality::FilterB");		
	}
	
	@Test
	public void testCardinalityFilteringOtherSideSimpleManyUnconditional() {
		selectElement(getAssociation(10));
		checkMenuItems(new String[] {"1", "0..1", "*"}, "Cardinality::FilterB");		
	}
	
	@Test
	public void testCardinalityFilteringOtherSideSimpleManyConditional() {
		selectElement(getAssociation(11));
		checkMenuItems(new String[] {"1", "0..1", "1..*"}, "Cardinality::FilterB");		
	}

	@Test
	public void testCardinalityFilteringFormOneUnconditional() {
		selectElement(getAssociation(15));
		checkMenuItems(new String[] {"0..1", "1..*", "*"}, "Cardinality::FilterB");		
	}
	
	@Test
	public void testCardinalityFilteringFormOneConditional() {
		selectElement(getAssociation(16));
		checkMenuItems(new String[] {"1", "1..*", "*"}, "Cardinality::FilterB");		
	}
	
	@Test
	public void testCardinalityFilteringFormManyUnconditional() {
		selectElement(getAssociation(17));
		checkMenuItems(new String[] {"1", "0..1", "*"}, "Cardinality::FilterB");		
	}
	
	@Test
	public void testCardinalityFilteringFormManyConditional() {
		selectElement(getAssociation(18));
		checkMenuItems(new String[] {"1", "0..1", "1..*"}, "Cardinality::FilterB");		
	}

	@Test
	public void testCardinalityFilteringLinkedOneSideOneUnconditional() {
		selectElement(getAssociation(14));
		checkMenuItems(new String[] {"0..1", "1..*", "*"}, "Cardinality::FilterA");
	}
	
	@Test
	public void testCardinalityFilteringLinkedOneSideSimpleOneConditional() {
		selectElement(getAssociation(13));
		checkMenuItems(new String[] {"1", "1..*", "*"}, "Cardinality::FilterA");
	}
	
	@Test
	public void testCardinalityFilteringLinkedOneSideSimpleManyUnconditional() {
		selectElement(getAssociation(20));
		checkMenuItems(new String[] {"1", "0..1", "*"}, "Cardinality::FilterA.''");		
	}
	
	@Test
	public void testCardinalityFilteringLinkedOneSideSimpleManyConditional() {
		selectElement(getAssociation(19));
		checkMenuItems(new String[] {"1", "0..1", "1..*"}, "Cardinality::FilterB.''");		
	}

	@Test
	public void testCardinalityFilteringLinkedOtherSideUnconditional() {
		selectElement(getAssociation(14));
		checkMenuItems(new String[] {"0..1", "1..*", "*"}, "Cardinality::FilterB");		
	}
	
	@Test
	public void testCardinalityFilteringLinkedOtherSideSimpleOneConditional() {
		selectElement(getAssociation(13));
		checkMenuItems(new String[] {"1", "1..*", "*"}, "Cardinality::FilterB");		
	}
	
	@Test
	public void testCardinalityFilteringLinkedOtherSideSimpleManyUnconditional() {
		selectElement(getAssociation(20));
		checkMenuItems(new String[] {"1", "0..1", "*"}, "Cardinality::FilterA.''");				
	}
	
	@Test
	public void testCardinalityFilteringLinkedOtherSideSimpleManyConditional() {
		selectElement(getAssociation(19));
		checkMenuItems(new String[] {"1", "0..1", "1..*"}, "Cardinality::FilterB.''");				
	}

	@Test
	public void testCardinalityFilteringCasOne() {
		Association_c association = getAssociation(13);
		ClassAsLink_c cas = ClassAsLink_c.getOneR_ASSROnR211(LinkedAssociation_c.getManyR_ASSOCsOnR206(association));
		selectElement(cas);
		checkMenuItems(new String[] {"{*}"}, "Cardinality::FilterD");				
	}
	
	@Test
	public void testCardinalityFilteringCasMany() {
		Association_c association = getAssociation(14);
		ClassAsLink_c cas = ClassAsLink_c.getOneR_ASSROnR211(LinkedAssociation_c.getManyR_ASSOCsOnR206(association));
		selectElement(cas);
		checkMenuItems(new String[] {" "}, "Cardinality::FilterC");						
	}
	
	private void checkMenuItems(String[] expected, String menuPath) {
		MenuItem[] items = UITestingUtilities.getMenuItemsAtPath(editor.getCanvas().getMenu(), menuPath);
		for(int i = 0; i < expected.length; i++) {
			assertEquals("", expected[i], items[i].getText());
		}
	}
	
	public interface VerifierResult {
		public void run();
	}
	
	/**
	 * Cardinality:
	 * 
	 *  0 : 1
	 *  1 : 0..1
	 *  2 : 1..*
	 *  3 : *
	 *  
	 */
	List<String> tableItems = new ArrayList<String>();
	public void testContextMenu(String menuPath, boolean exists, VerifierResult verifier) {
		if(exists) {
			assertTrue("Unable to find menu entry", UITestingUtilities.checkItemStatusInContextMenuByPath(editor.getCanvas().getMenu(), menuPath, false));
		} else {
			assertTrue("Found the menu entry when it should not exist", UITestingUtilities.getMenuItemByPath(editor.getCanvas().getMenu(), menuPath) == null);
			return;
		}
		UITestingUtilities.activateMenuItem(editor.getCanvas().getMenu(), menuPath);
		verifier.run();
	}
	
	private Integer[] getCardinality(NonRootModelElement association, boolean oneside) {
		Integer[] result = new Integer[2];
		if(association instanceof ClassAsLink_c) {
			result[0] = -1;
			result[1] = ((ClassAsLink_c) association).getMult();
		} else {
			SimpleAssociation_c simp = SimpleAssociation_c.getOneR_SIMPOnR206((Association_c) association);
			LinkedAssociation_c linked = LinkedAssociation_c.getOneR_ASSOCOnR206((Association_c) association);
			if(simp != null) {
				ClassAsSimpleParticipant_c[] parts = ClassAsSimpleParticipant_c.getManyR_PARTsOnR207(simp);
				if(parts.length > 1) {
					if(oneside) {
						result[0] = parts[0].getCond();
						result[1] = parts[0].getMult();
					} else {
						result[0] = parts[1].getCond();
						result[1] = parts[1].getMult();					
					}
				} else {
					if(oneside) {
						result[0] = parts[0].getCond();
						result[1] = parts[0].getMult();
					} else {
						ClassAsSimpleFormalizer_c form = ClassAsSimpleFormalizer_c.getOneR_FORMOnR208(simp);
						result[0] = form.getCond();
						result[1] = form.getMult();						
					}
				}
			} else {
				if(oneside) {
					ClassAsAssociatedOneSide_c one = ClassAsAssociatedOneSide_c.getOneR_AONEOnR209(linked);
					result[0] = one.getCond();
					result[1] = one.getMult();
				} else {
					ClassAsAssociatedOtherSide_c oth = ClassAsAssociatedOtherSide_c.getOneR_AOTHOnR210(linked);
					result[0] = oth.getCond();
					result[1] = oth.getMult();
				}
			}
		}		
		return result;
	}
	
	private void verifyRule(NonRootModelElement association, boolean oneside, int cond, int mult) {
		Integer[] result = getCardinality(association, oneside);
		if(!(association instanceof ClassAsLink_c)) {
			assertTrue("Conditionality was not properly set for cardinality.", result[0] == cond);
		}
		assertTrue("Multiplicity was not properly set for cardinality.", result[1] == mult);
	}

	public void selectElement(NonRootModelElement... elements) {
		UITestingUtilities.clearGraphicalSelection();
		for(NonRootModelElement element : elements) {
			UITestingUtilities.addElementToGraphicalSelection(element);
		}
		UIUtil.dispatchAll();
	}

	public Association_c getAssociation(int numb) {
		return getAssociation(candidate -> ((Association_c) candidate).getNumb() == numb);
	}

	public Association_c getAssociation(ClassQueryInterface_c query) {
		return Association_c.getOneR_RELOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(pkg), query);
	}

	static int previousFileHistoryCount = 0;
	public void checkPersistence(boolean changeExpected) {
		try {
			IFileState[] history = pkg.getFile().getHistory(new NullProgressMonitor());
			if(changeExpected) {
				assertTrue("Change was not persisted.", history.length > previousFileHistoryCount);
			} else {
				assertFalse("Change was persisted.", history.length > previousFileHistoryCount);
			}
			previousFileHistoryCount = history.length;
		} catch (CoreException e) {
			TestCase.fail("Unable to verify persistence of change.");
		}
	}

}
