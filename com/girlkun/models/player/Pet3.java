package com.girlkun.models.player;

import com.girlkun.models.item.Item;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.girlkun.models.matches.TYPE_LOSE_PVP;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Client;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.FriendAndEnemyService;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.MapService;

import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 *
 * @Stole By BTH
 */
public class Pet3 extends Player {

    public Player master;
    public short body;
    public short leg;
    public int congduk;
    public Map maptg;
    public boolean isvip;
    public static int idb = 5000000;

    public Pet3(Player master, short h, short b, short l) {
        this.master = master;
        this.isPet3 = true;
        this.id = idb;
        idb--;
        this.head = h;
        this.body = b;
        this.leg = l;
        this.congduk = Util.nextInt(1, 4);
    }

    @Override
    public short getHead() {
        return head;
    }

    @Override
    public short getBody() {
        return body;
    }

    @Override
    public short getLeg() {
        return leg;
    }

    public void joinMapMaster() {
        if (master == null) {
            return;
        }
        this.location.x = master.location.x + Util.nextInt(-10, 10);
        this.location.y = master.location.y;

        if (master.location.x - this.location.x <= 250) {
            ChangeMapService.gI().goToMap(this, master.zone);

        }
        if (this.zone != null) {
            this.zone.load_Me_To_Another(this);
        }
    }

    private long lastTimeMoveIdle;
    private int timeMoveIdle;
    public boolean idle;

    private void moveIdle() {
        if (idle && Util.canDoWithTime(lastTimeMoveIdle, timeMoveIdle)) {
            int dir = this.location.x - master.location.x <= 0 ? -1 : 1;
            PlayerService.gI().playerMove(this, master.location.x
                    + Util.nextInt(dir == -1 ? 30 : -50, dir == -1 ? 50 : 30), master.location.y);
            lastTimeMoveIdle = System.currentTimeMillis();
            timeMoveIdle = Util.nextInt(5000, 8000);
        }
    }

    @Override
    public void update() {
        if (this.isDie()) {
            //        Service.getInstance().hsChar(this, nPoint.hpMax, nPoint.mpMax);
            if (this != null && this.master != null) {
                Service.gI().sendThongBaoOK(this.master, "Đường tăng đã chết\nNhiệm vụ thất bại!");
                Service.getInstance().chat(this, "Chết mẹ thầy rồi");
                Service.getInstance().chat(this, "Sao mày ngu thế hả?");
                Service.gI().hsChar(this, nPoint.hpMax, nPoint.mpMax);
                ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
                ChangeMapService.gI().exitMap(this);
                this.dispose();

            }

            return;
        }
        super.update();
        if (master != null) {
            followMaster();
        }

        if (master != null && (this.zone == null || this.zone != master.zone) && !this.isDie()) {
            joinMapMaster();
        }
        if (master != null && master.isDie() && this.isDie()) {
            return;
        }
        //      moveIdle();
    }

    public void followMaster() {
        followMaster(50);
    }

    private void followMaster(int dis) {
        if (this.master != null) {
            int mX = master.location.x;
            int mY = master.location.y;
            int distance = (int) Math.sqrt(Math.pow(mX - this.location.x, 2) + Math.pow(mY - this.location.y, 2));

            if (distance >= 20 && distance <= 250) {
                int disX = this.location.x - mX;
                int disX1 = mX - this.location.x;

                if (disX < 0) {
                    this.location.x = this.location.x + 20;
                }
                if (disX1 < 0) {
                    this.location.x = this.location.x - 20;
                }
                if (disX < 0) {
                    //    Service.getInstance().sendThongBao(this.master, "Khoảng cách với sư phụ: " + (disX * -1) + "/300");            
                    this.location.y = this.zone.map.yPhysicInTop(this.location.x,
                            mY);
                } else {
                    //    Service.getInstance().sendThongBao(this.master, "Khoảng cách với sư phụ: " + disX  + "/300");            
                }
                PlayerService.gI().playerMove(this, this.location.x, this.location.y);
            } else if (distance > 250) {
                this.nPoint.hp = 0;
                Service.gI().sendThongBaoOK(this.master, "Đường tăng đã bị lạc\nNhiệm vụ thất bại!");
                this.master.zone.load_Me_To_Another(this.master);
            }
            if (master.zone.map == this.maptg) {
                this.master.congduc += this.congduk;
                updatekimco();
                Service.gI().sendThongBaoOK(this.master, "Hộ tống thành công, bạn nhận được " + this.congduk + " điểm công đức");
                Service.getInstance().chat(this, "Giỏi lắm đồ đệ của ta!");
                Service.gI().hsChar(this, nPoint.hpMax, nPoint.mpMax);
                ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
                ChangeMapService.gI().exitMap(this);
                this.dispose();
                Client.gI().kickSession(master.getSession());
            }
        }
    }

