/*
 * Copyright 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.camera2raw;

import android.app.Activity;
import android.content.res.AssetManager;
import android.hardware.camera2.CameraAccessException;
import android.os.Bundle;
import android.app.AlarmManager;
import android.content.Intent;
import android.app.PendingIntent;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Activity displaying a fragment that implements RAW photo captures.
 */
public class CameraActivity extends Activity {

    private static final String TAG = "Something";

    public static Camera2RawFragment cameraraw = Camera2RawFragment.newInstance();
    //String path = Environment.getDataDirectory()+"/android-Camera2Raw/";


    private void copy_assets(Context context) {
        AssetManager assetManager = context.getAssets();

        String path = getApplicationContext().getApplicationInfo().dataDir+"/files";
        Log.e(TAG, "Output path is"+path);

        new File(path).mkdirs();

        try {
            InputStream in = assetManager.open("index.html");
            OutputStream out = new FileOutputStream(path+"/index.html");

            Log.e(TAG, "Output path is"+path);

            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: "+ e.getMessage());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        int i;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        cameraraw.forced_focus_mm=60;
        //cameraraw.init_webserver();

        Context context = getApplicationContext();
        cameraraw.propagate_context(context);

        Log.e(TAG, "Trying to get assets");
        copy_assets(context);

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "macro_bracketing");

        Log.e(TAG, "Dir name is" + mediaStorageDir.getPath());

        //if this "JCGCamera folder does not exist
        if (!mediaStorageDir.exists()) {
            Log.e(TAG, "Trying to create subdir...");
            //if you cannot make this folder return
            if (!mediaStorageDir.mkdirs()) {
                Log.e(TAG, "Couldn't create dcim subdir...");
            }
        }

/*
        new Thread(new Runnable() {
            public void run() { cameraraw.ftp_upload();            }
        }).start();
*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        /*
        if(Settings.System.canWrite(this)){
            // change setting here
        }
        else{
            //Migrate to Setting write permission screen.
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + mContext.getPackageName()));
            startActivity(intent);
        }
*/
        //Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_OFF_TIMEOUT, 10000);

        /*
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (2 * 1000), pendingIntent);
*/


        if (null == savedInstanceState) {
            //getFragmentManager().beginTransaction().replace(R.id.container, Camera2RawFragment.newInstance()).commit();
            getFragmentManager().beginTransaction().replace(R.id.container, cameraraw).commit();
        }


        cameraraw.forced_exposure=30_000_000L;
        cameraraw.forced_iso=80;
        cameraraw.force_exposure=1;


/*
        final Handler handler = new Handler();

        for(i=0;i<0;i++) {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 1000ms
                    cameraraw.takePicture();
                    cameraraw.forced_focus_mm+=cameraraw.forced_focus_mm/8;
                }
            }, 1000+i*5000);


            //CameraActivity.cameraraw.takePicture();
            //cameraraw.takePicture();
        }
*/
        //cameraraw.takePicture();


    }

}
