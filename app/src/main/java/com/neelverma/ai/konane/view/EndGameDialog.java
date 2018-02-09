/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.content.DialogInterface;
import android.content.Intent;

import com.neelverma.ai.konane.model.Game;

/**
 * Class to handle button click action for the end game alert dialog.
 * Created by Neel on 2/08/2018.
 *
 * Redirects to the end activity and sends over the scores of the two players.
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
      this.gameObject = boardActivity.gameObject;
   }

   @Override
   public void onClick(DialogInterface dialog, int which) {
      Intent endIntent = new Intent(boardActivity, EndActivity.class);

      endIntent.putExtra("playerBlackScore", gameObject.playerBlack.getScore());
      endIntent.putExtra("playerWhiteScore", gameObject.playerWhite.getScore());

      boardActivity.startActivity(endIntent);
   }
}
