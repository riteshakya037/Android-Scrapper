package com.calebtrevino.tallystacker.models.preferences;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.views.activities.TallyStackerApplication;
import java.util.Map.Entry;

/**
 * This class allow you to use shared preferences between variable processes on android
 *
 * @author Ritesh Shakya
 */

@SuppressWarnings("unused") public class MultiProcessPreference extends ContentProvider {

    private static final String TYPE = "type";
    private static final String KEY = "key";
    private static final String INT_TYPE = "integer";
    private static final String LONG_TYPE = "long";
    private static final String FLOAT_TYPE = "float";
    private static final String BOOLEAN_TYPE = "boolean";
    private static final String STRING_TYPE = "string";
    private static final int MATCH_DATA = 0x010000;
    private static String PREFERENCE_AUTHORITY;
    private static Uri BASE_URI;
    private static UriMatcher matcher;

    private static void init(Context context) {

        PREFERENCE_AUTHORITY = context.getString(R.string.multiprocess_preferences_authority);

        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(PREFERENCE_AUTHORITY, "*/*", MATCH_DATA);

        BASE_URI = Uri.parse("content://" + PREFERENCE_AUTHORITY);
    }

    private static String getStringValue(Cursor cursor, String def) {
        if (cursor == null) return def;
        String value = def;
        if (cursor.moveToFirst()) {
            value = cursor.getString(0);
        }
        cursor.close();
        return value;
    }

    private static boolean getBooleanValue(Cursor cursor, boolean def) {
        if (cursor == null) return def;
        boolean value = def;
        if (cursor.moveToFirst()) {
            value = cursor.getInt(0) > 0;
        }
        cursor.close();
        return value;
    }

    private static int getIntValue(Cursor cursor, int def) {
        if (cursor == null) return def;
        int value = def;
        if (cursor.moveToFirst()) {
            value = cursor.getInt(0);
        }
        cursor.close();
        return value;
    }

    private static long getLongValue(Cursor cursor, long def) {
        if (cursor == null) return def;
        long value = def;
        if (cursor.moveToFirst()) {
            value = cursor.getLong(0);
        }
        cursor.close();
        return value;
    }

    private static float getFloatValue(Cursor cursor, float def) {
        if (cursor == null) return def;
        float value = def;
        if (cursor.moveToFirst()) {
            value = cursor.getFloat(0);
        }
        cursor.close();
        return value;
    }

    public static Editor edit(Context context) {
        return new Editor(context);
    }

    public static MultiProcessSharedPreferences getDefaultSharedPreferences() {
        return new MultiProcessSharedPreferences(TallyStackerApplication.get());
    }

    private static Uri getContentUri(Context context, String key, String type) {
        if (BASE_URI == null) {
            init(context);
        }
        return BASE_URI.buildUpon().appendPath(key).appendPath(type).build();
    }

    @Override public boolean onCreate() {
        if (matcher == null) {
            init(getContext());
        }
        return true;
    }

    @Override public String getType(@NonNull Uri uri) {
        return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + PREFERENCE_AUTHORITY + ".item";
    }

