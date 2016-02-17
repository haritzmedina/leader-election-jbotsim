package sia.sd.jbotsim;

import jbotsim.Message;
import jbotsim.Node;
import jbotsim.event.ClockListener;
import jbotsim.event.MessageListener;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Haritz Medina on 16/02/2016.
 */
public class NodeNeighbours extends Node implements ClockListener, MessageListener {

    HashMap<Integer, Integer> neighbours;

    public NodeNeighbours(){
        this.neighbours = new HashMap<>();
    }

    public void onClock() {
        //
        for(int i=0;i<neighbours.size();i++){
            if(neighbours.containsKey(i)){
                if(neighbours.get(i)<this.getTime()-30){
                    neighbours.remove(i);
                }
            }
        }

        //
        if(neighbours.size()>3){
            super.setColor(Color.green);
        }
        else{
            super.setColor(Color.red);
        }
        Message m = new Message();
        this.sendAll(m);
    }

    public void onMessage(Message msg) {
        Integer id = msg.getSender().getID();
        neighbours.put(id, this.getTime());
    }


}
