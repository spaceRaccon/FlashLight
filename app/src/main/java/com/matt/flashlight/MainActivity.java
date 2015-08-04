package com.matt.flashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends ActionBarActivity {

    private Camera camera;
    private Camera.Parameters parameters;

    private ImageButton lightImgBtn;

    private boolean isFlashOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // lock screen rotation

        lightImgBtn = (ImageButton)findViewById(R.id.imgLightBtn);
        lightImgBtn.setOnClickListener(new FlashOnOffListener());
        
        if (isFlashSupported()){
            camera = Camera.open();
            parameters = camera.getParameters();
        } else showFlashAlert();
    }

    private void showFlashAlert() {
        new AlertDialog.Builder(this).setMessage("Your device hardware does not support this app")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("ERROR")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    public boolean isFlashSupported() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private class FlashOnOffListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (isFlashOn){
                lightImgBtn.setImageResource(R.drawable.switch_off);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF); // turn flash off
                camera.setParameters(parameters);
                camera.stopPreview();
                //lightBtn.setText(getResources().getString(R.string.btn_off));
                isFlashOn = false;
            } else {
                lightImgBtn.setImageResource(R.drawable.switch_on);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
                camera.startPreview();
                //lightBtn.setText(getResources().getString(R.string.btn_on));
                isFlashOn = true;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
