package com.easyfield.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile LocationDao _locationDao;

  private volatile SaveLocationDao _saveLocationDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `location` (`location_id` INTEGER NOT NULL, `user_id` TEXT, `latitude` REAL, `longitude` REAL, `employee_punch_id` TEXT, `gps_status` INTEGER, `battery` TEXT, `network` TEXT, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`location_id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `location_dummy` (`location_id` INTEGER NOT NULL, `user_id` TEXT, `latitude` REAL, `longitude` REAL, `employee_punch_id` TEXT, `gps_status` INTEGER, `battery` TEXT, `network` TEXT, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`location_id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2b6f07ceb4d813b45324cb045dbe1ef6')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `location`");
        db.execSQL("DROP TABLE IF EXISTS `location_dummy`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsLocation = new HashMap<String, TableInfo.Column>(9);
        _columnsLocation.put("location_id", new TableInfo.Column("location_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocation.put("user_id", new TableInfo.Column("user_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocation.put("latitude", new TableInfo.Column("latitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocation.put("longitude", new TableInfo.Column("longitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocation.put("employee_punch_id", new TableInfo.Column("employee_punch_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocation.put("gps_status", new TableInfo.Column("gps_status", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocation.put("battery", new TableInfo.Column("battery", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocation.put("network", new TableInfo.Column("network", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocation.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocation = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocation = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocation = new TableInfo("location", _columnsLocation, _foreignKeysLocation, _indicesLocation);
        final TableInfo _existingLocation = TableInfo.read(db, "location");
        if (!_infoLocation.equals(_existingLocation)) {
          return new RoomOpenHelper.ValidationResult(false, "location(com.easyfield.location.models.LocationRequest).\n"
                  + " Expected:\n" + _infoLocation + "\n"
                  + " Found:\n" + _existingLocation);
        }
        final HashMap<String, TableInfo.Column> _columnsLocationDummy = new HashMap<String, TableInfo.Column>(9);
        _columnsLocationDummy.put("location_id", new TableInfo.Column("location_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationDummy.put("user_id", new TableInfo.Column("user_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationDummy.put("latitude", new TableInfo.Column("latitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationDummy.put("longitude", new TableInfo.Column("longitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationDummy.put("employee_punch_id", new TableInfo.Column("employee_punch_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationDummy.put("gps_status", new TableInfo.Column("gps_status", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationDummy.put("battery", new TableInfo.Column("battery", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationDummy.put("network", new TableInfo.Column("network", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationDummy.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocationDummy = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocationDummy = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocationDummy = new TableInfo("location_dummy", _columnsLocationDummy, _foreignKeysLocationDummy, _indicesLocationDummy);
        final TableInfo _existingLocationDummy = TableInfo.read(db, "location_dummy");
        if (!_infoLocationDummy.equals(_existingLocationDummy)) {
          return new RoomOpenHelper.ValidationResult(false, "location_dummy(com.easyfield.location.models.SaveLocationRequest).\n"
                  + " Expected:\n" + _infoLocationDummy + "\n"
                  + " Found:\n" + _existingLocationDummy);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2b6f07ceb4d813b45324cb045dbe1ef6", "c31e330a54d78ae5cc7ef6bd52dc4e2f");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "location","location_dummy");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `location`");
      _db.execSQL("DELETE FROM `location_dummy`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(LocationDao.class, LocationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SaveLocationDao.class, SaveLocationDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public LocationDao locationDao() {
    if (_locationDao != null) {
      return _locationDao;
    } else {
      synchronized(this) {
        if(_locationDao == null) {
          _locationDao = new LocationDao_Impl(this);
        }
        return _locationDao;
      }
    }
  }

  @Override
  public SaveLocationDao savelocationDao() {
    if (_saveLocationDao != null) {
      return _saveLocationDao;
    } else {
      synchronized(this) {
        if(_saveLocationDao == null) {
          _saveLocationDao = new SaveLocationDao_Impl(this);
        }
        return _saveLocationDao;
      }
    }
  }
}
