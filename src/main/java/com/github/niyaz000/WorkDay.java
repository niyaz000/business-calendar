package com.github.niyaz000;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public abstract class WorkDay {

  private final List<BusinessHourSlot> businessHourSlots;

  public WorkDay(List<BusinessHourSlot> businessHourSlots,
                 DayOfWeek day) {
    this.businessHourSlots = businessHourSlots;
    this.day = day;
  }

  private final DayOfWeek day;


  public DayOfWeek getDay() {
    return day;
  }

  public Duration duration() {
    var seconds = businessHourSlots
            .stream()
            .map(BusinessHourSlot::duration)
            .mapToLong(Duration::toSeconds)
            .summaryStatistics()
            .getSum();

    return Duration.ofSeconds(seconds);
  }

  public boolean isWithinWorkDay(LocalTime time) {
    return businessHourSlots
            .stream()
            .anyMatch(businessHourSlot -> businessHourSlot.isWithinSlot(time));
  }

  public boolean isOutsideWorkDay(LocalTime time) {
    return !isWithinWorkDay(time);
  }

  public Duration timeElapsed(LocalTime dateTime) {
//    var businessHourSlot = nextBusinessHourSlot(time);
    return Duration.ofSeconds(0);
  }

  public Duration timeRemaining(LocalTime dateTime) {
    return Duration.ofSeconds(0);
//    return duration(dateTime, endTime);
  }

  private static Duration duration(LocalTime start,
                                   LocalTime end) {
    return Duration.ofSeconds(end.toSecondOfDay() - start.toSecondOfDay());
  }

  private BusinessHourSlot nextBusinessHourSlot(LocalTime time) {
    return businessHourSlots
            .stream()
            .filter(businessHourSlot -> businessHourSlot.isWithinSlot(time))
            .findFirst()
            .orElse(new BusinessHourSlot(null, null));
  }
}
