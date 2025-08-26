package com.girlkun.models.map.GiaiCuuMiNuong;

import com.girlkun.models.boss.list_boss.PhoBanMiNuong.MiNuong;
import com.girlkun.models.boss.list_boss.PhoBanMiNuong.MiNuong2;
import com.girlkun.models.boss.list_boss.PhoBanMiNuong.MiNuong3;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

public class GiaiCuuMiNuongService {

    private static GiaiCuuMiNuongService i;

    private GiaiCuuMiNuongService() {
    }

    public static GiaiCuuMiNuongService gI() {
        if (i == null) {
            i = new GiaiCuuMiNuongService();
        }
        return i;
    }

    public void openGiaiCuuMiNuong(Player player) {
        if (player.clan != null && player.clan.giaiCuuMiNuong == null) {
            GiaiCuuMiNuong giaicuuminuong = null;
            for (GiaiCuuMiNuong gcmn : GiaiCuuMiNuong.GIAI_CUU_MI_NUONG) {
                if (!gcmn.isOpened) {
                    giaicuuminuong = gcmn;
                    break;
                }
            }
            if (giaicuuminuong != null) {
                giaicuuminuong.opengiaicuuminuong(player, player.clan);
                try {
                    long totalDame = 0;
                    long totalHp = 0;
                    for (Player play : player.clan.membersInGame) {
                        totalDame += play.nPoint.dame;
                        totalHp += play.nPoint.hpMax;
                    }
                    long dame = Util.TamkjllGH(totalHp / 20) * 5;
                    long hp = Util.TamkjllGH(totalDame * 4) * 5;
                    dame = Math.min(dame, 2000000000L);
                    hp = Math.min(hp, 2000000000L);
                    new MiNuong(player.clan.giaiCuuMiNuong.getMapById(188));
                    new MiNuong2(player.clan.giaiCuuMiNuong.getMapById(188));
                    new MiNuong3(player.clan.giaiCuuMiNuong.getMapById(188));

                } catch (Exception e) {
                    // Handle the exception appropriately (e.g., log the error).
                }
            } else {
                Service.getInstance().sendThongBao(player, "Giải Cứu Mị Nương đã đầy, vui lòng quay lại sau");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Yêu cầu phải có bang hội mới có thể tham gia");
        }
    }
}