    public void updatekimco() {

        for (Item io : this.master.inventory.itemsBody) {
            if (io.isNotNullItem()) {
                if (io.template.id == 543) {

                    io.itemOptions.get(0).param = master.congduc;
                    //                           InventoryServiceNew.gI().sendItemBody(player);

                    break;
                }
            }
        }

        for (Item item : this.master.inventory.itemsBag) {
            if (item.isNotNullItem()) {
                if (item.template.id == 543) {

                    item.itemOptions.get(0).param = master.congduc;
                    //                                   InventoryServiceNew.gI().sendItemBags(player);
                    break;
                }
            }
        }

        for (Item item : this.master.inventory.itemsBox) {
            if (item.isNotNullItem()) {
                if (item.template.id == 543) {

                    item.itemOptions.get(0).param = master.congduc;
                    //                                   InventoryServiceNew.gI().sendItemBags(player);
                    break;
                }
            }
        }
        InventoryServiceNew.gI().sendItemBags(master);
        InventoryServiceNew.gI().sendItemBags(master);
        InventoryServiceNew.gI().sendItemBody(master);
        Service.gI().point(this.master);

    }

    @Override
    protected void setDie(Player plAtt) {
        //xóa phù
        if (this.effectSkin.xHPKI > 1) {
            this.effectSkin.xHPKI = 1;
            Service.getInstance().point(this);
        }
        //xóa tụ skill đặc biệt
        this.playerSkill.prepareQCKK = false;
        this.playerSkill.prepareLaze = false;
        this.playerSkill.prepareTuSat = false;
        //xóa hiệu ứng skill
        this.effectSkill.removeSkillEffectWhenDie();
        //
        nPoint.setHp(0);
        nPoint.setMp(0);
        //xóa trứng
        if (this.mobMe != null) {
            this.mobMe.mobMeDie();
        }
        Service.getInstance().charDie(this);
        //add kẻ thù
        if (!this.isPet && !this.isNewPet && newpet != null && !this.isPet3 && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isPet3 && !plAtt.isNewPet && !plAtt.isBoss) {
            if (!plAtt.itemTime.isUseAnDanh) {
                FriendAndEnemyService.gI().addEnemy(this, plAtt);
            }
        }
        //kết thúc pk
        if (this.pvp != null) {
            this.pvp.lose(this, TYPE_LOSE_PVP.DEAD);
        }
//        PVPServcice.gI().finishPVP(this, PVP.TYPE_DIE);
        BlackBallWar.gI().dropBlackBall(this);
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        if (this.nPoint.voHieuChuong > 0) {
                            //                          PlayerService.gI().hoiPhuc(this, 0, damage * this.nPoint.voHieuChuong / 100);
                            return 0;
                        }
                }
            }
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (isMobAttack && this.charms.tdBatTu > System.currentTimeMillis() && damage >= this.nPoint.hp) {
                damage = this.nPoint.hp - 1;
            }

            this.nPoint.subHP(Util.nextInt(2000, 5000));
            if (isDie()) {
                if (this.zone.map.mapId == 112) {
                    plAtt.pointPvp++;
                }
                setDie(plAtt);
            }
            return (short) Util.nextInt(2000, 5000);
        } else {
            return 0;
        }
    }

    @Override
    public void dispose() {
        this.master = null;
        if (this.master != null && this.master.pet3 != null) {
            this.master.pet3 = null;
        }
        super.dispose();
    }
}
