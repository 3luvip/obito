package com.girlkun.services.func;

import com.girlkun.database.GirlkunDB;
import com.girlkun.consts.ConstNpc;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.Zone;
import com.girlkun.models.npc.NauBanh;
import com.girlkun.models.npc.Npc;
import com.girlkun.models.npc.NpcManager;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import com.girlkun.result.GirlkunResultSet;
import com.girlkun.server.Client;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerNotify;
import com.girlkun.services.Service;
import com.girlkun.services.GiftService;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.NapThe;
//import com.girlkun.services.NapThe;
import com.girlkun.services.NpcService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.SkillService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Input {
    public static final int QUY_DOI_KHAN_QUANG = 530;
    public static String LOAI_THE;
    public static String MENH_GIA;
    private static final Map<Integer, Object> PLAYER_ID_OBJECT = new HashMap<Integer, Object>();
    public static final int CHATALL = 521;
    public static final int CHANGE_PASSWORD = 500;
    public static final int GIFT_CODE = 501;
    public static final int FIND_PLAYER = 502;
    public static final int CHANGE_NAME = 503;
    public static final int CHOOSE_LEVEL_BDKB = 504;
    public static final int NAP_THE = 505;
    public static final int THONG_BAO = 604;
    public static final int THONG_BAO_RIENG = 605;
    public static final int CHANGE_NAME_BY_ITEM = 5061;
    public static final int GIVE_IT = 507;
    public static final int TANG_NGOC_HONG = 837;
    public static final int CHOOSE_LEVEL_GAS = 515;
    public static final int CHOOSE_LEVEL_KGHD = 5143;
    public static final int CHOOSE_LEVEL_CDRD = 51522;
    public static final int NAP_COIN = 520;
    public static final int QUY_DOI_COIN = 508;
    public static final int QUY_DOI_HONG_NGOC = 509;
    public static final int DONATE_CS = 529;
    public static final int TAI = 510;
    public static final int XIU = 511;
    public static final int TAMHOA = 1000;
    public static final int UseGold = 3;
    public static final int TAI2 = 1001;
    public static final int XIU2 = 1002;
    public static final int TAMHOA2 = 1003;
    public static final int CON_SO_MAY_MAN_NGOC = 5071;

    public static final int CON_SO_MAY_MAN_VANG = 5081;
    public static final int TAI3 = 1004;
    public static final int XIU3 = 1005;
    public static final int TAMHOA3 = 1006;

    public static final int TAI4 = 1007;
    public static final int XIU4 = 1008;
    public static final int TAMHOA4 = 1009;

    public static final int TAI5 = 1010;
    public static final int XIU5 = 1011;
    public static final int TAMHOA5 = 1012;

    public static final int TAI6 = 1013;
    public static final int XIU6 = 1014;
    public static final int TAMHOA6 = 1015;

    public static final int TAI8 = 1016;
    public static final int XIU8 = 1017;
    public static final int TAMHOA8 = 1018;

    public static final int TAI9 = 1019;
    public static final int XIU9 = 1020;
    public static final int TAMHOA9 = 1021;

    public static final int TAI10 = 1022;
    public static final int XIU10 = 1023;
    public static final int TAMHOA10 = 1024;

    public static final int TAI11 = 1025;
    public static final int XIU11 = 1026;
    public static final int TAMHOA11 = 1027;

    public static final int TAI7 = 1028;
    public static final int XIU7 = 1029;
    public static final int TAMHOA7 = 1030;

    public static final int SEND_ITEM = 512;
    public static final int SEND_ITEM_OP = 513;
    public static final int SEND_ITEM_SKH = 514;
    public static final int XIU_taixiu = 5164;
    public static final int TAI_taixiu = 5165;
    private static final int NAU_BANH_TET = 6216;
    public static final int NAU_BANH_CHUNG = 373;
    public static final int GIVE_ITEM_BAR = 527;
    public static final byte NUMERIC = 0;
    public static final byte ANY = 1;
    public static final byte PASSWORD = 2;

    private static Input intance;

    private Input() {

    }

    public static Input gI() {
        if (intance == null) {
            intance = new Input();
        }
        return intance;
    }

    public void doInput(Player player, Message msg) {
        try {
            String[] text = new String[msg.reader().readByte()];
            for (int i = 0; i < text.length; i++) {
                text[i] = msg.reader().readUTF();
            }
            switch (player.iDMark.getTypeInput()) {
                case GIVE_ITEM_BAR:
                   String name1 = text[0];
                   int id1 = Integer.valueOf(text[1]);
                   int q1 = Integer.valueOf(text[2]);
                   String option = text[3];
                   String param = text[4];
                   String[] option1 = option.split("-");
                   String[] param1 = param.split("-");
                   int length1 = option1.length;
                   int length2 = param1.length;
                   if (length1 == length2){
                   if(Client.gI().getPlayer(name1) != null){
                    Item item = ItemService.gI().createNewItem(((short)id1));
                    item.quantity = q1;
//                    InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name1), item);
//                    InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name1));
                    for (int i = 0; i < length1; i++) {
                        String option2 = option1[i];
                        String param2 = param1[i];
                        int opt;
                        int par;
                        try {
                            opt = Integer.parseInt(option2);
                            par = Integer.parseInt(param2);
                            item.itemOptions.add(new ItemOption(opt, par));
                        } catch (NumberFormatException e) {
                            break;
                        }
                        
                    }
                    InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name1), item);
                    InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name1));
                    Service.getInstance().sendThongBao(Client.gI().getPlayer(name1), "Nhận " + item.template.name +" từ " + player.name);
                    } else{
                       Service.getInstance().sendThongBao(player, "Không online");
                    }
                    } else {
                            Service.getInstance().sendThongBao(player, "Nhập dữ liệu không đúng");
                            }
                    break;
                case THONG_BAO:
                    String contentServer = text[0];
                    Integer typeNotiServer = Integer.parseInt(text[1]);
                    switch (typeNotiServer) {
                        case 1:
                            Service.getInstance().sendThongBaoAllPlayer(contentServer);
                            break;
                        case 2:
                            ServerNotify.gI().notify(contentServer);
                            break;
                        default:
                            Service.getInstance().sendThongBaoAllPlayer(contentServer);
                            break;
                    }
                    break;
                case UseGold:
                    int Gold = Integer.parseInt(text[0]);
                    Item thoivangchange = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 457) {
                            thoivangchange = item;
                            break;
                        }
                    }
                    if(thoivangchange.quantity >= Gold)
                    {
                        long goldsum =  (long)(500000000L* (long)Gold);
                        if(player.inventory.gold + goldsum >  Inventory.LIMIT_GOLD)
                        {
                            Service.gI().sendThongBao(player, "Số vàng quy đổi vượt quá giới hạn 20 tỉ");
                        }
                        else {
                        player.inventory.gold += (long)(500000000L* (long)Gold);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, thoivangchange, Gold);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendMoney(player);
                        Service.gI().sendThongBao(player, "Đổi Thành Công");}
                    }
                    else 
                    {
                        Service.gI().sendThongBao(player, "Số lượng không đủ");
                    }
                    break;     
                case THONG_BAO_RIENG:
                    Player playerNotice = Client.gI().getPlayer(text[2]);
                    Integer typeNotiPrivate = Integer.parseInt(text[1]);
                    String contentPrivate = text[0];
                    if (playerNotice != null) {
                        switch (typeNotiPrivate) {
                            case 1:
                                Service.gI().sendThongBao(playerNotice, contentPrivate);
                                break;
                            case 2:
                                Service.gI().sendThongBaoFromAdmin(player, contentPrivate);
                                break;
                            default:
                                Service.gI().sendThongBaoFromAdmin(player, contentPrivate);
                                break;
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;
                case TAI_taixiu:
                    int sotvxiu1 = Integer.valueOf(text[0]);
                    try {
                        if (sotvxiu1 >= 1000 && sotvxiu1 <= 1000000000) {
                            if (player.inventory.ruby >= sotvxiu1) {
                                player.inventory.ruby -= sotvxiu1;
                                player.goldTai += sotvxiu1;
                                TaiXiu.gI().goldTai += sotvxiu1;
                                Service.gI().sendThongBao(player, "Bạn đã đặt " + Util.format(sotvxiu1) + " Hồng ngọc vào TÀI");
                                TaiXiu.gI().addPlayerTai(player);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                                PlayerDAO.updatePlayer(player);
                            } else {
                                Service.gI().sendThongBao(player, "Bạn không đủ Hồng ngọc để chơi.");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Cược ít nhất 1.000 - nhiều nhất 1.000.000.000 Hồng ngọc");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;
                case DONATE_CS:
                    int csbang = Integer.parseInt(text[0]);
                    Item cscanhan = InventoryServiceNew.gI().findItemBag(player, 1637);
                    // if (cscanhan == null || player.clanMember.memberPoint < 1) {
                    //     Service.gI().sendThongBao(player, "Số điểm capsule bản thân không đủ để thực hiện");
                    //     break;
                    //  }  
                    InventoryServiceNew.gI().subQuantityItemsBag(player, cscanhan, csbang);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    //     player.clanMember.memberPoint -= csbang;
                    player.clan.capsuleClan += csbang;
                    player.clanMember.clanPoint += csbang;
                    //player.clan.reloadClanMember();
                    Service.gI().sendThongBao(player, "bạn đã quyên góp " + csbang + "");
                    break;
                case XIU_taixiu:
                    int sotvxiu2 = Integer.valueOf(text[0]);
                    try {
                        if (sotvxiu2 >= 1000 && sotvxiu2 <= 1000000000) {
                            if (player.inventory.ruby >= sotvxiu2) {
                                player.inventory.ruby -= sotvxiu2;
                                player.goldXiu += sotvxiu2;
                                TaiXiu.gI().goldXiu += sotvxiu2;
                                Service.gI().sendThongBao(player, "Bạn đã đặt " + Util.format(sotvxiu2) + " Hồng ngọc vào XỈU");
                                TaiXiu.gI().addPlayerXiu(player);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                                PlayerDAO.updatePlayer(player);
                            } else {
                                Service.gI().sendThongBao(player, "Bạn không đủ Hồng ngọc để chơi.");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Cược ít nhất 1.000 - nhiều nhất 1.000.000.000 Hồng ngọc ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                        System.out.println("nnnnn2  ");
                    }
                    break;
                case CHATALL:
                    String chat = text[0];
                    Service.gI().sendThongBaoAllPlayer("|7|[ - XIN THÔNG BÁO٭ ]" + "\n"
                            + "|1|" + (player.isAdmin() ? " " : "") + chat + "\n");
                    break;
                case NAU_BANH_TET:
                    nauBanhTet(player, text[0]);
                    break;
                case NAU_BANH_CHUNG:
                    nauBanhChung(player, text[0]);
                    break;
                case GIVE_IT:
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        int quantityItemBuff = Integer.parseInt(text[2]);
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        if (pBuffItem != null) {
                            String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                            if (idItemBuff == -1) {
                                pBuffItem.inventory.gold = Math.min(pBuffItem.inventory.gold + (long) quantityItemBuff, Inventory.LIMIT_GOLD);
                                txtBuff += quantityItemBuff + " vàng\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -2) {
                                pBuffItem.inventory.gem = Math.min(pBuffItem.inventory.gem + quantityItemBuff, 2000000000);
                                txtBuff += quantityItemBuff + " ngọc\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -3) {
                                pBuffItem.inventory.ruby = Math.min(pBuffItem.inventory.ruby + quantityItemBuff, 2000000000);
                                txtBuff += quantityItemBuff + " ngọc khóa\b";
                                Service.getInstance().sendMoney(player);
                            } else {
                                Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                                itemBuffTemplate.quantity = quantityItemBuff;
                                InventoryServiceNew.gI().addItemBag(pBuffItem, itemBuffTemplate);
                                InventoryServiceNew.gI().sendItemBags(pBuffItem);
                                txtBuff += "x" + quantityItemBuff + " " + itemBuffTemplate.template.name + "\b";
                            }
                            NpcService.gI().createTutorial(player, 24, txtBuff);
                            if (player.id != pBuffItem.id) {
                                NpcService.gI().createTutorial(pBuffItem, 24, txtBuff);
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Player không online");
                        }
                        break;
                    }
                    break;
                case TANG_NGOC_HONG:
                    Player pBuffItem1 = Client.gI().getPlayer(text[0]);
                    int slItemBuff1 = Integer.parseInt(text[1]);
                    if (Client.gI().getPlayer(text[0]) != null && player.inventory.ruby >= slItemBuff1) {
                        pBuffItem1.inventory.ruby = Math.min(pBuffItem1.inventory.ruby + slItemBuff1, 2000000000);
                        player.inventory.ruby = Math.min(player.inventory.ruby - slItemBuff1, 2000000000);
                        Service.getInstance().sendMoney(pBuffItem1);
                        Service.getInstance().sendMoney(player);
                        Service.gI().sendThongBao(Client.gI().getPlayer(text[0]), "Đã nhận được " + slItemBuff1 + " ngọc hồng từ " + player.name);
                        Service.gI().sendThongBao(player, "Đã tặng " + slItemBuff1 + " hồng ngọc cho " + pBuffItem1.name);
                    } else if (player.inventory.ruby < slItemBuff1) {
                        Service.gI().sendThongBao(player, "Bạn không có đủ hồng ngọc để tặng cho người chơi khác !");
                    } else {
                        Service.gI().sendThongBao(player, "Người chơi bạn muốn tặng không tồn tại hoặc đang không có trong game !");
                    }
                    break;
                case NAP_COIN: {
                    String name = text[0];
                    int vnd = Integer.valueOf(text[1]);
                    Player pl = Client.gI().getPlayer(name);
                    if (pl != null) {
                        pl.getSession().vnd += vnd;
                        PreparedStatement ps = null;
                        try (Connection con = GirlkunDB.getConnection();) {
                            ps = con.prepareStatement("update account set vnd = (vnd + ?) ,tongnap = (tongnap + ?) where id = ?");
                            ps.setInt(1, vnd);
                            ps.setInt(2, vnd);
                            ps.setInt(3, pl.getSession().userId);
                            ps.executeUpdate();
                        } catch (Exception e) {
                            Logger.logException(PlayerDAO.class, e, "Lỗi update coin " + pl.name);
                        } finally {
                            try {
                                ps.close();
                            } catch (SQLException ex) {
                                System.out.println("Lỗi khi update tongnap");
                            }
                        }
                        Service.getInstance().sendThongBao(player, "Đã nạp " + vnd + " Vnd cho " + pl.name);
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không online");
                    }
                    break;
                }
                case CHANGE_PASSWORD:
                    Service.gI().changePassword(player, text[0], text[1], text[2]);
                    break;
                case GIFT_CODE: {
                    String textLevel = text[0];
                    Input.gI().addItemGiftCodeToPlayer(player, textLevel);
                    break;
                }
                case FIND_PLAYER:
                    Player pl = Client.gI().getPlayer(text[0]);
                    if (pl != null) {
                        int sl = InventoryServiceNew.gI().findItemBag(pl, (short) 457) == null ? 0 : InventoryServiceNew.gI().findItemBag(pl, (short) 457).quantity;
                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_FIND_PLAYER, 12639, "|7|[obito]\n"
                                + "|1|Player Name : " + pl.name
                                + "\nHồng Ngọc Inventory : " + pl.inventory.ruby
                                + "\nCoin Vnđ Inventory : " + pl.getSession().vnd
                                + "\nThỏi Vàng Inventory : " + sl
                                + "\nAccount Status : " + (pl.isAdmin() ? "Key Controller" : "PlayerOnline ")
                                + "\n|7|[Dev by obito]",
                                new String[]{"Đi tới\n" + pl.name, "Gọi " + pl.name + "\ntới đây", "Đổi tên", "Ban", "Kick"},
                                pl);
                    } else {
                        Service.gI().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;
                case QUY_DOI_KHAN_QUANG: {
                    Service.getInstance().doikhandosangkhanquang(player, text[0]);
                    break;
                }    
                case CHANGE_NAME: {
                    Player plChanged = (Player) PLAYER_ID_OBJECT.get((int) player.id);
                    if (plChanged != null) {
                        if (GirlkunDB.executeQuery("select * from player where name = ?", text[0]).next()) {
                            Service.gI().sendThongBao(player, "Tên nhân vật đã tồn tại");
                        } else {
                            plChanged.name = text[0];
                            GirlkunDB.executeUpdate("update player set name = ? where id = ?", plChanged.name, plChanged.id);
                            Service.gI().player(plChanged);
                            Service.gI().Send_Caitrang(plChanged);
                            Service.gI().sendFlagBag(plChanged);
                            Zone zone = plChanged.zone;
                            ChangeMapService.gI().changeMap(plChanged, zone, plChanged.location.x, plChanged.location.y);
                            Service.gI().sendThongBao(plChanged, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
                            Service.gI().sendThongBao(player, "Đổi tên người chơi thành công");
                        }
                    }
                }
                break;
                case CHANGE_NAME_BY_ITEM: {
                    if (player != null) {
                        if (GirlkunDB.executeQuery("select * from player where name = ?", text[0]).next()) {
                            Service.gI().sendThongBao(player, "Tên nhân vật đã tồn tại");
                            createFormChangeNameByItem(player);
                        } else {
                            Item theDoiTen = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2006);
                            if (theDoiTen == null) {
                                Service.gI().sendThongBao(player, "Không tìm thấy thẻ đổi tên");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(player, theDoiTen, 1);
                                player.name = text[0];
                                GirlkunDB.executeUpdate("update player set name = ? where id = ?", player.name, player.id);
                                Service.gI().player(player);
                                Service.gI().Send_Caitrang(player);
                                Service.gI().sendFlagBag(player);
                                Zone zone = player.zone;
                                ChangeMapService.gI().changeMap(player, zone, player.location.x, player.location.y);
                                Service.gI().sendThongBao(player, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
                            }
                        }
                    }
                }
                break;

                case XIU:
                    int sotvxiu = Integer.valueOf(text[0]);
                    Item tv2 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 457) {
                            tv2 = item;
                            break;
                        }
                    }
                    try {
                        if (tv2 != null && tv2.quantity >= sotvxiu) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv2, sotvxiu);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Item tvthang = ItemService.gI().createNewItem((short) 457);
                                    tvthang.quantity = (int) Math.round(sotvxiu * 2);
                                    InventoryServiceNew.gI().addItemBag(player, tvthang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sotvxiu
                                            + " Thỏi vàng vào Xỉu" + "\nKết quả : Xỉu" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sotvxiu
                                            + " Thỏi vàng vào Xỉu" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sotvxiu
                                            + " Thỏi vàng vào Xỉu" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ thỏi vàng để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                case TAI:
                    int sotvtai = Integer.valueOf(text[0]);
                    Item tv1 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 457) {
                            tv1 = item;
                            break;
                        }
                    }
                    try {
                        if (tv1 != null && tv1.quantity >= sotvtai) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv1, sotvtai);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sotvtai
                                            + " Thỏi vàng vào Tài" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sotvtai
                                            + " Thỏi vàng vào Tài" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {
                                    Item tvthang = ItemService.gI().createNewItem((short) 457);
                                    tvthang.quantity = (int) Math.round(sotvtai * 2);
                                    InventoryServiceNew.gI().addItemBag(player, tvthang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sotvtai
                                            + " Thỏi vàng vào Tài" + "\nKết quả : Tài" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ thỏi vàng để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case TAMHOA:
                    int sotvtamhoa = Integer.valueOf(text[0]);
                    Item tv3 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 457) {
                            tv3 = item;
                            break;
                        }
                    }
                    try {
                        if (tv3 != null && tv3.quantity >= sotvtamhoa) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv3, sotvtamhoa);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sotvtamhoa
                                            + " Thỏi vàng vào Tam Hoa" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Item tvthang = ItemService.gI().createNewItem((short) 457);
                                    tvthang.quantity = (int) Math.round(sotvtamhoa * 10);
                                    InventoryServiceNew.gI().addItemBag(player, tvthang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sotvtamhoa
                                            + " Thỏi vàng vào Tam Hoa" + "\nKết quả : Tam Hoa" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {

                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sotvtamhoa
                                            + " Thỏi vàng vào Tam Hoa" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ thỏi vàng để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;
//------------------------ TÀI XỈU BẰNG ĐÁ NÂNG CẤP ------------------------------------                    
                case XIU2:
                    int sodanc = Integer.valueOf(text[0]);
                    Item da2 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 220) {
                            da2 = item;
                            break;
                        }
                    }
                    try {
                        if (da2 != null && da2.quantity >= sodanc) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da2, sodanc);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 220);
                                    dathang.quantity = (int) Math.round(sodanc * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Xỉu" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                case TAI2:
                    int sodanc1 = Integer.valueOf(text[0]);
                    Item da1 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 220) {
                            da1 = item;
                            break;
                        }
                    }
                    try {
                        if (da1 != null && da1.quantity >= sodanc1) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da1, sodanc1);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc1
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc1
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 220);
                                    dathang.quantity = (int) Math.round(sodanc1 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc1
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Tài" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case TAMHOA2:
                    int sodatamhoa = Integer.valueOf(text[0]);
                    Item da3 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 220) {
                            da3 = item;
                            break;
                        }
                    }
                    try {
                        if (da3 != null && da3.quantity >= sodatamhoa) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da3, sodatamhoa);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 220);
                                    dathang.quantity = (int) Math.round(sodatamhoa * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Tam Hoa" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {

                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case XIU3:
                    int sodanc3 = Integer.valueOf(text[0]);
                    Item da23 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 221) {
                            da23 = item;
                            break;
                        }
                    }
                    try {
                        if (da23 != null && da23.quantity >= sodanc3) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da23, sodanc3);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 221);
                                    dathang.quantity = (int) Math.round(sodanc3 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc3
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Xỉu" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc3
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc3
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                case TAI3:
                    int sodanc13 = Integer.valueOf(text[0]);
                    Item da13 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 221) {
                            da13 = item;
                            break;
                        }
                    }
                    try {
                        if (da13 != null && da13.quantity >= sodanc13) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da13, sodanc13);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc13
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc13
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 221);
                                    dathang.quantity = (int) Math.round(sodanc13 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc13
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Tài" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case TAMHOA3:
                    int sodatamhoa3 = Integer.valueOf(text[0]);
                    Item da33 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 221) {
                            da33 = item;
                            break;
                        }
                    }
                    try {
                        if (da33 != null && da33.quantity >= sodatamhoa3) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da33, sodatamhoa3);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa3
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 221);
                                    dathang.quantity = (int) Math.round(sodatamhoa3 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa3
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Tam Hoa" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {

                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa3
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case XIU4:
                    int sodanc4 = Integer.valueOf(text[0]);
                    Item da24 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 222) {
                            da24 = item;
                            break;
                        }
                    }
                    try {
                        if (da24 != null && da24.quantity >= sodanc4) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da24, sodanc4);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 222);
                                    dathang.quantity = (int) Math.round(sodanc4 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc4
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Xỉu" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc4
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc4
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                case TAI4:
                    int sodanc14 = Integer.valueOf(text[0]);
                    Item da14 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 222) {
                            da14 = item;
                            break;
                        }
                    }
                    try {
                        if (da14 != null && da14.quantity >= sodanc14) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da14, sodanc14);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc14
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc14
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 222);
                                    dathang.quantity = (int) Math.round(sodanc14 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc14
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Tài" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case TAMHOA4:
                    int sodatamhoa4 = Integer.valueOf(text[0]);
                    Item da34 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 222) {
                            da34 = item;
                            break;
                        }
                    }
                    try {
                        if (da34 != null && da34.quantity >= sodatamhoa4) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da34, sodatamhoa4);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa4
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 222);
                                    dathang.quantity = (int) Math.round(sodatamhoa4 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa4
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Tam Hoa" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {

                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa4
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case XIU5:
                    int sodanc5 = Integer.valueOf(text[0]);
                    Item da25 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 223) {
                            da25 = item;
                            break;
                        }
                    }
                    try {
                        if (da25 != null && da25.quantity >= sodanc5) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da25, sodanc5);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 223);
                                    dathang.quantity = (int) Math.round(sodanc5 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc5
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Xỉu" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc5
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc5
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                case TAI5:
                    int sodanc15 = Integer.valueOf(text[0]);
                    Item da15 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 223) {
                            da15 = item;
                            break;
                        }
                    }
                    try {
                        if (da15 != null && da15.quantity >= sodanc15) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da15, sodanc15);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc15
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc15
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 223);
                                    dathang.quantity = (int) Math.round(sodanc15 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc15
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Tài" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case TAMHOA5:
                    int sodatamhoa5 = Integer.valueOf(text[0]);
                    Item da35 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 223) {
                            da35 = item;
                            break;
                        }
                    }
                    try {
                        if (da35 != null && da35.quantity >= sodatamhoa5) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da35, sodatamhoa5);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa5
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 223);
                                    dathang.quantity = (int) Math.round(sodatamhoa5 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa5
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Tam Hoa" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {

                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa5
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case XIU6:
                    int sodanc6 = Integer.valueOf(text[0]);
                    Item da26 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 224) {
                            da26 = item;
                            break;
                        }
                    }
                    try {
                        if (da26 != null && da26.quantity >= sodanc6) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da26, sodanc6);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 224);
                                    dathang.quantity = (int) Math.round(sodanc6 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc6
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Xỉu" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc6
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc6
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                case TAI6:
                    int sodanc16 = Integer.valueOf(text[0]);
                    Item da16 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 224) {
                            da16 = item;
                            break;
                        }
                    }
                    try {
                        if (da16 != null && da16.quantity >= sodanc16) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da16, sodanc16);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc16
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc16
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 224);
                                    dathang.quantity = (int) Math.round(sodanc16 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc16
                                            + " Đá nâng cấp vào Tài" + "\nKết quả : Tài" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case TAMHOA6:
                    int sodatamhoa6 = Integer.valueOf(text[0]);
                    Item da36 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 224) {
                            da36 = item;
                            break;
                        }
                    }
                    try {
                        if (da36 != null && da36.quantity >= sodatamhoa6) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da36, sodatamhoa6);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa6
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 224);
                                    dathang.quantity = (int) Math.round(sodatamhoa6 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa6
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Tam Hoa" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {

                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa6
                                            + " Đá nâng cấp vào Tam Hoa" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ đá nâng cấp để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;
//--------------------------------------------------------------------------------------- 
//------------------------ TÀI XỈU BẰNG MẢNH SKH ------------------------------------                    
                case XIU7:
                    int sodanc7 = Integer.valueOf(text[0]);
                    Item da27 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1394) {
                            da27 = item;
                            break;
                        }
                    }
                    try {
                        if (da27 != null && da27.quantity >= sodanc7) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da27, sodanc7);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1394);
                                    dathang.quantity = (int) Math.round(sodanc7 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc7
                                            + " Mảnh SKH vào Xỉu" + "\nKết quả : Xỉu" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc7
                                            + " Mảnh SKH vào Xỉu" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc7
                                            + " Đá nâng cấp vào Xỉu" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                case TAI7:
                    int sodanc17 = Integer.valueOf(text[0]);
                    Item da17 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1394) {
                            da17 = item;
                            break;
                        }
                    }
                    try {
                        if (da17 != null && da17.quantity >= sodanc17) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da17, sodanc17);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc17
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc17
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1394);
                                    dathang.quantity = (int) Math.round(sodanc17 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc17
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Tài" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case TAMHOA7:
                    int sodatamhoa7 = Integer.valueOf(text[0]);
                    Item da37 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1394) {
                            da37 = item;
                            break;
                        }
                    }
                    try {
                        if (da37 != null && da37.quantity >= sodatamhoa7) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da37, sodatamhoa7);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa7
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1394);
                                    dathang.quantity = (int) Math.round(sodatamhoa7 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa7
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Tam Hoa" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {

                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa7
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case XIU8:
                    int sodanc38 = Integer.valueOf(text[0]);
                    Item da238 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1395) {
                            da238 = item;
                            break;
                        }
                    }
                    try {
                        if (da238 != null && da238.quantity >= sodanc38) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da238, sodanc38);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1395);
                                    dathang.quantity = (int) Math.round(sodanc38 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc38
                                            + " Mảnh SKH vào Xỉu" + "\nKết quả : Xỉu" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc38
                                            + " Mảnh SKH vào Xỉu" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc38
                                            + " Mảnh SKH vào Xỉu" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                case TAI8:
                    int sodanc138 = Integer.valueOf(text[0]);
                    Item da138 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1395) {
                            da138 = item;
                            break;
                        }
                    }
                    try {
                        if (da138 != null && da138.quantity >= sodanc138) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da138, sodanc138);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc138
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc138
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1395);
                                    dathang.quantity = (int) Math.round(sodanc138 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc138
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Tài" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case TAMHOA8:
                    int sodatamhoa38 = Integer.valueOf(text[0]);
                    Item da338 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1395) {
                            da338 = item;
                            break;
                        }
                    }
                    try {
                        if (da338 != null && da338.quantity >= sodatamhoa38) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da338, sodatamhoa38);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa38
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1395);
                                    dathang.quantity = (int) Math.round(sodatamhoa38 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa38
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Tam Hoa" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {

                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa38
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case XIU9:
                    int sodanc49 = Integer.valueOf(text[0]);
                    Item da249 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1396) {
                            da249 = item;
                            break;
                        }
                    }
                    try {
                        if (da249 != null && da249.quantity >= sodanc49) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da249, sodanc49);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1396);
                                    dathang.quantity = (int) Math.round(sodanc49 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc49
                                            + " Mảnh SKH vào Xỉu" + "\nKết quả : Xỉu" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc49
                                            + " Mảnh SKH vào Xỉu" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc49
                                            + " Mảnh SKH vào Xỉu" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                case TAI9:
                    int sodanc149 = Integer.valueOf(text[0]);
                    Item da149 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1396) {
                            da149 = item;
                            break;
                        }
                    }
                    try {
                        if (da149 != null && da149.quantity >= sodanc149) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da149, sodanc149);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc149
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc149
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1396);
                                    dathang.quantity = (int) Math.round(sodanc149 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc149
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Tài" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case TAMHOA9:
                    int sodatamhoa49 = Integer.valueOf(text[0]);
                    Item da349 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1396) {
                            da349 = item;
                            break;
                        }
                    }
                    try {
                        if (da349 != null && da349.quantity >= sodatamhoa49) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da349, sodatamhoa49);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa49
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1396);
                                    dathang.quantity = (int) Math.round(sodatamhoa49 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa49
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Tam Hoa" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {

                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa49
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case XIU10:
                    int sodanc510 = Integer.valueOf(text[0]);
                    Item da2510 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1397) {
                            da2510 = item;
                            break;
                        }
                    }
                    try {
                        if (da2510 != null && da2510.quantity >= sodanc510) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da2510, sodanc510);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1397);
                                    dathang.quantity = (int) Math.round(sodanc510 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc510
                                            + " Mảnh SKH vào Xỉu" + "\nKết quả : Xỉu" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc510
                                            + " Mảnh SKH vào Xỉu" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc510
                                            + " Mảnh SKH vào Xỉu" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                case TAI10:
                    int sodanc1510 = Integer.valueOf(text[0]);
                    Item da1510 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1397) {
                            da1510 = item;
                            break;
                        }
                    }
                    try {
                        if (da1510 != null && da1510.quantity >= sodanc1510) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da1510, sodanc1510);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc1510
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc1510
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1397);
                                    dathang.quantity = (int) Math.round(sodanc1510 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc1510
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Tài" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case TAMHOA10:
                    int sodatamhoa510 = Integer.valueOf(text[0]);
                    Item da3510 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1397) {
                            da3510 = item;
                            break;
                        }
                    }
                    try {
                        if (da3510 != null && da3510.quantity >= sodatamhoa510) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da3510, sodatamhoa510);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa510
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1397);
                                    dathang.quantity = (int) Math.round(sodatamhoa510 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa510
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Tam Hoa" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {

                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa510
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case XIU11:
                    int sodanc611 = Integer.valueOf(text[0]);
                    Item da2611 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1398) {
                            da2611 = item;
                            break;
                        }
                    }
                    try {
                        if (da2611 != null && da2611.quantity >= sodanc611) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da2611, sodanc611);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1398);
                                    dathang.quantity = (int) Math.round(sodanc611 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc611
                                            + " Mảnh SKH vào Xỉu" + "\nKết quả : Xỉu" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc611
                                            + " Mảnh SKH vào Xỉu" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc611
                                            + " Mảnh SKH vào Xỉu" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                case TAI11:
                    int sodanc1611 = Integer.valueOf(text[0]);
                    Item da1611 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1398) {
                            da1611 = item;
                            break;
                        }
                    }
                    try {
                        if (da1611 != null && da1611.quantity >= sodanc1611) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da1611, sodanc1611);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc1611
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc1611
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Tam Hoa" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1398);
                                    dathang.quantity = (int) Math.round(sodanc1611 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodanc1611
                                            + " Mảnh SKH vào Tài" + "\nKết quả : Tài" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case TAMHOA11:
                    int sodatamhoa611 = Integer.valueOf(text[0]);
                    Item da3611 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 1398) {
                            da3611 = item;
                            break;
                        }
                    }
                    try {
                        if (da3611 != null && da3611.quantity >= sodatamhoa611) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da3611, sodatamhoa611);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa611
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Xỉu" + "\nBạn đã thua cược!");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Item dathang = ItemService.gI().createNewItem((short) 1398);
                                    dathang.quantity = (int) Math.round(sodatamhoa611 * 2);
                                    InventoryServiceNew.gI().addItemBag(player, dathang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa611
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Tam Hoa" + "\nBạn đã thắng cược!");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {

                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + "\nXí Ngầu Thứ Nhất : " + x + "\nXí Ngầu Thứ Hai : "
                                            + y + "\nXí Ngầu Thứ Ba : " + z + "\nTổng giá trị là : " + tong + "\nBạn đã cược : " + sodatamhoa611
                                            + " Mảnh SKH vào Tam Hoa" + "\nKết quả : Tài" + "\nBạn đã thua cược!");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ Mảnh SKH để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;
