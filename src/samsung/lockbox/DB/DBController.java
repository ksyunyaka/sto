package samsung.lockbox.DB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import samsung.lockbox.model.Category;
import samsung.lockbox.model.Details;
import samsung.lockbox.model.EncryptedValue;
import samsung.lockbox.model.Entity;
import samsung.lockbox.model.Value;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.samsung.seclab.tzos.securestorage.SSController;
import com.samsung.seclab.tzos.securestorage.SSControllerSingleton;

public class DBController {

	private SQLiteDatabase database;

	private DBAdapter adapter;

	private String[] valueColumns = { DBAdapter.VALUE_ID, DBAdapter.VALUE_GUID };
	private String[] categoryColumns = { DBAdapter.CATEGORY_ID,
			DBAdapter.CATEGORY_NAME, DBAdapter.CATEGORY_ICON_PATH };
	private String[] detailsColumns = { DBAdapter.DETAILS_ID,
			DBAdapter.DETAILS_FIELD_NAME, DBAdapter.DETAILS_FIELD_TYPE,
			DBAdapter.DETAILS_VALUE, DBAdapter.DETAILS_ENTITY };
	private String[] entityColumns = { DBAdapter.ENTITY_ID,
			DBAdapter.ENTITY_CATEGORY, DBAdapter.ENTITY_TYPE,
			DBAdapter.ENTITY_FAVORITE };
	private String[] settingsColumns = { DBAdapter.SETTINGS_ID,
			DBAdapter.SETTINGS_NAME, DBAdapter.SETTINGS_VALUE };

	public DBController(Context context) {
		adapter = new DBAdapter(context);
	}

	public void open() {
		Log.e("Controller", "open");
		database = adapter.getWritableDatabase();
	}

	public void close() {
		Log.e("Controller", "close");
		adapter.close();
	}




	/**
	 * Method inserts string guid into the table Value.
	 * 
	 * @param guid
	 *            guid, stored into the table "value"
	 * 
	 * @return Value with fields(id, guid).
	 * @exception catch
	 *                exception if transaction is not successful
	 */
	public Value createValue(EncryptedValue encryptedValue) {
		database.beginTransaction();
		Cursor cursor = null;
		Value value = null;
		try {
			ContentValues values = new ContentValues();
			values.put(DBAdapter.VALUE_GUID, encryptedValue.getGuid());
			long valueId = database.insert(DBAdapter.VALUE_TABLE, null, values);

			value = new Value();
			value.setGuid(encryptedValue.getGuid());
			value.setId(valueId);
			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction", e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			};

			database.endTransaction();
		}
		return value;
	}

	public Value createValue() {
		database.beginTransaction();
		Value value = new Value();
		try {
			ContentValues values = new ContentValues();
			values.put(DBAdapter.VALUE_GUID, "");
			long valueId = database.insert(DBAdapter.VALUE_TABLE, null, values);
			value.setGuid("");
			value.setId(valueId);
			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction", e.getMessage());
		} finally {

			database.endTransaction();
		}
		return value;
	}

	private Value cursorToValue(Cursor cursor) {
		Value value = new Value();
		value.setId(cursor.getLong(0));
		String guid = cursor.getString(1);
		if (guid.length() > 0){
			value.setGuid(new String(SSControllerSingleton.getInstance(null)
					.readData(SSController.stringToArray(guid), null)));
		}
		else{
			value.setGuid("");
		}
		return value;
	}

