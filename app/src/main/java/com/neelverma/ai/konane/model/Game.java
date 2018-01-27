package com.neelverma.ai.konane.model;
import android.util.Pair;

import java.util.*;


/*
 * Class to play the game and execute all the game logic.
 * Created by Neel on 01/20/2018.
 *
 * GAME LOGIC:
 * 1) Computer randomly removes one black and one white stone.
 * 2) The player playing black stones moves first, and then they alternate.
 * 3) If a player can move, they must move. But if they can't, they must pass.
 * 4) The game keeps going until neither player can make a move.
 * 5) The winner is the player with the most points.
 *
 * MOVEMENT LOGIC:
 * 1) A player can move in one of four directions: up, down, left, right.
 * 2) They must jump over the other player's stone and land in an empty slot.
 * 3) When this happens, the player captures the opposing player's stone and removes it from the
 *    board.
 * 4) The player that moves then earns a point.
 * 5) A player cannot jump over their own stone.
 * 6) The player moving can make multiple jumps in one turn, providing that each jump is legal.
 */

import java.util.concurrent.ThreadLocalRandom;

public class Game {
   public Player playerBlack; // The player playing black stones.
   public Player playerWhite; // The player playing white stones.
   public Board boardObject; // The current game's board.

   /*
    * Description: Constructor. Will initialize the current game's variables through their
    * constructors. It also sets the current turns so that black always moves first.
    * Parameters: None.
    * Returns: Nothing.
    */

   public Game() {
      playerBlack = new Player(Player.BLACK);
      playerBlack.setIsTurn(true);
      playerWhite = new Player(Player.WHITE);
      playerWhite.setIsTurn(false);
      boardObject = new Board();
   }

   public Player getPlayerBlack() {
      return playerBlack;
   }

   public Player getPlayerWhite() {
      return playerWhite;
   }

   /*
    * Description: Method to remove two slots at the beginning of the game.
    * Parameters: None.
    * Returns: Nothing.
    */

   public Pair<Slot, Slot> removeTwoSlots() {
      int randomRowOne = new Random().nextInt(Board.MAX_ROW);
      int randomColumnOne = new Random().nextInt(Board.MAX_COLUMN);
      Slot removedSlotOne = boardObject.getSlot(randomRowOne, randomColumnOne);

      while (true) {
         int randomRowTwo = new Random().nextInt(Board.MAX_ROW);
         int randomColumnTwo = new Random().nextInt(Board.MAX_COLUMN);
         Slot removedSlotTwo = boardObject.getSlot(randomRowTwo, randomColumnTwo);

         if ((randomRowOne != randomRowTwo) &&
            (randomColumnOne != randomColumnTwo) &&
            (removedSlotOne.getColor() != removedSlotTwo.getColor())) {
            boardObject.setSlotColor(removedSlotOne, Slot.EMPTY);
            boardObject.setSlotColor(removedSlotTwo, Slot.EMPTY);
            return new Pair<>(removedSlotOne, removedSlotTwo);
         }
      }
   }

   /*
    * Description: Method to verify the validity of a move.
    * Parameters: Slot slotFrom, which is the slot to move from.
    *             Slot slotTo, which is the slot to move to.
    *             int color to verify the color restrictions of the move.
    * Returns: Whether the move is valid or not.
    */

