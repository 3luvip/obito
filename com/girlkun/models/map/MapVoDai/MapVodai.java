package com.girlkun.models.map.MapVoDai;

import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import java.util.List;

import com.girlkun.models.player.Player;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;
import java.util.Date;

public class MapVodai {
    
    //tiem pick ngọc rông
      private static long TIME_CAN_PICK_VD;
      public static final byte HOUR_CAN_PICK_VD = 21;
    public static final byte MIN_CAN_PICK_VD = 2;
    public static final byte SECOND_CAN_PICK_VD = 0;
public static final int TIME_CAN_PICK_VODAI_BALL_AFTER_DROP = 5000;


    public static final byte HOUR_OPEN_MAP_VODAI = 10;
    public static final byte MIN_OPEN_MAP_VODAI = 0;
    public static final byte SECOND_OPEN_MAP_VODAI = 0;
public static int numRegistered ;

    public static final byte HOUR_CLOSE_MAP_VODAI = 23;
    public static final byte MIN_CLOSE_MAP_VODAI = 59;
    public static final byte SECOND_CLOSE_MAP_VODAI = 0;

    public static final int AVAILABLE = 1;

    private static MapVodai i;

    public static long TIME_OPEN_VODAI;
    public static long TIME_CLOSE_VODAI;

    private int day = -1;

    public static MapVodai gI() {
        if (i == null) {
            i = new MapVodai();
        }
        i.setTimeJoinMapVodai();
        return i;
    }

