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
import org.xtuml.bp.core.test.ActivityTestsGenerics;
import org.xtuml.bp.core.test.CanRenameCanDeleteTestGenerics;
import org.xtuml.bp.core.test.ClassKeyLetters;
import org.xtuml.bp.core.test.CombineSplitReferentialsTestGenerics;
import org.xtuml.bp.core.test.CommunicationLinkTestsGenerics;
import org.xtuml.bp.core.test.CommunicationMessageTestsGenerics;
import org.xtuml.bp.core.test.CommunicationTestsGenerics;
import org.xtuml.bp.core.test.CoreGlobalsTestSuiteIIGenerics;
import org.xtuml.bp.core.test.CreationTransitionEventReassignmentTestGenerics;
import org.xtuml.bp.core.test.DeleteDatatypesTestGenerics;
import org.xtuml.bp.core.test.GlobalTestSetupClass;
import org.xtuml.bp.core.test.I810_SlowDeletionTestGenerics;
import org.xtuml.bp.core.test.IntegrityIssueTests;
import org.xtuml.bp.core.test.ModelChangeListenersBatchingTestGenerics;
import org.xtuml.bp.core.test.ModelIntegrityTests;
import org.xtuml.bp.core.test.ModelTransactionTestGenerics;
import org.xtuml.bp.core.test.ModifyNonFullyLoadedModelTestsGenerics;
import org.xtuml.bp.core.test.MultipleSelectionAssignmentTests;
import org.xtuml.bp.core.test.PolymorphicEventAssignmentTestGenerics;
import org.xtuml.bp.core.test.RefreshTestCoreGenerics;
import org.xtuml.bp.core.test.RenameInvolvingResourceTestGenerics;
import org.xtuml.bp.core.test.SequenceTestsGenerics;
import org.xtuml.bp.core.test.TransitionActionTestGenerics;
import org.xtuml.bp.core.test.TwoModelsSelectionTestGenerics;
import org.xtuml.bp.core.test.UndoRedoTestGenerics;
import org.xtuml.bp.core.test.UniqueNameTestGenerics;
import org.xtuml.bp.core.test.UseCaseTestsGenerics;
import org.xtuml.bp.core.test.ui.WritableContextMenuTestGenerics;

import junit.framework.TestSuite;

/**
* Test all areas of the core
*/
@RunWith(Suite.class)
@Suite.SuiteClasses({
		GlobalTestSetupClass.class,
		CoreGlobalsTestSuiteIIGenerics.class,
		ModifyNonFullyLoadedModelTestsGenerics.class,
		TransitionActionTestGenerics.class,
		I810_SlowDeletionTestGenerics.class,
		MultipleSelectionAssignmentTests.class,
		IntegrityIssueTests.class,
		SequenceTestsGenerics.class,
		CommunicationMessageTestsGenerics.class,
		CommunicationTestsGenerics.class,
		CommunicationLinkTestsGenerics.class,
		ModelTransactionTestGenerics.class,
		DeleteDatatypesTestGenerics.class,
		CanRenameCanDeleteTestGenerics.class,
		CombineSplitReferentialsTestGenerics.class,
		RenameInvolvingResourceTestGenerics.class,
		TwoModelsSelectionTestGenerics.class,
		ModelChangeListenersBatchingTestGenerics.class,
		WritableContextMenuTestGenerics.class,
		CreationTransitionEventReassignmentTestGenerics.class,
        PolymorphicEventAssignmentTestGenerics.class,
		UndoRedoTestGenerics.class,
		UseCaseTestsGenerics.class,
		RefreshTestCoreGenerics.class,
		UniqueNameTestGenerics.class,
		ActivityTestsGenerics.class,
		ModelIntegrityTests.class,
		ClassKeyLetters.class,
			
})
public class CoreGlobalsTestSuite2Generics extends TestSuite {

}