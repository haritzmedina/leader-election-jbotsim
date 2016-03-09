package jbotsim;

/**
 * Created by Haritz Medina on 01/03/2016.
 */
public class TickTimer {
    private int bound = 0;
    private int triggeringTime = 0;
    private boolean enabled = false;

    public TickTimer(int tickInitialBound) {
        this.setBound(tickInitialBound);
        this.reset();
    }
    public boolean triggered() {
        return (this.triggeringTime <= Main.topologia.getTime());
    }
    public boolean isEnabled() {
        return (this.enabled);
    }
    public void reset() {
        this.triggeringTime = Main.topologia.getTime() + this.bound;
        this.enabled = true;
    }
    public void setBound(int ticks) {
        this.bound = ticks;
    }
    public void incrBound() {
        this.bound++;
        Main.writeLog("Incrementing bound:" + this.bound);
    }
    public void setTimer(int tickBound) {
        this.setBound(tickBound);
        this.reset();
    }
    public void disable() {
        this.enabled = false;
    }
}