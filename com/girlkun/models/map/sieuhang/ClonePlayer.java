package com.girlkun.models.map.sieuhang;

import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.dhvt.BossDHVT;
import com.girlkun.models.player.Player;
import com.girlkun.utils.Util;

public class ClonePlayer extends BossDHVT {

    public ClonePlayer(Player player, BossData data, int id) throws Exception {
        super(Util.randomBossId(), data, 5000);
        this.playerAtt = player;
        this.idPlayer = id;
    }
}
