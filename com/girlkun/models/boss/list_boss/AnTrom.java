package com.girlkun.models.boss.list_boss;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.server.Client;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.MapService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.SkillService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Béo sì tai
 */
public class AnTrom extends Boss {

    private long goldAnTrom;
    private long thoivangAnTrom;
    private long lastTimeAnTrom;
    private long lastTimeJoinMap;
    private static final long timeChangeMap = 10000; // thời gian đổi map 1 lần

    public AnTrom() throws Exception {
        super(BossID.AN_TROM, BossesData.AN_TROM);
    }

    @Override
    public Zone getMapJoin() {
        int mapId = this.data[this.currentLevel].getMapJoin()[Util.nextInt(0, this.data[this.currentLevel].getMapJoin().length - 1)];
        return MapService.gI().getMapById(mapId).zones.get(0);
    }

    @Override
    public Player getPlayerAttack() {
        return super.getPlayerAttack();
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            damage = 1;
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
            SkillService.gI().useSkill(this, plAtt, null, null);
            return damage;
        } else {
            return 0;
        }
    }

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie()) {
                    return;
                }
                if (Util.getDistance(this, pl) <= 40) {
                    if (!Util.canDoWithTime(this.lastTimeAnTrom, 500) || goldAnTrom > 10_000_000_000L) {
                        return;
                    }
                    int gold = 0;

                    if (pl.inventory.gold >= 100000000) {//nếu số vàng trong túi của người chơi lớn hơn hoặc bằng 100tr
                        gold = Util.nextInt(2000000, 30000000);// thì boss sẽ ăn trộm từ 20tr đến 30tr gold
                    } else if (pl.inventory.gold >= 10000000) {
                        gold = Util.nextInt(3000000, 5000000);
                        ;
                    } else if (pl.inventory.gold >= 1000000) {
                        gold = Util.nextInt(300000, 5000000);
                    }
                    this.chat("Trộm được " + Util.numberToMoney(gold) + " Vàng rồi!");

                    if (gold > 0) {
                        pl.inventory.gold -= gold;
                        goldAnTrom += gold;
                        Service.gI().stealMoney(pl, -gold);
                        ItemMap itemMap = new ItemMap(this.zone, 190, gold, (this.location.x + pl.location.x) / 2, this.location.y, this.id);
                        Service.gI().dropItemMap(this.zone, itemMap);
                        Service.gI().sendToAntherMePickItem(this, itemMap.itemMapId);
                        this.zone.removeItemMap(itemMap);
                        this.lastTimeAnTrom = System.currentTimeMillis();
                    }
                } else {
//                    System.out.println("moveTo");
                    if (Util.isTrue(1, 2)) {
                        this.moveToPlayer(pl);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(30, 40);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
    }

    @Override
    public void reward(Player plKill) {
        if (goldAnTrom != 0) {
            goldAnTrom = goldAnTrom * 8 / 10;
            for (byte i = 0; i < 5; i++) {
                ItemMap it = new ItemMap(this.zone, 190, (int) (goldAnTrom ), this.location.x + Util.nextInt(10, 20), this.zone.map.yPhysicInTop(this.location.x,
                        this.location.y - 24), plKill.id);
                Service.gI().dropItemMap(this.zone, it);
            }
//                       ItemMap it = new ItemMap(this.zone, plKill.gender == 0 ? 2000 : (plKill.gender == 1 ? 2001 : 2002), Util.nextInt(3, 4), this.location.x, this.zone.map.yPhysicInTop(this.location.x,
//                               this.location.y - 24), plKill.id);
//                     Service.gI().dropItemMap(this.zone, it);

        }

    }

    @Override
    public void joinMap() {
        super.joinMap();
        lastTimeJoinMap = System.currentTimeMillis() + timeChangeMap;
//        System.err.println("BOSS ANTROM");
        goldAnTrom = 0;
    }

    @Override
    public void update() {
        super.update();
        Player ramdonPlayer = Client.gI().getPlayers().get(Util.nextInt(Client.gI().getPlayers().size()));
        if (ramdonPlayer != null && ramdonPlayer.zone.isKhongCoTrongTaiTrongKhu()) {
            if (ramdonPlayer.zone.map.mapId != 51 && ramdonPlayer.zone.map.mapId != 113 && ramdonPlayer.zone.map.mapId != 129 && this.zone.getPlayers().size() <= 0 && System.currentTimeMillis() > this.lastTimeJoinMap) {
                if (ramdonPlayer.id != -1000000) {
                    lastTimeJoinMap = System.currentTimeMillis() + timeChangeMap;
                    System.err.println("Ăn Trộm Đã Tìm Thấy: " + this.zone.getPlayers().size() + " Player");
                    ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
                    ChangeMapService.gI().exitMap(this);
                    this.zoneFinal = null;
                    this.lastZone = null;
                    this.zone = ramdonPlayer.zone;
                    this.location.x = Util.nextInt(100, zone.map.mapWidth - 100);
                    this.location.y = zone.map.yPhysicInTop(this.location.x, 100);
                    this.joinMap();
                    System.err.println("BOSS AN TROM THAY DOI MAP");
                    System.err.println("New MAP: " + this.zone.map.mapId);
                    System.err.println("Ăn Trộm Đã Tìm Thấy: " + this.zone.getPlayers().size() + " Player");
                }

            }
        }

    }

}
