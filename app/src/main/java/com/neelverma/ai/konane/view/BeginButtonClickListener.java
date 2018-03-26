/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 3 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 3/27/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.neelverma.ai.konane.R;
import com.neelverma.ai.konane.model.Board;

/**
 * Class to handle button click action for the begin game button.
 * Created by Neel on 2/08/2018.
 */

public class BeginButtonClickListener implements View.OnClickListener {
   private MainActivity mainActivity;

   /**
    * Description: Constructor. Will initialize the listener with the activity on which the button
    * is clicked.
    * Parameters: MainActivity mainActivity, which is the activity on which the button was pressed.
    * Returns: Nothing.
    */

   BeginButtonClickListener(MainActivity mainActivity) {
      this.mainActivity = mainActivity;
   }

   @Override
   public void onClick(View v) {
      SaveGameButtonClickListener.deleteFilePath();

      TextView boardSizeEditText = mainActivity.findViewById(R.id.boardSizeEditText);
      TextView guessEditText = mainActivity.findViewById(R.id.guessEditText);

      Intent boardIntent = new Intent(mainActivity, BoardActivity.class);
      boardIntent.putExtra("gameType", BoardActivity.NEW_GAME);

      if (guessEditText.getText().toString().equals("")) {
         Toast.makeText(mainActivity, "YOU MUST GUESS TO SEE WHO GOES FIRST.", Toast.LENGTH_SHORT).show();

         return;
      }

      boardIntent.putExtra("guess", guessEditText.getText().toString());

      if (boardSizeEditText.getText().toString().equals("")) {
         Board.MAX_ROW = 6;
         Board.MAX_COLUMN = 6;

         BoardActivity.MAX_ROW = 6;
         BoardActivity.MAX_COL = 6;
      } else {
         Board.MAX_ROW = Integer.parseInt(boardSizeEditText.getText().toString());
         Board.MAX_COLUMN = Integer.parseInt(boardSizeEditText.getText().toString());

         BoardActivity.MAX_ROW = Integer.parseInt(boardSizeEditText.getText().toString());
         BoardActivity.MAX_COL = Integer.parseInt(boardSizeEditText.getText().toString());
      }

      mainActivity.startActivity(boardIntent);
   }
}
