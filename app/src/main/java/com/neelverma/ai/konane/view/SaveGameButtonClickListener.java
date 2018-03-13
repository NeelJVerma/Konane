/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.neelverma.ai.konane.model.Game;

import java.io.File;

/**
 * Class to handle an on click event for the save game button in the board activity. This is in its
 * own class to easily reference the static file path.
 * Created by Neel on 2/05/2018.
 */

public class SaveGameButtonClickListener implements View.OnClickListener {
   private BoardActivity boardActivity;
   private Game gameObject;
   private static String filePath;

   /**
    * Description: Constructor. Will initialize the listener with the activity on which the button
    * is clicked.
    * Parameters: BoardActivity boardActivity, which is the activity on which the button was pressed.
    * Returns: Nothing.
    */

   SaveGameButtonClickListener(BoardActivity boardActivity) {
      this.boardActivity = boardActivity;
      this.gameObject = boardActivity.getGameObject();
   }

   @Override
   public void onClick(View v) {
      Context context = v.getContext();

      if (gameObject.isSuccessiveMove()) {
         Toast.makeText(boardActivity, "FILE NOT SAVED. CAN'T SAVE MID-TURN.", Toast.LENGTH_SHORT).show();
      } else {
         int i = 0;
         File dir = new File(context.getFilesDir(), "saved_games");

         while (true) {
            File file = new File(dir.toString() + "/saved_game" + i + ".txt");

            if (!file.exists()) {
               break;
            }

            i++;
         }

         filePath = gameObject.saveGame("/saved_game" + i + ".txt", context);

         Intent mainIntent = new Intent(boardActivity, MainActivity.class);
         boardActivity.startActivity(mainIntent);
      }
   }

   /**
    * Description: Method to get the file path.
    * Parameters: None.
    * Returns: The file path.
    */

   public static String getFilePath() {
      return filePath;
   }

   public static void setFilePath(String filePath1) {
      filePath = filePath1;
   }

   /**
    * Description: Method to clear out the file path.
    * Parameters: None.
    * Returns: Nothing.
    */

   public static void deleteFilePath() {
      filePath = null;
   }
}