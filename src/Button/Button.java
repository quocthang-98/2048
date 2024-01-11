package Button;

import main.Game;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class Button extends JFrame implements ActionListener {
    private JButton tryAgainButton;
    private JButton exitButton;


    public Button() {

        tryAgainButton = new JButton("Try Again");
        tryAgainButton.setBounds(50,50,200,30);
        tryAgainButton.setLocation(0,0);
        tryAgainButton.addActionListener(this);
        add(tryAgainButton);
        exitButton = new JButton("Exit");
        exitButton.setBounds(50,50,200,30);
        exitButton.setLocation(0,30);
        exitButton.addActionListener(this);
        add(exitButton);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(210,100);
        setLayout(null);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == tryAgainButton){
            Game.getframe().disposeFrame();
            dispose();
            Game.setInstanceNull();
            Game.getInstance();
        } else {
            try{
            Game.getframe().disposeFrame();
            dispose();}
            catch(Exception er){
                er.printStackTrace();
            }
        }
    }




}
