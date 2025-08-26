package com.girlkun.models.mob;

import com.girlkun.consts.ConstMap;
import com.girlkun.consts.ConstMob;
import com.girlkun.consts.ConstTask;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;

import java.util.List;

import com.girlkun.models.map.Zone;
import com.girlkun.models.map.bando.BanDoKhoBau;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.player.Location;
import com.girlkun.models.player.Pet;
import com.girlkun.models.player.Player;
import com.girlkun.models.reward.ItemMobReward;
import com.girlkun.models.reward.MobReward;
import com.girlkun.models.skill.PlayerSkill;
import com.girlkun.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerManager;
import com.girlkun.services.*;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import java.io.IOException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Mob {

    public int id;
    public Zone zone;
    public int tempId;
    public String name;
    public byte level;

    public MobPoint point;
    public MobEffectSkill effectSkill;
    public Location location;

    public byte pDame;
    public int pTiemNang;
    private long maxTiemNang;
    public int MobImage;

    public long lastTimeDie;
    public int lvMob = 0;
    public int status = 5;
    public byte typeHiru = 0;
    public long delayBoss = 0;
    public short pointX;
    public short pointY;

    public boolean isMobMe;

    public Mob(Mob mob) {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
        this.id = mob.id;
        this.tempId = mob.tempId;
        this.level = mob.level;
        this.point.setHpFull(mob.point.getHpFull());
        this.point.sethp(this.point.getHpFull());
        this.location.x = mob.location.x;
        this.location.y = mob.location.y;
        this.pDame = mob.pDame;
        this.pTiemNang = mob.pTiemNang;
        this.MobImage = mob.MobImage;
        this.setTiemNang();
    }

    public Mob() {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
    }

    public void setTiemNang() {
        this.maxTiemNang = (long) this.point.getHpFull() * (this.pTiemNang + Util.nextInt(-2, 2)) / 100;
    }

    private long lastTimeAttackPlayer;

    public boolean isDie() {
        return this.point.gethp() <= 0;
    }

    public boolean isSieuQuai() {
        return this.lvMob > 0;
    }

    public synchronized void injured(Player plAtt, double damage, boolean dieWhenHpFull) {
        if (!this.isDie()) {
            if (damage >= this.point.hp) {
                damage = this.point.hp;
            }
            if (this.zone.map.mapId == 112) {
                plAtt.NguHanhSonPoint++;
            }
            if (!dieWhenHpFull) {
                if (this.point.hp == this.point.maxHp && damage >= this.point.hp) {
                    damage = (long) this.point.hp - 1;
                }
                if (this.tempId == 0 || this.tempId == 87 && damage > 10) {
                    damage = 10;
                }
                if (this.tempId == 70) {
                    damage = 5000000;
                }
            }
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        if (plAtt.nPoint.multicationChuong > 0 && Util.canDoWithTime(plAtt.nPoint.lastTimeMultiChuong, PlayerSkill.TIME_MUTIL_CHUONG)) {
                            damage *= plAtt.nPoint.multicationChuong;
                            plAtt.nPoint.lastTimeMultiChuong = System.currentTimeMillis();
                        }

                }
            }

            this.point.hp -= (long) damage;
            if (this.point.hp > 2123455999L) {
                Service.getInstance().sendThongBao(plAtt, "|4|HP: \b|5|" + "-" + Util.format(this.point.hp));
            }

            if (this.isDie()) {
                this.lvMob = 0;
                this.status = 0;
                this.sendMobDieAffterAttacked(plAtt, damage);
                TaskService.gI().checkDoneTaskKillMob(plAtt, this);
                TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
                this.lastTimeDie = System.currentTimeMillis();
                if (this.id == 13) {
                    this.zone.isbulon13Alive = false;
                }
                if (this.id == 14) {
                    this.zone.isbulon14Alive = false;
                }
                if (this.isSieuQuai()) {
                    plAtt.achievement.plusCount(12);
                }
            } else {
                this.sendMobStillAliveAffterAttacked(damage, plAtt != null ? plAtt.nPoint.isCrit : false);
            }
            if (plAtt != null) {
                Service.gI().addSMTN(plAtt, (byte) 2, getTiemNangForPlayer(plAtt, damage), true);
            }
        }
    }

    public long getTiemNangForPlayer(Player pl, double dame) {
        int levelPlayer = Service.getInstance().getCurrLevel(pl);
        int n = levelPlayer - this.level;
        long pDameHit = 0;
        if (point.getHpFull() >= 100000000) {
            pDameHit = Util.TamkjllGH(dame) * 500 / point.getHpFull();
        } else {
            pDameHit = Util.TamkjllGH(dame) * 100 / point.getHpFull();
        }

        long tiemNang = pDameHit * maxTiemNang / 100;
        if (n >= 0) {
            for (int i = 0; i < n; i++) {
                long sub = tiemNang * 10 / 100;
                if (sub <= 0) {
                    sub = 1;
                }
                tiemNang -= sub;
            }
        } else {
            for (int i = 0; i < -n; i++) {
                long add = tiemNang * 10 / 100;
                if (add <= 0) {
                    add = 1;
                }
                tiemNang += add;
            }
        }
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        tiemNang = Util.TamkjllGH(pl.nPoint.calSucManhTiemNang(tiemNang));
        if (pl.zone.map.mapId == 122
                || pl.zone.map.mapId == 123
                || pl.zone.map.mapId == 124
                || pl.zone.map.mapId == 141
                || pl.zone.map.mapId == 142
                || pl.zone.map.mapId == 146) {

            tiemNang *= 2;
        }
        return tiemNang;
    }

    public void update() {
        if (this.tempId == 71) {
            try {
                Message msg = new Message(102);
                msg.writer().writeByte(5);
                msg.writer().writeShort(this.zone.getPlayers().get(0).location.x);
                Service.gI().sendMessAllPlayerInMap(zone, msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }

        if (isDie() && (tempId == 71 || tempId == 72)) {
            Service.getInstance().sendBigBoss(this.zone, tempId == 71 ? 7 : 6, 0, -1, -1);
        }

        if (isDie() && this.tempId == 70 && (System.currentTimeMillis() - lastTimeDie) > 3000 && level <= 2) {
            if (level == 0) {
                level = 1;
                action = 6;
                this.point.hp = this.point.maxHp;
            } else if (level == 1) {
                level = 2;
                action = 5;
                this.point.hp = this.point.maxHp;
            } else if (level == 2) {
                level = 3;
                action = 9;
            }
            int trai = 0;
            int phai = 1;
            int next = 0;

            for (int i = 0; i < 30; i++) {
                int X = next == 0 ? -7 * trai : 7 * phai;
                if (next == 0) {
                    trai++;
                } else {
                    phai++;
                }
                next = next == 0 ? 1 : 0;
                if (trai > 10) {
                    trai = 0;
                }
                if (phai > 10) {
                    phai = 0;
                }
                
                ItemMap itemMap = new ItemMap(zone, 76, Util.nextInt(20000, 35000),
                        location.x + X + Util.nextInt(10, 20), location.y, -1);
                Service.getInstance().dropItemMap(zone, itemMap);
            }
            if (Util.isTrue(40, 100)) {
                for (int i = 0; i < Util.nextInt(1, 3); i++) {
                    ItemMap itemMap2 = new ItemMap(zone, 568, 1, location.x + Util.nextInt(-100, 100), location.y, -1);
                    Service.getInstance().dropItemMap(zone, itemMap2);
                }
                Random random = new Random();
                if (Util.isTrue(50, 100)) {
                    for (int i = 0; i < Util.nextInt(3, 6); i++) {
                        byte randomItemIndexDoTl = (byte) random.nextInt(Manager.itemIds_TL.length);
                        ItemMap itemMap3 = Util.ratiItem(zone, Manager.itemIds_TL[randomItemIndexDoTl], 1,
                                location.x + Util.nextInt(-100, 100), location.y, -1);
                        Service.getInstance().dropItemMap(zone, itemMap3);
                    }
                }
            }
            Service.gI().sendBigBoss2(zone, action, this);
            if (level <= 2) {
                Message msg = null;
                try {
                    msg = new Message(-9);
                    msg.writer().writeByte(this.id);
                    msg.writer().writeInt((int) this.point.hp);
                    msg.writer().writeInt(1);
                    Service.gI().sendMessAllPlayerInMap(zone, msg);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (msg != null) {
                        msg.cleanup();
                        msg = null;
                    }
                }
            } else {
                location.x = -1000;
                location.y = -1000;
            }
        }


        if (this.isDie() && !Maintenance.isRuning) {
            switch (zone.map.type) {
                case ConstMap.MAP_DOANH_TRAI:
                    if (this.zone.isTrungUyTrangAlive && this.tempId == 22 && this.zone.map.mapId == 59) {
                        if (Util.canDoWithTime(lastTimeDie, 5000)) {
                            if (this.id == 13) {
                                this.zone.isbulon13Alive = true;
                            }
                            if (this.id == 14) {
                                this.zone.isbulon14Alive = true;
                            }
                            this.hoiSinh();
                            this.sendMobHoiSinh();
                        }

                    }
                    break;
                case ConstMap.MAP_BAN_DO_KHO_BAU:
                    if (this.tempId == 72 || this.tempId == 71) {//ro bot bao ve
                        if (System.currentTimeMillis() - this.lastTimeDie > 3000) {
                            try {
                                Message t = new Message(102);
                                t.writer().writeByte((this.tempId == 71 ? 7 : 6));
                                Service.getInstance().sendMessAllPlayerInMap(this.zone, t);
                                t.cleanup();
                            } catch (IOException e) {
                            }
                        }
                    }
                    break;
                case ConstMap.MAP_KHI_GAS:
                    break;
                case ConstMap.MAP_CON_DUONG_RAN_DOC:
                    break;
                case ConstMap.MAP_GIAI_CUU_MI_NUONG:
                    break;
                case ConstMap.MAP_HIRU:
                    if (Util.canDoWithTime(this.lastTimeDie, 5000) && this.tempId == 70 && !Util.isTimeHiru()) {
                        this.level = 0;
                        this.hoiSinh();
                        this.sendMobHoiSinh();
                    } else if (this.tempId == 70 || this.tempId == 71 || this.tempId == 72 && this.isDie() && this.level == 3) {
                        this.zone.mobs.remove(0);
                    }
                    break;
                default:
                    if (Util.canDoWithTime(lastTimeDie, 5000)) {
                        this.randomSieuQuai();
                        this.hoiSinh();
                        this.sendMobHoiSinh();
                    }

            }
        }
        effectSkill.update();
        if (tempId >= 70 && tempId <= 72) {
            BigbossAttack();
        } else {
            attackPlayer();
        }
    }

    public void attackPlayer() {
        if (!isDie() && !effectSkill.isHaveEffectSkill() && !(tempId == 0) && !(tempId == 82) && !(tempId == 83) && !(tempId == 84)) {

            if ((this.tempId == 72 || this.tempId == 71) && Util.canDoWithTime(lastTimeAttackPlayer, 300)) {
                List<Player> pl = getListPlayerCanAttack();
                if (!pl.isEmpty()) {
                    this.sendMobBossBdkbAttack(pl, this.point.getDameAttack());
                } else {
                    if (this.tempId == 71) {
                        Player plA = getPlayerCanAttack();
                        if (plA != null) {
                            try {
                                Message t = new Message(102);
                                t.writer().writeByte(5);
                                t.writer().writeByte(plA.location.x);
                                this.location.x = plA.location.x;
                                Service.getInstance().sendMessAllPlayerInMap(this.zone, t);
                                t.cleanup();
                            } catch (IOException e) {
                            }
                        }

                    }
                }
                this.lastTimeAttackPlayer = System.currentTimeMillis();
            } else if (Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
                Player pl = getPlayerCanAttack();
                if (pl != null) {
                    this.mobAttackPlayer(pl);
                }
                this.lastTimeAttackPlayer = System.currentTimeMillis();
            }

        }
    }

    private void sendMobBossBdkbAttack(List<Player> players, long dame) {
        if (this.tempId == 72) {
            try {
                Message t = new Message(102);
                int action = Util.nextInt(0, 2);
                t.writer().writeByte(action);
                if (action != 1) {
                    this.location.x = players.get(Util.nextInt(0, players.size() - 1)).location.x;
                }
                t.writer().writeByte(players.size());
                for (Byte i = 0; i < players.size(); i++) {
                    t.writer().writeInt((int) players.get(i).id);
                    t.writer().writeInt((int) players.get(i).injured(null, (int) dame, false, true));
                }
                Service.getInstance().sendMessAllPlayerInMap(this.zone, t);
                t.cleanup();
            } catch (IOException e) {
            }
        } else if (this.tempId == 71) {
            try {
                Message t = new Message(102);
                t.writer().writeByte(Util.getOne(3, 4));
                t.writer().writeByte(players.size());
                for (Byte i = 0; i < players.size(); i++) {
                    t.writer().writeInt((int) players.get(i).id);
                    t.writer().writeInt((int) players.get(i).injured(null, (int) dame, false, true));
                }
                Service.getInstance().sendMessAllPlayerInMap(this.zone, t);
                t.cleanup();
            } catch (IOException e) {
            }
        }
    }

    private List<Player> getListPlayerCanAttack() {
        List<Player> plAttack = new ArrayList<>();
        int distance = (this.tempId == 71 ? 250 : 600);
        try {
            List<Player> players = this.zone.getNotBosses();
            for (Player pl : players) {
                if (!pl.isDie() && !pl.isBoss && !pl.effectSkin.isVoHinh) {
                    int dis = Util.getDistance(pl, this);
                    if (dis <= distance) {
                        plAttack.add(pl);
                    }
                }
            }
        } catch (Exception e) {
        }

        return plAttack;
    }

    public static void initMopbKhiGas(Mob mob, int level) {
        if (level <= 700) {
            mob.point.dame = (level * 3250 * mob.level * 4) * 5;
            mob.point.maxHp = (level * 12472 * mob.level * 2 + level * 7263 * mob.tempId) * 5;
        }
        if (level > 700 && level <= 10000) {
            mob.point.dame = (level * 3250 * mob.level * 4) * 5;
            mob.point.maxHp = 2100000000;
        }
        if (level > 10000) {
            mob.point.dame = 2000000000;
            mob.point.maxHp = 2100000000;
        }
    }

    public static void initMobConDuongRanDoc(Mob mob, int level) {
        mob.point.dame = level * 3250 * mob.level * 4;
        mob.point.maxHp = level * 12472 * mob.level * 2 + level * 7263 * mob.tempId;
    }

    public static void hoiSinhMob(Mob mob) {
        mob.point.hp = mob.point.maxHp;
        mob.setTiemNang();
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(mob.id);
            msg.writer().writeByte(mob.tempId);
            msg.writer().writeByte(0); //level mob
            msg.writer().writeInt((mob.point.hp));
            Service.getInstance().sendMessAllPlayerInMap(mob.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendMobHoiSinh() {
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(this.id);
            msg.writer().writeByte(this.tempId);
            msg.writer().writeByte(lvMob);
            msg.writer().writeInt(this.point.hp);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private Player getPlayerCanAttack() {
        int distance = 100;
        Player plAttack = null;
        try {
            List<Player> players = this.zone.getNotBosses();
            for (Player pl : players) {
                if (!pl.isDie() && !pl.isBoss && !pl.effectSkin.isVoHinh && !pl.isNewPet) {
                    int dis = Util.getDistance(pl, this);
                    if (dis <= distance) {
                        plAttack = pl;
                        distance = dis;
                    }
                }
            }
        } catch (Exception e) {

        }
        return plAttack;
    }

    //**************************************************************************
    public void mobAttackPlayer(Player player) {
        double dameMob = this.point.getDameAttack();
        if (player.charms.tdDaTrau > System.currentTimeMillis()) {
            dameMob /= 2;
        }
        if (this.isSieuQuai()) {
            dameMob = player.nPoint.hpMax / 10;
        }
        double dame = player.injured(null, dameMob, false, true);
        if (player.isPet3) {
            dame = Util.nextInt(500, 1000);
        }
        this.sendMobAttackMe(player, dame);
        this.sendMobAttackPlayer(player);
    }

    private void sendMobAttackMe(Player player, double dame) {
        if (!player.isPet && !player.isNewPet) {
            Message msg;
            try {
                msg = new Message(-11);
                msg.writer().writeByte(this.id);
                msg.writer().writeInt(Util.TamkjllGH(dame)); //dame
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    private void sendMobAttackPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-10);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeInt(Util.TamkjllGH(player.nPoint.hp));
            Service.getInstance().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void randomSieuQuai() {
        if (this.tempId != 0 && MapService.gI().isMapKhongCoSieuQuai(this.zone.map.mapId) && Util.nextInt(0, 150) < 1) {
            this.lvMob = 1;
        }
    }

    public void hoiSinh() {
        this.status = 5;
        this.point.hp = this.point.maxHp;
        this.setTiemNang();
    }

    //**************************************************************************
    private void sendMobDieAffterAttacked(Player plKill, double dameHit) {
        Message msg;
        try {
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(Util.TamkjllGH(dameHit));
            msg.writer().writeBoolean(plKill.nPoint.isCrit); // crit
            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            if (plKill.zone != null && plKill.isPl()) {
                if (plKill.zone.map.mapId == 181) {
                    if (Util.isTrue(60, 100)) {
                        plKill.diembitich++;
                        Service.getInstance().sendMoney(plKill);
                    }
                }

            }
            hutItem(plKill, items);
        } catch (Exception e) {
        }
    }

    public void sendMobDieAfterMobMeAttacked(Player plKill, long dameHit) {
        this.status = 0;
        Message msg;
        try {
            if (this.id == 13) {
                this.zone.isbulon13Alive = false;
            }
            if (this.id == 14) {
                this.zone.isbulon14Alive = false;
            }
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writer().writeLong(dameHit);
            msg.writer().writeBoolean(false); // crit

            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            hutItem(plKill, items);
        } catch (IOException e) {
            Logger.logException(Mob.class, e);
        }
//        if (plKill.isPl()) {
//            if (TaskService.gI().IsTaskDoWithMemClan(plKill.playerTask.taskMain.id)) {
//                TaskService.gI().checkDoneTaskKillMob(plKill, this, true);
//            } else {
//                TaskService.gI().checkDoneTaskKillMob(plKill, this, false);
//            }
//
//        }
        this.lastTimeDie = System.currentTimeMillis();
    }

    private void hutItem(Player player, List<ItemMap> items) {
        if (!player.isPet && !player.isNewPet) {
            if (player.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(player, item.itemMapId, true);
                    }
                }
            }
        } else {
            if (((Pet) player).master.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(((Pet) player).master, item.itemMapId, true);
                    }
                }
            }
        }
    }

    private List<ItemMap> mobReward(Player player, ItemMap itemTask, Message msg) {
        int mapid = player.zone.map.mapId;

//        nplayer
        List<ItemMap> itemReward = new ArrayList<>();
        try {
//            if ((!player.isPet && player.getSession().actived && player.setClothes.setDHD == 5)) {
//                byte random = 0;
//                if (Util.isTrue(5, 100)) {
//                    random = 1;
//
//                    Item i = Manager.RUBY_REWARDS.get(Util.nextInt(0, Manager.RUBY_REWARDS.size() - 1));
//                    i.quantity = random;
//                    InventoryServiceNew.gI().addItemBag(player, i);
//                    InventoryServiceNew.gI().sendItemBags(player);
//                    Service.gI().sendThongBao(player, "Bạn Vừa Nhận Được" + i.template.name);
//                }
//            }
            if (!player.isBoss && (mapid == 189)) {
                byte random = 1;
                if (Util.isTrue(100, 100)) {
                    random = 1;

                    Item i = Manager.HONGNGOC_REWARDS.get(Util.nextInt(0, Manager.HONGNGOC_REWARDS.size() - 1));
                    i.quantity = random;
                    InventoryServiceNew.gI().addItemBag(player, i);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendThongBao(player, "Bạn vừa nhận được " + random + " hồng ngọc");
                }
            }

            itemReward = this.getItemMobReward(player, this.location.x + Util.nextInt(-10, 10),
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y));
            if (itemTask != null) {
                itemReward.add(itemTask);
            }
            msg.writer().writeByte(itemReward.size()); //sl item roi
            for (ItemMap itemMap : itemReward) {
                msg.writer().writeShort(itemMap.itemMapId);// itemmapid
                msg.writer().writeShort(itemMap.itemTemplate.id); // id item
                msg.writer().writeShort(itemMap.x); // xend item
                msg.writer().writeShort(itemMap.y); // yend item
                msg.writer().writeInt((int) itemMap.playerId); // id nhan nat
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemReward;
    }

    public List<ItemMap> getItemMobReward(Player player, int x, int yEnd) {
        List<ItemMap> list = new ArrayList<>();

        final Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(11);

        int tileVang = 0;
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length);
//        List<ItemMap> list = new ArrayList<>();
//        int tileVang = 0;
//        MobReward mobReward = Manager.MOB_REWARDS.get(this.tempId);
//        if (mobReward == null) {
//            return list;
//
//        }
//        final Calendar rightNow = Calendar.getInstance();
//        int hour = rightNow.get(11);
//        List<ItemMobReward> items = mobReward.getItemReward();
//        List<ItemMobReward> golds = mobReward.getGoldReward();
//        if (!items.isEmpty()) {
//            ItemMobReward item = items.get(Util.nextInt(0, items.size() - 1));
//            ItemMap itemMap = item.getItemMap(zone, player, x, yEnd);
//            if (itemMap != null) {
//                list.add(itemMap);
//            }
//        }
//        if (!golds.isEmpty()) {
//            ItemMobReward gold = golds.get(Util.nextInt(0, golds.size() - 1));
//            ItemMap itemMap = gold.getItemMap(zone, player, x, yEnd);
//            if (itemMap != null) {
//                list.add(itemMap);
//            }
//        }
        if (player.itemTime.isUseMayDo && Util.isTrue(21, 100) && this.tempId > 57 && this.tempId < 66) {
            list.add(new ItemMap(zone, 380, 1, x, player.location.y, player.id));
            if (Util.isTrue(1, 100) && this.tempId > 57 && this.tempId < 66) {    //up bí kíp
                list.add(new ItemMap(zone, Util.nextInt(1099, 1102), 1, x, player.location.y, player.id));
            }
        }// vat phẩm rơi khi user maaáy dò adu hoa r o day ti code choa
        if (player.itemTime.isUseMayDo2 && Util.isTrue(1, 100) && this.tempId > 1 && this.tempId < 81) {
            list.add(new ItemMap(zone, 2036, 1, x, player.location.y, player.id));// cai nay sua sau nha
        }
        if (player.cFlag >= 1 && Util.isTrue(100, 100) && this.tempId == 0 && hour != 1 && hour != 3 && hour != 5 && hour != 7 && hour != 9 && hour != 11 && hour != 13 && hour != 15 && hour != 17 && hour != 19 && hour != 21 && hour != 23) {    //up bí kíp
            list.add(new ItemMap(zone, 590, 1, x, player.location.y, player.id));// cai nay sua sau nha
            if (Util.isTrue(50, 100) && this.tempId == 0) {    //up bí kíp
                list.add(new ItemMap(zone, 590, 1, x, player.location.y, player.id));
                if (Util.isTrue(50, 100) && this.tempId == 0) {    //up bí kíp
                    list.add(new ItemMap(zone, 590, 1, x, player.location.y, player.id));
                    if (Util.isTrue(50, 100) && this.tempId == 0) {    //up bí kíp
                        list.add(new ItemMap(zone, 590, 1, x, player.location.y, player.id));
                    }
                }
            }
        }
        if (this.zone.map.mapId == 192) {
            if (Util.isTrue(30, 100)) {
                Item quatrung = ItemService.gI().createNewItem((short) (568));
                InventoryServiceNew.gI().addItemBag(player, quatrung);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Quả Trứng");
            }
        }

        // Rơi vật phẩm Mảnh Vỡ Bông Tai ( ID 541 )        
        if (this.zone.map.mapId == 156) {
            if (Util.isTrue(70, 100)) {
                Item manh2 = ItemService.gI().createNewItem((short) (1359));
                InventoryServiceNew.gI().addItemBag(player, manh2);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Mảnh Vỡ Bông Tai ");
            }
        }
        // Rơi vật phẩm Mảnh Vỡ Bông Tai ( ID 541 )        
        if (this.zone.map.mapId == 157) {
            if (Util.isTrue(70, 100)) {
                Item manh3 = ItemService.gI().createNewItem((short) (1360));
                InventoryServiceNew.gI().addItemBag(player, manh3);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Mảnh Vỡ Bông Tai ");
            }
        }
        // Rơi vật phẩm Mảnh Vỡ Bông Tai ( ID 541 )        
        if (this.zone.map.mapId == 158) {
            if (Util.isTrue(70, 100)) {
                Item manh4 = ItemService.gI().createNewItem((short) (1361));
                InventoryServiceNew.gI().addItemBag(player, manh4);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Mảnh Vỡ Bông Tai ");
            }
        }

        if ((zone.map.mapId >= 135 && zone.map.mapId <= 138) && Util.isTrue(100, 100)) {
            if (player.clan.banDoKhoBau.level <= 10) {
                int min = 1000;
                int max = 1700;
                Random random = new Random();
                int randomvang = random.nextInt(max - min + 1) + min;
                int randomvang2 = random.nextInt(max - min + 1) + min;

                for (int i = 0; i < player.clan.banDoKhoBau.level / 2; i++) {
                    ItemMap it = new ItemMap(this.zone, 76, randomvang, this.location.x + i * 20, this.location.y, player.id);
                    //   ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                    //   Service.gI().dropItemMap(this.zone, it2);
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level / 3; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2, this.location.x - i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level / 4; i++) {
                    ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 17, this.location.y, player.id);
                    //    Service.gI().dropItemMap(this.zone, it2);
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level / 4; i++) {
                    ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x - i * 17, this.location.y, player.id);
                    //    Service.gI().dropItemMap(this.zone, it2);
                }
            }
            if (player.clan.banDoKhoBau.level > 10 && player.clan.banDoKhoBau.level <= 50) {
                int min = 1200;
                int max = 2000;
                Random random = new Random();
                int randomvang = random.nextInt(max - min + 1) + min;
                int randomvang2 = random.nextInt(max - min + 1) + min;

                for (int i = 0; i < player.clan.banDoKhoBau.level * (3 / 5); i++) {
                    ItemMap it = new ItemMap(this.zone, 76, randomvang, this.location.x + i * 20, this.location.y, player.id);
                    //   ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                    //   Service.gI().dropItemMap(this.zone, it2);
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level / 2; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2, this.location.x - i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level / 3; i++) {
                    ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 17, this.location.y, player.id);
                    //    Service.gI().dropItemMap(this.zone, it2);
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level / 3; i++) {
                    ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x - i * 17, this.location.y, player.id);
                    //    Service.gI().dropItemMap(this.zone, it2);
                }
            } else if (player.clan.banDoKhoBau.level > 50 && player.clan.banDoKhoBau.level <= 80) {
                int min = 3000;
                int max = 3500;
                int minx = 42;
                int maxx = 1165;
                Random random = new Random();
                int randomvang2 = random.nextInt(max - min + 1) + min;
//                int randomtoado = ;
                for (int i = 0; i < player.clan.banDoKhoBau.level / 4; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);

                }
                for (int i = 0; i < player.clan.banDoKhoBau.level / 4; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2, this.location.x - i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level / 6; i++) {
                    ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 17, this.location.y, player.id);
                    //    Service.gI().dropItemMap(this.zone, it2);
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level / 6; i++) {
                    ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x - i * 17, this.location.y, player.id);
                    //    Service.gI().dropItemMap(this.zone, it2);
                }
            } else {
                int min = 3500;
                int max = 5500;
                int minx = 42;
                int maxx = 1165;
                Random random = new Random();
                int randomvang2 = random.nextInt(max - min + 1) + min;
//                int randomtoado = ;
                for (int i = 0; i < player.clan.banDoKhoBau.level / 3; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);

                }
                for (int i = 0; i < player.clan.banDoKhoBau.level / 3; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2, this.location.x - i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level / 6; i++) {
                    ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 17, this.location.y, player.id);
                    //    Service.gI().dropItemMap(this.zone, it2);
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level / 6; i++) {
                    ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x - i * 17, this.location.y, player.id);
                    //    Service.gI().dropItemMap(this.zone, it2);
                }
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159 && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            if (Util.isTrue(10, 100)) {    //up bí kíp
                list.add(new ItemMap(zone, 2076, 1, x, player.location.y, player.id));
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159) {
            if (Util.isTrue(10, 100)) {    //up bí kíp
                list.add(new ItemMap(zone, 933, 1, x, player.location.y, player.id));
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159) {
            if (Util.isTrue(10, 100)) {    //up bí kíp
                list.add(new ItemMap(zone, 934, 1, x, player.location.y, player.id));
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159 && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            if (Util.isTrue(10, 100)) {    //up bí kíp
                list.add(new ItemMap(zone, 2077, 1, x, player.location.y, player.id));
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159 && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            if (Util.isTrue(10, 100)) {    //up bí kíp
                list.add(new ItemMap(zone, 2036, 1, x, player.location.y, player.id));
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159 && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            if (Util.isTrue(10, 100)) {    //up bí kíp
                list.add(new ItemMap(zone, 2036, 1, x, player.location.y, player.id));
            }
        }
        if (player.setClothes.setGod() && this.zone.map.mapId >= 105 && this.zone.map.mapId <= 111) {
            if (Util.isTrue(10, 100)) {    //up bí kíp
                list.add(new ItemMap(zone, Util.nextInt(663, 667), 1, x, player.location.y, player.id));
            }
        }
        if (player.setClothes.setGod14() && this.zone.map.mapId == 155) {
            if (Util.isTrue(5, 100)) {    //up bí kíp
                list.add(new ItemMap(zone, Util.nextInt(1066, 1070), 1, x, player.location.y, player.id));
            }
        }
        Item item = player.inventory.itemsBody.get(1);
        if (this.zone.map.mapId > 0) {
            if (item.isNotNullItem()) {
                if (item.template.id == 691) {
                    if (Util.isTrue(10, 100)) {    //sự kiện hè by obito
                        list.add(new ItemMap(zone, Util.nextInt(695, 698), 1, x, player.location.y, player.id));
                    }
                } else if (item.template.id != 691 && item.template.id != 692 && item.template.id != 693) {
                    if (Util.isTrue(0, 1)) {
                        list.add(new ItemMap(zone, 76, 1, x, player.location.y, player.id));
                    }
                }
            }
        }
        if (this.zone.map.mapId > 0) {
            if (item.isNotNullItem()) {
                if (item.template.id == 691) {
                    if (Util.isTrue(10, 100)) {
                        list.add(new ItemMap(zone, Util.nextInt(1813, 1815), 1, x, player.location.y, player.id));
                    }
                } else if (item.template.id != 691 && item.template.id != 692 && item.template.id != 693) {
                    if (Util.isTrue(0, 1)) {
                        list.add(new ItemMap(zone, 76, 1, x, player.location.y, player.id));
                    }
                }
            }
        }
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 // các map hoạt động sự kiện hè
                || this.zone.map.mapId >= 146 && this.zone.map.mapId <= 148
                || this.zone.map.mapId >= 53 && this.zone.map.mapId <= 62
                || this.zone.map.mapId >= 141 && this.zone.map.mapId <= 144) {
            if (item.isNotNullItem()) {
                if (item.template.id == 692) {
                    if (Util.isTrue(10, 100)) {    //up bí kíp
                        list.add(new ItemMap(zone, Util.nextInt(695, 698), 1, x, player.location.y, player.id));
                    }
                } else if (item.template.id != 691 && item.template.id != 692 && item.template.id != 693) {
                    if (Util.isTrue(0, 1)) {
                        list.add(new ItemMap(zone, 76, 1, x, player.location.y, player.id));
                    }
                }
            }
        }
        if (this.zone.map.mapId > 0) {
            if (item.isNotNullItem()) {
                if (item.template.id == 693) {
                    if (Util.isTrue(10, 100)) {    //up bí kíp
                        list.add(new ItemMap(zone, Util.nextInt(695, 698), 1, x, player.location.y, player.id));
                    }
                } else if (item.template.id != 691 && item.template.id != 692 && item.template.id != 693) {
                    if (Util.isTrue(0, 1)) {
                        list.add(new ItemMap(zone, 76, 1, x, player.location.y, player.id));
                    }
                }
            }
        }
//        if (this.zone.map.mapId >= 0) {
//            if (Util.isTrue(1, 666)) {
//                list.add(new ItemMap(zone, 2000 + player.gender, 1, x, player.location.y, player.id));
//            }
//     

// rớt sao pha lê
if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 200) {
            if (Util.isTrue(1, 432)) {
                Item tnsm = ItemService.gI().createNewItem((short) (447));
                tnsm.itemOptions.add(new Item.ItemOption(101, 5));
                InventoryServiceNew.gI().addItemBag(player, tnsm);
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }

if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 200) {
            if (Util.isTrue(1, 432)) {
                Item vang = ItemService.gI().createNewItem((short) (446));
                vang.itemOptions.add(new Item.ItemOption(100, 5));
                InventoryServiceNew.gI().addItemBag(player, vang);
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }
if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 200) {
            if (Util.isTrue(1, 432)) {
                Item canchien = ItemService.gI().createNewItem((short) (445));
                canchien.itemOptions.add(new Item.ItemOption(99, 5));
                InventoryServiceNew.gI().addItemBag(player, canchien);
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }
if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 200) {
            if (Util.isTrue(1, 432)) {
                Item truong = ItemService.gI().createNewItem((short) (444));
                truong.itemOptions.add(new Item.ItemOption(98, 5));
                InventoryServiceNew.gI().addItemBag(player, truong);
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }
if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 200) {
            if (Util.isTrue(1, 432)) {
                Item phan = ItemService.gI().createNewItem((short) (443));
                phan.itemOptions.add(new Item.ItemOption(97, 5));
                InventoryServiceNew.gI().addItemBag(player, phan);
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }
if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 200) {
            if (Util.isTrue(1, 432)) {
                Item hutki = ItemService.gI().createNewItem((short) (442));
                hutki.itemOptions.add(new Item.ItemOption(96, 5));
                InventoryServiceNew.gI().addItemBag(player, hutki);
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }
if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 200) {
            if (Util.isTrue(1, 432)) {
                Item huthp = ItemService.gI().createNewItem((short) (441));
                huthp.itemOptions.add(new Item.ItemOption(95, 5));
                InventoryServiceNew.gI().addItemBag(player, huthp);
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }
if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 200) {
            if (Util.isTrue(1, 432)) {
                Item tnsm = ItemService.gI().createNewItem((short) (447));
                tnsm.itemOptions.add(new Item.ItemOption(101, 5));
                InventoryServiceNew.gI().addItemBag(player, tnsm);
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }


//rớt đá nâng cấp
  if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 200) {
            if (Util.isTrue(1, 432)) {
                Item lucbao = ItemService.gI().createNewItem((short) (220));
                InventoryServiceNew.gI().addItemBag(player, lucbao);
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }
    if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 200) {
            if (Util.isTrue(1, 432)) {
                Item saphia = ItemService.gI().createNewItem((short) (221));
                InventoryServiceNew.gI().addItemBag(player, saphia);
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }
      if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 200) {
            if (Util.isTrue(1, 432)) {
                Item ruby = ItemService.gI().createNewItem((short) (222));
                InventoryServiceNew.gI().addItemBag(player, ruby);
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }
        if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 200) {
            if (Util.isTrue(1, 432)) {
                Item titan = ItemService.gI().createNewItem((short) (223));
                InventoryServiceNew.gI().addItemBag(player, titan);
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }
          if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 200) {
            if (Util.isTrue(1, 432)) {
                Item tat = ItemService.gI().createNewItem((short) (224));
                InventoryServiceNew.gI().addItemBag(player, tat);
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }
            if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 200) {
            if (Util.isTrue(1, 321)) {
                Item manhvo = ItemService.gI().createNewItem((short) (225));
                InventoryServiceNew.gI().addItemBag(player, manhvo);
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }

// Rơi thức ăn

     if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 666)) {
                Item pudding = ItemService.gI().createNewItem((short) (663));
              
                InventoryServiceNew.gI().addItemBag(player, pudding);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + pudding.template.name);
            }
        }
          if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 666)) {
                Item xucxich = ItemService.gI().createNewItem((short) (664));
              
                InventoryServiceNew.gI().addItemBag(player, xucxich);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + xucxich.template.name);
            }
        }
               if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 666)) {
                Item kemdau = ItemService.gI().createNewItem((short) (665));
              
                InventoryServiceNew.gI().addItemBag(player, kemdau);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + kemdau.template.name);
            }
        }
                    if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 666)) {
                Item myly = ItemService.gI().createNewItem((short) (666));
               
                InventoryServiceNew.gI().addItemBag(player, myly);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + myly.template.name);
            }
        } 
                        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 666)) {
                Item sushi = ItemService.gI().createNewItem((short) (667));
               
                InventoryServiceNew.gI().addItemBag(player, sushi);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + sushi.template.name);
            }
        }


        //Roi Do Than Cold
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 3000)) {
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (556));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(22, Util.nextInt(55, 65)));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Quanthanlinh.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 3000)) {
                Item Quanthanlinhxd = ItemService.gI().createNewItem((short) (560));
                Quanthanlinhxd.itemOptions.add(new Item.ItemOption(22, Util.nextInt(45, 55)));
                Quanthanlinhxd.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Quanthanlinhxd.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 3000)) {
                Item Quanthanlinhnm = ItemService.gI().createNewItem((short) (558));
                Quanthanlinhnm.itemOptions.add(new Item.ItemOption(22, Util.nextInt(45, 60)));
                Quanthanlinhnm.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinhnm);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Quanthanlinhnm.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 3000)) {
                Item Aothanlinh = ItemService.gI().createNewItem((short) (555));
                Aothanlinh.itemOptions.add(new Item.ItemOption(47, Util.nextInt(500, 600)));
                Aothanlinh.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Aothanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Aothanlinh.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 3000)) {
                Item Aothanlinhnm = ItemService.gI().createNewItem((short) (557));
                Aothanlinhnm.itemOptions.add(new Item.ItemOption(47, Util.nextInt(400, 550)));
                Aothanlinhnm.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Aothanlinhnm);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Aothanlinhnm.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 3000)) {
                Item Aothanlinhxd = ItemService.gI().createNewItem((short) (559));
                Aothanlinhxd.itemOptions.add(new Item.ItemOption(47, Util.nextInt(600, 700)));
                Aothanlinhxd.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Aothanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Aothanlinhxd.template.name);
            }
        }

        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 3000)) {
                Item Gangthanlinh = ItemService.gI().createNewItem((short) (562));
                Gangthanlinh.itemOptions.add(new Item.ItemOption(0, Util.nextInt(6000, 7000)));
                Gangthanlinh.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Gangthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Gangthanlinh.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 3000)) {
                Item Gangthanlinhxd = ItemService.gI().createNewItem((short) (566));
                Gangthanlinhxd.itemOptions.add(new Item.ItemOption(0, Util.nextInt(6500, 7500)));
                Gangthanlinhxd.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Gangthanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Gangthanlinhxd.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 3000)) {
                Item Gangthanlinhnm = ItemService.gI().createNewItem((short) (564));
                Gangthanlinhnm.itemOptions.add(new Item.ItemOption(0, Util.nextInt(5500, 6500)));
                Gangthanlinhnm.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Gangthanlinhnm);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Gangthanlinhnm.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 3000)) {
                Item Giaythanlinh = ItemService.gI().createNewItem((short) (563));
                Giaythanlinh.itemOptions.add(new Item.ItemOption(23, Util.nextInt(50, 60)));
                Giaythanlinh.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Giaythanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Giaythanlinh.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 3000)) {
                Item Giaythanlinhxd = ItemService.gI().createNewItem((short) (567));
                Giaythanlinhxd.itemOptions.add(new Item.ItemOption(23, Util.nextInt(55, 65)));
                Giaythanlinhxd.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Giaythanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Giaythanlinhxd.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 3000)) {
                Item Giaythanlinhnm = ItemService.gI().createNewItem((short) (565));
                Giaythanlinhnm.itemOptions.add(new Item.ItemOption(23, Util.nextInt(65, 75)));
                Giaythanlinhnm.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Giaythanlinhnm);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Giaythanlinhnm.template.name);
            }
        }
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(1, 3000)) {
                Item Nhanthanlinh = ItemService.gI().createNewItem((short) (561));
                Nhanthanlinh.itemOptions.add(new Item.ItemOption(14, Util.nextInt(13, 16)));
                Nhanthanlinh.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
                InventoryServiceNew.gI().addItemBag(player, Nhanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + Nhanthanlinh.template.name);
            }
        }
        if (this.zone.map.mapId >= 53 && this.zone.map.mapId <= 62) {
            if (Util.isTrue(100, 100)) {
                Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, 861, Util.nextInt(10000, 30000), Util.nextInt(this.location.x - 20, this.location.x + 20), zone.map.yPhysicInTop(this.location.x, this.location.y - 24), player.id));
                Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, 861, Util.nextInt(10000, 30000), Util.nextInt(this.location.x - 20, this.location.x + 20), zone.map.yPhysicInTop(this.location.x, this.location.y - 24), player.id));
                Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, 861, Util.nextInt(10000, 30000), Util.nextInt(this.location.x - 20, this.location.x + 20), zone.map.yPhysicInTop(this.location.x, this.location.y - 24), player.id));
            }
        }
        if (this.tempId == 0) {
            player.achievement.plusCount(7);
        }
        if (this.tempId == 7 || this.tempId == 8 || this.tempId == 9 || this.tempId == 10 || this.tempId == 11 || this.tempId == 12) {
            player.achievement.plusCount(6);
        }
        return list;
    }

    private ItemMap dropItemTask(Player player) {
        ItemMap itemMap = null;
        switch (this.tempId) {
            case ConstMob.KHUNG_LONG:
            case ConstMob.LON_LOI:
            case ConstMob.QUY_DAT:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_2_0) {
                    itemMap = new ItemMap(this.zone, 73, 1, this.location.x, this.location.y, player.id);
                }
                break;
        }
        if (itemMap != null) {
            return itemMap;
        }
        return null;
    }

    private void sendMobStillAliveAffterAttacked(double dameHit, boolean crit) {
        Message msg;
        try {
            msg = new Message(-9);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(this.point.gethp());
            msg.writer().writeInt(Util.TamkjllGH(dameHit));
            msg.writer().writeBoolean(crit); // chí mạng
            msg.writer().writeInt(-1);
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }
    public short cx;
    public short cy;
    public int Gio;
    public int action = 0;
    public static int TIME_START_HIRU = 23;

    private boolean isHaveEffectSkill() {
        return effectSkill.isAnTroi || effectSkill.isBlindDCTT || effectSkill.isStun || effectSkill.isThoiMien
                || effectSkill.isSocola  || effectSkill.isHoaLanh;
    }
    private void BigbossAttack() {
        if (!isDie() && !isHaveEffectSkill() && Util.canDoWithTime(lastTimeAttackPlayer, 1000)) {
            Message msg = null;
            try {
                switch (tempId) {
                    case 70: // Hirudegarn
                        if (!Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
                            return;
                        }
                        if (this.isDie()) {
                            return;
                        }
                        // 0: bắn - 1: Quật đuôi - 2: dậm chân - 3: Bay - 4: tấn công - 5: Biến hình -
                        // 6: Biến hình lên cấp
                        // 7: vận chiêu - 8: Di chuyển - 9: Die
                        int[] idAction = new int[] { 1, 2, 3, 7 };
                        if (this.level >= 2) {

                            idAction = new int[] { 1, 2 };
                        }
                        action = action == 7 ? 0 : idAction[Util.nextInt(0, idAction.length - 1)];
                        int index = Util.nextInt(0, zone.getPlayers().size() - 1);
                        Player player = zone.getPlayers().get(index);
                        if (player == null || player.isDie()) {
                            return;
                        }
                        if (action == 1) {
                            location.x = (short) player.location.x;
                            Service.getInstance().sendBigBoss2(zone, 8, this);
                        }
                        msg = new Message(101);
                        msg.writer().writeByte(action);
                        if (action >= 0 && action <= 4) {
                            if (action == 1) {
                                msg.writer().writeByte(1);
                                int dame = (int) player.injured(player, (int) this.point.getDameAttack(), false, true);
                                if (dame <= 0) {
                                    dame = 1;
                                }

                                msg.writer().writeInt((int) player.id);
                                msg.writer().writeInt(dame);
                            } else if (action == 3) {
                                location.x = (short) player.location.x;
                                msg.writer().writeShort(location.x);
                                msg.writer().writeShort(location.y);
                            } else {
                                msg.writer().writeByte(zone.getHumanoids().size());
                                for (int i = 0; i < zone.getHumanoids().size(); i++) {
                                    Player pl = zone.getHumanoids().get(i);
                                    int dame = (int) player.injured(player, (int) this.point.getDameAttack(), false,
                                            true);
                                    if (dame <= 0) {
                                        dame = 1;
                                    }

                                    msg.writer().writeInt((int) pl.id);
                                    msg.writer().writeInt(dame);
                                }
                            }
                        } else {
                            if (action == 6 || action == 8) {
                                location.x = (short) player.location.x;
                                msg.writer().writeShort(location.x);
                                msg.writer().writeShort(location.y);
                            }
                        }
                        Service.getInstance().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();
                        break;
                    case 71: // Vua Bạch Tuộc
                        int[] idAction2 = new int[] { 3, 4, 5 };
                        action = action == 7 ? 0 : idAction2[Util.nextInt(0, idAction2.length - 1)];
                        int index2 = Util.nextInt(0, zone.getPlayers().size() - 1);
                        Player player2 = zone.getPlayers().get(index2);
                        if (player2 == null || player2.isDie()) {
                            return;
                        }
                        msg = new Message(102);
                        msg.writer().writeByte(action);
                        if (action >= 0 && action <= 5) {
                            if (action != 5) {
                                msg.writer().writeByte(1);
                                int dame = (int) player2.injured(player2, (int) this.point.getDameAttack(), false,
                                        true);
                                if (dame <= 0) {
                                    dame = 1;
                                }
                                msg.writer().writeInt((int) player2.id);
                                msg.writer().writeInt(dame);
                            }
                            if (action == 5) {
                                location.x = (short) player2.location.x;
                                msg.writer().writeShort(location.x);
                            }
                        } else {

                        }
                        Service.getInstance().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();
                        break;
                    case 72: // Rôbốt bảo vệ
                        int[] idAction3 = new int[] { 0, 1, 2,7 };
                        action = action == 7 ? 0 : idAction3[Util.nextInt(0, idAction3.length - 1)];
                        int index3 = Util.nextInt(0, zone.getPlayers().size() - 1);
                        Player player3 = zone.getPlayers().get(index3);
                        if (player3 == null || player3.isDie()) {
                            return;
                        }
                        msg = new Message(102);
                        msg.writer().writeByte(action);
                        if (action >= 0 && action <= 2) {
                            msg.writer().writeByte(1);
                            int dame = (int) player3.injured(player3, (int) this.point.getDameAttack(), false, true);
                            if (dame <= 0) {
                                dame = 1;
                            }
                            msg.writer().writeInt((int) player3.id);
                            msg.writer().writeInt(dame);
                        }
                        Service.getInstance().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();
                        break;
                }
            } catch (Exception e) {
                // Util.debug("ERROR BIG BOSS");
            } finally {
                if (msg != null) {
                    msg.cleanup();
                    msg = null;
                }
            }
        }
    }
}
