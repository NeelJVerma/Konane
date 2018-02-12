package com.neelverma.ai.konane.view;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.Toast;

import com.neelverma.ai.konane.model.Game;
import com.neelverma.ai.konane.model.Slot;

public class NextButtonClickListener implements View.OnClickListener {
   private BoardActivity boardActivity;
   private Game gameObject;

   NextButtonClickListener(BoardActivity boardActivity) {
      this.boardActivity = boardActivity;
      this.gameObject = boardActivity.getGameObject();
   }

   @Override
   public void onClick(View v) {
      if (!gameObject.isFirstClick() || gameObject.isSuccessiveMove()) {
         Toast.makeText(boardActivity, "CAN'T FIND NEXT MOVE MID-TURN", Toast.LENGTH_SHORT).show();

         return;
      }

      gameObject.setTurnColor(gameObject.getPlayerWhite().isTurn() ? Slot.WHITE : Slot.BLACK);

      gameObject.breadthFirstSearch();

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
   }

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

   private boolean switchTurns() {
      if (gameObject.getPlayerBlack().isTurn()) {
         gameObject.getPlayerBlack().addToScore();

         String text = "BLACK: " + gameObject.getPlayerBlack().getScore();
         boardActivity.getPlayerBlackScore().setText(text.trim());

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

         if (!gameObject.playerCanMove(gameObject.getPlayerBlack()) && gameObject.playerCanMove(gameObject.getPlayerWhite())) {
            Toast.makeText(boardActivity, "BLACK CAN'T MOVE", Toast.LENGTH_SHORT).show();

            return false;
         }

         gameObject.getPlayerWhite().setIsTurn(false);
         gameObject.getPlayerBlack().setIsTurn(true);
      }

      return true;
   }
}
