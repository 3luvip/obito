package com.girlkun.models.boss.list_boss.bandokhobau;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;

import static com.girlkun.models.item.ItemTime.BAN_DO_KHO_BAU;
import com.girlkun.models.map.ItemMap;

import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.SkillService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;

/**
 *
 * @author Khánh Đẹp Zoai
 */
public class TrungUyXanhLoBdkb extends Boss {

    private int dameClan;
    private int hpClan;

    public TrungUyXanhLoBdkb(int dame, int hp, Zone zone) throws Exception {
        super(BossID.TRUNG_UY_XANH_LO, TRUNG_UY_XANH_LO);
        this.dameClan = dame;
        this.hpClan = hp;
        this.zoneFinal = zone;
    }

    private static final BossData TRUNG_UY_XANH_LO = new BossData(
            "Trung úy xanh lơ", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{135, 136, 137, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            500, //dame
            new long[]{500}, //hp
            new int[]{1}, //map join
            new int[][]{
                {Skill.DRAGON, 3, 1000},
                {Skill.KAMEJOKO, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 3, 13000}},
            new String[]{}, //text chat 1
            new String[]{"|-1|Tao hơn hẳn mày, mày nên cầu cho may mắn ở phía mày đi",
                "|-1|Ha ha ha! Mắt mày mù à? Nhìn máy đo chỉ số đi!!",
                "|-1|Định chạy trốn hả, hử",
                "|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Hahaha mày đây rồi",
                "|-1|Tao đã có lệnh từ đại ca rồi"
            }, //text chat 2
            new String[]{"|-2|Đẹp trai nó phải thế"}, //text chat 3
            5 //second rest
    );

    @Override
    public void initBase() {
        BossData data = this.data[this.currentLevel];
        this.name = String.format(data.getName(), Util.nextInt(0, 100));
        this.gender = data.getGender();
        this.nPoint.mpg = 23_07_2003;
        this.nPoint.dameg = this.dameClan;
        this.nPoint.hpg = this.hpClan;
        this.nPoint.hp = nPoint.hpg;
        this.nPoint.calPoint();
        this.initSkill();
        this.resetBase();
    }

//    @Override
//    public void joinMap() {
//        if (zoneFinal != null) {
//            ChangeMapService.gI().changeMap(this, zoneFinal, 147, 456);
//            this.zone.isTrungUyTrangAlive = true;
//        }
//    }
    public void joinMapByZone(Zone zone, int x) {
        if (zone != null) {
            this.zone = zone;
            ChangeMapService.gI().changeMap(this, this.zone, x, 456);
        }
    }

    @Override
    public void joinMap() {
        if (zoneFinal != null) {
            joinMapByZone(zoneFinal, 195);
//            ChangeMapService.gI().changeMap(this, zoneFinal, 897, 384);
            this.zone.isTrungUyTrangAlive = true;
        }
    }
    private long lastTimeFindPlayerToChangeFlag;

    @Override
    public void active() {
        if (Util.canDoWithTime(this.lastTimeFindPlayerToChangeFlag, 500) && this.typePk == ConstPlayer.NON_PK) {
            if (getPlayerAttack() == null) {
                this.lastTimeFindPlayerToChangeFlag = System.currentTimeMillis();
            } else {
                this.changeToTypePK();
            }
        }
        this.attack();
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
        if (this.playerTarger != null && this.playerTarger.effectSkin != null && this.playerTarger.effectSkin.isVoHinh) {
            this.playerTarger = null;
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(5000, 7000);
        }
        if (this.playerTarger == this.pet) {
            this.playerTarger = null;
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(5000, 7000);
        }
        if (this.playerTarger != null && this.typePk == ConstPlayer.NON_PK) {
            if ((this.playerTarger.location.x > -50 || this.playerTarger.location.x < 500) && this.playerTarger.location.y < 300) {
                this.playerTarger = null;
                this.lastTimeTargetPlayer = System.currentTimeMillis();
                this.timeTargetPlayer = Util.nextInt(5000, 7000);
            }
            if (this.playerTarger != null && this.playerTarger.location.y > 300 && this.playerTarger.location.x > 500) {
                this.playerTarger = null;
                this.lastTimeTargetPlayer = System.currentTimeMillis();
                this.timeTargetPlayer = Util.nextInt(5000, 7000);
            }

        }
        return this.playerTarger;
    }

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie()) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, pl) <= 100) {
                    if (Util.isTrue(5, 10)) {
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
                    this.moveToPlayer(pl);
                }
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(50, 100);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -25 : 0));
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        synchronized (this) {
            BossManager.gI().removeBoss(this);
        }
        this.bossStatus = null;
        this.lastZone = null;
        this.playerTarger = null;
        this.bossAppearTogether = null;
        this.parentBoss = null;
        this.zoneFinal = null;
        this.dispose();
    }

    @Override
    public void reward(Player plKill) {
        plKill.clan.banDoKhoBau_haveGone = true;
        plKill.clan.banDoKhoBau.timePickReward = true;
        plKill.clan.banDoKhoBau.setLastTimeOpen(System.currentTimeMillis() + 35000);
        for (Player pl : plKill.clan.membersInGame) {
            ItemTimeService.gI().removeItemTime(pl, violate);
            ItemTimeService.gI().sendTextTime(pl, (byte) BAN_DO_KHO_BAU, "Động kho báu kết thúc sau: ", 30);
        }
    }
}
