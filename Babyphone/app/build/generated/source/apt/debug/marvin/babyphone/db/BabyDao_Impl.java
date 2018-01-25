package marvin.babyphone.db;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import marvin.babyphone.model.BabyEntry;

public class BabyDao_Impl implements BabyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfBabyEntry;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public BabyDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBabyEntry = new EntityInsertionAdapter<BabyEntry>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `baby_entries`(`timeStamp`,`baby_state`,`sitter_state`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BabyEntry value) {
        stmt.bindLong(1, value.getTimeStamp());
        final int _tmp;
        _tmp = BabyStateConverter.BabyStateToInt(value.getBabyState());
        stmt.bindLong(2, _tmp);
        final int _tmp_1;
        _tmp_1 = SitterStateConverter.BabyStateToInt(value.getSitterState());
        stmt.bindLong(3, _tmp_1);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM baby_entries";
        return _query;
      }
    };
  }

  @Override
  public void insert(BabyEntry entry) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfBabyEntry.insert(entry);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<BabyEntry> getAll() {
    final String _sql = "SELECT * FROM baby_entries";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfTimeStamp = _cursor.getColumnIndexOrThrow("timeStamp");
      final int _cursorIndexOfBabyState = _cursor.getColumnIndexOrThrow("baby_state");
      final int _cursorIndexOfSitterState = _cursor.getColumnIndexOrThrow("sitter_state");
      final List<BabyEntry> _result = new ArrayList<BabyEntry>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final BabyEntry _item;
        final long _tmpTimeStamp;
        _tmpTimeStamp = _cursor.getLong(_cursorIndexOfTimeStamp);
        final BabyEntry.BabyState _tmpBabyState;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfBabyState);
        _tmpBabyState = BabyStateConverter.fromInteger(_tmp);
        final BabyEntry.SitterState _tmpSitterState;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfSitterState);
        _tmpSitterState = SitterStateConverter.fromInteger(_tmp_1);
        _item = new BabyEntry(_tmpTimeStamp,_tmpBabyState,_tmpSitterState);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
