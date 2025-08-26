package com.girlkun.models.boss.list_boss.TrainOffline;

import com.girlkun.models.boss.*;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.services.TaskService;

/**
 * @Stole By obito ZALO 0866468126
 */
public class ThanMeo extends TrainBoss {

    public ThanMeo(byte bossID, BossData bossData, Zone zone, int x, int y) throws Exception {
        super(BossID.MEO_THAN, BossesData.THAN_MEO, zone, x, y);
    }

    @Override
    public void reward(Player plKill) {
        //vật phẩm rơi khi diệt boss nhân bản
        if (plKill.isfight1) {
            plKill.typetrain++;
            TaskService.gI().checkDoneTaskKillBoss(plKill, this);
        }
        plKill.rsfight();
        this.chat("Hôm nay ta không được khỏe");
        this.playerkill = plKill;

    }

}
