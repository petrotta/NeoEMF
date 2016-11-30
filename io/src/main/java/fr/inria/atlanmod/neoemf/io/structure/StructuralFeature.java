/*
 * Copyright (c) 2013-2016 Atlanmod INRIA LINA Mines Nantes.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Atlanmod INRIA LINA Mines Nantes - initial API and implementation
 */

package fr.inria.atlanmod.neoemf.io.structure;

import fr.inria.atlanmod.neoemf.datastore.store.PersistentStore;

/**
 * A simple structural feature which can be either a reference or an attribute.
 */
public abstract class StructuralFeature extends NamedElement {

    private Identifier id;
    private int index;
    private boolean many;

    public StructuralFeature(String localName) {
        super(localName);
        this.index = PersistentStore.NO_INDEX;
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isMany() {
        return many;
    }

    public void setMany(boolean many) {
        this.many = many;
    }

    /**
     * Defines if this structural element is a {@link Reference}.
     *
     * @return {@code true} if this structural element is a reference.
     */
    public boolean isReference() {
        return false;
    }

    /**
     * Defines if this structural element is a {@link Attribute}.
     *
     * @return {@code true} if this structural element is a attribute.
     */
    public boolean isAttribute() {
        return false;
    }
}
