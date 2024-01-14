package threads;

public abstract class AbilityThread {
    
    protected boolean isRunning;
    protected long duration, prevDuration, startTime;

    public void resetDuration() {
        duration = 0;
        prevDuration = 0;
        isRunning = true;
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
        isRunning = true;
    }

    public void pauseTimer() {
        setDuration();
        prevDuration = duration;
        isRunning = false;
    }

    public void stopTimer() {
        setDuration();
        isRunning = false;
    }

    public void setDuration () {
        if (isRunning) {
            duration = System.currentTimeMillis() - startTime + prevDuration;
        }
    }

    public void resetCooldown () {
        this.resetDuration();
        this.startTimer();
    }

    public abstract void cooldownUpdate();
}
