package rutasver.transver.Modelo;


public class Camion {

    public String nombre; //nombre del camión
    public String horario; //horario del camión
    public int photoId; //id de la foto

    public Camion(String nombre, String horario, int photoId) { //constructor
        this.nombre = nombre; //inicializamos la variable con lo que recibe el constructor
        this.horario = horario; //inicializamos la variable con lo que recibe el constructor
        this.photoId = photoId; //inicializamos la variable con lo que recibe el constructor
    }
}