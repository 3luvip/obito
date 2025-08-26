package com.girlkun.models.boss.list_boss.TrainOffline;

import com.girlkun.models.boss.*;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.services.SkillService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;

/**
 * @Stole By obito ZALO 0866468126
 */
public class Bubbles extends Boss {

    public Bubbles() throws Exception {
        super(BossID.BUBBLES, BossesData.BUBBLES);
    }

    @Override
    public void reward(Player plKill) {
        //vật phẩm rơi khi diệt boss nhân bản
        if (plKill.isfight) {
            plKill.typetrain++;
        }
        plKill.rsfight();
        this.chat("Hôm nay ta không được khỏe");
        this.playerkill = plKill;

    }

    @Override
    public void active() {

        attack();
    }

    @Override
    public void checkPlayerDie(Player player) {
        if (player.isDie()) {
            this.chat("Quá gà");
            ChangeMapService.gI().changeMapYardrat(this, this.zone, 322, 408);
            this.changeToTypeNonPK();
            player.rsfight();
        }

    }

    @Override
    public void joinMapByZone(Zone zone) {
        if (zone != null) {
            this.zone = zone;
            ChangeMapService.gI().changeMapYardrat(this, this.zone, 378, 240);
        }
    }

    @Override
    public void update() {
        super.update();
        if (this.zone.getNumOfPlayers() < 1) {
            this.changeToTypeNonPK();
            ChangeMapService.gI().changeMapYardrat(this, this.zone, 378, 240);
            nPoint.setFullHpMp();
        }
    }

    @Override
    public void joinMap() {
        this.location.x = 378;
        this.location.y = 240;
        if (zoneFinal != null) {
            joinMapByZone(zoneFinal);

            this.notifyJoinMap();
            return;
        }
        if (this.zone == null) {
            if (this.parentBoss != null) {
                this.zone = parentBoss.zone;
            } else if (this.lastZone == null) {
                this.zone = getMapJoin();
            } else {
                this.zone = this.lastZone;
            }
        }
        if (this.zone != null) {
            if (this.currentLevel == 0) {

                ChangeMapService.gI().changeMapYardrat(this, this.zone, 322, 408);
            }
            Service.getInstance().sendFlagBag(this);
//            this.notifyJoinMap();
        }
    }

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100)) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl.isDie() || pl.isfake) {
                    if (pl == null) {
                        this.changeToTypeNonPK();
                    }
                    return;
                }
                if (pl.istry || pl.isfight) {
                    this.changeToTypePK();
                    if (this.isDie()) {
                        Service.gI().hsChar(this, this.nPoint.hpMax, this.nPoint.mpMax);
                    }
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
//                Logger.logException(Boss.class, ex);
            }
        }
    }

    @Override
    public Player getPlayerAttack() {
        if (this.playerTarger != null && (this.playerTarger.isDie() || !this.zone.equals(this.playerTarger.zone))) {
            this.playerTarger = null;
        }
        if (this.playerTarger == null || Util.canDoWithTime(this.lastTimeTargetPlayer, this.timeTargetPlayer)) {
            this.playerTarger = this.zone.getplayertrain();
            this.lastTimeTargetPlayer = System.currentTimeMillis();
        }
        return this.playerTarger;
    }

    @Override
    public void die(Player plKill) {
        if (plKill != null) {
            reward(plKill);
        }
        this.playerkill.rsfight();
    }

    @Override
    public void leaveMap() {
    }
}
