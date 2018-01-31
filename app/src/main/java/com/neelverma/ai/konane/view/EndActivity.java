/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 1 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Date: 2/02/2018                                          *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.neelverma.ai.konane.R;

/**
 * Class to hold the end screen.
 * Created by Neel on 1/29/2018.
 *
 * It displays the end screen saying that the game is over and who won, if anyone.
 * It also has a button that can redirect to BoardActivity if the user(s) want to play again.
 */

public class EndActivity extends AppCompatActivity {

   /**
    * Description: Method to create the activity.
    * Parameters: Bundle savedInstanceState, which is the state of the current activity's data. This
    * is used so that, if need be, the activity can restore itself from its previous state.
    * Returns: Nothing.
    */

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_end);

      TextView blackScoreTextView = findViewById(R.id.blackScoreTextView);
      TextView whiteScoreTextView = findViewById(R.id.whiteScoreTextView);
      TextView winnerTextView = findViewById(R.id.winnerTextView);

      Bundle bundle = getIntent().getExtras();
      int playerBlackScore = bundle.getInt("playerBlackScore");
      int playerWhiteScore = bundle.getInt("playerWhiteScore");

      // Set the respective text views with the black and white scores.
      blackScoreTextView.setText(playerBlackScore + " POINTS");
      whiteScoreTextView.setText(playerWhiteScore + " POINTS");

      // Display who won or if there was a draw.
      if (playerBlackScore > playerWhiteScore) {
         winnerTextView.setText("BLACK WINS!");
      } else if (playerBlackScore < playerWhiteScore) {
         winnerTextView.setText("WHITE WINS!");
      } else {
         winnerTextView.setText("IT'S A DRAW");
      }

      Button playAgainButton = findViewById(R.id.playAgainButton);

      // If the user clicks this button, they will be sent back to the board screen to play a new
      // game.
      playAgainButton.setOnClickListener(new View.OnClickListener() {

         /**
          * Description: Method to handle the on click event for the play again button.
          * Parameters: View v, which is the view object of whatever is being clicked.
          * Returns: Nothing.
          */

         @Override
         public void onClick(View v) {
            Intent boardIntent = new Intent(EndActivity.this,
               BoardActivity.class);
            startActivity(boardIntent);
         }
      });
   }
}