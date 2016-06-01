package aplicaciones.lectorconcentracion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RcfServer extends AsyncTask <String,String,String>{
    private Context context;

    public RcfServer(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... urls) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String respues = "";
        while(pref.getBoolean("conexion",false)) {

            try {
                respues = downloadUrl(urls[0]);
                publishProgress(respues);
                esperar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return respues;
    }

    private String downloadUrl(String myurl) throws IOException{
        InputStream is = null;
        int len = 100;
        try{
            //Establecimiento de parametros previos a la conexion
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //Establecer que se recibira algo luego de la conexion
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            //Inicio de la conexion
            conn.connect();

            //Respuesta
            int response = conn.getResponseCode();
            Log.d("respuesta","The response is:" + response);
            is = conn.getInputStream();
            //Convierte el InputStream a String
            String contentAsString = readIt(is,len);
            return contentAsString;
        }finally{	//Cierra el flujo de entrada
            if(is != null){
                is.close();
            }
        }
    }

    private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    private void esperar(){
        try{
            Thread.sleep(5000);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
