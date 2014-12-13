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

@Api(name = "storeeventendpoint", namespace = @ApiNamespace(ownerDomain = "example.com", ownerName = "example.com", packagePath = "remindme"))
public class StoreEventEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listStoreEvent")
	public CollectionResponse<StoreEvent> listStoreEvent(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<StoreEvent> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr
					.createQuery("select from StoreEvent as StoreEvent");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<StoreEvent>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (StoreEvent obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<StoreEvent> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getStoreEvent")
	public StoreEvent getStoreEvent(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		StoreEvent storeevent = null;
		try {
			storeevent = mgr.find(StoreEvent.class, id);
		} finally {
			mgr.close();
		}
		return storeevent;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param storeevent the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertStoreEvent")
	public StoreEvent insertStoreEvent(StoreEvent storeevent) {
		EntityManager mgr = getEntityManager();
		try {
			mgr.persist(storeevent);
		} finally {
			mgr.close();
		}
		return storeevent;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param storeevent the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateStoreEvent")
	public StoreEvent updateStoreEvent(StoreEvent storeevent) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsStoreEvent(storeevent)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(storeevent);
		} finally {
			mgr.close();
		}
		return storeevent;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeStoreEvent")
	public void removeStoreEvent(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			StoreEvent storeevent = mgr.find(StoreEvent.class, id);
			mgr.remove(storeevent);
		} finally {
			mgr.close();
		}
	}

	private boolean containsStoreEvent(StoreEvent storeevent) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			StoreEvent item = mgr.find(StoreEvent.class, storeevent.getKey());
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
