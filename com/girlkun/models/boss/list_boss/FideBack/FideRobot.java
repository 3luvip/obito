package com.girlkun.models.boss.list_boss.FideBack;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;
import java.util.Random;

public class FideRobot extends Boss {

    public FideRobot() throws Exception {
        super(BossID.FIDE_ROBOT, BossesData.FIDE_ROBOT);
    }

    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemDC12.length);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        int[] itemDos = new int[]{233, 237, 241, 245, 249, 253, 257, 261, 265, 269, 273, 277, 281};
        int randomc12 = new Random().nextInt(itemDos.length);
        if (Util.isTrue(BossManager.ratioReward, 100)) {
            if (Util.isTrue(100, 100)) {
                Service.gI().dropItemMap(this.zone, new ItemMap(zone, 874, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.RaitiDoc12(zone, Manager.itemDC12[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else if (Util.isTrue(2, 5)) {
            Service.gI().dropItemMap(this.zone, Util.RaitiDoc12(zone, itemDos[randomc12], 1, this.location.x, this.location.y, plKill.id));
            return;
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
    }

    @Override
    public void active() {
        this.attack();
    }

//    @Override
//    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
//        if (plAtt != null) {
//            switch (plAtt.playerSkill.skillSelect.template.id) {
//                case Skill.KAMEJOKO:
//                case Skill.MASENKO:
//                case Skill.ANTOMIC:
//                    int hpHoi = (int) ((long) damage * 80 / 100);
//                    PlayerService.gI().hoiPhuc(this, hpHoi, 0);
//                    if (Util.isTrue(1, 5)) {
//                        this.chat("Hahaha,Các ngươi nghĩ sao vậy?");
//                    }
//                    return 0;
//            }
//        }
//        return super.injured(plAtt, damage, piercing, isMobAttack);
//    }
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
