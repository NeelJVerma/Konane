/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.neelverma.ai.konane.model.Game;
import com.neelverma.ai.konane.model.Slot;

public class NextButtonClickListener implements View.OnClickListener {
   private BoardActivity boardActivity;
   private Game gameObject;
   private static ImageButton buttonOne;
   private static ImageButton buttonTwo;
   private static TextView playerScore;

   /**
    * Description: Constructor. Will initialize the dialog box with an activity and a button choice.
    * Parameters: BoardActivity boardActivity, which is the activity to display this dialog on.
    * Returns: Nothing.
    */

   NextButtonClickListener(BoardActivity boardActivity) {
      this.boardActivity = boardActivity;
      this.gameObject = boardActivity.getGameObject();
      buttonOne = null;
      buttonTwo = null;
      playerScore = null;
   }

   @Override
   public void onClick(View v) {
      if (gameObject.isSuccessiveMove()) {
         Toast.makeText(boardActivity, "YOU CAN ONLY MOVE FROM THE POSITION YOU WERE ON", Toast.LENGTH_SHORT).show();

         return;
      }

      if (!gameObject.isFirstClick()) {
         Toast.makeText(boardActivity, "CAN'T SUGGEST MOVE MID CLICK", Toast.LENGTH_SHORT).show();

         return;
      }

      if (buttonOne != null) {
         buttonOne.clearAnimation();
      }

      if (buttonTwo != null) {
         buttonTwo.clearAnimation();
      }

      if (playerScore != null) {
         playerScore.clearAnimation();
      }

      gameObject.setTurnColor(gameObject.getPlayerWhite().isTurn() ? Slot.WHITE : Slot.BLACK);

      executeAlgorithm();

      if (suggestedAllMoves()) {
         Toast.makeText(boardActivity, "SUGGESTED ALL MOVES", Toast.LENGTH_SHORT).show();

         boardActivity.getPlayerBlackScore().setText(("BLACK: " + gameObject.getPlayerBlack().getScore()).trim());
         boardActivity.getPlayerWhiteScore().setText(("WHITE: " + gameObject.getPlayerWhite().getScore()).trim());

         return;
      }

      Pair<Pair<Slot, Slot>, Integer> pair = dequeueNextAvailableMove();

      startAnimations(pair);
   }

   /**
    * Description: Method to execute the chosen algorithm as given from the spinner.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void executeAlgorithm() {
      switch (AlgorithmSpinnerItemSelectedListener.getAlgorithmType()) {
         case AlgorithmSpinnerItemSelectedListener.DEPTH_FIRST:
            gameObject.depthFirstSearch();
            break;
         case AlgorithmSpinnerItemSelectedListener.BREADTH_FIRST:
            gameObject.breadthFirstSearch();
            break;
         case AlgorithmSpinnerItemSelectedListener.BEST_FIRST:
            gameObject.bestFirstSearch();
            break;
         case AlgorithmSpinnerItemSelectedListener.BRANCH_AND_BOUND:
            gameObject.branchAndBound(); // TODO: IMPLEMENT
            break;
      }
   }

   /**
    * Description: Method to check if all the moves have been suggested.
    * Parameters: None.
    * Returns: Whether or not all moves have been suggested.
    */

   private boolean suggestedAllMoves() {
      switch (AlgorithmSpinnerItemSelectedListener.getAlgorithmType()) {
         case AlgorithmSpinnerItemSelectedListener.DEPTH_FIRST:
            if (gameObject.getDfsMoves().size() == 0) {
               return true;
            }

            break;
         case AlgorithmSpinnerItemSelectedListener.BREADTH_FIRST:
            if (gameObject.getBfsMoves().size() == 0) {
               return true;
            }

            break;
         case AlgorithmSpinnerItemSelectedListener.BEST_FIRST:
            if (gameObject.getBestFirstSearchMoves().size() == 0) {
               return true;
            }

            break;
         case AlgorithmSpinnerItemSelectedListener.BRANCH_AND_BOUND:
            if (gameObject.getBranchAndBoundMoves().size() == 0) {
               return true;
            }

            break;
      }

      return false;
   }

