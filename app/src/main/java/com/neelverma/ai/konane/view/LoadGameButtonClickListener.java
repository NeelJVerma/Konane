/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.neelverma.ai.konane.R;
import com.neelverma.ai.konane.model.Board;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
      Context context = v.getContext();

      TextView loadFileEditText = mainActivity.findViewById(R.id.loadFileEditText);

      int gameState;
      File loadFile = new File(context.getFilesDir() + "/saved_games/" + loadFileEditText.getText().toString());

      if (!loadFile.exists() || loadFile.isDirectory()) {
         Toast.makeText(mainActivity, "FILE DOESN'T EXIST.", Toast.LENGTH_SHORT).show();

         return;
      } else {
         gameState = BoardActivity.LOADED_GAME;

         setBoardSize(loadFile.toString());

         SaveGameButtonClickListener.setFilePath(loadFile.toString());
      }

      boardIntent.putExtra("gameType", gameState);
      mainActivity.startActivity(boardIntent);
   }

   /**
    * Description: Method to set the board size based on whatever file it is loading.
    * Parameters: String loadFile, which is the name of the loaded file.
    * Returns: Nothing.
    */

   private void setBoardSize(String loadFile) {
      try (BufferedReader bufferedReader = new BufferedReader(new FileReader(loadFile))) {
         int lineCounter = 0;

         while (bufferedReader.readLine() != null) {
            lineCounter++;
         }

         if (lineCounter == 10) {
            Board.MAX_ROW = 6;
            Board.MAX_COLUMN = 6;

            BoardActivity.MAX_ROW = 6;
            BoardActivity.MAX_COL = 6;
         } else if (lineCounter == 12) {
            Board.MAX_ROW = 8;
            Board.MAX_COLUMN = 8;

            BoardActivity.MAX_ROW = 8;
            BoardActivity.MAX_COL = 8;
         } else {
            Board.MAX_ROW = 10;
            Board.MAX_COLUMN = 10;

            BoardActivity.MAX_ROW = 10;
            BoardActivity.MAX_COL = 10;
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}