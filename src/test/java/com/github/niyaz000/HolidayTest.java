package com.github.niyaz000;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class HolidayTest {

  @Test
  void test_withEntireDayOff() {
    var h = Holiday.withEntireDayOff(LocalDate.of(2022, 5, 10));
    Assertions.assertThat(h.getStartTime()).isEqualTo("2022-05-10T00:00");
    Assertions.assertThat(h.getEndTime()).isEqualTo("2022-05-10T23:59:59");
  }

  @Test
  void test_withOffTimeBetween() {
    var h = Holiday.withOffTimeBetween(LocalDateTime.parse("2022-05-10T10:30:23"), LocalDateTime.parse("2022-05-10T10:40:45"));
    Assertions.assertThat(h.getStartTime()).isEqualTo("2022-05-10T10:30:23");
    Assertions.assertThat(h.getEndTime()).isEqualTo("2022-05-10T10:40:45");
  }

  @Test
  void test_duration() {
    var h = Holiday.withEntireDayOff(LocalDate.of(2022, 5, 10));
    Assertions.assertThat(h.duration()).isEqualTo(Duration.ofSeconds(59).plusHours(23).plusMinutes(59));

    h = Holiday.withOffTimeBetween(LocalDateTime.parse("2022-05-10T10:30:23"), LocalDateTime.parse("2022-05-10T10:40:45"));
    Assertions.assertThat(h.duration()).isEqualTo(Duration.ofSeconds(22).plusMinutes(10));
  }

  @Test
  void test_holiday() {
    var h = Holiday.withEntireDayOff(LocalDate.of(2022, 5, 10));
    Assertions.assertThat(h.isHoliday(LocalDate.of(2022, 5, 10))).isTrue();
    Assertions.assertThat(h.isHoliday(LocalDate.of(2022, 5, 11))).isFalse();

    h = Holiday.withOffTimeBetween(LocalDateTime.parse("2022-05-10T10:30:23"), LocalDateTime.parse("2022-05-10T10:40:45"));
    Assertions.assertThat(h.isHoliday(LocalDateTime.of(2022, 5, 10, 10, 30, 22))).isFalse();
    Assertions.assertThat(h.isHoliday(LocalDateTime.of(2022, 5, 10, 10, 40, 46))).isFalse();

    Assertions.assertThat(h.isHoliday(LocalDateTime.of(2022, 5, 10, 10, 30, 23))).isTrue();
    Assertions.assertThat(h.isHoliday(LocalDateTime.of(2022, 5, 10, 10, 40, 45))).isTrue();
    Assertions.assertThat(h.isHoliday(LocalDateTime.of(2022, 5, 10, 10, 31, 11))).isTrue();
  }

  @Test
  void test_isWithinHoliday() {
    var h = Holiday.withEntireDayOff(LocalDate.of(2022, 5, 10));
    Assertions.assertThat(h.isWithinHoliday(LocalDate.of(2022, 5, 10))).isTrue();
    Assertions.assertThat(h.isWithinHoliday(LocalDate.of(2022, 5, 11))).isFalse();
    Assertions.assertThat(h.isWithinHoliday(LocalDate.of(2022, 5, 9))).isFalse();

    h = Holiday.withOffTimeBetween(LocalDateTime.parse("2022-05-10T10:30:23"), LocalDateTime.parse("2022-05-10T10:40:45"));
    Assertions.assertThat(h.isWithinHoliday(LocalDateTime.parse("2022-05-10T10:30:22"))).isFalse();
    Assertions.assertThat(h.isWithinHoliday(LocalDateTime.parse("2022-05-10T10:40:46"))).isFalse();
    Assertions.assertThat(h.isWithinHoliday(LocalDateTime.parse("2022-05-10T10:30:50"))).isTrue();
  }

  @Test
  void test_timeElapsed() {
    var h = Holiday.withEntireDayOff(LocalDate.of(2022, 5, 10));
    Assertions.assertThat(h.timeElapsed(LocalDateTime.parse("2022-05-10T10:30:23"))).isEqualTo(Duration.ofSeconds(23).plusMinutes(30).plusHours(10));
    Assertions.assertThat(h.timeElapsed(LocalDateTime.parse("2022-05-10T00:00:00"))).isEqualTo(Duration.ZERO);
    Assertions.assertThat(h.timeElapsed(LocalDateTime.parse("2022-05-11T00:00:00"))).isEqualTo(Duration.ZERO);
    Assertions.assertThat(h.timeElapsed(LocalDateTime.parse("2022-05-10T00:00:00"))).isEqualTo(Duration.ZERO);

    h = Holiday.withOffTimeBetween(LocalDateTime.parse("2022-05-10T10:30:23"), LocalDateTime.parse("2022-05-10T10:40:45"));
    Assertions.assertThat(h.timeElapsed(LocalDateTime.parse("2022-05-10T10:30:22"))).isEqualTo(Duration.ZERO);
    Assertions.assertThat(h.timeElapsed(LocalDateTime.parse("2022-05-10T10:30:23"))).isEqualTo(Duration.ZERO);
    Assertions.assertThat(h.timeElapsed(LocalDateTime.parse("2022-05-10T10:40:46"))).isEqualTo(Duration.ZERO);
    Assertions.assertThat(h.timeElapsed(LocalDateTime.parse("2022-05-10T10:35:00"))).isEqualTo(Duration.ofMinutes(4).plusSeconds(37));
    Assertions.assertThat(h.timeElapsed(LocalDateTime.parse("2022-05-10T10:40:45"))).isEqualTo(Duration.ofMinutes(10).plusSeconds(22));
  }

  @Test
  void test_timeRemaining() {
    var h = Holiday.withEntireDayOff(LocalDate.of(2022, 5, 10));
    Assertions.assertThat(h.timeRemaining(LocalDateTime.parse("2022-05-10T10:30:23"))).isEqualTo(Duration.ofSeconds(36).plusMinutes(29).plusHours(13));
    Assertions.assertThat(h.timeRemaining(LocalDateTime.parse("2022-05-10T00:00:00"))).isEqualTo(Duration.ofSeconds(59).plusMinutes(59).plusHours(23));
    Assertions.assertThat(h.timeRemaining(LocalDateTime.parse("2022-05-11T00:00:00"))).isEqualTo(Duration.ZERO);

    h = Holiday.withOffTimeBetween(LocalDateTime.parse("2022-05-10T10:30:23"), LocalDateTime.parse("2022-05-10T10:40:45"));
    Assertions.assertThat(h.timeRemaining(LocalDateTime.parse("2022-05-10T10:30:22"))).isEqualTo(Duration.ZERO);
    Assertions.assertThat(h.timeRemaining(LocalDateTime.parse("2022-05-10T10:30:23"))).isEqualTo(Duration.ofMinutes(10).plusSeconds(22));
    Assertions.assertThat(h.timeRemaining(LocalDateTime.parse("2022-05-10T10:40:46"))).isEqualTo(Duration.ZERO);
    Assertions.assertThat(h.timeRemaining(LocalDateTime.parse("2022-05-10T10:35:00"))).isEqualTo(Duration.ofMinutes(5).plusSeconds(45));
    Assertions.assertThat(h.timeRemaining(LocalDateTime.parse("2022-05-10T10:40:45"))).isEqualTo(Duration.ZERO);
  }

  @Test
  void test_toString() {
    var h = Holiday.withEntireDayOff(LocalDate.of(2022, 5, 10));
    Assertions.assertThat(h.toString()).isEqualTo("Holiday{startTime=2022-05-10T00:00, endTime=2022-05-10T23:59:59}");
  }

  @Test
  void test_eq_hcode() {
    var h1 = Holiday.withEntireDayOff(LocalDate.of(2022, 5, 10));
    var h2 = Holiday.withEntireDayOff(LocalDate.of(2022, 5, 10));
    var h3 = Holiday.withEntireDayOff(LocalDate.of(2022, 5, 11));

    Assertions.assertThat(h1.equals(h2)).isTrue();
    Assertions.assertThat(h1.equals(h3)).isFalse();
  }
}
