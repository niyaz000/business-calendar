package com.github.niyaz000;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class Holiday {

  private LocalDateTime start;

  private LocalDateTime end;

  public static Holiday withEntireDayOff(LocalDate date) {
    return new Holiday(date.atStartOfDay());
  }

  public static Holiday withOffTimeBetween(LocalDate start, LocalDate end) {
    return new Holiday(start.atStartOfDay(), end.atTime(23, 59, 59));
  }

  private Holiday(LocalDateTime start) {
    new Holiday(start, start.withHour(23).withMinute(59).withSecond(59));
  }

  private Holiday(LocalDateTime start, LocalDateTime end) {
    this.start = start.truncatedTo(ChronoUnit.SECONDS);
    this.end = end.truncatedTo(ChronoUnit.SECONDS);
  }

  public Duration duration(ChronoUnit d) {
    return Duration.ofSeconds(end.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC)).truncatedTo(d);
  }

  public boolean isHoliday(LocalDate time) {
    return isHoliday(time.atStartOfDay());
  }

  public boolean isHoliday(LocalDateTime time) {
    return start.compareTo(time) >=0 && end.compareTo(time) <= 0;
  }

  public Duration timeElapsed(LocalDateTime dateTime) {
    return duration(start, dateTime);
  }

  public Duration timeRemaining(LocalDateTime dateTime) {
    return duration(dateTime, end);
  }

  private static Duration duration(LocalDateTime start,
                                   LocalDateTime end) {
    return Duration.ofSeconds(end.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC)).truncatedTo(ChronoUnit.SECONDS);
  }
}
