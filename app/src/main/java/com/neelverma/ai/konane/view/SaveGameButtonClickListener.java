/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.content.Context;
import android.view.View;

import com.neelverma.ai.konane.model.Game;

/**
 * Class to handle an on click event for the save game button in the board activity. This is in its
 * own class to easily reference the static file path.
 * Created by Neel on 2/05/2018.
 *
 * It handles saving a game. The current game object will then retain its state.
 */

public class SaveGameButtonClickListener implements View.OnClickListener {
   private BoardActivity boardActivity;
   private Game gameObject;
   private Context context;
   private static String filePath;

   /**
    * Description: Constructor. Will initialize the listener with the activity on which the button
    * is clicked.
    * Parameters: BoardActivity boardActivity, which is the activity on which the button was pressed.
    * Returns: Nothing.
    */

   SaveGameButtonClickListener(BoardActivity boardActivity) {
      this.boardActivity = boardActivity;
      this.gameObject = boardActivity.gameObject;
   }

   @Override
   public void onClick(View v) {
      context = v.getContext();
      filePath = gameObject.saveGame("saved_game.txt", context);
   }

   /**
    * Description: Method to get the file path.
    * Parameters: None.
    * Returns: The file path.
    */

   public static String getFilePath() {
      return filePath;
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