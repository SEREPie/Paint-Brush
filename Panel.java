/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

/**
 *
 * @author pc
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author pc
 */
public class Panel extends JPanel {
    Color selectedColor = Color.BLACK;
    Color lastUsedColor = selectedColor;

    boolean dottedMode = false;
    boolean filledMode = false;
    String currMode;
    int x1, x2, y1, y2;
    ArrayList<Shape> shapes = new ArrayList<>();

    BufferedImage image;

    private void undo() {
        if (!shapes.isEmpty()) {
            shapes.remove(shapes.size() - 1);
        }
        repaint();
    }



    public Panel() {
        setLayout(new BorderLayout());
        JPanel controlPanel = new JPanel(new GridLayout(0, 1));



        JButton red = new JButton();
        controlPanel.add(red);
        red.setBackground(Color.RED);

        JButton green = new JButton();
        controlPanel.add(green);
        green.setBackground(Color.GREEN);

        JButton blue = new JButton();
        controlPanel.add(blue);
        blue.setBackground(Color.blue);

        red.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedColor = Color.RED;
            }
        });
        green.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedColor = Color.GREEN;
            }
        });
        blue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedColor = Color.BLUE;
            }
        });


        JButton drawLine = new JButton("Line");
        controlPanel.add(drawLine);
        drawLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currMode = "Line";
            }
        });

        JButton drawRect = new JButton("Rectangle");
        controlPanel.add(drawRect);
        drawRect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currMode = "Rectangle";
            }
        });

        JButton drawOval = new JButton("Oval");
        controlPanel.add(drawOval);
        drawOval.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currMode = "Oval";
            }
        });

        JButton freeHand = new JButton("Free Hand");
        controlPanel.add(freeHand);
        freeHand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currMode = "Freehand";
            }
        });

        JButton eraser = new JButton("Eraser");
        controlPanel.add(eraser);
        eraser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currMode = "Erase";
            }
        });

        JButton clearAllButton = new JButton("Clear All");
        controlPanel.add(clearAllButton);

        JCheckBox dottedCheckBox, filledCheckBox;
        dottedCheckBox = new JCheckBox("Dotted");
        controlPanel.add(dottedCheckBox);

        filledCheckBox = new JCheckBox("Filled");
        controlPanel.add(filledCheckBox);


        JButton undoButton = new JButton("Undo");
        controlPanel.add(undoButton);


        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
                repaint();
            }
        });


        JButton saveButton = new JButton("Save");
        controlPanel.add(saveButton);

        


        JButton openButton = new JButton("Open");
        controlPanel.add(openButton);


        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        image = ImageIO.read(file);
                        repaint();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        dottedCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                dottedMode = e.getStateChange() == ItemEvent.SELECTED;
            }
        });

        filledCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                filledMode = e.getStateChange() == ItemEvent.SELECTED;
            }
        });

        clearAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapes.clear();
                repaint();
            }
        });

        add(controlPanel, BorderLayout.WEST);
        JPanel drawingPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g;

                if (image != null) {
                    g2d.drawImage(image, 0, 0, null);
                }

                for (Shape shape : shapes) {
                    shape.draw((Graphics2D) g);
                }

                

                if (x1 != 0 && y1 != 0 && x2 != 0 && y2 != 0) {
                    g.setColor(selectedColor);
                    int x = Math.min(x1, x2);
                    int y = Math.min(y1, y2);
                    int width = Math.abs(x1 - x2);
                    int height = Math.abs(y1 - y2);
                    switch (currMode) {
                        case ("Line"):
                            if (dottedMode) {
                                float[] dashPattern = {5, 5};
                                ((Graphics2D) g).setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
                            }
                            g.drawLine(x1, y1, x2, y2);
                            break;
                        case ("Rectangle"):
                            if (filledMode)
                                g.fillRect(x, y, width, height);
                            else {
                                if (dottedMode) {
                                    float[] dashPattern = {5, 5};
                                    ((Graphics2D) g).setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
                                }
                                ((Graphics2D) g).drawRect(x, y, width, height);
                            }
                            break;
                        case ("Oval"):
                            if (filledMode)
                                g.fillOval(x, y, width, height);
                            else {
                                if (dottedMode) {
                                    float[] dashPattern = {5, 5};
                                    ((Graphics2D) g).setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
                                }
                                ((Graphics2D) g).drawOval(x, y, width, height);
                            }
                            break;
                    }
                }

            }
        };
            
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".jpg")) {
                        file = new File(file.getParentFile(), file.getName() + ".jpg");
            }
                    try {
                        BufferedImage image = new BufferedImage(drawingPanel.getWidth(), drawingPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
                        Graphics2D g2d = image.createGraphics();
                        drawingPanel.paint(g2d);
                        g2d.dispose();
                        ImageIO.write(image, "jpg", file);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        drawingPanel.addMouseListener(new MouseListener() {
                                          @Override
                                          public void mouseClicked(MouseEvent e) {
                                          }

                                          @Override
                                          public void mousePressed(MouseEvent e) {
                                              lastUsedColor = selectedColor;
                                              switch (currMode){
                                                  case "Line" :
                                                        x1 = e.getX();
                                                        y1 = e.getY();
                                                        x2 = x1;
                                                        y2 = y1;
                                                        break;
                                                  case "Rectangle" :
                                                        x1 = e.getX();
                                                        y1 = e.getY();
                                                        x2 = x1;
                                                        y2 = y1;
                                                        break;
                                                  case "Oval" :
                                                        x1 = e.getX();
                                                        y1 = e.getY();
                                                        x2 = x1;
                                                        y2 = y1;
                                                        break;
                                                  case "Freehand" :
                                                        x1 = e.getX();
                                                        y1 = e.getY();
                                                        x2 = x1;
                                                        y2 = y1;
                                                        break;
                                                  case "Erase" :
                                                        x1 = e.getX();
                                                        y1 = e.getY();
                                                        shapes.add(new Eraser(x1, y1));
                                                        repaint();
                                                        break;
                                           
                                              }
                                          }

                                          @Override
                                          public void mouseReleased(MouseEvent e) {
                                              int x = Math.min(x1, x2);
                                              int y = Math.min(y1, y2);
                                              int width = Math.abs(x1 - x2);
                                              int height = Math.abs(y1 - y2);
                                              selectedColor = lastUsedColor;
                                              switch (currMode){
                                                case "Line":
                                                    shapes.add(new Line(x1, x2, y1, y2, selectedColor, dottedMode));
                                                    break;
                                                case "Rectangle":
                                                    shapes.add(new Rect(x, y, width, height, selectedColor, filledMode, dottedMode));
                                                    break;
                                                case "Oval":
                                                    shapes.add(new Oval(x, y, width, height, selectedColor, filledMode, dottedMode));
                                                    break;
                                                case "Freehand":
                                                    shapes.add(new Line(x1, x2, y1, y2, selectedColor, dottedMode));
                                                    break;
                                                case "Erase":
                                                    shapes.add(new Eraser(x1, y1));
                                                    selectedColor = lastUsedColor;
                                                    repaint();
                                                    break;
                                              }
                                              x1 = 0;
                                              y1 = 0;
                                              x2 = 0;
                                              y2 = 0;
                                              repaint();

                                          }

                                          @Override
                                          public void mouseEntered(MouseEvent e) {
                                          }

                                          @Override
                                          public void mouseExited(MouseEvent e) {
                                          }
                                      }
        );

        drawingPanel.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                switch (currMode) {
                    case  "Line":
                        x2 = e.getX();
                        y2 = e.getY();
                        break;
                    case  "Rectangle":
                        x2 = e.getX();
                        y2 = e.getY();
                        break;
                        case  "Oval":
                        x2 = e.getX();
                        y2 = e.getY();
                        break;

                    case "Freehand":
                        x2 = e.getX();
                        y2 = e.getY();
                        shapes.add(new Line(x1, x2, y1, y2, selectedColor, dottedMode));
                        x1 = x2;
                        y1 = y2;
                        break;
                    case "Erase":
                        x2 = e.getX();
                        y2 = e.getY();
                        shapes.add(new Eraser(x2, y2));
                         x1 = x2;
                         y1 = y2;
                        repaint(x2 - 5, y2 - 5, 10, 10);
                        break;


                }
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        add(drawingPanel, BorderLayout.CENTER);

        drawingPanel.setBackground(Color.WHITE);
    }

    abstract static class Shape {
        int x1, y1, x2, y2;
        Color color;
        boolean dottedMode;
        boolean filledMode;

        Shape(int x1, int y1, int x2, int y2, Color color, boolean filledMode, boolean dottedMode) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
            this.filledMode = filledMode;
            this.dottedMode = dottedMode;
        }

        abstract void draw(Graphics2D g);
    }

    static class Line extends Shape {
        Line(int x1, int x2, int y1, int y2, Color color, boolean dottedMode) {
            super(x1, y1, x2, y2, color, false, dottedMode);
        }

        @Override
        void draw(Graphics2D g) {
            Stroke oldStroke = g.getStroke();
            if (dottedMode) {
                float[] dashPattern = {5, 5};
                g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
            }
            g.setColor(color);
            g.drawLine(x1, y1, x2, y2);
            g.setStroke(oldStroke);
        }
    }

    static class Rect extends Shape {

        public Rect(int x1, int y1, int width, int height, Color color, boolean filledMode, boolean dottedMode) {
            super(x1, y1, x1 + width, y1 + height, color, filledMode, dottedMode);
        }

        @Override
        void draw(Graphics2D g) {
            int x = Math.min(x1, x2);
            int y = Math.min(y1, y2);
            int width = Math.abs(x1 - x2);
            int height = Math.abs(y1 - y2);
            g.setColor(color);
            if (filledMode)
                g.fillRect(x, y, width, height);
            else {
                Stroke oldStroke = g.getStroke();
                if (dottedMode) {
                    float[] dashPattern = {5, 5};
                    g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
                }
                g.drawRect(x, y, width, height);
                g.setStroke(oldStroke);
            }
        }
    }

    static class Oval extends Shape {
        Oval(int x1, int y1, int width, int height, Color color, boolean filledMode, boolean dottedMode) {
            super(x1, y1, x1 + width, y1 + height, color, filledMode, dottedMode);
        }

        @Override
        void draw(Graphics2D g) {
            g.setColor(color);
            if (filledMode)
                g.fillOval(x1, y1, x2 - x1, y2 - y1);
            else {
                Stroke oldStroke = g.getStroke();
                if (dottedMode) {
                    float[] dashPattern = {5, 5};
                    g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
                }
                g.drawOval(x1, y1, x2 - x1, y2 - y1);
                g.setStroke(oldStroke);
            }
        }
    }

    static class Eraser  extends Shape{

        public Eraser(int x1, int y1) {
            super(x1, y1, 10, 10, Color.WHITE,false,false);
        }


        @Override
        void draw(Graphics2D g) {
            g.setColor(Color.WHITE);
            g.fillRect(x1, y1, 10, 10);
            g.setColor(Color.BLACK);
        }
    }

}