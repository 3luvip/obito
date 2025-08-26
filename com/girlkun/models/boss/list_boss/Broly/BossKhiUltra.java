package com.girlkun.models.boss.list_boss.Broly;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.list_boss.android.*;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Client;
import com.girlkun.server.ServerNotify;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.SkillService;
import com.girlkun.services.TaskService;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class BossKhiUltra extends Boss {

    private long lastTimeHapThu;
    private int timeHapThu;

    public BossKhiUltra() throws Exception {

        super(BossID.BOSS_KHI_ULTRA, BossesData.BOSS_KHI_ULTRA);
    }

    public List<Player> PlayerPlAtt = new ArrayList<>();

    public void addPlayerPlAtt(Player pl) {
        if (!this.PlayerPlAtt.contains(pl) && pl.isPl()) {
            PlayerPlAtt.add(pl);
        }
    }

    public int gethour() {
        Calendar rightNow = Calendar.getInstance();
        return rightNow.get(11);
    }

    public String df(double sfm) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(sfm);
    }

    public void reward(Player plKill) {
        if (this.gethour() >= 21 && this.gethour() <= 23) { // Sửa ở đây

            int a = 0;
            for (int i = 0; i < 20; i++) {
                ItemMap it1 = new ItemMap(this.zone, 1400, 1, this.location.x + a, this.zone.map.yPhysicInTop(this.location.x,
                        this.location.y - 24), -1);
                Service.getInstance().dropItemMap(this.zone, it1);
                a += 30;
            }

            if (plKill != null) {
//                plKill.inventory.ruby += 100000;
//                plKill.inventory.gem += 100000;
//                Service.gI().sendMoney(plKill);
                if (plKill.DucNTdamethanmeo > 500000000) {
                    plKill.DucNTdamethanmeo = 500000000;
                }

                double ptnhanquaKill = plKill.DucNTdamethanmeo * 100 / 500000000;

                if (ptnhanquaKill > 99) {
                    ptnhanquaKill = 99;
                }
                Item capsuleTT = new Item();
                capsuleTT.template = ItemService.gI().getTemplate(1409);
                capsuleTT.quantity = 20;
                InventoryServiceNew.gI().addItemBag(plKill, capsuleTT);
                Item Duoikhi = new Item();
                Duoikhi.template = ItemService.gI().getTemplate(1400);
                Duoikhi.quantity = 10;
                InventoryServiceNew.gI().addItemBag(plKill, Duoikhi);
                InventoryServiceNew.gI().sendItemBags(plKill);
                Service.gI().sendThongBaoOK(plKill, "\nBạn nhận được 10 Đuôi Khỉ và 20 Capsule Trung Thu do kết liễu Boss.");
                plKill.DucNTdamethanmeo = 0;
            }

            for (int i = 0; i < this.PlayerPlAtt.size(); i++) {
                Player pl = this.PlayerPlAtt.get(i);
                if (pl != null && pl.zone != null && pl.zone.map.mapId == 27
                        && pl.DucNTdamethanmeo > 0) {
                    if (pl.DucNTdamethanmeo > 500000000) {
                        pl.DucNTdamethanmeo = 500000000;
                    }
                    double ptnhanqua = pl.DucNTdamethanmeo * 100 / 500000000;
                    if (ptnhanqua < 0) {
                        ptnhanqua = 0;
                    }
                    if (ptnhanqua > 99) {
                        ptnhanqua = 99;
                    }
                    if (ptnhanqua >= 5 && ptnhanqua <= 99) {
//                        Item tv = new Item();
//                        tv.template = ItemService.gI().getTemplate(457);
//                        tv.quantity = (int) (ptnhanqua * 10);
//                        InventoryServiceNew.gI().addItemBag(pl, tv);
//                        Item DaPhaLe = new Item();
//                        DaPhaLe.template = ItemService.gI().getTemplate(1992);
//                        DaPhaLe.quantity = (int) (ptnhanqua * 0.5);
//                        InventoryServiceNew.gI().addItemBag(pl, DaPhaLe);
                        Item Duoikhi = new Item();
                        Duoikhi.template = ItemService.gI().getTemplate(1400);
                        Duoikhi.quantity = 2;
                        InventoryServiceNew.gI().addItemBag(pl, Duoikhi);
                        InventoryServiceNew.gI().sendItemBags(pl);
                        Service.gI().sendThongBaoOK(pl, "Bạn nhận được 2 đuôi khỉ thưởng tham gia sự kiện ");
                        pl.DucNTdamethanmeo = 0;

                    } else {
                        pl.inventory.ruby += 10000;
                        pl.inventory.gem += 10000;
                        Service.gI().sendThongBaoOK(pl,
                                "Bạn đã gây " + this.df(ptnhanqua) + "% HP boss\n vì dưới 5% Bạn nhận được:\n"
                                + "10K ngọc thưởng tham gia");
                    }
                    this.PlayerPlAtt.remove(pl);
                }
            }
            Client.gI().getPlayers().forEach(player -> {
                if (player.DucNTdamethanmeo > 0) {
                    player.DucNTdamethanmeo = 0;
                }
            });
        }
        this.PlayerPlAtt.clear();
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (this.gethour() >= 21 && this.gethour() <= 23) { // Sửa ở đây

            if (!this.isDie()) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        int hpHoi = (int) ((long) damage * 80 / 100);
                        PlayerService.gI().hoiPhuc(this, hpHoi, 0);
                        if (Util.isTrue(1, 5)) {
                            this.chat("Hấp thụ.. các ngươi nghĩ sao vậy?");
                        }
                        return 0;
                }
                if (this.gethour() < 21 && this.gethour() > 23) { // Sửa ở đây
                    return 0;
                }
                if (damage < 5000) {
                    return 0;
                }
                if (damage > 1_000_000_000) {
                    damage = 1_000_000_000;
                }
                damage = this.nPoint.subDameInjureWithDeff(damage / 3);
                if (!piercing && effectSkill.isShielding) {
                    if (damage > nPoint.hpMax) {
                        EffectSkillService.gI().breakShield(this);
                    }
                    damage = 1;
                }
                if (plAtt != null && plAtt.isPl()) {
                    this.addPlayerPlAtt(plAtt);
                    plAtt.DucNTdamethanmeo += damage;
                }
                this.nPoint.subHP(damage);
                if (isDie()) {
                    this.setDie(this);
                    if (plAtt != null) {
                        reward(plAtt);
                        ServerNotify.gI().notify(plAtt.name + " vừa tiêu diệt được " + this.name
                                + " nhận được 100K ngọc");
                    }
                    this.changeStatus(BossStatus.DIE);
                }
                return damage;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.attack();
        this.hapThu();
    }

