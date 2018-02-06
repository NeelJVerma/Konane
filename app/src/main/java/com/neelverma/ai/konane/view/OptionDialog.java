/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.content.DialogInterface;
import android.view.View;

/**
 * Class to handle the option to move again dialog.
 * Created by Neel on 2/04/2018.
 *
 * It handles whether the user can move again or not in the form of a dialog.
 */

public class OptionDialog implements DialogInterface.OnClickListener {
   private BoardActivity boardActivity;
   boolean yesButton;

   /**
    * Description: Constructor. Will initialize the dialog box with an activity and a button choice.
    * Parameters: BoardActivity boardActivity, which is the activity to display this dialog on.
    *             boolean yesButton, which indicates whether or not "YES" was pressed.
    * Returns: Nothing.
    */

   OptionDialog(BoardActivity boardActivity, boolean yesButton) {
      this.boardActivity = boardActivity;
      this.yesButton = yesButton;
   }

   @Override
   public void onClick(DialogInterface dialog, int which) {
      if (yesButton) {
         boardActivity.gameObject.successiveMove = true;

         if (boardActivity.gameObject.playerWhite.isTurn()) {
            boardActivity.gameObject.playerWhite.setIsTurn(false);
            boardActivity.gameObject.playerBlack.setIsTurn(true);

            boardActivity.playerBlackTurn.setVisibility(View.VISIBLE);
            boardActivity.playerWhiteTurn.setVisibility(View.INVISIBLE);
         } else {
            boardActivity.gameObject.playerBlack.setIsTurn(false);
            boardActivity.gameObject.playerWhite.setIsTurn(true);

            boardActivity.playerWhiteTurn.setVisibility(View.VISIBLE);
            boardActivity.playerBlackTurn.setVisibility(View.INVISIBLE);
         }
      } else {
         boardActivity.gameObject.successiveMove = false;
      }
   }
}