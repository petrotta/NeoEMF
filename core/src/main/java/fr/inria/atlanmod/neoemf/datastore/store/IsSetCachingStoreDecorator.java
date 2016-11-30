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

package fr.inria.atlanmod.neoemf.datastore.store;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import fr.inria.atlanmod.neoemf.cache.FeatureKey;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * A {@link PersistentStore} wrapper that caches the presence of a value.
 */
public class IsSetCachingStoreDecorator extends AbstractPersistentStoreDecorator {

    private final Cache<FeatureKey, Boolean> isSetCache;

    public IsSetCachingStoreDecorator(PersistentStore eStore) {
        this(eStore, 10000);
    }

    public IsSetCachingStoreDecorator(PersistentStore eStore, int cacheSize) {
        super(eStore);
        this.isSetCache = Caffeine.newBuilder().maximumSize(cacheSize).build();
    }

    @Override
    public Object get(InternalEObject object, EStructuralFeature feature, int index) {
        Object returnValue = super.get(object, feature, index);
        if (nonNull(returnValue)) {
            FeatureKey featureKey = FeatureKey.from(object, feature);
            isSetCache.put(featureKey, true);
        }
        return returnValue;
    }

    @Override
    public Object set(InternalEObject object, EStructuralFeature feature, int index, Object value) {
        FeatureKey featureKey = FeatureKey.from(object, feature);
        isSetCache.put(featureKey, true);
        return super.set(object, feature, index, value);
    }

    @Override
    public boolean isSet(InternalEObject object, EStructuralFeature feature) {
        FeatureKey featureKey = FeatureKey.from(object, feature);
        Boolean isSet = isSetCache.getIfPresent(featureKey);
        return isNull(isSet) ? super.isSet(object, feature) : isSet;
    }

    @Override
    public void unset(InternalEObject object, EStructuralFeature feature) {
        FeatureKey featureKey = FeatureKey.from(object, feature);
        isSetCache.put(featureKey, false);
        super.unset(object, feature);
    }

    @Override
    public boolean contains(InternalEObject object, EStructuralFeature feature, Object value) {
        boolean returnValue = super.contains(object, feature, value);
        if (returnValue) {
            FeatureKey featureKey = FeatureKey.from(object, feature);
            isSetCache.put(featureKey, true);
        }
        return returnValue;
    }

    @Override
    public void add(InternalEObject object, EStructuralFeature feature, int index, Object value) {
        FeatureKey featureKey = FeatureKey.from(object, feature);
        isSetCache.put(featureKey, true);
        super.add(object, feature, index, value);
    }

    @Override
    public Object remove(InternalEObject object, EStructuralFeature feature, int index) {
        FeatureKey featureKey = FeatureKey.from(object, feature);
        isSetCache.invalidate(featureKey);
        return super.remove(object, feature, index);
    }

    @Override
    public Object move(InternalEObject object, EStructuralFeature feature, int targetIndex, int sourceIndex) {
        FeatureKey featureKey = FeatureKey.from(object, feature);
        isSetCache.put(featureKey, true);
        return super.move(object, feature, targetIndex, sourceIndex);
    }

    @Override
    public void clear(InternalEObject object, EStructuralFeature feature) {
        FeatureKey featureKey = FeatureKey.from(object, feature);
        isSetCache.put(featureKey, false);
        super.clear(object, feature);
    }
}
