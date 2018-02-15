package org.xtuml.bp.debug.ui.test.sets;

import org.xtuml.bp.core.common.NonRootModelElement;

public class SetOperationsPrecedenceTests extends SetOperationsTests {

    public static boolean generateResults = false;
    public static boolean useDrawResults = true;

    public SetOperationsPrecedenceTests(String subTypeClassName, String subTypeArg0) {
        super(subTypeClassName, subTypeArg0);
    }

    /**
     * "G" is one of the degrees of freedom as specified in this issues
     * test matrix.
     * This routine gets the "G" instance from the given name.
     * 
     * @param element The degree of freedom instance to retrieve
     * @return A model element used in the test as specified by the test matrix
     */
    public NonRootModelElement selectG( String element ) {
        return selectG( element, null );
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
    public NonRootModelElement selectG( String element, Object extraData ) {
        NonRootModelElement nrme = selectColumn( element, extraData );
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
    public NonRootModelElement selectCDEF( String element ) {
        return selectCDEF( element, null );
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
    public NonRootModelElement selectCDEF( String element, Object extraData ) {
        NonRootModelElement nrme = selectRow( element, extraData );
        assertTrue("An instance with degree of freedom type \"CDEF\" was not found.  Instance Name: " + element + ".", nrme!=null);
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
    public void G_CDEF_Action( NonRootModelElement columnInstance, NonRootModelElement rowInstance ) {
        testAction( columnInstance, rowInstance );
    }

}
