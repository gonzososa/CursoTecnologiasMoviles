package rutasver.transver.Fragmentos;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import rutasver.transver.Base_de_datos.AdaptadorBd;
import rutasver.transver.R;

public class DialogCercanas extends DialogFragment {
    ListView lista_resultados;
    LatLng latlang_cercanas;
    AdaptadorBd base_de_datos;
    TextView titulo;
    String lista_cercanas[];
    ListenerdeOnclick listenerdeOnclick;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listenerdeOnclick=(ListenerdeOnclick) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState!=null){
            latlang_cercanas= new LatLng(savedInstanceState.getDouble("latitude"),savedInstanceState.getDouble("longitude"));
        }

        View view = inflater.inflate(R.layout.fragment_dialog_cercanas, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        lista_resultados = (ListView) view.findViewById(R.id.list);
        base_de_datos = new AdaptadorBd(getActivity());
        lista_cercanas = base_de_datos.rutas_cercanas(latlang_cercanas);
        titulo = (TextView) view.findViewById(R.id.text);
        String texto_titulo = String.valueOf(lista_cercanas.length)+getString(R.string.resultados);
        titulo.setText(texto_titulo);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, lista_cercanas);

        lista_resultados.setAdapter(adapter);

        lista_resultados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listenerdeOnclick.ruta_seleccionada(lista_resultados.getItemAtPosition(position).toString());
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override //sobrescribe método del padre
    public void onSaveInstanceState(Bundle outState) { //método que es llamado antes de que la app sea destruida por ejemplo cuando se rota el dispositivo
        super.onSaveInstanceState(outState); //se le pasa al padre el bundle outState
        if(latlang_cercanas!=null){
            outState.putDouble("latitude", latlang_cercanas.latitude); //guardamos en el bundle una variable
            outState.putDouble("longitude", latlang_cercanas.longitude); //guardamos en el bundle una variable
        }
    }

    public interface ListenerdeOnclick{
        public void ruta_seleccionada(String nombre);
    }
}