   public boolean isValidMove(Slot slotFrom, Slot slotTo, int color) {
      // In bounds verification.
      boolean slotFromGood = ((slotFrom.getRow() < Board.MAX_ROW && slotFrom.getRow() >= Board.MIN_ROW) &&
         (slotFrom.getColumn() < Board.MAX_COLUMN && slotFrom.getColumn() >= Board.MIN_COLUMN));
      boolean slotToGood = ((slotTo.getRow() < Board.MAX_ROW && slotTo.getRow() >= Board.MIN_ROW) &&
         (slotTo.getColumn() < Board.MAX_COLUMN && slotTo.getColumn() >= Board.MIN_COLUMN));
      boolean bothSlotsPositionsGood = (slotFromGood && slotToGood);

      if (!bothSlotsPositionsGood) {
         return false;
      }

      // Four direction verification.
      if ((slotFrom.getColumn() == slotTo.getColumn()) &&
         (Math.abs(slotFrom.getRow() - slotTo.getRow()) != 2)) {
         return false;
      }

      if ((slotFrom.getRow() == slotTo.getRow()) &&
         (Math.abs(slotFrom.getColumn() - slotTo.getColumn()) != 2)) {
         return false;
      }

      if (slotFrom.getRow() != slotTo.getRow()) {
         if (slotFrom.getColumn() != slotTo.getColumn()) {
            return false;
         }
      }

      if (slotFrom.getColumn() != slotTo.getColumn()) {
         if (slotFrom.getRow() != slotTo.getRow()) {
            return false;
         }
      }

      // Color verification.
      if ((boardObject.getSlot(slotTo.getRow(), slotTo.getColumn()).getColor() != Slot.EMPTY) ||
         (boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn()).getColor() == Slot.EMPTY)) {
         return false;
      }

      String directionMoving;
      if (slotFrom.getRow() == slotTo.getRow()) {
         if (slotFrom.getColumn() - slotTo.getColumn() == -2) {
            directionMoving = "right";
         } else {
            directionMoving = "left";
         }
      } else {
         if (slotFrom.getRow() - slotTo.getRow() == -2) {
            directionMoving = "down";
         } else {
            directionMoving = "up";
         }
      }

