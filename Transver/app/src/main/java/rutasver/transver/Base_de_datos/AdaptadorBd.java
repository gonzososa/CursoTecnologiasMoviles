package rutasver.transver.Base_de_datos;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import rutasver.transver.Modelo.Camion;
import rutasver.transver.R;



public class AdaptadorBd { //adaptador de la bd se crea la bd con la ayuda del auxiliar y se realizan consultas

    private static final String NOMBRE_DB = "transver.db"; //nombre de la bd
    private static final int VERSION_DB = 1; //versión de la vd
    public AuxiliarBd auxbd; //instancia de la clase AuxiliarBd
    public SQLiteDatabase db; /*SQLiteDatabase expone los métodos para manejar una base de datos,
    por ejemplo crear, borrar,ejecutar comandos de SQL y realizar otras tareas comunes de las bd,
    se le pone en este caso el nombre de instancia db*/
    public Context contexto; /*Context es una interfaz para acceder a la información global acerca de el entorno de
     una aplicación. Esta es una clase abstracta cuya implementación es proveída por el sistema de android. Permite acceso
     a recursos y clases específicos de la aplicación, así como llamadas para operaciones a nivel de aplicación
     como lanzar actividades, difundir información y recibir intents,etc. */
    public static final String nombre = "NOMBRE";//cadena con el nombre de la columna 1 de la tabla camiones
    public static final String sentido = "SENTIDO";//cadena con el nombre de la columna 2 de la tabla camiones
    public static final String horario = "HORARIO";//cadena con el nombre de la columna 3 de la tabla camiones
    public static final String imagen = "IMAGEN";//cadena con el nombre de la columna 4 de la tabla camiones
    public static final String imagen_online = "IMAGEN_ONLINE";//cadena con el nombre de la columna 5 de la tabla camiones
    public static final String camiones_id = "CAMIONES_ID";//cadena con el nombre de la columna 1 de la tabla coordenadas
    public static final String lat = "LAT";//cadena con el nombre de la columna 2 de la tabla coordenadas
    public static final String lon = "LON";//cadena con el nombre de la columna 3 de la tabla coordenadas

    public AdaptadorBd(Context contexto){ //constructor de esta clase recibe Context
        auxbd = new AuxiliarBd(contexto,NOMBRE_DB,null,VERSION_DB); //inicializamos auxbd
        this.contexto=contexto; //inicializamos la variable this.contexto con el contexto que recibe el constructor
    }

    public boolean esta_vacia(){ //método que revisa si la bd esta vacía
        db = auxbd.getReadableDatabase();
        if(DatabaseUtils.queryNumEntries(db, "CAMIONES") == 0){
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }
    }

