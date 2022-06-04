package gui;

import algorithm.DjikstraAlgorithm;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import utils.EdgeAdaptor;
import utils.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.Deque;
import java.util.Map;

public class MainPage extends JFrame {
    private SimpleWeightedGraph<String, EdgeAdaptor> graph;
    private Visualizer visualizer;
    //private JLabel judul;
    private JLabel uploadLabel;
    private JLabel fileName;
    private JLabel analysisLabel;
    private JLabel runLabel;
    private JLabel runtimeLabel;
    private JLabel iterationsLabel;
    private JButton uploadButton;
    public JButton runButton;
    public JButton restartButton;
    private static final Dimension DEFAULT_SIZE = new Dimension(530, 600);
    private GridBagConstraints gbc;
    private static MainPage parentFrame;
    private int frameWidth;
    private int frameHeight;
    private boolean debugMode;
    public String namafile;
    private DjikstraAlgorithm da;
    Deque<String> solusi;
    String startNode;
    String endNode;
    public Map<String, Map<String,Double>> historyData;

    public static final Font TITLE_FONT = new Font("Serif", Font.BOLD,20);
    //test
    //private mxIGraphLayout layout;
    //mxGraphComponent component;
    //private JGraphXAdapter<String, DefaultWeightedEdge> jgxAdapter;
    public static  MainPage getInstance(){
        return parentFrame;
    }
    public void setStartNode(String name){
        this.startNode = name;
        System.out.println(startNode);
    }
    public int getFrameWidth(){
        return frameWidth;
    }
    public int getFrameHeight(){
        return frameHeight;
    }
    public String getStartNode(){
        return startNode;
    }
    public void setEndNode(String name){
        this.endNode = name;
        System.out.println(endNode);
    }
    public String getEndNode(){
        return endNode;
    }
    public MainPage(boolean debugMode){
       // this.setPreferredSize(DEFAULT_SIZE);
        this.setTitle("Pathfinder D-2000");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.debugMode = debugMode;

        this.da = null;

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
                    parentFrame.startNode = null;
                    parentFrame.endNode = null;
                    parentFrame.graph = parser.parse();
                    parentFrame.namafile = selectedFile.getName();
                    parentFrame.fileName.setText(parentFrame.namafile);
                    parentFrame.runButton.setEnabled(false);
                    parentFrame.visualizer.update(parentFrame.graph);
                    parentFrame.visualizer.setBounds(getFractionSize(frameWidth,11,40),
                            getFractionSize(frameHeight,1,40),
                            getFractionSize(frameWidth,28,40),
                            getFractionSize(frameHeight,28,40)
                    );
                    parentFrame.resetMap();
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
        this.namafile = "";
        this.fileName = new JLabel(namafile);
        this.fileName.setBounds(getFractionSize(frameWidth,1,40),
                getFractionSize(frameHeight,4.5,40),
                getFractionSize(frameWidth,5,40),
                getFractionSize(frameHeight,2,40)
        );
        add(fileName);
        //visualizer
        this.graph = new SimpleWeightedGraph<String, EdgeAdaptor>(EdgeAdaptor.class);
        this.visualizer = new Visualizer(graph);
        this.visualizer.setBounds(getFractionSize(frameWidth,11,40),
                getFractionSize(frameHeight,1,40),
                getFractionSize(frameWidth,28,40),
                getFractionSize(frameHeight,28,40)
        );
        add(visualizer);
        //run
        //label
        this.runLabel = new JLabel("JALANKAN PROGRAM");
        this.runLabel.setFont(TITLE_FONT);
        this.runLabel.setBounds(getFractionSize(frameWidth,0.5,40),
                getFractionSize(frameHeight,6,40),
                getFractionSize(frameWidth,7,40),
                getFractionSize(frameHeight,2,40)
        );
        this.add(this.runLabel);
        //run button
        this.runButton = new JButton("RUN");
        this.runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.da = new DjikstraAlgorithm(parentFrame.graph);
                parentFrame.solusi = da.solve(startNode,endNode);
                parentFrame.historyData = da.getHistory();
                parentFrame.runtimeLabel.setText("Total Waktu: "+da.getTime());
                parentFrame.iterationsLabel.setText("Jumlah Iterasi: "+da.getIterations());
                parentFrame.restartButton.setEnabled(true);
                parentFrame.runButton.setEnabled(false);
                //update graf
                parentFrame.updateGraf();
            }
        }
        );
        this.runButton.setBounds(getFractionSize(frameWidth,1,40),
                getFractionSize(frameHeight,8,40),
                getFractionSize(frameWidth,5,40),
                getFractionSize(frameHeight,2,40)
        );
        //cek apakah udah dimuat belum filenya
        if(this.namafile.equals("")){
            this.runButton.setEnabled(false);
        }
        add(runButton);
        //tombol restart
        restartButton = new JButton("RESTART");
        restartButton.setBounds(
                parentFrame.getFractionSize(parentFrame.getFrameWidth(),1,40),
                parentFrame.getFractionSize(parentFrame.getFrameHeight(),10.5,40),
                parentFrame.getFractionSize(parentFrame.getFrameWidth(),5,40),
                parentFrame.getFractionSize(parentFrame.getFrameHeight(),2,40)
        );
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.visualizer.resetGraph();
                parentFrame.resetMap();
            }
        });
        //cek apakah sudah di run
        if(solusi==null){
            restartButton.setEnabled(false);
        }
        add(restartButton);
        //analisis
        //label
        this.analysisLabel = new JLabel("RUNTIME STATISTIC");
        this.analysisLabel.setFont(TITLE_FONT);
        this.analysisLabel.setBounds(getFractionSize(frameWidth,0.6,40),
                getFractionSize(frameHeight,13,40),
                getFractionSize(frameWidth,7,40),
                getFractionSize(frameHeight,2,40)
        );
        this.add(this.analysisLabel);
        //waktu pengerjaan
        this.runtimeLabel = new JLabel("Total Waktu:");
        this.runtimeLabel.setBounds(getFractionSize(frameWidth,1,40),
                getFractionSize(frameHeight,15,40),
                getFractionSize(frameWidth,7,40),
                getFractionSize(frameHeight,2,40)
        );
        this.add(this.runtimeLabel);
        //jumlah iterasi
        this.iterationsLabel = new JLabel("Jumlah Iterasi:");
        this.iterationsLabel.setBounds(getFractionSize(frameWidth,1,40),
                getFractionSize(frameHeight,17,40),
                getFractionSize(frameWidth,7,40),
                getFractionSize(frameHeight,2,40)
        );
        this.add(this.iterationsLabel);
    }
    public int getFractionSize(double base,double pembilang,double penyebut){
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
            for (int i = 0; i < 40; i++) {
                JLabel label = new JLabel(Integer.toString(i));
                System.out.println((int) (this.frameHeight * (i / 40.0) - this.frameHeight / 34.0));
                label.setBounds(470, (int) (this.frameHeight * (i / 40.0) - this.frameHeight / 34.0), 50, 50);
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
        this.graph = new SimpleWeightedGraph<String, EdgeAdaptor>(EdgeAdaptor.class);
        this.visualizer = new Visualizer(graph);
        //test
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth=3;
        gbc.gridheight=3;
        this.add(visualizer, gbc);

    }
    public void updateGraf(){
        //hapus elemen pertama sama terakhir
        String startNode = this.solusi.removeFirst();
        String endNode = this.solusi.removeLast();
        //ganti warna elemen antara
        mxCell cell;
        for(String nama:solusi){
            cell = visualizer.getVertex(nama);
            this.visualizer.updateCell(new Object[]{cell},"ffff00","1A1AFF");//kuning
        }
        //ganti warna edge nya
        //ganti untuk yang ngubungin startNode sama node selanjutnya

        //inisialisasi array
        Object[] highlitedPath = new Object[solusi.size()+1];
        //System.out.println("aaa:"+startNode+solusi.getFirst());
        if(solusi.size()>0) {
            highlitedPath[0] = this.visualizer.getEdge(startNode, solusi.getFirst());
            highlitedPath[solusi.size()] = this.visualizer.getEdge(solusi.getLast(), endNode);

            int size = solusi.size() - 1;
            String current = solusi.size() > 0 ? this.solusi.removeFirst() : null;
            String next = solusi.size() > 0 ? this.solusi.removeFirst() : null;
            for (int i = 0; i < size; i++) {
                if (current != null && next != null)
                    highlitedPath[i + 1] = this.visualizer.getEdge(current, next);
                current = next;
                next = solusi.size() > 0 ? this.solusi.removeFirst() : null;
            }
        }
        else{
            //gak ada node antara
            highlitedPath[0] = this.visualizer.getEdge(startNode, endNode);
        }
        this.visualizer.updateEdge(highlitedPath,"ffff00","00ff00");
    }
    public void resetMap(){
        this.startNode = null;
        this.endNode = null;
        runtimeLabel.setText("Total Waktu:");
        iterationsLabel.setText("Jumlah Iterasi:");
        runButton.setEnabled(false);
        restartButton.setEnabled(false);
        solusi = null;
        historyData=null;
    }

}


