package de.jmizv.timetracker;

import de.jmizv.timetracker.ui.MainFrame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import de.jmizv.timetracker.model.Week;

public class TimeTracker implements ActionListener {

    private final static String dataFile = "./data.json";
    private List<Week> weeks;
    private MainFrame frame;
    private int workDaysInWeek = 5;
    private int workingHoursInDay = 8;

    /**
     * Don't do this at home. These are u
     * @param msg 
     */
    public static void log(String msg) {
        log(msg, (Exception) null, (Object[]) null);
    }

    public static void log(String msg, Object... args) {
        log(msg, null, args);
    }

    public static void log(String msg, Exception ex, Object... args) {
        String _msg = args != null ? MessageFormat.format(msg, args) : msg;
        if (ex != null) {
            System.err.println(_msg);
        } else {
            System.out.println(_msg);
        }
    }

    public TimeTracker(MainFrame frame) {
        this.frame = frame;
    }

    public void init() throws FileNotFoundException, IOException {
        File file = new File(dataFile);
        if (!file.exists()) {
            weeks = new ArrayList<>();
            file.createNewFile();
        } else {
            Gson gson = new Gson();
            weeks = gson.fromJson(new FileReader(file), new TypeToken<List<Week>>() {
            }.getType());
            if (weeks == null) {
                weeks = new ArrayList<>();
            }
        }
        log("Loaded JSON data. Now update frame.");
        updateFrame(0);
    }

    private void updateFrame(long additionalSeconds) {
        Week todayWeek = getTodayWeek();
        if (todayWeek != null) {
            frame.setWeekPercentage(todayWeek.getSumOfWeek(), (int) additionalSeconds, workingHoursInDay * 3600 * workDaysInWeek);
            frame.setDayPercentage(todayWeek.getSumOfDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)), (int) additionalSeconds, workingHoursInDay * 3600);
        }
    }

    private Week getTodayWeek() {
        Calendar now = Calendar.getInstance();
        return getWeek(now);
    }

    private Week getWeek(Calendar dayOfWeek) {
        Calendar clone = (Calendar) dayOfWeek.clone();
        Week.transformCalendar(clone);
        for (Week week : weeks) {
            if (week.getWeek().equals(clone)) {
                return week;
            }
        }
        log("Now week for today was found.");
        return null;
    }

    private void persist() throws IOException {
        // Pretty printing for better reading in JSON.
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // sorting the weeks for better reading in JSON.
        weeks.sort((p,q)-> p.getWeek().compareTo(q.getWeek()));
        String jsonWeeks = gson.toJson(weeks);
        try (FileWriter fWriter = new FileWriter(new File(dataFile))) {
            fWriter.write(jsonWeeks, 0, jsonWeeks.length());
            fWriter.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                // Nimbus provides better readability of JProgressBar than native OSX LAF.
                if ("Nimbus".equals(info.getName())) { //NOI18N
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                   break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        }
        
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
        TimeTracker timeTracker = new TimeTracker(frame);
        timeTracker.init();
        frame.setActionListener(timeTracker);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                log("Closing application.");
                frame.setVisible(false);
                frame.dispose();
                try {
                    timeTracker.persist();
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        });
    }

    @Override
    public void actionPerformed(Event event) {
        switch (event.getID()) {
            case 1:
                updateFrame(event.getTimespan());
                break;
            case 2:
                Week todayWeek = getTodayWeek();
                if (todayWeek == null) {
                    todayWeek = new Week();
                    todayWeek.setWeek(Calendar.getInstance());
                    weeks.add(todayWeek);
                }
                todayWeek.add(Calendar.getInstance(), (int) event.getTimespan());
                updateFrame(0);
                try {
                    persist();
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case 3:
                Calendar cal = event.getCalendar();
                Week week = getWeek(cal);
                if (week == null) {
                    week = new Week();
                    week.setWeek(cal);
                    weeks.add(week);
                }
                week.add(cal, (int) event.getTimespan());
                updateFrame(0);
                try {
                    persist();
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            default:
                break;
        }
    }
}
