package gui;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.*;
import com.mxgraph.view.mxStylesheet;
import org.jgrapht.graph.*;
import utils.CustomJGraphXAdapter;
import utils.EdgeAdaptor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class Visualizer extends JPanel {
    SimpleWeightedGraph<String, EdgeAdaptor> graph;
    private static final Dimension DEFAULT_SIZE = new Dimension(400, 320);
    private CustomJGraphXAdapter<String, EdgeAdaptor> jgxAdapter;
    public static Visualizer visualizer;
    public mxGraphComponent component;

    public Visualizer(SimpleWeightedGraph<String, EdgeAdaptor> graph){
        this.graph = graph;
        this.setBackground(Color.WHITE);
        visualizer = this;
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
                    jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"774300",new Object[]{cell});
                    jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, "f0f0f0", new Object[]{cell});
                }
            }
        }
        finally{
            jgxAdapter.getModel().endUpdate();
        }
    }
    private void initializeGUI(){
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
                            MainPage.getInstance().selectHint.setText("Pilih End Vertex");
                        }
                        else if(isStartNodeSelected && !isEndNodeSelected && cell.getValue().toString().equals(startNode)){
                            //batal pilih start node
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"000000",new Object[]{cell});//fontcolor putih
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR,"f0f0f0",new Object[]{cell});//bg abu abu
                            MainPage.getInstance().setStartNode(null);
                            MainPage.getInstance().selectHint.setText("Pilih Start Vertex");
                        }
                        else if(isStartNodeSelected&& !isEndNodeSelected && !cell.getValue().toString().equals(startNode)){
                            //pilih end node
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"0000CC",new Object[]{cell});//fontcolor biru
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR,"00ff00",new Object[]{cell});//bg hijau
                            MainPage.getInstance().setEndNode(cell.getValue().toString());
                            MainPage.getInstance().selectHint.setText("Tekan Tombol Run");
                        }
                        else if(isStartNodeSelected&& isEndNodeSelected && cell.getValue().toString().equals(endNode)){
                            //batal pilih end node
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR,"000000",new Object[]{cell});//fontcolor putih
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR,"f0f0f0",new Object[]{cell});//bg abu abu
                            MainPage.getInstance().setEndNode(null);
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
        this.add(component);
        mxFastOrganicLayout layout = new mxFastOrganicLayout(jgxAdapter);
        //set geometry
        double panelWidth = this.getWidth();
        double panelHeight = this.getHeight();
        jgxAdapter.getModel().setGeometry(jgxAdapter.getDefaultParent(),
                new mxGeometry(0, panelHeight/10,
                        panelWidth, panelHeight)
        );
        jgxAdapter.getModel().beginUpdate();
        try{
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
            //shape edges
            Hashtable<String,Object> style_e = new Hashtable<>();
            stylesheet.putCellStyle("LINE EDGE",style_e);
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
                    jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR,"f0f0f0",new Object[]{cell});
                    jgxAdapter.setCellStyles(mxConstants.STYLE_SHAPE,mxConstants.SHAPE_ELLIPSE,new Object[]{cell});
                }
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
        return jgxAdapter.getSelectionCells();
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
        return edgeToCellMap.get(this.graph.getEdge(startNode,endNode));
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
            int newline_pos = cell_old_value.indexOf('\n');
            if(newline_pos!=-1) {
                String new_value = cell_old_value.substring(0, newline_pos);
                jgxAdapter.getModel().setValue(cell, new_value);
            }
        }
    }

    public void renderStepGraph(Deque<String> histori_node,Map<String, Map<String,Double>> it_his,int counter){
        resetGraphValue();
        Map<String,Double> record;
        resetGraph();
        int idx=0;
        String key=null;
        for(String node: histori_node){
            if(idx==counter){
                key = node;
                break;
            }
            else{
                idx++;
            }
        }
        //warnain node yang diperiksa sebagai merah
        try{
            if(!key.equals("INITIAL_STATE")){
                mxCell keyCell = getVertex(key);
                updateCell(new Object[]{keyCell},"ff0000","55E675");
            }
        }
        catch(NullPointerException npe){
            //
        }
        record = it_his.get(key);
        for(String recKey:record.keySet()){
            mxCell cell = getVertex(recKey);
            Double value = record.get(recKey);
            String val;
            if(value==Double.POSITIVE_INFINITY){
                val = "∞";
            }
            else{
                val = Double.toString(value);
            }
            String new_value = cell.getValue()+"\n"+val;
            jgxAdapter.getModel().setValue(cell,new_value);
        }
    }
}