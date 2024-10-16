package com.example.queues_management_application.BusinessLogic;

import com.example.queues_management_application.GUI.ControllerQueueEvolution;
import com.example.queues_management_application.LogEvents;
import com.example.queues_management_application.Model.Server;
import com.example.queues_management_application.Model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Scheduler {
    private BlockingQueue<Server> serverList = new LinkedBlockingQueue<>();
    //private ConcurrentLinkedQueue<Server> serverListMinimumWaitingTimes;
    private int maxNoServers;
    private SelectionPolicy strategy;
    private Strategy myStrategy;
    private ControllerQueueEvolution controllerQueueEvolution;
    private LogEvents logEvents = new LogEvents();
    private int maxNrPeople;
    private int peakHour;

    public Scheduler(int maxNoServers, ControllerQueueEvolution controllerQueueEvolution, LogEvents logEvents) {
        this.maxNoServers = maxNoServers;
        this.controllerQueueEvolution = controllerQueueEvolution;
        this.strategy = controllerQueueEvolution.getStrategy();
        this.maxNrPeople = 0;
        this.peakHour = 0;

        for (int i = 0; i < this.maxNoServers; i++) {
            //create N servers
            Server server = new Server(i, controllerQueueEvolution, logEvents);
            this.serverList.add(server);

            //create a thread for each server
            Thread t = new Thread(server);
            t.start();
        }

        //  this.serverListMinimumWaitingTimes.addAll(this.serverList);
    }

    public void changeStrategy(SelectionPolicy policy) {
        strategy = policy;
    }

    public void dispatchTask(Task t) throws InterruptedException {
        if (strategy == SelectionPolicy.SHORTEST_QUEUE) {
            myStrategy = new QueueStrategy();
            myStrategy.addTask(serverList, t);
        } else {
            myStrategy = new TimeStrategy(this);
            myStrategy.addTask(serverList, t);
        }
    }

    public BlockingQueue<Server> getServerList() {
        return serverList;
    }

   /* public ConcurrentLinkedQueue<Server> getServerListMinimumWaitingTimes() {
        return serverListMinimumWaitingTimes;
    }

    public void setServerListMinimumWaitingTimes(ConcurrentLinkedQueue<Server> serverListMinimumWaitingTimes) {
        this.serverListMinimumWaitingTimes = serverListMinimumWaitingTimes;
    }*/

    public synchronized void computePeakHour(int currentTime) {
        int nrPoepleInQueues = 0;
        for (Server s : this.serverList) {
            nrPoepleInQueues = nrPoepleInQueues + s.getTasksList().size();
        }

        if (nrPoepleInQueues > this.maxNrPeople) {
            setMaxNrPeople(nrPoepleInQueues);
            setPeakHour(currentTime);
        }
    }

    public void setPeakHour(int peakHour) {
        this.peakHour = peakHour;
    }

    public void setMaxNrPeople(int maxNrPeople) {
        this.maxNrPeople = maxNrPeople;
    }

    public int getPeakHour() {
        return peakHour;
    }
}
