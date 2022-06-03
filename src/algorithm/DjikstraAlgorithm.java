package algorithm;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import utils.EdgeAdaptor;

import java.text.DecimalFormat;
import java.util.*;

public class DjikstraAlgorithm {
    private int iteration;
    private SimpleWeightedGraph<String, EdgeAdaptor> graph;
    private Map<String,String> distanceMap;
    private long startTime;
    private long endTime;
    private PriorityQueue<Nodes> pq;
    public DjikstraAlgorithm(SimpleWeightedGraph<String, EdgeAdaptor> graph){
        this.graph = graph;
        this.startTime = System.nanoTime();
    }
    public Deque<String> solve(String startNode, String endNode){
        setupTable(startNode);
        return constructSolution(startNode,endNode);
    }
    private Deque<String> constructSolution(String startNode,String endNode){
        //return
        Deque<String> daftarNode = new LinkedList<>();
        String selectedNode;
        selectedNode = endNode;
        do{
            daftarNode.addFirst(selectedNode);
            selectedNode = distanceMap.get(selectedNode);
            System.out.println(distanceMap.get("A"));
        }while(selectedNode!=null);
        this.endTime = System.nanoTime();
        return daftarNode;
    }
    public int getIterations(){
        return this.iteration;
    }
    private void setupTable(String startNode){
        distanceMap = new HashMap<>();
        iteration = 1;
        //Set<String> nodeSet = this.graph.vertexSet();
        List<String> nodeSet = new ArrayList<>(this.graph.vertexSet());
        String selectedNode = startNode;
        List<String> neighborList;
        double curDist;
        Nodes vertice;
        Nodes curNode;
        //isi prio queue isi node node
        pq = new PriorityQueue<Nodes>(nodeSet.size(),new NodesComparator());
        for(String node:nodeSet){
            /*
            System.out.println("Start node:"+startNode+",Sekarang:"+node);
            if(node.charAt(0)=='A'){
                System.out.println("Panjang kata:"+node.length());
                System.out.println("Panjang start node:"+startNode.length());
                if(node!=startNode){
                    System.out.println("kenapa "+startNode+" !="+node);
                }
            }
            */
            if(node.equals(startNode)){
                System.out.println("Start nodenyaL:"+node);
                //node awal dikasih nilai 0
                pq.add(new Nodes(node,0f));
            }
            else{
                pq.add(new Nodes(node,Double.POSITIVE_INFINITY));
            }
            distanceMap.put(node,null);
        }
        //iterasi algoritma djikstra
        while(nodeSet.size()>0){
            neighborList = Graphs.neighborListOf(this.graph,selectedNode);
            for(String neighbor:neighborList){
                if(nodeSet.contains(neighbor)) {
                    curDist = this.graph.getEdgeWeight(graph.getEdge(selectedNode,neighbor));
                    curNode = pq_search(selectedNode);
                    vertice = pq_search(neighbor);
                    System.out.println("kelewat pas "+selectedNode+" "+neighbor);
                    System.out.println("verticenya:"+vertice.shortestDistance);
                    if(vertice!=null && curNode.shortestDistance+curDist < vertice.shortestDistance){
                        vertice.shortestDistance = curDist;
                        System.out.println("keganti jadi:"+selectedNode);
                        distanceMap.put(neighbor, selectedNode);
                    }
                }
            }
            iteration++;
            nodeSet.remove(selectedNode);
            curNode = getSmallest(nodeSet);
            if(curNode==null){
                return;
            }
            selectedNode = curNode.nodeName;
            /*
            do{
                curNode = pq.poll();
                if(curNode!=null) {
                    selectedNode = curNode.nodeName;
                }
            }while(!nodeSet.contains(selectedNode) && curNode!=null);
            */
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
    private Nodes pq_search(String name){
        Nodes[] nodesarray = this.pq.toArray(new Nodes[pq.size()]);
        for(Nodes node:nodesarray){
            System.out.println("nama nodenya:"+node.nodeName+" "+node.shortestDistance);
            if(node.nodeName.equals(name)){
                return node;
            }
        }
        return null;
    }
    private Nodes getSmallest(List<String> ls){
        Nodes smallest = null;
        Nodes temp;
        for(String nodename:ls){
            temp = pq_search(nodename);
            if(smallest==null){
                smallest = temp;
            }
            else if(temp.shortestDistance< smallest.shortestDistance){
                smallest = temp;
            }
        }
        return smallest;
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