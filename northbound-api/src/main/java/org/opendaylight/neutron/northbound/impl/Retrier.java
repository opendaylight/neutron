/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.northbound.impl;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Retries an action a number of times.
 *
 * @author Michael Vorburger.ch
 */
public class Retrier<E extends Exception> {

    // TODO This class is incubated in Neutron, but is intended to be moved to infrautils (or yangtools, for mdsal)

    // TODO The RetryingManagedNewTransactionRunnerImpl (in genius, AND mdsal..) should be retrofitted to use this

    // TODO The org.opendaylight.genius.datastoreutils.TaskRetryLooper should be @Deprecate (retrofitted to use this?)

    private static final Logger LOG = LoggerFactory.getLogger(Retrier.class);

    private final int maxRetries;
    private final Class<E> exceptionToRetry;
    private final Duration waitTime;

    public Retrier(Class<E> exceptionToRetry, int maxRetries, Duration waitTime) {
        this.exceptionToRetry = exceptionToRetry;
        this.maxRetries = maxRetries;
        this.waitTime = waitTime;
        if (waitTime.isNegative()) {
            throw new IllegalArgumentException("Negative wait time: " + waitTime);
        }
    }

    protected boolean isRetriableException(Throwable throwable) {
        return exceptionToRetry.isInstance(requireNonNull(throwable, "throwable == null"));
    }

    // TODO on infrautils master, rather use an InterruptibleCheckedFunction (and add an InterruptibleCheckedRunnable)
    public void runWithRetries(CheckedRunnable<E> runnable) throws RetryExhaustedException, InterruptedException {
        runWithRetries(runnable, maxRetries);
    }

    @SuppressWarnings("checkstyle:IllegalCatch")
    private void runWithRetries(CheckedRunnable<E> runnable, int remainingRetries)
            throws RetryExhaustedException, InterruptedException {
        try {
            runnable.run();
            LOG.debug("Succeeded to run: ", runnable);
        } catch (Throwable exception) {
            if (isRetriableException(exception)) {
                if (remainingRetries > 0) {
                    if (!waitTime.isZero()) {
                        // NOT waitTime.toMillis(), that would be wrong!
                        Thread.sleep(waitTime.getSeconds() * 1000, waitTime.getNano());
                    }
                    LOG.debug("Re-running retryable operation after previous failure and wait", runnable, exception);
                    runWithRetries(runnable, remainingRetries - 1);
                } else {
                    LOG.error("Failed to run operation after max. retries (expected exception): ", runnable, exception);
                    throw new RetryExhaustedException("All " + maxRetries + " retries failed, giving up", exception);
                }
            } else {
                LOG.error("Failed to run retryable operation, unexpected exception (giving up): ", runnable, exception);
                if (exception instanceof RuntimeException) {
                    throw (RuntimeException) exception;
                } else {
                    // This normally shouldn't happen, and typically indicates a broken #isRetriableException() impl
                    throw new RuntimeException("Unexpected checked exception", exception);
                }
            }
        }
    }

    public static class RetryExhaustedException extends Exception {
        private static final long serialVersionUID = -4121136490952468719L;

        public RetryExhaustedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @FunctionalInterface
    // FYI copy/pasted here from infrautils only to avoid adding a new cross-project dependency in stable Oxygen branch
    public interface CheckedRunnable<E extends Exception> {
        void run() throws E;
    }
}
