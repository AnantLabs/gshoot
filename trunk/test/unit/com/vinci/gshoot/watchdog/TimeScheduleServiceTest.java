package com.vinci.gshoot.watchdog;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.fail;
import org.junit.Test;

public class TimeScheduleServiceTest {
    private Observer mockObserver = createMock(Observer.class);

    @Test
    public void should_throw_exception_if_time_out_is_0() {
        try {
            new TimeScheduleService(0);
            fail("Should throw exception as time out is 0");
        }
        catch (InvalidConfigurationException e) {
        }
    }

    @Test
    public void should_index_all_when_start() {
        mockObserver.fired();
        replay(mockObserver);
        int timeoutInSeconds = 100;
        TimeScheduleService dog = new TimeScheduleService(timeoutInSeconds);
        dog.add(mockObserver);

        dog.start();
        dog.stop();
        verify(mockObserver);
    }
}