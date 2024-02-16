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
import javax.swing.*;
import java.awt.*;

public class Frame {
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        Panel board = new Panel();
        frame.setContentPane(board);
        board.setBackground(Color.WHITE);
        frame.setTitle ("Paint Brush");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE) ;
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
