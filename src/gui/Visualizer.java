package gui;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
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
        mxGraphComponent component = new mxGraphComponent(jgxAdapter);
        this.add(component);
        int radius = 100;
        mxIGraphLayout layout = new mxCircleLayout(jgxAdapter);

        layout.execute(jgxAdapter.getDefaultParent());
    }
}
