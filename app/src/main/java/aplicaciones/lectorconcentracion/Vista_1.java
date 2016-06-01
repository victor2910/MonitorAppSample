package aplicaciones.lectorconcentracion;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Vista_1 extends Fragment {
    private FragmentIterationListener mCallback = null;
    private float valmedida;
    public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }

    public static Vista_1 newInstance(Bundle arguments){
        Vista_1 f = new Vista_1();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public Vista_1(){}

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
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Bundle b=getArguments();
        float limite = Float.parseFloat(pref.getString("limitar","0.0"));
        valmedida = b.getFloat("MED");
        View v =  inflater.inflate(R.layout.fragment_vista_1, container, false);
        if(v != null){
            TextView tvMedida = (TextView) v.findViewById(R.id.medida);
            if(valmedida>limite){
                tvMedida.setTextColor(Color.RED);
            }else{
                tvMedida.setTextColor(Color.BLUE);
            }

            String cdmed = ""+valmedida +" ug/L";
            tvMedida.setText(cdmed);
        }

        return v;
    }
    //La vista de layout ha sido creada y ya está disponible
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
