/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.model;

/**
 * Class to hold the Konane game board.
 * Created by Neel on 01/20/2018.
 */

public class Board {
   public static final int MAX_ROW = 6;
   public static final int MAX_COLUMN = 6;
   public static final int MIN_ROW = 0;
   public static final int MIN_COLUMN = 0;

   private Slot[][] gameBoard = new Slot[MAX_ROW][MAX_COLUMN]; // A 2D array of slots as the board.

   /**
    * Description: Constructor. Will initialize the board with 36 slots, each alternating between
    * black and white spaces.
    * Parameters: None.
    * Returns: Nothing.
    */

   public Board() {
      int currentColor = Slot.BLACK;

      for (int r = 0; r < MAX_ROW; r++) {
         for (int c = 0; c < MAX_COLUMN; c++) {
            Slot currentSlot = new Slot(r, c, currentColor);
            setSlot(r, c, currentSlot);
            currentColor = -currentColor;
         }
         currentColor = -currentColor;
      }
   }

   /**
    * Description: Method to set the slot of the current board object. It is private because it is
    * only used in this class.
    * Parameters: int row to set the row.
    *             int column to set the column.
    *             Slot slotObject to set the slot properties of the board at the row, column pair.
    * Returns: Nothing.
    */

   private void setSlot(int row, int column, Slot slotObject) {
      gameBoard[row][column] = slotObject;
   }

   /**
    * Description: Method to set the slot color of the current board object.
    * Parameters: Slot slotObject to set the passed in slot with the passed in color.
    *             int color to set the slot with this color.
    * Returns: Nothing.
    */

   public void setSlotColor(Slot slotObject, int color) {
      slotObject.setColor(color);
   }

   /**
    * Description: Method to get the slot located at the row, column pair passed in.
    * Parameters: int row to specify the row.
    *             int column to specify the column.
    * Returns: The slot, if it is a valid slot, otherwise a slot located at (6, 6) with a color of
    * 2, an invalid slot. We need to return an invalid slot because of how the slots are handled in
    * checking if a move is valid (see the Game class).
    */

   public Slot getSlot(int row, int column) {
      if ((row < MAX_ROW && row >= MIN_ROW) && (column < MAX_COLUMN && column >= MIN_COLUMN)) {
         return gameBoard[row][column];
      }
      Slot ret = new Slot(MAX_ROW, MAX_COLUMN, 2);
      return ret;
   }
}