	/**
	 * Method builds details, which includes name, typeID, valueID and entityID
	 * and inserts it to database.
	 * 
	 * @param type
	 *            field type.
	 * @param value
	 *            value
	 * @param name
	 *            field name
	 * @param entity
	 *            item or template
	 * @return Details with fields: type, value, name, entity.
	 * @exception catch
	 *                exception if transaction is not successful
	 */
	public Details createDetails(String type, Value value, String name,
			Entity entity) {
		Value newValue = createValue();
		if (value != null){ 
			newValue.setGuid(value.getGuid());
		}
		update(newValue);
		database.beginTransaction();
		Cursor cursor = null;
		Details details = null;
		try {
			ContentValues values = new ContentValues();
			values.put(DBAdapter.DETAILS_FIELD_NAME, name);
			values.put(DBAdapter.DETAILS_FIELD_TYPE, type);

			values.put(DBAdapter.DETAILS_VALUE, newValue.getId());

			values.put(DBAdapter.DETAILS_ENTITY, entity.getId());
			long detailsId = database.insert(DBAdapter.DETAILS_TABLE, null,
					values);

			cursor = database.query(DBAdapter.DETAILS_TABLE, detailsColumns,
					DBAdapter.DETAILS_ID + " = " + detailsId, null, null, null,
					null);

			cursor.moveToFirst();
			details = cursorToDetails(cursor, type, newValue, entity);
			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction" + e.getMessage(), e.getMessage());
			delete(value);
		} finally {
			database.endTransaction();
			if (cursor != null){
				cursor.close();
			}
		}
		return details;
	}

	private Details cursorToDetails(Cursor cursor, String type, Value value,
			Entity entity) {
		Details details = new Details();
		details.setId(cursor.getLong(0));
		details.setName(cursor.getString(1));
		details.setType(type);
		details.setValue(value);
		details.setEntity(entity);
		return details;
	}

	/**
	 * Method inserts category into table category
	 * 
	 * @param name
	 *            name of category
	 * @param iconPath
	 *            path to icon
	 * 
	 * @return Category with fields id, name, iconPath.
	 * @exception catch
	 *                exception if transaction is not successful
	 */
	public Category createCategory(String name, String iconPath) {
		database.beginTransaction();
		Cursor cursor = null;
		Category category = null;
		try {
			ContentValues values = new ContentValues();
			values.put(DBAdapter.CATEGORY_NAME, name);
			values.put(DBAdapter.CATEGORY_ICON_PATH, iconPath);
			long categoryId = database.insert(DBAdapter.CATEGORY_TABLE, null,
					values);
			cursor = database.query(DBAdapter.CATEGORY_TABLE, categoryColumns,
					DBAdapter.CATEGORY_ID + " = " + categoryId, null, null,
					null, null);
			cursor.moveToFirst();
			category = cursorToCategory(cursor);
			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction", e.getMessage());
		} finally {
			database.endTransaction();
			cursor.close();
		}
		return category;
	}

	private Category cursorToCategory(Cursor cursor) {
		Category category = new Category();
		category.setId(cursor.getLong(0));
		category.setName(cursor.getString(1));
		category.setIconPath(cursor.getString(2));
		return category;
	}

	/**
	 * Inserts entity with specified categoryID into the table.
	 * 
	 * @param category
	 *            specified category like "Business"
	 * @param type
	 *            type of entity "Template" or "Item"
	 * 
	 * @return entity with specified category and type.
	 * @exception catch
	 *                exception if transaction is not successful
	 */
	public Entity createEntity(Category category, String type) {
		database.beginTransaction();
		Cursor cursor = null;
		Entity entity = null;
		try {
			ContentValues values = new ContentValues();
			if (category != null) {
				values.put(DBAdapter.ENTITY_CATEGORY, category.getId());
			}

			values.put(DBAdapter.ENTITY_TYPE, type);
			long entityId = database.insert(DBAdapter.ENTITY_TABLE, null,
					values);

			cursor = database.query(DBAdapter.ENTITY_TABLE, entityColumns,
					DBAdapter.ENTITY_ID + " = " + entityId, null, null, null,
					null);
			cursor.moveToFirst();

			entity = cursorToEntity(category, cursor);
			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction", e.getMessage());
		} finally {
			database.endTransaction();
			cursor.close();
		}
		return entity;
	}

	private Entity cursorToEntity(Category category, Cursor cursor) {
		Entity entity = new Entity();
		entity.setCategory(category);
		entity.setId(cursor.getLong(0));
		entity.setType(cursor.getString(2));
		entity.setFavorite((cursor.getInt(3) == 1) ? true : false);
		return entity;
	}

