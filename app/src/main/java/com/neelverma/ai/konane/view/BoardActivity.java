package com.neelverma.ai.konane.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.neelverma.ai.konane.R;

public class BoardActivity extends AppCompatActivity {
   private static final int MAX_ROW = 6;
   private static final int MAX_COL = 6;
   private ImageView[][] gameBoard = new ImageView[MAX_ROW][MAX_COL];
   private Context context;
   private Drawable drawCell[] = new Drawable[4];

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_board);
      context = this;
      loadResources();
      drawBoardGame();
   }

   private void loadResources() {
      // empty, black, white, background
      drawCell[0] = null;
      drawCell[1] = context.getResources().getDrawable(R.drawable.circle_black);
      drawCell[2] = context.getResources().getDrawable(R.drawable.circle_white);
      drawCell[3] = context.getResources().getDrawable(R.drawable.board_bg);
   }

   private void drawBoardGame() {
      int sizeOfCell = Math.round(screenWidth() / MAX_ROW) - 20;
      LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(sizeOfCell * MAX_ROW, sizeOfCell);
      LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(sizeOfCell, sizeOfCell);
      LinearLayout boardView = (LinearLayout) findViewById(R.id.boardLayout);

      int cellTracker = 1;
      for (int r = 0; r < MAX_ROW; r++) {
         LinearLayout linRow = new LinearLayout(context);
         for (int c = 0; c < MAX_COL; c++) {
            gameBoard[r][c] = new ImageView(context);
            if (cellTracker == 1) {
               gameBoard[r][c].setBackground(drawCell[1]);
            } else if (cellTracker == -1) {
               gameBoard[r][c].setBackground(drawCell[2]);
            }
            linRow.addView(gameBoard[r][c], lpCell);
            cellTracker = -cellTracker;
         }
         boardView.addView(linRow, lpRow);
         cellTracker = -cellTracker;
      }
   }

   private float screenWidth() {
      Resources resources = context.getResources();
      DisplayMetrics dm = resources.getDisplayMetrics();
      return dm.widthPixels;
   }
}