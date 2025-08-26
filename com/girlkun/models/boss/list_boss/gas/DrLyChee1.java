package com.girlkun.models.boss.list_boss.gas;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.*;
import static com.girlkun.models.boss.BossStatus.ACTIVE;
import static com.girlkun.models.boss.BossStatus.JOIN_MAP;
import static com.girlkun.models.boss.BossStatus.RESPAWN;
import com.girlkun.models.boss.list_boss.cell.SieuBoHung;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import static com.girlkun.models.item.ItemTime.BAN_DO_KHO_BAU;
import static com.girlkun.models.item.ItemTime.KHI_GASS;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.map.challenge.MartialCongressService;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.RewardService;
import com.girlkun.services.SkillService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author BTH sieu cap vippr0
 */
public class DrLyChee1 extends Boss {

    private int levell;
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};
    private long lastTimeHapThu;
    private int timeHapThu;
    private long lastUpdate = System.currentTimeMillis();
    private long timeJoinMap;
    private int initSuper = 0;
    protected Player playerAtt;
    private int timeLive = 200000000;

    public DrLyChee1(Zone zone, int level, int dame, long hp, Player pl) throws Exception {
        super(BossID.DR_LYCHEE1, new BossData(
                 "Dr Lychee",
                ConstPlayer.XAYDA,
                new short[]{742, 743, 744, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((5000 * level)), //dame
                new long[]{((100000000L * level))}, //hp
                new int[]{148}, //map join
                new int[][]{
                    {Skill.DEMON, 3, 1}, {Skill.DEMON, 6, 2}, {Skill.DRAGON, 7, 3}, {Skill.DRAGON, 1, 4}, {Skill.GALICK, 5, 5},
                    {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                    {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16}, {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                    {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                    {Skill.KAMEJOKO, 7, 1000},},
                new String[]{}, //text chat 1
                new String[]{"|-1|Nhóc con"}, //text chat 2
                new String[]{}, //text chat 3
                60
        ));
        this.zone = zone;
        this.levell = level;
    }
@Override
    public void reward(Player plKill) {
    if (Util.isTrue(100, 100)) {
        ItemMap it = new ItemMap(this.zone, 738, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
        this.location.y - 24), plKill.id);
        it.options.add(new Item.ItemOption(50, Util.nextInt(5, 45)));
        it.options.add(new Item.ItemOption(77, Util.nextInt(5, 45)));
        it.options.add(new Item.ItemOption(103, Util.nextInt(5, 45)));
        it.options.add(new Item.ItemOption(5, Util.nextInt(1, 20)));
        // Thêm đoạn kiểm tra tỷ lệ 20% không có và 80% có option 93
        if (Util.isTrue(90, 100)) { // 80% có option 93
            it.options.add(new Item.ItemOption(93, Util.nextInt(1, 7)));
        }
        Service.getInstance().dropItemMap(this.zone, it);
    }

    plKill.clan.pointGas += 1;
    if (plKill.clan.pointGas >= 2) {
        for (Player pl : plKill.clan.membersInGame) {
            pl.clan.haveGoneGas = true;
            pl.clan.khiGas = null;
            ItemTimeService.gI().sendTextTime(plKill, (byte) KHI_GASS, "Khí gas hủy diệt sắp kết thúc : ", 30);
            ChangeMapService.gI().goToHome(plKill);
        }
    }
}

    @Override
    public void active() {
        super.active();
    }
    @Override
    public void joinMap() {
        super.joinMap();
    }

    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (levell <= 30000) {
            if (Util.isTrue((levell / 1000), 100) && plAtt != null) {//tỉ lệ hụt của thiên sứ
                Util.isTrue(this.nPoint.tlNeDon, 1000000);
                if (Util.isTrue(1, 100)) {
                    this.chat("Đạt Gà đánh mạnh lên");
                    this.chat("Hiếu Gà đánh mạnh lên");
                } else if (Util.isTrue(1, 100)) {
                    this.chat("Ngậm hành đi kkkkk");
                    this.chat("Anh Đức đẹp trai nhất SV");
                    this.chat("Các ngươi sẽ tránh được mọi nguy hiểm");
                } else if (Util.isTrue(1, 100)) {
                    this.chat("Anh Đức đẹp trai nhất SV");
                }
                damage = 0;
            }
        } else {
            if (Util.isTrue(40, 100) && plAtt != null) {//tỉ lệ hụt của thiên sứ
                Util.isTrue(this.nPoint.tlNeDon, 1000000);
                if (Util.isTrue(1, 100)) {
                    this.chat("Đạt Gà đánh mạnh lên");
                    this.chat("Hiếu Gà đánh mạnh lên");
                } else if (Util.isTrue(1, 100)) {
                    this.chat("Ngậm hành đi kkkkk");
                    this.chat("Anh Đức đẹp trai nhất SV");
                    this.chat("Các ngươi sẽ tránh được mọi nguy hiểm");
                } else if (Util.isTrue(1, 100)) {
                    this.chat("Anh Đức đẹp trai nhất SV");
                }
                damage = 0;
            }

        }
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = (int) this.nPoint.subDameInjureWithDeff(damage / 2);
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
            if (levell <= 30000) {
                damage -= damage * (levell / 1000) / 100;
            } else {
                damage -= damage * 40 / 100;
            }
            return damage;
        } else {
            return 0;
        }

    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
