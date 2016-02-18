package sia.sd.jbotsim.neighbournode;

// Class to implement a timer which allows to measure a given time.
// Functions:
// - TickTimer(n): create a timer to measure n ticks
// - triggered(): true if the number of ticks gets the bound
// 		(initially n, but it might have been changed by incr() or reset() methods)
// - incBound(): increase the bound of the timer
public class TickTimer {
    private int bound = 0;
	private int triggeringTime = 0;
    private boolean enabled = false;

    public TickTimer(int tickInitialBound) {
        this.bound = tickInitialBound;
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
    public void incBound() {
        this.bound++;
    }
    public void enable() {
    	this.enabled = true;
    }
    public void disable() {
    	this.enabled = false;
    }
}
