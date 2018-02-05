/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 1 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Date: 2/02/2018                                          *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
 * The game will then alternate between black and white until the game is over.
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
   public static final int MAX_ROW = 6; // The max amount of rows in the board.
   public static final int MAX_COL = 6; // The max amount of columns in the board.

   public ImageButton[][] gameBoard = new ImageButton[MAX_ROW][MAX_COL]; // A 2D array of image
   // buttons to hold the game
   // board.
   public Context context; // A context variable to obtain access to resources.
   public Drawable drawCell[] = new Drawable[4]; // An array to hold the different states each cell
   // can be in (empty, white, black, can move).
   public Game gameObject = new Game(); // A game object to enforce game logic on the GUI.

   TextView playerBlackScore;
   TextView playerWhiteScore;
   TextView playerBlackTurn;
   TextView playerWhiteTurn;

   /**
    * Description: Method to create the activity. It handles all GUI changing, resource loading, etc.
    * Parameters: Bundle savedInstanceState, which is the state of the current activity's data. This
    * is used so that, if need be, the activity can restore itself from its previous state.
    * Returns: Nothing.
    */

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_board);
      context = this;

      // Load resources into the game, meaning the drawable cells.
      loadResources();

      // Draw the initial board game of 18 black and 18 white cells.
      drawBoardGame();

      Button removeButton;
      removeButton = findViewById(R.id.removeButton);

      removeButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Button button = (Button) v;
            button.setVisibility(View.GONE);

            playerBlackScore = findViewById(R.id.playerBlackScore);
            playerWhiteScore = findViewById(R.id.playerWhiteScore);
            playerBlackTurn = findViewById(R.id.playerBlackTurn);
            playerWhiteTurn = findViewById(R.id.playerWhiteTurn);

            // Locate the entire layout container for this activity.
            RelativeLayout boardActivityLayout = findViewById(R.id.boardActivityLayout);

            // Show player scores and that it's black's turn, because the player playing black pieces
            // always starts.
            playerBlackScore.setVisibility(View.VISIBLE);
            playerWhiteScore.setVisibility(View.VISIBLE);
            playerBlackTurn.setVisibility(View.VISIBLE);

            // Change the background to get rid of the game name.
            boardActivityLayout.setBackgroundColor(Color.parseColor("#4b76ad"));

            enableBoard();
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
            gameBoard[r][c].setEnabled(false);
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

   public void enableBoard() {
      Pair<Slot, Slot> slotPair = gameObject.removeTwoSlots();
      gameBoard[slotPair.first.getRow()][slotPair.first.getColumn()].setBackground(drawCell[3]);
      gameBoard[slotPair.second.getRow()][slotPair.second.getColumn()].setBackground(drawCell[3]);

      for (int r = 0; r < MAX_ROW; r++) {
         for (int c = 0; c < MAX_COL; c++) {
            gameBoard[r][c].setEnabled(true);
            gameBoard[r][c].setOnClickListener(new GameBoardClickListener(r, c, BoardActivity.this));
         }
      }
   }
}