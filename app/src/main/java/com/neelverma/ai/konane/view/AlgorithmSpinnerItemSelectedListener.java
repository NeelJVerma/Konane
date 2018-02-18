/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.view.View;
import android.widget.AdapterView;

import com.neelverma.ai.konane.model.Game;

/**
 * Class to handle item selected listener for the algorithm spinner.
 * Created by Neel on 2/08/2018.
 */

public class AlgorithmSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {
   private BoardActivity boardActivity;
   private static int algorithmType;
   private Game gameObject;
   private static boolean selected;

   public static final int BREADTH_FIRST = 0;
   public static final int DEPTH_FIRST = 1;
   public static final int BEST_FIRST = 2;
   public static final int BRANCH_AND_BOUND = 3;

   /**
    * Description: Constructor. Will initialize the listener with the activity on which the button
    * is clicked.
    * Parameters: BoardActivity boardActivity, which is the activity on which the button was pressed.
    * Returns: Nothing.
    */

   AlgorithmSpinnerItemSelectedListener(BoardActivity boardActivity) {
      this.boardActivity = boardActivity;
      this.gameObject = boardActivity.getGameObject();
      this.selected = false;
   }

   @Override
   public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
      selected = true;

      gameObject.getDfsMoves().clear();
      gameObject.getBfsMoves().clear();
      gameObject.getBestFirstSearchMoves().clear();
      gameObject.getBranchAndBoundMoves().clear();

      String item = adapterView.getItemAtPosition(i).toString();

      switch (item) {
         case "DEPTH FIRST":
            algorithmType = DEPTH_FIRST;
            break;
         case "BREADTH FIRST":
            algorithmType = BREADTH_FIRST;
            break;
         case "BEST FIRST":
            algorithmType = BEST_FIRST;
            break;
         case "BRANCH AND BOUND":
            algorithmType = BRANCH_AND_BOUND;
            break;
      }
   }

   @Override
   public void onNothingSelected(AdapterView<?> adapterView) {
      // Need this method to implement AdapterView.OnItemSelectedListener, even though it doesns't do
      // anything.
      return;
   }

   /**
    * Description: Method to get the type of algorithm that was selected.
    * Parameters: None.
    * Returns: The type of algorithm that was selected.
    */

   public static int getAlgorithmType() {
      return algorithmType;
   }

   public static boolean isSelected() {
      return selected;
   }

   public static void setSelected(boolean selectedStatic) {
      selected = selectedStatic;
   }
}