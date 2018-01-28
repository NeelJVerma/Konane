/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 1 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Date: 2/02/2018                                          *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neelverma.ai.konane.R;
import com.neelverma.ai.konane.model.Board;
import com.neelverma.ai.konane.model.Game;
import com.neelverma.ai.konane.model.Slot;

/**
 * Class to execute changes to the GUI, while using the model as a reference for game rules.
 * Created by Neel on 01/28/2018.
 *
 * Implements the game board as a 2D array of 36 buttons.
 * Each button represents a different black or white piece, or an empty slot.
 * At first, the user is presented with a button that asks them to press to start the game.
 * After this button is pressed, two pieces will get removed from the board.
 * The game will then alternate between black and white players until the game is over.
 * At each step, the score of the player moving is updated to reflect their most current score.
 * Additionally at every click, the model is verifying whether the move being made is valid or not.
 * Some examples of invalid moves that are caught are a player moving out of turn, a player moving
 * a different piece during a multi-jump turn, or a player trying to move onto or from an empty slot.
 * Each invalid move notifies the player(s) with a dialog box.
 * If a player can't move, they must pass the turn. This is also communicated to the player(s) via
 * dialog box.
 * After the game ends, a dialog box pops up letting the player(s) know who won. On this dialog box,
 * there is only one option, which when pressed, will bring the game back to the title screen.
 * From here, they can play again or just exit the app.
 */

public class BoardActivity extends AppCompatActivity {
   // Symbolic constants.
   private static final int MAX_ROW = 6; // The max amount of rows in the board.
   private static final int MAX_COL = 6; // The max amount of columns in the board.

