package fr.inria.atlanmod.neoemf.benchmarks.resource;

import fr.inria.atlanmod.commons.io.MoreFiles;
import fr.inria.atlanmod.neoemf.benchmarks.adapter.Adapter;
import fr.inria.atlanmod.neoemf.option.PersistenceOptions;

import org.eclipse.emf.ecore.resource.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static fr.inria.atlanmod.commons.Preconditions.checkArgument;

@ParametersAreNonnullByDefault
interface StoreCreator {

    @Nonnull
    static String getTargetFileName(File resourceFile, Adapter.Internal adapter) {
        Resources.checkValid(resourceFile.getName());
        checkArgument(resourceFile.exists(), "Resource '%s' does not exist", resourceFile);

        String targetFileName = MoreFiles.nameWithoutExtension(resourceFile.getAbsolutePath());

        // Has been converted ?
        if (targetFileName.contains("-id." + adapter.getResourceExtension() + ".")) {
            targetFileName = targetFileName.replaceFirst("-id", "");
        }

        return targetFileName + "." + adapter.getStoreExtension();
    }

    /**
     * Retrieves or creates a new {@link Resource} (a {@link fr.inria.atlanmod.neoemf.resource.PersistentResource} in
     * case of NeoEMF) from the given {@code file}, and stores it to the given {@code targetAdapter}, located in {@code
     * dir}.
     *
     * @param file    the resource file
     * @param adapter the adapter where to store the resource
     * @param dir     the location of the adapter
     *
     * @return the created file
     *
     * @throws IOException if a error occurs during the creation of the store
     */
    @Nonnull
    File getOrCreateStore(File file, PersistenceOptions options, Adapter.Internal adapter, Path dir) throws IOException;
}