package com.github.niyaz000;

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
}
