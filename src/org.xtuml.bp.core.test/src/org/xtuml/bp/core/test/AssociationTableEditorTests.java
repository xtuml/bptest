package org.xtuml.bp.core.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;
import org.junit.Test;
import org.xtuml.bp.core.Association_c;
import org.xtuml.bp.core.ClassAsLink_c;
import org.xtuml.bp.core.ClassAsSubtype_c;
import org.xtuml.bp.core.ImportedClass_c;
import org.xtuml.bp.core.LinkedAssociation_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.PackageableElement_c;
import org.xtuml.bp.core.SubtypeSupertypeAssociation_c;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.common.NonRootModelElement;
import org.xtuml.bp.test.TestUtil;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.TestingUtilities;
import org.xtuml.bp.test.common.UITestingUtilities;
import org.xtuml.bp.ui.graphics.editor.GraphicalEditor;

public class AssociationTableEditorTests extends BaseTest {

	static private Package_c pkg;
	static private GraphicalEditor editor;

	@Override
	public void initialSetup() throws Exception {
		String testElementName = "AssociationEditing";
		TestingUtilities.importTestingProjectIntoWorkspace(testElementName);
		BaseTest.dispatchEvents();
		m_sys = getSystemModel(testElementName);
		modelRoot = Ooaofooa.getInstance("/AssociationEditing/models/AssociationEditing/AssociationEditing/AssociationEditing.xtuml");
		pkg = Package_c.getOneEP_PKGOnR1401(m_sys,
				candidate -> ((Package_c) candidate).getName().equals(testElementName));
		editor = UITestingUtilities.getGraphicalEditorFor(pkg, true);
	};

	@Test
	public void testCMEAssociation() {
		selectElement(getAssociation(1));
		testContextMenu("Configure Association", new String[] { "1" }, true);
	}

	@Test
	public void testCMEMultipleAssociation() {
		selectElement(getAssociation(1), getAssociation(2));
		testContextMenu("Configure Association", new String[] { "1", "2" }, true);
	}

	@Test
	public void testCMEAssociationAndClass() {
		selectElement(getAssociation(1), getModelClass("D"));
		// no heterogeneous cme support yet
		testContextMenu(null, false);
	}

	@Test
	public void testCMESimpleAssociationAndSubtype() {
		Association_c superSub = getAssociation(3);
		selectElement(getAssociation(1),
				ClassAsSubtype_c.getOneR_SUBOnR213(SubtypeSupertypeAssociation_c.getManyR_SUBSUPsOnR206(superSub)));
		testContextMenu(null, false);
	}

	@Test
	public void testCMESimpleAssociationSupertype() {
		// no heterogeneous cme support yet
		selectElement(getAssociation(1), getAssociation(3));
		testContextMenu(null, false);
	}

	@Test
	public void testCMESupertype() {
		selectElement(getAssociation(3));
		testContextMenu(null, false);
	}

	@Test
	public void testCMELink() {
		Association_c assoc = getAssociation(1);
		ClassAsLink_c link = ClassAsLink_c.getOneR_ASSROnR211(LinkedAssociation_c.getManyR_ASSOCsOnR206(assoc));
		selectElement(link);
		testContextMenu("Configure Association", new String[] { "1" }, true);
	}

	@Test
	public void testCMESubtype() {
		Association_c superSub = getAssociation(3);
		ClassAsSubtype_c subtype = ClassAsSubtype_c.getOneR_SUBOnR213(SubtypeSupertypeAssociation_c.getManyR_SUBSUPsOnR206(superSub));
		selectElement(subtype);
		testContextMenu(null, false);
	}

	@Test
	public void testCMEClass() {
		selectElement(getModelClass("B"));
		testContextMenu(new String[] { "1", "2", "4" }, true);
	}

	@Test
	public void testCMEImportedClass() {
		selectElement(ImportedClass_c.getOneO_IOBJOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(pkg)));
		testContextMenu(new String[] { "4" }, true);
	}

	@Test
	public void testCMEClasses() {
		selectElement(getModelClass("A"), getModelClass("D"));
		testContextMenu(new String[] { "1", "2" }, true);
	}

	@Test
	public void testCMEClassImportClass() {
		selectElement(ImportedClass_c.getOneO_IOBJOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(pkg)),
				getModelClass("A"));
		testContextMenu(null, false);
	}

	@Test
	public void testCMEImportedClasses() {
		ImportedClass_c[] imports = ImportedClass_c
				.getManyO_IOBJsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(pkg));
		selectElement(imports[0], imports[1]);
		testContextMenu(new String[] { "4", "5", "6" }, true);
	}

	@Test
	public void testCMEClassesWithSameAssociation() {
		selectElement(getModelClass("A"), getModelClass("B"));
		testContextMenu(new String[] { "1", "2", "4" }, true);
	}

	@Test
	public void testCMEClassAndAttachedAssociation() {
		selectElement(getAssociation(1), getModelClass("A"));
		testContextMenu(null, false);
	}

	@Test
	public void testCMEDiagram() {
		selectElement(pkg);
		testContextMenu(new String[] { "1", "2", "4", "5", "6" }, true);
	}

	public void selectElement(NonRootModelElement... elements) {
		UITestingUtilities.clearGraphicalSelection();
		for(NonRootModelElement element : elements) {
			UITestingUtilities.addElementToGraphicalSelection(element);
		}
		BaseTest.dispatchEvents();
	}

	public Association_c getAssociation(int numb) {
		return getAssociation(candidate -> ((Association_c) candidate).getNumb() == numb);
	}

	public Association_c getAssociation(ClassQueryInterface_c query) {
		return Association_c.getOneR_RELOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(pkg), query);
	}

	public void testContextMenu(String[] expectedResults, boolean exists) {
		testContextMenu("Configure Associations", expectedResults, exists);
	}

	List<String> tableItems = new ArrayList<String>();
	public void testContextMenu(String name, String[] expectedResults, boolean exists) {
		tableItems.clear();
		if(exists) {
			assertTrue("Unable to find menu entry", UITestingUtilities.checkItemStatusInContextMenu(editor.getCanvas().getMenu(), name, "", false));
		} else {
			assertTrue("Found the menu entry when it should not exist", UITestingUtilities.getMenuItem(editor.getCanvas().getMenu(), name) == null);
			return;
		}
		TestUtil.dismissShell(shell -> {
			if (shell != null && shell.getData() instanceof Dialog) {
				Control[] children = shell.getChildren();
				TableItem[] items = TestUtil
						.getTableItems(shell);
				for(TableItem item : items) {
					tableItems.add(item.getText());
				}
				Button close = TestUtil.findButton((Composite) children[0], "Close");
				close.notifyListeners(SWT.Selection, new Event());
				return true;
			}
			return false;
		});
		UITestingUtilities.activateMenuItem(editor.getCanvas().getMenu(), name);
		for(int i = 0; i < expectedResults.length; i++) {
			assertEquals("Found unexpected table item.", expectedResults[i], tableItems.get(i));
		}
	}

}