    @SuppressWarnings("ConstantConditions") @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case MATCH_DATA:
                PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext())
                        .edit()
                        .clear()
                        .apply();
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri " + uri);
        }

        return 0;
    }

    @SuppressWarnings("ConstantConditions") @SuppressLint("NewApi") @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        switch (matcher.match(uri)) {
            case MATCH_DATA:
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                        getContext().getApplicationContext()).edit();
                for (Entry<String, Object> entry : values.valueSet()) {
                    final Object value = entry.getValue();
                    final String key = entry.getKey();
                    if (value == null) {
                        editor.remove(key);
                    } else if (value instanceof String) {
                        editor.putString(key, (String) value);
                    } else if (value instanceof Boolean) {
                        editor.putBoolean(key, (Boolean) value);
                    } else if (value instanceof Long) {
                        editor.putLong(key, (Long) value);
                    } else if (value instanceof Integer) {
                        editor.putInt(key, (Integer) value);
                    } else if (value instanceof Float) {
                        editor.putFloat(key, (Float) value);
                    } else {
                        throw new IllegalArgumentException("Unsupported type " + uri);
                    }
                }
                editor.apply();
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri " + uri);
        }

        return null;
    }

    @SuppressWarnings("ConstantConditions") @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        MatrixCursor cursor;
        switch (matcher.match(uri)) {
            case MATCH_DATA:
                final String key = uri.getPathSegments().get(0);
                final String type = uri.getPathSegments().get(1);
                cursor = new MatrixCursor(new String[] { key });
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                        getContext().getApplicationContext());
                if (!sharedPreferences.contains(key)) return cursor;
                MatrixCursor.RowBuilder rowBuilder = cursor.newRow();
                Object object;
                if (STRING_TYPE.equals(type)) {
                    object = sharedPreferences.getString(key, null);
                } else if (BOOLEAN_TYPE.equals(type)) {
                    object = sharedPreferences.getBoolean(key, false) ? 1 : 0;
                } else if (LONG_TYPE.equals(type)) {
                    object = sharedPreferences.getLong(key, 0L);
                } else if (INT_TYPE.equals(type)) {
                    object = sharedPreferences.getInt(key, 0);
                } else if (FLOAT_TYPE.equals(type)) {
                    object = sharedPreferences.getFloat(key, 0f);
                } else {
                    throw new IllegalArgumentException("Unsupported type " + uri);
                }
                rowBuilder.add(object);
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri " + uri);
        }
        return cursor;
    }

    @Override public int update(@NonNull Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused") public static class Editor {

        private final Context context;
        private final ContentValues values = new ContentValues();

        private Editor(Context context) {
            this.context = context;
        }

        private void apply() {
            context.getContentResolver().insert(getContentUri(context, KEY, TYPE), values);
        }

        public void commit() {
            apply();
        }

        public Editor putString(String key, String value) {
            values.put(key, value);
            return this;
        }

        public Editor putLong(String key, long value) {
            values.put(key, value);
            return this;
        }

        public Editor putBoolean(String key, boolean value) {
            values.put(key, value);
            return this;
        }

        public Editor putInt(String key, int value) {
            values.put(key, value);
            return this;
        }

        public Editor putFloat(String key, float value) {
            values.put(key, value);
            return this;
        }

        public void remove(String key) {
            values.putNull(key);
        }

        /**
         * Call content provider method immediately. apply or commit is not required for this case
         * So it's sync method.
         */
        public void clear() {
            context.getContentResolver().delete(getContentUri(context, KEY, TYPE), null, null);
        }
    }

    @SuppressWarnings({ "SameParameterValue", "unused" })
    public static class MultiProcessSharedPreferences {

        private final Context context;

        private MultiProcessSharedPreferences(Context context) {
            this.context = context;
        }

        public Editor edit() {
            return new Editor(context);
        }

        public String getString(String key, String def) {
            Cursor cursor = context.getContentResolver()
                    .query(getContentUri(context, key, STRING_TYPE), null, null, null, null);
            return getStringValue(cursor, def);
        }

        public long getLong(String key, long def) {
            Cursor cursor = context.getContentResolver()
                    .query(getContentUri(context, key, LONG_TYPE), null, null, null, null);
            return getLongValue(cursor, def);
        }

        public float getFloat(String key, float def) {
            Cursor cursor = context.getContentResolver()
                    .query(getContentUri(context, key, FLOAT_TYPE), null, null, null, null);
            return getFloatValue(cursor, def);
        }

        public boolean getBoolean(String key, boolean def) {
            Cursor cursor = context.getContentResolver()
                    .query(getContentUri(context, key, BOOLEAN_TYPE), null, null, null, null);
            return getBooleanValue(cursor, def);
        }

        public int getInt(String key, int def) {
            Cursor cursor = context.getContentResolver()
                    .query(getContentUri(context, key, INT_TYPE), null, null, null, null);
            return getIntValue(cursor, def);
        }
    }
}