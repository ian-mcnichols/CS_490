/*
Utility is responsible for reading the input file and creating a process list from the contents, stores the waiting
and finished process lists, the system clock and execution speed, and contains functions to access and manipulate them.

Written by Team 6:
Tristan Boler
Laura Estep
Amber Lai Hipp
Ian McNichols

CS 490
Fall 2021
*/

import java.io.*;
import java.util.*;

public final class Utility {
    // Get input file name from config file and load the processes list text file into a java list of Process objects
    public static List<Process> readFile() {
        List<Process> processList = new ArrayList<Process>();
        BufferedReader br = null;
        // Read in config file to find input file name
        BufferedReader reader;
        String processFile = "";
        try {
            reader = new BufferedReader(new FileReader("./config.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                // Grab the last line of the file. Should only be 1 line.
                processFile = line.split("= ")[1];
            }
        } catch (IOException e) {
            // Give info on why it couldn't find the config file for debugging, exit before errors can be propagated
            System.out.println("Current path:" + new File(".").getAbsolutePath());
            e.printStackTrace();
            System.out.println("Config file not found. Exiting.");
            System.exit(1);
        }
        try {
            br = new BufferedReader(new FileReader(processFile));
            String line;
            Process curr_process;
            // Read input file, create Process, and add Process to list
            while ((line = br.readLine()) != null) {
                curr_process = new Process();
                String[] values = line.split(", ");
                curr_process.setArrivalTime(Integer.parseInt(values[0]));
                curr_process.setProcessId(values[1].replaceAll("process ", "").charAt(0));
                curr_process.setServiceTime(Integer.parseInt(values[2]));
                curr_process.setPriority(Integer.parseInt(values[3]));
                processList.add(curr_process);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the file
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return processList;
    }

    public static int getResponseRatio(Process checkProcess){
        int waitTime = Utility.getSystemClock() - checkProcess.getArrivalTime();
        int burstTime = checkProcess.getServiceTime();
        int responseRatio = (waitTime + burstTime) / burstTime;
        return responseRatio;
    }

    public static Process getHRRNProcess() throws Exception {
        Process returnProcess = null;
        int highestRR = -1;
        System.out.println("Waiting processes: " + waitingProcessesHRRN.toString());
        for (Process p : waitingProcessesHRRN){
            System.out.println(p.getProcessId() + " response ratio: " + getResponseRatio(p));
            if (getResponseRatio(p) > highestRR){
                returnProcess = p;
                highestRR = getResponseRatio(p);
            }
        }
        if (returnProcess != null){
            return returnProcess;
        }
        else {
            throw new Exception("Waiting list empty");
        }
    }

    // Add finished Process to the finishedProcesses list
    public static void addFinishedProcessHRRN(Process finished) {
        finishedProcessesHRRN.add(finished);
    }
    public static void addFinishedProcessRR(Process finished) {
        finishedProcessesRR.add(finished);
    }

    // Get the list of finished Processes
    public static List<Process> getFinishedProcessesHRRN() {
        return finishedProcessesHRRN;
    }
    public static List<Process> getFinishedProcessesRR() {
        return finishedProcessesRR;
    }

    // Add a Process that has arrived to the waitingProcesses list
    public static void addWaitingProcessHRRN(Process arrived) {
        waitingProcessesHRRN.add(arrived);
    }
    public static void addWaitingProcessRR(Process arrived) {
        waitingProcessesRR.add(arrived);
    }

    // Get the list of waiting Processes
    public static List<Process> getWaitingProcessListHRRN() {
        return waitingProcessesHRRN;
    }
    public static List<Process> getWaitingProcessListRR() {
        return waitingProcessesRR;
    }

    // Get first waiting Process in waitingProcesses
    public static Process getNextWaitingProcessHRRN() { return waitingProcessesHRRN.get(0); };
    public static Process getNextWaitingProcessRR() { return waitingProcessesRR.get(0); };

    // Remove waiting Process at index 0 of waitingProcesses
    public static void removeWaitingProcessHRRN(Process p) { waitingProcessesHRRN.remove(p); };
    public static void removeWaitingProcessRR(Process p) { waitingProcessesRR.remove(p); };

    // Get current execution speed
    public static int getExecutionSpeed() {
        return executionSpeed;
    }

    // Set execution speed to new value
    public static void setExecutionSpeed(int executionSpeed) {
        Utility.executionSpeed = executionSpeed;
    }

    // Get current value of systemClock
    public static int getSystemClock() {
        return systemClock;
    }

    // Increase the systemClock by 1
    public static void increaseSystemClock() {
        systemClock += 1;
    }

    // Variables
    private static List<Process> finishedProcessesRR = new ArrayList<>();
    private static List<Process> waitingProcessesRR = new ArrayList<>();
    private static List<Process> waitingProcessesHRRN = new ArrayList<>();
    private static List<Process> finishedProcessesHRRN = new ArrayList<>();
    private static int executionSpeed = 500;
    private static int systemClock = 0;
}
