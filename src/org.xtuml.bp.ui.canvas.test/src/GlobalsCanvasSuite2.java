
//=====================================================================
//
//File:      $RCSfile: GlobalsCanvasSuite2.javav $
//Version:   $Revision: 1.3 $
//Modified:  $Date: 2013/05/10 05:47:48 $
//
//(c) Copyright 2007-2014 by Mentor Graphics Corp. All rights reserved.
//
//=====================================================================
// Licensed under the Apache License Version 2.0 (the "License"); you may not 
// use this file except in compliance with the License.  You may obtain a copy 
// of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing software 
// distributed under the License is distributed on an "AS IS" BASIS WITHOUT 
// WARRANTIES OR CONDITIONS OF ANY KIND either express or implied.   See the 
// License for the specific language governing permissions and limitations under
// the License.
//=====================================================================


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.xtuml.bp.ui.canvas.test.ConnectorPolicyTests;
import org.xtuml.bp.ui.canvas.test.GlobalTestSetupClass;
import org.xtuml.bp.ui.canvas.test.TestReflexiveConnectorCreation;
import org.xtuml.bp.ui.canvas.test.anchors.GraphicalAnchorTests_0;
import org.xtuml.bp.ui.canvas.test.movement.ConnectorMoveTests_0;
import org.xtuml.bp.ui.canvas.test.routing.RectilinearRoutingTests_0;
import org.xtuml.bp.ui.canvas.test.assoc.AssociationMove_0;

import junit.framework.TestSuite;

/**
 * Test all areas of the canvas
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	GlobalTestSetupClass.class,
	GraphicalAnchorTests_0.class,
	TestReflexiveConnectorCreation.class,
    ConnectorPolicyTests.class,
    ConnectorMoveTests_0.class,
    RectilinearRoutingTests_0.class,
    AssociationMove_0.class
})
public class GlobalsCanvasSuite2 extends TestSuite {

	
}
