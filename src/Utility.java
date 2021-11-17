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

    public static float getResponseRatio(Process checkProcess){
        int waitTime = Utility.getSystemClock() - checkProcess.getArrivalTime();
        int burstTime = checkProcess.getServiceTime();
        return (float) ((waitTime + burstTime) / burstTime);
    }

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
    private static int executionSpeed = 200;
    private static int systemClock = 0;
}