      if (directionMoving == "right") {
         if ((boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn() + 1).getColor() == color) ||
            (boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn() + 1).getColor() == Slot.EMPTY)) {
            return false;
         }
      } else if (directionMoving == "left") {
         if ((boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn() - 1).getColor() == color) ||
            (boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn() - 1).getColor() == Slot.EMPTY)) {
            return false;
         }
      } else if (directionMoving == "down") {
         if ((boardObject.getSlot(slotFrom.getRow() + 1, slotFrom.getColumn()).getColor() == color) ||
            (boardObject.getSlot(slotFrom.getRow() + 1, slotFrom.getColumn()).getColor() == Slot.EMPTY)) {
            return false;
         }
      } else {
         if ((boardObject.getSlot(slotFrom.getRow() - 1, slotFrom.getColumn()).getColor() == color) ||
            (boardObject.getSlot(slotFrom.getRow() - 1, slotFrom.getColumn()).getColor() == Slot.EMPTY)) {
            return false;
         }
      }
      return true;
   }

   /*
    * Description: Method to make the specified move.
    * Parameters: Slot slotFrom, which is the slot to move from.
    *             Slot slotTo, which is the slot to move to.
    * Returns: Whether the move was successful or not.
    */

   public boolean makeMove(Slot slotFrom, Slot slotTo) {
      String directionMoving;
      int color = slotFrom.getColor();
      if (isValidMove(slotFrom, slotTo, color)) {
         boardObject.setSlotColor(slotFrom, Slot.EMPTY);
         boardObject.setSlotColor(slotTo, color);

         if (slotFrom.getRow() == slotTo.getRow()) {
            if (slotFrom.getColumn() - slotTo.getColumn() == -2) {
               directionMoving = "right";
            } else {
               directionMoving = "left";
            }
         } else {
            if (slotFrom.getRow() - slotTo.getRow() == -2) {
               directionMoving = "down";
            } else {
               directionMoving = "up";
            }
         }

         Slot slotRight = boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn() + 1);
         Slot slotLeft = boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn() - 1);
         Slot slotUp = boardObject.getSlot(slotFrom.getRow() + 1, slotFrom.getColumn());
         Slot slotDown = boardObject.getSlot(slotFrom.getRow() - 1, slotFrom.getColumn());

         if (directionMoving == "right") {
            boardObject.setSlotColor(slotRight, Slot.EMPTY);
         } else if (directionMoving == "left") {
            boardObject.setSlotColor(slotLeft, Slot.EMPTY);
         } else if (directionMoving == "down") {
            boardObject.setSlotColor(slotUp, Slot.EMPTY);
         } else {
            boardObject.setSlotColor(slotDown, Slot.EMPTY);
         }
         return true;
      }
      return false;
   }

   /*
    * Description: Method to verify whether a player can make a move.
    * Parameters: Player playerObject, which is the player that can move or not.
    * Returns: Whether or not the player can move.
    */

   public boolean playerCanMove(Player playerObject) {
      for (int r = 0; r < Board.MAX_ROW; r++) {
         for (int c = 0; c < Board.MAX_COLUMN; c++) {
            // Check all four movements.
            Slot slotFrom = boardObject.getSlot(r, c);
            Slot slotRight = boardObject.getSlot(r, c + 2);
            Slot slotLeft = boardObject.getSlot(r, c - 2);
            Slot slotUp = boardObject.getSlot(r + 2, c);
            Slot slotDown = boardObject.getSlot(r - 2, c);

            if (isValidMove(slotFrom, slotRight, playerObject.getColor())) {
               return true;
            }

            if (isValidMove(slotFrom, slotLeft, playerObject.getColor())) {
               return true;
            }

            if (isValidMove(slotFrom, slotUp, playerObject.getColor())) {
               return true;
            }

            if (isValidMove(slotFrom, slotDown, playerObject.getColor())) {
               return true;
            }
         }
      }
      return false;
   }

   /*
    * Description: Method to check whether or not a player can move again.
    * Parameters: Slot slotFrom, which is the slot to move from.
    *             Player playerObject, which is the player that can move again or not.
    * Returns: Whether or not the player can move again.
    */

   public boolean canMoveAgain(Slot slotFrom, Player playerObject) {
      Slot slotRight = boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn() + 2);
      Slot slotLeft = boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn() - 2);
      Slot slotUp = boardObject.getSlot(slotFrom.getRow() + 2, slotFrom.getColumn());
      Slot slotDown = boardObject.getSlot(slotFrom.getRow() - 2, slotFrom.getColumn());

      return ((isValidMove(slotFrom, slotRight, playerObject.getColor())) ||
         (isValidMove(slotFrom, slotLeft, playerObject.getColor())) ||
         (isValidMove(slotFrom, slotUp, playerObject.getColor())) ||
         (isValidMove(slotFrom, slotDown, playerObject.getColor())));
   }

   /*
    * Description: Method to verify the validity of the possible chained jump. In a chained jump,
    * the player must move the same piece that they were already moving.
    * Parameters: Slot potentialSlotFrom, which is the potential slot to move from.
    *             Slot slotFrom, which is the slot to move from.
    * Returns: Whether the move is valid or not.
    */

   public boolean verifySuccessiveMove(Slot potentialSlotFrom, Slot slotFrom) {
      return ((slotFrom.getRow() == potentialSlotFrom.getRow()) &&
         (slotFrom.getColumn() == potentialSlotFrom.getColumn()));
   }

   /*
    * Description: Method to display the winner of the game.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void displayWinner() {
      if (playerBlack.getScore() > playerWhite.getScore()) {
         System.out.println("BLACK WINS!");
      } else if (playerWhite.getScore() > playerBlack.getScore()) {
         System.out.println("WHITE WINS!");
      } else {
         System.out.println("IT'S A DRAW.");
      }
   }

   /*
    * Description: Method to play the game and execute all game logic.
    * Parameters: None.
    * Returns: Nothing.
    */

   public void playGame() {
      boardObject.printBoard();
      removeTwoSlots();
      boardObject.printBoard();

      int numTurns = 1;
      int turnCounter = 0;
      int rowFrom, rowTo, columnFrom, columnTo;
      Scanner scnr = new Scanner(System.in);

      // Invalid successive slot to start. This is used because potentialSuccessiveSlot is used to
      // verify if the player can make a chain jump, which is done before it is initialized, to
      // retain the previous value (refer to where canMoveAgain and verifySuccessiveMove are
      // called.
      Slot potentialSuccessiveSlot = new Slot(Board.MAX_ROW, Board.MAX_COLUMN, 2);

      while (playerCanMove(playerBlack) || playerCanMove(playerWhite)) {
         System.out.println("TURN " + numTurns);
         if (playerWhite.isTurn()) {
            System.out.println("WHITE TURN");
            if (!playerCanMove(playerWhite)) {
               System.out.println("WHITE CAN'T MOVE");
               playerBlack.setIsTurn(true);
               playerWhite.setIsTurn(false);
               continue;
            }
         } else {
            System.out.println("BLACK TURN");
            if (!playerCanMove(playerBlack)) {
               System.out.println("BLACK CAN'T MOVE");
               playerWhite.setIsTurn(true);
               playerBlack.setIsTurn(false);
               continue;
            }
         }

         System.out.print("Row from: ");
         while (!scnr.hasNextInt()) {
            scnr.next();
            System.out.print("Row from: ");
         }
         rowFrom = scnr.nextInt();

         System.out.print("Column from: ");
         while (!scnr.hasNextInt()) {
            scnr.next();
            System.out.print("Column from: ");
         }
         columnFrom = scnr.nextInt();

         System.out.print("Row to: ");
         while (!scnr.hasNextInt()) {
            scnr.next();
            System.out.print("Row to: ");
         }
         rowTo = scnr.nextInt();

         System.out.print("Column to: ");
         while (!scnr.hasNextInt()) {
            scnr.next();
            System.out.print("Column to: ");
         }
         columnTo = scnr.nextInt();

         Slot slotFrom = boardObject.getSlot(rowFrom, columnFrom);
         Slot slotTo = boardObject.getSlot(rowTo, columnTo);

         if (playerWhite.isTurn()) {
            if ((canMoveAgain(potentialSuccessiveSlot, playerWhite)) &&
               (!verifySuccessiveMove(slotFrom, potentialSuccessiveSlot))) {
               System.out.println("MUST START FROM POSITION YOU ENDED ON");
               continue;
            }
         } else if (playerBlack.isTurn()) {
            if ((canMoveAgain(potentialSuccessiveSlot, playerBlack)) &&
               (!verifySuccessiveMove(slotFrom, potentialSuccessiveSlot))) {
               System.out.println("MUST START FROM POSITION YOU ENDED ON");
               continue;
            }
         }

         if ((playerWhite.isTurn()) &&
            (boardObject.getSlot(rowFrom, columnFrom).getColor() != Slot.WHITE)) {
            System.out.println("NOT A VALID MOVE");
            continue;
         }

         if ((playerBlack.isTurn()) &&
            (boardObject.getSlot(rowFrom, columnFrom).getColor() != Slot.BLACK)) {
            System.out.println("NOT A VALID MOVE");
            continue;
         }

         if (makeMove(slotFrom, slotTo)) {
            boardObject.printBoard();
         } else {
            System.out.println("NOT A VALID MOVE");
            continue;
         }

         if (playerWhite.isTurn()) {
            playerWhite.addToScore();

            if (canMoveAgain(slotTo, playerWhite)) {
               String choice;
               System.out.println("Would you like to make another move? ");
               choice = scnr.next();
               if (choice.equals("yes")) {
                  potentialSuccessiveSlot.setRow(rowTo);
                  potentialSuccessiveSlot.setColumn(columnTo);
                  potentialSuccessiveSlot.setColor(Slot.WHITE);
                  continue;
               }
            }

            playerBlack.setIsTurn(true);
            playerWhite.setIsTurn(false);
         } else if (playerBlack.isTurn()) {
            playerBlack.addToScore();

            if (canMoveAgain(slotTo, playerBlack)) {
               String choice;
               System.out.println("Would you like to make another move? ");
               choice = scnr.next();
               if (choice.equals("yes")) {
                  potentialSuccessiveSlot.setRow(rowTo);
                  potentialSuccessiveSlot.setColumn(columnTo);
                  potentialSuccessiveSlot.setColor(Slot.BLACK);
                  continue;
               }
            }

            playerWhite.setIsTurn(true);
            playerBlack.setIsTurn(false);
         }

         System.out.println("Score white: " + playerWhite.getScore());
         System.out.println("Score black: " + playerBlack.getScore());
      }

      System.out.println("GAME OVER");
      displayWinner();
   }
}