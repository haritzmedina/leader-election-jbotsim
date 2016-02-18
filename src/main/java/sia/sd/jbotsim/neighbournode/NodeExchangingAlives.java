package sia.sd.jbotsim.neighbournode;

import jbotsim.Node;
import jbotsim.Message;

import java.awt.Color;

public class NodeExchangingAlives extends Node {

    private TickTimer timerSendingALIVEs;                      // Temporizador para el envío periódico de ALIVEs
    private InfoProcesses PI;

    private final Integer id;

    Integer leader = -1;

    public Integer getId() {
        return this.id;
    }

    public NodeExchangingAlives(){
    	this.timerSendingALIVEs = new TickTimer(Main.defaultTimeSendingALIVEs);
        this.setCommunicationRange(400);

        this.id = Main.dameTicket();
        this.PI = new InfoProcesses(this.id);
        this.leader = this.id;
        this.updateState();
    }
    public void onClock(){

        // TASK 1: sending ALIVEs
        if (timerSendingALIVEs.triggered()) {
            EnviarMensajeALIVE();
            timerSendingALIVEs.reset();
        }

        // TASK 2: checking other processes ALIVEs got in time
        boolean anyChange = false;
    	for (InfoProcess infoProcess : this.PI.infoProcesses) {
    		if (infoProcess.timer.isEnabled() && infoProcess.timer.triggered()) {
    			// mark that process as faulty and disble timer
    			infoProcess.timer.disable();
    			anyChange = true;
    		}
    	}
    	if (anyChange) {
    		System.out.println("Nodo " + this.id + " has " + this.PI.getNumberAliveProcesses() + " neighbours " + this.PI.getListAliveProcesses());
    		this.updateState();
    	}
    }

    private void EnviarMensajeALIVE(){
        ContentMessage content = new ContentMessage();
        content.type = ContentMessage.ALIVE;
        this.sendAll(new Message(content));
        System.out.println("T " + this.getTime() + " Nodo " + this.id + " sent an ALIVE message");
    }

    public void onMessage(Message msg) {
    	ContentMessage contenidoMensaje = (ContentMessage) msg.getContent();
        Integer piLowestId = this.PI.getLowestProcessId();
        if(piLowestId==null){
            this.leader = this.id;
        }
        else if(this.id < piLowestId){
            this.leader = this.id;
        }
        else{
            this.leader = piLowestId;
        }
        if (contenidoMensaje.type == ContentMessage.ALIVE) {
            NodeExchangingAlives sender = (NodeExchangingAlives) msg.getSender();
        	this.PI.updateReception(sender.getId());
        }
        this.updateState();
    }

    private void updateState () {
        if(this.leader==this.id){
            this.setColor(Color.cyan);
        }
		else if (this.PI.getNumberAliveProcesses()>=3) {
			this.setColor(Color.green);
		} else {
			this.setColor(Color.red);
		}
    }
}

class ContentMessage {
    static final int ALIVE = 0;     // types of messages
    static final int OTHERTYPE = 1;

    public int type;        // type of message
    // more content
}


