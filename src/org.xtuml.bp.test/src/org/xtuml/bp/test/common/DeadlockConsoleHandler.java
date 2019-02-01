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
import java.util.Map;

public class DeadlockConsoleHandler implements DeadlockHandler {

	@Override
	public void handleDeadlock(ThreadInfo[] deadlockedThreads) {
		if (deadlockedThreads != null) {
			System.err.println("Deadlock detected!");

			Map<Thread, StackTraceElement[]> stackTraceMap = Thread.getAllStackTraces();
			for (ThreadInfo threadInfo : deadlockedThreads) {

				if (threadInfo != null) {

					for (Thread thread : Thread.getAllStackTraces().keySet()) {
						
						if (thread.getId() == threadInfo.getThreadId()) {
							System.err.println(threadInfo.toString().trim());

							for (StackTraceElement ste : thread.getStackTrace()) {
								System.err.println("\t" + ste.toString().trim());
							}
						}
						// stop the thread
						thread.interrupt();
						try {
							thread.wait(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
