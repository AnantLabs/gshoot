package com.vinci.gshoot.watchdog;

import com.vinci.gshoot.IService;

import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

public class TimeScheduleService implements Runnable, IService {

    private long watchInterval = -1;
    private boolean stopped = false;
    private List<Observer> observers = new LinkedList<Observer>();

    public TimeScheduleService(long watchInterval, Observer... observers) {
        if (watchInterval < 1) {
            throw new InvalidConfigurationException("Timeout should be bigger than 1 second");
        }
        this.watchInterval = watchInterval * 1000;
        this.observers.addAll(Arrays.asList(observers));
    }

    protected final void fireTimeoutOccured() {
        for (Observer observer : observers) {
            observer.fired();
        }
    }

    public void add(Observer observer) {
        this.observers.add(observer);
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
