package com.example.diegox101.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diegox101.asynctasks.DownloadImageTask;
import com.example.diegox101.climacool.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class Main2ActivityFragment extends Fragment {



    public Main2ActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_main2, container, false);

        Bundle ret = new Bundle();
        ret = getArguments();

        if(ret.getString("firstexec").equals("N")){
            ((TextView)inflatedView.findViewById(R.id.tvNombre)).setText(ret.getString("nombre"));
            ((TextView)inflatedView.findViewById(R.id.tvHumedad)).setText(ret.getString("humedad"));
            ((TextView)inflatedView.findViewById(R.id.tvPresion)).setText(ret.getString("presion"));
            ((TextView)inflatedView.findViewById(R.id.tvTemp)).setText(ret.getString("temp"));
            ((TextView)inflatedView.findViewById(R.id.tvTempMax)).setText(ret.getString("tempMax"));
            ((TextView)inflatedView.findViewById(R.id.tvTempMin)).setText(ret.getString("tempMin"));
            ((TextView)inflatedView.findViewById(R.id.tvVientos)).setText(ret.getString("veloc"));
            ImageView imagenClima = (ImageView) inflatedView.findViewById(R.id.climaImageView);
            imagenClima.setImageBitmap(DownloadImageTask.loadBitmap(this.getActivity(),ret.getString("imagenUrl")));
        }

        return inflatedView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

}
