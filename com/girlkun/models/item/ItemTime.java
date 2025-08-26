package com.girlkun.models.item;

import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.player.NPoint;
import com.girlkun.models.player.Player;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import com.girlkun.services.ItemTimeService;

public class ItemTime {

    //id item text
    public static final byte DOANH_TRAI = 0;
    public static final byte BAN_DO_KHO_BAU = 1;
    public static final byte KHI_GASS = 2;
    public static final byte CON_DUONG_RAN_DOC = 3;
    public static final byte GIAI_CUU_MI_NUONG = 4;

    public static final int TIME_ITEM = 600000;
    public static final int TIME_OPEN_POWER = 86400000;
    public static final int TIME_MAY_DO = 1800000;
    public static final int TIME_MAY_DO2 = 1800000;
    public static final int TIME_EAT_MEAL = 600000;
    public static final int TIME_DUOI_KHI = 600000;
    public boolean isCauCa;
    public static final int TIME_BROLY = 300000;
    private Player player;

    public boolean isUseBoHuyet;
    public boolean isUseBoKhi;
    public boolean isUseGiapXen;
    public boolean isUseCuongNo;
    public boolean isUseAnDanh;
    public boolean isUseBoHuyet2;
    public boolean isUseBoKhi2;
    public boolean isUseGiapXen2;
    public boolean isUseCuongNo2;
    public boolean isUseAnDanh2;
    public boolean isUseDuoiKhi;
    public boolean isUse;

    public boolean isUseMaydoCua;
    public boolean isUseMaydoTom;
    public boolean isUseMaydoCa;
    public boolean isUseMaydoOc;

    public long lastTimeBoHuyet;
    public long lastTimeBoKhi;
    public long lastTimeGiapXen;
    public long lastTimeCuongNo;
    public long lastTimeAnDanh;

    public boolean isUseItemDeTu;
    public long lastTimeDeTu;

    public long lastTimeMaydoOc;
    public long lastTimeMaydoCua;
    public long lastTimeMaydoCa;
    public long lastTimeMaydoTom;

    public long lastTimeBoHuyet2;
    public long lastTimeBoKhi2;
    public long lastTimeGiapXen2;
    public long lastTimeCuongNo2;
    public long lastTimeAnDanh2;

    public boolean isUseMayDo;
    public long lastTimeUseMayDo;//lastime de chung 1 cai neu time = nhau
    public boolean isUseMayDo2;
    public long lastTimeUseMayDo2;

    public boolean isOpenPower;
    public long lastTimeOpenPower;

    public boolean isUseTDLT;
    public long lastTimeUseTDLT;
    public int timeTDLT;

    public boolean isEatMeal;
    public long lastTimeEatMeal;
    public int iconMeal;

    public boolean isdkhi;
    public long lastTimedkhi;
    public int icondkhi;
    public boolean isdcarot;
    public long lastTimedcarot;
    public long lastTimeCauCa;
    public int iconCauCa;
    public static final byte CAU_CA = 60;

    public ItemTime(Player player) {
        this.player = player;
    }

    public void update() {
        if (isCauCa) {
            if (Util.canDoWithTime(lastTimeCauCa, 10000)) {
                isCauCa = false;
                int[] listCa1 = new int[] { 1416, 1415, 1406, 2000 + player.gender, 14, 1416, 1415, 1406, 1398, 1397, };// ca
                                                                                                                        // loai
                                                                                                                        // 1
                                                                                                                        // xin
                                                                                                                        // xo
                                                                                                                        // vcl
                int[] listCa2 = new int[] { 1407, 1409, 1417, 1412, 1078, 1084, 1085, 1086, 1407, 1409, 1417, 1412, 15,
                        1358, 1359, 1360, 1361, };// ca loai 2 cung hoi xin
                int[] listCa3 = new int[] { 1404, 1403, 1405, 1411, 1418, 16, 1066, 1067, 1404, 1403, 1405, 1411, 1418,
                        1068, 1069, 1070 };// ca loai 3 tam trung
                int[] listCa4 = new int[] { 1410, 1408, 1402, 1401, 1414, 1413, 1201, 1202, 1203, 1178, 1179, 1180,
                        1181, 1182 };// ca loai 4 phen
                int[] listCa5 = new int[] { 380, 663, 664, 664, 666, 667, 220, 221, 222, 223, 224, 987 };// rac
                int rd = Util.nextInt(0, 180-player.rateCauCa*2);
                if (rd <= 2) {
                    Item ca1 = ItemService.gI().createNewItem((short) listCa1[Util.nextInt(listCa1.length)]);
                    ca1.itemOptions.add(new ItemOption(30, 0));
                    InventoryServiceNew.gI().addItemBag(player, ca1);
                    player.chat("|7|Vừa câu được " + ca1.template.name + ", xịn vãi l! ");
                    InventoryServiceNew.gI().sendItemBags(player);
                    player.pointCauCa += 30;

                } else if (rd <= 5) {
                    Item ca2 = ItemService.gI().createNewItem((short) listCa2[Util.nextInt(listCa2.length)]);
                    ca2.itemOptions.add(new ItemOption(30, 0));
                    InventoryServiceNew.gI().addItemBag(player, ca2);
                    player.chat("|5|Vừa câu được " + ca2.template.name + ", cũng xịn ! ");
                    InventoryServiceNew.gI().sendItemBags(player);
                    player.pointCauCa += 20;

                } else if (rd <= 25) {
                    Item ca3 = ItemService.gI().createNewItem((short) listCa3[Util.nextInt(listCa1.length)]);
                    ca3.itemOptions.add(new ItemOption(30, 0));
                    InventoryServiceNew.gI().addItemBag(player, ca3);
                    player.chat("|4|Vừa câu được " + ca3.template.name + ", cũng thường thôi ! ");
                    InventoryServiceNew.gI().sendItemBags(player);
                    player.pointCauCa += 10;

                } else if (rd <= 50) {
                    Item ca4 = ItemService.gI().createNewItem((short) listCa4[Util.nextInt(listCa4.length)]);
                    ca4.itemOptions.add(new ItemOption(30, 0));
                    InventoryServiceNew.gI().addItemBag(player, ca4);
                    player.chat("|3|Vừa câu được " + ca4.template.name + ", tàm tạm ! ");
                    InventoryServiceNew.gI().sendItemBags(player);
                    player.pointCauCa += 5;

                } else {
                    Item ca5 = ItemService.gI().createNewItem((short) listCa5[Util.nextInt(listCa5.length)]);
                    
                    InventoryServiceNew.gI().addItemBag(player, ca5);
                    player.chat("Vừa câu được " + ca5.template.name + ", đen rồi !");
                    InventoryServiceNew.gI().sendItemBags(player);
                    player.pointCauCa += 1;

                }
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }
        if (isEatMeal) {
            if (Util.canDoWithTime(lastTimeEatMeal, TIME_EAT_MEAL)) {
                isEatMeal = false;
                Service.gI().point(player);
            }
        }
        if (isUseBoHuyet) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
                isUseBoHuyet = false;
                Service.gI().point(player);
            }
        }

