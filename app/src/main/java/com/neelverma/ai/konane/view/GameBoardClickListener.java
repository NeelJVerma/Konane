package com.neelverma.ai.konane.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.neelverma.ai.konane.model.Slot;

public class GameBoardClickListener implements View.OnClickListener {
   private BoardActivity boardActivity;
   private int currentRow;
   private int currentCol;

   GameBoardClickListener(int currentRow, int currentCol, BoardActivity boardActivity) {
      this.boardActivity = boardActivity;
      this.currentRow = currentRow;
      this.currentCol = currentCol;
   }

   @Override
   public void onClick(View v) {
      int turnColor = boardActivity.gameObject.playerWhite.isTurn() ? Slot.WHITE : Slot.BLACK;

      if (boardActivity.gameObject.firstClick) {
         boardActivity.gameObject.rowFrom = currentRow;
         boardActivity.gameObject.columnFrom = currentCol;
         boardActivity.gameObject.slotFrom = boardActivity.gameObject.boardObject.getSlot(currentRow, currentCol);

         if (boardActivity.gameObject.slotFrom.getColor() != turnColor) {
            if (boardActivity.gameObject.slotFrom.getColor() == Slot.EMPTY) {
               displayDialog(v, "YOU CAN'T MOVE AN EMPTY SLOT", "passive");
            } else {
               displayDialog(v, "NOT YOUR TURN", "passive");
            }

            return;
         }

         // Check that if a successive move is able to be made for the player playing
         // black pieces, they started moving the same piece and didn't switch pieces
         // mid turn.
         if (boardActivity.gameObject.playerBlack.isTurn()) {
            if ((boardActivity.gameObject.canMoveAgain(boardActivity.gameObject.potentialSuccessiveSlot, boardActivity.gameObject.playerBlack)) &&
               (!boardActivity.gameObject.verifySuccessiveMove(boardActivity.gameObject.slotFrom, boardActivity.gameObject.potentialSuccessiveSlot))) {
               displayDialog(v, "YOU MUST START FROM THE POSITION YOU ENDED ON", "passive");

               return;
            }
         }

         // Check that if a successive move is able to be made for the player playing
         // white pieces, they started moving the same piece and didn't switch pieces
         // mid turn.
         if (boardActivity.gameObject.playerWhite.isTurn()) {
            if ((boardActivity.gameObject.canMoveAgain(boardActivity.gameObject.potentialSuccessiveSlot, boardActivity.gameObject.playerWhite)) &&
               (!boardActivity.gameObject.verifySuccessiveMove(boardActivity.gameObject.slotFrom, boardActivity.gameObject.potentialSuccessiveSlot))) {
               displayDialog(v, "YOU MUST START FROM THE POSITION YOU ENDED ON", "passive");

               return;
            }
         }

         if (!drawPotentialMoves(turnColor, boardActivity.drawCell[0])) {
            displayDialog(v, "THIS PIECE CAN'T MOVE", "passive");

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

      // Verify whether or not the move made was valid according to in bounds
      // verification, four direction verification, and color verification (see the
      // makeMove and isValidMove functions in the Game class).
      if (!boardActivity.gameObject.makeMove(boardActivity.gameObject.slotFrom, boardActivity.gameObject.slotTo)) {
         displayDialog(v, "INVALID MOVE", "passive");

         return;
      }

      // If the function got here, the first move in a possible multi-jump move was
      // successful.
      drawMoveSlots();

      // If it is black's turn, add to his/her score. Then check if
      // the player playing white pieces can move. If not, don't switch the turns.
      // Then, check if black can move again. If they can, set their
      // successive slot to be the current slot they just moved to. This way, we can
      // verify that they are the same when we come back for another click. If none
      // of those conditions are hit, switch the turns. Do the same thing for
      // white.
      if (boardActivity.gameObject.playerBlack.isTurn()) {
         boardActivity.gameObject.playerBlack.addToScore();
         String text = "BLACK: " + boardActivity.gameObject.playerBlack.getScore();
         boardActivity.playerBlackScore.setText(text.trim());

         if (boardActivity.gameObject.canMoveAgain(boardActivity.gameObject.slotTo, boardActivity.gameObject.playerBlack)) {
            boardActivity.gameObject.potentialSuccessiveSlot.setRow(boardActivity.gameObject.rowTo);
            boardActivity.gameObject.potentialSuccessiveSlot.setColumn(boardActivity.gameObject.columnTo);
            boardActivity.gameObject.potentialSuccessiveSlot.setColor(Slot.BLACK);

            return;
         }

         if (!boardActivity.gameObject.playerCanMove(boardActivity.gameObject.playerWhite) && boardActivity.gameObject.playerCanMove(boardActivity.gameObject.playerBlack)) {
            displayDialog(v, "WHITE CAN'T MOVE", "passive");
            return;
         }

         boardActivity.gameObject.playerWhite.setIsTurn(true);
         boardActivity.gameObject.playerBlack.setIsTurn(false);
      } else {
         boardActivity.gameObject.playerWhite.addToScore();
         String text = "WHITE: " + boardActivity.gameObject.playerWhite.getScore();
         boardActivity.playerWhiteScore.setText(text.trim());

         if (boardActivity.gameObject.canMoveAgain(boardActivity.gameObject.slotTo, boardActivity.gameObject.playerWhite)) {
            boardActivity.gameObject.potentialSuccessiveSlot.setRow(boardActivity.gameObject.rowTo);
            boardActivity.gameObject.potentialSuccessiveSlot.setColumn(boardActivity.gameObject.columnTo);
            boardActivity.gameObject.potentialSuccessiveSlot.setColor(Slot.WHITE);

            return;
         }

         if (!boardActivity.gameObject.playerCanMove(boardActivity.gameObject.playerBlack) && boardActivity.gameObject.playerCanMove(boardActivity.gameObject.playerWhite)) {
            displayDialog(v, "BLACK CAN'T MOVE", "passive");

            return;
         }

         boardActivity.gameObject.playerWhite.setIsTurn(false);
         boardActivity.gameObject.playerBlack.setIsTurn(true);
      }

      if (boardActivity.gameObject.playerBlack.isTurn()) {
         boardActivity.playerBlackTurn.setVisibility(View.VISIBLE);
         boardActivity.playerWhiteTurn.setVisibility(View.INVISIBLE);
      } else {
         boardActivity.playerBlackTurn.setVisibility(View.INVISIBLE);
         boardActivity.playerWhiteTurn.setVisibility(View.VISIBLE);
      }

      if (!boardActivity.gameObject.playerCanMove(boardActivity.gameObject.playerWhite) && !boardActivity.gameObject.playerCanMove(boardActivity.gameObject.playerBlack)) {
         displayDialog(v, "GAME OVER. PRESS OK TO SEE THE RESULTS.", "end");
      }
   }

   public void displayDialog(final View v, String alertMessage, final String type) {
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

                  v.getContext().startActivity(endIntent);
               }
            }
         });

      AlertDialog alert = builder.create();
      alert.show();
   }

   public void drawMoveSlots() {
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

   public boolean drawPotentialMoves(int turnColor, Drawable drawCell) {
      // Mark off the spots that a player can move if they are making their first
      // click.
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