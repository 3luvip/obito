package com.girlkun.models.boss.list_boss.fide;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;

import java.util.Calendar;

public class Fide extends Boss {

    public Fide() throws Exception {
        super(BossID.FIDE, BossesData.FIDE_DAI_CA_1, BossesData.FIDE_DAI_CA_2, BossesData.FIDE_DAI_CA_3);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(5, 600)) {
            ItemMap it = new ItemMap(this.zone, 19, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
        }

        Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, 76, Util.nextInt(1000000, 3000000), Util.nextInt(this.location.x - 20, this.location.x + 20), zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, 76, Util.nextInt(1000000, 3000000), Util.nextInt(this.location.x - 20, this.location.x + 20), zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, 76, Util.nextInt(1000000, 3000000), Util.nextInt(this.location.x - 20, this.location.x + 20), zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        //    if(Util.canDoWithTime(st,900000)){
        //        this.changeStatus(BossStatus.LEAVE_MAP);
        //    }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }

            final Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(11);
            if (hour >= 17 && hour < 19) {
                if (plAtt.playerTask.taskMain.id != 21) {
                    if (damage >= 0) {
                        damage = 0;
                        Service.getInstance().sendThongBao(plAtt, "Bây giờ là giờ nhiệm vụ, không phải nhiệm vụ hiện tại của bạn, boss miễn nhiễm sát thương");
                    }
                }
                if (this.name.equals("Fide đại ca 1")) {
                    if (plAtt.playerTask.taskMain.id == 21) {
                        if (plAtt.playerTask.taskMain.index != 1) {
                            if (damage >= 0) {
                                damage = 0;
                                Service.getInstance().sendThongBao(plAtt, "Bây giờ là giờ nhiệm vụ, không phải nhiệm vụ hiện tại của bạn, boss miễn nhiễm sát thương");
                            }
                        }
                    }
                }
                if (this.name.equals("Fide đại ca 2")) {
                    if (plAtt.playerTask.taskMain.id == 21) {
                        if (plAtt.playerTask.taskMain.index != 2) {
                            if (damage >= 0) {
                                damage = 0;
                                Service.getInstance().sendThongBao(plAtt, "Bây giờ là giờ nhiệm vụ, không phải nhiệm vụ hiện tại của bạn, boss miễn nhiễm sát thương");
                            }
                        }
                    }
                }
                if (this.name.equals("Fide đại ca 3")) {
                    if (plAtt.playerTask.taskMain.id == 21) {
                        if (plAtt.playerTask.taskMain.index != 3) {
                            if (damage >= 0) {
                                damage = 0;
                                Service.getInstance().sendThongBao(plAtt, "Bây giờ là giờ nhiệm vụ, không phải nhiệm vụ hiện tại của bạn, boss miễn nhiễm sát thương");
                            }
                        }
                    }
                }
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

}