    public void insertarDatos(){ //método encargado de la inserción de los datos a la bd

        db = auxbd.getWritableDatabase(); /*inicializamos bd con una llamada a través de auxbd
        con un getWritableDatabase() que es para obtener una bd en modo de escritura*/

        ContentValues _Values1 = new ContentValues(); //Content Values es una clase utilizada para guardar un conjunto de valores se le asigna una instancia
        ContentValues _Values2 = new ContentValues(); //Content Values es una clase utilizada para guardar un conjunto de valores se le asigna una instancia

        Resources res = contexto.getResources(); // a través de resources obtenemos acceso a la carpeta res de nuestra app

        XmlResourceParser xml1 = res.getXml(R.xml.datos); //se abre el archivo xml correspondiente
        XmlResourceParser xml2 = res.getXml(R.xml.registros_coordenadas); //se abre el archivo xml correspondiente

        try //con try-catch se atrapan las excepciones por cualquier error que ocurra al leer nuestro xml
        {
            db.beginTransaction(); //iniciamos una transacción a la bd es la manera más rápida de bulk insertion
            int posicion = xml1.getEventType(); //con getEventType obtenemos con los TAGS en que posición del documento estamos
            while (posicion != XmlPullParser.END_DOCUMENT) { //ciclo que continua mientras posición sea diferente del final del documento
                if ((posicion == XmlPullParser.START_TAG) &&(xml1.getName().equals("registro"))){ /*si la posición esta dentro del bloque START_TAG
                    y el la etiqueta xml tiene como nombre registro entramos a obtener los valores*/

                    String _nombre = xml1.getAttributeValue(null, nombre); //guardamos en una cadena el atributo que contenga la etiqueta correspondiente
                    String _sentido = xml1.getAttributeValue(null, sentido); //guardamos en una cadena el atributo que contenga la etiqueta correspondiente
                    String _horario = xml1.getAttributeValue(null, horario); //guardamos en una cadena el atributo que contenga la etiqueta correspondiente
                    String _imagen = xml1.getAttributeValue(null, imagen); //guardamos en una cadena el atributo que contenga la etiqueta correspondiente
                    String _imagen_online = xml1.getAttributeValue(null, imagen_online); //guardamos en una cadena el atributo que contenga la etiqueta correspondiente
                    _Values1.put(nombre, _nombre); //guardamos en el Content Values lo obtenido del xml y le asignamos el nombre de la columna de bd
                    _Values1.put(sentido, _sentido); //guardamos en el Content Values lo obtenido del xml y le asignamos el nombre de la columna de bd
                    _Values1.put(horario, _horario); //guardamos en el Content Values lo obtenido del xml y le asignamos el nombre de la columna de bd
                    _Values1.put(imagen, _imagen); //guardamos en el Content Values lo obtenido del xml y le asignamos el nombre de la columna de bd
                    _Values1.put(imagen_online, _imagen_online); //guardamos en el Content Values lo obtenido del xml y le asignamos el nombre de la columna de bd
                    db.insert("CAMIONES", null, _Values1); //hacemos la bulk insertion en la tabla CAMIONES de los Content Values
                }
                posicion = xml1.next(); //movemos la posición a la siguiente fila
            }
            db.setTransactionSuccessful(); //marcamos la transacción como exitosa
        }
        catch (XmlPullParserException|IOException e) //atrapamos el error ya sea del xml parser o de input output
        {
            Log.e("XML", e.getMessage(), e); //mostramos en el log mensaje con el error
        }
        finally
        {
            db.endTransaction(); //terminamos la transacción de la bd
            xml1.close(); //cerramos el archivo xml
        }

        try //con try-catch se atrapan las excepciones por cualquier error que ocurra al leer nuestro xml
        {
            db.beginTransaction(); //iniciamos una transacción a la bd es la manera más rápida de bulk insertion
            int posicion = xml2.getEventType(); //con getEventType obtenemos con los TAGS en que posición del documento estamos
            while (posicion != XmlPullParser.END_DOCUMENT) { //ciclo que continua mientras posición sea diferente del final del document
                if ((posicion == XmlPullParser.START_TAG) &&(xml2.getName().equals("registro"))){/*si la posición esta dentro del bloque START_TAG
                    y el la etiqueta xml tiene como nombre registro entramos a obtener los valores*/

                    String _camiones_id = xml2.getAttributeValue(null, camiones_id); //guardamos en una cadena el atributo que contenga la etiqueta correspondiente
                    String _lat = xml2.getAttributeValue(null, lat); //guardamos en una cadena el atributo que contenga la etiqueta correspondiente
                    String _lon = xml2.getAttributeValue(null, lon); //guardamos en una cadena el atributo que contenga la etiqueta correspondiente
                    _Values2.put(camiones_id, _camiones_id); //guardamos en el Content Values lo obtenido del xml y le asignamos el nombre de la columna de bd
                    _Values2.put(lat, _lat); //guardamos en el Content Values lo obtenido del xml y le asignamos el nombre de la columna de bd
                    _Values2.put(lon, _lon); //guardamos en el Content Values lo obtenido del xml y le asignamos el nombre de la columna de bd
                    db.insert("COORDENADAS", null, _Values2); //hacemos la bulk insertion en la tabla CAMIONES de los Content Values
                }
                posicion = xml2.next(); //movemos la posición a la siguiente fila
            }
            db.setTransactionSuccessful(); //marcamos la transacción como exitosa
        }
        catch (XmlPullParserException|IOException e) //atrapamos el error ya sea del xml parser o de input output
        {
            Log.e("XML", e.getMessage(), e); //mostramos en el log mensaje con el error
        }
        finally
        {
            db.endTransaction(); //terminamos la transacción de la bd
            xml2.close(); //cerramos el archivo xml
            db.close(); //cerramos la bd, ya no se va a usar en este método
        }
    }

    public void llenar_arreglo(List<Camion> camiones){

        db = auxbd.getReadableDatabase(); //inicializamos bd con una llamada a través de auxbd

        String lista_camiones=
                "SELECT NOMBRE,HORARIO,IMAGEN FROM CAMIONES WHERE " +
                        "SENTIDO='ida' ORDER BY NOMBRE COLLATE LOCALIZED ASC";

        Cursor mcursor = db.rawQuery(lista_camiones, null);

        while(mcursor.moveToNext()){
            String nombre = mcursor.getString(mcursor.getColumnIndex("NOMBRE"));
            String horario = mcursor.getString(mcursor.getColumnIndex("HORARIO"));
            String photoId = mcursor.getString(mcursor.getColumnIndex("IMAGEN"));
            int id=contexto.getResources().getIdentifier(photoId,"drawable","rutasver.transver");
            camiones.add(new Camion(nombre,horario,id));
        }
        mcursor.close();
        db.close();
    }

