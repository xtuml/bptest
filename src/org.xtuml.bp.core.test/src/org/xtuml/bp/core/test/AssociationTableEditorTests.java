package org.xtuml.bp.core.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
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
import org.xtuml.bp.core.CorePlugin;
import org.xtuml.bp.core.ImportedClass_c;
import org.xtuml.bp.core.LinkedAssociation_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.PackageableElement_c;
import org.xtuml.bp.core.SubtypeSupertypeAssociation_c;
import org.xtuml.bp.core.common.BridgePointPreferencesStore;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.common.NonRootModelElement;
import org.xtuml.bp.core.editors.association.AssociationEditorTab;
import org.xtuml.bp.core.editors.association.dialogs.AssociationTableDialog;
import org.xtuml.bp.core.editors.association.editing.AssociationEditingSupport;
import org.xtuml.bp.core.ui.BinaryFormalizeOnR_RELWizard;
import org.xtuml.bp.core.ui.LinkedFormalizeOnR_RELWizard;
import org.xtuml.bp.core.util.UIUtil;
import org.xtuml.bp.test.TestUtil;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.TestingUtilities;
import org.xtuml.bp.test.common.UITestingUtilities;
import org.xtuml.bp.ui.graphics.editor.GraphicalEditor;

import junit.framework.AssertionFailedError;

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
	public void testFirstElementSelectedAndEditorStarted() {
		selectElement(getAssociation(1));
		testDialog(tab -> {
			TableViewer viewer = tab.getTableViewer();
			IStructuredSelection ss = viewer.getStructuredSelection();
			assertTrue("Incorrect selection found", ss.size() == 1);
			Association_c element = (Association_c) ss.getFirstElement();
			assertTrue("Selection was not at the proper location", viewer.getElementAt(0) == element);
			assertTrue("Found the incorrect table item at the first location.", String.valueOf(element.getNumb()).equals("1"));
			assertTrue("Editor was not enabled on initial start", viewer.isCellEditorActive());
		});
	}
	
	@Test
	public void testFormalizedButtonStateMatchesModel() {
		selectElement(getAssociation(1));
		testDialog(tab -> assertTrue("Formalize button was not checked when a formal association was selected.", tab.getFormalizeButton().isEnabled()));		
	}
	
	@Test
	public void testBadValueForNumber() {
		selectElement(getAssociation(1));
		testDialog(tab -> {
			CellEditor cellEditor = tab.getEditingSupport()[0].getActiveCellEditor();
			cellEditor.setValue("x");
			assertFalse("Invalid value was allowed to be entered for Number.", cellEditor.isValueValid());
		});				
	}
	
	@Test
	public void testBadValueForTextPhrase() {
		// this test needs the MASL char checking enabled
		CorePlugin.getDefault().getPreferenceStore().putValue(BridgePointPreferencesStore.REQUIRE_MASL_STYLE_IDENTIFIERS, "true");
		selectElement(getAssociation(1));
		testDialog(tab -> {
			tab.getTableViewer().editElement(tab.getTableViewer().getElementAt(0), 3);
			CellEditor cellEditor = tab.getEditingSupport()[3].getActiveCellEditor();
			cellEditor.setValue("!");
			tab.getTableViewer().applyEditorValue();
			CorePlugin.getDefault().getPreferenceStore().putValue(BridgePointPreferencesStore.REQUIRE_MASL_STYLE_IDENTIFIERS, "false");
			assertFalse("Invalid value was allowed to be entered for Phrase.", cellEditor.isValueValid());
		});						
		
	}
	
	@Test
	public void testNoEditorForOneSide() {
		selectElement(getAssociation(1));
		testDialog(tab -> {
			tab.getTableViewer().editElement(tab.getTableViewer().getElementAt(0), 1);
			AssociationEditingSupport editingSupport = tab.getEditingSupport()[1];
			assertNull("Cell editor was created for ClassAsAssociatedOneSide", editingSupport);
		});						
	}
	
	@Test
	public void testNoEditorForOtherSide() {
		selectElement(getAssociation(1));
		testDialog(tab -> {
			tab.getTableViewer().editElement(tab.getTableViewer().getElementAt(0), 4);
			AssociationEditingSupport editingSupport = tab.getEditingSupport()[4];
			assertNull("Cell editor was created for ClassAsAssociatedOtherSide", editingSupport);
		});						
	}

	@Test
	public void testNoEditorForLink() {
		selectElement(getAssociation(1));
		testDialog(tab -> {
			tab.getTableViewer().editElement(tab.getTableViewer().getElementAt(0), 8);
			AssociationEditingSupport editingSupport = tab.getEditingSupport()[8];
			assertNull("Cell editor was created for ClassAsLink", editingSupport);
		});						
	}

	@Test
	public void testNoEditorForOneSideBinary() {
		selectElement(getAssociation(1));
		testDialog(tab -> {
			tab.getTableViewer().editElement(tab.getTableViewer().getElementAt(1), 1);
			AssociationEditingSupport editingSupport = tab.getEditingSupport()[1];
			assertNull("Cell editor was created for ClassAsSimpleFormalizer", editingSupport);
		});						
	}

	@Test
	public void testNoEditorForOtherSideBinary() {
		selectElement(getAssociation(1));
		testDialog(tab -> {
			tab.getTableViewer().editElement(tab.getTableViewer().getElementAt(1), 4);
			AssociationEditingSupport editingSupport = tab.getEditingSupport()[4];
			assertNull("Cell editor was created for ClassAsSimpleParticipant", editingSupport);
		});						
	}

	@Test
	public void testTabTraversal() {
		selectElement(pkg);
		testDialog(tab -> {
			TableViewer viewer = tab.getTableViewer();
			testTabTraversal(viewer, 2);
			testTabTraversal(viewer, 3);
			testTabTraversal(viewer, 5);
			testTabTraversal(viewer, 6);
			testTabTraversal(viewer, 7);
		});
	}
	
	boolean wizardOpened = false;
	
	@Test
	public void testFormalizeBinary() {
		selectElement(getAssociation(2));
		testDialog(tab -> {
			tab.getTableViewer().setSelection(new StructuredSelection(getAssociation(2)));
			Button formalizeButton = tab.getFormalizeButton();
			TestUtil.dismissShell(shell -> {
				if(!shell.isDisposed() && shell.getData() instanceof BinaryFormalizeOnR_RELWizard) {
					wizardOpened = true;
					Button cancel = TestUtil.findButton(shell, "Cancel");
					cancel.notifyListeners(SWT.Selection, null);
					return true;
				}
				Button cancel = TestUtil.findButton(shell, "Cancel");
				cancel.notifyListeners(SWT.Selection, null);
				return false;
			});
			formalizeButton.setSelection(!formalizeButton.getSelection());
			formalizeButton.notifyListeners(SWT.Selection, null);
			UIUtil.dispatchAll();
		});
		assertTrue("Binary association formalize wizard did not open with configuration dialog.", wizardOpened);
	}

	@Test
	public void testUnformalizeBinary() {
		Association_c r7 = getAssociation(7);
		selectElement(r7);
		testDialog(tab -> {
			tab.getTableViewer().setSelection(new StructuredSelection(getAssociation(7)));
			Button formalizeButton = tab.getFormalizeButton();
			formalizeButton.setSelection(!formalizeButton.getSelection());
			formalizeButton.notifyListeners(SWT.Selection, null);
			UIUtil.dispatchAll();
		});
		assertFalse("Configuration dialog did not unformalize association.", r7.Isformalized());
	}
	
	@Test
	public void testFormalizeLinked() {
		wizardOpened = false;
		selectElement(getAssociation(8));
		testDialog(tab -> {
			tab.getTableViewer().setSelection(new StructuredSelection(getAssociation(8)));
			Button formalizeButton = tab.getFormalizeButton();
			TestUtil.dismissShell(shell -> {
				if(!shell.isDisposed() && shell.getData() instanceof LinkedFormalizeOnR_RELWizard) {
					wizardOpened = true;
					Button cancel = TestUtil.findButton(shell, "Cancel");
					cancel.notifyListeners(SWT.Selection, null);
					return true;
				}
				Button cancel = TestUtil.findButton(shell, "Cancel");
				cancel.notifyListeners(SWT.Selection, null);
				return false;
			});
			formalizeButton.setSelection(!formalizeButton.getSelection());
			formalizeButton.notifyListeners(SWT.Selection, null);
			UIUtil.dispatchAll();
		});		
		assertTrue("Linked association formalize wizard did not open.", wizardOpened);
	}

	@Test
	public void testUnformalizeLinked() {
		Association_c r1 = getAssociation(1);
		selectElement(r1);
		testDialog(tab -> {
			tab.getTableViewer().setSelection(new StructuredSelection(getAssociation(1)));
			Button formalizeButton = tab.getFormalizeButton();
			formalizeButton.setSelection(!formalizeButton.getSelection());
			formalizeButton.notifyListeners(SWT.Selection, null);
			UIUtil.dispatchAll();
		});
		assertFalse("Configuration dialog did not unformalize association.", r1.Isformalized());
	}

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
	
	@Test
	public void testCMEReflexive() {
		selectElement(getAssociation(7));
		testContextMenu("Configure Association", new String[] { "7" }, true);
	}

	@Test
	public void testCMEReflexiveClass() {
		selectElement(getModelClass("E"));
		testContextMenu(new String[] { "7" }, true);
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

	public void testTabTraversal(TableViewer viewer, int expectedColumn) {
		viewer.getTable().setFocus();
		Event tabEvent = new Event();
		tabEvent.detail = SWT.TRAVERSE_TAB_NEXT;
		tabEvent.character = SWT.TAB;
		tabEvent.doit = true;
		tabEvent.keyCode = SWT.TAB;
		Control[] children = viewer.getTable().getChildren();
		Control child = children[children.length - 1];
		child.notifyListeners(SWT.Traverse, tabEvent);
		assertTrue("After tab traversal the next editor was not active.", viewer.isCellEditorActive());
		ViewerCell focusCell = viewer.getColumnViewerEditor().getFocusCell();
		assertTrue("Tab traversal did not move to the next editable cell.",
				focusCell.getColumnIndex() == expectedColumn);
		BaseTest.dispatchEvents();
	}
	
	interface DialogTest {
		public void doTest(AssociationEditorTab tab);
	}
	
	Throwable error = null;
	public void testDialog(DialogTest test) {
		// all tests can work against the diagram cme
		selectElement(pkg);
		TestUtil.dismissShell(shell -> {
			if(shell != null && shell.getData() instanceof AssociationTableDialog) {
				AssociationTableDialog tableDialog = (AssociationTableDialog) shell.getData();
				AssociationEditorTab tab = (AssociationEditorTab) tableDialog.createdTab;
				try {
					test.doTest(tab);
					Button close = TestUtil.findButton(shell, "Close");
					close.notifyListeners(SWT.Selection, new Event());
					return true;
				} catch (AssertionFailedError e) {
					error = e;
					Button close = TestUtil.findButton(shell, "Close");
					close.notifyListeners(SWT.Selection, new Event());
					return true;
				} finally {
					if(!shell.isDisposed()) {
						Button close = TestUtil.findButton(shell, "Close");
						close.notifyListeners(SWT.Selection, new Event());	
					}
				}
			}
			return false;
		});
		UITestingUtilities.activateMenuItem(editor.getCanvas().getMenu(), "Configure Associations");
		if(error != null) {
			Throwable t = error;
			error = null;
			// re-throw errors on the correct thread
			throw new AssertionFailedError(t.getMessage());
		}
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
