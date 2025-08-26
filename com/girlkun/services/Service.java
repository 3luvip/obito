package com.girlkun.services;

import com.girlkun.database.GirlkunDB;
import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.BossID;
import com.girlkun.utils.FileIO;
import com.girlkun.data.DataGame;
import com.girlkun.jdbc.daos.GodGK;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.boss.list_boss.TrainOffline.Thuongde;
import com.girlkun.models.boss.list_boss.doanh_trai_old.TrungUyTrang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.npc.specialnpc.MabuEgg;
import com.girlkun.models.player.Pet;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.Zone;
import com.girlkun.models.matches.PVP;
import com.girlkun.models.matches.PVPManager;
import com.girlkun.models.matches.TOP;
import com.girlkun.models.npc.specialnpc.BillEgg;
import com.girlkun.models.player.Pet3;
import com.girlkun.models.player.Player;
import com.girlkun.models.shop.ItemShop;
import com.girlkun.models.shop.Shop;
import com.girlkun.server.io.MySession;
import com.girlkun.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.girlkun.network.server.GirlkunSessionManager;
import com.girlkun.network.session.ISession;
import com.girlkun.network.session.Session;
import com.girlkun.result.GirlkunResultSet;
import com.girlkun.server.Client;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerManager;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.services.func.EffectMapService;
import com.girlkun.services.func.Input;
import com.girlkun.services.func.SummonDragon;
import static com.girlkun.services.func.SummonDragon.DRAGON_SHENRON;
import com.girlkun.utils.ErrorReslover;
import com.girlkun.utils.Logger;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;
import com.sun.management.OperatingSystemMXBean;
import java.io.DataOutputStream;

