package de.jmizv.timetracker.ui;

import java.awt.Dialog;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import javax.swing.JProgressBar;
import de.jmizv.timetracker.ActionListener;
import de.jmizv.timetracker.ActionListener.Event;
import de.jmizv.timetracker.TimeTracker;

public class MainFrame extends javax.swing.JFrame {

    private boolean trackingActive = false;
    private Calendar startOfTracking;
    private Calendar stopOfTracking;
    private ActionListener actionListener;
    private Thread workerThread;
    private boolean stopThread = false;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        initOwnComponents();
    }

    public static String getString(String key) {
        return ResourceBundle.getBundle("de/jmizv/timetracker/Bundle").getString(key);
    }
    
    public ActionListener getActionListener() {
        return actionListener;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private void initOwnComponents() {
        toggleButtonText();
    }

    private void toggleButtonText() {
        String text = trackingActive ? getString("ACTIV") : getString("INACTIV");
        if (stopOfTracking != null && stopOfTracking.before(startOfTracking) && startOfTracking.get(Calendar.DAY_OF_MONTH) == stopOfTracking.get(Calendar.DAY_OF_MONTH)) {
            long lastPause = startOfTracking.getTimeInMillis() - stopOfTracking.getTimeInMillis();
            statusLabel.setText(MessageFormat.format(getString("LASTPAUSE"), new Object[]{lastPause / 60_000}));
        }
        trackingButton.setText(text);
    }

    public Calendar getStartOfTracking() {
        return startOfTracking;
    }

    public void setWeekPercentage(int seconds, int additionalSeconds, int factor) {
        updateProgressBar(weekProgressBar, seconds, additionalSeconds, factor, "WEEK");
    }

    private String formatPercentage(int h, int m, int s, String key) {
        if (h == 0) {
            return MessageFormat.format(getString(key + "_M_S"), new Object[]{(m <= 9 ? "0" : ""), m, (s <= 9 ? "0" : ""), s});
        }
        return MessageFormat.format(getString(key + "_H_M"), new Object[]{(h <= 9 ? "0" : ""), h, (m <= 9 ? "0" : ""), m});
    }

    public void setDayPercentage(int seconds, int additionalSeconds, int factor) {
        updateProgressBar(dayProgressBar, seconds, additionalSeconds, factor, "DAY");
    }

    private void updateProgressBar(JProgressBar progressBar, int seconds, int additionalSeconds, int factor, String prefix) {
        int percentage = (int) ((seconds + additionalSeconds) * 100.0 / factor);
        progressBar.setValue(percentage > 100 ? 100 : percentage);
        String stringToSet = percentage + "%";
        int remainingSeconds = Math.abs(factor - (seconds + additionalSeconds));
        int h = remainingSeconds / 3600;
        int m = (remainingSeconds % 3600) / 60;
        int s = remainingSeconds - h * 3600 - m * 60;
        if (percentage < 100) {
            stringToSet += " " + formatPercentage(h, m, s, "TIME_LEFT");
        } else if (percentage > 100) {
            stringToSet += " " + formatPercentage(h, m, s, "TIME_OVER");
        }
        progressBar.setString(getString(prefix) + ": " + stringToSet);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        trackingButton = new javax.swing.JToggleButton();
        dayProgressBar = new javax.swing.JProgressBar();
        weekProgressBar = new javax.swing.JProgressBar();
        statusLabel = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("TimeTracker v0.3");
        setResizable(false);

        trackingButton.setText("jToggleButton1");
        trackingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trackingButtonActionPerformed(evt);
            }
        });

        dayProgressBar.setStringPainted(true);

        weekProgressBar.setStringPainted(true);

        statusLabel.setText(" ");

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/jmizv/timetracker/Bundle"); // NOI18N
        addButton.setText(bundle.getString("ADD")); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(trackingButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dayProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addComponent(weekProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(trackingButton)
                    .addComponent(addButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dayProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(weekProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void trackingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trackingButtonActionPerformed
        trackingActive = !trackingActive;
        if (trackingActive) {
            startOfTracking = Calendar.getInstance();
            //stopOfTracking = null;
            stopThread = false;
            workerThread = new Thread(() -> {
                while (!stopThread) {
                    try {
                        Thread.sleep(250);
                        if (!stopThread) {
                            actionListener.actionPerformed(new Event(1, getSeconds()));
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace(System.err);
                        break;
                    }
                }
            });
            workerThread.start();
            TimeTracker.log("Started Worker Thread.");
        } else {
            signalToStop();
        }
        toggleButtonText();
    }//GEN-LAST:event_trackingButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        AddTimeDialog addTimeDialog = new AddTimeDialog();
        addTimeDialog.setActionListener(actionListener);
        addTimeDialog.setModal(true);
        addTimeDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        addTimeDialog.setLocation(this.getLocation());
        addTimeDialog.setVisible(true);
    }//GEN-LAST:event_addButtonActionPerformed

    private long getSeconds() {
        return (System.currentTimeMillis() - startOfTracking.getTimeInMillis()) / 1000;
    }

    private void signalToStop() {
        TimeTracker.log("Signal to stop Worker Thread.");
        stopThread = true;
        stopOfTracking = Calendar.getInstance();
        if (startOfTracking != null) {
            actionListener.actionPerformed(new Event(2, getSeconds()));
        }
    }

    @Override
    public void dispose() {
        if (!stopThread) {
            signalToStop();
        }
        try {
            if (workerThread != null) {
                workerThread.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
        }
        super.dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JProgressBar dayProgressBar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JToggleButton trackingButton;
    private javax.swing.JProgressBar weekProgressBar;
    // End of variables declaration//GEN-END:variables
}
