package org.xtuml.bp.debug.test;

import java.lang.reflect.Method;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xtuml.bp.als.oal.ParserAllActivityModifier;
import org.xtuml.bp.core.ComponentInstance_c;
import org.xtuml.bp.core.Component_c;
import org.xtuml.bp.core.CorePlugin;
import org.xtuml.bp.core.Function_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.PackageableElement_c;
import org.xtuml.bp.core.SystemModel_c;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.common.IAllActivityModifier;
import org.xtuml.bp.core.common.NonRootModelElement;
import org.xtuml.bp.core.ui.Selection;
import org.xtuml.bp.core.ui.perspective.BridgePointPerspective;
import org.xtuml.bp.debug.ui.launch.BPDebugUtils;
import org.xtuml.bp.debug.ui.test.DebugUITestUtilities;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.TestingUtilities;
import org.xtuml.bp.ui.canvas.test.*;

public class UdtConstantTest extends CanvasTest {

    public static boolean generateResults = false;
    public static boolean useDrawResults = true;

    String test_id = "";

    // test variables
    private static final String PROJECT_NAME = "test_udt_constants";
    private static final String LIBRARY_PACKAGE = "lib";
    private static final String DOMAIN_NAME = "test";
    private static boolean initialized = false;

    private boolean parseSucceeded;
    private Component_c domain;
    private ComponentInstance_c runningEngine;
    
    private static int numTests;
    private static int currentTest;
    

    protected String getResultName() {
        return getClass().getSimpleName() + "_" + test_id;
    }

    public UdtConstantTest() {
        super( "UdtConstantTest", null );
    }

    protected String getTestId( String src, String dest, String count ) {
        return "test_" + count;
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        if ( !initialized ) {
            BaseTest.dispatchEvents(0);
            // get number of tests
            currentTest = 0;
            numTests = 0;
            for ( Method method : this.getClass().getMethods() ) if ( null != method.getAnnotation( org.junit.Test.class ) ) numTests++;
            // load project
            loadProject( PROJECT_NAME );
            // launch verifier
            launchVerifier();
            initialized = true;
        }
        currentTest++;
        parseSucceeded = false;
        BaseTest.logFileCheckingEnabled = false;
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        if ( currentTest >= numTests ) {
            DebugUITestUtilities.stopSession( m_sys, PROJECT_NAME );
            initialized = false;
        }
        BaseTest.logFileCheckingEnabled = true;
    }
    
    @Test
    public void testUdtConstants() {
    	Function_c test1 = Function_c.getOneS_SYNCOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(
            Package_c.getManyEP_PKGsOnR1405(m_sys)), selected -> ((Function_c)selected).getName().equals("test1") );
    	testAction( test1 );
    	assertTrue( "Function failed to parse", checkResult_parseSucceeded() );
    	assertTrue( "Output produced incorrect value", checkResult_correctValue( test1 ) );
    }
    
    private void launchVerifier() throws Exception {
        // set perspective switch dialog on launch
        CorePlugin.disableParseAllOnResourceChange();
        DebugUITestUtilities.processDebugEvents();
        m_sys = SystemModel_c.SystemModelInstance(Ooaofooa.getDefaultInstance(), new ClassQueryInterface_c() {
            @Override
            public boolean evaluate( Object candidate ) {
                return ((SystemModel_c)candidate).getName().equals( PROJECT_NAME );
            }
        });
        CorePlugin.enableParseAllOnResourceChange();
        TestingUtilities.allowJobCompletion();
        while ( !ResourcesPlugin.getWorkspace().getRoot().isSynchronized( IProject.DEPTH_INFINITE ) ) {
            ResourcesPlugin.getWorkspace().getRoot().refreshLocal( IProject.DEPTH_INFINITE, new NullProgressMonitor() );
            while ( PlatformUI.getWorkbench().getDisplay().readAndDispatch() );
        }
        Ooaofooa.setPersistEnabled( true );

        // get domain
        domain = Component_c.getOneC_COnR8001( PackageableElement_c.getManyPE_PEsOnR8000( Package_c.getOneEP_PKGOnR1405( m_sys, new ClassQueryInterface_c() {
            @Override
            public boolean evaluate( Object candidate ) {
                return ((Package_c)candidate).getName().equals( LIBRARY_PACKAGE );
            }
        })), new ClassQueryInterface_c() {
            @Override
            public boolean evaluate( Object candidate ) {
                return ((Component_c)candidate).getName().equals( DOMAIN_NAME );
            }
        });
        assertNotNull( domain );

        // launch the domain
        DebugUITestUtilities.stopSession( m_sys, PROJECT_NAME );
        DebugUITestUtilities.launchElement( domain, m_bp_tree.getControl().getMenu());
        DebugUITestUtilities.waitForElementsToStart(1);

        // get the execution engine
        runningEngine = ComponentInstance_c.getOneI_EXEOnR2955( domain );
        assertNotNull( runningEngine );
    }
    

    private void testAction( NonRootModelElement activity ) {
        parseAction( activity );
        if ( parseSucceeded ) {
            executeAction( activity );
        }
    }

    private void parseAction( NonRootModelElement element ) {
        try {
            clearErrorMarkers( element );
        } catch ( CoreException e ) {
            assertNull( "Error clearing problem markers.", e );
        }
        ParserAllActivityModifier parser = new ParserAllActivityModifier( element.getModelRoot(), new NonRootModelElement[] {element}, new NullProgressMonitor() );
        parser.processAllActivities( IAllActivityModifier.PARSE, false );
        try {
            IMarker[] problems = collectErrorMarkers( element );
            parseSucceeded = problems.length == 0;
        } catch ( CoreException e ) {
            assertNull( "Error collecting problem markers.", e );
        }
    }

    private IMarker[] collectErrorMarkers( NonRootModelElement element ) throws CoreException {
        return element.getFile().getParent().findMarkers( IMarker.PROBLEM, false, IResource.DEPTH_INFINITE );
    }

    private void clearErrorMarkers( NonRootModelElement element ) throws CoreException {
        IMarker[] problems = collectErrorMarkers( element );
        for ( IMarker problem : problems ) {
            problem.delete();
        }
    }
    
    private void executeAction( NonRootModelElement element ) {
        openPerspectiveAndView( "org.xtuml.bp.debug.ui.DebugPerspective", BridgePointPerspective.ID_MGC_BP_EXPLORER );
        Selection.getInstance().clear();
        Selection.getInstance().addToSelection( element );
        BPDebugUtils.openSessionExplorerView( true );
        BPDebugUtils.executeElement( element );
        DebugUITestUtilities.waitForExecution();
        DebugUITestUtilities.waitForBPThreads( m_sys );
    }

    private boolean checkResult_parseSucceeded() {
        return parseSucceeded;
    }

    private boolean checkResult_correctValue( NonRootModelElement activity ) {
        String consoleOutput = DebugUITestUtilities.getConsoleText( "" );
        String expectedOutput = "User invoked function: " + activity.getName() + "\n" + 
                                "LogInfo:  Success\n" + 
                                "LogInfo:  Success\n" +
                                "LogInfo:  Success\n";
        return consoleOutput.contains( expectedOutput );
    }


}