import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class Service {

    private static Service instance;
    public long lasttimechatbanv = 0;
    public long lasttimechatmuav = 0;

    public static Service gI() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public void sendchienlinh(Player player, short smallId) {
        Message msg;
        try {
            msg = new Message(31);
            msg.writer().writeInt((int) player.id);
            if (smallId == 0) {
                msg.writer().writeByte(0);
            } else {
                msg.writer().writeByte(1);
                msg.writer().writeShort(smallId);
                msg.writer().writeByte(1);
                int[] fr = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
                msg.writer().writeByte(fr.length);
                for (int i = 0; i < fr.length; i++) {
                    msg.writer().writeByte(fr[i]);
                }
                msg.writer().writeShort(smallId == 15067 ? 65 : 225);
                msg.writer().writeShort(smallId == 15067 ? 65 : 225);
            }
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            System.out.println("wrwert");
        }
    }

    public void sendchienlinh(Player me, Player pl) {
        Item linhThu = pl.inventory.itemsBody.get(10);
        if (!linhThu.isNotNullItem()) {
            return;
        }
        short smallId = (short) (linhThu.template.iconID - 1);
        Message msg;
        try {
            msg = new Message(31);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(smallId);
            msg.writer().writeByte(1);
            int[] fr = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
            msg.writer().writeByte(fr.length);
            for (int i = 0; i < fr.length; i++) {
                msg.writer().writeByte(fr[i]);
            }
            msg.writer().writeShort(smallId == 15067 ? 225 : 225);
            msg.writer().writeShort(smallId == 15067 ? 222 : 225);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            System.out.println("Lỗi Send Chiến Linh");
        }
    }

    public void SendMsgUpdateHoaDa(Player player, byte typead, byte typeTar, byte type) {
        try {
            Message message = new Message(-124);
            message.writer().writeByte(typead);
            message.writer().writeByte(typeTar);
            message.writer().writeByte(type);
            message.writer().writeInt((int) player.id);
            sendMessAllPlayerInMap(player, message);
            message.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SendImgSkill9(short SkillId, int IdAnhSKill) {
        Message msg = new Message(62);
        DataOutputStream ds = msg.writer();
        try {
            ds.writeShort(SkillId);
            ds.writeByte(1);
            ds.writeByte(IdAnhSKill);
            ds.flush();
            Service.getInstance().sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendTitle(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (id == 891) {
                me.writer().writeShort(85);
            }
            if (id == 889) {
                me.writer().writeShort(86);
            }
            if (id == 890) {
                me.writer().writeShort(84);
            }
            switch (id) {
                case 1684:
                    me.writer().writeShort(60);
                    break;
                case 1696:
                    me.writer().writeShort(86);
                    break;
                case 1671:
                    me.writer().writeShort(67);
                    break;
                case 1672:
                    me.writer().writeShort(61);
                    break;
                case 1673:
                    me.writer().writeShort(62);
                    break;
                case 1674:
                    me.writer().writeShort(63);
                    break;
                case 1675:
                    me.writer().writeShort(84);
                    break;
                case 1676:
                    me.writer().writeShort(96);
                    break;
                case 1677:
                    me.writer().writeShort(97);
                    break;
                case 1678:
                    me.writer().writeShort(98);
                    break;
                case 1679:
                    me.writer().writeShort(99);
                    break;
                case 1680:
                    me.writer().writeShort(212);
                    break;
                case 1681:
                    me.writer().writeShort(213);
                    break;
                case 1682:
                    me.writer().writeShort(215);
                    break;
                case 1683:
                    me.writer().writeShort(356);
                    break;
            }
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTitleRv(Player player, Player p2, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (id == 891) {
                me.writer().writeShort(85);
            }
            if (id == 889) {
                me.writer().writeShort(86);
            }
            if (id == 890) {
                me.writer().writeShort(84);
            }
            switch (id) {
                case 1684:
                    me.writer().writeShort(60);
                    break;
                case 1696:
                    me.writer().writeShort(86);
                    break;
                case 1671:
                    me.writer().writeShort(67);
                    break;
                case 1672:
                    me.writer().writeShort(61);
                    break;
                case 1673:
                    me.writer().writeShort(62);
                    break;
                case 1674:
                    me.writer().writeShort(63);
                    break;
                case 1675:
                    me.writer().writeShort(84);
                    break;
                case 1676:
                    me.writer().writeShort(96);
                    break;
                case 1677:
                    me.writer().writeShort(97);
                    break;
                case 1678:
                    me.writer().writeShort(98);
                    break;
                case 1679:
                    me.writer().writeShort(99);
                    break;
                case 1680:
                    me.writer().writeShort(212);
                    break;
                case 1681:
                    me.writer().writeShort(213);
                    break;
                case 1682:
                    me.writer().writeShort(215);
                    break;
                case 1683:
                    me.writer().writeShort(356);
                    break;
//                case 1684:
//                    me.writer().writeShort(319);
//                    break;
//                case 1685:
//                    me.writer().writeShort(2206);
//                    break;   
            }
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            p2.sendMessage(me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFoot(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            switch (id) {
                case 1328:
                    me.writer().writeShort(74);
                    break;
                case 1329:
                    me.writer().writeShort(75);
                    break;
                case 1330:
                    me.writer().writeShort(76);
                    break;
                case 1331:
                    me.writer().writeShort(77);
                    break;
                case 1332:
                    me.writer().writeShort(78);
                    break;
                case 1333:
                    me.writer().writeShort(79);
                    break;
                case 1334:
                    me.writer().writeShort(80);
                    break;
                case 1335:
                    me.writer().writeShort(81);
                    break;
                case 1336:
                    me.writer().writeShort(82);
                    break;
                case 1327:
                    me.writer().writeShort(106);
                    break;
                default:
                    break;
            }
            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFootRv(Player player, Player p2, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            switch (id) {
                case 1328:
                    me.writer().writeShort(74);
                    break;
                case 1329:
                    me.writer().writeShort(75);
                    break;
                case 1330:
                    me.writer().writeShort(76);
                    break;
                case 1331:
                    me.writer().writeShort(77);
                    break;
                case 1332:
                    me.writer().writeShort(78);
                    break;
                case 1333:
                    me.writer().writeShort(79);
                    break;
                case 1334:
                    me.writer().writeShort(80);
                    break;
                case 1335:
                    me.writer().writeShort(81);
                    break;
                case 1336:
                    me.writer().writeShort(82);
                    break;
                case 1327:
                    me.writer().writeShort(106);
                    break;
                default:
                    break;
            }

            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(-1);
            p2.sendMessage(me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void reloadTitle(Player player) {
        Service.getInstance().sendFlagBag(player);
        if (player.inventory.itemsBody.get(11).isNotNullItem() && player.inventory.itemsBody.get(11) != null) {
            Service.getInstance().sendFoot(player, (player.inventory.itemsBody.get(11).template.id));
        }
        if (player.inventory.itemsBody.get(12).isNotNullItem() && player.inventory.itemsBody.get(12) != null) {
            Service.getInstance().sendTitle(player, (player.inventory.itemsBody.get(12).template.id));
        }
        if (player.inventory.itemsBody.get(10).isNotNullItem() && player.inventory.itemsBody.get(10) != null && player.reloadtitle == true) {
            Service.getInstance().sendPetFollow(player, (short) ((short) player.inventory.itemsBody.get(10).template.iconID - 1));
        }
        if (player.reloadtitle == true) {
            player.reloadtitle = false;
        }
    }

    public void removeTitle(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
            player.reloadtitle = true;
            if (player.inventory.itemsBody.get(11).isNotNullItem()) {
                Service.getInstance().sendFoot(player, (short) player.inventory.itemsBody.get(11).template.id);
            }
            if (player.inventory.itemsBody.get(12).isNotNullItem()) {
                Service.getInstance().sendTitle(player, (short) player.inventory.itemsBody.get(12).template.id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addEffectChar(Player pl, int id, int layer, int loop, int loopcount, int stand) {
        if (!pl.idEffChar.contains(id)) {
            pl.idEffChar.add(id);
        }
        try {
            Message msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(id);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(loop);
            msg.writer().writeShort(loopcount);
            msg.writer().writeByte(stand);
            sendMessAllPlayerInMap(pl.zone, msg);
        } catch (IOException e) {

        }
    }

    public void showListPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("(" + TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss") + ")");
            msg.writer().writeByte(Client.gI().getPlayers().size());
            for (int i = 0; i < Client.gI().getPlayers().size(); i++) {
                Player pl = Client.gI().getPlayers().get(i);
                if (pl == null) {
                    pl = player;
                }
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(pl.isAdmin() ? "" : pl.isAdmin2() ? "" : "Member");
                msg.writer().writeUTF("SM: " + Util.powerToString(pl.nPoint.power)
                        + "\nTN: " + Util.powerToString(pl.nPoint.tiemNang)
                        + "\nHP: " + Util.powerToString(pl.nPoint.hpMax)
                        + "\nKI: " + Util.powerToString(pl.nPoint.mpMax)
                        + "\nSD: " + Util.powerToString(pl.nPoint.dame)
                        + "\nDEF: " + Util.powerToString(pl.nPoint.def)
                        + "\nCM: " + pl.nPoint.crit + "%"
                        + "\n|7|[Map: " + pl.zone.map.mapName + "(" + pl.zone.map.mapId + ") " + "Khu: " + pl.zone.zoneId + "]");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void showListTopDauNhanh(Player player, List<TOP> tops, byte isPVP, int start) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top");
            msg.writer().writeByte(tops.size());

            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadById(top.getId_player());
//                msg.writer().writeInt(isPVP != 1 ? (i + 1) : (int)pl.rankSieuHang);
                msg.writer().writeInt(start);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(top.getInfo1());
//                msg.writer().writeUTF(isPVP == 1 ? top.getInfo2() : top.getInfo2() + pl.numKillSieuHang);
                msg.writer().writeUTF(isPVP == 1 ? ("Sức Đánh: " + pl.nPoint.dame + "\n" + "HP: " + pl.nPoint.hpMax + "\n" + "KI: " + pl.nPoint.mpMax + "\n" + "Điểm hạng: " + pl.rankSieuHang) : top.getInfo2());
                start++;
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListTop(Player player, List<TOP> tops) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top");
            msg.writer().writeByte(tops.size());
            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadById(top.getId_player());
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(top.getInfo1());
                msg.writer().writeUTF(top.getInfo2());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void showListTop(Player player, List<TOP> tops, byte isPVP) {
//        Message msg;
//        try {
//            msg = new Message(-96);
//            msg.writer().writeByte(0);
//            msg.writer().writeUTF("Top");
//            msg.writer().writeByte(tops.size());
//            for (int i = 0; i < tops.size(); i++) {
//                TOP top = tops.get(i);
//                Player pl = GodGK.loadById(top.getId_player());
//                msg.writer().writeInt(isPVP != 1 ? (i + 1) : (int) pl.rankSieuHang);
////                msg.writer().writeInt(i + 1);
//                msg.writer().writeInt((int) pl.id);
//                msg.writer().writeShort(pl.getHead());
//                if (player.getSession().version > 214) {
//                    msg.writer().writeShort(-1);
//                }
//                msg.writer().writeShort(pl.getBody());
//                msg.writer().writeShort(pl.getLeg());
//                msg.writer().writeUTF(pl.name);
//                msg.writer().writeUTF(top.getInfo1());
//                msg.writer().writeUTF(isPVP == 1 ? top.getInfo2() : top.getInfo2() + pl.numKillSieuHang);
//                //               msg.writer().writeUTF(isPVP == 1 ? ("Sức Đánh: " + pl.nPoint.dame +"\n"+ "HP: " + pl.nPoint.hpMax +"\n"+ "KI: " + pl.nPoint.mpMax +"\n" + "Điểm hạng: " + pl.rankSieuHang) : top.getInfo2());
//            }
//            player.sendMessage(msg);
//            msg.cleanup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void showListTop(Player player, List<TOP> tops, byte isPVP) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top");
            msg.writer().writeByte(tops.size());
            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadById(top.getId_player());
                msg.writer().writeInt(isPVP != 1 ? (i + 1) : (int) pl.rankSieuHang);
//                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(top.getInfo1());
//                msg.writer().writeUTF(isPVP == 1 ? top.getInfo2() : top.getInfo2() + pl.numKillSieuHang);
                msg.writer().writeUTF(isPVP == 1 ? ("Sức Đánh: " + pl.nPoint.dameg + "\n" + "HP: " + pl.nPoint.hpg + "\n" + "KI: " + pl.nPoint.mpg + "\n" + "Điểm hạng: " + pl.rankSieuHang + "\n" + pl.name + "đang thi đấu tại " + pl.zone.map.mapName + " khu vực " + "5" + "(" + pl.zone.map.mapId + ")") : top.getInfo2());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRank(Player player) {
        Message msg = null;
        try {
            msg = new Message(-119);
            msg.writer().writeInt((int) player.rankSieuHang);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPopUpMultiLine(Player pl, int tempID, int avt, String text) {
        Message msg = null;
        try {
            msg = new Message(-218);
            msg.writer().writeShort(tempID);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(avt);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendPetFollow(Player player, short smallId) {
    Message msg = new Message(31);
    try {
        msg.writer().writeInt((int) player.id);
        if (smallId == 0) {
            msg.writer().writeByte(0);
        } else {
            msg.writer().writeByte(1);
            msg.writer().writeShort(smallId);
            msg.writer().writeByte(1);

            int[] fr = getImageFrames(smallId);
            msg.writer().writeByte(fr.length);
            for (int i = 0; i < fr.length; i++) {
                msg.writer().writeByte(fr[i]);
            }

            int[] dimensions = getImageDimensions(smallId);
            msg.writer().writeShort(dimensions[0]);
            msg.writer().writeShort(dimensions[1]);
        }
        sendMessAllPlayerInMap(player, msg);
        msg.cleanup();
    } catch (Exception e) {
        e.printStackTrace(); // Thêm lệnh in ra lỗi để debug
    }
}

public void sendPetFollowToMe(Player me, Player pl) {
    if (pl.inventory == null || pl.inventory.itemsBody == null || pl.inventory.itemsBody.size() < 13) {
        System.out.println("Player inventory or itemsBody is null");
        return;
    }

    Item linhThu = pl.inventory.itemsBody.get(10);
    if (!linhThu.isNotNullItem()) {
        return;
    }

    short smallId = (short) (linhThu.template.iconID - 1);
    Message msg = new Message(31);
    try {
        msg.writer().writeInt((int) pl.id);
        msg.writer().writeByte(1);
        msg.writer().writeShort(smallId);
        msg.writer().writeByte(1);

        int[] fr = getImageFrames(smallId);
        msg.writer().writeByte(fr.length);
        for (int i = 0; i < fr.length; i++) {
            msg.writer().writeByte(fr[i]);
        }

        int[] dimensions = getImageDimensions(smallId);
        msg.writer().writeShort(dimensions[0]);
        msg.writer().writeShort(dimensions[1]);

        me.sendMessage(msg);
        msg.cleanup();
    } catch (Exception e) {
        e.printStackTrace(); // Thêm lệnh in ra lỗi để debug
    }
}


private int[] getImageFrames(short smallId) {
    switch (smallId) {
        case 16046:
        case 16048:
        case 16050:
        case 16052:
        case 16054:
        case 16056:
        case 16058:
        case 16060:
        case 16062:
        case 16064:
            return new int[]{0, 1, 2};
        case 16066:
        case 16068:
        case 16070:
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        case 16072:
        case 16074:  
        case 16076:
        case 16078:
            return new int[]{0, 1, 2};
        case 16080:
            return new int[]{0, 1, 2, 3, 4};
        case 16082:
            return new int[]{0, 1, 2, 3, 4, 5};
        case 16084:
            return new int[]{0, 1, 2, 3, 4, 5};
        case 16086:
            return new int[]{0, 1, 2, 3, 4, 5};
        case 16088:
        case 16090:
        case 16092:
        case 16094:
        case 16096:
        case 16098:
        case 16100:
        case 16102:
        case 16104:
            return new int[]{0, 1, 2, 3};
        case 16106:
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}; 
        case 16108:
        case 16110:
        case 16112:
            return new int[]{0, 1, 2, 3, 4};     
        default:
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7};
        }
    }

private int[] getImageDimensions(short smallId) {
    switch (smallId) {
        case 16046:
        case 16048:
        case 16050:
        case 16052:
        case 16054:
        case 16056:
        case 16058:
        case 16060:
        case 16062:
        case 16064:
            return new int[]{45, 50};
        case 16066:
        case 16068:
        case 16070:
            return new int[]{32, 40};
        case 16072:
            return new int[]{22, 21};
        case 16074:
            return new int[]{19, 21};
        case 16076:
            return new int[]{26, 27};
        case 16078:
            return new int[]{20, 20};
        case 16080:
            return new int[]{64, 64};
        case 16082:
            return new int[]{46, 62};
        case 16084:
            return new int[]{31, 29};
        case 16086:
            return new int[]{32, 36};
        case 16088:
        case 16090:
        case 16092:
        case 16094:
        case 16096:
        case 16098:
        case 16100:
        case 16102:
        case 16104:
            return new int[]{20, 20};
        case 16106:
            return new int[]{20, 20};
        case 16108:
        case 16110:
        case 16112:
            return new int[]{20, 20};
        default:
            return new int[]{smallId == 15067 ? 65 : 75, smallId == 15067 ? 65 : 75};
        }
    }
    public void sendMessAllPlayer(Message msg) {
        PlayerService.gI().sendMessageAllPlayer(msg);
    }

    public void sendMessAllPlayerIgnoreMe(Player player, Message msg) {
        PlayerService.gI().sendMessageIgnore(player, msg);
    }

    public void sendMessAllPlayerInMap(Zone zone, Message msg) {
        if (zone == null) {
            msg.dispose();
            return;
        }
        List<Player> players = zone.getPlayers();
        if (players.isEmpty()) {
            msg.dispose();
            return;
        }
        for (Player pl : players) {
            if (pl != null) {
                pl.sendMessage(msg);
            }
        }
        msg.cleanup();
    }

    public void sendMessAllPlayerInMap(Player player, Message msg) {
        if (player == null || player.zone == null) {
            msg.dispose();
            return;
        }
        if (MapService.gI().isMapOffline(player.zone.map.mapId)) {
            if (player.isPet) {
                ((Pet) player).master.sendMessage(msg);
            } else {
                player.sendMessage(msg);
            }
            if (player.isPet3) {
                ((Pet3) player).master.sendMessage(msg);
            } else {
                player.sendMessage(msg);
            }
        } else {
            List<Player> players = player.zone.getPlayers();
            if (players.isEmpty()) {
                msg.dispose();
                return;
            }
            for (int i = 0; i < players.size(); i++) {
                Player pl = players.get(i);
                if (pl != null) {
                    pl.sendMessage(msg);
                }
            }
        }
        msg.cleanup();
    }

    public void regisAccount(Session session, Message _msg) {
        try {
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            String user = _msg.readUTF();
            String pass = _msg.readUTF();
            if (!(user.length() >= 4 && user.length() <= 18)) {
                sendThongBaoOK((MySession) session, "Tài khoản phải có độ dài 4-18 ký tự");
                return;
            }
            if (!(pass.length() >= 5 && pass.length() <= 18)) {
                sendThongBaoOK((MySession) session, "Mật khẩu phải có độ dài 5-18 ký tự");
                return;
            }
            GirlkunResultSet rs = GirlkunDB.executeQuery("select * from account where username = ?", user);
            if (rs.first()) {
                sendThongBaoOK((MySession) session, "Tài khoản đã tồn tại");
            } else {
                GirlkunDB.executeUpdate("insert into account (username,password) values()", user, pass);
                sendThongBaoOK((MySession) session, "Đăng ký tài khoản thành công!");
            }
            rs.dispose();
        } catch (Exception e) {

        }
    }

    public void sendMessAnotherNotMeInMap(Player player, Message msg) {
        if (player == null || player.zone == null) {
            msg.dispose();
            return;
        }
        List<Player> players = player.zone.getPlayers();
        if (players.isEmpty()) {
            msg.dispose();
            return;
        }
        for (Player pl : players) {
            if (pl != null && !pl.equals(player)) {
                pl.sendMessage(msg);
            }
        }
        msg.cleanup();

    }

    public void Send_Info_NV(Player pl) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 14);//Cập nhật máu
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(Util.TamkjllGH(pl.nPoint.hp));
            msg.writer().writeByte(0);//Hiệu ứng Ăn Đậu
            msg.writer().writeInt(Util.TamkjllGH(pl.nPoint.hpMax));
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendInfoPlayerEatPea(Player pl) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 14);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(Util.TamkjllGH(pl.nPoint.hp));
            msg.writer().writeByte(1);
            msg.writer().writeInt(Util.TamkjllGH(pl.nPoint.hpMax));
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void loginDe(MySession session, short second) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(second);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void resetPoint(Player player, int x, int y) {
        Message msg;
        try {
            player.location.x = x;
            player.location.y = y;
            msg = new Message(46);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            player.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void clearMap(Player player) {
        Message msg;
        try {
            msg = new Message(-22);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void switchToRegisterScr(ISession session) {
        try {
            Message message;
            try {
                message = new Message(42);
                message.writeByte(0);
                session.sendMessage(message);
                message.cleanup();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }

    public void chat(Player player, String text) {
        if (text.equals("send")) {
            Service.gI().sendTitle(player, 171);
            return;
        }
        else if (text.equals("fix")) {
            try {
                Service.gI().sendThongBao(player, player.inventory.itemsBody.get(11).template.name);
            } catch (Exception e) {
                ErrorReslover.howToFix(e.toString());
            }
                return;
//            }
//        if (text.startsWith("callbot ")) {
//            try {
//                MySession _session = player.getSession();
//                int sl = Integer.parseInt(text.replace("callbot ", ""));
//                while (sl > 0) {
//                    Client.gI().createBot(_session);
//                    sl--;
//                    Service.gI().sendThongBao(player, "|7|Create BotPlayer Success!");
//                }
//            } catch (NumberFormatException e) {
//            }
//            return;
//        }
//        if (text.startsWith("drop")) {
//            try {
//                Boss trung = new TrungUyTrang(1000, 60000, player.zone);
//                System.err.println("name: " + trung.name);
//                System.err.println("mapid: " + trung.zoneFinal.map.mapId);
//
//            } catch (Exception ex) {
//                java.util.logging.Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
//            }

        }

        if (player.getSession() != null && player.isAdmin()) {
            if (text.equals("loadsv")) {
                Manager.loadPart();
                DataGame.updateData(player.getSession());
                return;
//            }
//            if (text.equals("hoiskill")) {
//                Service.getInstance().releaseCooldownSkill(player);
//                return;
//            }
//            if (text.equals("skillxd")) {
//                SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
//                return;
//            }
//            if (text.equals("skilltd")) {
//                SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
//                return;
//            }
//            if (text.equals("skillnm")) {
//                SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
//                return;
//            }
//            if (text.equals("dt")) {
//                PetService.Pet03(player, 467, 468, 469);
//                Service.getInstance().point(player);
//
//                return;
//            }
//            if (text.equals("hskill")) {
//                Service.getInstance().releaseCooldownSkill(player);
//                return;
//            }
//            if (text.equals("skillxd")) {
//                SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
//                return;
//            }
//            if (text.equals("broly")) {
//                SkillService.gI().learSkillSpecial(player, Skill.BIEN_BROLY);
//                return;
//            }
//            if (text.equals("tienhoa")) {
//                SkillService.gI().learSkillSpecial(player, Skill.TIEN_HOA);
//                return;
//            }
//            if (text.equals("khi")) {
//                SkillService.gI().learSkillSpecial(player, Skill.BIEN_KHI);
//                return;
//            }
//            if (text.equals("knn")) {
//                SkillService.gI().learSkillSpecial(player, Skill.KHIEN_NANG_LUONG);
//                return;
//            }
//            if (text.equals("skilltd")) {
//                SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
//                SkillService.gI().learSkillSpecial(player, Skill.TIEN_HOA);
//                SkillService.gI().learSkillSpecial(player, Skill.BIEN_BROLY);
//                SkillService.gI().learSkillSpecial(player, Skill.VO_HIEU_CHUONG);
//
//                return;
//            }
//            if (text.equals("client")) {
//                Client.gI().show(player);
//            } else if (text.equals("map")) {
//                sendThongBao(player, "Thông tin map: " + player.zone.map.mapName + " (" + player.zone.map.mapId + ")");
//                return;
//
//            } else if (text.startsWith("adde")) {
//                try {
//                    int mapId = Integer.parseInt(text.replace("adde", ""));
//                    EffectMapService.gI().sendEffectMapToAllInMap(player.zone, mapId, 3, 1, player.location.x, player.location.y + 32, 0);
//                    return;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } else if (text.startsWith("xoae")) {
//                try {
//                    byte mapId = Byte.parseByte(text.replace("xoae", ""));
//                    EffectSkillService.gI().sendEffectPlayer(player, player, EffectSkillService.TURN_ON_EFFECT, (byte) mapId);
//                    return;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } else if (text.equals("dh")) {
//                TimerTask task = new TimerTask() {
//                    @Override
//                    public void run() {
//                        EffectMapService.gI().sendEffectMapToAllInMap(player.zone,
//                                215, 3, 1, player.location.x, player.location.y + 32, 0);
//
//                    }
//                };
//                Timer timer = new Timer();
//                timer.schedule(task, 0, 500); // Bắt đầu thực thi sau 0 millisecond và lặp lại sau mỗi 1000 milliseconds
//            } else if (text.equals("vt")) {
//                sendThongBao(player, player.location.x + " - " + player.location.y + "\n"
//                        + player.zone.map.yPhysicInTop(player.location.x, player.location.y));
            } else if (text.equals("hs")) {
                player.nPoint.setFullHpMp();
                PlayerService.gI().sendInfoHpMp(player);
                sendThongBao(player, "Quyền năng trị liệu\n");
                return;
            } else if (text.equals("m")) {
                sendThongBao(player, "Map " + player.zone.map.mapName + " (" + player.zone.map.mapId + ")");
                return;
//            } else if (text.equals("boss")) {
//                String str = "";
//                for (Player b : player.zone.getBosses()) {
//                    str += b.name + "\n";
//                }
//                sendThongBao(player, str);
            } else if (text.equals("vt")) {
                sendThongBao(player, player.location.x + " - " + player.location.y + "\n"
                        + player.zone.map.yPhysicInTop(player.location.x, player.location.y));
//            } else if (text.startsWith("ss")) {
//            } else if (text.equals("b")) {
//                Message msg;
//                try {
//                    msg = new Message(52);
//                    msg.writer().writeByte(0);
//                    msg.writer().writeInt((int) player.id);
//                    sendMessAllPlayerInMap(player, msg);
//                    msg.cleanup();
//                } catch (Exception e) {
//                }
//            } else if (text.equals("c")) {
//                Message msg;
//                try {
//                    msg = new Message(52);
//                    msg.writer().writeByte(2);
//                    msg.writer().writeInt((int) player.id);
//                    msg.writer().writeInt((int) player.zone.getHumanoids().get(1).id);
//                    sendMessAllPlayerInMap(player, msg);
//                    msg.cleanup();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (text.equals("nrnm")) {
//                Service.gI().activeNamecShenron(player);
//            }
//            if (text.equals("ts")) {
//                sendThongBaoFromAdmin(player, "Time start server: " + ServerManager.timeStart + "\n");
//                return;
//            }
//            if (text.startsWith("notify")) {
//                String a = text.replace("notify ", "");
//                Service.gI().sendThongBaoAllPlayer(a);
//            }
//            if (text.equals("rr")) {
//                addEffectChar(player.pet, 357, 1, -1, -1, 1);
//                if (player.pet.gender == 0) {
//                    addEffectChar(player.pet, 85, 1, -1, -1, 1);
//                } else if (player.pet.gender == 1) {
//                    addEffectChar(player.pet, 84, 1, -1, -1, 1);
//                } else if (player.pet.gender == 2) {
//                    addEffectChar(player.pet, 86, 1, -1, -1, 1);
//                }
//                return;
//            }
//            if (text.startsWith("ep ")) {
//                try {
//                    int effid = Integer.parseInt(text.replace("ep ", ""));
//                    addEffectChar(player, effid, 1, -1, -1, 1);
//                    return;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//            if (text.equals("77")) {
//                try {
//                    new Thuongde(BossID.THUONG_DE, BossesData.THUONG_DE, player.zone, player.location.x - 10, player.location.y);
//                    player.zone.load_Another_To_Me(player);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
            if (text.equals("info")) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_PLAYER, 15934,
                        ""
                                + "|4| THÔNG TIN NHÂN VẬT NGỌC RỒNG AKAYA\n"
                                + "|8|Nhân vật : " + player.name 
//                        + "\nHành Tinh " + player.gender
                      
                        + "\n\n|7| HP : " + player.nPoint.hp + "/" + player.nPoint.hpMax
                        + "\n|2| KI : " + player.nPoint.mp + "/" + player.nPoint.mpMax 
                             + "\n|4| SỨC ĐÁNH  : " + player.nPoint.dame,
                             
                             
//                                        + "\n|8|SỨC MẠNH :" + player.nPoint.power
//                        + "\n|4|TIỀM NĂNG:" + player.nPoint.tiemNang,
//                        + "\n|7| Đệ Tử " + player.pet.name
//                        + "\nHành Tinh " + player.pet.gender
//                        + "\nSức Đánh " + player.pet.nPoint.dame
//                        + "\nHp " + player.pet.nPoint.hp + "/" + player.pet.nPoint.hpMax
//                        + "\nKi " + player.pet.nPoint.mp + "/" + player.pet.nPoint.hpMax,
                        "Đóng");
                return;
            }  if (text.equals("a")) {
                BossManager.gI().showListBoss(player);
                return;
//            } else if (text.equals("onl")) {
//                showListPlayer(player);
//                return;
//            } else if (text.equals("nrnm")) {
//                Service.gI().activeNamecShenron(player);
//                return;
//            } else if (text.equals("loadtop")) {
//                Manager.gI().loadAllTop();
//                sendThongBao(player, "RELOAD SHOP SUCCESS!");
//                return;
//            } else if (text.equals("hocskill")) {
//                List<Skill> skfix2 = new ArrayList<>();
//                switch (player.gender) {
//                    case 0:
//                        skfix2.add(SkillUtil.createSkill(0, 7));
//                        skfix2.add(SkillUtil.createSkill(1, 7));
//                        skfix2.add(SkillUtil.createSkill(6, 7));
//                        skfix2.add(SkillUtil.createSkill(9, 7));
//                        skfix2.add(SkillUtil.createSkill(10, 7));
//                        skfix2.add(SkillUtil.createSkill(20, 7));
//                        skfix2.add(SkillUtil.createSkill(22, 7));
//                        skfix2.add(SkillUtil.createSkill(19, 7));
//                        skfix2.add(SkillUtil.createSkill(24, 7));
//                        skfix2.add(SkillUtil.createSkill(28, 5));
//                        player.playerSkill.skills = skfix2;
//                        sendThongBao(player, "All Skill Thành Công, Hãy Học Lại Skill");
//                        return;
//                    case 1: // namek 2, 3, 7, 11, 12, 17, 18,26, 19}
//                        skfix2.add(SkillUtil.createSkill(2, 7));
//                        skfix2.add(SkillUtil.createSkill(3, 7));
//                        skfix2.add(SkillUtil.createSkill(7, 7));
//                        skfix2.add(SkillUtil.createSkill(11, 7));
//                        skfix2.add(SkillUtil.createSkill(12, 7));
//                        skfix2.add(SkillUtil.createSkill(17, 7));
//                        skfix2.add(SkillUtil.createSkill(18, 7));
//                        skfix2.add(SkillUtil.createSkill(19, 7));
//                        skfix2.add(SkillUtil.createSkill(26, 7));
//                        skfix2.add(SkillUtil.createSkill(28, 5));
//                        player.playerSkill.skills = skfix2;
//                        sendThongBao(player, "All Skill Thành Công, Hãy Học Lại Skill");
//                        return;
//                    case 2:
//                        skfix2.add(SkillUtil.createSkill(4, 7));
//                        skfix2.add(SkillUtil.createSkill(5, 7));
//                        skfix2.add(SkillUtil.createSkill(8, 7));
//                        skfix2.add(SkillUtil.createSkill(13, 7));
//                        skfix2.add(SkillUtil.createSkill(14, 7));
//                        skfix2.add(SkillUtil.createSkill(21, 7));
//                        skfix2.add(SkillUtil.createSkill(23, 7));
//                        skfix2.add(SkillUtil.createSkill(25, 7));
//                        skfix2.add(SkillUtil.createSkill(19, 7));
//                        skfix2.add(SkillUtil.createSkill(28, 5));
//                        player.playerSkill.skills = skfix2;
//                        sendThongBao(player, "All Skill Thành Công, Hãy Học Lại Skill");
//                        return;
//                }
//            }
//            if (text.equals("xoaskill")) {
//                List<Skill> skfix = new ArrayList<>();
//                if (player.gender == 0) { // td
//                    skfix.add(SkillUtil.createSkill(0, 1));
//                    skfix.add(SkillUtil.createSkillLevel0(1));
//                    skfix.add(SkillUtil.createSkillLevel0(6));
//                    skfix.add(SkillUtil.createSkillLevel0(9));
//                    skfix.add(SkillUtil.createSkillLevel0(10));
//                    skfix.add(SkillUtil.createSkillLevel0(20));
//                    skfix.add(SkillUtil.createSkillLevel0(22));
//                    skfix.add(SkillUtil.createSkillLevel0(19));
//                    skfix.add(SkillUtil.createSkillLevel0(24));
//                    skfix.add(SkillUtil.createSkillLevel0(28));
//                    player.playerSkill.skills = skfix;
//                    sendThongBao(player, "Fix Skill Thành Công, Hãy Học Lại Skill");
//                    return;
//                }
//                if (player.gender == 1) { // namek 2, 3, 7, 11, 12, 17, 18,26, 19}
//                    skfix.add(SkillUtil.createSkill(2, 1));
//                    skfix.add(SkillUtil.createSkillLevel0(3));
//                    skfix.add(SkillUtil.createSkillLevel0(7));
//                    skfix.add(SkillUtil.createSkillLevel0(11));
//                    skfix.add(SkillUtil.createSkillLevel0(12));
//                    skfix.add(SkillUtil.createSkillLevel0(17));
//                    skfix.add(SkillUtil.createSkillLevel0(18));
//                    skfix.add(SkillUtil.createSkillLevel0(19));
//                    skfix.add(SkillUtil.createSkillLevel0(26));
//                    skfix.add(SkillUtil.createSkillLevel0(28));
//                    player.playerSkill.skills = skfix;
//                    sendThongBao(player, "Fix Skill Thành Công, Hãy Học Lại Skill");
//                    return;
//                }
//                if (player.gender == 2) { // xd 4, 5, 8, 13, 14, 21, 23, 25, 19}
//                    skfix.add(SkillUtil.createSkill(4, 1));
//                    skfix.add(SkillUtil.createSkillLevel0(5));
//                    skfix.add(SkillUtil.createSkillLevel0(8));
//                    skfix.add(SkillUtil.createSkillLevel0(13));
//                    skfix.add(SkillUtil.createSkillLevel0(14));
//                    skfix.add(SkillUtil.createSkillLevel0(21));
//                    skfix.add(SkillUtil.createSkillLevel0(23));
//                    skfix.add(SkillUtil.createSkillLevel0(19));
//                    skfix.add(SkillUtil.createSkillLevel0(25));
//                    skfix.add(SkillUtil.createSkillLevel0(28));
//                    player.playerSkill.skills = skfix;
//                    sendThongBao(player, "Fix Skill Thành Công, Hãy Học Lại Skill");
//                    return;
//                }
//            } else if (text.contains("tsm ")) {
//                long power = Long.parseLong(text.replaceAll("tsm ", ""));
//                player.nPoint.power += (long) power;
//                addSMTN(player, (byte) 2, power, false);
//                sendThongBao(player, "Bạn vừa tăng thành công " + power + " sức mạnh");
//                return;
//            } else if (text.contains("gsm ")) {
//                long power = Long.parseLong(text.replaceAll("gsm ", ""));
//                player.nPoint.power -= (long) power;
//                addSMTN(player, (byte) 2, -power, false);
//                sendThongBao(player, "Bạn vừa giảm thành công " + power + " sức mạnh");
//                return;
//            } else if (text.contains("ttn ")) {
//                long power = Long.parseLong(text.replaceAll("ttn ", ""));
//                player.nPoint.tiemNang += (long) power;
//                addSMTN(player, (byte) 2, power, false);
//                sendThongBao(player, "Bạn vừa tăng thành công " + power + " tiềm năng");
//                return;
//            } else if (text.contains("gtn ")) {
//                long power = Long.parseLong(text.replaceAll("gtn ", ""));
//                player.nPoint.tiemNang -= (long) power;
//                addSMTN(player, (byte) 2, -power, false);
//                sendThongBao(player, "Bạn vừa giảm thành công " + power + " tiềm năng");
//                return;
//            } else if (text.equals("nnv") && player.playerTask.taskMain.id <= 31) {
//                TaskService.gI().sendNextTaskMain(player);
//                return;
//            } else if (text.equals("bnv") && player.playerTask.taskMain.id >= 1) {
//                TaskService.gI().sendBackTaskMain(player);
//                return;
//            } else if (text.equals("thread")) {
//                sendThongBao(player, "Current thread: " + Thread.activeCount());
//                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
//                threadSet.forEach((t) -> {
//                    System.out.println(t.getName());
//                });
//                return;
//            } else if (text.equals("toado")) {
//                Service.getInstance().sendThongBao(player, player.location.x + " - " + player.location.y);
//                return;
//            } else if (text.equals("dapdo")) {
//                player.autodapdo = !player.autodapdo;
//                return;
//            }
//            if (text.equals("broly")) {
//                if (Util.canDoWithTime(player.effectSkill.lastTimeUpBroly, 600000)) {
//                    if (player.inventory.itemsBody.get(5).isNotNullItem()) {
//                        if (player.inventory.itemsBody.get(5).template.id == 1479) {
//                            EffectSkillService.gI().sendeffectBROLY(player);
//                            EffectSkillService.gI().setIsBroly(player);
//                            Service.gI().sendSpeedPlayer(player, 0);
//                            Service.gI().Send_Caitrang(player);
//                            Service.gI().sendSpeedPlayer(player, -1);
//                            if (!player.isPet) {
//                                PlayerService.gI().sendInfoHpMp(player);
//                            }
//                            Service.gI().point(player);
//                            Service.gI().Send_Info_NV(player);
//                            Service.gI().sendInfoPlayerEatPea(player);
//                            player.zone.load_Another_To_Me(player);
//                            player.zone.load_Another_To_Me(player);
//                        } else {
//                            Service.gI().sendThongBao(player, "Yêu cầu cải trang Broly");
//                            return;
//                        }
//                    } else {
//                        Service.gI().sendThongBao(player, "Yêu cầu cải trang Broly");
//                        return;
//                    }
//                } else {
//                    Service.gI().sendThongBao(player, "Vui lòng đợi " + TimeUtil.getTimeLeft(player.effectSkill.lastTimeUpBroly, 600));
//                }
//            } else if (text.equals("r")) {
//                try {
//                    activeNamecShenron(player);
//                    return;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
            OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            long totalPhysicalMemorySize = operatingSystemMXBean.getTotalPhysicalMemorySize();
            long freePhysicalMemorySize = operatingSystemMXBean.getFreePhysicalMemorySize();
            long usedPhysicalMemory = totalPhysicalMemorySize - freePhysicalMemorySize;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String cpuUsage = decimalFormat.format(operatingSystemMXBean.getSystemCpuLoad() * 100);
            String usedPhysicalMemoryStr = decimalFormat.format((double) usedPhysicalMemory / (1024 * 1024 * 1024));
            if (text.equals("obito")) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_ADMIN, 15934, "|7| NGỌC RỒNG BY OBITO\b|4| Số Người Online: " + Client.gI().getPlayers().size() + "\n" + "|8|Current Thread: " + (Thread.activeCount() - ServerManager.gI().threadMap)
                        + " : SeeSion " + GirlkunSessionManager.gI().getSessions().size()
                        + "\n|2|CPU : " + cpuUsage + "/100%   " + "  RAM : " + usedPhysicalMemoryStr +"Gb"
                        + "\n|2|Time Start Server: " + ServerManager.timeStart,
                        "Menu Admin", "Call Boss", "Buff Item", "Gifcode", "Nạp Tiền", "List Boss", "Đóng");
                return;
//            } else if (text.startsWith("power")) {
//                try {
//                    long power = Long.parseLong(text.replaceAll("power", ""));
//                    addSMTN(player.pet, (byte) 2, power, false);
//                    return;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } else if (text.startsWith("up")) {
//                try {
//                    long power = Long.parseLong(text.replaceAll("up", ""));
//                    addSMTN(player, (byte) 2, power, false);
//                    return;
//                } catch (Exception e) {
//                }

            } else if (text.startsWith("map")) {
                try {
                    int mapId = Integer.parseInt(text.replace("map", ""));
                    ChangeMapService.gI().changeMapInYard(player, mapId, -1, -1);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (text.startsWith("i")) {
                try {
                    String[] item = text.replace("i", "").split(" ");
                    if (Short.parseShort(item[0]) <= 2065) {
                        Item it = ItemService.gI().createNewItem((short) Short.parseShort(item[0]));
                        if (it != null && item.length == 1) {
                            InventoryServiceNew.gI().addItemBag(player, it);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.gI().sendThongBao(player, "Đã nhận được " + it.template.name);
                        } else if (it != null && item.length == 2 && Client.gI().getPlayer(String.valueOf(item[1])) == null) {
                            it.quantity = Integer.parseInt(item[1]);
                            InventoryServiceNew.gI().addItemBag(player, it);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.gI().sendThongBao(player, "Đã nhận được x" + Integer.valueOf(item[1]) + " " + it.template.name);
                        } else if (it != null && item.length == 2 && Client.gI().getPlayer(String.valueOf(item[1])) != null) {
                            String name = String.valueOf(item[1]);
                            InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name), it);
                            InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name));
                            Service.gI().sendThongBao(player, "Đã buff " + it.template.name + " đến player " + name);
                            Service.gI().sendThongBao(Client.gI().getPlayer(name), "Đã nhận được " + it.template.name);
                        } else if (it != null && item.length == 3 && Client.gI().getPlayer(String.valueOf(item[2])) != null) {
                            String name = String.valueOf(item[2]);
                            it.quantity = Integer.parseInt(item[1]);
                            InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name), it);
                            InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name));
                            Service.gI().sendThongBao(player, "Đã buff x" + Integer.valueOf(item[1]) + " " + it.template.name + " đến player " + name);
                            Service.gI().sendThongBao(Client.gI().getPlayer(name), "Đã nhận được x" + Integer.valueOf(item[1]) + " " + it.template.name);
                        } else {
                            Service.gI().sendThongBao(player, "Không tìm thấy player");
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không tìm thấy item");
                    }
                } catch (NumberFormatException e) {
                    Service.gI().sendThongBao(player, "Không tìm thấy player");
                }
                return;
            } else if (text.equals("give")) {
                Input.gI().createFormSenditem(player);
//            } else if (text.equals("givevip")) {
//                Input.gI().createFormSenditem1(player);
//            } else if (text.equals("giveskh")) {
//                Input.gI().createFormSenditem2(player);
//            } else if (text.equals("thread")) {
//                sendThongBao(player, "Current thread: " + Thread.activeCount());
//                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
//                return;
//            } else if (text.startsWith("tocdo")) {
//                try {
//                    player.nPoint.speed = (byte) Integer.parseInt(text.substring(1));
//                    point(player);
//                    return;
//                } catch (Exception e) {
//                }
            }
        }

//        if (text.equals("banv")) {
//
//            long now = System.currentTimeMillis();
//            if (now >= lasttimechatbanv + 10000) {
//                if (player.muav == false) {
//                    if (player.banv == false) {
//                        player.banv = true;
//                        Service.getInstance().sendThongBao(player, "Đã bật tự động bán vàng khi vàng dưới 1 tỷ !");
//                        lasttimechatbanv = System.currentTimeMillis();
//                        Logger.success("Thằng " + player.name + " chat banv\n");
//                        return;
//                    } else if (player.banv == true) {
//                        player.banv = false;
//                        Service.getInstance().sendThongBao(player, "Đã tắt tự động bán vàng khi vàng dưới 1 tỷ !");
//                        lasttimechatbanv = System.currentTimeMillis();
//                        Logger.success("Thằng " + player.name + " chat banv\n");
//                        return;
//                    }
//                } else {
//                    Service.getInstance().sendThongBao(player, "Vui lòng tắt mua vàng !");
//                    lasttimechatbanv = System.currentTimeMillis();
//                    return;
//                }
//            } else {
//                Service.getInstance().sendThongBao(player, "Spam chat con mọe m !");
//                return;
//            }
//        }

//        if (text.equals("muav")) {
//
//            long now = System.currentTimeMillis();
//            if (now >= lasttimechatmuav + 10000) {
//                if (player.banv == false) {
//                    if (player.muav == false) {
//                        player.muav = true;
//                        Service.getInstance().sendThongBao(player, "Đã bật tự động mua vàng khi vàng trên 500tr !");
//                        lasttimechatmuav = System.currentTimeMillis();
//                        Logger.success("Thằng " + player.name + " chat muav\n");
//                        return;
//                    } else if (player.muav == true) {
//                        player.muav = false;
//                        Service.getInstance().sendThongBao(player, "Đã tắt tự động mua vàng khi vàng trên 500tr !");
//                        lasttimechatmuav = System.currentTimeMillis();
//                        Logger.success("Thằng " + player.name + " chat muav\n");
//                        return;
//                    }
//                } else {
//                    Service.getInstance().sendThongBao(player, "Vui lòng tắt ban vàng !");
//                    lasttimechatmuav = System.currentTimeMillis();
//                    return;
//                }
//            } else {
//                Service.getInstance().sendThongBao(player, "Spam chat con mọe m !");
//                return;
//            }
//        }
//        if (text.equals("a")) {
//            BossManager.gI().showListBoss(player);
//            return;
//
//        }
        if (text.startsWith("ten con la ")) {
            PetService.gI().changeNamePet(player, text.replaceAll("ten con la ", ""));
        }
//        if (text.equals("player")) {
//            NpcService.gI().createMenuConMeo(player, ConstNpc.CLCK, 15934, "|7|Dragon Ball Online\n" + "\n"
//                    + "|1|ACCOUNT SERVICE" + "\n" + "Chào bạn : " + player.name + " | ID: (" + player.id + ") | " + "Map : " + player.zone.map.mapName + "\n"
//                    + "Số Dư Khả Dụng : " + player.getSession().vnd + " VNĐ "
//                    + "\n[Trạng thái tài khoản :" + (player.getSession().actived == true ? " Đã Mở Thành Viên]" : " Chưa Mở Thành Viên]\n")
//                    + "\nChọn [ĐỆ TỬ] để mua đệ tử hoặc đổi skill và hành tinh\n"
//                    + "Chọn [INFO] để xem thông tin nhân vật và nhập giftcode",
//                    "ĐỆ TỬ", "NHẬN\nHỒNG NGỌC", "NHẬN\nTHỎI VÀNG", "GIFTCODE", "INFO", "ĐÓNG");
//            return;
//        }
        if (player.pet != null) {
            switch (text) {
                case "di theo":
                case "follow":
                    player.pet.changeStatus(Pet.FOLLOW);
                    break;
                case "bao ve":
                case "protect":
                    player.pet.changeStatus(Pet.PROTECT);
                    break;
                case "tan cong":
                case "attack":
                    player.pet.changeStatus(Pet.ATTACK);
                    break;
                case "ve nha":
                case "go home":
                    player.pet.changeStatus(Pet.GOHOME);
                    break;
                case "bien hinh":
                case "boom":
                    player.pet.transform();
                    break;
                default:
                    break;
            }
        }

        if (text.length() > 100) {
            text = text.substring(0, 100);
        }
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeUTF(text);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void chatJustForMe(Player me, Player plChat, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeUTF(text);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void Transport(Player pl) {
        Message msg = null;
        try {
            msg = new Message(-105);
            msg.writer().writeShort(pl.maxTime);
            msg.writer().writeByte(pl.type);
            pl.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    private static double getUsedMemoryGB() {
        com.sun.management.OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        long freeMemory = osBean.getFreePhysicalMemorySize();
        long totalMemory = osBean.getTotalPhysicalMemorySize();

        long usedMemory = totalMemory - freeMemory;

        double usedMemoryGB = (double) usedMemory / (1024 * 1024 * 1024);

        return Math.round(usedMemoryGB * 10) / 10.0;
    }

    private static double getTotalMemoryGB() {
        com.sun.management.OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        long totalMemory = osBean.getTotalPhysicalMemorySize();

        double totalMemoryGB = (double) totalMemory / (1024 * 1024 * 1024);

        return Math.round(totalMemoryGB * 10) / 10.0;
    }

    private static double getRAMUsagePercentage() {
        com.sun.management.OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        long totalMemory = osBean.getTotalPhysicalMemorySize();
        long freeMemory = osBean.getFreePhysicalMemorySize();
        long usedMemory = totalMemory - freeMemory;

        double ramUsagePercentage = ((double) usedMemory / totalMemory) * 100;

        return ramUsagePercentage;
    }

    private static double getCPUUsagePercentage() {
        com.sun.management.OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();

        double cpuUsage = osBean.getSystemCpuLoad() * 100;

        return cpuUsage;
    }

    public long exp_level1(long sucmanh) {
        if (sucmanh < 3000) {
            return 3000;
        } else if (sucmanh < 15000) {
            return 15000;
        } else if (sucmanh < 40000) {
            return 40000;
        } else if (sucmanh < 90000) {
            return 90000;
        } else if (sucmanh < 170000) {
            return 170000;
        } else if (sucmanh < 340000) {
            return 340000;
        } else if (sucmanh < 700000) {
            return 700000;
        } else if (sucmanh < 1500000) {
            return 1500000;
        } else if (sucmanh < 15000000) {
            return 15000000;
        } else if (sucmanh < 150000000) {
            return 150000000;
        } else if (sucmanh < 1500000000) {
            return 1500000000;
        } else if (sucmanh < 5000000000L) {
            return 5000000000L;
        } else if (sucmanh < 10000000000L) {
            return 10000000000L;
        } else if (sucmanh < 40000000000L) {
            return 40000000000L;
        } else if (sucmanh < 50010000000L) {
            return 50010000000L;
        } else if (sucmanh < 60010000000L) {
            return 60010000000L;
        } else if (sucmanh < 70010000000L) {
            return 70010000000L;
        } else if (sucmanh < 80010000000L) {
            return 80010000000L;
        } else if (sucmanh < 200010000000L) {
            return 200010000000L;
        }
        return 1000;
    }

    public void point(Player player) {
        player.nPoint.calPoint();
        Send_Info_NV(player);
        if (!player.isPet && !player.isBoss && !player.isNewPet) {
            Message msg;
            try {
                msg = new Message(-42);
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.hpg));
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.mpg));
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.dameg));
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.hpMax));// hp full
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.mpMax));// mp full
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.hp));// hp
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.mp));// mp
                msg.writer().writeByte(Util.TamkjllGH(player.nPoint.speed));// speed
                msg.writer().writeByte(20);
                msg.writer().writeByte(20);
                msg.writer().writeByte(1);
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.dame));// dam base
                msg.writer().writeInt(player.nPoint.def);// def full
                msg.writer().writeByte(player.nPoint.crit);// crit full
                msg.writer().writeLong(player.nPoint.tiemNang);
                msg.writer().writeShort(100);
                msg.writer().writeShort(player.nPoint.defg);
                msg.writer().writeByte(player.nPoint.critg);
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void activeNamecShenron(Player pl) {
        Message msg;
        try {
            msg = new Message(-83);
            msg.writer().writeByte(0);

            msg.writer().writeShort(22);
            msg.writer().writeShort(pl.zone.map.bgId);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) -1);
            msg.writer().writeUTF("");
            msg.writer().writeShort(-1);
            msg.writer().writeShort(-1);
            msg.writer().writeByte(-1);
            //   lastTimeShenronWait = System.currentTimeMillis();
            //   isShenronAppear = true;

            Service.gI().sendMessAllPlayerInMap(pl, msg);
        } catch (Exception e) {
        }
    }

    public void player(Player pl) {
        if (pl == null) {
            return;
        }
        Message msg;
        try {
            msg = messageSubCommand((byte) 0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.playerTask.taskMain.id);
            msg.writer().writeByte(pl.gender);
            msg.writer().writeShort(pl.head);
            msg.writer().writeUTF(pl.name);
            msg.writer().writeByte(0); //cPK
            msg.writer().writeByte(pl.typePk);
            msg.writer().writeLong(pl.nPoint.power);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(pl.gender);
            //--------skill---------

            ArrayList<Skill> skills = (ArrayList<Skill>) pl.playerSkill.skills;

            msg.writer().writeByte(pl.playerSkill.getSizeSkill());

            for (Skill skill : skills) {
                if (skill.skillId != -1) {
                    msg.writer().writeShort(skill.skillId);
                }
            }

            //---vang---luong--luongKhoa
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.gold);
            } else {
                msg.writer().writeInt((int) pl.inventory.gold);
            }
            msg.writer().writeInt(pl.inventory.ruby);
            msg.writer().writeInt(pl.inventory.gem);

            //--------itemBody---------
            ArrayList<Item> itemsBody = (ArrayList<Item>) pl.inventory.itemsBody;
            msg.writer().writeByte(itemsBody.size());
            for (Item item : itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            //--------itemBag---------
            ArrayList<Item> itemsBag = (ArrayList<Item>) pl.inventory.itemsBag;
            msg.writer().writeByte(itemsBag.size());
            for (int i = 0; i < itemsBag.size(); i++) {
                Item item = itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            //--------itemBox---------
            ArrayList<Item> itemsBox = (ArrayList<Item>) pl.inventory.itemsBox;
            msg.writer().writeByte(itemsBox.size());
            for (int i = 0; i < itemsBox.size(); i++) {
                Item item = itemsBox.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            //-----------------
            DataGame.sendHeadAvatar(msg);
            //-----------------
            msg.writer().writeShort(514); //char info id - con chim thông báo
            msg.writer().writeShort(515); //char info id
            msg.writer().writeShort(15934); //char info id
            msg.writer().writeByte(pl.fusion.typeFusion != ConstPlayer.NON_FUSION ? 1 : 0); //nhập thể
            msg.writer().writeInt(333); //deltatime
            msg.writer().writeByte(pl.isNewMember ? 1 : 0); //is new member

//            if (pl.isAdmin()) {
////            msg.writer().writeShort(pl.idAura); //idauraeff
            msg.writer().writeShort(pl.getAura()); //idauraeff
            msg.writer().writeByte(pl.getEffFront());
            msg.writer().writeByte(5);
//            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public Message messageNotLogin(byte command) throws IOException {
        Message ms = new Message(-29);
        ms.writer().writeByte(command);
        return ms;
    }

    public void showYourNumber(Player player, String Number, String result, String finish, int type) {
        Message msg = null;
        try {
            msg = new Message(-126);
            msg.writer().writeByte(type); // 1 = RESET GAME | 0 = SHOW CON SỐ CỦA PLAYER
            if (type == 0) {
                msg.writer().writeUTF(Number);
            } else if (type == 1) {
                msg.writer().writeByte(type);
                msg.writer().writeUTF(result); // 
                msg.writer().writeUTF(finish);
            }
            player.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public Message messageNotMap(byte command) throws IOException {
        Message ms = new Message(-28);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageSubCommand(byte command) throws IOException {
        Message ms = new Message(-30);
        ms.writer().writeByte(command);
        return ms;
    }

    public void addSMTN(Player player, byte type, long param, boolean isOri) {
        if (player.isPet) {
            player.nPoint.powerUp(param);
            player.nPoint.tiemNangUp(param);
            Player master = ((Pet) player).master;

            param = master.nPoint.calSubTNSM(param);
            master.nPoint.powerUp(param);
            master.nPoint.tiemNangUp(param);
            addSMTN(master, type, param, true);
        } else {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            switch (type) {
                case 1:
                    player.nPoint.tiemNangUp(param);
                    break;
                case 2:
                    player.nPoint.powerUp(param);
                    player.nPoint.tiemNangUp(param);
                    break;
                default:
                    player.nPoint.powerUp(param);
                    break;
            }
            PlayerService.gI().sendTNSM(player, type, param);
            if (isOri) {
                if (player.clan != null) {
                    player.clan.addSMTNClan(player, param);
                }
            }
        }
    }

    //    public void congTiemNang(Player pl, byte type, int tiemnang) {
//        Message msg;
//        try {
//            msg = new Message(-3);
//            msg.writer().writeByte(type);// 0 là cộng sm, 1 cộng tn, 2 là cộng cả 2
//            msg.writer().writeInt(tiemnang);// số tn cần cộng
//            if (!pl.isPet) {
//                pl.sendMessage(msg);
//            } else {
//                ((Pet) pl).master.nPoint.powerUp(tiemnang);
//                ((Pet) pl).master.nPoint.tiemNangUp(tiemnang);
//                ((Pet) pl).master.sendMessage(msg);
//            }
//            msg.cleanup();
//            switch (type) {
//                case 1:
//                    pl.nPoint.tiemNangUp(tiemnang);
//                    break;
//                case 2:
//                    pl.nPoint.powerUp(tiemnang);
//                    pl.nPoint.tiemNangUp(tiemnang);
//                    break;
//                default:
//                    pl.nPoint.powerUp(tiemnang);
//                    break;
//            }
//        } catch (Exception e) {
//
//        }
//    }
    public String get_HanhTinh(int hanhtinh) {
        switch (hanhtinh) {
            case 0:
                return "Trái Đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "";
        }
    }

    public String getCurrStrLevel(Player pl) {
        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return "Tân thủ";
        } else if (sucmanh < 15000) {
            return "Tập sự sơ cấp";
        } else if (sucmanh < 40000) {
            return "Tập sự trung cấp";
        } else if (sucmanh < 90000) {
            return "Tập sự cao cấp";
        } else if (sucmanh < 170000) {
            return "Tân binh";
        } else if (sucmanh < 340000) {
            return "Chiến binh";
        } else if (sucmanh < 700000) {
            return "Chiến binh cao cấp";
        } else if (sucmanh < 1500000) {
            return "Vệ binh";
        } else if (sucmanh < 15000000) {
            return "Vệ binh hoàng gia";
        } else if (sucmanh < 150000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 1500000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 5000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 10000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 4";
        } else if (sucmanh < 40000000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 50010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 60010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 70010000000L) {
            return "Giới Vương Thần cấp 11";
        } else if (sucmanh < 80010000000L) {
            return "Giới Vương Thần cấp 2";
        } else if (sucmanh < 100010000000L) {
            return "Giới Vương Thần cấp 3";
        } else if (sucmanh < 150010000000L) {
            return "Vương Diệt Thần";
        } else if (sucmanh < 21100010000000L) {
            return "Hạn Diệt Thần";
        }
        return "Thần Huỷ Diệt cấp 2";
    }

    public int getCurrLevel(Player pl) {
        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return 1;
        } else if (sucmanh < 15000) {
            return 2;
        } else if (sucmanh < 40000) {
            return 3;
        } else if (sucmanh < 90000) {
            return 4;
        } else if (sucmanh < 170000) {
            return 5;
        } else if (sucmanh < 340000) {
            return 6;
        } else if (sucmanh < 700000) {
            return 7;
        } else if (sucmanh < 1500000) {
            return 8;
        } else if (sucmanh < 15000000) {
            return 9;
        } else if (sucmanh < 150000000) {
            return 10;
        } else if (sucmanh < 1500000000) {
            return 11;
        } else if (sucmanh < 5000000000L) {
            return 12;
        } else if (sucmanh < 10000000000L) {
            return 13;
        } else if (sucmanh < 40000000000L) {
            return 14;
        } else if (sucmanh < 50010000000L) {
            return 15;
        } else if (sucmanh < 60010000000L) {
            return 16;
        } else if (sucmanh < 70010000000L) {
            return 17;
        } else if (sucmanh < 80010000000L) {
            return 18;
        } else if (sucmanh < 100010000000L) {
            return 19;
        } else if (sucmanh < 150010000000L) {
            return 20;
        } else if (sucmanh < 2100010000000L) {
            return 21;
        }
        return 21;
    }

    public void hsChar(Player pl, long hp, long mp) {
        Message msg;
        try {
            pl.setJustRevivaled();
            pl.nPoint.setHp(hp);
            pl.nPoint.setMp(mp);
            if (!pl.isPet && !pl.isNewPet) {
                msg = new Message(-16);
                pl.sendMessage(msg);
                msg.cleanup();
                PlayerService.gI().sendInfoHpMpMoney(pl);
            }

            msg = messageSubCommand((byte) 15);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(Util.TamkjllGH(hp));
            msg.writer().writeInt(Util.TamkjllGH(mp));
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            Send_Info_NV(pl);
            PlayerService.gI().sendInfoHpMp(pl);
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendBigBoss(Zone map, int action, int size, int id, int dame) {
        Message msg = null;
        try {
            msg = new Message(102);
            msg.writer().writeByte(action);
            if (action != 6 && action != 7) {
                msg.writer().writeByte(size); // SIZE PLAYER ATTACK
                msg.writer().writeInt(id); // PLAYER ID
                msg.writer().writeInt(dame); // DAME
            }
            sendMessAllPlayerInMap(map, msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendBigBoss2(Zone map, int action, Mob bigboss) {
        Message msg = null;
        try {
            msg = new Message(101);
            msg.writer().writeByte(action);
            msg.writer().writeShort(bigboss.location.x);
            msg.writer().writeShort(bigboss.location.y);
            sendMessAllPlayerInMap(map, msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void charDie(Player pl) {
        Message msg;
        try {
            if (!pl.isPet && !pl.isNewPet && !pl.isPet3) {
                msg = new Message(-17);
                msg.writer().writeByte((int) pl.id);
                msg.writer().writeShort(pl.location.x);
                msg.writer().writeShort(pl.location.y);
                pl.sendMessage(msg);
                msg.cleanup();
            } else if (pl.isPet) {
                ((Pet) pl).lastTimeDie = System.currentTimeMillis();
            }
            if (!pl.isPet && !pl.isPet3 && !pl.isBoss && pl.idNRNM != -1) {
                ItemMap itemMap = new ItemMap(pl.zone, pl.idNRNM, 1, pl.location.x, pl.location.y, -1);
                Service.gI().dropItemMap(pl.zone, itemMap);
                NgocRongNamecService.gI().pNrNamec[pl.idNRNM - 353] = "";
                NgocRongNamecService.gI().idpNrNamec[pl.idNRNM - 353] = -1;
                pl.idNRNM = -1;
                PlayerService.gI().changeAndSendTypePK(pl, ConstPlayer.NON_PK);
                Service.gI().sendFlagBag(pl);
            }
            if (pl.zone.map.mapId == 51) {
                ChangeMapService.gI().changeMapBySpaceShip(pl, 21 + pl.gender, 0, -1);
            }
            msg = new Message(-8);
            msg.writer().writeShort((int) pl.id);
            msg.writer().writeByte(0); //cpk
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();

//            Send_Info_NV(pl);
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void attackMob(Player pl, int mobId) {
        if (pl != null && pl.zone != null) {
            for (Mob mob : pl.zone.mobs) {
                if (mob.id == mobId) {
                    SkillService.gI().useSkill(pl, null, mob, null);

                    break;
                }
            }
        }
    }

    public void doikhandosangkhanquang(Player player, String soluong) {
        try {
            int sl = Integer.parseInt(soluong);
            int tongso = sl * 30;
            Item khando = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1182);
            Item khanquang = ItemService.gI().createNewItem((short) 1183, sl);
            if (player.inventory.ruby < tongso) {
                Service.getInstance().sendThongBao(player, "Không đủ khăn đỏ để đổi: " + sl + " Khăn quàng");
                return;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(player, khando, tongso);
            Service.getInstance().sendMoney(player);
            InventoryServiceNew.gI().addItemBag(player, khanquang);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận: " + sl + " Khăn quàng");
        } catch (Exception e) {
        }
    }

    public void Send_Caitrang(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); //id player
                short head = player.getHead();
                short body = player.getBody();
                short leg = player.getLeg();
                short aura = player.getAura();
                short eff = player.getEffFront();

                msg.writer().writeShort(head);//set head
                msg.writer().writeShort(body);//setbody
                msg.writer().writeShort(leg);//set leg
                msg.writer().writeShort(aura);// set leg
                msg.writer().writeShort(eff);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);//set khỉ
                msg.writer().writeByte(player.effectSkill.isBroly ? 1 : 0);// set biến broly
                msg.writer().writeByte(player.effectSkill.istienhoa ? 1 : 0);// set tiến hóa
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void setNotMonkey(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void setNotBroly(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }
    

    public void setNottienhoa(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendFlagBag(Player pl) {
        Message msg;
        try {
            int Flagbag = pl.getFlagBag();
            if (pl.isPl() && pl.getSession().version >= 222) {
                switch (Flagbag) {
                    case 143:
                        Flagbag = 205;
                        break;
                    case 2010:
                        Flagbag = 246;
                        break;
                }
            }
            msg = new Message(-64);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(Flagbag);
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendThongBaoOK(Player pl, String text) {
        if (pl.isPet || pl.isNewPet || pl.isPet3) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendThongBaoOK(MySession session, String text) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendThongBaoAllPlayer(String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void ChatAll(int iconId, String text) {
        Message msg;
        try {
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendBigMessage(Player player, int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {

        }
    }

    public void sendThongBaoFromAdmin(Player player, String text) {
        sendBigMessage(player, 15934, text);
    }

    public void sendThongBao(Player pl, String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            pl.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void sendThongBaoBenDuoi(String text) {
        Message msg = null;
        try {
            msg = new Message(93);
            msg.writer().writeUTF(text);
            sendMessAllPlayer(msg);
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendThongBao(List<Player> pl, String thongBao) {
        for (int i = 0; i < pl.size(); i++) {
            Player ply = pl.get(i);
            if (ply != null) {
                this.sendThongBao(ply, thongBao);
            }
        }
    }

    public void sendMoney(Player pl) {
        Message msg;
        try {
            msg = new Message(6);
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.gold);
            } else {
                msg.writer().writeInt((int) pl.inventory.gold);
            }
            msg.writer().writeInt(pl.inventory.gem);
            msg.writer().writeInt(pl.inventory.ruby);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendToAntherMePickItem(Player player, int itemMapId) {
        Message msg;
        try {
            msg = new Message(-19);
            msg.writer().writeShort(itemMapId);
            msg.writer().writeInt((int) player.id);
            sendMessAllPlayerIgnoreMe(player, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public static final int[] flagTempId = {363, 364, 365, 366, 367, 368, 369, 370, 371, 519, 520, 747};
    public static final int[] flagIconId = {2761, 2330, 2323, 2327, 2326, 2324, 2329, 2328, 2331, 4386, 4385, 2325};

    public void openFlagUI(Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(0);
            msg.writer().writeByte(flagTempId.length);
            for (int i = 0; i < flagTempId.length; i++) {
                msg.writer().writeShort(flagTempId[i]);
                msg.writer().writeByte(1);
                switch (flagTempId[i]) {
                    case 363:
                        msg.writer().writeByte(73);
                        msg.writer().writeShort(0);
                        break;
                    case 371:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(10);
                        break;
                    default:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(5);
                        break;
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changeFlag(Player pl, int index) {
        Message msg;
        try {
            pl.cFlag = (byte) index;
            msg = new Message(-103);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(index);
            Service.gI().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(index);
            msg.writer().writeShort(flagIconId[index]);
            Service.gI().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            if (pl.pet != null) {
                pl.pet.cFlag = (byte) index;
                msg = new Message(-103);
                msg.writer().writeByte(1);
                msg.writer().writeInt((int) pl.pet.id);
                msg.writer().writeByte(index);
                Service.gI().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();

                msg = new Message(-103);
                msg.writer().writeByte(2);
                msg.writer().writeByte(index);
                msg.writer().writeShort(flagIconId[index]);
                Service.gI().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();
            }
            pl.iDMark.setLastTimeChangeFlag(System.currentTimeMillis());
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendFlagPlayerToMe(Player me, Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(pl.cFlag);
            msg.writer().writeShort(flagIconId[pl.cFlag]);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chooseFlag(Player pl, int index) {
        if (MapService.gI().isMapBlackBallWar(pl.zone.map.mapId)
                || MapService.gI().isMapMaBu(pl.zone.map.mapId)
                || MapService.gI().isMapPVP(pl.zone.map.mapId)
                || MapService.gI().isnguhs(pl.zone.map.mapId)
                || MapService.gI().is22h(pl.zone.map.mapId)
                || MapService.gI().isMapSatan(pl.zone.map.mapId)
                || MapService.gI().isMapVodai(pl.zone.map.mapId)) {
            sendThongBao(pl, "Không thể đổi cờ lúc này!");
            return;
        }
        if (Util.canDoWithTime(pl.iDMark.getLastTimeChangeFlag(), 60000)) {
            changeFlag(pl, index);
        } else {
            sendThongBao(pl, "Không thể đổi cờ lúc này! Vui lòng đợi " + TimeUtil.getTimeLeft(pl.iDMark.getLastTimeChangeFlag(), 60) + " nữa!");
        }
    }

    public void attackPlayer(Player pl, int idPlAnPem) {
        SkillService.gI().useSkill(pl, pl.zone.getPlayerInMap(idPlAnPem), null, null);
    }

    public void releaseCooldownSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                skill.coolDown = 0;
                msg.writer().writeShort(skill.skillId);
                int leftTime = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (leftTime < 0) {
                    leftTime = 0;
                }
                msg.writer().writeInt(leftTime);
            }
            pl.sendMessage(msg);
            pl.nPoint.setMp(pl.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMpMoney(pl);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void sendTimeSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                msg.writer().writeShort(skill.skillId);
                int timeLeft = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (timeLeft < 0) {
                    timeLeft = 0;
                }
                msg.writer().writeInt(timeLeft);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void dropItemMap(Zone zone, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt(3);//
            sendMessAllPlayerInMap(zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void dropItemMapForMe(Player player, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt(3);//
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void showInfoPet(Player pl) {
        if (pl != null && pl.pet != null) {
            Message msg;
            try {
                msg = new Message(-107);
                msg.writer().writeByte(2);
                msg.writer().writeShort(pl.pet.getAvatar());
                msg.writer().writeByte(pl.pet.inventory.itemsBody.size());

                for (Item item : pl.pet.inventory.itemsBody) {
                    if (!item.isNotNullItem()) {
                        msg.writer().writeShort(-1);
                    } else {
                        msg.writer().writeShort(item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());

                        int countOption = item.itemOptions.size();
                        msg.writer().writeByte(countOption);
                        for (ItemOption iop : item.itemOptions) {
                            msg.writer().writeByte(iop.optionTemplate.id);
                            msg.writer().writeShort(iop.param);
                        }
                    }
                }

                msg.writer().writeInt(Util.TamkjllGH(pl.pet.nPoint.hp)); //hp
                msg.writer().writeInt(Util.TamkjllGH(pl.pet.nPoint.hpMax)); //hpfull
                msg.writer().writeInt(Util.TamkjllGH(pl.pet.nPoint.mp)); //mp
                msg.writer().writeInt(Util.TamkjllGH(pl.pet.nPoint.mpMax)); //mpfull
                msg.writer().writeInt(Util.TamkjllGH(pl.pet.nPoint.dame)); //damefull
                msg.writer().writeUTF(pl.pet.name); //name
                msg.writer().writeUTF(getCurrStrLevel(pl.pet)); //curr level
                msg.writer().writeLong(pl.pet.nPoint.power); //power
                msg.writer().writeLong(pl.pet.nPoint.tiemNang); //tiềm năng
                msg.writer().writeByte(pl.pet.getStatus()); //status
                msg.writer().writeShort(pl.pet.nPoint.stamina); //stamina
                msg.writer().writeShort(pl.pet.nPoint.maxStamina); //stamina full
                msg.writer().writeByte(pl.pet.nPoint.crit); //crit
                msg.writer().writeShort(pl.pet.nPoint.def); //def
                int sizeSkill = pl.pet.playerSkill.skills.size();
                msg.writer().writeByte(5); //counnt pet skill
                for (int i = 0; i < pl.pet.playerSkill.skills.size(); i++) {
                    if (pl.pet.playerSkill.skills.get(i).skillId != -1) {
                        msg.writer().writeShort(pl.pet.playerSkill.skills.get(i).skillId);
                    } else {
                        switch (i) {
                            case 1:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 150tr để mở");
                                break;
                            case 2:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 1tỷ5 để mở");
                                break;
                            case 3:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 20tỷ\nđể mở");
                                break;
                            default:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 60tỷ\nđể mở");
                                break;
                        }
                    }
                }

                pl.sendMessage(msg);
                msg.cleanup();

            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void sendSpeedPlayer(Player pl, int speed) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 8);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(speed != -1 ? speed : pl.nPoint.speed);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void setPos(Player player, int x, int y) {
        player.location.x = x;
        player.location.y = y;
        Message msg;
        try {
            msg = new Message(123);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeByte(1);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void getPlayerMenu(Player player, int playerId) {
        Message msg;
        try {
            msg = new Message(-79);
            Player pl = player.zone.getPlayerInMap(playerId);
            if (pl != null) {
                msg.writer().writeInt(playerId);
                msg.writer().writeLong(pl.nPoint.power);
                msg.writer().writeUTF(Service.gI().getCurrStrLevel(pl));
                player.sendMessage(msg);
            }
            msg.cleanup();
            if (player.isAdmin()) {
                SubMenuService.gI().showMenuForAdmin(player);
            }
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void hideWaitDialog(Player pl) {
        Message msg;
        try {
            msg = new Message(-99);
            msg.writer().writeByte(-1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chatPrivate(Player plChat, Player plReceive, String text) {
        Message msg;
        try {
            msg = new Message(92);
            msg.writer().writeUTF(plChat.name);
            msg.writer().writeUTF("|7|" + text);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeShort(plChat.getHead());
            msg.writer().writeShort(-1);
            msg.writer().writeShort(plChat.getBody());
            msg.writer().writeShort(plChat.getFlagBag()); //bag
            msg.writer().writeShort(plChat.getLeg());
            msg.writer().writeByte(1);
            plChat.sendMessage(msg);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changePassword(Player player, String oldPass, String newPass, String rePass) {
        if (player.getSession().pp.equals(oldPass)) {
            if (newPass.length() >= 5) {
                if (newPass.equals(rePass)) {
                    player.getSession().pp = newPass;
                    try {
                        GirlkunDB.executeUpdate("update account set password = ? where id = ? and username = ?",
                                rePass, player.getSession().userId, player.getSession().uu);
                        Service.gI().sendThongBao(player, "Đổi mật khẩu thành công!");
                    } catch (Exception ex) {
                        Service.gI().sendThongBao(player, "Đổi mật khẩu thất bại!");
                        Logger.logException(Service.class, ex);
                    }
                } else {
                    Service.gI().sendThongBao(player, "Mật khẩu nhập lại không đúng!");
                }
            } else {
                Service.gI().sendThongBao(player, "Mật khẩu ít nhất 5 ký tự!");
            }
        } else {
            Service.gI().sendThongBao(player, "Mật khẩu cũ không đúng!");
        }
    }

    public void switchToCreateChar(MySession session) {
        Message msg;
        try {
            msg = new Message(2);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendCaption(MySession session, byte gender) {
        Message msg;
        try {
            msg = new Message(-41);
            msg.writer().writeByte(Manager.CAPTIONS.size());
            for (String caption : Manager.CAPTIONS) {
                msg.writer().writeUTF(caption.replaceAll("%1", gender == ConstPlayer.TRAI_DAT ? "Trái đất"
                        : (gender == ConstPlayer.NAMEC ? "Namếc" : "Xayda")));
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendHavePet(Player player) {
        Message msg;
        try {
            msg = new Message(-107);
            msg.writer().writeByte(player.pet == null ? 0 : 1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendWaitToLogin(MySession session, int secondsWait) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(secondsWait);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendMessage(MySession session, int cmd, String path) {
        Message msg;
        try {
            msg = new Message(cmd);
            msg.writer().write(FileIO.readFile(path));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createItemMap(Player player, int tempId) {
        ItemMap itemMap = new ItemMap(player.zone, tempId, 1, player.location.x, player.location.y, player.id);
        dropItemMap(player.zone, itemMap);
    }

    public void sendNangDong(Player player) {
        Message msg;
        try {
            msg = new Message(-97);
            msg.writer().writeInt(100);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void setClientType(MySession session, Message msg) {
        try {
            session.typeClient = (msg.reader().readByte());//client_type
            session.zoomLevel = msg.reader().readByte();//zoom_level
            msg.reader().readBoolean();//is_gprs
            msg.reader().readInt();//width
            msg.reader().readInt();//height
            msg.reader().readBoolean();//is_qwerty
            msg.reader().readBoolean();//is_touch
            String platform = msg.reader().readUTF();
            String[] arrPlatform = platform.split("\\|");
            session.version = Integer.parseInt(arrPlatform[1].replaceAll("\\.", ""));

//            System.out.println(platform);
        } catch (Exception e) {
        } finally {
            msg.cleanup();
        }
        DataGame.sendLinkIP(session);
    }

    public void DropVeTinh(Player pl, Item item, Zone map, int x, int y) {
        ItemMap itemMap = new ItemMap(map, item.template, item.quantity, x, y, pl.id);
        itemMap.options = item.itemOptions;
        map.addItem(itemMap);
        Message msg = null;
        try {
            msg = new Message(68);
            msg.writer().writeShort(itemMap.itemMapId);
            msg.writer().writeShort(itemMap.itemTemplate.id);
            msg.writer().writeShort(itemMap.x);
            msg.writer().writeShort(itemMap.y);
            msg.writer().writeInt(-2);
            msg.writer().writeShort(200);
            sendMessAllPlayerInMap(map, msg);
        } catch (Exception exception) {

        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void stealMoney(Player pl, int stealMoney) {//danh cho boss an trom
        Message msg;
        try {
            msg = new Message(95);
            msg.writer().writeInt(stealMoney);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

}
