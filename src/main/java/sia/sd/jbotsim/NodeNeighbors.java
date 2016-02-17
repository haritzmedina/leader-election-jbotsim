package sia.sd.jbotsim;

import jbotsim.Message;
import jbotsim.Node;

import java.awt.*;
import java.util.Vector;

public class NodeNeighbors extends Node {

    private Vecindad vecindad = new Vecindad(); // clase privada para guardar info sobre la vecindad

    Timer TempoEnvioAlive;                      // Temporizador para el envío periódico de ALIVEs
    Timer TempoActualizarVecindad;              // Temporizador para la actualización periódica de la vecindad

    // Código que se ejecutará al crear el objeto (método con el mismo nombre que la clase)
    public NodeNeighbors(){

        TempoEnvioAlive = new Timer(10);
        TempoActualizarVecindad = new Timer(10);

        this.setCommunicationRange(400);
    }
    public void onClock(){
        // Incrementar temporizadores asociados a las distintas tareas
        TempoEnvioAlive.incr();
        TempoActualizarVecindad.incr();
        // TAREA 1: informar de la presencia a sus nodos vecinos
        if (TempoEnvioAlive.triggered()) {
            EnviarMensajeALIVE();
            TempoEnvioAlive.reset();
        }
        // TAREA 2: comprobar el estado de la vecindad (en función
        // del tick en el que se recibió el mensaje de cada vecino
        if (TempoActualizarVecindad.triggered()) {
            this.vecindad.actualizarVecindad(this.getTime());
            TempoActualizarVecindad.reset();
            if (this.vecindad.cardinalidad()>3) {
                this.setColor(Color.green);
            } else {
                this.setColor(Color.red);
            }
        }
        this.ActualizarToolTip();
    }
    public void onMessage(Message msg) {
        ContentMessage contenidoMensaje = (ContentMessage) msg.getContent();
        if (contenidoMensaje.type == ContentMessage.ALIVE) {
            vecindad.actualizarTick(msg.getSender().getID(), this.getTime());
        }
    }

    private void ActualizarToolTip(){
        String texto = "Id: " + this.getID() + ". Vecinos: " + this.vecindad.imprimir();
        this.setState(texto);
    }

    private void EnviarMensajeALIVE(){
        ContentMessage content = new ContentMessage();
        content.type = ContentMessage.ALIVE;
        this.sendAll(new Message(content));
    }
}

class ContentMessage {
    static final int ALIVE = 0;     // types of messages
    static final int OTHERTYPE = 1;

    public int type;        // type of message
    // more content
}


class Vecindad{
    // Clase que permite almacenar información sobre la vecindad
    // de cada nodo
    int antiguedadMaxima = 200; // si no ha recibido información de un vecino en este tiempo, se considera que ya no está

    private Vector<NodoVecino> NodosVecinos = new Vector<NodoVecino>();

    class NodoVecino {
        int id;             // identificador del vecino
        int UltimoTick;     // tick de reloj en el cuál recibió comunicación de ese nodo
    }
    public void add(int nodoid,int tickUltimoContacto) {
        NodoVecino nodo = new NodoVecino();
        nodo.id= nodoid;
        nodo.UltimoTick = tickUltimoContacto;
        this.NodosVecinos.add(nodo);
    }
    public void actualizarTick(int nodoid,int tickUltimoContacto) {
        boolean Encontrado = false;
        for (NodoVecino Vecino : NodosVecinos) {
            if (Vecino.id == nodoid) {
                Vecino.UltimoTick = tickUltimoContacto;
                Encontrado = true;
            }
        }
        if (Encontrado == false)
            this.add(nodoid, tickUltimoContacto);
    }
    public void actualizarVecindad(int CurrentTick) {
        // este método busca entre todos los vecinos aquellos
        // cuales su último mensaje se recibió hace demasiado tiempo
        int tickAntiguedadMaxima = CurrentTick - this.antiguedadMaxima;
        // Los vecinos no se eliminarán directamente del vector vecindad sino que
        // en primer lugar se añadirán a un vector auxiliar y luego se eliminarán
        // haciendo un recorrido por ese vector.  Esta técnica evita
        // condiciones de carrera (borrar nodos en el vector que se está recorriendo.
        Vector<NodoVecino> NodosVecinosParaEliminar = new Vector<NodoVecino>();

        for (NodoVecino vecino : this.NodosVecinos)
            if (vecino.UltimoTick < tickAntiguedadMaxima)
                NodosVecinosParaEliminar.add(vecino);
        // En el bucle siguiente se borran los vecinos marcados en el bucle anterior.
        for (NodoVecino vecino : NodosVecinosParaEliminar)
            this.NodosVecinos.removeElement(vecino);
    }
    public String imprimir() {
        String texto = "";
        for (NodoVecino Vecino : NodosVecinos) {
            if (texto!="") texto= texto + " - ";
            texto = texto + Vecino.id;
        }
        return texto;
    }
    public int cardinalidad() {
        return this.NodosVecinos.size();
    }
}