/*
Main driver for the program. Sets up process list from input file, GUI, and semaphore.
Contains a loop that runs while program and determines when to increase the clock,
when a process has arrived and adds it to the waiting list, and updates the GUI.

Written by Team 6:
Tristan Boler
Laura Estep
Amber Lai Hipp
Ian McNichols

CS 490
Fall 2021
*/

import java.util.*;
import java.util.concurrent.Semaphore;

public class main extends Thread {
    public static void main(String[] args) throws InterruptedException {
        // Create a main thread to help with execution timing
        Thread mainThread = Thread.currentThread();

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SchedulerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SchedulerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SchedulerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SchedulerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Create GUI
        SchedulerGUI gui = new SchedulerGUI();
        // List to store processes removed from input list when placed in waiting list

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                gui.setVisible(true);
            }
        });

        // Variables to determine timing of "clock cycle"
        long startTime = 0;
        long elapsedTime = 0;

        // While the program is running, loop every "clock cycle" and do certain actions
        while(gui.checkProgramRunning()) {
            mainThread.sleep(Utility.getExecutionSpeed());
            // If system is running
            if (gui.checkSystemRunning()) {
                // Increase "clock"
                Utility.increaseSystemClock();
                }
            // Calculate and display current throughput
            gui.setThroughputLabel();
        }
    }
}
