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
      if (buttonOne != null) {
         buttonOne.clearAnimation();
      }

      if (buttonTwo != null) {
         buttonTwo.clearAnimation();
      }

      gameObject.setTurnColor(gameObject.getPlayerWhite().isTurn() ? Slot.WHITE : Slot.BLACK);

      executeAlgorithm();

      Pair<Slot, Slot> pair = gameObject.dequeueNextAvailableMove();

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