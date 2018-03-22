/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.neelverma.ai.konane.R;

/**
 * Class to handle button click action for the button to remove two slots and start the game.
 * Created by Neel on 2/08/2018.
 */

public class RemoveButtonClickListener implements View.OnClickListener {
   private BoardActivity boardActivity;
   private int row;
   private int col;

   /**
    * Description: Constructor. Will initialize the listener with the activity on which the button
    * is clicked.
    * Parameters: BoardActivity boardActivity, which is the activity on which the button was pressed.
    * Returns: Nothing.
    */

   RemoveButtonClickListener(BoardActivity boardActivity, int row, int col) {
      this.boardActivity = boardActivity;
      this.row = row;
      this.col = col;
   }

   @Override
   public void onClick(View v) {
      Button thisButton = (Button) v;
      thisButton.setVisibility(View.INVISIBLE);

      boardActivity.startGame();
      boardActivity.removeTwoSlots(row, col);
   }
}