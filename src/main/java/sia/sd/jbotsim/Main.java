package sia.sd.jbotsim;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.ui.JViewer;

/**
 * Created by Haritz Medina on 16/02/2016.
 */
public class Main{

    private static final int NUM_NODOS = 10;
    private static int ticket = 0;

    public static void main(String[] args) {
        Topology topologia = new Topology(700, 600);   // creación de la topología
        topologia.setDefaultNodeModel(NodeIni.class);

        NodeNeighbours[] nodos = new NodeNeighbours[NUM_NODOS];// Array que contendrá los nodos creados

        int [] coordenadas = new int[2];      // Array auxiliar para calcular las coordenadas de cada nodo

        for (int i = 0; i < NUM_NODOS; i++) {
            nodos[i] = new NodeNeighbours();          // Creación del nodo
            obtenerCoordenadasCirculo(i,NUM_NODOS,coordenadas);
            nodos[i].setLocation(coordenadas[0],coordenadas[1]);
            topologia.addNode(nodos[i]);      // Añadir el nodo a la topología
        }

        JViewer j = new JViewer(topologia);   // mostrar el simulador
        j.setTitle("First Simulation: custom node behaviour (NodeIni)");
        topologia.start();

    }

    private static void obtenerCoordenadasCirculo(int indice,int NumNodos,int [] coordenadas) {
        // función que, a partir de un índice de elemento y un número
        // total de elementos, // calcula las coordenadas que ese elemento
        // debería tener para que todos los elementos // formen un círculo
        int pos_central_x = 300;
        int pos_central_y = 300;
        int radio = 200;
        double angulo= (360/NumNodos)*indice;   // angulo en grados
        angulo = angulo * 2.0 * Math.PI/360.0;  // angulo en radianes
        int x = (int)(pos_central_x + (Math.sin(angulo)*radio));
        int y = (int)(pos_central_y + (Math.cos(angulo)*radio));
        coordenadas[0] = x;
        coordenadas[1] = y;
    }

    public static int giveTicket() {
        return ticket++;
    }

}