package org.xtuml.bp.als.oal.test;

//=====================================================================
// Licensed under the Apache License, Version 2.0 (the "License"); you may not
// use this file except in compliance with the License.  You may obtain a copy
// of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   See the
// License for the specific language governing permissions and limitations under
// the License.
//=====================================================================
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtuml.bp.core.ActionHome_c;
import org.xtuml.bp.core.Action_c;
import org.xtuml.bp.core.CorePlugin;
import org.xtuml.bp.core.MooreActionHome_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.StateMachineState_c;
import org.xtuml.bp.core.StateMachine_c;
import org.xtuml.bp.core.common.BridgePointPreferencesStore;
import org.xtuml.bp.test.common.BaseTest;
import org.xtuml.bp.test.common.OrderedRunner;
import org.xtuml.bp.test.common.TestingUtilities;

import antlr.RecognitionException;
import antlr.TokenStreamException;

@RunWith(OrderedRunner.class)
public class TestAllowInterfaceNameInICMsg_Generics extends BaseTest {

    public static boolean configured = false;

    public TestAllowInterfaceNameInICMsg_Generics() {
        super("org.xtuml.bp.als.oal.test", "Tracking");
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        if (configured) {
            return;
        }
        configured = true;
        super.setUp();
        TestingUtilities.importTestingProjectIntoWorkspace("Tracking");
        OalParserTest_Generics.modelRoot = Ooaofooa.getInstance(Ooaofooa.createModelRootId(getProjectHandle("Tracking"), "Tracking", true));
        modelRoot = OalParserTest_Generics.modelRoot;
        populateStateActionInstance();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
      try {
        super.tearDown();
        OalParserTest_Generics.tearDownActionData();
      } catch (RecognitionException re) {
        // do nothing
      } catch (TokenStreamException te) {
        // do nothing
      }
    }

    @Test
    public void testSendUsingInterfaceNameWhenInterfaceNameDisallowed() throws RecognitionException, TokenStreamException {
        IPreferenceStore store = CorePlugin.getDefault().getPreferenceStore();
        store.setValue(BridgePointPreferencesStore.ALLOW_INTERFACE_NAME_IN_IC_MESSAGE, false);

        String act = "send Tracking_HeartRateMonitor::registerListener();";
        String x = OalParserTest_Generics.parseAction(act, OalParserTest_Generics.ACTIVITY_TYPE_STATE, 0);
        String lines[] = x.split("\n");
        assertEquals(":1:33-48: Interface names are not allowed for sending messages.  Use the port name.", lines[0]);
    }

    @Test
    public void testSendUsingPortNameWhenIntefaceNameDisallowed() throws RecognitionException, TokenStreamException {
        String act = "send HR::registerListener();";
        String x = OalParserTest_Generics.parseAction(act, OalParserTest_Generics.ACTIVITY_TYPE_STATE, 0);
        assertEquals("", x);
    }

    // Now we test the above sends with the preference set to allow the behavior
    // and expect to not get syntax errors reported
    @Test
    public void testSendUsingInterfaceNameWhenInterfaceNameAllowed() throws RecognitionException, TokenStreamException {
        IPreferenceStore store = CorePlugin.getDefault().getPreferenceStore();
        store.setValue(BridgePointPreferencesStore.ALLOW_INTERFACE_NAME_IN_IC_MESSAGE, true);

        String act = "send Tracking_HeartRateMonitor::registerListener();";
        String x = OalParserTest_Generics.parseAction(act, OalParserTest_Generics.ACTIVITY_TYPE_STATE, 0);
        assertEquals("", x);
    }

    @Test
    public void testSendUsingPortNameWhenIntefaceNameAllowed() throws RecognitionException, TokenStreamException {
        String act = "send HR::registerListener();";
        String x = OalParserTest_Generics.parseAction(act, OalParserTest_Generics.ACTIVITY_TYPE_STATE, 0);
        assertEquals("", x);
    }

    private void populateStateActionInstance() {
        StateMachine_c sm = StateMachine_c.StateMachineInstance(modelRoot);
        assertNotNull("State Machine not found.", sm);
        StateMachineState_c state = StateMachineState_c.getOneSM_STATEOnR501(sm);
        assertTrue("No test states found", null != state);
        Action_c i_act = Action_c.getOneSM_ACTOnR514(ActionHome_c.getOneSM_AHOnR513(MooreActionHome_c.getOneSM_MOAHOnR511(state)));
        assertTrue("No test actions found", null != i_act);
        OalParserTest_Generics.m_testAction[0] = i_act;
    }

}
