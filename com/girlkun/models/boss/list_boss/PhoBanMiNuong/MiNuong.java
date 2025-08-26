package com.girlkun.models.boss.list_boss.PhoBanMiNuong;

import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.Boss;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 * @author Bkt
 */
public class MiNuong extends Boss {

    protected Player playerAtt;

    public MiNuong(Zone zone) throws Exception {
        super(-100, new BossData(
                "Mị nương", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{841, 842, 843, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((50000)), //dame
                new long[]{((2000000000))}, //hp
                new int[]{}, //map join
                new int[][]{
                    {Skill.DEMON, 3, 1}, {Skill.DEMON, 6, 2}, {Skill.DRAGON, 7, 3}, {Skill.DRAGON, 1, 4}, {Skill.GALICK, 5, 5},
                    {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                    {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16}, {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                    {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                    {Skill.KAMEJOKO, 7, 1000},},
                new String[]{}, //text chat 1
                new String[]{"|-1|"}, //text chat 2
                new String[]{}, //text chat 3
                60
        ));

        this.zone = zone;
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap caitrangmi = new ItemMap(this.zone, 860, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            caitrangmi.options.add(new Item.ItemOption(50, 24));
            caitrangmi.options.add(new Item.ItemOption(77, 32));
            caitrangmi.options.add(new Item.ItemOption(117, 20));
            caitrangmi.options.add(new Item.ItemOption(93, new Random().nextInt(3) + 4));
            Service.getInstance().dropItemMap(this.zone, caitrangmi);
        }
    }

    @Override
    public void active() {
        super.active();
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage / 2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 2;
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
