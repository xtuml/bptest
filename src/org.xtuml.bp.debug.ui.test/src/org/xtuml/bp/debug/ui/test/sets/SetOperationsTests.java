package org.xtuml.bp.debug.ui.test.sets;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
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

public class SetOperationsTests extends CanvasTest {

    public static boolean generateResults = false;
    public static boolean useDrawResults = true;

    String test_id = "";

    // test variables
    private static final String PROJECT_NAME = "test_set_operations_5007";
    private static final String LIBRARY_PACKAGE = "lib";
    private static final String DOMAIN_NAME = "comp2";
    private static final String POPULATE_FUNCTION = "populate";
    private static boolean initialized = false;

    private boolean parseSucceeded;
    private Component_c domain;
    private ComponentInstance_c runningEngine;
    

    protected String getResultName() {
        return getClass().getSimpleName() + "_" + test_id;
    }

    public SetOperationsTests( String subTypeClassName, String subTypeArg0 ) {
        super( subTypeClassName, subTypeArg0 );
    }

    protected String getTestId( String src, String dest, String count ) {
        return "test_" + count;
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        if ( !initialized ) {
            BaseTest.dispatchEvents(0);
            // load project
            loadProject( PROJECT_NAME );
            // launch verifier
            launchVerifier();
            // initialize instance population
            createPopulation();
            initialized = true;
        }
        parseSucceeded = false;
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
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
        DebugUITestUtilities.launchElement( domain, m_bp_tree.getControl().getMenu());
        DebugUITestUtilities.waitForElementsToStart(1);

        // get the execution engine
        runningEngine = ComponentInstance_c.getOneI_EXEOnR2955( domain );
        assertNotNull( runningEngine );
    }
    
    private void createPopulation() throws Exception {
        // create the instance population
        Function_c populate = Function_c.FunctionInstance( domain.getModelRoot(), new ClassQueryInterface_c() {
            @Override
            public boolean evaluate( Object candidate ) {
                return ((Function_c)candidate).getName().equals( POPULATE_FUNCTION );
            }
        });
        assertNotNull( populate );
        openPerspectiveAndView( "org.xtuml.bp.debug.ui.DebugPerspective", BridgePointPerspective.ID_MGC_BP_EXPLORER );
        Selection.getInstance().clear();
        Selection.getInstance().addToSelection( populate );
        BPDebugUtils.openSessionExplorerView( true );
        BPDebugUtils.executeElement( populate );
        DebugUITestUtilities.waitForExecution();
        DebugUITestUtilities.waitForBPThreads( m_sys );
    }

    public NonRootModelElement selectColumn( String element, Object extraData ) {
        return Package_c.getOneEP_PKGOnR1405( m_sys, new ClassQueryInterface_c() {
            @Override
            public boolean evaluate(Object candidate) {
                return ((Package_c)candidate).getName().equals( element );
            }
        });
    }

    public NonRootModelElement selectRow( String element, Object extraData ) {
        return Function_c.getOneS_SYNCOnR8001( PackageableElement_c.getManyPE_PEsOnR8000( (Package_c)extraData ), new ClassQueryInterface_c() {
            @Override
            public boolean evaluate(Object candidate) {
                return ((Function_c)candidate).getName().contains( element );
            }
        });
    }
   
    public void testAction( NonRootModelElement columnInstance, NonRootModelElement rowInstance ) {
        parseAction( rowInstance );
        if ( parseSucceeded ) {
            executeAction( rowInstance );
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

    /**
    * This function verifies an expected result.
    *
    * @param source A model element instance aquired through a action taken
    *               on a column of the matrix.
    * @param destination A model element instance aquired through a action taken
    *                    taken on a row of the matrix.
    * @return true if the test succeeds, false if it fails
    */
    public boolean checkResult_parseSucceeded( NonRootModelElement source, NonRootModelElement destination ) {
        return parseSucceeded;
    }

    /**
    * This function verifies an expected result.
    *
    * @param source A model element instance aquired through a action taken
    *               on a column of the matrix.
    * @param destination A model element instance aquired through a action taken
    *                    taken on a row of the matrix.
    * @return true if the test succeeds, false if it fails
    */
    public boolean checkResult_correctValue( NonRootModelElement source, NonRootModelElement destination ) {
        String consoleOutput = DebugUITestUtilities.getConsoleText( "" );
        String expectedOutput = "User invoked function: " + destination.getName() + "\nLogSuccess:  passed.";
        return consoleOutput.contains( expectedOutput );
    }


}
