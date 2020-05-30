package shapes;

import java.awt.*;

public class Rectangle extends BaseShape {
    private int height;
    private int width;

   public Rectangle(int x, int y, int height, int width, Color color) {
        super(x, y, color);
        this.height = height;
        this.width = width;
    }

    public Rectangle(Rectangle target) {
        super(target);
        if (target != null){
            this.height = target.height;
            this.width = target.width;
        }
    }

    @Override
    public BaseShape clone() {
        return new Rectangle(this);
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
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.drawRect(x, y, getWidth() - 1, getHeight() - 1);
    }


}
