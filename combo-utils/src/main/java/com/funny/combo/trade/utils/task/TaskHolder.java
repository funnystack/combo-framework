package com.funny.combo.trade.utils.task;
/**
 *
 * @author wensh.zhu
 * @date 2018-04-22
 */
public class TaskHolder {

    /** 任务所需等待的圈数，即任务需要走几圈**/
    private int cycles;
    private int delays;
    private Runnable task;

    public TaskHolder() {}

    public TaskHolder(int cycles, int delays, Runnable task) {
        this.cycles = cycles;
        this.delays = delays;
        this.task = task;
    }

    public boolean isTimeOut() {
        return cycles <= 0;
    }

    public void cutDown() {
        cycles --;
    }

    public int getCycles() {
        return cycles;
    }

    public void setCycles(int cycles) {
        this.cycles = cycles;
    }

    public int getDelays() {
        return delays;
    }

    public void setDelays(int delays) {
        this.delays = delays;
    }

    public Runnable getTask() {
        return task;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "TaskHolder[cycles=" + cycles + ", delays=" + delays + "]";
    }

}
