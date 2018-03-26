/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 3 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 3/27/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.content.DialogInterface;
import android.view.View;

import com.neelverma.ai.konane.model.Game;

/**
 * Class to handle the option to move again dialog.
 * Created by Neel on 2/04/2018.
 */

public class OptionDialog implements DialogInterface.OnClickListener {
   private BoardActivity boardActivity;
   private Game gameObject;
   private boolean yesButton;

   /**
    * Description: Constructor. Will initialize the dialog box with an activity and a button choice.
    * Parameters: BoardActivity boardActivity, which is the activity to display this dialog on.
    *             boolean yesButton, which indicates whether or not "YES" was pressed.
    * Returns: Nothing.
    */

   OptionDialog(BoardActivity boardActivity, boolean yesButton) {
      this.boardActivity = boardActivity;
      this.yesButton = yesButton;
      this.gameObject = boardActivity.getGameObject();
   }

   @Override
   public void onClick(DialogInterface dialog, int which) {
      if (yesButton) {
         gameObject.setSuccessiveMove(true);

         if (gameObject.getPlayerWhite().isTurn()) {
            gameObject.getPlayerWhite().setIsTurn(false);
            gameObject.getPlayerBlack().setIsTurn(true);

            boardActivity.getPlayerBlackTurn().setVisibility(View.VISIBLE);
            boardActivity.getPlayerWhiteTurn().setVisibility(View.INVISIBLE);
         } else {
            gameObject.getPlayerBlack().setIsTurn(false);
            gameObject.getPlayerWhite().setIsTurn(true);

            boardActivity.getPlayerWhiteTurn().setVisibility(View.VISIBLE);
            boardActivity.getPlayerBlackTurn().setVisibility(View.INVISIBLE);
         }
      } else {
         gameObject.setSuccessiveMove(false);
      }
   }
}