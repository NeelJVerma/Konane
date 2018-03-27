/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 3 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 3/27/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.neelverma.ai.konane.R;
import com.neelverma.ai.konane.model.Game;
import com.neelverma.ai.konane.model.MoveNode;

/**
 * Class to handle button click action for the hint button.
 * Created by Neel on 3/15/2018.
 */

public class HintButtonClickListener implements View.OnClickListener {
   private BoardActivity boardActivity;
   private Game gameObject;

   /**
    * Description: Constructor. Will initialize the listener with the activity on which the button
    * is clicked.
    * Parameters: BoardActivity boardActivity, which is the activity on which the button was pressed.
    * Returns: Nothing.
    */

   HintButtonClickListener(BoardActivity boardActivity) {
      this.boardActivity = boardActivity;
      this.gameObject = boardActivity.getGameObject();
   }

   @Override
   public void onClick(View v) {
      boardActivity.stopScoreAnimation();

      if (gameObject.getBestMove() != null) {
         boardActivity.stopMoveAnimation(gameObject.getBestMove());
      }

      TextView plyCutoffEditText = boardActivity.findViewById(R.id.plyCutoffEditText);
      int plyCutoff;

      if (plyCutoffEditText.getText().toString().isEmpty()) {
         plyCutoff = 1;
      } else {
         plyCutoff = Integer.parseInt(plyCutoffEditText.getText().toString());
      }

      gameObject.setPlyCutoff(plyCutoff);

      CheckBox alphaBetaCheckBox = boardActivity.findViewById(R.id.alphaBetaCheckBox);
      alphaBetaCheckBox.setVisibility(View.VISIBLE);

      if (alphaBetaCheckBox.isChecked()) {
         gameObject.setAlphaBetaEnable(true);
      } else {
         gameObject.setAlphaBetaEnable(false);
      }

      long startTime = System.currentTimeMillis();

      if (gameObject.getPlayerBlack().isTurn()) {
         gameObject.callMinimax(gameObject.getPlayerBlack());
      } else {
         gameObject.callMinimax(gameObject.getPlayerWhite());
      }

      long endTime = System.currentTimeMillis();

      Toast.makeText(boardActivity, "MINIMAX TOOK " + (endTime - startTime) + "ms TO RUN", Toast.LENGTH_LONG).show();

      MoveNode moveNode = gameObject.getMinimaxMove();

      boardActivity.showMoveFromMinimax(moveNode);
      boardActivity.showScoresFromMinimax(moveNode);
   }
}