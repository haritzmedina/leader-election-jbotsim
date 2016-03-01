package com.haritzmedina.sia.jbotsim;

/**
 * Created by Haritz Medina on 01/03/2016.
 */
import java.util.ArrayList;;

class InfoProcess {
    int ID;
    TickTimer timer;

    public InfoProcess(int processID) {
        this.ID = processID;
        this.timer = new TickTimer(Main.defaultTimeWaitingALIVEs);
    }
    public boolean isAlive () {
        return(this.timer.isEnabled());
    }

    public void setNonAlive() {
        this.timer.disable();
    }
}

public class InfoProcesses{

    private int whoAmI;
    public ArrayList<InfoProcess> InfoProcesses;

    public InfoProcesses(int processID){
        this.whoAmI = processID;
        this.InfoProcesses = new ArrayList<InfoProcess>();
    }

    public void add(int processID) {
        InfoProcess process = new InfoProcess(processID);
        process.timer.setTimer(Main.defaultTimeWaitingALIVEs);
        this.InfoProcesses.add(process);
    }

    public int getNumberAliveProcesses () {
        int nAlive = 0;
        for (InfoProcess infoProcess : this.InfoProcesses)
            if (infoProcess.isAlive()) nAlive = nAlive + 1;
        return(nAlive);
    }

    public String getListAliveProcesses () {
        String aliveProcesses = "[ ";
        for (InfoProcess infoProcess : this.InfoProcesses)
            if (infoProcess.isAlive()) aliveProcesses = aliveProcesses + infoProcess.ID + " ";
        aliveProcesses = aliveProcesses + "]";
        return(aliveProcesses);
    }

    public int getMinimumID() {
        /**
         * get the minimum process ID among 'alive' processes and the process itself
         * @return an integer (minimum ID)
         */
        int MinimumID = this.whoAmI;
        for (InfoProcess infoProcess : this.InfoProcesses)
            if (infoProcess.isAlive()) MinimumID = Math.min(MinimumID, infoProcess.ID);
        return(MinimumID);
    }

    public boolean isKnown(int processID) {
        for (InfoProcess infoProcess : this.InfoProcesses)
            if (infoProcess.ID == processID) return(true);
        return(false);
    }

    public InfoProcess getInfoProcess (int processID) {
        for (InfoProcess infoProcess : this.InfoProcesses)
            if (infoProcess.ID == processID) return(infoProcess);
        return(null);
    }
}
