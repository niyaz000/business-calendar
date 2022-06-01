package com.github.niyaz000;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class BusinessHourSlotTest {

  @Test
  void test1() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new BusinessHourSlot(LocalTime.NOON, null));

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new BusinessHourSlot(null, LocalTime.NOON));


    Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new BusinessHourSlot(LocalTime.of(10, 30, 0), LocalTime.of(10, 30, 0)));

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new BusinessHourSlot(LocalTime.of(10, 30, 0), LocalTime.of(10, 29, 59)));
  }

  @Test
  void test2() {
    var b = new BusinessHourSlot(LocalTime.of(10, 30, 30), LocalTime.of(10, 40, 30));
    Assertions.assertThat(b.duration().toSeconds()).isEqualTo(600);

    b = new BusinessHourSlot(LocalTime.of(10, 30, 30, 1234), LocalTime.of(10, 40, 30, 12345));
    Assertions.assertThat(b.duration().toSeconds()).isEqualTo(600);

  }
}
