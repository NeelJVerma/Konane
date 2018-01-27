package com.neelverma.ai.konane.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.neelverma.ai.konane.R;
import com.neelverma.ai.konane.model.Board;
import com.neelverma.ai.konane.model.Game;
import com.neelverma.ai.konane.model.Player;
import com.neelverma.ai.konane.model.Slot;

import java.util.Random;

public class BoardActivity extends AppCompatActivity {
   private static final int MAX_ROW = 6;
   private static final int MAX_COL = 6;
   private ImageButton[][] gameBoard = new ImageButton[MAX_ROW][MAX_COL];
   private Context context;
   private Drawable drawCell[] = new Drawable[4];
   private Game gameObject = new Game();
   private int rowFrom = -1;
   private int columnFrom = -1;
   private int rowTo = -1;
   private int columnTo = -1;
   private int moveCounter = 0;
   private Slot potentialSuccessiveSlot = new Slot(Board.MAX_ROW, Board.MAX_COLUMN, 2);
   Slot slotFrom = gameObject.boardObject.getSlot(rowFrom, columnFrom);
   Slot slotTo = gameObject.boardObject.getSlot(rowTo, columnTo);

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_board);
      context = this;
      loadResources();
      drawBoardGame();

      for (int r = 0; r < MAX_ROW; r++) {
         for (int c = 0; c < MAX_COL; c++) {
            gameBoard[r][c].setEnabled(false);
         }
      }

      Button removeButton;
      removeButton = findViewById(R.id.removeButton);

      removeButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Button button = (Button) v;
            button.setVisibility(View.GONE);
            Pair<Slot, Slot> removePair = gameObject.removeTwoSlots();
            int removeRowOne = removePair.first.getRow();
            int removeColumnOne = removePair.first.getColumn();
            int removeRowTwo = removePair.second.getRow();
            int removeColumnTwo = removePair.second.getColumn();
            gameBoard[removeRowOne][removeColumnOne].setBackground(drawCell[3]);
            gameBoard[removeRowTwo][removeColumnTwo].setBackground(drawCell[3]);

            for (int r = 0; r < MAX_ROW; r++) {
               for (int c = 0; c < MAX_COL; c++) {
                  gameBoard[r][c].setEnabled(true);
                  gameBoard[r][c].setClickable(false);
                  gameBoard[r][c].setFocusable(false);
               }
            }

            for (int r = 0; r < MAX_ROW; r++) {
               for (int c = 0; c < MAX_COL; c++) {
                  final int finalR = r;
                  final int finalC = c;
                  gameBoard[r][c].setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                        System.out.println("Player black turn: " + gameObject.playerBlack.isTurn());
                        System.out.println("Player white turn: " + gameObject.playerWhite.isTurn());
                        if (gameObject.playerWhite.isTurn()) {
                           System.out.println("WHITE TURN");
                           if (!gameObject.playerCanMove(gameObject.playerWhite)) {
                              System.out.println("WHITE CAN'T MOVE");
                              gameObject.playerBlack.setIsTurn(true);
                              gameObject.playerWhite.setIsTurn(false);
                              moveCounter = 0;
                              return;
                           }
                        } else {
                           System.out.println("BLACK TURN");
                           if (!gameObject.playerCanMove(gameObject.playerBlack)) {
                              System.out.println("BLACK CAN'T MOVE");
                              gameObject.playerWhite.setIsTurn(true);
                              gameObject.playerBlack.setIsTurn(false);
                              moveCounter = 0;
                              return;
                           }
                        }

                        if (moveCounter == 0) {
                           rowFrom = finalR;
                           columnFrom = finalC;
                           slotFrom = gameObject.boardObject.getSlot(rowFrom, columnFrom);
                        } else if (moveCounter == 1) {
                           rowTo = finalR;
                           columnTo = finalC;
                           slotTo = gameObject.boardObject.getSlot(rowTo, columnTo);
                           moveCounter = -1;
                        }

                        if (moveCounter == -1) {
                           System.out.println("Row from: " + rowFrom + " Column from: " + columnFrom);
                           System.out.println("Row to: " + rowTo + " Column to: " + columnTo);

                           if (gameObject.playerWhite.isTurn()) {
                              if ((gameObject.canMoveAgain(potentialSuccessiveSlot, gameObject.playerWhite)) &&
                                 (!gameObject.verifySuccessiveMove(slotFrom, potentialSuccessiveSlot))) {
                                 System.out.println("MUST START FROM POSITION YOU ENDED ON");
                                 moveCounter = 0;
                                 return;
                              }
                           } else if (gameObject.playerBlack.isTurn()) {
                              if ((gameObject.canMoveAgain(potentialSuccessiveSlot, gameObject.playerBlack)) &&
                                 (!gameObject.verifySuccessiveMove(slotFrom, potentialSuccessiveSlot))) {
                                 System.out.println("MUST START FROM POSITION YOU ENDED ON");
                                 moveCounter = 0;
                                 return;
                              }
                           }

                           if ((gameObject.playerWhite.isTurn()) &&
                              (gameObject.boardObject.getSlot(rowFrom, columnFrom).getColor() != Slot.WHITE)) {
                              System.out.println("NOT A VALID MOVE");
                              moveCounter = 0;
                              return;
                           }

                           if ((gameObject.playerBlack.isTurn()) &&
                              (gameObject.boardObject.getSlot(rowFrom, columnFrom).getColor() != Slot.BLACK)) {
                              System.out.println("NOT A VALID MOVE");
                              moveCounter = 0;
                              return;
                           }

                           if (gameObject.makeMove(slotFrom, slotTo)) {
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

                              Drawable draw = drawCell[0];

                              if (gameObject.boardObject.getSlot(slotTo.getRow(), slotTo.getColumn()).getColor() == Slot.WHITE) {
                                 draw = drawCell[2];
                              } else if (gameObject.boardObject.getSlot(slotTo.getRow(), slotTo.getColumn()).getColor() == Slot.BLACK) {
                                 draw = drawCell[1];
                              }

                              gameBoard[slotTo.getRow()][slotTo.getColumn()].setBackground(draw);
                           } else {
                              System.out.println("NOT A VALID MOVE");
                              moveCounter = 0;
                              return;
                           }

                           if (gameObject.playerWhite.isTurn()) {
                              gameObject.playerWhite.addToScore();

                              if (gameObject.canMoveAgain(slotTo, gameObject.playerWhite)) {
                                 potentialSuccessiveSlot.setRow(rowTo);
                                 potentialSuccessiveSlot.setColumn(columnTo);
                                 potentialSuccessiveSlot.setColor(Slot.WHITE);
                                 moveCounter = 0;
                                 return;
                              }

                              gameObject.playerBlack.setIsTurn(true);
                              gameObject.playerWhite.setIsTurn(false);
                           } else if (gameObject.playerBlack.isTurn()) {
                              gameObject.playerBlack.addToScore();

                              if (gameObject.canMoveAgain(slotTo, gameObject.playerBlack)) {
                                 potentialSuccessiveSlot.setRow(rowTo);
                                 potentialSuccessiveSlot.setColumn(columnTo);
                                 potentialSuccessiveSlot.setColor(Slot.BLACK);
                                 moveCounter = 0;
                                 return;
                              }

                              gameObject.playerWhite.setIsTurn(true);
                              gameObject.playerBlack.setIsTurn(false);
                           }
                        }

                        moveCounter++;
                        gameObject.boardObject.printBoard();
                        System.out.println("Player black score: " + gameObject.playerBlack.getScore());
                        System.out.println("Player white score: " + gameObject.playerWhite.getScore());

                        if (!gameObject.playerCanMove(gameObject.playerWhite) && !gameObject.playerCanMove(gameObject.playerBlack)) {
                           System.out.println("GAME OVER");
                        }
                     }
                  });
               }
            }
         }
      });
   }

   private void loadResources() {
      // can move, black, white, background
      drawCell[0] = context.getResources().getDrawable(R.drawable.board_bg_2);
      drawCell[1] = context.getResources().getDrawable(R.drawable.circle_black);
      drawCell[2] = context.getResources().getDrawable(R.drawable.circle_white);
      drawCell[3] = context.getResources().getDrawable(R.drawable.board_bg);
   }

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

   private float screenWidth() {
      Resources resources = context.getResources();
      DisplayMetrics dm = resources.getDisplayMetrics();
      return dm.widthPixels;
   }
}