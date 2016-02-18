package sia.sd.jbotsim.neighbours;

/**
 * Created by Haritz Medina on 16/02/2016.
 */
import jbotsim.Node;

import java.awt.*;

public class NodeIni extends Node {

    private int id = -1;

    public String toString(){
        String s = String.valueOf(id);
        return s;
    }

    // At creation node color => white
    public NodeIni(){
        this.id = Main.giveTicket();
    }

    public void onClock() {
        if (this.getNeighbors().size()>3)
            this.setColor(Color.green);
        else
            this.setColor(Color.red);
    }

}