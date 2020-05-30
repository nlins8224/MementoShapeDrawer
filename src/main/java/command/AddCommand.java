package command;

import editor.Editor;
import shapes.Shape;

public class AddCommand implements Command{
    private final Shape shape;
    private final Editor editor;

    public AddCommand(Shape shape, Editor editor) {
        this.shape = shape;
        this.editor = editor;
    }

    @Override
    public String getName() {
        return "Drawing";
    }

    @Override
    public void execute() {
        editor.addShape(shape);
    }
}
