import javax.swing.*;
import java.awt.*;

public class TagExtractor {
    public static void main(String[] args) {
        //get the dimension of the screen resolution
        double dHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        double dWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

        //create and int value for 3/4ths the width and height of the screen resolution
        int height = ((int)(dHeight * .75));
        int width = ((int)(dWidth * .75));

        //create a JFrame using FortuneTellerFrame
        JFrame frame = new TagFrame();
        frame.setTitle("Tag Extractor");
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
