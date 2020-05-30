package memento;

import command.Command;

import java.util.ArrayList;
import java.util.List;

public class Caretaker {
   private List<Pair> caretaker = new ArrayList<>();
   private int virtualSize = 0;

   private static class Pair {
       Command command;
       Memento memento;
       Pair(Command command, Memento memento){
           this.command = command;
           this.memento = memento;
       }

       private Command getCommand(){
           return command;
       }

       private Memento getMemento(){
           return memento;
       }
   }

   public void push(Command command, Memento memento){
       if (virtualSize != caretaker.size() && virtualSize > 0){
           caretaker = caretaker.subList(0, virtualSize - 1);
       }
       caretaker.add(new Pair(command, memento));
       virtualSize = caretaker.size();
   }

   public boolean undo(){
       Pair pair = getUndo();
       if (pair == null){
           return false;
       }
       System.out.println("Undo: " + pair.getCommand().getName());
       pair.getMemento().restore();
       return true;
   }

   public boolean redo(){
       Pair pair = getRedo();
       if (pair == null){
           return false;
       }
      System.out.println("Redo: " + pair.getCommand().getName());
       pair.getMemento().restore();
       pair.getCommand().execute();
       return true;
   }

   private Pair getUndo(){
       if (virtualSize == 0){
           return null;
       }
       virtualSize = Math.max(0, virtualSize - 1);
       return caretaker.get(virtualSize);
   }

   private Pair getRedo(){
       if (virtualSize == caretaker.size()){
           return null;
       }
       virtualSize = Math.min(caretaker.size(), virtualSize + 1);
       return caretaker.get(virtualSize - 1);
   }
}
