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

/**
 * Class to handle an on click event for a specific button in the game board.
 * Created by Neel on 2/04/2018.
 */

public class GameBoardClickListener implements View.OnClickListener {
   private BoardActivity boardActivity;
   private int currentRow;
   private int currentCol;
   private Game gameObject;

   /**
    * Description: Constructor. Will initialize the listener with a row, column, and an instance of
    * a board activity so that this class will have access to all board activity features.
    * Parameters: int currentRow, to set the current row.
    *             int currentCol, to set the current column.
    *             BoardActivity boardActivity, to provide access for all board activity features.
    * Returns: Nothing.
    */

   GameBoardClickListener(int currentRow, int currentCol, BoardActivity boardActivity) {
      this.boardActivity = boardActivity;
      this.currentRow = currentRow;
      this.currentCol = currentCol;
      this.gameObject = boardActivity.getGameObject();
   }

   @Override
   public void onClick(View v) {
      gameObject.setTurnColor(gameObject.getPlayerWhite().isTurn() ? Slot.WHITE : Slot.BLACK);

      if (gameObject.isFirstClick()) {
         processFirstClick();
      } else {
         processSecondClick();
      }
   }

   /**
    * Description: Method to handle all first click functionality of the game.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void processFirstClick() {
      gameObject.setSlotFrom(gameObject.getBoardObject().getSlot(currentRow, currentCol));

      if (gameObject.getSlotFrom().getColor() != gameObject.getTurnColor()) {
         if (gameObject.getSlotFrom().getColor() == Slot.EMPTY) {
            Toast.makeText(boardActivity, "YOU CAN'T MOVE AN EMPTY SLOT", Toast.LENGTH_SHORT).show();
         } else {
            Toast.makeText(boardActivity, "NOT YOUR TURN", Toast.LENGTH_SHORT).show();
         }

         return;
      }

      if (gameObject.isSuccessiveMove()) {
         if ((gameObject.canMoveAgain(gameObject.getPotentialSuccessiveSlot(), gameObject.getTurnColor())) &&
            (!gameObject.verifySuccessiveMove(gameObject.getSlotFrom(), gameObject.getPotentialSuccessiveSlot()))) {
            Toast.makeText(boardActivity, "YOU MUST START FROM THE POSITION YOU ENDED ON", Toast.LENGTH_SHORT).show();

            return;
         }
      }

      if (!drawPotentialMoves(gameObject.getTurnColor(), boardActivity.getDrawCell()[0])) {
         Toast.makeText(boardActivity, "THIS PIECE CAN'T MOVE", Toast.LENGTH_SHORT).show();

         return;
      }

      gameObject.setFirstClick(false);
   }

   /**
    * Description: Method to handle all second click functionality of the game.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void processSecondClick() {
      gameObject.setFirstClick(true);

      drawPotentialMoves(gameObject.getTurnColor(), boardActivity.getDrawCell()[3]);

      gameObject.setSlotTo(gameObject.getBoardObject().getSlot(currentRow, currentCol));

      if (!gameObject.makeMove(gameObject.getSlotFrom(), gameObject.getSlotTo())) {
         Toast.makeText(boardActivity, "INVALID MOVE", Toast.LENGTH_SHORT).show();

         return;
      }

      drawMoveSlots();

      if (!switchTurns()) {
         return;
      }

      gameObject.setSuccessiveMove(false);

      if (gameObject.getPlayerBlack().isTurn()) {
         boardActivity.getPlayerBlackTurn().setVisibility(View.VISIBLE);
         boardActivity.getPlayerWhiteTurn().setVisibility(View.INVISIBLE);
      } else {
         boardActivity.getPlayerBlackTurn().setVisibility(View.INVISIBLE);
         boardActivity.getPlayerWhiteTurn().setVisibility(View.VISIBLE);
      }

      if (!gameObject.playerCanMove(gameObject.getPlayerWhite()) && !gameObject.playerCanMove(gameObject.getPlayerBlack())) {
         boardActivity.getPlayerWhiteTurn().setVisibility(View.INVISIBLE);
         boardActivity.getPlayerBlackTurn().setVisibility(View.INVISIBLE);
         displayEndGameDialog();
      }

      if (gameObject.canMoveAgain(gameObject.getSlotTo(), gameObject.getTurnColor())) {
         showOptionDialog("WOULD YOU LIKE TO CONTINUE YOUR TURN?");
      }
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

         gameObject.getPotentialSuccessiveSlot().setRow(currentRow);
         gameObject.getPotentialSuccessiveSlot().setColumn(currentCol);

         if (!gameObject.playerCanMove(gameObject.getPlayerWhite()) && gameObject.playerCanMove(gameObject.getPlayerBlack())) {
            Toast.makeText(boardActivity, "WHITE CAN'T MOVE", Toast.LENGTH_SHORT).show();
            gameObject.setSuccessiveMove(false);

            if (gameObject.canMoveAgain(gameObject.getPotentialSuccessiveSlot(), gameObject.getTurnColor())) {
               gameObject.setSuccessiveMove(true);
            }

            return false;
         }

         gameObject.getPlayerWhite().setIsTurn(true);
         gameObject.getPlayerBlack().setIsTurn(false);

         return true;
      } else {
         gameObject.getPlayerWhite().addToScore();

         String text = "WHITE: " + gameObject.getPlayerWhite().getScore();
         boardActivity.getPlayerWhiteScore().setText(text.trim());

         gameObject.getPotentialSuccessiveSlot().setRow(currentRow);
         gameObject.getPotentialSuccessiveSlot().setColumn(currentCol);

         if (!gameObject.playerCanMove(gameObject.getPlayerBlack()) && gameObject.playerCanMove(gameObject.getPlayerWhite())) {
            Toast.makeText(boardActivity, "BLACK CAN'T MOVE", Toast.LENGTH_SHORT).show();
            gameObject.setSuccessiveMove(false);

            if (gameObject.canMoveAgain(gameObject.getPotentialSuccessiveSlot(), gameObject.getTurnColor())) {
               gameObject.setSuccessiveMove(true);
            }

            return false;
         }

         gameObject.getPlayerWhite().setIsTurn(false);
         gameObject.getPlayerBlack().setIsTurn(true);

         return true;
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

      if (gameObject.getBoardObject().getSlot(gameObject.getSlotTo().getRow(), gameObject.getSlotTo().getColumn()).getColor() == Slot.WHITE) {
         draw = boardActivity.getDrawCell()[2];
      } else {
         draw = boardActivity.getDrawCell()[1];
      }

      boardActivity.getGameBoard()[gameObject.getSlotTo().getRow()][gameObject.getSlotTo().getColumn()].setBackground(draw);
   }

   /**
    * Description: Method to draw, on the GUI, all potential moves a user has from a given position.
    * Parameters: int turnColor, which is the current player's piece color.
    *             Drawable drawCell, which is the image to use for drawing. This is a parameter because
    *             we also have to handle un-drawing potential move slots, so a different image is used.
    * Returns: Whether or not the current piece has any potential spots to move.
    */

