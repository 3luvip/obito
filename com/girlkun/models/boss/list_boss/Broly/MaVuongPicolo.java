package com.girlkun.models.boss.list_boss.Broly;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.list_boss.android.*;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
////import com.girlkun.models.boss.BossManager;
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

public class MaVuongPicolo extends Boss {

    private long lastTimeHapThu;
    private int timeHapThu;

    public MaVuongPicolo() throws Exception {

        super(BossID.BOSS_MA_VUONG_PICOLO, BossesData.BOSS_MA_VUONG_PICOLO);
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

        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, 1653, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            if (Util.isTrue(20, 100)) {
                it.options.add(new Item.ItemOption(224, Util.nextInt(7, 10)));
                it.options.add(new Item.ItemOption(225, 0));
                it.options.add(new Item.ItemOption(226, 0));
            } else {
                it.options.add(new Item.ItemOption(224, Util.nextInt(3, 6)));
                it.options.add(new Item.ItemOption(225, 0));
                it.options.add(new Item.ItemOption(226, 0));
            }

            int diembitich = Util.nextInt(150, 250);
            plKill.diembitich += diembitich;
            Service.gI().sendThongBaoOK(plKill, "Bạn nhận được " + diembitich + "Điểm bí tịch");
            Service.getInstance().dropItemMap(this.zone, it);
        }
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }

            if (Util.isTrue(10, 100)) {
                int hpHoi = (int) ((long) damage * 100 / 100);
                PlayerService.gI().hoiPhuc(this, hpHoi, 0);
                if (Util.isTrue(5, 5)) {
                    this.chat("Tái Tạo ... ");
                }

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
//        this.hapThu();        
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

//    @Override
//    public void attack() {
//        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL
//                && (this.gethour() >= 0 && this.gethour() <= 24)) { // Sửa ở đây
//            this.lastTimeAttack = System.currentTimeMillis();
//            try {
//                Player pl = getPlayerAttack();
//                if (pl == null || pl.isDie()) {
//                    return;
//                }
//                this.playerSkill.skillSelect = this.playerSkill.skills
//                        .get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
//                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
//                    if (Util.isTrue(5, 20)) {
//                        if (SkillUtil.isUseSkillChuong(this)) {
//                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
//                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
//                        } else {
//                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
//                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));
//                        }
//                    }
//                    SkillService.gI().useSkillboss(this, pl, null);
//                    checkPlayerDie(pl);
//                } else {
//                    if (Util.isTrue(1, 2)) {
//                        this.moveToPlayer(pl);
//                    }
//                }
//            } catch (Exception ex) {
//            }
//        }
//    }
//    private void hapThu() {
//
//if(Util.isTrue(5, 100)){
//        Player pl = this.zone.getRandomPlayerInMap();
//        if (pl == null || pl.isDie()) {
//            return;
//        }
//        int hpHoi = (int) ((long) pl.nPoint.hp * 80 / 100);
//        PlayerService.gI().hoiPhuc(this, hpHoi, 0);
//        
//        this.nPoint.dameg += (pl.nPoint.dame * 5 / 100);
//        this.nPoint.hpg += (pl.nPoint.hp * 10 / 100);
//        this.nPoint.critg++;
//        this.nPoint.calPoint();
//        PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);
//        pl.injured(null, pl.nPoint.hpMax, true, false);
//        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " Nuốt chửng!");
//        this.chat(2, "Ui cha cha, kinh dị quá. " + pl.name + " vừa bị tên " + this.name + " nuốt chửng kìa!!!");
//        this.chat("Haha, ngọt lắm đấy " + pl.name + "..");
//        this.lastTimeHapThu = System.currentTimeMillis();
//        this.timeHapThu = Util.nextInt(7000000, 15000000);
//    }
//    }    
}
