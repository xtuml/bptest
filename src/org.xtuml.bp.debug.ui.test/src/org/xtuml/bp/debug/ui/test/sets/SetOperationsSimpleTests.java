package org.xtuml.bp.debug.ui.test.sets;

import org.xtuml.bp.core.common.NonRootModelElement;

public class SetOperationsSimpleTests extends SetOperationsTests {

    public static boolean generateResults = false;
    public static boolean useDrawResults = true;

    public SetOperationsSimpleTests( String subTypeClassName, String subTypeArg0 ) {
        super( subTypeClassName, subTypeArg0 );
    }

    /**
     * "O" is one of the degrees of freedom as specified in this issues
     * test matrix.
     * This routine gets the "O" instance from the given name.
     * 
     * @param element The degree of freedom instance to retrieve
     * @return A model element used in the test as specified by the test matrix
     */
    public NonRootModelElement selectO( String element ) {
        return selectO( element, null );
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
    public NonRootModelElement selectO( String element, Object extraData ) {
        NonRootModelElement nrme = selectColumn( element, extraData );
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
    public NonRootModelElement selectAB( String element ) {
        return selectAB( element, null );
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
    public NonRootModelElement selectAB( String element, Object extraData ) {
        NonRootModelElement nrme = selectRow( element, extraData );
        assertTrue("An instance with degree of freedom type \"AB\" was not found.  Instance Name: " + element + ".", nrme!=null);
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
    public void O_AB_Action( NonRootModelElement columnInstance, NonRootModelElement rowInstance ) {
        testAction( columnInstance, rowInstance );
    }

}
