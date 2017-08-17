package org.xtuml.bp.als.oal.test;

import java.io.StringReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtuml.bp.als.oal.OalLexer;
import org.xtuml.bp.als.oal.OalParser;
import org.xtuml.bp.als.oal.Oal_validate;
import org.xtuml.bp.core.AssignToMember_c;
import org.xtuml.bp.core.Block_c;
import org.xtuml.bp.core.Body_c;
import org.xtuml.bp.core.FunctionBody_c;
import org.xtuml.bp.core.Function_c;
import org.xtuml.bp.core.LeafSymbolicConstant_c;
import org.xtuml.bp.core.LiteralSymbolicConstant_c;
import org.xtuml.bp.core.Oalconstants_c;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.PackageableElement_c;
import org.xtuml.bp.core.SymbolicConstantValue_c;
import org.xtuml.bp.core.SymbolicConstant_c;
import org.xtuml.bp.core.TransientValueReference_c;
import org.xtuml.bp.core.Value_c;
import org.xtuml.bp.core.Variable_c;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.util.ContainerUtil;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.OrderedRunner;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.TokenStreamRecognitionException;

@RunWith(OrderedRunner.class)
public class TestScopedConstants extends BaseTest {
    
    private static boolean initialized = false;
    
    private boolean requiresClear;
    private Function_c testFunc = null;
    private static final String test_project = "9566_constants";
    private static final String test_func_package = "functions";
    private static final String test_func_name = "foo";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        if (!initialized) {
            testFunc = null;
            loadProject( test_project );
            initialized = true;
        }
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        if ( requiresClear ) {
            clearActionData();
        }
    }

    private String parseAction( String stmts ) {
        if ( null == testFunc ) {
            testFunc = getTestFunction();
        }
        requiresClear = true;
        OalLexer lexer = new OalLexer(new StringReader(stmts));
        OalParser parser = new OalParser(modelRoot, lexer);
        try {
            parser.m_oal_context = new Oal_validate( ContainerUtil.getContainer( testFunc ) );
            parser.action( testFunc.getSync_id(), Oalconstants_c.FUNCTION_TYPE);
        }
        catch (TokenStreamException e) {
            Block_c.Clearcurrentscope(modelRoot, parser.m_oal_context.m_act_id);
            if ( e instanceof TokenStreamRecognitionException ) {
                TokenStreamRecognitionException tsre = (TokenStreamRecognitionException)e;
                parser.reportError(tsre.recog);
            }
            else {
                fail("Token stream exception in parser");
            }
        }
        catch (RecognitionException e) {
            Block_c.Clearcurrentscope(modelRoot, parser.m_oal_context.m_act_id);
            parser.reportError(e);
        }
        catch (InterruptedException ie) {}
        return parser.m_output;
    }

    private void clearActionData() {
        Body_c actact = Body_c.getOneACT_ACTOnR698(FunctionBody_c.getOneACT_FNBOnR695( testFunc ));
        if (actact != null) {
            actact.Clear_blocks();
        }
        requiresClear = false;
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
    
    @Test
    public void testScopedConstants1() {
        String err = parseAction( "fail1 = non_existent::FOO;" );
        String[] lines = err.split( "\n" );
        assertEquals( 2, lines.length );
        assertEquals( ":1:9-20: Cannot find specified enumeration or constant specification ->non_existent<-.", lines[0] );
        assertEquals( "line 1:23: expecting Semicolon, found 'FOO'", lines[1] );
    }
    
    @Test
    public void testScopedConstants2() {
        String err = parseAction( "fail2 = math::E" );
        String[] lines = err.split( "\n" );
        assertEquals( 2, lines.length );
        assertEquals( ":1:9-12: Multiple constant specifications found for ->math<-: 9566_constants::constants1::math, 9566_constants::constants2::math", lines[0] );
        assertEquals( "line 1:15: expecting Semicolon, found 'E'", lines[1] );
    }
    
    @Test
    public void testScopedConstants3() {
        String err = parseAction( "fail3 = levels::HIGH;" );
        String[] lines = err.split( "\n" );
        assertEquals( 3, lines.length );
        assertEquals( ":1:17-20: Found enumerator and constant with the same name->HIGH<- for ->levels<-", lines[0] );
        assertEquals( "line 1:22: unexpected token: null", lines[1] );
        assertEquals( "line 1:22: expecting Semicolon, found 'null'", lines[2] );
    }
    
    @Test
    public void testScopedConstants4() {
        String err = parseAction( "fail4 = primary_colors::PURPLE;" );
        String[] lines = err.split( "\n" );
        assertEquals( 3, lines.length );
        assertEquals( ":1:25-30: Cannot find enumerator or constant ->PURPLE<- for ->primary_colors<-", lines[0] );
        assertEquals( "line 1:32: unexpected token: null", lines[1] );
        assertEquals( "line 1:32: expecting Semicolon, found 'null'", lines[2] );
    }
    
    @Test
    public void testScopedConstants5() {
        String err = parseAction( "fail5 = primary_colors_of_light::PURPLE;" );
        String[] lines = err.split( "\n" );
        assertEquals( 3, lines.length );
        assertEquals( ":1:34-39: Cannot find constant ->PURPLE<- for constant specification ->primary_colors_of_light<-", lines[0] );
        assertEquals( "line 1:41: unexpected token: null", lines[1] );
        assertEquals( "line 1:41: expecting Semicolon, found 'null'", lines[2] );
    }
    
    @Test
    public void testScopedConstants6() {
        String err = parseAction( "fail6 = RED;" );
        String[] lines = err.split( "\n" );
        assertEquals( 3, lines.length );
        assertEquals( ":1:9-11: There is more than one constant named ->RED<- in the system, cannot resolve.", lines[0] );
        assertEquals( "line 1:13: unexpected token: null", lines[1] );
        assertEquals( "line 1:13: expecting Semicolon, found 'null'", lines[2] );
    }
    
    @Test
    public void testScopedConstants7() {
        String err = parseAction( "success1a = primary_colors_of_light::RED;\nsuccess1b = primary_colors::RED;" );
        assertEquals( "", err );
        Variable_c success1a = Variable_c.getOneV_VAROnR823(Block_c.getManyACT_BLKsOnR612(
                Body_c.getOneACT_ACTOnR698(FunctionBody_c.getOneACT_FNBOnR695(testFunc))), new ClassQueryInterface_c() {
                    @Override
                    public boolean evaluate(Object candidate) {
                        return ((Variable_c)candidate).getName().equals("success1a");
                    }
        });
        Variable_c success1b = Variable_c.getOneV_VAROnR823(Block_c.getManyACT_BLKsOnR612(
                Body_c.getOneACT_ACTOnR698(FunctionBody_c.getOneACT_FNBOnR695(testFunc))), new ClassQueryInterface_c() {
                    @Override
                    public boolean evaluate(Object candidate) {
                        return ((Variable_c)candidate).getName().equals("success1b");
                    }
        });
        LiteralSymbolicConstant_c success1a_val = LiteralSymbolicConstant_c.getOneCNST_LSCOnR1503(LeafSymbolicConstant_c.getOneCNST_LFSCOnR1502(
                SymbolicConstant_c.getOneCNST_SYCOnR850(SymbolicConstantValue_c.getOneV_SCVOnR801(Value_c.getOneV_VALOnR609(
                AssignToMember_c.getOneACT_AIOnR689(Value_c.getOneV_VALOnR801(TransientValueReference_c.getOneV_TVLOnR805(success1a))))))));
        LiteralSymbolicConstant_c success1b_val = LiteralSymbolicConstant_c.getOneCNST_LSCOnR1503(LeafSymbolicConstant_c.getOneCNST_LFSCOnR1502(
                SymbolicConstant_c.getOneCNST_SYCOnR850(SymbolicConstantValue_c.getOneV_SCVOnR801(Value_c.getOneV_VALOnR609(
                AssignToMember_c.getOneACT_AIOnR689(Value_c.getOneV_VALOnR801(TransientValueReference_c.getOneV_TVLOnR805(success1b))))))));
        assertNotNull( success1a_val );
        assertEquals( "14", success1a_val.getValue() );
        assertNotNull( success1b_val );
        assertEquals( "10", success1b_val.getValue() );
    }
    
    @Test
    public void testScopedConstants8() {
        String err = parseAction( "success2 = directions::LEFT;" );
        assertEquals( "", err );
        Variable_c success2 = Variable_c.getOneV_VAROnR823(Block_c.getManyACT_BLKsOnR612(
                Body_c.getOneACT_ACTOnR698(FunctionBody_c.getOneACT_FNBOnR695(testFunc))), new ClassQueryInterface_c() {
                    @Override
                    public boolean evaluate(Object candidate) {
                        return ((Variable_c)candidate).getName().equals("success2");
                    }
        });
        LiteralSymbolicConstant_c success2_val = LiteralSymbolicConstant_c.getOneCNST_LSCOnR1503(LeafSymbolicConstant_c.getOneCNST_LFSCOnR1502(
                SymbolicConstant_c.getOneCNST_SYCOnR850(SymbolicConstantValue_c.getOneV_SCVOnR801(Value_c.getOneV_VALOnR609(
                AssignToMember_c.getOneACT_AIOnR689(Value_c.getOneV_VALOnR801(TransientValueReference_c.getOneV_TVLOnR805(success2))))))));
        assertNotNull( success2_val );
        assertEquals( "16", success2_val.getValue() );
    }
    
    @Test
    public void testScopedConstants9() {
        String err = parseAction( "success3 = days::WEEKDAY;" );
        assertEquals( "", err );
        Variable_c success3 = Variable_c.getOneV_VAROnR823(Block_c.getManyACT_BLKsOnR612(
                Body_c.getOneACT_ACTOnR698(FunctionBody_c.getOneACT_FNBOnR695(testFunc))), new ClassQueryInterface_c() {
                    @Override
                    public boolean evaluate(Object candidate) {
                        return ((Variable_c)candidate).getName().equals("success3");
                    }
        });
        LiteralSymbolicConstant_c success3_val = LiteralSymbolicConstant_c.getOneCNST_LSCOnR1503(LeafSymbolicConstant_c.getOneCNST_LFSCOnR1502(
                SymbolicConstant_c.getOneCNST_SYCOnR850(SymbolicConstantValue_c.getOneV_SCVOnR801(Value_c.getOneV_VALOnR609(
                AssignToMember_c.getOneACT_AIOnR689(Value_c.getOneV_VALOnR801(TransientValueReference_c.getOneV_TVLOnR805(success3))))))));
        assertNotNull( success3_val );
        assertEquals( "3", success3_val.getValue() );
    }

}
