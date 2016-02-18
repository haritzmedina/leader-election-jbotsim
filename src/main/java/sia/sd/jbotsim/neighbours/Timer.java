package sia.sd.jbotsim.neighbours;

// Class to implement a timer which allows to measure a given time.
// Functions:
// - Timer(n): create a timer to measure n ticks
// - incr(): a function to increment the number of ticks
// - triggered(): true if the number of ticks gets the bound
//          (initially n, but it might have been changed by incr() or reset() methods)
// - incBound(): increase the bound of the timer
public class Timer {
    private int bound = 0;
    private int count = 0;
    private boolean enabled = false;

    public Timer(int ticks) {
        this.bound = ticks;
        this.count = 0;
        this.enabled = true;
    }
    public void incr() {
        // Se asume que se llama a este método cada tick de reloj
        if (this.enabled) this.count++;
    }
    public boolean triggered() {
        return (this.count >= this.bound);
    }
    public void reset() {
        this.count = 0;
        this.enabled = true;
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