package com.example.queues_management_application.BusinessLogic;

import com.example.queues_management_application.Model.Server;
import com.example.queues_management_application.Model.Task;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface Strategy {
    public void addTask(BlockingQueue<Server> serverList, Task t) throws InterruptedException;
}