package com.girlkun.models.npc;

import com.girlkun.MaQuaTang.MaQuaTangManager;
import com.girlkun.kygui.ItemKyGui;
import com.girlkun.kygui.ShopKyGuiService;
import com.girlkun.kygui.ShopKyGuiManager;
import com.girlkun.consts.ConstMap;
import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.consts.ConstTask;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.list_boss.NhanBan;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.clan.ClanMember;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.MapMaBu.MapMaBu;
import com.girlkun.models.map.Zone;
import com.girlkun.models.map.bando.BanDoKhoBau;
import com.girlkun.models.map.bando.BanDoKhoBauService;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.girlkun.models.map.challenge.MartialCongressService;
import com.girlkun.models.map.doanhtrai.DoanhTrai;
import com.girlkun.models.map.doanhtrai.DoanhTraiService;
import com.girlkun.models.map.nguhanhson.nguhs;
import com.girlkun.data.DataGame;
import com.girlkun.database.GirlkunDB;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.boss.list_boss.Broly.ThachDauGoJo;
import com.girlkun.models.boss.list_boss.TrainOffline.Bill;
import com.girlkun.models.boss.list_boss.TrainOffline.MeoThan;
import com.girlkun.models.boss.list_boss.TrainOffline.ThanVuTru;
import com.girlkun.models.boss.list_boss.TrainOffline.Thuongde;
import com.girlkun.models.boss.list_boss.TrainOffline.ToSuKaio;
import com.girlkun.models.boss.list_boss.TrainOffline.Yajiro;
import com.girlkun.models.map.ConDuongRanDoc.ConDuongRanDoc;
import com.girlkun.models.map.ConDuongRanDoc.ConDuongRanDocService;
import com.girlkun.models.map.GiaiCuuMiNuong.GiaiCuuMiNuongService;
import com.girlkun.MaQuaTang.MaQuaTangManager;
import com.girlkun.lib.RandomCollection;
import com.girlkun.models.Template.ItemTemplate;
import com.girlkun.models.boss.list_boss.Broly.ThachDauWhis;
import com.girlkun.models.boss.list_boss.DuongTank;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.MapSatan.MapSatan;
import com.girlkun.models.map.MapVoDai.MapVodai;
import com.girlkun.models.map.Mapchichi.Map22h;
import com.girlkun.models.map.daihoi.DaiHoiManager;
import com.girlkun.models.map.gas.Gas;
import com.girlkun.models.map.gas.GasService;
import com.girlkun.models.map.vodai.MartialCongressServices;
import com.girlkun.models.matches.PVPService;
import com.girlkun.models.matches.TOP;
import com.girlkun.models.matches.pvp.DaiHoiVoThuat;
import com.girlkun.models.matches.pvp.DaiHoiVoThuatService;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.NPoint;
import com.girlkun.models.player.Player;
import com.girlkun.models.shop.ShopServiceNew;
import com.girlkun.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.girlkun.result.GirlkunResultSet;
import com.girlkun.server.Client;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerManager;
import com.girlkun.services.*;
import com.girlkun.services.func.*;
import static com.girlkun.services.func.CombineServiceNew.EP_CHUNG_NHAN_XUAT_SU;
import static com.girlkun.services.func.CombineServiceNew.THUC_TINH_DT;
import com.girlkun.utils.Logger;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import static com.girlkun.services.func.SummonDragon.*;
import com.girlkun.services.func.minigame.ChonAiDay_Gem;
import com.girlkun.services.func.minigame.ChonAiDay_Gold;
import com.girlkun.services.func.minigame.ChonAiDay_Ruby;
import com.girlkun.utils.SkillUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class NpcFactory {

    private static final int COST_HD = 50000000;

    private static boolean nhanVang = false;
    private static boolean nhanDeTu = false;
    private static int vodaiBallNumber = 0;
    //playerid - object
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private NpcFactory() {

    }

      public static Npc pgg(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ngươi muốn làm gì?",
                            "Cửa Hàng","Học\nSkill");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "OBITO", false);
                                    break;
                                        case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "TUI_VO_HANG", false);
                                    break;
//                                case 1:
//                                    if (player.checkPGG() == true) {
//                                        ShopServiceNew.gI().opendShop(player, "PGG", true);
//                                    } else {
//                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ngươi không có phiếu giảm giá", "Đóng");
//                                    }
//                                    break;
//                                case 2:
//                                    Input.gI().createFormQDTV(player);
//                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

      public static Npc berus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|Shop Đồ Hủy diệt\n|2| Mang đủ  99 Thức ăn đến cho ta. Ta sẽ bán đồ Hủy diệt cho ngươi",
                            "Cửa hàng\nHủy diệt", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (select == 0) {
                                if (player.check99ThucAnHuyDiet() == true) {
//                                    if (player.setClothes.setDTL == 5) {
                                        ShopServiceNew.gI().opendShop(player, "BILL", true);
//                                    } else {
//                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, "Yêu cầu mặc 5 món Thần linh", "Đóng");
//                                    }
                                } else {
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ngươi chưa đủ 99 thức ăn", "Đóng");
                                }
                                break;
                            }
                            break;
                    }
                }
            }
        };
    }

    
    public static Npc gohanzombie(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Để tới Nghĩa Trang cần có vé du hành\n"
                                + "Thời gian còn lại có thể tới là " + Util.tinhgio(player.thoigianduhanh),
                                "Đến vũ trụ\nNghĩa Trang");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.thoigianduhanh > 0) {

                                    ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 384);
                                } else {
                                    Service.gI().sendThongBao(player, "hết thời gian");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc panchy(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
             public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                     
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "|7| Tôi là"
                                            + " Nu ,"
                                            + "với siêu thuyền của tôi ,tôi sẽ đưa cậu du hành đa vũ trụ \n"
                                            + "|4| Cậu Muốn Đi Đâu ... ! ",
//                                            + "|2| Chức Năng Đang Phát Triển, Sẽ Ra Mắt Khi Open Sever",
                                    "Đến Khu\nTest Dame","Gia Nhập\nMap Ninja","Đến Đảo\nKho Báu","Gia Nhập\nSát Quỷ\nĐoàn");
                
                }
                    }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                        if (this.mapId == 5)
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 224, -1, -1);
                                break;
                                  case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 217, -1, -1);
                                break;
                                  case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 190, -1, -1);
                                break;
                                   case 3:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 218, -1, -1);
                                break;
                          
                        }
                    }
                }
            }
        };
    }

     public static Npc dendemoi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
//      
                    if (mapId == 5) {
                        this.createOtherMenu(player, 0, "|7| Tôi có thể đưa cậu đến khắp hành tinh , cậu muốn đi đâu ...  ?\n"
                                + "|4|Phí Bay : 500 hồng ngọc \n"
                                + "|4|Sức Mạnh Yêu Cầu : 80 tỉ", "Hành\nTinh\nVampa", "Hành\nTinh\nPlant");
                    }
                    if (mapId == 245) {
                        this.createOtherMenu(player, 0, "|2| Cậu muốn chở về ư ,?", "OK", "Từ chối");

                    }
                    if (mapId == 244) {
                        this.createOtherMenu(player, 0, "|2| Cậu muốn chở về ư ,?", "OK", "Từ chối");

                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                    if (mapId == 5) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.power < 80000000000L) {
                                    Service.getInstance().sendThongBao(player, "Sức mạnh của anh còn quá yếu");
                                    return;
                                } else if (player.inventory.ruby < 500) {
                                    Service.getInstance().sendThongBao(player, "Phí Vào Là 500 hồng ngọc một lần\n bạn không đủ hồng ngọc để vào !");
                                    return;
                                } else {
                                    player.inventory.ruby -= 500;
                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                    ChangeMapService.gI().changeMapInYard(player, 242, -1, -1);
                                }
                                break;
                                 case 1:
                                if (player.nPoint.power < 80000000000L) {
                                    Service.getInstance().sendThongBao(player, "Sức mạnh của anh còn quá yếu");
                                    return;
                                } else if (player.inventory.ruby < 500) {
                                    Service.getInstance().sendThongBao(player, "Phí Vào Là 500 hồng ngọc một lần\n bạn không đủ hồng ngọc để vào !");
                                    return;
                                } else {
                                    player.inventory.ruby -= 500;
                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                    ChangeMapService.gI().changeMapInYard(player, 241, -1, -1);
                                }
                                break;
                        }
                    }
                    if (mapId == 245) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapInYard(player, 5, -1, 469);
                        }
                    }
                    if (mapId == 244) {
                         if (select == 0) {
                            ChangeMapService.gI().changeMapInYard(player, 5, -1, 469);
                        }
                            }

                        }
                    }

                
            
        };
}

    
   public static Npc SUKIENHE(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 5) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|4|_SỰ KIỆN HÈ 2024_\n"
                                    + "|2|Hiện Tại Bạn Đang Đang Có Số Điểm Sự Kiện Là " + player.NguHanhSonPoint + "\n"
                            + "|7|Để Đổi Hộp Qùa Sự Kiện Hè Bạn Cần\n"
                            + "|8|99 San Hô + 99 Bình Nước + 99 Khúc Gỗ + 1 Que Diêm + 5 Thỏi Vàng\n"
                            + "|5| → Bạn Sẽ Nhận Được Hộp Qùa VIP Và Điểm Sự Kiện.\n"
                            + "|8|X99 Nguyên Liệu Mỗi Loại Kết Hợp Với 1 Que Diêm \n"                  
                            + "|5| → Bạn Sẽ Nhận Được Hộp Qùa Thường Và Điểm Sự Kiện\n", 
                            "Đổi \n Hộp Qùa ", "Đổi \n Vật Phẩm", "Chế biến", "Top","Shop");

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.MUAHE_1, "|7|Xin chào\n"
                                            + "|5|Con muốn đổi hộp quà nào\n"
                                            + "|6| Hộp quà thường :Đá nâng cấp, Item Cấp 1 và 1điểm sự kiện\n"
                                            + "|6| Hộp quà Vip : Đá nâng cấp, Item Cấp 2 và 2điểm sự kiện",
                                            "Hộp Quà\n Thường ", "Hộp Quà\nVIP", "Từ chối");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.MUAHE, "|5|-Sự Kiện Hè-\n"
                                            + "Chỉ Có Thể Up Vật Phẩm Tại Các Map Sau : Nam Kame, Nam Guru\n"
                                            + "|1|Yêu Cầu : Mang trên người (Quần Đi Biển)\n"
                                            + "|6|x999 Sao Biển: Ngọc Rồng 2-5 Sao Ngẫu Nhiên\n"
                                            + "|6|x999 Con Cua: Cải Trang, Có Khả Năng Vĩnh Viễn\n"
                                            + "|6|x999 Vỏ Sò: Pet Ngãu Nhiên, Có Khả Năng Vĩnh Viễn\n"
                                            + "|6|x999 Vỏ Ốc: Thỏi Vàng (Có thể đổi số lượng lớn)",
                                            "Đổi\nSao Biển", "Đổi\nCon Cua", "Đổi\nVỏ Sò", "Đổi\nVỏ Ốc");
                                    break;
                                case 2:
                                    this.createOtherMenu(player, ConstNpc.MUAHE_2, "|7|Con cần chế biến gì\n"
                                            + "|5|Chế Biến Cua rang me: Cần x10 con cua\n"
                                            + "|5|Chế Biến Bạch tuộc nướng: cần x10 bạch tuộc\n"
                                            + "|5|Chế Biến Tôm tẩm bột chiên xù: cần x99 tôm\n",
                                            "Cua Rang\nMe", "Bạch Tuộc\nNướng", "Tôm Chiên\n Xù", "Từ chối");
                                    break;
                                case 3:
                                    Service.getInstance().sendThongBaoOK(player, TopService.gettopsukienhe());
                                    break;
                                case 4:
                                    ShopServiceNew.gI().opendShop(player, "SHOP_SU_KIEN_HE", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MUAHE_2) {
                            switch (select) {
                                case 0:
                                    Item concua = null;
                                    Item traidua = null;
                                    try {
                                        concua = InventoryServiceNew.gI().findItemBag(player, 1815);
                                        traidua = InventoryServiceNew.gI().findItemBag(player, 694);
                                    } catch (Exception e) {
                                    }
                                    if (concua == null || concua.quantity < 10) {
                                        this.npcChat(player, "Bạn không đủ x10 con cua");
                                    } else if (traidua == null || traidua.quantity < 1) {
                                        this.npcChat(player, "Bạn không đủ x1 trái dừa");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 20_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, concua, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, traidua, 1);
                                        Service.gI().sendMoney(player);
                                        if (Util.isTrue(99, 100)) {
                                            Item ngocrong = ItemService.gI().createNewItem((short) 880);
                                            InventoryServiceNew.gI().addItemBag(player, ngocrong);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            this.npcChat(player, "Bạn nhận được " + ngocrong);
                                        }
                                    }
                                    break;
                                case 1:
                                    Item bachtuot = null;
                                    Item traidua1 = null;
                                    try {
                                        bachtuot = InventoryServiceNew.gI().findItemBag(player, 1814);
                                        traidua1 = InventoryServiceNew.gI().findItemBag(player, 694);
                                    } catch (Exception e) {
                                    }
                                    if (bachtuot == null || bachtuot.quantity < 10) {
                                        this.npcChat(player, "Bạn không đủ x10 bạch tuộc");
                                    } else if (traidua1 == null || traidua1.quantity < 1) {
                                        this.npcChat(player, "Bạn không đủ x1 trái dừa");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 20_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, bachtuot, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, traidua1, 1);
                                        Service.gI().sendMoney(player);
                                        if (Util.isTrue(99, 100)) {
                                            Item ngocrong = ItemService.gI().createNewItem((short) 881);
                                            InventoryServiceNew.gI().addItemBag(player, ngocrong);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            this.npcChat(player, "Bạn nhận được " + ngocrong);
                                        }
                                    }
                                    break;
                                case 2:
                                    Item contom = null;
                                    Item traidua2 = null;
                                    try {
                                        contom = InventoryServiceNew.gI().findItemBag(player, 1813);
                                        traidua2 = InventoryServiceNew.gI().findItemBag(player, 694);
                                    } catch (Exception e) {
                                    }
                                    if (contom == null || contom.quantity < 10) {
                                        this.npcChat(player, "Bạn không đủ x10 con Tôm");
                                    } else if (traidua2 == null || traidua2.quantity < 1) {
                                        this.npcChat(player, "Bạn không đủ x1 trái dừa");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 20_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, contom, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, traidua2, 1);
                                        Service.gI().sendMoney(player);
                                        if (Util.isTrue(99, 100)) {
                                            Item ngocrong = ItemService.gI().createNewItem((short) 882);
                                            InventoryServiceNew.gI().addItemBag(player, ngocrong);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            this.npcChat(player, "Bạn nhận được " + ngocrong);
                                        }
                                    }
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MUAHE_1) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 221, "|7|Xin chào\n"
                                            + "|5|Con muốn đổi hộp quà thường à\n"
                                            + "|6|x99 Sao Biển\n "
                                            + "|6|x99 Con Cua\n"
                                            + "|6|x99 Vỏ Sò\n"
                                            + "|6|x99 Vỏ Ốc\n",
                                            "Sao\nBiển", "Con\nCua",  "Vỏ\nXò","Vỏ\nỐc");
                                    break;
                                case 1:
                                    Item sanho = null;
                                    Item binhnuoc = null;
                                    Item khucgo = null;
                                    Item thoivang = null;
                                    try {
                                        sanho = InventoryServiceNew.gI().findItemBag(player, 880);
                                        binhnuoc = InventoryServiceNew.gI().findItemBag(player, 881);
                                        khucgo = InventoryServiceNew.gI().findItemBag(player, 882);
                                        thoivang = InventoryServiceNew.gI().findItemBag(player, 697);
                                    } catch (Exception e) {
                                    }
                                    if (sanho == null || sanho.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ x99 Cua rang me");
                                    } else if (binhnuoc == null || binhnuoc.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ x99 Bạch tuộc nướng");
                                    } else if (khucgo == null || khucgo.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ x99 Tôm tẩm bột chiên xù");
                                    } else if (thoivang == null || thoivang.quantity < 5) {
                                        this.npcChat(player, "Bạn không đủ x5 thỏi vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, sanho, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, binhnuoc, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, khucgo, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 5);
                                        Service.gI().sendMoney(player);
                                        if (Util.isTrue(99, 100)) {
                                            Item ngocrong = ItemService.gI().createNewItem((short) 1812);
                                            InventoryServiceNew.gI().addItemBag(player, ngocrong);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            this.npcChat(player, "Bạn nhận được " + ngocrong);
                                        }
                                    }
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == 221) {
                            switch (select) {
                                case 0:
                                    Item SaoBien = null;
                                    try {
                                        SaoBien = InventoryServiceNew.gI().findItemBag(player, 698);
                                    } catch (Exception e) {
                                    }
                                    if (SaoBien == null || SaoBien.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ x99 sao biển");
                                    } else if (player.inventory.gold < 200_000_000) {
                                        this.npcChat(player, "Bạn không đủ 200tr vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 200_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, SaoBien, 99);
                                        Service.gI().sendMoney(player);
                                        if (Util.isTrue(99, 100)) {
                                            Item ngocrong = ItemService.gI().createNewItem((short) 1811);
                                            InventoryServiceNew.gI().addItemBag(player, ngocrong);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            this.npcChat(player, "Bạn nhận được " + ngocrong);
                                        }
                                    }
                                    break;
                                case 1:
                                    Item ConCua = null;
                                    try {
                                        ConCua = InventoryServiceNew.gI().findItemBag(player, 697);
                                    } catch (Exception e) {
                                    }
                                    if (ConCua == null || ConCua.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ x99 con cua");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 200tr vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 200_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, ConCua, 99);
                                        Service.gI().sendMoney(player);
                                        if (Util.isTrue(99, 100)) {
                                            Item ngocrong = ItemService.gI().createNewItem((short) 1811);
                                            InventoryServiceNew.gI().addItemBag(player, ngocrong);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            this.npcChat(player, "Bạn nhận được " + ngocrong);
                                        }
                                    }
                                    break;
                                case 2:
                                    Item VoSo = null;
                                    try {
                                        VoSo = InventoryServiceNew.gI().findItemBag(player, 696);
                                    } catch (Exception e) {
                                    }
                                    if (VoSo == null || VoSo.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ x99 vỏ sò");
                                    } else if (player.inventory.gold < 200_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 200_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, VoSo, 99);
                                        Service.gI().sendMoney(player);
                                        if (Util.isTrue(99, 100)) {
                                            Item ngocrong = ItemService.gI().createNewItem((short) 1811);
                                            InventoryServiceNew.gI().addItemBag(player, ngocrong);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            this.npcChat(player, "Bạn nhận được " + ngocrong);
                                        }
                                    }
                                    break;
                                case 3:
                                    Item VoOc = null;
                                    try {
                                        VoOc = InventoryServiceNew.gI().findItemBag(player, 695);
                                    } catch (Exception e) {
                                    }
                                    if (VoOc == null || VoOc.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ x99 vỏ ốc");
                                    } else if (player.inventory.gold < 200_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 200_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, VoOc, 99);
                                        Service.gI().sendMoney(player);
                                        if (Util.isTrue(99, 100)) {
                                            Item ngocrong = ItemService.gI().createNewItem((short) 1811);
                                            InventoryServiceNew.gI().addItemBag(player, ngocrong);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            this.npcChat(player, "Bạn nhận được " + ngocrong);
                                        }
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MUAHE) {
                            switch (select) {
                                case 0:
                                    Item SaoBien = null;
                                    try {
                                        SaoBien = InventoryServiceNew.gI().findItemBag(player, 698);
                                    } catch (Exception e) {
                                    }
                                    if (SaoBien == null || SaoBien.quantity < 999) {
                                        this.npcChat(player, "Bạn không đủ x999 sao biển");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, SaoBien, 999);
                                        Service.gI().sendMoney(player);
                                        if (Util.isTrue(99, 100)) {
                                            Item ngocrong = ItemService.gI().createNewItem((short) Util.nextInt(16, 18));
                                            InventoryServiceNew.gI().addItemBag(player, ngocrong);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            this.npcChat(player, "Bạn nhận được " + ngocrong);
                                        } else if (Util.isTrue(1, 100)) {
                                            Item ngocrong2 = ItemService.gI().createNewItem((short) Util.nextInt(14, 15));
                                            InventoryServiceNew.gI().addItemBag(player, ngocrong2);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            this.npcChat(player, "Bạn nhận được " + ngocrong2);
                                        }
                                    }
                                    break;
                                case 1:
                                    Item ConCua = null;
                                    try {
                                        ConCua = InventoryServiceNew.gI().findItemBag(player, 697);
                                    } catch (Exception e) {
                                    }
                                    if (ConCua == null || ConCua.quantity < 999) {
                                        this.npcChat(player, "Bạn không đủ x999 con cua");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, ConCua, 999);
                                        Service.gI().sendMoney(player);
                                        Item caitrang = ItemService.gI().createNewItem((short) (1248 + player.gender));
                                        caitrang.itemOptions.add(new Item.ItemOption(50, Util.nextInt(15, 35)));
                                        caitrang.itemOptions.add(new Item.ItemOption(14, Util.nextInt(1, 10)));
                                        caitrang.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20, 35)));
                                        caitrang.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 35)));
                                        caitrang.itemOptions.add(new Item.ItemOption(101, Util.nextInt(20, 50)));
                                        if (Util.isTrue(999, 1000)) {
                                            caitrang.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 15)));
                                        }
                                        InventoryServiceNew.gI().addItemBag(player, caitrang);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được Cải Trang Sự Kiện");
                                    }
                                    break;
                                case 2:
                                    Item VoSo = null;
                                    try {
                                        VoSo = InventoryServiceNew.gI().findItemBag(player, 696);
                                    } catch (Exception e) {
                                    }
                                    if (VoSo == null || VoSo.quantity < 999) {
                                        this.npcChat(player, "Bạn không đủ x999 vỏ sò");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, VoSo, 999);
                                        Service.gI().sendMoney(player);
                                        Item pet = ItemService.gI().createNewItem((short) Util.nextInt(2085, 2090));
                                        pet.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 30)));
                                        pet.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 30)));
                                        pet.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 30)));
                                        if (Util.isTrue(99, 100)) {
                                            pet.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
                                        }
                                        InventoryServiceNew.gI().addItemBag(player, pet);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được pet");
                                    }
                                    break;
                                case 3:
                                    Item VoOc = null;
                                    try {
                                        VoOc = InventoryServiceNew.gI().findItemBag(player, 695);
                                    } catch (Exception e) {
                                    }
                                    if (VoOc == null || VoOc.quantity < 999) {
                                        this.npcChat(player, "Bạn không đủ x999 vỏ ốc");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        Item tv = ItemService.gI().createNewItem((short) 457, 1);
                                        tv.itemOptions.add(new Item.ItemOption(30, 0));
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, VoOc, 999);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        InventoryServiceNew.gI().addItemBag(player, tv);
                                        this.npcChat(player, "Bạn nhận được 1 thỏi vàng");
                                    }
                                    break;
                            }
                        }

                    }
                }
            }
        };
    }

    
//    public static Npc bar(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (this.mapId == 5) {
//                    this.createOtherMenu(player, ConstNpc.BASE_MENU,"|6|Ngươi cần mua gì!!!\n"
//                            + "|2|Ta bán rất nhiều đồ đó.", "MUA","HONG MUA");
//                }
//            }
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 5) {
//                        if (player.iDMark.isBaseMenu()) {
//                            switch (select) {
//                                case 0:
//                                    ShopServiceNew.gI().opendShop(player, "TUI_VO_HANG", true);
//                                    break;
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }
//
      public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 5 || this.mapId == 20 || this.mapId == 13) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|2|Xin Chào, Ta có một số vật phẩm đặc biệt cậu có muốn xem không ? ",
                            "Cửa\nHảng","Mở rộng\nHành trang\nRương đồ","Nhập mã\nQuà tặng","Cửa hàng\nHạn sử\ndụng","Danh\nHiệu");
                }
            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 20 || this.mapId == 13) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "SANTA", true);
                                    break;
                                       case 1:
                                    ShopServiceNew.gI().opendShop(player, "MO_RONG", true);
                                    break;
                                     case 2:
                                Input.gI().createFormGiftCode(player);
                                break;
                                       case 3:
                                    ShopServiceNew.gI().opendShop(player, "HSD", true);
                                    break;
//                                       case 4:
//                                    ShopServiceNew.gI().opendShop(player, "TIEM_SU_KIEN", true);
//                                    break;
                                       case 4:
                                    ShopServiceNew.gI().opendShop(player, "DANH_HIEU", true);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }


//        public static Npc allsukien(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 5 || this.mapId == 46) {
//                        switch (ConstEvent.EVENT) {
//                        case 1:
//                        this.createOtherMenu(player, ConstEvent.MUAHE, "|7|-Sự Kiện Mè Hùa-\n"
//                            + "Chỉ Có Thể Up Vật Phẩm Tại Các Map Sau : Nam Kame, Nam Guru\n"
//                            + "Yêu Cầu : Mang trên người (Quần Đi Biển)\n\n"
//                            + "|1| x999 Sao Biển: Ngọc Rồng 2-5 Sao Ngẫu Nhiên\n"
//                            + " x999 Con Cua: Cải Trang, Có Khả Năng Vĩnh Viễn\n"
//                            + " x999 Vỏ Sò: Pet Ngãu Nhiên, Có Khả Năng Vĩnh Viễn\n"
//                            + " x999 Vỏ Ốc: Thỏi Vàng (Có thể đổi số lượng lớn)\n",
//                            "Đổi\nSao Biển", "Đổi\nCon Cua","Đổi\nVỏ Sò","Đổi\nVỏ Ốc");
//                        break;
//                        case 2:
//                        this.createOtherMenu(player, ConstEvent.HUNGVUONG, "|7|-Sự Kiện Hùng Vương-\n"
//                            + "Có Thể Up Vật Phẩm Tại Tất Cả Các Map\n"
//                            + "Yêu Cầu : Mang trên người (Cải Trang Mị Nương)\n"
//                            + "\n|2|Dâng Dưa Hấu Để Đổi Lấy Điểm Sự Kiện"
//                            + "\n|2|Sử Dụng Điểm Để Đổi Công Thức Chế Tạo Đồ Thiên Sứ"
//                            + "\n|2|Sử Dụng Điểm Để Để Thử Vận May Nhận Item SC"
//                            + "\n|2|Sử Dụng Điểm Để Đổi Cải Trang Vip (Có Khả Năng Vĩnh Viễn)"
//                            + "\n|2|Sử Dụng Điểm Để Thử Vận May Nhận Ngọc Rồng",
//                            "Dâng\nDưa Hấu", "Xem Điểm\nSự Kiện", "Đổi\nCông Thức", "Đổi\nItem SC", "Đổi\nCải Trang", "Thử\nVận May", "Từ chối");
//                        break;
//                        case 3:
//                        this.createOtherMenu(player, ConstEvent.TRUNGTHU, "|7| SỰ KIỆN TRUNG THU"
//                            + "\n\n|2|Nguyên liệu cần nấu bánh Trung thu"
//                            + "\n|-1|- Bánh Hạt sen : 99 Hạt sen + 50 Bột nếp + 2 Mồi lửa"
//                            + "\n|-1|- Bánh Đậu xanh : 99 Đậu xanh + 50 Bột nếp + 2 Mồi lửa"
//                            + "\n|-1|- Bánh Thập cẩm : 99 Hạt sen + 99 Đậu xanh + 99 Bột nếp + 5 Mồi lửa"
//                            + "\n|7|Làm bánh sẽ tốn phí 2Ty Vàng/lần"
//                            + "\n\n|1|Điểm sự kiện : " + Util.format(player.nhsPoint) + " Điểm",
//                            "Thể lệ", "Làm bánh", "Đổi điểm\nTrung thu");
//                        break;
//                        case 4:
//                        this.createOtherMenu(player, ConstEvent.HLWEEN,
//                            "Sự kiện Halloween chính thức tại Ngọc Rồng Kamui\n"
//                            + "Chuẩn bị x10 nguyên liệu Kẹo, Bánh Quy, Bí ngô để đổi Giỏ Kẹo cho ta nhé\n"
//                            + "Nguyên Liệu thu thập bằng cách đánh quái tại các hành tinh được chỉ định\n"
//                            + "Tích lũy 3 Giỏ Kẹo +  3 Vé mang qua đây ta sẽ cho con 1 Hộp Ma Quỷ\n"
//                            + "Tích lũy 3 Giỏ Kẹo, 3 Hộp Ma Quỷ + 3 Vé \nmang qua đây ta sẽ cho con 1 hộp quà thú vị.",
//                            "Đổi\nGiỏ Kẹo", "Đổi Hộp\nMa Quỷ", "Đổi Hộp\nQuà Halloween",
//                            "Từ chối");
//                        break;
//                        case 5:
//                        this.createOtherMenu(player, ConstEvent.NHAGIAO, 
//                            "Sự kiện 20/11 chính thức tại Ngọc Rồng Kamui\n"
//                            + "Số điểm hiện tại của bạn là : "+ player.inventory.event
//                            + "\nTổng số hoa đã tặng "+player.inventory.event+ "/999"
//                            + "\nToàn bộ máy chủ được nhân đôi số vàng rơi ra từ quái,thời gian còn lại "
//                            + "5" + " phút."
//                            + "\nKhi tặng đủ 999 bông hoa toàn bộ máy chủ được nhân đôi số vàng rơi ra từ quái trong 60 phút",
//                            "Tặng 1\n Bông hoa", "Tặng\n10 Bông", "Tặng\n99 Bông",
//                            "Đổi\nHộp quà");
//                        break;
//                        case 6:
//                        this.createOtherMenu(player, ConstEvent.NOEL,
//                            "Sự kiên giáng sinh Ngọc Rồng Kamui"
//                            + "\nKhi đội mũ len bất kì đánh quái sẽ có cơ hội nhận được kẹo giáng sinh"
//                            + "\nĐem 99 kẹo giáng sinh tới đây để đổi 1 Vớ,tất giáng sinh"
//                            + "\nChúc bạn một mùa giáng sinh vui vẻ",
//                            "Đổi\nTất giáng sinh");
//                        break;
//                        case 7:
//                        this.createOtherMenu(player, ConstEvent.TET,
//                            "Mừng Ngày Tết Nguyên Đán Nro Kamui"
//                            + "\nBạn đang có: " + player.inventory.event + " điểm sự kiện"
//                            + "\n" + (ConstEvent.TONGSOBANH >= 500 ? "|7|HIỆN TẠI ĐANG X2 EXP TRÊN TOÀN MÁY CHỦ\nTHỜI GIAN CÒN: " + Util.tinhgio(ConstEvent.X2) : "|7|Tổng số bánh server: " + ConstEvent.TONGSOBANH
//                            + "\nKhi số bánh nấu đạt đủ 500 bánh toàn máy chủ sẽ được X2 EXP")
//                            + "\n|-1|Chúc bạn năm mới dui dẻ",
//                            "Nhận Lìxì", "Đổi Điểm\nSự Kiện","Cửa Hàng\nSự Kiện","Nhận Quần\nHoa Văn");
//                        break;
//                        case 8:
//                        this.createOtherMenu(player, ConstEvent.PHUNU,
//                            "Sự kiện 8/3 chính thức tại Ngọc Rồng Kamui"
//                            + "\nBạn đang có: " + player.inventory.event
//                            + " điểm sự kiện\nChúc bạn chơi game dui dẻ",
//                            "Tặng 1\n Bông hoa", "Tặng\n10 Bông", "Tặng\n99 Bông","Đổi Capsule");
//                        break;
//                        default:
//                        Service.gI().sendThongBaoFromAdmin(player, "|7|Hiện chưa mở sự kiện nào!");
//                        break;
//                        }
//                    } 
//                }
//            }
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 5 || this.mapId == 46) {
//                        switch (player.iDMark.getIndexMenu()) {
//                            case ConstEvent.MUAHE:
//                                switch (select) {
//                                    case 0:
//                                        Item SaoBien = null;
//                                        try {
//                                            SaoBien = InventoryServiceNew.gI().findItemBag(player, 698);
//                                        } catch (Exception e) {
//                                        }
//                                        if (SaoBien == null || SaoBien.quantity < 999) {
//                                            this.npcChat(player, "Bạn không đủ x999 sao biển");
//                                        } else if (player.inventory.gold < 1_000_000_000) {
//                                            this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                        } else {
//                                            player.inventory.gold -= 1_000_000_000;
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, SaoBien, 999);
//                                            Service.gI().sendMoney(player);
//                                            if (Util.isTrue(99, 100)){
//                                                Item ngocrong = ItemService.gI().createNewItem((short) Util.nextInt(16,18));
//                                                InventoryServiceNew.gI().addItemBag(player, ngocrong);
//                                                InventoryServiceNew.gI().sendItemBags(player);
//                                                this.npcChat(player, "Bạn nhận được " + ngocrong);
//                                            }
//                                            else if (Util.isTrue(1, 100)){
//                                                Item ngocrong2 = ItemService.gI().createNewItem((short) Util.nextInt(14,15));
//                                                InventoryServiceNew.gI().addItemBag(player, ngocrong2);
//                                                InventoryServiceNew.gI().sendItemBags(player);
//                                                this.npcChat(player, "Bạn nhận được " + ngocrong2);
//                                            }}
//                                    break;
//                                    case 1:
//                                        Item ConCua = null;
//                                        try {
//                                            ConCua = InventoryServiceNew.gI().findItemBag(player, 697);
//                                        } catch (Exception e) {
//                                        }
//                                        if (ConCua == null || ConCua.quantity < 999) {
//                                            this.npcChat(player, "Bạn không đủ x999 con cua");
//                                        } else if (player.inventory.gold < 1_000_000_000) {
//                                            this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                        } else {
//                                            player.inventory.gold -= 1_000_000_000;
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, ConCua, 999);
//                                            Service.gI().sendMoney(player);
//                                            Item caitrang = ItemService.gI().createNewItem((short) (1248+player.gender));
//                                            caitrang.itemOptions.add(new Item.ItemOption(50, Util.nextInt(15,35)));
//                                            caitrang.itemOptions.add(new Item.ItemOption(14, Util.nextInt(1,10)));
//                                            caitrang.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20,35)));
//                                            caitrang.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20,35)));
//                                            caitrang.itemOptions.add(new Item.ItemOption(101, Util.nextInt(20,50)));
//                                            if (Util.isTrue(999, 1000)){
//                                                caitrang.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3,15)));
//                                            }
//                                            InventoryServiceNew.gI().addItemBag(player, caitrang);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "Bạn nhận được Cải Trang Sự Kiện");
//                                        }
//                                    break;
//                                    case 2:
//                                        Item VoSo = null;
//                                        try {
//                                            VoSo = InventoryServiceNew.gI().findItemBag(player, 696);
//                                        } catch (Exception e) {
//                                        }
//                                        if (VoSo == null || VoSo.quantity < 999) {
//                                            this.npcChat(player, "Bạn không đủ x999 vỏ sò");
//                                        } else if (player.inventory.gold < 1_000_000_000) {
//                                            this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                        } else {
//                                            player.inventory.gold -= 1_000_000_000;
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, VoSo, 999);
//                                            Service.gI().sendMoney(player);
//                                            Item pet = ItemService.gI().createNewItem((short) Util.nextInt(2085,2090));
//                                            pet.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10,30)));
//                                            pet.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10,30)));
//                                            pet.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10,30)));
//                                            if (Util.isTrue(99, 100)){
//                                                pet.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1,15)));
//                                            }
//                                            InventoryServiceNew.gI().addItemBag(player, pet);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "Bạn nhận được pet");
//                                        }
//                                    break;
//                                    case 3:
//                                        Item VoOc = null;
//                                        try {
//                                            VoOc = InventoryServiceNew.gI().findItemBag(player, 695);
//                                        } catch (Exception e) {
//                                        }
//                                        if (VoOc == null || VoOc.quantity < 999) {
//                                            this.npcChat(player, "Bạn không đủ x999 vỏ ốc");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                        } else {
//                                            Item tv = ItemService.gI().createNewItem((short)457, 1);
//                                            tv.itemOptions.add(new Item.ItemOption(30, 0));
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, VoOc, 999);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            InventoryServiceNew.gI().addItemBag(player, tv);
//                                            this.npcChat(player, "Bạn nhận được 1 thỏi vàng");
//                                        }
//                                        break;
//                                }   
//                            break;
//                            case ConstEvent.HUNGVUONG:
//                                switch (select) {
//                                    case 0: //doi diem
//                                       Input.gI().duahau(player);
//                                        break;
//                                    case 1:
//                                        this.createOtherMenu(player, ConstNpc.NAP_THE, "|2|Khó Đã Có Viettinbank :3 \nNgươi đang có: " + player.inventory.coupon + " điểm sự kiện", "Đóng");
//                                        break;
//                                    case 2:
//                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_DIEM_DUA, -1, "Đổi Công Thức Chế Tạo Đồ Thiên Sứ?\nTa Cần 200 điểm sự kiện đấy... ",
//                                                "Đồng ý", "Từ chối");
//                                        break;
//                                    case 3:
//                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_DIEM_ITEMC2, -1, "Ta Sẽ Cho Con Item siêu cấp ngẫu nhiên?\nTa Cần 100 Điểm Sự Kiện... ",
//                                                "Đồng ý", "Từ chối");
//                                        break;
//                                    case 4:
//                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_DIEM_CT, -1, "Cần 999 Điểm Sự Kiện Để Lấy Cải Trang Random \nCó Tỉ Lệ May Mắn Được Vĩnh Viễn...Thử Ngay Nào ",
//                                                "Đồng ý", "Từ chối");
//                                        break;
//                                    case 5:
//                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_ITEM_NR, -1, "Còn Thở Còn Gỡ Còn Điểm Còn Đổi ..?\nPhải giao cho ta 200 điểm sự kiện đấy...\nNếu May Mắn Sẽ Nhận Được Nro Torobo random Sao, Bông Tai Porata 3,... ",
//                                                "Đồng ý", "Từ chối");
//                                        break;
//                                }   
//                            break;
//                            case ConstEvent.TRUNGTHU:
//                                switch (select) {
//                                    case 0: {
//                                        this.createOtherMenu(player, ConstEvent.TRUNGTHU_HD,
//                                                "|7|SỰ KIỆN TRUNG THU"
//                                                        + "\n\n|2|Cách thức tìm nguyên liệu nấu bánh Trung thu"
//                                                        + "\n|4|- Hạt sen : Đánh các quái bay trên không"
//                                                        + "\n- Đậu xanh : Đánh các quái dưới đất"
//                                                        + "\n- Bột nếp : Đánh quái Sên ở Tương lai"
//                                                        + "\n- Mồi lửa : Giết Boss Thỏ trắng (5 phút xuất hiện 1 lần)"
//                                                        + "\n\n|5|Ăn bánh trung thu nhận được Điểm tích lũy Sự kiện Trung thu đổi được các phần quà hấp dẫn"
//                                                        + "\n|-1|- Bánh Hạt sen : Nhận 2 Điểm Sự kiện"
//                                                        + "\n|-1|- Bánh Đậu xanh : Nhận 2 Điểm Sự kiện"
//                                                        + "\n|-1|- Bánh Thập cẩm : Nhận 5 Điểm Sự kiện"
//                                                        + "\n|5|- Quy đổi tiền 1.000Đ nhận thêm 1 Điểm Sự kiện (Không tính đổi Xu)"
//                                                        + "\n\n|7|Chị hiểu hôn ???",
//                                                "Đã hiểu");
//                                        break;
//                                    }
//                                    case 1: {
//                                        this.createOtherMenu(player, ConstEvent.TRUNGTHU_LAMBANH,
//                                                "|7|SỰ KIỆN TRUNG THU"
//                                                        + "\n\n|2|Bạn muốn làm bánh gì?",
//                                                "Bánh\nHạt sen", "Bánh\nĐậu xanh", "Bánh\nThập cẩm");
//                                        break;
//                                    }
//                                    case 2: {
//                                        this.createOtherMenu(player, ConstEvent.TRUNGTHU_DOIDIEM,
//                                                "|7|TÍCH ĐIỂM SỰ KIỆN TRUNG THU"
//                                                        + "\n\n|1|Khi đổi điểm thì sẽ được cộng điểm đua top trung thu\n"
//                                                        + "|2|Mốc 500 Điểm\n"
//                                                        + "|4|200 Mảnh thiên sứ ngẫu nhiên, 20 rương thần linh, 15 Hộp quà Trung thu, 10 Thẻ gia hạn, 1 Phiếu giảm giá + 250k HN \n"
//                                                        + "|2|Mốc 300 Điểm\n"
//                                                        + "|4|100 Mảnh thiên sứ ngẫu nhiên, 15 rương thần linh, 10 Thẻ gia hạn, 15 Hộp Trung thu + 150k HN \n"
//                                                        + "|2|Mốc 200 Điểm\n"
//                                                        + "|4|50 Mảnh thiên sứ ngẫu nhiên, 10 rương thần linh, 5 Thẻ gia hạn, 10 Hộp Trung thu + 100k HN \n"
//                                                        + "|2|Mốc 50 Điểm\n"
//                                                        + "|4|10 Mảnh thiên sứ ngẫu nhiên, 5 rương thần linh + 25k HN"
//                                                        + "\n\n|7|Điểm sự kiện : " + Util.format(player.inventory.coupon) + " Điểm"
//                                                                + "\n|1|Điểm Top Trung thu : " + Util.format(player.inventory.event) + " Điểm",
//                                                "500 Điểm", "300 Điểm", "200 Điểm", "50 Điểm");
//                                        break;
//                                    }
//                                }       
//                                break;
//                            case ConstEvent.TRUNGTHU_LAMBANH:
//                                switch (select) {
//                                    case 0: {
//                                        Item hatsen = null;
//                                        Item botnep = null;
//                                        Item moilua = null;
//                                        try {
//                                            hatsen = InventoryServiceNew.gI().findItemBag(player, 1340);
//                                            botnep = InventoryServiceNew.gI().findItemBag(player, 1338);
//                                            moilua = InventoryServiceNew.gI().findItemBag(player, 1341);
//                                        } catch (Exception e) {
////                                        throw new RuntimeException(e);
//                                        }
//                                        if (hatsen == null || hatsen.quantity < 99) {
//                                            this.npcChat(player, "|7|Bạn không đủ 99 Hạt sen");
//                                        } else if (botnep == null || botnep.quantity < 50) {
//                                            this.npcChat(player, "|7|Bạn không đủ 50 Bột nếp");
//                                        } else if (moilua == null || moilua.quantity < 2) {
//                                            this.npcChat(player, "|7|Bạn không đủ 2 Mồi lửa");
//                                        } else if (player.inventory.gold < 2_000_000_000) {
//                                            this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
//                                        } else {
//                                            player.inventory.gold -= 2_000_000_000;
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, hatsen, 99);
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, botnep, 50);
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, moilua, 2);
//                                            Service.getInstance().sendMoney(player);
//                                            Item banhtrungthu = ItemService.gI().createNewItem((short) 1336);
//                                            InventoryServiceNew.gI().addItemBag(player, banhtrungthu);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "|4|Bạn nhận được Bánh trung thu Hạt sen");
//                                        }
//                                        break;
//                                    }
//                                    case 1: {
//                                        Item dauxanh = null;
//                                        Item botnep = null;
//                                        Item moilua = null;
//                                        try {
//                                            dauxanh = InventoryServiceNew.gI().findItemBag(player, 1339);
//                                            botnep = InventoryServiceNew.gI().findItemBag(player, 1338);
//                                            moilua = InventoryServiceNew.gI().findItemBag(player, 1341);
//                                        } catch (Exception e) {
////                                        throw new RuntimeException(e);
//                                        }
//                                        if (dauxanh == null || dauxanh.quantity < 99) {
//                                            this.npcChat(player, "|7|Bạn không đủ 99 Đậu xanh");
//                                        } else if (botnep == null || botnep.quantity < 50) {
//                                            this.npcChat(player, "|7|Bạn không đủ 50 Bột nếp");
//                                        } else if (moilua == null || moilua.quantity < 2) {
//                                            this.npcChat(player, "|7|Bạn không đủ 2 Mồi lửa");
//                                        } else if (player.inventory.gold < 2_000_000_000) {
//                                            this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
//                                        } else {
//                                            player.inventory.gold -= 2_000_000_000;
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, botnep, 50);
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, moilua, 2);
//                                            Service.getInstance().sendMoney(player);
//                                            Item banhtrungthu = ItemService.gI().createNewItem((short) 1335);
//                                            InventoryServiceNew.gI().addItemBag(player, banhtrungthu);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "|4|Bạn nhận được Bánh trung thu Đậu xanh");
//                                        }
//                                        break;
//                                    }
//                                    case 2: {
//                                        Item hatsen = null;
//                                        Item dauxanh = null;
//                                        Item botnep = null;
//                                        Item moilua = null;
//                                        try {
//                                            hatsen = InventoryServiceNew.gI().findItemBag(player, 1340);
//                                            dauxanh = InventoryServiceNew.gI().findItemBag(player, 1339);
//                                            botnep = InventoryServiceNew.gI().findItemBag(player, 1338);
//                                            moilua = InventoryServiceNew.gI().findItemBag(player, 1341);
//                                        } catch (Exception e) {
////                                        throw new RuntimeException(e);
//                                        }
//                                        if (hatsen == null || hatsen.quantity < 99) {
//                                            this.npcChat(player, "|7|Bạn không đủ 99 Hạt sen");
//                                        } else if (botnep == null || botnep.quantity < 99) {
//                                            this.npcChat(player, "|7|Bạn không đủ 99 Bột nếp");
//                                        } else if (dauxanh == null || dauxanh.quantity < 99) {
//                                            this.npcChat(player, "|7|Bạn không đủ 99 Đậu xanh");
//                                        } else if (moilua == null || moilua.quantity < 5) {
//                                            this.npcChat(player, "|7|Bạn không đủ 5 Mồi lửa");
//                                        } else if (player.inventory.gold < 2_000_000_000) {
//                                            this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
//                                        } else {
//                                            player.inventory.gold -= 2_000_000_000;
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, hatsen, 99);
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, botnep, 99);
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, moilua, 5);
//                                            Service.getInstance().sendMoney(player);
//                                            Item banhtrungthu = ItemService.gI().createNewItem((short) 1337);
//                                            InventoryServiceNew.gI().addItemBag(player, banhtrungthu);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "|4|Bạn nhận được Bánh trung thu Thập cẩm");
//                                        }
//                                        break;
//                                    }
//                                }       
//                            break;
//                            case ConstEvent.TRUNGTHU_DOIDIEM:
//                                switch (select) {
//                                    case 0: {
//                                        byte randommanh = (byte) new Random().nextInt(Manager.manhts.length);
//                                        int manh = Manager.manhts[randommanh];
//                                        if (player.inventory.coupon < 500) {
//                                            this.npcChat(player, "|7|Bạn không đủ 500 Điểm sự kiện");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 5) {
//                                            this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 5 ô trống");
//                                        } else {
//                                            player.inventory.coupon -= 500;
//                                            player.inventory.event += 500;
//                                            player.inventory.ruby += 250_000;
//                                            Service.getInstance().sendMoney(player);
//                                            Item manhthiensu = ItemService.gI().createNewItem((short) manh);
//                                            Item ruongthan = ItemService.gI().createNewItem((short) 1334);
//                                            Item hoptt = ItemService.gI().createNewItem((short) 1342);
//                                            Item phieugg = ItemService.gI().createNewItem((short) 459);
//                                            Item thegh = ItemService.gI().createNewItem((short) 1346);
//                                            phieugg.itemOptions.add(new Item.ItemOption(30, 1));
//                                            manhthiensu.quantity = 200;
//                                            ruongthan.quantity = 20;
//                                            hoptt.quantity = 15;
//                                            thegh.quantity = 30;
//                                            InventoryServiceNew.gI().addItemBag(player, manhthiensu);
//                                            InventoryServiceNew.gI().addItemBag(player, ruongthan);
//                                            InventoryServiceNew.gI().addItemBag(player, hoptt);
//                                            InventoryServiceNew.gI().addItemBag(player, phieugg);
//                                            InventoryServiceNew.gI().addItemBag(player, thegh);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "|4|Bạn nhận được 200 " + manhthiensu.template.name
//                                                    + ", 20 Rương thần linh, 15 Hộp Trung thu, 30 Thẻ gia hạn, 1 Phiếu giảm giá và 250k Hồng ngọc");
//                                        }
//                                        break;
//                                    }
//                                    case 1: {
//                                        byte randommanh = (byte) new Random().nextInt(Manager.manhts.length);
//                                        int manh = Manager.manhts[randommanh];
//                                        if (player.inventory.coupon < 300) {
//                                            this.npcChat(player, "|7|Bạn không đủ 300 Điểm sự kiện");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 4) {
//                                            this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 4 ô trống");
//                                        } else {
//                                            player.inventory.coupon -= 300;
//                                            player.inventory.event += 300;
//                                            player.inventory.ruby += 150_000;
//                                            Service.getInstance().sendMoney(player);
//                                            Item manhthiensu = ItemService.gI().createNewItem((short) manh);
//                                            Item ruongthan = ItemService.gI().createNewItem((short) 1334);
//                                            Item hoptt = ItemService.gI().createNewItem((short) 1342);
//                                            Item thegh = ItemService.gI().createNewItem((short) 1346);
//                                            manhthiensu.quantity = 100;
//                                            ruongthan.quantity = 15;
//                                            hoptt.quantity = 15;
//                                            thegh.quantity = 10;
//                                            InventoryServiceNew.gI().addItemBag(player, manhthiensu);
//                                            InventoryServiceNew.gI().addItemBag(player, ruongthan);
//                                            InventoryServiceNew.gI().addItemBag(player, hoptt);
//                                            InventoryServiceNew.gI().addItemBag(player, thegh);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "|4|Bạn nhận được 100 " + manhthiensu.template.name
//                                                    + ", 15 Rương thần linh, 15 Hộp Trung thu, 10 Thẻ gia hạn và 150k Hồng ngọc");
//                                        }
//                                        break;
//                                    }
//                                    case 2: {
//                                        byte randommanh = (byte) new Random().nextInt(Manager.manhts.length);
//                                        int manh = Manager.manhts[randommanh];
//                                        if (player.inventory.coupon < 200) {
//                                            this.npcChat(player, "|7|Bạn không đủ 200 Điểm sự kiện");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 4) {
//                                            this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 4 ô trống");
//                                        } else {
//                                            player.inventory.coupon -= 200;
//                                            player.inventory.event += 200;
//                                            player.inventory.ruby += 100_000;
//                                            Service.getInstance().sendMoney(player);
//                                            Item manhthiensu = ItemService.gI().createNewItem((short) manh);
//                                            Item ruongthan = ItemService.gI().createNewItem((short) 1334);
//                                            Item hoptt = ItemService.gI().createNewItem((short) 1342);
//                                            Item thegh = ItemService.gI().createNewItem((short) 1346);
//                                            manhthiensu.quantity = 50;
//                                            ruongthan.quantity = 10;
//                                            hoptt.quantity = 10;
//                                            thegh.quantity = 5;
//                                            InventoryServiceNew.gI().addItemBag(player, manhthiensu);
//                                            InventoryServiceNew.gI().addItemBag(player, ruongthan);
//                                            InventoryServiceNew.gI().addItemBag(player, hoptt);
//                                            InventoryServiceNew.gI().addItemBag(player, thegh);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "|4|Bạn nhận được 50 " + manhthiensu.template.name
//                                                    + ", 10 Rương thần linh, 10 Hộp Trung thu, 5 Thẻ gia hạn và 100k Hồng ngọc");
//                                        }
//                                        break;
//                                    }
//                                    case 3: {
//                                        byte randommanh = (byte) new Random().nextInt(Manager.manhts.length);
//                                        int manh = Manager.manhts[randommanh];
//                                        if (player.inventory.coupon < 50) {
//                                            this.npcChat(player, "|7|Bạn không đủ 50 Điểm sự kiện");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 2) {
//                                            this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 2 ô trống");
//                                        } else {
//                                            player.inventory.coupon -= 50;
//                                            player.inventory.event += 50;
//                                            player.inventory.ruby += 25_000;
//                                            Service.getInstance().sendMoney(player);
//                                            Item manhthiensu = ItemService.gI().createNewItem((short) manh);
//                                            Item ruongthan = ItemService.gI().createNewItem((short) 1334);
//                                            manhthiensu.quantity = 10;
//                                            ruongthan.quantity = 5;
//                                            InventoryServiceNew.gI().addItemBag(player, manhthiensu);
//                                            InventoryServiceNew.gI().addItemBag(player, ruongthan);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "|4|Bạn nhận được 10 " + manhthiensu.template.name
//                                                    + ", 5 Rương thần linh và 25k Hồng ngọc");
//                                        }
//                                        break;
//                                    }
//                                }  
//                                break;
//                                case ConstEvent.HLWEEN:
//                                switch (select) {
//                                case 0:
//                                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                Item keo = InventoryServiceNew.gI().finditemnguyenlieuKeo(player);
//                                Item banh = InventoryServiceNew.gI().finditemnguyenlieuBanh(player);
//                                Item bingo = InventoryServiceNew.gI().finditemnguyenlieuBingo(player);
//                                if (keo != null && banh != null && bingo != null) {
//                                Item GioBingo = ItemService.gI().createNewItem((short) 2016, 1);
//                                // - Số item sự kiện có trong rương
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, keo, 10);
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, banh, 10);
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, bingo, 10);
//
//                                GioBingo.itemOptions.add(new ItemOption(74, 0));
//                                InventoryServiceNew.gI().addQuantityItemsBag(player, GioBingo, 0);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendThongBao(player, "Đổi quà sự kiện thành công");
//                            } else {
//                                Service.getInstance().sendThongBao(player,
//                                        "Vui lòng chuẩn bị x10 Nguyên Liệu Kẹo, Bánh Quy, Bí Ngô để đổi vật phẩm sự kiện");
//                            }
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                        }
//                        break;
//                    case 1:
//                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                            Item ve = InventoryServiceNew.gI().finditemnguyenlieuVe(player);
//                            Item giokeo = InventoryServiceNew.gI().finditemnguyenlieuGiokeo(player);
//
//                            if (ve != null && giokeo != null) {
//                                Item Hopmaquy = ItemService.gI().createNewItem((short) 2017, 1);
//                                // - Số item sự kiện có trong rương
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, ve, 3);
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, giokeo, 3);
//
//                                Hopmaquy.itemOptions.add(new ItemOption(74, 0));
//                                InventoryServiceNew.gI().addQuantityItemsBag(player, Hopmaquy, 0);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendThongBao(player, "Đổi quà sự kiện thành công");
//                            } else {
//                                Service.getInstance().sendThongBao(player,
//                                        "Vui lòng chuẩn bị x3 Vé đổi Kẹo và x3 Giỏ kẹo để đổi vật phẩm sự kiện");
//                            }
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                        }
//                        break;
//                    case 2:
//                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                            Item ve = InventoryServiceNew.gI().finditemnguyenlieuVe(player);
//                            Item giokeo = InventoryServiceNew.gI().finditemnguyenlieuGiokeo(player);
//                            Item hopmaquy = InventoryServiceNew.gI().finditemnguyenlieuHopmaquy(player);
//
//                            if (ve != null && giokeo != null && hopmaquy != null) {
//                                Item HopQuaHLW = ItemService.gI().createNewItem((short) 2012, 1);
//                                // - Số item sự kiện có trong rương
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, ve, 3);
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, giokeo, 3);
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, hopmaquy, 3);
//
//                                HopQuaHLW.itemOptions.add(new ItemOption(74, 0));
//                                HopQuaHLW.itemOptions.add(new ItemOption(30, 0));
//                                InventoryServiceNew.gI().addQuantityItemsBag(player, HopQuaHLW, 0);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendThongBao(player,
//                                        "Đổi quà hộp quà sự kiện Halloween thành công");
//                            } else {
//                                Service.getInstance().sendThongBao(player,
//                                        "Vui lòng chuẩn bị x3 Hộp Ma Quỷ, x3 Vé đổi Kẹo và x3 Giỏ kẹo để đổi vật phẩm sự kiện");
//                            }
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                        }
//                        break;
//                }
//                break;
//                case ConstEvent.NHAGIAO:// 20/11
//                switch (select) {
//                    case 3:
//                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                            if (player.inventory.event >= 999) {
//                                Item HopQua = ItemService.gI().createNewItem((short) 763, 1);
//                                player.inventory.event -= 999;
//                                HopQua.itemOptions.add(new ItemOption(207, 0));
//                                HopQua.itemOptions.add(new ItemOption(30, 0));
//                                InventoryServiceNew.gI().addItemBag(player, HopQua);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendThongBao(player, "Bạn nhận được Hộp Quà Teacher Day");
//                            } else {
//                                Service.getInstance().sendThongBao(player, "Cần 999 điểm tích lũy để đổi");
//                            }
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                        }
//                        break;
//                    default:
//                        int n = 0;
//                        switch (select) {
//                            case 0:
//                                n = 1;
//                                break;
//                            case 1:
//                                n = 10;
//                                break;
//                            case 2:
//                                n = 99;
//                                break;
//                        }
//                        if (n > 0) {
//                            Item bonghoa = InventoryServiceNew.gI().finditemBongHoa(player, n);
//                            if (bonghoa != null) {
//                                player.inventory.event += n;
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, bonghoa, n);
//                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
//                            } else {
//                                Service.getInstance().sendThongBao(player,
//                                        "Cần ít nhất " + n + " bông hoa để có thể tặng");
//                            }
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Cần ít nhất " + n + " bông hoa để có thể tặng");
//                        }
//                }
//                break;
//                case ConstEvent.NOEL:
//                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                    Item keogiangsinh = InventoryServiceNew.gI().finditemKeoGiangSinh(player);
//
//                    if (keogiangsinh != null && keogiangsinh.quantity >= 99) {
//                        Item tatgiangsinh = ItemService.gI().createNewItem((short) 649, 1);
//                        // - Số item sự kiện có trong rương
//                        InventoryServiceNew.gI().subQuantityItemsBag(player, keogiangsinh, 99);
//
//                        tatgiangsinh.itemOptions.add(new ItemOption(74, 0));
//                        tatgiangsinh.itemOptions.add(new ItemOption(30, 0));
//                        InventoryServiceNew.gI().addQuantityItemsBag(player, tatgiangsinh, 0);
//                        InventoryServiceNew.gI().sendItemBags(player);
//                        Service.getInstance().sendThongBao(player, "Bạn nhận được Tất,vớ giáng sinh");
//                    } else {
//                        Service.getInstance().sendThongBao(player,
//                                "Vui lòng chuẩn bị x99 kẹo giáng sinh để đổi vớ tất giáng sinh");
//                    }
//                } else {
//                    Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                }
//                break;
//                case ConstEvent.TET:
//                switch (select) {
//                    case 0:
//                        if (player.diemdanhtet < 1) {
//                            Calendar cal = Calendar.getInstance();
//                            int day = cal.get(Calendar.DAY_OF_MONTH);
//                            if (day >= 8 && day <= 15) {
//                                player.diemdanhtet++;
//                                int[] hn = {1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000};
//                                int slhn = hn[Util.nextInt(hn.length)];
//                                int sltv = Util.nextInt(1, 10);
//                                Item tv = ItemService.gI().createNewItem((short) 457);
//                                player.inventory.ruby += slhn;
//                                tv.quantity = sltv;
//                                InventoryServiceNew.gI().addItemBag(player, tv);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                PlayerService.gI().sendInfoHpMpMoney(player);
//                                Service.getInstance().sendThongBaoOK(player, "NroKamui lì xì cho bạn\nx" + slhn + " hồng ngọc\nx" + sltv + " thỏi vàng");
//                                Service.getInstance().sendThongBao(player, "Nhận lì xì thành công,chúc bạn năm mới dui dẻ");
//                            } else if (day > 15) {
//                                Service.getInstance().sendThongBao(player, "Hết tết rồi còn đòi lì xì");
//                            } else {
//                                Service.getInstance().sendThongBao(player, "Đã tết đâu mà đòi lì xì");
//                            }
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Bạn đã nhận lì xì rồi");
//                        }
//                        break;
//                    case 1:
//                        ShopServiceNew.gI().opendShop(player, "SHOP_LUOT", true);
//                        break;
//                    case 2:
//                        ShopServiceNew.gI().opendShop(player, "SHOP_TET", true);
//                        break;
//                    case 3:
//                        if (InventoryServiceNew.gI().findItemBag(player, 1436) == null && InventoryServiceNew.gI().findItemBody(player, 1436) == null) {
//                            Item qdbm = ItemService.gI().createNewItem((short) 1436);
//                            qdbm.itemOptions.add(new ItemOption(101, 12));
//                            qdbm.itemOptions.add(new ItemOption(30, 0));
//                            InventoryServiceNew.gI().addItemBag(player, qdbm);
//                            InventoryServiceNew.gI().sendItemBags(player);
//                            Service.getInstance().sendThongBao(player, "Bạn nhận được Quần Hoa Văn");
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Bạn đã nhận Quần Hoa Văn rồi.");
//                        }
//                        break;
//                }
//                break;
//            case ConstEvent.PHUNU:
//                switch (select) {
//                    case 3:
//                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                            Input.gI().bonghoa(player);
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                        }
//                        break;
//                    default:
//                        int n = 0;
//                        switch (select) {
//                            case 0:
//                                n = 1;
//                                break;
//                            case 1:
//                                n = 10;
//                                break;
//                            case 2:
//                                n = 99;
//                                break;
//                        }
//                        if (n > 0) {
//                            Item bonghoa = InventoryServiceNew.gI().finditemBongHoa(player, n);
//                            if (bonghoa != null) {
//                                player.inventory.event += n;
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, bonghoa, n);
//                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
//                            } else {
//                                Service.getInstance().sendThongBao(player,
//                                        "Cần ít nhất " + n + " bông hoa để có thể tặng");
//                            }
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Cần ít nhất " + n + " bông hoa để có thể tặng");
//                        }
//                    }
//                    break;
//                    default:
//                        break;
//                        }
//                    }
//                }
//            }
//        };
//    }
//    private static Npc Skien_tet(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7| SỰ KIỆN TRUNG THU"
//                            + "\n\n|2|Nguyên liệu cần nấu Bánh Trưng"
//                            + "\n|-1|- Bánh Trưng Nhưng Mỡ : 99 Thịt Mỡ + 50 Hạt Nếp + 2 Mồi lửa"
//                            + "\n|-1|- Bánh Trưng Nhưng Thịt : 99 Thịt Nạt + 50 Hạt Nếp + 2 Mồi lửa"
//                            + "\n|-1|- Bánh Trưng Đặc Biệt : 99 Hạt sen + 99 Đậu xanh + 99 Bột nếp + 5 Mồi lửa"
//                            + "\n|7|Làm bánh sẽ tốn phí 2tỷ Vàng/lần"
//                            + "\n\n|1|Điểm sự kiện : " + Util.format(player.diemtrungthu) + " Điểm",
//                            "Thể lệ", "Làm bánh", "Đổi điểm\nNấu Bánh", "Top Sự\nKiện");
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (player.iDMark.isBaseMenu()) {
//                        switch (select) {
//                            case 0: {
//                                this.createOtherMenu(player, 1234,
//                                        "|7|Sự Kiện Nấu Bánh Trưng"
//                                        + "\n\n|2|Cách thức tìm nguyên liệu Nấu Bánh Bánh Trưng"
//                                        + "\n|4|- Thịt Mỡ : Đánh các quái bay trên không"
//                                        + "\n- Thịt Nạt : Đánh các quái dưới đất"
//                                        + "\n- Hạt Nếp : Đánh quái Sên ở Tương lai"
//                                        + "\n- Mồi lửa : Giết Boss Thỏ trắng (5 phút xuất hiện 1 lần)"
//                                        + "\n\n|5|Ăn bánh Bánh Trưng nhận được Điểm tích lũy Sự kiện Trung thu đổi được các phần quà hấp dẫn"
//                                        + "\n|-1|- Bánh Nhưng Mỡ : Nhận 2 Điểm Sự kiện"
//                                        + "\n|-1|- Bánh Nhưng Thịt : Nhận 2 Điểm Sự kiện"
//                                        + "\n|-1|- Bánh Thập cẩm : Nhận 5 Điểm Sự kiện"
//                                        + "\n|5|- Quy đổi tiền 1.000Đ nhận thêm 1 Điểm Sự kiện (Không tính đổi Xu)"
//                                        + "\n\n|7|Chị hiểu hôn ???",
//                                        "Đã hiểu");
//                                break;
//                            }
//                            case 1: {
//                                this.createOtherMenu(player, 1111,
//                                        "|7|Sự Kiện Nấu Bánh Bánh Trưng"
//                                        + "\n\n|2|Bạn muốn làm bánh gì?",
//                                        "Bánh\nNhưng Mỡ", "Bánh\nNhưng Thịt", "Bánh\nThập cẩm");
//                                break;
//                            }
//                            case 2: {
//                                this.createOtherMenu(player, 2222,
//                                        "|7|Tích Điểm Nấu Bánh Bánh Trưng"
//                                        + "\n\n|1|Khi đổi điểm thì sẽ được cộng điểm đua top Nấu Bánh Bánh Trưng\n"
//                                        + "|2|Mốc 500 Điểm\n"
//                                        + "|4|200 Mảnh thiên sứ ngẫu nhiên, 50 rương Hủy diệt, 30 Hộp quà Tết, 30 Thẻ gia hạn, 1 Phiếu giảm giá + 250k HN \n"
//                                        + "|2|Mốc 300 Điểm\n"
//                                        + "|4|100 Mảnh thiên sứ ngẫu nhiên, 40 rương Hủy diệt, 10 Thẻ gia hạn, 15 Hộp quà tết + 150k HN \n"
//                                        + "|2|Mốc 200 Điểm\n"
//                                        + "|4|50 Mảnh thiên sứ ngẫu nhiên, 30 rương Hủy diệt, 5 Thẻ gia hạn, 10 Hộp quà tết + 100k HN \n"
//                                        + "|2|Mốc 50 Điểm\n"
//                                        + "|4|10 Mảnh thiên sứ ngẫu nhiên, 5 rương Hủy diệt + 25k HN"
//                                        + "\n\n|7|Điểm sự kiện : " + Util.format(player.diemtrungthu) + " Điểm"
//                                        + "\n|1|Điểm Top Nấu Bánh Bánh Trưng : " + Util.format(player.inventory.skien) + " Điểm",
//                                        "500 Điểm", "300 Điểm", "200 Điểm", "50 Điểm");
//                                break;
//                            }
//                            case 3:
//                                Service.getInstance().sendThongBaoOK(player, TopService.gettopnaubanhtrung());
//                                break;
//                        }
//                    } else if (player.iDMark.getIndexMenu() == 1111) {
//                        switch (select) {
//                            case 0: {
//                                Item hatsen = null;
//                                Item botnep = null;
//                                Item moilua = null;
//                                try {
//                                    hatsen = InventoryServiceNew.gI().findItemBag(player, 1175);
//                                    botnep = InventoryServiceNew.gI().findItemBag(player, 1177);
//                                    moilua = InventoryServiceNew.gI().findItemBag(player, 1176);
//                                } catch (Exception e) {
////                                        throw new RuntimeException(e);
//                                }
//                                if (hatsen == null || hatsen.quantity < 99) {
//                                    this.npcChat(player, "|7|Bạn không đủ 99 hạt sen");
//                                } else if (botnep == null || botnep.quantity < 50) {
//                                    this.npcChat(player, "|7|Bạn không đủ 50 bột nếp");
//                                } else if (moilua == null || moilua.quantity < 2) {
//                                    this.npcChat(player, "|7|Bạn không đủ 2 Mồi lửa");
//                                } else if (player.inventory.gold < 2_000_000_000) {
//                                    this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
//                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                    this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
//                                } else {
//                                    player.inventory.gold -= 2_000_000_000;
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, hatsen, 99);
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, botnep, 50);
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, moilua, 2);
//                                    Service.getInstance().sendMoney(player);
//                                    Item banhtrungthu = ItemService.gI().createNewItem((short) 465);
//                                    InventoryServiceNew.gI().addItemBag(player, banhtrungthu);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    this.npcChat(player, "|4|Bạn nhận được Bánh Trung Thu 1 trứng");
//                                }
//                                break;
//                            }
//                            case 1: {
//                                Item dauxanh = null;
//                                Item botnep = null;
//                                Item moilua = null;
//                                try {
//                                    dauxanh = InventoryServiceNew.gI().findItemBag(player, 1178);
//                                    botnep = InventoryServiceNew.gI().findItemBag(player, 1177);
//                                    moilua = InventoryServiceNew.gI().findItemBag(player, 1176);
//                                } catch (Exception e) {
////                                        throw new RuntimeException(e);
//                                }
//                                if (dauxanh == null || dauxanh.quantity < 99) {
//                                    this.npcChat(player, "|7|Bạn không đủ 99 Đậu xanh");
//                                } else if (botnep == null || botnep.quantity < 50) {
//                                    this.npcChat(player, "|7|Bạn không đủ 50 Bột nếp");
//                                } else if (moilua == null || moilua.quantity < 2) {
//                                    this.npcChat(player, "|7|Bạn không đủ 2 Mồi lửa");
//                                } else if (player.inventory.gold < 2_000_000_000) {
//                                    this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
//                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                    this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
//                                } else {
//                                    player.inventory.gold -= 2_000_000_000;
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, botnep, 50);
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, moilua, 2);
//                                    Service.getInstance().sendMoney(player);
//                                    Item banhtrungthu = ItemService.gI().createNewItem((short) 466);
//                                    InventoryServiceNew.gI().addItemBag(player, banhtrungthu);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    this.npcChat(player, "|4|Bạn nhận được Bánh Trung Thu 2 trứng");
//                                }
//                                break;
//                            }
//                            case 2: {
//                                Item hatsen = null;
//                                Item dauxanh = null;
//                                Item botnep = null;
//                                Item moilua = null;
//                                try {
//                                    hatsen = InventoryServiceNew.gI().findItemBag(player, 1175);
//                                    dauxanh = InventoryServiceNew.gI().findItemBag(player, 1178);
//                                    botnep = InventoryServiceNew.gI().findItemBag(player, 1177);
//                                    moilua = InventoryServiceNew.gI().findItemBag(player, 1176);
//                                } catch (Exception e) {
////                                        throw new RuntimeException(e);
//                                }
//                                if (hatsen == null || hatsen.quantity < 99) {
//                                    this.npcChat(player, "|7|Bạn không đủ 99 Hạt sen");
//                                } else if (botnep == null || botnep.quantity < 99) {
//                                    this.npcChat(player, "|7|Bạn không đủ 99 Bột nếp");
//                                } else if (dauxanh == null || dauxanh.quantity < 99) {
//                                    this.npcChat(player, "|7|Bạn không đủ 99 Đậu xanh");
//                                } else if (moilua == null || moilua.quantity < 5) {
//                                    this.npcChat(player, "|7|Bạn không đủ 5 Mồi lửa");
//                                } else if (player.inventory.gold < 2_000_000_000) {
//                                    this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
//                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                    this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
//                                } else {
//                                    player.inventory.gold -= 2_000_000_000;
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, hatsen, 99);
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, botnep, 99);
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, moilua, 5);
//                                    Service.getInstance().sendMoney(player);
//                                    Item banhtrungthu = ItemService.gI().createNewItem((short) 472);
//                                    InventoryServiceNew.gI().addItemBag(player, banhtrungthu);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    this.npcChat(player, "|4|Bạn nhận được Bánh Trung Thu Đặc Biệt");
//                                }
//                                break;
//                            }
//                        }
//                    } else if (player.iDMark.getIndexMenu() == 2222) {
//                        switch (select) {
//                            case 0: {
//                                byte randommanh = (byte) new Random().nextInt(Manager.itemManh.length);
//                                int manh = Manager.itemManh[randommanh];
//                                if (player.diemtrungthu < 500) {
//                                    this.npcChat(player, "|7|Bạn không đủ 500 Điểm sự kiện");
//                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 5) {
//                                    this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 5 ô trống");
//                                } else {
//                                    player.diemtrungthu -= 500;
//                                    player.diemnaubanhtrung += 500;
//                                    player.inventory.ruby += 250_000;
//                                    Service.getInstance().sendMoney(player);
//                                    Item manhthiensu = ItemService.gI().createNewItem((short) manh);
//                                    Item ruongthan = ItemService.gI().createNewItem((short) 2005);
//                                    Item hoptt = ItemService.gI().createNewItem((short) 1186);
//                                    Item phieugg = ItemService.gI().createNewItem((short) 459);
//                                    Item thegh = ItemService.gI().createNewItem((short) 1650);
//                                    phieugg.itemOptions.add(new Item.ItemOption(30, 1));
//                                    manhthiensu.quantity = 200;
//                                    ruongthan.quantity = 50;
//                                    hoptt.quantity = 30;
//                                    thegh.quantity = 30;
//                                    InventoryServiceNew.gI().addItemBag(player, manhthiensu);
//                                    InventoryServiceNew.gI().addItemBag(player, ruongthan);
//                                    InventoryServiceNew.gI().addItemBag(player, hoptt);
//                                    InventoryServiceNew.gI().addItemBag(player, phieugg);
//                                    InventoryServiceNew.gI().addItemBag(player, thegh);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    this.npcChat(player, "|4|Bạn nhận được 200 " + manhthiensu.template.name
//                                            + ", 50 Rương thần linh, 30 Hộp Bánh Trưng, 30 Thẻ gia hạn, 1 Phiếu giảm giá và 250k Hồng ngọc");
//                                }
//                                break;
//                            }
//                            case 1: {
//                                byte randommanh = (byte) new Random().nextInt(Manager.itemManh.length);
//                                int manh = Manager.itemManh[randommanh];
//                                if (player.diemtrungthu < 300) {
//                                    this.npcChat(player, "|7|Bạn không đủ 300 Điểm sự kiện");
//                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 4) {
//                                    this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 4 ô trống");
//                                } else {
//                                    player.diemtrungthu -= 300;
//                                    player.diemnaubanhtrung += 300;
//                                    player.inventory.ruby += 150_000;
//                                    Service.getInstance().sendMoney(player);
//                                    Item manhthiensu = ItemService.gI().createNewItem((short) manh);
//                                    Item ruongthan = ItemService.gI().createNewItem((short) 2005);
//                                    Item hoptt = ItemService.gI().createNewItem((short) 1186);
//                                    Item thegh = ItemService.gI().createNewItem((short) 1650);
//                                    manhthiensu.quantity = 100;
//                                    ruongthan.quantity = 40;
//                                    hoptt.quantity = 15;
//                                    thegh.quantity = 10;
//                                    InventoryServiceNew.gI().addItemBag(player, manhthiensu);
//                                    InventoryServiceNew.gI().addItemBag(player, ruongthan);
//                                    InventoryServiceNew.gI().addItemBag(player, hoptt);
//                                    InventoryServiceNew.gI().addItemBag(player, thegh);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    this.npcChat(player, "|4|Bạn nhận được 100 " + manhthiensu.template.name
//                                            + ", 40 Rương thần linh, 15 Hộp Bánh trưng, 10 Thẻ gia hạn và 150k Hồng ngọc");
//                                }
//                                break;
//                            }
//                            case 2: {
//                                byte randommanh = (byte) new Random().nextInt(Manager.itemManh.length);
//                                int manh = Manager.itemManh[randommanh];
//                                if (player.diemtrungthu < 200) {
//                                    this.npcChat(player, "|7|Bạn không đủ 200 Điểm sự kiện");
//                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 4) {
//                                    this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 4 ô trống");
//                                } else {
//                                    player.diemtrungthu -= 200;
//                                    player.diemnaubanhtrung += 200;
//                                    player.inventory.ruby += 100_000;
//                                    Service.getInstance().sendMoney(player);
//                                    Item manhthiensu = ItemService.gI().createNewItem((short) manh);
//                                    Item ruongthan = ItemService.gI().createNewItem((short) 2005);
//                                    Item hoptt = ItemService.gI().createNewItem((short) 1186);
//                                    Item thegh = ItemService.gI().createNewItem((short) 1650);
//                                    manhthiensu.quantity = 50;
//                                    ruongthan.quantity = 30;
//                                    hoptt.quantity = 10;
//                                    thegh.quantity = 5;
//                                    InventoryServiceNew.gI().addItemBag(player, manhthiensu);
//                                    InventoryServiceNew.gI().addItemBag(player, ruongthan);
//                                    InventoryServiceNew.gI().addItemBag(player, hoptt);
//                                    InventoryServiceNew.gI().addItemBag(player, thegh);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    this.npcChat(player, "|4|Bạn nhận được 50 " + manhthiensu.template.name
//                                            + ", 30 Rương thần linh, 10 Hộp Bánh trưng, 5 Thẻ gia hạn và 100k Hồng ngọc");
//                                }
//                                break;
//                            }
//                            case 3: {
//                                byte randommanh = (byte) new Random().nextInt(Manager.itemManh.length);
//                                int manh = Manager.itemManh[randommanh];
//                                if (player.diemtrungthu < 50) {
//                                    this.npcChat(player, "|7|Bạn không đủ 50 Điểm sự kiện");
//                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 2) {
//                                    this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 2 ô trống");
//                                } else {
//                                    player.diemtrungthu -= 50;
//                                    player.diemnaubanhtrung += 50;
//                                    player.inventory.ruby += 25_000;
//                                    Service.getInstance().sendMoney(player);
//                                    Item manhthiensu = ItemService.gI().createNewItem((short) manh);
//                                    Item ruongthan = ItemService.gI().createNewItem((short) 2005);
//                                    manhthiensu.quantity = 10;
//                                    ruongthan.quantity = 5;
//                                    InventoryServiceNew.gI().addItemBag(player, manhthiensu);
//                                    InventoryServiceNew.gI().addItemBag(player, ruongthan);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    this.npcChat(player, "|4|Bạn nhận được 10 " + manhthiensu.template.name
//                                            + ", 5 Rương thần linh và 25k Hồng ngọc");
//                                }
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }
//
//    public static Npc Pic2Mai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (this.mapId == 7) {
//                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                            "|7|Hiện Tại Bạn Đang Đang Có Số Điểm Sự Kiện Là " + player.NguHanhSonPoint + "\nĐể Đổi Hộp Qùa Sự Kiện Hè Bạn Cần 99 San Hô + 99 Bình Nước + 99 Khúc Gỗ + 1 Que Diêm + 5 Thỏi Vàng. Bạn Sẽ Nhận Được Hộp Qùa VIP Và Điểm Sự Kiện. Ngoài Ra Bạn Có Thể Sử Dụng X99 Nguyên Liệu Mỗi Loại Kết Hợp Với 1 Que Diêm Để Nhận Được Hộp Qùa Thường Và Điểm Sự Kiện.", "Đổi \n Hộp Qùa VIP", "Đổi \n Hộp Qùa VIP");
//
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 7) {
//                        if (player.iDMark.isBaseMenu()) {
//                            switch (select) {
//                                case 0: {
//                                    Item sanho = null;
//                                    Item binhnuoc = null;
//                                    Item khucgo = null;
//                                    Item quediem = null;
//                                    Item thoivang = null;
//
//                                    try {
//                                        sanho = InventoryServiceNew.gI().findItemBag(player, 1251);
//                                        binhnuoc = InventoryServiceNew.gI().findItemBag(player, 1252);
//                                        khucgo = InventoryServiceNew.gI().findItemBag(player, 1253);
//                                        quediem = InventoryServiceNew.gI().findItemBag(player, 1254);
//                                        thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
//
//                                    } catch (Exception e) {
////                                        throw new RuntimeException(e);
//                                    }
//                                    if (sanho == null || sanho.quantity < 99 || binhnuoc == null || binhnuoc.quantity < 99 || khucgo == null || khucgo.quantity < 99 || quediem == null || quediem.quantity < 1) {
//                                        this.npcChat(player, "Bạn không đủ nguyên liệu để nấu bánh");
//                                    } else if (thoivang == null || thoivang.quantity < 5) {
//                                        this.npcChat(player, "Bạn không đủ thỏi vàng");
//                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                    } else {
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, sanho, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, binhnuoc, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, khucgo, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, quediem, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 5);
//
//                                        Service.getInstance().sendMoney(player);
//                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 1255);
//                                        player.NguHanhSonPoint += 1;
//                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        this.npcChat(player, "Bạn nhận được 1 Hộp Qùa VIP");
//                                    }
//                                    break;
//                                }
//
//                                case 1: {
//                                    Item sanho = null;
//                                    Item binhnuoc = null;
//                                    Item khucgo = null;
//                                    Item quediem = null;
//
//                                    try {
//                                        sanho = InventoryServiceNew.gI().findItemBag(player, 1251);
//                                        binhnuoc = InventoryServiceNew.gI().findItemBag(player, 1252);
//                                        khucgo = InventoryServiceNew.gI().findItemBag(player, 1253);
//                                        quediem = InventoryServiceNew.gI().findItemBag(player, 1254);
//
//                                    } catch (Exception e) {
////                                        throw new RuntimeException(e);
//                                    }
//                                    if (sanho == null || sanho.quantity < 99 || binhnuoc == null || binhnuoc.quantity < 99 || khucgo == null || khucgo.quantity < 99 || quediem == null || quediem.quantity < 1) {
//                                        this.npcChat(player, "Bạn không đủ nguyên liệu để nấu bánh");
//                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                    } else {
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, sanho, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, khucgo, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, quediem, 1);
//                                        Service.getInstance().sendMoney(player);
//                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 1255);
//                                        player.NguHanhSonPoint += 1;
//                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, binhnuoc, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, khucgo, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, quediem, 1);
//                                        Service.getInstance().sendMoney(player);
//                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        this.npcChat(player, "Bạn nhận được 1 Hộp Qùa Thường");
//                                    }
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }
//
//    public static Npc hungvuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Giỗ tổ hùng vương là 1 ngày lễ của việt nam ta con muốn gì nào:"
//                            + "\n|7| Để Gộp Tre Trăm Đốt Cần 1K khung tre + 1TV vàng"
//                            + "\n|3| Cần 50 điểm cho 1 lần đổi", "Gộp Tre Trăm Đốt", "Đổi điểm đánh sơn tinh", "Đổi điểm đánh thủy tinh", "Đổi dưa hấu", "Xem Số Điểm sự kiện", "Đóng");
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (player.iDMark.isBaseMenu()) {
//                        switch (select) {
//                            case 0:
//                                Item khungtre = null;
//                                Item tv = null;
//                                try {
//                                    khungtre = InventoryServiceNew.gI().findItemBag(player, 2019);
//                                    tv = InventoryServiceNew.gI().findItemBag(player, 457);
//                                } catch (Exception e) {
//                                }
//                                if (khungtre == null || khungtre.quantity < 1000) {
//                                    this.npcChat(player, "Bạn còn thiếu x1000 Khung tre");
//                                } else if (tv == null || tv.quantity < 1) {
//                                    this.npcChat(player, "Bạn còn thiếu x1 Thỏi vàng");
//                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                    this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                } else {
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, khungtre, 1000);
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
//                                    Item vacxin = ItemService.gI().createNewItem((short) 1174);
//                                    khungtre.itemOptions.add(new Item.ItemOption(73, 0));
//                                    InventoryServiceNew.gI().addItemBag(player, vacxin);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    this.npcChat(player, "Bạn nhận được Cây tre trăm đốt");
//                                }
//                                break;
//                            case 1:
//                                //    if (Manager.SUKIEN == 2) {
//                                if (player.diemsontinh >= 50) {
//                                    Item sontinh = ItemService.gI().createItemSetKichHoat(421, 1);
//                                    sontinh.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 50)));
//                                    sontinh.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 150)));
//                                    sontinh.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 50)));
//                                    if (Util.isTrue(199, 200)) {
//                                        sontinh.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
//                                    }
//                                    InventoryServiceNew.gI().addItemBag(player, sontinh);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    player.diemsontinh -= 50;
//                                    Service.getInstance().sendThongBao(player, "\n|7|Chúc mừng bạn nhận được cải trang thành sơn tinh");
//                                    break;
//                                } else {
//                                    Service.getInstance().sendThongBao(player, "Không đủ 50 điểm");
//                                }
//                                //   } else {
//                                //       Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
//                                //   }
//                                break;
//                            case 2:
//                                //  if (Manager.SUKIEN == 2) {
//                                if (player.diemthuytinh >= 50) {
//                                    Item thuytinh = ItemService.gI().createItemSetKichHoat(422, 1);
//                                    thuytinh.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 50)));
//                                    thuytinh.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 50)));
//                                    thuytinh.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 150)));
//                                    if (Util.isTrue(199, 200)) {
//                                        thuytinh.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
//                                    }
//                                    InventoryServiceNew.gI().addItemBag(player, thuytinh);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    player.diemthuytinh -= 50;
//                                    Service.getInstance().sendThongBao(player, "\n|5|Chúc mừng bạn nhận được cải trang thành thủy tinh");
//                                    break;
//                                } else {
//                                    Service.getInstance().sendThongBao(player, "Không đủ 50 điểm");
//                                }
//                                //    } else {
//                                //        Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
//                                //    }
//                                break;
//                            case 3:
//                                //     if (Manager.SUKIEN == 2) {
//                                this.createOtherMenu(player, ConstNpc.MENUSUKIENGIOTO, "Con muốn đổi dưa hấu hả", "1 quả 10 thỏi vàng", "5 quả 70 thỏi vàng", "10 quả 200 thỏi vàng", "15 quả 350 thỏi vàng", "20 quả 500 thỏi vàng", "25 quả 700 thỏi vàng", "Đóng");
//                                //      } else {
//                                //         Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
//                                //     }
//                                break;
//                            case 4:
//                                //   if (Manager.SUKIEN == 2) {
//                                Service.getInstance().sendThongBaoOK(player, "Bạn đã tiêu diệt số sơn tinh là :" + player.diemsontinh + "\nBạn đã tiêu diệt số thủy tinh là :" + player.diemthuytinh);
//                                //    } else {
//                                //        Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
//                                //    }
//                                break;
//                        }
//                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENUSUKIENGIOTO) {
//                        //   if (Manager.SUKIEN == 2) {
//                        switch (select) {
//                            case 0:
//                                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                    Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
//                                    if (duahau != null && duahau.quantity >= 1) {
//                                        Item thoivang = ItemService.gI().createNewItem((short) 457, 10);
//
//                                        // - Số item sự kiện có trong rương
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 1);
//
//                                        thoivang.itemOptions.add(new Item.ItemOption(73, 0));
//                                        InventoryServiceNew.gI().addItemBag(player, thoivang);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.getInstance().sendThongBao(player, "Đổi thành công");
//                                    } else {
//                                        Service.getInstance().sendThongBao(player, "Không đủ dưa");
//                                    }
//                                } else {
//                                    Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                                }
//                                break;
//                            case 1:
//                                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                    Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
//                                    if (duahau != null && duahau.quantity >= 5) {
//                                        Item thoivang = ItemService.gI().createNewItem((short) 457, 70);
//
//                                        // - Số item sự kiện có trong rương
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 5);
//
//                                        thoivang.itemOptions.add(new Item.ItemOption(73, 0));
//                                        InventoryServiceNew.gI().addItemBag(player, thoivang);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.getInstance().sendThongBao(player, "Đổi thành công");
//                                    } else {
//                                        Service.getInstance().sendThongBao(player, "Không đủ dưa");
//                                    }
//                                } else {
//                                    Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                                }
//                                break;
//                            case 2:
//                                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                    Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
//                                    if (duahau != null && duahau.quantity >= 10) {
//                                        Item thoivang = ItemService.gI().createNewItem((short) 457, 200);
//
//                                        // - Số item sự kiện có trong rương
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 10);
//
//                                        thoivang.itemOptions.add(new Item.ItemOption(73, 0));
//                                        InventoryServiceNew.gI().addItemBag(player, thoivang);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.getInstance().sendThongBao(player, "Đổi thành công");
//                                    } else {
//                                        Service.getInstance().sendThongBao(player, "Không đủ dưa");
//                                    }
//                                } else {
//                                    Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                                }
//                                break;
//                            case 3:
//                                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                    Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
//                                    if (duahau != null && duahau.quantity >= 15) {
//                                        Item thoivang = ItemService.gI().createNewItem((short) 457, 350);
//
//                                        // - Số item sự kiện có trong rương
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 15);
//
//                                        thoivang.itemOptions.add(new Item.ItemOption(73, 0));
//                                        InventoryServiceNew.gI().addItemBag(player, thoivang);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.getInstance().sendThongBao(player, "Đổi thành công");
//                                    } else {
//                                        Service.getInstance().sendThongBao(player, "Không đủ dưa");
//                                    }
//                                } else {
//                                    Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                                }
//                                break;
//                            case 4:
//                                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                    Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
//                                    if (duahau != null && duahau.quantity >= 20) {
//                                        Item thoivang = ItemService.gI().createNewItem((short) 457, 500);
//
//                                        // - Số item sự kiện có trong rương
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 20);
//
//                                        thoivang.itemOptions.add(new Item.ItemOption(73, 0));
//                                        InventoryServiceNew.gI().addItemBag(player, thoivang);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.getInstance().sendThongBao(player, "Đổi thành công");
//                                    } else {
//                                        Service.getInstance().sendThongBao(player, "Không đủ dưa");
//                                    }
//                                } else {
//                                    Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                                }
//                                break;
//                            case 5:
//                                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                    Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
//                                    if (duahau != null && duahau.quantity >= 25) {
//                                        Item thoivang = ItemService.gI().createNewItem((short) 457, 700);
//
//                                        // - Số item sự kiện có trong rương
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 25);
//
//                                        thoivang.itemOptions.add(new Item.ItemOption(73, 0));
//                                        InventoryServiceNew.gI().addItemBag(player, thoivang);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.getInstance().sendThongBao(player, "Đổi thành công");
//                                    } else {
//                                        Service.getInstance().sendThongBao(player, "Không đủ dưa");
//                                    }
//                                } else {
//                                    Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                                }
//                                break;
//                        }
//                        //    } else {
//                        //        Service.getInstance().sendThongBaoOK(player, "Sự kiện đã kết thúc");
//                        //    }
//                    }
//                }
//            }
//        };
//    }
//
//    public static Npc noibanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (this.mapId == 5) {
//                    if (NauBanh.gI().NauXong == true) {
//                        this.createOtherMenu(player, 0, "Bánh đã chín, Bạn có "
//                                + Util.tinhgio(NauBanh.gI().ThoiGianChoLayBanh) + " để lấy\n|2|Nếu offline số bánh có thể được lấy vào đợt sau!",
//                                "Lấy Bánh");
//                    } else if (NauBanh.gI().ChoXong == true) {
//                        this.createOtherMenu(player, 1, "|2|Nồi Bánh Toàn Server Đợt " + NauBanh.gI().Count
//                                + "\n|-1|Đang trong thời gian nấu, bạn có thể cho thêm bánh vào nấu ké"
//                                + "\nSố lượng bánh nấu sẽ không giới hạn"
//                                + "\nThời gian nấu bánh còn: " + Util.tinhgio(NauBanh.gI().ThoiGianNau)
//                                + "\nHiện tại có: " + (NauBanh.gI().ListPlNauBanh.size()) + " bánh đang nấu"
//                                + "\nTrong đó bạn có: " + (NauBanh.gI().plBanhChung + NauBanh.gI().plBanhTet) + " bánh mới\n("
//                                + (player.BanhChung + player.BanhTet) + " bánh trước đó chưa lấy)", "Nấu bánh chưng", "Nấu bánh tét", "Hướng dẫn");
//                    } else if (NauBanh.gI().ChoXong == false) {
//                        this.createOtherMenu(player, 4, "|2|Nồi Bánh Toàn Máy chủ " + NauBanh.gI().Count
//                                + "\n|-1|Thời gian chờ nấu còn: " + Util.tinhgio(NauBanh.gI().ThoiGianCho)
//                                + "\nMực nước trong nồi: " + Util.format(NauBanh.gI().Nuoc) + " % "
//                                + (NauBanh.gI().Nuoc >= 50 && NauBanh.gI().Nuoc < 100 ? "(Trung bình)" : NauBanh.gI().Nuoc >= 100 ? "(Đã đầy)" : "(Thấp)")
//                                + "\nSố củi đã thêm: " + NauBanh.gI().Cui
//                                + "\nĐủ củi vả nước sẽ bắt đầu nấu"
//                                + "\nThêm đủ nước để nồi không bị cháy và nhận đủ số bánh nấu"
//                                + "\nThêm củi lửa để tăng tốc thời gian nấu bánh",
//                                "Thêm nước để nấu", "Thêm củi lữa", "Hướng dẫn");
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 5) {
//                        switch (player.iDMark.getIndexMenu()) {
//                            case 1:
//                                switch (select) {
//                                    case 0:
//                                        this.createOtherMenu(player, 2, "Bánh chưng: 12 Lá giong, 2 Gạo nếp, 1 Đậu xanh, 12 Gióng tre, 3 Thịt lợn, 1 Muối và 03 Nước nấu.", "Nấu", "Đóng");
//                                        break;
//                                    case 1:
//                                        this.createOtherMenu(player, 3, "Bánh tết: 12 Lá chuối, 2 Gạo nếp, 1 Đậu xanh, 12 Giống tre, 3 Thịt lợn, 1 Muối và 03 Nước nấu.", "Nấu", "Đóng");
//                                        break;
//                                }
//                                break;
//                            case 2:
//                                Input.gI().createFormNauBanhChung(player);
//                                break;
//                            case 3:
//                                Input.gI().createFormNauBanhTet(player);
//                                break;
//                        }
//                        if (player.iDMark.getIndexMenu() == 0) {
//                            if (player.BanhChung == 0 && player.BanhTet == 0) {
//                                Service.gI().sendThongBao(player, "Có nấu gì đéo đâu mà đòi nhận");
//                                return;
//                            }
//                            if (player.BanhTet != 0) {
//                                Item BanhChung = ItemService.gI().createNewItem((short) 1446, player.BanhTet);
//                                InventoryServiceNew.gI().addItemBag(player, BanhChung);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.gI().sendThongBao(player, "Bạn đã nhận được bánh tét");
//                                player.BanhTet = 0;
//                            }
//                            if (player.BanhChung != 0) {
//                                Item BanhTet = ItemService.gI().createNewItem((short) 1445, player.BanhChung);
//                                InventoryServiceNew.gI().addItemBag(player, BanhTet);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.gI().sendThongBao(player, "Bạn đã nhận được bánh chưng");
//                                player.BanhChung = 0;
//                            }
//                        } else if (player.iDMark.getIndexMenu() == 4) {
//                            switch (select) {
//                                case 0:
//                                    Item nuocNau = InventoryServiceNew.gI().findItemBag(player, 1443);
//                                    if (nuocNau == null) {
//                                        Service.gI().sendThongBao(player, "Bạn không có nước để nấu");
//                                        return;
//                                    }
//                                    if (NauBanh.gI().Nuoc < 100) {
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, nuocNau, 1);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        NauBanh.gI().Nuoc++;
//                                    } else {
//                                        Service.gI().sendThongBao(player, "Bạn đã thêm đủ nước rồi");
//                                    }
//                                    break;
//                                case 1:
//                                    Item cuiLua = InventoryServiceNew.gI().findItemBag(player, 1444);
//                                    if (cuiLua == null) {
//                                        Service.gI().sendThongBao(player, "bạn không có củi lửa để nấu");
//                                        return;
//                                    }
//                                    if (NauBanh.gI().Cui < 100) {
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, cuiLua, 1);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        NauBanh.gI().Cui++;
//                                        NauBanh.gI().ThoiGianNau -= (1000);
//                                    }
//                                    break;
//                                case 2:
//                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_NAU_BANH);
//                                    break;
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }

    public static Npc luffy(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            String[] menuselect = new String[]{};

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 190) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "|2| Tôi là Monkey D. Luffy , Người Sẽ Trở Thành Vua Hải Tặc ...",
                                "Liên Đấu\nOne Piece", "Võ Đài\nOne Piece");
                    } else if (this.mapId == 112) {
                        int goldchallenge = pl.goldChallenge;
                        if (pl.levelWoodChest == 0) {
                            menuselect = new String[]{"Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Quay Về"};
                        } else {
                            menuselect = new String[]{"Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Nhận thưởng\nRương cấp\n" + pl.levelWoodChest, "Về Đảo Kame"};
                        }
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Võ Đài One Piece \nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào", menuselect, "Từ chối");

                    } else {
                        super.openBaseMenu(pl);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.map.mapId == 190) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 112, 203, 408);
                                    break;
                                 case 1:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 196, 203, 408);
                                    break;   
                            }
                        }
                    } else if (this.mapId == 112) {
                        int goldchallenge = player.goldChallenge;
                        if (player.levelWoodChest == 0) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressServices.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 190, -1, 450, 288);
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressServices.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    if (!player.receivedWoodChest) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item it = ItemService.gI().createNewItem((short) 571);
                                            it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
                                            it.itemOptions.add(new Item.ItemOption(30, 0));
                                            it.createTime = System.currentTimeMillis();
                                            InventoryServiceNew.gI().addItemBag(player, it);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            player.receivedWoodChest = true;
                                            player.levelWoodChest = 0;
                                            Service.getInstance().sendThongBao(player, "Bạn nhận được rương ngọc rồng");
                                        } else {
                                            this.npcChat(player, "Hành trang đã đầy");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                    }
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMap(player, 190, -1, 1030, 408);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc vihop(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "Đổi Đệ Vip");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.BUY_DE_TU_VIP,
                                            "Xin chào, ta có một số bé pet vip nè!\n|1|Số tiền của bạn còn : " + Util.format(player.getSession().vnd)
                                            + "\n|7|Đệ Thiên Tử: 30k coin, Hợp thể tăng 30% chỉ số"
                                            + "\n|7|Đệ Black Goku: 40k coin, Hợp thể tăng 40% chỉ số"
                                            + "\n|7|Đệ Kaido: 50k coin, Hợp thể tăng 50% chỉ số"
                                            + "\n|7| Lưu Ý Phải Có Đệ Thường Mới Mua Được Đệ Vip",
                                            "Thiên Tử", "Black Goku", "Kaido");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.BUY_DE_TU_VIP) {
                            PreparedStatement ps = null;
                            try (Connection con = GirlkunDB.getConnection();) {
                                switch (select) {
                                    case 0:
                                        if (player.getSession().vnd < 30000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 30k coin");
                                            return;
                                        }
                                        if (player.pet == null) {
                                            Service.gI().sendThongBao(player, "Bạn cần phải có đệ tử thường trước");
                                            return;
                                        }
                                        player.getSession().vnd -= 30000;
                                        PetService.gI().createThienTuPetVip(player, true, player.pet.gender);
                                        break;
                                    case 1:
                                        if (player.getSession().vnd < 40000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 30k coin");
                                            return;
                                        }
                                        if (player.pet == null) {
                                            Service.gI().sendThongBao(player, "Bạn cần phải có đệ tử thường trước");
                                            return;
                                        }

                                        player.getSession().vnd -= 40000;
                                        PetService.gI().createBlackGokuPetVip(player, true, player.pet.gender);
                                        break;
                                    case 2:
                                        if (player.getSession().vnd < 50000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 50k coin");
                                            return;
                                        }
                                        if (player.pet == null) {
                                            Service.gI().sendThongBao(player, "Bạn cần phải có đệ tử thường trước");
                                            return;
                                        }

                                        player.getSession().vnd -= 50000;
                                        PetService.gI().createKaidoPetVip(player, player.pet != null, player.pet.gender);
                                        break;
                                }
                                ps = con.prepareStatement("update account set vnd = ? where id = ?");
                                ps.setInt(1, player.getSession().vnd);
                                ps.setInt(2, player.getSession().userId);
                                ps.executeUpdate();
                                ps.close();

                            } catch (Exception e) {
                                Logger.logException(NpcFactory.class, e, "Lỗi update vnd " + player.name);
                            } finally {
                                try {
                                    if (ps != null) {
                                        ps.close();
                                    }
                                } catch (SQLException ex) {
                                    System.out.println("Lỗi khi update vnd");

                                }
                            }
                        }

                    }
                }
            }
        };
    }

    public static Npc Tapion(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 19) {
                        try {
                            MapSatan.gI().setTimeJoinMapSatan();
                            if (this.mapId == 19) {
                                long now = System.currentTimeMillis();
                                if (now > MapSatan.TIME_OPEN_SATAN && now < MapSatan.TIME_CLOSE_SATAN) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_SANTA, "Đại chiến Hirudegan đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_SANTA,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Đổi huy hiệu", "Từ chối");
                                }

                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu tapion");
                        }

                    } else if (this.mapId == 192) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 19) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_SANTA:
                                break;
                            case ConstNpc.MENU_OPEN_SANTA:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_SATAN);
                                } else if (select == 1) {
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player,
                                                "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else
                                        ChangeMapService.gI().changeMap(player, 192, Util.nextInt(0, 8), 163, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_SATAN);
                                }
                                if (select == 1) {
                                    if (player.nPoint.power >= 2000000000) {

//                                        ShopServiceNew.gI().opendShop(player, "TAPION", false);
                                    } else {

                                        Service.gI().sendThongBaoOK(player,
                                                "Yêu cầu 2 tỉ sức mạnh");

                                    }
                                }
                                break;
                        }
                    } else if (this.mapId == 192) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }

            };

        };

    }

    private static Npc quaythu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 190) {
                        this.createOtherMenu(player, 1234, "|7|- •⊹٭Ngọc Rồng Akaya٭⊹• -\nVòng Quay Trứng Thú\n" + "|3|QUAY THƯỜNG : 5-10% CHỈ SỐ\nQUAY CAO CẤP : 10-20% CHỈ SỐ\nQUAY VIP : 15-25% CHỈ SỐ" + "\nQUAY X1 : QUAY THỦ CÔNG\nQUAY X10 : AUTO X10 LẦN QUAY\nQUAY X100 : AUTO X100 LẦN QUAY\n" + "|7|LƯU Ý : MỌI CHỈ SỐ ĐỀU RANDOM KHÔNG CÓ OPTION NHẤT ĐỊNH\nNẾU MUỐN NGƯNG AUTO QUAY CHỈ CẦN THOÁT GAME VÀ VÀO LẠI!",
                                "Quay Thường", "Quay Cao Cấp", "Quay VIP", "Mua xu", "Xem Top", "Rương Đồ");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 190) {
                        if (player.iDMark.getIndexMenu() == 1234) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 12345, "|6|Quay Thú Thường" + "\n|7|Tiến Hành Quay",
                                            "Quay x1", "Quay x10", "Quay x100", "Rương Đồ");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, 12346, "|6|Quay Thú Cao Cấp" + "\n|7|Tiến Hành Quay",
                                            "Quay x1", "Quay x10", "Quay x100", "Rương Đồ");
                                    break;
                                case 2:
                                    this.createOtherMenu(player, 12347, "|6|Quay Thú VIP" + "\n|7|Tiến Hành Quay",
                                            "Quay x1", "Quay x10", "Quay x100", "Rương Đồ");
                                    break;
                                case 3:
                                    this.createOtherMenu(player, 12348, "Ở đây ta có bán xu gấp thú",
                                            "Xu thường", "Xu cao cấp", "Xu vip", "Rương Đồ");
                                    break;
                                case 4:
                                    Service.gI().showListTop(player, Manager.TopGapThu);
                                    break;
                                case 5:
                                    this.createOtherMenu(player, ConstNpc.RUONG_PHU,
                                            "|1|Tình yêu như một dây đàn\n"
                                            + "Tình vừa được thì đàn đứt dây\n"
                                            + "Đứt dây này anh thay dây khác\n"
                                            + "Mất em rồi anh biết thay ai?",
                                            "Rương Phụ\n(" + (player.inventory.itemsBoxCrackBall.size()
                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                            "Xóa Hết\nRương Phụ", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 12345) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394) == null) {
                                        this.createOtherMenu(player, 12345, "|7|HẾT XU!",
                                                "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                        break;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x1 lần");
                                        int timex1 = 1;
                                        int count = 0;
                                        while (timex1 > 0) {
                                            timex1--;
                                            count++;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394), 1);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Thread.sleep(100);
                                            if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394) == null) {
                                                this.createOtherMenu(player, 12345, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ QUAY : " + count,
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 200) {
                                                this.createOtherMenu(player, 12345, "|7|DỪNG AUTO QUAY, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT QUAY : " + count + " LƯỢT" + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            player.point_gapthu += 1;
                                            short[] bkt = {2019, 2020, 2021};
                                            Item gapx1 = Util.petrandom(bkt[Util.nextInt(bkt.length)]);
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    InventoryServiceNew.gI().addItemBag(player, gapx1);
                                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH QUAY AUTO X1\nSỐ LƯỢT CÒN : " + timex1 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx1.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH QUAY AUTO X1\nSỐ LƯỢT CÒN : " + timex1 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.inventory.itemsBoxCrackBall.add(gapx1);
                                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex1 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx1.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex1 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 1:
                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394) == null) {
                                        this.createOtherMenu(player, 12345, "|7|HẾT XU!",
                                                "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                        break;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x10 lần");
                                        int timex10 = 10;
                                        int count = 0;
                                        while (timex10 > 0) {
                                            timex10--;
                                            count++;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394), 1);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Thread.sleep(100);
                                            if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394) == null) {
                                                this.createOtherMenu(player, 12345, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ QUAY : " + count,
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 200) {
                                                this.createOtherMenu(player, 12345, "|7|DỪNG AUTO QUAY, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT QUAY : " + count + " LƯỢT" + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            player.point_gapthu += 1;
                                            short[] bkt = {2019, 2020, 2021};
                                            Item gapx10 = Util.petrandom(bkt[Util.nextInt(bkt.length)]);
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    InventoryServiceNew.gI().addItemBag(player, gapx10);
                                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH QUAY AUTO X10\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx10.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH QUAY AUTO X10\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.inventory.itemsBoxCrackBall.add(gapx10);
                                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx10.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 2:
                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394) == null) {
                                        this.createOtherMenu(player, 12345, "|7|HẾT XU!",
                                                "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                        break;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x100 lần");
                                        int timex100 = 100;
                                        int count = 0;
                                        while (timex100 > 0) {
                                            timex100--;
                                            count++;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394), 1);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Thread.sleep(100);
                                            if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394) == null) {
                                                this.createOtherMenu(player, 12345, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ QUAY : " + count,
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 200) {
                                                this.createOtherMenu(player, 12345, "|7|DỪNG AUTO QUAY, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT QUAY : " + count + " LƯỢT" + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            player.point_gapthu += 1;
                                            short[] bkt = {2019, 2020, 2021};
                                            Item gapx100 = Util.petrandom(bkt[Util.nextInt(bkt.length)]);
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    InventoryServiceNew.gI().addItemBag(player, gapx100);
                                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH QUAY AUTO X1\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx100.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH QUAY AUTO X1\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.inventory.itemsBoxCrackBall.add(gapx100);
                                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx100.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1394).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 3:
                                    this.createOtherMenu(player, ConstNpc.RUONG_PHU,
                                            "|1|Tình yêu như một dây đàn\n"
                                            + "Tình vừa được thì đàn đứt dây\n"
                                            + "Đứt dây này anh thay dây khác\n"
                                            + "Mất em rồi anh biết thay ai?",
                                            "Rương Phụ\n(" + (player.inventory.itemsBoxCrackBall.size()
                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                            "Xóa Hết\nRương Phụ", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 12346) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395) == null) {
                                        this.createOtherMenu(player, 12346, "|7|HẾT XU!",
                                                "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                        break;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x1 lần");
                                        int timex1 = 1;
                                        int count = 0;
                                        while (timex1 > 0) {
                                            timex1--;
                                            count++;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395), 2);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Thread.sleep(100);
                                            if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395) == null) {
                                                this.createOtherMenu(player, 12346, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ QUAY : " + count,
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 200) {
                                                this.createOtherMenu(player, 12346, "|7|DỪNG AUTO QUAY, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT QUAY : " + count + " LƯỢT" + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            player.point_gapthu += 1;
                                            short[] bkt = {2022, 2023, 2024};
                                            Item gapx1 = Util.petccrandom(bkt[Util.nextInt(bkt.length)]);
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    InventoryServiceNew.gI().addItemBag(player, gapx1);
                                                    this.createOtherMenu(player, 12346, "|7|ĐANG TIẾN HÀNH QUAY AUTO X1\nSỐ LƯỢT CÒN : " + timex1 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx1.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12346, "|7|ĐANG TIẾN HÀNH QUAY AUTO X1\nSỐ LƯỢT CÒN : " + timex1 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.inventory.itemsBoxCrackBall.add(gapx1);
                                                    this.createOtherMenu(player, 12346, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex1 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx1.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12346, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex1 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 1:
                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395) == null) {
                                        this.createOtherMenu(player, 12346, "|7|HẾT XU!",
                                                "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                        break;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x10 lần");
                                        int timex10 = 10;
                                        int count = 0;
                                        while (timex10 > 0) {
                                            timex10--;
                                            count++;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395), 2);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Thread.sleep(100);
                                            if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395) == null) {
                                                this.createOtherMenu(player, 12346, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ QUAY : " + count,
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 200) {
                                                this.createOtherMenu(player, 12346, "|7|DỪNG AUTO QUAY, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT QUAY : " + count + " LƯỢT" + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            player.point_gapthu += 1;
                                            short[] bkt = {2022, 2023, 2024};
                                            Item gapx10 = Util.petccrandom(bkt[Util.nextInt(bkt.length)]);
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    InventoryServiceNew.gI().addItemBag(player, gapx10);
                                                    this.createOtherMenu(player, 12346, "|7|ĐANG TIẾN HÀNH QUAY AUTO X10\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx10.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12346, "|7|ĐANG TIẾN HÀNH QUAY AUTO X10\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.inventory.itemsBoxCrackBall.add(gapx10);
                                                    this.createOtherMenu(player, 12346, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx10.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12346, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 2:
                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395) == null) {
                                        this.createOtherMenu(player, 12346, "|7|HẾT XU!",
                                                "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                        break;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x100 lần");
                                        int timex100 = 100;
                                        int count = 0;
                                        while (timex100 > 0) {
                                            timex100--;
                                            count++;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395), 2);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Thread.sleep(100);
                                            if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395) == null) {
                                                this.createOtherMenu(player, 12346, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ QUAY : " + count,
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 200) {
                                                this.createOtherMenu(player, 12346, "|7|DỪNG AUTO QUAY, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT QUAY : " + count + " LƯỢT" + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            player.point_gapthu += 1;
                                            short[] bkt = {2022, 2023, 2024};
                                            Item gapx100 = Util.petccrandom(bkt[Util.nextInt(bkt.length)]);
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    InventoryServiceNew.gI().addItemBag(player, gapx100);
                                                    this.createOtherMenu(player, 12346, "|7|ĐANG TIẾN HÀNH QUAY AUTO X1\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx100.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12346, "|7|ĐANG TIẾN HÀNH QUAY AUTO X1\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.inventory.itemsBoxCrackBall.add(gapx100);
                                                    this.createOtherMenu(player, 12346, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx100.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12346, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1395).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 3:
                                    this.createOtherMenu(player, ConstNpc.RUONG_PHU,
                                            "|1|Tình yêu như một dây đàn\n"
                                            + "Tình vừa được thì đàn đứt dây\n"
                                            + "Đứt dây này anh thay dây khác\n"
                                            + "Mất em rồi anh biết thay ai?",
                                            "Rương Phụ\n(" + (player.inventory.itemsBoxCrackBall.size()
                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                            "Xóa Hết\nRương Phụ", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 12347) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396) == null) {
                                        this.createOtherMenu(player, 12347, "|7|HẾT XU!",
                                                "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                        break;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x1 lần");
                                        int timex1 = 1;
                                        int count = 0;
                                        while (timex1 > 0) {
                                            timex1--;
                                            count++;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396), 3);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Thread.sleep(100);
                                            if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396) == null) {
                                                this.createOtherMenu(player, 12347, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ QUAY : " + count,
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 200) {
                                                this.createOtherMenu(player, 12347, "|7|DỪNG AUTO QUAY, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT QUAY : " + count + " LƯỢT" + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            player.point_gapthu += 1;
                                            short[] bkt = {1397, 1398, 1399, 1400, 1401, 1402, 1377};
                                            Item gapx1 = Util.petviprandom(bkt[Util.nextInt(bkt.length)]);
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    InventoryServiceNew.gI().addItemBag(player, gapx1);
                                                    this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH QUAY AUTO X1\nSỐ LƯỢT CÒN : " + timex1 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx1.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH QUAY AUTO X1\nSỐ LƯỢT CÒN : " + timex1 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.inventory.itemsBoxCrackBall.add(gapx1);
                                                    this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex1 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx1.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex1 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 1:
                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396) == null) {
                                        this.createOtherMenu(player, 12347, "|7|HẾT XU!",
                                                "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                        break;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x10 lần");
                                        int timex10 = 10;
                                        int count = 0;
                                        while (timex10 > 0) {
                                            timex10--;
                                            count++;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396), 3);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Thread.sleep(100);
                                            if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396) == null) {
                                                this.createOtherMenu(player, 12347, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ QUAY : " + count,
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 200) {
                                                this.createOtherMenu(player, 12347, "|7|DỪNG AUTO QUAY, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT QUAY : " + count + " LƯỢT" + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            player.point_gapthu += 1;
                                            short[] bkt = {1397, 1398, 1399, 1400, 1401, 1402, 1377};
                                            Item gapx10 = Util.petviprandom(bkt[Util.nextInt(bkt.length)]);
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    InventoryServiceNew.gI().addItemBag(player, gapx10);
                                                    this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH QUAY AUTO X10\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx10.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH QUAY AUTO X10\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.inventory.itemsBoxCrackBall.add(gapx10);
                                                    this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx10.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 2:
                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396) == null) {
                                        this.createOtherMenu(player, 12347, "|7|HẾT XU!",
                                                "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                        break;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x100 lần");
                                        int timex100 = 100;
                                        int count = 0;
                                        while (timex100 > 0) {
                                            timex100--;
                                            count++;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396), 3);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Thread.sleep(100);
                                            if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396) == null) {
                                                this.createOtherMenu(player, 12347, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ QUAY : " + count,
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 200) {
                                                this.createOtherMenu(player, 12347, "|7|DỪNG AUTO QUAY, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT QUAY : " + count + " LƯỢT" + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                break;
                                            }
                                            player.point_gapthu += 1;
                                            short[] bkt = {1397, 1398, 1399, 1400, 1401, 1402, 1377};
                                            Item gapx100 = Util.petviprandom(bkt[Util.nextInt(bkt.length)]);
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    InventoryServiceNew.gI().addItemBag(player, gapx100);
                                                    this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH QUAY AUTO X1\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx100.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH QUAY AUTO X1\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.inventory.itemsBoxCrackBall.add(gapx100);
                                                    this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx100.template.name + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                } else {
                                                    this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH QUAY AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Quay hụt rồi!" + "\nSố xu còn : " + InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1396).quantity + "\n|7|TỔNG ĐIỂM : " + player.point_gapthu,
                                                            "Quay X1", "Quay X10", "Quay X100", "Rương Đồ");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 3:
                                    this.createOtherMenu(player, ConstNpc.RUONG_PHU,
                                            "|1|Tình yêu như một dây đàn\n"
                                            + "Tình vừa được thì đàn đứt dây\n"
                                            + "Đứt dây này anh thay dây khác\n"
                                            + "Mất em rồi anh biết thay ai?",
                                            "Rương Phụ\n(" + (player.inventory.itemsBoxCrackBall.size()
                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                            "Xóa Hết\nRương Phụ", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 12348) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 2221, "|7|Ở dây ta có bán XU thường\n"
                                            + "|5|Ta hỗ trợ mua với số lượng lớn quy định như sau:\n"
                                            + "|5|10 Xu thường = 15 Thỏi vàng\n"
                                            + "|5|105 Xu thường = 150 Thỏi vàng\n"
                                            + "|5|1100 Xu thường = 1600 Thỏi vàng\n"
                                            + "|3|Ngươi muốn mua số lượng bao nhiêu?", ""
                                            + "Mua 15 Xu", "Mua 150 Xu", "Mua 1600 Xu");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, 2222, "|7|Ở dây ta có bán XU cao cấp\n"
                                            + "|1|Giá Nước hoa tại Cửa hàng Santa: 500Tr vàng\n"
                                            + "|5|Ta hỗ trợ mua với số lượng lớn quy định như sau:\n"
                                            + "|5|10 Xu cao cấp = 13 Thỏi vàng\n"
                                            + "|5|105 Xu cao cấp = 135 Thỏi vàng\n"
                                            + "|5|1100 Xu cao cấp = 1400 Thỏi vàng\n"
                                            + "|3|Ngươi muốn mua số lượng bao nhiêu?", ""
                                            + "Mua 13 Xu", "Mua 135 Xu", "Mua 1400 Xu");
                                    break;
                                case 2:
                                    this.createOtherMenu(player, 2223, "|7|Ở dây ta có bán XU Vip\n"
                                            + "|1|Giá Nước hoa tại Cửa hàng Santa: 500Tr vàng\n"
                                            + "|5|Ta hỗ trợ mua với số lượng lớn quy định như sau:\n"
                                            + "|5|10 Xu vip = 10 Thỏi vàng\n"
                                            + "|5|105 Xu vip = 100 Thỏi vàng\n"
                                            + "|5|1100 Xu vip = 1000 Thỏi vàng\n"
                                            + "|3|Ngươi muốn mua số lượng bao nhiêu?", ""
                                            + "Mua 10 Xu", "Mua 105 Xu", "Mua 1110 Xu");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 2221) {
                            switch (select) {
                                case 0:
                                    Item tv = null;
                                    try {
                                        tv = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
                                    }
                                    if (tv == null || tv.quantity < 10) {
                                        this.npcChat(player, "Con còn thiếu x10 thỏi vàng");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 10);
                                        Item capsule = ItemService.gI().createNewItem((short) 1394, 15);
                                        InventoryServiceNew.gI().addItemBag(player, capsule);
                                        player.point_gapthu += 10;
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Con nhận được Nước hoa");
                                    }
                                    break;
                                case 1:
                                    Item tv1 = null;
                                    try {
                                        tv1 = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
                                    }
                                    if (tv1 == null || tv1.quantity < 100) {
                                        this.npcChat(player, "Con còn thiếu x100 thỏi vàng");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv1, 100);
                                        Item capsule = ItemService.gI().createNewItem((short) 1394, 155);
                                        InventoryServiceNew.gI().addItemBag(player, capsule);
                                        player.point_gapthu += 100;
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Con nhận được Nước hoa");
                                    }
                                    break;
                                case 2:
                                    Item tv2 = null;
                                    try {
                                        tv2 = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
                                    }
                                    if (tv2 == null || tv2.quantity < 1000) {
                                        this.npcChat(player, "Con còn thiếu x1000 thỏi vàng");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv2, 1000);
                                        Item capsule = ItemService.gI().createNewItem((short) 1394, 1600);
                                        InventoryServiceNew.gI().addItemBag(player, capsule);
                                        player.point_gapthu += 1000;
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Con nhận được Nước hoa");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 2222) {
                            switch (select) {
                                case 0:
                                    Item tv = null;
                                    try {
                                        tv = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
                                    }
                                    if (tv == null || tv.quantity < 10) {
                                        this.npcChat(player, "Con còn thiếu x10 thỏi vàng");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 10);
                                        Item capsule = ItemService.gI().createNewItem((short) 1395, 13);
                                        InventoryServiceNew.gI().addItemBag(player, capsule);
                                        player.point_gapthu += 10;
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Con nhận được Nước hoa");
                                    }
                                    break;
                                case 1:
                                    Item tv1 = null;
                                    try {
                                        tv1 = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
                                    }
                                    if (tv1 == null || tv1.quantity < 100) {
                                        this.npcChat(player, "Con còn thiếu x100 thỏi vàng");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv1, 100);
                                        Item capsule = ItemService.gI().createNewItem((short) 1395, 135);
                                        InventoryServiceNew.gI().addItemBag(player, capsule);
                                        player.point_gapthu += 100;
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Con nhận được Nước hoa");
                                    }
                                    break;
                                case 2:
                                    Item tv2 = null;
                                    try {
                                        tv2 = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
                                    }
                                    if (tv2 == null || tv2.quantity < 1000) {
                                        this.npcChat(player, "Con còn thiếu x1000 thỏi vàng");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv2, 1000);
                                        Item capsule = ItemService.gI().createNewItem((short) 1395, 1400);
                                        InventoryServiceNew.gI().addItemBag(player, capsule);
                                        player.point_gapthu += 1000;
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Con nhận được Nước hoa");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 2223) {
                            switch (select) {
                                case 0:
                                    Item tv = null;
                                    try {
                                        tv = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
                                    }
                                    if (tv == null || tv.quantity < 10) {
                                        this.npcChat(player, "Con còn thiếu x10 thỏi vàng");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 10);
                                        Item capsule = ItemService.gI().createNewItem((short) 1396, 10);
                                        InventoryServiceNew.gI().addItemBag(player, capsule);
                                        player.point_gapthu += 10;
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Con nhận được Nước hoa");
                                    }
                                    break;
                                case 1:
                                    Item tv1 = null;
                                    try {
                                        tv1 = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
                                    }
                                    if (tv1 == null || tv1.quantity < 100) {
                                        this.npcChat(player, "Con còn thiếu x100 thỏi vàng");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv1, 100);
                                        Item capsule = ItemService.gI().createNewItem((short) 1396, 105);
                                        InventoryServiceNew.gI().addItemBag(player, capsule);
                                        player.point_gapthu += 100;
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Con nhận được Nước hoa");
                                    }
                                    break;
                                case 2:
                                    Item tv2 = null;
                                    try {
                                        tv2 = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
                                    }
                                    if (tv2 == null || tv2.quantity < 1000) {
                                        this.npcChat(player, "Con còn thiếu x1000 thỏi vàng");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv2, 1000);
                                        Item capsule = ItemService.gI().createNewItem((short) 1396, 1100);
                                        InventoryServiceNew.gI().addItemBag(player, capsule);
                                        player.point_gapthu += 1000;
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Con nhận được Nước hoa");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.RUONG_PHU) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "RUONG_PHU", true);
                                    break;
                                case 1:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "|3|Bạn chắc muốn xóa hết vật phẩm trong rương phụ?\n"
                                            + "|7|Sau khi xóa sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc Monaka(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay", "Đến \nPotaufeu");
                    } else if (this.mapId == 139) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu muốn trở về?", "Quay về", "Từ chối");
                    } else if (this.mapId == 224) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    } else if (this.mapId == 218) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu muốn trở về?", "Quay về", "Từ chối");
                        } else if (this.mapId == 190) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu muốn trở về?", "Quay về", "Từ chối");
                        } else if (this.mapId == 217) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                //đến potaufeu
                                ChangeMapService.gI().goToPotaufeu(player);
                            }
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về trạm vũ trụ
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24 + player.gender, -1, -1);
                                    break;
                            }
                        }
                    } else if (this.mapId == 224) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 21 + player.gender, -1, -1);
                                    break;

                            }
                        }
                    } else if (this.mapId == 218) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 21 + player.gender, -1, -1);
                                    break;

                            }
                        }
                        } else if (this.mapId == 190) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 21 + player.gender, -1, -1);
                                    break;

                            }
                        }
                        } else if (this.mapId == 217) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 21 + player.gender, -1, -1);
                                    break;

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc miNuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.MENU_JOIN_GIAI_CUU_MI_NUONG,
                            "Ta đang bị kẻ xấu lợi dụng kiểm soát bản thân\n"
                            + "Các chàng trai hãy cùng nhau nhanh chóng tập hợp lên đường giải cứu ta",
                            "Tham gia", "Hướng dẫn", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                int nPlSameClan = 0;
                for (Player pl : player.zone.getPlayers()) {
                    if (!pl.equals(player) && pl.clan != null
                            && pl.clan.equals(player.clan) && pl.location.x >= 1285
                            && pl.location.x <= 1645) {
                        nPlSameClan++;
                    }
                }
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_GIAI_CUU_MI_NUONG:
                            if (select == 0) {
                                if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu cầu gia nhập bang hội");
                                    break;
                                }
                                if (player.clan.giaiCuuMiNuong != null) {
                                    ChangeMapService.gI().changeMapInYard(player, 185, player.clan.giaiCuuMiNuong.id, 60);
                                    break;
                                } else if (nPlSameClan < 0) {
                                    Service.gI().sendThongBao(player, "Yêu cầu tham gia cùng 2 đồng đội");
                                    break;
                                } else if (player.clanMember.getNumDateFromJoinTimeToToday() < 0) {
                                    Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 1 ngày");
                                    break;
                                } else if (player.clan.haveGoneGiaiCuuMiNuong) {
                                    Service.gI().sendThongBaoOK(player, "Bang hội của ngươi đã tham gia vào lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenGiaiCuuMiNuong, "HH:mm:ss") + "\nVui lòng tham gia vào ngày mai");
                                    break;
                                } else {
                                    GiaiCuuMiNuongService.gI().openGiaiCuuMiNuong(player);
                                }
                            } else if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_GIAI_CUU_MI_NUONG);
                            }
                            break;
                    }
                }
            }
        };
    }

    private static Npc TrongTai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 113) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đại hội võ thuật Siêu Hạng\n\ndiễn ra 24/7 kể cả ngày lễ và chủ nhật\nHãy thi đấu để khẳng định đẳng cấp của mình nhé", "Top 100\nCao thủ\n", "Hướng\ndẫn\nthêm", "Đấu ngay\n", "Về\nĐại Hội\nVõ Thuật");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 113) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    try (Connection con = GirlkunDB.getConnection()) {
                                    Manager.topSieuHang = Manager.realTopSieuHang(con);
                                } catch (Exception ignored) {
                                    Logger.error("Lỗi đọc top");
                                }
                                Service.gI().showListTop(player, Manager.topSieuHang, (byte) 1);
                                break;
                                case 2:
                                    List<TOP> tops = new ArrayList<>();
                                    tops.addAll(Manager.realTopSieuHang(player));
                                    Service.gI().showListTop(player, tops, (byte) 1);
                                    tops.clear();
                                    break;
                                case 3:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, -1, 432);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc TrongTai1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                try {
                    MapVodai.gI().setTimeJoinMapVodai();
                    if (this.mapId == 13) {
                        long now = System.currentTimeMillis();
                        if (now > MapVodai.TIME_OPEN_VODAI && now < MapVodai.TIME_CLOSE_VODAI) {
                            int numRegistered = MapVodai.gI().getNumRegistered();
                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_VODAI,
                                    "Đại chiến ngọc rồng namec đã mở, "
                                    + "ngươi có muốn tham gia không?\n\n"
                                    + "|7|Hiện tại đã có " + numRegistered + " người đăng ký tham gia.",
                                    "Hướng dẫn\nthêm", "Đăng ký", "Tham gia", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_VODAI,
                                    "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                        }
                    } else if (this.mapId == 166) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ta có thể giúp gì cho ngươi?", "Nhận thưởng", "Quay về");
                    }
                } catch (Exception ex) {
                    Logger.error("Lỗi mở menu trọng tài");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 13) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_VODAI:
                                break;
                            case ConstNpc.MENU_OPEN_VODAI:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_VODAI);
                                }
                                if (select == 1) {
                                    MapVodai.gI().registerPlayer(player);
                                } else if (select == 2) {
                                    MapVodai.gI().joinBattle(player);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_VODAI);
                                }
                                break;
                        }
                    } else if (this.mapId == 166) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            } else {
                                this.npcChat(player, "Hãy chiến đấu vì ");
                            }
                        }
                    }
                }

            }
        ;
    }

    ;
    }

    private static Npc trungLinhThu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đổi Trứng Linh thú cần:\b|7|X99 Hồn Linh Thú + 1 Tỷ vàng", "Đổi Trứng\nLinh thú", "Nâng Chiến Linh", "Mở chỉ số ẩn\nChiến Linh", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item honLinhThu = null;
                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 2029);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 Hồn Linh thú");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                        Service.gI().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 2028);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Trứng Linh thú");
                                    }
                                    break;
                                }

                                case 1:
//                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.Nang_Chien_Linh);
                                    break;
                                case 2:
                                    //                                   CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_Chien_Linh);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
//                            switch (player.combineNew.typeCombine) {
                            //                               case CombineServiceNew.Nang_Chien_Linh:
                            //                               case CombineServiceNew.MO_CHI_SO_Chien_Linh:
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                            //                                break;
                        }
                        //                     }
                    }
                }
            }
        };
    }

    private static Npc kyGui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, 0, "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cảm ơn bạn đã ghé thăm.", "Hướng\ndẫn\nthêm", "Mua bán\nKý gửi", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    switch (select) {
                        case 0:
                            Service.gI().sendPopUpMultiLine(pl, tempId, avartar, "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 5 hồng ngọc\bGiá trị ký gửi 10k-200Tr vàng hoặc 2-2k ngọc\bMột người bán, vạn người mua, mại dô, mại dô");
                            break;
                        case 1:
                            if (pl.nPoint.power < 17000000000L) {
                                Service.gI().sendThongBao(pl, "Vui lòng đạt 17 tỷ sức mạnh và đạt nhiệm vụ 22");
                            } else if (pl.playerTask.taskMain.id < 30) {
                                Service.gI().sendThongBao(pl, "Vui lòng đạt nhiệm vụ 30");
                            } else {
                                ShopKyGuiService.gI().openShopKyGui(pl);
                            }
                            break;

                    }
                }
            }
        };
    }

    private static Npc poTaGe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đa vũ trụ song song \b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá 200tr vàng không?", "Gọi Boss\nNhân bản", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Boss oldBossClone = BossManager.gI().getBossById(Util.createIdBossClone((int) player.id));
                                    if (oldBossClone != null) {
                                        this.npcChat(player, "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu " + oldBossClone.zone.zoneId);
                                    } else if (player.inventory.gold < 200_000_000) {
                                        this.npcChat(player, "Nhà ngươi không đủ 200 Triệu vàng ");
                                    } else {
                                        List<Skill> skillList = new ArrayList<>();
                                        for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                            Skill skill = player.playerSkill.skills.get(i);
                                            if (skill.point > 0) {
                                                skillList.add(skill);
                                            }
                                        }
                                        int[][] skillTemp = new int[skillList.size()][3];
                                        for (byte i = 0; i < skillList.size(); i++) {
                                            Skill skill = skillList.get(i);
                                            if (skill.point > 0) {
                                                skillTemp[i][0] = skill.template.id;
                                                skillTemp[i][1] = skill.point;
                                                skillTemp[i][2] = skill.coolDown;
                                            }
                                        }
                                        BossData bossDataClone = new BossData(
                                                "Nhân Bản" + player.name,
                                                player.gender,
                                                new short[]{player.getHead(), player.getBody(), player.getLeg(), player.getFlagBag(), player.idAura, player.getEffFront()},
                                                player.nPoint.dame,
                                                new long[]{player.nPoint.hpMax},
                                                new int[]{140},
                                                skillTemp,
                                                new String[]{"|-2|Boss nhân bản đã xuất hiện rồi"}, //text chat 1
                                                new String[]{"|-1|Ta sẽ chiếm lấy thân xác của ngươi hahaha!"}, //text chat 2
                                                new String[]{"|-1|Lần khác ta sẽ xử đẹp ngươi"}, //text chat 3
                                                60
                                        );

                                        try {
                                            new NhanBan(Util.createIdBossClone((int) player.id), bossDataClone, player.zone);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //trừ vàng khi gọi boss
                                        player.inventory.gold -= 200_000_000;
                                        Service.gI().sendMoney(player);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (TaskService.gI().getIdTask(player) == ConstTask.TASK_18_4) {
                            TaskService.gI().sendNextTaskMain(player);
                        } else if (TaskService.gI().getIdTask(player) == ConstTask.TASK_16_4) {
                            TaskService.gI().sendNextTaskMain(player);
                        }
                        if (player.getSession().is_gift_box) {
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?",
                                    "Nhiệm\nVụ","Nói chuyện");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:// nhiệm vụ
                                                    NpcService.gI().createTutorial(player, avartar, "Nhiệm vụ hiện tại của con: " + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                                                    break;
                            case 1:
                                createOtherMenu(player, ConstNpc.NOICHUYEN,
                                        "Ngươi tìm ta có việc gì?\n",
                                        "Chức năng\nbang hội", "Bản Đồ Kho Báu", "Đóng");
                                break;
//                             case 2: //shop skill
//                                    ShopServiceNew.gI().opendShop(player, "HOC_SKILL", false);
//                                    break;   
                            case 99:
                                createOtherMenu(player, ConstNpc.SU_KIEN,
                                        "Ngươi tìm ta có việc gì?\n",
                                        "Đổi quà\nsự kiện", "Đổi\nKhăn quàng", "Từ chối");
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.SU_KIEN) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, ConstNpc.MENU_NHAP_HOC,
                                        "Mừng khai giảng năm học mới, ta có một số phần quà dành cho con!",
                                        "Quà cấp tiểu học",
                                        "Quà cấp đại học",
                                        "Nhận danh hiệu\nBé ngoan");
                                break;
                            case 1:
                                Input.gI().createFormQDKQ(player);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NHAP_HOC) {
                        Item[] items = new Item[4];
                        items[0] = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1182);
                        items[1] = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1183);
                        items[2] = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1184);
                        items[3] = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1185);
                        int quantity1 = (items[0] != null) ? items[0].quantity : 0;
                        int quantity2 = (items[1] != null) ? items[1].quantity : 0;
                        int quantity3 = (items[2] != null) ? items[2].quantity : 0;
                        int quantity4 = (items[3] != null) ? items[3].quantity : 0;
                        int giavang;
                        int giaruby;
                        switch (select) {
                            case 0:
                                giavang = 20000000;
                                if (items[0] != null && items[1] != null
                                        && items[0].quantity >= 30 && items[1].quantity >= 30 && player.inventory.gold >= giavang) {
                                    player.inventory.gold -= giavang;

                                    Item caitrang = ItemService.gI().createNewItem((short) Util.nextInt(1483, 1485));
                                    caitrang.itemOptions.clear();
                                    caitrang.itemOptions.add(new Item.ItemOption(217, Util.nextInt(30, 35)));
                                    if (Util.isTrue(30, 100)) {
                                        caitrang.itemOptions.add(new Item.ItemOption(5, Util.nextInt(1, 15)));
                                    } else if (Util.isTrue(30, 100)) {
                                        caitrang.itemOptions.add(new Item.ItemOption(94, Util.nextInt(1, 15)));
                                    } else if (Util.isTrue(30, 100)) {
                                        caitrang.itemOptions.add(new Item.ItemOption(108, Util.nextInt(1, 15)));
                                    } else if (Util.isTrue(30, 100)) {
                                        caitrang.itemOptions.add(new Item.ItemOption(14, Util.nextInt(1, 15)));
                                    } else {
                                        caitrang.itemOptions.add(new Item.ItemOption(97, Util.nextInt(1, 15)));
                                    }
                                    caitrang.itemOptions.add(new Item.ItemOption(73, 1));
                                    caitrang.itemOptions.add(new Item.ItemOption(30, 0));
                                    InventoryServiceNew.gI().addItemBag(player, caitrang);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, items[1], 30);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, items[0], 30);

                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + caitrang.template.name);

                                } else {
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "|1| Đổi Thần Tài\n|2| Khăn đỏ " + quantity1 + "/30\nKhăn quàng "
                                            + quantity2 + "/30\n Giá vàng: "
                                            + Util.formatNumber(giavang),
                                            "OK\nTa biết rồi");
                                }
                                break;

                            case 1:
                                giavang = 50000000;
                                giaruby = 150;
                                if (items[0] != null && items[1] != null
                                        && items[0].quantity >= 50 && items[1].quantity >= 50 && player.inventory.gold >= giavang
                                        && player.inventory.ruby >= giaruby) {
                                    player.inventory.gold -= giavang;
                                    Item linhthu = ItemService.gI().createNewItem((short) Util.nextInt(1802, 1803));
                                    linhthu.itemOptions.clear();
                                    if (Util.isTrue(33, 100)) {
                                        linhthu.itemOptions.add(new Item.ItemOption(217, 10));
                                        linhthu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 10)));
                                    } else if (linhthu.template.id == Util.nextInt(1273, 1275)) {
                                        linhthu.itemOptions.add(new Item.ItemOption(73, 0));
                                    } else if (Util.isTrue(33, 100)) {
                                        linhthu.itemOptions.add(new Item.ItemOption(217, 10));
                                        linhthu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
                                    } else {
                                        linhthu.itemOptions.add(new Item.ItemOption(217, 10));
                                        linhthu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 10)));
                                    }
                                    linhthu.itemOptions.add(new Item.ItemOption(73, 1));
                                    linhthu.itemOptions.add(new Item.ItemOption(30, 0));
                                    InventoryServiceNew.gI().addItemBag(player, linhthu);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, items[1], 50);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, items[0], 50);

                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + linhthu.template.name);

                                } else {
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "|1| Chế tạo bồn tắm vàng\n|2| Khăn đỏ " + quantity1 + "/50\nKhăn quàng "
                                            + quantity2 + "/50\n Giá vàng: "
                                            + Util.formatNumber(giavang) + "\nGiá Hồng ngọc: " + giaruby,
                                            "OK\nTa biết rồi");
                                }
                                break;
                            case 2:

                                if (items[2] != null && items[3] != null
                                        && items[2].quantity >= 10 && items[3].quantity >= 10) {
                                    Item caitrang = ItemService.gI().createNewItem((short) 1696);
                                    caitrang.itemOptions.clear();
                                    caitrang.itemOptions.add(new Item.ItemOption(73, 1));
                                    caitrang.itemOptions.add(new Item.ItemOption(30, 0));
                                    InventoryServiceNew.gI().addItemBag(player, caitrang);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, items[2], 10);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, items[3], 10);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + caitrang.template.name);

                                } else {
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "|1| Danh hiệu bé ngoan:\n|2| Phiếu bé ngoan: " + quantity3 + "/10\nĐiểm 10: "
                                            + quantity4 + "/10",
                                            "OK\nTa biết rồi");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.NOICHUYEN) {
                        switch (select) {
                            case 0:
                                createOtherMenu(player, ConstNpc.BANGHOI,
                                        "Ngươi tìm ta có việc gì?\n",
                                        "Giải tán\nbang hội", "Khu Vực\nBang Hội");
                                        //, "Nâng cấp\nBang hội", "Quyên góp");
                                break;
                            case 1:
                                if (player.clan == null) {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Vào bang hội trước", "Đóng");
                                    break;
                                }
                                if (player.clan.banDoKhoBau != null) {
                                    createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                            "Bang hội của con đang tham gia Bản đồ kho báu cấp độ " + player.clan.banDoKhoBau.level + "\n"
                                            + "Thời gian còn lại là "
                                            + TimeUtil.getSecondLeft(player.clan.banDoKhoBau.getLastTimeOpen(), BanDoKhoBau.TIME_BAN_DO_KHO_BAU / 1000)
                                            + " giây. Con có muốn tham gia không?",
                                            "Tham gia", "Không");
                                    break;
                                }
                                Input.gI().createFormChooseLevelBDKB(player);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.BANGHOI) {
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                if (clan != null) {
                                    ClanMember cm = clan.getClanMember((int) player.id);
                                    if (cm != null) {
                                        if (clan.members.size() > 1) {
                                            Service.gI().sendThongBao(player, "Bang phải còn một người");
                                            break;
                                        }
                                        if (!clan.isLeader(player)) {
                                            Service.gI().sendThongBao(player, "Phải là bảng chủ");
                                            break;
                                        }
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                                "Đồng ý", "Từ chối!");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "bạn đã có bang hội đâu!!!");
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 39999999999L) {

                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 40 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                Clan clan2 = player.clan;
                                if (player.clan != null) {
                                    if (!clan2.isLeader(player)) {
                                        Service.gI().sendThongBao(player, "Yêu cầu phải là bang chủ!");
                                        break;
                                    }
                                    if (player.clan.level >= 0 && player.clan.level <= 9) {
                                        this.createOtherMenu(player, ConstNpc.CHUC_NANG_BANG_HOI2,
                                                "Bạn có muốn Nâng cấp lên " + (player.clan.level + 11) + " thành viên không?\n"
                                                + "Cần 1000 Capsule Bang\n"
                                                + "(Thu thập Capsule Bang bằng cách tiêu diệt quái tại Map Rừng Bamboo\n"
                                                + "cùng các thành viên khác)", "Nâng cấp\n(1 tỷ vàng)", "Từ chối");
                                    } else {
                                        Service.gI().sendThongBao(player, "Bang của bạn đã đạt cấp tối đa!");
                                        return;
                                    }
                                    break;
                                } else if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                    break;
                                }
                            case 3:
                                if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                    break;
                                }
                                Input.gI().DonateCsbang(player);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.CHUC_NANG_BANG_HOI2) {
                        Clan clan = player.clan;
                        switch (select) {
                            case 0:
                                if (player.clan.capsuleClan >= 1000 && clan.isLeader(player) && player.inventory.gold >= 1000000000L) {
                                    player.clan.level += 1;
                                    player.clan.maxMember += 1;
                                    player.clan.capsuleClan -= 1000;
                                    player.inventory.gold -= 1000000000L;
                                    player.clan.update();
                                    Service.gI().sendThongBao(player, "Yêu cầu nâng cấp bang hội thành công");
                                    break;
                                } //             
                                else if (!clan.isDeputy(player)) {
                                                                    Service.gI().sendThongBao(player, "Yêu cầu phải là bang chủ");
                                                                    return;
                                                                }
                                else if (player.inventory.gold < 1000000000L) {
                                    Service.gI().sendThongBaoOK(player, "Bạn còn thiều " + (1000000000L - player.inventory.gold) + " vàng");
                                    return;
                                } else if (player.clan.capsuleClan < 1000) {
                                    Service.gI().sendThongBaoOK(player, "Bang của bạn còn thiều " + (1000 - player.clan.capsuleClan) + " Capsule bang");
                                    return;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    Input.gI().createFormChooseLevelBDKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                        switch (select) {
                            case 0:
                                Object level = PLAYERID_OBJECT.get(player.id);
                                BanDoKhoBauService.gI().openBDKB(player, (int) level);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                        if (select == 0) {
                            BanDoKhoBauService.gI().joinBDKB(player);
                        }
                    }
                }
            }
        };
    }

    public static Npc truongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc vuaVegeta(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc ongGohan_ongMoori_ongParagus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    String checkvnd;
                    String mtv;
                    int thoivang;
                    if (player.tongtien >= 0 && player.tongtien < 100000) {
                        checkvnd = "Tân Thủ";
                        thoivang = 10;
                    } else if (player.tongtien > 100000 && player.tongtien < 500000) {
                        checkvnd = "VIP";
                        thoivang = 20;
                    } else {
                        checkvnd = "SVIP";
                        thoivang = 30;
                    }
                    if (player.getSession().actived) {
                        mtv = "Bạn đã là thành viên chính thức của Ngọc rồng Akaya. Đã mở khóa tính năng Giao dịch và Chat thế giới !!!";
                    } else {
                        mtv = "Tài khoản của bạn chưa được Mở thành viên nên còn bị khóa Chat Thế Giới với không giao dịch được!!!";
                    }
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (TaskService.gI().getIdTask(player) == ConstTask.TASK_4_3) {
                            TaskService.gI().sendNextTaskMain(player);
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Tốt lắm, con hoàn thành rất xuất xắc", "Đóng");
                        } else if (TaskService.gI().getIdTask(player) == ConstTask.TASK_10_1) {
                            TaskService.gI().sendNextTaskMain(player);
                        }
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|7| ～♪ CHÀO MỪNG BẠN ĐÃ ĐẾN VỚI NRO AKAYA ♪⁠～  \n"
                                        + "|2| Sever By Obito\n"
                                        + "|2| Zalo Admin : 0866468126\n"
                                        + "|4| Bạn Đang Có : " + Util.format(player.getSession().vnd) + " VND ...! "
                                .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                        : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru" : "Vua Vegeta"),
                                "Hỗ Trợ");
//                                , "Mở Thành Viên", "Điểm danh", "Nhận đệ tử", "GiftCode");//, "Mốc Nạp");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                createOtherMenu(player, ConstNpc.HOTRO,
                                        "|7|HỖ TRỢ PLAYER\n"
                                                + "|2| 1. Nhận Ngọc Xanh  -  Free : 50.000 Ngọc Xanh\n"
                                                + "|2| 2. Next Nhiệm Vụ Kết Bạn Và Nhiệm Vụ Vào Bang",
                                        "Nhận\nNgọc\nXanh", "Next\nNhiệm\nVụ");
                                break;
                            case 1:
                                if (!player.getSession().actived) {
                                    if (player.getSession().vnd >= 10000) {
                                        player.getSession().actived = true;
                                        if (PlayerDAO.subvnd(player, 10000)) {
                                            Service.gI().sendThongBao(player, "Bạn đã mở thành viên");
                                        }
                                    } else {
                                        this.npcChat(player, "Bạn còn thiếu " + (10000 - player.getSession().vnd) + " để mở thành viên");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Bạn đã mở thành viên rồi");
                                }
                                break;
                            case 2:
                                if (!player.getSession().actived) {
                                    Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                } else if (player.diemdanh == 0) {
                                    int thoivang1;
                                    if (player.tongtien >= 0 && player.tongtien < 100000) {
                                        thoivang1 = 10;
                                    } else if (player.tongtien > 100000 && player.tongtien < 500000) {
                                        thoivang1 = 20;
                                    } else {
                                        thoivang1 = 30;
                                    }
                                    Item thoivang = ItemService.gI().createNewItem((short) 457, thoivang1);
                                    InventoryServiceNew.gI().addItemBag(player, thoivang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemdanh = System.currentTimeMillis();
                                    Service.getInstance().sendThongBao(player, "|5|Bạn vừa nhận được " + thoivang1 + " Thỏi vàng");
                                } else {
                                    this.npcChat(player, "|5|Hôm nay đã nhận rồi mà !!!");
                                }
                                break;
                            case 3:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                    Service.gI().sendThongBaoOK(player, "Chức mừng bạn đã nhận được đệ tử!! hãy mau mau tu luyện cho đệ tử trở nên càng mạnh nào.");
                                } else {
                                    this.npcChat(player, "Bạn đã có rồi");
                                }
                                break;
                            case 4:
                                Input.gI().createFormGiftCode(player);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.NAPTICHLUY) {
                        switch (select) {
                            case 0:
                                try {
                                GirlkunResultSet checkmocnap = GirlkunDB.executeQuery(
                                        "select * from account where id = ? and username = ?", player.getSession().userId, player.getSession().uu);
                                if (checkmocnap.first()) {
                                    int mocnap = checkmocnap.getInt("mocnap");
                                    if (player.getSession().tongnap >= 50000 && mocnap < 50000) {
                                        GirlkunDB.executeUpdate(
                                                "update account set mocnap = ? where id = ? and username = ?", 50000, player.getSession().userId, player.getSession().uu);
                                        if (player.gender == 0) {
                                            Item ao = ItemService.gI().itemtlinhItem((short) 555);
                                            Item quan = ItemService.gI().itemtlinhItem((short) 556);
                                            Item gang = ItemService.gI().itemtlinhItem((short) 562);
                                            Item giay = ItemService.gI().itemtlinhItem((short) 563);
                                            Item rada = ItemService.gI().itemtlinhItem((short) 561);
                                            InventoryServiceNew.gI().addItemBag(player, ao);
                                            InventoryServiceNew.gI().addItemBag(player, quan);
                                            InventoryServiceNew.gI().addItemBag(player, gang);
                                            InventoryServiceNew.gI().addItemBag(player, giay);
                                            InventoryServiceNew.gI().addItemBag(player, rada);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                        } else if (player.gender == 1) {
                                            Item ao = ItemService.gI().itemtlinhItem((short) 557);
                                            Item quan = ItemService.gI().itemtlinhItem((short) 558);
                                            Item gang = ItemService.gI().itemtlinhItem((short) 564);
                                            Item giay = ItemService.gI().itemtlinhItem((short) 565);
                                            Item rada = ItemService.gI().itemtlinhItem((short) 561);
                                            InventoryServiceNew.gI().addItemBag(player, ao);
                                            InventoryServiceNew.gI().addItemBag(player, quan);
                                            InventoryServiceNew.gI().addItemBag(player, gang);
                                            InventoryServiceNew.gI().addItemBag(player, giay);
                                            InventoryServiceNew.gI().addItemBag(player, rada);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                        } else {
                                            Item ao = ItemService.gI().itemtlinhItem((short) 559);
                                            Item quan = ItemService.gI().itemtlinhItem((short) 560);
                                            Item gang = ItemService.gI().itemtlinhItem((short) 566);
                                            Item giay = ItemService.gI().itemtlinhItem((short) 567);
                                            Item rada = ItemService.gI().itemtlinhItem((short) 561);
                                            InventoryServiceNew.gI().addItemBag(player, ao);
                                            InventoryServiceNew.gI().addItemBag(player, quan);
                                            InventoryServiceNew.gI().addItemBag(player, gang);
                                            InventoryServiceNew.gI().addItemBag(player, giay);
                                            InventoryServiceNew.gI().addItemBag(player, rada);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                        }
                                        Service.gI().sendThongBao(player, "Tiến Hành Nhận Mốc Nạp 50K");
                                        Thread.sleep(3000);
                                        Service.gI().sendThongBao(player, "Bạn Đã Nhận Được một set thần linh");
                                    } else {
                                        this.npcChat(player, "Bạn đã nhận mốc nạp " + Util.format(mocnap) + "\nHãy Nạp Thêm Để Nhận Mốc Nạp");
                                    }
                                }
                            } catch (Exception DM_BKT) {
                            }
                            break;
                            case 1:
                                try {
                                GirlkunResultSet checkmocnap = GirlkunDB.executeQuery(
                                        "select * from account where id = ? and username = ?", player.getSession().userId, player.getSession().uu);
                                if (checkmocnap.first()) {
                                    int mocnap = checkmocnap.getInt("mocnap");
                                    if (player.getSession().tongnap >= 100000 && mocnap < 100000) {
                                        GirlkunDB.executeUpdate(
                                                "update account set mocnap = ? where id = ? and username = ?", 100000, player.getSession().userId, player.getSession().uu);

                                        int dohd = player.gender == 0 ? 2003 : player.gender == 1 ? 2004 : 2005;
                                        Item cc = ItemService.gI().createNewItem((short) dohd, 2);
                                        cc.itemOptions.add(new Item.ItemOption(30, 0));
                                        InventoryServiceNew.gI().addItemBag(player, cc);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Tiến Hành Nhận Mốc Nạp 100k");
                                        Thread.sleep(3000);
                                        Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + cc.template.name);
                                    } else {
                                        this.npcChat(player, "Bạn đã nhận mốc nạp " + Util.format(mocnap) + "\nHãy Nạp Thêm Để Nhận Mốc Nạp");
                                    }
                                }
                            } catch (Exception DM_BKT) {
                            }
                            break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.HOTRO) {
                        switch (select) {
                            case 0:
                                if (player.inventory.gem >= 10000000) {
                                    this.npcChat(player, "Bạn đã nhận hỗ trợ ngọc xanh rồi ");
                                    break;
                                }
                                player.inventory.gem = 500000;
                                Service.getInstance().sendMoney(player);
                                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 500.000 ngọc xanh");
                                break;
                            case 1:
                                if (player.playerTask.taskMain.id == 11) {
                                    if (player.playerTask.taskMain.index == 0) {
                                        TaskService.gI().doneTask(player, ConstTask.TASK_11_0);
                                    } else if (player.playerTask.taskMain.index == 1) {
                                        for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 25; i++) {
                                            TaskService.gI().doneTask(player, ConstTask.TASK_11_1);
                                        }
                                    } else if (player.playerTask.taskMain.index == 2) {
                                        for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 25; i++) {
                                            TaskService.gI().doneTask(player, ConstTask.TASK_11_2);
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Ta đã giúp con hoàn thành nhiệm vụ rồi mau đi trả nhiệm vụ");
                                    }
                                } else if (player.playerTask.taskMain.id == 13) {
                                    if (player.playerTask.taskMain.index == 0) {
                                        TaskService.gI().doneTask(player, ConstTask.TASK_13_0);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Ta đã giúp con hoàn thành nhiệm vụ rồi mau đi trả nhiệm vụ");
                                    }
                                } else if (player.playerTask.taskMain.id == 27) {
                                    if (player.playerTask.taskMain.index == 0) {
                                        TaskService.gI().doneTask(player, ConstTask.TASK_27_0);
                                    } else if (player.playerTask.taskMain.index == 1) {
                                        for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 25; i++) {
                                            TaskService.gI().doneTask(player, ConstTask.TASK_27_1);
                                        }
                                    } else if (player.playerTask.taskMain.index == 2) {
                                        for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 25; i++) {
                                            TaskService.gI().doneTask(player, ConstTask.TASK_27_2);
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Ta đã giúp con hoàn thành nhiệm vụ rồi mau đi trả nhiệm vụ");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Nhiệm vụ hiện tại không thuộc diện hỗ trợ");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.TRAI_DAT) {
                                   ShopServiceNew.gI().opendShop(player, "BUNMA", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }
    
    
//    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//
//            public void Npcchat(Player player) {
//                String[] chat = {
//                    "Giúp Ta đẫn Hổ mập Về Nhà",
//                    "Em buông tay anh vì lí do gì ",
//                    "Người hãy nói đi , đừng Bắt Anh phải nghĩ suy"
//                };
//                Timer timer = new Timer();
//                timer.scheduleAtFixedRate(new TimerTask() {
//                    int index = 0;
//
//                    @Override
//                    public void run() {
//                        npcChat(player, chat[index]);
//                        index = (index + 1) % chat.length;
//                    }
//                }, 6000, 6000);
//            }
//
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé\n Bạn đang có\b|7| "
//                                + player.diemhotong
//                                + " Điểm đi buôn", "Cửa\nhàng", "Hộ tống",
//                                "Cửa hàng\nHộ tống", "TOP Hộ Tống", "hướng dẫn\nhộ tống");
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (player.iDMark.isBaseMenu()) {
//                        switch (select) {
//                            case 0://Shop
//                                if (player.gender == ConstPlayer.TRAI_DAT) {
//                                    ShopServiceNew.gI().opendShop(player, "BUNMA", true);
//                                } else {
//                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
//                                }
//                                break;
//                            case 1:
//                                createOtherMenu(player, ConstNpc.HO_TONG,
//                                        "Ngươi tìm ta có việc gì?\n",
//                                        "Đi thôi", "Mua Đá", "Từ chối");
//                                break;
//                            case 2://Shop
//                                ShopServiceNew.gI().opendShop(player, "CUA_HANG_HO_TONG", true);
//                                break;
//                            case 3:
//                                Service.getInstance().sendThongBaoOK(player, TopService.gethotong());
//                                break;
//                            case 4:
//                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_HO_TONG);
//                                break;
//                        }
//                    } else if (player.iDMark.getIndexMenu() == ConstNpc.HO_TONG) {
//                        switch (select) {
//                            case 0:
//                                Boss oldDuongTank = BossManager.gI().getBossById(Util.createIdDuongTank((int) player.id));
//                                if (oldDuongTank != null) {
//                                    this.npcChat(player, " Bumma đang được hộ tống" + oldDuongTank.zone.zoneId);
//                                } else if (player.inventory.gold < 500000000) {
//                                    //  player.inventory.ruby -= 2000;
//                                    this.npcChat(player, "Nhà ngươi không đủ 500 triệu vàng");
//                                } else {
//                                    BossData bossDataClone = new BossData(
//                                            "Bunma do" + " " + player.name + " hộ tống",
//                                            (byte) 2,
//                                            new short[]{42, 43, 44, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
//                                            100000,
//                                            new long[]{player.nPoint.hpMax * 2},
//                                            new int[]{103},
//                                            new int[][]{
//                                                {Skill.TAI_TAO_NANG_LUONG, 7, 15000}},
//                                            new String[]{}, //text chat 1
//                                            new String[]{}, //text chat 2
//                                            new String[]{}, //text chat 3
//                                            60
//                                    );
//                                    try {
//                                        DuongTank dt = new DuongTank(Util.createIdDuongTank((int) player.id), bossDataClone, player.zone, player.location.x - 20, player.location.y);
//                                        dt.playerTarger = player;
//                                        int[] map = {6, 29, 30, 4, 5, 27, 28};
//                                        dt.mapCongDuc = map[Util.nextInt(map.length)];
//                                        player.haveDuongTang = true;
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    //trừ vàng khi gọi boss
//                                    player.inventory.gold -= 500000000;
//                                    Service.getInstance().sendMoney(player);
//                                    break;
//                                }
//                            case 1:
//                                createOtherMenu(player, ConstNpc.MUA_XU,
//                                        "|1|Ở đây ta có bán Đá Ngũ Sắc Đỏ\n"
//                                        + "|5|10 XU = 10 Thỏi vàng\n"
//                                        + "|5|105 XU = 100 Thỏi vàng\n"
//                                        + "|5|1100 XU = 1000 Thỏi vàng\n"
//                                        + "|3|Ngươi muốn mua số lượng bao nhiêu?",
//                                        "Mua 10\nĐá", "Mua 105\nĐá", "Mua 1100\nĐá", "Từ chối");
//                                break;
//                        }
//                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MUA_XU) {
//                        switch (select) {
//                            case 0:
//                                Item tv = null;
//                                try {
//                                    tv = InventoryServiceNew.gI().findItemBag(player, 457);
//                                } catch (Exception e) {
//                                }
//                                if (tv == null || tv.quantity < 10) {
//                                    this.npcChat(player, "Con còn thiếu x10 thỏi vàng");
//
//                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                    this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                } else {
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 10);
//                                    Item capsule = ItemService.gI().createNewItem((short) 1665, 10);
//                                    InventoryServiceNew.gI().addItemBag(player, capsule);
//                                    player.diemhotong += 10;
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    this.npcChat(player, "Con nhận được x10 Đá Ngũ Sắc Đỏ");
//                                }
//                                break;
//                            case 1:
//                                Item tv1 = null;
//                                try {
//                                    tv1 = InventoryServiceNew.gI().findItemBag(player, 457);
//                                } catch (Exception e) {
//                                }
//                                if (tv1 == null || tv1.quantity < 100) {
//                                    this.npcChat(player, "Con còn thiếu x100 thỏi vàng");
//
//                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                    this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                } else {
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, tv1, 100);
//                                    Item capsule = ItemService.gI().createNewItem((short) 1665, 105);
//                                    InventoryServiceNew.gI().addItemBag(player, capsule);
//                                    player.diemhotong += 100;
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    this.npcChat(player, "Con nhận được x105 Đá Ngũ Sắc Đỏ");
//                                }
//                                break;
//                            case 2:
//                                Item tv2 = null;
//                                try {
//                                    tv2 = InventoryServiceNew.gI().findItemBag(player, 457);
//                                } catch (Exception e) {
//                                }
//                                if (tv2 == null || tv2.quantity < 1000) {
//                                    this.npcChat(player, "Con còn thiếu x1000 thỏi vàng");
//
//                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                    this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                } else {
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, tv2, 1000);
//                                    Item capsule = ItemService.gI().createNewItem((short) 1665, 1100);
//                                    InventoryServiceNew.gI().addItemBag(player, capsule);
//                                    player.diemhotong += 1000;
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    this.npcChat(player, "Con nhận được x1100 Đá Ngũ Sắc Đỏ");
//                                }
//                                break;
//
//                        }
//                    }
//                }
//            }
//        };
//    }

    public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.idNRNM != -1) {
                            if (player.zone.map.mapId == 7) {
                                this.createOtherMenu(player, 1, "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước", "Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối");
                            }
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.NAMEC) {
                                    ShopServiceNew.gI().opendShop(player, "DENDE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc", "Đóng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {

                        if (player.clan == null) {
                            Service.gI().sendThongBao(player, "Không có bang hội");
                            return;
                        }
                        if (player.idNRNM != 353) {
                            Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            return;
                        }

                        byte numChar = 0;
                        for (Player pl : player.zone.getPlayers()) {
                            if (pl.clan.id == player.clan.id && pl.id != player.id) {
                                if (pl.idNRNM != -1) {
                                    numChar++;
                                }
                            }
                        }
                        if (numChar < 6) {
                            Service.gI().sendThongBao(player, "Anh hãy tập hợp đủ 7 viên ngọc rồng nameck đi");
                            return;
                        }

                        if (player.zone.map.mapId == 7 && player.idNRNM != -1) {
                            if (player.idNRNM == 353) {
                                NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() + 86400000;
                                NgocRongNamecService.gI().firstNrNamec = true;
                                NgocRongNamecService.gI().timeNrNamec = 0;
                                NgocRongNamecService.gI().doneDragonNamec();
                                NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
                                NgocRongNamecService.gI().reInitNrNamec((long) 86399000);
                                SummonDragon.gI().summonNamec(player);
                            } else {
                                Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaNm(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (this.mapId == 206) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Hãy gia nhập phe chúng tôi để chống lại Fide",
                                    "Đổi phe", "Giao ngọc");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Cadic và Fide đang kéo cả hành tinh Namec đến bờ vực diệt vong, hãy đến giải cứu hành tinh Namec",
                                    "Đến chiến trường", "Bảng xếp hạng");
                        }

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        if (this.mapId == 206) {
                            switch (select) {
                                // về trạm vũ trụ
                                case 0:
                                    if (!player.haveChangeFlagNamec) {
                                        Service.gI().changeFlag(player, 9);
                                        player.haveChangeFlagNamec = true;
                                    } else {
                                        this.npcChat(player, "Cậu đã có phe của mình rồi ?");
                                    }
                                    break;
                                case 1:
                                    if (player.getSession().actived && player.cFlag == 9) {
                                        List<Item> listNro = new ArrayList<>();
                                        Item nroNM1s = InventoryServiceNew.gI().findItemBag(player, 1816);
                                        if (nroNM1s != null) {
                                            listNro.add(nroNM1s);
                                        }
                                        Item nroNM2s = InventoryServiceNew.gI().findItemBag(player, 1817);
                                        if (nroNM2s != null) {
                                            listNro.add(nroNM2s);
                                        }
                                        Item nroNM3s = InventoryServiceNew.gI().findItemBag(player, 1818);
                                        if (nroNM3s != null) {
                                            listNro.add(nroNM3s);
                                        }
                                        Item nroNM4s = InventoryServiceNew.gI().findItemBag(player, 1819);
                                        if (nroNM4s != null) {
                                            listNro.add(nroNM4s);
                                        }
                                        Item nroNM5s = InventoryServiceNew.gI().findItemBag(player, 1820);
                                        if (nroNM5s != null) {
                                            listNro.add(nroNM5s);
                                        }
                                        Item nroNM6s = InventoryServiceNew.gI().findItemBag(player, 1821);
                                        if (nroNM6s != null) {
                                            listNro.add(nroNM6s);
                                        }
                                        Item nroNM7s = InventoryServiceNew.gI().findItemBag(player, 1822);
                                        if (nroNM7s != null) {
                                            listNro.add(nroNM7s);
                                        }
                                        if (listNro.size() > 0) {
                                            for (Item nro : listNro) {
                                                Service.gI().sendThongBao(player, "Bạn vừa giao " + nro.template.name
                                                        + " cho Bulma và nhận 1 điểm!");
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, nro, 1);
                                                player.pointNroNamec++;
                                                InventoryServiceNew.gI().sendItemBags(player);
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player,
                                                    "Cậu không có ngọc rồng !");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Cậu phải cùng phe mới có thể giao ngọc rồng !");
                                    }
                                    break;
                            }
                        } else {

                            switch (select) {
                                // về trạm vũ trụ
                                case 0:
                                    final Calendar rightNow = Calendar.getInstance();
                                    int hour = rightNow.get(11);
                                    if (hour >= 14 && hour < 17) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 206, -1, 1542);
                                    } else {
                                        Service.gI().sendThongBaoFromAdmin(player,
                                                "Chỉ mở từ 14h đến 17h !");
                                    }
                                    break;
                                case 1:
                                    Service.getInstance().sendThongBaoOK(player, TopService.getpointNroNamec());
                                    break;
                            }
                        }
                    }

                }
            }
        };
    }

    public static Npc fideNm(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        String vodaiBallMessage = "|7|Các ngươi đã giao " + vodaiBallNumber + " viên ngọc rồng."
                                + "\nHãy tìm đủ 7 viên để có thể nhận thưởng ";
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                vodaiBallMessage + "\n Hãy gia nhập với bọn ta, ta sẽ chia cho ngươi 1 nửa hành tinh ",
                                "Đổi phe", "Giao ngọc", "Nhận thưởng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (!player.haveChangeFlagNamec) {
                                    Service.gI().changeFlag(player, 10);
                                    player.haveChangeFlagNamec = true;
                                } else {
                                    this.npcChat(player, "Cậu đã có phe của mình rồi ?");
                                }
                                break;
                            case 1:
                                if (player.getSession().actived && player.cFlag == 10 && player.iDMark.isHoldVodaiBall()) {
                                    Service.gI().sendThongBao(player, "Ngươi đã giao ngọc thành công hãy tìm thêm những viên còn lại");
                                    // Thực hiện logic cấp phần thưởng cho người chơi ở đây
                                    player.iDMark.setHoldVodaiBall(false);
                                    player.iDMark.setTempIdVodaiBallHold(-1);
                                    vodaiBallNumber++;
                                    Service.gI().sendFlagBag(player);
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi phải cùng phe mới có thể giao ngọc rồng !");
                                }
                                break;
                            case 2:
                                if (player.getSession().actived && player.cFlag == 10 && vodaiBallNumber >= 7) {
                                    int[] itemDos = new int[]{457, 1669, 933, 225, 1669, 1652, 1653, 1651, 1648, 1649, 1650, 224, 225, 226, 227, 228};
                                    for (int itemId : itemDos) {
                                        Item item = ItemService.gI().createNewItem((short) itemId);
                                        item.quantity = 5; // Đặt số lượng là 10 cho mỗi vật phẩm
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                    }
                                    InventoryServiceNew.gI().sendItemBags(player);

                                    // Reset số lượng ngọc về 0
                                    vodaiBallNumber = 0;

                                    Service.gI().sendThongBao(player, "|7|Bạn nhận được các vật phẩm đặc biệt với số lượng 10 cho mỗi vật phẩm!");

                                    // Thêm thông báo để người chơi quay lại ngày hôm sau
                                    Service.gI().sendThongBao(player, "Ngày mai hãy quay lại để nhận thưởng tiếp!");
                                } else {
                                    Service.gI().sendThongBao(player, "Các ngươi chưa giao đủ 7 viên ngọc cho ta");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.XAYDA) {
                                    ShopServiceNew.gI().opendShop(player, "APPULE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 84) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất" : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 84) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_FIND_BOSS = 50000000;

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang bị bắt cóc rồi");
                        } else {
                            if (this.mapId == 19) {

                                int taskId = TaskService.gI().getIdTask(pl);
                                switch (taskId) {
                                    case ConstTask.TASK_19_0:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_KUKU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    default:
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");

                                        break;
                                }
                            } else if (this.mapId == 68) {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                            } else {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, "
                                        + "có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                                        "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 26) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 19) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().player.nPoint.power >= 41000000000L
                                            && player.playerTask.taskMain.id > 26) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    } // break;
                                    else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ 26 Và Đạt 41 Tỷ Sức Mạnh");
                                    }
                                    break;
                                case 1:
                                    if (player.playerTask.taskMain.id >= 17) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ Đi");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.KUKUMDDRAMBO);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                    boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                        boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold)
                                                    + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    if (player.getSession().player.nPoint.power >= 41000000000L
                                            && player.playerTask.taskMain.id > 26) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    } // break;
                                    else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ 26 Và Đạt 41 Tỷ Sức Mạnh");
                                    }
                                    break;
                                case 2:
                                    if (player.playerTask.taskMain.id >= 17) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ Đi");
                                    }
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 68) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

//    public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    createOtherMenu(player, ConstNpc.BASE_MENU,
//                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
//                            "Cửa hàng", "Sự Kiện", "QUY ĐỔI");
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
//                        if (player.iDMark.isBaseMenu()) {
//                            switch (select) {
//                                case 0: //shop
//                                    createOtherMenu(player, ConstNpc.SHOPSANTA,
//                                            "|7|Xin chào, Ngươi muốn mua gì",
//                                            "Shop Thường", "Shop Hồng Ngọc", "Shop Thỏi Vàng");
//                                    break;
//
//                                case 1:
//                                    ShopServiceNew.gI().opendShop(player, "SU_KIEN", true);
//                                    break;
//                                case 2:
//                                    this.createOtherMenu(player, ConstNpc.QUY_DOI, "|7|Số tiền của bạn còn : " + player.getSession().vnd + "\n"
//                                            + "Muốn quy đổi không", "Quy Đổi\n10.000\n 20 Thỏi Vàng", "Quy Đổi\n20.000\n 40 Thỏi Vàng", "Quy Đổi\n30.000\n 60 Thỏi Vàng", "Quy Đổi\n50.000\n 100 Thỏi Vàng", "Quy Đổi\n100.000 \n200 Thỏi Vàng", "Đóng");
//                                    break;
//
//                            }
//
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.SHOPSANTA) {
//                            switch (select) {
//                                case 0:
//                                    ShopServiceNew.gI().opendShop(player, "SANTA", true);
//                                    break;
//                                case 1: //shoptrue
//                                    ShopServiceNew.gI().opendShop(player, "SHOP_NGU_SAC", false);
//                                    break;
//                                case 2:
//                                    ShopServiceNew.gI().opendShop(player, "BKT", true);
//                                    break;
//                            }
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI) {
//                            PreparedStatement ps = null;
//                            try (Connection con = GirlkunDB.getConnection();) {
//                                switch (select) {
//                                    case 0:
//                                        Item thoivang = ItemService.gI().createNewItem((short) (457));
//                                        thoivang.quantity += 19;
//                                        if (player.getSession().vnd < 10000) {
//                                            Service.gI().sendThongBao(player, "Bạn không có đủ 10k coin");
//                                            return;
//                                        }
//                                        player.getSession().vnd -= 10000;
//                                        InventoryServiceNew.gI().addItemBag(player, thoivang);
//                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 20 " + thoivang.template.name + " Nhớ out game vô lại");
//                                        break;
//                                    case 1:
//                                        Item thoivangg = ItemService.gI().createNewItem((short) (457));
//                                        thoivangg.quantity += 39;
//                                        if (player.getSession().vnd < 20000) {
//                                            Service.gI().sendThongBao(player, "Bạn không có đủ 20k coin");
//                                            return;
//                                        }
//                                        player.getSession().vnd -= 20000;
//                                        InventoryServiceNew.gI().addItemBag(player, thoivangg);
//                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 40 " + thoivangg.template.name + " Nhớ out game vô lại");
//                                        break;
//                                    case 2:
//                                        Item thoivanggg = ItemService.gI().createNewItem((short) (457));
//                                        thoivanggg.quantity += 59;
//                                        if (player.getSession().vnd < 30000) {
//                                            Service.gI().sendThongBao(player, "Bạn không có đủ 30k coin");
//                                            return;
//                                        }
//                                        player.getSession().vnd -= 30000;
//                                        InventoryServiceNew.gI().addItemBag(player, thoivanggg);
//                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 60 " + thoivanggg.template.name + " Nhớ out game vô lại");
//                                        break;
//                                    case 3:
//                                        Item thoivangggg = ItemService.gI().createNewItem((short) (457));
//                                        thoivangggg.quantity += 99;
//                                        if (player.getSession().vnd < 50000) {
//                                            Service.gI().sendThongBao(player, "Bạn không có đủ 50k coin");
//                                            return;
//                                        }
//                                        player.getSession().vnd -= 50000;
//                                        InventoryServiceNew.gI().addItemBag(player, thoivangggg);
//                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 1000 " + thoivangggg.template.name + " Nhớ out game vô lại");
//                                        break;
//                                    case 4:
//                                        Item thoivanggggg = ItemService.gI().createNewItem((short) (457));
//                                        thoivanggggg.quantity += 199;
//                                        if (player.getSession().vnd < 100000) {
//                                            Service.gI().sendThongBao(player, "Bạn không có đủ 100k coin");
//                                            return;
//                                        }
//                                        player.getSession().vnd -= 100000;
//                                        InventoryServiceNew.gI().addItemBag(player, thoivanggggg);
//                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 200 " + thoivanggggg.template.name + " Nhớ out game vô lại");
//                                        break;
//                                }
//
//                                ps = con.prepareStatement("update account set vnd = ? where id = ?");
//                                ps.setInt(1, player.getSession().vnd);
//                                ps.setInt(2, player.getSession().userId);
//                                ps.executeUpdate();
//                                ps.close();
//
//                            } catch (Exception e) {
//                                Logger.logException(NpcFactory.class, e, "Lỗi update vnd " + player.name);
//                            } finally {
//                                try {
//                                    if (ps != null) {
//                                        ps.close();
//                                    }
//                                } catch (SQLException ex) {
//                                    System.out.println("Lỗi khi update vnd");
//
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }
    
    public static Npc napvip(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "Cửa hàng", "Quy đổi\nVNĐ", "Mở\nThẻ tháng", "Mở thành viên", "Nhận Quà\nTích Lũy", "Hồi\nskill nhanh");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "SANTA", false);
                                    break;
//                                case 1: //tiệm hồng ngọc
//                                    ShopServiceNew.gI().opendShop(player, "SANTA_RUBY", false);
//                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.QUY_DOI, "|7|Số tiền của con còn : " + Util.format(player.getSession().vnd) + " VNĐ" + "\n"
                                            + "|1|Con Muốn quy đổi không", "Quy đổi\nThỏi vàng", "Quy đổi\nHồng ngọc");
                                    break;
                                case 2:
                                    this.createOtherMenu(player, 6,
                                            "|7|Số tiền của bạn còn : " + Util.format(player.getSession().vnd) + " VNĐ\n"
                                            + "\n|1|Mở thẻ tháng, trong tháng đó mỗi ngày lại đây điểm danh nhận 2000 hồng ngọc và 10 thỏi vàng",
                                            "Kích hoạt\n thẻ tháng", "Điểm danh\n thẻ tháng");
                                    break;
                                case 3:
                                    this.createOtherMenu(player, 7,
                                            "|7|Số tiền của bạn còn : " + Util.format(player.getSession().vnd) + " VNĐ\n"
                                            + "\n|1|Mở thành viên với giá 20.000 vnđ"
                                            + "\n Sau khi mở thành viên bạn sẽ được nhận 1 rương trái ác quỷ thường"
                                            + "\b|7|Bạn chắc chắn mở chứ chứ?",
                                            "Mở thành viên", "Từ chối");
                                    break;
                                case 4:
                                    this.createOtherMenu(player, 3,
                                            "\b|1|Mốc nạp càng cao giá trị quà càng lớn!"
                                            + "\b|5|LƯU Ý:Hãy chừa ô trống để nhận quà!!!"
                                            + "\b|7|Bạn đã nạp tích lũy được: " + Util.format(player.tongtien) + " VND",
                                            "Mốc ", "Đóng");
                                    break;
                                case 5:
                                    this.createOtherMenu(player, ConstNpc.HOI_SKILL,
                                            "Đưa ta 50 thỏi vàng để hồi skill cấp tốc"
                                            + "\n|1|Lưu ý: Ấn vào skill muốn hồi rồi ấn đồng ý, chỉ hồi 1 skill chỉ định"
                                            + "\n|2| Chúc bạn chơi game vui vẻ",
                                            "Đồng ý", "Từ chối");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.QUY_DOI_VANG,
                                            "|1|Con Muốn đổi thỏi vàng à?\n"
                                            + "|7|Con đang có :" + Util.format(player.getSession().vnd) + " VNĐ\n",
                                            "20k\n20 thỏi vàng", "50k\n50 thỏi vàng",
                                            "100k\n105 thỏi vàng", "200k\n210 thỏi vàng",
                                            "500k\n530 thỏi vàng", "1000k\n1100 thỏi vàng");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.QUY_DOI_NGOC,
                                            "|1|Con Muốn đổi hồng ngọc à?\n"
                                            + "|7|Con đang có " + Util.format(player.getSession().vnd) + " VNĐ\n",
                                            "20k\n20000 ngọc", "50k\n50000 ngọc", "100k\n100000 ngọc", "200k\n200000 ngọc");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.HOI_SKILL) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().findItemBag(player, 457).isNotNullItem()
                                            && InventoryServiceNew.gI().findItemBag(player, 457).quantity >= 50) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player,
                                                InventoryServiceNew.gI().findItemBag(player, 457), 50);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        int subTimeParam = 80;
                                        int coolDown = player.playerSkill.skillSelect.coolDown;
                                        player.playerSkill.skillSelect.lastTimeUseThisSkill = System.currentTimeMillis() - (coolDown * subTimeParam / 100);
                                        if (subTimeParam != 0) {
                                            Service.getInstance().sendTimeSkill(player);
                                        }
                                        return;
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Không có tiền mà đòi hít ... thơm");

                                    }
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI_VANG) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().vnd < 20000) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ 20k VND");
                                        return;
                                    }
                                    try {
                                        int sum = player.getSession().vnd - 20000;
                                        PlayerDAO.subvnd(player, 20000);
                                        GirlkunDB.executeUpdate(
                                                "update account set vnd = ? where id = ?", sum,
                                                player.account_id);
                                        player.getSession().vnd = sum;
                                    } catch (Exception e) {
                                        this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                    }
                                    Item i = ItemService.gI().createNewItem((short) 457, (short) 20);
                                    InventoryServiceNew.gI().addItemBag(player, i);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được 20 thỏi vàng");
                                    break;

                                case 1:
                                    if (player.getSession().vnd < 50000) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ 50k VND");
                                        return;
                                    }
                                    try {
                                        int sum = player.getSession().vnd - 50000;
                                        PlayerDAO.subvnd(player, 50000);
                                        GirlkunDB.executeUpdate(
                                                "update account set vnd = ? where id = ?", sum,
                                                player.account_id);
                                        player.getSession().vnd = sum;
                                    } catch (Exception e) {
                                        this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                    }
                                    Item i2 = ItemService.gI().createNewItem((short) 457, (short) 50);
                                    InventoryServiceNew.gI().addItemBag(player, i2);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được 50 thỏi vàng");
                                    break;
                                case 2:
                                    if (player.getSession().vnd < 100000) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ 100k VND");
                                        return;
                                    }

                                    try {
                                        int sum = player.getSession().vnd - 100000;
                                        PlayerDAO.subvnd(player, 100000);
                                        GirlkunDB.executeUpdate(
                                                "update account set vnd = ? where id = ?", sum,
                                                player.account_id);
                                        player.getSession().vnd = sum;
                                    } catch (Exception e) {
                                        this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                    }
                                    Item i3 = ItemService.gI().createNewItem((short) 457, (short) 105);
                                    InventoryServiceNew.gI().addItemBag(player, i3);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được 100 thỏi vàng");
                                    break;
                                case 3:
                                    if (player.getSession().vnd < 200000) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ 200k VND");
                                        return;
                                    }
                                    try {
                                        int sum = player.getSession().vnd - 200000;
                                        PlayerDAO.subvnd(player, 200000);
                                        GirlkunDB.executeUpdate(
                                                "update account set vnd = ? where id = ?", sum,
                                                player.account_id);
                                        player.getSession().vnd = sum;
                                    } catch (Exception e) {
                                        this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                    }
                                    Item i4 = ItemService.gI().createNewItem((short) 457, (short) 210);
                                    InventoryServiceNew.gI().addItemBag(player, i4);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được 200 thỏi vàng");
                                    break;
                                case 4:
                                    if (player.getSession().vnd < 500000) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ 500k VND");
                                        return;
                                    }
                                    try {
                                        int sum = player.getSession().vnd - 500000;
                                        PlayerDAO.subvnd(player, 500000);
                                        GirlkunDB.executeUpdate(
                                                "update account set vnd = ? where id = ?", sum,
                                                player.account_id);
                                        player.getSession().vnd = sum;
                                    } catch (Exception e) {
                                        this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                    }
                                    Item i5 = ItemService.gI().createNewItem((short) 457, (short) 520);
                                    InventoryServiceNew.gI().addItemBag(player, i5);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được 200 thỏi vàng");
                                    break;
                                case 5:
                                    if (player.getSession().vnd < 1000000) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ 1000k VND");
                                        return;
                                    }
                                    try {
                                        int sum = player.getSession().vnd - 1000000;
                                        PlayerDAO.subvnd(player, 1000000);
                                        GirlkunDB.executeUpdate(
                                                "update account set vnd = ? where id = ?", sum,
                                                player.account_id);
                                        player.getSession().vnd = sum;
                                    } catch (Exception e) {
                                        this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                    }
                                    Item i6 = ItemService.gI().createNewItem((short) 457, (short) 1200);
                                    InventoryServiceNew.gI().addItemBag(player, i6);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được 200 thỏi vàng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI_NGOC) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().vnd < 20000) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ 20k VND");
                                        return;
                                    }

                                    if (PlayerDAO.subquydoi(player, 20000)) {
                                        player.inventory.ruby += 20000;
                                        Service.getInstance().sendMoney(player);
                                        Service.gI().sendThongBao(player, "Bạn đã nhận được 20000 hồng ngọc");
                                    }
                                    break;

                                case 1:
                                    if (player.getSession().vnd < 50000) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ 50k VND");
                                        return;
                                    }

                                    if (PlayerDAO.subquydoi(player, 50000)) {

                                        player.inventory.ruby += 50000;
                                        Service.getInstance().sendMoney(player);
                                        Service.gI().sendThongBao(player, "Bạn đã nhận được 50000 hồng ngọc");
                                    }
                                    break;
                                case 2:
                                    if (player.getSession().vnd < 100000) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ 100k VND");
                                        return;
                                    }

                                    if (PlayerDAO.subquydoi(player, 100000)) {

                                        player.inventory.ruby += 100000;
                                        Service.getInstance().sendMoney(player);
                                        Service.gI().sendThongBao(player, "Bạn đã nhận được 100000 hồng ngọc");
                                    }
                                    break;
                                case 3:
                                    if (player.getSession().vnd < 200000) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ 200k VND");
                                        return;
                                    }

                                    if (PlayerDAO.subquydoi(player, 200000)) {

                                        player.inventory.ruby += 200000;
                                        Service.getInstance().sendMoney(player);
                                        Service.gI().sendThongBao(player, "Bạn đã nhận được 200000 hồng ngọc");
                                    }
                            }
                        } else if (player.iDMark.getIndexMenu() == 7) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().actived) {
                                        Service.gI().sendThongBao(player, "Bạn đã mở thành viên rồi");
                                        return;
                                    }
                                    if (player.getSession().vnd >= 20000) {
                                        player.getSession().actived = true;
                                        PlayerDAO.subvnd(player, 20000);
                                        Item chest = ItemService.gI().createNewItem((short) (1551));
                                        InventoryServiceNew.gI().addItemBag(player, chest);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Chúc mừng bạn đã mở thành viên thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn méo đủ tiền");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 6) {
                            switch (select) {
                                case 0:
                                    if (player.Month_Card == 1) {
                                        Service.gI().sendThongBao(player, "Bạn đã kích hoạt thẻ tháng này rồi");
                                        return;
                                    }
                                    if (player.getSession().vnd >= 100000) {
                                        player.Month_Card = 1;
                                        PlayerDAO.subvnd(player, 100000);
                                        Service.gI().sendThongBaoOK(player, "Chúc mừng bạn đã kích hoạt thẻ tháng thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn méo đủ tiền");
                                    }
                                    break;
                                case 1:
                                    if (player.Month_Card == 1) {
                                        LocalDate now = LocalDate.now();
                                        LocalDate lastDate = LocalDate.ofEpochDay(player.last_time_dd / (24 * 60 * 60 * 1000));
                                        long caichogi = now.toEpochDay() * (24 * 60 * 60 * 1000);
                                        boolean isAttended = now.isEqual(lastDate);
                                        if (!isAttended && caichogi != player.last_time_dd) {
                                            System.out.println(caichogi);
                                            System.out.println(player.last_time_dd);
                                            player.inventory.ruby += 2000;
                                            Item tv = ItemService.gI().createNewItem((short) 457, 10);
                                            InventoryServiceNew.gI().addItemBag(player, tv);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendMoney(player);
                                            Service.gI().sendThongBaoOK(player, "Điểm danh thành công nhận 2.000 hồng ngọc và 10 thỏi vàng");
                                            player.last_time_dd = now.toEpochDay() * (24 * 60 * 60 * 1000);
                                        } else {
                                            Service.gI().sendThongBao(player, "Hôm nay bạn đã điểm danh rồi");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa kích hoạt thẻ tháng");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 3) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 5,
                                            "\b|1|Nạp càng nhiều nhận quà càng ngon, mại zô mại zô!"
                                            + "\b|5|LƯU Ý: HÃY CHỪA ĐỦ Ô TRỐNG TRONG HÀNH TRANG ĐỂ TRÁNH NHẬN KO ĐỦ ĐỒ, NẾU MẤT ADMIN KO CHỊU TRÁCH NHIỆM!!!"
                                            + "\b|7|Bạn đã nạp tích lũy được: " + Util.format(player.tongtien) + "VND",
                                            "Nhận mốc 10k", "Nhận mốc 20k", "Nhận mốc 50k", "Nhận mốc 100k", "Nhận mốc 200k", "Nhận mốc 500k", "Nhận mốc 1000k");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 5) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 66,
                                            "|6|Đây là quà mốc tích lũy!\n"
                                            + "|7|Mốc 10K: 10V 3sao\n"
                                            + "|3|Bạn đã nạp tích lũy được: " + Util.format(player.tongtien) + "VND\n"
                                            + "|2|Cảm ơn quý khách đã ghé, chúc bạn chơi game vui vẻ!",
                                            "Nhận ngay", "Từ chối");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, 77,
                                            "|6|Đây là quà mốc tích lũy!\n"
                                            + "|7|Mốc 20K: 20v 3sao\n"
                                            + "|3|Bạn đã nạp tích lũy được: " + Util.format(player.tongtien) + "VND\n"
                                            + "|2|Cảm ơn quý khách đã ghé, chúc bạn chơi game vui vẻ!",
                                            "Nhận ngay", "Từ chối");
                                    break;
                                case 2:
                                    this.createOtherMenu(player, 88,
                                            "|6|Đây là quà mốc tích lũy!\n"
                                            + "|7|Mốc 50K: Cải trang Zamatsu, Linh Thú, Tuần Lộc\n"
                                            + "|3|Bạn đã nạp tích lũy được: " + Util.format(player.tongtien) + "VND\n"
                                            + "|2|Cảm ơn quý khách đã ghé, chúc bạn chơi game vui vẻ!",
                                            "Nhận ngay", "Từ chối");
                                    break;
                                case 3:
                                    this.createOtherMenu(player, 99,
                                            "|6|Đây là quà mốc tích lũy!\n"
                                            + "|7|Mốc 100K: 1 Set Kích Hoạt thường, 3 bộ nro\n"
                                            + "|3|Bạn đã nạp tích lũy được: " + Util.format(player.tongtien) + "VND\n"
                                            + "|2|Cảm ơn quý khách đã ghé, chúc bạn chơi game vui vẻ!",
                                            "Nhận ngay", "Từ chối");
                                    break;
                                case 4:
                                    this.createOtherMenu(player, 100,
                                            "|6|Đây là quà mốc tích lũy!\n"
                                            + "|7|Mốc 200k: Kiếm, x99(cn,bh,gx,bk)\n"
                                            + "|3|Bạn đã nạp tích lũy được: " + Util.format(player.tongtien) + "VND\n"
                                            + "|2|Cảm ơn quý khách đã ghé, chúc bạn chơi game vui vẻ!",
                                            "Nhận ngay", "Từ chối");
                                    break;
                                case 5:
                                    this.createOtherMenu(player, 111,
                                            "|6|Đây là quà mốc tích lũy!\n"
                                            + "|7|Mốc 500K: Cải trang Obito lục đạo, x2000 viên đá các loại\n"
                                            + "|3|Bạn đã nạp tích lũy được: " + Util.format(player.tongtien) + "VND\n"
                                            + "|2|Cảm ơn quý khách đã ghé, chúc bạn chơi game vui vẻ!",
                                            "Nhận ngay", "Từ chối");
                                    break;
                                case 6:
                                    this.createOtherMenu(player, 122,
                                            "|6|Đây là quà mốc tích lũy!\n"
                                            + "|7|Mốc 1000K: Meow tà ác, cải trang Majin Form, Xe tăng, x99 3sao\n"
                                            + "|3|Bạn đã nạp tích lũy được: " + Util.format(player.tongtien) + "VND\n"
                                            + "|2|Cảm ơn quý khách đã ghé, chúc bạn chơi game vui vẻ!",
                                            "Nhận ngay", "Từ chối");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 66) {
                            switch (select) {
                                case 0: //obito
                                    if (player.tongtien < 10000) {
                                        Service.gI().sendThongBao(player, "Bạn không tích đủ mốc 10k");
                                        return;
                                    } else {
                                        if (player.flagMoc10k == 0) {
                                            Item i = ItemService.gI().createNewItem((short) 16, 10);
                                            InventoryServiceNew.gI().addItemBag(player, i);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn đã nhận quà mốc 10k, Xin chúc mừng");
                                            player.flagMoc10k += 1;
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn đã nhận quà mốc 10k rồi!");
                                        }
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 77) {
                            switch (select) {
                                case 0:
                                    if (player.tongtien < 20000) {
                                        Service.gI().sendThongBao(player, "Bạn không tích đủ mốc 20k");
                                        return;
                                    } else {
                                        if (player.flagMoc20k == 0) {
                                            Item i = ItemService.gI().createNewItem((short) 16, 20);
                                            Item i1 = ItemService.gI().createNewItem((short) 20, 20);
                                            InventoryServiceNew.gI().addItemBag(player, i);
                                            InventoryServiceNew.gI().addItemBag(player, i1);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn đã nhận quà mốc 20k, Xin chúc mừng");
                                            player.flagMoc20k += 1;
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn đã nhận quà mốc 20k rồi!");
                                        }
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 88) {
                            switch (select) {
                                case 0:
                                    if (player.tongtien < 50000) {
                                        Service.gI().sendThongBao(player, "Bạn không tích đủ mốc 50k");
                                        return;
                                    } else {
                                        if (player.flagMoc50k == 0) {
                                            Item i1 = ItemService.gI().createNewItem((short) 1531, 1);
                                            Item i2 = ItemService.gI().createNewItem((short) 1413, 1);
                                            Item i3 = ItemService.gI().createNewItem((short) 1310, 1);
                                            i1.itemOptions.add(new Item.ItemOption(50, Util.nextInt(3, 7)));
                                            i1.itemOptions.add(new Item.ItemOption(77, Util.nextInt(3, 7)));
                                            i1.itemOptions.add(new Item.ItemOption(103, Util.nextInt(3, 7)));
                                            i3.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 13)));
                                            i3.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 13)));
                                            i3.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 13)));
                                            i2.itemOptions.add(new Item.ItemOption(2, Util.nextInt(10, 15)));
                                            i2.itemOptions.add(new Item.ItemOption(95, 15));
                                            i2.itemOptions.add(new Item.ItemOption(96, 15));
                                            InventoryServiceNew.gI().addItemBag(player, i1);
                                            InventoryServiceNew.gI().addItemBag(player, i2);
                                            InventoryServiceNew.gI().addItemBag(player, i3);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn đã nhận quà mốc 50k, Xin chúc mừng");
                                            player.flagMoc50k += 1;
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn đã nhận quà mốc 50k rồi!");
                                        }
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 99) {
                            switch (select) {
                                case 0:
                                    if (player.tongtien < 100000) {
                                        Service.gI().sendThongBao(player, "Bạn không tích đủ mốc 100k");
                                        return;
                                    } else {
                                        if (player.flagMoc100k == 0) {
                                            if (player.gender == 0) {
                                                Item ao = ItemService.gI().createNewItem((short) 0, 1);
                                                Item quan = ItemService.gI().createNewItem((short) 6, 1);
                                                Item gang = ItemService.gI().createNewItem((short) 21, 1);
                                                Item giay = ItemService.gI().createNewItem((short) 27, 1);
                                                Item rada = ItemService.gI().createNewItem((short) 12, 1);
                                                ao.itemOptions.add(new Item.ItemOption(47, 2));
                                                ao.itemOptions.add(new Item.ItemOption(129, 1));
                                                ao.itemOptions.add(new Item.ItemOption(141, 1));
                                                ao.itemOptions.add(new Item.ItemOption(30, 1));
                                                quan.itemOptions.add(new Item.ItemOption(6, 30));
                                                quan.itemOptions.add(new Item.ItemOption(129, 1));
                                                quan.itemOptions.add(new Item.ItemOption(141, 1));
                                                quan.itemOptions.add(new Item.ItemOption(30, 1));
                                                gang.itemOptions.add(new Item.ItemOption(0, 5));
                                                gang.itemOptions.add(new Item.ItemOption(129, 1));
                                                gang.itemOptions.add(new Item.ItemOption(141, 1));
                                                gang.itemOptions.add(new Item.ItemOption(30, 1));
                                                giay.itemOptions.add(new Item.ItemOption(7, 30));
                                                giay.itemOptions.add(new Item.ItemOption(129, 1));
                                                giay.itemOptions.add(new Item.ItemOption(141, 1));
                                                giay.itemOptions.add(new Item.ItemOption(30, 1));
                                                rada.itemOptions.add(new Item.ItemOption(14, 1));
                                                rada.itemOptions.add(new Item.ItemOption(129, 1));
                                                rada.itemOptions.add(new Item.ItemOption(141, 1));
                                                rada.itemOptions.add(new Item.ItemOption(30, 1));
                                                InventoryServiceNew.gI().addItemBag(player, ao);
                                                InventoryServiceNew.gI().addItemBag(player, quan);
                                                InventoryServiceNew.gI().addItemBag(player, gang);
                                                InventoryServiceNew.gI().addItemBag(player, giay);
                                                InventoryServiceNew.gI().addItemBag(player, rada);
                                            }
                                            if (player.gender == 1) {
                                                Item ao = ItemService.gI().createNewItem((short) 1, 1);
                                                Item quan = ItemService.gI().createNewItem((short) 7, 1);
                                                Item gang = ItemService.gI().createNewItem((short) 22, 1);
                                                Item giay = ItemService.gI().createNewItem((short) 28, 1);
                                                Item rada = ItemService.gI().createNewItem((short) 12, 1);
                                                ao.itemOptions.add(new Item.ItemOption(47, 2));
                                                ao.itemOptions.add(new Item.ItemOption(131, 1));
                                                ao.itemOptions.add(new Item.ItemOption(143, 1));
                                                ao.itemOptions.add(new Item.ItemOption(30, 1));
                                                quan.itemOptions.add(new Item.ItemOption(6, 30));
                                                quan.itemOptions.add(new Item.ItemOption(131, 1));
                                                quan.itemOptions.add(new Item.ItemOption(143, 1));
                                                quan.itemOptions.add(new Item.ItemOption(30, 1));
                                                gang.itemOptions.add(new Item.ItemOption(0, 5));
                                                gang.itemOptions.add(new Item.ItemOption(131, 1));
                                                gang.itemOptions.add(new Item.ItemOption(143, 1));
                                                gang.itemOptions.add(new Item.ItemOption(30, 1));
                                                giay.itemOptions.add(new Item.ItemOption(7, 30));
                                                giay.itemOptions.add(new Item.ItemOption(131, 1));
                                                giay.itemOptions.add(new Item.ItemOption(143, 1));
                                                giay.itemOptions.add(new Item.ItemOption(30, 1));
                                                rada.itemOptions.add(new Item.ItemOption(14, 1));
                                                rada.itemOptions.add(new Item.ItemOption(131, 1));
                                                rada.itemOptions.add(new Item.ItemOption(143, 1));
                                                rada.itemOptions.add(new Item.ItemOption(30, 1));
                                                InventoryServiceNew.gI().addItemBag(player, ao);
                                                InventoryServiceNew.gI().addItemBag(player, quan);
                                                InventoryServiceNew.gI().addItemBag(player, gang);
                                                InventoryServiceNew.gI().addItemBag(player, giay);
                                                InventoryServiceNew.gI().addItemBag(player, rada);
                                            }
                                            if (player.gender == 2) {
                                                Item ao = ItemService.gI().createNewItem((short) 2, 1);
                                                Item quan = ItemService.gI().createNewItem((short) 8, 1);
                                                Item gang = ItemService.gI().createNewItem((short) 23, 1);
                                                Item giay = ItemService.gI().createNewItem((short) 29, 1);
                                                Item rada = ItemService.gI().createNewItem((short) 12, 1);
                                                ao.itemOptions.add(new Item.ItemOption(47, 2));
                                                ao.itemOptions.add(new Item.ItemOption(135, 1));
                                                ao.itemOptions.add(new Item.ItemOption(138, 1));
                                                ao.itemOptions.add(new Item.ItemOption(30, 1));
                                                quan.itemOptions.add(new Item.ItemOption(6, 30));
                                                quan.itemOptions.add(new Item.ItemOption(135, 1));
                                                quan.itemOptions.add(new Item.ItemOption(138, 1));
                                                quan.itemOptions.add(new Item.ItemOption(30, 1));
                                                gang.itemOptions.add(new Item.ItemOption(0, 5));
                                                gang.itemOptions.add(new Item.ItemOption(135, 1));
                                                gang.itemOptions.add(new Item.ItemOption(138, 1));
                                                gang.itemOptions.add(new Item.ItemOption(30, 1));
                                                giay.itemOptions.add(new Item.ItemOption(7, 30));
                                                giay.itemOptions.add(new Item.ItemOption(135, 1));
                                                giay.itemOptions.add(new Item.ItemOption(138, 1));
                                                giay.itemOptions.add(new Item.ItemOption(30, 1));
                                                rada.itemOptions.add(new Item.ItemOption(14, 1));
                                                rada.itemOptions.add(new Item.ItemOption(135, 1));
                                                rada.itemOptions.add(new Item.ItemOption(138, 1));
                                                rada.itemOptions.add(new Item.ItemOption(30, 1));
                                                InventoryServiceNew.gI().addItemBag(player, ao);
                                                InventoryServiceNew.gI().addItemBag(player, quan);
                                                InventoryServiceNew.gI().addItemBag(player, gang);
                                                InventoryServiceNew.gI().addItemBag(player, giay);
                                                InventoryServiceNew.gI().addItemBag(player, rada);
                                            }
                                            for (int i = 14; i <= 20; i++) {
                                                Item nr = ItemService.gI().createNewItem((short) i, 3);
                                                InventoryServiceNew.gI().addItemBag(player, nr);
                                            }
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn đã nhận quà mốc 100k, Xin chúc mừng");

                                            player.flagMoc100k += 1;
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn đã nhận quà mốc 100k rồi!");
                                        }
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 100) {
                            switch (select) {
                                case 0:
                                    if (player.tongtien < 200000) {
                                        Service.gI().sendThongBao(player, "Bạn không tích đủ mốc 200k");
                                        return;
                                    } else {
                                        if (player.flagMoc200k == 0) {
                                            Item dt = ItemService.gI().createNewItem((short) 1525, 1);
                                            Item cn = ItemService.gI().createNewItem((short) 381, 99);
                                            Item bh = ItemService.gI().createNewItem((short) 382, 99);
                                            Item bk = ItemService.gI().createNewItem((short) 383, 99);
                                            Item gx = ItemService.gI().createNewItem((short) 384, 99);
                                            InventoryServiceNew.gI().addItemBag(player, dt);
                                            InventoryServiceNew.gI().addItemBag(player, cn);
                                            InventoryServiceNew.gI().addItemBag(player, bh);
                                            InventoryServiceNew.gI().addItemBag(player, bk);
                                            InventoryServiceNew.gI().addItemBag(player, gx);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn đã nhận quà mốc 200k, Xin chúc mừng");
                                            player.flagMoc200k += 1;
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn đã nhận quà mốc 200k rồi!");
                                        }
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 111) {
                            switch (select) {
                                case 0:
                                    if (player.tongtien < 500000) {
                                        Service.gI().sendThongBao(player, "Bạn không tích đủ mốc 500k");
                                        return;
                                    } else {
                                        if (player.flagMoc500k == 0) {
                                            Item i4 = ItemService.gI().createNewItem((short) 1603, 1);
                                            Item i5 = ItemService.gI().createNewItem((short) 220, 2000);
                                            Item i6 = ItemService.gI().createNewItem((short) 221, 2000);
                                            Item i7 = ItemService.gI().createNewItem((short) 222, 2000);
                                            Item i8 = ItemService.gI().createNewItem((short) 223, 2000);
                                            i4.itemOptions.add(new Item.ItemOption(50, 35));
                                            i4.itemOptions.add(new Item.ItemOption(77, 35));
                                            i4.itemOptions.add(new Item.ItemOption(103, 35));
                                            i4.itemOptions.add(new Item.ItemOption(14, 10));
                                            i4.itemOptions.add(new Item.ItemOption(5, 7));
                                            i4.itemOptions.add(new Item.ItemOption(30, 1));
                                            InventoryServiceNew.gI().addItemBag(player, i4);
                                            InventoryServiceNew.gI().addItemBag(player, i5);
                                            InventoryServiceNew.gI().addItemBag(player, i6);
                                            InventoryServiceNew.gI().addItemBag(player, i7);
                                            InventoryServiceNew.gI().addItemBag(player, i8);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn đã nhận quà mốc 500k, Xin chúc mừng");
                                            player.flagMoc500k += 1;
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn đã nhận quà mốc 500k rồi!");
                                        }
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 122) {
                            switch (select) {
                                case 0:
                                    if (player.tongtien < 1000000) {
                                        Service.gI().sendThongBao(player, "Bạn không tích đủ mốc 1000k");
                                        return;
                                    } else {
                                        if (player.flagMoc1cu == 0) {
                                            Item meow = ItemService.gI().createNewItem((short) 1618, 1);
                                            Item ct = ItemService.gI().createNewItem((short) 1596, 1);
                                            Item nr = ItemService.gI().createNewItem((short) 16, 99);
                                            Item du = ItemService.gI().createNewItem((short) 1580, 1);
                                            ct.itemOptions.add(new Item.ItemOption(50, 40));
                                            ct.itemOptions.add(new Item.ItemOption(77, 40));
                                            ct.itemOptions.add(new Item.ItemOption(103, 40));
                                            ct.itemOptions.add(new Item.ItemOption(14, 15));
                                            ct.itemOptions.add(new Item.ItemOption(5, 10));
                                            ct.itemOptions.add(new Item.ItemOption(30, 1));
                                            meow.itemOptions.add(new Item.ItemOption(50, 20));
                                            meow.itemOptions.add(new Item.ItemOption(77, 20));
                                            meow.itemOptions.add(new Item.ItemOption(103, 20));
                                            du.itemOptions.add(new Item.ItemOption(50, 20));
                                            du.itemOptions.add(new Item.ItemOption(77, 20));
                                            du.itemOptions.add(new Item.ItemOption(103, 20));
                                            InventoryServiceNew.gI().addItemBag(player, meow);
                                            InventoryServiceNew.gI().addItemBag(player, ct);
                                            InventoryServiceNew.gI().addItemBag(player, nr);
                                            InventoryServiceNew.gI().addItemBag(player, du);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn đã nhận quà mốc 1000k, Xin chúc mừng");
                                            player.flagMoc1cu += 1;
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn đã nhận quà mốc 1000k rồi!");
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thodaika(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                createOtherMenu(player, 0, "\b|8|Trò chơi Tài Xỉu đang được diễn ra\n\n|6|Thử vận may của bạn với trò chơi Tài Xỉu! Đặt cược và dự đoán đúng"
                        + "\n kết quả, bạn sẽ được nhận thưởng lớn. Hãy tham gia ngay và\n cùng trải nghiệm sự hồi hộp, thú vị trong trò chơi này!"
                        + "\n\n|7|(Điều kiện tham gia : mở thành viên)\n\n|2|Đặt tối thiểu: 1.000 Hồng ngọc\n Tối đa: 1.000.000.000.000 Hồng ngọc"
                        + "\n\n|7| Lưu ý : Thoát game khi chốt Kết quả sẽ MẤT Tiền cược và Tiền thưởng", "Thể lệ", "Tham gia");
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    String time = ((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (pl.iDMark.getIndexMenu() == 0) {
                        if (select == 0) {
                            createOtherMenu(pl, ConstNpc.IGNORE_MENU, "|5|Có 2 nhà cái Tài và Xĩu, bạn chỉ được chọn 1 nhà để tham gia"
                                    + "\n\n|6|Sau khi kết thúc thời gian đặt cược. Hệ thống sẽ tung xí ngầu để biết kết quả Tài Xỉu"
                                    + "\n\nNếu Tổng số 3 con xí ngầu <=10 : XỈU\nNếu Tổng số 3 con xí ngầu >10 : TÀI\nNếu 3 Xí ngầu cùng 1 số : TAM HOA (Nhà cái lụm hết)"
                                    + "\n\n|7|Lưu ý: Số Hồng ngọc nhận được sẽ bị nhà cái lụm đi 20%. Trong quá trình diễn ra khi đặt cược nếu thoát game trong lúc phát thưởng phần quà sẽ bị HỦY", "Ok");
                        } else if (select == 1) {
                            if (TaiXiu.gI().baotri == false) {
                                if (pl.goldTai == 0 && pl.goldXiu == 0) {
                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ LỒN NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                } else if (pl.goldTai > 0) {
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI TÀI XỈU---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                } else {
                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ LỒN NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                }
                            } else {
                                if (pl.goldTai == 0 && pl.goldXiu == 0) {
                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ LỒN NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                } else if (pl.goldTai > 0) {
                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ LỒN NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                } else {
                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ LỒN NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(pl.goldXiu) + " Hồng ngọc" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                }
                            }
                        }
                    } else if (pl.iDMark.getIndexMenu() == 1) {
                        if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai == 0 && pl.goldXiu == 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ LỒN NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                    break;
                                case 1:
                                    if (!pl.getSession().actived) {
                                        Service.gI().sendThongBao(pl, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                        Input.gI().TAI_taixiu(pl);
                                    }
                                    break;
                                case 2:
                                    if (!pl.getSession().actived) {
                                        Service.gI().sendThongBao(pl, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                        Input.gI().XIU_taixiu(pl);
                                    }
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai > 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ LỒN NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu > 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ LỒN NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai > 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ LỒN NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu > 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ LỒN NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu == 0 && pl.goldTai == 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ LỒN NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");

                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    ShopServiceNew.gI().opendShop(pl, "URON", false);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc gohannn(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                        this.createOtherMenu(player, 0, "Tiến vào map hỗ trợ tân thủ\nNơi up set kích hoạt và nhiều phần quà hấp dẫn\nChỉ dành cho người chơi từ 2k đến 60 tỷ sức mạnh!",
                                "Đến\nRừng Aurura", "Từ chối");
                    } else if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Hãy mang cho ta x20 mảnh SKH mỗi loại ta sẽ giúp ngươi đổi SKH",
                                "Đổi SKH\n Trái Đất", "Đổi SKH\n Namec", "Đổi SKH\n Xayda");
                    } else {
                        this.createOtherMenu(player, 0, "Ngươi muốn quay về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Item manhSKHao = null;
                                    Item manhSKHquan = null;
                                    Item manhSKHgang = null;
                                    Item manhSKHgiay = null;
                                    Item manhSKHrada = null;
                                    try {
                                        manhSKHao = InventoryServiceNew.gI().findItemBag(player, 1394);
                                        manhSKHquan = InventoryServiceNew.gI().findItemBag(player, 1395);
                                        manhSKHgang = InventoryServiceNew.gI().findItemBag(player, 1396);
                                        manhSKHgiay = InventoryServiceNew.gI().findItemBag(player, 1397);
                                        manhSKHrada = InventoryServiceNew.gI().findItemBag(player, 1398);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (manhSKHao == null || manhSKHquan == null || manhSKHgang == null || manhSKHgiay == null || manhSKHrada == null
                                            || manhSKHao.quantity < 20 || manhSKHquan.quantity < 20
                                            || manhSKHgang.quantity < 20 || manhSKHgiay.quantity < 20 || manhSKHrada.quantity < 20) {
                                        this.npcChat(player, "Bạn không đủ vật phẩm");
                                    } else {

                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHao, 20);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHquan, 20);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHgang, 20);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHgiay, 20);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHrada, 20);
                                        Service.getInstance().sendMoney(player);
                                        Item hopSKH = ItemService.gI().createNewItem((short) 2000, 1); // Hộp SKH Trái Đất                                                              
                                        InventoryServiceNew.gI().addItemBag(player, hopSKH);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được hộp SKH Trái Đất");
                                    }
                                    break;
                                case 1:
                                    Item manhSKHao2 = null;
                                    Item manhSKHquan2 = null;
                                    Item manhSKHgang2 = null;
                                    Item manhSKHgiay2 = null;
                                    Item manhSKHrada2 = null;
                                    try {
                                        manhSKHao2 = InventoryServiceNew.gI().findItemBag(player, 1394);
                                        manhSKHquan2 = InventoryServiceNew.gI().findItemBag(player, 1395);
                                        manhSKHgang2 = InventoryServiceNew.gI().findItemBag(player, 1396);
                                        manhSKHgiay2 = InventoryServiceNew.gI().findItemBag(player, 1397);
                                        manhSKHrada2 = InventoryServiceNew.gI().findItemBag(player, 1398);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (manhSKHao2 == null || manhSKHquan2 == null || manhSKHgang2 == null || manhSKHgiay2 == null || manhSKHrada2 == null
                                            || manhSKHao2.quantity < 20 || manhSKHquan2.quantity < 20
                                            || manhSKHgang2.quantity < 20 || manhSKHgiay2.quantity < 20 || manhSKHrada2.quantity < 20) {
                                        this.npcChat(player, "Bạn không đủ vật phẩm");
                                    } else {

                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHao2, 20);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHquan2, 20);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHgang2, 20);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHgiay2, 20);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHrada2, 20);
                                        Service.getInstance().sendMoney(player);
                                        Item hopSKH = ItemService.gI().createNewItem((short) 2001, 1); // Hộp SKH Trái Đất                                                              
                                        InventoryServiceNew.gI().addItemBag(player, hopSKH);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được hộp SKH Namek");
                                    }
                                    break;
                                case 2:
                                    Item manhSKHao3 = null;
                                    Item manhSKHquan3 = null;
                                    Item manhSKHgang3 = null;
                                    Item manhSKHgiay3 = null;
                                    Item manhSKHrada3 = null;
                                    try {
                                        manhSKHao3 = InventoryServiceNew.gI().findItemBag(player, 1394);
                                        manhSKHquan3 = InventoryServiceNew.gI().findItemBag(player, 1395);
                                        manhSKHgang3 = InventoryServiceNew.gI().findItemBag(player, 1396);
                                        manhSKHgiay3 = InventoryServiceNew.gI().findItemBag(player, 1397);
                                        manhSKHrada3 = InventoryServiceNew.gI().findItemBag(player, 1398);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (manhSKHao3 == null || manhSKHquan3 == null || manhSKHgang3 == null || manhSKHgiay3 == null || manhSKHrada3 == null
                                            || manhSKHao3.quantity < 20 || manhSKHquan3.quantity < 20
                                            || manhSKHgang3.quantity < 20 || manhSKHgiay3.quantity < 20 || manhSKHrada3.quantity < 20) {
                                        this.npcChat(player, "Bạn không đủ vật phẩm");
                                    } else {

                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHao3, 20);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHquan3, 20);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHgang3, 20);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHgiay3, 20);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhSKHrada3, 20);
                                        Service.getInstance().sendMoney(player);
                                        Item hopSKH = ItemService.gI().createNewItem((short) 2002, 1); // Hộp SKH Trái Đất                                                              
                                        InventoryServiceNew.gI().addItemBag(player, hopSKH);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được hộp SKH Xayda");
                                    }
                                    break;
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.TAY_PS_HOA_TRANG_BI);
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.AN_TRANG_BI);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:

                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    } else if (this.mapId == 112) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                    break;
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                            "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                            + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                            "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng", "Đóng");
                                    break;
                                case 1:
//
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2: //nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI);
                                    break;
                                case 3: //Mở chỉ số bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_CHUNG_NHAN_XUAT_SU);
                                    break;
                                case 5:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", true);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "BUA_8H", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                case CombineServiceNew.LAM_PHEP_NHAP_DA:
                                case CombineServiceNew.EP_CHUNG_NHAN_XUAT_SU:
                                case CombineServiceNew.NHAP_NGOC_RONG:
                                case CombineServiceNew.PHAN_RA_DO_THAN_LINH:
                                case CombineServiceNew.NANG_CAP_DO_TS:
                                case CombineServiceNew.NANG_CAP_SKH_VIP:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc shopdevip(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "SẮM ĐỆ", "ĐỔI\nSKILL ĐỆ", "Đổi\nhành tinh", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.BUY_DE_TU_VIP,
                                            "Xin chào, ta có một số bé Đệ VIP nè!\n|1|Số tiền của bạn còn : " + Util.format(player.getSession().vnd) + " VNĐ"
                                            + "\n|7|Đệ Super Bư: 30k Vnđ, Hợp thể tăng 30% chỉ số"
                                            + "\n|7|Đệ Berus: 40k Vnđ, Hợp thể tăng 40% chỉ số"
                                            + "\n|7|Đệ Uub: 50k Vnđ, Hợp thể tăng 50% chỉ số"
                                            + "\n|7|Đệ Bư music: 70k Vnđ, Hợp thể tăng 60% chỉ số"
                                            + "\n|7|Đệ ngộ không: 150k Vnđ, Hợp thể tăng 100% chỉ số"
                                            + "\n|7|Đệ Pazuzz: 200k Vnđ, Hợp thể tăng 120% chỉ số"
                                            + "\n|7|Đệ Kaie: 250k Vnđ, Hợp thể tăng 140% chỉ số"
                                            + "\n|7| Lưu Ý Phải Có Đệ Thường Mới Mua Được Đệ Vip",
                                            "Đệ\nSuper Bư", "Đệ Berus", "Đệ Uub", "Đệ\nBư music", "Đệ\nngộ không", "Đệ Pazuzu", "Đệ Kaie");
                                    break;
                                case 1:
                                    createOtherMenu(player, ConstNpc.DOI_SKILL,
                                            "ĐỔI SKILL ĐỆ TỬ\n"
                                            + "|2|Chào bạn : " + player.name
                                            + "\nSố Dư : " + Util.format(player.getSession().vnd) + " VNĐ\n"
                                            + "|7|Bạn muốn đổi skill nào?",
                                            "SKILL 2,3\n10.000VNĐ", "SKILL 3,4\n20.000VNĐ");
                                    break;
                                case 2:
                                    createOtherMenu(player, ConstNpc.DOI_HANH_TINH, "|1|ĐỔI HÀNH TINH"
                                            + "\n|5|Ta sẽ giúp người đổi hành tinh của mình?"
                                            + "\n|5|Việc này giúp ngươi có thể dễ dàng làm nhiệm vụ, cũng như săn đệ.."
                                            + "\n|7|chỉ có thể đổi tạm thời, out game là mất!"
                                            + "\n|2|Phí đổi Hành tinh là 50.000 Hồng ngọc"
                                            + "\n|4|Bạn muốn chọn gì?", "Trái Đất", "Namek", "Xayda", "Từ chối");
                                    break;
//                                case 3:
//                                    BossManager.gI().showListBossNormal(player);
//                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.DOI_HANH_TINH) {
                            switch (select) {
                                case 0:
                                    if (player.gender == 0) {
                                        Service.gI().sendThongBao(player, "|7|Bạn đã là Hành tinh Trái đất rồi mà!!!");
                                        return;
                                    }
                                    if (player.inventory.ruby < 50_000) {
                                        Service.gI().sendThongBao(player, "|7|Bạn không đủ 50.000 Hồng ngọc");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        if (player.inventory.itemsBody.get(0).quantity < 1
                                                && player.inventory.itemsBody.get(1).quantity < 1
                                                && player.inventory.itemsBody.get(2).quantity < 1
                                                && player.inventory.itemsBody.get(3).quantity < 1
                                                && player.inventory.itemsBody.get(4).quantity < 1) {
                                            player.inventory.ruby -= 50000;
                                            player.gender = 0;
                                            short[] headtd = {30, 31, 64};
                                            player.playerSkill.skills.clear();
                                            for (Skill skill : player.playerSkill.skills) {
                                                skill.point = 1;
                                            }
                                            int[] skillsArr = new int[]{0, 1, 6, 9, 10, 20, 22, 24, 19};
                                            for (int i = 0; i < skillsArr.length; i++) {
                                                player.playerSkill.skills.add(SkillUtil.createSkill(skillsArr[i], 1));
                                            }
                                            player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(0);
                                            player.playerIntrinsic.intrinsic.param1 = 0;
                                            player.playerIntrinsic.intrinsic.param2 = 0;
                                            player.playerIntrinsic.countOpen = 0;
                                            player.head = headtd[Util.nextInt(headtd.length)];
                                            Service.gI().sendThongBao(player, "|1|Đổi hành tinh thành công");
                                            Service.gI().player(player);
                                            Service.getInstance().sendFlagBag(player);
                                            Service.getInstance().Send_Caitrang(player);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            Service.gI().Send_Info_NV(player);
                                        } else {
                                            Service.gI().sendThongBao(player, "Tháo hết 5 món đầu đang mặc ra nha");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Balo đầy");
                                    }
                                    Service.gI().player(player);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendMoney(player);
                                    break;
                                case 1:
                                    if (player.gender == 1) {
                                        Service.gI().sendThongBao(player, "|7|Bạn đã là Hành tinh Namek rồi mà!!!");
                                        return;
                                    }
                                    if (player.inventory.ruby < 50_000) {
                                        Service.gI().sendThongBao(player, "|7|Bạn không đủ 50.000 Hồng ngọc");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        if (player.inventory.itemsBody.get(0).quantity < 1
                                                && player.inventory.itemsBody.get(1).quantity < 1
                                                && player.inventory.itemsBody.get(2).quantity < 1
                                                && player.inventory.itemsBody.get(3).quantity < 1
                                                && player.inventory.itemsBody.get(4).quantity < 1) {
                                            player.inventory.ruby -= 50000;
                                            player.gender = 1;
                                            short[] headnm = {9, 29, 32};
                                            player.playerSkill.skills.clear();
                                            for (Skill skill : player.playerSkill.skills) {
                                                skill.point = 1;
                                            }
                                            int[] skillsArr = new int[]{2, 3, 7, 11, 12, 17, 18, 26, 19};
                                            for (int i = 0; i < skillsArr.length; i++) {
                                                player.playerSkill.skills.add(SkillUtil.createSkill(skillsArr[i], 1));
                                            }
                                            player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(0);
                                            player.playerIntrinsic.intrinsic.param1 = 0;
                                            player.playerIntrinsic.intrinsic.param2 = 0;
                                            player.playerIntrinsic.countOpen = 0;
                                            player.head = headnm[Util.nextInt(headnm.length)];
                                            Service.gI().sendThongBao(player, "|1|Đổi hành tinh thành công");
                                            Service.gI().player(player);
                                            Service.getInstance().sendFlagBag(player);
                                            Service.getInstance().Send_Caitrang(player);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            Service.gI().Send_Info_NV(player);
                                        } else {
                                            Service.gI().sendThongBao(player, "Tháo hết 5 món đầu đang mặc ra nha");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Balo đầy");
                                    }
                                    Service.gI().player(player);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendMoney(player);
                                    break;
                                case 2:
                                    if (player.gender == 2) {
                                        Service.gI().sendThongBao(player, "|7|Bạn đã là Hành tinh Xayda rồi mà!!!");
                                        return;
                                    }
                                    if (player.inventory.ruby < 50_000) {
                                        Service.gI().sendThongBao(player, "|7|Bạn không đủ 50.000 Hồng ngọc");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        if (player.inventory.itemsBody.get(0).quantity < 1
                                                && player.inventory.itemsBody.get(1).quantity < 1
                                                && player.inventory.itemsBody.get(2).quantity < 1
                                                && player.inventory.itemsBody.get(3).quantity < 1
                                                && player.inventory.itemsBody.get(4).quantity < 1) {
                                            player.inventory.ruby -= 50000;
                                            player.gender = 2;
                                            short[] headxd = {27, 28, 6};
                                            player.playerSkill.skills.clear();
                                            for (Skill skill : player.playerSkill.skills) {
                                                skill.point = 1;
                                            }
                                            int[] skillsArr = new int[]{4, 5, 8, 13, 14, 21, 23, 25, 19};
                                            for (int i = 0; i < skillsArr.length; i++) {
                                                player.playerSkill.skills.add(SkillUtil.createSkill(skillsArr[i], 1));
                                            }
                                            player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(0);
                                            player.playerIntrinsic.intrinsic.param1 = 0;
                                            player.playerIntrinsic.intrinsic.param2 = 0;
                                            player.playerIntrinsic.countOpen = 0;
                                            player.head = headxd[Util.nextInt(headxd.length)];
                                            Service.gI().sendThongBao(player, "|1|Đổi hành tinh thành công");
                                            Service.gI().player(player);
                                            Service.getInstance().sendFlagBag(player);
                                            Service.getInstance().Send_Caitrang(player);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            Service.gI().Send_Info_NV(player);
                                        } else {
                                            Service.gI().sendThongBao(player, "Tháo hết 5 món đầu đang mặc ra nha");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Balo đầy");
                                    }
                                    Service.gI().player(player);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendMoney(player);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.DOI_SKILL) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().vnd < 10000) {
                                        Service.gI().sendThongBao(player, "Còn thiếu " + (10000 - player.getSession().vnd));
                                    } else {
                                        if (player.pet == null) {
                                            Service.gI().sendThongBao(player, "Ngươi làm gì có đệ tử?");
                                            break;
                                        }
                                        if (player.pet.playerSkill.skills.get(1).skillId != -1 && player.pet.playerSkill.skills.get(2).skillId != -1) {
                                            try {
                                                int sum = player.getSession().vnd - 10000;
                                                GirlkunDB.executeUpdate(
                                                        "update account set vnd = ? where id = ?", sum,
                                                        player.account_id);
                                                player.getSession().vnd = sum;
                                            } catch (Exception e) {
                                                this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                            }
                                            player.pet.openSkill2();
                                            player.pet.openSkill3();
                                            Service.gI().sendThongBao(player, "Đã đổi thành công chiêu 2,3 đệ tử");
                                        } else if (player.pet.playerSkill.skills.get(1).skillId != -1) {
                                            try {
                                                int sum = player.getSession().vnd - 15000;
                                                GirlkunDB.executeUpdate(
                                                        "update account set vnd = ? where id = ?", sum,
                                                        player.account_id);
                                                player.getSession().vnd = sum;
                                            } catch (Exception e) {
                                                this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                            }
                                            player.pet.openSkill2();
                                            Service.gI().sendThongBao(player, "Đã đổi thành công chiêu 2,3 đệ tử");
                                        } else {
                                            Service.gI().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                                        }
                                    }
                                    break;
                                case 1:
                                    if (player.getSession().vnd < 20000) {
                                        Service.gI().sendThongBao(player, "Còn thiếu " + (20000 - player.getSession().vnd));
                                    } else {
                                        if (player.pet == null) {
                                            Service.gI().sendThongBao(player, "Ngươi làm gì có đệ tử?");
                                            break;
                                        }
                                        if (player.pet.playerSkill.skills.get(2).skillId != -1 && player.pet.playerSkill.skills.get(3).skillId != -1) {
                                            try {
                                                int sum = player.getSession().vnd - 20000;
                                                GirlkunDB.executeUpdate("update account set vnd = ? where id = ?", sum, player.account_id);
                                                player.getSession().vnd = sum;
                                            } catch (Exception e) {
                                                this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                            }
                                            player.pet.openSkill3();
                                            player.pet.openSkill4();
                                            Service.gI().sendThongBao(player, "Đã đổi thành công chiêu 3,4 đệ tử");
                                        } else if (player.pet.playerSkill.skills.get(2).skillId != -1) {
                                            try {
                                                int sum = player.getSession().vnd - 30000;
                                                GirlkunDB.executeUpdate("update account set vnd = ? where id = ?", sum, player.account_id);
                                                player.getSession().vnd = sum;
                                            } catch (Exception e) {
                                                this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                            }
                                            player.pet.openSkill3();
                                            Service.gI().sendThongBao(player, "Đã đổi thành công chiêu 3,4 đệ tử");
                                        } else {
                                            Service.gI().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 3 chứ!");
                                        }
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.BUY_DE_TU_VIP) {
                            PreparedStatement ps = null;
                            try (Connection con = GirlkunDB.getConnection();) {
                                switch (select) {
                                    case 0:
                                        if (player.getSession().vnd < 30000) {
                                            //     if (player.pet.typePet == 1) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 30k Vnđ");
                                            return;
                                        }
                                        if (player.pet == null) {
                                            Service.gI().sendThongBao(player, "Bạn cần phải có đệ tử thường trước");
                                            return;
                                        }

                                        player.getSession().vnd -= 30000;
                                        PetService.gI().createSuperbu(player, true, player.pet.gender);
                                        //             PetService.gI().createMabuPet(player);
                                        break;

                                    case 1:
                                        if (player.getSession().vnd < 40000) {
                                            //             if (player.pet.typePet == 2) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 40k Vnđ");
                                            return;
                                        }
                                        if (player.pet == null) {
                                            Service.gI().sendThongBao(player, "Bạn cần phải có đệ tử thường trước");
                                            return;
                                        }

                                        player.getSession().vnd -= 40000;
                                        //  PetService.gI().createBerusPet(player);
                                        PetService.gI().createberus(player, true, player.pet.gender);
                                        break;

                                    case 2:
                                        if (player.getSession().vnd < 50000) {
                                            //             if (player.pet.typePet == 3) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 50k Vnđ");
                                            return;
                                        }
                                        if (player.pet == null) {
                                            Service.gI().sendThongBao(player, "Bạn cần phải có đệ tử thường trước");
                                            return;
                                        }

                                        player.getSession().vnd -= 50000;
                                        //  PetService.gI().createUubPet(player);
                                        PetService.gI().createuub(player, player.pet != null, player.pet.gender);
                                        break;

                                    case 3:
                                        if (player.getSession().vnd < 70000) {
                                            //     if (player.pet.typePet == 4) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 70k Vnđ");
                                            return;
                                        }
                                        if (player.pet == null) {
                                            Service.gI().sendThongBao(player, "Bạn cần phải có đệ tử thường trước");
                                            return;
                                        }

                                        player.getSession().vnd -= 70000;
                                        //                   PetService.gI().createJanembaPet(player);
                                        PetService.gI().createbumusic(player, player.pet != null, player.pet.gender);
                                        break;

                                    case 4:
                                        if (player.getSession().vnd < 150000) {
                                            //                        if (player.pet.typePet == 5) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 150k Vnđ");
                                            return;
                                        }
                                        if (player.pet == null) {
                                            Service.gI().sendThongBao(player, "Bạn cần phải có đệ tử thường trước");
                                            return;
                                        }

                                        player.getSession().vnd -= 150000;
                                        PetService.gI().createngokhong(player, player.pet != null, player.pet.gender);
                                        break;
                                    case 5: //pazuzu
                                        if (player.getSession().vnd < 200000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 200k Vnđ");
                                            return;
                                        }
                                        if (player.pet == null) {
                                            Service.gI().sendThongBao(player, "Bạn cần phải có đệ tử thường trước");
                                            return;
                                        }

                                        player.getSession().vnd -= 200000;
                                        PetService.gI().createpazuzu(player, player.pet != null, player.pet.gender);
                                        break;
                                    case 6: //kaie
                                        if (player.getSession().vnd < 250000) {
                                            //                        if (player.pet.typePet == 5) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 250k Vnđ");
                                            return;
                                        }
                                        if (player.pet == null) {
                                            Service.gI().sendThongBao(player, "Bạn cần phải có đệ tử thường trước");
                                            return;
                                        }

                                        player.getSession().vnd -= 250000;
                                        PetService.gI().createkaie(player, player.pet != null, player.pet.gender);
                                        break;
                                }

                                ps = con.prepareStatement("update account set vnd = ? where id = ?");
                                ps.setInt(1, player.getSession().vnd);
                                ps.setInt(2, player.getSession().userId);
                                ps.executeUpdate();
                                ps.close();

                            } catch (Exception e) {
                                Logger.logException(NpcFactory.class, e, "Lỗi update Vnđ " + player.name);
                            } finally {
                                try {
                                    if (ps != null) {
                                        ps.close();
                                    }
                                } catch (SQLException ex) {
                                    System.out.println("Lỗi khi update coin");

                                }
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.BUY_PET_VIP) {
                            PreparedStatement ps = null;
                            try (Connection con = GirlkunDB.getConnection();) {
                                switch (select) {
                                    case 0:
                                        Item petvip = ItemService.gI().createNewItem((short) (1434));
                                        if (player.getSession().vnd < 50000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 50k coin");
                                            return;
                                        }
                                        player.getSession().vnd -= 50000;
                                        InventoryServiceNew.gI().addItemBag(player, petvip);
                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 1 " + petvip.template.name + " Nhớ out game vô lại");
                                        break;
                                }

                                ps = con.prepareStatement("update account set vnd = ? where id = ?");
                                ps.setInt(1, player.getSession().vnd);
                                ps.setInt(2, player.getSession().userId);
                                ps.executeUpdate();
                                ps.close();

                            } catch (Exception e) {
                                Logger.logException(NpcFactory.class, e, "Lỗi update coin " + player.name);
                            } finally {
                                try {
                                    if (ps != null) {
                                        ps.close();
                                    }
                                } catch (SQLException ex) {
                                    System.out.println("Lỗi khi update coin");

                                }
                            }
                        }

                    }
                }
            }
        };
    }

    public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Ép sao\ntrang bị",
                                "Pha lê\nhóa\ntrang bị",
                                "Nâng Cấp\n SKH");
//                                "Chuyển hóa\ntrang bị",
//                                "Bóng tối\ntrang bị",
//                                "Nâng cấp\ntrang bị",
//                                "Gia hạn\nvật phẩm",
//                                "Ấn\nKích Hoạt",
//                                "Nâng Cấp\nKí",
//                                "Nâng cấp\nchân mệnh",
//                                "Khảm Đá",
//                                "Luyện Hóa",
//                                "Tinh luyện\nCải trang",
//                                "Ma sứ");
                    } else if (this.mapId == 121) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Về đảo\nrùa");

//                    } else if (this.mapId == 20) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Điểm bí tịch của ngươi hiện tại là : " + player.diembitich,
//                                "Mở chỉ số\n Bí Tịch", "Đổi Sách\n Luyện\n Bí Tịch", "Luyện\n Bí Tịch");

                    } else {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
//                                "Thưởng\nBùa 1h\ngẫu nhiên",
                                "Cửa hàng\nBùa", 
                                "Nâng cấp\nVật phẩm",
                                "Nâng cấp\nBông tai\nPorata",
//                                "Ép Chứng Nhận",
                                "Nhập\nNgọc Rồng",
                                "Sách Tuyệt Kỹ");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;
                                case 2:  CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_KICH_HOAT_VIP);
                                    break;
//                                    createOtherMenu(player, ConstNpc.MENU_CHUYEN_HOA_TRANG_BI,
//                                            "Ta sẽ biến trang bị mới cao cấp của ngươi\n"
//                                            + "thành trang bị có cấp độ và sao pha lê của trang bị cũ",
//                                            "Chuyển hóa\nDùng vàng", "Chuyển hóa\nDùng ngọc");
//                                    break;
//                                case 3:
//                                    createOtherMenu(player, ConstNpc.NANG_KICH_HOAT_VIP,
//                                            "Ngươi tìm ta có việc gì?\n",
//                                            "Nâng\ncấp", "Xóa\ndòng", "Hướng đẫn");
//                                    break;
//
//                                case 4:
//                                    createOtherMenu(player, ConstNpc.NANG_KICK_HOAT_VIP2,
//                                            "Ngươi tìm ta có việc gì?\n",
//                                            "Hiến tế\n THẦN LINH", "Hiến tế\nHỦY DIỆT", "Chuyển hóa\nHỦY DIỆT", "Hướng dẫn");
//                                    break;
                                case 5:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.GIA_HAN_VAT_PHAM);
                                    break;
                                case 6:
                                    createOtherMenu(player, ConstNpc.TINH_AN_TRANG_BI,
                                            "Ngươi tìm ta có việc gì?\n",
                                            "Ấn trang bị", "Hướng dẫn");
                                    break;
                                case 7:
                                    createOtherMenu(player, ConstNpc.SKH,
                                            "Ngươi tìm ta có việc gì?\n",
                                            "NÂNG CẤP\n(1)", "NÂNG CẤP\n(2)", "Từ Chối");
                                    break;
                                case 8:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_CHAN_MENH);
                                    break;
                                case 9:
                                    createOtherMenu(player, ConstNpc.KHAM_DA,
                                            "Ngươi tìm ta có việc gì?\n",
                                            "Khảm HP", "Khảm Ki", "Khảm Dame", "Từ Chối");
                                    break;
                                case 10:
                                    createOtherMenu(player, ConstNpc.LUYEN_HOA,
                                            "Ngươi tìm ta có việc gì?\n",
                                            "Luyện\nLinh Thú", "Luyện\nPhụ Kiện", "Từ Chối");
                                    break;
                                case 11:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_CAI_TRANG);
                                    break;
                                case 12:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.OPEN_SKH_MA_SU);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                case CombineServiceNew.CHUYEN_HOA_TRANG_BI:
                                case CombineServiceNew.AN_TRANG_BI:
                                case CombineServiceNew.NANG_CAP_SKH_VIP:
                                case CombineServiceNew.PS_HOA_TRANG_BI:
                                case CombineServiceNew.TAY_PS_HOA_TRANG_BI:
                                case CombineServiceNew.GIA_HAN_VAT_PHAM:
                                case CombineServiceNew.kh_Tl:
                                case CombineServiceNew.kh_Hd:
                                case CombineServiceNew.kh_Ts:
                                case CombineServiceNew.KHAM_DA_HP:
                                case CombineServiceNew.KHAM_DA_MP:
                                case CombineServiceNew.KHAM_DA_DAME:
                                case CombineServiceNew.THANG_HOA_NGOC_BOI:
                                case CombineServiceNew.NANG_CAP_CHAN_MENH:
                                case CombineServiceNew.NANG_CAP_PET2:
                                case CombineServiceNew.NANG_CAP_PK:
                                case CombineServiceNew.CUONG_HOA:
                                case CombineServiceNew.NANG_KICH_HOAT_VIP:
                                case CombineServiceNew.NANG_KICH_HOAT_VIP2:
                                case CombineServiceNew.CHUYEN_HOA_BANG_VANG:
                                case CombineServiceNew.CHUYEN_HOA_BANG_NGOC:
                                case CombineServiceNew.CHUYEN_HOA_DO_HUY_DIET:
                                case CombineServiceNew.MO_CHI_SO_CAI_TRANG:
                                case CombineServiceNew.OPEN_SKH_MA_SU:
                                    switch (select) {
                                        case 0:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 1;
                                            }
                                            break;
                                        case 1:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 10;
                                            }
                                            break;
                                        case 2:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 100;
                                            }
                                            break;
                                    }
                                    CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHUYEN_HOA_DO_HUY_DIET) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_THANG_HOA_NGOC_BOI) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().startCombine1(player, 1);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().startCombine1(player, 1);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHUYEN_HOA_TRANG_BI) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHUYEN_HOA_BANG_VANG);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHUYEN_HOA_BANG_NGOC);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.LUYEN_HOA) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_PET2);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_PK);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.KHAM_DA) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.KHAM_DA_HP);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.KHAM_DA_MP);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.KHAM_DA_DAME);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.PHAP_SU) {
                            switch (select) {
                                case 2:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_PHAP_SU_HOA);
                                    break;
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PS_HOA_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.TAY_PS_HOA_TRANG_BI);
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.NANG_CAP_TRANG_BI) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_Hd);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_T);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHUYEN_HOA_DO_HUY_DIET);
                                    break;
                                case 3:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONGDANHIENTE);
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.TINH_AN_TRANG_BI) {
                            switch (select) {
                                case 0:
                                    //    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.AN_TRANG_BI );
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CUONG_HOA);
                                    break;
                                case 1:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.AN_TRANG_BI_HUONG_DAN);
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.SKH) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_KICH_HOAT_VIP);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_KICH_HOAT_VIP2);
                                    break;
                                case 2:
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO_KICH_HOAT) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        }
                    } else if (this.mapId == 112) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                    break;
                            }
                        }
                    } else if (this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BI_TICH);
                                    break;
                                case 1:
                                    if (player.diembitich >= 250) {
                                        player.diembitich -= 250;
                                        Service.getInstance().sendMoney(player);
                                        Item SachBiTich = ItemService.gI().createNewItem((short) 1652, 1); // Sách Bí Tịch                                                             
                                        InventoryServiceNew.gI().addItemBag(player, SachBiTich);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được Sách Luyện Bí Tịch");
                                    } else {
                                        this.npcChat(player, "Bạn không đủ điểm bí tịch");
                                        return;
                                    }
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.LUYEN_BI_TICH);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.MO_CHI_SO_BI_TICH:
                                case CombineServiceNew.LUYEN_BI_TICH:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
//                                   case 0: // Ngẫu nhiên bùa 1h
//                                                        if (player.event.luotNhanBuaMienPhi == 1) {
//                                                            int idItem = Util.nextInt(213, 219);
//                                                            player.charms.addTimeCharms(idItem, 60);
//                                                            Item bua = ItemService.gI().createNewItem((short) idItem);
//                                                            Service.getInstance().sendThongBao(player, "Bạn vừa nhận thưởng " + bua.getName());
//                                                            player.event.luotNhanBuaMienPhi = 0;
//                                                        } else {
//                                                            Service.getInstance().sendThongBao(player, "Hôm nay bạn đã nhận bùa miễn phí rồi!!!");
//                                                        }
//                                                        break;
                                
                                case 0: //shop bùa
                                    
                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                            "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                            + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                            "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng", "Đóng");
                                    break;
                                case 1:
//
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2:
                                    createOtherMenu(player, ConstNpc.MENU_NANG_BONGTAI,
                                            "Ngươi muốn nâng bông tai à",
                                            "Nâng Cấp\nBông Tai", "Mở Chỉ Số\nBông Tai", "Đóng");
                                    break;
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;
                                case 4:
                                    createOtherMenu(player, ConstNpc.SACH_TUYET_KY, "Ta có thể giúp gì cho ngươi ?",
                                            "Đóng thành\nSách cũ",
                                            "Đổi Sách\nTuyệt kỹ",
                                            "Giám định\nSách",
                                            "Tẩy\nSách",
                                            "Nâng cấp\nSách\nTuyệt kỹ",
                                            "Hồi phục\nSách",
                                            "Phân rã\nSách");
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_BONGTAI) {
                            switch (select) {
                                case 0://menu lựa chọn nâng cấp bông tai
                                    createOtherMenu(player, 55,
                                            "Ngươi muốn nâng bông tai à",
                                            "Nâng cấp\nPorata 1", "Nâng cấp\nPorata 2", "Nâng cấp\nPorata 3");
                                    break;
                                case 1:
                                    createOtherMenu(player, 66,
                                            "Ngươi muốn mở chỉ số bông tai à\n"
                                            + "|1|Ngươi chỉ có thể mở chỉ số bông tai Cấp 4\n"
                                            + "|7|Ngươi có muốn thực hiện không?",
                                            "Thực hiện", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 55) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP3);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP4);
                                    break;
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP5);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 66) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.SACH_TUYET_KY) {
                            switch (select) {
                                case 0:
                                    Item trangSachCu = InventoryServiceNew.gI().findItemBag(player, 1556);

                                    Item biaSach = InventoryServiceNew.gI().findItemBag(player, 1552);
                                    if ((trangSachCu != null && trangSachCu.quantity >= 9999) && (biaSach != null && biaSach.quantity >= 1)) {
                                        createOtherMenu(player, ConstNpc.DONG_THANH_SACH_CU,
                                                "|2|Chế tạo Cuốn sách cũ\n"
                                                + "|1|Trang sách cũ " + trangSachCu.quantity + "/9999\n"
                                                + "Bìa sách " + biaSach.quantity + "/1\n"
                                                + "Tỉ lệ thành công: 20%\n"
                                                + "Thất bại mất 99 trang sách và 1 bìa sách", "Đồng ý", "Từ chối");
                                        break;
                                    } else {
                                        String NpcSay = "|2|Chế tạo Cuốn sách cũ\n";
                                        if (trangSachCu == null) {
                                            NpcSay += "|7|Trang sách cũ " + "0/9999\n";
                                        } else {
                                            NpcSay += "|1|Trang sách cũ " + trangSachCu.quantity + "/9999\n";
                                        }
                                        if (biaSach == null) {
                                            NpcSay += "|7|Bìa sách " + "0/1\n";
                                        } else {
                                            NpcSay += "|1|Bìa sách " + biaSach.quantity + "/1\n";
                                        }

                                        NpcSay += "|7|Tỉ lệ thành công: 20%\n";
                                        NpcSay += "|7|Thất bại mất 99 trang sách và 1 bìa sách";
                                        createOtherMenu(player, ConstNpc.DONG_THANH_SACH_CU_2,
                                                NpcSay, "Từ chối");
                                        break;
                                    }
                                case 1:
                                    Item cuonSachCu = InventoryServiceNew.gI().findItemBag(player, 1555);
                                    Item kimBam = InventoryServiceNew.gI().findItemBag(player, 1553);

                                    if ((cuonSachCu != null && cuonSachCu.quantity >= 10) && (kimBam != null && kimBam.quantity >= 1)) {
                                        createOtherMenu(player, ConstNpc.DOI_SACH_TUYET_KY,
                                                "|2|Đổi sách tuyệt kỹ 1\n"
                                                + "|1|Cuốn sách cũ " + cuonSachCu.quantity + "/10\n"
                                                + "Kìm bấm giấy " + kimBam.quantity + "/1\n"
                                                + "Tỉ lệ thành công: 20%\n", "Đồng ý", "Từ chối");
                                        break;
                                    } else {
                                        String NpcSay = "|2|Đổi sách Tuyệt kỹ 1\n";
                                        if (cuonSachCu == null) {
                                            NpcSay += "|7|Cuốn sách cũ " + "0/10\n";
                                        } else {
                                            NpcSay += "|1|Cuốn sách cũ " + cuonSachCu.quantity + "/10\n";
                                        }
                                        if (kimBam == null) {
                                            NpcSay += "|7|Kìm bấm giấy " + "0/1\n";
                                        } else {
                                            NpcSay += "|1|Kìm bấm giấy " + kimBam.quantity + "/1\n";
                                        }
                                        NpcSay += "|7|Tỉ lệ thành công: 20%\n";
                                        createOtherMenu(player, ConstNpc.DOI_SACH_TUYET_KY_2,
                                                NpcSay, "Từ chối");
                                    }
                                    break;
                                case 2:// giám định sách
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.GIAM_DINH_SACH);
                                    break;
                                case 3:// tẩy sách
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.TAY_SACH);
                                    break;
                                case 4:// nâng cấp sách
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.NANG_CAP_SACH_TUYET_KY);
                                    break;
                                case 5:// phục hồi sách
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHUC_HOI_SACH);
                                    break;
                                case 6:// phân rã sách
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHAN_RA_SACH);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.DOI_SACH_TUYET_KY) {
                            switch (select) {
                                case 0:
                                    Item cuonSachCu = InventoryServiceNew.gI().findItemBag(player, 1555);
                                    Item kimBam = InventoryServiceNew.gI().findItemBag(player, 1553);

                                    short baseValue = 1544;
                                    short genderModifier = (player.gender == 0) ? -2 : ((player.gender == 2) ? 2 : (short) 0);

                                    Item sachTuyetKy = ItemService.gI().createNewItem((short) (baseValue + genderModifier));

                                    if (Util.isTrue(20, 100)) {

                                        sachTuyetKy.itemOptions.add(new ItemOption(233, 0));
                                        sachTuyetKy.itemOptions.add(new ItemOption(21, 40));
                                        sachTuyetKy.itemOptions.add(new ItemOption(30, 0));
                                        sachTuyetKy.itemOptions.add(new ItemOption(87, 1));
                                        sachTuyetKy.itemOptions.add(new ItemOption(230, 5));
                                        sachTuyetKy.itemOptions.add(new ItemOption(231, 1000));
                                        try { // send effect susscess
                                            Message msg = new Message(-81);
                                            msg.writer().writeByte(0);
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeShort(tempId);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                            msg = new Message(-81);
                                            msg.writer().writeByte(1);
                                            msg.writer().writeByte(2);
                                            msg.writer().writeByte(InventoryServiceNew.gI().getIndexBag(player, kimBam));
                                            msg.writer().writeByte(InventoryServiceNew.gI().getIndexBag(player, cuonSachCu));
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                            msg = new Message(-81);
                                            msg.writer().writeByte(7);
                                            msg.writer().writeShort(sachTuyetKy.template.iconID);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                        } catch (Exception e) {
                                            System.out.println("lỗi 4");
                                        }
                                        InventoryServiceNew.gI().addItemBag(player, sachTuyetKy);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, cuonSachCu, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, kimBam, 1);
                                        InventoryServiceNew.gI().sendItemBags(player);
//                                                    npcChat(player, "Thành công gòi cu ơi");
                                        return;
                                    } else {
                                        try { // send effect faile
                                            Message msg = new Message(-81);
                                            msg.writer().writeByte(0);
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeShort(tempId);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                            msg = new Message(-81);
                                            msg.writer().writeByte(1);
                                            msg.writer().writeByte(2);
                                            msg.writer().writeByte(InventoryServiceNew.gI().getIndexBag(player, kimBam));
                                            msg.writer().writeByte(InventoryServiceNew.gI().getIndexBag(player, cuonSachCu));
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                            msg = new Message(-81);
                                            msg.writer().writeByte(8);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                        } catch (Exception e) {
                                            System.out.println("lỗi 3");
                                        }
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, cuonSachCu, 5);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, kimBam, 1);
                                        InventoryServiceNew.gI().sendItemBags(player);
//                                                    npcChat(player, "Thất bại gòi cu ơi");
                                    }
                                    return;
                                case 1:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.DONG_THANH_SACH_CU) {
                            switch (select) {
                                case 0:

                                    Item trangSachCu = InventoryServiceNew.gI().findItemBag(player, 1556);
                                    Item biaSach = InventoryServiceNew.gI().findItemBag(player, 1552);
                                    Item cuonSachCu = ItemService.gI().createNewItem((short) 1555);
                                    if (Util.isTrue(20, 100)) {
                                        cuonSachCu.itemOptions.add(new ItemOption(30, 0));

                                        try { // send effect susscess
                                            Message msg = new Message(-81);
                                            msg.writer().writeByte(0);
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeShort(tempId);
                                            player.sendMessage(msg);
                                            msg.cleanup();

                                            msg = new Message(-81);
                                            msg.writer().writeByte(1);
                                            msg.writer().writeByte(2);
                                            msg.writer().writeByte(InventoryServiceNew.gI().getIndexBag(player, trangSachCu));
                                            msg.writer().writeByte(InventoryServiceNew.gI().getIndexBag(player, biaSach));
                                            player.sendMessage(msg);
                                            msg.cleanup();

                                            msg = new Message(-81);
                                            msg.writer().writeByte(7);
                                            msg.writer().writeShort(cuonSachCu.template.iconID);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            player.sendMessage(msg);
                                            msg.cleanup();

                                        } catch (Exception e) {
                                            System.out.println("lỗi 1");
                                        }

                                        InventoryServiceNew.gI().addItemList(player.inventory.itemsBag, cuonSachCu, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, trangSachCu, 9999);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, biaSach, 1);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        return;
                                    } else {
                                        try { // send effect faile
                                            Message msg = new Message(-81);
                                            msg.writer().writeByte(0);
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeUTF("test");
                                            msg.writer().writeShort(tempId);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                            msg = new Message(-81);
                                            msg.writer().writeByte(1);
                                            msg.writer().writeByte(2);
                                            msg.writer().writeByte(InventoryServiceNew.gI().getIndexBag(player, biaSach));
                                            msg.writer().writeByte(InventoryServiceNew.gI().getIndexBag(player, trangSachCu));
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                            msg = new Message(-81);
                                            msg.writer().writeByte(8);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            msg.writer().writeShort(-1);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                        } catch (Exception e) {
                                            System.out.println("lỗi 2");
                                        }
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, trangSachCu, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, biaSach, 1);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                    }
                                    return;
                                case 1:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", true);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "BUA_8H", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.LAM_PHEP_NHAP_DA:
                                case CombineServiceNew.NHAP_NGOC_RONG:
                                case CombineServiceNew.PHAN_RA_DO_THAN_LINH:
                                case CombineServiceNew.NANG_CAP_DO_TS:
                                case CombineServiceNew.NANG_CAP_SKH_VIP:
                                case CombineServiceNew.EP_CHUNG_NHAN_XUAT_SU:
                                case CombineServiceNew.GIAM_DINH_SACH:
                                case CombineServiceNew.TAY_SACH:
                                case CombineServiceNew.NANG_CAP_SACH_TUYET_KY:
                                case CombineServiceNew.PHUC_HOI_SACH:
                                case CombineServiceNew.PHAN_RA_SACH:
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                case CombineServiceNew.NANG_CAP_BONG_TAI_CAP3:
                                case CombineServiceNew.NANG_CAP_BONG_TAI_CAP4:
                                case CombineServiceNew.NANG_CAP_BONG_TAI_CAP5:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:

                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }

                    }
                }
            }
        };
    }

    public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    InventoryServiceNew.gI().sendItemBox(player);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
//                    if (mapId == 0 || mapId == 182) {
//                        nguhs.gI().setTimeJoinnguhs();
//                        long now = System.currentTimeMillis();
//                        if (now > nguhs.TIME_OPEN_NHS && now < nguhs.TIME_CLOSE_NHS) {
//                            this.createOtherMenu(player, 0, "Ngũ Hàng Sơn x10 Tnsm\nHỗ trợ cho Ae trên 80 Tỷ SM?\nThời gian từ 20h - 23h59, 250 hồng ngọc 1 lần vào", "OK", "Đóng");
//                        } else {
//                        this.createOtherMenu(player, 0, "Ngũ Hàng Sơn x10 Tnsm\nHỗ trợ cho Ae trên 80 Tỷ SM?\nThời gian từ 20h - 23h59, 250 hồng ngọc 1 lần vào", "OK");
//                    }
//                    }
                    if (mapId == 0) {
                        this.createOtherMenu(player, 0, "|7| Thí chủ muốn đến Ngũ Hành Sơn ...  ?\n"
                                + "|4|Phí Vào : 500 hồng ngọc \n"
                                + "|4|Sức Mạnh Yêu Cầu : 80 tỉ", "Vào Ngay", "Từ chối");
                    }
                    if (mapId == 122) {
                        this.createOtherMenu(player, 0, "Thí Chủ Muốn Quay Trở Lại Làng Aru ?", "OK", "Từ chối");

                    }
                    if (mapId == 124) {
                        this.createOtherMenu(player, 0, "A mi phò phò, thí chủ hãy thu thập bùa 'giải khai phong ấn', mỗi chữ 99 cái.",
                                "Đổi đào chín", "Giải phong ấn", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                    if (mapId == 0 || mapId == 182) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.power < 80000000000L) {
                                    Service.getInstance().sendThongBao(player, "Sức mạnh thí chủ chưa đủ mạnh để vào Ngũ Hành Sơn");
                                    return;
                                } else if (player.inventory.ruby < 500) {
                                    Service.getInstance().sendThongBao(player, "Phí Vào Là 500 hồng ngọc một lần\n bạn không đủ hồng ngọc để vào !");
                                    return;
                                } else {
                                    player.inventory.ruby -= 500;
                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                    ChangeMapService.gI().changeMapInYard(player, 122, -1, -1);
                                }
                                break;
                        }
                    }
                    if (mapId == 122) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapInYard(player, 0, -1, 469);
                        }
                    }
                    if (mapId == 124) {
                        switch (select) {
                            case 0:
                                Item item = null;
                                try {
                                    item = InventoryServiceNew.gI().findItemBag(player, 541);
                                } catch (Exception e) {
                                }
                                if (item == null || item.quantity < 10) {
                                    npcChat(player,
                                            "Cần 10 quả đào xanh để đổi lấy đào chín từ bần tăng.");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    npcChat(player, "Túi đầy rồi kìa.");
                                    return;
                                }
                                Item newItem = ItemService.gI()
                                        .createNewItem((short) ConstNpc.QUA_HONG_DAO_CHIN, 1);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, item, 10);
                                InventoryServiceNew.gI().addItemBag(player, newItem, 0);
                                InventoryServiceNew.gI().sendItemBags(player);
                                npcChat(player,
                                        "Ta đã đổi cho thí chủ rồi đó, hãy mang cho đệ tử ta đi nào.");
                                break;

                            case 1: {
                                Item khai = null;
                                Item giai = null;
                                Item phong = null;
                                Item an = null;

                                try {
                                    khai = InventoryServiceNew.gI().findItemBag(player, 538);
                                    giai = InventoryServiceNew.gI().findItemBag(player, 537);
                                    phong = InventoryServiceNew.gI().findItemBag(player, 539);
                                    an = InventoryServiceNew.gI().findItemBag(player, 540);

                                } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                }
                                if (khai == null || khai.quantity < 99) {
                                    this.npcChat(player, "Bạn không đủ x99 Chữ Khai");
                                } else if (giai == null || giai.quantity < 99) {
                                    this.npcChat(player, "Bạn không đủ x99 Chữ Giải");
                                } else if (phong == null || phong.quantity < 99) {
                                    this.npcChat(player, "Bạn không đủ x99 Chữ Phong");
                                } else if (an == null || an.quantity < 99) {
                                    this.npcChat(player, "Bạn không đủ x99 Chữ Ấn");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, khai, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, giai, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, phong, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, an, 99);

                                    Service.getInstance().sendMoney(player);
//                                    Item hopqua = ItemService.gI().createNewItem((short) 1255);
//                                    InventoryServiceNew.gI().addItemBag(player, hopqua);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    int randomItem = Util.nextInt(2);
                                    if (randomItem == 0) {
                                        Item VatPham = ItemService.gI().createNewItem((short) (547));
                                        VatPham.itemOptions.add(new Item.ItemOption(50, 20));
                                        VatPham.itemOptions.add(new Item.ItemOption(77, 10));
                                        VatPham.itemOptions.add(new Item.ItemOption(103, 10));
                                        VatPham.itemOptions.add(new Item.ItemOption(14, 5));
                                        VatPham.itemOptions.add(new Item.ItemOption(93, 14));
                                        InventoryServiceNew.gI().addItemBag(player, VatPham);
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, "ta tặng cải trang ngộ không", "Ok");
                                    } else if (randomItem == 1) {
                                        Item VatPham = ItemService.gI().createNewItem((short) (548));
                                        VatPham.itemOptions.add(new Item.ItemOption(50, 20));
                                        VatPham.itemOptions.add(new Item.ItemOption(77, 10));
                                        VatPham.itemOptions.add(new Item.ItemOption(103, 10));
                                        VatPham.itemOptions.add(new Item.ItemOption(14, 5));
                                        InventoryServiceNew.gI().addItemBag(player, VatPham);
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, "ta tặng cải trang trư bác dới", "Ok");
                                    } else if (randomItem == 2) {
                                        Item VatPham = ItemService.gI().createNewItem((short) (547));
                                        VatPham.itemOptions.add(new Item.ItemOption(50, 20));
                                        VatPham.itemOptions.add(new Item.ItemOption(77, 10));
                                        VatPham.itemOptions.add(new Item.ItemOption(103, 10));
                                        VatPham.itemOptions.add(new Item.ItemOption(14, 5));
                                        InventoryServiceNew.gI().addItemBag(player, VatPham);
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, "ta tặng cải trang trư bác dới", "Ok");
                                    } else if (randomItem == 3) {
                                        Item VatPham = ItemService.gI().createNewItem((short) (548));
                                        VatPham.itemOptions.add(new Item.ItemOption(50, 20));
                                        VatPham.itemOptions.add(new Item.ItemOption(77, 10));
                                        VatPham.itemOptions.add(new Item.ItemOption(103, 10));
                                        VatPham.itemOptions.add(new Item.ItemOption(14, 5));
                                        VatPham.itemOptions.add(new Item.ItemOption(93, 14));
                                        InventoryServiceNew.gI().addItemBag(player, VatPham);
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, "ta tặng cải trang trư bác dới", "Ok");
                                    }
                                    this.npcChat(player, "Bạn nhận được quà mở phong ấn");
                                }
                                break;
                            }

                        }
                    }

                }
            }
        };
    }

    public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.magicTree.openMenuTree();
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                if (player.magicTree.level == 11) {
                                    player.magicTree.fastRespawnPea();
                                } else {
                                    player.magicTree.showConfirmUpgradeMagicTree();
                                }
                            } else if (select == 2) {
                                player.magicTree.fastRespawnPea();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUpgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                            if (select == 0) {
                                player.magicTree.upgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_UPGRADE:
                            if (select == 0) {
                                player.magicTree.fastUpgradeMagicTree();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUnuppgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                            if (select == 0) {
                                player.magicTree.unupgradeMagicTree();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            private void changeMap() {
                if (this.mapId != 102) {
                    count++;
                    if (this.count >= COUNT_CHANGE) {
                        count = 0;
                        this.map.npcs.remove(this);
                        Map map = MapService.gI().getMapForCalich();
                        if (map != null) {
                            this.mapId = map.mapId;
                            this.cx = Util.nextInt(100, map.mapWidth - 100);
                            this.cy = map.yPhysicInTop(this.cx, 0);
                            this.map = map;
                            this.map.npcs.add(this);
                        }
                    }
                }
            }

            @Override
            public void openBaseMenu(Player player) {
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    Service.gI().hideWaitDialog(player);
                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                    return;
                }
                if (this.mapId != player.zone.map.mapId) {
                    Service.gI().sendThongBao(player, "Calích đã rời khỏi map!");
                    Service.gI().hideWaitDialog(player);
                    return;
                }

                if (this.mapId == 102) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?",
                            "Kể\nChuyện", "Quay về\nQuá khứ");
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?", "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối", "Sát Thương\n Thần Mèo");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 102) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            //kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                        } else if (select == 1) {
                            //về quá khứ
                            ChangeMapService.gI().goToQuaKhu(player);
                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        //kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                    } else if (select == 3) {
                        Service.gI().sendThongBaoOK(player,
                                "Bạn đã gây " + player.damethanmeo + " Sát thương"
                                + "\nBạn đã gây " + player.ptdame + "% Sát thương");
                    } else if (select == 1) {
                        //đến tương lai
                        changeMap();
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_20_0) {
                            Service.gI().sendThongBao(player, "Vui lòng hoàn thành nhiệm vụ trước khi tới đây!");
                            ChangeMapService.gI().goToTL(player);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                }
            }
        };
    }

    public static Npc jaco(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay", "Đến \nPotaufeu");
                    } else if (this.mapId == 139) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        if (player.getSession().player.nPoint.power >= 800000000L) {

                            ChangeMapService.gI().goToPotaufeu(player);
                        } else {
                            this.npcChat(player, "Bạn chưa đủ 800tr sức mạnh để vào!");
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về trạm vũ trụ
                                case 0:
                                    this.npcChat(player, "Chức Năng Tạm Thời Đang Bảo Trì!");
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    //public static Npc Potage(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 149) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "tét", "Gọi nhân bản");
//                    }
//                }
//            }
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                   if (select == 0){
//                        BossManager.gI().createBoss(-214);
//                   }
//                }
//            }
//        };
//    }
//    public static Npc npclytieunuong54(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                createOtherMenu(player, 0, "Trò chơi Chọn ai đây đang được diễn ra, nếu bạn tin tưởng mình đang tràn đầy may mắn thì có thể tham gia thử", "Đóng");
//            }
//
//            @Override
//            public void confirmMenu(Player pl, int select) {
//                if (canOpenNpc(pl)) {
//                    String time = ((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
//                    if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
//                        ChonAiDay.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
//                    }
//                    if (pl.iDMark.getIndexMenu() == 0) {
//                        if (select == 0) {
//                            createOtherMenu(pl, ConstNpc.IGNORE_MENU, "Thời gian giữa các giải là 5 phút\nKhi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn.\nLưu ý: Số thỏi vàng nhận được sẽ bị nhà cái lụm đi 5%!Trong quá trình diễn ra khi đặt cược nếu thoát game mọi phần đặt đều sẽ bị hủy", "Ok");
//                        } else if (select == 1) {
//                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
//                        }
//                    } else if (pl.iDMark.getIndexMenu() == 1) {
//                        if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0) {
//                            switch (select) {
//                                case 0:
//                                            Service.gI().sendThongBao(pl, "Chờ ĐứcNT update nhé!!!");                                    
////                                    createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
////                                    
//                                    break;
//                                case 1: {
//                                    try {
//                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 20) {
//                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 20);
//                                            InventoryServiceNew.gI().sendItemBags(pl);
//                                            pl.goldNormar += 20;
//                                            ChonAiDay.gI().goldNormar += 20;
//                                            ChonAiDay.gI().addPlayerNormar(pl);
//                                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
//                                        } else {
//                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
//
//                                        }
//                                    } catch (Exception ex) {
//                                        java.util.logging.Logger.getLogger(NpcFactory.class
//                                                .getName()).log(Level.SEVERE, null, ex);
//                                    }
//                                }
//                                break;
//
//                                case 2: {
//                                    try {
//                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 200) {
//                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 200);
//                                            InventoryServiceNew.gI().sendItemBags(pl);
//                                            pl.goldVIP += 200;
//                                            ChonAiDay.gI().goldVip += 200;
//                                            ChonAiDay.gI().addPlayerVIP(pl);
//                                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
//                                        } else {
//                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
//                                        }
//                                    } catch (Exception ex) {
////                                            java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
//                                    }
//                                }
//                                break;
//
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }
    public static Npc npclytieunuong99(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
//                            if (canOpenNpc(player)) {
                createOtherMenu(player, ConstNpc.BASE_MENU, "Mini game.", "Kéo\nBúa\nBao", "Con số\nmay mắn\nthỏi vàng", "Con số\nmay mắn\nngọc xanh", "Chọn ai đây", "Đóng");
                return;
//                            }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                String time = ((ChonAiDay_Gold.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                if (((ChonAiDay_Gold.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                    ChonAiDay_Gold.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                }
                String time2 = ((ChonAiDay_Ruby.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                if (((ChonAiDay_Ruby.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                    ChonAiDay_Ruby.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                }
                String time3 = ((ChonAiDay_Gem.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                if (((ChonAiDay_Gem.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                    ChonAiDay_Gem.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                }
//                            if (canOpenNpc(player)) {
//                                if (this.mapId == 5) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0: // kéo, búa, bao
                            // Thêm logic cho trường hợp 0
                            break;
                        case 1: // Con số may mắn vàng
                            xửLýLựaChọnMiniGame_Gold(player);
                            break;
                        case 2:
                            xửLýLựaChọnMiniGame(player);
                            break;
                        case 3: // chọn ai đây
                            createOtherMenu(player, ConstNpc.CHON_AI_DAY, "Trò chơi Chọn Ai Đây đang được diễn ra, nếu bạn tin tưởng mình đang tràn đầy "
                                    + "may mắn thì có thể tham gia thử", "Thể lệ", "Chọn\nVàng", "Chọn\nhồng ngọc", "Chọn\nngọc xanh");
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.CON_SO_MAY_MAN_NGOC_XANH) {
                    xửLýConSoMayManNgocXanh(player, select);
                } else if (player.iDMark.getIndexMenu() == ConstNpc.CON_SO_MAY_MAN_VANG) {
                    xửLýConSoMayManVang(player, select);
                } else if (player.iDMark.getIndexMenu() == ConstNpc.CHON_AI_DAY) {
                    xửLýChonAiDay(player, select, time);
                } else if (player.iDMark.getIndexMenu() == ConstNpc.CHON_AI_DAY_VANG) {
                    xửLýChonAiDayVang(player, select, time);
                } else if (player.iDMark.getIndexMenu() == ConstNpc.CHON_AI_DAY_HONG_NGOC) {
                    xửLýChonAiDayRuby(player, select, time2);
                } else if (player.iDMark.getIndexMenu() == ConstNpc.CHON_AI_DAY_NGOC) {
                    xửLýChonAiDayGem(player, select, time3);
                } else if (player.iDMark.getIndexMenu() == ConstNpc.UPDATE_CHON_AI_DAY_NGOC) {
                    switch (select) {
                        case 0:
                            createOtherMenu(player, ConstNpc.UPDATE_CHON_AI_DAY_NGOC, "Thời gian từ 8h đến hết 21h59 hằng ngày\n"
                                    + "Mỗi lượt được chọn 10 con số từ 0 đến 99\n"
                                    + "Thời gian mỗi lượt là 5 phút", "Cập nhật", "Đóng");
                            break;
                    }
                }
            }
//                            }
//                        }

            // Thêm các phương thức mới để xử lý logic cho mỗi trường hợp
            private void xửLýLựaChọnMiniGame(Player player) {
                LocalTime thoi_gian_hien_tai = LocalTime.now();
                int gio = thoi_gian_hien_tai.getHour();
                int phut = thoi_gian_hien_tai.getMinute();
                String plWin = MiniGame.gI().MiniGame_S1.result_name;
                String KQ = MiniGame.gI().MiniGame_S1.result + "";
                String Money = MiniGame.gI().MiniGame_S1.money + "";
                String count = MiniGame.gI().MiniGame_S1.players.size() + "";
                String second = MiniGame.gI().MiniGame_S1.second + "";
                String number = MiniGame.gI().MiniGame_S1.strNumber((int) player.id);
                StringBuilder previousResults = new StringBuilder("");
                if (MiniGame.gI().MiniGame_S1.dataKQ_CSMM != null && !MiniGame.gI().MiniGame_S1.dataKQ_CSMM.isEmpty()) {
                    int maxResultsToShow = Math.min(10, MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size());
                    for (int i = MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size() - maxResultsToShow; i < MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size(); i++) {
                        previousResults.append(MiniGame.gI().MiniGame_S1.dataKQ_CSMM.get(i));
                        if (i < MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size() - 1) {
                            previousResults.append(",");
                        }
                    }
                }

                String npcSay = ""
                        + "Kết quả giải trước: " + KQ + "\n"
                        + (previousResults.toString() != "" ? previousResults.toString() + "\n" : "")
                        + "Tổng giải thưởng: " + Money + " ngọc\n"
                        + "<" + second + ">giây\n"
                        + (number != "" ? "Các số bạn chọn: " + number : "");
                String[] Menus = {
                    "Cập nhật",
                    "1 Số\n5 ngọc xanh",
                    "Ngẫu nhiên\n1 số lẻ\n5 ngọc xanh",
                    "Ngẫu nhiên\n1 số chẵn\n5 ngọc xanh",
                    "Hướng\ndẫn\nthêm",
                    "Đóng"
                };
                createOtherMenu(player, ConstNpc.CON_SO_MAY_MAN_NGOC_XANH, npcSay, Menus);
                return;
            }

            // Thêm các phương thức mới để xử lý logic cho mỗi trường hợp
            private void xửLýLựaChọnMiniGame_Gold(Player player) {
                LocalTime thoi_gian_hien_tai = LocalTime.now();
                int gio = thoi_gian_hien_tai.getHour();
                int phut = thoi_gian_hien_tai.getMinute();
                String plWin = MiniGame.gI().MiniGame_S1.result_name;
                String KQ = MiniGame.gI().MiniGame_S1.result + "";
                String Money = Util.mumberToLouis(MiniGame.gI().MiniGame_S1.gold) + "";
                String count = MiniGame.gI().MiniGame_S1.players.size() + "";
                String second = MiniGame.gI().MiniGame_S1.second + "";
                String number = MiniGame.gI().MiniGame_S1.strNumber((int) player.id);
                StringBuilder previousResults = new StringBuilder("");
                if (MiniGame.gI().MiniGame_S1.dataKQ_CSMM != null && !MiniGame.gI().MiniGame_S1.dataKQ_CSMM.isEmpty()) {
                    int maxResultsToShow = Math.min(10, MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size());
                    for (int i = MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size() - maxResultsToShow; i < MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size(); i++) {
                        previousResults.append(MiniGame.gI().MiniGame_S1.dataKQ_CSMM.get(i));
                        if (i < MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size() - 1) {
                            previousResults.append(",");
                        }
                    }
                }

                String npcSay = ""
                        + "Kết quả giải trước: " + KQ + "\n"
                        + (previousResults.toString() != "" ? previousResults.toString() + "\n" : "")
                        + "Tổng giải thưởng: " + Money + " thỏi vàng\n"
                        + "<" + second + ">giây\n"
                        + (number != "" ? "Các số bạn chọn: " + number : "");
                String[] Menus = {
                    "Cập nhật",
                    "1 Số\n 1 thỏi vàng",
                    "Ngẫu nhiên\n1 số lẻ\n 1 thỏi vàng",
                    "Ngẫu nhiên\n1 số chẵn\n 1 thỏi vàng",
                    "Hướng\ndẫn\nthêm",
                    "Đóng"
                };
                createOtherMenu(player, ConstNpc.CON_SO_MAY_MAN_VANG, npcSay, Menus);
                return;
            }

            private void xửLýConSoMayManNgocXanh(Player player, int select) {
                switch (select) {
                    case 0: // cập nhật
                        xửLýLựaChọnMiniGame(player);
                        break;
                    case 1: // chọn 1 số
                        Input.gI().createFormConSoMayMan_Gem(player);
                        break;
                    case 2: // chọn 1 số lẻ
                        MiniGame.gI().MiniGame_S1.ramdom1SoLe(player, 1);
                        break;
                    case 3: // chọn 1 số chẵn
                        MiniGame.gI().MiniGame_S1.ramdom1SoChan(player, 1);
                        break;
                    case 4:
                        createOtherMenu(player, 1, "Thời gian từ 8h đến hết 21h59 hằng ngày\n"
                                + "Mỗi lượt được chọn 10 con số từ 0 đến 99\n"
                                + "Thời gian mỗi lượt là 5 phút.", "Đồng ý");
                        break;
                }
            }

            private void xửLýConSoMayManVang(Player player, int select) {
                switch (select) {
                    case 0: // cập nhật
                        xửLýLựaChọnMiniGame_Gold(player);
                        break;
                    case 1: // chọn 1 số
                        Input.gI().createFormConSoMayMan_Gold(player);
                        break;
                    case 2: // chọn 1 số lẻ
                        MiniGame.gI().MiniGame_S1.ramdom1SoLe(player, 0);
                        break;
                    case 3: // chọn 1 số chẵn
                        MiniGame.gI().MiniGame_S1.ramdom1SoChan(player, 0);
                        break;
                    case 4:
                        createOtherMenu(player, 1, "Thời gian từ 8h đến hết 21h59 hằng ngày\n"
                                + "Mỗi lượt được chọn 10 con số từ 0 đến 99\n"
                                + "Thời gian mỗi lượt là 5 phút.", "Đồng ý");
                        break;
                }
            }

            private void xửLýChonAiDay(Player player, int select, String time) {
                switch (select) {
                    case 0:
                        createOtherMenu(player, ConstNpc.IGNORE_MENU, "Mỗi lượt chơi có 6 giải thưởng\n"
                                + "Được chọn tối đa 10 lần mỗi giải\n"
                                + "Thời gian 1 lượt chọn là 5 phút\n"
                                + "Khi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn\n"
                                + "của từng giải và trao thưởng.\n"
                                + "Lưu ý: Nếu tham gia bằng Ngọc Xanh hoặc Hồng ngọc thì người thắng sẽ nhận thưởng là hồng ngọc.", "OK");
                        break;
                    case 1:
                        createOtherMenu(player, ConstNpc.CHON_AI_DAY_VANG, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldNormar) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(0) + "%\n"
                                + "Tổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldVip) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(1) + "%\n"
                                + "Thời gian còn lại: " + time, "Cập nhập", "Thường\n1 triệu\nvàng", "VIP\n10 triệu\nvàng", "Đóng");
                        break;
                    case 2:
                        createOtherMenu(player, ConstNpc.CHON_AI_DAY_HONG_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 hồng\nngọc", "VIP\n100 hồng\nngọc", "Đóng");
                        break;
                    case 3:
                        createOtherMenu(player, ConstNpc.CHON_AI_DAY_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 ngọc\nxanh", "VIP\n100 ngọc\nxanh", "Đóng");
                        break;
                }
            }

            private void xửLýChonAiDayVang(Player player, int select, String time) {
                switch (select) {
                    case 0:
                        createOtherMenu(player, ConstNpc.CHON_AI_DAY_VANG, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldNormar) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldVip) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n1 triệu\nvàng", "VIP\n10 triệu\nvàng", "Đóng");
                        break;
                    case 1:
                        xửLýThuong1TrieuVang(player);
                        break;
                    case 2:
                        xửLýVIP10TrieuVang(player);
                        break;
                }
            }

            private void xửLýChonAiDayRuby(Player player, int select, String time) {
                switch (select) {
                    case 0:
                        createOtherMenu(player, ConstNpc.CHON_AI_DAY_HONG_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 hồng\nngọc", "VIP\n100 hồng\nngọc", "Đóng");
                        break;
                    case 1:
                        xửLýThuong10HongNgoc(player);
                        break;
                    case 2:
                        xửLýVIP100HongNgoc(player);
                        break;
                }
            }

            private void xửLýChonAiDayGem(Player player, int select, String time) {
                switch (select) {
                    case 0:
                        createOtherMenu(player, ConstNpc.CHON_AI_DAY_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 ngọc\nxanh", "VIP\n100 ngọc\nxanh", "Đóng");
                        break;
                    case 1:
                        xửLýThuong10NgocXanh(player);
                        break;
                    case 2:
                        xửLýVIP100NgocXanh(player);
                        break;
                }
            }

            // Thêm các phương thức mới để xử lý logic cho mỗi trường hợp
            private void xửLýThuong1TrieuVang(Player player) {
                try {
                    String time = ((ChonAiDay_Gold.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (((ChonAiDay_Gold.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                        ChonAiDay_Gold.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                    }
                    if (player.inventory.gold >= 1_000_000) {
                        player.inventory.gold -= 1_000_000;
                        Service.gI().sendMoney(player);
                        player.goldNormar += 1_000_000;
                        ChonAiDay_Gold.gI().goldNormar += 1_000_000;
                        ChonAiDay_Gold.gI().addPlayerNormar(player);
                        createOtherMenu(player, ConstNpc.CHON_AI_DAY_VANG, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldNormar) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldVip) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n1 triệu\nvàng", "VIP\n10 triệu\nvàng", "Đóng");
                    } else {
                        Service.gI().sendThongBao(player, "Bạn không đủ vàng");
                    }
                } catch (Exception ex) {
                    System.out.println("Lỗi CHON_AI_DAY_VANG");
                }
            }

            private void xửLýVIP10TrieuVang(Player player) {
                try {
                    String time = ((ChonAiDay_Gold.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (((ChonAiDay_Gold.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                        ChonAiDay_Gold.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                    }
                    if (player.inventory.gold >= 10_000_000) {
                        player.inventory.gold -= 10_000_000;
                        Service.gI().sendMoney(player);
                        player.goldVIP += 10_000_000;
                        ChonAiDay_Gold.gI().goldVip += 10_000_000;
                        ChonAiDay_Gold.gI().addPlayerVIP(player);
                        createOtherMenu(player, ConstNpc.CHON_AI_DAY_VANG, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldNormar) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldVip) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n1 triệu\nvàng", "VIP\n10 triệu\nvàng", "Đóng");
                    } else {
                        Service.gI().sendThongBao(player, "Bạn không đủ vàng");
                    }
                } catch (Exception ex) {
                    System.out.println("Lỗi CHON_AI_DAY_VANG VIP");
                }
            }

            // Thêm các phương thức mới để xử lý logic cho mỗi trường hợp
            private void xửLýThuong10HongNgoc(Player player) {
                try {
                    String time = ((ChonAiDay_Ruby.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (((ChonAiDay_Ruby.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                        ChonAiDay_Ruby.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                    }
                    if (player.inventory.ruby >= 10) {
                        player.inventory.ruby -= 10;
                        Service.gI().sendMoney(player);
                        player.rubyNormar += 10;
                        ChonAiDay_Ruby.gI().rubyNormar += 10;
                        ChonAiDay_Ruby.gI().addPlayerNormar(player);
                        createOtherMenu(player, ConstNpc.CHON_AI_DAY_HONG_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 hồng\nngọc", "VIP\n100 hồng\nngọc", "Đóng");
                    } else {
                        Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc");
                    }
                } catch (Exception ex) {
                    System.out.println("Lỗi CHON_AI_DAY_HONG_NGOC");
                }
            }

            private void xửLýVIP100HongNgoc(Player player) {
                try {
                    String time = ((ChonAiDay_Ruby.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (((ChonAiDay_Ruby.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                        ChonAiDay_Ruby.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                    }
                    if (player.inventory.ruby >= 100) {
                        player.inventory.ruby -= 100;
                        Service.gI().sendMoney(player);
                        player.rubyVIP += 100;
                        ChonAiDay_Ruby.gI().rubyVip += 100;
                        ChonAiDay_Ruby.gI().addPlayerVIP(player);
                        createOtherMenu(player, ConstNpc.CHON_AI_DAY_HONG_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 hồng\nngọc", "VIP\n100 hồng\nngọc", "Đóng");
                    } else {
                        Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc");
                    }
                } catch (Exception ex) {
                    System.out.println("Lỗi CHON_AI_DAY_HONG_NGOC VIP");
                }
            }

            // Thêm các phương thức mới để xử lý logic cho mỗi trường hợp
            private void xửLýThuong10NgocXanh(Player player) {
                try {
                    String time = ((ChonAiDay_Gem.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (((ChonAiDay_Gem.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                        ChonAiDay_Gem.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                    }
                    if (player.inventory.gem >= 10) {
                        player.inventory.gem -= 10;
                        Service.gI().sendMoney(player);
                        player.gemNormar += 10;
                        ChonAiDay_Gem.gI().gemNormar += 10;
                        ChonAiDay_Gem.gI().addPlayerNormar(player);
                        createOtherMenu(player, ConstNpc.CHON_AI_DAY_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 ngọc\nxanh", "VIP\n100 ngọc\nxanh", "Đóng");
                    } else {
                        Service.gI().sendThongBao(player, "Bạn không đủ ngọc xanh");
                    }
                } catch (Exception ex) {
                    System.out.println("Lỗi CHON_AI_DAY_NGOC_XANH");
                }
            }

            private void xửLýVIP100NgocXanh(Player player) {
                try {
                    String time = ((ChonAiDay_Gem.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (((ChonAiDay_Gem.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                        ChonAiDay_Gem.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                    }
                    if (player.inventory.gem >= 100) {
                        player.inventory.gem -= 100;
                        Service.gI().sendMoney(player);
                        player.gemVIP += 100;
                        ChonAiDay_Gem.gI().gemVip += 100;
                        ChonAiDay_Gem.gI().addPlayerVIP(player);
                        createOtherMenu(player, ConstNpc.CHON_AI_DAY_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 ngọc\nxanh", "VIP\n100 ngọc\nxanh", "Đóng");
                    } else {
                        Service.gI().sendThongBao(player, "Bạn không đủ ngọc xanh");
                    }
                } catch (Exception ex) {
                    System.out.println("Lỗi CHON_AI_DAY_NGOC_XANH VIP");

                }
            }
        };
    }

    public static Npc npclytieunuong54(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                createOtherMenu(player, 0, "\b|2|TRÒ CHƠI TÀI XỈU\n"
                        + "\n|7| Kết quả từ của tổng 3 xúc sắc từ 3 -> 10 (Xỉu)\n"
                        + "\n|7| Còn kết quả lớn hơn hoặc bằng 11 sẽ là   (Tài)\n"
                        + "\n\n|4|(Điều kiện tham gia : Có đủ số dư tối thiểu là 25.000 Hồng Ngọc)\n\n|2|Đặt tối thiểu: 20.000 HN\n Tối đa: 1.000.000 HN \n "
                        + "\n|4| Lưu ý : Thoát game khi chốt Kết quả sẽ Mất Tiền Cược và Tiền Thưởng", "Hướng Dẫn Chơi", "Tham gia");
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    String time = ((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (pl.iDMark.getIndexMenu() == 0) {
                        if (select == 0) {
                            createOtherMenu(pl, ConstNpc.IGNORE_MENU, "|5|Có 2 nhà cái Tài và Xĩu, bạn chỉ được chọn 1 nhà để tham gia"
                                    + "\n\n|6|Sau khi kết thúc thời gian đặt cược. Hệ thống sẽ tung xí ngầu để biết kết quả Tài Xỉu"
                                    + "\n\nNếu Tổng số 3 con xí ngầu <=10 : XỈU\nNếu Tổng số 3 con xí ngầu >10 : TÀI\nNếu 3 Xí ngầu cùng 1 số : TAM HOA (Nhà cái lụm hết)"
                                    + "\n\n|7|Lưu ý: Số Hồng Ngọc nhận được sẽ bị nhà cái trừ  đi 20%. Thắng sẽ x2 số hồng ngọc. Trong quá trình diễn ra khi đặt cược nếu thoát game trong lúc phát thưởng phần quà sẽ bị HỦY", "Ok");
                        } else if (select == 1) {
                            if (TaiXiu.gI().baotri == false) {
                                if (pl.goldTai == 0 && pl.goldXiu == 0) {
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU NGỌC RỒNG---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đặt Tài", "Đặt Xỉu", "Đóng");
                                } else if (pl.goldTai > 0) {
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU NGỌC RỒNG---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đặt Tài", "Đặt Xỉu", "Đóng");
                                } else {
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU NGỌC RỒNG---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đặt Tài", "Đặt Xỉu", "Đóng");
                                }
                            } else {
                                if (pl.goldTai == 0 && pl.goldXiu == 0) {
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU NGỌC RỒNG---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đặt Tài", "Đặt Xỉu", "Đóng");
                                } else if (pl.goldTai > 0) {
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU NGỌC RỒNG---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đặt Tài", "Đặt Xỉu", "Đóng");
                                } else {
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU NGỌC RỒNG---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(pl.goldXiu) + " Hồng Ngọc" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                }
                            }
                        }
                    } else if (pl.iDMark.getIndexMenu() == 1) {
                        if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai == 0 && pl.goldXiu == 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU NGỌC RỒNG---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Đặt Xỉu", "Đóng");
                                    break;
                                case 1:
                                    if (pl.inventory.ruby >= 25000) {
                                        Input.gI().TAI_taixiu(pl);
                                    } else {
                                        Service.gI().sendThongBao(pl, "Số dư của bạn cần ít nhất 25.000 để tham gia đánh tài Tài");
                                    }
                                    break;
                                case 2:
                                    if (pl.inventory.ruby >= 25000) {
                                        Input.gI().XIU_taixiu(pl);
                                    } else {
                                        Service.gI().sendThongBao(pl, "Số dư của bạn cần ít nhất 25.000 để tham gia đánh tài xỉu");
                                    }
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai > 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU NGỌC RỒNG---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu > 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU NGỌC RỒNG---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai > 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU NGỌC RỒNG---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu > 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU NGỌC RỒNG---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu == 0 && pl.goldTai == 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU NGỌC RỒNG---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");

                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
//    public static Npc npclytieunuong98(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player pl) {
//                String plWin = MiniGame.gI().MiniGame_S1.result_name;
//                            String KQ = MiniGame.gI().MiniGame_S1.result + "";
//                            String Money = MiniGame.gI().MiniGame_S1.money + "";
//                            String count = MiniGame.gI().MiniGame_S1.players.size() + "";
//                            String second = MiniGame.gI().MiniGame_S1.second + "";
//                            String number = MiniGame.gI().MiniGame_S1.strNumber(pl.id);
//                            String npcSay = ""
//                                    + "Kết quả giải trước: " + KQ + "\n"
//                                    + (plWin != null ? "Thắng giải trước: " + plWin + "\n" : "")
//                                    + "Tham gia: " + count + " tổng giải thưởng: " + Money + " ngọc\n"
//                                    + "<" + second + ">giây\n"
//                                    + (number != "" ? "Các số bạn chọn: " + number : "");
//                            String[] Menus = {
//                                "1 Số\n50\nngọc ",
//                                "Hướng\ndẫn\nthêm",
//                                "Đóng"
//                            };
//                            createOtherMenu(pl, 0, npcSay, Menus);
//                        }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                            switch (select) {
//                                case 0:
//                                    if (player.iDMark.getIndexMenu() == 0) {
//                                        player.typeInput = 20;
//                                        Service.gI().sendInputText(player, "Hãy chọn 1 số từ " + MiniGame.gI().MiniGame_S1.min + "-" + MiniGame.gI().MiniGame_S1.max + ", giá 50 thỏi vàng", 1, new int[]{1}, new String[]{"Số bạn chọn"});
//                                    }
//                                    break;
//                                //Service.gI().sendThongBao(player, "Chức năng này đang được bảo trì và phát triển");
//                                //      break;
//                                case 1:
//                                    if (player.iDMark.getIndexMenu() == 0) {
//                                        String npcSay = ""
//                                                + "Thời gian từ " + Setting.TIME_START_GAME + "h đến hết " + (Setting.TIME_END_GAME - 1) + "h59 hàng ngày\n"
//                                                + "Mỗi lượt được chọn 10 con số từ " + MiniGame.gI().MiniGame_S1.min + "-" + MiniGame.gI().MiniGame_S1.max + "\n"
//                                                + "Thời gian mỗi lượt là " + Setting.TIME_MINUTES_GAME + " phút.";
//                                        createOtherMenu(player, 1, npcSay, "Đồng ý");
//                                    }
//                                    break;
//                
//                            
//                        
//                    
//                }
//            }
//        };
//    }

    public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (!canOpenNpc(player)) {
                    return;
                }

                String message;
                if (this.mapId == 141) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hãy nắm lấy tay ta mau!", "Về\nthần điện");
                } else {
                    if (player.typetrain == 1 && !player.istrain) {
                        message = "Pôpô là đệ tử của ta, luyện tập với Pôpô con sẽ có nhiều kinh nghiệm đánh bại được Pôpô ta sẽ dạy võ công cho con";
                    } else if (player.typetrain == 2 && player.istrain) {
                        message = "Từ nay con sẽ là đệ tử của ta. Ta sẽ truyền cho con tất cả tuyệt kĩ";
                    } else if (player.typetrain == 1 && player.istrain) {
                        message = "Pôpô là đệ tử của ta, luyện tập với Pôpô con sẽ có nhiều kinh nghiệm đánh bại được Pôpô ta sẽ dạy võ công cho con";
                    } else if (player.typetrain == 2 && !player.istrain) {
                        message = "Từ nay con sẽ là đệ tử của ta. Ta sẽ truyền cho con tất cả tuyệt kĩ";
                    } else {
                        message = "Con đã mạnh hơn ta, ta sẽ chỉ đường cho con đến Kaio để gặp thần Vũ Trụ Phương Bắc\nNgài là thần cai quản vũ trụ này, hãy theo ngài ấy học võ công";
                    }

                    if (player.typetrain == 1 && !player.istrain) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động", "Tập luyện với Mr.PôPô", "Thách đấu Mr.PôPô", "Đến Kaio", "Quay số\nmay mắn");
                    } else if (player.typetrain == 2 && player.istrain) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động", "Tập luyện với Mr.PôPô", "Thách đấu\nvới thượng đế", "Đến Kaio", "Quay số\nmay mắn");
                    } else if (player.typetrain == 1 && player.istrain) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động", "Tập luyện với Mr.PôPô", "Thách đấu Mr.PôPô", "Đến Kaio", "Quay số\nmay mắn");
                    } else if (player.typetrain == 2 && !player.istrain) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động", "Tập luyện\nvới Mr.PôPô", "Thách đấu\nvới thượng đế", "Đến Kaio", "Quay số\nmay mắn");
                    } else {
                        if (!player.istrain) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động", "Tập luyện với Mr.PôPô", "Tập luyện với thượng đế", "Đến Kaio", "Quay số\nmay mắn");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động", "Tập luyện với Mr.PôPô", "Tập luyện với thượng đế", "Đến Kaio", "Quay số\nmay mắn");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player) && this.mapId == 45) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            if (!player.istrain) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE, "Đăng ký để mỗi khi Offline quá 30 phút, con sẽ được tự động luyện tập với tốc độ " + player.nPoint.getexp() + " sức mạnh mỗi phút", "Hướng dẫn thêm", "Đồng ý 500 ngọc mỗi lần", "Không đồng ý");
                            } else {
                                player.istrain = false;
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con đã hủy thành công đăng ký tập tự động", "Đóng");
                            }
                        } else if (select == 1) {
                            this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY0, "Con có chắc muốn tập luyện?\nTập luyện với " + player.nPoint.getNameNPC(player, this, (byte) select) + " sẽ tăng " + player.nPoint.getExpbyNPC(player, this, (byte) select) + " sức mạnh mỗi phút", "Đồng ý luyện tập", "Không đồng ý");
                        } else if (select == 2) {
                            if (player.typetrain > 2) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY1, "Con có chắc muốn tập luyện?\nTập luyện với " + player.nPoint.getNameNPC(player, this, (byte) select) + " sẽ tăng " + player.nPoint.getExpbyNPC(player, this, (byte) select) + " sức mạnh mỗi phút", "Đồng ý luyện tập", "Không đồng ý");
                            } else if (player.typetrain == 1) {
                                player.setfight((byte) 1, (byte) 0);
                                player.zone.load_Me_To_Another(player);
                                player.zone.load_Another_To_Me(player);

                            } else {

//                                ChangeMapService.gI().changeMap(player, 49, 0, 578, 456);
                                player.setfight((byte) 1, (byte) 1);
                                try {
                                    new Thuongde(BossID.THUONG_DE, BossesData.THUONG_DE, player.zone, player.location.x, player.location.y);
                                    player.zone.load_Another_To_Me(player);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (select == 3) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                        } else if (select == 4) {
                            this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                    "Con muốn làm gì nào?", "Quay bằng\nvàng", "Quay bằng\nngọc",
                                    "Rương phụ\n("
                                    + (player.inventory.itemsBoxCrackBall.size()
                                    - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                    + " món)",
                                    "Xóa hết\ntrong rương", "Đóng");

                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE) {
                        switch (select) {
                            case 0:
                                Service.gI().sendPopUpMultiLine(player, tempId, this.avartar, ConstNpc.INFOR_TRAIN_OFFLINE);
                                break;
                            case 1:
                                if (player.inventory.ruby < 500) {
                                        Service.gI().sendThongBao(player, "|7|Bạn không đủ 500 Hồng ngọc");
                                        return;
                                    }
                                    player.inventory.ruby -= 500;
                                player.istrain = true;
                                NpcService.gI().createTutorial(player, this.avartar, "Từ giờ, quá 30 phút Offline con sẽ tự động luyện tập");
                                break;
                            case 3:
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY0) {
                        switch (select) {
                            case 0:
                                player.setfight((byte) 0, (byte) 0);
                                player.zone.load_Me_To_Another(player);
                                player.zone.load_Another_To_Me(player);

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY1) {
                        switch (select) {
                            case 0:
                                player.setfight((byte) 1, (byte) 1);
                                ChangeMapService.gI().changeMap(player, 49, 0, 578, 456);
                                try {
                                    new Thuongde(BossID.THUONG_DE, BossesData.THUONG_DE, player.zone, player.location.x - 10, player.location.y);
                                    player.zone.load_Another_To_Me(player);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                        switch (select) {
                            case 0:
                                LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                                break;
                            case 1:
                                LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GEM);
                                break;
                            case 2:
                                ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                break;
                            case 3:
                                NpcService.gI().createMenuConMeo(player,
                                        ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                        "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                        + "sẽ không thể khôi phục!",
                                        "Đồng ý", "Hủy bỏ");
                                break;
                        }
                    }
                }
                if (this.mapId == 141) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapInYard(player, 45, 0, 408);
                                Service.gI().sendThongBao(player, "Hãy xuống dưới gặp thần\nmèo Karin");
                                player.clan.gobosscdrd = true;
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    String message;
                    if (this.mapId == 48) {
                        if (player.typetrain == 3 && !player.istrain) {
                            message = "Thượng đế đưa người đến đây, chắc muốn ta dạy võ chứ gì\nBắt được con khỉ Bubbles rồi hãy tính";
                        } else if (player.typetrain == 4 && player.istrain) {
                            message = "Ta là Thần Vũ Trụ Phương Bắc cai quản khu vực Bắc Vũ Trụ\nnếu thắng được ta\nngươi sẽ đến lãnh địa Kaio, nơi ở của thần linh ";
                        } else if (player.typetrain == 3 && player.istrain) {
                            message = "Thượng đế đưa người đến đây, chắc muốn ta dạy võ chứ gì\nBắt được con khỉ Bubbles rồi hãy tính";
                        } else if (player.typetrain == 4 && !player.istrain) {
                            message = "Ta là Thần Vũ Trụ Phương Bắc cai quản khu vực Bắc Vũ Trụ\nnếu thắng được ta\nngươi sẽ đến lãnh địa Kaio, nơi ở của thần linh ";
                        } else {
                            message = "Con mạnh nhất phía bắc vũ trụ này rồi đấy nhưng ngoài vũ trụ bao la kia vẫn có những kẻ mạnh hơn nhiều\ncon cần phải tập luyện để mạnh hơn nữa";
                        }

                        if (player.typetrain == 3 && !player.istrain) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động", "Tập luyện với Khỉ Bubbles", "Thách đấu Khỉ Bubbles", "Di chuyển");
                        } else if (player.typetrain == 4 && player.istrain) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động", "Tập luyện với Khỉ Bubbles", "Thách đấu\nvới Thần\nVũ Trụ", "Di chuyển");
                        } else if (player.typetrain == 3 && player.istrain) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động", "Tập luyện với Khỉ Bubbles", "Thách đấu Khỉ Bubbles", "Di chuyển");
                        } else if (player.typetrain == 4 && !player.istrain) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động", "Tập luyện\nvới Khỉ Bubbles", "Thách đấu\nvới Thần\nVũ Trụ", "Di chuyển");
                        } else {
                            if (!player.istrain) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động", "Tập luyện với Khỉ Bubbles", "Tập luyện \nvới Thần\nVũ Trụ", "Di chuyển");
                            } else {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động", "Tập luyện với Khỉ Bubbles", "Tập luyện\nvới Thần\nVũ Trụ", "Di chuyển");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                if (!player.istrain) {
                                    this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE, "Đăng ký để mỗi khi Offline quá 30 phút, con sẽ được tự động luyện tập với tốc độ " + player.nPoint.getexp() + " sức mạnh mỗi phút", "Hướng dẫn thêm", "Đồng ý 1200 ngọc mỗi lần", "Không đồng ý");
                                } else {
                                    player.istrain = false;
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con đã hủy thành công đăng ký tập tự động", "Đóng");
                                }
                            } else if (select == 1) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY0, "Con có chắc muốn tập luyện?\nTập luyện với " + player.nPoint.getNameNPC(player, this, (byte) select) + " sẽ tăng " + player.nPoint.getExpbyNPC(player, this, (byte) select) + " sức mạnh mỗi phút", "Đồng ý luyện tập", "Không đồng ý");
                            } else if (select == 2) {
                                if (player.typetrain > 4) {
                                    this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY1, "Con có chắc muốn tập luyện?\nTập luyện với " + player.nPoint.getNameNPC(player, this, (byte) select) + " sẽ tăng " + player.nPoint.getExpbyNPC(player, this, (byte) select) + " sức mạnh mỗi phút", "Đồng ý luyện tập", "Không đồng ý");
                                } else if (player.typetrain == 3) {
                                    player.setfight((byte) 1, (byte) 0);
                                    player.zone.load_Me_To_Another(player);
                                    player.zone.load_Another_To_Me(player);

                                } else {
                                    player.setfight((byte) 1, (byte) 1);
                                    player.zone.mapInfo(player);
                                    DataGame.updateMap(player.getSession());
                                    try {
                                        new ThanVuTru(BossID.THAN_VUTRU, BossesData.THAN_VU_TRU, player.zone, this.cx, this.cy);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (select == 3) {
                                this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                        "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio", "Con\nđường\nrắn độc", "Từ chối");

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE) {
                            switch (select) {
                                case 0:
                                    Service.gI().sendPopUpMultiLine(player, tempId, this.avartar, ConstNpc.INFOR_TRAIN_OFFLINE);
                                    break;
                                case 1:
                                     if (player.inventory.ruby < 1200) {
                                        Service.gI().sendThongBao(player, "|7|Bạn không đủ 1200 Hồng ngọc");
                                        return;
                                    }
                                    player.inventory.ruby -= 1200;
                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                    player.istrain = true;
                                    NpcService.gI().createTutorial(player, this.avartar, "Từ giờ, quá 30 phút Offline con sẽ tự động luyện tập");
                                    break;
                                case 3:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY0) {
                            switch (select) {
                                case 0:
                                    player.setfight((byte) 0, (byte) 0);
                                    player.zone.load_Me_To_Another(player);
                                    player.zone.load_Another_To_Me(player);

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY1) {
                            switch (select) {
                                case 0:
                                    player.setfight((byte) 1, (byte) 1);
                                    player.zone.mapInfo(player);
                                    DataGame.updateMap(player.getSession());
                                    try {
                                        new ThanVuTru(BossID.THAN_VUTRU, BossesData.THAN_VU_TRU, player.zone, this.cx, this.cy);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 2:
                                    if (player.clan != null) {
                                        if (player.clan.ConDuongRanDoc != null) {
                                            this.createOtherMenu(player, ConstNpc.MENU_OPENED_CDRD,
                                                    "Bang hội của con đang đi con đường rắn độc cấp độ "
                                                    + player.clan.ConDuongRanDoc.level + "\nCon có muốn đi theo không?",
                                                    "Đồng ý", "Từ chối");
                                        } else {

                                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_CDRD,
                                                    "Đây là Con đường rắn độc \nCác con cứ yên tâm lên đường\n"
                                                    + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                    "Chọn\ncấp độ", "Từ chối");
                                        }
                                    } else {
                                        this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_CDRD) {
                            switch (select) {
                                case 0:
                                    if (player.clan.haveGoneConDuongRanDoc) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Bang hội của ngươi đã đi con đường rắn độc lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenConDuongRanDoc, "HH:mm:ss") + " hôm nay. Người mở\n"
                                                + "(" + player.clan.playerOpenConDuongRanDoc.name + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                                        return;
//                                    } else if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
//                                        Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 2 ngày!");
//                                        return;
                                    } else if (player.nPoint.power < ConDuongRanDoc.POWER_CAN_GO_TO_CDRD) {
                                        this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                + Util.numberToMoney(ConDuongRanDoc.POWER_CAN_GO_TO_CDRD));
                                        return;
                                    } else {
                                        ChangeMapService.gI().goToCDRD(player);
                                    }
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_CDRD) {
                            switch (select) {
                                case 0:
                                    if (player.clan.haveGoneConDuongRanDoc) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Bang hội của ngươi đã đi con đường rắn độc lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenConDuongRanDoc, "HH:mm:ss") + " hôm nay. Người mở\n"
                                                + "(" + player.clan.playerOpenConDuongRanDoc.name + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                                        return;
//                                    } else if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
//                                        Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 2 ngày!");
//                                        return;
                                    } else if (player.nPoint.power < ConDuongRanDoc.POWER_CAN_GO_TO_CDRD) {
                                        this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                + Util.numberToMoney(ConDuongRanDoc.POWER_CAN_GO_TO_CDRD));
                                        return;
                                    } else {
                                        Input.gI().createFormChooseLevelCDRD(player);
                                    }
                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_CDRD) {
                            switch (select) {
                                case 0:
                                    ConDuongRanDocService.gI().openConDuongRanDoc(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                    break;
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc TosuKaio(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    String message;
                    if (this.mapId == 50) {
                        if (player.typetrain >= 5) {
                            message = "Tập luyện với Tổ sư Kaio sẽ tăng " + player.nPoint.getexp() + " sức mạnh mỗi phút, con có muốn đăng ký không?";
                            if (!player.istrain) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động", "Đồng ý\nluyện tập", "Không đồng ý");
                            } else {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động", "Đồng ý\nluyện tập", "Không đồng ý");
                            }
                        } else if (player.typetrain < 5) {
                            message = "Hãy đánh bại các cao thủ rồi quay lại đây";
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Vâng ạ");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50 && player.typetrain == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                if (!player.istrain) {
                                    this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE, "Đăng ký để mỗi khi Offline quá 30 phút, con sẽ được tự động luyện tập với tốc độ " + player.nPoint.getexp() + " sức mạnh mỗi phút", "Hướng dẫn thêm", "Đồng ý 5000 ngọc mỗi lần", "Không đồng ý");
                                } else {
                                    player.istrain = false;
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con đã hủy thành công đăng ký tập tự động", "Đóng");
                                }
                            } else if (select == 1) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY0, "Con có chắc muốn tập luyện?\nTập luyện với " + player.nPoint.getNameNPC(player, this, (byte) select) + " sẽ tăng " + player.nPoint.getExpbyNPC(player, this, (byte) select) + " sức mạnh mỗi phút", "Đồng ý luyện tập", "Không đồng ý");
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE) {
                            switch (select) {
                                case 0:
                                    Service.gI().sendPopUpMultiLine(player, tempId, this.avartar, ConstNpc.INFOR_TRAIN_OFFLINE);
                                    break;
                                case 1:
                                    if (player.inventory.ruby < 5000) {
                                        Service.gI().sendThongBao(player, "|7|Bạn không đủ 5000 Hồng ngọc");
                                        return;
                                    }
                                    player.inventory.ruby -= 5000;
                                    player.istrain = true;
                                    NpcService.gI().createTutorial(player, this.avartar, "Từ giờ, quá 30 phút Offline con sẽ tự động luyện tập");
                                    break;
                                case 3:
                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY0) {
                            switch (select) {
                                case 0:
                                    player.setfight((byte) 1, (byte) 1);
                                    player.zone.mapInfo(player);
                                    DataGame.updateMap(player.getSession());
                                    try {
                                        new ToSuKaio(BossID.TS_KAIO, BossesData.TO_SU_KAIO, player.zone, this.cx, this.cy);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc kibit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Từ chối");
                    }
                    if (this.mapId == 114) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                    } else if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                    } else if (this.mapId == 155) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else if (this.mapId == 52) {
                        try {
                            MapMaBu.gI().setTimeJoinMapMaBu();
                            if (this.mapId == 52) {
                                long now = System.currentTimeMillis();
                                if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "Đại chiến Ma Bư đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }

                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu osin");
                        }

                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
                            this.createOtherMenu(player, ConstNpc.GO_UPSTAIRS_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Lên Tầng!", "Quay về", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Quay về", "Từ chối");
                        }
                    } else if (this.mapId == 120) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                                case 1:
                                     if (player.nPoint.power < 40000000000L) {
                                    Service.getInstance().sendThongBao(player, "Yêu cầu 40 tỉ sức mạnh");
                                    return;
                                     } else {
                                    
                                    ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                    break;
                            }
                        }
                        }
                    } else if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 1:
     if (player.nPoint.power < 60000000000L) {
                                    Service.getInstance().sendThongBao(player, "Yêu cầu 60 tỉ sức mạnh");
                                    return;
                                     } else {
                                    ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                    break;
                            }
                        }
                        }
                    } else if (this.mapId == 155) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                            }
                        }
                    } else if (this.mapId == 52) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                                break;
                            case ConstNpc.MENU_OPEN_MMB:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
//                                    if (!player.getSession().actived) {
//                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//                                    } else
                                    ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                }
                                break;
                        }
                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
                            if (select == 0) {
                                player.fightMabu.clear();
                                ChangeMapService.gI().changeMap(player, this.map.mapIdNextMabu((short) this.mapId), -1, this.cx, this.cy);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        } else {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    } else if (this.mapId == 120) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc docNhan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn người không vào bang", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai",
                                "OK");
                        return;
                    }

                    boolean flag = true;
                    for (Mob mob : player.zone.mobs) {
                        if (!mob.isDie()) {
                            flag = false;
                        }
                    }
                    for (Player boss : player.zone.getBosses()) {
                        if (!boss.isDie()) {
                            flag = false;
                        }
                    }

                    if (flag) {
                        player.clan.doanhTrai_haveGone = true;
                        player.clan.doanhTrai.setLastTimeOpen(System.currentTimeMillis() + 290_000);
                        player.clan.doanhTrai.DropNgocRong();
                        for (Player pl : player.clan.membersInGame) {
                            ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Doanh trại độc nhãn sắp kết thúc : ", 300);
                            ItemTimeService.gI().removeTextDoanhTrai(pl);
                        }
                        player.clan.doanhTrai.timePickDragonBall = true;
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai",
                                "OK");
                    } else {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy tiêu diệt hết quái và boss trong map", "OK");
                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.getMembers().size() < DoanhTrai.N_PLAYER_CLAN) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội phải có ít nhất 3 thành viên mới có thể mở", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai != null && player.clanMember.getNumDateFromJoinTimeToToday() >= 2 && !player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                                + "Thời gian còn lại là "
                                + TimeUtil.getMinLeft(player.clan.doanhTrai.getLastTimeOpen(), DoanhTrai.TIME_DOANH_TRAI / 1000)
                                + " Phút" + ". Ngươi có muốn tham gia không?",
                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    int nPlSameClan = 10;
                    for (Player pl : player.zone.getPlayers()) {
                        if (!pl.equals(player) && pl.clan != null
                                && pl.clan.equals(player.clan) && pl.location.x >= 1285
                                && pl.location.x <= 1645) {
                            nPlSameClan++;
                        }
                    }
                    if (nPlSameClan < DoanhTrai.N_PLAYER_MAP) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi phải có ít nhất " + DoanhTrai.N_PLAYER_MAP + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                + "tuy nhiên ta khuyên ngươi nên đi cùng với 1 người để khỏi chết.\n"
                                + "Hahaha.", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
//                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 1 || (player.clan.doanhTrai != null && player.clanMember.getNumDateFromJoinTimeToToday() < 1)) {
//                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Doanh trại chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
//                                "OK", "Hướng\ndẫn\nthêm");
//                        return;
//                    }

//                    if (!player.clan.doanhTrai_haveGone) {
//                        player.clan.doanhTrai_haveGone = (new java.sql.Date(player.clan.doanhTrai_lastTimeOpen)).getDay() == (new java.sql.Date(System.currentTimeMillis())).getDay();
//                    }
//                    if (player.clan.doanhTrai_haveGone) {
//                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Bang hội của ngươi đã đi trại lúc " + TimeUtil.formatTime(player.clan.doanhTrai_lastTimeOpen, "HH:mm:ss") + " hôm nay. Người mở\n"
//                                + "(" + player.clan.doanhTrai_playerOpen + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
//                        return;
//                    }
                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                            "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                            + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (!player.getSession().actived) {
                                Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                            } else if (select == 0) {
                                try {
                                    DoanhTraiService.gI().joinDoanhTrai(player);
                                } catch (Exception ex) {
                                    java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    private static Npc popo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
//                if (player.clanMember.getNumDateFromJoinTimeToToday() < 1 && player.clan != null) {
//                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Map Khí Gas chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
//                                "OK", "Hướng\ndẫn\nthêm");
//                        return;
//                    }
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.getSession().is_gift_box) {
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?", "Giải tán bang hội", "Nhận quà\nđền bù");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Thượng đế vừa phát hiện 1 loại khí đang âm thầm\nhủy diệt mọi mầm sống trên Trái Đất,\nnó được gọi là Destron Gas.\nTa sẽ đưa các cậu đến nơi ấy, các cậu sẵn sàng chưa?", "Thông Tin Chi Tiết", "OK", "Từ Chối");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 1:
                                if (player.clan != null) {
                                    if (player.clan.khiGas != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_GAS,
                                                "Bang hội của con đang đi DesTroy Gas cấp độ "
                                                + player.clan.khiGas.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else if (player.clan.haveGoneGas) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Bang hội của ngươi đã đi Khí gas hủy diệt lúc " + TimeUtil.formatTime(player.clan.timeOpenKhiGas, "HH:mm:ss") + " hôm nay. Người mở\n"
                                                + "(" + player.clan.playerOpenKhiGas.name + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                                        return;
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_GAS,
                                                "Khí Gas Huỷ Diệt đã chuẩn bị tiếp nhận các đợt tấn công của quái vật\n"
                                                + "các con hãy giúp chúng ta tiêu diệt quái vật \n"
                                                + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
//                            case 2:
//                                Clan clan = player.clan;
//                                if (clan != null) {
//                                    ClanMember cm = clan.getClanMember((int) player.id);
//                                    if (cm != null) {
//                                        if (clan.members.size() > 1) {
//                                            Service.gI().sendThongBao(player, "Bang phải còn một người");
//                                            break;
//                                        }
//                                        if (!clan.isLeader(player)) {
//                                            Service.gI().sendThongBao(player, "Phải là bảng chủ");
//                                            break;
//                                        }
////                                        
//                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
//                                                "Yes you do!", "Từ chối!");
//                                    }
//                                    break;
//                                }
//                                Service.gI().sendThongBao(player, "Có bang hội đâu ba!!!");
//                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_GAS) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
                                    ChangeMapService.gI().goToGas(player);
                                } else if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
                                    Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 2 ngày!");
                                    return;
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_GAS) {
                        switch (select) {
                            case 0:

                                if (player.clan.haveGoneGas) {
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Bang hội của ngươi đã đi con đường rắn độc lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenConDuongRanDoc, "HH:mm:ss") + " hôm nay. Người mở\n"
                                            + "(" + player.clan.playerOpenConDuongRanDoc.name + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                                    return;
                                } else if (player.nPoint.power < Gas.POWER_CAN_GO_TO_GAS) {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(ConDuongRanDoc.POWER_CAN_GO_TO_CDRD));
                                    return;
                                    } else if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
                                        Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 2 ngày!");
                                        return;
                                } else {
                                    Input.gI().createFormChooseLevelGas(player);
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCPET_GO_TO_GAS) {
                        switch (select) {
                            case 0:
                                GasService.gI().openBanDoKhoBau(player, Integer.parseInt(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc quaTrung(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_AP_TRUNG_NHANH = 1000000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        player.mabuEgg.sendMabuEgg();
                        if (player.mabuEgg.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Burk Burk...",
                                    "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Burk Burk...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
                        }
                    }
                    if (this.mapId == 154) {
                        player.billEgg.sendBillEgg();
                        if (player.billEgg.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Burk Burk...",
                                    "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Burk Burk...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_EGG:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.mabuEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.mabuEgg.sendMabuEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                                "Bạn có chắc chắn cho trứng nở?\n"
                                                + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                                                "Đệ mabư\nTrái Đất", "Đệ mabư\nNamếc", "Đệ mabư\nXayda", "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        break;
                                    case 1:
                                        player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                        break;
                                    case 2:
                                        player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_EGG:
                                if (select == 0) {
                                    player.mabuEgg.destroyEgg();
                                }
                                break;
                        }
                    }
                    if (this.mapId == 154) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_BILL:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Bill?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.billEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.billEgg.sendBillEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_BILL,
                                                "Bạn có chắc chắn cho trứng nở?\n"
                                                + "Đệ tử của bạn sẽ được thay thế bằng đệ Bill",
                                                "Đệ Bill\nTrái Đất", "Đệ Bill\nNamếc", "Đệ Bill\nXayda", "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng Bill?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_BILL:
                                switch (select) {
                                    case 0:
                                        player.billEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        break;
                                    case 1:
                                        player.billEgg.openEgg(ConstPlayer.NAMEC);
                                        break;
                                    case 2:
                                        player.billEgg.openEgg(ConstPlayer.XAYDA);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_BILL:
                                if (select == 0) {
                                    player.billEgg.destroyEgg();
                                }
                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                        "Bản thân", "Đệ tử");
//                        "Bản nguyên", "Hợp nhất", "Từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                            + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Sức mạnh của con đã đạt tới giới hạn",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                "Đóng");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                                }
                                //giới hạn đệ tử
                                break;
                            case 2:
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.THUC_TINH_DT);
                                break;
                            case 3:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_HOP_NHAT,
                                                "|3|CHÀO CON!\n|5|--CON CÓ MUỐN HỢP NHẤT VỚI ĐỆ TỬ--"
                                                + "\n|1|ĐỂ GIA TĂNG SỨC MẠNH BẢN THÂN HAY KHÔNG?"
                                                + "\n|1|CON SẼ MẤT 30 TỈ SỨC MẠNH VÀ ĐỆ TỬ CỦA CON!!!"
                                                + "\n|1|CHỈ SỐ GỐC SẼ SẼ ĐƯỢC CỘNG NGẪU NHIÊN CHO CON"
                                                + "\n|7|Yêu Cầu:" + Util.getFormatNumber(player.pet.nPoint.power)
                                                + "/" + "100.000.000.000 sức mạnh",
                                                "HI SINH ĐỆ TỬ", "từ chối");
                                    }
                                }
                                //giới hạn đệ tử
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {
                            case CombineServiceNew.THUC_TINH_DT:
                                switch (select) {
                                    case 0:
                                        CombineServiceNew.gI().thuctinhDT(player, 1);
                                        System.out.print("test");
                                        break;
                                    case 1:
                                        CombineServiceNew.gI().thuctinhDT(player, 10);
                                        break;
                                    case 2:
                                        CombineServiceNew.gI().thuctinhDT(player, 100);
                                        break;
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_HOP_NHAT) {
                        switch (select) {
                            case 0:
                                Item dt = null;

                                try {
                                    dt = InventoryServiceNew.gI().findItemBag(player, 1763);

                                } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                }
                                if (dt == null || dt.quantity < 1) {
                                    Service.gI().sendThongBao(player, "|2|CHỨC NĂNG YÊU CẦU CÓ 3 THẺ CHUYỂN SINH ĐỆ TỬ <>!");
                                } else if (player.pet.nPoint.power < 100000000000L) {
                                    Service.gI().sendThongBao(player, "|2|YÊU CẦU ĐỆ TỬ PHẢI 100 TỈ SM NHÉ !");
                                } else if (player.nPoint.power < 100000000000L) {
                                    Service.gI().sendThongBao(player, "|2|YÊU CẦU SƯ PHỤ TRIÊN 100 TỈ  TỈ SM NHÉ !");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, dt, 1);
                                    // PetService.gI().createNormalPet(player);   tạo pett mới thì bật lên ko thì tắt đi 
                                    Service.gI().sendMoney(player);
                                    player.nPoint.power -= 30000000000L;
                                    player.pet.nPoint.power = 1200L;
                                    player.nPoint.hpg += Util.nextInt(200, 1500); // chỉ số cộng cho sư phụ 
                                    player.nPoint.dameg += Util.nextInt(50, 300);
                                    player.nPoint.mpg += Util.nextInt(200, 2000);
                                    // chỉ số pet ki bị trừ 
                                    player.pet.nPoint.hpg = Util.nextInt(200, 2000);
                                    player.pet.nPoint.mpg = Util.nextInt(200, 2000);
                                    player.pet.nPoint.dameg = Util.nextInt(10, 200);
                                    player.pet.nPoint.critg = Util.nextInt(1, 5);
                                    player.pet.nPoint.defg = Util.nextInt(10, 309);
                                    player.pet.nPoint.critg = 1;
                                    player.pet.nPoint.defg = 100;
                                    player.pet.playerSkill.skills.get(1).skillId = -1;
                                    player.pet.playerSkill.skills.get(2).skillId = -1;
                                    player.pet.playerSkill.skills.get(4).skillId = -1;
                                    player.pet.playerSkill.skills.get(3).skillId = -1;
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "|2| ĐỆ NHÀ NGƯƠI ĐÃ BỊ T RÚT MÁU HAHA 🤣 CHỈ \n chỉ số nhà người đã dc cộng ngẫu nghiên ");
                                    //  Client.gI().kickSession(player.getSession());
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                            case 0:
                                OpenPowerService.gI().openPowerBasic(player);
                                break;
                            case 1:
                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.gI().sendMoney(player);
                                    }
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ vàng để mở, còn thiếu "
                                            + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.gI().sendMoney(player);
                                }
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Bạn không đủ vàng để mở, còn thiếu "
                                        + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc Gojo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến Map Leo Tháp");
                    } else if (this.mapId == 181) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi muốn tiếp tục leo tháp chứ!\n Tháp hiện tại của ngươi là :" + player.capboss,
                                "Thách Đấu", "Xem Top Lep Tháp", "Về Đảo Kame", "Từ chối");
                    }
                }
            }

            @Override

            public void confirmMenu(Player player, int select
            ) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        if (this.mapId == 181) {
                            switch (select) {
                                case 0:
                                    if (player.inventory.gem < 5) {
                                        this.npcChat(player, "Cần 5 ngọc xanh");
                                        return;
                                    }
                                    if (player.nPoint.hpMax + player.nPoint.dame < 20000) {
                                        this.npcChat(player, "Bạn còn quá yếu vui lòng quay lại sau");
                                        return;
                                    }
                                    Boss oldBossClone = BossManager.gI().getBossById(Util.createIdBossLV(player.id));
                                    if (oldBossClone != null) {
                                        oldBossClone.setDieLv(oldBossClone);
                                        this.npcChat(player, "Ấn thách đấu lại xem!");
                                    } else {
                                        int hp = 0;

//                                double dk = (player.xuatsu + 1) * 10;
                                        int dk = (player.capboss + 1) * 2;
                                        long hptong = (player.nPoint.hpMax + hp) * dk
                                                * (player.capboss >= 5 ? 2 * dk : 1);
                                        BossData bossDataClone = new BossData(
                                                "Yaritobe Super Red [Lv: " + player.capboss + "]",
                                                ConstPlayer.NAMEC,
                                                new short[]{1441, 1442, 1443, player.getFlagBag(), player.idAura,
                                                    player.getEffFront()},
                                                10_000 * dk,
                                                new long[]{10_000_000 * dk},
                                                new int[]{174},
                                                new int[][]{
                                                    {Skill.LIEN_HOAN, 7, 500},
                                                    {Skill.MASENKO, 7, 3000},
                                                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 60000},
                                                    {Skill.BIEN_KHI, 1, 60000}
                                                },
                                                new String[]{"|-2|Ta sẽ tiêu diệt ngươi"}, // text
                                                // chat 1
                                                new String[]{"|-1|Ta Sẽ đập nát đầu ngươi!"}, // text chat 2
                                                new String[]{"|-1|Hẹn người lần sau"}, // text chat 3
                                                1);
                                        try {
                                            new ThachDauGoJo(Util.createIdBossLV(player.id), bossDataClone, player.zone,
                                                    player.name, player.capboss, player);

                                        } catch (Exception e) {
                                            Logger.logException(NpcFactory.class, e);
                                        }
                                        player.inventory.gem -= 5;
                                        Service.gI().sendMoney(player);
                                    }
                                    break;
                                case 1:
                                    Service.gI().showListTop(player, Manager.TopLeoThap);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMap(player, 5, -1, 1043, 168);
                            }
                        } else if (this.mapId == 5) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 181, -1, 513, 480);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaTL(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng", "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                            }
                        }
                    }
//                    else if (this.mapId == 104) {
//                        if (player.iDMark.isBaseMenu()) {
//                            if (select == 0) {
//                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
//                            }
//                        }
//                    }
                }
            }
        };
    }

    public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    BlackBallWar.gI().setTime();
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        try {
                            long now = System.currentTimeMillis();
                            if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW, "Đường đến với ngọc rồng sao đen đã mở, "
                                        + "ngươi có muốn tham gia không?",
                                        "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            } else {
                                String[] optionRewards = new String[7];
                                int index = 0;
                                for (int i = 0; i < 7; i++) {
                                    if (player.rewardBlackBall.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                                        String quantily = player.rewardBlackBall.quantilyBlackBall[i] > 1 ? "x" + player.rewardBlackBall.quantilyBlackBall[i] + " " : "";
                                        optionRewards[index] = quantily + (i + 1) + " sao";
                                        index++;
                                    }
                                }
                                if (index != 0) {
                                    String[] options = new String[index + 1];
                                    for (int i = 0; i < index; i++) {
                                        options[i] = optionRewards[i];
                                    }
                                    options[options.length - 1] = "Từ chối";
                                    this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW, "Ngươi có một vài phần thưởng ngọc "
                                            + "rồng sao đen đây!",
                                            options);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }
                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu rồng Omega");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_REWARD_BDW:
                            player.rewardBlackBall.getRewardSelect((byte) select);
                            break;
                        case ConstNpc.MENU_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            } else if (select == 1) {
//                                if (!player.getSession().actived) {
//                                    Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//
//                                } else
                                player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                ChangeMapService.gI().openChangeMapTab(player);
                            }
                            break;
                        case ConstNpc.MENU_NOT_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            }
                            break;
                    }
                }
            }

        };
    }

    public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isHoldBlackBall()) {
                        this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù hộ", "Từ chối");
                    } else {
                        if (BossManager.gI().existBossOnPlayer(player)
                                || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                                || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối", "Gọi BOSS");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                    "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                    "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                    "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                    "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                    "Từ chối"
                            );
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                        } else if (select == 2) {
                            BossManager.gI().callBoss(player, mapId);
                        } else if (select == 1) {
                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                        if (player.effectSkin.xHPKI > 1) {
                            Service.gI().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                break;
                            case 1:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                break;
                            case 2:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                break;
                            case 3:
                                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc npcThienSu64(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 7) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 0) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 146) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 147) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 148) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
//                }
//                if (this.mapId == 200) {
//                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!", "Hướng Dẫn",
//                            "Đổi Thức Ăn\nLấy Điểm", "Từ Chối");
                }
                if (this.mapId == 154) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!",
                            "Chế Tạo trang bị thiên sứ", "Cửa Hàng\nBán Ấy", "Địt Nhau");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 7) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 146, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 14) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 148, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 0) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 147, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 147) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 148) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 14, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 146) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 450);
                        }
                        if (select == 1) {
                        }

//                    }
//                    if (player.iDMark.isBaseMenu() && this.mapId == 48) {
//                        if (select == 0) {
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "|2| Phân Rã Đồ Thần : 1 món = 1 điểm ");
//                        }
//                        if (select == 1) {
//                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.DOI_DIEM);
//                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {
                            case CombineServiceNew.DOI_DIEM:

                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }

                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 154) {
                        if (select == 0) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_TS);
                        }
                        if (select == 1) {
                            ShopServiceNew.gI().opendShop(player, "WHIS", true);
                        }
                        if (select == 2) {
                            Service.gI().sendThongBaoOK(player, "Hahahah");
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {
                            case CombineServiceNew.CHE_TAO_TRANG_BI_TS:

                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }

                    }
                }
            }

        };
    }

    public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Gặp Whis Để Đổi Thức Ăn Lấy Điểm Sau Đó Gặp Ta Để Mua Trang Bị Hủy Diệt",
                            "Điểm",
                            "Shop Hủy Diệt", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 48:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, "Mày Có " + player.inventory.coupon + " Điểm", "Đóng");
                                    }
                                    if (select == 1) {
                                        if (player.inventory.coupon == 0) {
                                            createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ngươi Không Có Điểm Vui Lòng Đổi Điểm Bằng Thức Ăn", "Đóng");
                                        } else {
                                            ShopServiceNew.gI().opendShop(player, "BILL", false);
                                            break;
                                        }
                                    }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc whis(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 154) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Thử đánh với ta xem nào.\nNgươi còn 1 lượt cơ mà.",
                            "Nói chuyện", "Học tuyệt kỹ", "Thách Đấu Whis", "Top Thách Đấu", "Từ chối");
                } else if (this.mapId == 48) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngươi tìm ta có việc gì?",
                            "Đóng");

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 154) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 5, "Ta sẽ giúp ngươi chế tạo trang bị thiên sứ", "Chế tạo", "Từ chối");
                                break;
                            case 1:
                                Item BiKiepTuyetKy = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1320);
                                if (BiKiepTuyetKy != null) {
                                    if (player.gender == 0) {
                                        this.createOtherMenu(player, 6, "|1|Ta sẽ dạy ngươi tuyệt kỹ Super kamejoko\n" + "|7|Bí kiếp tuyệt kỹ: " + BiKiepTuyetKy.quantity + "/9999\n" + "|2|Giá vàng: 10.000.000\n" + "|2|Giá ngọc: 99",
                                                "Đồng ý", "Từ chối");
                                    }
                                    if (player.gender == 1) {
                                        this.createOtherMenu(player, 6, "|1|Ta sẽ dạy ngươi tuyệt kỹ Ma phông ba\n" + "|7|Bí kiếp tuyệt kỹ: " + BiKiepTuyetKy.quantity + "/9999\n" + "|2|Giá vàng: 10.000.000\n" + "|2|Giá ngọc: 99",
                                                "Đồng ý", "Từ chối");
                                    }
                                    if (player.gender == 2) {
                                        this.createOtherMenu(player, 6, "|1|Ta sẽ dạy ngươi tuyệt kỹ "
                                                + "đíc chưởng liên hoàn\n" + "|7|Bí kiếp tuyệt kỹ: " + BiKiepTuyetKy.quantity + "/9999\n" + "|2|Giá vàng: 10.000.000\n" + "|2|Giá ngọc: 99",
                                                "Đồng ý", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Hãy tìm bí kíp rồi quay lại gặp ta!");
                                }
                                break;
                            case 2:
                                if (player.inventory.gem < 5) {
                                    this.npcChat(player, "Cần 5 ngọc xanh");
                                    return;
                                }
                                if (player.nPoint.hpMax + player.nPoint.dame < 20000) {
                                    this.npcChat(player, "Bạn còn quá yếu vui lòng quay lại sau");
                                    return;
                                }
                                Boss oldBossClone = BossManager.gI().getBossById(Util.createIdBossLV(player.id));
                                if (oldBossClone != null) {
                                    oldBossClone.setDieLv(oldBossClone);
                                    this.npcChat(player, "Ấn thách đấu lại xem!");
                                } else {
                                    int hp = 0;
                                    int dk = (player.thachdauwhis + 1) * 2;
                                    long hptong = (player.nPoint.hpMax + hp) * dk
                                            * (player.thachdauwhis >= 5 ? 2 * dk : 1);
                                    BossData bossDataClone = new BossData(
                                            "Whis [Lv: " + player.thachdauwhis + "]",
                                            ConstPlayer.NAMEC,
                                            new short[]{505, 506, 507, -1, -1, -1},
                                            10_000 * dk,
                                            new long[]{10_000_000 * dk},
                                            new int[]{174},
                                            new int[][]{
                                                {Skill.LIEN_HOAN, 7, 500},
                                                {Skill.KAMEJOKO, 7, 3000},
                                                {Skill.DICH_CHUYEN_TUC_THOI, 7, 60000}
                                            },
                                            new String[]{"|-2|Ta sẽ tiêu diệt ngươi"}, // text
                                            // chat 1
                                            new String[]{"|-1|Ta Sẽ đập nát đầu ngươi!"}, // text chat 2
                                            new String[]{"|-1|Hẹn người lần sau"}, // text chat 3
                                            1);
                                    try {
                                        new ThachDauWhis(Util.createIdBossLV(player.id), bossDataClone, player.zone,
                                                player.name, player.thachdauwhis, player);

                                    } catch (Exception e) {
                                        Logger.logException(NpcFactory.class, e);
                                    }
                                    player.inventory.gem -= 5;
                                    Service.gI().sendMoney(player);
                                }
                                break;
                            case 3:
                                Service.gI().showListTop(player, Manager.TopThachDau);
                                break;
                        }
                    } else if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                            }
                        }
                    } else if (player.iDMark.getIndexMenu() == 5) {
                        switch (select) {
                            // case 0:
                            //    ShopServiceNew.gI().opendShop(player, "THIEN_SU", false);
                            //   break;
                            case 0:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_TS);
                                break;
                        }
                        //   } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO) {
                        //     if (select == 0) {
                        //       CombineServiceNew.gI().startCombine(player);
                    } else if (player.iDMark.getIndexMenu() == 6) {
                        switch (select) {
                            case 0:
                                Item sach = InventoryServiceNew.gI().findItemBag(player, 1320);
                                if (sach != null && sach.quantity >= 9999 && player.inventory.gold >= 10000000 && player.inventory.gem > 99 && player.nPoint.power >= 1000000000L) {

                                    if (player.gender == 2) {
                                        SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                                    }
                                    if (player.gender == 0) {
                                        SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                                    }
                                    if (player.gender == 1) {
                                        SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
                                    }
                                    InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag, sach, 9999);
                                    player.inventory.gold -= 10000000;
                                    player.inventory.gem -= 99;
                                    InventoryServiceNew.gI().sendItemBags(player);
                                } else if (player.nPoint.power < 1000000000L) {
                                    Service.getInstance().sendThongBao(player, "Ngươi không đủ sức mạnh để học tuyệt kỹ");
                                    return;
                                } else if (sach.quantity <= 9999) {
                                    int sosach = 9999 - sach.quantity;
                                    Service.getInstance().sendThongBao(player, "Ngươi còn thiếu " + sosach + " bí kíp nữa.\nHãy tìm đủ rồi đến gặp ta.");
                                    return;
                                } else if (player.inventory.gold <= 10000000) {
                                    Service.getInstance().sendThongBao(player, "Hãy có đủ vàng thì quay lại gặp ta.");
                                    return;
                                } else if (player.inventory.gem <= 99) {
                                    Service.getInstance().sendThongBao(player, "Hãy có đủ ngọc xanh thì quay lại gặp ta.");
                                    return;
                                }

                                break;
                        }
                    }
                }
            }

        };
    }

    public static Npc boMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (this.mapId == 47 || this.mapId == 84) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Chào bạn \btôi có thể giúp bạn làm nhiệm vụ", "Nhiệm vụ\nhàng ngày", "Nhận ngọc\nmiễn phí", "Từ chối");
                        }
//                    if (this.mapId == 47) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Xin chào, cậu muốn tôi giúp gì?", "Từ chối");
//                    }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.playerTask.sideTask.template != null) {
                                        String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName() + " ("
                                                + player.playerTask.sideTask.getLevel() + ")"
                                                + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                                + player.playerTask.sideTask.maxCount + " ("
                                                + player.playerTask.sideTask.getPercentProcess() + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK;
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                "Tôi có vài nhiệm vụ theo cấp bậc, "
                                                + "sức cậu có thể làm được cái nào?",
                                                "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
                                    }
                                    break;
                                case 1:
                                    player.achievement.Show();
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    TaskService.gI().changeSideTask(player, (byte) select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                    TaskService.gI().paySideTask(player);
                                    break;
                                case 1:
                                    TaskService.gI().removeSideTask(player);
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc karin(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        return new Npc(mapId, status, cx, cy, tempId, avatar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player) && this.mapId == 46) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        String message;
                        if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 5) {
                            if (player.istrain) {
                                message = "Muốn chiến thắng Tàu Pảy Pảy phải đánh bại được ta";
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động", "Tập luyện với\nThần Mèo", "Thách đấu với\nThần Mèo");
                            } else {
                                message = "Muốn chiến thắng Tàu Pảy Pảy phải đánh bại được ta";
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động", "Tập luyện với\nThần Mèo", "Thách đấu với\nThần Mèo");
                            }
                        } else if (player.typetrain == 0 && !player.istrain) {
                            message = "Từ giờ Yajirô sẽ luyện tập cùng ngươi. Yajirô đã lên đây đã từng lên đây tập luyện và bây giờ hắn mạnh hơn ta đấy";
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động", "Tập luyện với Yajirô", "Thách đấu Yajirô");
                        } else if (player.typetrain != 0 && player.istrain) {
                            message = "Con hãy bay theo cây Gậy Như Ý trên đỉnh tháp để đến Thần Điện gặp Thượng Đế\nCon rất xứng đáng để làm đệ tự của ông ấy";
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động", "Tập luyện với Yajirô", "Tập luyện với thần mèo");
                        } else if (player.typetrain == 0 && player.istrain) {
                            message = "Từ giờ Yajirô sẽ luyện tập cùng ngươi. Yajirô đã lên đây đã từng lên đây tập luyện và bây giờ hắn mạnh hơn ta đấy";
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động", "Tập luyện với Yajirô", "Thách đấu Yajirô");
                        } else if (player.clan != null && player.clan.ConDuongRanDoc != null && player.clan.gobosscdrd) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hãy cầm lấy hai hạt đậu cuối cùng của ta đây\nCố giữ mình nhé " + player.name, "Cám ơn\nsư phụ");
                            Service.gI().sendThongBao(player, "Hãy mau bay xuống\nchân tháp Karin");
                        } else {
                            message = "Con hãy bay theo cây Gậy Như Ý trên đỉnh tháp để đến Thần Điện gặp Thượng Đế\nCon rất xứng đáng để làm đệ tự của ông ấy";
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động", "Tập luyện với Yajirô", "Tập luyện với thần mèo");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player) && this.mapId == 46) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            if (player.clan != null && player.clan.ConDuongRanDoc != null && player.clan.gobosscdrd) {
                                player.nPoint.setFullHpMp();
                                //                           ChangeMapService.gI().changeMapInYard(player, 144, 0, 131);
                            } else {
                                if (!player.istrain) {
                                    this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE, "Đăng ký để mỗi khi Offline quá 30 phút, con sẽ được tự động luyện tập với tốc độ " + player.nPoint.getexp() + " sức mạnh mỗi phút", "Hướng dẫn thêm", "Đồng ý 200 ngọc mỗi lần", "Không đồng ý");
                                } else {
                                    player.istrain = false;
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con đã hủy thành công đăng ký tập tự động", "Đóng");
                                }
                            }
                        } else if (select == 1) {
                            if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 5) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY0, "Con có chắc muốn tập luyện?\nTập luyện với mèo thần Karin?", "Đồng ý luyện tập", "Không đồng ý");
                            } else {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY0, "Con có chắc muốn tập luyện?\nTập luyện với " + player.nPoint.getNameNPC(player, this, (byte) select) + " sẽ tăng " + player.nPoint.getExpbyNPC(player, this, (byte) select) + " sức mạnh mỗi phút", "Đồng ý luyện tập", "Không đồng ý");
                            }
                        } else if (select == 2) {
                            if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 5) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY1, "Con có chắc muốn thách đấu?\nThách đấu với mèo thần Karin?", "Đồng ý thách đấu", "Không đồng ý");
                            } else if (player.typetrain != 0) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY1, "Con có chắc muốn tập luyện?\nTập luyện với " + player.nPoint.getNameNPC(player, this, (byte) select) + " sẽ tăng " + player.nPoint.getExpbyNPC(player, this, (byte) select) + " sức mạnh mỗi phút", "Đồng ý luyện tập", "Không đồng ý");
                            } else {
                                player.setfight((byte) 1, (byte) 0);
                                player.zone.load_Me_To_Another(player);
                                player.zone.load_Another_To_Me(player);

                            }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE) {
                        switch (select) {
                            case 0:
                                Service.gI().sendPopUpMultiLine(player, tempId, this.avartar, ConstNpc.INFOR_TRAIN_OFFLINE);
                                break;
                            case 1:
                                if (player.inventory.ruby < 1) {
                                        Service.gI().sendThongBao(player, "|7|Bạn không đủ 1 Hồng ngọc");
                                        return;
                                    }
                                    player.inventory.ruby -= 1;
                                player.istrain = true;
                                NpcService.gI().createTutorial(player, this.avartar, "Từ giờ, quá 30 phút Offline con sẽ tự động luyện tập");
                                break;
                            case 3:
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY0) {
                        switch (select) {
                            case 0:
                                if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 5) {
                                    player.setfight((byte) 0, (byte) 1);
                                    player.zone.load_Me_To_Another(player);
                                    player.zone.load_Another_To_Me(player);
                                    player.zone.mapInfo(player);
                                    DataGame.updateMap(player.getSession());
                                    try {
                                        new MeoThan(BossID.MEO_THAN, BossesData.THAN_MEO, player.zone, this.cx, this.cy);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    player.setfight((byte) 0, (byte) 0);
                                    player.zone.load_Me_To_Another(player);
                                    player.zone.load_Another_To_Me(player);
                                }

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY1) {
                        switch (select) {
                            case 0:
                                if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 5) {
                                    player.setfight((byte) 1, (byte) 1);
                                } else {
                                    player.setfight((byte) 0, (byte) 1);
                                }
                                player.zone.load_Me_To_Another(player);
                                player.zone.load_Another_To_Me(player);
                                player.zone.mapInfo(player);
                                DataGame.updateMap(player.getSession());
                                try {
                                    new MeoThan(BossID.MEO_THAN, BossesData.THAN_MEO, player.zone, this.cx, this.cy);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                        }
                    }
                } else if (this.mapId == 104) {
                    if (player.iDMark.isBaseMenu() && select == 0) {
                        ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                    }
                }

            }
        };
    }

    public static Npc vados(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            public void chatWithNpc(Player player) {
                String[] chat = {
                    "|7|Top Máy Chủ",};
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int index = 0;

                    @Override
                    public void run() {
                        npcChat(player, chat[index]);
                        index = (index + 1) % chat.length;
                    }
                }, 1000, 1000);
            }

            @Override
            public void openBaseMenu(Player player) {
                chatWithNpc(player);
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|2|Ta Vừa Hack Được Top Của Toàn Server\b|7|Mi Muống Xem Tóp Gì?",
                                "Tóp Sức Mạnh", "Top Nhiệm Vụ", "Top Sức Đánh");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        switch (select) {
                            case 0:
                                Service.gI().showListTop(player, Manager.topSM);
                                break;
                            case 1:
                                Service.gI().showListTop(player, Manager.topNV);
                                break;
                            case 2:
                                Service.gI().showListTop(player, Manager.topSD);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc NPC_cadic(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            public void chatWithNpc(Player player) {
                String[] chat = {
                    "|7|Top Máy Chủ",};
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int index = 0;

                    @Override
                    public void run() {
                        npcChat(player, chat[index]);
                        index = (index + 1) % chat.length;
                    }
                }, 1000, 1000);
            }

            @Override
            public void openBaseMenu(Player player) {
                chatWithNpc(player);
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|2|Ta Vừa Hack Được Top Của Toàn Server\b|7|Mi Muống Xem Tóp Gì?",
                                "Top\nSức Mạnh", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        switch (select) {
                            case 0:
                                Service.gI().showListTop(player, Manager.topSM);
                                break;
                            case 1:
                                Service.gI().showListTop(player, Manager.topNV);
                                break;
                            case 2:
                                Service.gI().showListTop(player, Manager.topSD);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 80) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Tới hành tinh\nYardart", "Từ chối");
                    } else if (this.mapId == 131) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (this.mapId == 131) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                                }
                            }
                            if (this.mapId == 80) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMap(player, 131, -1, 901, 240);
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc mavuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, tôi có thể giúp gì cho cậu?", "Tây thánh địa", "Từ chối");
                    } else if (this.mapId == 156) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                //đến tay thanh dia
                                ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 360);
                            }
                        }
                    } else if (this.mapId == 156) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về lanh dia bang hoi
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 432);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    ///////////////////////////////////////////NPC Chopper///////////////////////////////////////////
    public static Npc chopper(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1|Êi êi cậu có muốn cùng Chopper đi đến Đảo Kho Báu không,\nnhóm Hải Tặc Mũ Rơm đang chờ đợi cậu đến đó\n Có rất nhiều phần quà mùa hấp dẫn ở đó.\n Đi thôi nào....",
                                "Đi đến\nĐảo Kho Báu", "Chi tiết", "Từ chối");
                    }
                    if (this.mapId == 170) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1|Cậu muốn quay về Đảo kame à,\nChopper tôi sẽ đưa cậu đi",
                                "Đi thôi", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 170, -1, 1560);
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 170) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 312);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    ///////////////////////////////////////////NPC Nami///////////////////////////////////////////
    public static Npc nami(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|1|Oh hoan nghên bạn đến với của hàng của tôi\n bạn có muốn đổi vỏ ốc, cua đỏ\nlấy các món đồ mùa hè không?.",
                                "Cửa hàng\nNami");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "EVENT_MUA_HE", true);
                                break;
                        }
                    }
                }
            }
        };
    }

    ///////////////////////////////////////////NPC ushop///////////////////////////////////////////
    public static Npc ushop(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {

                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào anh, bọn ta đang cần 1 số lượng cá rất lớn. Cậu có thể giúp bọn ta câu cá được không?",
                            "Hướng dẫn", "Cửa hàng\nDụng cụ", "Của hàng\nCá", "Nâng cấp cần câu", "Sửa cần câu");
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_CAU_CA);
                                break;
                            case 1:
                                ShopServiceNew.gI().opendShop(player, "DUNG_CU_CAU_CA", true);
                                break;
                            case 2:
                                ShopServiceNew.gI().opendShop(player, "BAN_CA", true);
                                break;
                            case 3:
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.NANG_CAP_CAN_CAU);
                                break;
                            case 4:
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.SUA_CHUA_CAN_CAU);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {

                            case CombineServiceNew.SUA_CHUA_CAN_CAU:
                            case CombineServiceNew.NANG_CAP_CAN_CAU:
                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Vào các khung giờ chẵn trong ngày\n"
                                + "Khi luyện tập với Mộc nhân với chế độ bật Cờ sẽ đánh rơi Bí kíp\n"
                                + "Hãy cố găng tập luyện thu thập 9999 bí kíp rồi quay lại gặp ta nhé", "Nhận\nthưởng", "OK");

                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (select == 0) {
                            if (biKiep != null) {
                                if (biKiep.quantity >= 10000 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                    yardart.itemOptions.add(new Item.ItemOption(47, 400));
                                    yardart.itemOptions.add(new Item.ItemOption(108, 10));
                                    InventoryServiceNew.gI().addItemBag(player, yardart);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 10000);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
                                } else if (biKiep.quantity < 10000) {
                                    Service.gI().sendThongBao(player, "Vui lòng sưu tầm đủ\n9999 bí kíp");
                                }
                            } else {
                                Service.gI().sendThongBao(player, "Vui lòng sưu tầm đủ\n9999 bí kíp");
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        };
    }

    public static Npc GhiDanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            String[] menuselect = new String[]{};

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.map.mapId == 52) {
                        if (DaiHoiManager.gI().openDHVT && (System.currentTimeMillis() <= DaiHoiManager.gI().tOpenDHVT)) {
                            String nameDH = DaiHoiManager.gI().nameRoundDHVT();
                            this.createOtherMenu(pl, ConstNpc.MENU_DHVT, "Hiện đang có giải đấu " + nameDH + " bạn có muốn đăng ký không? \nSố người đã đăng ký :" + DaiHoiManager.gI().lstIDPlayers.size(), new String[]{"Giải\n" + nameDH + "\n(" + DaiHoiManager.gI().costRoundDHVT() + ")", "Từ chối", "Đại Hội\nVõ Thuật\nLần thứ\n23", "Giải siêu hạng"});
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đã hết hạn đăng ký thi đấu, xin vui lòng chờ đến giải sau", new String[]{"Thông tin\bChi tiết", "OK", "Đại Hội\nVõ Thuật\nLần thứ\n23", "Giải siêu hạng"});
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = pl.goldChallenge;
                        if (pl.levelWoodChest == 0) {
                            menuselect = new String[]{"Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Về\nĐại Hội\nVõ Thuật"};
                        } else {
                            menuselect = new String[]{"Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Nhận thưởng\nRương cấp\n" + pl.levelWoodChest, "Về\nĐại Hội\nVõ Thuật"};
                        }
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào", menuselect, "Từ chối");

                    } else {
                        super.openBaseMenu(pl);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.map.mapId == 52) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Lịch thi đấu trong ngày\bGiải Nhi đồng: 8,13,18h\bGiải Siêu cấp 1: 9,14,19h\bGiải Siêu cấp 2: 10,15,20h\bGiải Siêu cấp 3: 11,16,21h\bGiải Ngoại hạng: 12,17,22,23h\nGiải thưởng khi thắng mỗi vòng\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 10.000 vàng\bVô địch: 5 viên đá nâng cấp\nVui lòng đến đúng giờ để đăng ký thi đấu");
                                    break;
                                case 1:
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Nhớ Đến Đúng Giờ nhé");
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                    break;
                                case 3:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 113, player.location.x, 360);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DHVT) {
                            switch (select) {
                                case 0:
//                                    if (DaiHoiService.gI().canRegisDHVT(player.nPoint.power)) {
                                    if (DaiHoiManager.gI().lstIDPlayers.size() < 256) {
                                        if (DaiHoiManager.gI().typeDHVT == (byte) 5 && player.inventory.gold >= 10000) {
                                            if (DaiHoiManager.gI().isAssignDHVT(player.id)) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                            } else {
                                                player.inventory.gold -= 10000;
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                                DaiHoiManager.gI().lstIDPlayers.add(player.id);
                                            }
                                        } else if (DaiHoiManager.gI().typeDHVT > (byte) 0 && DaiHoiManager.gI().typeDHVT < (byte) 5 && player.inventory.gem >= (int) (2 * DaiHoiManager.gI().typeDHVT)) {
                                            if (DaiHoiManager.gI().isAssignDHVT(player.id)) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                            } else {
                                                player.inventory.gem -= (int) (2 * DaiHoiManager.gI().typeDHVT);
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                                DaiHoiManager.gI().lstIDPlayers.add(player.id);
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng ngọc để đăng ký thi đấu");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hiện tại đã đạt tới số lượng người đăng ký tối đa, xin hãy chờ đến giải sau");
                                    }

//                                    } else {
//                                        Service.getInstance().sendThongBao(player, "Bạn không đủ điều kiện tham gia giải này, hãy quay lại vào giải phù hợp");
//                                    }
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                    break;
                                case 3:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 113, player.location.x, 360);
                                    break;
                            }
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = player.goldChallenge;
                        if (player.levelWoodChest == 0) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    if (!player.receivedWoodChest) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item it = ItemService.gI().createNewItem((short) 570);
                                            it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
                                            it.itemOptions.add(new Item.ItemOption(30, 0));
                                            it.createTime = System.currentTimeMillis();
                                            InventoryServiceNew.gI().addItemBag(player, it);
                                            InventoryServiceNew.gI().sendItemBags(player);

                                            player.receivedWoodChest = true;
                                            player.levelWoodChest = 0;
                                            Service.getInstance().sendThongBao(player, "Bạn nhận được rương gỗ");
                                        } else {
                                            this.npcChat(player, "Hành trang đã đầy");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                    }
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc unkonw(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 190) {
                        this.createOtherMenu(player, 0,
                                "|2| Tôi là Monkey D. Luffy, người sẽ trở thành vua hải tặc ... ", "Võ Đài\nOne Piece","Đấu Trường\nTrên Mây");
                    }
                    if (this.mapId == 196) {
                        this.createOtherMenu(player, 0,
                                "Bạn đang còn : " + player.pointPvp + " điểm PvP Point", "Quay Về", "Đổi Cải trang sự kiên", "Top PVP");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 190) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    if (player.getSession().player.nPoint.power >= 10000000000L) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 112, -1, 495);
                                        Service.gI().changeFlag(player, Util.nextInt(8));
                                    } else {
                                        this.npcChat(player, "Bạn cần 10 tỷ sức mạnh mới có thể vào");
                                    }
                                    break; // qua vo dai
                                        case 1:
                                    if (player.getSession().player.nPoint.power >= 10000000000L) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 196, -1, 495);
                                        Service.gI().changeFlag(player, Util.nextInt(8));
                                    } else {
                                        this.npcChat(player, "Bạn cần 10 tỷ sức mạnh mới có thể vào");
                                    }
                                    break; // qua vo dai

                            }
                        }
                    }

                    if (this.mapId == 196) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 190, -1, 319);
                                    break; // ve dao kame
                                case 1:  // 
                                    this.createOtherMenu(player, 1,
                                            "Bạn có muốn đổi 500 điểm PVP lấy \n|6|Cải trang Goku SSJ3\n với chỉ số random từ 20 > 30% \n ", "Ok", "Không");
                                    // bat menu doi item
                                    break;

                                case 2:  // 
                                    Service.gI().showListTop(player, Manager.topPVP);
                                    // mo top pvp
                                    break;

                            }
                        }
                        if (player.iDMark.getIndexMenu() == 1) { // action doi item
                            switch (select) {
                                case 0: // trade
                                    if (player.pointPvp >= 500) {
                                        player.pointPvp -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (1227)); // 49
                                        item.itemOptions.add(new Item.ItemOption(49, Util.nextInt(20, 30)));
                                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20, 30)));
                                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 30)));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
                                        item.itemOptions.add(new Item.ItemOption(33, 0));
//                                      
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Cải Trang Thành Công !");
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ điểm bạn còn " + (500 - player.pointPvp) + " Điểm nữa");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc monaito(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        this.createOtherMenu(player, 0,
                                "Chào bạn tôi sẽ đưa bạn đến hành tinh Cereal?", "Đồng ý", "Từ chối");
                    }
                    if (this.mapId == 170) {
                        this.createOtherMenu(player, 0,
                                "Ta ở đây để đưa con về", "Về Làng Mori", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 170, -1, 264);
                                    break; // den hanh tinh cereal
                            }
                        }
                    }
                    if (this.mapId == 170) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 432);
                                    break; // quay ve

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc granala(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {

                    if (this.mapId == 171) {
                        this.createOtherMenu(player, 0,
                                "Ngươi!\n Hãy cầm đủ 7 viên ngọc rồng \n Monaito đến đây gặp ta ta sẽ ban cho ngươi\n 1 điều ước ", "Gọi rồng", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                    if (this.mapId == 171) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    this.npcChat(player, "Chức Năng Đang Được Update!");
                                    break; // goi rong

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc vip_truongchimto(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 181) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Thức Tỉnh");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 181) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.THUC_TINH_DT);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.THUC_TINH_DT:
                                    switch (select) {
                                        case 0:
                                            CombineServiceNew.gI().thuctinhDT(player, 1);
                                            System.out.print("test");
                                            break;
                                        case 1:
                                            CombineServiceNew.gI().thuctinhDT(player, 10);
                                            break;
                                        case 2:
                                            CombineServiceNew.gI().thuctinhDT(player, 100);
                                            break;
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            switch (tempId) {
                case ConstNpc.UNKOWN:
                    return luffy(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.LUFFY:
                    return unkonw(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GHI_DANH:
                    return GhiDanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUNG_LINH_THU:
                    return trungLinhThu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POTAGE:
                    return poTaGe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUY_LAO_KAME:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MR_POPO:
                    return popo(mapId, status, cx, cy, tempId, avatar);
                        case ConstNpc.PANCHY:
                    return panchy(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THO_DAI_CA:
                    return thodaika(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUONG_LAO_GURU:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VUA_VEGETA:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA:
                    return bulmaQK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE:
                    return dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE:
                    return appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF:
                    return drDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO:
                    return cargo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUI:
                    return cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA:
                    return santa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.URON:
                    return uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT:
                    return baHatMit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_NAMEC:
                    return bulmaNm(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.FIDE:
                    return fideNm(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SU_KIEN_HE:
                    return SUKIENHE(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.HUNG_VUONG:
//                    return hungvuong(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.SkienTrungThu:
//                    return Skien_trungthu(mapId, status, cx, cy, tempId, avatar);
                       case ConstNpc.SHOP_DE_VIP:
            return pgg(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.BAR:
//                    return bar(mapId, status, cx, cy, tempId, avatar);    
                case ConstNpc.CC:
                    return gohanzombie(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.NOI_BANH:
//                    return noibanh(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.Pic:
//                    return Pic2Mai(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.VODAI:
//                    return Vodai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO:
                    return ruongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN:
                    return dauThan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CALICK:
                    return calick(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.JACO:
                    return jaco(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRONG_TAI:
                    return TrongTai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRONG_TAI1:
                    return TrongTai1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE:
                    return thuongDe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Granola:
                    return granala(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GIUMA_DAU_BO:
                    return mavuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TAPION:
                    return Tapion(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUA_HANG_KY_GUI:
                    return kyGui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Monaito:
                    return monaito(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VADOS:
                    return vados(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_VU_TRU:
                    return thanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT:
                    return kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN:
                    return osin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LY_TIEU_NUONG:
                    return npclytieunuong54(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LY_TIEU_NUONG_99:
                    return npclytieunuong99(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LINH_CANH:
                    return linhCanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DOC_NHAN:
                    return docNhan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUA_TRUNG:
                    return quaTrung(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUOC_VUONG:
                    return quocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOJO:
                    return Gojo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_TL:
                    return bulmaTL(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA:
                    return rongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC_64:
                    return npcThienSu64(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC_100:
                    return NPC_cadic(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BILL:
                    return berus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.WHIS:
                    return whis(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BO_MONG:
                    return boMong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_MEO_KARIN:
                    return karin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ:
                    return gokuSSJ_1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ_:
                    return gokuSSJ_2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUONG_TANG:
                    return duongtank(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOHAN_NHAT_NGUYET:
                    return gohannn(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.CHOPPER:
//                    return chopper(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.USHOP:
//                    return ushop(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.NAMI:
//                    return quaythu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TO_SU_KAIO:
                    return TosuKaio(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MINUONG:
                    return miNuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Mon_aka:
                    return Monaka(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.GAPTHU1:
//                    return gapthu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DE:
                    return vihop(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUCTINH:
                    return vip_truongchimto(mapId, status, cx, cy, tempId, avatar);
                default:
                    return new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
//                                ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0, player.gender);
                            }
                        }
                    };

            }
        } catch (Exception e) {
            Logger.logException(NpcFactory.class,
                    e, "Lỗi load npc");
            return null;
        }
    }

    //girlbeo-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1 && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2 && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.MAKE_MATCH_PVP: {
                        if (Maintenance.isRuning) {
                            break;
                        }
                        PVPService.gI().sendInvitePVP(player, (byte) select);
                        break;
                    }
//                        else {
//                            Service.gI().sendThongBao(player, "|5|VUI LÒNG KÍCH HOẠT TÀI KHOẢN TẠI\n|7|NROGOD.COM\n|5|ĐỂ MỞ KHÓA TÍNH NĂNG");
//                            break;
//                        }
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGONTRB://TRB
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TRB);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRONTRB:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TRB);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenronTRB(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGONTRB1://TRB
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TRB1);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRONTRB1:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TRB1);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenronTRB1(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2000:
                    case ConstNpc.MENU_OPTION_USE_ITEM2001:
                    case ConstNpc.MENU_OPTION_USE_ITEM2002:
                        try {
                        ItemService.gI().OpenSKH(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2003:
                    case ConstNpc.MENU_OPTION_USE_ITEM2004:
                    case ConstNpc.MENU_OPTION_USE_ITEM2005:
                        try {
                        ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM736:
                        try {
                        ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.HOISINH:
                        if (select == 0) {
                            if (player.inventory.ruby >= 1000) {
                                player.inventory.ruby -= 1000;
                                Service.gI().sendMoney(player);
                                Service.gI().hsChar(player, player.nPoint.hpMax, player.nPoint.mpMax);
                                player.achievement.plusCount(13);
                            } else {
                                Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc");
                            }
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player, "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.gI().sendThongBao(player, "Phát đệ tử cho " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.UP_TOP_ITEM:
                        if (select == 0) {
                            if (player.inventory.ruby >= 50 && player.iDMark.getIdItemUpTop() != -1) {
                                ItemKyGui it = ShopKyGuiService.gI().getItemBuy(player.iDMark.getIdItemUpTop());
                                if (it == null || it.isBuy) {
                                    Service.gI().sendThongBao(player, "Vật phẩm không tồn tại hoặc đã được bán");
                                    return;
                                }
                                if (it.player_sell != player.id) {
                                    Service.gI().sendThongBao(player, "Vật phẩm không thuộc quyền sở hữu");
                                    ShopKyGuiService.gI().openShopKyGui(player);
                                    return;
                                }
                                player.inventory.ruby -= 50;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Thành công");
                                it.isUpTop += 1;
                                ShopKyGuiService.gI().openShopKyGui(player);
                            } else {
                                Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc");
                                player.iDMark.setIdItemUpTop(-1);
                            }
                        }
                        break;
                    case ConstNpc.PETSERVICE:
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.MUA_DE, 15934, "|7|Pet Service" + "\n" + "|1|" + "\n"
                                        + "Chào bạn : " + player.name + " | ID: (" + player.id + ") | " + "Map : " + player.zone.map.mapName + "\n"
                                        + "Số Dư Khả Dụng : " + player.getSession().vnd + " VNĐ" + "\n"
                                        + "Bạn có chăc muốn đổi đệ không?" + "\n" + "\n"
                                        + "|7|* Chỉ Cần Mua 1 Đệ Tử Bất Kỳ, Hệ Thống Sẽ \nTự Động Kích Hoạt Tài Khoản Cho Bạn!!\nCHÚ Ý : Đệ nhận miễn phí không tự động Active Account",
                                        "Đệ Thiên Tử\n30.000VNĐ", "Đệ Black Goku\n40.000VNĐ", "Đệ Kaido\n50.000VNĐ", "Đóng");
                                break;
                        }
                        break;
                    case ConstNpc.MUA_DE:
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, ConstNpc.CONFIRM_MUA_DE_THIENTU, "Bạn có chắc muốn mua đệ Thiên Tử không?", "Đồng ý", "Không");
                                break;
                            case 1:
                                this.createOtherMenu(player, ConstNpc.CONFIRM_MUA_DE_BLACK, "Bạn có chắc muốn mua đệ Black Goku không?", "Đồng ý", "Không");
                                break;
                            case 2:
                                this.createOtherMenu(player, ConstNpc.CONFIRM_MUA_DE_KAIDO, "Bạn có chắc muốn mua đệ Kaido không?", "Đồng ý", "Không");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_MUA_DE_THIENTU:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd < 30000) {
                                    Service.gI().sendThongBao(player, "Bạn không có đủ 30k coin");
                                    return;
                                }
                                if (player.pet == null) {
                                    Service.gI().sendThongBao(player, "Bạn cần phải có đệ tử thường trước");
                                    return;
                                }
                                player.getSession().vnd -= 30000;
                                PetService.gI().createThienTuPetVip(player, true, player.pet.gender);
                                break;
                            case 1:
                                this.npcChat(player, "Cảm ơn đã quý khách đã ghé shop");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_MUA_DE_BLACK:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd < 40000) {
                                    Service.gI().sendThongBao(player, "Bạn không có đủ 30k coin");
                                    return;
                                }
                                if (player.pet == null) {
                                    Service.gI().sendThongBao(player, "Bạn cần phải có đệ tử thường trước");
                                    return;
                                }

                                player.getSession().vnd -= 40000;
                                PetService.gI().createBlackGokuPetVip(player, true, player.pet.gender);
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_MUA_DE_KAIDO:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd < 50000) {
                                    Service.gI().sendThongBao(player, "Bạn không có đủ 50k coin");
                                    return;
                                }
                                if (player.pet == null) {
                                    Service.gI().sendThongBao(player, "Bạn cần phải có đệ tử thường trước");
                                    return;
                                }

                                player.getSession().vnd -= 50000;
                                PetService.gI().createKaidoPetVip(player, true, player.pet.gender);
                                break;
                        }
                        break;
                    case ConstNpc.CLCK:
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.PETSERVICE, 15934, "|7|Pet Service" + "\n" + "|1|" + "\n"
                                        + "Chào bạn : " + player.name + " | ID: (" + player.id + ") | " + "Map : " + player.zone.map.mapName + "\n"
                                        + "Số Dư Khả Dụng : " + player.getSession().vnd + " VNĐ" + "\n"
                                        + "Chọn dịch vụ?" + "\n",
                                        "MUA\nĐỆ TỬ", "Đóng");
                                break;
                            case 1:
                                if (player.inventory.ruby >= 2000000) {
                                    Service.gI().sendThongBao(player, "Quá giới hạn nhận Hồng Ngọc");
                                    break;
                                }
                                player.inventory.ruby = 2000000;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Nhận thành công 2 triệu Hồng Ngọc");
                                break;
                            case 2:
                                Item thoivang = null;
                                try {
                                    thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                                } catch (Exception e) {
                                }
                                if (thoivang == null || thoivang.quantity < 200000) {
                                    Item tvnhan = ItemService.gI().createNewItem((short) 457);
                                    tvnhan.quantity = 200000;
                                    InventoryServiceNew.gI().addItemBag(player, tvnhan);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Nhận thành công 200k Thỏi Vàng");
                                    break;
                                } else {
                                    thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                                    if (thoivang.quantity >= 200000) {
                                        Service.gI().sendThongBao(player, "Quá giới hạn nhận Thỏi Vàng");
                                        break;
                                    }
                                }
                                break;
                            case 3:
                                Input.gI().createFormGiftCode(player);
                                break;
                            case 4:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_PLAYER, 15934,
                                        "|7|Nhân vật: " + player.name + "\n"
                                        + "[Trạng thái tài khoản : " + (player.getSession().actived == true ? " Đã Mở Thành Viên]" : " Chưa Mở Thành Viên]\n")
                                        + "\n|1|Chỉ số hiện tại"
                                        + "\n|2|Hp: " + player.nPoint.hp + "/" + player.nPoint.hpMax
                                        + "\n|2|Ki: " + player.nPoint.mp + "/" + player.nPoint.mpMax
                                        + "\n|2|Sức đánh: " + player.nPoint.dame + "\n"
                                        + "\n|1|Chỉ số gốc"
                                        + "\n|2|Hp gốc: " + player.nPoint.hpg
                                        + "\n|2|Ki gốc: " + player.nPoint.mpg
                                        + "\n|2|Sức đánh gốc: " + player.nPoint.dameg, "Nhập giftcode", "Đổi mật khẩu", "Đóng");
                                break;
                        }
                        break;
                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                this.createMenuConMeo(player, ConstNpc.ADMIN1, 15934, "|7| Admin Ngọc Rồng Online\b|2| obito\b|4| Người Đang Chơi: " + Client.gI().getPlayers().size() + "\n" + "|8|Current thread: " + (Thread.activeCount() - ServerManager.gI().threadMap) + "\n",
                                        "Bảo Trì", "Tìm Kiếm\nPlayer", "Chat all");
                                break;
                            case 1:
                                this.createOtherMenu(player, ConstNpc.CALL_BOSS,
                                        "|2| PANNEL ADMIN - NRO AKAYA : CALL BOSS", "Cụm\nAndroid", "Goku\nBlack", "Broly", "Cụm\nCell",
                                        "Cụm\nDoanh trại", "Doraemon", "Fide", "Fide\nBlack", "Cụm\nGinyu", "Cụm\nNappa");
                                break;
                            case 2:
                                this.createOtherMenu(player, ConstNpc.BUFFITEM,
                                        "|2| PANNEL ADMIN : BUFF VẬT PHẨM - NRO AKAYA", "Buff\nItem", "Item\nOption", "Buff \nItem\nSKH");
                                break;
                            case 3:
                                MaQuaTangManager.gI().checkInfomationGiftCode(player);
                                break;
                            case 4:
                                Input.gI().createFormNapCoin(player);
                                break;
                            case 5:
                                BossManager.gI().showListBoss(player);
                                break;
                        }
                        break;
                    case ConstNpc.BUFFITEM:
                        switch (select) {
                            case 0:
                                Input.gI().createFormSenditem(player);
                                break;
                            case 1:
                                Input.gI().createFormSendItem1(player);
                                break;
                            case 2:
                                Input.gI().createFormSenditem2(player);
                                break;
                        }
                        break;
                    case ConstNpc.ADMIN1:
                        switch (select) {
//                            case 0:
//                                for (int i = 14; i <= 20; i++) {
//                                    Item item = ItemService.gI().createNewItem((short) i);
//                                    InventoryServiceNew.gI().addItemBag(player, item);
//                                }
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                break;
//                            case 1:
//                                if (player.pet == null) {
//                                    PetService.gI().createNormalPet(player);
//                                } else {
//                                    if (player.pet.typePet == 1) {
//                                        PetService.gI().changePicPet(player);
//                                    } else if (player.pet.typePet == 2) {
//                                        PetService.gI().changeMabuPet(player);
//                                    }
//                                    PetService.gI().changeBerusPet(player);
//                                }
//                                break;
                            case 0:
                                if (player.isAdmin()) {
                                    System.out.println(player.name);
                                    Maintenance.gI().start(15);
                                    System.out.println(player.name);
                                }
                                break;
                            case 1:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 2:
                                Input.gI().ChatAll(player);
                                break;
                        }
                        break;
                    case ConstNpc.CALL_BOSS:
                        switch (select) {
                            case 0:
                                BossManager.gI().createBoss(BossID.ANDROID_13);
                                BossManager.gI().createBoss(BossID.ANDROID_14);
                                BossManager.gI().createBoss(BossID.ANDROID_15);
                                BossManager.gI().createBoss(BossID.APK1920);
                                BossManager.gI().createBoss(BossID.PICPOCKING);
                                break;
                            case 1:
                                BossManager.gI().createBoss(BossID.BLACK);
                                break;
                            case 2:
                                BossManager.gI().createBoss(BossID.BROLY);
                                break;
                            case 3:
                                BossManager.gI().createBoss(BossID.SIEU_BO_HUNG);
                                BossManager.gI().createBoss(BossID.XEN_BO_HUNG);
                                BossManager.gI().createBoss(BossID.XEN_CON);
                                break;
                            case 4:
                                Service.getInstance().sendThongBao(player, "Không có boss");
                                break;
                            case 5:
                                BossManager.gI().createBoss(BossID.CHAIEN);
                                BossManager.gI().createBoss(BossID.XEKO);
                                BossManager.gI().createBoss(BossID.XUKA);
                                BossManager.gI().createBoss(BossID.NOBITA);
                                BossManager.gI().createBoss(BossID.DORAEMON);
                                break;
                            case 6:
                                BossManager.gI().createBoss(BossID.FIDE);
                                break;
                            case 7:
                                BossManager.gI().createBoss(BossID.FIDE_ROBOT);
                                BossManager.gI().createBoss(BossID.VUA_COLD);
                                break;
                            case 8:
                                BossManager.gI().createBoss(BossID.TDST);
                                break;
                            case 9:
                                BossManager.gI().createBoss(BossID.KUKUMDDRAMBO);
                                break;
                        }
                        break;

                    case ConstNpc.QUY_DOI_HN:
                        switch (select) {
                            case 0:
                                int coin = 10000;
                                int tv = 30;
                                int dans = 3;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 1:
                                coin = 20000;
                                tv = 60;
                                dans = 6;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 2:
                                coin = 30000;
                                tv = 90;
                                dans = 9;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 3:
                                coin = 50000;
                                tv = 160;
                                dans = 16;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 4:
                                coin = 100000;
                                tv = 330;
                                dans = 33;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 5:
                                coin = 200000;
                                tv = 670;
                                dans = 67;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 6:
                                coin = 300000;
                                tv = 1050;
                                dans = 105;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 7:
                                coin = 500000;
                                tv = 1800;
                                dans = 180;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 8:
                                coin = 1000000;
                                tv = 3700;
                                dans = 370;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                        }
                        break;
                    case ConstNpc.menutd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settaiyoken(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgenki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setkamejoko(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgoddam(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setsummon(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodgalick(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setmonkey(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setgodhp(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                clan.deleteDB(clan.id);
                                Manager.CLANS.remove(clan);
                                player.clan = null;
                                player.clanMember = null;
                                ClanService.gI().sendMyClan(player);
                                ClanService.gI().sendClanId(player);
                                Service.gI().sendThongBao(player, "Đã giải tán bang hội.");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_ACTIVE:
                        switch (select) {
                            case 0:
                                if (player.getSession().goldBar >= 20) {
                                    player.getSession().actived = true;
                                    if (PlayerDAO.subGoldBar(player, 20)) {
                                        Service.gI().sendThongBao(player, "Đã mở thành viên thành công!");
                                        break;
                                    } else {
                                        this.npcChat(player, "Lỗi vui lòng báo admin...");
                                    }
                                }
                                Service.gI().sendThongBao(player, "Bạn không có vàng\n Vui lòng NROSEOBI.ME để nạp thỏi vàng");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.gI().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x, p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x, player.location.y);
                                    }
                                    break;
                                case 2:
                                    Input.gI().createFormChangeName(player, p);
                                    break;
                                case 3:
                                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    break;
                                case 4:
                                    Service.gI().sendThongBao(player, "Kik người chơi " + p.name + " thành công");
                                    Client.gI().getPlayers().remove(p);
                                    Client.gI().kickSession(p.getSession());
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.MENU_EVENT:
                        switch (select) {
                            case 0:
                                Service.gI().sendThongBaoOK(player, "Điểm sự kiện: " + player.inventory.event + " ngon ngon...");
                                break;
                            case 1:
                                Service.gI().showListTop(player, Manager.topSK);
                                break;
                            case 2:
                                Service.gI().sendThongBao(player, "Sự kiện đã kết thúc...");
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_GIAO_BONG, -1, "Người muốn giao bao nhiêu bông...",
//                                        "100 bông", "1000 bông", "10000 bông");
                                break;
                            case 3:
                                Service.gI().sendThongBao(player, "Sự kiện đã kết thúc...");
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN, -1, "Con có thực sự muốn đổi thưởng?\nPhải giao cho ta 3000 điểm sự kiện đấy... ",
//                                        "Đồng ý", "Từ chối");
                                break;

                        }
                        break;
                    case ConstNpc.MENU_GIAO_BONG:
                        ItemService.gI().giaobong(player, (int) Util.tinhLuyThua(10, select + 2));
                        break;
                    case ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN:
                        if (select == 0) {
                            ItemService.gI().openBoxVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_TELE_NAMEC:
                        if (select == 0) {
                            NgocRongNamecService.gI().teleportToNrNamec(player);
                            player.inventory.subGemAndRuby(50);
                            Service.gI().sendMoney(player);
                        }
                        break;
                }
            }
        };
    }

}
