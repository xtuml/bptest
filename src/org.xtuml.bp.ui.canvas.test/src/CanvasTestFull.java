import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.TestSuite;

/**
 * Test all areas of the verifier
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ CanvasCCPGlobalsTestSuite.class, GlobalsCanvasSuite1.class, GlobalsCanvasSuite2.class,
		GlobalsCanvasSuite3.class, })

public class CanvasTestFull extends TestSuite {
}
