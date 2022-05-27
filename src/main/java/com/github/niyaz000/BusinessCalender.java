package com.github.niyaz000;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public abstract class BusinessCalender {

  private Holidays holidays;
  private final List<WorkDay> workDays;

  public BusinessCalender(Holidays holidays,
                          List<WorkDay> workDays) {
    this.holidays = holidays;
    this.workDays = workDays;
  }

  public Collection<Holiday> holidays() {
    return holidays.holidays();
  }

  public abstract boolean isHoliday();

  public abstract boolean isWorkingDay();

  public abstract LocalDateTime nextWorkingDay();

  public abstract LocalDateTime previousWorkingDay();

  public abstract LocalDateTime lastBusinessHourStart(LocalDateTime dateTime);

  public abstract LocalDateTime lastBusinessHourEnd(LocalDateTime dateTime);

  public abstract LocalDateTime nextBusinessHourEnd(LocalDateTime dateTime);

  public abstract LocalDateTime nextBusinessHourStart(LocalDateTime dateTime);

  public abstract boolean isWithinBusinessHour(LocalDateTime dateTime);

  public abstract boolean isOutsideABusinessHour(LocalDateTime dateTime);

}
