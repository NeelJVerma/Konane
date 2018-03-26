/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 3 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 3/27/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.model;

import java.util.ArrayList;

/**
 * Class to hold a node in the minimax tree.
 * Created by Neel on 03/13/2018
 */

public class MoveNode {
   private Slot source;
   private Slot dest;
   private int score;
   private int minimaxValue;
   private ArrayList<Slot> movePath = new ArrayList<>();

   /**
    * Description: Constructor. Will initialize the node with a source and a destination, which are
    *              only used as identifiers for the node.
    * Parameters: Slot source, which is the source of the move.
    *             Slot dest, which is the destination of the move.
    * Returns: Nothing.
    */

   MoveNode(Slot source, Slot dest){
      this.source = source;
      this.dest = dest;
   }

   /**
    * Description: Method to return the score of the move.
    * Parameters: None.
    * Returns: The score.
    */

   public int getScore() {
      return score;
   }

   /**
    * Description: Method to set the minimax value of the move.
    * Parameters: int minimaxValue, which is the minimax value to set.
    * Returns: Nothing.
    */

   public void setMinimaxValue(int minimaxValue) {
      this.minimaxValue = minimaxValue;
   }

   /**
    * Description: Method to get the path of the move.
    * Parameters: None.
    * Returns: The path.
    */

   public ArrayList<Slot> getMovePath() {
      return movePath;
   }

   /**
    * Description: Method to set the path of the move.
    * Parameters: ArrayList<Slot> movePath, which is the path to set.
    * Returns: Nothing.
    */

   public void setMovePath(ArrayList<Slot> movePath) {
      score = movePath.size() - 1;
      this.movePath = movePath;
   }

   /**
    * Description: Method to return the minimax value of the move.
    * Parameters: None.
    * Returns: The minimax value.
    */

   public int getMinimaxValue() {
      return minimaxValue;
   }
}