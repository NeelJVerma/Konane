/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.content.Intent;
import android.view.View;

/**
 * Class to handle button click action for the play again button.
 * Created by Neel on 2/08/2018.
 *
 * Redirects to the starting page of the app.
 */

public class PlayAgainButtonClickListener implements View.OnClickListener {
   private EndActivity endActivity;

   /**
    * Description: Constructor. Will initialize the listener with the activity on which the button
    * is clicked.
    * Parameters: EndActivity endActivity, which is the activity on which the button was pressed.
    * Returns: Nothing.
    */

   PlayAgainButtonClickListener(EndActivity endActivity) {
      this.endActivity = endActivity;
   }

   @Override
   public void onClick(View v) {
      SaveGameButtonClickListener.deleteFilePath();

      Intent boardIntent = new Intent(endActivity, MainActivity.class);
      endActivity.startActivity(boardIntent);
   }
}