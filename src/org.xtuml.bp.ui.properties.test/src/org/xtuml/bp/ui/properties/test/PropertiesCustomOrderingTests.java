package org.xtuml.bp.ui.properties.test;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.junit.Test;
import org.xtuml.bp.core.Attribute_c;
import org.xtuml.bp.core.ImportedClass_c;
import org.xtuml.bp.core.ModelClass_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.PackageableElement_c;
import org.xtuml.bp.core.common.ModelElement;
import org.xtuml.bp.core.common.NonRootModelElement;
import org.xtuml.bp.core.common.Transaction;
import org.xtuml.bp.core.common.TransactionException;
import org.xtuml.bp.core.common.TransactionManager;
import org.xtuml.bp.core.util.UIUtil;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.ExplorerUtil;
import org.xtuml.bp.test.common.UITestingUtilities;

/**
 * This test suite provides tests relating to custom
 * ordering in the properties view.  By default entries
 * are ordered by name.
 * 
 * @author Travis London
 *
 */
public class PropertiesCustomOrderingTests extends BaseTest {
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
	}
	
	/**
	 * test Attribute custom sorter
	 */
	String[] expectedChildResult = { "Array Dimensions", "Attribute Name", "Attribute Name Prefix",
			"Attribute Prefix Mode", "Attribute Root Name", "Default Value", "Description", "New Base Attribute", "Type" };
	@Test
	public void testAttributeSelectedOrdering() throws TransactionException, PartInitException {
		Package_c pkg = createPackage();
		ModelClass_c clazz = createClass(pkg);
		Attribute_c attr = createAttribute(clazz);
		addElementToSelection(attr);
		while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
		testPropertiesOrder(expectedChildResult, "Attribute Name");
	}

	@Test
	public void testAttributeParentPackageSelected() throws TransactionException, PartInitException {
		Package_c pkg = createPackage();
		ModelClass_c clazz = createClass(pkg);
		createAttribute(clazz);
		addElementToSelection(pkg);
		while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
		testPropertiesOrder(expectedChildResult, "Attribute Name");		
	}

	@Test
	public void testAttributeParentModelClassSelected() throws TransactionException, PartInitException {
		Package_c pkg = createPackage();
		ModelClass_c clazz = createClass(pkg);
		createAttribute(clazz);
		addElementToSelection(clazz);
		while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
		testPropertiesOrder(expectedChildResult, "Attribute Name");		
	}

	@Test
	public void testAttributeParentSystemSelected() throws TransactionException, PartInitException {
		Package_c pkg = createPackage();
		ModelClass_c clazz = createClass(pkg);
		createAttribute(clazz);
		addElementToSelection(m_sys);
		while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
		testPropertiesOrder(expectedChildResult, "Attribute Name");		
	}

	@Test
	public void testImportedClassSelectedOrdering() throws TransactionException, PartInitException {
		Package_c pkg = createPackage();
		Package_c import_pkg = createPackage();
		ModelClass_c clazz = createClass(pkg);
		ImportedClass_c importClazz = createIClass(import_pkg, clazz);
		createAttribute(clazz);
		addElementToSelection(importClazz);
		while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
		testPropertiesOrder(expectedChildResult, "Attribute Name");
	}
	
	private void addElementToSelection(NonRootModelElement element) {
		// for imported classes 
		if(element instanceof ImportedClass_c) {
			UITestingUtilities.getGraphicalEditorFor(element, false);
			UITestingUtilities.addElementToGraphicalSelection(element);
		} else {
			TreeItem item = ExplorerUtil.findItem(element);
			ExplorerUtil.selectItem(item);
		}
	}

	private Package_c createPackage() throws TransactionException {
		Transaction transaction = TransactionManager.getSingleton().startTransaction("", new ModelElement[] {Ooaofooa.getDefaultInstance()});
		m_sys.Newpackage();
		TransactionManager.getSingleton().endTransaction(transaction);
		while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
		Package_c[] pkgs = Package_c.getManyEP_PKGsOnR1405(m_sys);
		return pkgs[pkgs.length - 1];
	}
	
	private ModelClass_c createClass(Package_c pkg) throws TransactionException {
		Transaction transaction = TransactionManager.getSingleton().startTransaction("", new ModelElement[] {Ooaofooa.getDefaultInstance()});
		pkg.Newclass();
		TransactionManager.getSingleton().endTransaction(transaction);
		while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
		ModelClass_c[] classes = ModelClass_c.getManyO_OBJsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(pkg));
		return classes[classes.length - 1];
	}

	private ImportedClass_c createIClass(Package_c pkg, ModelClass_c clazz) throws TransactionException {
		Transaction transaction = TransactionManager.getSingleton().startTransaction("", new ModelElement[] {Ooaofooa.getDefaultInstance()});
		pkg.Newiclass();
		ImportedClass_c[] imports = ImportedClass_c.getManyO_IOBJsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(pkg));
		ImportedClass_c iclass = imports[imports.length - 1];
		iclass.relateAcrossR101To(clazz);
		TransactionManager.getSingleton().endTransaction(transaction);
		while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
		return iclass;
	}

	private Attribute_c createAttribute(ModelClass_c clazz) throws TransactionException {
		Transaction transaction = TransactionManager.getSingleton().startTransaction("", new ModelElement[] {Ooaofooa.getDefaultInstance()});
		clazz.Newattribute();
		Attribute_c[] attrs = Attribute_c.getManyO_ATTRsOnR102(clazz);
		TransactionManager.getSingleton().endTransaction(transaction);
		while(PlatformUI.getWorkbench().getDisplay().readAndDispatch());
		return attrs[attrs.length - 1];
	}

	private void testPropertiesOrder(String[] expectedResults, String childName) throws PartInitException {
		Tree propertyTree = UIUtil.getPropertyTree();
		propertyTree.redraw();
		propertyTree.update();
		TreeItem item = UITestingUtilities.findItemInTreeWithExpansion(propertyTree, childName);
		assertNotNull(item);
		assertEquals(true, item.getParentItem().getItems().length != 0);
		int count = 0;
		for(TreeItem child : item.getParentItem().getItems()) {
			if(expectedResults[count].equals("New Base Attribute")) {
				continue;
			}
			assertTrue("Expected item: " + expectedResults[count] + " was not found in correct location [index == " + count
					+ "]", child.getText().equals(expectedResults[count]));
			count += 1;
		}
	}

}
