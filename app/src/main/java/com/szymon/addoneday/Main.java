package com.szymon.addoneday;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class Main extends Activity {
    private boolean stan = false;
    private boolean odejmij = false;
    int ilosc = 1;

    Switch czyodejmij;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.v("App", "Build Version Greater than or equal to M: " + Build.VERSION_CODES.M);
            checkDrawOverlayPermission();
        } else {
            Log.v("App", "OS Version Less than M");
            //No need for Permission as less then M OS.
        }


        start = findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMyServiceRunning(OverlayShowingService.class)) {
                    startService();
                    start.setText("Stop");
                }
                else {
                    stopService();
                    start.setText("Start");
                }
            }
        });
    }

    public void stopService() {
        Intent svc = new Intent(this,OverlayShowingService.class);
        stopService(svc);
    }
    public void startService(){
        Intent svc = new Intent(this,OverlayShowingService.class);
        svc.putExtra("czyodejmij",odejmij);
        svc.putExtra("ilosc",ilosc);
        startService(svc);
    }
    public void showSettings(View view){
        Intent set = new Intent(this, com.szymon.addoneday.Settings.class);
        set.putExtra("ilosc",ilosc);
        set.putExtra("czyodejmij",odejmij);
        startActivityForResult(set,1);
    }

    public void showInfo(View view){
        Intent inf = new Intent(this,info.class);
        startActivity(inf);
    }
    public void exit(View view){
        finish();
    }



    // code to post/handler request for permission
    public final static int REQUEST_CODE = -1010101;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        Log.v("App", "Package Name: " + getApplicationContext().getPackageName());

        // check if we already  have permission to draw over other apps
        if (!Settings.canDrawOverlays(getApplicationContext())) {
       //     Log.v("App", "Requesting Permission" + Settings.canDrawOverlays(context));
            // if not construct intent to request permission
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getApplicationContext().getPackageName()));
    /// request permission via start activity for result
            startActivityForResult(intent, REQUEST_CODE);
        } else {
     //       Log.v("App", "We already have permission for it.");

        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    Log.v("App", "OnActivity Result.");
        //check if received result code
        //  is equal our requested code for draw permission
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    //disablePullNotificationTouch();
                }
            }
        }
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                ilosc = data.getIntExtra("ilosc",ilosc);
                odejmij = data.getBooleanExtra("czyodejmij",odejmij);
            }
            if (resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),"Nic nie zmieniono",Toast.LENGTH_SHORT).show();

            }
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
