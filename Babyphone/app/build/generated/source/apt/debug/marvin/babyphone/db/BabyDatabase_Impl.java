package marvin.babyphone.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.HashSet;

public class BabyDatabase_Impl extends BabyDatabase {
  private volatile BabyDao _babyDao;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `baby_entries` (`timeStamp` INTEGER NOT NULL, `baby_state` INTEGER, `sitter_state` INTEGER, PRIMARY KEY(`timeStamp`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"1b0c7794dd76f53dc710359fa23ea5bc\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `baby_entries`");
      }

      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsBabyEntries = new HashMap<String, TableInfo.Column>(3);
        _columnsBabyEntries.put("timeStamp", new TableInfo.Column("timeStamp", "INTEGER", true, 1));
        _columnsBabyEntries.put("baby_state", new TableInfo.Column("baby_state", "INTEGER", false, 0));
        _columnsBabyEntries.put("sitter_state", new TableInfo.Column("sitter_state", "INTEGER", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBabyEntries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBabyEntries = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBabyEntries = new TableInfo("baby_entries", _columnsBabyEntries, _foreignKeysBabyEntries, _indicesBabyEntries);
        final TableInfo _existingBabyEntries = TableInfo.read(_db, "baby_entries");
        if (! _infoBabyEntries.equals(_existingBabyEntries)) {
          throw new IllegalStateException("Migration didn't properly handle baby_entries(marvin.babyphone.model.BabyEntry).\n"
                  + " Expected:\n" + _infoBabyEntries + "\n"
                  + " Found:\n" + _existingBabyEntries);
        }
      }
    }, "1b0c7794dd76f53dc710359fa23ea5bc");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "baby_entries");
  }

  @Override
  public BabyDao babyDao() {
    if (_babyDao != null) {
      return _babyDao;
    } else {
      synchronized(this) {
        if(_babyDao == null) {
          _babyDao = new BabyDao_Impl(this);
        }
        return _babyDao;
      }
    }
  }
}
