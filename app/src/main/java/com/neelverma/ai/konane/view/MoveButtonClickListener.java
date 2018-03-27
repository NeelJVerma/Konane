/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 3 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 3/27/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.neelverma.ai.konane.R;
import com.neelverma.ai.konane.model.Game;
import com.neelverma.ai.konane.model.MoveNode;
import com.neelverma.ai.konane.model.Slot;

/**
 * Class to handle button click action for the move button.
 * Created by Neel on 3/15/2018.
 */

public class MoveButtonClickListener implements View.OnClickListener {
   private BoardActivity boardActivity;
   private Game gameObject;

   /**
    * Description: Constructor. Will initialize the listener with the activity on which the button
    * is clicked.
    * Parameters: BoardActivity boardActivity, which is the activity on which the button was pressed.
    * Returns: Nothing.
    */

   MoveButtonClickListener(BoardActivity boardActivity) {
      this.boardActivity = boardActivity;
      this.gameObject = boardActivity.getGameObject();
   }

   @Override
   public void onClick(View v) {
      boardActivity.stopScoreAnimation();

      if (gameObject.getBestMove() != null) {
         boardActivity.stopMoveAnimation(gameObject.getBestMove());
      }

      gameObject.setTurnColor((gameObject.getPlayerWhite().isTurn() ? Slot.WHITE : Slot.BLACK));

      if ((gameObject.getPlayerBlack().isTurn() && !gameObject.getPlayerBlack().isComputer()) ||
         (gameObject.getPlayerWhite().isTurn() && !gameObject.getPlayerWhite().isComputer())) {
         Toast.makeText(boardActivity, "CAN ONLY MAKE COMPUTER MOVE", Toast.LENGTH_SHORT).show();

         return;
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

      if (gameObject.isFirstClickCompMove()) {
         double startTime = System.nanoTime();

         if (gameObject.getPlayerBlack().isComputer()) {
            gameObject.callMinimax(gameObject.getPlayerBlack());
         } else {
            gameObject.callMinimax(gameObject.getPlayerWhite());
         }

         double endTime = System.nanoTime();

         Toast.makeText(boardActivity, "MINIMAX TOOK " + (endTime - startTime) + "ms TO RUN", Toast.LENGTH_LONG).show();
      }

      MoveNode moveNode = gameObject.getMinimaxMove();

      gameObject.makeMoveFromMinimax(moveNode);
      boardActivity.reDrawBoard();

      reDrawGuiElements();

      if (!gameObject.playerCanMove(gameObject.getPlayerWhite()) && !gameObject.playerCanMove(gameObject.getPlayerBlack())) {
         boardActivity.getPlayerWhiteTurn().setVisibility(View.INVISIBLE);
         boardActivity.getPlayerBlackTurn().setVisibility(View.INVISIBLE);
         displayEndGameDialog();
      }

      gameObject.setFirstClickCompMove(true);
   }

   /**
    * Description: Method to redraw everything on the GUI.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void reDrawGuiElements() {
      if (gameObject.getTurnColor() == Slot.BLACK) {
         gameObject.getPlayerBlack().addToScore(gameObject.getBestMove().getScore());
      } else {
         gameObject.getPlayerWhite().addToScore(gameObject.getBestMove().getScore());
      }

      handleTurns();

      boardActivity.reDrawScores();
      boardActivity.reDrawTurns();
   }

   /**
    * Description: Method to handle the turn switch or not switch.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void handleTurns() {
      if (gameObject.getPlayerBlack().isTurn() && !gameObject.playerCanMove(gameObject.getPlayerWhite())) {
         gameObject.setTurnColor(Slot.BLACK);
      } else if (gameObject.getPlayerWhite().isTurn() && !gameObject.playerCanMove(gameObject.getPlayerBlack())) {
         gameObject.setTurnColor(Slot.WHITE);
      } else {
         gameObject.setTurnColor((gameObject.getPlayerWhite().isTurn() ? Slot.BLACK : Slot.WHITE));
      }

      if (gameObject.getTurnColor() == Slot.WHITE) {
         gameObject.getPlayerWhite().setIsTurn(true);
         gameObject.getPlayerBlack().setIsTurn(false);
      } else {
         gameObject.getPlayerWhite().setIsTurn(false);
         gameObject.getPlayerBlack().setIsTurn(true);
      }
   }

   /**
    * Description: Method to display the end game dialog message to the user.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void displayEndGameDialog() {
      AlertDialog.Builder builder = new AlertDialog.Builder(boardActivity);

      builder.setMessage("GAME OVER. PRESS OK TO CONTINUE.")
         .setCancelable(false)
         .setPositiveButton("OK", new EndGameDialog(boardActivity));

      AlertDialog alert = builder.create();
      alert.show();
   }
}