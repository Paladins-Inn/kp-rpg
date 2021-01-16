/*
 * Copyright 2021 downgoon, http://downgoon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.kaiserpfalzedv.rpg.core.snowflake;

import java.util.concurrent.CountDownLatch;

/**
 * @author downgoon {@literal http://downgoon.com}
 * @since 1.0.0 2021-01-11
 */
public class ConcurrentTestFramework {

	private final String name;

	private final boolean debugMode;

	public ConcurrentTestFramework(String name, boolean debugMode) {
		this.name = name;
		this.debugMode = debugMode;
	}

	static class SummaryReport {

		/** concurrent count */
		private final int c;

		/** action number per thread */
		private final int n;

		/** start time stamp */
		private volatile long startNanoTime = -1L;

		/** end time stamp */
		private volatile long endedNanoTime = -1L;

		private String attachment;

		public SummaryReport(int c, int n) {
			this.c = c;
			this.n = n;
		}

		public boolean startIfNot() {
			if (startNanoTime < 0) {
				startNanoTime = System.nanoTime();
				return true;
			}
			return false;
		}

		public boolean endIfNot() {
			if (endedNanoTime < 0) {
				endedNanoTime = System.nanoTime();
				return true;
			}
			return false;
		}

		public boolean isStarted() {
			return startNanoTime > 0;
		}

		public boolean isEnded() {
			return endedNanoTime > 0;
		}

		public void setAttachment(String attachment) {
			this.attachment = attachment;
		}

		@Override
		public String toString() {
			long costNano = (endedNanoTime - startNanoTime);
			long costMS = costNano / (1000000L);

			/* 1ms = 1000us = 1000*1000 nano */
			double qpms = ((c * n + 0.0) / (costNano)) * 1000000;
			double qps = (c * n + 0.0) / (costNano / 1000000000.0);

			if (qpms > 4096) { // max sequence
				/* anti time precision lost by plus 1 ms */
				// costNano += 1000000L;
				qpms = 4096;
				qps = qpms * 1000L;
			}

			return String.format("C%dN%d: costMS=%d, QPMS=%.2f, costSec=%d, QPS=%.2f, %s", c, n, costMS, qpms,
					(costMS / 1000L), qps, attachment);
		}

	}

	public SummaryReport test(final int c, final int n, final Runnable action) throws Exception {
		final SummaryReport report = new SummaryReport(c, n);
		Thread[] threads = new Thread[c];
		final CountDownLatch startLatch = new CountDownLatch(c);
		final CountDownLatch finishLatch = new CountDownLatch(c);

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Runnable() {
				/* Runnable proxy */
				@Override
				public void run() {

					try {
						startLatch.await(); // wait for start cmd
						if (debugMode) {
							System.out.println(Thread.currentThread().getName() + " starting ...");
						}
						report.startIfNot();
						for (int j = 0; j < n; j++) {
							action.run();
						}
						finishLatch.countDown(); // report finish
						if (debugMode) {
							System.out.println(Thread.currentThread().getName() + " finished !!!");
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}, name + "#" + i);
		}

		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
			startLatch.countDown();
		}

		finishLatch.await(); // wait all finish
		report.endIfNot();
		return report;

	}

}
