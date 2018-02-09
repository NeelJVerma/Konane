/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
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

      blackScoreTextView.setText(playerBlackScore + " POINTS");
      whiteScoreTextView.setText(playerWhiteScore + " POINTS");

      if (playerBlackScore > playerWhiteScore) {
         winnerTextView.setText("BLACK WINS!");
      } else if (playerBlackScore < playerWhiteScore) {
         winnerTextView.setText("WHITE WINS!");
      } else {
         winnerTextView.setText("IT'S A DRAW");
      }

      Button playAgainButton = findViewById(R.id.playAgainButton);

      playAgainButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            SaveGameClickListener.deleteFilePath();
            Intent boardIntent = new Intent(EndActivity.this,
               MainActivity.class);
            startActivity(boardIntent);
         }
      });
   }
}