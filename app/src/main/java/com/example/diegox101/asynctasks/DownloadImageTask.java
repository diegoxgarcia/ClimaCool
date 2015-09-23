package com.example.diegox101.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by diegoxgarcia on 8/9/15.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Void> {

    private ImageView bmImage;
    private Context context;
    private ProgressDialog dialog;
    private String pngName;
    private Activity activity;
    Activity mContext = null;
    private String progressTitle = "";
    private String progressMsj = "";

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    public DownloadImageTask(Activity activity, Context context, String progressMsj, String progressTitle){
        this.activity = activity;
        this.context = context;
        this.progressMsj = progressMsj;
        this.progressTitle = progressTitle;
    }

    private DownloadImageTask(Activity iContext){
        this.mContext = iContext;
    }


    @Override
    protected void onPreExecute() {
        if(!progressTitle.equals(""))
        dialog = ProgressDialog.show(this.activity, this.progressTitle, this.progressMsj, true);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void v) {

    }

    @Override
    protected Void doInBackground(String... urls) {
        String[] urlBmap = urls[0].split(" - ");
        Bitmap bmap = null;

        try{
            for(int i=0; i < urlBmap.length; i++){
                InputStream in = new URL(urlBmap[i]).openStream();
                bmap = BitmapFactory.decodeStream(in);
                String pngName = urlBmap[i].replaceAll("http://openweathermap.org/img/w/", "");
                saveFile(this.activity, bmap, pngName);
            }
        }catch(IOException e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void saveFile(Context context, Bitmap b, String pngName){
        FileOutputStream fos;
        try{
            fos = context.openFileOutput(pngName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.close();
        }catch (FileNotFoundException e){
            Log.d("Error","File Not Found");
            e.printStackTrace();
        }catch (IOException e){
            Log.d("Error", "Error IO");
            e.printStackTrace();
        }
    }

    public static Bitmap loadBitmap(Context context, String picName){
        Bitmap b = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
            fis.close();

        }
        catch (FileNotFoundException e) {
            Log.d("Error", "File Not Found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("Error", "Error IO");
            e.printStackTrace();
        }
        return b;
    }
}
