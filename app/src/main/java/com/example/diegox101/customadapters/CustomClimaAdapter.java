package com.example.diegox101.customadapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diegox101.fragments.Main2ActivityFragment;
import com.example.diegox101.asynctasks.DownloadImageTask;
import com.example.diegox101.climacool.R;
import com.example.diegox101.models.Ciudad;

import java.util.ArrayList;

/**
 * Created by Diegox101 on 03/09/2015.
 */
public class CustomClimaAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Ciudad> dataCiudades;
    private Resources res;
    private static LayoutInflater inflater = null;
    public TextView txtNombre;
    public TextView txtTemperatura;
    public ImageView imagenClima;
    public Ciudad tempValues = null;

    public CustomClimaAdapter(Activity activity, ArrayList<Ciudad> dataCiudades, Resources res) {
        this.activity = activity;
        this.dataCiudades = dataCiudades;
        this.res = res;
        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(dataCiudades.size()<0) return 1;
        return dataCiudades.size();
    }

    @Override
    public Object getItem(int position) {
        return dataCiudades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if(convertView==null){
            vi = inflater.inflate(R.layout.tabitem,null);
        }

        txtNombre = (TextView) vi.findViewById(R.id.nombreCiudad);
        txtTemperatura = (TextView) vi.findViewById(R.id.temperaturaCiudad);
        imagenClima = (ImageView) vi.findViewById(R.id.climaImage);

        tempValues = null;
        tempValues = dataCiudades.get(position);

        txtNombre.setText(tempValues.getNombre());
        txtTemperatura.setText(tempValues.getTemperatura());
        imagenClima.setImageBitmap(DownloadImageTask.loadBitmap(this.activity,tempValues.getImagen()));

        vi.setOnClickListener(new OnItemClickListener(position, this.activity, this.dataCiudades));

        return vi;
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;
        private Activity act;
        private ArrayList<Ciudad> ciudades;

        OnItemClickListener(int position, Activity act, ArrayList<Ciudad> ciudades){
            this.mPosition = position;
            this.act = act;
            this.ciudades = ciudades;
        }

        @Override
        public void onClick(View arg0) {
            Ciudad c = this.ciudades.get(this.mPosition);
            Bundle argum = new Bundle();
            argum.putString("firstexec","N");
            argum.putString("humedad",c.getHumedad());
            argum.putString("nombre",c.getNombre());
            argum.putString("tempMax",c.getTempMax());
            argum.putString("tempMin",c.getTempMin());
            argum.putString("temp",c.getTemperatura());
            argum.putString("veloc",c.getVelocViento());
            argum.putString("presion", c.getPresion());
            argum.putString("imagenUrl",c.getImagen());
            Main2ActivityFragment fragment = new Main2ActivityFragment();
            fragment.setArguments(argum);
            FragmentManager fm = this.act.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.climaDetailFragment, fragment, "detailFragment");
            ft.commit();
        }
    }

}
