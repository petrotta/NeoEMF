/*
 * Copyright (c) 2013-2017 Atlanmod INRIA LINA Mines Nantes.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Atlanmod INRIA LINA Mines Nantes - initial API and implementation
 */

package fr.inria.atlanmod.neoemf.data.mapper;

import fr.inria.atlanmod.neoemf.core.Id;
import fr.inria.atlanmod.neoemf.data.structure.ClassDescriptor;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An object capable of mapping metaclasses represented as a set of key/value pair.
 *
 * @see ClassDescriptor
 */
@ParametersAreNonnullByDefault
public interface ClassMapper {

    /**
     * Returns whether this mapper supports the {@link ClassDescriptor} mapping.
     *
     * @return {@code true} if this mapper supports the {@link ClassDescriptor} mapping, {@code false} otherwise
     */
    default boolean supportsClassMapping() {
        return true;
    }

    /**
     * Retrieves the {@link ClassDescriptor} for the specified {@code id}.
     *
     * @param id the {@link Id} of the element
     *
     * @return an {@link Optional} containing the {@link ClassDescriptor}, or {@link Optional#empty()} if the {@code id}
     * has no defined metaclass.
     *
     * @throws NullPointerException if any parameter is {@code null}
     */
    @Nonnull
    Optional<ClassDescriptor> metaclassOf(Id id);

    /**
     * Stores the {@link ClassDescriptor} for the specified {@code id}.
     *
     * @param id        the {@link Id} of the element
     * @param metaclass the {@link ClassDescriptor} containing element's metaclass information to store
     *
     * @throws NullPointerException if any parameter is {@code null}
     */
    void metaclassFor(Id id, ClassDescriptor metaclass);

    /**
     * Retrieves all instances of the given {@code metaclass}.
     *
     * @param metaclass the metaclass to compute the instances of
     * @param strict    {@code true} if the lookup searches for strict instances
     *
     * @return a {@link Iterable} containing the instances of the {@link ClassDescriptor}
     *
     * @throws UnsupportedOperationException if the mapper doesn't support the lookup of all instances
     */
    @Nonnull
    default Iterable<Id> allInstancesOf(ClassDescriptor metaclass, boolean strict) {
        throw new UnsupportedOperationException("This back-end doesn't support the lookup of all instances");
    }
}