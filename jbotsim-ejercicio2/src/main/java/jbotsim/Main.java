package jbotsim;

import jbotsim.ui.JViewer;
//import jbotsimx.messaging.DelayMessageEngine;

public class Main {

    static int ticket = 0;

    static final int INITIAL_NUM_NODES = 1;
    public static Topology topologia;
    public static final int DEFAULT_TIME_SENDING_ALIVE = 100;
    public static final int DEFAULT_TIME_WAITING_ALIVE = 100;

    static public int getTicket() {
        return ticket++;
    }

    public static void main(String[] args) {

        topologia= new Topology(500, 400, false);   // creación de la topología
        topologia.setDefaultNodeModel(NodeOldestLeader.class);

//        DelayMessageEngine DME = new DelayMessageEngine(true,5);
//        topologia.setMessageEngine(DME);

        topologia.setClockSpeed(20);

        NodeOldestLeader[] nodos= new NodeOldestLeader[INITIAL_NUM_NODES];// Array que contendrá los nodos creados

        int [] coordenadas = new int[2];      // Array auxiliar para calcular las coordenadas de cada nodo

        for (int i = 0; i < INITIAL_NUM_NODES; i++) {
            nodos[i] = new NodeOldestLeader();          // Creación del nodo
            obtenerCoordenadasCirculo(i,INITIAL_NUM_NODES,coordenadas);
            nodos[i].setLocation(coordenadas[0],coordenadas[1]);
            topologia.addNode(nodos[i]);      // Añadir el nodo a la topología
        }

        JViewer j = new JViewer(topologia);   // mostrar el simulador
        j.setTitle("Exchanging Alives: simulation");
        topologia.start();
    }

    private static void obtenerCoordenadasCirculo(int indice,int NumNodos,int [] coordenadas) {
        // función que, a partir de un índice de elemento y un número
        // total de elementos, // calcula las coordenadas que ese elemento
        // debería tener para que todos los elementos // formen un círculo
        int pos_central_x = 300;
        int pos_central_y = 300;
        int radio = 170;
        double angulo= (360/NumNodos)*indice;   // angulo en grados
        angulo = angulo * 2.0 * Math.PI/360.0;  // angulo en radianes
        int x = (int)(pos_central_x + (Math.cos(angulo)*radio));
        int y = (int)(pos_central_y + (Math.sin(angulo)*radio));
        coordenadas[0] = x;
        coordenadas[1] = y;
    }

    public static void writeLog (String text) {
        System.out.println("T " + Main.topologia.getTime() + " " + text);
    }
}