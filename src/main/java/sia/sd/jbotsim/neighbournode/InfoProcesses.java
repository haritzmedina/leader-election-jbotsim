package sia.sd.jbotsim.neighbournode;

import java.util.ArrayList;;

class InfoProcess {
	int ID;
	TickTimer timer;
	
    public boolean isAlive () {
        return(this.timer.isEnabled());
    }

}

public class InfoProcesses{

	private int whoAmI;
	public ArrayList<InfoProcess> infoProcesses;

    public InfoProcesses(int processID){
    	this.whoAmI = processID;
    	this.infoProcesses = new ArrayList<InfoProcess>();
    }

    public void add(int processID,int tickUltimoContacto) {
    	InfoProcess process = new InfoProcess();
    	process.ID = processID;
    	process.timer = new TickTimer(Main.defaultTimeWaitingALIVEs);
    	this.infoProcesses.add(process);
    	System.out.println("Nodo " + this.whoAmI + " added a new neighbor: " + processID);
    }

    public int getNumberAliveProcesses () {
    	int nAlive = 0;
		for (InfoProcess infoProcess : this.infoProcesses) {
			if (infoProcess.isAlive()) nAlive = nAlive + 1;
		}
    	return(nAlive);
    }

    public String getListAliveProcesses () {
    	String aliveProcesses = "[ ";
		for (InfoProcess infoProcess : this.infoProcesses) {
			if (infoProcess.isAlive()) {
				aliveProcesses = aliveProcesses + infoProcess.ID + " ";
			}
		}
		aliveProcesses = aliveProcesses + "]";
    	return(aliveProcesses);
    }

    public void updateReception (int processID) {
    	boolean already = false;
		for (InfoProcess infoProcess : this.infoProcesses) {
			if (infoProcess.ID == processID) {
				already = true;
				infoProcess.timer.reset();
				break;
			}
		}
		if (!already) {
			this.add(processID, 0);
		}
    }

	public Integer getLowestProcessId(){
		Integer lowestId = null;
		for(InfoProcess infoProcess : this.infoProcesses){
            if(infoProcess.isAlive()){
                if(lowestId == null){
                    lowestId = infoProcess.ID;
                }
                else if(lowestId > infoProcess.ID){
                    lowestId = infoProcess.ID;
                }
            }
		}
		return lowestId;
	}
}

