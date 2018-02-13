/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import com.neelverma.ai.konane.model.Game;
import com.neelverma.ai.konane.model.Slot;

public class NextButtonClickListener implements View.OnClickListener {
   private BoardActivity boardActivity;
   private Game gameObject;

   /**
    * Description: Constructor. Will initialize the dialog box with an activity and a button choice.
    * Parameters: BoardActivity boardActivity, which is the activity to display this dialog on.
    * Returns: Nothing.
    */

   NextButtonClickListener(BoardActivity boardActivity) {
      this.boardActivity = boardActivity;
      this.gameObject = boardActivity.getGameObject();
   }

   @Override
   public void onClick(View v) {
      if (!gameObject.isFirstClick()) {
         Toast.makeText(boardActivity, "CAN'T FIND NEXT MOVE MID-TURN", Toast.LENGTH_SHORT).show();

         return;
      }

      gameObject.setTurnColor(gameObject.getPlayerWhite().isTurn() ? Slot.WHITE : Slot.BLACK);

      executeAlgorithm();
      executeMove();
   }

   /**
    * Description: Method to execute the move suggested by the algorithm.
    * Parameters: None.
    * Returns: Nothing.
    */

   public void executeMove() {
      gameObject.makeMove(gameObject.getSlotFrom(), gameObject.getSlotTo());

      drawMoveSlots();

      if (!switchTurns()) {
         return;
      }

      if (gameObject.getPlayerBlack().isTurn()) {
         boardActivity.getPlayerBlackTurn().setVisibility(View.VISIBLE);
         boardActivity.getPlayerWhiteTurn().setVisibility(View.INVISIBLE);
      } else {
         boardActivity.getPlayerBlackTurn().setVisibility(View.INVISIBLE);
         boardActivity.getPlayerWhiteTurn().setVisibility(View.VISIBLE);
      }

      if (!gameObject.playerCanMove(gameObject.getPlayerBlack()) && !gameObject.playerCanMove(gameObject.getPlayerWhite())) {
         displayEndGameDialog();
      }

      if (gameObject.canMoveAgain(gameObject.getSlotTo(), gameObject.getTurnColor())) {
         showOptionDialog("WOULD YOU LIKE TO CONTINUE YOUR TURN?");
      }
   }

   /**
    * Description: Method to display the end game dialog message to the user.
    * Parameters: None.
    * Returns: Nothing.
    */

   public void displayEndGameDialog() {
      boardActivity.getPlayerWhiteTurn().setVisibility(View.INVISIBLE);
      boardActivity.getPlayerBlackTurn().setVisibility(View.INVISIBLE);

      AlertDialog.Builder builder = new AlertDialog.Builder(boardActivity);

      builder.setMessage("GAME OVER. PRESS OK TO CONTINUE.")
         .setCancelable(false)
         .setPositiveButton("OK", new EndGameDialog(boardActivity));

      AlertDialog alert = builder.create();
      alert.show();
   }

   /**
    * Description: Method to draw, on the GUI, the appropriate slot images onto the slots involved in
    * a successful move.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void drawMoveSlots() {
      boardActivity.getGameBoard()[gameObject.getSlotFrom().getRow()][gameObject.getSlotFrom().getColumn()].setBackground(boardActivity.getDrawCell()[3]);
      String directionMoving;

      if (gameObject.getSlotFrom().getRow() == gameObject.getSlotTo().getRow()) {
         if (gameObject.getSlotFrom().getColumn() - gameObject.getSlotTo().getColumn() == -2) {
            directionMoving = "right";
         } else {
            directionMoving = "left";
         }
      } else {
         if (gameObject.getSlotFrom().getRow() - gameObject.getSlotTo().getRow() == -2) {
            directionMoving = "down";
         } else {
            directionMoving = "up";
         }
      }

      if (directionMoving.equals("right")) {
         boardActivity.getGameBoard()[gameObject.getSlotFrom().getRow()][gameObject.getSlotFrom().getColumn() + 1].setBackground(boardActivity.getDrawCell()[3]);
      } else if (directionMoving.equals("left")) {
         boardActivity.getGameBoard()[gameObject.getSlotFrom().getRow()][gameObject.getSlotFrom().getColumn() - 1].setBackground(boardActivity.getDrawCell()[3]);
      } else if (directionMoving.equals("down")) {
         boardActivity.getGameBoard()[gameObject.getSlotFrom().getRow() + 1][gameObject.getSlotFrom().getColumn()].setBackground(boardActivity.getDrawCell()[3]);
      } else if (directionMoving.equals("up")) {
         boardActivity.getGameBoard()[gameObject.getSlotFrom().getRow() - 1][gameObject.getSlotFrom().getColumn()].setBackground(boardActivity.getDrawCell()[3]);
      }

      Drawable draw;

      if (gameObject.getTurnColor() == Slot.WHITE) {
         draw = boardActivity.getDrawCell()[2];
      } else {
         draw = boardActivity.getDrawCell()[1];
      }

      boardActivity.getGameBoard()[gameObject.getSlotTo().getRow()][gameObject.getSlotTo().getColumn()].setBackground(draw);
   }

   /**
    * Description: Method to verify a turn switch for a current turn cycle.
    * Parameters: None.
    * Returns: Whether or not the turn switch was successful.
    */

