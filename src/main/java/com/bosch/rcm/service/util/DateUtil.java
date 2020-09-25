package com.bosch.rcm.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Service
public class DateUtil {

    @Value("${time.reset-error-date.value}")
    private String reloadRange;
    private Calendar calendar = Calendar.getInstance();

    public Instant minusTimestampMonth(Instant timestamp) {
        int minusTimeInMonth = Integer.parseInt(reloadRange);
        calendar.setTime(Date.from(timestamp));
        calendar.add(Calendar.MONTH, -minusTimeInMonth);
        return calendar.toInstant();
    }


    public Instant createInstantByParams(Integer hour, Integer minute, Integer second) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.toInstant();
    }

    public Long getMinuteFromTimeToNow(Instant time) {
        LocalDateTime parseTime1 = time.atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime parseTime2 = Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long result  = Duration.between(parseTime1.withSecond(0).withNano(0), parseTime2.withSecond(0).withNano(0)).toMinutes();
        if (result < 0) {
            result = - result;
        }
        return result;
    }

    public long getYearFromInstant(Instant value) {
        calendar.setTime(Date.from(value));
        return calendar.get(Calendar.YEAR);
    }
}
