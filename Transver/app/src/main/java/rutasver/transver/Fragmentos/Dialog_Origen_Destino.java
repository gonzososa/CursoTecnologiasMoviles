package rutasver.transver.Fragmentos;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import rutasver.transver.R;
public class Dialog_Origen_Destino extends DialogFragment {

    ListView lista_resultados;
    TextView titulo;
    String resultado_origen_destino[];
    ListenerdeOnclick listenerdeOnclick;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listenerdeOnclick=(ListenerdeOnclick) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState!=null){
            resultado_origen_destino= savedInstanceState.getStringArray("resultado_origen_destino");
        }

        View view = inflater.inflate(R.layout.fragment_dialog__origen__destino, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        lista_resultados = (ListView) view.findViewById(R.id.list);

        titulo = (TextView) view.findViewById(R.id.text);
        String texto_titulo = String.valueOf(resultado_origen_destino.length)+getString(R.string.resultados);
        titulo.setText(texto_titulo);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, resultado_origen_destino);

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
        if(resultado_origen_destino!=null){
            outState.putStringArray("resultado_origen_destino", resultado_origen_destino); //guardamos en el bundle una variable
        }
    }

    public interface ListenerdeOnclick{
        public void ruta_seleccionada(String nombre);
    }
}
