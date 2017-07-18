//=====================================================================
//
//File:      $RCSfile: SymbolTest.java,v $
//Version:   $Revision: 1.31 $
//Modified:  $Date: 2013/05/10 05:41:51 $
//
//(c) Copyright 2004-2014 Mentor Graphics Corporation All rights reserved.
//
//=====================================================================
// Licensed under the Apache License, Version 2.0 (the "License"); you may not
// use this file except in compliance with the License.  You may obtain a copy
// of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   See the
// License for the specific language governing permissions and limitations under
// the License.
//=====================================================================
//
//This class is responsible for performing the automatic test of the
//Graphics Domain by showing specially prepared canvases that show
//unusually extreme class, package and state shapes and comparing them
//with previously created test result data. Examples of extreme shapes
//very wide and thin or very tall and thin. This exercises the label
//placement and truncation algorithms. The test data is created by the
//CanvasTestResultCreator class defined elsewhere in this package. The
//test creator class also creates a JPEG for each canvas so that the
//sample results may be viewed by a human. 
//
package org.xtuml.bp.ui.canvas.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.ui.PlatformUI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtuml.bp.core.Association_c;
import org.xtuml.bp.core.ClassAsLink_c;
import org.xtuml.bp.core.ClassAsSubtype_c;
import org.xtuml.bp.core.ClassStateMachine_c;
import org.xtuml.bp.core.Component_c;
import org.xtuml.bp.core.CorePlugin;
import org.xtuml.bp.core.End_c;
import org.xtuml.bp.core.InstanceStateMachine_c;
import org.xtuml.bp.core.LinkedAssociation_c;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.PackageableElement_c;
import org.xtuml.bp.core.Style_c;
import org.xtuml.bp.core.SubtypeSupertypeAssociation_c;
import org.xtuml.bp.core.SystemModel_c;
import org.xtuml.bp.core.common.BridgePointPreferencesStore;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.common.NonRootModelElement;
import org.xtuml.bp.test.common.OrderedRunner;
import org.xtuml.bp.test.common.TestingUtilities;
import org.xtuml.bp.test.common.UITestingUtilities;
import org.xtuml.bp.ui.canvas.ElementSpecification_c;
import org.xtuml.bp.ui.canvas.ModelSpecification_c;
import org.xtuml.bp.ui.canvas.Ooaofgraphics;
import org.xtuml.bp.ui.graphics.figures.DecoratedPolylineConnection;
import org.xtuml.bp.ui.graphics.parts.ConnectorEditPart;

@RunWith(OrderedRunner.class)
public class SymbolTest extends CanvasTest {
	public SymbolTest() {
		super(null, null);
	}

	public static boolean generateResults = false;
	private static String root = "canvastest";
	private static String test_id = "";

	@Before
	public void setUp() throws Exception {
		setModelName("canvastest");
		super.setUp();

		loadProject("canvastest");
		TestingUtilities.importTestingProjectIntoWorkspace("AssociationFeedbackTestModel");
	}

	protected String getResultName() {
		return root + "_" + test_id;
	}

