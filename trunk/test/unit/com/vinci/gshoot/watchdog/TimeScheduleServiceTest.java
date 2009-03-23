package com.vinci.gshoot.watchdog;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.vinci.gshoot.index.IndexService;

public class TimeScheduleServiceTest {
    private IndexService mockIndexService = createMock(IndexService.class);

    @Test
    public void should_throw_exception_if_time_out_is_less_than_30() {
        try {
            new TimeScheduleService(20, null);
            fail("Should throw exception as time out is less than 0");
        }
        catch (InvalidConfigurationException e) {
        }
    }

    @Test
    public void should_index_all_when_start() {
        mockIndexService.indexAll();
        replay(mockIndexService);
        TimeScheduleService scheduleService = new TimeScheduleService(20, mockIndexService);
        scheduleService.fireTimeoutOccured();
        verify(mockIndexService);
    }
}