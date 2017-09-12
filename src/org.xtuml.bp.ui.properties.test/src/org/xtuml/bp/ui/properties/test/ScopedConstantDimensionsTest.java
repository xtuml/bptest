package org.xtuml.bp.ui.properties.test;

import org.eclipse.ui.views.properties.IPropertySource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtuml.bp.core.Dimensions_c;
import org.xtuml.bp.core.Function_c;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.PackageableElement_c;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.ui.cells.editors.DimensionsValidator;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.OrderedRunner;
import org.xtuml.bp.ui.properties.FunctionS_SYNCPropertySource;

@RunWith(OrderedRunner.class)
public class ScopedConstantDimensionsTest extends BaseTest {
    
    private static boolean initialized = false;

    private static final String test_project = "9566_constants";
    private static final String test_func_package = "constants1";
    private static final String test_func_name = "bar";
    
    private Function_c testFunc;
    private DimensionsValidator validator;
    private IPropertySource propertySource;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        if (!initialized) {
            loadProject( test_project );
            initialized = true;
        }
        testFunc = getTestFunction();
        validator = new DimensionsValidator( testFunc );
        propertySource = new FunctionS_SYNCPropertySource( testFunc );
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    private Function_c getTestFunction() {
        return Function_c.getOneS_SYNCOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(
                Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
            @Override
            public boolean evaluate(Object selected) {
                return ((Package_c)selected).getName().equals( test_func_package );
            }
        })), new ClassQueryInterface_c() {
            @Override
            public boolean evaluate(Object selected) {
                return ((Function_c)selected).getName().equals( test_func_name );
            }
        });
    }
    
    private void setDimensions( String input ) {
        propertySource.setPropertyValue( "Return_Dimensions", input );
        dispatchEvents();
    }
    
    @Test
    public void testScopedConstantsDimensions1() {
        String input = "[non_existent::FOO]";
        assertEquals( "No constant specification found with name non_existent", validator.isValid( input ) );
    }

    @Test
    public void testScopedConstantsDimensions2() {
        String input = "[primary_colors_of_light::PURPLE]";
        assertEquals( "primary_colors_of_light::PURPLE is not a valid constant name", validator.isValid( input ) );
    }

    @Test
    public void testScopedConstantsDimensions3() {
        String input = "[NON_EXISTENT]";
        assertEquals( "NON_EXISTENT is not a valid constant name", validator.isValid( input ) );
    }

    @Test
    public void testScopedConstantsDimensions4() {
        String input = "[RED]";
        assertEquals( "RED is not a valid constant name", validator.isValid( input ) );
    }

    @Test
    public void testScopedConstantsDimensions5() {
        String input = "[primary_colors::RED]";
        assertEquals( "", validator.isValid( input ) );
        setDimensions( input );
        Dimensions_c returnDimensions = Dimensions_c.getOneS_DIMOnR51( testFunc );
        assertNotNull( returnDimensions );
        assertEquals( 10, returnDimensions.getElementcount() );
    }

    @Test
    public void testScopedConstantsDimensions6() {
        String input = "[YELLOW]";
        assertEquals( "", validator.isValid( input ) );
        setDimensions( input );
        Dimensions_c returnDimensions = Dimensions_c.getOneS_DIMOnR51( testFunc );
        assertNotNull( returnDimensions );
        assertEquals( 11, returnDimensions.getElementcount() );
    }

}
