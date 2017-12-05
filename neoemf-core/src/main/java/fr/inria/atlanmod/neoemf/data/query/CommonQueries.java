/*
 * Copyright (c) 2013-2017 Atlanmod, Inria, LS2N, and IMT Nantes.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v2.0 which accompanies
 * this distribution, and is available at https://www.eclipse.org/legal/epl-2.0/
 */

package fr.inria.atlanmod.neoemf.data.query;

import fr.inria.atlanmod.commons.annotation.Static;
import fr.inria.atlanmod.neoemf.core.Id;
import fr.inria.atlanmod.neoemf.data.bean.ClassBean;
import fr.inria.atlanmod.neoemf.data.mapping.ClassAlreadyExistsException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;

/**
 * Static utility class that provides common queries, and functions used to build queries.
 */
@Static
@ParametersAreNonnullByDefault
public final class CommonQueries {

    /**
     * This class should not be instantiated.
     *
     * @throws IllegalStateException every time
     */
    private CommonQueries() {
        throw new IllegalStateException("This class should not be instantiated");
    }

    /**
     * Returns a {@link Consumer} that process an already existing meta-class.
     *
     * @param <T> the type of the consumed object
     *
     * @return a consumer
     *
     * @see fr.inria.atlanmod.neoemf.data.mapping.ClassMapper#metaClassFor(Id, ClassBean)
     */
    @Nonnull
    public static <T> Consumer<T> classAlreadyExists() {
        return Functions.actionConsumer(() -> { throw new ClassAlreadyExistsException(); });
    }

    /**
     * Returns a deferred computation that sends the {@link RuntimeException} used when calling {@link
     * fr.inria.atlanmod.neoemf.data.mapping.DataMapper#allInstancesOf(Set)} on an instance that does not support this
     * operation.
     *
     * @return a deferred computation
     *
     * @see fr.inria.atlanmod.neoemf.data.mapping.ClassMapper#allInstancesOf(ClassBean, boolean)
     * @see fr.inria.atlanmod.neoemf.data.mapping.ClassMapper#allInstancesOf(Set)
     */
    @Nonnull
    public static <V> Flowable<V> unsupportedAllInstancesOf() {
        return Flowable.error(UnsupportedOperationException::new);
    }

    /**
     * Returns a {@link Maybe} instance into an {@link Optional}.
     *
     * @param maybe the {@code Maybe} to convert
     * @param <R>   the result type
     *
     * @return an {@link Optional} containing the value of {@code maybe}, or {@link Optional#empty()} if {@code maybe}
     * has no value
     */
    @Nonnull
    public static <R> Optional<R> toOptional(Maybe<R> maybe) {
        return maybe.to(v -> Optional.ofNullable(v.blockingGet()));
    }

    /**
     * Returns a {@link Single} instance into an {@link Optional}.
     *
     * @param single the {@code Single} to convert
     * @param <R>    the result type
     *
     * @return an {@link Optional} containing the value of {@code single}
     */
    @Nonnull
    public static <R> Optional<R> toOptional(Single<R> single) {
        return single.to(v -> Optional.ofNullable(v.blockingGet()));
    }

    /**
     * Returns a {@link Maybe} instance into an {@link Stream}.
     *
     * @param maybe the {@code Maybe} to convert
     * @param <R>   the result type
     *
     * @return a {@link Stream} containing the value of {@code maybe}
     */
    @Nonnull
    public static <R> Stream<R> toStream(Maybe<R> maybe) {
        return toStream(maybe.toFlowable());
    }

    /**
     * Returns a {@link Single} instance into an {@link Stream}.
     *
     * @param single the {@code Single} to convert
     * @param <R>    the result type
     *
     * @return a {@link Stream} containing the value of {@code single}
     */
    @Nonnull
    public static <R> Stream<R> toStream(Single<R> single) {
        return toStream(single.toFlowable());
    }

    /**
     * Returns a {@link Flowable} instance into an {@link Stream}.
     *
     * @param flowable the {@code Flowable} to convert
     * @param <R>      the result type
     *
     * @return a {@link Stream} containing the values of {@code flowable}
     */
    @Nonnull
    public static <R> Stream<R> toStream(Flowable<R> flowable) {
        return flowable.to(f -> f.toList().blockingGet().stream());
    }

    /**
     * Returns a {@link Observable} instance into an {@link Stream}.
     *
     * @param observable the {@code Observable} to convert
     * @param <R>        the result type
     *
     * @return a {@link Stream} containing the values of {@code observable}
     */
    @Nonnull
    public static <R> Stream<R> toStream(Observable<R> observable) {
        return observable.to(f -> f.toList().blockingGet().stream());
    }
}
