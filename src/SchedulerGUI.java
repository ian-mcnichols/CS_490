
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.table.DefaultTableModel;

public class SchedulerGUI extends javax.swing.JFrame {

    /**
     * Creates new form SchedulerGUI
     */
    public SchedulerGUI(List<Process> testList, Semaphore semaphore) {
        initComponents();
        // Copy process list
        this.processList = processList;
        // Setup process tables
        hrrnProcessQueueModel = (DefaultTableModel) hrrnProcessQueueTable.getModel();
        rrProcessQueueModel = (DefaultTableModel) rrProcessQueueTable.getModel();
        hrrnReportTableModel = (DefaultTableModel) hrrnReportTable.getModel();
        rrReportTableModel = (DefaultTableModel) rrReportTable.getModel();

        // Display CPU status
        updateCpuTextField(null, -1, 1);
        updateCpuTextField(null, -1, 2);

        // Copy semaphore
        this.semaphore = this.semaphore;

        // Create CPU threads
        cpu1_thread = thread_run.make(s1, this.semaphore, processList, this, 1);
        cpu2_thread = thread_run.make(s2, this.semaphore, processList, this, 2);
    }

    private void initComponents() {

        startButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        systemStatusLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        hrrnProcessQueueTable = new javax.swing.JTable();
        hrrnProcessQueueLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        cpu1TextArea = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        cpu2TextArea = new javax.swing.JTextArea();
        timeUnitTextField = new javax.swing.JTextField();
        timeUnitLabel = new javax.swing.JLabel();
        timeUnitLabel2 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        hrrnReportTable = new javax.swing.JTable();
        hrrn_nTAT_label = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        rrReportTable = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        rrProcessQueueTable = new javax.swing.JTable();
        rr_nTAT_label = new javax.swing.JLabel();
        rrTimeSliceLabel = new javax.swing.JLabel();
        rrTimeSliceTextArea = new javax.swing.JTextField();
        rrProcessQueueLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                systemRunningFlag = false;
            }
        });

        startButton.setText("Start System");
        startButton.setSize(new java.awt.Dimension(80, 25));
        startButton.addActionListener(this::startButtonActionPerformed);

        pauseButton.setText("Pause/Resume System");
        pauseButton.setSize(new java.awt.Dimension(80, 25));
        pauseButton.addActionListener(this::pauseButtonActionPerformed);
        pauseButton.setEnabled(false);

        systemStatusLabel.setText("System Status...");
        systemStatusLabel.setPreferredSize(new java.awt.Dimension(150, 25));
        systemStatusLabel.setSize(new java.awt.Dimension(150, 25));

        hrrnProcessQueueTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "Process Name", "Service Time"
                }
        ) {
            Class[] types = new Class [] {
                    java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        hrrnProcessQueueTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane2.setViewportView(hrrnProcessQueueTable);
        if (hrrnProcessQueueTable.getColumnModel().getColumnCount() > 0) {
            hrrnProcessQueueTable.getColumnModel().getColumn(0).setResizable(false);
            hrrnProcessQueueTable.getColumnModel().getColumn(1).setResizable(false);
        }

        hrrnProcessQueueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hrrnProcessQueueLabel.setText("Waiting Process Queue");
        hrrnProcessQueueLabel.setSize(new java.awt.Dimension(40, 20));

        cpu1TextArea.setEditable(false);
        cpu1TextArea.setColumns(20);
        cpu1TextArea.setLineWrap(true);
        cpu1TextArea.setRows(5);
        cpu1TextArea.setMaximumSize(new java.awt.Dimension(230, 90));
        cpu1TextArea.setPreferredSize(new java.awt.Dimension(230, 90));
        cpu1TextArea.setSize(new java.awt.Dimension(230, 90));
        jScrollPane3.setViewportView(cpu1TextArea);

        cpu2TextArea.setEditable(false);
        cpu2TextArea.setColumns(20);
        cpu2TextArea.setLineWrap(true);
        cpu2TextArea.setRows(5);
        cpu2TextArea.setPreferredSize(new java.awt.Dimension(230, 90));
        cpu2TextArea.setRequestFocusEnabled(false);
        cpu2TextArea.setSize(new java.awt.Dimension(230, 90));
        jScrollPane4.setViewportView(cpu2TextArea);

        timeUnitTextField.setText(String.valueOf(Utility.getExecutionSpeed()));
        timeUnitTextField.setSize(new java.awt.Dimension(50, 20));
        timeUnitTextField.addActionListener(this::timeUnitTextFieldActionPerformed);

        timeUnitLabel.setText("1 time unit =");
        timeUnitLabel.setPreferredSize(new java.awt.Dimension(80, 20));
        timeUnitLabel.setSize(new java.awt.Dimension(80, 20));

        timeUnitLabel2.setText("ms");
        timeUnitLabel2.setSize(new java.awt.Dimension(25, 20));

        hrrnReportTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "Process Name", "Arrival Time", "Service Time", "Finish Time", "TAT", "nTAT"
                }
        ) {
            final Class[] types = new Class [] {
                    java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        hrrnReportTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(hrrnReportTable);
        if (hrrnReportTable.getColumnModel().getColumnCount() > 0) {
            hrrnReportTable.getColumnModel().getColumn(0).setResizable(false);
            hrrnReportTable.getColumnModel().getColumn(1).setResizable(false);
            hrrnReportTable.getColumnModel().getColumn(2).setResizable(false);
            hrrnReportTable.getColumnModel().getColumn(3).setResizable(false);
            hrrnReportTable.getColumnModel().getColumn(4).setResizable(false);
            hrrnReportTable.getColumnModel().getColumn(5).setResizable(false);
        }

        // Set preferred width for report table columns
        hrrnReportTable.getColumnModel().getColumn(0).setPreferredWidth(90);
        hrrnReportTable.getColumnModel().getColumn(1).setPreferredWidth(85);
        hrrnReportTable.getColumnModel().getColumn(2).setPreferredWidth(85);
        hrrnReportTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        hrrnReportTable.getColumnModel().getColumn(4).setPreferredWidth(40);
        hrrnReportTable.getColumnModel().getColumn(5).setPreferredWidth(40);
        hrrnReportTable.setShowVerticalLines(true);
        hrrnReportTable.setShowHorizontalLines(true);

        hrrn_nTAT_label.setText("Current average nTAT:");

        rrReportTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "Process Name", "Arrival Time", "Service Time", "Finish Time", "TAT", "nTAT"
                }
        ) {
            final Class[] types = new Class [] {
                    java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        rrReportTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(rrReportTable);
        if (rrReportTable.getColumnModel().getColumnCount() > 0) {
            rrReportTable.getColumnModel().getColumn(0).setResizable(false);
            rrReportTable.getColumnModel().getColumn(1).setResizable(false);
            rrReportTable.getColumnModel().getColumn(2).setResizable(false);
            rrReportTable.getColumnModel().getColumn(3).setResizable(false);
            rrReportTable.getColumnModel().getColumn(4).setResizable(false);
            rrReportTable.getColumnModel().getColumn(5).setResizable(false);
        }

        // Set preferred width for report table columns
        rrReportTable.getColumnModel().getColumn(0).setPreferredWidth(90);
        rrReportTable.getColumnModel().getColumn(1).setPreferredWidth(85);
        rrReportTable.getColumnModel().getColumn(2).setPreferredWidth(85);
        rrReportTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        rrReportTable.getColumnModel().getColumn(4).setPreferredWidth(40);
        rrReportTable.getColumnModel().getColumn(5).setPreferredWidth(40);
        rrReportTable.setShowVerticalLines(true);
        rrReportTable.setShowHorizontalLines(true);

        rr_nTAT_label.setText("Current average nTAT:");

        rrProcessQueueTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "Process Name", "Service Time"
                }
        ) {
            Class[] types = new Class [] {
                    java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        rrProcessQueueTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane7.setViewportView(rrProcessQueueTable);
        if (rrProcessQueueTable.getColumnModel().getColumnCount() > 0) {
            rrProcessQueueTable.getColumnModel().getColumn(0).setResizable(false);
            rrProcessQueueTable.getColumnModel().getColumn(1).setResizable(false);
        }

        rrTimeSliceLabel.setText("Round Robin Time Slice Length:");

        rrTimeSliceTextArea.setText("100");
        rrTimeSliceTextArea.setSize(new java.awt.Dimension(50, 20));
        rrTimeSliceTextArea.addActionListener(this::rrTimeSliceTextAreaActionPerformed);

        rrProcessQueueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rrProcessQueueLabel.setText("Waiting Process Queue");
        rrProcessQueueLabel.setSize(new java.awt.Dimension(40, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(360, Short.MAX_VALUE)
                                .addComponent(startButton)
                                .addGap(40, 40, 40)
                                .addComponent(pauseButton)
                                .addGap(353, 353, 353))
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(40, 40, 40)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(hrrn_nTAT_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                        .addComponent(hrrnProcessQueueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(40, 40, 40)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(rr_nTAT_label, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(rrTimeSliceTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(rrProcessQueueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGap(18, 18, 18)
                                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(rrTimeSliceLabel)))
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addComponent(timeUnitLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(timeUnitTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(timeUnitLabel2)))
                                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(431, 431, 431)
                                                .addComponent(systemStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(startButton)
                                        .addComponent(pauseButton)
                                        .addComponent(timeUnitTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(timeUnitLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(timeUnitLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(systemStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(hrrnProcessQueueLabel)
                                        .addComponent(rrProcessQueueLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(31, 31, 31)
                                                                .addComponent(rrTimeSliceLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(rrTimeSliceTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(50, 50, 50)
                                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(hrrn_nTAT_label)
                                        .addComponent(rr_nTAT_label))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rrTimeSliceTextAreaActionPerformed(ActionEvent actionEvent) {
    }

    // When start button is clicked
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // Start the threads
            cpu1_thread.start();
            cpu2_thread.start();
            // Update system status label
            systemStatusLabel.setText("System Running");
            // Update system running flag
            systemRunningFlag = true;
            // Disable the start button
            startButton.setEnabled(false);
            // Enable the pause button
            pauseButton.setEnabled(true);
        } catch (IllegalMonitorStateException e) {
            System.out.println("Thread already started...");
        }
    }

    // When a new execution speed is entered
    private void timeUnitTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        // Save new execution speed
        Utility.setExecutionSpeed(Integer.parseInt(timeUnitTextField.getText()));
    }

    // When pause/resume button is clicked
    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // If system is not running
        if (!systemRunningFlag) {
            try {
                // Resume the threads
                cpu1_thread.resume();
                cpu2_thread.resume();
                // Update system running flag
                systemRunningFlag = true;
                // Update system status label
                updateSystemStatusLabel("System Running");
            } catch (IllegalMonitorStateException e) {
                System.out.println("There are no threads running, cannot resume.");
            }
        }
        // Else, system is running
        else {
            // Pause the threads
            cpu1_thread.pause();
            cpu2_thread.pause();
            // Update system running flag
            systemRunningFlag = false;
            // Update system status label
            updateSystemStatusLabel("System Paused");
            // Update CPU text fields
            updateCpuTextField(null, -1, 1);
            updateCpuTextField(null, -1, 2);
        }
    }

    // Refresh the waiting process table
    public void populateProcessTable() {
//        Object[] rowData = new Object[2];
//        // Clear the table
//        for (int rowCount = (processQueueModel.getRowCount() - 1); rowCount >= 0; rowCount--) {
//            processQueueModel.removeRow(rowCount);
//            processQueueTable.revalidate();
//        }
//        // Place new data in table
//        for (Process process : Utility.getWaitingProcessList()) {
//            rowData[0] = process.getProcessId();
//            rowData[1] = process.getServiceTime();
//            processQueueModel.addRow(rowData);
//        }
    }

    // Update the CPU text fields with current processing information
    public void updateCpuTextField(Process currentProcess, int timeRemaining, int threadNumber) {
        // If updating for thread 1
        if (threadNumber == 1) {
            cpu1TextArea.setText("\nCPU 1\n");

            // If system is not running, display process being executed and time remaining
            if (!systemRunningFlag || currentProcess == null) {
                cpu1TextArea.append("Executing: Idle\n");
                cpu1TextArea.append("Time Remaining = n/a");
            }
            // Else, system is running, update with current process and time remaining
            else {
                cpu1TextArea.append("Executing: Process " + currentProcess.getProcessId() + "\n");
                cpu1TextArea.append("Time Remaining = " + timeRemaining);
            }
        }
        // If updating for thread 2
        else if (threadNumber == 2) {
            cpu2TextArea.setText("\nCPU 2\n");

            // If system is not running, display process being executed and time remaining
            if (!systemRunningFlag || currentProcess == null) {
                cpu2TextArea.append("Executing: Idle\n");
                cpu2TextArea.append("Time Remaining = n/a");
            }
            // Else, system is running, update with current process and time remaining
            else {
                cpu2TextArea.append("Executing: Process " + currentProcess.getProcessId() + "\n");
                cpu2TextArea.append("Time Remaining = " + timeRemaining); // determine time remaining
            }
        }
    }

    // Update system status label with passed in string
    public void updateSystemStatusLabel(String newLabel) {
        systemStatusLabel.setText(newLabel);
    }

    // Update report table
    public void updateReportTable() {
//        Object[] rowData = new Object[6];
//        // Clear table
//        for (int rowCount = (reportTableModel.getRowCount() - 1); rowCount >= 0; rowCount--) {
//            reportTableModel.removeRow(rowCount);
//            reportTable.revalidate();
//        }
//        // Update table with new information
//        for (Process finishedProcess : Utility.getFinishedProcesses()) {
//            rowData[0] = finishedProcess.getProcessId();
//            rowData[1] = finishedProcess.getArrivalTime();
//            rowData[2] = finishedProcess.getServiceTime();
//            rowData[3] = finishedProcess.getFinish_time();
//            rowData[4] = finishedProcess.getTurnaround_time();
//            rowData[5] = finishedProcess.getNorm_turnaround_time();
//            reportTableModel.addRow(rowData);
//        }
    }

    // Update throughput label with current throughput
    public void setThroughputLabel() {
        // Calculate current throughput
        throughput = (Utility.getFinishedProcesses().size() / (float) Utility.getSystemClock());
        // Create new label
        String newLabel = "Current Throughput: " + throughput + " process/unit of time";
        // Set new label
//        throughputLabel.setText(newLabel);
    }

    // Check if program is currently running
    public boolean checkProgramRunning() {
        return programRunning;
    }

    // Check if system is currently running or if it is paused
    public boolean checkSystemRunning() throws InterruptedException {
        // If the system is running
        if (systemRunningFlag) {
            // If both threads have finished, change system to not running
            if (cpu1_thread.finished() && cpu2_thread.finished()) {
                semaphore.acquire();
                // Set systemRunning flag to false, change status label to complete, and disable pause button
                systemRunningFlag = false;
                updateSystemStatusLabel("All Processes Complete");
                pauseButton.setEnabled(false);
                semaphore.release();
            }
        }
        return systemRunningFlag;
    }

    // Functional variables
    private List<Process> processList;
    private boolean systemRunningFlag = false;
    private boolean programRunning = true;
    private Semaphore semaphore;
    private float throughput = 0;

    // Thread variables
    private AtomicInteger n = new AtomicInteger();
    private AtomicInteger n2 = new AtomicInteger();
    private thread_run cpu1_thread;
    private thread_run cpu2_thread;

    thread_run.Stepper s1 = () -> {
        n.addAndGet(1);
        Thread.sleep(1);
    };
    thread_run.Stepper s2 = () -> {
        n2.addAndGet(1);
        Thread.sleep(1);
    };

    // Variables declaration
    private DefaultTableModel hrrnProcessQueueModel;
    private DefaultTableModel hrrnReportTableModel;
    private DefaultTableModel rrProcessQueueModel;
    private DefaultTableModel rrReportTableModel;
    private javax.swing.JTextArea cpu1TextArea;
    private javax.swing.JTextArea cpu2TextArea;
    private javax.swing.JTable hrrnProcessQueueTable;
    private javax.swing.JLabel hrrnProcessQueueLabel;
    private javax.swing.JTable hrrnReportTable;
    private javax.swing.JLabel hrrn_nTAT_label;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JButton pauseButton;
    private javax.swing.JLabel rrProcessQueueLabel;
    private javax.swing.JTable rrProcessQueueTable;
    private javax.swing.JTable rrReportTable;
    private javax.swing.JLabel rrTimeSliceLabel;
    private javax.swing.JTextField rrTimeSliceTextArea;
    private javax.swing.JLabel rr_nTAT_label;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel systemStatusLabel;
    private javax.swing.JLabel timeUnitLabel;
    private javax.swing.JLabel timeUnitLabel2;
    private javax.swing.JTextField timeUnitTextField;
    // End of variables declaration//GEN-END:variables
}
