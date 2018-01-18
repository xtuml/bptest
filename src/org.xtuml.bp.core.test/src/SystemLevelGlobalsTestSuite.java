//=====================================================================
// Licensed under the Apache License, Version 2.0 (the "License"); you may not 
// use this file except in compliance with the License.  You may obtain a copy 
// of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   See the 
// License for the specific language governing permissions and limitations under
// the License.
//=====================================================================


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.xtuml.bp.core.test.ComponentContextMenuTests;
import org.xtuml.bp.core.test.ComponentContextMenuTests2;
import org.xtuml.bp.core.test.GlobalTestSetupClass;
import org.xtuml.bp.core.test.ImportedComponentIFTests;
import org.xtuml.bp.core.test.RemoveSignalTests;
import org.xtuml.bp.core.test.SystemLevelGlobalsTest;
import org.xtuml.bp.core.test.UIConfigurationTests;
import org.xtuml.bp.core.test.ui.DocGenTest;

import junit.framework.TestSuite;

/**
* Test the system level areas of core.
*/
@RunWith(Suite.class)
@Suite.SuiteClasses({
		GlobalTestSetupClass.class,
		SystemLevelGlobalsTest.class,
		// See 10035 - DocGenTest.class,
		ComponentContextMenuTests.class, 
		ComponentContextMenuTests2.class, 
		UIConfigurationTests.class,
		ImportedComponentIFTests.class, 
		RemoveSignalTests.class,
})
public class SystemLevelGlobalsTestSuite extends TestSuite {


}