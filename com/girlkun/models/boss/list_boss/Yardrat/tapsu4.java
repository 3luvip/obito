package com.girlkun.models.boss.list_boss.Yardrat;

import com.girlkun.models.boss.*;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

public class tapsu4 extends Boss {

    public tapsu4() throws Exception {
        super(BossID.TAPSU4, BossesData.TAPSU1, BossesData.TAPSU2, BossesData.TAPSU3, BossesData.TAPSU4);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(20, 50)) {
            Service.gI().dropItemMap(this.zone, Util.bikip(zone, 590, 50, this.location.x, this.location.y, plKill.id));
            return;
        }

    }

    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage / 2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 2;
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
