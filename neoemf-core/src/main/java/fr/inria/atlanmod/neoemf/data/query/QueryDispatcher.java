/*
 * Copyright (c) 2013-2017 Atlanmod, Inria, LS2N, and IMT Nantes.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v2.0 which accompanies
 * this distribution, and is available at https://www.eclipse.org/legal/epl-2.0/
 */

package fr.inria.atlanmod.neoemf.data.query;

import fr.inria.atlanmod.neoemf.data.Backend;

import java.io.Closeable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.reactivex.CompletableTransformer;
import io.reactivex.FlowableTransformer;
import io.reactivex.MaybeTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;

/**
 * An object that dispatches and executes queries on a {@link Backend} instance.
 * <p>
 * <b>NOTE:</b> Each query is executed once.
 */
@ParametersAreNonnullByDefault
public interface QueryDispatcher extends Closeable {

    /**
     * Asynchronously executes the given {@code query}, using the {@code backend}.
     *
     * @return the deferred computation
     */
    @Nonnull
    CompletableTransformer dispatchCompletable();

    /**
     * Asynchronously executes the given {@code query}, using the {@code backend}.
     *
     * @param <T> the type of the expected result
     *
     * @return the deferred computation with a result
     */
    @Nonnull
    <T> MaybeTransformer<T, T> dispatchMaybe();

    /**
     * Asynchronously executes the given {@code query}, using the {@code backend}.
     *
     * @param <T> the type of the expected result
     *
     * @return the deferred computation with a result
     */
    @Nonnull
    <T> SingleTransformer<T, T> dispatchSingle();

    /**
     * Asynchronously executes the given {@code query}, using the {@code backend}.
     *
     * @param <T> the type of the expected result
     *
     * @return the deferred computation with results
     */
    @Nonnull
    <T> ObservableTransformer<T, T> dispatchObservable();

    /**
     * Asynchronously executes the given {@code query}, using the {@code backend}.
     *
     * @param <T> the type of the expected result
     *
     * @return the deferred computation with results
     */
    @Nonnull
    <T> FlowableTransformer<T, T> dispatchFlowable();
}
