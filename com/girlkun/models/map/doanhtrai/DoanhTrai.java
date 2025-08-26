package com.girlkun.models.map.doanhtrai;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.mob.Mob;
import com.girlkun.services.ItemTimeService;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;
import java.util.Arrays;

/**
 *
 * - Stole By hope
 */
@Data
public class DoanhTrai {

    //bang hội đủ số người mới đc mở
    public static final int N_PLAYER_CLAN = 3;
    //số người đứng cùng khu
    public static final int N_PLAYER_MAP = 3;
    public static final int AVAILABLE = 50; // số lượng doanh trại trong server
    public static final int TIME_DOANH_TRAI = 1800000;

    private int id;
    private List<Zone> zones;
    private Clan clan;

    private long lastTimeOpen;

    public boolean timePickDragonBall;

    List<Integer> listMap = Arrays.asList(53, 58, 59, 60, 61, 62, 55, 56, 54, 57);
    private int currentIndexMap = -1;
    private List<Boss> bossDoanhTrai;

    public DoanhTrai(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
        this.bossDoanhTrai = new ArrayList<>();
    }

    public Zone getMapById(int mapId) {
        for (Zone zone : this.zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    public void openDoanhTrai(Player player) throws Exception {
        this.lastTimeOpen = System.currentTimeMillis();
        this.clan = player.clan;
        player.clan.doanhTrai = this;
        player.clan.doanhTrai_playerOpen = player.name;
        player.clan.doanhTrai_lastTimeOpen = this.lastTimeOpen;
        player.clan.timeOpenDoanhTrai = this.lastTimeOpen;
        //Khởi tạo quái, boss
        this.init();
        //Đưa thành viên vào doanh trại
        for (Player pl : player.clan.membersInGame) {
            if (pl == null || pl.zone == null || !player.zone.equals(pl.zone)) {
                continue;
            }
            ChangeMapService.gI().changeMapInYard(pl, 53, -1, 60);
            ItemTimeService.gI().sendTextDoanhTrai(pl);
        }
    }

    private void init() throws Exception{
        long totalDame = 0;
        long totalHp = 0;
        for(Player pl : this.clan.membersInGame){
            totalDame += pl.nPoint.dame;
            totalHp += pl.nPoint.hpMax;
        }
        
        
        //Hồi sinh quái
        for(Zone zone : this.zones){
            for(Mob mob : zone.mobs){
                mob.point.dame = (int) (totalHp / 20);
               mob.point.maxHp = (int) (totalDame * 40);
                mob.hoiSinh();
            }
        }
    }

    public void DropNgocRong() {
        for (Zone zone : zones) {
            ItemMap itemMap = null;
            switch (zone.map.mapId) {
                case 53:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, 917, 384, -1);
                    itemMap.isDoanhTraiBall = true;
                    Service.gI().dropItemMap(zone, itemMap);
                    break;
                case 58:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, 658, 336, -1);
                    itemMap.isDoanhTraiBall = true;
                    Service.gI().dropItemMap(zone, itemMap);
                    break;
                case 59:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, 675, 240, -1);
                    itemMap.isDoanhTraiBall = true;
                    Service.gI().dropItemMap(zone, itemMap);
                    break;
                case 60:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, Util.nextInt(725, 1241), 384, -1);
                    itemMap.isDoanhTraiBall = true;
                    Service.gI().dropItemMap(zone, itemMap);
                    break;
                case 61:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, 789, 264, -1);
                    itemMap.isDoanhTraiBall = true;
                    Service.gI().dropItemMap(zone, itemMap);
                    break;
                case 62:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, Util.nextInt(197, 1294), 384, -1);
                    itemMap.isDoanhTraiBall = true;
                    Service.gI().dropItemMap(zone, itemMap);
                    break;
                case 55:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, 789, 312, -1);
                    itemMap.isDoanhTraiBall = true;
                    Service.gI().dropItemMap(zone, itemMap);
                    break;
                case 56:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, 789, 312, -1);
                    itemMap.isDoanhTraiBall = true;
                    Service.gI().dropItemMap(zone, itemMap);
                    break;
            }
        }

    }

    public void dispose() {
        for (Boss b : bossDoanhTrai) {
            if (b != null) {
                b.changeStatus(BossStatus.LEAVE_MAP);
                b = null;
            }

        }
        this.clan = null;
        this.bossDoanhTrai.clear();
        timePickDragonBall = false;
        currentIndexMap = -1;
        bossDoanhTrai.clear();
    }
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - Stole By Arriety
 */
