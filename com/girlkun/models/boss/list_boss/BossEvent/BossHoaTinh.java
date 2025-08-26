package com.girlkun.models.boss.list_boss.BossEvent;

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
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class BossHoaTinh extends Boss {

    private long lastTimeHapThu;
    private int timeHapThu;

    public BossHoaTinh() throws Exception {

        super(BossID.BOSS_HOA_TINH, BossesData.BOSS_HOA_TINH);
        this.tilepst = 50; // Tỉ lệ PST 
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

        int a = 0;
        for (int i = 0; i < 10; i++) {
            ItemMap it1 = new ItemMap(this.zone, 1414, 1, this.location.x + a, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), -1);
            Service.getInstance().dropItemMap(this.zone, it1);
            a += 30;
        }

        for (int i = 0; i < 5; i++) {
            ItemMap it2 = new ItemMap(this.zone, 1416, 1, this.location.x + a, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), -1);
            Service.getInstance().dropItemMap(this.zone, it2);
            a += 30;
        }

        if (plKill != null) {

            if (plKill.DucNTdamethanmeo > 750_000_000) {
                plKill.DucNTdamethanmeo = 750_000_000;
            }

            double ptnhanquaKill = plKill.DucNTdamethanmeo / 750_000_000;

            if (ptnhanquaKill > 99) {
                ptnhanquaKill = 99;
            }
            Item capsuleTT = new Item();
            capsuleTT.template = ItemService.gI().getTemplate(1416);
            capsuleTT.quantity = 5;
            InventoryServiceNew.gI().addItemBag(plKill, capsuleTT);
            Item Duoikhi = new Item();
            Duoikhi.template = ItemService.gI().getTemplate(1414);
            Duoikhi.quantity = 10;
            InventoryServiceNew.gI().addItemBag(plKill, Duoikhi);
            InventoryServiceNew.gI().sendItemBags(plKill);
            Service.gI().sendThongBaoOK(plKill, "\nBạn nhận được 10 Hộp Quà Phái Đẹp và 5 Long Châu do kết liễu Boss.");
            plKill.DucNTdamethanmeo = 0;
        }

        for (int i = 0; i < this.PlayerPlAtt.size(); i++) {
            Player pl = this.PlayerPlAtt.get(i);
            if (pl != null && pl.zone != null && pl.zone.map.mapId == 184
                    && pl.DucNTdamethanmeo > 0) {
                if (pl.DucNTdamethanmeo > 750_000_000) {
                    pl.DucNTdamethanmeo = 750_000_000;
                }
                double ptnhanqua = pl.DucNTdamethanmeo / 750_000_000;
                if (ptnhanqua < 0) {
                    ptnhanqua = 0;
                }
                if (ptnhanqua > 99) {
                    ptnhanqua = 99;
                }
                if (ptnhanqua >= 1 && ptnhanqua <= 99) {

                    Item Duoikhi = new Item();
                    Duoikhi.template = ItemService.gI().getTemplate(1414);
                    Duoikhi.quantity = 5;
                    InventoryServiceNew.gI().addItemBag(pl, Duoikhi);
                    InventoryServiceNew.gI().sendItemBags(pl);
                    Service.gI().sendThongBaoOK(pl, "Bạn nhận được 5 Hộp Quà Phái Đẹp thưởng tham gia sự kiện ");
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
        this.PlayerPlAtt.clear();
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }

//            if(Util.isTrue(20, 100)){
//                int hpHoi = (int) ((long) damage);
//                PlayerService.gI().hoiPhuc(this, hpHoi, 0);
//                if (Util.isTrue(5, 5)) {
//                    this.chat("Nảy Nảy ... Ngươi không hạ gục được ta đâu! ");
//                    }
//
//            }            
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
