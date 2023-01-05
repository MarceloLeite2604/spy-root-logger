package com.figtreelake.spyrootlogger.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapperTest {

  private Mapper<Integer, String> oneWayMapper;

  private Mapper<String, String> twoWayMapper;

  @BeforeEach
  void setUp() {

    oneWayMapper = value -> null;

    twoWayMapper = new Mapper<>() {

      @Override
      public String mapTo(String value) {
        return "mappedTo-" + value;
      }

      @Override
      public String mapFrom(String value) {
        return "mappedFrom-" + value;
      }
    };
  }

  @Test
  void shouldThrowUnsupportedOperationExceptionWhenInvokingMapTo() {
    assertThrows(UnsupportedOperationException.class, () -> oneWayMapper.mapTo(9));
  }

  @Test
  void shouldThrowUnsupportedOperationExceptionWhenInvokingMapFrom() {
    assertThrows(UnsupportedOperationException.class, () -> oneWayMapper.mapFrom("value"));
  }

  @Test
  void shouldMapAllValuesFromCollectionWhenMapAllToIsInvoked() {
    final var input = Set.of("firstValue", "secondValue");
    final var expected = Set.of("mappedTo-firstValue", "mappedTo-secondValue");

    final var actual = twoWayMapper.mapAllTo(input);

    assertThat(actual).containsAll(expected);
  }

  @Test
  void shouldMapAllValuesFromCollectionWhenMapAllFromIsInvoked() {
    final var input = Set.of("firstValue", "secondValue");
    final var expected = Set.of("mappedFrom-firstValue", "mappedFrom-secondValue");

    final var actual = twoWayMapper.mapAllFrom(input);

    assertThat(actual).containsAll(expected);
  }

  @Test
  void shouldReturnEmptyListWhenMappingNullValueUsingMapAllTo() {
    final var actual = twoWayMapper.mapAllTo(null);

    assertThat(actual).isEmpty();
  }

  @Test
  void shouldReturnEmptyListWhenMappingNullValueUsingMapAllFrom() {
    final var actual = twoWayMapper.mapAllFrom(null);

    assertThat(actual).isEmpty();
  }

}