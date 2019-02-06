//========================================================================
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
//========================================================================
package org.xtuml.bp.test.common;

import java.lang.management.ThreadInfo;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.runners.model.TestClass;

import junit.framework.TestCase;

/**
 * This class performs both deadlock detection AND implements a 
 * timeout for test suites that exceed a defined time boundary.
 */
public class DeadlockJUnitHandler implements DeadlockHandler {
	private BaseTest testInstance = null;
	private String testName;
	private Thread testThread = null;
	private Instant start = null;
	public static final long MaxTestTimeAllowedInSeconds = 15 * 60; // 15 minutes
	
	public DeadlockJUnitHandler(String pTestName, BaseTest baseTest, Thread unitTestThread) {
		this.testName = pTestName;
		testInstance = baseTest;
		testThread = unitTestThread;
	}


	@Override
	public void handleDeadlock(ThreadInfo[] deadlockedThreads) {
		StringBuffer failureMessage = new StringBuffer();			
		Set<Thread> threadsToInterrupt = new HashSet<Thread>();
				
		boolean deadlockDetected = false;
		if (deadlockedThreads != null) {
			failureMessage.append("ERROR! Deadlock detected in: " + testName + "\n");
			failureMessage.append("\tThe test thread is being \"interrupt()\"'ed\n");
			deadlockDetected = true;
			dumpThreadInfo(deadlockedThreads, failureMessage, threadsToInterrupt, true, "deadlock");			
		}
		
		if (deadlockDetected) {
			handleError(failureMessage, threadsToInterrupt);			
		}
		
	}


	private void dumpThreadInfo(ThreadInfo[] threadList, StringBuffer failureMessage,
			Set<Thread> threadsToInterrupt, boolean addThreadsToInterruptSet, String dumpType) {
		failureMessage.append("\t=================Start " + dumpType + " thread dump=================\n");				
		
		for (ThreadInfo threadInfo : threadList) {
			if (threadInfo != null) {
				for (Thread thread : Thread.getAllStackTraces().keySet()) {
					
					if (thread.getId() == threadInfo.getThreadId()) {
						
						if (addThreadsToInterruptSet) {
							threadsToInterrupt.add(thread);
						}
						
						failureMessage.append(threadInfo.toString().trim() + "\n");

						for (StackTraceElement ste : thread.getStackTrace()) {
							failureMessage.append("\t" + ste.toString().trim() + "\n");
						}
					}						
				}
			}
		}
		failureMessage.append("\t=================End " + dumpType + " thread dump=================\n");		
	}


	@Override
	public void handleTimeExceeded(ThreadInfo[] allThreadIds) {
		StringBuffer failureMessage = new StringBuffer();			
		Set<Thread> threadsToInterrupt = new HashSet<Thread>();
		boolean maxTestTimeExceeded = false;
		
		if (start == null) {
			start = Instant.now();
		} else {
			long gap = ChronoUnit.MILLIS.between(start,Instant.now());
			if (gap >= (MaxTestTimeAllowedInSeconds*1000)) {
				maxTestTimeExceeded = true;
				long timeoutInMinutes = (MaxTestTimeAllowedInSeconds/60);
				failureMessage.append("ERROR! Maximum unit test time (" + String.valueOf(timeoutInMinutes)
						+ "minutes) has been exceeded in: " + testName + "\n");
				failureMessage.append("\tThe test thread is being \"interrupt()\"'ed\n");
				dumpThreadInfo(allThreadIds, failureMessage, threadsToInterrupt, false, "time-exceeded");			
			}
		}
		
		if (maxTestTimeExceeded) {
			handleError(failureMessage, threadsToInterrupt);			
		}
		
	}


	private void handleError(StringBuffer failureMessage, Set<Thread> threadsToInterrupt) {
		// Print this out for interactive debugging purposes 
		System.err.println(failureMessage);			
		
		// Set the flag to report the problem in teardown()
		if (testInstance != null) {
			testInstance.setDeadLockDetected(failureMessage.toString());
		}
		
		// interrupt the test thread
		threadsToInterrupt.add(testThread);
		
		// interrupt the test thread
		for (Thread thread : threadsToInterrupt) {
			if (thread.isAlive()) {
				thread.interrupt();
			}
		}
	}
}