   private boolean switchTurns() {
      if (gameObject.getPlayerBlack().isTurn()) {
         gameObject.getPlayerBlack().addToScore();

         String text = "BLACK: " + gameObject.getPlayerBlack().getScore();
         boardActivity.getPlayerBlackScore().setText(text.trim());

         gameObject.getPotentialSuccessiveSlot().setRow(gameObject.getSlotTo().getRow());
         gameObject.getPotentialSuccessiveSlot().setColumn(gameObject.getSlotTo().getColumn());

         if (!gameObject.playerCanMove(gameObject.getPlayerWhite()) && gameObject.playerCanMove(gameObject.getPlayerBlack())) {
            Toast.makeText(boardActivity, "WHITE CAN'T MOVE", Toast.LENGTH_SHORT).show();

            return false;
         }

         gameObject.getPlayerWhite().setIsTurn(true);
         gameObject.getPlayerBlack().setIsTurn(false);
      } else {
         gameObject.getPlayerWhite().addToScore();

         String text = "WHITE: " + gameObject.getPlayerWhite().getScore();
         boardActivity.getPlayerWhiteScore().setText(text.trim());

         gameObject.getPotentialSuccessiveSlot().setRow(gameObject.getSlotTo().getRow());
         gameObject.getPotentialSuccessiveSlot().setColumn(gameObject.getSlotTo().getColumn());

         if (!gameObject.playerCanMove(gameObject.getPlayerBlack()) && gameObject.playerCanMove(gameObject.getPlayerWhite())) {
            Toast.makeText(boardActivity, "BLACK CAN'T MOVE", Toast.LENGTH_SHORT).show();

            return false;
         }

         gameObject.getPlayerWhite().setIsTurn(false);
         gameObject.getPlayerBlack().setIsTurn(true);
      }

      return true;
   }

   /**
    * Description: Method to display a dialog message asking whether the user wants to move again.
    * Parameters: String alertMessage, which is the message to set the alert with.
    * Returns: Nothing.
    */

   private void showOptionDialog(String alertMessage) {
      AlertDialog.Builder builder = new AlertDialog.Builder(boardActivity);

      builder.setMessage(alertMessage)
         .setCancelable(false)
         .setPositiveButton("YES", new OptionDialog(boardActivity, true))
         .setNegativeButton("NO", new OptionDialog(boardActivity, false));

      AlertDialog alert = builder.create();
      alert.show();
   }

   /**
    * Description: Method to execute the chosen algorithm as given from the spinner.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void executeAlgorithm() {
      switch (AlgorithmSpinnerItemSelectedListener.getAlgorithmType()) {
         case AlgorithmSpinnerItemSelectedListener.DEPTH_FIRST:
            gameObject.depthFirstSearch();
            break;
         case AlgorithmSpinnerItemSelectedListener.BREADTH_FIRST:
            gameObject.breadthFirstSearch();
            break;
         case AlgorithmSpinnerItemSelectedListener.BEST_FIRST:
            gameObject.bestFirstSearch(); // TODO: IMPLEMENT
            return;
         case AlgorithmSpinnerItemSelectedListener.BRANCH_AND_BOUND:
            gameObject.branchAndBound(); // TODO: IMPLEMENT
            return;
      }
   }
}