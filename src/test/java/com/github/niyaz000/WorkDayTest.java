package com.github.niyaz000;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class WorkDayTest {

  @Test
  void test1_null_args() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new WorkDay(null, DayOfWeek.MONDAY));

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new WorkDay(Collections.emptyList(), null));
  }

  @Test
  void test1_invalid_args() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new WorkDay(Collections.emptyList(), DayOfWeek.MONDAY));

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new WorkDay(List.of(new BusinessHourSlot(LocalTime.of(10, 30), LocalTime.of(10, 45)),
                    new BusinessHourSlot(LocalTime.of(10, 45), LocalTime.of(10, 46))), DayOfWeek.MONDAY));


    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new WorkDay(List.of(new BusinessHourSlot(LocalTime.of(10, 30), LocalTime.of(10, 45)),
                    new BusinessHourSlot(LocalTime.of(10, 44), LocalTime.of(10, 47))), DayOfWeek.MONDAY));
  }

  @Test
  void test1_getDay() {
    var w = new WorkDay(List.of(new BusinessHourSlot(LocalTime.of(10, 30), LocalTime.of(10, 45)),
            new BusinessHourSlot(LocalTime.of(10, 46), LocalTime.of(10, 47))), DayOfWeek.MONDAY);
    Assertions.assertThat(w.getDay()).isEqualTo(DayOfWeek.MONDAY);
  }

  @Test
  void test1_duration() {
    var w = new WorkDay(List.of(new BusinessHourSlot(LocalTime.of(10, 30), LocalTime.of(10, 45)),
            new BusinessHourSlot(LocalTime.of(10, 50), LocalTime.of(10, 53))), DayOfWeek.MONDAY);
    Assertions.assertThat(w.duration()).isEqualTo(Duration.ofMinutes(18));
  }

  @Test
  void test1_currentBusinessHourSlot() {
    var slot1 = new BusinessHourSlot(LocalTime.of(10, 30), LocalTime.of(10, 45));
    var slot2 = new BusinessHourSlot(LocalTime.of(10, 50), LocalTime.of(10, 53));
    var w = new WorkDay(List.of(slot1, slot2), DayOfWeek.MONDAY);

    Assertions.assertThat(w.currentBusinessHourSlot(LocalTime.of(10, 30)).get()).isEqualTo(slot1);
    Assertions.assertThat(w.currentBusinessHourSlot(LocalTime.of(10, 31)).get()).isEqualTo(slot1);
    Assertions.assertThat(w.currentBusinessHourSlot(LocalTime.of(10, 45)).get()).isEqualTo(slot1);

    Assertions.assertThat(w.currentBusinessHourSlot(LocalTime.of(10, 50)).get()).isEqualTo(slot2);
    Assertions.assertThat(w.currentBusinessHourSlot(LocalTime.of(10, 51)).get()).isEqualTo(slot2);
    Assertions.assertThat(w.currentBusinessHourSlot(LocalTime.of(10, 53)).get()).isEqualTo(slot2);

    Assertions.assertThat(w.currentBusinessHourSlot(LocalTime.of(10, 29))).isEmpty();
    Assertions.assertThat(w.currentBusinessHourSlot(LocalTime.of(10, 54))).isEmpty();
  }

  @Test
  void test1_isOutsideWorkDay() {
    var slot1 = new BusinessHourSlot(LocalTime.of(10, 30), LocalTime.of(10, 45));
    var slot2 = new BusinessHourSlot(LocalTime.of(10, 50), LocalTime.of(10, 53));
    var w = new WorkDay(List.of(slot1, slot2), DayOfWeek.MONDAY);

    Assertions.assertThat(w.isOutsideWorkDay(LocalTime.of(10, 30))).isFalse();
    Assertions.assertThat(w.isOutsideWorkDay(LocalTime.of(10, 31))).isFalse();
    Assertions.assertThat(w.isOutsideWorkDay(LocalTime.of(10, 45))).isFalse();

    Assertions.assertThat(w.isOutsideWorkDay(LocalTime.of(10, 50))).isFalse();
    Assertions.assertThat(w.isOutsideWorkDay(LocalTime.of(10, 51))).isFalse();
    Assertions.assertThat(w.isOutsideWorkDay(LocalTime.of(10, 53))).isFalse();

    Assertions.assertThat(w.isOutsideWorkDay(LocalTime.of(10, 29))).isTrue();
    Assertions.assertThat(w.isOutsideWorkDay(LocalTime.of(10, 54))).isTrue();
  }
}
