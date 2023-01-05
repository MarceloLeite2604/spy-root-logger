package com.figtreelake.spyrootlogger.mapper;

import java.util.Collection;
import java.util.Collections;

/**
 * Map objects from one type to another and vice-versa.
 *
 * @param <F> Type to map from.
 * @param <T> Type to map to.
 *
 * @author MarceloLeite2604
 */
public interface Mapper<F, T> {

  /**
   * Map {@code F} parameter into an object of type {@code T}.
   *
   * @param value The {@code F} object to map into {@code T} type.
   * @return An object of {@code T} type created based on {@code value}
   * parameter.
   */
  T mapTo(F value);

  /**
   * Map {@code T} parameter into an object of type {@code F}.<br />
   * Since it is not necessary to implement such method for one-way mappers, the
   * interface has a default for it which will throw an
   * {@link UnsupportedOperationException} if invoked.
   *
   * @param value The {@code T} object to map into {@code F} type.
   * @return An object of {@code F} type created based on {@code value}
   * parameter.
   */
  default F mapFrom(T value) {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  /**
   * Map a collection of {@code T} objects into another collection of type
   * {@code F} objects.<br />
   * The default implementation will invoke {@link Mapper#mapTo(Object)} for
   * each element and return a new {@link java.util.List} with them.
   *
   * @param values A {@link Collection} of {@code T} objects to map into
   * {@code F} type.
   * @return A {@link Collection} of {@code F} type objects created based on
   * {@code values} parameter.
   */
  default Collection<T> mapAllTo(Collection<F> values) {

    if (values == null) {
      return Collections.emptyList();
    }

    return values.stream()
        .map(this::mapTo)
        .toList();
  }

  /**
   * Map a collection of {@code F} objects into another collection of type
   * {@code T} objects.<br />
   * The default implementation will invoke {@link Mapper#mapFrom(Object)} for
   * each element and return a new {@link java.util.List} with them.
   *
   * @param values A {@link Collection} of {@code F} objects to map into
   * {@code T} type.
   * @return A {@link Collection} of {@code T} type objects created based on
   * {@code values} parameter.
   */
  default Collection<F> mapAllFrom(Collection<T> values) {

    if (values == null) {
      return Collections.emptyList();
    }

    return values.stream()
        .map(this::mapFrom)
        .toList();
  }
}
