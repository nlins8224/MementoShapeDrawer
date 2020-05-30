import editor.Editor;
import shapes.Circle;

import java.awt.*;

public class App {
    public static void main(String[] args){
        Editor editor = new Editor();
        editor.loadShapes(
                new Circle(10, 10, 1, Color.BLUE),
                        new Circle(600, 600, 1, Color.RED)
        );

    }
}
