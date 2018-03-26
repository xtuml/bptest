package org.xtuml.bp.debug.ui.test.sets;

import org.xtuml.bp.core.common.NonRootModelElement;

public class SetOperationsCompareTests extends SetOperationsTests {

    public static boolean generateResults = false;
    public static boolean useDrawResults = true;

    public SetOperationsCompareTests( String subTypeClassName, String subTypeArg0 ) {
        super( subTypeClassName, subTypeArg0 );
    }

    /**
     * "H" is one of the degrees of freedom as specified in this issues
     * test matrix.
     * This routine gets the "H" instance from the given name.
     * 
     * @param element The degree of freedom instance to retrieve
     * @return A model element used in the test as specified by the test matrix
     */
    NonRootModelElement selectH( String element ) {
        return selectH( element, null );
    }

    /**
     * "H" is one of the degrees of freedom as specified in this issues
     * test matrix.
     * This routine gets the "H" instance from the given name.
     * 
     * @param element The degree of freedom instance to retrieve
     * @param extraData Extra data needed for selection
     * @return A model element used in the test as specified by the test matrix
     */
    NonRootModelElement selectH( String element, Object extraData ) {
        NonRootModelElement nrme = selectColumn( element, extraData );
        assertTrue("An instance with degree of freedom type \"H\" was not found.  Instance Name: " + element + ".", nrme!=null);
        return nrme;
    }

    /**
     * "IJ" is one of the degrees of freedom as specified in this issues
     * test matrix.
     * This routine gets the "IJ" instance from the given name.
     * 
     * @param element The degree of freedom instance to retrieve
     * @return A model element used in the test as specified by the test matrix
     */
    NonRootModelElement selectIJ( String element ) {
        return selectIJ( element, null );
    }

    /**
     * "IJ" is one of the degrees of freedom as specified in this issues
     * test matrix.
     * This routine gets the "IJ" instance from the given name.
     * 
     * @param element The degree of freedom instance to retrieve
     * @param extraData Extra data needed for selection
     * @return A model element used in the test as specified by the test matrix
     */
    NonRootModelElement selectIJ( String element, Object extraData ) {
        NonRootModelElement nrme = selectRow( element, extraData );
        assertTrue("An instance with degree of freedom type \"IJ\" was not found.  Instance Name: " + element + ".", nrme!=null);
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
    void H_IJ_Action( NonRootModelElement columnInstance, NonRootModelElement rowInstance ) {
        testAction( columnInstance, rowInstance );
    }

}
