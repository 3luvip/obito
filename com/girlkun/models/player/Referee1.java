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
public class Referee1 extends Player {

    private long lastTimeChat;
    private Player playerTarget;

    private long lastTimeTargetPlayer;
    private long timeTargetPlayer = 5000;
    private long lastZoneSwitchTime;
    private long zoneSwitchInterval;
    private List<Zone> availableZones;

    public void initReferee1() {
        init();
    }

    @Override
    public short getHead() {
        return 114;
    }

    @Override
    public short getBody() {
        return 115;
    }

    @Override
    public short getLeg() {
        return 116;
    }

    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
    }

    @Override
    public void update() {
        if (Util.canDoWithTime(lastTimeChat, 5000)) {
            Service.getInstance().chat(this, "Võ đài One Piece chính thức khai mạc");
            Service.getInstance().chat(this, "Còn chờ gì nữa mà không đăng kí tham gia để nhận nhiều phẩn quà hấp dẫn");
            lastTimeChat = System.currentTimeMillis();
            if (this.nPoint.hp <= 1000000000) {
                Service.gI().hsChar(this, this.nPoint.hpMax, this.nPoint.mpMax);
            }
        }
    }

    private void init() {
        int id = -1000000;
        for (Map m : Manager.MAPS) {
            if (m.mapId == 112) {
                for (Zone z : m.zones) {
                    Referee1 pl = new Referee1();
                    pl.name = "Trọng Tài";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.hpMax = (int) 500;
                    pl.nPoint.hpg = (int) 500;
                    pl.nPoint.hp = (int) 500;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 468;
                    pl.location.y = 336;
                    joinMap(z, pl);
                    z.setReferee1(pl);

                }
            }
        }
    }
}

//}

