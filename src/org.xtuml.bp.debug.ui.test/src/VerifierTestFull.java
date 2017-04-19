import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.TestSuite;

/**
 * Test all areas of the verifier
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ VerifierTestSuite.class, VerifierTestSuite2.class, })

public class VerifierTestFull extends TestSuite {
}
