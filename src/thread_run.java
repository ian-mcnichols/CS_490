/*
thread_run handles the execution of each thread, representing an individual CPU. It will run until the original
process list is empty and there are no waiting processes. If there is a waiting process, it will grab the process
from the list, execute for the service time of the process, checking for pauses every unit of time, and adds the process
to the finished list when completed.

Written by Team 6:
Tristan Boler
Laura Estep
Amber Lai Hipp
Ian McNichols

CS 490
Fall 2021
*/

import java.util.ArrayList;
import java.util.List;

// Generates a thread that opens a process, runs it, and then looks for another until no more processes remain
public abstract class thread_run implements Runnable {
    @Override
    // The core run mechanism.
    public void run() {
        // Track my current thread.
        me = Thread.currentThread();

        // Check for processes that arrive at time 0
        checkForProcessesHRRN();
        GUI.populateProcessTable();
        try {
            // While there is work to be done
            while (!finished()) {
//                System.out.println("Process list: " + processList);
                // If thread has been paused, sleep for one cycle at a time until resumed.
                while (threadPaused){
                    Thread.sleep(Utility.getExecutionSpeed());
                }
                // If waitingProcess list is not empty
                if (!waitingProcessesHRRN.isEmpty()) {
                    // Get a process from waiting list
                    currentProcess = getNextProcess();
                    System.out.println("Starting process " + currentProcess.getProcessId());
                    // Remove process from waiting list
                    System.out.println("Removing process " + currentProcess.getProcessId());
                    waitingProcessesHRRN.remove(currentProcess);
                    // Update GUI process table
                    GUI.populateProcessTable();
                    // Update CPU text field
                    currentServiceTime = currentProcess.getServiceTime();
                    GUI.updateCpuTextField(currentProcess, currentServiceTime, thread_no);

                    // Reset working counter
                    workCounter = 0;
                    // Run process for necessary amount of time, checking for pauses after each unit of time
                    // While the process still requires more work
                    while (workCounter < currentProcess.getServiceTime()) {
                        // If thread has been paused, sleep for one cycle at a time until resumed.
                        while (threadPaused){
                            Thread.sleep(Utility.getExecutionSpeed());
                        }
                        // Get current time
                        processStartTime = System.currentTimeMillis();
                        // Update CPU text field
                        GUI.updateCpuTextField(currentProcess, currentServiceTime - workCounter, thread_no);

                        // Increase unit of work done
                        workCounter +=1;
                        // If the loop took less than the time set for one cycle
                        processElapsedTime = System.currentTimeMillis() - processStartTime;
                        if (processElapsedTime < Utility.getExecutionSpeed()) {
                            // Sleep for any remaining time
                            Thread.sleep(Utility.getExecutionSpeed() - processElapsedTime);
                        }
                        // Check for newly entered process
                        checkForProcessesHRRN();
                    }

                    // Add process to finished list
                    currentProcess.setFinish_time(Utility.getSystemClock());
                    currentProcess.setTurnaround_time();
                    currentProcess.setNorm_turnaround_time();
                    finishedProcessesHRRN.add(currentProcess);
                    // Update GUI report table
                    GUI.updateReportTable();
                    // Update HRRN avg nTAT
                    GUI.setHRRNLabel();
                    step();
                }
                // If there are no waiting processes, sleep for a cycle
                else {
                    Thread.sleep(Utility.getExecutionSpeed());
                }
                // If all processes have finished running, shut down the thread
                if (processList.isEmpty() && waitingProcessesHRRN.isEmpty()) {
                    GUI.updateCpuTextField(null, -1, thread_no);
                    cancel();
                }
            }
        } catch (
                Throwable ex) {
            // Just fall out when exception is thrown.
            ex.printStackTrace();
        }
    }


    // Check if the work is finished
    public boolean finished() {
        return cancelled || me.isInterrupted();
    }

    // Pause the thread
    public void pause() {
        threadPaused = true;
    }

    // Resume the thread
    public void resume() {
        threadPaused = false;
    }

    // Stop
    public void cancel() {
        // Stop everything.
        cancelled = true;
    }

    // Start the thread
    public void start() {
        // Wrap me in a thread and start
        new Thread(this).start();
    }

    // Get the exception that was thrown to stop the thread or null if the thread was cancelled.
    public Exception getThrown() {
        return thrown;
    }

    // Expose my Thread.
    public Thread getThread() {
        return me;
    }


    // Any thrown exception stops the whole process.
    public abstract void step() throws Exception;

    // Factory to wrap a Stepper in a Pauseable Thread
    public static thread_run make(Stepper stepper,
                                  SchedulerGUI GUI,
                                  int thread_no) {
        // That's the thread they can pause/resume.
        // Get process list from file
        processList = Utility.readFile();
        return new StepperThread(stepper, GUI, thread_no);

    }

    // One of these must be used.
    public interface Stepper {
        // A Stepper has a step method.
        // Any exception thrown causes the enclosing thread to stop.
        void step() throws Exception;
    }

    // Holder for a Stepper.
    private static class StepperThread extends thread_run {
        private final Stepper stepper;

        StepperThread(Stepper stepper,
                      SchedulerGUI GUI, int thread_no) {
            this.stepper = stepper;
            this.GUI = GUI;
            this.thread_no = thread_no;
        }

        @Override
        public void step() throws Exception {
            stepper.step();
        }
    }

    public float getResponseRatio(Process checkProcess){
        int waitTime = Utility.getSystemClock() - checkProcess.getArrivalTime();
        int burstTime = checkProcess.getServiceTime();
        return (float) ((float) (waitTime + burstTime) / (float) burstTime);
    }

    public Process getNextProcess() throws Exception {
        Process returnProcess = null;
        float highestRR = -1;
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

    public void checkForProcessesHRRN() {
        for (int i = 0; i < processList.size(); i++) {
            Process tempProcess = processList.get(i);
            // If a process has arrived
            if (tempProcess.getArrivalTime() <= Utility.getSystemClock()) {
                System.out.println("Arrival time: " + tempProcess.getArrivalTime());
                System.out.println("Current time: " + Utility.getSystemClock());
                // Add the process to the waiting list
                waitingProcessesHRRN.add(tempProcess);
                processList.remove(tempProcess);
            }
        }
    }

    public List<Process> getWaitingProcessListHRRN() {
        return waitingProcessesHRRN;
    }

    public List<Process> getFinishedProcessesHRRN() {
        return finishedProcessesHRRN;
    }

    // Variables
    private static List<Process> processList;
    private List<Process> waitingProcessesHRRN = new ArrayList<>();
    private List<Process> finishedProcessesHRRN = new ArrayList<>();
    // Flag to cancel the whole process.
    private volatile boolean cancelled = false;
    // The exception that cause it to finish.
    private Exception thrown = null;
    // The thread that is me.
    private Thread me = null;
    protected SchedulerGUI GUI;
    protected int thread_no;
    private Process currentProcess = null;
    private int workCounter = 0;
    private int tempFinishTime = 0;
    private int currentServiceTime = 0;
    private long processStartTime = 0;
    private long processElapsedTime = 0;
    private  boolean threadPaused = false;
}
