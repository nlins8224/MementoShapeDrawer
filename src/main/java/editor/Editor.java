package editor;

import command.Command;
import memento.Caretaker;
import memento.Memento;
import shapes.ShapeManager;

import shapes.Shape;

import javax.swing.*;
import java.io.*;
import java.util.Base64;

public class Editor extends JComponent {
    private final Board canvas;
    public ShapeManager allShapes = new ShapeManager();
    private final Caretaker caretaker;

    public Editor() {
        canvas = new Board(this);
        caretaker = new Caretaker();
    }


    public void loadShapes(Shape... shapes) {
        allShapes.clear();
        allShapes.add(shapes);
        canvas.refresh();
    }

    public void addShape(Shape shape){
        allShapes.add(shape);
        canvas.refresh();
    }

    public void removeShape(Shape shape){
        System.out.println(allShapes);
        allShapes.remove(shape);
    }

    public ShapeManager getShapes() {
        return allShapes;
    }

    public void execute(Command c) {
        caretaker.push(c, new Memento(this));
        c.execute();
    }

    public void undo() {
        if (caretaker.undo())
            canvas.repaint();
    }

    public void redo() {
        if (caretaker.redo())
            canvas.repaint();
    }

    public String backup() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this.allShapes);
            oos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            return "";
        }
    }

    public void restore(String state) {
        try {
            byte[] data = Base64.getDecoder().decode(state);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            this.allShapes = (ShapeManager) ois.readObject();
            ois.close();
        } catch (ClassNotFoundException e) {
            System.out.print("ClassNotFoundException occurred.");
        } catch (IOException e) {
            System.out.print("IOException occurred.");
        }
    }

}
