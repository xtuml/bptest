package org.xtuml.bp.core.test.deployments;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.TestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ImportFromComponentTests.class, ImportFromFileTests.class, TerminatorUpdateTests.class,
        DeploymentExportTests.class })

public class DeploymentTestFull extends TestSuite {
}
