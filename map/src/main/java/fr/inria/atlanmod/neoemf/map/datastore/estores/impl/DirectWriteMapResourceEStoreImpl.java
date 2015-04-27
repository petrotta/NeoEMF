/*******************************************************************************
 * Copyright (c) 2013 Atlanmod INRIA LINA Mines Nantes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Atlanmod INRIA LINA Mines Nantes - initial API and implementation
 *******************************************************************************/
package fr.inria.atlanmod.neoemf.map.datastore.estores.impl;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.jboss.util.collection.SoftValueHashMap;
import org.mapdb.DB;
import org.mapdb.Fun;
import org.mapdb.Fun.Tuple2;

import fr.inria.atlanmod.neoemf.core.Id;
import fr.inria.atlanmod.neoemf.core.PersistentEObject;
import fr.inria.atlanmod.neoemf.core.impl.NeoEObjectAdapterFactoryImpl;
import fr.inria.atlanmod.neoemf.datastore.InternalPersistentEObject;
import fr.inria.atlanmod.neoemf.datastore.estores.SearcheableResourceEStore;
import fr.inria.atlanmod.neoemf.logger.NeoLogger;
import fr.inria.atlanmod.neoemf.map.datastore.estores.impl.pojo.ContainerInfo;
import fr.inria.atlanmod.neoemf.map.datastore.estores.impl.pojo.EClassInfo;

public class DirectWriteMapResourceEStoreImpl implements SearcheableResourceEStore {

	protected static final String INSTANCE_OF = "kyanosInstanceOf";
	protected static final String CONTAINER = "eContainer";

	@SuppressWarnings("unchecked")
	protected Map<Object, InternalPersistentEObject> loadedEObjects = new SoftValueHashMap();
	
	protected DB db;
	
	protected Map<Tuple2<Id, String>, Object> map;
	
	protected Map<Id, EClassInfo> instanceOfMap;

	protected Map<Id, ContainerInfo> containersMap;
	
	protected Resource.Internal resource;

	public DirectWriteMapResourceEStoreImpl(Resource.Internal resource, DB db) {
		this.db = db;
		this.resource = resource;
		this.map = db.getHashMap("Kyanos");
		this.instanceOfMap = db.getHashMap(INSTANCE_OF);
		this.containersMap = db.getHashMap(CONTAINER);
	}


	@Override
	public Resource.Internal resource() {
		return resource;
	}


	@Override
	public Object get(InternalEObject object, EStructuralFeature feature, int index) {
		PersistentEObject kyanosEObject = NeoEObjectAdapterFactoryImpl.getAdapter(object, PersistentEObject.class);
		if (feature instanceof EAttribute) {
			return get(kyanosEObject, (EAttribute) feature, index);
		} else if (feature instanceof EReference) {
			return get(kyanosEObject, (EReference) feature, index);
		} else {
			throw new IllegalArgumentException(feature.toString());
		}
	}
	
	protected Object get(PersistentEObject object, EAttribute eAttribute, int index) {
		Object value = getFromMap(object, eAttribute);
		if (!eAttribute.isMany()) {
			return parseMapValue(eAttribute, value);
		} else {
			Object[] array = (Object[]) value;
			return parseMapValue(eAttribute, array[index]);
		}
	}

	protected Object get(PersistentEObject object, EReference eReference, int index) {
		Object value = getFromMap(object, eReference);
		if (!eReference.isMany()) {
			return eObject((Id) value);
		} else {
			Object[] array = (Object[]) value;
			return eObject((Id) array[index]);
		}
	}


	@Override
	public Object set(InternalEObject object, EStructuralFeature feature, int index, Object value) {
		PersistentEObject kyanosEObject = NeoEObjectAdapterFactoryImpl.getAdapter(object, PersistentEObject.class);
		if (feature instanceof EAttribute) {
			return set(kyanosEObject, (EAttribute) feature, index, value);
		} else if (feature instanceof EReference) {
			PersistentEObject referencedEObject = NeoEObjectAdapterFactoryImpl.getAdapter(value, PersistentEObject.class);
			return set(kyanosEObject, (EReference) feature, index, referencedEObject);
		} else {
			throw new IllegalArgumentException(feature.toString());
		}
	}

