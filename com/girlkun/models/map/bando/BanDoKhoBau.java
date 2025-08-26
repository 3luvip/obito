package com.girlkun.models.map.bando;

import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossStatus;

import com.girlkun.models.clan.Clan;
import com.girlkun.models.boss.list_boss.bandokhobau.TrungUyXanhLoBdkb;
import com.girlkun.models.map.Zone;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Player;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.func.ChangeMapService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Khánh Đẹp Zoai
 */
@Data
public class BanDoKhoBau {

    public static final long POWER_CAN_GO_TO_DBKB = 2000000000L;

    public static final int N_PLAYER_CLAN = 2;
    public static final int AVAILABLE = 20; // số lượng bdkb trong server
    public static final int TIME_BAN_DO_KHO_BAU = 1_800_000;

    public static final int MAX_HP_MOB = 32_600_000;
    public static final int MIN_HP_MOB = 16_300_000;

    public static final int MAX_DAME_MOB = 2_000_000;
    public static final int MIN_DAME_MOB = 300000;

    private int id;
    private List<Zone> zones;
    private Clan clan;

    private long lastTimeOpen;

    public int level;

    List<Integer> listMap = Arrays.asList(135, 138, 136, 137);
    private int currentIndexMap = -1;

    TrungUyXanhLoBdkb boss;

    public boolean timePickReward;

    public BanDoKhoBau(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
    }

    public Zone getMapById(int mapId) {
        for (Zone zone : this.zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    public void openBDKB(Player player, int level) {
        this.lastTimeOpen = System.currentTimeMillis();
        this.clan = player.clan;
        this.level = level;
        player.clan.banDoKhoBau = this;
        player.clan.banDoKhoBau_playerOpen = player.name;
        player.clan.banDoKhoBau_lastTimeOpen = this.lastTimeOpen;

        player.bdkb_isJoinBdkb = true;
        player.bdkb_countPerDay++;
        player.bdkb_lastTimeJoin = System.currentTimeMillis();
        ChangeMapService.gI().goToDBKB(player);
        for (Player pl : player.clan.membersInGame) {
            if (pl == null || pl.zone == null) {
                continue;
            }
            ItemTimeService.gI().sendTextBanDoKhoBau(pl);
        }

    }

    public void setLastTimeOpen(long lastTimeOpen) {
        this.lastTimeOpen = lastTimeOpen;
    }

    public void init() {
        //Hồi sinh quái và thả boss
        for (Zone zone : this.zones) {
            if (zone.map.mapId == this.listMap.get(this.currentIndexMap)) {
                long newHpMob = ((this.level - 1) / (2 - 1)) * (MAX_HP_MOB - MIN_HP_MOB) + MIN_HP_MOB;
                long newDameMob = ((this.level - 1) / (2 - 1)) * (MAX_DAME_MOB - MIN_DAME_MOB) + MIN_DAME_MOB;
                for (Mob mob : zone.mobs) {
                    mob.point.dame = (int) (newDameMob);
                    mob.point.maxHp = (int) (newHpMob);
                    mob.hoiSinh();
                }
                int idBoss = (zone.map.mapId == 137 ? BossID.TRUNG_UY_XANH_LO : -1);
                if (idBoss != -1) {
                    boss = (TrungUyXanhLoBdkb) BossManager.gI().createBossBdkb(idBoss, (int) (newDameMob * 4 > 2_000_000_000 ? 2_000_000_000 : newDameMob * 4), (int) (newHpMob * 5 > 2_000_000_000 ? 2_000_000_000 : newHpMob * 5), zone);
                }
            }
        }
    }

    public void dispose() {
        boss.changeStatus(BossStatus.LEAVE_MAP);
        this.clan = null;
        this.boss = null;
        timePickReward = false;
        currentIndexMap = -1;
    }

}
