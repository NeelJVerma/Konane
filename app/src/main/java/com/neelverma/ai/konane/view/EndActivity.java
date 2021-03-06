/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 3 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 3/27/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.neelverma.ai.konane.R;

/**
 * Class to hold the end screen.
 * Created by Neel on 1/29/2018.
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
      playAgainButton.setOnClickListener(new PlayAgainButtonClickListener(EndActivity.this));
   }
}