	@Test
	public void testPackageDiagram() throws Exception {
		test_id = "1";
		// domain id's change each time someone creates a
		// workspace from a .bak file in BP 6.1
		Package_c d2 = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
			
			@Override
			public boolean evaluate(Object candidate) {
				return ((Package_c) candidate).getName().equals("Class Diagram Test Subsystem");
			}
		});
		validateOrGenerateResults(UITestingUtilities.getGraphicalEditorFor(d2, true), generateResults);
	}

	@Test
	public void testComponentDiagram() throws Exception {
		test_id = "2";
		Component_c comp = Component_c.ComponentInstance(modelRoot);
		validateOrGenerateResults(UITestingUtilities.getGraphicalEditorFor(comp, true), generateResults);
	}

	@Test
	public void testInstanceStateMachineDiagram() throws Exception {
		test_id = "3";
		InstanceStateMachine_c ism = InstanceStateMachine_c.InstanceStateMachineInstance(modelRoot);
		validateOrGenerateResults(UITestingUtilities.getGraphicalEditorFor(ism, true), generateResults);
	}
	
	@Test
	public void testClassStateMachineDiagram() throws Exception {
		test_id = "4";
		ClassStateMachine_c csm = ClassStateMachine_c.ClassStateMachineInstance(modelRoot);
		validateOrGenerateResults(UITestingUtilities.getGraphicalEditorFor(csm, true), generateResults);
	}
	
	// Testing if Canvas Plugin loads the elemType values properly
	private class ElementSpecification_forName_c implements
			ClassQueryInterface_c {
		private String m_name;

		ElementSpecification_forName_c(String p_name) {
			m_name = p_name;
		}

		public boolean evaluate(Object candidate) {
			ElementSpecification_c selected = (ElementSpecification_c) candidate;
			return (selected.getName().equals(m_name));
		}
	}

	// Testing if Canvas Plugin loads the modelType values properly
	private class ModelSpecification_forName_c implements ClassQueryInterface_c {
		private String m_name;

		ModelSpecification_forName_c(String p_name) {
			m_name = p_name;
		}

		public boolean evaluate(Object candidate) {
			ModelSpecification_c selected = (ModelSpecification_c) candidate;
			return (selected.getName().equals(m_name));
		}
	}

	@Test
	public void test_CanvasPlugin() {
		// *** symbol.elemType ***
		specifyElementType("Package", 108);
		specifyElementType("Component", 107);
		specifyModelType("Instance State Machine", 8);
		specifyModelType("Class State Machine", 10);
	}

	public void specifyElementType(String name, int value) {
		ElementSpecification_c es = ElementSpecification_c
				.ElementSpecificationInstance(
						Ooaofgraphics.getDefaultInstance(),
						new ElementSpecification_forName_c(name));
		assertNotNull(es);
		assertEquals(value, es.getOoa_type());
	}

	public void specifyModelType(String name, int value) {
		ModelSpecification_c ms = ModelSpecification_c
				.ModelSpecificationInstance(Ooaofgraphics.getDefaultInstance(),
						new ModelSpecification_forName_c(name));
		assertNotNull(ms);
		assertEquals(value, ms.getModel_type());
	}
	
	private NonRootModelElement getAssociationForTest(int numb, Class<?> expectedClassType) {
		SystemModel_c system = getSystemModel("AssociationFeedbackTestModel");
		Package_c pkg = Package_c.getOneEP_PKGOnR1401(system, new ClassQueryInterface_c() {
			
			@Override
			public boolean evaluate(Object candidate) {
				return ((Package_c) candidate).getName().equals("AssociationFeedback");
			}
		});
		Association_c assoc = Association_c.getOneR_RELOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(pkg), new ClassQueryInterface_c() {
			
			@Override
			public boolean evaluate(Object candidate) {
				return ((Association_c) candidate).getNumb() == numb;
			}
		});
		if(expectedClassType == Association_c.class) {
			return assoc;
		}
		if(expectedClassType == ClassAsLink_c.class) {
			return ClassAsLink_c.getOneR_ASSROnR211(LinkedAssociation_c.getManyR_ASSOCsOnR206(assoc));
		}
		if(expectedClassType == ClassAsSubtype_c.class) {
			ClassAsSubtype_c subtype = ClassAsSubtype_c.getOneR_SUBOnR213(SubtypeSupertypeAssociation_c.getManyR_SUBSUPsOnR206(assoc));
			return subtype;
		}
		// unsupported type
		return null;
	}
	
	private boolean testGetConnectorStyle(NonRootModelElement element, boolean bold) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = element.getClass().getMethod("Get_style", new Class[] {Integer.TYPE});
		Object invoke = method.invoke(element, new Object[] {End_c.Additional});
		if(bold) {
			return ((Integer) invoke) == Style_c.Bold;
		} else {
			if(element instanceof ClassAsLink_c) {
				return ((Integer) invoke) == Style_c.Broken;
			}
			return ((Integer) invoke) == Style_c.None || ((Integer) invoke) == Style_c.Solid;
		}
	}
	
	private boolean testBoldAssociationDrawing(NonRootModelElement element, boolean bold) {
		UITestingUtilities.getGraphicalEditorFor(element.getFirstParentPackage(), true);
		ConnectorEditPart editorPartFor = (ConnectorEditPart) UITestingUtilities.getEditorPartFor(element);
		DecoratedPolylineConnection connectionFigure = (DecoratedPolylineConnection) editorPartFor.getConnectionFigure();
		Image image = new Image(PlatformUI.getWorkbench().getDisplay(), new ImageData(1280, 1024, 24, new PaletteData(0,  0, 0)));
		GC gc = new GC(image);
		Graphics g = new SWTGraphics(gc);
		connectionFigure.paintFigure(g);
		// assert that the line width is 3 if bold, 1 if not
		if(bold) {
			return g.getLineWidth() == 3;
		} else {
			return g.getLineWidth() == 0;
		}
	}
	
	@Test
	public void testSimpleAssociationFormalized() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Association 2
		NonRootModelElement associationForTest = getAssociationForTest(2, Association_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, true));
	}
	@Test
	public void testSimpleAssociationUnformalized() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Association 1
		NonRootModelElement associationForTest = getAssociationForTest(1, Association_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, false));
	}
	
	@Test
	public void testLinkedAssociationFormalized() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Association 4
		NonRootModelElement associationForTest = getAssociationForTest(4, Association_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, true));
		// also test class as link
		associationForTest = getAssociationForTest(4, ClassAsLink_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, true));
	}
	@Test
	public void testLinkedAssociationUnformalized() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Association 3
		NonRootModelElement associationForTest = getAssociationForTest(3, Association_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, false));
		// also test class as link
		associationForTest = getAssociationForTest(3, ClassAsLink_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, false));		
	}

	@Test
	public void testSubSupAssociationFormalized() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Association 6
		NonRootModelElement associationForTest = getAssociationForTest(6, Association_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, true));
		// also test class as subtype
		associationForTest = getAssociationForTest(6, ClassAsSubtype_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, true));
	}
	@Test
	public void testSubSupAssociationUnformalized() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Association 5
		NonRootModelElement associationForTest = getAssociationForTest(5, Association_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, false));
		// also test class as subtype
		associationForTest = getAssociationForTest(5, ClassAsSubtype_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, false));
	}

	@Test
	public void testSimpleAssociationFormalizedBoldGraphics() {
		// Association 2
		NonRootModelElement associationForTest = getAssociationForTest(2, Association_c.class);
		assertTrue(testBoldAssociationDrawing(associationForTest, true));
	}
	@Test
	public void testSimpleAssociationUnformalizedBoldGraphics() {
		// Association 1
		NonRootModelElement associationForTest = getAssociationForTest(1, Association_c.class);
		assertTrue(testBoldAssociationDrawing(associationForTest, false));
	}
	
	@Test
	public void testLinkedAssociationFormalizedBoldGraphics() {
		// Association 4
		NonRootModelElement associationForTest = getAssociationForTest(4, Association_c.class);
		assertTrue(testBoldAssociationDrawing(associationForTest, true));
		// also test class as link
		associationForTest = getAssociationForTest(4, ClassAsLink_c.class);
		assertTrue(testBoldAssociationDrawing(associationForTest, true));
	}
	@Test
	public void testLinkedAssociationUnformalizedBoldGraphics() {
		// Association 3
		NonRootModelElement associationForTest = getAssociationForTest(3, Association_c.class);
		assertTrue(testBoldAssociationDrawing(associationForTest, false));
		// also test class as link
		associationForTest = getAssociationForTest(3, ClassAsLink_c.class);
		assertTrue(testBoldAssociationDrawing(associationForTest, false));		
	}

	@Test
	public void testSubSupAssociationFormalizedBoldGraphics() {
		// Association 6
		NonRootModelElement associationForTest = getAssociationForTest(6, Association_c.class);
		assertTrue(testBoldAssociationDrawing(associationForTest, true));
		// also test class as subtype
		associationForTest = getAssociationForTest(6, ClassAsSubtype_c.class);
		assertTrue(testBoldAssociationDrawing(associationForTest, true));
	}
	@Test
	public void testSubSupAssociationUnformalizedBoldGraphics() {
		// Association 5
		NonRootModelElement associationForTest = getAssociationForTest(5, Association_c.class);
		assertTrue(testBoldAssociationDrawing(associationForTest, false));
		// also test class as subtype
		associationForTest = getAssociationForTest(5, ClassAsSubtype_c.class);
		assertTrue(testBoldAssociationDrawing(associationForTest, false));
	}
	
	@Test
	public void testFeedbackPreferenceOffSimpleAssociatonFormalized() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Association 2
		CorePlugin.getDefault().getPreferenceStore().setValue(BridgePointPreferencesStore.SHOW_FORMALIZATIONS, false);
		NonRootModelElement associationForTest = getAssociationForTest(2, Association_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, false));
		CorePlugin.getDefault().getPreferenceStore().setValue(BridgePointPreferencesStore.SHOW_FORMALIZATIONS, true);
	}
	
	@Test
	public void testFeedbackPreferenceOffLinkedAssociatonFormalized() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Association 4
		CorePlugin.getDefault().getPreferenceStore().setValue(BridgePointPreferencesStore.SHOW_FORMALIZATIONS, false);
		NonRootModelElement associationForTest = getAssociationForTest(4, Association_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, false));
		// also test class as link
		associationForTest = getAssociationForTest(4, ClassAsLink_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, false));
		CorePlugin.getDefault().getPreferenceStore().setValue(BridgePointPreferencesStore.SHOW_FORMALIZATIONS, true);
	}

	@Test
	public void testFeedbackPreferenceOffSubSupAssociatonFormalized() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Association 6
		CorePlugin.getDefault().getPreferenceStore().setValue(BridgePointPreferencesStore.SHOW_FORMALIZATIONS, false);
		NonRootModelElement associationForTest = getAssociationForTest(6, Association_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, false));
		// also test class as subtype
		associationForTest = getAssociationForTest(6, ClassAsSubtype_c.class);
		assertTrue(testGetConnectorStyle(associationForTest, false));
		CorePlugin.getDefault().getPreferenceStore().setValue(BridgePointPreferencesStore.SHOW_FORMALIZATIONS, false);
	}

}