   /**
    * Description: Method to dequeue the next available move for whatever algorithm.
    * Parameters: None.
    * Returns: A pair of pair of slots and an integer, which is the suggested move and the added score.
    */

   private Pair<Pair<Slot, Slot>, Integer> dequeueNextAvailableMove() {
      Slot returnSlotOne = new Slot(-1, -1, 2);
      Slot returnSlotTwo = new Slot(-1, -1, 2);
      int returnScore = 0;

      switch (AlgorithmSpinnerItemSelectedListener.getAlgorithmType()) {
         case AlgorithmSpinnerItemSelectedListener.DEPTH_FIRST:
            returnSlotOne = gameObject.getDfsMoves().get(0).first.first;
            returnSlotTwo = gameObject.getDfsMoves().get(0).first.second;
            returnScore = gameObject.getDfsMoves().get(0).second;

            gameObject.getDfsMoves().remove(0);

            break;
         case AlgorithmSpinnerItemSelectedListener.BREADTH_FIRST:
            returnSlotOne = gameObject.getBfsMoves().get(0).first.first;
            returnSlotTwo = gameObject.getBfsMoves().get(0).first.second;
            returnScore = gameObject.getBfsMoves().get(0).second;

            gameObject.getBfsMoves().remove(0);

            break;
         case AlgorithmSpinnerItemSelectedListener.BEST_FIRST:
            returnSlotOne = gameObject.getBestFirstSearchMoves().get(0).first.first;
            returnSlotTwo = gameObject.getBestFirstSearchMoves().get(0).first.second;
            returnScore = gameObject.getBestFirstSearchMoves().get(0).second;

            gameObject.getBestFirstSearchMoves().remove(0);

            break;
         case AlgorithmSpinnerItemSelectedListener.BRANCH_AND_BOUND:
            returnSlotOne = gameObject.getBranchAndBoundMoves().get(0).first;
            returnSlotTwo = gameObject.getBranchAndBoundMoves().get(0).second;

            gameObject.getBranchAndBoundMoves().remove(0);

            break;
      }

      return new Pair<>(new Pair<>(returnSlotOne, returnSlotTwo), returnScore);
   }

   /**
    * Description: Method to get the image button one.
    * Parameters: None.
    * Returns: The image button one.
    */

   public static ImageButton getButtonOne() {
      return buttonOne;
   }

   /**
    * Description: Method to get the image button two.
    * Parameters: None.
    * Returns: The image button two.
    */

   public static ImageButton getButtonTwo() {
      return buttonTwo;
   }

   /**
    * Description: Method to get the player score text view.
    * Parameters: None.
    * Returns: The player score text view.
    */

   public static TextView getPlayerScore() {
      return playerScore;
   }

   /**
    * Description: Method to start the button blinking.
    * Parameters: Pair<Pair<Slot, Slot>, Integer> pair, which is the move and the addition to the score.
    * Returns: Nothing.
    */

   private void startAnimations(Pair<Pair<Slot, Slot>, Integer> pair) {
      Animation animation = new AlphaAnimation(1, 0);
      animation.setDuration(150);
      animation.setInterpolator(new LinearInterpolator());
      animation.setRepeatCount(Animation.INFINITE);
      animation.setRepeatMode(Animation.REVERSE);

      buttonOne = boardActivity.getGameBoard()[pair.first.first.getRow()][pair.first.first.getColumn()];
      buttonTwo = boardActivity.getGameBoard()[pair.first.second.getRow()][pair.first.second.getColumn()];
      playerScore = gameObject.getPlayerWhite().isTurn() ? boardActivity.getPlayerWhiteScore() : boardActivity.getPlayerBlackScore();

      int tempScore = gameObject.getPlayerWhite().isTurn() ? gameObject.getPlayerWhite().getScore() : gameObject.getPlayerBlack().getScore();
      tempScore += pair.second;
      String blackPlayerText = "BLACK: " + tempScore;
      String whitePlayerText = "WHITE: " + tempScore;
      String mainText = gameObject.getPlayerWhite().isTurn() ? whitePlayerText : blackPlayerText;

      playerScore.setText(mainText.trim());
      buttonOne.startAnimation(animation);
      buttonTwo.startAnimation(animation);
      playerScore.startAnimation(animation);
   }
}