/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 3 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 3/27/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.content.DialogInterface;
import android.content.Intent;

import com.neelverma.ai.konane.model.Game;

/**
 * Class to handle button click action for the end game alert dialog.
 * Created by Neel on 2/08/2018.
 */

public class EndGameDialog implements DialogInterface.OnClickListener {
   private BoardActivity boardActivity;
   private Game gameObject;

   /**
    * Description: Constructor. Will initialize the listener with the activity on which the button
    * is clicked.
    * Parameters: BoardActivity boardActivity, which is the activity on which the button was pressed.
    * Returns: Nothing.
    */

   EndGameDialog(BoardActivity boardActivity) {
      this.boardActivity = boardActivity;
      this.gameObject = boardActivity.getGameObject();
   }

   @Override
   public void onClick(DialogInterface dialog, int which) {
      Intent endIntent = new Intent(boardActivity, EndActivity.class);

      endIntent.putExtra("playerBlackScore", gameObject.getPlayerBlack().getScore());
      endIntent.putExtra("playerWhiteScore", gameObject.getPlayerWhite().getScore());

      boardActivity.startActivity(endIntent);
   }
}
