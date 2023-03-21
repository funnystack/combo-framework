package com.funny.combo.trade.utils.task;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author wensh.zhu
 * @date 2018-04-22
 */
public class TimerContext {

    public static final int DEFAULT_TICKS = 60;
    public static final int DEFAULT_TICK_DURATION = 1;

    private Map<Integer, Queue<TaskHolder>> taskHolders;
    private volatile int currentTick = 0;

    /** tick一圈的长度 **/
    private int ticks = DEFAULT_TICKS;

    /** 每tick一次的时间间隔，单位：秒**/
    private int tickDuration = DEFAULT_TICK_DURATION;

    public TimerContext() {
        init();
    }

    public TimerContext(int ticks, int tickDuration) {
        if (ticks <= 0)
            throw new IllegalArgumentException("ticks must be greater than 0");

        if (tickDuration <= 0)
            throw new IllegalArgumentException("tickDuration must be greater than 0");

        this.ticks = ticks;
        this.tickDuration = tickDuration;
        init();
    }

    private void init() {
        taskHolders = new ConcurrentHashMap<Integer, Queue<TaskHolder>>();
        for (int i = 0; i < ticks; i ++)
            taskHolders.put(i, new ConcurrentLinkedQueue<TaskHolder>());
    }

    /**
     * 添加一个定时任务并计算需要走的圈数和落脚的index
     * @param task
     * @param delays
     */
    public void addTask(Runnable task, int delays) {
        if (task == null)
            throw new NullPointerException("task must not be null");

        if (delays <=0)
            throw new IllegalArgumentException("delays must be greater than 0");

        int allSeconds = ticks * tickDuration;
        int cycles = delays / allSeconds;
        int index = ((delays % allSeconds) / tickDuration) + currentTick;
        TaskHolder metaData = new TaskHolder(cycles, delays, task);
        Queue<TaskHolder> tasks = taskHolders.get(index);
        synchronized (tasks) {
            tasks.add(metaData);
        }
    }

    public int tick() {
        currentTick = (currentTick + 1) % ticks;
        return currentTick;
    }

    public Queue<TaskHolder> getCurrentTasks() {
        return taskHolders.get(currentTick);
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public int getTicks() {
        return ticks;
    }

    public int getTickDuration() {
        return tickDuration;
    }

    @Override
    public String toString() {
        return "TimerContext [timers=" + taskHolders + ", ticks=" + ticks + ", tickDuration=" + tickDuration
                + ", currentTick=" + currentTick + "]";
    }
}
