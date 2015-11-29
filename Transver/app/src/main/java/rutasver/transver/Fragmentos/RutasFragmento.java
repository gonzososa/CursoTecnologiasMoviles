package rutasver.transver.Fragmentos;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import rutasver.transver.Adaptador.AdaptadorRecyclerView;
import rutasver.transver.Adaptador.RecyclerItemClickListener;
import rutasver.transver.Base_de_datos.AdaptadorBd;
import rutasver.transver.Modelo.Camion;
import rutasver.transver.R;

public class RutasFragmento extends Fragment {

    public String query;
    public RecyclerView rv;
    public AdaptadorBd base_de_datos;
    public List<Camion> camiones = new ArrayList<>();
    public SearchView searchView;
    public static boolean esta_buscando;
    public static boolean hay_texto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState!=null) {
            esta_buscando=savedInstanceState.getBoolean("esta_buscando"); //asignamos a la variable correspondiente el valor guardado en savedInstanceState
            hay_texto=savedInstanceState.getBoolean("hay_texto"); //asignamos a la variable correspondiente el valor guardado en savedInstanceState
            query=savedInstanceState.getString("query"); //asignamos a la variable correspondiente el valor guardado en savedInstanceState
            //
            // searchView.setQuery(query,true);
        }

        View v = inflater.inflate(R.layout.fragment_rutas,container,false);

        rv = (RecyclerView) v.findViewById(R.id.rv);

        rv.setHasFixedSize(true);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        AdaptadorRecyclerView.position = position;
                    }
                })
        );

        setHasOptionsMenu(true);

        base_de_datos = new AdaptadorBd(getActivity()); //igualamos la variable a la clase datos

        if(base_de_datos.esta_vacia()) { //si regresa true el método quiere decir que la bd esta vacía
            base_de_datos.insertarDatos(); //llamada al método encargado de llenar la bd
        }

        initializeData();
        initializeAdapter();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Infla el menu; esto agrega elementos a la barra de acción
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint("Buscar...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                camiones.clear();
                rv_vista(query);
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esta_buscando=true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                esta_buscando = false;
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void rv_vista(String query){
        this.query=query;
        if(!query.equals(""))
            hay_texto=true;
        else
            hay_texto=false;
        base_de_datos.buscar_camiones(camiones, query);
        initializeAdapter();
    }

    private void initializeData(){
        base_de_datos.llenar_arreglo(camiones);
    }

    private void initializeAdapter(){
        AdaptadorRecyclerView adapter = new AdaptadorRecyclerView(getActivity(),camiones);
        rv.setAdapter(adapter);
    }

    @Override //sobrescribe método del padre
    public void onSaveInstanceState(Bundle outState) { //método que es llamado antes de que la app sea destruida por ejemplo cuando se rota el dispositivo
        super.onSaveInstanceState(outState); //se le pasa al padre el bundle outState
        outState.putBoolean("esta_buscando", esta_buscando); //guardamos en el bundle una variable
        outState.putBoolean("hay_texto", hay_texto); //guardamos en el bundle una variable
        outState.putString("query", query); //guardamos en el bundle una variable
    }
}
