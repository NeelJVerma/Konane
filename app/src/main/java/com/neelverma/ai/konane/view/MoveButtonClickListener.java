package com.neelverma.ai.konane.view;

import android.util.Pair;
import android.view.View;
import android.widget.Toast;

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
      // Don't know why I need this, but turns get switched otherwise.
      gameObject.setTurnColor((gameObject.getPlayerWhite().isTurn() ? Slot.WHITE : Slot.BLACK));

      /*if ((gameObject.getPlayerBlack().isTurn() && !gameObject.getPlayerBlack().isComputer()) ||
         (gameObject.getPlayerWhite().isTurn() && !gameObject.getPlayerWhite().isComputer())) {
         Toast.makeText(boardActivity, "CAN ONLY MAKE COMPUTER MOVE", Toast.LENGTH_SHORT).show();

         return;
      }*/

      gameObject.setPlyCutoff(2);

      gameObject.callMinimax();

      MoveNode moveNode = gameObject.getBestMove();

      for (Slot s : moveNode.getMovePath()) {
         System.out.println(s.getRow() + "x" + s.getColumn());
      }

      gameObject.getBoardObject().printBoard();
      gameObject.getBestMove().getMovePath().clear();
   }
}