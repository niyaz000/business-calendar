package com.github.niyaz000;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public abstract class WorkDay {

  private final LocalTime startTime;

  private final LocalTime endTime;

  private final DayOfWeek day;

  public WorkDay(LocalTime startTime,
                 LocalTime endTime,
                 DayOfWeek day) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.day = day;
  }

  public DayOfWeek getDay() {
    return day;
  }

  public Duration duration() {
    return Duration.ofNanos(endTime.toNanoOfDay() - startTime.toNanoOfDay()).truncatedTo(ChronoUnit.SECONDS);
  }

  public boolean isWithinWorkDay(LocalTime time) {
    return startTime.compareTo(time) >= 0 && endTime.compareTo(time) <= 0;
  }

  public boolean isOutsideWorkDay(LocalTime time) {
    return !isWithinWorkDay(time);
  }

  public Duration timeElapsed(LocalTime dateTime) {
    return duration(startTime, dateTime);
  }

  public Duration timeRemaining(LocalTime dateTime) {
    return duration(dateTime, endTime);
  }

  private static Duration duration(LocalTime start,
                                   LocalTime end) {
    return Duration.ofSeconds(end.toSecondOfDay() - start.toSecondOfDay());
  }
}
