package com.github.niyaz000;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public final class Holidays {

  private final Collection<Holiday> holidays;

  public Holidays(Collection<Holiday> holidays) {
    Objects.requireNonNull(holidays, "holidays cannot be null");
    this.holidays = Collections.unmodifiableCollection(holidays);
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

}
