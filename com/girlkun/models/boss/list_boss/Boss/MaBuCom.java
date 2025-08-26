package com.girlkun.models.boss.list_boss.Boss;

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
import com.girlkun.server.Manager;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MaBuCom extends Boss {

    private long lastTimeHapThu;
    private int timeHapThu;

    public MaBuCom() throws Exception {

        super(BossID.BOSS_MA_BU_COM, BossesData.BOSS_MA_BU_COM);
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

        int randomMSKH = (int) new Random().nextInt(Manager.itemIds_MANH_SKH.length);

        List<Integer> AoTL = Arrays.asList(555, 557, 559);
        int randomAoTL = AoTL.get(Util.nextInt(0, 2));

        List<Integer> QuanTL = Arrays.asList(556, 558, 560);
        int randomQuanTL = QuanTL.get(Util.nextInt(0, 2));

        List<Integer> GangTL = Arrays.asList(562, 564, 566);
        int randomGangTL = GangTL.get(Util.nextInt(0, 2));

        List<Integer> GiayTL = Arrays.asList(563, 565, 567);
        int randomGiayTL = GiayTL.get(Util.nextInt(0, 2));

        int NhanTL = 561;

        if (Util.isTrue(100, 100)) {
            if (Util.isTrue(20, 100)) {
                Service.gI().dropItemMap(this.zone, new ItemMap(zone, 1359, Util.nextInt(1, 2), this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
            } else if (Util.isTrue(10, 100)) { // Áo
                ItemMap it = new ItemMap(this.zone, randomAoTL, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
                if (randomAoTL == 555) {
                    int ramdomcs = Util.nextInt(960, 1152);
                    int danhgiacs = ramdomcs * 100 / 640;
                    it.options.add(new Item.ItemOption(47, ramdomcs));
                    it.options.add(new Item.ItemOption(21, 15));
                    it.options.add(new Item.ItemOption(86, 1));
                    it.options.add(new Item.ItemOption(209, 15));
                    it.options.add(new Item.ItemOption(45, danhgiacs));
                    Service.getInstance().dropItemMap(this.zone, it);
                } else if (randomAoTL == 557) {
                    int ramdomcs = Util.nextInt(960, 1152);
                    int danhgiacs = ramdomcs * 100 / 640;
                    it.options.add(new Item.ItemOption(47, ramdomcs));
                    it.options.add(new Item.ItemOption(21, 15));
                    it.options.add(new Item.ItemOption(86, 1));
                    it.options.add(new Item.ItemOption(209, 15));
                    it.options.add(new Item.ItemOption(45, danhgiacs));
                    Service.getInstance().dropItemMap(this.zone, it);
                } else {
                    int ramdomcs = Util.nextInt(1260, 1512);
                    int danhgiacs = ramdomcs * 100 / 720;
                    it.options.add(new Item.ItemOption(47, ramdomcs));
                    it.options.add(new Item.ItemOption(21, 15));
                    it.options.add(new Item.ItemOption(86, 1));
                    it.options.add(new Item.ItemOption(209, 15));
                    it.options.add(new Item.ItemOption(45, danhgiacs));
                    Service.getInstance().dropItemMap(this.zone, it);
                }
            } else if (Util.isTrue(10, 100)) { //Quần
                ItemMap it = new ItemMap(this.zone, randomQuanTL, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
                if (randomAoTL == 556) {
                    int ramdomcs = Util.nextInt(832, 998);
                    int ramdomcs2 = Util.nextInt(10000, 12000);
                    int danhgiacs = ramdomcs * 100 / 416;
                    it.options.add(new Item.ItemOption(43, ramdomcs));
                    it.options.add(new Item.ItemOption(27, ramdomcs2));
                    it.options.add(new Item.ItemOption(21, 15));
                    it.options.add(new Item.ItemOption(86, 1));
                    it.options.add(new Item.ItemOption(209, 15));
                    it.options.add(new Item.ItemOption(45, danhgiacs));
                    Service.getInstance().dropItemMap(this.zone, it);
                } else if (randomAoTL == 558) {
                    int ramdomcs = Util.nextInt(600, 720);
                    int ramdomcs2 = Util.nextInt(7050, 8460);
                    int danhgiacs = ramdomcs * 100 / 400;
                    it.options.add(new Item.ItemOption(43, ramdomcs));
                    it.options.add(new Item.ItemOption(27, ramdomcs2));
                    it.options.add(new Item.ItemOption(21, 15));
                    it.options.add(new Item.ItemOption(86, 1));
                    it.options.add(new Item.ItemOption(209, 15));
                    it.options.add(new Item.ItemOption(45, danhgiacs));
                    Service.getInstance().dropItemMap(this.zone, it);
                } else {
                    int ramdomcs = Util.nextInt(576, 691);
                    int ramdomcs2 = Util.nextInt(6900, 8280);
                    int danhgiacs = ramdomcs * 100 / 384;
                    it.options.add(new Item.ItemOption(43, ramdomcs));
                    it.options.add(new Item.ItemOption(27, ramdomcs2));
                    it.options.add(new Item.ItemOption(21, 15));
                    it.options.add(new Item.ItemOption(86, 1));
                    it.options.add(new Item.ItemOption(209, 15));
                    it.options.add(new Item.ItemOption(45, danhgiacs));
                    Service.getInstance().dropItemMap(this.zone, it);
                }
            } else if (Util.isTrue(5, 100)) { //Găng
                ItemMap it = new ItemMap(this.zone, randomGangTL, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
                if (randomAoTL == 562) {
                    int ramdomcs = Util.nextInt(6160, 7392);
                    int danhgiacs = ramdomcs * 100 / 3520;
                    it.options.add(new Item.ItemOption(0, ramdomcs));
                    it.options.add(new Item.ItemOption(21, 17));
                    it.options.add(new Item.ItemOption(86, 1));
                    it.options.add(new Item.ItemOption(209, 15));
                    it.options.add(new Item.ItemOption(45, danhgiacs));
                    Service.getInstance().dropItemMap(this.zone, it);
                } else if (randomAoTL == 564) {
                    int ramdomcs = Util.nextInt(6020, 7224);
                    int danhgiacs = ramdomcs * 100 / 3440;
                    it.options.add(new Item.ItemOption(0, ramdomcs));
                    it.options.add(new Item.ItemOption(21, 17));
                    it.options.add(new Item.ItemOption(86, 1));
                    it.options.add(new Item.ItemOption(209, 15));
                    it.options.add(new Item.ItemOption(45, danhgiacs));
                    Service.getInstance().dropItemMap(this.zone, it);
                } else {
                    int ramdomcs = Util.nextInt(7200, 8640);
                    int danhgiacs = ramdomcs * 100 / 3600;
                    it.options.add(new Item.ItemOption(0, ramdomcs));
                    it.options.add(new Item.ItemOption(21, 17));
                    it.options.add(new Item.ItemOption(86, 1));
                    it.options.add(new Item.ItemOption(209, 15));
                    it.options.add(new Item.ItemOption(45, danhgiacs));
                    Service.getInstance().dropItemMap(this.zone, it);
                }
            } else if (Util.isTrue(10, 100)) { //Giày
                ItemMap it = new ItemMap(this.zone, randomGiayTL, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
                if (randomAoTL == 563) {
                    int ramdomcs = Util.nextInt(576, 691);
                    int ramdomcs2 = Util.nextInt(5250, 6300);
                    int danhgiacs = ramdomcs * 100 / 384;
                    it.options.add(new Item.ItemOption(44, ramdomcs));
                    it.options.add(new Item.ItemOption(28, ramdomcs2));
                    it.options.add(new Item.ItemOption(21, 15));
                    it.options.add(new Item.ItemOption(86, 1));
                    it.options.add(new Item.ItemOption(209, 15));
                    it.options.add(new Item.ItemOption(45, danhgiacs));
                    Service.getInstance().dropItemMap(this.zone, it);
                } else if (randomAoTL == 565) {
                    int ramdomcs = Util.nextInt(800, 960);
                    int ramdomcs2 = Util.nextInt(7400, 8880);
                    int danhgiacs = ramdomcs * 100 / 400;
                    it.options.add(new Item.ItemOption(44, ramdomcs));
                    it.options.add(new Item.ItemOption(28, ramdomcs2));
                    it.options.add(new Item.ItemOption(21, 15));
                    it.options.add(new Item.ItemOption(86, 1));
                    it.options.add(new Item.ItemOption(209, 15));
                    it.options.add(new Item.ItemOption(45, danhgiacs));
                    Service.getInstance().dropItemMap(this.zone, it);
                } else {
                    int ramdomcs = Util.nextInt(552, 662);
                    int ramdomcs2 = Util.nextInt(5700, 6840);
                    int danhgiacs = ramdomcs * 100 / 368;
                    it.options.add(new Item.ItemOption(44, ramdomcs));
                    it.options.add(new Item.ItemOption(28, ramdomcs2));
                    it.options.add(new Item.ItemOption(21, 15));
                    it.options.add(new Item.ItemOption(86, 1));
                    it.options.add(new Item.ItemOption(209, 15));
                    it.options.add(new Item.ItemOption(45, danhgiacs));
                    Service.getInstance().dropItemMap(this.zone, it);
                }
            } else if (Util.isTrue(5, 100)) { //Nhẫn
                ItemMap it = new ItemMap(this.zone, NhanTL, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
                int ramdomcs = Util.nextInt(18, 22);
                int danhgiacs = ramdomcs * 100 / 15;
                it.options.add(new Item.ItemOption(14, ramdomcs));
                it.options.add(new Item.ItemOption(21, 15));
                it.options.add(new Item.ItemOption(86, 1));
                it.options.add(new Item.ItemOption(209, 15));
                it.options.add(new Item.ItemOption(45, danhgiacs));
                Service.getInstance().dropItemMap(this.zone, it);
            } else {
                Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_MANH_SKH[randomMSKH], Util.nextInt(8, 12), this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
            }
        }
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (Util.isTrue(30, 100) && plAtt != null) {//tỉ lệ hụt của thiên sứ
            Util.isTrue(this.nPoint.tlNeDon, 10000000);
            int hpHoi = Util.nextInt(1000000, 5000000);
            PlayerService.gI().hoiPhuc(this, hpHoi, 0);
            this.nPoint.dameg += 10000;
            if (Util.isTrue(50, 100)) {
                this.chat("Hãy để bản năng tự vận động");
                this.chat("Tránh các động tác thừa");
            } else if (Util.isTrue(50, 100)) {
                this.chat("Chậm lại,các ngươi quá nhanh rồi");
                this.chat("Chỉ cần hoàn thiện nó!");
                this.chat("Các ngươi sẽ tránh được mọi nguy hiểm");
            } else if (Util.isTrue(50, 100)) {
                this.chat("Đây chính là bản năng vô cực");
            }
            damage = 0;

        }
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage / 3);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
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
    }

    @Override
    public void joinMap() {
        super.joinMap(); // To change body of generated methods, choose Tools | Templates.
    }

}
