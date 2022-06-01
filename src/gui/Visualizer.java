package gui;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.*;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.*;
import javax.swing.*;
import java.awt.*;

public class Visualizer extends JPanel {
    SimpleWeightedGraph<String, DefaultWeightedEdge> graph;
    private static final Dimension DEFAULT_SIZE = new Dimension(400, 320);
    private JGraphXAdapter<String, DefaultWeightedEdge> jgxAdapter;

    public Visualizer(SimpleWeightedGraph<String, DefaultWeightedEdge> graph){
        this.graph = graph;
        initializeGUI();
    }
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
    public void update(SimpleWeightedGraph<String, DefaultWeightedEdge> graph){
        this.graph = graph;
        this.removeAll();
        initializeGUI();
    }
}
