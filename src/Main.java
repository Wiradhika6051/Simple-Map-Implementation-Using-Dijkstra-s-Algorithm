import algorithm.DjikstraAlgorithm;
import gui.Visualizer;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.Deque;
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
        //pilih vertex mana yang ingin dicari (edge start)
        String[] vertexArray = new String[sv.size()];
        sv.toArray(vertexArray);
        System.out.println("Pilih vertex yang ingin dipilih sebagai start vertex:");
        for(int i=0;i<vertexArray.length;i++){
            System.out.println((i+1)+"."+vertexArray[i]);
        }
      //  int startVertex = sc.nextInt();
      //  String start = vertexArray[startVertex-1];
        System.out.println("Pilih vertex yang ingin dipilih sebagai end vertex:");
        for(int i=0;i<vertexArray.length;i++){
    //        if(i!=startVertex) {
                System.out.println((i+1) + "." + vertexArray[i]);
      //      }
        }
      //  int endVertex  = sc.nextInt();
        //cari jawaban
        DjikstraAlgorithm da = new DjikstraAlgorithm(graph);
        Deque<String> solusi = da.solve("Aceh","C");
        for (String node:solusi){
            System.out.println("-"+node);
        }
    }
}
