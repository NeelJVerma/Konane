package com.neelverma.ai.konane.view;

import android.app.AlertDialog;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.neelverma.ai.konane.R;
import com.neelverma.ai.konane.model.Game;
import com.neelverma.ai.konane.model.MoveNode;
import com.neelverma.ai.konane.model.Slot;

import java.util.ArrayList;
import java.util.HashMap;

public class MoveButtonClickListener implements View.OnClickListener {
   private BoardActivity boardActivity;
   private Game gameObject;

   MoveButtonClickListener(BoardActivity boardActivity) {
      this.boardActivity = boardActivity;
      this.gameObject = boardActivity.getGameObject();
   }

   @Override
   public void onClick(View v) {
      boardActivity.stopPlayerAnimation();

      if (gameObject.getBestMove() != null) {
         boardActivity.stopMoveAnimation(gameObject.getBestMove());
      }

      // Don't know why I need this, but turns get switched otherwise.
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
      gameObject.callMinimax();

      MoveNode moveNode = gameObject.getBestMove();

      gameObject.makeMoveAlgo(moveNode);
      boardActivity.reDrawBoard();

      if (gameObject.getTurnColor() == Slot.BLACK) {
         gameObject.getPlayerBlack().addToScore(gameObject.getBestMove().getScore());
      } else {
         gameObject.getPlayerWhite().addToScore(gameObject.getBestMove().getScore());
      }

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
         gameObject.getPlayerWhite().setIsTurn(true);
         gameObject.getPlayerBlack().setIsTurn(false);
      }

      boardActivity.reDrawScores();
      boardActivity.reDrawTurns();

      if (!gameObject.playerCanMove(gameObject.getPlayerWhite()) && !gameObject.playerCanMove(gameObject.getPlayerBlack())) {
         boardActivity.getPlayerWhiteTurn().setVisibility(View.INVISIBLE);
         boardActivity.getPlayerBlackTurn().setVisibility(View.INVISIBLE);
         displayEndGameDialog();
      }
   }

   private void displayEndGameDialog() {
      AlertDialog.Builder builder = new AlertDialog.Builder(boardActivity);

      builder.setMessage("GAME OVER. PRESS OK TO CONTINUE.")
         .setCancelable(false)
         .setPositiveButton("OK", new EndGameDialog(boardActivity));

      AlertDialog alert = builder.create();
      alert.show();
   }
}