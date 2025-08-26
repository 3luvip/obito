package com.girlkun.models.boss.list_boss.Broly;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.jdbc.daos.PlayerDAO;
import java.util.Random;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.services.SkillService;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;

/**
 *
 * @author Tamkjll
 */
public class ThachDauWhis extends Boss {

    String nameString;
    private int lvboss;
    Player plattack;

    public ThachDauWhis(int bossID, BossData bossData, Zone zone, String name, int lvdame, Player pl) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
        nameString = name;
        this.nPoint.khangTDHS = true;
        this.nPoint.khangTM = true;
        this.plattack = pl;
    }

    @Override
    public void reward(Player plKill) {

        plKill.thachdauwhis++;
        Service.gI().sendMoney(plKill);
        PlayerDAO.updatePlayer(plKill);
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (this.plattack != null && !plAtt.isPl() && !plAtt.isNewPet) {
            this.leaveMap();
            return 0;
        }
        if (Util.isTrue(1f, 100) && plAtt != null) {// tỉ lệ hụt của thiên sứ
            Util.isTrue(this.nPoint.tlNeDon - plAtt.nPoint.tlNeDon, 100000);
            if (Util.isTrue(100, 100)) {
                this.chat("Ngươi đánh quá chậm, hãy đánh nhanh hơn đi");
                this.chat("Tránh các động tác thừa");
            }
            damage = 0;
        }
        if (plAtt != null) {
            switch (plAtt.playerSkill.skillSelect.template.id) {
                case Skill.THAI_DUONG_HA_SAN:
                case Skill.MASENKO:
                case Skill.ANTOMIC:
                case Skill.DICH_CHUYEN_TUC_THOI:
                    return 0;
            }
        }
        if (!this.isDie()) {
            if (!plAtt.isPl()) {
                damage /= 2;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage / plAtt.capboss);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                    if (plAtt != null) {
                        EffectSkillService.gI().breakShield(plAtt);
                    }
                }
                damage = 1;
            }
            if (damage > this.nPoint.hpMax * 10 / 100) {
                damage = this.nPoint.hpMax * 10 / 100;
            }
            this.nPoint.subHP(damage);
            Timegayst = System.currentTimeMillis();
            if (isDie()) {
                this.setDie(this);
                if (plAtt != null) {
                    die(plAtt);
                }
            }
            return damage;
        } else {
            return 0;
        }
    }

    private long Timegayst;

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.nPoint.khangTDHS = true;
        this.nPoint.khangTM = true;
        this.nPoint.critg = 110;
        this.nPoint.tlNeDon = 90;
        this.nPoint.calPoint();
        Player pl = this.plattack;
        if (pl == null || pl.isDie()
                || this.zone.getNumOfPlayers() >= 2) {
            this.leaveMap();
        }
        super.active();
        this.attack();
        if (Util.canDoWithTime(Timegayst, 60000)) {
            this.leaveMap();
        }
    }

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = this.plattack;
                if (pl == null || pl.isDie()) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
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
                    SkillService.gI().useSkill(this, pl, null, null);
                    checkPlayerDie(pl);
                } else {
                    if (Util.isTrue(1, 2)) {
                        this.moveToPlayer(pl);
                    }
                }
            } catch (Exception ex) {
            }
        }
        if (this.plattack == null || this.plattack.isDie()) {
            this.leaveMap();
        }
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }

    @Override
    public void joinMap() {
        super.joinMap(); // To change body of generated methods, choose Tools | Templates.
        Timegayst = System.currentTimeMillis();
    }
}
