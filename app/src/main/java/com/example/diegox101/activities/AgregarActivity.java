package com.example.diegox101.activities;

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

import com.example.diegox101.asynctasks.LeerClima;
import com.example.diegox101.climacool.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AgregarActivity extends AppCompatActivity implements View.OnClickListener{

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
            if(clima.getCod(input)!=null && clima.getCod(input).equals("200")){
                Intent datos = new Intent();
                datos.putExtra("ciudad", etNombreText);
                setResult(RESULT_OK, datos);
                finish();
            }else{
                Toast.makeText(AgregarActivity.this, "La ciudad no existe...", Toast.LENGTH_SHORT).show();
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
