package com.company.skt.lib;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/** Custom {@link RuntimeException} with the sole purpose of breaking an infinite loop in an task executed by
 * {@code Skt}´s {@link ScheduledThreadPoolExecutor}. */
public class TaskCompleteException extends RuntimeException {
}
