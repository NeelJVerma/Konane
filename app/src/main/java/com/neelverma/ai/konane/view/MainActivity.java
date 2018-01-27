package com.neelverma.ai.konane.view;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.neelverma.ai.konane.R;

public class MainActivity extends AppCompatActivity {
   Intent boardIntent;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      Button beginButton;
      beginButton = findViewById(R.id.beginButton);

      beginButton.setOnClickListener(new View.OnClickListener() {
         public void onClick(View arg0) {
            boardIntent = new Intent(MainActivity.this,
               BoardActivity.class);
            startActivity(boardIntent);
         }
      });
   }
}
