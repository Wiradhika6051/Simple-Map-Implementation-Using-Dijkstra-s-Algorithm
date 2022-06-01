package utils;

import org.jgrapht.graph.DefaultWeightedEdge;

public class EdgeAdaptor extends DefaultWeightedEdge {
    @Override
    public String toString() {
        return String.valueOf(getWeight());
    }

}