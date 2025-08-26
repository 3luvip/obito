package com.girlkun.models.player;

import com.girlkun.models.map.Map;
import com.girlkun.models.map.Zone;
import com.girlkun.models.skill.PlayerSkill;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

import java.util.List;

/**
 * @author Duy Béo
 */
public class Referee extends Player {

    private long lastTimeChat;
    private Player playerTarget;

    private long lastTimeTargetPlayer;
    private long timeTargetPlayer = 5000;
    private long lastZoneSwitchTime;
    private long zoneSwitchInterval;
    private List<Zone> availableZones;

    public void initReferee() {
        init();
    }

    @Override
    public short getHead() {
        if (this.name.endsWith("Trọng Tài")) {
            return 114;
        }
        if (this.name.endsWith("Yajirô")) {
            return 77;
        }
        return 0;
    }

    @Override
    public short getBody() {
        return (short) (getHead() + 1);
    }

    @Override
    public short getLeg() {
        return (short) (getHead() + 2);
    }
    @Override
    public short getFlagBag() {
        return 156;
    }
    @Override
    public byte getAura() {
        return 84;
    }
    
    @Override
    public byte getEffFront() {
        return 5;
    }
    
    @Override
    public short getMount() {
        return 47;
    }

    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
    }

    @Override
    public void update() {
        if (Util.canDoWithTime(lastTimeChat, 5000) && this.id != 1) {
            Service.getInstance().chat(this, "Đại Hội Võ Thuật lần thứ 23 đã chính thức khai mạc");
            Service.getInstance().chat(this, "Còn chờ gì nữa mà không đăng kí tham gia để nhận nhiều phẩn quà hấp dẫn");
            lastTimeChat = System.currentTimeMillis();
        }
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {

        return 0;

    }

    private void init() {
        int id = -1000000;
        for (Map m : Manager.MAPS) {
            if (m.mapId == 52) {
                for (Zone z : m.zones) {
                    Referee pl = new Referee();
                    pl.name = "Trọng Tài";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.isfake = true;
                    pl.nPoint.hpMax = 69;
                    pl.nPoint.hpg = 69;
                    pl.nPoint.hp = 69;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 387;
                    pl.location.y = 336;
                    joinMap(z, pl);
                    z.setReferee(pl);
                    z.update();
                }
            } else if (m.mapId == 129) {
                for (Zone z : m.zones) {
                    Referee pl = new Referee();
                    pl.name = "Trọng Tài";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.isfake = true;
                    pl.nPoint.hpMax = 69;
                    pl.nPoint.hpg = 69;
                    pl.nPoint.hp = 69;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 385;
                    pl.location.y = 264;
                    joinMap(z, pl);
                    z.setReferee(pl);
                }
            } else if (m.mapId == 46) {
                for (Zone z : m.zones) {
                    Referee pl = new Referee();
                    pl.name = "Yajirô";
                    pl.gender = 0;
                    pl.id = 1;
                    pl.isfake = true;
                    pl.nPoint.hpMax = 1100;
                    pl.nPoint.hpg = 1100;
                    pl.nPoint.hp = 1100;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 322;
                    pl.location.y = 408;
                    joinMap(z, pl);
                    z.setReferee(pl);
                }
            }
        }
    }
}
