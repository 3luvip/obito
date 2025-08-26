package com.girlkun.models.map.sieuhang;

import com.girlkun.database.GirlkunDB;
import com.girlkun.models.matches.PVP;
import com.girlkun.models.matches.TYPE_LOSE_PVP;
import com.girlkun.models.matches.TYPE_PVP;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.dhvt.BossDHVT;
import com.girlkun.models.player.Player;
import com.girlkun.server.Client;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Logger;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.jdbc.daos.GodGK;
import com.girlkun.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SieuHang {

    //    @Setter
//    @Getter
    private Player player;
    //    @Setter
    private Boss boss;

    //    @Setter
    private int time;
    //    @Setter
    private int timeWait;

    public void update() {
        if (time > 0) {
            time--;
            if (player.isDie()) {
                die();
                return;
            }
            if (player.location != null && !player.isDie() && player != null && player.zone != null) {
                if (boss.isDie()) {
                    endChallenge();
                    if (player.rankSieuHang == boss.rankSieuHang) {
                        player.rankSieuHang--;
                    } else if (player.rankSieuHang > boss.rankSieuHang) {
                        long temp = player.rankSieuHang;
                        player.rankSieuHang = boss.rankSieuHang;
                        BossDHVT bossDHVT = (BossDHVT) boss;
                        Player player1 = Client.gI().getPlayer(bossDHVT.idPlayer);
                        if (player1 == null) {
                            player1 = GodGK.loadByIdSieuHang(bossDHVT.idPlayer);
                        }
                        player1.rankSieuHang = temp;
                        updateTop(player1, (int) player1.rankSieuHang);
                        // PlayerDAO.updatePlayer(player1);
                    }
                    Service.gI().chat(player, "Haha thắng cuộc rồi! Đã thăng lên hạng " + player.rankSieuHang);
                    //  PlayerDAO.updatePlayer(player);
                    updateTop(player, (int) player.rankSieuHang);
                    boss.leaveMap();
                }
                if (player.location.y > 264) {
                    leave();
                }
            } else {
                if (boss != null) {
                    boss.leaveMap();
                }
                SieuHangManager.gI().remove(this);
            }
        } else {
            timeOut();
        }
        if (timeWait > 0) {
            switch (timeWait) {
                case 5:
                    Service.getInstance().chat(boss, "Sẵn sàng chưa");
                    ready();
                    break;
                case 1:
                    Service.getInstance().chat(player, "Ok");
                    break;
            }
            timeWait--;
        }
    }

    public static boolean updateTop(Player player, int num) {
        try (Connection con = GirlkunDB.getConnection(); PreparedStatement ps = con.prepareStatement("UPDATE player SET `rank_sieu_hang` = ? WHERE id = ?")) {
            ps.setInt(1, num);
            ps.setLong(2, player.id);
            ps.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(PlayerDAO.class, e, "Lỗi update Top" + player.name);
            return false;
        }
    }

    public void ready() {
        EffectSkillService.gI().startStun(boss, System.currentTimeMillis(), 5000);
        EffectSkillService.gI().startStun(player, System.currentTimeMillis(), 5000);
        ItemTimeService.gI().sendItemTime(player, 3779, 5);
        Util.setTimeout(() -> {
            if (boss.effectSkill != null) {
                EffectSkillService.gI().removeStun(boss);
            }
            SieuHangService.gI().sendTypePK(player, boss);
            PlayerService.gI().changeAndSendTypePK(this.player, ConstPlayer.PK_PVP);
            PVP pvp = new PVP(TYPE_PVP.THACH_DAU, player, boss) {
                @Override
                public void finish() {

                }

                @Override
                public void update() {

                }

                @Override
                public void reward(Player plWin) {

                }

                @Override
                public void sendResult(Player plLose, TYPE_LOSE_PVP typeLose) {

                }
            };
            player.pvp = pvp;
            boss.pvp = pvp;

            boss.changeStatus(BossStatus.ACTIVE);
        }, 5000);
    }

    public void toTheNextRound(Boss bss) {
        try {
            PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
            SieuHangService.gI().moveFast(player, 335, 264);
            Service.gI().hsChar(player, player.nPoint.hpMax, player.nPoint.mpMax);
            setTimeWait(6);
            setBoss(bss);
            setTime(185);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Boss getBoss() {
        return boss;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTimeWait() {
        return timeWait;
    }

    public void setTimeWait(int timeWait) {
        this.timeWait = timeWait;
    }

    private void die() {
        Service.getInstance().sendThongBao(player, "Thất bại rồi nhục nhã quá");
        if (player.zone != null) {
            endChallenge();
        }
    }

    private void timeOut() {
        Service.getInstance().sendThongBao(player, "Bạn bị xử thua vì hết thời gian");
        endChallenge();
    }

    public void leave() {
        setTime(0);
        EffectSkillService.gI().removeStun(player);
        Service.getInstance().sendThongBao(player, "Bạn bị xử thua vì rời khỏi võ đài");
        endChallenge();
    }

    public void endChallenge() {
        if (player.zone != null) {
            player.pvp = null;
            PlayerService.gI().hoiSinh(player);
        }
        PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
        if (player != null && player.zone != null && player.zone.map.mapId == 113) {
            Util.setTimeout(() -> {
                ChangeMapService.gI().changeMapNonSpaceship(player, 113, player.location.x, 360);
            }, 500);
        }
        if (boss != null) {
            boss.pvp = null;
            boss.leaveMap();
        }
        SieuHangManager.gI().remove(this);
    }
}
