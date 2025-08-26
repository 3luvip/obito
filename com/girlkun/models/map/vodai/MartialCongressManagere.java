package com.girlkun.models.map.vodai;

import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author obito
 */
public class MartialCongressManagere {

    private static MartialCongressManagere i;
    private long lastUpdate;
    private static List<MartialCongresse> list = new ArrayList<>();
    private static List<MartialCongresse> toRemove = new ArrayList<>();

    public static MartialCongressManagere gI() {
        if (i == null) {
            i = new MartialCongressManagere();
        }
        return i;
    }

    public void update() {
        if (Util.canDoWithTime(lastUpdate, 1000)) {
            lastUpdate = System.currentTimeMillis();
            synchronized (list) {
                for (MartialCongresse mc : list) {
                    try {
                        mc.update();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                list.removeAll(toRemove);
            }
        }
    }

    public void add(MartialCongresse mc) {
        synchronized (list) {
            list.add(mc);
        }
    }

    public void remove(MartialCongresse mc) {
        synchronized (toRemove) {
            toRemove.add(mc);
        }
    }
}
