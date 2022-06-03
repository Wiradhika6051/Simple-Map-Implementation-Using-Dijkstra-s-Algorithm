package gui;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.*;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.*;
import utils.EdgeAdaptor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.List;

public class Visualizer extends JPanel {
    SimpleWeightedGraph<String, EdgeAdaptor> graph;
    private static final Dimension DEFAULT_SIZE = new Dimension(400, 320);
    private JGraphXAdapter<String, EdgeAdaptor> jgxAdapter;
    private String[][] locationMatrix;
    private int arraySize;
    private GraphCanvas canvas;

    public Visualizer(SimpleWeightedGraph<String, EdgeAdaptor> graph){
        this.graph = graph;
        this.setBackground(Color.WHITE);
        //initGUI();
        //initializeGUI();
    }
    void initGUI(){
        this.canvas = canvas;
        generateLocationMatrix();
        fillLocationMatrix();
    }
    void generateLocationMatrix(){
        this.arraySize = (int)Math.ceil(Math.sqrt(this.graph.vertexSet().size()));
        this.locationMatrix = new String[arraySize][arraySize];
        for(int i=0;i<arraySize;i++){
            for(int j=0;j<arraySize;j++){
                locationMatrix[i][j] = null;
            }
        }
    }
    void fillLocationMatrix(){
        Set<String> vertices = this.graph.vertexSet();
        String maxConnectionVertice;
        List<String> tetangga;
        while(vertices.size()>0){
            maxConnectionVertice = getMaxConnextedVertice(vertices);
            tetangga = Graphs.neighborListOf(this.graph,maxConnectionVertice);
            setPosition(maxConnectionVertice);
            vertices.remove(maxConnectionVertice);
            for(String node:tetangga){
                maxConnectionVertice = getMaxConnextedVertice(vertices);
                if(!locationContains(maxConnectionVertice)){
                    setPosition(maxConnectionVertice);
                }
                vertices.remove(maxConnectionVertice);
            }
        }
        Map<String,List<String>> problematicNode = validateMatrix();
        List<String> neighborProblem;
        if(problematicNode.size()>0) {
            for (String key : problematicNode.keySet()) {
                Point locSource = getLocation(key);
                neighborProblem = problematicNode.get(key);
                for(String problem:neighborProblem){
                    Point locEnd = getLocation(problem);
                    if(isProblematic(locSource,locEnd)){

                    }
                }
            }
        }
    }
    Map<String,List<String>> validateMatrix(){
        Map<String,List<String>> daftar = new HashMap<>();
        List<String> tetangga;
        Point location;
        List<String> probNeigh;
        for(int i=0;i<arraySize;i++){
            for(int j=0;j<arraySize;j++){
                tetangga = Graphs.neighborListOf(this.graph,locationMatrix[i][j]);
                for(String node:tetangga){
                    location = getLocation(node);
                    if(isProblematic(new Point(i,j),location)){
                        if(!daftar.containsKey(locationMatrix[i][j])){
                            daftar.put(locationMatrix[i][j],new ArrayList<String>());
                        }
                        daftar.get(locationMatrix[i][j]).add(node);
                    }
                }
            }
        }
        return daftar;
    }
    boolean isProblematic(Point p1,Point p2){
        return Math.abs(p1.x-p2.x)>1 || Math.abs(p1.y-p2.y)>1;
    }
    Point getLocation(String name) {
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                if (locationMatrix.equals(name)) {
                    return new Point(j,i);
                }
            }
        }
        return null;
    }
    boolean locationContains(String node){
        for(int i=0;i<arraySize;i++){
            for(int j=0;j<arraySize;j++){
                if(locationMatrix[i][j].equals(node)){
                    return true;
                }
            }
        }
        return false;
    }
    void setPosition(String node){
        int x= arraySize/2;//x ke kanan+
        int y= arraySize/2;//y ke bawah +
        int tempX=x;
        int tempY=y;
        Direction dir = Direction.UP;
        while(locationMatrix[x][y]!=null){
            if(dir==Direction.UP){
                if(y>0) {
                    x = tempX;
                    y = tempY;
                    y--;
                }
                dir = Direction.RIGHT;
            }
            else if(dir==Direction.UP_RIGHT){
                if(y>0 && x<arraySize-1) {
                    x = tempX;
                    y = tempY;
                    y--;
                    x++;
                }
                dir = Direction.DOWN_RIGHT;
            }
            else if(dir==Direction.RIGHT){
                if(x<arraySize-1) {
                    x = tempX;
                    y = tempY;
                    x++;
                }
                dir = Direction.DOWN;
            }
            else if(dir==Direction.DOWN_RIGHT){
                if(x<arraySize-1 && y<arraySize-1) {
                    x = tempX;
                    y = tempY;
                    x++;
                    y++;
                }
                dir = Direction.DOWN_LEFT;
            }
            else if(dir==Direction.DOWN){
                if(y<arraySize-1) {
                    x = tempX;
                    y = tempY;
                    y++;
                }
                dir = Direction.LEFT;
            }
            else if(dir==Direction.DOWN_LEFT){
                if(y<arraySize-1 && x>0) {
                    x = tempX;
                    y = tempY;
                    y++;
                    x--;
                }
                dir = Direction.UP_LEFT;
            }
            else if(dir==Direction.LEFT){
                if(x>0) {
                    x = tempX;
                    y = tempY;
                    x--;
                }
                dir = Direction.UP_RIGHT;
            }
            else if(dir==Direction.UP_LEFT){
                if(x>0 && y>0) {
                    x = tempX;
                    y = tempY;
                    x--;
                    y--;
                }
                dir = Direction.UP;
            }
        }
        locationMatrix[x][y]= node;
    }
    String getMaxConnextedVertice(Set<String> vertices){
        int maxConnection = 0;
        int tempCount;
        String mostConnectedNode=null;
        for(String vertice:vertices){
            tempCount = Graphs.neighborListOf(this.graph,vertice).size();
            if(tempCount>maxConnection){
                maxConnection = tempCount;
                mostConnectedNode = vertice;
            }
        }
        return mostConnectedNode;
    }
