package com.github.niyaz000;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public final class Holidays {

  public Holidays(Collection<Holiday> holidays) {
    this.holidays = Collections.unmodifiableCollection(holidays);
  }

  private final Collection<Holiday> holidays;

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
