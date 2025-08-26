package com.girlkun.models.player;

import com.girlkun.models.shop.ShopServiceNew;
import com.girlkun.services.MapService;
import com.girlkun.consts.ConstMap;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.Zone;
import com.girlkun.server.Manager;
import com.girlkun.services.MapService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
// đây
import java.util.ArrayList;
import java.util.List;

/**
 * @author BTH sieu cap vippr0
 */
public class Referee2 extends Player {

    private long lastTimeChat;
    private Player playerTarget;

    private long lastTimeTargetPlayer;
    private long timeTargetPlayer = 5000;
    private long lastZoneSwitchTime;
    private long zoneSwitchInterval;
    private List<Zone> availableZones;

    public void initReferee2() {
        init();
    }

    @Override
    public short getHead() {
        return 83;
    }

    @Override
    public short getBody() {
        return 84;
    }

    @Override
    public short getLeg() {
        return 85;
    }

    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
    }

    @Override
    public void update() {
        if (Util.canDoWithTime(lastTimeChat, 5000)) {
            Service.getInstance().chat(this, "Chào mừng đã đến với NRO AKAYA");
            Service.getInstance().chat(this, "Sever By Obito");
            lastTimeChat = System.currentTimeMillis();
            if (this.nPoint.hp <= 1000000000) {
                Service.gI().hsChar(this, this.nPoint.hpMax, this.nPoint.mpMax);
            }
        }
    }

    private void init() {
        int id = -1000000;
        for (Map m : Manager.MAPS) {
            if (m.mapId == 45) {
                for (Zone z : m.zones) {
                    Referee2 pl = new Referee2();
                    pl.name = "Mr.PoPo";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.hpMax = (int) 500;
                    pl.nPoint.hpg = (int) 500;
                    pl.nPoint.hp = (int) 500;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 317;
                    pl.location.y = 408;
                    joinMap(z, pl);
                    z.setReferee2(pl);

                }
            }
        }
    }
    
    
}

//}

