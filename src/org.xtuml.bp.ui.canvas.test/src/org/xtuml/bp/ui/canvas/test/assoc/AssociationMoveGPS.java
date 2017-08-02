package org.xtuml.bp.ui.canvas.test.assoc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xtuml.bp.core.Association_c;
import org.xtuml.bp.core.ClassAsSimpleParticipant_c;
import org.xtuml.bp.core.ClassInAssociation_c;
import org.xtuml.bp.core.ModelClass_c;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.PackageableElement_c;
import org.xtuml.bp.core.ReferredToClassInAssoc_c;
import org.xtuml.bp.core.SimpleAssociation_c;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.common.NonRootModelElement;

public class AssociationMoveGPS extends AssociationMove {

    public AssociationMoveGPS() {
        super("AssociationMoveGPS", null);
    }

    protected String getResultName() {
        return super.getResultName();
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected String getTestModel() {
        return "GPS Watch";
    }

    @Override
    protected Package_c getTestPackage() {
        Package_c result = Package_c.getOneEP_PKGOnR1405(m_sys, new ClassQueryInterface_c() {
            @Override
            public boolean evaluate(Object selected) {
                return ((Package_c)selected).getName().equals( "Tracking" );
            }
        });
        return result;
    }
    
    @Override
    NonRootModelElement selectABCE(String element, Object extraData) {
        NonRootModelElement nrme = null;
        Package_c testPackage = getTestPackage();
        
        // select R14 "has open" end
        nrme = ClassInAssociation_c.getOneR_OIROnR203(ReferredToClassInAssoc_c.getOneR_RTOOnR204(ClassAsSimpleParticipant_c.getManyR_PARTsOnR207(
                SimpleAssociation_c.getOneR_SIMPOnR206(Association_c.getOneR_RELOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(testPackage), new ClassQueryInterface_c() {
            @Override
            public boolean evaluate(Object candidate) {
                return ((Association_c)candidate).getNumb() == 14;
            }
        })), new ClassQueryInterface_c() {
            @Override
            public boolean evaluate(Object candidate) {
                return ((ClassAsSimpleParticipant_c)candidate).getTxt_phrs().equals("has open");
            }
        })));

        assertTrue("An instance with degree of freedom type \"ABCE\" was not found.  Instance Name: " + element + ".", nrme!=null);
        testRel = Association_c.getOneR_RELOnR201((ClassInAssociation_c)nrme);
        testOirId = ((ClassInAssociation_c)nrme).getOir_id();
        return nrme;
    }

    @Override
    NonRootModelElement selectDEFC(String element, Object extraData) {
        NonRootModelElement nrme = null;
        Package_c testPackage = getTestPackage();

        // select "Display" class
        nrme = PackageableElement_c.getOnePE_PEOnR8000( testPackage, new ClassQueryInterface_c() {
            @Override
            public boolean evaluate(Object candidate) {
                ModelClass_c display = ModelClass_c.getOneO_OBJOnR8001((PackageableElement_c)candidate);
                return display != null && display.getName().equals( "Display" );
            }
        });

        assertTrue("An instance with degree of freedom type \"DEFC\" was not found.  Instance Name: " + element + ".", nrme!=null);
        return nrme;
    }

    @Test
    public void testGPS() throws Exception {
        setUp();
        test_id = getTestId("R14.'has open'", "Display", "1");

        NonRootModelElement src = selectABCE( null );

        NonRootModelElement dest = selectDEFC( null );

        ABCE_DEFC_Action(src, dest);
        assertTrue("The move was not completed correctly.", checkResult_moveComplete(src,dest));
        assertTrue("The association details were not preserved.", checkResult_assocInfoSame(src,dest));
        assertTrue("The association is formalized after move.", checkResult_assocUnformal(src,dest));
        
        tearDown();
    }

}
