/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 3 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 3/27/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.model;

/**
 * Class to hold a particular slot in the game board.
 * Created by Neel on 01/20/2018.
 */

public class Slot {
   private int row;
   private int column;
   private int color;

   public static final int BLACK = 1;
   public static final int WHITE = -1;
   public static final int EMPTY = 0;

   /**
    * Description: Constructor. Will initialize the slot with values passed in.
    * Parameters: int row to set the row.
    *             int column to set the column.
    *             int color to set the color.
    * Returns: Nothing.
    */

   public Slot(int row, int column, int color) {
      this.row = row;
      this.column = column;
      this.color = color;
   }

   /**
    * Description: Method to set the row of the current slot object.
    * Parameters: int row to set the row.
    * Returns: Nothing.
    */

   public void setRow(int row) {
      this.row = row;
   }

   /**
    * Description: Method to set the column of the current slot object.
    * Parameters: int column to set the column.
    * Returns: Nothing.
    */

   public void setColumn(int column) {
      this.column = column;
   }

   /**
    * Description: Method to set the color of the current slot object.
    * Parameters: int color to set the color.
    * Returns: Nothing.
    */

   public void setColor(int color) {
      this.color = color;
   }

   /**
    * Description: Method to get the row of the current slot object.
    * Parameters: None.
    * Returns: The row, if it is a valid row, otherwise -1, an invalid row. We need to return -1
    * because of how the slots are handled in checking if a move is valid (see the Game class).
    */

   public int getRow() {
      if (row < Board.MAX_ROW && row >= Board.MIN_ROW) {
         return row;
      }

      return -1;
   }

   /**
    * Description: Method to get the column of the current slot object.
    * Parameters: None.
    * Returns: The column, if it is a valid column, otherwise -1, an invalid column. We need to
    * return -1 because of how the slots are handled in checking if a move is valid (see the
    * Game class).
    */

   public int getColumn() {
      if (row < Board.MAX_COLUMN && column >= Board.MIN_COLUMN) {
         return column;
      }

      return -1;
   }

   /**
    * Description: Method to get the color of the current slot object.
    * Parameters: None.
    * Returns: The color of the current slot object.
    */

   public int getColor() {
      return color;
   }

   /**
    * Description: A method to overload the equals method.
    * Parameters: Object slot, which is the slot to compare.
    * Returns: Whether or not the two slots are equal.
    */

   @Override
   public boolean equals(Object slot) {
      Slot tempSlot = (Slot) slot;

      return row == tempSlot.row && column == tempSlot.column;
   }
}