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

package fr.inria.atlanmod.neoemf.demo.importer;

import fr.inria.atlanmod.neoemf.data.Backend;
import fr.inria.atlanmod.neoemf.data.BackendFactory;
import fr.inria.atlanmod.neoemf.data.hbase.HBaseBackendFactory;
import fr.inria.atlanmod.neoemf.data.hbase.option.HBaseOptions;
import fr.inria.atlanmod.neoemf.data.hbase.util.HBaseUri;
import fr.inria.atlanmod.neoemf.data.mapping.DataMapper;
import fr.inria.atlanmod.neoemf.data.store.StoreFactory;
import fr.inria.atlanmod.neoemf.io.Migrator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.gmt.modisco.java.JavaPackage;

import java.io.File;
import java.util.Map;

/**
 * Imports an existing model stored in a XMI files into a HBase-based {@link fr.inria.atlanmod.neoemf.resource.PersistentResource}
 * using a scalable XMI parser that bypasses the EMF API to improve performances and enable large XMI imports.
 */
public class DirectHBaseImporter {

    public static void main(String[] args) throws Exception {
        EPackage.Registry.INSTANCE.put(JavaPackage.eNS_URI, JavaPackage.eINSTANCE);

        Map<String, Object> options = HBaseOptions.builder()
                .autoSave()
                .asMap();

        BackendFactory factory = HBaseBackendFactory.getInstance();

        File sourceFile = new File("models/sample.xmi");
        URI targetUri = HBaseUri.builder().fromServer("localhost", 2181, "sample2.hbase");

        try (Backend backend = factory.createPersistentBackend(targetUri, options); DataMapper mapper = StoreFactory.getInstance().createStore(backend, options)) {
            Migrator.fromXmi(sourceFile)
                    .toMapper(mapper)
                    .withTimer()
                    .migrate();
        }
    }
}