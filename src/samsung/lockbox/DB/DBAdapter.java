package samsung.lockbox.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "lockBoxDB";
	private static final int DATABASE_VERSION = 2;

	public static final String VALUE_TABLE = "value";
	public static final String VALUE_ID = "id";
	public static final String VALUE_GUID = "guid";

	public static final String DETAILS_TABLE = "details";
	public static final String DETAILS_ID = "id";
	public static final String DETAILS_FIELD_TYPE = "field_type";
	public static final String DETAILS_FIELD_NAME = "field_name";
	public static final String DETAILS_VALUE = "value";
	public static final String DETAILS_ENTITY = "entity";

	public static final String CATEGORY_TABLE = "category";
	public static final String CATEGORY_ID = "id";
	public static final String CATEGORY_NAME = "category_name";
	public static final String CATEGORY_ICON_PATH = "icon_path";

	public static final String ENTITY_TABLE = "entity";
	public static final String ENTITY_ID = "id";
	public static final String ENTITY_CATEGORY = "category";
	public static final String ENTITY_TYPE = "type";
	public static final String ENTITY_FAVORITE = "favorite";

	public static final String FILE_TABLE = "file";
	public static final String FILE_ID = "id";
	public static final String FILE_PATH = "path";

	public static final String SETTINGS_TABLE = "settings";
	public static final String SETTINGS_ID = "id";
	public static final String SETTINGS_NAME = "name";
	public static final String SETTINGS_VALUE = "value";

	// public static final String ENCRYPTED_VALUES_TABLE = "encrypted_values";
	// public static final String ENCRYPTED_VALUES_ID = "id";
	// public static final String ENCRYPTED_VALUES_GUID = "guid";
	// public static final String ENCRYPTED_VALUES_VALUE = "value";
	// public static final String ENCRYPTED_VALUES_IV = "iv";
	// public static final String ENCRYPTED_VALUES_AUTH_TAG = "auth_tag";

	// private String CREATE_ENCRYPTED_VALUE = "CREATE TABLE "
	// + ENCRYPTED_VALUES_TABLE + "(" + ENCRYPTED_VALUES_ID
	// + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ENCRYPTED_VALUES_GUID
	// + " TEXT NOT NULL, " + ENCRYPTED_VALUES_VALUE + " TEXT ,"
	// + ENCRYPTED_VALUES_IV + " TEXT NOT NULL, "
	// + ENCRYPTED_VALUES_AUTH_TAG + " TEXT NOT NULL);";
	/**
	 * 
	 */

	/*
	 * private String CREATE_VALUE = "CREATE TABLE " + VALUE_TABLE + "(" +
	 * VALUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + VALUE_GUID +
	 * " TEXT NOT NULL);";// with secure storage.
	 */

	private String CREATE_VALUE = "CREATE TABLE " + VALUE_TABLE + "("
			+ VALUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + VALUE_GUID
			+ " TEXT NOT NULL);";

	private String CREATE_DETAILS = "CREATE TABLE " + DETAILS_TABLE + "("
			+ DETAILS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DETAILS_FIELD_NAME + " TEXT NOT NULL, " + DETAILS_FIELD_TYPE
			+ " TEXT NOT NULL , " + DETAILS_VALUE + " INTEGER, "
			+ DETAILS_ENTITY + " INTEGER NOT NULL , " + "FOREIGN KEY("
			+ DETAILS_VALUE + ") REFERENCES " + VALUE_TABLE + "(" + VALUE_ID
			+ "), " + "FOREIGN KEY(" + DETAILS_ENTITY + ") REFERENCES "
			+ ENTITY_TABLE + "(" + ENTITY_ID + "));";

	private String CREATE_CATEGORY = "CREATE TABLE " + CATEGORY_TABLE + " ("
			+ CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ CATEGORY_NAME + " TEXT NOT NULL, " + CATEGORY_ICON_PATH
			+ " TEXT NOT NULL);";

	private String CREATE_ENTITY = "CREATE TABLE " + ENTITY_TABLE + "("
			+ ENTITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ ENTITY_CATEGORY + " INTEGER, " + ENTITY_FAVORITE
			+ " INTEGER DEFAULT 0, " + ENTITY_TYPE + " TEXT NOT NULL, "
			+ "FOREIGN KEY(" + ENTITY_CATEGORY + ") REFERENCES "
			+ CATEGORY_TABLE + "(" + CATEGORY_ID + "));";

	// private String CREATE_FILE = "CREATE TABLE " + FILE_TABLE + "(" + FILE_ID
	// + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FILE_PATH
	// + " TEXT NOT NULL);";

	private String CREATE_SETTINGS = "CREATE TABLE " + SETTINGS_TABLE + "("
			+ SETTINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ SETTINGS_NAME + " TEXT NOT NULL, " + SETTINGS_VALUE
			+ " TEXT NOT NULL);";;

	public DBAdapter(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// db.execSQL(CREATE_ENCRYPTED_VALUE);
		db.execSQL(CREATE_VALUE);
		db.execSQL(CREATE_CATEGORY);
		db.execSQL(CREATE_ENTITY);
		db.execSQL(CREATE_DETAILS);
		// db.execSQL(CREATE_FILE);
		db.execSQL(CREATE_SETTINGS);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBAdapter.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		// db.execSQL("DROP TABLE IF EXISTS " + ENCRYPTED_VALUES_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + VALUE_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ENTITY_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + DETAILS_TABLE);
		// db.execSQL("DROP TABLE IF EXISTS " + FILE_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SETTINGS_TABLE);
		onCreate(db);
	}
	

}
