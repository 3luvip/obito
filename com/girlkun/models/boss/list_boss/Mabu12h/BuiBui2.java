package com.girlkun.models.boss.list_boss.Mabu12h;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;

import java.util.Random;

public class BuiBui2 extends Boss {

    public BuiBui2() throws Exception {
        super(Util.randomBossId(), BossesData.BUI_BUI_2);
    }

    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        int[] itemDos = new int[] { 555, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566, 567 };
        int randomc12 = new Random().nextInt(itemDos.length);
        if (Util.isTrue(BossManager.ratioReward, 100)) {
            if (Util.isTrue(3, 5)) {
                Service.gI().dropItemMap(this.zone,
                        Util.ratiItem(zone, 561, 1, this.location.x, this.location.y, plKill.id));
            }
            if (Util.isTrue(2, 10)) {
                Service.gI().dropItemMap(this.zone,
                        Util.ratiItem(zone, 2000 + plKill.gender, 1, this.location.x, this.location.y, plKill.id));
            }
            Service.gI().dropItemMap(this.zone,
                    Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else if (Util.isTrue(2, 5)) {
            Service.gI().dropItemMap(this.zone,
                    Util.RaitiDoc12(zone, itemDos[randomc12], 1, this.location.x, this.location.y, plKill.id));

        } else {
            Service.gI().dropItemMap(this.zone,
                    new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
        plKill.fightMabu.changePoint(10);

        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        this.checkAnThan(plAtt);
        if (this != null) {
            this.nPoint.isBuiBui = true;
        }
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                this.chat("Xí hụt");
                return 0;
            }
            if (plAtt != null && plAtt.nPoint.isSieuThan) {
                damage = this.nPoint.subDameInjureWithDeff(damage);
            } else {
                damage = this.nPoint.subDameInjureWithDeff(damage / 2);
            }
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 1;
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
