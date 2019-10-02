package com.android.imabhishekkumar.back2college.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class AsyncHandler extends AsyncTask<String, String,String> {
    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            Log.d("file_path_url",url.toString());
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+
                    "/SavedImages";
            Log.d("file_path",file_path);
            saveBitmap(image,file_path);
        } catch(IOException e) {
            System.out.println(e);
        }
    return null;
    }
    private void saveBitmap(Bitmap bitmap,String path){
        try {
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }

            Random random = new Random();
            int r = random.nextInt(10000);
            File f = new File(file, "image" + r + ".jpg");
            FileOutputStream fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i(null, "Save file error!");
        }
    }
}
