package com.girlkun.models.npc;

import com.girlkun.consts.ConstNpc;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.network.io.Message;
import com.girlkun.server.Client;
import com.girlkun.services.MapService;
import com.girlkun.services.NpcService;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import jdk.internal.org.jline.utils.Log;

public abstract class Npc implements IAtionNpc {

    public int mapId;
    public Map map;

    public int status;

    public int cx;

    public int cy;

    public int tempId;

    public int avartar;

    public BaseMenu baseMenu;

    public long LastTimeAutoChat = 0;

    public void AutoChat() {

        switch (tempId) {
//            case 39:
//            case 54:
            case 22:
            case 13:
                for (Player pl : Client.gI().getPlayers()) {
                    if (pl.zone.map.mapId == mapId && pl.isPl()) {
                        if (System.currentTimeMillis() - LastTimeAutoChat > 5000) {
                            LastTimeAutoChat = System.currentTimeMillis();
                        }
                        //      npcChat(pl, getText(tempId));
                    }
                }
                break;
            default:
                break;
        }
    }

    private String getText(int id) {

        if (id == 13) {
            return textQuyLao[Util.nextInt(0, textQuyLao.length - 1)];
        } else if (id == 54) {
            return textLyTieuNuong[Util.nextInt(0, textLyTieuNuong.length - 1)];
        } else if (id == 22) {
            return textLyTrongTai[Util.nextInt(0, textLyTrongTai.length - 1)];
        } else {
            return textSanta[Util.nextInt(0, textSanta.length - 1)];
        }
    }

    private static final String[] textLyTieuNuong = new String[]{
        "Con số gì đây..",};

    private static final String[] textLyTrongTai = new String[]{
        "Đại Hội Võ Thuật lần thứ 23 đã chính thức khai mạc",
        "Còn chờ gì nữa mà không đăng kí tham gia để nhận nhiều phẩn quà hấp dẫn"
    };

    private static final String[] textSanta = new String[]{
        "Mọi sự cố gắng đều sẽ được đền đáp xứng đáng.",};

    private static final String[] textQuyLao = new String[]{
        "Lá Là La...",
        "Ngày tươi đẹp nhất là ngày được may mắn nhìn thấy em",
        "Tình yêu không cần phải hoàn hảo, chỉ cần sự chân thật. ",
        "Tôi là một ngọn lửa cháy lên, nhưng sẽ không thể bị lụi tàn. "
    };

    protected Npc(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        this.map = MapService.gI().getMapById(mapId);
        this.mapId = mapId;
        this.status = status;
        this.cx = cx;
        this.cy = cy;
        this.tempId = tempId;
        this.avartar = avartar;
        Manager.NPCS.add(this);
    }

    public void initBaseMenu(String text) {
        text = text.substring(1);
        String[] data = text.split("\\|");
        baseMenu = new BaseMenu();
        baseMenu.npcId = tempId;
        baseMenu.npcSay = data[0].replaceAll("<>", "\n");
        baseMenu.menuSelect = new String[data.length - 1];
        for (int i = 0; i < baseMenu.menuSelect.length; i++) {
            baseMenu.menuSelect[i] = data[i + 1].replaceAll("<>", "\n");
        }
    }

    public void createMenuConMeo(Player player, int indexMenu, int avatar, String npcSay, String... menuSelect) {
        createMenu(player, indexMenu, ConstNpc.CON_MEO, avatar, npcSay, menuSelect);
    }

    private void createMenu(Player player, int indexMenu, byte npcTempId, int avatar, String npcSay, String... menuSelect) {
        if (player == null) {
            return;
        }
        Message msg;
        try {
            player.iDMark.setIndexMenu(indexMenu);
            msg = new Message(32);
            msg.writer().writeShort(npcTempId);
            msg.writer().writeUTF(npcSay);
            msg.writer().writeByte(menuSelect.length);
            for (String menu : menuSelect) {
                msg.writer().writeUTF(menu);
            }
            if (avatar != -1) {
                msg.writer().writeShort(avatar);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(NpcService.class, e);
        }
    }

    public void createOtherMenu(Player player, int indexMenu, String npcSay, String... menuSelect) {
        Message msg;
        try {
            player.iDMark.setIndexMenu(indexMenu);
            msg = new Message(32);
            msg.writer().writeShort(tempId);
            msg.writer().writeUTF(npcSay);
            msg.writer().writeByte(menuSelect.length);
            for (String menu : menuSelect) {
                msg.writer().writeUTF(menu);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createOtherMenu(Player player, int indexMenu, String npcSay, String[] menuSelect, Object object) {
        NpcFactory.PLAYERID_OBJECT.put(player.id, object);
        Message msg;
        try {
            player.iDMark.setIndexMenu(indexMenu);
            msg = new Message(32);
            msg.writer().writeShort(tempId);
            msg.writer().writeUTF(npcSay);
            msg.writer().writeByte(menuSelect.length);
            for (String menu : menuSelect) {
                msg.writer().writeUTF(menu);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
            try {
                if (baseMenu != null) {
                    baseMenu.openMenu(player);
                } else {
                    Message msg;
                    msg = new Message(32);
                    msg.writer().writeShort(tempId);
                    msg.writer().writeUTF("|2|Ngọc Rồng Akaya by Obito , sever beta "
                            + "\n Full Chức Năng Hấp Dẫn Sẽ Ra Mắt Khi Open");
                    msg.writer().writeByte(1);
                    msg.writer().writeUTF("Không");
                    player.sendMessage(msg);
                    msg.cleanup();
                }
            } catch (Exception e) {
                Logger.logException(Npc.class, e);
            }
        }
    }

    public void npcChat(Player player, String text) {
        Message msg;
        try {
            msg = new Message(124);
            msg.writer().writeShort(tempId);
            msg.writer().writeUTF(text);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }
    public void npcChat(Zone zone, String text) {
        Message msg;
        try {
            msg = new Message(124);
            msg.writer().writeShort(tempId);
            msg.writer().writeUTF(text);
            Service.getInstance().sendMessAllPlayerInMap(zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void npcChat(String text) {
        Message msg;
        try {
            msg = new Message(124);
            msg.writer().writeShort(tempId);
            msg.writer().writeUTF(text);
            for (Zone zone : map.zones) {
                Service.gI().sendMessAllPlayerInMap(zone, msg);
            }
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public boolean canOpenNpc(Player player) {
        if (this.tempId == ConstNpc.DAU_THAN) {
            if (player.zone.map.mapId == 21
                    || player.zone.map.mapId == 22
                    || player.zone.map.mapId == 23) {
                return true;
            } else {
                Service.gI().hideWaitDialog(player);
                Service.gI().sendThongBao(player, "Không thể thực hiện");
                return false;
            }
        }
        if (player.zone.map.mapId == this.mapId
                && Util.getDistance(this.cx, this.cy, player.location.x, player.location.y) <= 60 && !player.isfight && !player.isfight1 && !player.istry && !player.istry1) {
            player.iDMark.setNpcChose(this);
            return true;

        }
        if (player.zone.map.mapId == this.mapId
                && player.zone.map.mapId == 50 && Util.getDistance(this.cx, this.cy, player.location.x, player.location.y) <= 60) {
            player.iDMark.setNpcChose(this);
            return true;

        } else {
            Service.gI().hideWaitDialog(player);
            if (player.isfight || player.isfight1 || player.istry || player.istry1) {
                this.npcChat(player, "Hãy đánh bại ta trước");
            } else {
                Service.gI().sendThongBao(player, "Không thể thực hiện khi đứng quá xa");
            }
            return false;
        }
    }

}
