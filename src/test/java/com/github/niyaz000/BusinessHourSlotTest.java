package com.github.niyaz000;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class BusinessHourSlotTest {

  @Test
  void test1_null_args() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new BusinessHourSlot(LocalTime.NOON, null));

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new BusinessHourSlot(null, LocalTime.NOON));
  }

  @Test
  void test1_startGtThanOrEqEnd() {

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new BusinessHourSlot(LocalTime.of(10, 30, 0), LocalTime.of(10, 30, 0)));

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new BusinessHourSlot(LocalTime.of(10, 30, 0), LocalTime.of(10, 29, 59)));
  }

  @Test
  void test_duration() {
    var b = new BusinessHourSlot(LocalTime.of(10, 30, 30), LocalTime.of(10, 40, 30));
    Assertions.assertThat(b.duration().toSeconds()).isEqualTo(600);

    b = new BusinessHourSlot(LocalTime.of(10, 30, 30, 1234), LocalTime.of(10, 40, 30, 12345));
    Assertions.assertThat(b.duration().toSeconds()).isEqualTo(600);

  }

  @Test
  void test_isWithinSlot() {
    var b = new BusinessHourSlot(LocalTime.of(10, 30, 30, 1234), LocalTime.of(10, 40, 30, 12345));
    Assertions.assertThat(b.isWithinSlot(LocalTime.of(10, 30, 30))).isTrue();

    Assertions.assertThat(b.isWithinSlot(LocalTime.of(10, 40, 30))).isTrue();

    Assertions.assertThat(b.isWithinSlot(LocalTime.of(10, 33, 30))).isTrue();

    Assertions.assertThat(b.isWithinSlot(LocalTime.of(10, 40, 31))).isFalse();
    Assertions.assertThat(b.isWithinSlot(LocalTime.of(10, 29, 59))).isFalse();

  }

  @Test
  void test_isOutsideSlot() {
    var b = new BusinessHourSlot(LocalTime.of(10, 30, 30, 1234), LocalTime.of(10, 40, 30, 12345));
    Assertions.assertThat(b.isOutsideSlot(LocalTime.of(10, 30, 30))).isFalse();

    Assertions.assertThat(b.isOutsideSlot(LocalTime.of(10, 40, 30))).isFalse();

    Assertions.assertThat(b.isOutsideSlot(LocalTime.of(10, 33, 30))).isFalse();

    Assertions.assertThat(b.isOutsideSlot(LocalTime.of(10, 40, 31))).isTrue();
    Assertions.assertThat(b.isOutsideSlot(LocalTime.of(10, 29, 59))).isTrue();
  }

  @Test
  void test_timeElapsed() {

    var b = new BusinessHourSlot(LocalTime.of(10, 30, 30, 1234), LocalTime.of(10, 40, 30, 12345));
    Assertions.assertThat(b.timeElapsed(LocalTime.of(10, 30, 30)).toSeconds()).isZero();

    Assertions.assertThat(b.timeElapsed(LocalTime.of(10, 40, 30)).toSeconds()).isEqualTo(600);

    Assertions.assertThat(b.timeElapsed(LocalTime.of(10, 33, 30)).toSeconds()).isEqualTo(180);

    Assertions.assertThat(b.timeElapsed(LocalTime.of(10, 29, 30)).toSeconds()).isZero();
    Assertions.assertThat(b.timeElapsed(LocalTime.of(10, 41, 30)).toSeconds()).isZero();

  }

  @Test
  void test_timeRemaining() {
    var b = new BusinessHourSlot(LocalTime.of(10, 30, 30, 1234), LocalTime.of(10, 40, 30, 12345));
    Assertions.assertThat(b.timeRemaining(LocalTime.of(10, 30, 30)).toSeconds()).isEqualTo(600);

    Assertions.assertThat(b.timeRemaining(LocalTime.of(10, 40, 30)).toSeconds()).isZero();

    Assertions.assertThat(b.timeRemaining(LocalTime.of(10, 33, 30)).toSeconds()).isEqualTo(420);

    Assertions.assertThat(b.timeRemaining(LocalTime.of(10, 29, 30)).toSeconds()).isZero();
    Assertions.assertThat(b.timeRemaining(LocalTime.of(10, 41, 30)).toSeconds()).isZero();
  }

  @Test
  void test_equals() {
    var b1 = new BusinessHourSlot(LocalTime.of(10, 30, 30, 1234), LocalTime.of(10, 40, 30, 12345));
    var b2 = new BusinessHourSlot(LocalTime.of(10, 30, 30, 1234), LocalTime.of(10, 40, 30, 12345));

    Assertions.assertThat(b1).isEqualTo(b2);
    Assertions.assertThat(b1).isEqualTo(b1);
  }

  @Test
  void test_toString() {
    var b = new BusinessHourSlot(LocalTime.of(10, 30, 30, 1234), LocalTime.of(10, 40, 30, 12345));
    Assertions.assertThat(b.toString()).isEqualTo("BusinessHourSlot{startTime=10:30:30, endTime=10:40:30}");
  }
}
