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

import fr.inria.atlanmod.neoemf.logger.NeoLogger;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.InternalEObject.EStore;
import org.eclipse.emf.ecore.resource.Resource;
import org.mapdb.DB;

public class AutocommitMapResourceEStoreImpl extends DirectWriteMapResourceEStoreImpl {

	/**
	 * Default number of allowed modifications (100000) between commits on the
	 * underlying db
	 */
	protected static final int OPS_BETWEEN_COMMITS_DEFAULT = 100000;

	/**
	 * Number of allowed modifications between commits on the underlying db
	 * for this {@link AutocommitMapResourceEStoreImpl}
	 */
	protected final int opsBetweenCommits;

	/**
	 * Current number of modifications modulo {@link #opsBetweenCommits}
	 */
	protected int opCount = 0;

	/**
	 * Constructor for this {@link DB}-based {@link EStore}. Allows to
	 * specify the number of allowed modification on the underlying db before
	 * calling the {@link DB#commit()} method automatically.
	 * 
	 * @param resource
	 * @param db
	 * @param opsBetweenCommits
	 */
	public AutocommitMapResourceEStoreImpl(Resource.Internal resource, DB db, int opsBetweenCommits) {
		super(resource, db);
		this.opsBetweenCommits = opsBetweenCommits;
	}

	/**
	 * Constructor for this {@link DB}-based {@link EStore}. Allows to
	 * make {@link #OPS_BETWEEN_COMMITS_DEFAULT} modifications on the underlying
	 * db before calling the {@link DB#commit()} method
	 * automatically.
	 * 
	 * @param resource
	 * @param db
	 */
	public AutocommitMapResourceEStoreImpl(Resource.Internal resource, DB db) {
		this(resource, db, OPS_BETWEEN_COMMITS_DEFAULT);
	}

	@Override
	public void add(InternalEObject object, EStructuralFeature feature, int index, Object value) {
		super.add(object, feature, index, value);
		incrementAndCommit();
	}

	@Override
	public Object set(InternalEObject object, EStructuralFeature feature, int index, Object value) {
		Object returnValue = super.set(object, feature, index, value);
		incrementAndCommit();
		return returnValue;
	}

	@Override
	public Object move(InternalEObject object, EStructuralFeature feature, int targetIndex, int sourceIndex) {
		Object returnValue = super.move(object, feature, targetIndex, sourceIndex);
		incrementAndCommit();
		return returnValue;
	}

	@Override
	public Object remove(InternalEObject object, EStructuralFeature feature, int index) {
		Object returnValue = super.remove(object, feature, index);
		incrementAndCommit();
		return returnValue;
	}

	@Override
	public void unset(InternalEObject object, EStructuralFeature feature) {
		super.unset(object, feature);
		incrementAndCommit();
	}

	@Override
	public void clear(InternalEObject object, EStructuralFeature feature) {
		super.clear(object, feature);
		incrementAndCommit();
	}

	private void incrementAndCommit() {
		opCount = (opCount + 1) % opsBetweenCommits;
		if (opCount == 0) {
			db.commit();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		try {
			db.commit();
		} catch (Exception ex) {
			NeoLogger.log(NeoLogger.SEVERITY_ERROR, ex);
		}
		super.finalize();
	}
}
