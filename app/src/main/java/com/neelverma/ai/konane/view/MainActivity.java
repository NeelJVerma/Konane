/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 3 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 3/27/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.neelverma.ai.konane.R;

import java.io.File;

/**
 * Class to hold the beginning screen of the app.
 * Created by Neel on 01/28/2018.
 */


public class MainActivity extends AppCompatActivity {
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
      int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

      String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

      if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_DENIED) {
         ActivityCompat.requestPermissions(this, permissions, 1);
      }

      Button beginButton = findViewById(R.id.beginButton);
      beginButton.setOnClickListener(new BeginButtonClickListener(MainActivity.this));

      Button loadGameButton = findViewById(R.id.loadGameButton);
      loadGameButton.setOnClickListener(new LoadGameButtonClickListener(MainActivity.this));
   }
}