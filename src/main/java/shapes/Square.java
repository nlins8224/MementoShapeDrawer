package shapes;

import java.awt.*;

public class Square extends BaseShape{
    private int height;

    public Square(int x, int y, int height, Color color) {
        super(x, y, color);
        this.height = height;
    }

    public Square(Square target) {
        super(target);
        if (target != null){
            this.height = target.height;
        }
    }

    @Override
    public BaseShape clone() {
        return new Square(this);
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return height;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.drawRect(x, y, getHeight() - 1, getHeight() - 1);
    }

}
