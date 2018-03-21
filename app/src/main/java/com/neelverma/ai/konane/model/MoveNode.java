package com.neelverma.ai.konane.model;

import java.util.ArrayList;

public class MoveNode {
   private Slot source;
   private Slot dest;
   private int score;
   private int minimaxValue;
   ArrayList<Slot> movePath = new ArrayList<>();

   MoveNode(Slot source, Slot dest){
      this.source = source;
      this.dest = dest;
   }

   public Slot getSource() {
      return source;
   }

   public void setSource(Slot source) {
      this.source = source;
   }

   public Slot getDest() {
      return dest;
   }

   public void setDest(Slot dest) {
      this.dest = dest;
   }

   public int getScore() {
      return score;
   }

   public void setScore(int score) {
      this.score = score;
   }

   public int getMinimaxValue() {
      return minimaxValue;
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