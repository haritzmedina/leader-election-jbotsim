package com.haritzmedina.sia.jbotsim;

import jbotsim.Message;
import jbotsim.Node;

import java.awt.*;

/**
 * Created by Haritz Medina on 01/03/2016.
 */

public class NodeLeaderWithCommDelays extends Node {

    int ID = -1;
    int leader = -1;

    private TickTimer timerSendingALIVEs;
    private InfoProcesses PI;

    public NodeLeaderWithCommDelays(){
        this.ID = Main.getTicket();                // get an identifier

        // activate timer for sending ALIVEs with boundStep of 1
        this.timerSendingALIVEs = new TickTimer(Main.defaultTimeSendingALIVEs, Main.defaultBoundStep);
        this.PI = new InfoProcesses(this.ID);    // initialize PI (set of processes)
        this.setCommunicationRange(400);        // wide comm range
        this.updateState();                        // update global variables + visual state
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
                this.updateState();
            }
        }

        this.leader = this.PI.getMinimumID();
        setColor(this.ID == this.leader? Color.blue:Color.green);
        this.setState("Nodo " + this.ID + " Leader:" + this.leader);

    }

    private void EnviarMensajeALIVE(){
        if(this.ID == this.leader){
            ContentMessage content = new ContentMessage();
            content.type = ContentMessage.ALIVE;
            content.processSender = this.ID;
            this.sendAll(new Message(content));
            Main.writeLog("N " + this.ID + " sent an ALIVE message");
        }
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
                    infoProcess.timer.incrBound();
                } else {
                    infoProcess.timer.reset();
                }
            } else { // processSender is still unknown
                this.PI.add(content.processSender); // add the new process (and internally resets the timer)
                Main.writeLog("N " + this.ID + " found a new node: " + content.processSender);
            }
        }
        this.updateState();
    }

    private void updateState () {
        this.setState("N " + this.ID + " Trusts:" + this.PI.getListAliveProcesses());
    }

    private class ContentMessage {
        static final int ALIVE = 0;     // types of messages
        //static final int OTHERTYPE = 1;

        public int type;        // type of message
        public int processSender;
        // more content
    }
}
