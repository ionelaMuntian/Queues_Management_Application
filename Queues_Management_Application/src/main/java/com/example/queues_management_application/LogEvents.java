package com.example.queues_management_application;

import com.example.queues_management_application.Model.Server;
import com.example.queues_management_application.Model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LogEvents {
    private static final String LOG_FILE_PATH = "log.txt";
    private static FileWriter fileWriter;
    private float waitingtime=0;

    static {
        try {
            fileWriter = new FileWriter(LOG_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logEventWaiting(List<Task> generatedTasks, int currentTime) throws IOException {
        String logEntry = "\nTime " + currentTime + "\n";

        fileWriter.write(logEntry);
        fileWriter.flush(); // Flush the buffer to ensure data is written immediately

        fileWriter.write("Waiting Clients: ");
        for (Task task : generatedTasks) {
            if (task != null) {
                if (task.getArrivalTime() > currentTime) {
                    fileWriter.write(" (" + task.getID() + " , " + task.getArrivalTime() + " , " + task.getServiceTime() + ")");
                }
            }
        }
        fileWriter.write("\n");
    }

    public void logEventServers(BlockingQueue<Server> serverList, int currentTime) throws IOException {
        for (Server server : serverList) {
            boolean empty = true;
            fileWriter.write("Queue " + server.getServerID() + ": ");
            BlockingQueue<Task> tasksQueue = server.getTasksList();
            for (Task task : tasksQueue) {
                if (task != null && task.getWaitingTime().get() > 0 && task.getArrivalTime() <= currentTime) {
                    empty = false;
                    fileWriter.write(" (" + task.getID() + " , " + task.getArrivalTime() + " , " + task.getServiceTime() + ")");
                }
            }
            if (empty) {
                fileWriter.write("closed");
            }
            fileWriter.write("\n");
        }

    }

    public void avgWaitingTimeInFile(float time) throws IOException {
        fileWriter.write("Average waiting time: " + time);
        fileWriter.write("\n");
        System.out.println("time "+time);
    }

    public float getWaitingtime() {
        return waitingtime;
    }

    public void setWaitingtime(float waitingtime) {
        this.waitingtime = waitingtime;
    }
}