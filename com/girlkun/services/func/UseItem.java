package com.girlkun.services.func;

import com.girlkun.card.Card;
import com.girlkun.card.RadarCard;
import com.girlkun.card.RadarService;
import com.girlkun.consts.ConstMap;
import com.girlkun.models.item.Item;
import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Inventory;
import static com.girlkun.models.player.Inventory.LIMIT_GOLD;
import com.girlkun.models.player.Pet;
import com.girlkun.services.*;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;
import com.girlkun.server.io.MySession;
import com.girlkun.utils.Logger;

import java.util.Date;
import java.util.Random;

public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;

    private static int HP_BUFF = 100000;
    private static int MP_BUFF = 100000;
    private static int SD_BUFF = 5000;

    private static UseItem instance;
    public static final int[][][] LIST_ITEM_CLOTHES = {
        // áo , quần , găng ,giày,rada
        //td -> nm -> xd
        {{0, 33, 3, 34, 136, 137, 138, 139, 230, 231, 232, 233, 555}, {6, 35, 9, 36, 140, 141, 142, 143, 242, 243, 244, 245, 556}, {21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257, 562}, {27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269, 563}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
        {{1, 41, 4, 42, 152, 153, 154, 155, 234, 235, 236, 237, 557}, {7, 43, 10, 44, 156, 157, 158, 159, 246, 247, 248, 249, 558}, {22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261, 564}, {28, 47, 31, 48, 164, 165, 166, 167, 270, 271, 272, 273, 565}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
        {{2, 49, 5, 50, 168, 169, 170, 171, 238, 239, 240, 241, 559}, {8, 51, 11, 52, 172, 173, 174, 175, 250, 251, 252, 253, 560}, {23, 53, 26, 54, 176, 177, 178, 179, 262, 263, 264, 265, 566}, {29, 55, 32, 56, 180, 181, 182, 183, 274, 275, 276, 277, 567}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}}
    };

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(MySession session, Message msg) {
        Player player = session.player;
        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            if (index == -1) {
                return;
            }
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    InventoryServiceNew.gI().itemBoxToBodyOrBag(player, index);
                    TaskService.gI().checkDoneTaskGetItemBox(player);
                    break;
                case ITEM_BAG_TO_BOX:
                    InventoryServiceNew.gI().itemBagToBox(player, index);
                    break;
                case ITEM_BODY_TO_BOX:
                    InventoryServiceNew.gI().itemBodyToBox(player, index);
                    break;
                case ITEM_BAG_TO_BODY:
                    InventoryServiceNew.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryServiceNew.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    InventoryServiceNew.gI().itemBagToPetBody(player, index);
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryServiceNew.gI().itemPetBodyToBag(player, index);
                    break;
            }
            player.setClothes.setup();
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            Service.gI().point(player);
//            testItem(player, msg);
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);

        }
    }

    public void testItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        try {
            byte type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            System.out.println("type: " + type);
            System.out.println("where: " + where);
            System.out.println("index: " + index);
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);
        }
    }

    public void doItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        byte type;
        try {
            type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
//            System.out.println(type + " " + where + " " + index);
            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
                        if (index != -1) {
                            Item item = player.inventory.itemsBag.get(index);
                            if (item.isNotNullItem()) {
                                if (item.template.type == 7) {
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc chắn học " + player.inventory.itemsBag.get(index).template.name + "?");
                                    player.sendMessage(msg);
                                } else {
                                    UseItem.gI().useItem(player, item, index);
                                }
                            }
                        } else {
                            this.eatPea(player);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    if (!(player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23)) {
                        Item item = null;
                        if (where == 0) {
                            item = player.inventory.itemsBody.get(index);
                        } else {
                            item = player.inventory.itemsBag.get(index);
                        }
                        msg = new Message(-43);
                        msg.writer().writeByte(type);
                        msg.writer().writeByte(where);
                        msg.writer().writeByte(index);
                        msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + "?");
                        player.sendMessage(msg);
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case ACCEPT_THROW_ITEM:
                    InventoryServiceNew.gI().throwItem(player, where, index);
                    Service.gI().point(player);
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;
                case ACCEPT_USE_ITEM:
                    UseItem.gI().useItem(player, player.inventory.itemsBag.get(index), index);
                    break;
            }
        } catch (Exception e) {
//            Logger.logException(UseItem.class, e);
        }
    }

    private void useItem(Player pl, Item item, int indexBag) {
        if (item.template.strRequire <= pl.nPoint.power) {
            switch (item.template.type) {
                case 74:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendFoot(pl, item.template.id);
                    break;
                case 21:
                    if (pl.newpet != null) {
                        ChangeMapService.gI().exitMap(pl.newpet);
                        pl.newpet.dispose();
                        pl.newpet = null;
                    }
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    PetService.Pet2(pl, item.template.head, item.template.body, item.template.leg);
                    Service.getInstance().point(pl);
                    break;
                case 7: //sách học, nâng skill
                    learnSkill(pl, item);
                    break;
                case 33:
                    UseCard(pl, item);
                    break;
                case 6: //đậu thần
                    this.eatPea(pl);
                    break;
                case 77:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    break;
                case 12: //ngọc rồng các loại
                    controllerCallRongThan(pl, item);
                    controllerCalltrb(pl, item);
                    controllerCalltrb1(pl, item);
                    break;
                case 23: //thú cưỡi mới
                case 24: //thú cưỡi cũ
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    break;
                case 11: //item bag
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendFlagBag(pl);
                    break;
                case 75:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.getInstance().sendchienlinh(pl, (short) (item.template.iconID - 1));
                    break;    
                case 72: 
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendPetFollow(pl, (short) (item.template.iconID - 1));
                    break;
                case 39:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().removeTitle(pl);
                    Service.gI().sendTitle(pl, item.template.id);
                  //  DanhHieu.AddDanhHieu(pl, item);
                    Service.gI().point(pl);
                    break;     
                
                default:
                    switch (item.template.id) {
                        case 992:
                            pl.type = 1;
                            pl.maxTime = 5;
                            Service.gI().Transport(pl);
                            break;
                        case 1526:
                            openngocrongchest(pl, item);
                            break;
                            case 571:
                            Openhoppet(pl, item);
                            break;

                        case 361:
                            if (pl.idNRNM != -1) {
                                Service.gI().sendThongBao(pl, "Không thể thực hiện");
                                return;
                            }
                            pl.idGo = (short) Util.nextInt(0, 6);
                            NpcService.gI().createMenuConMeo(pl, ConstNpc.CONFIRM_TELE_NAMEC, -1, "1 Sao (" + NgocRongNamecService.gI().getDis(pl, 0, (short) 353) + ")\n2 Sao (" + NgocRongNamecService.gI().getDis(pl, 1, (short) 354) + ")\n3 Sao (" + NgocRongNamecService.gI().getDis(pl, 2, (short) 355) + ")\n4 Sao (" + NgocRongNamecService.gI().getDis(pl, 3, (short) 356) + ")\n5 Sao (" + NgocRongNamecService.gI().getDis(pl, 4, (short) 357) + ")\n6 Sao (" + NgocRongNamecService.gI().getDis(pl, 5, (short) 358) + " m)\n7 Sao (" + NgocRongNamecService.gI().getDis(pl, 6, (short) 359) + ")", "Đến ngay\nViên " + (pl.idGo + 1) + " Sao\n50 ngọc", "Kết thức");
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            break;
                        case 1530: //máy dò boss
                            BossManager.gI().showListBoss2(pl);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            break;

                        case 211: //nho tím
                        case 212: //nho xanh
                            eatGrapes(pl, item);
                            break;
                        case 1105://hop qua skh, item 2002 xd
                            UseItem.gI().Hopts(pl, item);
                            break;
                        case 1536:
                            if (pl.thoigianduhanh == 0) {
                                pl.thoigianduhanh += System.currentTimeMillis() + (1000 * 60 * 60 * 2);
                            } else {
                                pl.thoigianduhanh += (1000 * 60 * 60 * 2);
                            }
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 1997://hop qua skh, item 2002 xd
                            Openhopct(pl, item);
                            break;
                        case 1186: // quà chúc tết
                            UseItem.gI().usehopquatet(pl);
                            break;
                        case 1994:// Hộp Quà Top 1
                            Openhoptop1(pl, item);
                            break;
                        case 1995:// Hộp Quà Top 2
                            Openhoptop2(pl, item);
                            break;
                        case 1996:// Hộp Quà Top 3
                            Openhoptop3(pl, item);
                            break;

                        case 1998://hop qua skh, item 2002 xd
                            Openhopflagbag(pl, item);
                            break;
                        case 1999://hop qua skh, item 2002 xd
                            Openhoppet(pl, item);
                            break;
                        case 342:
                        case 343:
                        case 344:
                        case 345:
                            if (pl.zone.items.stream().filter(it -> it != null && it.itemTemplate.type == 22).count() < 3) {
                                Service.gI().DropVeTinh(pl, item, pl.zone, pl.location.x, pl.location.y);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.gI().sendThongBao(pl, "Đặt ít thôi con");
                            }
                            break;
                        case 380: //cskb
                            openCSKB(pl, item);
                            break;
                        case 1365:
                            openHopTangNgoc(pl, item);
                            break;
                        case 1400:
                        case 1406:
                        case 381: //cuồng nộ
                        case 382: //bổ huyết
                        case 383: //bổ khí
                        case 384: //giáp xên
                        case 385: //ẩn danh
                        case 379: //máy dò capsule
                        case 2037: //máy dò cosmos
                        case 663: //bánh pudding
                        case 664: //xúc xíc
                        case 665: //kem dâu
                        case 666: //mì ly
                        case 667: //sushi
                        case 1099:
                        case 1100:
                        case 1101:
                        case 1102:
                        case 1103:

                            useItemTime(pl, item);
                            break;
                        case 521: //tdlt
                            useTDLT(pl, item);
                            break;
                        case 454: //bông tai
                            UseItem.gI().usePorata(pl);
                            break;
                        case 1153:
                            UseItem.gI().usehpbuff(pl);
                            break;
                        case 1152:
                            UseItem.gI().usesdbuff(pl);
                            break;
                        case 1154:
                            UseItem.gI().usekibuff(pl);
                            break;
                        case 1399:
                            useCanCau(pl, item);
                            break;    
                        case 193: //gói 10 viên capsule
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        case 194: //capsule đặc biệt

                            if (pl.pet3 != null && pl.pet3.zone != null) {
                                Service.gI().sendThongBao(pl, "Định ăn gian à :>"); //đá bảo vệ
                            } else {
                                openCapsuleUI(pl);
                            }
                            break;
                        case 401: //đổi đệ tử
                            changePet(pl, item);
                            break;
                        case 402: //sách nâng chiêu 1 đệ tử
                        case 403: //sách nâng chiêu 2 đệ tử
                        case 404: //sách nâng chiêu 3 đệ tử
                        case 759: //sách nâng chiêu 4 đệ tử
                            upSkillPet(pl, item);
                            break;
//                        case 457:
//                            Input.gI().createFormUseGold(pl);
//                            break;
                        case 921: //bông tai c2
                            UseItem.gI().usePorata2(pl);
                            break;
                        case 1155: //bông tai c3
                            UseItem.gI().usePorata3(pl);
                            break;
                        case 1156: //bông tai c4
                            UseItem.gI().usePorata4(pl);
                            break;
                        case 2000://hop qua skh, item 2000 td
                        case 2001://hop qua skh, item 2001 nm
                        case 2002://hop qua skh, item 2002 xd
                            UseItem.gI().ItemSKH(pl, item);
                            break;

                        case 2003://hop qua skh, item 2003 td
                        case 2004://hop qua skh, item 2004 nm
                        case 2005://hop qua skh, item 2005 xd
                            UseItem.gI().ItemDHD(pl, item);
                            break;
                        case 736:
                            ItemService.gI().OpenItem736(pl, item);
                            break;
                        case 1408:
                            ItemService.gI().OpenItem1408(pl, item);
                            break;
                        case 1409:
                            ItemService.gI().OpenItem1409(pl, item);
                            break;
                        case 1416:
                            ItemService.gI().OpenItem1416(pl, item);
                            break;

                        case 2048:
                            ItemService.gI().OpenItem2048(pl, item);
                            break;
                        case 2049:
                            ItemService.gI().OpenItem2049(pl, item);
                            break;
                        case 987:
                            Service.gI().sendThongBao(pl, "Bảo vệ trang bị không bị rớt cấp"); //đá bảo vệ
                            break;
                        case 1120:
                            useItemHopQuaTanThu(pl, item);
                            break;
                        case 1180:
                            ItemService.gI().OpenItem1180(pl, item);
                            break;
                        case 1181:
                            ItemService.gI().OpenItem1181(pl, item);
                            break;    
                        case 2006:
                            Input.gI().createFormChangeNameByItem(pl);
                            break;
                        case 1131:
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                break;
                            }

                            if (pl.pet.playerSkill.skills.get(1).skillId != -1 && pl.pet.playerSkill.skills.get(2).skillId != -1) {
                                pl.pet.openSkill2();
                                pl.pet.openSkill3();
                                InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Đã đổi thành công chiêu 2 3 đệ tử");
                            } else {
                                Service.gI().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                            }
                            break;

                        case 2027:
                        case 2028: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(2019, 2026));
                                linhThu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 10)));
                                linhThu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
                                linhThu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 10)));
