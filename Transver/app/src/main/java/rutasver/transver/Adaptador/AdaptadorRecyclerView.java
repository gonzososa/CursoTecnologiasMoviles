package rutasver.transver.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
import rutasver.transver.Actividades.Imagen;
import rutasver.transver.Actividades.MainActivity;
import rutasver.transver.Actividades.RutaMapa;
import rutasver.transver.Modelo.Camion;
import rutasver.transver.R;



public class AdaptadorRecyclerView extends RecyclerView.Adapter<AdaptadorRecyclerView.CamionViewHolder>{

    Context mContext;
    List<Camion> camiones;

    public AdaptadorRecyclerView(Context context,List<Camion> camiones){
        mContext = context;
        this.camiones = camiones;
    }

    public static CardView cv;
    public static int position;

    public class CamionViewHolder extends RecyclerView.ViewHolder {

        TextView nombreCamion;
        TextView horarioCamion;
        ImageView fotoCamion;
        RelativeLayout relative_card;

        CamionViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            nombreCamion = (TextView) itemView.findViewById(R.id.nombre_camion);
            horarioCamion = (TextView) itemView.findViewById(R.id.horario_camion);
            fotoCamion = (ImageView) itemView.findViewById(R.id.foto_camion);
            relative_card = (RelativeLayout) itemView.findViewById(R.id.rl);

            fotoCamion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Imagen.nombre_camion=camiones.get(position).nombre;
                    Intent imagen_online = new Intent(mContext,Imagen.class);
                    mContext.startActivity(imagen_online);
                }
            });

            relative_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RutaMapa.busqueda = camiones.get(position).nombre;

                    Intent ruta_mapa = new Intent(mContext,RutaMapa.class);
                    mContext.startActivity(ruta_mapa);
                }
            });
        }

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CamionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        CamionViewHolder pvh = new CamionViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(CamionViewHolder camionViewHolder, int i) {
        camionViewHolder.nombreCamion.setText(camiones.get(i).nombre);
        camionViewHolder.horarioCamion.setText(camiones.get(i).horario);
        camionViewHolder.fotoCamion.setImageResource(camiones.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return camiones.size();
    }
}