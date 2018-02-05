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
   }

   @Override
   public void onClick(View v) {
      context = v.getContext();
      int turnColor = boardActivity.gameObject.playerWhite.isTurn() ? Slot.WHITE : Slot.BLACK;

      if (boardActivity.gameObject.firstClick) {
         boardActivity.gameObject.rowFrom = currentRow;
         boardActivity.gameObject.columnFrom = currentCol;
         boardActivity.gameObject.slotFrom = boardActivity.gameObject.boardObject.getSlot(currentRow, currentCol);

         if (boardActivity.gameObject.slotFrom.getColor() != turnColor) {
            if (boardActivity.gameObject.slotFrom.getColor() == Slot.EMPTY) {
               displayDialog("YOU CAN'T MOVE AN EMPTY SLOT", "passive");
            } else {
               displayDialog("NOT YOUR TURN", "passive");
            }

            return;
         }

         if (boardActivity.gameObject.playerBlack.isTurn()) {
            if ((boardActivity.gameObject.canMoveAgain(boardActivity.gameObject.potentialSuccessiveSlot, boardActivity.gameObject.playerBlack)) &&
               (!boardActivity.gameObject.verifySuccessiveMove(boardActivity.gameObject.slotFrom, boardActivity.gameObject.potentialSuccessiveSlot))) {
               displayDialog("YOU MUST START FROM THE POSITION YOU ENDED ON", "passive");

               return;
            }
         }

         if (boardActivity.gameObject.playerWhite.isTurn()) {
            if ((boardActivity.gameObject.canMoveAgain(boardActivity.gameObject.potentialSuccessiveSlot, boardActivity.gameObject.playerWhite)) &&
               (!boardActivity.gameObject.verifySuccessiveMove(boardActivity.gameObject.slotFrom, boardActivity.gameObject.potentialSuccessiveSlot))) {
               displayDialog("YOU MUST START FROM THE POSITION YOU ENDED ON", "passive");

               return;
            }
         }

         if (!drawPotentialMoves(turnColor, boardActivity.drawCell[0])) {
            displayDialog("THIS PIECE CAN'T MOVE", "passive");

            return;
         }

         boardActivity.gameObject.firstClick = false;

         return;
      }

      boardActivity.gameObject.firstClick = true;

      drawPotentialMoves(turnColor, boardActivity.drawCell[3]);

      boardActivity.gameObject.rowTo = currentRow;
      boardActivity.gameObject.columnTo = currentCol;
      boardActivity.gameObject.slotTo = boardActivity.gameObject.boardObject.getSlot(currentRow, currentCol);

      if (!boardActivity.gameObject.makeMove(boardActivity.gameObject.slotFrom, boardActivity.gameObject.slotTo)) {
         displayDialog("INVALID MOVE", "passive");

         return;
      }

      drawMoveSlots();

      if (!switchTurns()) {
         return;
      }

      if (boardActivity.gameObject.playerBlack.isTurn()) {
         boardActivity.playerBlackTurn.setVisibility(View.VISIBLE);
         boardActivity.playerWhiteTurn.setVisibility(View.INVISIBLE);
      } else {
         boardActivity.playerBlackTurn.setVisibility(View.INVISIBLE);
         boardActivity.playerWhiteTurn.setVisibility(View.VISIBLE);
      }

      if (!boardActivity.gameObject.playerCanMove(boardActivity.gameObject.playerWhite) && !boardActivity.gameObject.playerCanMove(boardActivity.gameObject.playerBlack)) {
         displayDialog("GAME OVER. PRESS OK TO SEE THE RESULTS.", "end");
      }
   }

   /**
    * Description: Method to verify a turn switch for a current turn cycle.
    * Parameters: None.
    * Returns: Whether or not the turn switch was successful.
    */

   private boolean switchTurns() {
      if (boardActivity.gameObject.playerBlack.isTurn()) {
         boardActivity.gameObject.playerBlack.addToScore();
         String text = "BLACK: " + boardActivity.gameObject.playerBlack.getScore();
         boardActivity.playerBlackScore.setText(text.trim());

         if (boardActivity.gameObject.canMoveAgain(boardActivity.gameObject.slotTo, boardActivity.gameObject.playerBlack)) {
            boardActivity.gameObject.potentialSuccessiveSlot.setRow(boardActivity.gameObject.rowTo);
            boardActivity.gameObject.potentialSuccessiveSlot.setColumn(boardActivity.gameObject.columnTo);
            boardActivity.gameObject.potentialSuccessiveSlot.setColor(Slot.BLACK);

            return false;
         }

         if (!boardActivity.gameObject.playerCanMove(boardActivity.gameObject.playerWhite) && boardActivity.gameObject.playerCanMove(boardActivity.gameObject.playerBlack)) {
            displayDialog("WHITE CAN'T MOVE", "passive");

            return false;
         }

         boardActivity.gameObject.playerWhite.setIsTurn(true);
         boardActivity.gameObject.playerBlack.setIsTurn(false);

         return true;
      } else {
         boardActivity.gameObject.playerWhite.addToScore();
         String text = "WHITE: " + boardActivity.gameObject.playerWhite.getScore();
         boardActivity.playerWhiteScore.setText(text.trim());

         if (boardActivity.gameObject.canMoveAgain(boardActivity.gameObject.slotTo, boardActivity.gameObject.playerWhite)) {
            boardActivity.gameObject.potentialSuccessiveSlot.setRow(boardActivity.gameObject.rowTo);
            boardActivity.gameObject.potentialSuccessiveSlot.setColumn(boardActivity.gameObject.columnTo);
            boardActivity.gameObject.potentialSuccessiveSlot.setColor(Slot.WHITE);

            return false;
         }

         if (!boardActivity.gameObject.playerCanMove(boardActivity.gameObject.playerBlack) && boardActivity.gameObject.playerCanMove(boardActivity.gameObject.playerWhite)) {
            displayDialog("BLACK CAN'T MOVE", "passive");

            return false;
         }

         boardActivity.gameObject.playerWhite.setIsTurn(false);
         boardActivity.gameObject.playerBlack.setIsTurn(true);

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
            public void onClick(DialogInterface dialog, int id) {
               if (type == "end") {
                  Intent endIntent = new Intent(boardActivity,
                     EndActivity.class);
                  endIntent.putExtra("playerBlackScore", boardActivity.gameObject.playerBlack.getScore());
                  endIntent.putExtra("playerWhiteScore", boardActivity.gameObject.playerWhite.getScore());

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
      boardActivity.gameBoard[boardActivity.gameObject.slotFrom.getRow()][boardActivity.gameObject.slotFrom.getColumn()].setBackground(boardActivity.drawCell[3]);
      String directionMoving;

      if (boardActivity.gameObject.slotFrom.getRow() == boardActivity.gameObject.slotTo.getRow()) {
         if (boardActivity.gameObject.slotFrom.getColumn() - boardActivity.gameObject.slotTo.getColumn() == -2) {
            directionMoving = "right";
         } else {
            directionMoving = "left";
         }
      } else {
         if (boardActivity.gameObject.slotFrom.getRow() - boardActivity.gameObject.slotTo.getRow() == -2) {
            directionMoving = "down";
         } else {
            directionMoving = "up";
         }
      }

      if (directionMoving == "right") {
         boardActivity.gameBoard[boardActivity.gameObject.slotFrom.getRow()][boardActivity.gameObject.slotFrom.getColumn() + 1].setBackground(boardActivity.drawCell[3]);
      } else if (directionMoving == "left") {
         boardActivity.gameBoard[boardActivity.gameObject.slotFrom.getRow()][boardActivity.gameObject.slotFrom.getColumn() - 1].setBackground(boardActivity.drawCell[3]);
      } else if (directionMoving == "down") {
         boardActivity.gameBoard[boardActivity.gameObject.slotFrom.getRow() + 1][boardActivity.gameObject.slotFrom.getColumn()].setBackground(boardActivity.drawCell[3]);
      } else if (directionMoving == "up") {
         boardActivity.gameBoard[boardActivity.gameObject.slotFrom.getRow() - 1][boardActivity.gameObject.slotFrom.getColumn()].setBackground(boardActivity.drawCell[3]);
      }

      Drawable draw;

      if (boardActivity.gameObject.boardObject.getSlot(boardActivity.gameObject.slotTo.getRow(), boardActivity.gameObject.slotTo.getColumn()).getColor() == Slot.WHITE) {
         draw = boardActivity.drawCell[2];
      } else {
         draw = boardActivity.drawCell[1];
      }

      boardActivity.gameBoard[boardActivity.gameObject.slotTo.getRow()][boardActivity.gameObject.slotTo.getColumn()].setBackground(draw);
   }

   /**
    * Description: Method to draw, on the GUI, all potential moves a user has from a given position.
    * Parameters: int turnColor, which is the current player's piece color.
    *             Drawable drawCell, which is the image to use for drawing. This is a parameter because
    *             we also have to handle un-drawing potential move slots, so a different image is used.
    * Returns: Whether or not the current piece has any potential spots to move.
    */

   private boolean drawPotentialMoves(int turnColor, Drawable drawCell) {
      Slot slotRight = boardActivity.gameObject.boardObject.getSlot(boardActivity.gameObject.slotFrom.getRow(), boardActivity.gameObject.slotFrom.getColumn() + 2);
      Slot slotLeft = boardActivity.gameObject.boardObject.getSlot(boardActivity.gameObject.slotFrom.getRow(), boardActivity.gameObject.slotFrom.getColumn() - 2);
      Slot slotUp = boardActivity.gameObject.boardObject.getSlot(boardActivity.gameObject.slotFrom.getRow() + 2, boardActivity.gameObject.slotFrom.getColumn());
      Slot slotDown = boardActivity.gameObject.boardObject.getSlot(boardActivity.gameObject.slotFrom.getRow() - 2, boardActivity.gameObject.slotFrom.getColumn());
      boolean pieceCanMove = false;

      if (boardActivity.gameObject.isValidMove(boardActivity.gameObject.slotFrom, slotRight, turnColor)) {
         boardActivity.gameBoard[slotRight.getRow()][slotRight.getColumn()].setBackground(drawCell);
         pieceCanMove = true;
      }

      if (boardActivity.gameObject.isValidMove(boardActivity.gameObject.slotFrom, slotLeft, turnColor)) {
         boardActivity.gameBoard[slotLeft.getRow()][slotLeft.getColumn()].setBackground(drawCell);
         pieceCanMove = true;
      }

      if (boardActivity.gameObject.isValidMove(boardActivity.gameObject.slotFrom, slotUp, turnColor)) {
         boardActivity.gameBoard[slotUp.getRow()][slotUp.getColumn()].setBackground(drawCell);
         pieceCanMove = true;
      }

      if (boardActivity.gameObject.isValidMove(boardActivity.gameObject.slotFrom, slotDown, turnColor)) {
         boardActivity.gameBoard[slotDown.getRow()][slotDown.getColumn()].setBackground(drawCell);
         pieceCanMove = true;
      }

      return pieceCanMove;
   }
}