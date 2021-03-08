package com.example.clientcontactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE = "ContactList.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("PRAGMA foreign_keys = 1");
        db.execSQL("PRAGMA auto_vacuum = 1");

        db.execSQL("CREATE TABLE GUEST(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Vorname VARCHAR(50) NOT NULL CHECK(length(Vorname)>0)," +
                "Nachname VARCHAR(50) NOT NULL CHECK(length(Nachname)>0)," +
                "Adresse VARCHAR(100) NOT NULL CHECK(length(Adresse)>0)," +
                "Telefonnummer VARCHAR(15) NOT NULL CHECK(length(Telefonnummer)>0))");

        db.execSQL("CREATE TABLE VISIT(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "GuestID," +
                "Datum DATE NOT NULL," +
                "Tisch INTEGER NOT NULL," +
                "FOREIGN KEY(GuestID) REFERENCES Guest(ID)" +
                "ON DELETE CASCADE" +
                " ON UPDATE CASCADE )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS VISIT");
        db.execSQL("DROP TABLE IF EXISTS GUEST");
        onCreate(db);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean insertValues(String vorname, String nachname, String adresse, String telefonnummer, String tisch) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("nachname",nachname);
        contentValues.put("vorname",vorname);
        contentValues.put("adresse",adresse);
        contentValues.put("telefonnummer",telefonnummer);

        Long result = db.insert("GUEST",null,contentValues);
        if (result == -1) {return false;}


        String args[] = {nachname,vorname};
        Cursor res = db.rawQuery("SELECT ID FROM GUEST WHERE NACHNAME LIKE ? AND VORNAME LIKE ? ORDER BY ID DESC " , args );
        res.moveToFirst();
        String id = res.getString(0);

        ContentValues contentValues2 = new ContentValues();
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");


        contentValues2.put("GuestID",id);
        contentValues2.put("datum",now.format(formatter));
        contentValues2.put("tisch",tisch);
        Long result2 = db.insert("Visit",null,contentValues2);
        if (result2 == -1) {return false;}

        return true;

    }
    public Cursor getAllLogs() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT g.ID,Nachname,Vorname,Adresse,Telefonnummer,v.Datum,v.tisch FROM GUEST g INNER JOIN VISIT v ON g.ID = v.GuestID ORDER BY v.Datum DESC ", null);
        return res;

    }
    public Cursor getLog(String id, String date, String nachname) {
        if (id.equals(null) ||id.equals("")){id = "%";}
        if(date.equals(null) ||date.equals("")){date = "%";}
        if(nachname.equals(null) ||nachname.equals("")){nachname = "%";}
        date = date + "%";
        String []args = {id,date,nachname};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT g.ID,Nachname,Vorname,Adresse,Telefonnummer,v.Datum,v.tisch FROM GUEST g INNER JOIN VISIT v ON g.ID = v.GuestID WHERE g.id like ? AND v.Datum like ? AND g.nachname like ? ORDER BY v.Datum DESC ", args);
        return res;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean visitLogin(String id, String nachname, String tisch) {
        SQLiteDatabase db = this.getWritableDatabase();

        String args[] = {id};
        Cursor res = db.rawQuery("SELECT Nachname FROM GUEST WHERE ID Like ? ", args );
        res.moveToFirst();

        nachname = nachname.toUpperCase();
        String nachnameDb = res.getString(0).toUpperCase();
        char initial = nachnameDb.charAt(0);
        if(!(initial == nachname.charAt(0))){
            return false;
        }

        ContentValues contentValues2 = new ContentValues();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");

        contentValues2.put("GuestID",id);
        contentValues2.put("datum",now.format(formatter));
        contentValues2.put("tisch",tisch);
        Long result2 = db.insert("Visit",null,contentValues2);
        if (result2 == -1) {return false;}

        return true;
    }

    public String getLastId() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT ID FROM GUEST ORDER BY ID DESC LIMIT 1", null);
        res.moveToFirst();
        return res.getString(0);
    }

    public String deleteOldRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("DELETE FROM VISIT WHERE ID IN (SELECT v.ID FROM GUEST g INNER JOIN VISIT v ON g.ID = v.GuestID WHERE v.datum < datetime('now','-30 days'))", null).moveToFirst();
        Cursor res = db.rawQuery("SELECT COUNT(ID) FROM GUEST WHERE ID NOT IN (SELECT v.ID FROM GUEST g INNER JOIN VISIT v ON g.ID = v.GuestID)", null);
        res.moveToFirst();
        db.rawQuery("DELETE FROM GUEST WHERE ID NOT IN (SELECT g.ID FROM GUEST g INNER JOIN VISIT v ON g.ID = v.GuestID)", null).moveToFirst();
        return res.getString(0);
    }

    public String searchId(String id) {
        String args[] = {id};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT Count(ID) FROM GUEST WHERE ID = ?", args);
        res.moveToFirst();
        return res.getString(0);
    }
}
