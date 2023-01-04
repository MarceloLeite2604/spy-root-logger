package com.figtreelake.spyrootlogger.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapperTest {

  private Mapper<Integer, String> mapper;

  private Mapper<String, String> mapperImplementation;

  @BeforeEach
  void setUp() {

    mapper = new Mapper<>() {};

    mapperImplementation = new Mapper<>() {

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
    assertThrows(UnsupportedOperationException.class, () -> mapper.mapTo(9));
  }

  @Test
  void shouldThrowUnsupportedOperationExceptionWhenInvokingMapFrom() {
    assertThrows(UnsupportedOperationException.class, () -> mapper.mapFrom("value"));
  }

  @Test
  void shouldMapAllValuesFromCollectionWhenMapAllToIsInvoked() {
    final var input = Set.of("firstValue", "secondValue");
    final var expected = Set.of("mappedTo-firstValue", "mappedTo-secondValue");

    final var actual = mapperImplementation.mapAllTo(input);

    assertThat(actual).containsAll(expected);
  }

  @Test
  void shouldMapAllValuesFromCollectionWhenMapAllFromIsInvoked() {
    final var input = Set.of("firstValue", "secondValue");
    final var expected = Set.of("mappedFrom-firstValue", "mappedFrom-secondValue");

    final var actual = mapperImplementation.mapAllFrom(input);

    assertThat(actual).containsAll(expected);
  }

  @Test
  void shouldReturnEmptyListWhenMappingNullValueUsingMapAllTo() {
    final var actual = mapperImplementation.mapAllTo(null);

    assertThat(actual).isEmpty();
  }

  @Test
  void shouldReturnEmptyListWhenMappingNullValueUsingMapAllFrom() {
    final var actual = mapperImplementation.mapAllFrom(null);

    assertThat(actual).isEmpty();
  }

}