//                                linhThu.itemOptions.add(new Item.ItemOption(95, Util.nextInt(5, 25)));
//                                linhThu.itemOptions.add(new Item.ItemOption(96, Util.nextInt(5, 25)));
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Chúc mừng bạn nhận được Linh thú " + linhThu.template.name);
                            }
                            break;

                        }
                        case 570:
                            openWoodChest(pl, item);
                            break;
                    }
                    break;
            }
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.gI().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
        }
    }

    private void Openhopct(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 5) {
            Item aotl = ItemService.gI().createNewItem((short) 557);
            Item quantl = ItemService.gI().createNewItem((short) 558);
            Item gangtl = ItemService.gI().createNewItem((short) 564);
            Item giaytl = ItemService.gI().createNewItem((short) 565);
            Item nhantl = ItemService.gI().createNewItem((short) 561);

            aotl.itemOptions.add(new Item.ItemOption(47, 1091));
            aotl.itemOptions.add(new Item.ItemOption(72, 3));
            aotl.itemOptions.add(new Item.ItemOption(107, 8));

            quantl.itemOptions.add(new Item.ItemOption(6, 70000));
            quantl.itemOptions.add(new Item.ItemOption(27, 6868));
            quantl.itemOptions.add(new Item.ItemOption(72, 2));
            quantl.itemOptions.add(new Item.ItemOption(107, 8));

            gangtl.itemOptions.add(new Item.ItemOption(0, 5514));
            gangtl.itemOptions.add(new Item.ItemOption(72, 2));
            gangtl.itemOptions.add(new Item.ItemOption(107, 8));

            giaytl.itemOptions.add(new Item.ItemOption(7, 62591));
            giaytl.itemOptions.add(new Item.ItemOption(28, 4719));
            giaytl.itemOptions.add(new Item.ItemOption(72, 2));
            giaytl.itemOptions.add(new Item.ItemOption(107, 8));

            nhantl.itemOptions.add(new Item.ItemOption(14, 19));
            nhantl.itemOptions.add(new Item.ItemOption(72, 4));
            nhantl.itemOptions.add(new Item.ItemOption(107, 8));

            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, aotl);
            InventoryServiceNew.gI().addItemBag(pl, quantl);
            InventoryServiceNew.gI().addItemBag(pl, gangtl);
            InventoryServiceNew.gI().addItemBag(pl, giaytl);
            InventoryServiceNew.gI().addItemBag(pl, nhantl);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được quà bù");
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 5 ô trống trong hành trang.");
        }
    }
    
    public void usehopquatet(Player player) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 2) {
                Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 3 ô trống hành trang");
                return;
            }
            short[] icon = new short[2];
            Item ruonggioto = null;
            for (Item item : player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id ==  1185) {
                    ruonggioto= item;
                    break;
                }
            }
            if (ruonggioto != null){
            int rd2 = Util.nextInt(0, 100);
            int rac2 = 60;
            int ruby2 = 15;
            int tv2 = 20;
            int ct2 = 5;          
            Item item = randomRac(true);
            if (rd2 <= rac2){
                item = randomRac(true);
            } else if (rd2 <= rac2 + ruby2){
                item = hongngocrdv2();
            } else if (rd2 <= rac2 + ruby2 + tv2) {
                item = thoivangrdv2(true);
            } else if (rd2 <= rac2 + ruby2 + tv2 + ct2) {
                item = pettet(true);
            }
            icon[0] = ruonggioto.template.iconID;
            icon[1] = item.template.iconID;
            InventoryServiceNew.gI().subQuantityItemsBag(player, ruonggioto, 1);
            InventoryServiceNew.gI().addItemBag(player, item);
            player.diemsukientet +=1;
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Item pettet(boolean rating) {
       short[] ct2 = {1200,1202,1172,1179};
        Item item = ItemService.gI().createNewItem(ct2[Util.nextInt(ct2.length - 1)], 1);
        item.itemOptions.add(new Item.ItemOption(76, 1));//VIP
        item.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5000,10000)));//ki 50%
        item.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5000,10000)));//sd 50%
        if (Util.isTrue(1, 2) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(50) + 1));//hsd
        }
        return item;
    }
    public Item randomRac(boolean rating) {
        short[] rac2 = {220,221,222,223,224};
        Item item = ItemService.gI().createNewItem(rac2[Util.nextInt(rac2.length - 1)], 1);
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(20) + 1));//hsd
       }
        return item;
    }
    public Item hongngocrdv2() {
        Item item = ItemService.gI().createNewItem((short)861);
        item.quantity = Util.nextInt(1000,1500);
        return item;
    }
    public Item thoivangrdv2(boolean rating) {
         short[] thoivangrdv2 = {15,16,17,18,19,20};
        Item item = ItemService.gI().createNewItem(thoivangrdv2[Util.nextInt(thoivangrdv2.length - 1)],30 );
       return item;
        }

    private void Openhoptop1(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 2) {
            Item danhhieu = ItemService.gI().createNewItem((short) 1350);
            Item daphale = ItemService.gI().createNewItem((short) 1399, 3);

            danhhieu.itemOptions.add(new Item.ItemOption(50, 15));
            danhhieu.itemOptions.add(new Item.ItemOption(77, 15));
            danhhieu.itemOptions.add(new Item.ItemOption(103, 15));

            daphale.itemOptions.add(new Item.ItemOption(50, 5));

            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, danhhieu);
            InventoryServiceNew.gI().addItemBag(pl, daphale);

            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được quà Top 1");
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống trong hành trang.");
        }
    }

    private void Openhoptop2(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 2) {
            Item danhhieu = ItemService.gI().createNewItem((short) 1351);
            Item daphale = ItemService.gI().createNewItem((short) 1399, 2);

            danhhieu.itemOptions.add(new Item.ItemOption(50, 10));
            danhhieu.itemOptions.add(new Item.ItemOption(77, 10));
            danhhieu.itemOptions.add(new Item.ItemOption(103, 10));

            daphale.itemOptions.add(new Item.ItemOption(50, 5));

            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, danhhieu);
            InventoryServiceNew.gI().addItemBag(pl, daphale);

            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được quà Top 2");
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống trong hành trang.");
        }
    }

    private void Openhoptop3(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 2) {
            Item danhhieu = ItemService.gI().createNewItem((short) 1352);
            Item daphale = ItemService.gI().createNewItem((short) 1399, 1);

            danhhieu.itemOptions.add(new Item.ItemOption(50, 5));
            danhhieu.itemOptions.add(new Item.ItemOption(77, 5));
            danhhieu.itemOptions.add(new Item.ItemOption(103, 5));

            daphale.itemOptions.add(new Item.ItemOption(50, 5));

            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, danhhieu);
            InventoryServiceNew.gI().addItemBag(pl, daphale);

            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được quà Top 3");
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống trong hành trang.");
        }
    }

    private void Openhopflagbag(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            int id = Util.nextInt(0, 100);
            int[] rdfl = new int[]{1157, 1203, 1204, 1205, 1206, 1207, 954, 955, 1211, 1212, 1213,
                1214, 1215, 1216, 1217, 1218, 1219, 1220, 1221, 966, 1222, 1223, 1224, 1225, 1226, 1228,
                1229, 467, 468, 469, 470, 982, 471, 983, 994, 995, 740, 996, 741, 997, 998, 999, 1000, 745,
                1001, 1007, 2035, 1013, 1021, 766, 1022, 767, 1023};
            int[] rdop = new int[]{50, 77, 103};
            int randomfl = new Random().nextInt(rdfl.length);
            int randomop = new Random().nextInt(rdop.length);
            Item fl = ItemService.gI().createNewItem((short) rdfl[randomfl]);
            Item vt = ItemService.gI().createNewItem((short) Util.nextInt(342, 345));
            if (id <= 90) {
                fl.itemOptions.add(new Item.ItemOption(rdop[randomop], Util.nextInt(5, 10)));
                fl.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 30)));
            } else {
                fl.itemOptions.add(new Item.ItemOption(rdop[randomop], Util.nextInt(5, 10)));
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, fl);
            InventoryServiceNew.gI().addItemBag(pl, vt);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + fl.template.name + " và " + vt.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
        }
    }

    private void openngocrongchest(Player pl, Item item) {
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
        if (time != 0) {
            Item itemReward = null;
            int param = item.itemOptions.get(0).param;
            int gold = 0;
            int ruby = 0;
            int quantity = 0;
            int[] listItem = {14, 15, 16, 17, 18, 19, 20};
            int[] listClothesReward;
            int[] listItemReward;
            String text = "Bạn nhận được\n";
            if (param <= 3) {
                ruby = Util.nextInt(50, 100);
            } else if (param == 4) {
                ruby = Util.nextInt(100, 150);
            } else if (param == 5) {
                ruby = Util.nextInt(150, 200);
            } else if (param == 6) {
                ruby = Util.nextInt(200, 250);
            } else {
                Item thoivang = ItemService.gI().createNewItem((short) 457, 50);
                ruby = Util.nextInt(450, 500);
                InventoryServiceNew.gI().addItemBag(pl, thoivang);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
            NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1, "Bạn nhận được\n|1|+" + ruby + " hồng ngọc", "OK [" + pl.textRuongGo.size() + "]");
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            pl.inventory.ruby += ruby;
            InventoryServiceNew.gI().sendItemBags(pl);
            PlayerService.gI().sendInfoHpMpMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Vui lòng đợi 24h");
        }
    }

    private void Openhoppet(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            int id = Util.nextInt(0, 100);
            int[] rdpet = new int[]{1311, 1312, 1313};
            int[] rdop = new int[]{50, 77, 103};
            int randompet = new Random().nextInt(rdpet.length);
            int randomop = new Random().nextInt(rdop.length);
            Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
            Item vt = ItemService.gI().createNewItem((short) Util.nextInt(342, 345));
            if (id <= 90) {
                pet.itemOptions.add(new Item.ItemOption(50, 13));
                pet.itemOptions.add(new Item.ItemOption(77, 12));
                pet.itemOptions.add(new Item.ItemOption(103, 14));
                pet.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 15)));
            } else {
                pet.itemOptions.add(new Item.ItemOption(50, 13));
                pet.itemOptions.add(new Item.ItemOption(77, 12));
                pet.itemOptions.add(new Item.ItemOption(103, 14));

            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, pet);
            InventoryServiceNew.gI().addItemBag(pl, vt);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name + " và " + vt.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
        }
    }

    private void openWoodChest(Player pl, Item item) {
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
        if (time != 0) {
            Item itemReward = null;
            int param = item.itemOptions.get(0).param;
            int gold = 0;
            int ruby = 0;
            int quantity = 0;
            int[] listItem = {441, 442, 443, 444, 445, 446, 447, 220, 221, 222, 223, 224, 225};
            int[] listClothesReward;
            int[] listItemReward;
            String text = "Bạn nhận được\n";
            if (param <= 3) {
                ruby = Util.nextInt(50, 100);
                //               listClothesReward = new int[]{randClothes(param)};
                //               listItemReward = Util.pickNRandInArr(listItem, 3);
            } else if (param == 4) {
                ruby = Util.nextInt(100, 150);
//                listClothesReward = new int[]{randClothes(param), randClothes(param)};
                ///               listItemReward = Util.pickNRandInArr(listItem, 4);
            } else if (param == 5) {
                ruby = Util.nextInt(150, 200);
//                listClothesReward = new int[]{randClothes(param), randClothes(param)};
                ///               listItemReward = Util.pickNRandInArr(listItem, 4);
            } else if (param == 6) {
                ruby = Util.nextInt(200, 250);
//                listClothesReward = new int[]{randClothes(param), randClothes(param)};
                ///               listItemReward = Util.pickNRandInArr(listItem, 4);
            } else if (param == 7) {
                ruby = Util.nextInt(250, 300);
//                listClothesReward = new int[]{randClothes(param), randClothes(param)};
                ///               listItemReward = Util.pickNRandInArr(listItem, 4);
            } else if (param == 8) {
                ruby = Util.nextInt(300, 350);
//                listClothesReward = new int[]{randClothes(param), randClothes(param)};
                ///               listItemReward = Util.pickNRandInArr(listItem, 4);
            } else if (param == 9) {
                ruby = Util.nextInt(350, 400);
//                listClothesReward = new int[]{randClothes(param), randClothes(param)};
                ///               listItemReward = Util.pickNRandInArr(listItem, 4);
            } else if (param == 10) {
                ruby = Util.nextInt(400, 450);
//                listClothesReward = new int[]{randClothes(param), randClothes(param)};
                ///               listItemReward = Util.pickNRandInArr(listItem, 4);
            } else {
                Item thoivang = ItemService.gI().createNewItem((short) 457, 300);
                ruby = Util.nextInt(450, 500);
                InventoryServiceNew.gI().addItemBag(pl, thoivang);
                InventoryServiceNew.gI().sendItemBags(pl);
                ///               listClothesReward = new int[]{randClothes(param), randClothes(param), randClothes(param)};
                //               listItemReward = Util.pickNRandInArr(listItem, 5);
                //               int ruby = Util.nextInt(1, 5);

            }
            /*            for (int i : listClothesReward) {
             itemReward = ItemService.gI().createNewItem((short) i);
             RewardService.gI().initBaseOptionClothes(itemReward.template.id, itemReward.template.type, itemReward.itemOptions);
             RewardService.gI().initStarOption(itemReward, new RewardService.RatioStar[]{new RewardService.RatioStar((byte) 1, 1, 2), new RewardService.RatioStar((byte) 2, 1, 3), new RewardService.RatioStar((byte) 3, 1, 4), new RewardService.RatioStar((byte) 4, 1, 5),});
             InventoryServiceNew.gI().addItemBag(pl, itemReward);
             pl.textRuongGo.add(text + itemReward.getInfoItem());
             }
             for (int i : listItemReward) {
             itemReward = ItemService.gI().createNewItem((short) i);
             RewardService.gI().initBaseOptionSaoPhaLe(itemReward);
             itemReward.quantity = Util.nextInt(1, 5);
             InventoryServiceNew.gI().addItemBag(pl, itemReward);
             pl.textRuongGo.add(text + itemReward.getInfoItem());
             }
             */
            NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1, "Bạn nhận được\n|1|+" + ruby + " hồng ngọc", "OK [" + pl.textRuongGo.size() + "]");
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            pl.inventory.ruby += ruby;
            InventoryServiceNew.gI().sendItemBags(pl);
            PlayerService.gI().sendInfoHpMpMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Vui lòng đợi 24h");
        }
    }

    private int randClothes(int level) {
        return LIST_ITEM_CLOTHES[Util.nextInt(0, 2)][Util.nextInt(0, 4)][level - 1];
    }

    public void UseCard(Player pl, Item item) {
        RadarCard radarTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(c -> c.Id == item.template.id).findFirst().orElse(null);
        if (radarTemplate == null) {
            return;
        }
        if (radarTemplate.Require != -1) {
            RadarCard radarRequireTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(r -> r.Id == radarTemplate.Require).findFirst().orElse(null);
            if (radarRequireTemplate == null) {
                return;
            }
            Card cardRequire = pl.Cards.stream().filter(r -> r.Id == radarRequireTemplate.Id).findFirst().orElse(null);
            if (cardRequire == null || cardRequire.Level < radarTemplate.RequireLevel) {
                Service.gI().sendThongBao(pl, "Bạn cần sưu tầm " + radarRequireTemplate.Name + " ở cấp độ " + radarTemplate.RequireLevel + " mới có thể sử dụng thẻ này");
                return;
            }
        }
        Card card = pl.Cards.stream().filter(r -> r.Id == item.template.id).findFirst().orElse(null);
        if (card == null) {
            Card newCard = new Card(item.template.id, (byte) 1, radarTemplate.Max, (byte) -1, radarTemplate.Options);
            if (pl.Cards.add(newCard)) {
                RadarService.gI().RadarSetAmount(pl, newCard.Id, newCard.Amount, newCard.MaxAmount);
                RadarService.gI().RadarSetLevel(pl, newCard.Id, newCard.Level);
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        } else {
            if (card.Level >= 2) {
                Service.gI().sendThongBao(pl, "Thẻ này đã đạt cấp tối đa");
                return;
            }
            card.Amount++;
            if (card.Amount >= card.MaxAmount) {
                card.Amount = 0;
                if (card.Level == -1) {
                    card.Level = 1;
                } else {
                    card.Level++;
                }
                Service.gI().point(pl);
            }
            RadarService.gI().RadarSetAmount(pl, card.Id, card.Amount, card.MaxAmount);
            RadarService.gI().RadarSetLevel(pl, card.Id, card.Level);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
        }
    }

    private void useItemChangeFlagBag(Player player, Item item) {
        switch (item.template.id) {
            case 994: //vỏ ốc
                break;
            case 995: //cây kem
                break;
            case 996: //cá heo
                break;
            case 997: //con diều
                break;
            case 998: //diều rồng
                break;
            case 999: //mèo mun
                if (!player.effectFlagBag.useMeoMun) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useMeoMun = !player.effectFlagBag.useMeoMun;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1000: //xiên cá
                if (!player.effectFlagBag.useXienCa) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useXienCa = !player.effectFlagBag.useXienCa;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1001: //phóng heo
                if (!player.effectFlagBag.usePhongHeo) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.usePhongHeo = !player.effectFlagBag.usePhongHeo;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
        }
        Service.gI().point(player);
        Service.gI().sendFlagBag(player);
    }

    private void changePet(Player player, Item item) {
        if (player.pet != null) {
            if (player.pet.typePet == 1) {
                int gender = player.pet.gender + 1;
                if (gender > 2) {
                    gender = 0;
                }
                PetService.gI().changeMabuPet(player, gender);
                InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            } else {
                int gender = player.pet.gender + 1;
                if (gender > 2) {
                    gender = 0;
                }
                PetService.gI().changeNormalPet(player, gender);
                InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            }
        } else {
            Service.gI().sendThongBao(player, "Bạn không có đệ tử");
        }
    }

    private void openPhieuCaiTrangHaiTac(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            Item ct = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
            ct.itemOptions.add(new ItemOption(147, 3));
            ct.itemOptions.add(new ItemOption(77, 3));
            ct.itemOptions.add(new ItemOption(103, 3));
            ct.itemOptions.add(new ItemOption(149, 0));
            if (item.template.id == 2006) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            } else if (item.template.id == 2007) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 30)));
            }
            InventoryServiceNew.gI().addItemBag(pl, ct);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.gI().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }

    private void openCSKB(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {13, 14, 16, 190, 381, 382, 383, 384, 385};
            int[][] gold = {{1000000, 2000000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openHopTangNgoc(Player pl, Item item) {
        Input.gI().createFormTangNgocHong(pl);
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
    }

    private void useItemHopQuaTanThu(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {14, 16, 17, 18, 19, 20, 21, 22};
            int[][] gold = {{100000000, 200000000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useItemTime(Player pl, Item item) {
        switch (item.template.id) {
            case 382: //bổ huyết
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;
                break;
            case 383: //bổ khí
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                break;
            case 384: //giáp xên
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                break;
            case 381: //cuồng nộ
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                Service.gI().point(pl);
                break;
            case 385: //ẩn danh
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case 379: //máy dò capsule
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;
            case 1099:// cn
                pl.itemTime.lastTimeCuongNo2 = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo2 = true;
                Service.gI().point(pl);

                break;
            case 1100:// bo huyet
                pl.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet2 = true;
                break;
            case 1101://bo khi
                pl.itemTime.lastTimeBoKhi2 = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi2 = true;
                break;
            case 1102://xbh
                pl.itemTime.lastTimeGiapXen2 = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen2 = true;
                break;
            case 1103://an danh
                pl.itemTime.lastTimeAnDanh2 = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh2 = true;
                break;
            case 1400:
                pl.itemTime.lastTimedkhi = System.currentTimeMillis();
                pl.itemTime.isdkhi = true;
                break;
            case 1406:
                pl.itemTime.lastTimedcarot = System.currentTimeMillis();
                pl.itemTime.isdcarot = true;
                break;
            case 663: //bánh pudding
            case 664: //xúc xíc
            case 665: //kem dâu
            case 666: //mì ly
            case 667: //sushi
                pl.itemTime.lastTimeEatMeal = System.currentTimeMillis();
                pl.itemTime.isEatMeal = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal);
                pl.itemTime.iconMeal = item.template.iconID;
                break;
            case 1142: //thuc ăn cho dog
                pl.itemTime.lastTimeDeTu = System.currentTimeMillis();
                pl.itemTime.isUseItemDeTu = true;
                break;
            case 2037: //máy dò đồ
                pl.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis();
                pl.itemTime.isUseMayDo2 = true;
                break;

        }
        Service.gI().point(pl);
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
            switch (tempId) {
                case SummonDragon.NGOC_RONG_1_SAO:
                case SummonDragon.NGOC_RONG_2_SAO:
                case SummonDragon.NGOC_RONG_3_SAO:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13));
//                    Service.gI().sendThongBao(pl, "NONOOO");

                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON,
                            -1, "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        }
    }
    
    private void controllerCalltrb(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONGTRB1 && tempId <= SummonDragon.NGOC_RONGTRB7) {
            switch (tempId) {
                case SummonDragon.NGOC_RONGTRB1:
                    SummonDragon.gI().openMenuSummonShenronTRB(pl, (byte) (tempId - 1813));
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGONTRB,
                            -1, "Bạn chỉ có thể gọi rồng siêu cấp 1 sao ", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        }
    }
    private void controllerCalltrb1(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONGTRB11 && tempId <= SummonDragon.NGOC_RONGTRB77) {
            switch (tempId) {
                case SummonDragon.NGOC_RONGTRB11:
                    SummonDragon.gI().openMenuSummonShenronTRB1(pl, (byte) (tempId - 1820));
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGONTRB1,
                            -1, "Bạn chỉ có thể gọi rồng băng 1 sao ", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        }
    }

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {
                String[] subName = item.template.name.split("");
                byte level = Byte.parseByte(subName[subName.length - 1]);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                if (curSkill.point == 7) {
                    Service.gI().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (level == 1) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.gI().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Skill skillNeed = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            Service.gI().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " cấp " + skillNeed.point + " trước!");
                        }
                    } else {
                        if (curSkill.point + 1 == level) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            //System.out.println(curSkill.template.name + " - " + curSkill.point);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.gI().messageSubCommand((byte) 62);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Service.gI().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp " + (curSkill.point + 1) + " trước!");
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(pl);
                }
            } else {
                Service.gI().sendThongBao(pl, "Không thể thực hiện");
            }
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);
        }
    }
    private void useCanCau(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            if (pl.zone.map.mapId != 190) {
                Service.gI().sendThongBao(pl, "Hãy đến đảo kho báu để sử dụng cần câu!");
                return;
            }
            if (pl.itemTime.isCauCa) {
                Service.gI().sendThongBao(pl, "Đang trong thời gian đợi cá cắn!");
                return;
            }
            Item moicau = null;
            int durCancau = 0;
            int lvlCanCau = 0;
            try {
                moicau = InventoryServiceNew.gI().findItemBag(pl, 1400);
            } catch (Exception e) {
                // catch loi lam deo gi
            }
            if (moicau == null) {
                Service.gI().sendThongBao(pl, "Hết mồi câu rồi!");
                return;
            }

            for (ItemOption io : item.itemOptions) {
                switch (io.optionTemplate.id) {
                    case 240:
                        durCancau = io.param;
                        break;
                    case 72:
                        lvlCanCau = io.param;
                        break;
                }
            }

            if (durCancau == 0) {
                Service.gI().sendThongBao(pl, "Cần câu đã hư rồi, cần được sửa chữa!");
                return;
            }
            pl.lastTimeCauCa = System.currentTimeMillis();
            ItemTimeService.gI().sendTextCauCa(pl);
            pl.itemTime.lastTimeCauCa = System.currentTimeMillis();
            pl.itemTime.isCauCa = true;
            pl.rateCauCa = lvlCanCau;
            ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconCauCa);
            pl.itemTime.iconCauCa = item.template.iconID;
            Service.getInstance().point(pl);

            durCancau--;
            item.itemOptions.clear();
            item.itemOptions.add(new ItemOption(72, lvlCanCau));
            item.itemOptions.add(new ItemOption(247, durCancau));
            InventoryServiceNew.gI().subQuantityItemsBag(pl, moicau, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }

    private void usePorata(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 8 || pl.fusion.typeFusion == 10 || pl.fusion.typeFusion == 12) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata2(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 10 || pl.fusion.typeFusion == 12) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion2(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata3(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 8 || pl.fusion.typeFusion == 12) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion3(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata4(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 8 || pl.fusion.typeFusion == 10) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion4(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usehpbuff(Player pl) {
        Item hpbuff = null;
        for (Item item : pl.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 1153) {
                hpbuff = item;
                break;
            }
        }
        if (hpbuff != null) {
            pl.nPoint.hpg += HP_BUFF;
            InventoryServiceNew.gI().subQuantityItemsBag(pl, hpbuff, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBaoOK(pl, "HP Gốc của bạn đã tăng  " + HP_BUFF);
            Service.getInstance().point(pl);
        }
    }

    private void usesdbuff(Player pl) {
        Item sdbuff = null;
        for (Item item : pl.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 1152) {
                sdbuff = item;
                break;
            }
        }
        if (sdbuff != null) {
            pl.nPoint.dameg += SD_BUFF;
            InventoryServiceNew.gI().subQuantityItemsBag(pl, sdbuff, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBaoOK(pl, "SD Gốc của bạn đã tăng  " + SD_BUFF);
            Service.getInstance().point(pl);
        }
    }

    private void usekibuff(Player pl) {
        Item kibuff = null;
        for (Item item : pl.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 1154) {
                kibuff = item;
                break;
            }
        }
        if (kibuff != null) {
            pl.nPoint.mpg += MP_BUFF;
            InventoryServiceNew.gI().subQuantityItemsBag(pl, kibuff, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBaoOK(pl, "KI Gốc của bạn đã tăng  " + MP_BUFF);
            Service.getInstance().point(pl);
        }
    }

    public void usethoivang(Player player) {
        Item tv = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 457) {
                tv = item;
                break;
            }
        }
        if (tv != null) {
            if (player.inventory.gold <= LIMIT_GOLD) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                player.inventory.gold += 500000000;
                PlayerService.gI().sendInfoHpMpMoney(player);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Đã đạt giới hạn vàng");
            }
        }
    }

    private void openCapsuleUI(Player pl) {
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }

    public void choseMapCapsule(Player pl, int index) {
        int zoneId = -1;
        Zone zoneChose = pl.mapCapsule.get(index);
        //Kiểm tra số lượng người trong khu

        if (zoneChose.getNumOfPlayers() > 25
                || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                || MapService.gI().isMapGiaiCuuMiNuong(zoneChose.map.mapId)
                || MapService.gI().isMapBanDoKhoBau(zoneChose.map.mapId)
                || MapService.gI().isMapKhiGas(zoneChose.map.mapId)
                || MapService.gI().isMapVodai(zoneChose.map.mapId)
                || MapService.gI().isMapDiaNguc(zoneChose.map.mapId)
                || MapService.gI().isMapSatan(zoneChose.map.mapId)
                || MapService.gI().isMapConDuongRanDoc(zoneChose.map.mapId)
                || MapService.gI().isMapMaBu(zoneChose.map.mapId)) {
            Service.gI().sendThongBao(pl, "Hiện tại không thể vào được khu!");
            return;
        }
        if (zoneChose.map.mapId == 189 || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                || MapService.gI().isMapGiaiCuuMiNuong(zoneChose.map.mapId)
                || MapService.gI().isMapBanDoKhoBau(zoneChose.map.mapId)) {
            Service.gI().sendThongBaoOK(pl, "Map này không thể khứ hồi!!");
            return;
        } else {
            if (index != 0 || zoneChose.map.mapId == 21
                    || zoneChose.map.mapId == 22
                    || zoneChose.map.mapId == 23) {
                pl.mapBeforeCapsule = pl.zone;
            } else {
                zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
                pl.mapBeforeCapsule = null;
            }
            ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
        }
    }

    public void eatPea(Player player) {
        Item pea = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == 6) {
                pea = item;
                break;
            }
        }
        if (pea != null) {
            int hpKiHoiPhuc = 0;

            int lvPea = 0;

            if (pea.template.id == 1151) {
                lvPea = 10;
            } else {
                lvPea = Integer.parseInt(pea.template.name.substring(13));
            }
            for (Item.ItemOption io : pea.itemOptions) {
                if (io.optionTemplate.id == 2) {
                    hpKiHoiPhuc = io.param * 1000;
                    break;
                }
                if (io.optionTemplate.id == 48) {
                    hpKiHoiPhuc = io.param;
                    break;
                }
            }
            player.nPoint.setHp(player.nPoint.hp + hpKiHoiPhuc);
            player.nPoint.setMp(player.nPoint.mp + hpKiHoiPhuc);
            PlayerService.gI().sendInfoHpMp(player);
            Service.gI().sendInfoPlayerEatPea(player);
            if (player.pet != null && player.zone.equals(player.pet.zone) && !player.pet.isDie()) {
                int statima = 100 * lvPea;
                player.pet.nPoint.stamina += statima;
                if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
                    player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
                }
                player.pet.nPoint.setHp(player.pet.nPoint.hp + hpKiHoiPhuc);
                player.pet.nPoint.setMp(player.pet.nPoint.mp + hpKiHoiPhuc);
                Service.gI().sendInfoPlayerEatPea(player.pet);
                Service.gI().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã cho con đậu thần");
            }

            InventoryServiceNew.gI().subQuantityItemsBag(player, pea, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        }
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: //skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 403: //skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 404: //skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 759: //skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;

            }

        } catch (Exception e) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    private void ItemSKH(Player pl, Item item) {//hop qua skh
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void ItemDHD(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void Hopts(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }

}