/*
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawOval(150, 150, 100, 100);
        g2d.setColor(Color.black);
        g2d.fillOval(150, 150, 100, 100);
    }
    */
    private void initializeGUI(){
       // ListenableGraph<String, DefaultWeightedEdge> g = new ListenableUndirectedWeightedGraph<String, DefaultWeightedEdge>();
        jgxAdapter = new JGraphXAdapter<>(this.graph);
        setPreferredSize(DEFAULT_SIZE);
        this.setSize(DEFAULT_SIZE);
        this.setBackground(Color.WHITE);
        mxGraphComponent component = new mxGraphComponent(jgxAdapter);
        component.getViewport().setOpaque(true);
        component.getViewport().setBackground(Color.WHITE);
        component.setBorder(BorderFactory.createEmptyBorder());
        this.add(component);
        int radius = 100;
        mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
        //set geometry
        double panelVerticalPos = this.getY();
        System.out.println(panelVerticalPos);
        double panelWidth = this.getWidth();
        double panelHorizonalPos = this.getX();
        double panelHeight = this.getHeight();
        System.out.println(panelHorizonalPos);
        jgxAdapter.getModel().setGeometry(jgxAdapter.getDefaultParent(),
                new mxGeometry(0, panelHeight/10,
                        panelWidth, panelHeight)
        );
        //layout.setX0(panelUpperBorder);
        //layout.setY0(panelLeftBorder);
        layout.execute(jgxAdapter.getDefaultParent());
    }
    public void update(SimpleWeightedGraph<String, EdgeAdaptor> graph){
        this.graph = graph;
        this.removeAll();
        //initGUI();
        initializeGUI();
    }
}
