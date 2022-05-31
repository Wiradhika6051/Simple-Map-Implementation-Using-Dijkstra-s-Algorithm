package algorithm;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.text.DecimalFormat;
import java.util.*;

public class DjikstraAlgorithm {
    private int iteration;
    private SimpleWeightedGraph<String, DefaultWeightedEdge> graph;
    private Map<String,String[]> distanceMap;
    private long startTime;
    private long endTime;
    public DjikstraAlgorithm(SimpleWeightedGraph<String, DefaultWeightedEdge> graph){
        this.graph = graph;
        this.startTime = System.nanoTime();
    }
    public String[] solve(String startNode, String endNode){
        setupTable(startNode);
        return distanceMap.get(endNode);
    }
    public int getIterations(){
        return this.iteration;
    }
    public void setupTable(String startNode){
        distanceMap = new HashMap<>();
        iteration = 1;
        Set<String> nodeSet = this.graph.vertexSet();
        //isi prio queue isi node node
        PriorityQueue<Nodes> pq = new PriorityQueue<Nodes>(nodeSet.size(),new NodesComparator());
        for(String node:nodeSet){
            if(node==startNode){
                //node awal dikasih nilai 0
                pq.add(new Nodes(node,0f));
            }
            else{
                pq.add(new Nodes(node,Double.POSITIVE_INFINITY));
            }
        }

    }
    public String getTime(){
        double timeElapsed = (double)(endTime-startTime);
        String time;
        if(timeElapsed<=1_000){
            //kurang dari 1000 ns
            time = timeElapsed+" ns";
        }
        else if(timeElapsed<1_000_000_000){
            //kurang dari 1 s
            timeElapsed /= 1_000_000;
            time = String.format("%.4f ms",timeElapsed);
        }
        else{
            //lebih dari 1s
            timeElapsed /= 1_000_000_000;
            time = String.format("%.4f sekon",timeElapsed);
        }
        return time;
    }

}
class Nodes{
    public String nodeName;
    public double shortestDistance;

    public Nodes(String name, double distance){
        this.nodeName = name;
        this.shortestDistance = distance;
    }
}
class NodesComparator implements Comparator<Nodes> {
    public int compare(Nodes n1,Nodes n2){
        if(n1.shortestDistance<=n2.shortestDistance){
            return -1;
        }
        return 1;
    }
}