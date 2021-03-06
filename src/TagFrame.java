import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.Attributes;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;

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
    JButton SaveBtn;


    //ActionListeners
    JFileChooser chooser;
    boolean FileLoaded = false;
    Path FilePath;
    File OriginalFile;
    File StopWordsFile;
    ArrayList<String> StopWords;
    Scanner in;
    Map<String, Integer> hashes;
    String[] words;
    boolean ignore = false;


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
        LoadBtn = new JButton("Load File");
        SaveBtn = new JButton("Save Tags to File");
        LoadBtn.addActionListener(new LoadListener());
        SaveBtn.addActionListener(new SaveListener());
        QuitBtn.addActionListener((ActionEvent ae) -> {System.exit(0);});
        BtnPanel.add(LoadBtn);
        BtnPanel.add(SaveBtn);
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
                    StopWordsFile = new File("English Stop Words.txt");
                    FileLoaded = true;

                    FileName.setText(OriginalFile.getName());

                    Scanner StopIn = new Scanner(StopWordsFile);
                    StopWords = new ArrayList<>();
                    while(StopIn.hasNextLine()){
                        String line = StopIn.nextLine();
                        StopWords.add(line.toLowerCase(Locale.ROOT));
                    }

                    in = new Scanner(OriginalFile);
                    hashes = new HashMap<String, Integer>();

                    while (in.hasNextLine()) {
                        String line = in.nextLine();
                        words = line.toLowerCase(Locale.ROOT).split(" ");
                        for (String word : words) {
                            ignore = false;
                            for(String stop : StopWords){
                                if(word.equals(stop)){
                                    ignore = true;
                                    break;
                                }
                            }
                            if(!ignore) {
                                if (!hashes.containsKey(word)) {
                                    hashes.put(word, 1);
                                } else {
                                    hashes.put(word, hashes.get(word) + 1);
                                }
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
        }
    }
    public class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(FileLoaded){
                File workingDirectory = new File(System.getProperty("user.dir"));
                Path file = Paths.get(workingDirectory.getPath() + "\\Tags.txt");

                try
                {
                    OutputStream out = new BufferedOutputStream(Files.newOutputStream(file, CREATE));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
                    TagsArea.write(writer);
                    writer.close();
                    JOptionPane.showMessageDialog(null, "File written!");
                }
                catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "There is no file loaded!");
            }
        }
    }


}
