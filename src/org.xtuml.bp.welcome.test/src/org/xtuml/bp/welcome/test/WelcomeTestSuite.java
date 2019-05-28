package org.xtuml.bp.welcome.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.util.WorkspaceUtil;
import org.junit.Ignore;

import junit.framework.TestSuite;

/**
* Test all areas of the core
*/
@Ignore
@RunWith(Suite.class)
@Suite.SuiteClasses({
	
	GlobalTestSetupClass.class,
	WelcomePageTestGPS.class,
	WelcomePageTest.class,
	WelcomePageTestMetamodel.class,	
})
public class WelcomeTestSuite extends TestSuite {


}