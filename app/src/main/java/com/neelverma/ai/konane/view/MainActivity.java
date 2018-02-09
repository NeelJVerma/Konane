/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

      beginButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            SaveGameClickListener.deleteFilePath();
            Intent boardIntent = new Intent(MainActivity.this,
               BoardActivity.class);
            boardIntent.putExtra("gameType", BoardActivity.NEW_GAME);
            startActivity(boardIntent);
         }
      });

      Button loadGameButton = findViewById(R.id.loadGameButton);

      loadGameButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            boolean hasSaveFile = true;
            Intent boardIntent = new Intent(MainActivity.this,
               BoardActivity.class);

            int gameState = SaveGameClickListener.getFilePath() == null ? BoardActivity.NEW_GAME : BoardActivity.LOADED_GAME;

            if (gameState == BoardActivity.NEW_GAME) {
               hasSaveFile = false;

               if (!hasSaveFile) {
                  AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                  builder.setMessage("NO SAVE FILES EXIST. START A NEW GAME.")
                     .setCancelable(false)
                     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                           return;
                        }
                     });

                  AlertDialog alert = builder.create();
                  alert.show();
               }
            }

            boardIntent.putExtra("gameType", gameState);
            startActivity(boardIntent);
         }
      });
   }
}