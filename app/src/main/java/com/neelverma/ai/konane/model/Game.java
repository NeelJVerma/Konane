/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.model;
import android.content.Context;
import android.os.Environment;
import android.util.Pair;

import com.neelverma.ai.konane.view.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;


/**
 * Class to play the game and execute all the game logic. It always holds the state of the game.
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

public class Game {
   private Player playerBlack;
   private Player playerWhite;
   private Board boardObject;

   private Slot potentialSuccessiveSlot;
   private Slot slotFrom;
   private Slot slotTo;

   private boolean firstClick;
   private boolean successiveMove;
   private int turnColor;

   /**
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

      // Set invalid parameters to start. This is used in move verification.
      potentialSuccessiveSlot = new Slot(Board.MAX_ROW, Board.MAX_COLUMN, 2);
      slotFrom = boardObject.getSlot(-1, -1);
      slotTo = boardObject.getSlot(-1, -1);

      // First click is always true in the starting game state.
      firstClick = true;

      successiveMove = false;
      turnColor = Slot.BLACK;
   }

   /**
    * Description: Method to remove two slots at the beginning of the game.
    * Parameters: None.
    * Returns: A pair of the two slots so that the BoardActivity class can know which slots to mark
    * as empty.
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

   /**
    * Description: Method to verify the validity of a move.
    * Parameters: Slot slotFrom, which is the slot to move from.
    * Slot slotTo, which is the slot to move to.
    * int color to verify the color restrictions of the move.
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

      if (directionMoving.equals("right")) {
         if ((boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn() + 1).getColor() == color) ||
            (boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn() + 1).getColor() == Slot.EMPTY)) {
            return false;
         }
      } else if (directionMoving.equals("left")) {
         if ((boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn() - 1).getColor() == color) ||
            (boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn() - 1).getColor() == Slot.EMPTY)) {
            return false;
         }
      } else if (directionMoving.equals("down")) {
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

   /**
    * Description: Method to make the specified move.
    * Parameters: Slot slotFrom, which is the slot to move from.
    * Slot slotTo, which is the slot to move to.
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
         Slot slotDown = boardObject.getSlot(slotFrom.getRow() + 1, slotFrom.getColumn());
         Slot slotUp = boardObject.getSlot(slotFrom.getRow() - 1, slotFrom.getColumn());

         if (directionMoving.equals("right")) {
            boardObject.setSlotColor(slotRight, Slot.EMPTY);
         } else if (directionMoving.equals("left")) {
            boardObject.setSlotColor(slotLeft, Slot.EMPTY);
         } else if (directionMoving.equals("down")) {
            boardObject.setSlotColor(slotDown, Slot.EMPTY);
         } else {
            boardObject.setSlotColor(slotUp, Slot.EMPTY);
         }

         return true;
      }

      return false;
   }

   /**
    * Description: Method to verify whether a player can make a move.
    * Parameters: Player playerObject, which is the player that can move or not.
    * Returns: Whether or not the player can move.
    */

   public boolean playerCanMove(Player playerObject) {
      for (int r = 0; r < Board.MAX_ROW; r++) {
         for (int c = 0; c < Board.MAX_COLUMN; c++) {
            Slot slotFrom = boardObject.getSlot(r, c);
            Slot slotRight = boardObject.getSlot(r, c + 2);
            Slot slotLeft = boardObject.getSlot(r, c - 2);
            Slot slotUp = boardObject.getSlot(r - 2, c);
            Slot slotDown = boardObject.getSlot(r + 2, c);

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

   /**
    * Description: Method to check whether or not a player can move again.
    * Parameters: Slot slotFrom, which is the slot to move from.
    * int turnColor, which is the color the current player is playing.
    * Returns: Whether or not the player can move again.
    */

   public boolean canMoveAgain(Slot slotFrom, int turnColor) {
      Slot slotRight = boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn() + 2);
      Slot slotLeft = boardObject.getSlot(slotFrom.getRow(), slotFrom.getColumn() - 2);
      Slot slotUp = boardObject.getSlot(slotFrom.getRow() - 2, slotFrom.getColumn());
      Slot slotDown = boardObject.getSlot(slotFrom.getRow() + 2, slotFrom.getColumn());

      return ((isValidMove(slotFrom, slotRight, turnColor)) ||
         (isValidMove(slotFrom, slotLeft, turnColor)) ||
         (isValidMove(slotFrom, slotUp, turnColor)) ||
         (isValidMove(slotFrom, slotDown, turnColor)));
   }

   /**
    * Description: Method to verify the validity of the possible chained jump. In a chained jump,
    * the player must move the same piece that they were already moving.
    * Parameters: Slot potentialSlotFrom, which is the potential slot to move from.
    * Slot slotFrom, which is the slot to move from.
    * Returns: Whether the move is valid or not.
    */

   public boolean verifySuccessiveMove(Slot potentialSlotFrom, Slot slotFrom) {
      return ((slotFrom.getRow() == potentialSlotFrom.getRow()) &&
         (slotFrom.getColumn() == potentialSlotFrom.getColumn()));
   }

   /**
    * Description: Method to save the game state.
    * Parameters: String fileName, which is the name of the file to create/write.
    * Context context, which is the context in which to look for internal storage
    * directories.
    * Returns: The name of the full path to the file.
    */

   public String saveGame(String fileName, Context context) {
      File file = new File(context.getFilesDir(), "saved_games");

      if (!file.exists()) {
         file.mkdir();
      }

      File saveFile = new File(file, fileName);

      try {
         PrintWriter writer = new PrintWriter(saveFile.getAbsolutePath(), "UTF-8");
         writer.println("Black: " + playerBlack.getScore());
         writer.println("White: " + playerWhite.getScore());
         writer.println("Board:");

         for (int r = 0; r < Board.MAX_ROW; r++) {
            for (int c = 0; c < Board.MAX_COLUMN; c++) {
               if (boardObject.getSlot(r, c).getColor() == Slot.BLACK) {
                  writer.print("B ");
               } else if (boardObject.getSlot(r, c).getColor() == Slot.WHITE) {
                  writer.print("W ");
               } else {
                  writer.print("O ");
               }
            }
            writer.println();
         }

         String nextPlayer = playerWhite.isTurn() ? "White" : "Black";

         writer.println("Next player: " + nextPlayer);
         writer.close();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return saveFile.getAbsolutePath();
   }

   /**
    * Description: Method to set the game state.
    * Parameters: String filePath, which is the full path of the file that contains the game state.
    *             int boardSize, which is the size of the board.
    * Returns: Nothing.
    */

   public void setGameFromState(String filePath, int boardSize) {
      int whiteScore = 0;
      int blackScore = 0;
      String turn = "black";
      int endBoardLine;

      if (boardSize == 6) {
         endBoardLine = 8;
      } else if (boardSize == 8) {
         endBoardLine = 10;
      } else {
         endBoardLine = 12;
      }

      try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
         String line;
         int lineCounter = 0;

         while ((line = bufferedReader.readLine()) != null) {
            if (lineCounter == 0) {
               blackScore = Integer.parseInt(line.substring(7));
            } else if (lineCounter == 1) {
               whiteScore = Integer.parseInt(line.substring(7));
            } else if (lineCounter >= 3 && lineCounter <= endBoardLine) {
               for (int c = 0; c < line.length(); c += 2) {
                  if (line.charAt(c) == 'B') {
                     boardObject.setSlotColor(boardObject.getSlot(lineCounter - 3, c / 2), Slot.BLACK);
                  } else if (line.charAt(c) == 'W') {
                     boardObject.setSlotColor(boardObject.getSlot(lineCounter - 3, c / 2), Slot.WHITE);
                  } else if (line.charAt(c) == 'O') {
                     boardObject.setSlotColor(boardObject.getSlot(lineCounter - 3, c / 2), Slot.EMPTY);
                  }
               }
            } else if (lineCounter == endBoardLine + 1) {
               turn = line.substring(13);
            }

            lineCounter++;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      playerWhite.setScore(whiteScore);
      playerBlack.setScore(blackScore);

      if (turn.equals("White")) {
         playerWhite.setIsTurn(true);
         playerBlack.setIsTurn(false);
      } else {
         playerBlack.setIsTurn(true);
         playerWhite.setIsTurn(false);
      }
   }

   /**
    * Description: Method to get the black player object.
    * Parameters: None.
    * Returns: The black player object.
    */

   public Player getPlayerBlack() {
      return playerBlack;
   }

   /**
    * Description: Method to get the white player object.
    * Parameters: None.
    * Returns: The white player object.
    */

   public Player getPlayerWhite() {
      return playerWhite;
   }

   /**
    * Description: Method to get the board object.
    * Parameters: None.
    * Returns: The board object.
    */

   public Board getBoardObject() {
      return boardObject;
   }

   /**
    * Description: Method to get the potential successive slot object.
    * Parameters: None.
    * Returns: The potential successive slot object.
    */

   public Slot getPotentialSuccessiveSlot() {
      return potentialSuccessiveSlot;
   }

   /**
    * Description: Method to get the slot from object.
    * Parameters: None.
    * Returns: The slot from object.
    */

   public Slot getSlotFrom() {
      return slotFrom;
   }

   /**
    * Description: Method to set the slot from object.
    * Parameters: Slot slotFrom, which is the slot to set the current slotFrom object with.
    * Returns: Nothing.
    */

   public void setSlotFrom(Slot slotFrom) {
      this.slotFrom = slotFrom;
   }

   /**
    * Description: Method to get the slot to object.
    * Parameters: None.
    * Returns: The slot to object.
    */

   public Slot getSlotTo() {
      return slotTo;
   }

   /**
    * Description: Method to set the slot to object.
    * Parameters: Slot slotTo, which is the slot to set the current slotTo object with.
    * Returns: Nothing.
    */

   public void setSlotTo(Slot slotTo) {
      this.slotTo = slotTo;
   }

   /**
    * Description: Method to get the first click boolean.
    * Parameters: None.
    * Returns: The first click boolean.
    */

   public boolean isFirstClick() {
      return firstClick;
   }

   /**
    * Description: Method to set the first click boolean.
    * Parameters: boolean firstClick, which is the value to set the first click boolean with.
    * Returns: Nothing.
    */

   public void setFirstClick(boolean firstClick) {
      this.firstClick = firstClick;
   }

   /**
    * Description: Method to get the successive move boolean.
    * Parameters: None.
    * Returns: The successive move boolean.
    */

   public boolean isSuccessiveMove() {
      return successiveMove;
   }

   /**
    * Description: Method to set the successive move boolean.
    * Parameters: boolean successiveMove, which is the value to set the successive move boolean with.
    * Returns: Nothing.
    */

   public void setSuccessiveMove(boolean successiveMove) {
      this.successiveMove = successiveMove;
   }

   /**
    * Description: Method to get the turn color.
    * Parameters: None.
    * Returns: The turn color.
    */

   public int getTurnColor() {
      return turnColor;
   }

   /**
    * Description: Method to set the turn color.
    * Parameters: int turnColor, which is the turn color to set the current turn color with.
    * Returns: Nothing.
    */

   public void setTurnColor(int turnColor) {
      this.turnColor = turnColor;
   }

   /**
    * Description: Method to determine whether or not a slot can move.
    * Parameters: Slot slot, which is the slot to check.
    * Returns: If that slot can move or not.
    */

   private boolean slotCanMove(Slot slot) {
      Slot slotRight = boardObject.getSlot(slot.getRow(), slot.getColumn() + 2);
      Slot slotLeft = boardObject.getSlot(slot.getRow(), slot.getColumn() - 2);
      Slot slotUp = boardObject.getSlot(slot.getRow() - 2, slot.getColumn());
      Slot slotDown = boardObject.getSlot(slot.getRow() + 2, slot.getColumn());

      return ((isValidMove(slot, slotRight, turnColor)) || (isValidMove(slot, slotLeft, turnColor)) ||
         (isValidMove(slot, slotUp, turnColor)) || (isValidMove(slot, slotDown, turnColor)));
   }
}