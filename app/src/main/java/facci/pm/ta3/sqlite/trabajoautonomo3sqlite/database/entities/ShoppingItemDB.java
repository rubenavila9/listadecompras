package facci.pm.ta3.sqlite.trabajoautonomo3sqlite.database.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import facci.pm.ta3.sqlite.trabajoautonomo3sqlite.database.helper.ShoppingElementHelper;
import facci.pm.ta3.sqlite.trabajoautonomo3sqlite.database.model.ShoppingItem;
import java.util.ArrayList;

public class ShoppingItemDB {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private ShoppingElementHelper dbHelper;

    public ShoppingItemDB(Context context) {
        // Create new helper
        dbHelper = new ShoppingElementHelper(context);
    }

    /* Inner class that defines the table contents */
    public static abstract class ShoppingElementEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                COLUMN_NAME_TITLE + TEXT_TYPE + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    public void insertElement(String productName) {
        //TODO: Todo el código necesario para INSERTAR un Item a la Base de datos
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ShoppingElementHelper.COLUMN_NAME_TITLE, productName);


        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ShoppingElementHelper.TABLE_NAME, null, values);

    }


    public ArrayList<ShoppingItem> getAllItems() {

        ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();

        String[] allColumns = { ShoppingElementEntry._ID,
                ShoppingElementEntry.COLUMN_NAME_TITLE};

        Cursor cursor = dbHelper.getReadableDatabase().query(
                ShoppingElementEntry.TABLE_NAME,    // The table to query
                allColumns,                         // The columns to return
                null,                               // The columns for the WHERE clause
                null,                               // The values for the WHERE clause
                null,                               // don't group the rows
                null,                               // don't filter by row groups
                null                                // The sort order
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ShoppingItem shoppingItem = new ShoppingItem(getItemId(cursor), getItemName(cursor));
            shoppingItems.add(shoppingItem);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        dbHelper.getReadableDatabase().close();
        return shoppingItems;
    }

    private long getItemId(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(ShoppingElementEntry._ID));
    }

    private String getItemName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(ShoppingElementEntry.COLUMN_NAME_TITLE));
    }


    public void clearAllItems() {
        //TODO: Todo el código necesario para ELIMINAR todos los Items de la Base de datos
        // Define 'where' part of query.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = ShoppingElementHelper.TABLE_NAME + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { "MyTitle" };
        // Issue SQL statement.
        int deletedRows = db.delete(ShoppingElementHelper.TABLE_NAME, selection, selectionArgs);

    }

    public void updateItem(ShoppingItem shoppingItem) {
        //TODO: Todo el código necesario para ACTUALIZAR un Item en la Base de datos
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // New value for one column
        String title = "MyNewTitle";
        ContentValues values = new ContentValues();
        values.put(ShoppingElementHelper.COLUMN_NAME_TITLE, title);

        // Which row to update, based on the title
        String selection = ShoppingElementHelper.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = { "MyOldTitle" };

        int count = db.update(
                ShoppingElementHelper.TABLE_NAME,
                values,
                selection,
                selectionArgs);


    }

    public void deleteItem(ShoppingItem shoppingItem) {
        //TODO: Todo el código necesario para ELIMINAR un Item de la Base de datos
        // Define 'where' part of query.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = ShoppingElementHelper.COLUMN_NAME_TITLE + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { "MyTitle" };
        // Issue SQL statement.
        int deletedRows = db.delete(ShoppingElementHelper.TABLE_NAME, selection, selectionArgs);
    }
}
