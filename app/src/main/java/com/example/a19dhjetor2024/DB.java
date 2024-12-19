package com.example.a19dhjetor2024;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.mindrot.jbcrypt.BCrypt;

public class DB extends SQLiteOpenHelper {

    public static final String DBNAME="loggi.db";

    public DB(@Nullable Context context) {
        super(context,"loggi.db", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE adminUser(email TEXT PRIMARY KEY, password TEXT, name TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion ) {
        db.execSQL("Drop table if exists adminUser");
        onCreate(db);
    }
    public Boolean insertAdminUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Hash the password before storing it
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        contentValues.put("email", email);
        contentValues.put("password", hashedPassword);
        contentValues.put("name", name);

        long result = db.insert("adminUser", null, contentValues);
        return result != -1; // Return true if insert was successful
    }
    public boolean isUserValid(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT password FROM adminUser WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        // Check if the user exists
        if (cursor.moveToFirst()) {
            String storedHashedPassword = cursor.getString(0); // Get the hashed password from the database
            cursor.close();


            return BCrypt.checkpw(password, storedHashedPassword);
        } else {
            cursor.close();
            return false; // Email not found
        }
    }
}