	/**
	 * Updates all changes, associated with value.
	 * 
	 * @param value
	 *            value with specified guid.
	 * @return true, if transaction is successful.
	 * @exception catch
	 *                exception if transaction is not successful
	 */
	public boolean update(Value value) {
		database.beginTransaction();
		boolean a = false;
		Value editedValue = getValue(value.getId());
		if (editedValue != null && editedValue.getGuid().length() > 0){
			SSControllerSingleton.getInstance(null).deletaData(
					SSController.stringToArray(editedValue.getGuid()));
		}
		if (value.getGuid() != null && value.getGuid().length() > 0) {
			byte[] newGuid = SSControllerSingleton.getInstance(null).storeData(
					0, "", value.getGuid().getBytes(), null);
			value.setGuid(SSController.bytesToString(newGuid));
		} else{
			value.setGuid("");
		}
		try {
			ContentValues values = new ContentValues();
			values.put(DBAdapter.VALUE_GUID, value.getGuid());
			a = database.update(DBAdapter.VALUE_TABLE, values,
					DBAdapter.VALUE_ID + " = " + value.getId(), null) > 0;
			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction", e.getMessage());
		} finally {
			database.endTransaction();
		}
		return a;
	}

	/**
	 * Updates category name or icon path, specified in param.
	 * 
	 * @param category
	 *            category with new icon path and name.
	 * @return category true if transaction was successful.
	 * @exception catch
	 *                exception if transaction is not successful.
	 */
	public boolean update(Category category) {
		database.beginTransaction();
		boolean a = false;
		try {
			ContentValues values = new ContentValues();
			values.put(DBAdapter.CATEGORY_NAME, category.getName());
			values.put(DBAdapter.CATEGORY_ICON_PATH, category.getIconPath());
			a = database.update(DBAdapter.CATEGORY_TABLE, values,
					DBAdapter.CATEGORY_ID + " = " + category.getId(), null) > 0;
			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction", e.getMessage());
		} finally {
			database.endTransaction();
		}
		return a;
	}

	/**
	 * 
	 * @param details
	 * @return
	 */
	public boolean update(Details details) {
		update(details.getValue());
		database.beginTransaction();
		boolean a = false;
		try {

			ContentValues values = new ContentValues();
			values.put(DBAdapter.DETAILS_FIELD_NAME, details.getName());
			values.put(DBAdapter.DETAILS_VALUE, details.getValue().getId());
			values.put(DBAdapter.DETAILS_FIELD_TYPE, details.getType());
			a = database.update(DBAdapter.DETAILS_TABLE, values,
					DBAdapter.DETAILS_ID + " = " + details.getId(), null) > 0;
			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction", e.getMessage());
		} finally {
			database.endTransaction();
		}
		return a;
	}

	public boolean update(Entity entity) {
		database.beginTransaction();
		boolean a = false;
		try {
			ContentValues values = new ContentValues();
			int favorite = 0;
			if (entity.isFavorite()){
				favorite = 1;
			}
			values.put(DBAdapter.ENTITY_TYPE, entity.getType());
			values.put(DBAdapter.ENTITY_FAVORITE, favorite);
			if (entity.getCategory() != null){
				values.put(DBAdapter.ENTITY_CATEGORY, entity.getCategory()
						.getId());
			}
			a = database.update(DBAdapter.ENTITY_TABLE, values,
					DBAdapter.ENTITY_ID + " = " + entity.getId(), null) > 0;

			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction", e.getMessage());
		} finally {
			database.endTransaction();
		}
		return a;
	}

	public boolean delete(Value value) {
		database.beginTransaction();
		boolean a = false;
		if (value.getGuid() != null && value.getGuid().length() > 0) {
			SSControllerSingleton.getInstance(null).deletaData(
					SSController.stringToArray(value.getGuid()));
		}
		try {
			a = database.delete(DBAdapter.VALUE_TABLE, DBAdapter.VALUE_ID
					+ " = " + value.getId(), null) > 0;
			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction", e.getMessage());
		} finally {
			database.endTransaction();
		}
		return a;
	}

	public boolean delete(Category category) {
		database.beginTransaction();
		boolean a = false;
		try {
			a = database.delete(DBAdapter.CATEGORY_TABLE, DBAdapter.CATEGORY_ID
					+ " = " + category.getId(), null) > 0;
			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction", e.getMessage());
		} finally {
			database.endTransaction();
		}
		return a;
	}

