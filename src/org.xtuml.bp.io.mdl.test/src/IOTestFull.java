import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.TestSuite;

/**
 * Test all areas of the verifier
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ IOMdlGlobalsTestSuiteGenerics.class, IOMdlGlobalsTestSuite2Generics.class,
		PkgCMGlobalsTestSuiteGenerics.class, })

public class IOTestFull extends TestSuite {
}