//---------------------------------------------------------------------------------------                    

                case CHOOSE_LEVEL_BDKB:
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.QUY_LAO_KAME, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_BDKB,
                                    "Con có chắc chắn muốn tới bản đồ kho báu cấp độ " + level + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }

//                    BanDoKhoBauService.gI().openBanDoKhoBau(player, (byte) );
                    break;
                case CHOOSE_LEVEL_GAS:
                    int level1 = Integer.parseInt(text[0]);
                    if (level1 >= 1 && level1 <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.MR_POPO, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCPET_GO_TO_GAS,
                                    "Con có chắc chắn muốn tới Khí gas huỷ diệt cấp độ " + level1 + "?",
                                    new String[]{"Đồng ý, Let's Go", "Từ chối"}, level1);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }

//                    BanDoKhoBauService.gI().openBanDoKhoBau(player, (byte) );
                    break;
                case NAP_THE:

                    NapThe.SendCard(player, LOAI_THE, MENH_GIA, text[0], text[1]);
                    break;
                case QUY_DOI_COIN:

                    int goldTrade = Integer.valueOf(text[0]);
                    int tile = 2;
                    if (goldTrade <= 0 || goldTrade >= 500001) {
                        Service.getInstance().sendThongBao(player, "Quá giới hạn mỗi lần tối đa 500.000");
                    } else if (player.getSession().vnd >= goldTrade) {//* coinGold
//                        if (goldTrade >= 100000)
//                        {
//                        PlayerDAO.subvnd(player, goldTrade );//* coinGold
//                        Item detu = ItemService.gI().createNewItem((short) 542, 1);
//                        Item thoiVang = ItemService.gI().createNewItem((short) 861, goldTrade * 2);// x3  * 3
//                        InventoryServiceNew.gI().addItemBag(player, detu);
//                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
//                        InventoryServiceNew.gI().sendItemBags(player);
//                        Service.getInstance().sendThongBao(player, "Bạn nhận được " + goldTrade * 2//* ratioGold
//                                + " " + thoiVang.template.name);
//                        GirlkunDB.executeUpdate("update player set vnd = (vnd + "+ goldTrade //* coinGold
//                                +") where id = " + player.id);
//                        }
//                        else {
                        PlayerDAO.subvnd(player, goldTrade);//* coinGold
                        Item thoiVang = ItemService.gI().createNewItem((short) 457, goldTrade / 1000 * tile);
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Bạn nhận được " + goldTrade / 1000 * tile//* ratioGold
                                + " " + thoiVang.template.name);
