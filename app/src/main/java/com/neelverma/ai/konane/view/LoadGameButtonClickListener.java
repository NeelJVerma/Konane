/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.neelverma.ai.konane.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class to handle button click action for the load game button.
 * Created by Neel on 2/08/2018.
 */

public class LoadGameButtonClickListener implements View.OnClickListener {
   private MainActivity mainActivity;

   /**
    * Description: Constructor. Will initialize the listener with the activity on which the button
    * is clicked.
    * Parameters: MainActivity mainActivity, which is the activity on which the button was pressed.
    * Returns: Nothing.
    */

   LoadGameButtonClickListener(MainActivity mainActivity) {
      this.mainActivity = mainActivity;
   }

   @Override
   public void onClick(View v) {
      Intent boardIntent = new Intent(mainActivity, BoardActivity.class);

      int gameState = BoardActivity.LOADED_GAME;

      boardIntent.putExtra("gameType", gameState);
      mainActivity.startActivity(boardIntent);
   }
}