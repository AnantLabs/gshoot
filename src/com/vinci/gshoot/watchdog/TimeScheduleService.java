package com.vinci.gshoot.watchdog;

import com.vinci.gshoot.IService;
import com.vinci.gshoot.index.IndexService;

import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

public class TimeScheduleService implements Runnable, IService {

    private long watchInterval = -1;
    private boolean stopped = false;
    private IndexService indexService;

    public TimeScheduleService(long watchInterval, IndexService indexService) {
        if (watchInterval < 30) {
            throw new InvalidConfigurationException("Timeout should be bigger than 30 second");
        }
        this.watchInterval = watchInterval * 1000;
        this.indexService = indexService;
    }

    protected final void fireTimeoutOccured() {
        indexService.indexAll();
    }

    public synchronized void start() {
        fireTimeoutOccured();
        stopped = false;
        Thread t = new Thread(this, "WATCHDOG");
        t.setDaemon(true);
        t.start();
    }

    public synchronized void stop() {
        stopped = true;
        notifyAll();
    }

    public synchronized void run() {
        while (!stopped) {
            final long until = System.currentTimeMillis() + watchInterval;
            long now;
            while (until > (now = System.currentTimeMillis())) {
                try {
                    wait(until - now);
                } catch (InterruptedException e) {
                }
            }
            if (!stopped) {
                fireTimeoutOccured();
            }
        }
    }
}
