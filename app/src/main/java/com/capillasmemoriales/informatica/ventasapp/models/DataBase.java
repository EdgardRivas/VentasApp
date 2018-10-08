package com.capillasmemoriales.informatica.ventasapp.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SisadLocal.db";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE seg_usuarios_local (" +
                "codempleado integer primary key, " +
                "usuario text, " +
                "email text, " +
                "cl_user text, " +
                "codempresa integer)");
        db.execSQL("CREATE TABLE crm_prospectos_local (" +
                "codprospecto integer primary key, " +
                "nombre text, " +
                "apellido text, " +
                "telefono text, " +
                "email text, " +
                "estado boolean /*1 = Modify*/, " +
                "codasesorasignado integer, " +
                "foreign key (codasesorasignado) references seg_usuarios_local (codempleado))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USERS");
        db.execSQL("DROP TABLE IF EXISTS CONTACTS");
       onCreate(db);
    }
}