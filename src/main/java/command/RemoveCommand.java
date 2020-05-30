package command;

import editor.Editor;
import shapes.Shape;

public class RemoveCommand implements Command{
    private final Shape shape;
    private final Editor editor;

    public RemoveCommand(Shape shape, Editor editor) {
        this.shape = shape;
        this.editor = editor;
    }

    @Override
    public String getName() {
        return "Removing";
    }

    @Override
    public void execute() {
        editor.removeShape(shape);
    }
}
