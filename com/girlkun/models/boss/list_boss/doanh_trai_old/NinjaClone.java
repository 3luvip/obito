/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.boss.list_boss.doanh_trai_old;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;

/**
 *
 * @author Khánh Đẹp Zoai
 */
public class NinjaClone extends TrungUyTrang {

    NinjaAoTim trueNinja;
    private int x = 0;
    private int y = 0;

    public NinjaClone(NinjaAoTim TrueNinja, int x, int y, int idBoss, int dame, int hp, Zone zone) throws Exception {
        super(dame, hp, zone, Util.randomBossId(), NINJA_CLONE);
        this.nPoint.khangTDHS = true;
        this.x = x;
        this.y = y;
        this.trueNinja = TrueNinja;
    }

    private static final BossData NINJA_CLONE = new BossData(
            "Ninja Áo Tím", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{123, 124, 125, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            500, // dame
            new long[]{500}, // hp
            new int[]{1}, // map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.KAMEJOKO, 7, 3000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Tao hơn hẳn mày, mày nên cầu cho may mắn ở phía mày đi",
                "|-1|Ha ha ha! Mắt mày mù à? Nhìn máy đo chỉ số đi!!",
                "|-1|Định chạy trốn hả, hử",
                "|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Hahaha mày đây rồi",
                "|-1|Tao đã có lệnh từ đại ca rồi"
            }, // text chat 2
            new String[]{"|-2|Đẹp trai nó phải thế"}, // text chat 3
            5 // second rest
    );

    @Override
    public void joinMap() {
        if (zoneFinal != null && this.x != 0 && this.y != 0 && this.trueNinja != null) {
            joinMapByZone(zoneFinal);
            // ChangeMapService.gI().changeMap(this, zoneFinal, Util.nextInt(x - 50, x +
            // 50), y);
        }
    }

    @Override
    public Player getPlayerAttack() {
        if (this.playerTarger != null && (this.playerTarger.isDie() || !this.zone.equals(this.playerTarger.zone))) {
            this.playerTarger = null;
        }
        if (this.playerTarger == null || Util.canDoWithTime(this.lastTimeTargetPlayer, this.timeTargetPlayer)) {
            this.playerTarger = this.zone.getRandomPlayerInMap();
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(5000, 7000);
        }
        if (this.playerTarger != null && this.playerTarger.effectSkin != null
                && this.playerTarger.effectSkin.isVoHinh) {
            this.playerTarger = null;
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(1000, 2000);
        }
        if (this.playerTarger == this.pet) {
            this.playerTarger = null;
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(1000, 2000);
        }
        return this.playerTarger;
    }

    private long lastTimeBlame;

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (plAtt != null && (plAtt.isPl() || plAtt.isPet) && plAtt.effectSkill.isAnThan) {
            EffectSkillService.gI().removeAnThan(plAtt);
        }
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(400, 1000)) {
                if (System.currentTimeMillis() - lastTimeBlame > 3000) {
                    this.chat("Xí hụt lêu lêu");
                }
                lastTimeBlame = System.currentTimeMillis();
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
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
    public void leaveMap() {
        super.leaveMap();
        this.trueNinja.numPhanThan--;
        synchronized (this) {
            BossManager.gI().removeBoss(this);
        }
        this.trueNinja = null;
        this.dispose();
    }

}
