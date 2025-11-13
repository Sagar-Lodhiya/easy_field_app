package com.easyfield.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.easyfield.location.models.SaveLocationRequest;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SaveLocationDao_Impl implements SaveLocationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SaveLocationRequest> __insertionAdapterOfSaveLocationRequest;

  private final SharedSQLiteStatement __preparedStmtOfDeleteallLocations;

  public SaveLocationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSaveLocationRequest = new EntityInsertionAdapter<SaveLocationRequest>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `location_dummy` (`location_id`,`user_id`,`latitude`,`longitude`,`employee_punch_id`,`gps_status`,`battery`,`network`,`timestamp`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SaveLocationRequest entity) {
        statement.bindLong(1, entity.getLocationId());
        if (entity.getUserId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUserId());
        }
        if (entity.getLatitude() == null) {
          statement.bindNull(3);
        } else {
          statement.bindDouble(3, entity.getLatitude());
        }
        if (entity.getLongitude() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getLongitude());
        }
        if (entity.getAttendanceId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAttendanceId());
        }
        if (entity.getGpsStatus() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getGpsStatus());
        }
        if (entity.getBatteryInPercentage() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getBatteryInPercentage());
        }
        if (entity.getNetworkType() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNetworkType());
        }
        statement.bindLong(9, entity.getTimestamp());
      }
    };
    this.__preparedStmtOfDeleteallLocations = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM location_dummy";
        return _query;
      }
    };
  }

  @Override
  public Object addLocation(final SaveLocationRequest location,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSaveLocationRequest.insert(location);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteallLocations(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteallLocations.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteallLocations.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public List<SaveLocationRequest> getAllLocations() {
    final String _sql = "SELECT * FROM location_dummy";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "location_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAttendanceId = CursorUtil.getColumnIndexOrThrow(_cursor, "employee_punch_id");
      final int _cursorIndexOfGpsStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "gps_status");
      final int _cursorIndexOfBatteryInPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "battery");
      final int _cursorIndexOfNetworkType = CursorUtil.getColumnIndexOrThrow(_cursor, "network");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final List<SaveLocationRequest> _result = new ArrayList<SaveLocationRequest>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SaveLocationRequest _item;
        final long _tmpLocationId;
        _tmpLocationId = _cursor.getLong(_cursorIndexOfLocationId);
        final String _tmpUserId;
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _tmpUserId = null;
        } else {
          _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        }
        final Double _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        final Double _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        final String _tmpAttendanceId;
        if (_cursor.isNull(_cursorIndexOfAttendanceId)) {
          _tmpAttendanceId = null;
        } else {
          _tmpAttendanceId = _cursor.getString(_cursorIndexOfAttendanceId);
        }
        final Integer _tmpGpsStatus;
        if (_cursor.isNull(_cursorIndexOfGpsStatus)) {
          _tmpGpsStatus = null;
        } else {
          _tmpGpsStatus = _cursor.getInt(_cursorIndexOfGpsStatus);
        }
        final String _tmpBatteryInPercentage;
        if (_cursor.isNull(_cursorIndexOfBatteryInPercentage)) {
          _tmpBatteryInPercentage = null;
        } else {
          _tmpBatteryInPercentage = _cursor.getString(_cursorIndexOfBatteryInPercentage);
        }
        final String _tmpNetworkType;
        if (_cursor.isNull(_cursorIndexOfNetworkType)) {
          _tmpNetworkType = null;
        } else {
          _tmpNetworkType = _cursor.getString(_cursorIndexOfNetworkType);
        }
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item = new SaveLocationRequest(_tmpLocationId,_tmpUserId,_tmpLatitude,_tmpLongitude,_tmpAttendanceId,_tmpGpsStatus,_tmpBatteryInPercentage,_tmpNetworkType,_tmpTimestamp);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Object deleteLocations(final Long[] ids, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("DELETE FROM location_dummy WHERE location_id IN (");
        final int _inputSize = ids.length;
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (long _item : ids) {
          _stmt.bindLong(_argIndex, _item);
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
