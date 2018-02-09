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
 * Class to handle button click action for the begin game button.
 * Created by Neel on 2/08/2018.
 */

public class BeginButtonClickListener implements View.OnClickListener {
   private MainActivity mainActivity;

   /**
    * Description: Constructor. Will initialize the listener with the activity on which the button
    * is clicked.
    * Parameters: MainActivity mainActivity, which is the activity on which the button was pressed.
    * Returns: Nothing.
    */

   BeginButtonClickListener(MainActivity mainActivity) {
      this.mainActivity = mainActivity;
   }

   @Override
   public void onClick(View v) {
      SaveGameButtonClickListener.deleteFilePath();

      Intent boardIntent = new Intent(mainActivity, BoardActivity.class);
      boardIntent.putExtra("gameType", BoardActivity.NEW_GAME);
      mainActivity.startActivity(boardIntent);
   }
}