	public boolean delete(Details details) {
		database.beginTransaction();
		boolean a = false;
		try {

			a = database.delete(DBAdapter.DETAILS_TABLE, DBAdapter.DETAILS_ID
					+ " = " + details.getId(), null) > 0;

			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction during removing" + e.getMessage(),
					e.getMessage());
		} finally {
			database.endTransaction();
		}
		return a;
	}

	public boolean delete(Entity entity) {
		database.beginTransaction();
		boolean a = false;
		try {
			a = database.delete(DBAdapter.ENTITY_TABLE, DBAdapter.ENTITY_ID
					+ " = " + entity.getId(), null) > 0;
			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction", e.getMessage());
		} finally {
			database.endTransaction();
		}
		return a;
	}

	public Category getCategory(long id) {
		Category category = null;
		Cursor cursor = database.query(DBAdapter.CATEGORY_TABLE,
				categoryColumns, DBAdapter.CATEGORY_ID + " = " + id, null,
				null, null, null);
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			category = cursorToCategory(cursor);
		}
		return category;
	}

	public Category getCategory(String name) {
		Category category = null;
		Cursor cursor = database.query(DBAdapter.CATEGORY_TABLE,
				categoryColumns, DBAdapter.CATEGORY_NAME + " ='" + name + "'",
				null, null, null, null);
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			category = cursorToCategory(cursor);
		}
		return category;
	}

	public Value getValue(long id) {
		Value value = null;
		Cursor cursor = database.query(DBAdapter.VALUE_TABLE, valueColumns,
				DBAdapter.VALUE_ID + " = " + id, null, null, null, null);
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			value = cursorToValue(cursor);
			value.setId(id);
		} else {
			ContentValues contentValues = new ContentValues();
			contentValues.put(valueColumns[1],
					"in get value...why detail alive??");
			long idValues = database.insert(DBAdapter.VALUE_TABLE, null,
					contentValues);
			contentValues.clear();
			contentValues.put(DBAdapter.DETAILS_VALUE, id);
			database.update(DBAdapter.DETAILS_TABLE, contentValues, null, null);
			value = getValue(idValues);
			value.setId(id);
		}
		return value;
	}

	public Entity getEntity(long id) {
		Entity entity = null;
		Cursor cursor = database.query(DBAdapter.ENTITY_TABLE, entityColumns,
				DBAdapter.ENTITY_ID + " = " + id, null, null, null,
				DBAdapter.ENTITY_FAVORITE + " ASC");
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			entity = cursorToEntity(getCategory(cursor.getLong(1)), cursor);
		}
		return entity;
	}

	public List<Category> getCategories() {
		List<Category> categories = new ArrayList<Category>();
		Cursor cursor = database.query(DBAdapter.CATEGORY_TABLE,
				categoryColumns, null, null, null, null,
				DBAdapter.CATEGORY_NAME + " ASC");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Category category = cursorToCategory(cursor);
			categories.add(category);
			cursor.moveToNext();
		}
		cursor.close();
		return categories;
	}

	public List<Entity> getEntitiesByCategory(Category category) {
		List<Entity> entities = new ArrayList<Entity>();
		Cursor cursor = null;
		if (category != null) {
			cursor = database.query(DBAdapter.ENTITY_TABLE, entityColumns,
					DBAdapter.ENTITY_CATEGORY + " = " + category.getId(), null,
					null, null, DBAdapter.ENTITY_FAVORITE + " DESC, "
							+ DBAdapter.ENTITY_TYPE + " ASC");
		} else {
			cursor = database.query(DBAdapter.ENTITY_TABLE, entityColumns,
					DBAdapter.ENTITY_CATEGORY + " IS NULL ", null, null, null,
					DBAdapter.ENTITY_TYPE + " ASC");
		}
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Entity entity = cursorToEntity(category, cursor);
			entities.add(entity);
			cursor.moveToNext();
		}
		cursor.close();
		return entities;
	}

	public List<Entity> getItems() {
		List<Entity> entities = new ArrayList<Entity>();
		Cursor cursor = null;
		cursor = database.query(DBAdapter.ENTITY_TABLE, entityColumns,
				DBAdapter.ENTITY_CATEGORY + " NOT NULL ", null, null, null,
				DBAdapter.ENTITY_TYPE + " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Category category = getCategory(cursor.getLong(1));
			Entity entity = cursorToEntity(category, cursor);
			entities.add(entity);
			cursor.moveToNext();
		}
		cursor.close();
		return entities;
	}

	public List<Details> getDetailsByEntity(Entity entity) {
		List<Details> details = new ArrayList<Details>();
		Cursor cursor = database.query(DBAdapter.DETAILS_TABLE, detailsColumns,
				DBAdapter.DETAILS_ENTITY + " = " + entity.getId(), null,
				DBAdapter.DETAILS_ID, null, DBAdapter.DETAILS_ID + " ASC");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String type = cursor.getString(2);
			Value value = getValue(cursor.getLong(3));

			Details detail = cursorToDetails(cursor, type, value, entity);
			details.add(detail);
			cursor.moveToNext();
		}
		cursor.close();
		return details;
	}
	
	/**
	 * Get detail for id
	 * @author a.hontar
	 * @param id
	 * @return detail
	 */
	public Details getDetail(long id) {
		Details detail = null;
		Cursor cursor = database.query(DBAdapter.DETAILS_TABLE, detailsColumns,
				DBAdapter.DETAILS_ID + " = " + id, null,
				DBAdapter.DETAILS_ID, null, DBAdapter.DETAILS_ID + " ASC");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String type = cursor.getString(2);
			Value value = getValue(cursor.getLong(3));

			detail = cursorToDetails(cursor, type, value, null);
			cursor.moveToNext();
		}
		cursor.close();
		return detail;
	}

	/**
	 * This method get settings from db.
	 * 
	 * @author a.hontar
	 * 
	 * @return settings in Map<name,value>;
	 */
	public Map<String, String> getSettings() {
		Map<String, String> settings = new HashMap<String, String>();

		Cursor cursor = database.query(DBAdapter.SETTINGS_TABLE,
				settingsColumns, null, null, DBAdapter.SETTINGS_ID, null,
				DBAdapter.SETTINGS_NAME);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			settings.put(cursor.getString(1), cursor.getString(2));
			cursor.moveToNext();
		}

		return settings;
	}

	/**
	 * Create setting with name and value in db
	 * 
	 * @author a.hontar
	 * 
	 * @param name
	 *            Setting name (for example "Autolock")
	 * @param value
	 *            Current value of this setting
	 */
	public void createSetting(String name, String value) {

		try {
			ContentValues values = new ContentValues();

			values.put(DBAdapter.SETTINGS_NAME, name);

			values.put(DBAdapter.SETTINGS_VALUE, value);
			database.insert(DBAdapter.SETTINGS_TABLE, null,
					values);

		} catch (Exception e) {
			Log.e("Error in transaction", e.getMessage());
		}
		// finally {
		//
		// }
	}

	/**
	 * Update lockbox settings in db
	 * 
	 * @author a.hontar
	 * 
	 * @param name
	 *            Setting name in db.
	 * @param value
	 *            New value
	 * @return Is update successful
	 */
	public boolean updateSetting(String name, String value) {
		database.beginTransaction();
		boolean a = false;
		try {

			ContentValues values = new ContentValues();
			values.put(DBAdapter.SETTINGS_VALUE, value);
			a = database.update(DBAdapter.SETTINGS_TABLE, values,
					DBAdapter.SETTINGS_NAME + " = '" + name + "'", null) > 0;
			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction", e.getMessage());
		} finally {
			database.endTransaction();
		}
		return a;
	}

	/**
	 * Delete setting with name from db
	 * 
	 * @author a.hontar
	 * 
	 * @param name
	 *            Name of current setting in db
	 * @return Is deleting successful
	 */
	public boolean deleteSetting(String name) {
		boolean a = false;
		database.beginTransaction();
		try {
			a = database.delete(DBAdapter.SETTINGS_TABLE,
					DBAdapter.SETTINGS_NAME + " = " + name, null) > 0;
			database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Error in transaction", e.getMessage());
		} finally {
			database.endTransaction();
		}
		return a;
	}

}
