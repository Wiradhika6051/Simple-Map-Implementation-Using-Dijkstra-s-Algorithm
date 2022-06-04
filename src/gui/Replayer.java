package gui;

import javax.swing.*;
import java.awt.*;

public class Replayer extends JPanel {
    JLabel iterationLabel;
    JButton nextButton;
    JButton prevButton;

    public Replayer(){
        iterationLabel = new JLabel("");
        MainPage parentFrame = MainPage.getInstance();
        iterationLabel.setBounds(
                parentFrame.getFractionSize(parentFrame.getFrameWidth(),11,40),
                parentFrame.getFractionSize(parentFrame.getFrameHeight(),30,40),
                parentFrame.getFractionSize(parentFrame.getFrameWidth(),6,40),
                parentFrame.getFractionSize(parentFrame.getFrameHeight(),2,40)
        );
        iterationLabel.setFont(parentFrame.TITLE_FONT);
        nextButton = new JButton("NEXT STEP");
        prevButton = new JButton("PREV STEP");
        this.setVisible(false);
    }

}
