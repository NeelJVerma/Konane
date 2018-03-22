package com.neelverma.ai.konane.model;

import java.util.ArrayList;

public class MoveNode {
   private Slot source;
   private Slot dest;
   private int score;
   private int minimaxValue;
   private ArrayList<Slot> movePath = new ArrayList<>();

   MoveNode(Slot source, Slot dest){
      this.source = source;
      this.dest = dest;
   }

   public int getScore() {
      return score;
   }

   public void setMinimaxValue(int minimaxValue) {
      this.minimaxValue = minimaxValue;
   }

   public ArrayList<Slot> getMovePath() {
      return movePath;
   }

   public void setMovePath(ArrayList<Slot> movePath) {
      score = movePath.size() - 1;
      this.movePath = movePath;
   }
}