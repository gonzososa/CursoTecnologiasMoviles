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
import rutasver.transver.R;

public class DialogMapa extends DialogFragment {

    ListView lista_opciones;
    ListenerdeOnclick listenerdeOnclick;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listenerdeOnclick=(ListenerdeOnclick) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        lista_opciones = (ListView) view.findViewById(R.id.list);

        String lista_camiones[] = getResources().getStringArray(R.array.lista_mapa);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, lista_camiones);

        lista_opciones.setAdapter(adapter);

        lista_opciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listenerdeOnclick.opciones(position);
                getDialog().dismiss();
            }
        });

        return view;
    }

    public interface ListenerdeOnclick{
        public void opciones(int eleccion);
    }
}