	protected Object set(PersistentEObject object, EAttribute eAttribute, int index, Object value) {
		if (!eAttribute.isMany()) {
			Object oldValue = map.put(Fun.t2(object.id(), eAttribute.getName()), serializeToMapValue(eAttribute, value));
			return parseMapValue(eAttribute, oldValue);
		} else {
			Object[] array = (Object[]) getFromMap(object, eAttribute);
			Object oldValue = array[index]; 
			array[index] = serializeToMapValue(eAttribute, value);
			map.put(Fun.t2(object.id(), eAttribute.getName()), array);
			return parseMapValue(eAttribute, oldValue);
		}
	}

	protected Object set(PersistentEObject object, EReference eReference, int index, PersistentEObject referencedObject) {
		updateContainment(object, eReference, referencedObject);
		updateInstanceOf(referencedObject);
		if (!eReference.isMany()) {
			Object oldId = map.put(Fun.t2(object.id(), eReference.getName()), referencedObject.id());
			return oldId != null ? eObject((Id) oldId) : null;
		} else {
			Object[] array = (Object[]) getFromMap(object, eReference);
			Object oldId = array[index];
			array[index] = referencedObject.id().toString();
			map.put(Fun.t2(object.id(), eReference.getName()), array);
			return oldId != null ? eObject((Id) oldId) : null;
		}
	}


	@Override
	public boolean isSet(InternalEObject object, EStructuralFeature feature) {
		PersistentEObject kyanosEObject = NeoEObjectAdapterFactoryImpl.getAdapter(object, PersistentEObject.class);
		return map.containsKey(Fun.t2(kyanosEObject.id().toString(), feature.getName()));
	}


	@Override
	public void add(InternalEObject object, EStructuralFeature feature, int index, Object value) {
		PersistentEObject kyanosEObject = NeoEObjectAdapterFactoryImpl.getAdapter(object, PersistentEObject.class);
		if (feature instanceof EAttribute) {
			add(kyanosEObject, (EAttribute) feature, index, value);
		} else if (feature instanceof EReference) {
			PersistentEObject referencedEObject = NeoEObjectAdapterFactoryImpl.getAdapter(value, PersistentEObject.class);
			add(kyanosEObject, (EReference) feature, index, referencedEObject);
		} else {
			throw new IllegalArgumentException(feature.toString());
		}
	}

	protected void add(PersistentEObject object, EAttribute eAttribute, int index, Object value) {
		Object[] array = (Object[]) getFromMap(object, eAttribute);
		if (array == null) {
			array = new Object[] {};
		}
		array = ArrayUtils.add(array, index, serializeToMapValue(eAttribute, value));
		map.put(Fun.t2(object.id(), eAttribute.getName()), array);
	}

	protected void add(PersistentEObject object, EReference eReference, int index, PersistentEObject referencedObject) {
		updateContainment(object, eReference, referencedObject);
		updateInstanceOf(referencedObject);
		Object[] array = (Object[]) getFromMap(object, eReference);
		if (array == null) {
			array = new Object[] {};
		}
		array = ArrayUtils.add(array, index, referencedObject.id());
		map.put(Fun.t2(object.id(), eReference.getName()), array);
	}

	@Override
	public Object remove(InternalEObject object, EStructuralFeature feature, int index) {
		PersistentEObject kyanosEObject = NeoEObjectAdapterFactoryImpl.getAdapter(object, PersistentEObject.class);
		if (feature instanceof EAttribute) {
			return remove(kyanosEObject, (EAttribute) feature, index);
		} else if (feature instanceof EReference) {
			return remove(kyanosEObject, (EReference) feature, index);
		} else {
			throw new IllegalArgumentException(feature.toString());
		}
	}