   private ImageButton[][] gameBoard = new ImageButton[MAX_ROW][MAX_COL]; // A 2D array of image
                                                                          // buttons to hold the game
                                                                          // board.
   private Context context; // A context variable to obtain access to resources.
   private Drawable drawCell[] = new Drawable[4]; // An array to hold the different states each cell
                                                  // can be in (empty, white, black, can move).
   private Game gameObject = new Game(); // A game object to enforce game logic on the GUI.

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_board);
      context = this;

      // Load resources into the game, meaning the drawable cells.
      loadResources();

      // Draw the initial board game of 18 black and 18 white cells.
      drawBoardGame();

      // Can't press the buttons until the button to start the game is pressed.
      for (int r = 0; r < MAX_ROW; r++) {
         for (int c = 0; c < MAX_COL; c++) {
            gameBoard[r][c].setEnabled(false);
         }
      }

      Button removeButton;
      removeButton = findViewById(R.id.removeButton);

      removeButton.setOnClickListener(new View.OnClickListener() {
         // Invalid board parameters to start. These are used in move verification.
         private int rowFrom = -1;
         private int columnFrom = -1;
         private int rowTo = -1;
         private int columnTo = -1;
         private Slot potentialSuccessiveSlot = new Slot(Board.MAX_ROW, Board.MAX_COLUMN, 2);
         Slot slotFrom = gameObject.boardObject.getSlot(rowFrom, columnFrom);
         Slot slotTo = gameObject.boardObject.getSlot(rowTo, columnTo);

         private boolean firstClick = true; // Boolean to verify if a click is the first or second.

         @Override
         public void onClick(View v) {
            // When the button to start the game is pressed, make it disappear, remove two slots,
            // make the 36 game board buttons enabled, pop up the scores of the two players, and
            // tell whose turn it is.
            Button button = (Button) v;
            button.setVisibility(View.GONE);
            final TextView playerBlackScore = findViewById(R.id.playerBlackScore);
            final TextView playerWhiteScore = findViewById(R.id.playerWhiteScore);
            final TextView playerBlackTurn = findViewById(R.id.playerBlackTurn);
            final TextView playerWhiteTurn = findViewById(R.id.playerWhiteTurn);
            RelativeLayout boardActivityLayout = findViewById(R.id.boardActivityLayout);
            playerBlackScore.setVisibility(View.VISIBLE);
            playerWhiteScore.setVisibility(View.VISIBLE);
            playerBlackTurn.setVisibility(View.VISIBLE);
            boardActivityLayout.setBackgroundResource(R.mipmap.board_background_2);
            Pair<Slot, Slot> removePair = gameObject.removeTwoSlots();
            int removeRowOne = removePair.first.getRow();
            int removeColumnOne = removePair.first.getColumn();
            int removeRowTwo = removePair.second.getRow();
            int removeColumnTwo = removePair.second.getColumn();
            gameBoard[removeRowOne][removeColumnOne].setBackground(drawCell[3]);
            gameBoard[removeRowTwo][removeColumnTwo].setBackground(drawCell[3]);

            // After game has started, enable all game board buttons.
            for (int r = 0; r < MAX_ROW; r++) {
               for (int c = 0; c < MAX_COL; c++) {
                  gameBoard[r][c].setEnabled(true);
               }
            }

            // Set on click listeners for all 36 game buttons.
            for (int r = 0; r < MAX_ROW; r++) {
               for (int c = 0; c < MAX_COL; c++) {
                  final int finalR = r;
                  final int finalC = c;

                  gameBoard[r][c].setOnClickListener(new View.OnClickListener() {

                     /**
                      * Description: Method to simulate one turn in the game.
                      * Parameters: None.
                      * Returns: Whether or not the turn was executed successfully or not.
                      */

                     private boolean simulateTurn() {
                        int turnColor = gameObject.playerWhite.isTurn() ? Slot.WHITE : Slot.BLACK;

                        // If the first button in the turn was clicked, verify whether or not the
                        // player is clicking their own color piece or not an empty slot.
                        if (firstClick) {
                           rowFrom = finalR;
                           columnFrom = finalC;
                           slotFrom = gameObject.boardObject.getSlot(rowFrom, columnFrom);

                           if (slotFrom.getColor() != turnColor) {
                              AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                              if (slotFrom.getColor() == Slot.EMPTY) {
                                 builder.setMessage("CANNOT MOVE AN EMPTY SLOT");
                              } else {
                                 builder.setMessage("NOT YOUR TURN");
                              }
                              builder.setCancelable(false)
                                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                       return;
                                    }
                                 });

                              AlertDialog alert = builder.create();
                              alert.show();

                              return false;
                           }

                           firstClick = false;

                           // We return because the following code only pertains to whether or not
                           // a second click was made.
                           return false;
                        }

                        firstClick = true;

                        rowTo = finalR;
                        columnTo = finalC;
                        slotTo = gameObject.boardObject.getSlot(rowTo, columnTo);

                        // Check that if a successive move is able to be made for the player playing
                        // black pieces, they started moving the same piece and didn't switch pieces
                        // mid turn.
                        if (gameObject.playerBlack.isTurn()) {
                           if ((gameObject.canMoveAgain(potentialSuccessiveSlot, gameObject.playerBlack)) &&
                              (!gameObject.verifySuccessiveMove(slotFrom, potentialSuccessiveSlot))) {
                              AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                              builder.setMessage("YOU MUST START FROM THE POSITION YOU ENDED ON")
                                 .setCancelable(false)
                                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                       return;
                                    }
                                 });

                              AlertDialog alert = builder.create();
                              alert.show();

                              return false;
                           }
                        }

                        // Check that if a successive move is able to be made for the player playing
                        // white pieces, they started moving the same piece and didn't switch pieces
                        // mid turn.
                        if (gameObject.playerWhite.isTurn()) {
                           if ((gameObject.canMoveAgain(potentialSuccessiveSlot, gameObject.playerWhite)) &&
                              (!gameObject.verifySuccessiveMove(slotFrom, potentialSuccessiveSlot))) {
                              AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                              builder.setMessage("YOU MUST START FROM THE POSITION YOU ENDED ON")
                                 .setCancelable(false)
                                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                       return;
                                    }
                                 });

                              AlertDialog alert = builder.create();
                              alert.show();

                              return false;
                           }
                        }

                        // Verify whether or not the move made was valid according to in bounds
                        // verification, four direction verification, and color verification (see the
                        // makeMove and isValidMove functions in the Game class).
                        if (!gameObject.makeMove(slotFrom, slotTo)) {
                           AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                           builder.setMessage("INVALID MOVE")
                              .setCancelable(false)
                              .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                    return;
                                 }
                              });

                           AlertDialog alert = builder.create();
                           alert.show();

                           return false;
                        }

                        // If the function got here, the first move in a possible multi-jump move was
                        // successful.
                        gameBoard[slotFrom.getRow()][slotFrom.getColumn()].setBackground(drawCell[3]);

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
                           gameBoard[slotFrom.getRow()][slotFrom.getColumn() + 1].setBackground(drawCell[3]);
                        } else if (directionMoving == "left") {
                           gameBoard[slotFrom.getRow()][slotFrom.getColumn() - 1].setBackground(drawCell[3]);
                        } else if (directionMoving == "down") {
                           gameBoard[slotFrom.getRow() + 1][slotFrom.getColumn()].setBackground(drawCell[3]);
                        } else if (directionMoving == "up") {
                           gameBoard[slotFrom.getRow() - 1][slotFrom.getColumn()].setBackground(drawCell[3]);
                        }

                        Drawable draw;

                        if (gameObject.boardObject.getSlot(slotTo.getRow(), slotTo.getColumn()).getColor() == Slot.WHITE) {
                           draw = drawCell[2];
                        } else {
                           draw = drawCell[1];
                        }

                        gameBoard[slotTo.getRow()][slotTo.getColumn()].setBackground(draw);

                        // If it is the black player's turn, add to his/her score. Then check if
                        // the player playing white pieces can move. If not, don't switch the turns.
                        // Then, check if the black player can move again. If they can, set their
                        // successive slot to be the current slot they just moved to. This way, we can
                        // verify that they are the same when we come back for another click. If none
                        // of those conditions are hit, switch the turns. Do the same thing for the
                        // white player.
                        if (gameObject.playerBlack.isTurn()) {
                           gameObject.playerBlack.addToScore();
                           String text = "BLACK: " + gameObject.playerBlack.getScore();
                           playerBlackScore.setText(text.trim());
                           if (!gameObject.playerCanMove(gameObject.playerWhite) && gameObject.playerCanMove(gameObject.playerBlack)) {
                              AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                              builder.setMessage("WHITE CANNOT MOVE")
                                 .setCancelable(false)
                                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                       return;
                                    }
                                 });

                              AlertDialog alert = builder.create();
                              alert.show();

                              return false;
                           }

                           if (gameObject.canMoveAgain(slotTo, gameObject.playerBlack)) {
                              potentialSuccessiveSlot.setRow(rowTo);
                              potentialSuccessiveSlot.setColumn(columnTo);
                              potentialSuccessiveSlot.setColor(Slot.BLACK);
                              return false;
                           }

                           gameObject.playerWhite.setIsTurn(true);
                           gameObject.playerBlack.setIsTurn(false);
                        } else {
                           gameObject.playerWhite.addToScore();
                           String text = "WHITE: " + gameObject.playerWhite.getScore();
                           playerWhiteScore.setText(text.trim());
                           if (!gameObject.playerCanMove(gameObject.playerBlack) && gameObject.playerCanMove(gameObject.playerWhite)) {
                              AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                              builder.setMessage("BLACK CANNOT MOVE")
                                 .setCancelable(false)
                                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                       return;
                                    }
                                 });

                              AlertDialog alert = builder.create();
                              alert.show();

                              return false;
                           }

                           if (gameObject.canMoveAgain(slotTo, gameObject.playerWhite)) {
                              potentialSuccessiveSlot.setRow(rowTo);
                              potentialSuccessiveSlot.setColumn(columnTo);
                              potentialSuccessiveSlot.setColor(Slot.WHITE);
                              return false;
                           }

                           gameObject.playerWhite.setIsTurn(false);
                           gameObject.playerBlack.setIsTurn(true);
                        }

                        return true;
                     }

                     @Override
                     public void onClick(View v) {
                        // If the simulated turn wasn't successful, go for another one.
                        if (!simulateTurn()) {
                           return;
                        }

                        // Set whose turn it is in the GUI.
                        if (gameObject.playerBlack.isTurn()) {
                           playerBlackTurn.setVisibility(View.VISIBLE);
                           playerWhiteTurn.setVisibility(View.INVISIBLE);
                        } else if (gameObject.playerWhite.isTurn()) {
                           playerBlackTurn.setVisibility(View.INVISIBLE);
                           playerWhiteTurn.setVisibility(View.VISIBLE);
                        }

                        // If the game is over, set both turn labels as invisible, because it is no
                        // one's turn. Pop up a dialog box displaying who won or if there was a draw.
                        // Once it is clicked, return to MainActivity.
                        if (!gameObject.playerCanMove(gameObject.playerWhite) && !gameObject.playerCanMove(gameObject.playerBlack)) {
                           playerBlackTurn.setVisibility(View.INVISIBLE);
                           playerWhiteTurn.setVisibility(View.INVISIBLE);

                           AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                           String winner;
                           if (gameObject.playerWhite.getScore() > gameObject.playerBlack.getScore()) {
                              winner = "WHITE PLAYER WINS!";
                           } else if (gameObject.playerWhite.getScore() < gameObject.playerBlack.getScore()) {
                              winner = "BLACK PLAYER WINS!";
                           } else {
                              winner = "IT'S A DRAW.";
                           }

                           builder.setMessage("GAME OVER. " + winner)
                              .setCancelable(false)
                              .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                    Intent mainIntent = new Intent(BoardActivity.this,
                                       MainActivity.class);
                                    startActivity(mainIntent);
                                 }
                              });

                           AlertDialog alert = builder.create();
                           alert.show();
                        }
                     }
                  });
               }
            }
         }
      });
   }

   /**
    * Description: Method to load in resources for the image buttons.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void loadResources() {
      // Can move, black, white, or empty.
      drawCell[0] = context.getResources().getDrawable(R.drawable.board_bg_2);
      drawCell[1] = context.getResources().getDrawable(R.drawable.circle_black);
      drawCell[2] = context.getResources().getDrawable(R.drawable.circle_white);
      drawCell[3] = context.getResources().getDrawable(R.drawable.board_bg);
   }

   /**
    * Description: Method to draw the initial state of the board game.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void drawBoardGame() {
      int sizeOfCell = Math.round(screenWidth() / MAX_ROW) - 20;
      LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(sizeOfCell * MAX_ROW, sizeOfCell);
      LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(sizeOfCell, sizeOfCell);
      LinearLayout boardLayout = findViewById(R.id.boardLayout);

      for (int r = 0; r < MAX_ROW; r++) {
         LinearLayout linRow = new LinearLayout(context);
         for (int c = 0; c < MAX_COL; c++) {
            gameBoard[r][c] = new ImageButton(context);
            if (gameObject.boardObject.getSlot(r, c).getColor() == Slot.BLACK) {
               gameBoard[r][c].setBackground(drawCell[1]);
            } else if (gameObject.boardObject.getSlot(r, c).getColor() == Slot.WHITE) {
               gameBoard[r][c].setBackground(drawCell[2]);
            }
            linRow.addView(gameBoard[r][c], lpCell);
         }
         boardLayout.addView(linRow, lpRow);
      }
   }

   /**
    * Description: Method to get the device's screen width. This is used when finding the right size
    * for the cells in the above method.
    * Parameters: None.
    * Returns: The screen width of the device, in pixels.
    */

   private float screenWidth() {
      Resources resources = context.getResources();
      DisplayMetrics dm = resources.getDisplayMetrics();
      return dm.widthPixels;
   }
}