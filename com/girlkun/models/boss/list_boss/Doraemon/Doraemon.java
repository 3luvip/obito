package com.girlkun.models.boss.list_boss.Doraemon;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;
import java.util.Random;

public class Doraemon extends Boss {

    public Doraemon() throws Exception {
        super(BossID.DORAEMON, BossesData.DORAEMON);
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

    TaskService.gI().checkDoneTaskKillBoss(plKill, this);

    // Rơi item ID 1651 tự do và ngẫu nhiên
    if (Util.isTrue(80, 100)) {
        // Số lượng item ID 1651 muốn rơi
        int numberOfItems = 5;
        
        // Tạo và rơi từng item ID 1651
        for (int i = 0; i < numberOfItems; i++) {
            // Random vị trí x và y để rơi
            int randomX = this.location.x + Util.nextInt(-24, 24); // Random từ -24 đến 24 đơn vị x
            int randomY = this.location.y + Util.nextInt(-24, 24); // Random từ -24 đến 24 đơn vị y
            
            // Tạo item ID 1651 với vị trí x và y ngẫu nhiên
            ItemMap it = new ItemMap(this.zone, 1651, 1, randomX, randomY, plKill.id);
            
            // Rơi item vào map
            Service.getInstance().dropItemMap(this.zone, it);
        }
    }
}



    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        //   if(Util.canDoWithTime(st,900000)){
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
/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
