package de.jmizv.timetracker.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import java.util.stream.Collectors;
import de.jmizv.timetracker.TimeTracker;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Week implements Serializable {

    // TODO: replace Calendar by new java.time.* package
    private Calendar week;
    private Map<Integer, List<Integer>> entries = new HashMap<>();

    public Map<Integer, List<Integer>> getEntries() {
        return entries;
    }

    public void setEntries(Map<Integer, List<Integer>> entries) {
        this.entries = entries;
    }

    public Calendar getWeek() {
        return week;
    }

    public void setWeek(Calendar week) {
        this.week = (Calendar) week.clone();
        transformCalendar(this.week);
    }

    /**
     * Normalize the given calendar to be the first day of the week at exactly
     * midnight. The given object is modified.
     *
     * @param cal
     */
    public static void transformCalendar(Calendar cal) {
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    }

    public void add(Calendar when, Integer amountOfSeconds) {
        TimeTracker.log("Add {0} seconds to the week of {1}.", amountOfSeconds, new SimpleDateFormat("dd.MM.yyyy").format(when.getTime()));
        int dayOfWeek = when.get(Calendar.DAY_OF_WEEK);
        if (entries.get(dayOfWeek) == null) {
            entries.put(dayOfWeek, new ArrayList<>());
        }
        entries.get(dayOfWeek).add(amountOfSeconds);
    }

    public int getSumOfWeek() {
        return entries.values().stream().flatMap(List::stream)
                .collect(Collectors.toList()).stream()
                .reduce(0, (a, b) -> a + b);
    }

    public int getSumOfDay(int day) {
        List<Integer> get = entries.get(day);
        if (get == null) {
            return 0;
        }
        return get.stream().reduce(0, (a, b) -> a + b);
    }
}
