import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.TestSuite;
import org.junit.Ignore;

/**
 * Test all areas of the verifier
 */

@Ignore
@RunWith(Suite.class)
@Suite.SuiteClasses({ IOMdlGlobalsTestSuiteGenerics.class, IOMdlGlobalsTestSuite2Generics.class,
		PkgCMGlobalsTestSuiteGenerics.class, })

public class IOTestFull extends TestSuite {
}
