package de.jmizv.timetracker;

import java.util.Calendar;

public interface ActionListener {

    void actionPerformed(Event e);

    static class Event {

        int id;
        long timespan;
        Calendar calendar;

        public Event(int id, long timespan) {
            this(id, null, timespan);
        }

        public Event(int id, Calendar cal, long timespan) {
            this.id = id;
            this.timespan = timespan;
            this.calendar = cal;
        }

        Calendar getCalendar() {
            return calendar;
        }

        long getTimespan() {
            return timespan;
        }

        int getID() {
            return id;
        }
    }
}