    public String obtener_imagen(String nombre){

        db = auxbd.getReadableDatabase(); //inicializamos bd con una llamada a través de auxbd

        String imagen_big=
                "SELECT IMAGEN_ONLINE FROM CAMIONES WHERE NOMBRE = '"+nombre+"'";

        Cursor mcursor = db.rawQuery(imagen_big, null);

        String resultado="";

        while(mcursor.moveToNext()) {
            resultado=mcursor.getString(mcursor.getColumnIndex("IMAGEN_ONLINE"));
        }

        mcursor.close();
        db.close();
        return resultado;
    }

    public ArrayList mostrar_ruta(String nombre,String sentido) {

        db = auxbd.getReadableDatabase(); //inicializamos bd con una llamada a través de auxbd

        String obtener_ruta =
                "SELECT (LAT || \",\" || LON) AS LATLON FROM COORDENADAS "+
                        "INNER JOIN CAMIONES "+
                        "ON COORDENADAS.CAMIONES_ID=CAMIONES.ID WHERE CAMIONES.NOMBRE = '"+nombre+"' AND "+
                        "CAMIONES.SENTIDO='"+sentido+"'";

        Cursor mcursor = db.rawQuery(obtener_ruta, null);
        StringBuffer buffer=new StringBuffer();

        if (mcursor.moveToFirst()) {
            do {
                buffer.append("" + mcursor.getString(mcursor.getColumnIndex("LATLON")) + ",");
            } while (mcursor.moveToNext());
        }
        db.close();
        mcursor.close();
        String latlang = buffer.toString();
        String [] latlang_cortada = latlang.split(",");

        ArrayList<LatLng> coordenadas = new ArrayList<>();

        double lat = 0;//variable double que contendra latitud

        //ciclo for que recorre la cadena latlang
        for(int i=0;i<latlang_cortada.length;i++){
            //cuando i es 0 o i es par, guardamos la latitud haciendo la conversion de cadena a double
            if(i==0||i%2==0) {
                lat = Double.parseDouble(latlang_cortada[i]);
            }
            else {
                double lon = Double.parseDouble(latlang_cortada[i]);
                coordenadas.add(new LatLng(lat, lon));//se agrega la latitud y longitud a la lista de arreglos
            }
        }

        return coordenadas;
    }

    public void buscar_camiones(List<Camion> camiones,String query){
        String lista_camiones=
                "SELECT NOMBRE,HORARIO,IMAGEN FROM CAMIONES WHERE " +
                        "SENTIDO='ida' AND NOMBRE LIKE '%" + query +"%'"+"ORDER BY NOMBRE COLLATE LOCALIZED ASC";

        db = auxbd.getReadableDatabase(); //inicializamos bd con una llamada a través de auxbd

        Cursor mcursor = db.rawQuery(lista_camiones, null);

        while(mcursor.moveToNext()){
            String nombre = mcursor.getString(mcursor.getColumnIndex("NOMBRE"));
            String horario = mcursor.getString(mcursor.getColumnIndex("HORARIO"));
            String photoId = mcursor.getString(mcursor.getColumnIndex("IMAGEN"));
            int id=contexto.getResources().getIdentifier(photoId,"drawable","rutasver.transver");
            camiones.add(new Camion(nombre,horario,id));
        }
        mcursor.close();
        db.close();
    }

    public String[] rutas_cercanas(LatLng myPosition){

        String buscar_cerca="SELECT NOMBRE, MIN (DISTANCE) FROM " +
                " ( SELECT NOMBRE, (('"+myPosition.latitude+"' - COORDENADAS.LAT) * ('"+myPosition.latitude+"' - COORDENADAS.LAT) + " +
                " ('"+myPosition.longitude+"' - COORDENADAS.LON) * ('"+myPosition.longitude+"' - COORDENADAS.LON)) AS DISTANCE "+
                "FROM COORDENADAS INNER JOIN CAMIONES ON COORDENADAS.CAMIONES_ID=CAMIONES.ID "+
                "WHERE DISTANCE < 0.000023544)"+
                "GROUP BY NOMBRE ORDER BY DISTANCE";

        db = auxbd.getReadableDatabase(); //inicializamos bd con una llamada a través de auxbd

        Cursor mcursor = db.rawQuery(buscar_cerca, null);

        String [] cercanas = new String[mcursor.getCount()];
        int i=0;
        while(mcursor.moveToNext()){
            cercanas[i]=mcursor.getString(mcursor.getColumnIndex(nombre));
            i++;
        }

        mcursor.close();
        db.close();
        return cercanas;
    }
}
