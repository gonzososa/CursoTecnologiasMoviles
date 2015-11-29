package rutasver.transver.Base_de_datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AuxiliarBd extends SQLiteOpenHelper { /*auxiliar para la creación de la base de datos
    extiende SQLiteOpenHelper de sqlcipher para cifrado de la bd*/

    //cadena que contiene la sentencia de sqlite a ejecutar para la creación de la tabla camiones
    private static final String CREAR_TABLA_CAMIONES =
            "CREATE TABLE CAMIONES"+
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "NOMBRE TEXT,"+
                    "SENTIDO TEXT,"+
                    "HORARIO TEXT,"+
                    "IMAGEN TEXT,"+
                    "IMAGEN_ONLINE TEXT);";

    //cadena que contiene la sentencia de sqlite a ejecutar para la creación de la tabla coordenadas
    private static final String CREAR_TABLA_COORDENADAS =
            "CREATE TABLE COORDENADAS"+
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "CAMIONES_ID INTEGER,"+
                    "LAT REAL,"+
                    "LON REAL,"+
                    "FOREIGN KEY (CAMIONES_ID) REFERENCES CAMIONES(ID) ON DELETE CASCADE);";


    //constructor de la esta clase que recibe contexto,nombre de la bd, cursor (se usa null), version de la bd
    public AuxiliarBd(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version); //se le pasa al padre los parámetros recibidos en el constructor
    }

    @Override //sobrescribe del padre
    public void onCreate(SQLiteDatabase sqLiteDatabase) { /*método del padre que se llama cuando se crea la bd
        específicamente cuando se solicita getWritableDatabase o getReadableDatabase*/
        sqLiteDatabase.execSQL(CREAR_TABLA_CAMIONES); //ejecuta la creación de la tabla camiones
        sqLiteDatabase.execSQL(CREAR_TABLA_COORDENADAS); //ejecuta la creación de la tabla coordenadas
    }

    @Override //sobrescribe del padre
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { /*método del padre que se llama cuando
    se actualiza la bd específicamente cuando se cambia la version de la bd*/

    }
}
