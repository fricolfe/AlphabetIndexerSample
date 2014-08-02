package com.suappmovil.alphabetindexersample.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.suappmovil.alphabetindexersample.db.MainDB;

public class Coches extends ContentProvider {
    //El nombre del proveedor, debe coincidir con el de AndroidManifest.xml
    private static final String PROVIDER_NAME = "suappmovil.com.alphabetindexersample.content.coches";
    //Los tipos segun indica el SDK
    private static final String TYPE_ITEM = "vnd.android.cursor.dir/vnd." + PROVIDER_NAME;
    private static final String TYPE_DIR = "vnd.android.cursor.item/vnd." + PROVIDER_NAME;
    //Definimos las Uris que utilizaremos
    private static final String SCHEMA = "content://";
    private static final Uri URI_CREATE = Uri.parse(SCHEMA + PROVIDER_NAME + "/create");
    private static final Uri URI_READ = Uri.parse(SCHEMA + PROVIDER_NAME + "/read");
    private static final Uri URI_UPDATE = Uri.parse(SCHEMA + PROVIDER_NAME + "/update");
    private static final Uri URI_DELETE = Uri.parse(SCHEMA + PROVIDER_NAME + "/delete");
    private static final Uri URI_FILE = Uri.parse(SCHEMA + PROVIDER_NAME + "/file");
    //Los id para validar las uris
    private static final int CREATE_COCHE = 1;
    private static final int READ_COCHES = 2;
    private static final int READ_COCHE = 3;
    private static final int UPDATE_COCHES = 4;
    private static final int UPDATE_COCHE = 5;
    private static final int DELETE_COCHES = 6;
    private static final int DELETE_COCHE = 7;
    //Declaramos la uris validas
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "create", CREATE_COCHE);
        uriMatcher.addURI(PROVIDER_NAME, "read", READ_COCHES);
        uriMatcher.addURI(PROVIDER_NAME, "read/#", READ_COCHE);
        uriMatcher.addURI(PROVIDER_NAME, "update", UPDATE_COCHES);
        uriMatcher.addURI(PROVIDER_NAME, "update/#", UPDATE_COCHE);
        uriMatcher.addURI(PROVIDER_NAME, "delete", DELETE_COCHES);
        uriMatcher.addURI(PROVIDER_NAME, "delete/#", DELETE_COCHE);
    }

    private static final String SELECTION_ID = "(" + MainDB.Coches.Field.ID + "=?)";

    //El helper para acceder a los datos
    private MainDB mDB;

    //Unas cuantas funciones para llamar a las uris.
    public static Uri getUriCreate() {
        return URI_CREATE;
    }

    public static Uri getUriRead() {
        return URI_READ;
    }

    public static Uri getUriRead(long id) {
        return Uri.parse(getUriRead() + "/" + id);
    }

    public static Uri getUriUpdate() {
        return URI_UPDATE;
    }

    public static Uri getUriUpdate(long id) {
        return Uri.parse(getUriUpdate() + "/" + id);
    }

    public static Uri getUriDelete() {
        return URI_DELETE;
    }

    public static Uri getUriDelete(long id) {
        return Uri.parse(getUriDelete() + "/" + id);
    }

    public Coches() {
    }

    @Override
    public boolean onCreate() {
        // Creamos el helper de datos
        mDB = new MainDB(getContext());
        return true;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //Validamos la uris
        switch (uriMatcher.match(uri)) {
            case DELETE_COCHES:
                break;
            case DELETE_COCHE:
                //En caso de borrar solo 1 items debemos modificar los parametros
                long id = ContentUris.parseId(uri);
                selection = SELECTION_ID;
                selectionArgs = new String[]{String.valueOf(id)};
                break;
            default:
                throw new IllegalArgumentException("Unsupported delete URI: " + uri);
        }
        SQLiteDatabase db = mDB.getWritableDatabase();
        int r = db.delete(MainDB.Coches.TABLE, selection, selectionArgs);
        if (r > 0) {
            //Si se ha modificado algo lo notificamos.
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return r;
    }

    @Override
    public String getType(Uri uri) {
        // Los tipos de devolvemos.
        switch (uriMatcher.match(uri)) {
            case READ_COCHES:
                return TYPE_DIR;
            case READ_COCHE:
                return TYPE_ITEM;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        switch (uriMatcher.match(uri)) {
            case CREATE_COCHE:
                break;
            default:
                throw new IllegalArgumentException("Unsupported insert URI: " + uri);
        }
        SQLiteDatabase db = mDB.getWritableDatabase();
        long id = db.insert(MainDB.Coches.TABLE, null, values);
        if (id < 0) {
            //No se pudo insertar.
            return null;
        }
        //Notificar el cambio
        getContext().getContentResolver().notifyChange(uri, null);
        //Devolvemos un uri para leer lo que hemos creado
        return getUriRead(id);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case READ_COCHES:
                break;
            case READ_COCHE:
                //Solo leemos uno
                long id = ContentUris.parseId(uri);
                selection = SELECTION_ID;
                selectionArgs = new String[]{String.valueOf(id)};
                break;
            default:
                throw new IllegalArgumentException("Unsupported query URI: " + uri);
        }
        SQLiteDatabase db = mDB.getReadableDatabase();
        return db.query(MainDB.Coches.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case UPDATE_COCHES:
                break;
            case UPDATE_COCHE:
                //Machacamos la seleccion
                long id = ContentUris.parseId(uri);
                selection = SELECTION_ID;
                selectionArgs = new String[]{String.valueOf(id)};
                break;
            default:
                throw new IllegalArgumentException("Unsupported update URI: " + uri);
        }
        SQLiteDatabase db = mDB.getWritableDatabase();
        int r = db.update(MainDB.Coches.TABLE, values, selection, selectionArgs);
        if (r > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return r;
    }
}
