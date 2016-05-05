/*******************************************************************************
 * Copyright (c) 2013 Atlanmod INRIA LINA Mines Nantes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * Atlanmod INRIA LINA Mines Nantes - initial API and implementation
 *******************************************************************************/

/**
 */
package fr.inria.atlanmod.neoemf.test.commons.models.mapSample.util;

import fr.inria.atlanmod.neoemf.test.commons.models.mapSample.*;

import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see fr.inria.atlanmod.neoemf.test.commons.models.mapSample.MapSamplePackage
 * @generated
 */
public class MapSampleAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static MapSamplePackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MapSampleAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = MapSamplePackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MapSampleSwitch<Adapter> modelSwitch =
        new MapSampleSwitch<Adapter>() {
            @Override
            public Adapter caseSampleModel(SampleModel object) {
                return createSampleModelAdapter();
            }
            @Override
            public Adapter caseStringToStringMap(Map.Entry<String, String> object) {
                return createStringToStringMapAdapter();
            }
            @Override
            public Adapter caseK(K object) {
                return createKAdapter();
            }
            @Override
            public Adapter caseV(V object) {
                return createVAdapter();
            }
            @Override
            public Adapter caseKToVMap(Map.Entry<K, V> object) {
                return createKToVMapAdapter();
            }
            @Override
            public Adapter caseSampleModelContentObject(SampleModelContentObject object) {
                return createSampleModelContentObjectAdapter();
            }
            @Override
            public Adapter casePack(Pack object) {
                return createPackAdapter();
            }
            @Override
            public Adapter caseAbstractPackContent(AbstractPackContent object) {
                return createAbstractPackContentAdapter();
            }
            @Override
            public Adapter casePackContent(PackContent object) {
                return createPackContentAdapter();
            }
            @Override
            public Adapter caseSpecializedPackContent(SpecializedPackContent object) {
                return createSpecializedPackContentAdapter();
            }
            @Override
            public Adapter casePackContent2(PackContent2 object) {
                return createPackContent2Adapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link fr.inria.atlanmod.neoemf.test.commons.models.mapSample.SampleModel <em>Sample Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see fr.inria.atlanmod.neoemf.test.commons.models.mapSample.SampleModel
     * @generated
     */
    public Adapter createSampleModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>String To String Map</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see java.util.Map.Entry
     * @generated
     */
    public Adapter createStringToStringMapAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link fr.inria.atlanmod.neoemf.test.commons.models.mapSample.K <em>K</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see fr.inria.atlanmod.neoemf.test.commons.models.mapSample.K
     * @generated
     */
    public Adapter createKAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link fr.inria.atlanmod.neoemf.test.commons.models.mapSample.V <em>V</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see fr.inria.atlanmod.neoemf.test.commons.models.mapSample.V
     * @generated
     */
    public Adapter createVAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>KTo VMap</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see java.util.Map.Entry
     * @generated
     */
    public Adapter createKToVMapAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link fr.inria.atlanmod.neoemf.test.commons.models.mapSample.SampleModelContentObject <em>Sample Model Content Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see fr.inria.atlanmod.neoemf.test.commons.models.mapSample.SampleModelContentObject
     * @generated
     */
    public Adapter createSampleModelContentObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link fr.inria.atlanmod.neoemf.test.commons.models.mapSample.Pack <em>Pack</em>}'.
     * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
     * @return the new adapter.
     * @see fr.inria.atlanmod.neoemf.test.commons.models.mapSample.Pack
     * @generated
     */
	public Adapter createPackAdapter() {
        return null;
    }

				/**
     * Creates a new adapter for an object of class '{@link fr.inria.atlanmod.neoemf.test.commons.models.mapSample.AbstractPackContent <em>Abstract Pack Content</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see fr.inria.atlanmod.neoemf.test.commons.models.mapSample.AbstractPackContent
     * @generated
     */
    public Adapter createAbstractPackContentAdapter() {
        return null;
    }

                /**
     * Creates a new adapter for an object of class '{@link fr.inria.atlanmod.neoemf.test.commons.models.mapSample.PackContent <em>Pack Content</em>}'.
     * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
     * @return the new adapter.
     * @see fr.inria.atlanmod.neoemf.test.commons.models.mapSample.PackContent
     * @generated
     */
	public Adapter createPackContentAdapter() {
        return null;
    }

				/**
     * Creates a new adapter for an object of class '{@link fr.inria.atlanmod.neoemf.test.commons.models.mapSample.SpecializedPackContent <em>Specialized Pack Content</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see fr.inria.atlanmod.neoemf.test.commons.models.mapSample.SpecializedPackContent
     * @generated
     */
    public Adapter createSpecializedPackContentAdapter() {
        return null;
    }

                /**
     * Creates a new adapter for an object of class '{@link fr.inria.atlanmod.neoemf.test.commons.models.mapSample.PackContent2 <em>Pack Content2</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see fr.inria.atlanmod.neoemf.test.commons.models.mapSample.PackContent2
     * @generated
     */
    public Adapter createPackContent2Adapter() {
        return null;
    }

                /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //MapSampleAdapterFactory
