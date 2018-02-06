/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.neelverma.ai.konane.model.Game;
import com.neelverma.ai.konane.model.Slot;

/**
 * Class to handle an on click event for a specific button in the game board.
 * Created by Neel on 2/04/2018.
 *
 * It handles all click logic for the game board, as well as all GUI logic.
 */

public class GameBoardClickListener implements View.OnClickListener {
   private BoardActivity boardActivity;
   private int currentRow;
   private int currentCol;
   private Game gameObject;
   private Context context;

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
      this.gameObject = boardActivity.gameObject;
   }

   @Override
   public void onClick(View v) {
      context = v.getContext();

      gameObject.turnColor = gameObject.playerWhite.isTurn() ? Slot.WHITE : Slot.BLACK;

      if (gameObject.firstClick) {
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
      gameObject.slotFrom = gameObject.boardObject.getSlot(currentRow, currentCol);

      if (gameObject.slotFrom.getColor() != gameObject.turnColor) {
         if (gameObject.slotFrom.getColor() == Slot.EMPTY) {
            displayDialog("YOU CAN'T MOVE AN EMPTY SLOT", "passive");
         } else {
            displayDialog("NOT YOUR TURN", "passive");
         }

         return;
      }

      if (gameObject.successiveMove) {
         if ((gameObject.canMoveAgain(gameObject.potentialSuccessiveSlot, gameObject.turnColor)) &&
            (!gameObject.verifySuccessiveMove(gameObject.slotFrom, gameObject.potentialSuccessiveSlot))) {
            displayDialog("YOU MUST START FROM THE POSITION YOU ENDED ON", "passive");
            return;
         }
      }

      if (!drawPotentialMoves(gameObject.turnColor, boardActivity.drawCell[0])) {
         displayDialog("THIS PIECE CAN'T MOVE", "passive");

         return;
      }

      gameObject.firstClick = false;
   }

   /**
    * Description: Method to handle all second click functionality of the game.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void processSecondClick() {
      gameObject.firstClick = true;
      gameObject.potentialSuccessiveSlot.setRow(currentRow);
      gameObject.potentialSuccessiveSlot.setColumn(currentCol);

      drawPotentialMoves(gameObject.turnColor, boardActivity.drawCell[3]);

      gameObject.slotTo = gameObject.boardObject.getSlot(currentRow, currentCol);

      if (!gameObject.makeMove(gameObject.slotFrom, gameObject.slotTo)) {
         displayDialog("INVALID MOVE", "passive");

         return;
      }

      drawMoveSlots();

      if (!switchTurns()) {
         return;
      }

      if (gameObject.playerBlack.isTurn()) {
         boardActivity.playerBlackTurn.setVisibility(View.VISIBLE);
         boardActivity.playerWhiteTurn.setVisibility(View.INVISIBLE);
      } else {
         boardActivity.playerBlackTurn.setVisibility(View.INVISIBLE);
         boardActivity.playerWhiteTurn.setVisibility(View.VISIBLE);
      }

      if (!gameObject.playerCanMove(gameObject.playerWhite) && !gameObject.playerCanMove(gameObject.playerBlack)) {
         displayDialog("GAME OVER. PRESS OK TO SEE THE RESULTS.", "end");
      }

      if (gameObject.canMoveAgain(gameObject.slotTo, gameObject.turnColor)) {
         showOptionDialog("WOULD YOU LIKE TO CONTINUE YOUR TURN?");
      }
   }

   /**
    * Description: Method to verify a turn switch for a current turn cycle.
    * Parameters: None.
    * Returns: Whether or not the turn switch was successful.
    */

   private boolean switchTurns() {
      if (gameObject.playerBlack.isTurn()) {
         gameObject.playerBlack.addToScore();
         String text = "BLACK: " + boardActivity.gameObject.playerBlack.getScore();
         boardActivity.playerBlackScore.setText(text.trim());

         if (!gameObject.playerCanMove(gameObject.playerWhite) && gameObject.playerCanMove(gameObject.playerBlack)) {
            displayDialog("WHITE CAN'T MOVE", "passive");

            return false;
         }

         gameObject.playerWhite.setIsTurn(true);
         gameObject.playerBlack.setIsTurn(false);

         return true;
      } else {
         gameObject.playerWhite.addToScore();
         String text = "WHITE: " + boardActivity.gameObject.playerWhite.getScore();
         boardActivity.playerWhiteScore.setText(text.trim());

         if (!gameObject.playerCanMove(gameObject.playerBlack) && gameObject.playerCanMove(gameObject.playerWhite)) {
            displayDialog("BLACK CAN'T MOVE", "passive");

            return false;
         }

         gameObject.playerWhite.setIsTurn(false);
         gameObject.playerBlack.setIsTurn(true);

         return true;
      }
   }

   /**
    * Description: Method to display a dialog message to the user.
    * Parameters: String alertMessage, which is the message to set the alert with.
    *             String type, which is the type of alert. It can only be "passive" or "end". Passive
    *             denotes a dialog in which the game does not end, and end denotes a dialog to handle
    *             the end game event.
    * Returns: Nothing.
    */

   private void displayDialog(String alertMessage, final String type) {
      AlertDialog.Builder builder = new AlertDialog.Builder(boardActivity);

      builder.setMessage(alertMessage)
         .setCancelable(false)
         .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
               if (type == "end") {
                  Intent endIntent = new Intent(boardActivity,
                     EndActivity.class);
                  endIntent.putExtra("playerBlackScore", gameObject.playerBlack.getScore());
                  endIntent.putExtra("playerWhiteScore", gameObject.playerWhite.getScore());

                  context.startActivity(endIntent);
               }
            }
         });

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
      boardActivity.gameBoard[gameObject.slotFrom.getRow()][gameObject.slotFrom.getColumn()].setBackground(boardActivity.drawCell[3]);
      String directionMoving;

      if (gameObject.slotFrom.getRow() == gameObject.slotTo.getRow()) {
         if (gameObject.slotFrom.getColumn() - gameObject.slotTo.getColumn() == -2) {
            directionMoving = "right";
         } else {
            directionMoving = "left";
         }
      } else {
         if (gameObject.slotFrom.getRow() - gameObject.slotTo.getRow() == -2) {
            directionMoving = "down";
         } else {
            directionMoving = "up";
         }
      }

      if (directionMoving == "right") {
         boardActivity.gameBoard[gameObject.slotFrom.getRow()][gameObject.slotFrom.getColumn() + 1].setBackground(boardActivity.drawCell[3]);
      } else if (directionMoving == "left") {
         boardActivity.gameBoard[gameObject.slotFrom.getRow()][gameObject.slotFrom.getColumn() - 1].setBackground(boardActivity.drawCell[3]);
      } else if (directionMoving == "down") {
         boardActivity.gameBoard[gameObject.slotFrom.getRow() + 1][gameObject.slotFrom.getColumn()].setBackground(boardActivity.drawCell[3]);
      } else if (directionMoving == "up") {
         boardActivity.gameBoard[gameObject.slotFrom.getRow() - 1][gameObject.slotFrom.getColumn()].setBackground(boardActivity.drawCell[3]);
      }

      Drawable draw;

      if (gameObject.boardObject.getSlot(gameObject.slotTo.getRow(), gameObject.slotTo.getColumn()).getColor() == Slot.WHITE) {
         draw = boardActivity.drawCell[2];
      } else {
         draw = boardActivity.drawCell[1];
      }

      boardActivity.gameBoard[gameObject.slotTo.getRow()][gameObject.slotTo.getColumn()].setBackground(draw);
   }

   /**
    * Description: Method to draw, on the GUI, all potential moves a user has from a given position.
    * Parameters: int turnColor, which is the current player's piece color.
    *             Drawable drawCell, which is the image to use for drawing. This is a parameter because
    *             we also have to handle un-drawing potential move slots, so a different image is used.
    * Returns: Whether or not the current piece has any potential spots to move.
    */

   private boolean drawPotentialMoves(int turnColor, Drawable drawCell) {
      Slot slotRight = gameObject.boardObject.getSlot(gameObject.slotFrom.getRow(), gameObject.slotFrom.getColumn() + 2);
      Slot slotLeft = gameObject.boardObject.getSlot(gameObject.slotFrom.getRow(), gameObject.slotFrom.getColumn() - 2);
      Slot slotUp = gameObject.boardObject.getSlot(gameObject.slotFrom.getRow() + 2, gameObject.slotFrom.getColumn());
      Slot slotDown = gameObject.boardObject.getSlot(gameObject.slotFrom.getRow() - 2, gameObject.slotFrom.getColumn());
      boolean pieceCanMove = false;

      if (gameObject.isValidMove(gameObject.slotFrom, slotRight, turnColor)) {
         boardActivity.gameBoard[slotRight.getRow()][slotRight.getColumn()].setBackground(drawCell);
         pieceCanMove = true;
      }

      if (gameObject.isValidMove(gameObject.slotFrom, slotLeft, turnColor)) {
         boardActivity.gameBoard[slotLeft.getRow()][slotLeft.getColumn()].setBackground(drawCell);
         pieceCanMove = true;
      }

      if (gameObject.isValidMove(gameObject.slotFrom, slotUp, turnColor)) {
         boardActivity.gameBoard[slotUp.getRow()][slotUp.getColumn()].setBackground(drawCell);
         pieceCanMove = true;
      }

      if (gameObject.isValidMove(gameObject.slotFrom, slotDown, turnColor)) {
         boardActivity.gameBoard[slotDown.getRow()][slotDown.getColumn()].setBackground(drawCell);
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