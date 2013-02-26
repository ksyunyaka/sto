package samsung.lockbox.DB;

import java.util.List;
import java.util.Map;

import samsung.lockbox.DB.DBController;
import samsung.lockbox.model.Category;
import samsung.lockbox.model.Details;
import samsung.lockbox.model.Entity;
import samsung.lockbox.model.Value;
import android.content.Context;

/**
 * Class required for database management
 * 
 * @authors Taras Dregalo, Andrey Hontar, Andrey Androsov
 * 
 * 
 */
public class Control {

	private DBController dbController;

	public Control(Context context) {
		dbController = new DBController(context);
	}

	public void open() {
		dbController.open();
	}

	public void close() {
		dbController.close();
	}

	public List<Category> getCategories() {
		List<Category> categories = dbController.getCategories();
		return categories;
	}

	public void removeCategory(Category category) {
		List<Entity> entities = dbController.getEntitiesByCategory(category);
		dbController.delete(category);
		for (Entity entity : entities) {
			removeEntity(entity);
		}
		entities = dbController.getEntitiesByCategory(category);
	}

	public void removeEntity(Entity entity) {
		List<Details> details = dbController.getDetailsByEntity(entity);
		dbController.delete(entity);
		for (Details detail : details) {
			removeDetail(detail);
		}
	}

	public boolean removeDetail(Details details) {
		Value value = details.getValue();
		if (value != null){
			dbController.delete(value);
		}
		return dbController.delete(details);
	}

	public Category createCategory(String category, String iconPath) {
		return dbController.createCategory(category, iconPath);
	}

	public Entity createTemplate() {
		return dbController.createEntity(null, "");
	}

	public Entity createTemplate(String type) {
		return dbController.createEntity(null, type);
	}

	public Entity createTemplate(Category categoryId, String type) {
		return dbController.createEntity(categoryId, type);
	}

	public List<Entity> getTemplates() {
		return dbController.getEntitiesByCategory(null);
	}

	public List<Entity> getItem(Category cat) {
		return dbController.getEntitiesByCategory(cat);
	}

	public void updateCategory(Category category) {
		dbController.update(category);
	}

	public void updateTemplate(Entity template) {
		dbController.update(template);
	}

	public List<Details> getDetailsByEntity(Entity entity) {
		return dbController.getDetailsByEntity(entity);
	}

	public void updateDetails(Details detail) {
		dbController.update(detail);
	}

	public void updateValue(Value value) {
		dbController.update(value);
	}

	public Details createDetails(String type, Value value, String name,
			Entity entity) {
		return dbController.createDetails(type, value, name, entity);
	}

	public Value createValue() {
		return dbController.createValue();
	}

	// public EncryptedValue createEncryptedValue(List<byte[]> list) {
	// return dbController.createEncryptedValue(list);
	// }

	public Entity getEntity(long id) {
		return dbController.getEntity(id);
	}

	public List<Entity> getItemList() {
		return dbController.getItems();
	}

	public Map<String, String> getSettings() {
		return dbController.getSettings();
	}

	public void createSetting(String name, String value) {
		Map<String, String> settings = getSettings();
		for (String settingName : settings.keySet()){
			if (settingName.equals(name)){
				deleteSetting(settingName);
			}
		}
		dbController.createSetting(name, value);
	}

	public Details getDetails(long detailId) {
		return dbController.getDetail(detailId);
	}

	public boolean updateSetting(String name, String value) {
		return dbController.updateSetting(name, value);
	}

	public boolean deleteSetting(String name) {
		return dbController.deleteSetting(name);
	}

	public void deleteSettings() {
		Map<String, String> settings = getSettings();
		for (String name : settings.keySet()) {
			deleteSetting(name);
		}
	}
}
