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

package fr.inria.atlanmod.neoemf.io;

import fr.inria.atlanmod.neoemf.io.structure.Attribute;
import fr.inria.atlanmod.neoemf.io.structure.Classifier;
import fr.inria.atlanmod.neoemf.io.structure.Reference;

/**
 * A object able to notify previously registered {@link InputHandler}s of events during an I/O process, such as
 * import or export.
 *
 * @param <T> the type of handlers
 *
 * @see InputHandler
 */
public interface InputNotifier<T extends InputHandler> {

    /**
     * Add an {@link InputHandler} that will be notified.
     *
     * @param handler the handler to add
     */
    void addHandler(T handler);

    /**
     * Defines if this notifier has at least one {@link InputHandler} to notify.
     *
     * @return {@code true} if this notifier has at least one handler to notify.
     */
    boolean hasHandler();

    /**
     * Returns all registered handlers.
     *
     * @return an immutable collection
     */
    Iterable<T> getHandlers();

    /**
     * Notifies the start of a document to all registered handlers.
     *
     * @see #notifyEndDocument()
     * @see InputHandler#processStartDocument()
     */
    default void notifyStartDocument() {
        getHandlers().forEach(InputHandler::processStartDocument);
    }

    /**
     * Notifies the start of a new element to all registered handlers.
     *
     * @param classifier the classifier of the new element
     *
     * @see #notifyEndElement()
     * @see InputHandler#processStartElement(Classifier)
     */
    default void notifyStartElement(Classifier classifier) {
        getHandlers().forEach(h -> h.processStartElement(classifier));
    }

    /**
     * Notifies a new attribute to all registered handlers.
     *
     * @param attribute the new attribute
     *
     * @see InputHandler#processAttribute(Attribute)
     */
    default void notifyAttribute(Attribute attribute) {
        getHandlers().forEach(h -> h.processAttribute(attribute));
    }

    /**
     * Notifies a new reference to all registered handlers.
     *
     * @param reference the new reference
     *
     * @see InputHandler#processReference(Reference)
     */
    default void notifyReference(Reference reference) {
        getHandlers().forEach(h -> h.processReference(reference));
    }

    /**
     * Notifies a new set of characters to all registered handlers.
     *
     * @param characters the new characters
     *
     * @see InputHandler#processCharacters(String)
     */
    default void notifyCharacters(String characters) {
        getHandlers().forEach(p -> p.processCharacters(characters));
    }

    /**
     * Notifies the end of the current element to all registered handlers.
     *
     * @see #notifyStartElement(Classifier)
     * @see InputHandler#processEndElement()
     */
    default void notifyEndElement() {
        getHandlers().forEach(InputHandler::processEndElement);
    }

    /**
     * Notifies the end of the current document to all registered handlers.
     *
     * @see #notifyStartDocument()
     * @see InputHandler#processEndDocument()
     */
    default void notifyEndDocument() {
        getHandlers().forEach(InputHandler::processEndDocument);
    }
}
