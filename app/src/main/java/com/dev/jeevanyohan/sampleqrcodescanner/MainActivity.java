package com.dev.jeevanyohan.sampleqrcodescanner;

/*
github.com/jeevan-yohan-varghese
medium.com/@dev.jeevanyohan

 */

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    Button btnScan;
    TextView tvResult,tvToFrag;

    /*
    Checkout manifest.xml to see how the orientation is set
    See colors.xml to customize scanner colors
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScan=(Button)findViewById(R.id.btn_scan);
        tvResult=(TextView)findViewById(R.id.tv_result);
        tvToFrag=(TextView)findViewById(R.id.tv_to_frag);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checking whether camera permission is granted. (Optional)
                //Already there is a permission check with in the scanner activity of Zxing.
                //In case you need to handle the permission request in your way try this
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
                }else {
                    IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);


                    integrator.setOrientationLocked(false);
                    integrator.setPrompt("Scan QR code");
                    integrator.setBeepEnabled(false);//Use this to set whether you need a beep sound when the QR code is scanned

                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);


                    integrator.initiateScan();

                }

            }
        });


        //This is just for seeing the fragment activity.  (Not necessary)
        tvToFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toFragIntent=new Intent(MainActivity.this,ScannerFragmentActivity.class);
                startActivity(toFragIntent);
            }
        });
    }


    //Handling the results of the scan
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {

                //result.getContents() gives the scanned string
                tvResult.setText(result.getContents());

            }
        }
    }




    ////Handling Permission request (Optional)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(MainActivity.this, "Permission granted.  Try scanning now", Toast.LENGTH_SHORT).show();





            } else {

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Permission Denied");
                builder.setMessage("You cannot continue without granting permission");
                builder.setCancelable(false);

                builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                        dialog.dismiss();
                    }
                });


                builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(MainActivity.this, "Denied", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        System.out.println("Permission Denied");

                    }
                });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        }
    }
}