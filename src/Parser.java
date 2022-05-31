import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.*;

public class Parser {
    private final String path;
    private File file;

    public Parser(String path){
        this.path = path;
    }
    public SimpleWeightedGraph<String, DefaultWeightedEdge> parse(){
        BufferedReader reader;
        String line;
        int nodeCount = -1;//diinisialisasi dengan -1
        int edgeCount = -1;
        int tempConvert;
        String[] token;
        SimpleWeightedGraph<String, DefaultWeightedEdge> g = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        DefaultWeightedEdge edge;
        try{
            file = new File("test/"+this.path);
            System.out.println("test/"+this.path);
            reader = new BufferedReader(new FileReader(this.file));
            do {
                line = reader.readLine();
                if(line.charAt(0)!='#' && line!=null){
                    //bukan komentar
                    token = line.split(" ");
                    if(token.length==1){
                        //periksa apakah ini jumlah atau daftar node
                        tempConvert = Integer.parseInt(token[0]);
                        //asumsi udah aman
                        if(tempConvert<0){
                            throw new Exception("Jumlah node/edge harus >=0");
                        }
                        if(nodeCount==-1){
                            nodeCount = tempConvert;
                            System.out.println(nodeCount);
                        }
                        else if(edgeCount==-1){
                            edgeCount= tempConvert;
                            System.out.println(edgeCount);
                        }
                        //isi node/graph tergantung yang barusan diisi, karena edge diisi terakhiran, ngeceknya mulai dari edge dulu
                        if(edgeCount>-1){
                            for(int i =0;i<edgeCount;i++){
                                do{
                                    line = reader.readLine();
                                }while(line.charAt(0)=='#' && line!=null);
                                if(line==null){
                                    throw new Exception("Jumlah edge yang dideclare dan di daftar berbeda");
                                }
                                //periksa apakah tokennya ada 3
                                token = line.split(" ");
                                if(token.length!=3){
                                    throw new Exception("Format edge yang benar: 'kota1 kota2 weight'");
                                }
                                //tokennya sudah 3
                                //add edge
                                System.out.println(token[0]+","+token[1]+","+token[2]);
                                edge = g.addEdge(token[0],token[1]);
                                //set weight
                                g.setEdgeWeight(edge,Double.parseDouble(token[2]));
                            }
                        }
                        else if(nodeCount>-1){
                            for(int i =0;i<nodeCount;i++){
                                do{
                                    line = reader.readLine();
                                }while(line.charAt(0)=='#' && line!=null);
                                if(line==null){
                                    throw new Exception("Jumlah node yang dideclare dan di daftar berbeda");
                                }
                                token = line.split(" ");
                                if(token.length!=1){
                                    throw new Exception("Tidak boleh ada spasi di nama node!");
                                }
                                //add node
                                g.addVertex(token[0]);
                                System.out.println(token[0]);
                            }
                        }
                    }
                }
            }while(line!=null);
            System.out.println("lewatkah?");
        }
        catch(FileNotFoundException fnf){
            System.out.println("File "+this.path+" tidak ditemukan di folder test!");
            System.out.println("ehe 2!?");
        }
        catch(IOException ioe){
            System.out.println("File kosong!");
            System.out.println("ehe matamu2!?");
        }
        catch(NumberFormatException nfe){
            System.out.println("ehe matamu1!?");
            System.out.println("Jumlah node/edge yang didefinisikan tidak sama dengan daftar node dan edge yang ada");
        }
        catch(Exception e){
            System.out.println("ehe matamu!?");
            System.out.println(e.getMessage());
        }
        return g;
    }
}
