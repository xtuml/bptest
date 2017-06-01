import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.xtuml.bp.core.test.ConsistencyTest;
import org.xtuml.bp.core.test.rtomove.RTOMoveTestsTestSuite;

import junit.framework.TestSuite;

/**
 * Test all areas of the core
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ CoreGlobalsTestSuiteGenerics.class, TestVisibilityInElementChooserSuite.class, RTOMoveTestsTestSuite.class, SystemLevelGlobalsTestSuite.class,
		CoreGlobalsTestSuite2Generics.class, ConsistencyTest.class })

public class CoreTestFull extends TestSuite {
}
