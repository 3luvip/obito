package com.girlkun.models.map.GiaiCuuMiNuong;

import com.girlkun.models.clan.Clan;
import com.girlkun.models.map.Zone;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Player;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bkt
 *
 *
 */
public class GiaiCuuMiNuong {

    public static final List<GiaiCuuMiNuong> GIAI_CUU_MI_NUONG;
    public static final int MAX_AVAILABLE = 500;
    public static final int TIME_GIAI_CUU_MI_NUONG = 1800000;
    public static int N_PLAYER_MAP = 3;
    public static final int N_PLAYER_CLAN = 0;
    private Player player;

    static {
        GIAI_CUU_MI_NUONG = new ArrayList<>();
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            GIAI_CUU_MI_NUONG.add(new GiaiCuuMiNuong(i));
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

    public GiaiCuuMiNuong(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
        running = true;
        //new Thread(this).start();
    }

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

    }

    public void opengiaicuuminuong(Player plOpen, Clan clan) {
        this.lastTimeOpen = System.currentTimeMillis();
        this.isOpened = true;
        this.clan = clan;
        this.clan.timeOpenGiaiCuuMiNuong = this.lastTimeOpen;
        this.clan.playerOpenGiaiCuuMiNuong = plOpen;
        this.clan.giaiCuuMiNuong = this;
        ChangeMapService.gI().changeMapBySpaceShip(plOpen, 185, -1, 60);
        resetGCMN();
        sendTextGiaiCuuMiNuong();
    }

    private void kickOutOfGCMN(Player player) {
        if (MapService.gI().isMapGiaiCuuMiNuong(player.zone.map.mapId)) {
            Service.getInstance().sendThongBao(player, "Giải Cứu Mị Nương Đã Kết Thúc Bạn Đang Được Đưa Ra Ngoài");
            ChangeMapService.gI().changeMapBySpaceShip(player, 27, -1, 1038);
            running = false;
            this.clan.giaiCuuMiNuong = null;
        }
    }

    private void resetGCMN() {
        long totalDame = 0;
        long totalHp = 0;
        for (Player pl : this.clan.membersInGame) {
            totalDame += pl.nPoint.dame;
            totalHp += pl.nPoint.hpMax;
        }

        for (Zone zone : this.zones) {
            for (Mob mob : zone.mobs) {
                mob.point.dame = (int) (totalHp / 20);
                mob.point.maxHp = (int) (200000000);
                mob.hoiSinhMob(mob);
            }
        }
    }

    public Zone getMapById(int mapId) {
        for (Zone zone : zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    public static void addZone(int idGiaiCuu, Zone zone) {
        GIAI_CUU_MI_NUONG.get(idGiaiCuu).zones.add(zone);
    }

    private void sendTextGiaiCuuMiNuong() {
        for (Player pl : this.clan.membersInGame) {
            ItemTimeService.gI().sendTextGiaiCuuMiNuong(pl);
        }
    }
}
