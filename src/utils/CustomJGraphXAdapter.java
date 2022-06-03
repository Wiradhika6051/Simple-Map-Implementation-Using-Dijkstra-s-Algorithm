package utils;

import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;

public class CustomJGraphXAdapter<V, E> extends mxGraph implements GraphListener<V, E> {
    private Graph<V, E> graphT;
    private HashMap<V, mxICell> vertexToCellMap;
    private HashMap<E, mxICell> edgeToCellMap;
    private HashMap<mxICell, V> cellToVertexMap;
    private HashMap<mxICell, E> cellToEdgeMap;

    public CustomJGraphXAdapter(ListenableGraph<V, E> graph) {
        this((Graph)graph);
        graph.addGraphListener(this);
    }

    public CustomJGraphXAdapter(Graph<V, E> graph) {
        this.vertexToCellMap = new HashMap();
        this.edgeToCellMap = new HashMap();
        this.cellToVertexMap = new HashMap();
        this.cellToEdgeMap = new HashMap();
        if (graph == null) {
            throw new IllegalArgumentException();
        } else {
            this.graphT = graph;
            this.insertJGraphT(graph);
            this.setAutoSizeCells(true);
        }
    }

    public HashMap<V, mxICell> getVertexToCellMap() {
        return this.vertexToCellMap;
    }

    public HashMap<E, mxICell> getEdgeToCellMap() {
        return this.edgeToCellMap;
    }

    public HashMap<mxICell, E> getCellToEdgeMap() {
        return this.cellToEdgeMap;
    }

    public HashMap<mxICell, V> getCellToVertexMap() {
        return this.cellToVertexMap;
    }

    public void vertexAdded(GraphVertexChangeEvent<V> e) {
        this.addJGraphTVertex(e.getVertex());
    }

    public void vertexRemoved(GraphVertexChangeEvent<V> e) {
        mxICell cell = (mxICell)this.vertexToCellMap.remove(e.getVertex());
        this.removeCells(new Object[]{cell});
        this.cellToVertexMap.remove(cell);
        this.vertexToCellMap.remove(e.getVertex());
        ArrayList<E> removedEdges = new ArrayList();
        Iterator i$ = this.cellToEdgeMap.values().iterator();

        Object edge;
        while(i$.hasNext()) {
            edge = i$.next();
            if (!this.graphT.edgeSet().contains(edge)) {
                removedEdges.add((E)edge);
            }
        }

        i$ = removedEdges.iterator();

        while(i$.hasNext()) {
            edge = i$.next();
            this.removeEdge((E)edge);
        }

    }

    public void edgeAdded(GraphEdgeChangeEvent<V, E> e) {
        this.addJGraphTEdge(e.getEdge());
    }

    public void edgeRemoved(GraphEdgeChangeEvent<V, E> e) {
        this.removeEdge(e.getEdge());
    }

    private void removeEdge(E edge) {
        mxICell cell = (mxICell)this.edgeToCellMap.remove(edge);
        this.removeCells(new Object[]{cell});
        this.cellToEdgeMap.remove(cell);
        this.edgeToCellMap.remove(edge);
    }

    private void addJGraphTVertex(V vertex) {
        this.getModel().beginUpdate();

        try {
            mxICell cell = (mxICell)this.insertVertex(this.defaultParent, (String)null, vertex, 0.0D, 0.0D, 0.0D, 0.0D);
            this.updateCellSize(cell);
            this.vertexToCellMap.put(vertex, cell);
            this.cellToVertexMap.put(cell, vertex);
        } finally {
            this.getModel().endUpdate();
        }

    }

    private void addJGraphTEdge(E edge) {
        this.getModel().beginUpdate();

        try {
            V sourceVertex = this.graphT.getEdgeSource(edge);
            V targetVertex = this.graphT.getEdgeTarget(edge);
            if (this.vertexToCellMap.containsKey(sourceVertex) && this.vertexToCellMap.containsKey(targetVertex)) {
                Object sourceCell = this.vertexToCellMap.get(sourceVertex);
                Object targetCell = this.vertexToCellMap.get(targetVertex);
                mxICell cell = (mxICell)this.insertEdge(this.defaultParent, (String)null, edge, sourceCell, targetCell);
                this.updateCellSize(cell);
                this.edgeToCellMap.put(edge, cell);
                this.cellToEdgeMap.put(cell, edge);
                return;
            }
        } finally {
            this.getModel().endUpdate();
        }

    }

    private void insertJGraphT(Graph<V, E> graph) {
        Iterator i$ = graph.vertexSet().iterator();

        Object edge;
        while(i$.hasNext()) {
            edge = i$.next();
            this.addJGraphTVertex((V)edge);
        }

        i$ = graph.edgeSet().iterator();

        while(i$.hasNext()) {
            edge = i$.next();
            this.addJGraphTEdge((E)edge);
        }

    }

    @Override
    public boolean isCellSelectable(Object cell){
        if(model.isEdge(cell)){
            return false;
        }
        return super.isCellSelectable(cell);
    }
    @Override
    public boolean isCellResizable(Object cell){
        return false;
    }
}
