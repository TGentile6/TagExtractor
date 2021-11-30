import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class TagFrame extends JFrame {
    //Main Panel
    JPanel MainPanel;

    //DataPanel
    JPanel NamePanel;
    JTextField FileName;
    JLabel FileLabel;

    //Tags Panel
    JPanel TagsPanel;
    JTextArea TagsArea;
    JScrollPane TagsScroll;

    //Button Panel
    JPanel BtnPanel;
    JButton QuitBtn;
    JButton LoadBtn;


    //ActionListeners
    JFileChooser chooser;
    boolean FileLoaded = false;
    Path FilePath;
    File OriginalFile;
    Scanner in;
    Map<String, Integer> hashes;
    String[] words;


    public TagFrame() throws HeadlessException {
        MainPanel = new JPanel();
        MainPanel.setLayout(new BoxLayout(MainPanel, BoxLayout.PAGE_AXIS));
        CreateNamePanel();
        CreateTagsPanel();
        CreateBtnPanel();
        MainPanel.add(NamePanel);
        MainPanel.add(TagsPanel);
        MainPanel.add(BtnPanel);
        add(MainPanel);
    }

    private void CreateNamePanel() {
        NamePanel = new JPanel();
        FileName = new JTextField(30);
        FileName.setEditable(false);
        FileLabel = new JLabel("File name:");
        NamePanel.add(FileLabel);
        NamePanel.add(FileName);

    }

    private void CreateTagsPanel() {
        TagsPanel = new JPanel();
        TagsArea = new JTextArea(30, 30);
        TagsArea.setEditable(false);
        TagsScroll = new JScrollPane(TagsArea);
        TagsPanel.add(TagsScroll);
    }

    private void CreateBtnPanel() {
        BtnPanel = new JPanel();
        QuitBtn = new JButton("Quit");
        LoadBtn= new JButton("Load File");
        LoadBtn.addActionListener(new LoadListener());
        QuitBtn.addActionListener((ActionEvent ae) -> {System.exit(0);});
        BtnPanel.add(LoadBtn);
        BtnPanel.add(QuitBtn);
    }

    public class LoadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            chooser = new JFileChooser();
            TagsArea.setText("");

            try{
                File workingDirectory = new File(System.getProperty("user.dir"));
                chooser.setCurrentDirectory(workingDirectory);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    OriginalFile = chooser.getSelectedFile();
                    FilePath = OriginalFile.toPath();
                    FileLoaded = true;

                    FileName.setText(OriginalFile.getName());

                    Scanner in = new Scanner(OriginalFile);
                    hashes = new HashMap<String, Integer>();

                    while (in.hasNextLine()) {
                        String line = in.nextLine();
                        words = line.split(" ");
                        for (String word : words) {
                            if (!hashes.containsKey(word)) {
                                hashes.put(word, 1);
                            }
                            else {
                                hashes.put(word, hashes.get(word) + 1);
                            }
                        }
                    }

                    for(Map.Entry<String,Integer> entry : hashes.entrySet()){
                        TagsArea.append(entry.getKey() + " : " + entry.getValue() + "\n");
                    }
                }
            } catch(FileNotFoundException e) {
                System.out.println("File not found!!!");
                e.printStackTrace();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }


}
