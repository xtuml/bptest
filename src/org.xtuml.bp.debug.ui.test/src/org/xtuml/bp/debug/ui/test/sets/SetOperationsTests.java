package org.xtuml.bp.debug.ui.test.sets;

import org.junit.After;
import org.junit.Before;
import org.xtuml.bp.core.common.NonRootModelElement;
import org.xtuml.bp.ui.canvas.test.*;

public class SetOperationsTests extends CanvasTest {
    public static boolean generateResults = false;
    public static boolean useDrawResults = true;

    String test_id = "";

    protected String getResultName() {
        return getClass().getSimpleName() + "_" + test_id;
    }

    public SetOperationsTests(String subTypeClassName, String subTypeArg0) {
        super(subTypeClassName, subTypeArg0);
    }

    protected String getTestId(String src, String dest, String count) {
        return "test_" + count;
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * "O" is one of the degrees of freedom as specified in this issues
     * test matrix.
     * This routine gets the "O" instance from the given name.
     * 
     * @param element The degree of freedom instance to retrieve
     * @return A model element used in the test as specified by the test matrix
     */
    NonRootModelElement selectO(String element) {
        return selectO(element, null);
    }

    /**
     * "O" is one of the degrees of freedom as specified in this issues
     * test matrix.
     * This routine gets the "O" instance from the given name.
     * 
     * @param element The degree of freedom instance to retrieve
     * @param extraData Extra data needed for selection
     * @return A model element used in the test as specified by the test matrix
     */
    NonRootModelElement selectO(String element, Object extraData) {
        NonRootModelElement nrme = null;
        if (element.equalsIgnoreCase("O1")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("O2")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("O3")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("O4")) {
            //TODO: Implement
        } 
        assertTrue("An instance with degree of freedom type \"O\" was not found.  Instance Name: " + element + ".", nrme!=null);
        return nrme;
    }

    /**
     * "AB" is one of the degrees of freedom as specified in this issues
     * test matrix.
     * This routine gets the "AB" instance from the given name.
     * 
     * @param element The degree of freedom instance to retrieve
     * @return A model element used in the test as specified by the test matrix
     */
    NonRootModelElement selectAB(String element) {
        return selectAB(element, null);
    }

    /**
     * "AB" is one of the degrees of freedom as specified in this issues
     * test matrix.
     * This routine gets the "AB" instance from the given name.
     * 
     * @param element The degree of freedom instance to retrieve
     * @param extraData Extra data needed for selection
     * @return A model element used in the test as specified by the test matrix
     */
    NonRootModelElement selectAB(String element, Object extraData) {
        NonRootModelElement nrme = null;
        if (element.equalsIgnoreCase("A1B1")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("A1B2")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("A2B1")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("A2B2")) {
            //TODO: Implement
        } else        assertTrue("An instance with degree of freedom type \"AB\" was not found.  Instance Name: " + element + ".", nrme!=null);
        return nrme;
    }

    /**
     * "G" is one of the degrees of freedom as specified in this issues
     * test matrix.
     * This routine gets the "G" instance from the given name.
     * 
     * @param element The degree of freedom instance to retrieve
     * @return A model element used in the test as specified by the test matrix
     */
    NonRootModelElement selectG(String element) {
        return selectG(element, null);
    }

    /**
     * "G" is one of the degrees of freedom as specified in this issues
     * test matrix.
     * This routine gets the "G" instance from the given name.
     * 
     * @param element The degree of freedom instance to retrieve
     * @param extraData Extra data needed for selection
     * @return A model element used in the test as specified by the test matrix
     */
    NonRootModelElement selectG(String element, Object extraData) {
        NonRootModelElement nrme = null;
        if (element.equalsIgnoreCase("G1")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("G2")) {
            //TODO: Implement
        } 
        assertTrue("An instance with degree of freedom type \"G\" was not found.  Instance Name: " + element + ".", nrme!=null);
        return nrme;
    }

    /**
     * "CDEF" is one of the degrees of freedom as specified in this issues
     * test matrix.
     * This routine gets the "CDEF" instance from the given name.
     * 
     * @param element The degree of freedom instance to retrieve
     * @return A model element used in the test as specified by the test matrix
     */
    NonRootModelElement selectCDEF(String element) {
        return selectCDEF(element, null);
    }

    /**
     * "CDEF" is one of the degrees of freedom as specified in this issues
     * test matrix.
     * This routine gets the "CDEF" instance from the given name.
     * 
     * @param element The degree of freedom instance to retrieve
     * @param extraData Extra data needed for selection
     * @return A model element used in the test as specified by the test matrix
     */
    NonRootModelElement selectCDEF(String element, Object extraData) {
        NonRootModelElement nrme = null;
        if (element.equalsIgnoreCase("C1D1E1F1")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C1D2E3F4")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C1D2E4F3")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C1D3E2F4")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C1D3E4F2")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C1D4E2F3")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C1D4E3F2")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C2D1E3F4")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C2D1E4F3")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C2D2E2F2")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C2D3E1F4")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C2D3E4F1")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C2D4E1F3")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C2D4E3F1")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C3D1E2F4")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C3D1E4F2")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C3D2E1F4")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C3D2E4F1")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C3D3E3F3")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C3D4E1F2")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C3D4E2F1")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C4D1E2F3")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C4D1E3F2")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C4D2E1F3")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C4D2E3F1")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C4D3E1F2")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C4D3E2F1")) {
            //TODO: Implement
        } else if (element.equalsIgnoreCase("C4D4E4F4")) {
            //TODO: Implement
        } else        assertTrue("An instance with degree of freedom type \"CDEF\" was not found.  Instance Name: " + element + ".", nrme!=null);
        return nrme;
    }

    /**
     * This routine performs the action associated with a matrix cell.
     * The parameters represent model instances aquired based on the specifed
     * column instance and row instance.
     * 
     * @param columnInstance Model instance from the column
     * @param rowInstance Model instance from the row
     */
    void O_AB_Action(NonRootModelElement columnInstance, NonRootModelElement rowInstance) {
        //TODO: Implement
    }

    /**
     * This routine performs the action associated with a matrix cell.
     * The parameters represent model instances aquired based on the specifed
     * column instance and row instance.
     * 
     * @param columnInstance Model instance from the column
     * @param rowInstance Model instance from the row
     */
    void G_CDEF_Action(NonRootModelElement columnInstance, NonRootModelElement rowInstance) {
        //TODO: Implement
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
    boolean checkResult_parseSucceeded(NonRootModelElement source, NonRootModelElement destination) {
        boolean parseSucceeded = false;
        //TODO: Implement
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
    boolean checkResult_correctValue(NonRootModelElement source, NonRootModelElement destination) {
        boolean correctValue = false;
        //TODO: Implement
        return correctValue;
    }


}
