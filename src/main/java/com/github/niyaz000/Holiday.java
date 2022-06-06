package com.github.niyaz000;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import org.jetbrains.annotations.NotNull;

public class Holiday {

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  private final LocalDateTime startTime;

  private final LocalDateTime endTime;

  public static Holiday withEntireDayOff(LocalDate date) {
    return new Holiday(date.atStartOfDay());
  }

  public static Holiday withOffTimeBetween(LocalDateTime start, LocalDateTime end) {
    return new Holiday(start, end);
  }

  private Holiday(LocalDateTime start) {
    this(start, start.withHour(23).withMinute(59).withSecond(59));
  }

  private Holiday(@NotNull LocalDateTime start,
                  @NotNull LocalDateTime end) {
    this.startTime = start.truncatedTo(ChronoUnit.SECONDS);
    this.endTime = end.truncatedTo(ChronoUnit.SECONDS);
  }

  @NotNull
  public Duration duration() {
    return Duration.ofSeconds(endTime.toEpochSecond(ZoneOffset.UTC) - startTime.toEpochSecond(ZoneOffset.UTC));
  }

  public boolean isHoliday(LocalDate time) {
    return isHoliday(time.atStartOfDay());
  }

  public boolean isHoliday(LocalDateTime dateTime) {
    return isWithinHoliday(dateTime);
  }

  public boolean isWithinHoliday(LocalDate date) {
    return isWithinHoliday(date.atStartOfDay());
  }

  public boolean isWithinHoliday(LocalDateTime dateTime) {
    return startTime.compareTo(dateTime) <= 0 && endTime.compareTo(dateTime) >= 0;
  }

  @NotNull
  public Duration timeElapsed(LocalDateTime dateTime) {
    if(isWithinHoliday(dateTime)) {
      return duration(startTime, dateTime);
    }
    return Duration.ZERO;
  }

  @NotNull
  public Duration timeRemaining(LocalDateTime dateTime) {
    if(isWithinHoliday(dateTime)) {
      return duration(dateTime, endTime);
    }
    return Duration.ZERO;
  }

  @NotNull
  private static Duration duration(LocalDateTime start,
                                   LocalDateTime end) {
    return Duration.ofSeconds(end.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC)).truncatedTo(ChronoUnit.SECONDS);
  }
}
