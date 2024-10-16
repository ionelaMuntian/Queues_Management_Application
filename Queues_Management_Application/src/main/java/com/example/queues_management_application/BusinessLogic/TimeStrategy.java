package com.example.queues_management_application.BusinessLogic;

import com.example.queues_management_application.Model.Server;
import com.example.queues_management_application.Model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeStrategy implements Strategy {
    Scheduler scheduler;
    public TimeStrategy(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void addTask(BlockingQueue<Server> serverList, Task t) throws InterruptedException {

        //find waiting time of the 1st server
        Server minServer = serverList.peek();
        int minWaitingTime = minServer.getWaitingTimeServer().get() + t.getServiceTime();

        for (Server s : serverList) {
            //each server has more tasks, so find the total waiting time/server
            int waitingTime = s.getWaitingTimeServer().get() + t.getServiceTime();
            if (waitingTime < minWaitingTime) {
                minWaitingTime = waitingTime;
                minServer = s;
            }
        }

        AtomicInteger i = new AtomicInteger(minWaitingTime);
        t.setWaitingTime(i);
        t.setWaitingTimeFinal(minWaitingTime);
        minServer.addTask(t);

    }

}
