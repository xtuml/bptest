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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DeadlockJUnitHandler implements DeadlockHandler {
	private BaseTest testInstance = null;
	private Thread testThread = null;
	
	public DeadlockJUnitHandler(BaseTest baseTest, Thread unitTestThread) {
		testInstance = baseTest;
		testThread = unitTestThread;
	}


	@Override
	public void handleDeadlock(ThreadInfo[] deadlockedThreads) {
		if (deadlockedThreads != null) {
			StringBuffer failureMessage = new StringBuffer();			
			failureMessage.append("Deadlock detected!\n");
			Set<Thread> threadsToInterrupt = new HashSet<Thread>();
			
			Map<Thread, StackTraceElement[]> stackTraceMap = Thread.getAllStackTraces();
			for (ThreadInfo threadInfo : deadlockedThreads) {
				if (threadInfo != null) {
					for (Thread thread : Thread.getAllStackTraces().keySet()) {
						
						if (thread.getId() == threadInfo.getThreadId()) {
							threadsToInterrupt.add(thread);
							failureMessage.append(threadInfo.toString().trim() + "\n");

							for (StackTraceElement ste : thread.getStackTrace()) {
								failureMessage.append("\t" + ste.toString().trim() + "\n");
							}
						}						
					}
				}
			}
			

			System.out.println(failureMessage);			
			testInstance.setDeadLockDetected(failureMessage.toString());
			
			// Interrupt the deadlocked threads
			for (Thread thread : threadsToInterrupt) {
				thread.interrupt();
			}
			// interrupt the test thread
			testThread.interrupt();
		}
	}
}
