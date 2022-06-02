package com.github.niyaz000;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class WorkDay {

  private final List<BusinessHourSlot> businessHourSlots;

  private final DayOfWeek day;

  public WorkDay(@NotNull List<BusinessHourSlot> businessHourSlots,
                 @NotNull DayOfWeek day) {
    Objects.requireNonNull(businessHourSlots, "businessHourSlots cannot be null or empty");
    Objects.requireNonNull(day, "day cannot be null");
    new ArrayList<>(businessHourSlots).sort(Comparator.comparing(BusinessHourSlot::startTime));
    this.businessHourSlots = Collections.unmodifiableList(businessHourSlots);
    raiseExceptionOnOverlappingOrEmptySlots();
    this.day = day;
  }

  private void raiseExceptionOnOverlappingOrEmptySlots() {
    if (businessHourSlots.isEmpty()) {
      throw new IllegalArgumentException("businessHourSlots cannot be null or empty");
    }
    for (int i = 0; i < businessHourSlots.size() - 1; ++i) {
      if (businessHourSlots.get(i).endTime().compareTo(businessHourSlots.get(i + 1).startTime()) >= 0) {
        throw new IllegalArgumentException("businessHourSlots cannot overlap");
      }
    }
  }

  @NotNull
  public DayOfWeek getDay() {
    return day;
  }

  @NotNull
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

  @NotNull
  public Duration timeElapsed(LocalTime time) {
    return Duration.ofSeconds(businessHourSlots
            .stream()
            .map(businessHourSlot -> businessHourSlot.timeElapsed(time))
            .mapToLong(Duration::getSeconds)
            .sum());
  }

  @NotNull
  public Duration timeRemaining(LocalTime time) {
    return Duration.ofSeconds(businessHourSlots
            .stream()
            .map(businessHourSlot -> businessHourSlot.timeRemaining(time))
            .mapToLong(Duration::getSeconds)
            .sum());
  }

  public Optional<BusinessHourSlot> currentBusinessHourSlot(LocalTime time) {
    return businessHourSlots
            .stream()
            .filter(businessHourSlot -> businessHourSlot.isWithinSlot(time))
            .findFirst();
  }
}