	protected Object remove(PersistentEObject object, EAttribute eAttribute, int index) {
		Object[] array = (Object[]) getFromMap(object, eAttribute);
		Object oldValue = array[index];
		array = ArrayUtils.remove(array, index);
		map.put(Fun.t2(object.id(), eAttribute.getName()), array);
		return parseMapValue(eAttribute, oldValue);
	}

	protected Object remove(PersistentEObject object, EReference eReference, int index) {
		Object[] array = (Object[]) getFromMap(object, eReference);
		Object oldId = array[index];
		array = ArrayUtils.remove(array, index);
		map.put(Fun.t2(object.id(), eReference.getName()), array);
		return eObject((Id)oldId);

	}

	@Override
	public Object move(InternalEObject object, EStructuralFeature feature, int targetIndex, int sourceIndex) {
		Object movedElement = remove(object, feature, sourceIndex);
		add(object, feature, targetIndex, movedElement);
		return movedElement;
	}


	@Override
	public void unset(InternalEObject object, EStructuralFeature feature) {
		PersistentEObject kyanosEObject = NeoEObjectAdapterFactoryImpl.getAdapter(object, PersistentEObject.class);
		map.remove(Fun.t2(kyanosEObject.id().toString(), feature.getName()));
	}


	@Override
	public boolean isEmpty(InternalEObject object, EStructuralFeature feature) {
		return size(object, feature) == 0; 
	}


	@Override
	public int size(InternalEObject object, EStructuralFeature feature) {
		PersistentEObject kyanosEObject = NeoEObjectAdapterFactoryImpl.getAdapter(object, PersistentEObject.class);
		Object[] array = (Object[]) getFromMap(kyanosEObject, feature);
		return array != null ? array.length : 0; 
	}


	@Override
	public boolean contains(InternalEObject object, EStructuralFeature feature, Object value) {
		return indexOf(object, feature, value) != -1;
	}


	@Override
	public int indexOf(InternalEObject object, EStructuralFeature feature, Object value) {
		PersistentEObject kyanosEObject = NeoEObjectAdapterFactoryImpl.getAdapter(object, PersistentEObject.class);
		Object[] array = (Object[]) getFromMap(kyanosEObject, feature);
		if (array == null) {
			return -1;
		}
		if (feature instanceof EAttribute) {
			return ArrayUtils.indexOf(array, serializeToMapValue((EAttribute) feature, value));
		} else {
			PersistentEObject childEObject = NeoEObjectAdapterFactoryImpl.getAdapter(value, PersistentEObject.class);
			return ArrayUtils.indexOf(array, childEObject.id().toString());
		}
	}


	@Override
	public int lastIndexOf(InternalEObject object, EStructuralFeature feature, Object value) {
		PersistentEObject kyanosEObject = NeoEObjectAdapterFactoryImpl.getAdapter(object, PersistentEObject.class);
		Object[] array = (Object[]) getFromMap(kyanosEObject, feature);
		if (array == null) {
			return -1;
		}
		if (feature instanceof EAttribute) {
			return ArrayUtils.lastIndexOf(array, serializeToMapValue((EAttribute) feature, value));
		} else {
			PersistentEObject childEObject = NeoEObjectAdapterFactoryImpl.getAdapter(value, PersistentEObject.class);
			return ArrayUtils.lastIndexOf(array, childEObject.id().toString());
		}
	}


	@Override
	public void clear(InternalEObject object, EStructuralFeature feature) {
		PersistentEObject kyanosEObject = NeoEObjectAdapterFactoryImpl.getAdapter(object, PersistentEObject.class);
		map.put(Fun.t2(kyanosEObject.id(), feature.getName()), new Object[] {});
	}


