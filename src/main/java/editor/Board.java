package editor;

import command.AddCommand;
import command.MoveCommand;

import command.RemoveCommand;
import command.SelectedEvent;
import shapes.*;
import shapes.Rectangle;
import shapes.Shape;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static command.SelectedEvent.*;

public class Board extends Canvas implements ActionListener {
    private final Editor editor;
    private JFrame frame;
    private static final int PADDING = 5;
    private static SelectedEvent selectedEvent = NONE;

    private Circle circleToClone;
    private Square squareToClone;
    private Rectangle rectangleToClone;

    JButton circle;
    JButton square;
    JButton rectangle;
    JButton move;
    JButton remove;
    JButton undo;
    JButton redo;

    Board(Editor editor) {
        this.editor = editor;
        createBasicShapes();
        createFrame();
        attachMouseListeners();
        refresh();
    }

    public void createBasicShapes(){
        circleToClone = new Circle(20, 20, 50, Color.RED);
        squareToClone = new Square(20, 20, 50, Color.GREEN);
        rectangleToClone = new Rectangle(20, 20, 50, 70, Color.BLUE);
    }

    private void initializeButtons(JPanel contentPanel){
        circle = new JButton("circle");
        square = new JButton("square");
        rectangle = new JButton("rectangle");
        move = new JButton("move");
        remove = new JButton("remove");
        undo = new JButton("undo");
        redo = new JButton("redo");

        circle.addActionListener(this);
        square.addActionListener(this);
        rectangle.addActionListener(this);
        move.addActionListener(this);
        remove.addActionListener(this);
        undo.addActionListener(this);
        redo.addActionListener(this);

        contentPanel.add(circle, BorderLayout.NORTH);
        contentPanel.add(square, BorderLayout.NORTH);
        contentPanel.add(rectangle, BorderLayout.NORTH);
        contentPanel.add(move, BorderLayout.NORTH);
        contentPanel.add(remove, BorderLayout.NORTH);
        contentPanel.add(undo, BorderLayout.NORTH);
        contentPanel.add(redo, BorderLayout.NORTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "circle" -> selectedEvent = CIRCLE;
            case "square" -> selectedEvent = SQUARE;
            case "rectangle" -> selectedEvent = RECTANGLE;
            case "remove" -> selectedEvent = REMOVE;
            case "move" -> selectedEvent = MOVE;
            case "undo" -> editor.undo();
            case "redo" -> editor.redo();
        }

        System.out.println(e.getActionCommand());
    }

    private void createFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 600);

        JPanel boardPanel = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING);
        boardPanel.setBorder(padding);
        boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.Y_AXIS));
        frame.setContentPane(boardPanel);

        JPanel toolbarPanel = new JPanel();
        frame.add(toolbarPanel, BorderLayout.NORTH);


        initializeButtons(toolbarPanel);

        boardPanel.add(this);

        frame.setVisible(true);
        boardPanel.setBackground(Color.LIGHT_GRAY);
    }


    private void attachMouseListeners() {

        MouseAdapter selector = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1) {
                    return;
                }

                Shape target = editor.getShapes().getChildAt(e.getX(), e.getY());

                if (target == null) {
                        editor.getShapes().unSelect();
                } else {
                        if (!target.isSelected()) {
                            editor.getShapes().unSelect();
                        }
                        target.select();
                }
                repaint();
            }
        };
        addMouseListener(selector);

        MouseAdapter executor = new MouseAdapter() {
            AddCommand addCommand;
            RemoveCommand removeCommand;
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedEvent.equals(CIRCLE) && e.getClickCount() == 2){
                    Circle circle = (Circle) circleToClone.clone();
                    circle.setX(e.getX() - PADDING);
                    circle.setY(e.getY() - PADDING);
                    addCommand = new AddCommand(circle, editor);
                }

                else if (selectedEvent.equals(SQUARE) && e.getClickCount() == 2){
                    Square square = (Square) squareToClone.clone();
                    square.setX(e.getX() - PADDING);
                    square.setY(e.getY() - PADDING);
                    addCommand = new AddCommand(square, editor);
                }

                else if (selectedEvent.equals(RECTANGLE) && e.getClickCount() == 2){
                    Rectangle rectangle = (Rectangle) rectangleToClone.clone();
                    rectangle.setX(e.getX() - PADDING);
                    rectangle.setY(e.getY() - PADDING);
                    addCommand = new AddCommand(rectangle, editor);
                }

                else if(selectedEvent.equals(REMOVE)) {
                        BaseShape toRemove = (BaseShape) editor.allShapes.getChildAt(e.getX(), e.getY());
                        removeCommand = new RemoveCommand(toRemove, editor);
                        System.out.println(editor.allShapes.getChildAt(e.getX(), e.getY()));

                }

                System.out.println(e.getX() + " " + e.getY());
                if (addCommand != null){
                    editor.execute(addCommand);
                    this.addCommand = null;
                }

                if (removeCommand != null){
                    editor.execute(removeCommand);
                    this.removeCommand = null;
                }
            }
        };
        addMouseListener(executor);

        MouseAdapter dragger = new MouseAdapter() {
            MoveCommand moveCommand;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!selectedEvent.equals(MOVE)){
                    return;
                }

                if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != MouseEvent.BUTTON1_DOWN_MASK) {
                    return;
                }
                if (moveCommand == null) {
                    moveCommand = new MoveCommand(editor);
                    moveCommand.start(e.getX(), e.getY());
                }
                moveCommand.move(e.getX(), e.getY());
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1 || moveCommand == null) {
                    return;
                }
                moveCommand.stop(e.getX(), e.getY());
                editor.execute(moveCommand);
                this.moveCommand = null;
                repaint();
            }
        };
        addMouseListener(dragger);
        addMouseMotionListener(dragger);
    }

    public int getWidth() {
        return editor.getShapes().getX() + editor.getShapes().getWidth() + PADDING;
    }

    public int getHeight() {
        return editor.getShapes().getY() + editor.getShapes().getHeight() + PADDING;
    }

    void refresh() {
        this.setSize(getWidth(), getHeight());
        frame.pack();
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics graphics) {
        BufferedImage buffer = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D ig2 = buffer.createGraphics();
        ig2.setBackground(Color.WHITE);
        ig2.clearRect(0, 0, this.getWidth(), this.getHeight());

        editor.getShapes().paint(buffer.getGraphics());

        graphics.drawImage(buffer, 0, 0, null);
    }
}
