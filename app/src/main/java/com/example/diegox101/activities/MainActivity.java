package com.example.diegox101.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import com.example.diegox101.asynctasks.DownloadImageTask;
import com.example.diegox101.customadapters.CustomClimaAdapter;
import com.example.diegox101.asynctasks.LeerClima;
import com.example.diegox101.climacool.R;
import com.example.diegox101.fragments.Main2ActivityFragment;
import com.example.diegox101.listeners.GPSTrackerListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String IMAGE_URL_BASE = "http://openweathermap.org/img/w/";
    private static final String[] FILE_NAMES = {"01d.png","02d.png", "03d.png", "04d.png", "09d.png",
                                                "10d.png","11d.png","13d.png","50d.png","01n.png",
                                                "02n.png","03n.png","04n.png","09n.png","10n.png",
                                                "11n.png","13n.png","50n.png"};

    private ArrayList<String> nombreCiudades = null;
    private ListView list;
    private CustomClimaAdapter adapter;
    private GPSTrackerListener gpsTracker;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadImages();

        Main2ActivityFragment detailFragment = new Main2ActivityFragment();
        setFragment(detailFragment);

        gpsTracker = new GPSTrackerListener(this);
        location = gpsTracker.getLocation();
        getClimaLocation(location.getLatitude(), location.getLongitude());

        createListCiudades(this.list, this.adapter, getResources());

    }

    private ArrayList<String> getSharedPref(String c){
        ArrayList<String> ciu = new ArrayList<String>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String ciudadesJSON = prefs.getString(c,"");
        if (!ciudadesJSON.isEmpty()) {
            try {
                JSONArray arrayJSON = new JSONArray(ciudadesJSON);
                for (int i=0; i<arrayJSON.length(); i++) {
                    ciu.add(arrayJSON.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ciu;
    }

    private void getClimaLocation(double lat, double lon){
        LeerClima leerClimaLocation = new LeerClima(this,"Por favor espere...","Cargando ciudades...");
        leerClimaLocation.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + " ! ");
    }



    private void createListCiudades(ListView list, CustomClimaAdapter adapter, Resources res){
        String urls = new String();
        this.nombreCiudades = getSharedPref("ciudades");
        list = (ListView)findViewById(R.id.listView);

        if(nombreCiudades.isEmpty()){
            Intent intent = new Intent();
            intent.setClass(this,AgregarActivity.class);
            startActivityForResult(intent,101);
        }else{
            LeerClima leerClima = new LeerClima(this,"Por favor espere...","Cargando ciudades...",list, adapter, getResources());
            for(int i = 0; i < this.nombreCiudades.size();i++){
                try {
                    urls += "http://api.openweathermap.org/data/2.5/weather?q=" +
                            URLEncoder.encode(nombreCiudades.get(i).toString(), "utf-8") + " - ";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            leerClima.execute(urls);
        }
    }

    private void setFragment(Fragment f){
        Bundle bundle = new Bundle();
        bundle.putString("firstexec","S");
        f.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.climaDetailFragment,f,"detailFragment");
        ft.commit();
    }

    public void onClickRefresh(View v){
        createListCiudades(list, adapter, getResources());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this,AgregarActivity.class);
        startActivityForResult(intent, 101);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode==101) && (resultCode==RESULT_OK)) {

            String nombreCiudad = data.getStringExtra("ciudad");

            if (nombreCiudad!=null && nombreCiudad.length()>2) {
                try {
                    nombreCiudades.add(new String(URLEncoder.encode(nombreCiudad, "utf-8")));
                } catch (Exception e) {
                    //
                }

                // Grabamos
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("ciudades", nombreCiudades.toString());
                editor.commit();
            }

        }
        createListCiudades(this.list, this.adapter, getResources());
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void downloadImages(){

        String 	localPath = this.getFilesDir().getAbsolutePath();
        String[] files = this.getFilesDir().list();
        ArrayList<String> filesArr = new ArrayList<String>(Arrays.asList(files));
        String urls = "";
        DownloadImageTask dit = null;
        if(filesArr.isEmpty() || !filesArr.contains("50n.png")){
            dit = new DownloadImageTask(this, getApplicationContext(), "Por favor espere...", "Descargando imagenes...");

            for(int i=0; i < FILE_NAMES.length; i++){
                urls += IMAGE_URL_BASE + FILE_NAMES[i] + " - ";
            }
            dit.execute(urls);
        }else{

        }
    }



}
