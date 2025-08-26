package com.girlkun.models.boss.list_boss.Broly;

import com.girlkun.models.boss.list_boss.*;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.*;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PetService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * @Stole By BKT
 */
public class Super extends Boss {

    public Super(int bossID, BossData bossData, Zone zone) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
    }

    @Override
//    public void reward(Player plKill) {
//        if (plKill.pet == null) {
//            if (plKill.xuatsu <= 3) {
//                PetService.gI().createNormalPet(plKill);
//                Service.gI().sendThongBao(plKill, "Bạn vừa nhận được đệ tử");
//            }
//
//            if (plKill.xuatsu > 3 && plKill.xuatsu <= 6) {
//                if (Util.isTrue(50, 100)) {
//                    PetService.gI().createFidePet(plKill);
//                    Service.gI().sendThongBao(plKill, "Bạn vừa nhận được đệ tử Fide");
//                } else {
//                    PetService.gI().createNormalPet(plKill);
//                    Service.gI().sendThongBao(plKill, "Bạn vừa nhận được đệ tử");
//                }
//            }
//
//            if (plKill.xuatsu > 6 && plKill.xuatsu <= 10) {
//                if (Util.isTrue(40, 100)) {
//                    PetService.gI().createXenPet(plKill);
//                    Service.gI().sendThongBao(plKill, "Bạn vừa nhận được đệ tử Xên Bọ Hung");
//                } else {
//                    PetService.gI().createFidePet(plKill);
//                    Service.gI().sendThongBao(plKill, "Bạn vừa nhận được đệ tử");
//                }
//            }
//
//            if (plKill.xuatsu > 10 && plKill.xuatsu <= 11) {
//                if (Util.isTrue(30, 100)) {
//                    PetService.gI().createMabuPet(plKill);
//                    Service.gI().sendThongBao(plKill, "Bạn vừa nhận được đệ tử Ma Bư");
//                } else {
//                    PetService.gI().createXenPet(plKill);
//                    Service.gI().sendThongBao(plKill, "Bạn vừa nhận được đệ tử");
//                }
//            }
//
//            if (plKill.xuatsu > 11) {
//                if (Util.isTrue(30 + plKill.xuatsu, 100)) {
//                    PetService.gI().createMabuPet(plKill);
//                    Service.gI().sendThongBao(plKill, "Bạn vừa nhận được đệ tử Ma Bư");
//                } else {
//                    PetService.gI().createXenPet(plKill);
//                    Service.gI().sendThongBao(plKill, "Bạn vừa nhận được đệ tử");
//                }
//            }
//        } else {
//            ItemMap it = new ItemMap(this.zone, 1131, 1, this.location.x, this.location.y, plKill.id);
//            Service.gI().dropItemMap(this.zone, it);
//        }
//    }

    public void reward (Player plKill) {
//        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length - 1);
        if (plKill.pet == null) {
            int gender = Util.nextInt(0, 2);
            PetService.gI().createNormalPet(plKill, gender);
            Service.getInstance().sendThongBao(plKill, "Bạn vừa nhận được đệ tử");
        } else {
            //    Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        }
    }
    
    @Override
    public void active() {
        Player player = this.zone.getPlayerInMap(id);
        if (player != null) {
            if (Util.canDoWithTime(st, 500)) {
                if (this.nPoint.hpMax < 160707770) {
                    this.nPoint.hpMax += this.nPoint.hpMax / 100;
                }
                if (this.nPoint.hpMax > 160707770) {
                    this.nPoint.hpMax = 160707770;
                }
            }
        }
        super.active();
        this.nPoint.dame = this.nPoint.hpMax / 100;//To change body of generated methods, choose Tools | Templates.
        if (!player.isBoss) {
            if (Util.canDoWithTime(st, 900000)) {
                if (player == null) {
                    this.changeStatus(BossStatus.LEAVE_MAP);
                    if (Util.isTrue(1, 100)) {
                        BossData Super = new BossData(
                                "Super Broly " + Util.nextInt(100),
                                this.gender,
                                new short[]{294, 295, 296, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                                Util.nextInt(100000, 200000), //dame
                                new long[]{Util.nextInt(10000000, 36789000)}, //hp
                                new int[]{6}, //map join
                                new int[][]{
                                    {Skill.ANTOMIC, 7, 100}, {Skill.MASENKO, 7, 100},
                                    {Skill.KAMEJOKO, 7, 100},
                                    {Skill.TAI_TAO_NANG_LUONG, 5, 15000}},
                                new String[]{"|-2|SuperBroly"}, //text chat 1
                                new String[]{"|-1|Ọc Ọc"}, //text chat 2
                                new String[]{"|-1|Lần khác ta sẽ xử đẹp ngươi"}, //text chat 3
                                60
                        );

                        try {
                            new Super(Util.createIdBossClone((int) this.id), Super, this.zone);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (plAtt != null) {
            switch (plAtt.playerSkill.skillSelect.template.id) {
                case Skill.ANTOMIC:
                case Skill.DEMON:
                case Skill.DRAGON:
                case Skill.GALICK:
                case Skill.KAIOKEN:
                case Skill.KAMEJOKO:
                case Skill.DICH_CHUYEN_TUC_THOI:
                case Skill.LIEN_HOAN:
                    damage = this.nPoint.hpMax / 100;
                    if (this.nPoint.hp < 1) {
                        this.setDie(plAtt);
                        die(this);
                    }
                    return super.injured(plAtt, damage, !piercing, isMobAttack);
            }
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;
}