   private boolean drawPotentialMoves(int turnColor, Drawable drawCell) {
      Slot slotRight = gameObject.getBoardObject().getSlot(gameObject.getSlotFrom().getRow(), gameObject.getSlotFrom().getColumn() + 2);
      Slot slotLeft = gameObject.getBoardObject().getSlot(gameObject.getSlotFrom().getRow(), gameObject.getSlotFrom().getColumn() - 2);
      Slot slotUp = gameObject.getBoardObject().getSlot(gameObject.getSlotFrom().getRow() - 2, gameObject.getSlotFrom().getColumn());
      Slot slotDown = gameObject.getBoardObject().getSlot(gameObject.getSlotFrom().getRow() + 2, gameObject.getSlotFrom().getColumn());
      boolean pieceCanMove = false;

      if (gameObject.isValidMove(gameObject.getSlotFrom(), slotRight, turnColor)) {
         boardActivity.getGameBoard()[slotRight.getRow()][slotRight.getColumn()].setBackground(drawCell);
         pieceCanMove = true;
      }

      if (gameObject.isValidMove(gameObject.getSlotFrom(), slotLeft, turnColor)) {
         boardActivity.getGameBoard()[slotLeft.getRow()][slotLeft.getColumn()].setBackground(drawCell);
         pieceCanMove = true;
      }

      if (gameObject.isValidMove(gameObject.getSlotFrom(), slotUp, turnColor)) {
         boardActivity.getGameBoard()[slotUp.getRow()][slotUp.getColumn()].setBackground(drawCell);
         pieceCanMove = true;
      }

      if (gameObject.isValidMove(gameObject.getSlotFrom(), slotDown, turnColor)) {
         boardActivity.getGameBoard()[slotDown.getRow()][slotDown.getColumn()].setBackground(drawCell);
         pieceCanMove = true;
      }

      return pieceCanMove;
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
}