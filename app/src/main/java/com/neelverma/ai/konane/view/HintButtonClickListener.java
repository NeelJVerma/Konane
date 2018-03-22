package com.neelverma.ai.konane.view;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.neelverma.ai.konane.R;
import com.neelverma.ai.konane.model.Game;
import com.neelverma.ai.konane.model.MoveNode;

public class HintButtonClickListener implements View.OnClickListener {
   private BoardActivity boardActivity;
   private Game gameObject;

   HintButtonClickListener(BoardActivity boardActivity) {
      this.boardActivity = boardActivity;
      this.gameObject = boardActivity.getGameObject();
   }

   @Override
   public void onClick(View v) {
      if ((gameObject.getPlayerBlack().isTurn() && gameObject.getPlayerBlack().isComputer()) ||
         (gameObject.getPlayerWhite().isTurn() && gameObject.getPlayerWhite().isComputer())) {
         Toast.makeText(boardActivity, "HINT NOT AVAILABLE FOR COMPUTER", Toast.LENGTH_SHORT).show();

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

      boardActivity.showMoveFromMinimax(moveNode);
      boardActivity.showScoresFromMinimax(moveNode);
   }
}