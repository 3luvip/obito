package com.girlkun.models.boss.list_boss.DoanhTraiDocNhan;
import com.girlkun.consts.ConstMob;
import com.girlkun.models.mob.Mob;
import java.util.Random;
import com.girlkun.server.Manager;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossData;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * @author by KhanhDTK
 */
public class TrungUyTrang extends Boss {
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};

    public TrungUyTrang(Zone zone , int dame, long hp) throws Exception {
        super(BossID.TRUNG_UY_TRANG, new BossData(
                "Trung úy trắng", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{141, 142, 143, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((1 + dame)), //dame
                new long[]{((1 + hp))}, //hp
                new int[]{59}, //map join
                (int[][]) Util.addArray(FULL_DEMON), //skill
                new String[]{}, //text chat 1
                new String[]{"|-1|Nhóc con"}, //text chat 2
                new String[]{}, //text chat 3
                60
        ));
        this.zone = zone;
    }
    @Override
    public void reward(Player plKill) {
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR.length);
        if (Util.isTrue(10, 100)) {
            if (Util.isTrue(100, 100)) 
            Service.gI().dropItemMap(this.zone,new ItemMap(zone, 611, 1, this.location.x, this.location.y, plKill.id));   
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
    }
    @Override
    public void active() {
        super.active();
    }

    @Override
    public void joinMap() {
        super.joinMap();
    }
    //@Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (plAtt != null && (plAtt.isPl() || plAtt.isPet) && plAtt.effectSkill.isAnThan) {
            EffectSkillService.gI().removeAnThan(plAtt);
        }
        if (!this.isDie()) {
            // if (this.zone.isbulon13Alive) {
            //     if (System.currentTimeMillis() - lastTimeBlame > 3000) {
            //         this.chat("Ngu lắm đánh bulon 13 trước đi con zai");
            //     }
            //     lastTimeBlame = System.currentTimeMillis();
            //     return 0;
            // }
            // if (this.zone.isbulon14Alive) {
            //     if (System.currentTimeMillis() - lastTimeBlame > 5000) {
            //         this.chat("Ngu lắm đánh bulon 14 trước đi con zai");
            //     }
            //     lastTimeBlame = System.currentTimeMillis();
            //     return 0;
            // }
            if (!piercing && Util.isTrue(1, 100)) {
                this.chat("Xí hụt lêu lêu");
                return 0;
            }
            damage /= 3;
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

}





