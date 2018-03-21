/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.model;

/**
 * Class to hold a player of the game.
 * Created by Neel on 01/20/2018.
 */

public class Player {
   private int color;
   private boolean isTurn;
   private int score;

   public static final int BLACK = 1;
   public static final int WHITE = -1;

   private boolean isComputer;

   /**
    * Description: Constructor. Will initialize the current player with a passed in color, a false
    * turn value, and a score of 0.
    * Parameters: int color to set the current color.
    * Returns: Nothing.
    */

   public Player(int color) {
      this.color = color;
      this.isTurn = false;
      this.score = 0;
      this.isComputer = false;
   }

   /**
    * Description: A method to return the color of the current player.
    * Parameters: None.
    * Returns: The current color.
    */

   public int getColor() {
      return color;
   }

   /**
    * Description: A method to return whether or not it is the turn of the current player.
    * Parameters: None.
    * Returns: The turn status of the current player.
    */

   public boolean isTurn() {
      return isTurn;
   }

   /**
    * Description: A method to set the turn status of the current player.
    * Parameters: boolean isTurn to set the turn status.
    * Returns: Nothing.
    */

   public void setIsTurn(boolean isTurn) {
      this.isTurn = isTurn;
   }

   /**
    * Description: A method to add to the current player's score. We don't need to pass in a value
    * because per turn, we will always add 1. Even in multi-jump turns, each jump is handled as one
    * turn, so we can just add 1 to each.
    * Parameters: None.
    * Returns: Nothing.
     */

   public void addToScore() {
      score += 1;
   }

   /**
    * Description: A method to get the score of the current player.
    * Parameters: None.
    * Returns: The current player's score.
    */

   public int getScore() {
      return score;
   }

   /**
    * Description: A method to set the score of the current player.
    * Parameters: int score, the score to set.
    * Returns: Nothing.
    */

   public void setScore(int score) {
      this.score = score;
   }

   public void setComputer(boolean isComputer) {
      this.isComputer = isComputer;
   }

   public boolean isComputer() {
      return isComputer;
   }
}