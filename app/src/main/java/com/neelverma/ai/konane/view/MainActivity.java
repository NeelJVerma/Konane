/************************************************************
 * Name: Neel Verma                                         *
 * Project: Project 1 - Two Player Konane                   *
 * Class: CMPS331 - Artificial Intelligence                 *
 * Date: 2/02/2018                                          *
 ************************************************************/

package com.neelverma.ai.konane.view;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.neelverma.ai.konane.R;

/**
 * Class to hold the beginning screen of the app.
 * Created by Neel on 01/28/2018.
 *
 * It displays the game name and has one button.
 * When this button is clicked, the app will redirect to BoardActivity, where the game will
 * begin.
 */


public class MainActivity extends AppCompatActivity {

   /**
    * Description: Method to create the activity.
    * Parameters: Bundle savedInstanceState, which is the state of the current activity's data. This
    * is used so that, if need be, the activity can restore itself from its previous state.
    * Returns: Nothing.
    */

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      Button beginButton;
      beginButton = findViewById(R.id.beginButton);

      beginButton.setOnClickListener(new View.OnClickListener() {

         /**
          * Description: Method to handle the on click event for the begin button.
          * Parameters: View v, which is the view object of whatever is being clicked.
          * Returns: Nothing.
          */

         @Override
         public void onClick(View v) {
            Intent boardIntent = new Intent(MainActivity.this,
               BoardActivity.class);
            startActivity(boardIntent);
         }
      });
   }
}