        if (isUseBoKhi) {
            if (Util.canDoWithTime(lastTimeBoKhi, TIME_ITEM)) {
                isUseBoKhi = false;
                Service.gI().point(player);
            }
        }

        if (isUseGiapXen) {
            if (Util.canDoWithTime(lastTimeGiapXen, TIME_ITEM)) {
                isUseGiapXen = false;
            }
        }
        if (isUseCuongNo) {
            if (Util.canDoWithTime(lastTimeCuongNo, TIME_ITEM)) {
                isUseCuongNo = false;
                Service.gI().point(player);
            }
        }
        if (isUseAnDanh) {
            if (Util.canDoWithTime(lastTimeAnDanh, TIME_ITEM)) {
                isUseAnDanh = false;
            }
        }

        if (isUseBoHuyet2) {
            if (Util.canDoWithTime(lastTimeBoHuyet2, TIME_ITEM)) {
                isUseBoHuyet2 = false;
                Service.gI().point(player);
            }
        }

        if (isUseBoKhi2) {
            if (Util.canDoWithTime(lastTimeBoKhi2, TIME_ITEM)) {
                isUseBoKhi2 = false;
                Service.gI().point(player);
            }
        }
        if (isUseGiapXen2) {
            if (Util.canDoWithTime(lastTimeGiapXen2, TIME_ITEM)) {
                isUseGiapXen2 = false;
            }
        }
        if (isUseCuongNo2) {
            if (Util.canDoWithTime(lastTimeCuongNo2, TIME_ITEM)) {
                isUseCuongNo2 = false;
                Service.gI().point(player);
            }
        }
        if (isUseAnDanh2) {
            if (Util.canDoWithTime(lastTimeAnDanh2, TIME_ITEM)) {
                isUseAnDanh2 = false;
            }
        }
        if (isdkhi) {
            if (Util.canDoWithTime(lastTimedkhi, TIME_DUOI_KHI)) {
                isdkhi = false;
            }
        }
        if (isdcarot) {
            if (Util.canDoWithTime(lastTimedcarot, TIME_DUOI_KHI)) {
                isdcarot = false;
            }
        }
        if (isOpenPower) {
            if (Util.canDoWithTime(lastTimeOpenPower, TIME_OPEN_POWER)) {
                player.nPoint.limitPower++;
                if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                    player.nPoint.limitPower = NPoint.MAX_LIMIT;
                }
                Service.gI().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
                isOpenPower = false;
            }
        }
        if (isUseMayDo) {
            if (Util.canDoWithTime(lastTimeUseMayDo, TIME_MAY_DO)) {
                isUseMayDo = false;
            }
        }
        if (isUseMayDo2) {
            if (Util.canDoWithTime(lastTimeUseMayDo2, TIME_MAY_DO2)) {
                isUseMayDo2 = false;
            }
        }
        if (isUseItemDeTu) {
            if (Util.canDoWithTime(lastTimeDeTu, TIME_ITEM)) {
                isUseItemDeTu = false;
            }
        }
        if (isUseTDLT) {
            if (Util.canDoWithTime(lastTimeUseTDLT, timeTDLT)) {
                this.isUseTDLT = false;
                ItemTimeService.gI().sendCanAutoPlay(this.player);
            }
        }
    }

    public void dispose() {
        this.player = null;
    }
}
