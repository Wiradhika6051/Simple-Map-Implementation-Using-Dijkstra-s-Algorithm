package gui;

import algorithm.DjikstraAlgorithm;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import utils.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.Deque;

public class MainPage extends JFrame {
    private SimpleWeightedGraph<String, DefaultWeightedEdge> graph;
    private Visualizer visualizer;
    //private JLabel judul;
    private JLabel uploadLabel;
    private JLabel fileName;
    private JButton uploadButton;
    private static final Dimension DEFAULT_SIZE = new Dimension(530, 600);
    private GridBagConstraints gbc;
    private static MainPage parentFrame;
    private int frameWidth;
    private int frameHeight;
    private boolean debugMode;
    private static final Font TITLE_FONT = new Font("Serif", Font.BOLD,20);
    //test
    //private mxIGraphLayout layout;
    //mxGraphComponent component;
    //private JGraphXAdapter<String, DefaultWeightedEdge> jgxAdapter;
    public MainPage(boolean debugMode){
       // this.setPreferredSize(DEFAULT_SIZE);
        this.setTitle("Pathfinder D-2000");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.debugMode = true;

       // this.setUndecorated(true);->ngilangin panel atas buat close
        this.setResizable(false);
        //this.pack();
        //this.setLayout(new GridBagLayout());
        this.setLayout(null);
        this.gbc = new GridBagConstraints();
        parentFrame = this;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.frameWidth = (int)screenSize.getWidth();
        this.frameHeight = (int)screenSize.getHeight();
        System.out.println(frameWidth+","+frameHeight);
        initComponent();

      //  DjikstraAlgorithm da = new DjikstraAlgorithm(graph);
      //  Deque<String> solusi = da.solve("A","C");
      //  System.out.println("waktu pengerjaan"+da.getTime());
      //  System.out.println("Jumlah iterasi:"+da.getIterations());
    }
    private void initComponent(){
        showAxis();
        //uploader
        //label
        this.uploadLabel = new JLabel("UPLOAD FILE");
        this.uploadLabel.setFont(TITLE_FONT);
        this.uploadLabel.setBounds(getFractionSize(frameWidth,1.25,40),(int)(this.frameHeight/40.0),(int)(this.frameWidth*(5.0/40)),(int)(this.frameHeight*(2/40.0)));
        this.add(this.uploadLabel);
        //button
        this.uploadButton = new JButton("PILIH FILE");
        this.uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("test/");
                int result = fileChooser.showOpenDialog(parentFrame);
                if(result==JFileChooser.APPROVE_OPTION){
                    File selectedFile = fileChooser.getSelectedFile();
                    Parser parser = new Parser(selectedFile);
                    parentFrame.graph = parser.parse();
                    parentFrame.fileName.setText(selectedFile.getName());
                    parentFrame.visualizer.update(parentFrame.graph);
                }
            }
        }
        );
        this.uploadButton.setBounds(getFractionSize(frameWidth,1,40),
                getFractionSize(frameHeight,3,40),
                getFractionSize(frameWidth,5,40),
                getFractionSize(frameHeight,2,40)
                );
        add(uploadButton);
        //filename
        this.fileName = new JLabel("");
        this.fileName.setBounds(getFractionSize(frameWidth,1,40),
                getFractionSize(frameHeight,4.5,40),
                getFractionSize(frameWidth,5,40),
                getFractionSize(frameHeight,2,40)
        );
        add(fileName);
    }
    private int getFractionSize(double base,double pembilang,double penyebut){
        return (int)(base*((1.0*pembilang)/penyebut));
    }
    private void showAxis(){
        if(this.debugMode) {
            for (int i = 0; i < 41; i++) {
                JLabel label = new JLabel(Integer.toString(i));
                System.out.println((int) (this.frameWidth * (i / 40.0)));
                label.setBounds((int) (this.frameWidth * (i / 40.0)), 200, 50, 50);
                add(label);
            }
            for (int i = 0; i < 20; i++) {
                JLabel label = new JLabel(Integer.toString(i));
                System.out.println((int) (this.frameHeight * (i / 20.0) - this.frameHeight / 34.0));
                label.setBounds(470, (int) (this.frameHeight * (i / 20.0) - this.frameHeight / 34.0), 50, 50);
                add(label);
            }
        }
    }
    private void fillComponent() {
       // this.judul = new JLabel("PATHFINDER D-2000");
        //this.visualizer = new Visualizer(graph);
        //baris 1
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
   //     this.add(judul, gbc);
        //baris 2
        //perintah upload
        this.uploadLabel = new JLabel("Masukkan File->");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(uploadLabel, gbc);
        //tombol upload
        this.uploadButton = new JButton("PILIH FILE");
        this.uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("test/");
                int result = fileChooser.showOpenDialog(parentFrame);
                if(result==JFileChooser.APPROVE_OPTION){
                    File selectedFile = fileChooser.getSelectedFile();
                    Parser parser = new Parser(selectedFile);
                    parentFrame.graph = parser.parse();
                    parentFrame.fileName.setText(selectedFile.getName());
                    parentFrame.visualizer.update(parentFrame.graph);
                }
            }
        }
        );
        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(uploadButton, gbc);
        //nama file
        this.fileName = new JLabel("");
        gbc.gridx = 2;
        gbc.gridy = 1;
        this.add(fileName, gbc);
        //baris 3
        //visualizer
        this.graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        this.visualizer = new Visualizer(graph);
        //test
        /*
        File selectedFile = new File("test/test0.txt");
        Parser parser = new Parser(selectedFile);
        parentFrame.graph = parser.parse();
        jgxAdapter = new JGraphXAdapter<>(this.graph);
        component = new mxGraphComponent(jgxAdapter);
        layout = new mxCircleLayout(jgxAdapter);
        layout.execute(jgxAdapter.getDefaultParent());
        */
        //
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth=3;
        gbc.gridheight=3;
        this.add(visualizer, gbc);

    }
}


