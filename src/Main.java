import gui.Visualizer;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.Scanner;

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
    }
}
