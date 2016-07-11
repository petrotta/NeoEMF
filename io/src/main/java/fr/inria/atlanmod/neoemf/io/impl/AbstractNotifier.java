/*
 * Copyright (c) 2013 Atlanmod INRIA LINA Mines Nantes.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Atlanmod INRIA LINA Mines Nantes - initial API and implementation
 */

package fr.inria.atlanmod.neoemf.io.impl;

import fr.inria.atlanmod.neoemf.io.IOHandler;
import fr.inria.atlanmod.neoemf.io.IONotifier;
import fr.inria.atlanmod.neoemf.io.beans.Attribute;
import fr.inria.atlanmod.neoemf.io.beans.Classifier;
import fr.inria.atlanmod.neoemf.io.beans.Reference;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An abstract implementation of a {@link IONotifier notifier}.
 */
public abstract class AbstractNotifier<T extends IOHandler> implements IONotifier<T> {

    private Set<T> handlers;

    public AbstractNotifier() {
        this.handlers = new HashSet<>();
    }

    @Override
    public void addHandler(T handler) {
        handlers.add(handler);
    }

    @Override
    public boolean hasHandler() {
        return !handlers.isEmpty();
    }

    @Override
    public Iterable<T> getHandlers() {
        return Collections.unmodifiableSet(handlers);
    }

    @Override
    public void notifyStartDocument() throws Exception {
        for (T h : getHandlers()) {
            h.handleStartDocument();
        }
    }

    @Override
    public void notifyStartElement(Classifier classifier) throws Exception {
        for (T h : getHandlers()) {
            h.handleStartElement(classifier);
        }
    }

    @Override
    public void notifyAttribute(Attribute attribute) throws Exception {
        for (T h : getHandlers()) {
            h.handleAttribute(attribute);
        }
    }

    @Override
    public void notifyReference(Reference reference) throws Exception {
        for (T h : getHandlers()) {
            h.handleReference(reference);
        }
    }

    @Override
    public void notifyEndElement() throws Exception {
        for (T h : getHandlers()) {
            h.handleEndElement();
        }
    }

    @Override
    public void notifyEndDocument() throws Exception {
        for (T h : getHandlers()) {
            h.handleEndDocument();
        }
    }
}