package com.girlkun.models.boss.dhvt;

import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;

/**
 * @author Duy Béo
 */
public class Ruong12 extends BossDHVT {

    public Ruong12(Player player) throws Exception {
        super(BossID.RUONG_12, BossesData.RUONG_12);
        this.playerAtt = player;
    }
}
