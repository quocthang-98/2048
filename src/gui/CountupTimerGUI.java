package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CountupTimerGUI implements Runnable {

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(new CountupTimerGUI());
    }

    private final CountupTimerModel model;

    private JButton resetButton, pauseButton, stopButton;

    private JLabel timerLabel;

    public CountupTimerGUI() {
        this.model = new CountupTimerModel();
    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Countup Timer GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(createDisplayPanel(), BorderLayout.NORTH);
        frame.add(createButtonPanel(), BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        Font font = panel.getFont().deriveFont(Font.BOLD, 48f);

        timerLabel = new JLabel(model.getFormattedDuration());
        timerLabel.setFont(font);
        panel.add(timerLabel);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        Font font = panel.getFont().deriveFont(Font.PLAIN, 16f);

        resetButton = new JButton("Reset");
        resetButton.setFont(font);
        panel.add(resetButton);
        resetButton.addActionListener(event -> {
            model.resetDuration();
            timerLabel.setText(model.getFormattedDuration());
        });

        pauseButton = new JButton("Restart");
        pauseButton.setFont(font);
        panel.add(pauseButton);
        pauseButton.addActionListener(event -> {
            String text = pauseButton.getText();
            if (text.equals("Pause")) {
                model.pauseTimer();
                pauseButton.setText("Restart");
            } else {
                model.startTimer();
                pauseButton.setText("Pause");
            }
        });

        Timer timer = new Timer(200,
                new CountupListener(CountupTimerGUI.this, model));
        stopButton = new JButton("Start");
        stopButton.setFont(font);
        panel.add(stopButton);
        stopButton.addActionListener(event -> {
            String text = stopButton.getText();
            if (text.equals("Start")) {
                model.resetDuration();
                model.startTimer();
                timer.start();
                resetButton.setEnabled(false);
                stopButton.setText("Stop");
            } else {
                model.stopTimer();
                timer.stop();
                resetButton.setEnabled(true);
                stopButton.setText("Start");
            }
        });

        Dimension d = getLargestJButton(resetButton, pauseButton, stopButton);
        resetButton.setPreferredSize(d);
        pauseButton.setPreferredSize(d);
        stopButton.setPreferredSize(d);
        pauseButton.setText("Pause");

        return panel;
    }

    private Dimension getLargestJButton(JButton... buttons) {
        Dimension largestDimension = new Dimension(0, 0);
        for (JButton button : buttons) {
            Dimension d = button.getPreferredSize();
            largestDimension.width = Math.max(largestDimension.width, d.width);
            largestDimension.height = Math.max(largestDimension.height,
                    d.height);
        }

        largestDimension.width += 10;
        return largestDimension;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public JButton getPauseButton() {
        return pauseButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JLabel getTimerLabel() {
        return timerLabel;
    }

    public class CountupListener implements ActionListener {

        private final CountupTimerGUI view;

        private final CountupTimerModel model;

        public CountupListener(CountupTimerGUI view, CountupTimerModel model) {
            this.view = view;
            this.model = model;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            model.setDuration();
            view.getTimerLabel().setText(model.getFormattedDuration());
        }

    }

    public class CountupTimerModel {

        private boolean isRunning;

        private long duration, previousDuration, startTime;

        public CountupTimerModel() {
            resetDuration();
        }

        public void resetDuration() {
            this.duration = 0L;
            this.previousDuration = 0L;
            this.isRunning = true;
        }

        public void startTimer() {
            this.startTime = System.currentTimeMillis();
            this.isRunning = true;
        }

        public void pauseTimer() {
            setDuration();
            this.previousDuration = duration;
            this.isRunning = false;
        }

        public void stopTimer() {
            setDuration();
            this.isRunning = false;
        }

        public void setDuration() {
            if (isRunning) {
                this.duration = System.currentTimeMillis() - startTime
                        + previousDuration;
            }
        }

        public String getFormattedDuration() {
            int seconds = (int) ((duration + 500L) / 1000L);
            int minutes = seconds / 60;
            int hours = minutes / 60;

            StringBuilder builder = new StringBuilder();
            if (hours > 0) {
                builder.append(hours);
                builder.append(":");
            }

            minutes %= 60;
            if (hours > 0) {
                builder.append(String.format("%02d", minutes));
                builder.append(":");
            } else if (minutes > 0) {
                builder.append(minutes);
                builder.append(":");
            }

            seconds %= 60;
            if (hours > 0 || minutes > 0) {
                builder.append(String.format("%02d", seconds));
            } else {
                builder.append(seconds);
            }

            return builder.toString();
        }
    }

}