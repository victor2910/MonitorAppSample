package aplicaciones.lectorconcentracion;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.wangyuwei.trend.Util;
import me.wangyuwei.trend.chart.TrendChart;
import me.wangyuwei.trend.entity.DateValueEntity;
import me.wangyuwei.trend.entity.LineEntity;


public class Vista_2 extends Fragment {
    public static final String TAG = "Vista2";
    private FragmentIterationListener mCallback = null;
    public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }

    public static Vista_2 newInstance(Bundle arguments){
        Vista_2 f = new Vista_2();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public Vista_2(){}

    //El fragment se ha adjuntado al Activity
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (FragmentIterationListener) activity;
        }catch(Exception ex){
            Log.e("ExampleFragment", "El Activity debe implementar la interfaz FragmentIterationListener");
        }
    }

    private LineEntity<DateValueEntity> getLineEntity(List<DateValueEntity> entities, String title, int lineColor) {
        LineEntity<DateValueEntity> lineEntity = new LineEntity<>();
        lineEntity.setTitle(title);
        lineEntity.setLineColor(lineColor);
        lineEntity.setLineData(entities);
        return lineEntity;
    }

    //El Fragment ha sido creado       
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //El Fragment va a cargar su layout, el cual debemos especificar
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v =  inflater.inflate(R.layout.fragment_vista_2, container, false);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(v != null){
            TextView tvEstado = (TextView) v.findViewById(R.id.v2stado);
            if(pref.getBoolean("conexion",false)){
                tvEstado.setText("Cargando ...");
            }else{
                tvEstado.setText("No conectado");
            }

            TrendChart trend_chart = (TrendChart) v.findViewById(R.id.trend_chart);
            if(trend_chart != null){
                List<DateValueEntity> lista = new ArrayList<>();
                lista.add(new DateValueEntity(0.0f,0));
                lista.add(new DateValueEntity(0.0f,1));

                List<LineEntity<DateValueEntity>> lines = new ArrayList<>();
                lines.add(getLineEntity(lista, "TREND", Color.BLUE));
                trend_chart.setLinesData(lines);
                trend_chart.withDisplayFrom(0)
                        .withDisplayNumber(lista.size())
                        .withMaxDisplayNum(lista.size())
                        .withMinDisplayNum(lista.size())
                        .withPrevClosePrice(lista.size())
                        .touchUp();
            }
        }

        return v;
    }


    //La vista de layout ha sido creada y ya está disponible
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState != null){

        }
    }

    //La vista ha sido creada y cualquier configuración guardada está carga
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    //El Activity que contiene el Fragment ha terminado su creación
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
    //El Fragment ha sido quitado de su Activity y ya no está disponible
    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

}
