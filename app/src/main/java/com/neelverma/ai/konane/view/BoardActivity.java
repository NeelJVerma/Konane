/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 3 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Due Date: 3/27/2018                                      *
 ************************************************************/

package com.neelverma.ai.konane.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neelverma.ai.konane.R;
import com.neelverma.ai.konane.model.Board;
import com.neelverma.ai.konane.model.Game;
import com.neelverma.ai.konane.model.MoveNode;
import com.neelverma.ai.konane.model.Slot;

import java.nio.channels.FileChannel;
import java.util.ArrayList;

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
   public static int MAX_ROW;
   public static int MAX_COL;
   public static final int NEW_GAME = 0;
   public static final int LOADED_GAME = 1;

   private ImageButton[][] gameBoard = new ImageButton[MAX_ROW][MAX_COL];
   private Context context;
   private Drawable drawCell[] = new Drawable[5];
   private Game gameObject = new Game();

   private TextView playerBlackScore;
   private TextView playerWhiteScore;
   private TextView playerBlackTurn;
   private TextView playerWhiteTurn;

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_board);
      context = this;

      Bundle bundle = getIntent().getExtras();
      int gameType = bundle.getInt("gameType");

      loadResources();

      Button removeButton;
      removeButton = findViewById(R.id.removeButton);

      if (gameType == LOADED_GAME) {
         removeButton.setVisibility(View.INVISIBLE);

         gameObject.setGameFromState(SaveGameButtonClickListener.getFilePath(), MAX_ROW);

         drawBoardGame();
         startGame();
      } else {
         String guess = bundle.getString("guess");

         int guessRow = Character.getNumericValue(guess.charAt(0));
         int guessCol = Character.getNumericValue(guess.charAt(3));

         drawBoardGame();
         removeButton.setOnClickListener(new RemoveButtonClickListener(BoardActivity.this, guessRow, guessCol));
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
   }

   /**
    * Description: Method to draw the initial state of the board game.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void drawBoardGame() {
      int sizeOfCell = Math.round(screenWidth() / (MAX_ROW)) - 20;
      LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(sizeOfCell * (MAX_ROW), sizeOfCell);
      LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(sizeOfCell, sizeOfCell);
      LinearLayout boardLayout = findViewById(R.id.boardLayout);

      for (int r = 0; r < MAX_ROW; r++) {
         LinearLayout linRow = new LinearLayout(context);

         for (int c = 0; c < MAX_COL; c++) {
            gameBoard[r][c] = new ImageButton(context);
            gameBoard[r][c].setEnabled(false);

            if (gameObject.getBoardObject().getSlot(r, c).getColor() == Slot.BLACK) {
               gameBoard[r][c].setBackground(drawCell[1]);
            } else if (gameObject.getBoardObject().getSlot(r, c).getColor() == Slot.WHITE) {
               gameBoard[r][c].setBackground(drawCell[2]);
            } else {
               gameBoard[r][c].setBackground(drawCell[3]);
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

   /**
    * Description: Method to remove two slots from the board GUI. This is essentially a wrapper for the
    * Game.removeTwoSlots method.
    * Parameters: None.
    * Returns: Nothing.
    */

   public void removeTwoSlots(int row, int col) {
      Pair<Slot, Slot> slotPair = gameObject.removeTwoSlots(row, col);

      gameBoard[slotPair.first.getRow()][slotPair.first.getColumn()].setBackground(drawCell[3]);
      gameBoard[slotPair.second.getRow()][slotPair.second.getColumn()].setBackground(drawCell[3]);
   }

   /**
    * Description: Method to enable the game board for playing.
    * Parameters: None.
    * Returns: Nothing.
    */

   private void enableBoard() {
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
      String blackScore = "BLACK: " + gameObject.getPlayerBlack().getScore();
      String whiteScore = "WHITE: " + gameObject.getPlayerWhite().getScore();

      playerBlackScore.setVisibility(View.VISIBLE);
      playerBlackScore.setText(blackScore.trim());
      playerWhiteScore.setVisibility(View.VISIBLE);
      playerWhiteScore.setText(whiteScore.trim());

      if (gameObject.getPlayerBlack().isTurn()) {
         playerBlackTurn.setVisibility(View.VISIBLE);
      } else {
         playerWhiteTurn.setVisibility(View.VISIBLE);
      }

      boardActivityLayout.setBackgroundColor(Color.parseColor("#4b76ad"));

      Button saveGameButton = findViewById(R.id.saveGameButton);
      saveGameButton.setVisibility(View.VISIBLE);
      saveGameButton.setOnClickListener(new SaveGameButtonClickListener(BoardActivity.this));

      Button moveButton = findViewById(R.id.moveButton);
      moveButton.setVisibility(View.VISIBLE);
      moveButton.setOnClickListener(new MoveButtonClickListener(BoardActivity.this));

      Button hintButton = findViewById(R.id.hintButton);
      hintButton.setVisibility(View.VISIBLE);
      hintButton.setOnClickListener(new HintButtonClickListener(BoardActivity.this));

      TextView plyCutoffEditText = findViewById(R.id.plyCutoffEditText);
      plyCutoffEditText.setVisibility(View.VISIBLE);

      CheckBox alphaBetaCheckBox = findViewById(R.id.alphaBetaCheckBox);
      alphaBetaCheckBox.setVisibility(View.VISIBLE);

      enableBoard();
   }

   /**
    * Description: Method to get the game board.
    * Parameters: None.
    * Returns: The game board.
    */

   public ImageButton[][] getGameBoard() {
      return gameBoard;
   }

   /**
    * Description: Method to get the array of board drawables.
    * Parameters: None.
    * Returns: The drawcell array.
    */

   public Drawable[] getDrawCell() {
      return drawCell;
   }

   /**
    * Description: Method to get the game object.
    * Parameters: None.
    * Returns: The game object.
    */

   public Game getGameObject() {
      return gameObject;
   }

   /**
    * Description: Method to get the black player score text view.
    * Parameters: None.
    * Returns: The player black score text view.
    */

   public TextView getPlayerBlackScore() {
      return playerBlackScore;
   }

   /**
    * Description: Method to get the white player score text view.
    * Parameters: None.
    * Returns: The player white score text view.
    */

   public TextView getPlayerWhiteScore() {
      return playerWhiteScore;
   }

   /**
    * Description: Method to get the black player turn text view.
    * Parameters: None.
    * Returns: The player black turn text view.
    */

   public TextView getPlayerBlackTurn() {
      return playerBlackTurn;
   }

   /**
    * Description: Method to get the white player turn text view.
    * Parameters: None.
    * Returns: The player white turn text view.
    */

   public TextView getPlayerWhiteTurn() {
      return playerWhiteTurn;
   }

   /**
    * Description: Method to show the blinking stones of the best move.
    * Parameters: MoveNode moveNode, which is the best move produced by the minimax algorithm.
    * Returns: Nothing.
    */

   public void showMoveFromMinimax(MoveNode moveNode) {
      for (Slot s : moveNode.getMovePath()) {
         Animation animation = new AlphaAnimation(1, 0);
         animation.setDuration(300);
         animation.setInterpolator(new LinearInterpolator());
         animation.setRepeatCount(Animation.INFINITE);
         animation.setRepeatMode(Animation.REVERSE);

         gameBoard[s.getRow()][s.getColumn()].startAnimation(animation);
      }
   }

   /**
    * Description: Method to stop the blinking stones of the best move.
    * Parameters: MoveNode moveNode, which is the best move produced by the minimax algorithm.
    * Returns: Nothing.
    */

   public void stopMoveAnimation(MoveNode moveNode) {
      for (Slot s : moveNode.getMovePath()) {
         gameBoard[s.getRow()][s.getColumn()].clearAnimation();
      }
   }

   /**
    * Description: Method to redraw the board after the computer has made their move.
    * Parameters: None.
    * Returns: Nothing.
    */

   public void reDrawBoard() {
      for (int r = 0; r < MAX_ROW; r++) {
         for (int c = 0; c < MAX_COL; c++) {
            if (gameObject.getBoardObject().getSlot(r, c).getColor() == Slot.BLACK) {
               gameBoard[r][c].setBackground(drawCell[1]);
            } else if (gameObject.getBoardObject().getSlot(r, c).getColor() == Slot.WHITE) {
               gameBoard[r][c].setBackground(drawCell[2]);
            } else {
               gameBoard[r][c].setBackground(drawCell[3]);
            }
         }
      }
   }

   /**
    * Description: Method to redraw the turns after the computer has made their move.
    * Parameters: None.
    * Returns: Nothing.
    */

   public void reDrawTurns() {
      if (gameObject.getPlayerBlack().isTurn()) {
         getPlayerBlackTurn().setVisibility(View.VISIBLE);
         getPlayerWhiteTurn().setVisibility(View.INVISIBLE);
      } else {
         getPlayerBlackTurn().setVisibility(View.INVISIBLE);
         getPlayerWhiteTurn().setVisibility(View.VISIBLE);
      }
   }

   /**
    * Description: Method to redraw the scores after the computer has made their move.
    * Parameters: None.
    * Returns: Nothing.
    */

   public void reDrawScores() {
      String blackText = "BLACK: " + gameObject.getPlayerBlack().getScore();
      playerBlackScore.setText(blackText.trim());

      String whiteText = "WHITE: " + gameObject.getPlayerWhite().getScore();
      playerWhiteScore.setText(whiteText.trim());
   }

   /**
    * Description: Method to show the blinking score of the best move.
    * Parameters: MoveNode moveNode, which is the best move produced by the minimax algorithm.
    * Returns: Nothing.
    */

   public void showScoresFromMinimax(MoveNode moveNode) {
      Animation animation = new AlphaAnimation(1, 0);
      animation.setDuration(300);
      animation.setInterpolator(new LinearInterpolator());
      animation.setRepeatCount(Animation.INFINITE);
      animation.setRepeatMode(Animation.REVERSE);

      if (gameObject.getTurnColor() == Slot.BLACK) {
         String blackText = "BLACK: " + (gameObject.getPlayerBlack().getScore() + moveNode.getScore());
         playerBlackScore.setText(blackText.trim());
         playerBlackScore.startAnimation(animation);
      } else {
         String whiteText = "WHITE: " + (gameObject.getPlayerWhite().getScore() + moveNode.getScore());
         playerWhiteScore.setText(whiteText.trim());
         playerWhiteScore.startAnimation(animation);
      }
   }

   /**
    * Description: Method to stop the score animation of the best move.
    * Parameters: None.
    * Returns: Nothing.
    */

   public void stopScoreAnimation() {
      reDrawScores();

      playerBlackScore.clearAnimation();
      playerWhiteScore.clearAnimation();
   }
}