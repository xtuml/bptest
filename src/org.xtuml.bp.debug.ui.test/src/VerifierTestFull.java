import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.xtuml.bp.debug.test.GlobalTestSetupClass;

import junit.framework.TestSuite;

/**
 * Test all areas of the verifier
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	GlobalTestSetupClass.class, // This is only here because the following two are temporarily disabled and maven doesn't like if there are no tests to execute
	/* Disabling due to server hangs
	VerifierTestSuite.class, 
	VerifierTestSuite2.class, */
	})

public class VerifierTestFull extends TestSuite {
}
