package com.github.niyaz000;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public final class Holidays {

  @NotNull
  private final List<Holiday> holidays;

  public Holidays(Collection<Holiday> holidays) {
    Objects.requireNonNull(holidays, "holidays cannot be null");
    var tempList = new ArrayList<>(holidays);
    tempList.sort(Comparator.comparing(Holiday::getStartTime));
    this.holidays = Collections.unmodifiableList(tempList);
    raiseExceptionOnOverlappingHolidays();
  }

  private void raiseExceptionOnOverlappingHolidays() {
    for (int i = 0; i < holidays.size() - 1; ++i) {
      if (holidays.get(i).getEndTime().compareTo(holidays.get(i + 1).getStartTime()) >= 0) {
        throw new IllegalArgumentException("holiday interval cannot overlap");
      }
    }
  }

  public boolean isHoliday(LocalDate dateTime) {
    return isHoliday(dateTime.atStartOfDay());
  }

  public boolean isHoliday(LocalDateTime dateTime) {
    return holidays.stream().anyMatch(holiday -> holiday.isHoliday(dateTime));
  }

  public Collection<Holiday> holidays() {
    return holidays;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Holidays holidays1 = (Holidays) o;

    return Objects.equals(holidays, holidays1.holidays);
  }

  @Override
  public String toString() {
    return "Holidays{" +
            "holidays=" + holidays +
            '}';
  }

  @Override
  public int hashCode() {
    return holidays.hashCode();
  }
}
