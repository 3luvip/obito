/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.boss.list_boss;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 * @@Stole By obito ZALO 0866468126
 */
public class gohanNN extends Boss {

    public gohanNN() throws Exception {
        super(BossID.GOHAN_NN, BossesData.GOHAN_NHAT_NGUYET);
    }

    @Override
    public void reward(Player plKill) {

        Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 16, Util.nextInt(1, 2), this.location.x, this.location.y, plKill.id));
        return;

    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            return Util.nextInt(700, 1000);
        } else {
            return 0;
        }
    }

    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        //    if (Util.canDoWithTime(st, 900000)) {
        //       this.changeStatus(BossStatus.LEAVE_MAP);
        //   }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;

}
