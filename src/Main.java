import gui.Visualizer;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.Scanner;
import java.util.Set;

public class Main {
    public static  void main(String[] args){
        //Visualizer frame = new Visualizer();
        //frame.setVisible(true);
        //CLI
        //input nama file
        System.out.print("Masukkan nama file: ");
        Scanner sc = new Scanner(System.in);
        String filepath = sc.nextLine();
        Parser parser = new Parser(filepath);
        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = parser.parse();
        if(graph == null){
            System.out.println("Lah kosong?");
        }
        //tampilkan daftar node
        Set<String> sv= graph.vertexSet();
        if(sv==null){
            System.out.println("kosong?");
        }
        else{
            sv.forEach((e)->System.out.println(e));
        }
        System.out.println(graph.getEdgeWeight(graph.getEdge("B","C")));
    }
}
