package com.example.queues_management_application.BusinessLogic;

import com.example.queues_management_application.Model.Server;
import com.example.queues_management_application.Model.Task;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueStrategy implements Strategy {

    public  void addTask(BlockingQueue<Server> serverList, Task t) throws InterruptedException {

        //Strategy: at the task to the server/query with the smallest nr of tasks
        //min - min length of the queries
        Server min = serverList.peek();
        for (Server s : serverList) {
            if (s.getTasksList().size() < min.getTasksList().size()) {
                min = s;
            }
        }
        AtomicInteger minWaitingTime = min.getWaitingTimeServer();
        minWaitingTime.addAndGet(t.getServiceTime());

        t.setWaitingTime(minWaitingTime);
        t.setWaitingTimeFinal(minWaitingTime.get());
        min.addTask(t);
    }

}