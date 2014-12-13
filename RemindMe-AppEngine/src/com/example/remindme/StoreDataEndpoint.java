package com.example.remindme;

import com.example.remindme.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "storedataendpoint", namespace = @ApiNamespace(ownerDomain = "example.com", ownerName = "example.com", packagePath = "remindme"))
public class StoreDataEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listStoreData")
	public CollectionResponse<StoreData> listStoreData(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<StoreData> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from StoreData as StoreData");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<StoreData>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (StoreData obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<StoreData> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getStoreData")
	public StoreData getStoreData(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		StoreData storedata = null;
		try {
			storedata = mgr.find(StoreData.class, id);
		} finally {
			mgr.close();
		}
		return storedata;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param storedata the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertStoreData")
	public StoreData insertStoreData(StoreData storedata) {
		EntityManager mgr = getEntityManager();
		try {
			mgr.persist(storedata);
		} finally {
			mgr.close();
		}
		return storedata;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param storedata the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateStoreData")
	public StoreData updateStoreData(StoreData storedata) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsStoreData(storedata)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(storedata);
		} finally {
			mgr.close();
		}
		return storedata;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeStoreData")
	public void removeStoreData(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			StoreData storedata = mgr.find(StoreData.class, id);
			mgr.remove(storedata);
		} finally {
			mgr.close();
		}
	}

	private boolean containsStoreData(StoreData storedata) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			StoreData item = mgr.find(StoreData.class, storedata.getKey());
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
