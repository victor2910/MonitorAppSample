package aplicaciones.lectorconcentracion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.wangyuwei.trend.chart.TrendChart;
import me.wangyuwei.trend.entity.DateValueEntity;
import me.wangyuwei.trend.entity.LineEntity;

public class Inicio extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, Dash.FragmentIterationListener,
        Vista_1.FragmentIterationListener, Vista_2.FragmentIterationListener {

    private static final String url="http://aquatek.mybluemix.net/getdata";
    private Float medida = 0.0f;
    private List<DateValueEntity> lista;
    private long cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lista = new ArrayList<>();
        lista.add(new DateValueEntity(0.0f,0));
        lista.add(new DateValueEntity(0.0f,1));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle("Inicio");

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cotent_inicio,Dash.newInstance(null));
        ft.commit();

    }

    @Override
    protected void onResume(){
        super.onResume();
        new RcfServer(this){
            @Override
            protected void onProgressUpdate(String... values ){
                TextView tvMedida = (TextView) findViewById(R.id.medida);
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                float limite = Float.parseFloat(pref.getString("limitar","20.0"));
                medida = Float.parseFloat(values[0]);

                if(tvMedida != null) {
                    if (medida > limite) {
                        tvMedida.setTextColor(Color.RED);
                    } else {
                        tvMedida.setTextColor(Color.BLUE);
                    }
                    tvMedida.setText(values[0]+" ug/L");
                }

                TextView v2Estado = (TextView) findViewById(R.id.v2stado);
                if(v2Estado!=null){
                    if(pref.getBoolean("conexion",false)){
                        v2Estado.setText("");
                    }
                }

                lista.add(new DateValueEntity(medida,cont++));
                TrendChart trend_chart = (TrendChart) findViewById(R.id.trend_chart);
                if(trend_chart != null){
                    List<LineEntity<DateValueEntity>> lines = new ArrayList<>();
                    lines.add(getLineEntity(lista, "TREND", Color.BLUE));
                    trend_chart.setLinesData(lines);
                    trend_chart.withDisplayFrom(0)
                            .withDisplayNumber(lista.size())
                            .withMaxDisplayNum(lista.size())
                            .withMinDisplayNum(lista.size())
                            .withPrevClosePrice(limite)
                            .touchUp();
                }
            }
        }.execute(url);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Bundle b = new Bundle();
        b.putFloat("MED",medida);
        int id = item.getItemId();
        Fragment fragment = null;
        boolean fragmentTransaction = false;

        if(id == R.id.nav_home){
            fragment = new Dash();
            fragmentTransaction = true;
            getSupportActionBar().setTitle("Inicio");
        }else if (id == R.id.nav_vista_1) {
            // Handle the camera action
            fragment = Vista_1.newInstance(b);
            fragmentTransaction = true;
            getSupportActionBar().setTitle("Vista 1");

        } else if (id == R.id.nav_vista_2) {
            fragment = new Vista_2();
            fragmentTransaction = true;
            getSupportActionBar().setTitle("Vista 2");

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(this,Settings.class));
        }

        if(fragmentTransaction) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cotent_inicio, fragment)
                    .commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentIteration(Bundle parameters) {

    }


    private LineEntity<DateValueEntity> getLineEntity(List<DateValueEntity> entities, String title, int lineColor) {
        LineEntity<DateValueEntity> lineEntity = new LineEntity<>();
        lineEntity.setTitle(title);
        lineEntity.setLineColor(lineColor);
        lineEntity.setLineData(entities);
        return lineEntity;
    }
}