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

package fr.inria.atlanmod.neoemf.io.persistence;

import fr.inria.atlanmod.neoemf.io.structure.Attribute;
import fr.inria.atlanmod.neoemf.io.structure.Classifier;
import fr.inria.atlanmod.neoemf.io.structure.Identifier;
import fr.inria.atlanmod.neoemf.io.structure.Reference;
import fr.inria.atlanmod.neoemf.util.logging.NeoLogger;

import java.util.Objects;

/**
 * A {@link PersistenceHandler} wrapper that logs every events.
 */
public class LoggingPersistenceHandlerDecorator extends AbstractPersistenceHandlerDecorator {

    /**
     * The current identifier, used to replace a full reference by "this".
     */
    private Identifier currentId;

    /**
     * Constructs a new {@code LoggingPersistenceHandlerDecorator}.
     *
     * @param handler the underlying handler
     */
    public LoggingPersistenceHandlerDecorator(PersistenceHandler handler) {
        super(handler);
    }

    @Override
    public void processStartDocument() {
        NeoLogger.debug("Starting document");

        super.processStartDocument();
    }

    @Override
    public void processStartElement(Classifier classifier) {
        NeoLogger.debug("[E] {0}:{1} \"{2}\" : {3} = {4}",
                classifier.getNamespace().getPrefix(),
                classifier.getLocalName(),
                classifier.getClassName(),
                classifier.getMetaClassifier().getLocalName(),
                classifier.getId());

        currentId = classifier.getId();

        super.processStartElement(classifier);
    }

    @Override
    public void processAttribute(Attribute attribute) {
        NeoLogger.debug("[A]    {0} ({1}) = {2}",
                attribute.getLocalName(),
                attribute.getIndex(),
                attribute.getValue());

        super.processAttribute(attribute);
    }

    @Override
    public void processReference(Reference reference) {
        NeoLogger.debug("[R]    {0} ({1}) = {2} -> {3}",
                reference.getLocalName(),
                reference.getIndex(),
                Objects.isNull(reference.getId()) ? "this" : reference.getId(),
                Objects.equals(reference.getIdReference(), currentId) ? "this" : reference.getIdReference());

        super.processReference(reference);
    }

    @Override
    public void processEndDocument() {
        NeoLogger.debug("Ending document");

        super.processEndDocument();
    }
}