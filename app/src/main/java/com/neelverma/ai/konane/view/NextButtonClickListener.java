/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.util.Pair;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import com.neelverma.ai.konane.model.Game;
import com.neelverma.ai.konane.model.Slot;

public class NextButtonClickListener implements View.OnClickListener {
   private BoardActivity boardActivity;
   private Game gameObject;
   private static ImageButton buttonOne;
   private static ImageButton buttonTwo;

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

      gameObject.setTurnColor(gameObject.getPlayerWhite().isTurn() ? Slot.WHITE : Slot.BLACK);

      executeAlgorithm();

      if (suggestedAllMoves()) {
         Toast.makeText(boardActivity, "SUGGESTED ALL MOVES", Toast.LENGTH_SHORT).show();

         return;
      }

      Pair<Slot, Slot> pair = dequeueNextAvailableMove();

      startButtonAnimations(pair);
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
            gameObject.bestFirstSearch(); // TODO: IMPLEMENT
            return;
         case AlgorithmSpinnerItemSelectedListener.BRANCH_AND_BOUND:
            gameObject.branchAndBound(); // TODO: IMPLEMENT
            return;
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
    * Returns: A pair of slots, which is the suggested move.
    */

   private Pair<Slot, Slot> dequeueNextAvailableMove() {
      Slot returnSlotOne = new Slot(-1, -1, 2);
      Slot returnSlotTwo = new Slot(-1, -1, 2);

      switch (AlgorithmSpinnerItemSelectedListener.getAlgorithmType()) {
         case AlgorithmSpinnerItemSelectedListener.DEPTH_FIRST:
            returnSlotOne = gameObject.getDfsMoves().get(0).first;
            returnSlotTwo = gameObject.getDfsMoves().get(0).second;

            gameObject.getDfsMoves().remove(0);

            break;
         case AlgorithmSpinnerItemSelectedListener.BREADTH_FIRST:
            returnSlotOne = gameObject.getBfsMoves().get(0).first;
            returnSlotTwo = gameObject.getBfsMoves().get(0).second;

            gameObject.getBfsMoves().remove(0);

            break;
         case AlgorithmSpinnerItemSelectedListener.BEST_FIRST:
            returnSlotOne = gameObject.getBestFirstSearchMoves().get(0).first;
            returnSlotTwo = gameObject.getBestFirstSearchMoves().get(0).second;

            gameObject.getBestFirstSearchMoves().remove(0);

            break;
         case AlgorithmSpinnerItemSelectedListener.BRANCH_AND_BOUND:
            returnSlotOne = gameObject.getBranchAndBoundMoves().get(0).first;
            returnSlotTwo = gameObject.getBranchAndBoundMoves().get(0).second;

            gameObject.getBranchAndBoundMoves().remove(0);

            break;
      }

      return new Pair<>(returnSlotOne, returnSlotTwo);
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
    * Description: Method to start the button blinking.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void startButtonAnimations(Pair<Slot, Slot> pair) {
      Animation animation = new AlphaAnimation(1, 0);
      animation.setDuration(150);
      animation.setInterpolator(new LinearInterpolator());
      animation.setRepeatCount(Animation.INFINITE);
      animation.setRepeatMode(Animation.REVERSE);
      buttonOne = boardActivity.getGameBoard()[pair.first.getRow()][pair.first.getColumn()];
      buttonTwo = boardActivity.getGameBoard()[pair.second.getRow()][pair.second.getColumn()];
      buttonOne.startAnimation(animation);
      buttonTwo.startAnimation(animation);
   }
}