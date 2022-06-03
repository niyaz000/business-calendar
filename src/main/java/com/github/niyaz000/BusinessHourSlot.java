package com.github.niyaz000;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public final class BusinessHourSlot {

  private final LocalTime startTime;

  private final LocalTime endTime;

  public BusinessHourSlot(@NotNull LocalTime startTime,
                          @NotNull LocalTime endTime) {
    validate(startTime, endTime);
    this.startTime = startTime.truncatedTo(ChronoUnit.SECONDS);
    this.endTime = endTime.truncatedTo(ChronoUnit.SECONDS);
  }

  public LocalTime startTime() {
    return startTime;
  }

  public LocalTime endTime() {
    return endTime;
  }

  private void validate(LocalTime startTime,
                        LocalTime endTime) {
    Objects.requireNonNull(startTime, "startTime must not be null");
    Objects.requireNonNull(endTime, "endTime must not be null");
    if (startTime.compareTo(endTime) >= 0) {
      throw new IllegalArgumentException("startTime must be < endTime");
    }
  }

  @NotNull
  public Duration duration() {
    return Duration.ofNanos(endTime.toNanoOfDay() - startTime.toNanoOfDay()).truncatedTo(ChronoUnit.SECONDS);
  }

  public boolean isWithinSlot(LocalTime time) {
    return startTime.compareTo(time) <= 0 && endTime.compareTo(time) >= 0;
  }

  public boolean isOutsideSlot(LocalTime time) {
    return !isWithinSlot(time);
  }

  @NotNull
  public Duration timeElapsed(LocalTime time) {
    if (isOutsideSlot(time)) {
      return Duration.ofSeconds(0);
    }
    return duration(startTime, time);
  }

  @NotNull
  public Duration timeRemaining(LocalTime time) {
    if (isOutsideSlot(time)) {
      return Duration.ofSeconds(0);
    }
    return duration(time, endTime);
  }

  @NotNull
  private static Duration duration(LocalTime start,
                                   LocalTime end) {
    return Duration.ofSeconds(end.toSecondOfDay() - start.toSecondOfDay());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BusinessHourSlot that = (BusinessHourSlot) o;

    return Objects.equals(endTime, that.endTime);
  }

  @Override
  public int hashCode() {
    int result = startTime != null ? startTime.hashCode() : 0;
    result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "BusinessHourSlot{" +
            "startTime=" + startTime +
            ", endTime=" + endTime +
            '}';
  }

  public boolean isAfter(LocalTime time) {
    return time.compareTo(this.endTime) > 0;
  }

  public boolean isBefore(LocalTime time) {
    return time.compareTo(startTime) < 0;
  }

}
