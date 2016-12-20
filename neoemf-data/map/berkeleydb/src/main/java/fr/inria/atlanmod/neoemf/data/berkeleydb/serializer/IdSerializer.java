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
package fr.inria.atlanmod.neoemf.data.berkeleydb.serializer;

import fr.inria.atlanmod.neoemf.core.Id;
import fr.inria.atlanmod.neoemf.core.StringId;
import org.apache.commons.lang3.SerializationUtils;

public class IdSerializer {

    public static byte[] serialize(Id id) {
        return SerializationUtils.serialize(id.toString());
    }

    public static Id deserialize(byte[] data) {
        //return (Id) SerializationUtils.deserialize(data);
        return new StringId(SerializationUtils.deserialize(data));
    }
}