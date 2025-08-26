package com.girlkun.models.boss.vodai;

import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;

/**
 * @author BTH sieu cap vippr0
 */
public class Bongbang extends BossVD {

    public Bongbang(Player player) throws Exception {
        super(BossID.BONGBANG, BossesData.BONGBANG);
        this.playerAtt = player;
    }
}
