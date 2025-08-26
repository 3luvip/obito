package com.girlkun.models.boss.list_boss.TrainOffline;

import com.girlkun.models.boss.*;
import com.girlkun.models.map.Zone;
import com.girlkun.services.func.ChangeMapService;

/**
 * @Stole By obito ZALO 0866468126
 */
public class Thuongde extends TrainBoss {

    public Thuongde(byte bossID, BossData bossData, Zone zone, int x, int y) throws Exception {
        super(BossID.THUONG_DE, BossesData.THUONG_DE, zone, x, y);
    }

    @Override
    public void leaveMap() {
        //       ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
        ChangeMapService.gI().exitMap(this);
        BossManager.gI().removeBoss(this);
        this.dispose();
        ChangeMapService.gI().changeMap(this.playerkill, 45, 0, 373, 408);
    }

}
