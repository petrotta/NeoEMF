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

package fr.inria.atlanmod.neoemf.io.internal.impl;

import fr.inria.atlanmod.neoemf.io.PersistenceHandler;
import fr.inria.atlanmod.neoemf.io.beans.Attribute;
import fr.inria.atlanmod.neoemf.io.beans.ClassifierElement;
import fr.inria.atlanmod.neoemf.io.beans.Reference;
import fr.inria.atlanmod.neoemf.io.internal.InternalHandler;
import fr.inria.atlanmod.neoemf.io.internal.InternalProcessor;

/**
 *
 */
public class AbstractInternalProcessor implements InternalProcessor {

    private final InternalHandler internalHandler;

    protected AbstractInternalProcessor(InternalHandler internalHandler) {
        this.internalHandler = internalHandler;
    }

    @Override
    public void handleStartDocument() throws Exception {
        internalHandler.handleStartDocument();
    }

    @Override
    public void handleStartElement(ClassifierElement element) throws Exception {
        internalHandler.handleStartElement(element);
    }

    @Override
    public void handleAttribute(Attribute attribute) throws Exception {
        internalHandler.handleAttribute(attribute);
    }

    @Override
    public void handleReference(Reference reference) throws Exception {
        internalHandler.handleReference(reference);
    }

    @Override
    public void handleCharacters(String characters) throws Exception {
        internalHandler.handleCharacters(characters);
    }

    @Override
    public void handleEndElement() throws Exception {
        internalHandler.handleEndElement();
    }

    @Override
    public void handleEndDocument() throws Exception {
        internalHandler.handleEndDocument();
    }

    @Override
    public void addHandler(PersistenceHandler persistenceHandler) {
        internalHandler.addHandler(persistenceHandler);
    }

    @Override
    public boolean hasHandler() {
        return internalHandler.hasHandler();
    }

    @Override
    public Iterable<PersistenceHandler> getHandlers() {
        return internalHandler.getHandlers();
    }

    @Override
    public void notifyStartDocument() throws Exception {
        internalHandler.notifyStartDocument();
    }

    @Override
    public void notifyStartElement(ClassifierElement element) throws Exception {
        internalHandler.notifyStartElement(element);
    }

    @Override
    public void notifyAttribute(Attribute attribute) throws Exception {
        internalHandler.notifyAttribute(attribute);
    }

    @Override
    public void notifyReference(Reference reference) throws Exception {
        internalHandler.notifyReference(reference);
    }

    @Override
    public void notifyEndElement() throws Exception {
        internalHandler.notifyEndElement();
    }

    @Override
    public void notifyEndDocument() throws Exception {
        internalHandler.notifyEndDocument();
    }
}
