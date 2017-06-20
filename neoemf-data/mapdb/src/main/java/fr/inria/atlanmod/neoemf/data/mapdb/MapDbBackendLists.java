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

package fr.inria.atlanmod.neoemf.data.mapdb;

import fr.inria.atlanmod.neoemf.data.BackendFactory;
import fr.inria.atlanmod.neoemf.data.mapper.ManyReferenceAsManyValue;
import fr.inria.atlanmod.neoemf.data.mapper.ManyValueWithLists;
import fr.inria.atlanmod.neoemf.data.mapper.ReferenceAsValue;

import org.mapdb.DB;
import org.mapdb.Serializer;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A {@link MapDbBackend} that use a {@link ManyValueWithLists} mapping for storing features.
 *
 * @see MapDbBackendFactory
 */
@ParametersAreNonnullByDefault
class MapDbBackendLists extends AbstractMapDbBackend implements ReferenceAsValue, ManyValueWithLists, ManyReferenceAsManyValue {

    /**
     * Constructs a new {@code MapDbBackendLists} wrapping the provided {@code db}.
     * <p>
     * This constructor initialize the different {@link java.util.concurrent.ConcurrentMap}s from the MapDB engine and
     * set their respective {@link Serializer}s.
     * <p>
     * <b>Note:</b> This constructor is protected. To create a new {@link MapDbBackend} use {@link
     * BackendFactory#createPersistentBackend(org.eclipse.emf.common.util.URI, java.util.Map)}.
     *
     * @param db the {@link DB} used to creates the used {@link java.util.concurrent.ConcurrentMap}s and manage the
     *           database
     *
     * @see MapDbBackendFactory
     */
    protected MapDbBackendLists(DB db) {
        super(db);
    }
}
