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

  @NotNull
  private final List<BusinessHourSlot> businessHourSlots;

  @NotNull
  private final DayOfWeek day;

  public WorkDay(@NotNull List<BusinessHourSlot> businessHourSlots,
                 @NotNull DayOfWeek day) {
    Objects.requireNonNull(businessHourSlots, "businessHourSlots cannot be null or empty");
    Objects.requireNonNull(day, "day cannot be null");
    var slots = new ArrayList<>(businessHourSlots);
    slots.sort(Comparator.comparing(BusinessHourSlot::startTime));
    this.businessHourSlots = Collections.unmodifiableList(slots);
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

    var durationInSeconds = businessHourSlots
            .stream()
            .filter(slot -> slot.isAfter(time))
            .map(BusinessHourSlot::duration)
            .mapToLong(Duration::getSeconds)
            .sum();

    durationInSeconds += businessHourSlots
            .stream()
            .filter(slot -> slot.isWithinSlot(time))
            .mapToLong(slot -> slot.timeElapsed(time).getSeconds()).sum();

    return Duration.ofSeconds(durationInSeconds);

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

  public Optional<BusinessHourSlot> nextBusinessHourSlot(LocalTime time) {
    return businessHourSlots
            .stream()
            .filter(businessHourSlot -> businessHourSlot.isAfter(time))
            .findFirst();
  }

  @Override
  public String toString() {
    return "WorkDay{" +
            "businessHourSlots=" + businessHourSlots +
            ", day=" + day +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    WorkDay workDay = (WorkDay) o;

    if (!Objects.equals(businessHourSlots, workDay.businessHourSlots))
      return false;
    return day == workDay.day;
  }

  @Override
  public int hashCode() {
    int result = businessHourSlots.hashCode();
    result = 31 * result + day.hashCode();
    return result;
  }
}
