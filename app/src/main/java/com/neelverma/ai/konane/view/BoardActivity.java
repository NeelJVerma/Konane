/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 2 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 2/16/2018                                      *
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
   public static final int MAX_ROW = 6;
   public static final int MAX_COL = 6;
   public static final int NEW_GAME = 0;
   public static final int LOADED_GAME = 1;

   // MAX_ROW + 1 and MAX_COL + 1 to put in labels for rows and columns.
   public ImageButton[][] gameBoard = new ImageButton[MAX_ROW + 1][MAX_COL + 1];
   public Context context;
   public Drawable drawCell[] = new Drawable[5];
   public Drawable drawNumbers[] = new Drawable[6];
   public Game gameObject = new Game();
   public int gameType;

   public TextView playerBlackScore;
   public TextView playerWhiteScore;
   public TextView playerBlackTurn;
   public TextView playerWhiteTurn;

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_board);
      context = this;

      Bundle bundle = getIntent().getExtras();
      gameType = bundle.getInt("gameType");

      loadResources();

      Button removeButton;
      removeButton = findViewById(R.id.removeButton);

      if (gameType == LOADED_GAME) {
         removeButton.setVisibility(View.GONE);
         gameObject.setGameFromState(SaveGameClickListener.getFilePath());
         drawBoardGame();
         startGame();
      } else {
         drawBoardGame();
         removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Button button = (Button) v;
               button.setVisibility(View.GONE);

               startGame();
               removeTwoSlots();
            }
         });
      }
   }

   /**
    * Description: Method to load in resources for the image buttons.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void loadResources() {
      drawCell[0] = context.getResources().getDrawable(R.drawable.board_bg_2);
      drawCell[1] = context.getResources().getDrawable(R.drawable.circle_black);
      drawCell[2] = context.getResources().getDrawable(R.drawable.circle_white);
      drawCell[3] = context.getResources().getDrawable(R.drawable.board_bg);
      drawCell[4] = null;

      drawNumbers[0] = context.getResources().getDrawable(R.drawable.number_one);
      drawNumbers[1] = context.getResources().getDrawable(R.drawable.number_two);
      drawNumbers[2] = context.getResources().getDrawable(R.drawable.number_three);
      drawNumbers[3] = context.getResources().getDrawable(R.drawable.number_four);
      drawNumbers[4] = context.getResources().getDrawable(R.drawable.number_five);
      drawNumbers[5] = context.getResources().getDrawable(R.drawable.number_six);
   }

   /**
    * Description: Method to draw the initial state of the board game.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void drawBoardGame() {
      int sizeOfCell = Math.round(screenWidth() / (MAX_ROW + 1)) - 20;
      LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(sizeOfCell * (MAX_ROW + 1), sizeOfCell);
      LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(sizeOfCell, sizeOfCell);
      LinearLayout boardLayout = findViewById(R.id.boardLayout);

      for (int r = 0; r < MAX_ROW + 1; r++) {
         LinearLayout linRow = new LinearLayout(context);

         for (int c = 0; c < MAX_COL + 1; c++) {
            gameBoard[r][c] = new ImageButton(context);
            gameBoard[r][c].setEnabled(false);

            if (c == 6) {
               gameBoard[r][c].setBackground(drawCell[2]);
            }

            if (gameObject.boardObject.getSlot(r, c).getColor() == Slot.BLACK) {
               gameBoard[r][c].setBackground(drawCell[1]);
            } else if (gameObject.boardObject.getSlot(r, c).getColor() == Slot.WHITE) {
               gameBoard[r][c].setBackground(drawCell[2]);
            } else {
               gameBoard[r][c].setBackground(drawCell[3]);
            }

            linRow.addView(gameBoard[r][c], lpCell);
         }

         boardLayout.addView(linRow, lpRow);
      }

      for (int r = 0; r < MAX_ROW; r++) {
         gameBoard[r][MAX_COL].setBackground(drawNumbers[r]);
      }

      for (int c = 0; c < MAX_COL; c++) {
         gameBoard[MAX_ROW][c].setBackground(drawNumbers[c]);
      }

      gameBoard[6][6].setBackground(drawCell[4]);
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

   private void removeTwoSlots() {
      Pair<Slot, Slot> slotPair = gameObject.removeTwoSlots();
      gameBoard[slotPair.first.getRow()][slotPair.first.getColumn()].setBackground(drawCell[3]);
      gameBoard[slotPair.second.getRow()][slotPair.second.getColumn()].setBackground(drawCell[3]);
   }

   /**
    * Description: Method to enable the game board for playing.
    * Parameters: None.
    * Returns: Nothing.
    */

   public void enableBoard() {
      for (int r = 0; r < MAX_ROW; r++) {
         for (int c = 0; c < MAX_COL; c++) {
            gameBoard[r][c].setEnabled(true);
            gameBoard[r][c].setOnClickListener(new GameBoardClickListener(r, c, BoardActivity.this));
         }
      }
   }

   /**
    * Description: Method to enable all game functionality.
    * Parameters: None.
    * Returns: Nothing.
    */

   public void startGame() {
      playerBlackScore = findViewById(R.id.playerBlackScore);
      playerWhiteScore = findViewById(R.id.playerWhiteScore);
      playerBlackTurn = findViewById(R.id.playerBlackTurn);
      playerWhiteTurn = findViewById(R.id.playerWhiteTurn);

      RelativeLayout boardActivityLayout = findViewById(R.id.boardActivityLayout);
      String blackScore = "BLACK: " + gameObject.playerBlack.getScore();
      String whiteScore = "WHITE: " + gameObject.playerWhite.getScore();

      playerBlackScore.setVisibility(View.VISIBLE);
      playerBlackScore.setText(blackScore.trim());
      playerWhiteScore.setVisibility(View.VISIBLE);
      playerWhiteScore.setText(whiteScore.trim());

      if (gameObject.playerBlack.isTurn()) {
         playerBlackTurn.setVisibility(View.VISIBLE);
      } else {
         playerWhiteTurn.setVisibility(View.VISIBLE);
      }

      boardActivityLayout.setBackgroundColor(Color.parseColor("#4b76ad"));

      Button saveGameButton = findViewById(R.id.saveGameButton);
      saveGameButton.setVisibility(View.VISIBLE);
      saveGameButton.setOnClickListener(new SaveGameClickListener(BoardActivity.this));

      enableBoard();
   }
}