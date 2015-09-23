package com.example.diegox101.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diegox101.climacool.R;
import com.example.diegox101.customadapters.CustomClimaAdapter;
import com.example.diegox101.models.Ciudad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by diegoxgarcia on 3/9/15.
 */
public class LeerClima extends AsyncTask<String, Void, ArrayList<String>> {

    private ProgressDialog dialog;
    private Activity activity = null;
    private String progressTitle = "";
    private String progressMsj = "";
    private String cod = "";
    private String cityName = "";
    private ListView listCiudades = null;
    private CustomClimaAdapter adapter = null;
    private Resources res = null;
    private ArrayList<Ciudad> ciudades = new ArrayList<Ciudad>();


    public LeerClima(Activity activity, String progressTitle, String progressMsj, ListView list, CustomClimaAdapter adapter, Resources res) {
        this.activity = activity;
        this.progressTitle = progressTitle;
        this.progressMsj = progressMsj;
        this.listCiudades = list;
        this.adapter = adapter;
        this.res = res;
    }

    public LeerClima(Activity activity, String progressTitle, String progressMsj){
        this.activity = activity;
        this.progressTitle = progressTitle;
        this.progressMsj = progressMsj;

    }

    public String getCod(String input) {
        try {
            JSONObject json = new JSONObject(processURLStream(input));
            this.cod = json.getString("cod");
            this.cityName = json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this.cod;
    }

    public String getCityName(){
        return this.cityName;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(this.activity, this.progressTitle, this.progressMsj, true);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<String> s) {
        dialog.cancel();

        try {
            for (Iterator iterator = s.iterator(); iterator.hasNext();) {
                String jsonResponse = (String) iterator.next();
                Ciudad ciudad = new Ciudad();
                // Tomo todo el string del JSON y genero un objeto de tipo JSON.
                JSONObject json = new JSONObject(jsonResponse.toString());

                ciudad.setNombre(json.getString("name"));

                // Tomo del objeto Main la temperatura.
                JSONObject jsonMain = json.getJSONObject("main");

                // Tomo un objeto de tipo Array donde puedo ir a buscar el m�s informaci�n de clima.
                JSONArray weatherJson = json.getJSONArray("weather");

                double temperaturaK = jsonMain.getDouble("temp");
                float temperaturaKFloat = ((int)temperaturaK*100)/100;
                float temperaturaC = (float) (temperaturaKFloat-273.15);

                double temperaturaMinK = jsonMain.getDouble("temp_min");
                float temperaturaMinKFloat = ((int)temperaturaMinK*100)/100;
                float temperaturaMinC = (float) (temperaturaMinKFloat-273.15);

                double temperaturaMaxK = jsonMain.getDouble("temp_max");
                float temperaturaMaxKFloat = ((int)temperaturaMaxK*100)/100;
                float temperaturaMaxC = (float) (temperaturaMaxKFloat-273.15);

                ciudad.setTempMax(String.valueOf(temperaturaMaxC) + " ºC");

                ciudad.setTempMin(String.valueOf(temperaturaMinC) + " ºC");

                ciudad.setTemperatura(String.valueOf(temperaturaC) + " ºC");

                ciudad.setHumedad(String.valueOf(jsonMain.getInt("humidity")) + "%");

                ciudad.setPresion(String.valueOf(jsonMain.getInt("pressure")) + " hpa");

                JSONObject jsonObjectIcon = (JSONObject) weatherJson.get(0);
                ciudad.setImagen(jsonObjectIcon.getString("icon") + ".png");

                if(jsonResponse.toString().contains("{latlon}")){
                    ((TextView)this.activity.findViewById(R.id.tvNombreLocal)).setText(ciudad.getNombre());
                    ((TextView)this.activity.findViewById(R.id.tvTempLocal)).setText(ciudad.getTemperatura());
                    ((TextView)this.activity.findViewById(R.id.tvTempLocalMax)).setText(ciudad.getTempMax());
                    ((TextView)this.activity.findViewById(R.id.tvTempLocalMin)).setText(ciudad.getTempMin());
                    ((TextView)this.activity.findViewById(R.id.tvHumedadLocal)).setText(ciudad.getHumedad());
                    ((TextView)this.activity.findViewById(R.id.tvPresionLocal)).setText(ciudad.getPresion());
                    ((ImageView) this.activity.findViewById(R.id.imageViewLocal)).setImageBitmap(
                            DownloadImageTask.loadBitmap(this.activity,ciudad.getImagen()));


                    return;
                }
                this.ciudades.add(ciudad);
            }

            adapter = new CustomClimaAdapter(this.activity, this.ciudades, this.res);
            listCiudades.setAdapter(adapter);
        } catch (Exception e) {

            // Cualquier problema en la lectura del JSON, se ir� por este camino.
            Toast.makeText(this.activity, "Hubo un problema", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        InputStream inputStream = null;
        String result = "";
        ArrayList<String> responseJson = new ArrayList<String>();
        String[] urls = {};
        if(!params[0].contains(" ! ")) {
            urls = params[0].split(" - ");
            try {
                for (int i = 0; i < urls.length; i++) {
                    inputStream = new URL(urls[i]).openStream();
                    if (inputStream != null) {
                        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
                        String line = "";
                        while ((line = buffer.readLine()) != null)
                            result += line;
                        responseJson.add(result);
                        result = "";
                        inputStream.close();
                    } else {
                        // ERROR;
                    }
                }


            } catch (Exception e) {
                // ERROR;
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return responseJson;
        }else{
            try {
                urls = params[0].split(" ! ");
                inputStream = new URL(urls[0]).openStream();
                if (inputStream != null) {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = buffer.readLine()) != null)
                        result += line;
                    result += "{latlon}";
                    responseJson.add(result);
                    result = "";
                    inputStream.close();
                } else {
                    // ERROR;
                }
            }catch(Exception e){

            }
        }
        return responseJson;
    }


    private String processURLStream(String input){
        InputStream inputStream = null;
        String result = "";
        try {
            inputStream = new URL(input).openStream();
            if(inputStream != null) {
                BufferedReader buffer = new BufferedReader( new InputStreamReader(inputStream));
                String line = "";
                while ((line = buffer.readLine()) != null)
                    result += line;

                inputStream.close();
            } else {
                // ERROR;
            }

        } catch (Exception e) {
            // ERROR;
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }


}