//    @Override
//    public void leaveMap() {
//        super.leaveMap();
//        BossManager.gI().removeBoss(this);
//        this.dispose();
//    }
    @Override
    public void joinMap() {
        super.joinMap(); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL
                && (this.gethour() >= 21 && this.gethour() <= 23)) { // Sửa ở đây
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie()) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills
                        .get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(5, 20)) {
                        if (SkillUtil.isUseSkillChuong(this)) {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
                        } else {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));
                        }
                    }
                    SkillService.gI().useSkillboss(this, pl, null);
                    checkPlayerDie(pl);
                } else {
                    if (Util.isTrue(1, 2)) {
                        this.moveToPlayer(pl);
                    }
                }
            } catch (Exception ex) {
            }
        }
    }

    private void hapThu() {

        if (Util.isTrue(5, 100)) {
            Player pl = this.zone.getRandomPlayerInMap();
            if (pl == null || pl.isDie()) {
                return;
            }
            int hpHoi = (int) ((long) pl.nPoint.hp * 80 / 100);
            PlayerService.gI().hoiPhuc(this, hpHoi, 0);

            this.nPoint.dameg += (pl.nPoint.dame * 5 / 100);
            this.nPoint.hpg += (pl.nPoint.hp * 10 / 100);
            this.nPoint.critg++;
            this.nPoint.calPoint();
            PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);
            pl.injured(null, pl.nPoint.hpMax, true, false);
            Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " Nuốt chửng!");
            this.chat(2, "Ui cha cha, kinh dị quá. " + pl.name + " vừa bị tên " + this.name + " nuốt chửng kìa!!!");
            this.chat("Haha, ngọt lắm đấy " + pl.name + "..");
            this.lastTimeHapThu = System.currentTimeMillis();
            this.timeHapThu = Util.nextInt(7000000, 15000000);
        }
    }

}
