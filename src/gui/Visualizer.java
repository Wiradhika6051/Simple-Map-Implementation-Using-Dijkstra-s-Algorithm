package gui;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.*;
import com.mxgraph.view.mxGraphSelectionModel;
import com.mxgraph.view.mxStylesheet;
import org.jgrapht.*;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.*;
import utils.CustomJGraphXAdapter;
import utils.EdgeAdaptor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.List;

public class Visualizer extends JPanel {
    SimpleWeightedGraph<String, EdgeAdaptor> graph;
    private static final Dimension DEFAULT_SIZE = new Dimension(400, 320);
    private CustomJGraphXAdapter<String, EdgeAdaptor> jgxAdapter;
    private String[][] locationMatrix;
    private int arraySize;
    private GraphCanvas canvas;
    public static Visualizer visualizer;
    public mxGraphComponent component;

    public Visualizer(SimpleWeightedGraph<String, EdgeAdaptor> graph){
        this.graph = graph;
        this.setBackground(Color.WHITE);
        visualizer = this;
        //initGUI();
        //initializeGUI();
    }
    void initGUI(){
        this.canvas = canvas;
        generateLocationMatrix();
        fillLocationMatrix();
    }
    public void resetGraph(){
        jgxAdapter.getModel().beginUpdate();
        try {
            jgxAdapter.clearSelection();
            jgxAdapter.selectAll();
            Object[] cells = jgxAdapter.getSelectionCells();
            Object[] edges = jgxAdapter.getAllEdges(cells);
            jgxAdapter.setCellStyles(mxConstants.STYLE_STROKECOLOR, "f0f0f0", edges);//warna edge
            jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"D7B698",edges);//warna weight
            for(Object c:cells) {
                mxCell cell = (mxCell) c;
                mxGeometry geometry = cell.getGeometry();
                if (cell.isVertex()) {
                    geometry.setWidth(60);
                    geometry.setHeight(60);
                    //cell.setStyle("ROUNDED");
                    jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"774300",new Object[]{cell});
                    jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, "f0f0f0", new Object[]{cell});
                }
            }
        }
        finally{
            jgxAdapter.getModel().endUpdate();
        }
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
        jgxAdapter = new CustomJGraphXAdapter<>(this.graph);
        setPreferredSize(DEFAULT_SIZE);
        this.setSize(DEFAULT_SIZE);
        this.setBackground(Color.WHITE);
        component = new mxGraphComponent(jgxAdapter);
        component.getViewport().setOpaque(true);
        component.getViewport().setBackground(Color.WHITE);
        component.setBorder(BorderFactory.createEmptyBorder());
        component.setDragEnabled(false);
        component.setAutoExtend(true);
        component.getVerticalScrollBar().setVisible(true);
        component.getHorizontalScrollBar().setVisible(true);
        jgxAdapter.setMinimumGraphSize(new mxRectangle(
                MainPage.getInstance().getFractionSize(MainPage.getInstance().getFrameWidth(),11,40),
                MainPage.getInstance().getFractionSize(MainPage.getInstance().getFrameHeight(),1,40),
                MainPage.getInstance().getFractionSize(MainPage.getInstance().getFrameWidth(),28,40),
                MainPage.getInstance().getFractionSize(MainPage.getInstance().getFrameHeight(),28,40)
        ));
        component.getGraphControl().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                mxCell cell =(mxCell) Visualizer.visualizer.component.getCellAt(e.getX(), e.getY());
                if(cell != null)
                {
                    if(cell.isVertex()){
                        boolean isStartNodeSelected = MainPage.getInstance().getStartNode()!=null;
                        boolean isEndNodeSelected = MainPage.getInstance().getEndNode()!=null;
                        String startNode = MainPage.getInstance().getStartNode();
                        String endNode = MainPage.getInstance().getEndNode();
                        if(!isStartNodeSelected && !isEndNodeSelected){
                            //pilih start node
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"ffffff",new Object[]{cell});//fontcolor hitam
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR,"0000ff",new Object[]{cell});// bg biru
                            MainPage.getInstance().setStartNode(cell.getValue().toString());
                            System.out.println("sn:"+MainPage.getInstance().getStartNode()+" en:"+MainPage.getInstance().getEndNode());
                            MainPage.getInstance().selectHint.setText("Pilih End Vertex");
                        }
                        else if(isStartNodeSelected && !isEndNodeSelected && cell.getValue().toString().equals(startNode)){
                            //batal pilih start node
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"000000",new Object[]{cell});//fontcolor putih
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR,"f0f0f0",new Object[]{cell});//bg abu abu
                            MainPage.getInstance().setStartNode(null);
                            System.out.println("sn:"+MainPage.getInstance().getStartNode()+" en:"+MainPage.getInstance().getEndNode());
                            MainPage.getInstance().selectHint.setText("Pilih Start Vertex");
                        }
                        else if(isStartNodeSelected&& !isEndNodeSelected && !cell.getValue().toString().equals(startNode)){
                            //pilih end node
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"0000CC",new Object[]{cell});//fontcolor biru
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR,"00ff00",new Object[]{cell});//bg hijau
                            MainPage.getInstance().setEndNode(cell.getValue().toString());
                            System.out.println("sn:"+MainPage.getInstance().getStartNode()+" en:"+MainPage.getInstance().getEndNode());
                            MainPage.getInstance().selectHint.setText("Tekan Tombol Run");
                        }
                        else if(isStartNodeSelected&& isEndNodeSelected && cell.getValue().toString().equals(endNode)){
                            //batal pilih end node
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"000000",new Object[]{cell});//fontcolor putih
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR,"f0f0f0",new Object[]{cell});//bg abu abu
                            MainPage.getInstance().setEndNode(null);
                            System.out.println("sn:"+MainPage.getInstance().getStartNode()+" en:"+MainPage.getInstance().getEndNode());
                            MainPage.getInstance().selectHint.setText("Pilih End Vertex");
                        }
                        //cek apakah startNode dan endNode nya dah ada, kalau ada aktifin run button
                        startNode = MainPage.getInstance().getStartNode();
                        endNode = MainPage.getInstance().getEndNode();
                        if(startNode!=null && endNode!=null) {
                            MainPage.getInstance().runButton.setEnabled(true);
                        }
                    }
                }
            }
        });
        /*
        jgxAdapter.getSelectionModel().addListener(mxEvent.CHANGE, new mxEventSource.mxIEventListener() {
            @Override
            public void invoke(Object o, mxEventObject mxEventObject) {
                mxGraphSelectionModel sm = (mxGraphSelectionModel) o;
                mxCell cell = (mxCell) sm.getCell();
                if (cell != null && cell.isVertex()) {

                    if(MainPage.getInstance().getStartNode()==null && MainPage.getInstance().getEndNode()==null){
                        jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"ffffff",new Object[]{cell});
                        jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR,"0000ff",new Object[]{cell});
                        MainPage.getInstance().setStartNode(cell.getValue().toString());
                        System.out.println("sn:"+MainPage.getInstance().getStartNode()+" en:"+MainPage.getInstance().getEndNode());
                    }
                    jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR,"00ffff",new Object[]{cell});
                    jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"ffffff",new Object[]{cell});

                //MainPage.getInstance().setStartNode();
            }
        }});*/
        //component.setEnabled(false);
        this.add(component);
        int radius = 100;
        mxFastOrganicLayout layout = new mxFastOrganicLayout(jgxAdapter);
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
        jgxAdapter.getModel().beginUpdate();
        try{
           // jgxAdapter.setAlternateEdgeStyle();
            //jgxAdapter.
            jgxAdapter.clearSelection();
            jgxAdapter.selectAll();
            Object[] cells = jgxAdapter.getSelectionCells();
            mxStylesheet stylesheet = jgxAdapter.getStylesheet();
            jgxAdapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_EDGE,mxConstants.EDGESTYLE_LOOP);
            //shape vertices
            Hashtable<String,Object> style_v = new Hashtable<>();
            style_v.put(mxConstants.STYLE_SHAPE,mxConstants.SHAPE_ACTOR);
            stylesheet.putCellStyle("ROUNDED",style_v);
            jgxAdapter.setAutoSizeCells(true);
            jgxAdapter.setAllowDanglingEdges(false);
            jgxAdapter.setCellsBendable(false);
            jgxAdapter.setCellsDeletable(false);
            jgxAdapter.setCellsLocked(false);
            jgxAdapter.setCellsEditable(false);
            //jgxAdapter.set
            //shape edges
            Hashtable<String,Object> style_e = new Hashtable<>();
           // style_e.put(mxConstants.STYLE_SHAPE,mxConstants.);
            stylesheet.putCellStyle("LINE EDGE",style_e);
            //layout.setForceConstant(150);
            Object[] edges = jgxAdapter.getAllEdges(cells);
            jgxAdapter.setCellStyles(mxConstants.STYLE_STROKECOLOR, "f0f0f0", edges);//warna edge
            jgxAdapter.setCellStyles(mxConstants.STYLE_STROKEWIDTH,Integer.toString(6),edges);
            jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"D7B698",edges);
            jgxAdapter.setCellStyles(mxConstants.STYLE_ENDARROW,mxConstants.NONE,edges);
            jgxAdapter.setCellStyles(mxConstants.STYLE_VERTICAL_ALIGN,mxConstants.ALIGN_TOP,edges);
            jgxAdapter.setCellStyles(mxConstants.STYLE_VERTICAL_LABEL_POSITION,mxConstants.ALIGN_BOTTOM,edges);
            jgxAdapter.setCellStyles(mxConstants.STYLE_FONTSIZE,Integer.toString(15),edges);
            for(Object c:cells){
                mxCell cell = (mxCell) c;
                mxGeometry geometry = cell.getGeometry();
                if(cell.isVertex()){
                    geometry.setWidth(60);
                    geometry.setHeight(60);
                    //cell.setStyle("ROUNDED");
                    jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR,"f0f0f0",new Object[]{cell});
                    jgxAdapter.setCellStyles(mxConstants.STYLE_SHAPE,mxConstants.SHAPE_ELLIPSE,new Object[]{cell});
                }
                else if(cell.isEdge()){
                 //   cell.setStyle("LINE EDGE");
                }

               // mxConstants.
             //   jgxAdapter.setCellsLocked(true);
            }
        }
        finally{
            jgxAdapter.getModel().endUpdate();
        }
        layout.execute(jgxAdapter.getDefaultParent());
    }
    public void update(SimpleWeightedGraph<String, EdgeAdaptor> graph){
        this.graph = graph;
        this.removeAll();
        //initGUI();
        initializeGUI();
    }
    public mxCell getVertex(String name){
        jgxAdapter.clearSelection();
        jgxAdapter.selectAll();
        Object[] cells = jgxAdapter.getSelectionCells();
        for(Object c:cells){
            mxCell cell = (mxCell) c;
            if(cell.getValue().equals(name)){
                return cell;
            }
        }
        return null;
    }
    public Object[] getAllCells(){
        jgxAdapter.clearSelection();
        jgxAdapter.selectAll();
        Object[] cells = jgxAdapter.getSelectionCells();
        return cells;
    }
    public void updateCell(Object[] cells,String color,String fontColor){
        jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,fontColor,cells);//fontcolor
        jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR,color,cells);//bg
    }
    public void updateEdge(Object[] edges,String color,String fontColor) {
        jgxAdapter.setCellStyles(mxConstants.STYLE_STROKECOLOR, color, edges);//warna edge
        jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,fontColor,edges);//warna font edge

    }
    public Object getEdge(String startNode,String endNode){
        HashMap<EdgeAdaptor,com.mxgraph.model.mxICell> edgeToCellMap = jgxAdapter.getEdgeToCellMap();
        return (Object)edgeToCellMap.get(this.graph.getEdge(startNode,endNode));
    }
    public void updateStartEndNode(String start,String end){//update start node
        jgxAdapter.clearSelection();
        jgxAdapter.selectAll();
        //start node
        mxCell cell = getVertex(start);
        jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"ffffff",new Object[]{cell});//fontcolor hitam
        jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR,"0000ff",new Object[]{cell});// bg biru
        //end node
        cell = getVertex(end);
        jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"0000CC",new Object[]{cell});//fontcolor biru
        jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR,"00ff00",new Object[]{cell});//bg hijau
    }
    public void resetGraphValue(){
        Object[] cells = getAllCells();
        //reset value di awal
        for(Object c:cells){
            mxCell cell = (mxCell) c;
            String cell_old_value = ((String)cell.getValue());
            for(int i =0;i<cell_old_value.length();i++){
                System.out.println(cell_old_value.charAt(i));
            }
            int newline_pos = cell_old_value.indexOf('\n');
            if(newline_pos!=-1) {
                String new_value = cell_old_value.substring(0, newline_pos);
                jgxAdapter.getModel().setValue(cell, new_value);
            }
            //cell.setValue(new_value);
        }
    }

    public void renderStepGraph(Deque<String> histori_node,Map<String, Map<String,Double>> it_his,int counter){
        resetGraphValue();
        Map<String,Double> record;
        //LinkedList<String> temp = (LinkedList<String>) histori_node.clone();
        resetGraph();
        int idx=0;
        String key=null;
        if(histori_node==null){
            System.out.println("Kok null?");
        }
        System.out.println(histori_node.size());
        System.out.println("KOnter"+counter);
        for(String node: histori_node){
            if(idx==counter){
                key = node;
                break;
            }
            else{
                idx++;
            }
            System.out.println(node);
        }
        System.out.println("key"+key);
        //warnain node yang diperiksa sebagai merah
        if(!key.equals("INITIAL_STATE")){
            mxCell keyCell = getVertex(key);
            updateCell(new Object[]{keyCell},"ff0000","55E675");
        }
        record = it_his.get(key);
        if(record==null){
            System.out.println("napa kosong dah?");
        }
        for(String recKey:record.keySet()){
            System.out.println(recKey);
            mxCell cell = getVertex(recKey);
            if(cell==null){
                System.out.println("fanfare");
            }
            Double value = record.get(recKey);
            String val=null;
            if(value==Double.POSITIVE_INFINITY){
                val = "∞";
            }
            else{
                val = Double.toString(value);
            }
            String new_value = cell.getValue()+"\n"+val;
            jgxAdapter.getModel().setValue(cell,new_value);
            //cell.setValue(new_value);
        }

    }
}/*
    private void uncolorSingleVertex(String label) {
    List<String> nodes = new List<>();
    }
    for(int i=0; i<nodes.size(); i++) {
        // keeps all the vertices
         Object o = nodes.get(i);
         if(graph.getModel().isVertex(o) && graph.getLabel(o).equals(label) )
         {
             mxCell vertex = (mxCell)o;
             graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#ffffff", new Object[]{vertex});

         } } }
*/
        //}
