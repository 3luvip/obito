package com.girlkun.models.npc;

import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstTask;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.TaskService;
import java.util.ArrayList;
import java.util.List;

public class NpcManager {

    public static Npc getByIdAndMap(int id, int mapId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == id && npc.mapId == mapId) {
                return npc;
            }
        }
        return null;
    }

    public static Npc getNpc(byte tempId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == tempId) {
                return npc;
            }
        }
        return null;
    }

    public static List<Npc> getNpcsByMapPlayer(Player player) {
        List<Npc> list = new ArrayList<>();
        if (player.zone != null) {
            for (Npc npc : player.zone.map.npcs) {
                if (npc.tempId == ConstNpc.QUA_TRUNG && player.mabuEgg == null && player.zone.map.mapId == (21 + player.gender)) {
                    continue;
                } else if (npc.tempId == ConstNpc.QUA_TRUNG && player.billEgg == null && player.zone.map.mapId == 154) {
                    continue;
                } else if (npc.tempId == ConstNpc.THAN_MEO_KARIN && (player.istry || player.istry1) && player.zone.map.mapId == 46) {
                    continue;
                } else if (npc.tempId == ConstNpc.THAN_VU_TRU && (player.istry || player.istry1 || player.isfight || player.isfight1) && player.zone.map.mapId == 48) {
                    continue;
                } else if (npc.tempId == ConstNpc.TO_SU_KAIO && (player.istry || player.istry1 || player.isfight || player.isfight1) && player.zone.map.mapId == 50) {
                    continue;
                } else if (npc.tempId == ConstNpc.CALICK && TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    continue;
                }
                list.add(npc);
            }
        }
        return list;
    }

}