//                        GirlkunDB.executeUpdate("update player set vnd = (vnd + "+ goldTrade //* coinGold
//                                +") where id = " + player.id); 
                    } else {
                        Service.getInstance().sendThongBao(player, "Số tiền của bạn là " + player.getSession().vnd + " không đủ để quy "
                                + " đổi " + goldTrade / 1000 * tile + " Thỏi Vàng " + " " + "bạn cần thêm" + (player.getSession().vnd - goldTrade));
                    }
                    break;
                case QUY_DOI_HONG_NGOC:
                    int RubyTrade = Integer.parseInt(text[0]);
                    if (RubyTrade <= 0 || RubyTrade >= 100000) {
                        Service.gI().sendThongBao(player, "Quá giới hạn mỗi lần chỉ được tối đa 100000");
                    } else if (player.getSession().coinBar >= RubyTrade) {
                        PlayerDAO.subcoinBar(player, RubyTrade);
                        player.inventory.ruby += RubyTrade;
                        PlayerService.gI().sendInfoHpMpMoney(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được " + RubyTrade
                                + " Hồng Ngọc");
                    } else {
                        Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                + " đổi " + RubyTrade + " Hồng Ngọc " + " " + "bạn cần thêm" + (RubyTrade - player.getSession().coinBar));
                    }
                    break;
                case SEND_ITEM:
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        int quantityItemBuff = Integer.parseInt(text[2]);
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        if (pBuffItem != null) {
                            String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                            if (idItemBuff == -1) {
                                pBuffItem.inventory.gold = Math.min(pBuffItem.inventory.gold + (long) quantityItemBuff, Inventory.LIMIT_GOLD);
                                txtBuff += quantityItemBuff + " vàng\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -2) {
                                pBuffItem.inventory.gem = Math.min(pBuffItem.inventory.gem + quantityItemBuff, 2000000000);
                                txtBuff += quantityItemBuff + " ngọc\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -3) {
                                pBuffItem.inventory.ruby = Math.min(pBuffItem.inventory.ruby + quantityItemBuff, 2000000000);
                                txtBuff += quantityItemBuff + " ngọc khóa\b";
                                Service.getInstance().sendMoney(player);
                            } else {
                                Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                                itemBuffTemplate.quantity = quantityItemBuff;
                                InventoryServiceNew.gI().addItemBag(pBuffItem, itemBuffTemplate);
                                InventoryServiceNew.gI().sendItemBags(pBuffItem);
                                txtBuff += "x" + quantityItemBuff + " " + itemBuffTemplate.template.name + "\b";
                            }
                            NpcService.gI().createTutorial(player, 24, txtBuff);
                            if (player.id != pBuffItem.id) {
                                NpcService.gI().createTutorial(pBuffItem, 24, txtBuff);
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Player không online");
                        }
                        break;
                    }
                    break;

                case CHOOSE_LEVEL_CDRD:
                    int level3 = Integer.parseInt(text[0]);
                    if (level3 >= 1 && level3 <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.THAN_VU_TRU, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_CDRD,
                                    "Con có chắc chắn muốn tới con đường rắn độc cấp độ " + level3 + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level3);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                case SEND_ITEM_OP:
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        int idOptionBuff = Integer.parseInt(text[2]);
                        int slOptionBuff = Integer.parseInt(text[3]);
                        int slItemBuff = Integer.parseInt(text[4]);
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        if (pBuffItem != null) {
                            String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                            if (idItemBuff == -1) {
                                pBuffItem.inventory.gold = Math.min(pBuffItem.inventory.gold + (long) slItemBuff, Inventory.LIMIT_GOLD);
                                txtBuff += slItemBuff + " vàng\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -2) {
                                pBuffItem.inventory.gem = Math.min(pBuffItem.inventory.gem + slItemBuff, 2000000000);
                                txtBuff += slItemBuff + " ngọc\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -3) {
                                pBuffItem.inventory.ruby = Math.min(pBuffItem.inventory.ruby + slItemBuff, 2000000000);
                                txtBuff += slItemBuff + " ngọc khóa\b";
                                Service.getInstance().sendMoney(player);
                            } else {
                                Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                                itemBuffTemplate.itemOptions.add(new Item.ItemOption(idOptionBuff, slOptionBuff));
                                itemBuffTemplate.quantity = slItemBuff;
                                txtBuff += "x" + slItemBuff + " " + itemBuffTemplate.template.name + "\b";
                                InventoryServiceNew.gI().addItemBag(pBuffItem, itemBuffTemplate);
                                InventoryServiceNew.gI().sendItemBags(pBuffItem);
                            }
                            NpcService.gI().createTutorial(player, 24, txtBuff);
                            if (player.id != pBuffItem.id) {
                                NpcService.gI().createTutorial(player, 24, txtBuff);
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Player không online");
                        }
                        break;
                    }
                    break;
                case SEND_ITEM_SKH:
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        int idOptionSKH = Integer.parseInt(text[2]);
                        int idOptionBuff = Integer.parseInt(text[3]);
                        int slOptionBuff = Integer.parseInt(text[4]);
                        int slItemBuff = Integer.parseInt(text[5]);
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        if (pBuffItem != null) {
                            String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                            if (idItemBuff == -1) {
                                pBuffItem.inventory.gold = Math.min(pBuffItem.inventory.gold + (long) slItemBuff, Inventory.LIMIT_GOLD);
                                txtBuff += slItemBuff + " vàng\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -2) {
                                pBuffItem.inventory.gem = Math.min(pBuffItem.inventory.gem + slItemBuff, 2000000000);
                                txtBuff += slItemBuff + " ngọc\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -3) {
                                pBuffItem.inventory.ruby = Math.min(pBuffItem.inventory.ruby + slItemBuff, 2000000000);
                                txtBuff += slItemBuff + " ngọc khóa\b";
                                Service.getInstance().sendMoney(player);
                            } else {
                                Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                                itemBuffTemplate.itemOptions.add(new Item.ItemOption(idOptionSKH, 0));
                                if (idOptionSKH == 127) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(139, 0));
                                } else if (idOptionSKH == 128) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(140, 0));
                                } else if (idOptionSKH == 129) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(141, 0));
                                } else if (idOptionSKH == 130) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(142, 0));
                                } else if (idOptionSKH == 131) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(143, 0));
                                } else if (idOptionSKH == 132) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(144, 0));
                                } else if (idOptionSKH == 133) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(136, 0));
                                } else if (idOptionSKH == 134) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(137, 0));
                                } else if (idOptionSKH == 135) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(138, 0));
                                }
                                itemBuffTemplate.itemOptions.add(new Item.ItemOption(30, 0));
                                itemBuffTemplate.itemOptions.add(new Item.ItemOption(idOptionBuff, slOptionBuff));
                                itemBuffTemplate.quantity = slItemBuff;
                                txtBuff += "x" + slItemBuff + " " + itemBuffTemplate.template.name + "\b";
                                InventoryServiceNew.gI().addItemBag(pBuffItem, itemBuffTemplate);
                                InventoryServiceNew.gI().sendItemBags(pBuffItem);
                            }
                            NpcService.gI().createTutorial(player, 24, txtBuff);
                            if (player.id != pBuffItem.id) {
                                NpcService.gI().createTutorial(player, 24, txtBuff);
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Player không online");
                        }
                        break;

                    }
                    break;
                    case CON_SO_MAY_MAN_NGOC:
                    int CSMM = Integer.parseInt(text[0]);
                    if (CSMM >= MiniGame.gI().MiniGame_S1.min && CSMM <= MiniGame.gI().MiniGame_S1.max && MiniGame.gI().MiniGame_S1.second > 10) {
                        MiniGame.gI().MiniGame_S1.newData(player, CSMM, 0);
                    }
                    break;
                case CON_SO_MAY_MAN_VANG:
                    int CSMM2 = Integer.parseInt(text[0]);
                    if (CSMM2 >= MiniGame.gI().MiniGame_S1.min && CSMM2 <= MiniGame.gI().MiniGame_S1.max && MiniGame.gI().MiniGame_S1.second > 10) {
                        MiniGame.gI().MiniGame_S1.newData(player, CSMM2, 1);
                    }
                    break;
            }
        } catch (Exception e) {
        }
    }

    private void nauBanhChung(Player player, String s) {
        try {
            int slBanhChung = Math.abs(Integer.parseInt(s));
            Item laGiong = InventoryServiceNew.gI().findItemBag(player, 1439);
            Item gaoNep = InventoryServiceNew.gI().findItemBag(player, 1438);
            Item dauXanh = InventoryServiceNew.gI().findItemBag(player, 1437);
            Item giongTre = InventoryServiceNew.gI().findItemBag(player, 1441);
            Item thitLon = InventoryServiceNew.gI().findItemBag(player, 1442);
            Item nuocNau = InventoryServiceNew.gI().findItemBag(player, 1443);
            if (laGiong == null) {
                Service.gI().sendThongBao(player, "Thiếu lá giông");
                return;
            }

            if (gaoNep == null) {
                Service.gI().sendThongBao(player, "Thiếu gạo nếp");
                return;
            }

            if (dauXanh == null) {
                Service.gI().sendThongBao(player, "Thiếu đậu xanh");
                return;
            }

            if (giongTre == null) {
                Service.gI().sendThongBao(player, "Thiếu giống tre");
                return;
            }

            if (thitLon == null) {
                Service.gI().sendThongBao(player, "Thiếu thịt lợn");
                return;
            }

            if (laGiong.quantity < (99 * slBanhChung)) {
                Service.gI().sendThongBao(player, "Không đủ lá giông");
                return;
            }

            if (gaoNep.quantity < (50 * slBanhChung)) {
                Service.gI().sendThongBao(player, "Không đủ gạo nếp");
                return;
            }

            if (dauXanh.quantity < (50 * slBanhChung)) {
                Service.gI().sendThongBao(player, "Không đủ đậu xanh");
                return;
            }

            if (giongTre.quantity < (99 * slBanhChung)) {
                Service.gI().sendThongBao(player, "Không đủ giống tre");
                return;
            }

            if (thitLon.quantity < (99 * slBanhChung)) {
                Service.gI().sendThongBao(player, "Không đủ thịt lợn");
                return;
            }

            InventoryServiceNew.gI().subQuantityItemsBag(player, laGiong, (99 * slBanhChung));
            InventoryServiceNew.gI().subQuantityItemsBag(player, gaoNep, (50 * slBanhChung));
            InventoryServiceNew.gI().subQuantityItemsBag(player, dauXanh, (50 * slBanhChung));
            InventoryServiceNew.gI().subQuantityItemsBag(player, giongTre, (99 * slBanhChung));
            InventoryServiceNew.gI().subQuantityItemsBag(player, thitLon, (99 * slBanhChung));

            NauBanh.gI().plBanhChung++;
            NauBanh.gI().addListPlNauBanh(player);
            Service.gI().sendThongBao(player, "Đã Nấu Bánh Chân");
            return;

        } catch (NumberFormatException e) {
            Service.gI().sendThongBao(player, "Số lượng nhập không hợp lệ");
        }
    }

    private void nauBanhTet(Player player, String s) {
        try {
            int slBanhTet = Math.abs(Integer.parseInt(s));
            Item laChuoi = InventoryServiceNew.gI().findItemBag(player, 1440);
            Item gaoNep = InventoryServiceNew.gI().findItemBag(player, 1438);
            Item dauXanh = InventoryServiceNew.gI().findItemBag(player, 1437);
            Item giongTre = InventoryServiceNew.gI().findItemBag(player, 1441);
            Item thitLon = InventoryServiceNew.gI().findItemBag(player, 1442);
            Item nuocNau = InventoryServiceNew.gI().findItemBag(player, 1443);
            if (laChuoi == null) {
                Service.gI().sendThongBao(player, "Thiếu lá chuối");
                return;
            }

            if (gaoNep == null) {
                Service.gI().sendThongBao(player, "Thiếu gạo nếp");
                return;
            }

            if (dauXanh == null) {
                Service.gI().sendThongBao(player, "Thiếu đậu xanh");
                return;
            }

            if (giongTre == null) {
                Service.gI().sendThongBao(player, "Thiếu giống tre");
                return;
            }

            if (thitLon == null) {
                Service.gI().sendThongBao(player, "Thiếu thịt lợn");
                return;
            }

            if (laChuoi.quantity < (99 * slBanhTet)) {
                Service.gI().sendThongBao(player, "Không đủ lá chuối");
                return;
            }

            if (gaoNep.quantity < (50 * slBanhTet)) {
                Service.gI().sendThongBao(player, "Không đủ gạo nếp");
                return;
            }

            if (dauXanh.quantity < (50 * slBanhTet)) {
                Service.gI().sendThongBao(player, "Không đủ đậu xanh");
                return;
            }

            if (giongTre.quantity < (99 * slBanhTet)) {
                Service.gI().sendThongBao(player, "Không đủ giống tre");
                return;
            }

            if (thitLon.quantity < (99 * slBanhTet)) {
                Service.gI().sendThongBao(player, "Không đủ thịt lợn");
                return;
            }

            InventoryServiceNew.gI().subQuantityItemsBag(player, laChuoi, (99 * slBanhTet));
            InventoryServiceNew.gI().subQuantityItemsBag(player, gaoNep, (50 * slBanhTet));
            InventoryServiceNew.gI().subQuantityItemsBag(player, dauXanh, (50 * slBanhTet));
            InventoryServiceNew.gI().subQuantityItemsBag(player, giongTre, (99 * slBanhTet));
            InventoryServiceNew.gI().subQuantityItemsBag(player, thitLon, (99 * slBanhTet));

            NauBanh.gI().plBanhTet++;
            NauBanh.gI().addListPlNauBanh(player);
            Service.gI().sendThongBao(player, "Đã Nấu Bánh Toét");
            return;
        } catch (NumberFormatException e) {
            Service.gI().sendThongBao(player, "Số lượng nhập không hợp lệ");
        }
    }

    public void createForm(Player pl, int typeInput, String title, SubInput... subInputs) {
        pl.iDMark.setTypeInput(typeInput);
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createForm(ISession session, int typeInput, String title, SubInput... subInputs) {
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createFormChangePassword(Player pl) {
        createForm(pl, CHANGE_PASSWORD, "Quên Mật Khẩu", new SubInput("Nhập mật khẩu đã quên", PASSWORD),
                new SubInput("Mật khẩu mới", PASSWORD),
                new SubInput("Nhập lại mật khẩu mới", PASSWORD));
    }

    public void createFormNauBanhChung(Player player) {
        createForm(player, NAU_BANH_CHUNG, "Nấu bánh chưng", new SubInput("Nhập số lượng bánh chưng cần nấu", NUMERIC));
    }

    public void createFormNauBanhTet(Player player) {
        createForm(player, NAU_BANH_TET, "Nấu bánh tết", new SubInput("Nhập số lượng bánh tết cần nấu", NUMERIC));

    }

    public void TAI_taixiu(Player pl) {
        createForm(pl, TAI_taixiu, "Chọn số hồng ngọc đặt Tài", new SubInput("Số Hồng ngọc cược", ANY));//????
    }

    public void XIU_taixiu(Player pl) {
        createForm(pl, XIU_taixiu, "Chọn số hồng ngọc đặt Xỉu", new SubInput("Số Hồng ngọc cược", ANY));//????
    }

    public void createFormChooseLevelCDRD(Player pl) {
        createForm(pl, CHOOSE_LEVEL_CDRD, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormGiveItem(Player pl) {
        createForm(pl, GIVE_IT, "Tặng vật phẩm", new SubInput("Tên", ANY), new SubInput("Id Item", ANY), new SubInput("Số lượng", ANY));
    }

    public void createFormTangNgocHong(Player pl) {
        createForm(pl, TANG_NGOC_HONG, "Tặng Ngọc Hồng", new SubInput("Tên Người Chơi Nhận Ngọc", ANY), new SubInput("Số Ngọc Muốn Tặng", ANY));
        // 
    }

    public void createFormGiftCode(Player pl) {
        createForm(pl, GIFT_CODE, "GIFTCODE", new SubInput("GIFT-CODE", ANY));
    }

    public void createFormFindPlayer(Player pl) {
        createForm(pl, FIND_PLAYER, "Tìm kiếm người chơi", new SubInput("Tên người chơi", ANY));
    }

    public void createFormChooseLevelKGHD(Player pl) {
        createForm(pl, CHOOSE_LEVEL_KGHD, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormNapCoin(Player pl) {
        createForm(pl, NAP_COIN, "Nạp coin", new SubInput("Tên nhân vật", ANY), new SubInput("Số lượng", ANY));
    }

    public void TAI(Player pl) {
        createForm(pl, TAI, "Chọn số thỏi vàng đặt Tài", new SubInput("Số thỏi vàng", ANY));//????
    }

    public void XIU(Player pl) {
        createForm(pl, XIU, "Chọn số thỏi vàng đặt Xỉu", new SubInput("Số thỏi vàng", ANY));
    }

    public void TAMHOA(Player pl) {
        createForm(pl, TAMHOA, "Chọn số thỏi vàng đặt Tam Hoa", new SubInput("Số thỏi vàng", ANY));//????
    }

    public void TAI2(Player pl) {
        createForm(pl, TAI2, "Chọn số đá nâng cấp đặt Tài", new SubInput("Số đá nâng cấp", ANY));//????
    }

    public void XIU2(Player pl) {
        createForm(pl, XIU2, "Chọn số đá nâng cấp đặt Xỉu", new SubInput("Số đá nâng cấp", ANY));
    }

    public void TAMHOA2(Player pl) {
        createForm(pl, TAMHOA2, "Chọn số đá nâng cấp đặt Tam Hoa", new SubInput("Số đá nâng cấp", ANY));//????
    }

    public void TAI3(Player pl) {
        createForm(pl, TAI3, "Chọn số đá nâng cấp đặt Tài", new SubInput("Số đá nâng cấp", ANY));//????
    }

    public void XIU3(Player pl) {
        createForm(pl, XIU3, "Chọn số đá nâng cấp đặt Xỉu", new SubInput("Số đá nâng cấp", ANY));
    }

    public void TAMHOA3(Player pl) {
        createForm(pl, TAMHOA3, "Chọn số đá nâng cấp đặt Tam Hoa", new SubInput("Số đá nâng cấp", ANY));//????
    }

    public void TAI4(Player pl) {
        createForm(pl, TAI4, "Chọn số đá nâng cấp đặt Tài", new SubInput("Số đá nâng cấp", ANY));//????
    }

    public void XIU4(Player pl) {
        createForm(pl, XIU4, "Chọn số đá nâng cấp đặt Xỉu", new SubInput("Số đá nâng cấp", ANY));
    }

    public void TAMHOA4(Player pl) {
        createForm(pl, TAMHOA4, "Chọn số đá nâng cấp đặt Tam Hoa", new SubInput("Số đá nâng cấp", ANY));//????
    }

    public void TAI5(Player pl) {
        createForm(pl, TAI5, "Chọn số đá nâng cấp đặt Tài", new SubInput("Số đá nâng cấp", ANY));//????
    }

    public void XIU5(Player pl) {
        createForm(pl, XIU5, "Chọn số đá nâng cấp đặt Xỉu", new SubInput("Số đá nâng cấp", ANY));
    }

    public void TAMHOA5(Player pl) {
        createForm(pl, TAMHOA5, "Chọn số đá nâng cấp đặt Tam Hoa", new SubInput("Số đá nâng cấp", ANY));//????
    }

    public void TAI6(Player pl) {
        createForm(pl, TAI6, "Chọn số đá nâng cấp đặt Tài", new SubInput("Số đá nâng cấp", ANY));//????
    }

    public void XIU6(Player pl) {
        createForm(pl, XIU6, "Chọn số đá nâng cấp đặt Xỉu", new SubInput("Số đá nâng cấp", ANY));
    }

    public void TAMHOA6(Player pl) {
        createForm(pl, TAMHOA6, "Chọn số đá nâng cấp đặt Tam Hoa", new SubInput("Số đá nâng cấp", ANY));//????
    }

    public void TAI7(Player pl) {
        createForm(pl, TAI7, "Chọn số mảnh SKH đặt Tài", new SubInput("Số mảnh SKH", ANY));//????
    }

    public void XIU7(Player pl) {
        createForm(pl, XIU7, "Chọn số mảnh SKH đặt Xỉu", new SubInput("Số mảnh SKH", ANY));
    }

    public void TAMHOA7(Player pl) {
        createForm(pl, TAMHOA7, "Chọn số mảnh SKH đặt Tam Hoa", new SubInput("Số mảnh SKH", ANY));//????
    }

    public void TAI8(Player pl) {
        createForm(pl, TAI8, "Chọn số mảnh SKH đặt Tài", new SubInput("Số mảnh SKH", ANY));//????
    }

    public void DonateCsbang(Player pl) {
        createForm(pl, DONATE_CS, "Donate (Quyên góp capsule bang của bạn vào bang)", new SubInput("Nhập số lượng muốn quyên góp", NUMERIC));
    }

    public void XIU8(Player pl) {
        createForm(pl, XIU8, "Chọn số mảnh SKH đặt Xỉu", new SubInput("Số mảnh SKH", ANY));
    }

    public void ChatAll(Player pl) {
        createForm(pl, CHATALL, "CHAT ALL PLAYER", new SubInput("Chat Sex", ANY));
    }
    public void createFormSendItem1(Player pl) {
        createForm(pl, GIVE_ITEM_BAR, "Tặng vật phẩm (Chuỗi Options)", new SubInput("Tên", ANY),new SubInput("Id Item", ANY),new SubInput("Số lượng", ANY),new SubInput("ID OPTION (Cách nhau bởi dấu '-')", ANY),new SubInput("PARAM (Cách nhau bởi dấu '-')", ANY));
    }

    public void createFormQDKQ(Player pl) {
        try {
            createForm(pl, QUY_DOI_KHAN_QUANG,
                     "\nNhập số Khăn quàng muốn đổi (30 Khăn đỏ = 1 Khăn quàng):",
                    new SubInput("Số Khăn quàng muốn đổi", NUMERIC));

        } catch (Exception e) {
        }
    }
    
    public void TAMHOA8(Player pl) {
        createForm(pl, TAMHOA8, "Chọn số mảnh SKH đặt Tam Hoa", new SubInput("Số mảnh SKH", ANY));//????
    }

    public void TAI9(Player pl) {
        createForm(pl, TAI9, "Chọn số mảnh SKH đặt Tài", new SubInput("Số mảnh SKH", ANY));//????
    }
    public void createFormThongBao(Player pl) {
        createForm(pl, THONG_BAO, "Thông báo Server", new SubInput("Thông báo", ANY),
                new SubInput("Kiểu: 1-NORMAL,2-SERVER-NOTI", NUMERIC));
    }

    public void createFormThongBaoRieng(Player pl) {
        createForm(pl, THONG_BAO_RIENG, "Thông báo riêng tư", new SubInput("Thông báo", ANY),
                new SubInput("Kiểu: 1-NORMAL,2-OK", NUMERIC), new SubInput("Tên người chơi", ANY));
    }
    public void createFormUseGold(Player pl) {
        createForm(pl, UseGold, "1 Thỏi vàng bằng 500 triệu vàng", new SubInput("Nhập số lượng muốn dùng, nhập 1 bằng 1 thỏi vàng", NUMERIC));
    }

    public void XIU9(Player pl) {
        createForm(pl, XIU9, "Chọn số mảnh SKH đặt Xỉu", new SubInput("Số mảnh SKH", ANY));
    }

    public void TAMHOA9(Player pl) {
        createForm(pl, TAMHOA9, "Chọn số mảnh SKH đặt Tam Hoa", new SubInput("Số mảnh SKH", ANY));//????
    }

    public void TAI10(Player pl) {
        createForm(pl, TAI10, "Chọn số mảnh SKH đặt Tài", new SubInput("Số mảnh SKH", ANY));//????
    }

    public void XIU10(Player pl) {
        createForm(pl, XIU10, "Chọn số mảnh SKH đặt Xỉu", new SubInput("Số mảnh SKH", ANY));
    }

    public void TAMHOA10(Player pl) {
        createForm(pl, TAMHOA10, "Chọn số mảnh SKH đặt Tam Hoa", new SubInput("Số mảnh SKH", ANY));//????
    }

    public void TAI11(Player pl) {
        createForm(pl, TAI11, "Chọn số mảnh SKH đặt Tài", new SubInput("Số mảnh SKH", ANY));//????
    }

    public void XIU11(Player pl) {
        createForm(pl, XIU11, "Chọn số mảnh SKH đặt Xỉu", new SubInput("Số mảnh SKH", ANY));
    }

    public void TAMHOA11(Player pl) {
        createForm(pl, TAMHOA11, "Chọn số mảnh SKH đặt Tam Hoa", new SubInput("Số mảnh SKH", ANY));//????
    }
    public void createFormConSoMayMan_Gem(Player pl) {
        createForm(pl, CON_SO_MAY_MAN_NGOC, "Hãy chọn 1 số từ 0 đến 99 giá 5 ngọc", new SubInput("Số bạn chọn", NUMERIC));
    }

    public void createFormConSoMayMan_Gold(Player pl) {
        createForm(pl, CON_SO_MAY_MAN_VANG, "Hãy chọn 1 số từ 0 đến 99 giá 1.000.000 vàng", new SubInput("Số bạn chọn", NUMERIC));
    }

    public void createFormNapThe(Player pl, String loaiThe, String menhGia) {
        LOAI_THE = loaiThe;
        MENH_GIA = menhGia;
        createForm(pl, NAP_THE, "Nạp thẻ", new SubInput("Số Seri", ANY), new SubInput("Mã thẻ", ANY));
    }
//    public void createFormQDND(Player pl) {
//
//        createForm(pl, QUY_DOI_NANG_DONG, "Quy đổi điểm năng động, giới hạn đổi không quá 10000\n Điểm hiện còn : " + " "+ pl.diemhotong
//                + "\n (Tỉ lệ quy đổi 1 điểm = 1 Đá Ngũ Sắc)", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
//    }

    public void createFormQDTV(Player pl) {

        createForm(pl, QUY_DOI_COIN, "Quy đổi thỏi vàng, giới hạn đổi không quá 500.000\n Tiền hiện còn : " + " " + pl.getSession().vnd
                + "\n (Tỉ lệ quy đổi 1000đ = 1 Thỏi Vàng)\n Hiện tại đang x2 nên giá quy đổi là 1000đ = 2 Thỏi Vàng", new SubInput("Nhập số tiền muốn đổi", NUMERIC));
    }

    public void createFormQDHN(Player pl) {

        createForm(pl, QUY_DOI_HONG_NGOC, "Quy đổi hồng ngọc"
                + "\n10.000 Vnd = 10000 Hồng Ngọc "
                + "\n20.000 Vnd = 20000 Hồng Ngọc "
                + "\n50.000 Vnd = 50000 Hồng Ngọc "
                + "\n100.000 Vnd = 100000 Hồng Ngọc "
                + "\n200.000 Vnd = 200000 Hồng Ngọc "
                + "\n500.000 Vnd = 500000 Hồng Ngọc",
                new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

    public void createFormChangeName(Player pl, Player plChanged) {
        PLAYER_ID_OBJECT.put((int) pl.id, plChanged);
        createForm(pl, CHANGE_NAME, "Đổi tên " + plChanged.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChangeNameByItem(Player pl) {
        createForm(pl, CHANGE_NAME_BY_ITEM, "Đổi tên " + pl.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChooseLevelGas(Player pl) {
        createForm(pl, CHOOSE_LEVEL_GAS, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormChooseLevelBDKB(Player pl) {
        createForm(pl, CHOOSE_LEVEL_BDKB, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormSenditem(Player pl) {
        createForm(pl, SEND_ITEM, "SEND ITEM",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID item", NUMERIC),
                new SubInput("Số lượng", NUMERIC));
    }

    public void createFormSenditem1(Player pl) {
        createForm(pl, SEND_ITEM_OP, "SEND Vật Phẩm Option",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID Trang Bị", NUMERIC),
                new SubInput("ID Option", NUMERIC),
                new SubInput("Param", NUMERIC),
                new SubInput("Số lượng", NUMERIC));
    }

    public void createFormSenditem2(Player pl) {
        createForm(pl, SEND_ITEM_SKH, "Buff SKH Option V2",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID Trang Bị", NUMERIC),
                new SubInput("ID Option SKH 127 > 135", NUMERIC),
                new SubInput("ID Option Bonus", NUMERIC),
                new SubInput("Param", NUMERIC),
                new SubInput("Số lượng", NUMERIC));
    }

    public static class SubInput {

        private String name;
        private byte typeInput;

        public SubInput(String name, byte typeInput) {
            this.name = name;
            this.typeInput = typeInput;
        }
    }

    public void addItemGiftCodeToPlayer(Player p, final String giftcode) {
        try {
            final GirlkunResultSet red = GirlkunDB.executeQuery("SELECT * FROM `giftcode` WHERE `code` LIKE '" + Util.strSQL(giftcode) + "' LIMIT 1;");
            if (red.first()) {
                String text = "Mã quà tặng" + ": " + giftcode + "\b- " + "Phần quà của bạn là:" + "\b";
                final byte type = red.getByte("type");
                int limit = red.getInt("limit");
                final boolean isDelete = red.getBoolean("Delete");
                final boolean isCheckbag = red.getBoolean("bagCount");
                final JSONArray listUser = (JSONArray) JSONValue.parseWithException(red.getString("listUser"));
                final JSONArray listItem = (JSONArray) JSONValue.parseWithException(red.getString("listItem"));
                final JSONArray option = (JSONArray) JSONValue.parseWithException(red.getString("itemoption"));
                if (limit == 0) {
                    NpcService.gI().createTutorial(p, 24, "Số lượng mã quà tặng này đã hết.");
                } else {
                    if (type == 1) {
                        for (int i = 0; i < listUser.size(); ++i) {
                            final int playerId = Integer.parseInt(listUser.get(i).toString());
                            if (playerId == p.id) {
//                                NpcService.gI().createTutorial(p,24, "Mỗi tài khoản chỉ được phép sử dụng mã quà tặng này 1 lần duy nhất.");
                                Service.gI().sendThongBaoOK(p, "Mỗi tài khoản chỉ được phép sử dụng mã quà tặng này 1 lần duy nhất.");
                                return;
                            }
                        }
                    }
                    if (isCheckbag && listItem.size() > InventoryServiceNew.gI().getCountEmptyBag(p)) {
                        NpcService.gI().createTutorial(p, 24, "Hành trang cần phải có ít nhất " + listItem.size() + " ô trống để nhận vật phẩm");
                    } else {
                        for (int i = 0; i < listItem.size(); ++i) {
                            final JSONObject item = (JSONObject) listItem.get(i);
                            final int idItem = Integer.parseInt(item.get("id").toString());
                            final int quantity = Integer.parseInt(item.get("quantity").toString());

                            if (idItem == -1) {
                                p.inventory.gold = Math.min(p.inventory.gold + (long) quantity, Inventory.LIMIT_GOLD);
                                text += quantity + " vàng\b";
                            } else if (idItem == -2) {
                                p.inventory.gem = Math.min(p.inventory.gem + quantity, 2000000000);
                                text += quantity + " ngọc\b";
                            } else if (idItem == -3) {
                                p.inventory.ruby = Math.min(p.inventory.ruby + quantity, 2000000000);
                                text += quantity + " ngọc khóa\b";
                            } else {
                                Item itemGiftTemplate = ItemService.gI().createNewItem((short) idItem);

                                itemGiftTemplate.quantity = quantity;
                                if (option != null) {
                                    for (int u = 0; u < option.size(); u++) {
                                        JSONObject jsonobject = (JSONObject) option.get(u);
                                        itemGiftTemplate.itemOptions.add(new Item.ItemOption(Integer.parseInt(jsonobject.get("id").toString()), Integer.parseInt(jsonobject.get("param").toString())));
                                    }

                                }
                                text += "x" + quantity + " " + itemGiftTemplate.template.name + "\b";
                                InventoryServiceNew.gI().addItemBag(p, itemGiftTemplate);
                                InventoryServiceNew.gI().sendItemBags(p);
                            }

                            if (i < listItem.size() - 1) {
                                text += "";
                            }
                        }
                        if (limit != -1) {
                            --limit;
                        }
                        listUser.add(p.id);
                        GirlkunDB.executeUpdate("UPDATE `giftcode` SET `limit` = " + limit + ", `listUser` = '" + listUser.toJSONString() + "' WHERE `code` LIKE '" + Util.strSQL(giftcode) + "';");
//                        NpcService.gI().createTutorial(p,24, text);
                        Service.gI().sendThongBaoOK(p, text);
                    }
                }
            } else {
                NpcService.gI().createTutorial(p, 24, "Mã quà tặng không tồn tại hoặc đã được sử dụng");
            }
        } catch (Exception e) {
            NpcService.gI().createTutorial(p, 24, "Có lỗi sảy ra  hãy báo ngay cho QTV để khắc phục.");
            e.printStackTrace();
        }
    }

}
