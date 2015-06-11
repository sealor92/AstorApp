package sebastianalarcon.com.astorapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManager {
    public static final String TABLE_NAME = "restaurantes";
    public static final String CN_ID = "_id";  // Nombre columna
    public static final String CN_NAME = "nombre";
    public static final String CN_LAT = "latitud";
    public static final String CN_LONG = "longitud";
    // create table contactos(
    //                          _id integer primary key autoincrement,
    //                          nombre text not null,
    //                          telefono text);
    public static final String CREATE_TABLE = "create table "+ TABLE_NAME+ " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_NAME + " text not null,"
            + CN_LAT + " text,"
            + CN_LONG + " text);";

    DBhelper helper;
    SQLiteDatabase db;
    public DataBaseManager(Context context) {
        helper = new DBhelper(context);
        db = helper.getWritableDatabase();
    }

    public ContentValues generarContentValues (String Nombre, String Latitud, String Longitud){
        ContentValues valores = new ContentValues();
        valores.put(CN_NAME,Nombre);
        valores.put(CN_LAT,Latitud);
        valores.put(CN_LONG,Longitud);
        return valores;
    }

    public void insertar(String Nombre, String Latitud, String Longitud){
        db.insert(TABLE_NAME,null,generarContentValues(Nombre,Latitud,Longitud));
    }

    public Cursor cargarCursorContactos(){
        String [] columnas = new String[]{CN_ID,CN_NAME,CN_LAT,CN_LONG};
        return db.query(TABLE_NAME,columnas,null,null,null,null,null);
    }

    public void eliminar(String nombre){
        db.delete(TABLE_NAME,CN_NAME + "=?", new String[]{nombre});
    }

    public void Modificar(String nombre, String nuevoLAT, String nuevoLONG){
        db.update(TABLE_NAME,generarContentValues(nombre,nuevoLAT,nuevoLONG),CN_NAME+"=?",new String[]{nombre});
    }

    public Cursor buscarContacto(String Nombre) {
        String [] columnas = new String[]{CN_ID,CN_NAME,CN_LAT,CN_LONG};
        return db.query(TABLE_NAME,columnas,CN_NAME + "=?",new String[]{Nombre},null,null,null);
    }
}
