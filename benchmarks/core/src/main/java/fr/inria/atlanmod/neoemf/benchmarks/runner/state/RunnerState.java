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

package fr.inria.atlanmod.neoemf.benchmarks.runner.state;

import fr.inria.atlanmod.commons.log.Log;
import fr.inria.atlanmod.neoemf.benchmarks.adapter.Adapter;
import fr.inria.atlanmod.neoemf.benchmarks.adapter.BerkeleyDbAdapter;
import fr.inria.atlanmod.neoemf.benchmarks.adapter.BlueprintsAdapter;
import fr.inria.atlanmod.neoemf.benchmarks.adapter.CdoAdapter;
import fr.inria.atlanmod.neoemf.benchmarks.adapter.MapDbAdapter;
import fr.inria.atlanmod.neoemf.benchmarks.adapter.XmiAdapter;
import fr.inria.atlanmod.neoemf.option.CommonOptions;
import fr.inria.atlanmod.neoemf.option.PersistenceOptions;
import fr.inria.atlanmod.neoemf.option.PersistentStoreOptions;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.RegEx;

import static fr.inria.atlanmod.commons.Preconditions.checkState;
import static java.util.Objects.nonNull;

/**
 * This state contains all the benchmarks parameters, and provides a ready-to-use {@link Adapter} and the preloaded
 * resource file. <p> <p>Note:</p> It does not load the datastores.
 */
@State(Scope.Thread)
public class RunnerState {

    /**
     * A map that holds all existing {@link Adapter} instances, identified by their name.
     */
    @Nonnull
    private static final Map<String, Class<? extends Adapter>> ADAPTERS = new HashMap<>();

    static {
        ADAPTERS.put("xmi", XmiAdapter.class);
        ADAPTERS.put("cdo", CdoAdapter.class);
        ADAPTERS.put("mapdb-i", MapDbAdapter.WithIndices.class);
        ADAPTERS.put("mapdb-a", MapDbAdapter.WithArrays.class);
        ADAPTERS.put("mapdb-l", MapDbAdapter.WithLists.class);
        ADAPTERS.put("berkeleydb-i", BerkeleyDbAdapter.WithIndices.class);
        ADAPTERS.put("berkeleydb-a", BerkeleyDbAdapter.WithArrays.class);
        ADAPTERS.put("berkeleydb-l", BerkeleyDbAdapter.WithLists.class);
        ADAPTERS.put("tinker", BlueprintsAdapter.Tinker.class);
        ADAPTERS.put("neo4j", BlueprintsAdapter.Neo4j.class);
    }

    // region JMH parameters

    /**
     * The name of the current {@link org.eclipse.emf.ecore.resource.Resource} file.
     */
    @Param({
            "set1",
            "set2",
            "set3",
    })
    protected String r;

    /**
     * The name of the current {@link Adapter}.
     */
    @Param({
            "xmi",
            "cdo",
            "neo4j",
            "berkeleydb-i",
            "mapdb-i",
    })
    protected String a;

    /**
     * The name of the current store chain.
     */
    @Param("AMC")
    protected String o;

    /**
     * {@code "true"} if the direct import has to be used when creating or importing resources.
     */
    @Param("true")
    protected String direct;

    // endregion

    /**
     * The current {@link org.eclipse.emf.ecore.resource.Resource} file.
     */
    private File resourceFile;

    /**
     * Returns the current adapter.
     */
    @Nonnull
    public Adapter adapter() {
        try {
            Adapter adapter;
            Class<? extends Adapter> instance = ADAPTERS.get(a);
            checkState(nonNull(instance), "No adapter named '%s' is registered", a);
            adapter = ADAPTERS.get(a).newInstance();
            return adapter;
        }
        catch (InstantiationException | IllegalAccessException e) {
            Log.error(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the current resource file.
     */
    @Nonnull
    public File resourceFile() {
        return resourceFile;
    }

    /**
     * Returns {@code true} if the direct import has to be used when creating or importing resources.
     */
    public boolean useDirectImport() {
        return Boolean.valueOf(direct);
    }

    /**
     * Loads and creates the current resource file.
     */
    @Setup(Level.Trial)
    public void initResource() throws IOException {
        Log.info("Initializing the resource");
        resourceFile = adapter().getOrCreateResource(r);
    }

    /**
     * Returns all existing {@link PersistentStoreOptions} instances.
     */
    @Nonnull
    public PersistenceOptions options() {
        return Options.parse(o);
    }

    /**
     * A class that handles options that can be used as benchmarks parameters.
     */
    @ParametersAreNonnullByDefault
    private final static class Options {

        /**
         * The regex of a number argument, as {@code {0189}}.
         */
        @RegEx
        private static final String ARG_NUMBER = "\\{(-?[0-9]+)\\}";

        /**
         * The regex of a text argument, as {@code {ABYZ}}.
         */
        @RegEx
        private static final String ARG_TEXT = "\\{([A-Z]+)\\}";

        /**
         * The option for caching features.
         */
        private static final String CACHE_FEATURES = "F";

        /**
         * The option for presence caching.
         */
        private static final String CACHE_IS_SET = "P";

        /**
         * The option for size caching.
         */
        private static final String CACHE_SIZES = "S";

        /**
         * The option for metaclass caching.
         */
        private static final String CACHE_METACLASSES = "M";

        /**
         * The option for container caching.
         */
        private static final String CACHE_CONTAINERS = "C";

        /**
         * The option for recording stats.
         */
        private static final String RECORD_STATS = "R";

        /**
         * The option for logging database calls.
         */
        private static final String LOG = "L";

        /**
         * The option for auto-saving.
         */
        private static final String AUTO_SAVE = "A";

        /**
         * The pattern for auto-saving, with a specified chunk.
         */
        private static final Pattern AUTO_SAVE_CHUCK = Pattern.compile(AUTO_SAVE + ARG_NUMBER, Pattern.CASE_INSENSITIVE);

        /**
         * Parses the given {@code text} and returns the associated {@link PersistenceOptions}.
         *
         * @param text the text containg the options to define
         *
         * @return a {@link PersistenceOptions}
         */
        @Nonnull
        public static PersistenceOptions parse(String text) {
            PersistenceOptions options = CommonOptions.builder();

            String upperText = text.toUpperCase();

            // Cache features
            if (upperText.contains(Options.CACHE_FEATURES)) {
                options.cacheFeatures();
            }

            // Cache presence
            if (upperText.contains(Options.CACHE_IS_SET)) {
                options.cacheIsSet();
            }

            // Cache sizes
            if (upperText.contains(Options.CACHE_SIZES)) {
                options.cacheSizes();
            }

            // Cache metaclasses (Defined by default)
            if (upperText.contains(Options.CACHE_METACLASSES)) {
                options.cacheMetaClasses();
            }

            // Cache containers (Defined by default)
            if (upperText.contains(Options.CACHE_CONTAINERS)) {
                options.cacheContainers();
            }

            // Stats recording
            if (upperText.contains(Options.RECORD_STATS)) {
                options.recordStats();
            }

            // Logging
            if (upperText.contains(Options.LOG)) {
                options.log();
            }

            // Auto-saving
            Matcher chuckMatcher = Options.AUTO_SAVE_CHUCK.matcher(upperText);
            if (chuckMatcher.find()) {
                options.autoSave(Long.parseLong(chuckMatcher.group(1)));
            }
            else if (upperText.contains(AUTO_SAVE)) {
                options.autoSave();
            }

            return options;
        }
    }
}