	@Override
	public Object[] toArray(InternalEObject object, EStructuralFeature feature) {
		int size = size(object, feature);
		Object[] result = new Object[size];
		for (int index = 0; index < size; index++) {
			result[index] = get(object, feature, index);
		}
		return result;
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(InternalEObject object, EStructuralFeature feature, T[] array) {
		int size = size(object, feature);
		T[] result = null;
		if (array.length < size) {
			result = Arrays.copyOf(array, size);
		} else {
			result = array;
		}
		for (int index = 0; index < size; index++) {
			result[index] = (T) get(object, feature, index);
		}
		return result;
	}


	@Override
	public int hashCode(InternalEObject object, EStructuralFeature feature) {
		return toArray(object, feature).hashCode();
	}


	@Override
	public InternalEObject getContainer(InternalEObject object) {
		PersistentEObject kyanosEObject = NeoEObjectAdapterFactoryImpl.getAdapter(object, PersistentEObject.class);
		ContainerInfo info = containersMap.get(kyanosEObject.id().toString());
		if (info != null) {
			return (InternalEObject) eObject(info.containerId);
		}
		return null;
	}


	@Override
	public EStructuralFeature getContainingFeature(InternalEObject object) {
		PersistentEObject kyanosEObject = NeoEObjectAdapterFactoryImpl.getAdapter(object, PersistentEObject.class);
		ContainerInfo info = containersMap.get(kyanosEObject.id());
		if (info != null) {
			EObject container = eObject(info.containerId);
			container.eClass().getEStructuralFeature(info.containingFeatureName);
		}
		return null;
	}


	@Override
	public EObject create(EClass eClass) {
		// This should not be called
		throw new UnsupportedOperationException();
	}


	@Override
	public EObject eObject(Id id) {
		if (id == null) {
			return null;
		}
		InternalPersistentEObject kyanosEObject = loadedEObjects.get(id);
		if (kyanosEObject == null) {
			EClass eClass = resolveInstanceOf(id.toString());
			if (eClass != null) {
				EObject eObject = EcoreUtil.create(eClass);
				if (eObject instanceof InternalPersistentEObject) {
					kyanosEObject = (InternalPersistentEObject) eObject;
				} else {
					kyanosEObject = NeoEObjectAdapterFactoryImpl.getAdapter(eObject, InternalPersistentEObject.class);
				}
				kyanosEObject.id(id);
			} else {
				NeoLogger.log(NeoLogger.SEVERITY_ERROR, 
						MessageFormat.format("Element {0} does not have an associated EClass", id));
			}
			loadedEObjects.put(id, kyanosEObject);
		}
		if (kyanosEObject.resource() != resource()) {
			kyanosEObject.resource(resource());
		}
		return kyanosEObject;
	}
	

	protected EClass resolveInstanceOf(String id) {
		EClassInfo eClassInfo = instanceOfMap.get(id);
		if (eClassInfo != null) {
			EClass eClass = (EClass) Registry.INSTANCE.getEPackage(eClassInfo.nsURI).getEClassifier(eClassInfo.className);
			return eClass;
		}
		return null;
	}
	
	protected void updateContainment(PersistentEObject object, EReference eReference, PersistentEObject referencedObject) {
		if (eReference.isContainment()) {
			ContainerInfo info = containersMap.get(referencedObject.id().toString());
			if (info == null || !(info.containerId.equals(object.id()))) {
				containersMap.put(referencedObject.id(), new ContainerInfo(object.id(), eReference.getName()));
			}
		}
	}
	
	protected void updateInstanceOf(PersistentEObject object) {
		EClassInfo info = instanceOfMap.get(object.id().toString());
		if (info == null) {
			instanceOfMap.put(object.id(), new EClassInfo(object.eClass().getEPackage().getNsURI(), object.eClass().getName()));
		}
	}


	protected static Object parseMapValue(EAttribute eAttribute, Object property) {
		return property != null ? EcoreUtil.createFromString(eAttribute.getEAttributeType(), property.toString()) : null;
	}

	protected static Object serializeToMapValue(EAttribute eAttribute, Object value) {
		return value != null ? EcoreUtil.convertToString(eAttribute.getEAttributeType(), value) : null;
	}
	
	protected Object getFromMap(PersistentEObject object, EStructuralFeature feature) {
		return map.get(Fun.t2(object.id(), feature.getName()));
	}
	
}