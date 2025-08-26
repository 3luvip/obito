package com.girlkun.models.boss.vodai;

import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;

/**
 * @author BTH sieu cap vippr0
 */
public class Thodaubac extends BossVD {

    public Thodaubac(Player player) throws Exception {
        super(BossID.THODAUBAC, BossesData.THODAUBAC);
        this.playerAtt = player;
    }
}
