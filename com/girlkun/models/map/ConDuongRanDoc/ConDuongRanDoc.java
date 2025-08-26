package com.girlkun.models.map.ConDuongRanDoc;

import com.girlkun.models.clan.Clan;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.utils.Util;
import com.girlkun.models.map.TrapMap;
import com.girlkun.models.mob.Mob;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;

import java.util.ArrayList;
import java.util.List;

public class ConDuongRanDoc implements Runnable {

    public static final long POWER_CAN_GO_TO_CDRD = 200;

    public static final List<ConDuongRanDoc> CON_DUONG_RAN_DOCS;
    public static final int MAX_AVAILABLE = 50;
    public static final int TIME_CON_DUONG_RAN_DOC = 1800000;

    private Player player;

    static {
        CON_DUONG_RAN_DOCS = new ArrayList<>();
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            CON_DUONG_RAN_DOCS.add(new ConDuongRanDoc(i));
        }
    }

    public int id;
    public byte level;
    public final List<Zone> zones;

    public Clan clan;
    public boolean isOpened;
    private long lastTimeOpen;
    private boolean running;
    private long lastTimeUpdate;

    public ConDuongRanDoc() {
        this.zones = new ArrayList<>();
        running = true;
    }

    public ConDuongRanDoc(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
        running = true;
//        new Thread(this).start();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(10000);
                if (Util.canDoWithTime(lastTimeUpdate, 10000)) {
                    update();
                    lastTimeUpdate = System.currentTimeMillis();
                }
            } catch (Exception ignored) {
            }

        }
    }

    public void update() {
        for (ConDuongRanDoc khiga : CON_DUONG_RAN_DOCS) {
            if (khiga.isOpened) {
                if (Util.canDoWithTime(this.clan.ConDuongRanDoc.lastTimeOpen, TIME_CON_DUONG_RAN_DOC)) {
                    this.finish();
                }
            }
        }
    }

    public void openConDuongRanDoc(Player plOpen, Clan clan, byte level) {
        this.level = level;
        this.lastTimeOpen = System.currentTimeMillis();
        this.isOpened = true;
        this.clan = clan;
        this.clan.timeOpenConDuongRanDoc = this.lastTimeOpen;
        this.clan.playerOpenConDuongRanDoc = plOpen;
        this.clan.ConDuongRanDoc = this;
        resetConDuong();
        ChangeMapService.gI().goToCDRD(plOpen);
        ChangeMapService.gI().changeMapBySpaceShip(plOpen, 143, -1, 60);
        sendTextConDuongRanDoc();
    }

    private void resetConDuong() {
        for (Zone zone : zones) {
            for (TrapMap trap : zone.trapMaps) {
                trap.dame = this.level * 10000;
            }
        }
        for (Zone zone : zones) {
            for (Mob m : zone.mobs) {
                m.initMobConDuongRanDoc(m, this.level);
                m.hoiSinhMob(m);
                m.hoiSinh();
                m.sendMobHoiSinh();
            }
        }
    }

    //kết thúc Con Đường Rắn Độc
    public void finish() {
        List<Player> plOutCD = new ArrayList();
        for (Zone zone : zones) {
            List<Player> players = zone.getPlayers();
            for (Player pl : players) {
                plOutCD.add(pl);
                kickOutOfCDRD(pl);
            }

        }
        for (Player pl : plOutCD) {
            ChangeMapService.gI().changeMapBySpaceShip(pl, 0, -1, 64);
        }

        this.clan.ConDuongRanDoc = null;
        this.clan = null;
        this.isOpened = false;
    }

    private void kickOutOfCDRD(Player player) {
        if (MapService.gI().isMapConDuongRanDoc(player.zone.map.mapId)) {
            Service.getInstance().sendThongBao(player, "Con Đường Rắn Độc Đã Kết Thúc!");
            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 64);
            running = false;
            this.clan.ConDuongRanDoc = null;
        }
    }

    public Zone getMapById(int mapId) {
        for (Zone zone : zones) {
            if (zone.map.mapId == mapId) {
                System.out.println("Zone " + mapId);
                return zone;
            }

        }
        System.out.println("Zone null");
        return null;
    }

    public static void addZone(int idConDuong, Zone zone) {
        CON_DUONG_RAN_DOCS.get(idConDuong).zones.add(zone);
    }

    private void sendTextConDuongRanDoc() {
        for (Player pl : this.clan.membersInGame) {
            ItemTimeService.gI().sendTextConDuongRanDoc(pl);
        }
    }
}
