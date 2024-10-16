package com.example.queues_management_application.Model;

import com.example.queues_management_application.GUI.ControllerQueueEvolution;
import com.example.queues_management_application.LogEvents;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private int serverID;
    private BlockingQueue<Task> tasksList;
    private ControllerQueueEvolution controllerQueueEvolution;
    private AtomicInteger waitingTimeServer;
    private LogEvents logEvents;
    private boolean done;


    public Server(int serverID, ControllerQueueEvolution controllerQueueEvolution, LogEvents logEvents) {
        this.serverID = serverID;
        this.tasksList = new LinkedBlockingQueue<>();
        this.controllerQueueEvolution = controllerQueueEvolution;
        this.waitingTimeServer = new AtomicInteger(0);
        this.logEvents = logEvents;
        this.done = true;
    }

    public void addTask(Task newTask) throws InterruptedException {
        this.tasksList.put(newTask);
        int time = newTask.getServiceTime();
        this.setWaitingTimeServer(time);

        controllerQueueEvolution.setWaitingTime(newTask,newTask.getWaitingTimeFinal());
        controllerQueueEvolution.moveFigure(newTask, this.getServerID(), this.getTasksList().size());
    }

    @Override
    public void run() {
        while (done) {

            //from here the code works bed
            Task task = tasksList.peek();
            if (task != null) {
                if (task.getWaitingTime().get() == 0) {
                    controllerQueueEvolution.setWaitingTime(task, 0);
                    controllerQueueEvolution.removeFigure(task);
                    try {
                        tasksList.take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    controllerQueueEvolution.updatePositionsInQueue(this);
                }
            }
            //until here
            for (Task t : this.tasksList) {
                AtomicInteger currentWaitingTime = t.getWaitingTime();

                if (currentWaitingTime.get() > 0){
                   currentWaitingTime.addAndGet(-1);
                    t.setWaitingTime(currentWaitingTime);
                    controllerQueueEvolution.setWaitingTime(t, currentWaitingTime.get());
                }
            }

            if (this.getWaitingTimeServer().get() > 0) {
                this.setWaitingTimeServer(-1);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public BlockingQueue<Task> getTasksList() {
        return tasksList;
    }

    public int getServerID() {
        return serverID;
    }

    public AtomicInteger getWaitingTimeServer() {
        return waitingTimeServer;
    }

    public void setWaitingTimeServer(int timeTask) {
        this.waitingTimeServer.addAndGet(timeTask);
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
