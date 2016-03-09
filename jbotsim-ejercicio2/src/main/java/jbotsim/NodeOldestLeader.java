package jbotsim;

import java.awt.*;

/**
 * Created by Haritz Medina on 01/03/2016.
 */

public class NodeOldestLeader extends Node {

    int ID = -1;
    int leader = -1;

    int joinPosition = 0;

    private TickTimer timerSendingALIVEs;
    private InfoProcesses PI;

    public NodeOldestLeader(){
        this.ID = Main.getTicket();                // get an identifier

        this.timerSendingALIVEs = new TickTimer(Main.DEFAULT_TIME_SENDING_ALIVE); // activate timer for sending ALIVEs
        this.PI = new InfoProcesses(this.ID);    // initialize PI (set of processes)
        this.setCommunicationRange(400);        // wide comm range
        this.updateState();                        // update global variables + visual state
    }

    public void onStart(){
        // Set itself as leader
        this.leader = this.ID;
        // Broadcast join
        this.EnviarMensajeJOINREQUEST();

    }

    public void onClock(){
        // TASK 1: sending ALIVEs
        if (timerSendingALIVEs.triggered()) {
            EnviarMensajeALIVE();
            timerSendingALIVEs.reset();
        }
        // TASK 2: checking other processes ALIVEs got in time
        for (InfoProcess infoProcess : this.PI.InfoProcesses) {
            if (infoProcess.isAlive() && infoProcess.timer.triggered()) {
                // mark that process as faulty and disable timer
                infoProcess.setNonAlive();
                Main.writeLog("N " + this.ID + " suspects node " + infoProcess.ID);
                this.leader = this.ID;
                this.updateState();
            }
        }

        //this.leader = this.PI.getMinimumID();
        setColor(this.ID == this.leader? Color.blue:Color.green);
        this.setState("Nodo " + this.ID + " Leader:" + this.leader);

    }

    private void EnviarMensajeALIVE(){
        if(this.ID == this.leader){
            ContentMessage content = new ContentMessage();
            content.type = ContentMessage.ALIVE;
            content.processSender = this.ID;
            content.joinPosition = this.joinPosition;
            this.sendAll(new Message(content));
            Main.writeLog("N " + this.ID + " sent an ALIVE message");
        }
    }

    private void EnviarMensajeLEADER(Node destination){
        ContentMessage content = new ContentMessage();
        content.type = ContentMessage.LEADER;
        content.joinPosition = this.joinPosition;
        this.send(destination, new Message(content));
        Main.writeLog("N " + this.ID + " sent an LEADER message");
    }

    private void EnviarMensajeJOINREQUEST(){
        ContentMessage content = new ContentMessage();
        content.type = ContentMessage.JOIN_REQUEST;
        content.processSender = this.ID;
        this.sendAll(new Message(content));
        Main.writeLog("N "+ this.ID + " sent an JOINREQUEST message.");
    }

    private void EnviarMensajeJOINRESPONSE(Node destination){
        ContentMessage content = new ContentMessage();
        content.type = ContentMessage.JOIN_RESPONSE;
        content.processSender = this.ID;
        content.joinPosition = this.joinPosition;
        this.send(destination, new Message(content));
        Main.writeLog("N " + this.ID + " sent an JOINRESPONSE message with position " + this.joinPosition);
    }

    public void onMessage(Message msg) {
        ContentMessage content = (ContentMessage) msg.getContent();
        if (content.type == ContentMessage.ALIVE) {
            if (this.PI.isKnown(content.processSender)) {
                InfoProcess infoProcess = this.PI.getInfoProcess(content.processSender);
                if (!infoProcess.isAlive()) {
                    // a message has been received from a suspected process => correct false suspicion
                    Main.writeLog("N " + this.ID + " received again from " + content.processSender + " " + infoProcess.ID);
                    infoProcess.timer.reset();
                } else {
                    infoProcess.timer.reset();
                }
            } else { // processSender is still unknown
                this.PI.add(content.processSender); // add the new process (and internally resets the timer)
                Main.writeLog("N " + this.ID + " found a new node: " + content.processSender);
            }
            if(this.joinPosition>content.joinPosition){
                this.leader = content.processSender;
            }
        }
        // Answer to a join request from a new node
        else if(content.type == ContentMessage.JOIN_REQUEST){
            // If it is leader, it send leader message, in other case response message
            if(this.ID == this.leader){
                this.EnviarMensajeLEADER(msg.getSender());
            }
            else{
                this.EnviarMensajeJOINRESPONSE(msg.getSender());
            }

        }
        // Process the answer from a join request made by you
        else if(content.type == ContentMessage.JOIN_RESPONSE){
            if(this.joinPosition<=content.joinPosition){
                this.joinPosition = content.joinPosition+1;
            }
        }
        // Process the leader anser from a join request made by you
        else if(content.type == ContentMessage.LEADER){
            this.leader = content.processSender;
            if(this.joinPosition<=content.joinPosition){
                this.joinPosition = content.joinPosition+1;
            }
        }
        this.updateState();
    }

    private void updateState () {
        this.setState("N " + this.ID + " Trusts:" + this.PI.getListAliveProcesses());
    }

    private class ContentMessage {
        static final int ALIVE = 0;     // types of messages
        static final int JOIN_REQUEST = 1;
        static final int JOIN_RESPONSE = 2;
        static final int LEADER = 3;

        public int type;        // type of message
        public int processSender;
        // more content
        public int joinPosition;
    }
}
