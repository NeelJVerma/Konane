/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.neelverma.ai.konane.R;

/**
 * Class to hold the beginning screen of the app.
 * Created by Neel on 01/28/2018.
 *
 * It displays the game name and has one button.
 * When this button is clicked, the app will redirect to BoardActivity, where the game will
 * begin.
 */


public class MainActivity extends AppCompatActivity {
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      Button beginButton = findViewById(R.id.beginButton);
      beginButton.setOnClickListener(new BeginButtonClickListener(MainActivity.this));

      Button loadGameButton = findViewById(R.id.loadGameButton);
      loadGameButton.setOnClickListener(new LoadGameButtonClickListener(MainActivity.this));
   }
}