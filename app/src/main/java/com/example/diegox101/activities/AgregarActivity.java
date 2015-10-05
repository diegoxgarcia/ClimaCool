package com.example.diegox101.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diegox101.DBAdapter.ClimaDBAdapter;
import com.example.diegox101.asynctasks.LeerClima;
import com.example.diegox101.climacool.R;
import com.example.diegox101.models.Ciudad;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;

public class AgregarActivity extends AppCompatActivity implements View.OnClickListener{

    private ClimaDBAdapter climaDBAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        Button btnAgregarCiudad = (Button) findViewById(R.id.button);

        btnAgregarCiudad.setOnClickListener(this);

        if (true) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }


    @Override
    public void onClick(View v) {
        String etNombreText = ((EditText) findViewById(R.id.etCiudad)).getText().toString();
        String input = "";
        try {
            input = "http://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(etNombreText, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LeerClima clima = new LeerClima(this,"Por favor espere...","Verificando ciudad",null,null,null);
        Ciudad ciudad = clima.getCod(input);
        climaDBAdapter = new ClimaDBAdapter(this);
        int count=0;
        try {
            count = climaDBAdapter.getRegistro(ciudad.getId()).getCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(ciudad.getCod()!=null && ciudad.getCod().equals("200") && count==0){
            ContentValues reg = new ContentValues();
            reg.put(ClimaDBAdapter.C_COLUMNA_NOMBRE, ciudad.getNombre());
            reg.put(ClimaDBAdapter.C_COLUMNA_ID, ciudad.getId());
            reg.put(ClimaDBAdapter.C_COLUMNA_TEMPERATURA, ciudad.getTemperatura());
            reg.put(ClimaDBAdapter.C_COLUMNA_HUMEDAD, ciudad.getHumedad());
            reg.put(ClimaDBAdapter.C_COLUMNA_PRESION, ciudad.getPresion());
            reg.put(ClimaDBAdapter.C_COLUMNA_TEMP_MAXIMA, ciudad.getTempMax());
            reg.put(ClimaDBAdapter.C_COLUMNA_TEMP_MINIMA, ciudad.getTempMin());
            reg.put(ClimaDBAdapter.C_COLUMNA_IMAGECODE, ciudad.getImagen());
            reg.put(ClimaDBAdapter.C_COLUMNA_VIENTO, ciudad.getVelocViento());
            climaDBAdapter.insert(reg);
            Intent datos = new Intent();
            // datos.putExtra("ciudad", etNombreText);
            setResult(RESULT_OK, datos);
            finish();
        }else{
            Toast.makeText(AgregarActivity.this, "La ciudad no existe o ya esta cargada...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agregar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent datos = new Intent();
        setResult(RESULT_OK, datos);
        finish();

        return super.onOptionsItemSelected(item);
    }
}