    public void setTimeJoinMapVodai() {
        if (i.day == -1 || i.day != TimeUtil.getCurrDay()) {
            i.day = TimeUtil.getCurrDay();
            try {
                TIME_OPEN_VODAI = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN_MAP_VODAI + ":" + MIN_OPEN_MAP_VODAI + ":" + SECOND_OPEN_MAP_VODAI, "dd/MM/yyyy HH:mm:ss");
                  this.TIME_CAN_PICK_VD = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CAN_PICK_VD + ":" + MIN_CAN_PICK_VD + ":" + SECOND_CAN_PICK_VD, "dd/MM/yyyy HH:mm:ss");
           
                TIME_CLOSE_VODAI = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE_MAP_VODAI + ":" + MIN_CLOSE_MAP_VODAI + ":" + SECOND_CLOSE_MAP_VODAI, "dd/MM/yyyy HH:mm:ss");
            } catch (Exception ignored) {
            }
        }
    }


    private void kickOutOfMapVodai(Player player) {
        if (MapService.gI().isMapVodai(player.zone.map.mapId)) {
            Service.gI().sendThongBao(player, "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
        }
    }

    private void ketthucvodai(Player player) {
       numRegistered = 0;
    player.registeredVodai = 0;
    player.ischeckjoinvodai = 0;
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            kickOutOfMapVodai(pl);
        }
    }

    public void joinMapVodai(Player player) {
        player.ischeckjoinvodai += 1;
         player.registeredVodai = 0;
        boolean changed = false;
        if (player.clan != null) {
            List<Player> players = player.zone.getPlayers();
            for (Player pl : players) {
                if (pl.clan != null && !player.equals(pl) && player.clan.equals(pl.clan) && !player.isBoss) {
                    Service.gI().changeFlag(player, Util.nextInt(9, 10));
                    changed = true;
                    break;
                }
            }
        }
        if (!changed && !player.isBoss) {
            Service.gI().changeFlag(player, Util.nextInt(9, 10));
        }
    }
    public void joinBattle(Player player) {
    if (player.registeredVodai == 1) {
        if (player.ischeckjoinvodai == 0) {
            // Code để cho phép người chơi tham gia trận đấu
            ChangeMapService.gI().changeMap(player, 206, 0, 673, 456);
        } else {
            Service.gI().sendThongBao(player, "Hiện tại, trận đấu đang diễn ra. Vui lòng quay lại vào ngày hôm sau để tham gia.");
        }
    } else {
        Service.gI().sendThongBao(player, "Bạn chưa đăng ký tham gia đại chiến vũ trụ. Vui lòng đăng ký trước khi tham gia.");
    }
}
    public void registerPlayer(Player player) {
        if (player.registeredVodai == 0) {
            player.registeredVodai = 1;
            this.numRegistered++;
            Service.gI().sendThongBao(player, "Bạn đã đăng ký tham gia đại chiến vũ trụ thành công!");
        } else {
            Service.gI().sendThongBao(player, "Bạn đã đăng ký rồi");
        }
    }

    public int getNumRegistered() {
        return this.numRegistered;
    }

   public boolean pickVodaiBall(Player player, Item item) {
    try {
        if (player.iDMark.isHoldVodaiBall()) {
            // Người chơi đang cầm một viên ngọc rồng
            Service.getInstance().sendThongBao(player, "Bạn đang cầm một viên ngọc rồng, không thể nhặt thêm!");
            return false;
        } else if (System.currentTimeMillis() < this.TIME_CAN_PICK_VD) {
            Service.getInstance().sendThongBao(player, "Chưa thể nhặt ngọc rồng ngay lúc này, vui lòng đợi "
                    + TimeUtil.diffDate(new Date(this.TIME_CAN_PICK_VD),
                    new Date(System.currentTimeMillis()), TimeUtil.SECOND) + " giây nữa");
            return false;
        } else if (player.zone.finishMapVodai) {
            Service.getInstance().sendThongBao(player, "Đại chiến ngọc rồng sao đen "
                    + "đã kết thúc, vui lòng đợi đến ngày mai");
            return false;
        } else {
            if (Util.canDoWithTime(player.zone.lastTimeDropVodaiBall, TIME_CAN_PICK_VODAI_BALL_AFTER_DROP)) {
                player.iDMark.setHoldVodaiBall(true);
                player.iDMark.setTempIdVodaiBallHold(item.template.id);
                player.iDMark.setLastTimeHoldVodaiBall(System.currentTimeMillis());
                Service.getInstance().sendFlagBag(player);
                if (player.clan != null) {
                    List<Player> players = player.zone.getPlayers();
                    for (Player pl : players) {
                        // Xử lý thông báo cho clan
                    }
                }
                return true;
            } else {
                Service.getInstance().sendThongBao(player, "Không thể nhặt ngọc rồng đen ngay lúc này");
                return false;
            }
        }
    } catch (Exception ex) {
        System.out.println("loi ne nrsd 3 ");
        return false;
    }
}
public synchronized void dropVodaiBall(Player player) {
        if (player.iDMark.isHoldVodaiBall()) {
            player.iDMark.setHoldVodaiBall(false);
            ItemMap itemMap = new ItemMap(player.zone,
                    player.iDMark.getTempIdVodaiBallHold(), 1, player.location.x,
                    player.zone.map.yPhysicInTop(player.location.x, player.location.y - 24),
                    -1);
            Service.getInstance().dropItemMap(itemMap.zone, itemMap);
            player.iDMark.setTempIdVodaiBallHold(-1);
            player.zone.lastTimeDropVodaiBall = System.currentTimeMillis();
            Service.getInstance().sendFlagBag(player); //gui vao tui do

            if (player.clan != null) {
                List<Player> players = player.zone.getPlayers();
                for (Player pl : players) {
                    if (pl.clan != null && player.clan.equals(pl.clan)) {
                        Service.getInstance().changeFlag(pl, Util.nextInt(9, 10));
                    }
                }
            } else {
                Service.getInstance().changeFlag(player, Util.nextInt(9, 10));
            }
        }
    }
    public void update(Player player) {
        if (player.zone == null || MapService.gI().isMapVodai(player.zone.map.mapId)) {
            try {
                long now = System.currentTimeMillis();
                if (now < TIME_OPEN_VODAI || now > TIME_CLOSE_VODAI) {
                    ketthucvodai(player);
                }
            } catch (Exception ignored) {
            }
        }

    }
}
