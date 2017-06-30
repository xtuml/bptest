package org.xtuml.bp.ui.properties.test;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.Test;
import org.xtuml.bp.core.Attribute_c;
import org.xtuml.bp.core.ModelClass_c;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.PackageableElement_c;
import org.xtuml.bp.core.ui.Selection;
import org.xtuml.bp.core.util.UIUtil;
import org.xtuml.bp.test.common.BaseTest;

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
	}
	
	/**
	 * test Attribute custom sorter
	 */
	String[] expectedResult = { "Array Dimensions", "Attribute Name", "Attribute Name Prefix",
			"Prefix Mode", "Attribute Root Name", "Default Value", "Description", "Type" };
	@Test
	public void testAttributeCustomOrdering() {
		Package_c pkg = createPackage();
		ModelClass_c clazz = createClass(pkg);
		Attribute_c attr = createAttribute(clazz);
		Selection.getInstance().clear(); Selection.getInstance().addToSelection(attr);
		testPropertiesOrder(expectedResult);
	}

	private Package_c createPackage() {
		m_sys.Newpackage();
		Package_c[] pkgs = Package_c.getManyEP_PKGsOnR1405(m_sys);
		return pkgs[pkgs.length - 1];
	}
	
	private ModelClass_c createClass(Package_c pkg) {
		pkg.Newclass();
		ModelClass_c[] classes = ModelClass_c.getManyO_OBJsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(pkg));
		return classes[classes.length - 1];
	}
	
	private Attribute_c createAttribute(ModelClass_c clazz) {
		clazz.Newattribute();
		Attribute_c[] attrs = Attribute_c.getManyO_ATTRsOnR102(clazz);
		return attrs[attrs.length - 1];
	}

	private void testPropertiesOrder(String[] expectedResults) {
		Tree propertyTree = UIUtil.getPropertyTree();
		assertEquals(true, propertyTree.getItems().length != 0);
		TreeItem parent = propertyTree.getItems()[0];
		TreeItem[] items = parent.getItems();
		int count = 0;
		for(TreeItem child : items) {
			assertTrue("Expected item: " + expectedResults[0] + " was not found in correct location [index == " + count
					+ "]", child.getText().equals(expectedResults[count]));
			count += 1;
		}
	}

}
