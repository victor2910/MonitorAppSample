package aplicaciones.lectorconcentracion;

import android.preference.PreferenceActivity;
import android.os.Bundle;

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
