package com.girlkun.models.boss.list_boss.TrainOffline;

import com.girlkun.models.boss.*;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;

/**
 * @Stole By obito ZALO 0866468126
 */
public class ToSuKaio extends TrainBoss {

    public ToSuKaio(byte bossID, BossData bossData, Zone zone, int x, int y) throws Exception {
        super(BossID.TS_KAIO, BossesData.TO_SU_KAIO, zone, x, y);
    }

    @Override
    public void reward(Player plKill) {
        //vật phẩm rơi khi diệt boss nhân bản

        plKill.rsfight();
        this.chat("Hôm nay ta không được khỏe");
        this.playerkill = plKill;

    }

    @Override
    public void attack() {
        if (this.playerTarger == null || this.playerTarger.isDie()) {
            this.leaveMap();
        }
    }

    @Override
    public void active() {

    }

}
