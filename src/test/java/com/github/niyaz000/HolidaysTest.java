package com.github.niyaz000;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class HolidaysTest {

  @Test
  void test_null_empty() {
    Assertions.assertThatThrownBy(() -> new Holidays(null))
            .isInstanceOf(NullPointerException.class);
  }

  @Test
  void test_overlap() {
    var holidays = List.of(Holiday.withEntireDayOff(LocalDate.of(2022, 5, 11)),
            Holiday.withEntireDayOff(LocalDate.of(2022, 5, 11)));

    Assertions.assertThatThrownBy(() -> new Holidays(holidays))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("holiday interval cannot overlap");

   var holidays1 = List.of(Holiday.withOffTimeBetween(LocalDateTime.parse("2022-05-14T10:30:23"), LocalDateTime.parse("2022-05-14T13:30:45")),
                       Holiday.withOffTimeBetween(LocalDateTime.parse("2022-05-14T11:00:00"), LocalDateTime.parse("2022-05-14T14:30:45")));

    Assertions.assertThatThrownBy(() -> new Holidays(holidays1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("holiday interval cannot overlap");
  }

  @Test
  void test_isHoliday() {
    var holidays = new Holidays(List.of(Holiday.withEntireDayOff(LocalDate.of(2022, 5, 11)),
            Holiday.withOffTimeBetween(LocalDateTime.parse("2022-05-14T10:30:23"), LocalDateTime.parse("2022-05-14T13:30:45"))));

    Assertions.assertThat(holidays.isHoliday(LocalDate.of(2022, 5, 11))).isTrue();
    Assertions.assertThat(holidays.isHoliday(LocalDate.of(2022, 5, 10))).isFalse();
    Assertions.assertThat(holidays.isHoliday(LocalDate.of(2022, 5, 12))).isFalse();

    Assertions.assertThat(holidays.isHoliday(LocalDateTime.parse("2022-05-14T10:30:23"))).isTrue();
    Assertions.assertThat(holidays.isHoliday(LocalDateTime.parse("2022-05-14T10:30:45"))).isTrue();
    Assertions.assertThat(holidays.isHoliday(LocalDateTime.parse("2022-05-14T10:30:43"))).isTrue();

    Assertions.assertThat(holidays.isHoliday(LocalDateTime.parse("2022-05-14T10:30:22"))).isFalse();
    Assertions.assertThat(holidays.isHoliday(LocalDateTime.parse("2022-05-14T13:30:46"))).isFalse();
  }

  @Test
  void test_toString() {
    var holidays = new Holidays(List.of(Holiday.withEntireDayOff(LocalDate.of(2022, 5, 11)),
            Holiday.withOffTimeBetween(LocalDateTime.parse("2022-05-14T10:30:23"), LocalDateTime.parse("2022-05-14T13:30:45"))));

    Assertions.assertThat(holidays.toString()).isEqualTo("Holidays{holidays=[Holiday{startTime=2022-05-11T00:00, endTime=2022-05-11T23:59:59}, Holiday{startTime=2022-05-14T10:30:23, endTime=2022-05-14T13:30:45}]}");
  }

  @Test
  void test_eq_hcode() {
    var holidays1 = new Holidays(List.of(Holiday.withEntireDayOff(LocalDate.of(2022, 5, 11)),
            Holiday.withOffTimeBetween(LocalDateTime.parse("2022-05-14T10:30:23"), LocalDateTime.parse("2022-05-14T13:30:45"))));

    var holidays2 = new Holidays(List.of(Holiday.withEntireDayOff(LocalDate.of(2022, 5, 11)),
            Holiday.withOffTimeBetween(LocalDateTime.parse("2022-05-14T10:30:23"), LocalDateTime.parse("2022-05-14T13:30:45"))));

    var holidays3 = new Holidays(List.of(Holiday.withEntireDayOff(LocalDate.of(2022, 5, 12)),
            Holiday.withOffTimeBetween(LocalDateTime.parse("2022-05-14T10:30:23"), LocalDateTime.parse("2022-05-14T13:30:45"))));

    Assertions.assertThat(holidays1.equals(holidays2)).isTrue();
    Assertions.assertThat(holidays1.equals(holidays3)).isFalse();
  }
}
