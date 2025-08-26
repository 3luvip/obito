package com.girlkun.jdbc.daos;

import com.girlkun.card.Card;
import com.girlkun.database.GirlkunDB;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.ItemTime;
import com.girlkun.models.player.Friend;
import com.girlkun.models.player.Fusion;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Manager;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.MapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
//import static sun.audio.AudioPlayer.player;

public class PlayerDAO {

    public static boolean createNewPlayer(int userId, String name, byte gender, int hair) {
        try {
            JSONArray dataArray = new JSONArray();

            dataArray.add(200000); //vàng
            dataArray.add(20); //ngọc xanh
            dataArray.add(0); //hồng ngọc
            dataArray.add(0); //point
            dataArray.add(0); //event
            // by obito
            dataArray.add(0); //skien
            dataArray.add(0); //skien1
            dataArray.add(0); //skien2
            dataArray.add(0); //skien3
            dataArray.add(0); //skien4
            dataArray.add(0); //skien5
            
            String inventory = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(21 + gender); // map
            dataArray.add(100); // x
            dataArray.add(336); // y
            String location = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //giới hạn sức mạnh
            dataArray.add(2000); //sức mạnh
            dataArray.add(2000); //tiềm năng
            dataArray.add(1000); //thể lực
            dataArray.add(1000); //thể lực đầy
            dataArray.add(gender == 0 ? 200 : 100); //hp gốc
            dataArray.add(gender == 1 ? 200 : 100); //ki gốc
            dataArray.add(gender == 2 ? 30 : 25); //sức đánh gốc
            dataArray.add(0); //giáp gốc
            dataArray.add(0); //chí mạng gốc
            dataArray.add(0); //năng động
            dataArray.add(gender == 0 ? 200 : 100); //hp hiện tại
            dataArray.add(gender == 1 ? 200 : 100); //ki hiện tại
            String point = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(1); //level
            dataArray.add(5); //curent pea
            dataArray.add(0); //is upgrade
            dataArray.add(new Date().getTime()); //last time harvest
            dataArray.add(new Date().getTime()); //last time upgrade
            String magicTree = dataArray.toJSONString();
            dataArray.clear();
            /**
             *
             * [
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"-1","option":[],"create_time":"0""}, ... ]
             */

            int idAo = gender == 0 ? 0 : gender == 1 ? 1 : 2;
            int idQuan = gender == 0 ? 6 : gender == 1 ? 7 : 8;
            int def = gender == 2 ? 3 : 2;
            int hp = gender == 0 ? 30 : 20;

            JSONArray item = new JSONArray();
            JSONArray options = new JSONArray();
            JSONArray opt = new JSONArray();
            for (int i = 0; i < 12; i++) {
                if (i == 0) { //áo
                    opt.add(47); //id option
                    opt.add(def); //param option
                    item.add(idAo); //id item
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else if (i == 1) { //quần
                    opt.add(6); //id option
                    opt.add(hp); //param option
                    item.add(idQuan); //id item
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else {
                    item.add(-1); //id item
                    item.add(0); //số lượng
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBody = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 20; i++) { // item tạo player 
//                if (i == 0) { //Capsule Vip
//                    opt.add(30); //id option cấm giao dịch
//                    opt.add(1); //param option
//                    item.add(194); //id item
//                    item.add(1); //số lượng
//                    options.add(opt.toJSONString());
//                    opt.clear();
                if (i == 1) { // Thỏi vàng
                    opt.add(30); //id option
                    opt.add(1); //param option
                    item.add(457); //id item
                    item.add(20); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else {
                    item.add(-1); //id item
                    item.add(0); //số lượng
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBag = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 20; i++) {
                if (i == 0) { //rada
                    opt.add(14); //id option
                    opt.add(1); //param option
                    item.add(12); //id item
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else {
                    item.add(-1); //id item
                    item.add(0); //số lượng
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBox = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 110; i++) {
                item.add(-1); //id item
                item.add(0); //số lượng
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBoxLuckyRound = dataArray.toJSONString();
            dataArray.clear();

            String friends = dataArray.toJSONString();
            String enemies = dataArray.toJSONString();

            dataArray.add(0); //id nội tại
            dataArray.add(0); //chỉ số 1
            dataArray.add(0); //chỉ số 2
            dataArray.add(0); //số lần mở
            String intrinsic = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //bổ huyết
            dataArray.add(0); //bổ khí
            dataArray.add(0); //giáp xên
            dataArray.add(0); //cuồng nộ
            dataArray.add(0); //ẩn danh
            dataArray.add(0); //mở giới hạn sức mạnh
            dataArray.add(0); //máy dò
            dataArray.add(0); //máy dò2
            dataArray.add(0); //thức ăn cold
            dataArray.add(0); //icon thức ăn cold
            dataArray.add(0); // tdlt
            String itemTime = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //bổ huyết2
            dataArray.add(0); //bổ khí2
            dataArray.add(0); //giáp xên2
            dataArray.add(0); //cuồng nộ2
            dataArray.add(0); //ẩn danh2
            String itemTimeSC = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); // máy dò cua
            dataArray.add(0); //Tôm
            dataArray.add(0); //Oocs
            dataArray.add(0); //Cá
            String itemMayDo = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(18); //id nhiệm vụ
            dataArray.add(0); //index nhiệm vụ con
            dataArray.add(0); //số lượng đã làm
            String task = dataArray.toJSONString();
            dataArray.clear();

            String mabuEgg = dataArray.toJSONString();
            String billEgg = dataArray.toJSONString();

            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ
            dataArray.add(System.currentTimeMillis()); //bùa mạnh mẽ
            dataArray.add(System.currentTimeMillis()); //bùa da trâu
            dataArray.add(System.currentTimeMillis()); //bùa oai hùng
            dataArray.add(System.currentTimeMillis()); //bùa bất tử
            dataArray.add(System.currentTimeMillis()); //bùa dẻo dai
            dataArray.add(System.currentTimeMillis()); //bùa thu hút
            dataArray.add(System.currentTimeMillis()); //bùa đệ tử
            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ x3
            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ x4
            String charms = dataArray.toJSONString();
            dataArray.clear();

            int[] skillsArr = gender == 0 ? new int[] { 0, 1, 6, 9, 10, 20, 22, 24, 19 , 28}
                    : gender == 1 ? new int[] { 2, 3, 7, 11, 12, 17, 18, 26, 19 ,28}
                            : new int[] { 4, 5, 8, 13, 14, 21, 23, 25, 19 , 28} ;
            // [{"temp_id":"4","point":0,"last_time_use":0},]

            JSONArray skill = new JSONArray();
            for (int i = 0; i < skillsArr.length; i++) {
                skill.add(skillsArr[i]); // id skill
                if (i == 0) {
                    skill.add(1); // level skill
                } else {
                    skill.add(0); // level skill
                }
                skill.add(0); // thời gian sử dụng trước đó
                dataArray.add(skill.toString());
                skill.clear();
            }
            String skills = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(gender == 0 ? 0 : gender == 1 ? 2 : 4);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            String skillsShortcut = dataArray.toJSONString();
            dataArray.clear();

            String petData = dataArray.toJSONString();

            JSONArray blackBall = new JSONArray();
            for (int i = 1; i <= 7; i++) {
                blackBall.add(0);
                blackBall.add(0);
                blackBall.add(0);
                dataArray.add(blackBall.toJSONString());
                blackBall.clear();
            }
            String dataBlackBall = dataArray.toString();
            dataArray.clear();

            dataArray.add(0); //id nội tại
            dataArray.add(0); //chỉ số 1
            String dataoff = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(-1); //id side task
            dataArray.add(0); //thời gian nhận
            dataArray.add(0); //số lượng đã làm
            dataArray.add(0); //số lượng cần làm
            dataArray.add(20); //số nhiệm vụ còn lại có thể nhận
            dataArray.add(0); //mức độ nhiệm vụ
            String dataSideTask = dataArray.toJSONString();
            dataArray.clear();

            String data_card = dataArray.toJSONString();
            String bill_data = dataArray.toJSONString();
            JSONObject achievementObject = new JSONObject();
            achievementObject.put("numPvpWin", 0);
            achievementObject.put("numSkillChuong", 0);
            achievementObject.put("numFly", 0);
            achievementObject.put("numKillMobFly", 0);
            achievementObject.put("numKillNguoiRom", 0);
            achievementObject.put("numHourOnline", 0);
            achievementObject.put("numGivePea", 0);
            achievementObject.put("numSellItem", 0);
            achievementObject.put("numPayMoney", 0);
            achievementObject.put("numKillSieuQuai", 0);
            achievementObject.put("numHoiSinh", 0);
            achievementObject.put("numSkillDacBiet", 0);
            achievementObject.put("numPickGem", 0);

            List<Boolean> list = new ArrayList<>(Arrays.asList(new Boolean[Manager.ACHIEVEMENTS.size()]));
            Collections.fill(list, Boolean.FALSE);
            dataArray.addAll(list);
            achievementObject.put("listReceiveGem", dataArray);
            String info_achive = achievementObject.toJSONString();

            String info_phoban = "[]";
            GirlkunDB.executeUpdate("insert into player"
                    + "(account_id, name, head, gender, have_tennis_space_ship, clan_id_sv" + Manager.SERVER + ", "
                    + "data_inventory, data_location, data_point, data_magic_tree, items_body, "
                    + "items_bag, items_box, items_box_lucky_round, friends, enemies, data_intrinsic, data_item_time, data_item_time_sieu_cap,"
                    + "data_task, data_mabu_egg, data_charm, skills, skills_shortcut, pet,"
                    + "data_black_ball, data_side_task, violate, data_card, bill_data, info_phoban, data_may_do, info_achievement, congduc, data_offtrain) "
                    + "values ()", userId, name, hair, gender, 0, -1, inventory, location, point, magicTree,
                    itemsBody, itemsBag, itemsBox, itemsBoxLuckyRound, friends, enemies, intrinsic,
                    itemTime, itemTimeSC, task, mabuEgg, charms, skills, skillsShortcut, petData, dataBlackBall, dataSideTask,0, data_card, bill_data, info_phoban, itemMayDo, info_achive, 0, dataoff);
            Logger.success(Logger.PURPLE + "Tạo player mới thành công!\n");
            return true;
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi tạo player mới");
            return false;
        }
    }

    public static void updatePlayer(Player player) {
        if (player.iDMark.isLoadedAllDataPlayer()) {
            long st = System.currentTimeMillis();
            try {
                JSONArray dataArray = new JSONArray();

                //data kim lượng
                dataArray.add(player.inventory.gold > Inventory.LIMIT_GOLD
                        ? Inventory.LIMIT_GOLD : player.inventory.gold);
                dataArray.add(player.inventory.gem);
                dataArray.add(player.inventory.ruby);
                dataArray.add(player.inventory.coupon);
                dataArray.add(player.inventory.event);
                dataArray.add(player.inventory.skien);
                dataArray.add(player.inventory.skien1);
                dataArray.add(player.inventory.skien2);
                dataArray.add(player.inventory.skien3);
                dataArray.add(player.inventory.skien4);
                dataArray.add(player.inventory.skien5);
                String inventory = dataArray.toJSONString();
                dataArray.clear();

                int mapId = -1;
                mapId = player.mapIdBeforeLogout;
                int x = player.location.x;
                int y = player.location.y;
                int hp = Util.TamkjllGH(player.nPoint.hp);
                int mp = Util.TamkjllGH(player.nPoint.mp);
                if (player.isDie()) {
                    mapId = player.gender + 21;
                    x = 300;
                    y = 336;
                    hp = 1;
                    mp = 1;
                } else {
                    if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                            || MapService.gI().isMapBanDoKhoBau(mapId)
                            || MapService.gI().isMapMaBu(mapId)
                            || MapService.gI().isMapVodai(mapId)
                            || MapService.gI().isMapKhiGas(mapId)
                            || MapService.gI().isMapConDuongRanDoc(mapId)
                            || MapService.gI().isMapGiaiCuuMiNuong(mapId)
                            || MapService.gI().isMapSatan(mapId)) {
                        mapId = player.gender + 21;
                        x = 300;
                        y = 336;
                    }
                }

                //data vị trí
                dataArray.add(mapId);
                dataArray.add(x);
                dataArray.add(y);
                String location = dataArray.toJSONString();
                dataArray.clear();

                //data vị trí
                dataArray.add(player.typetrain);
                dataArray.add(player.istrain ? 1 : 0);
                String dataoff = dataArray.toJSONString();
                dataArray.clear();
                //data chỉ số
                dataArray.add(player.nPoint.limitPower);
                dataArray.add(player.nPoint.power);
                dataArray.add(player.nPoint.tiemNang);
                dataArray.add(player.nPoint.stamina);
                dataArray.add(player.nPoint.maxStamina);
                dataArray.add(player.nPoint.hpg);
                dataArray.add(player.nPoint.mpg);
                dataArray.add(player.nPoint.dameg);
                dataArray.add(player.nPoint.defg);
                dataArray.add(player.nPoint.critg);
                dataArray.add(0);
                dataArray.add(hp);
                dataArray.add(mp);
                String point = dataArray.toJSONString();
                dataArray.add(player.numKillSieuHang);
                dataArray.add(player.rankSieuHang);
                dataArray.clear();

                //data đậu thần
                dataArray.add(player.magicTree.level);
                dataArray.add(player.magicTree.currPeas);
                dataArray.add(player.magicTree.isUpgrade ? 1 : 0);
                dataArray.add(player.magicTree.lastTimeHarvest);
                dataArray.add(player.magicTree.lastTimeUpgrade);
                String magicTree = dataArray.toJSONString();
                dataArray.clear();

                //data body
                JSONArray dataItem = new JSONArray();
                for (Item item : player.inventory.itemsBody) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 11) {
                                opt.add(io.optionTemplate.id);
                                opt.add(player.congduc);
                            } else {
                                opt.add(io.optionTemplate.id);
                                opt.add(io.param);
                            }
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBody = dataArray.toJSONString();
                dataArray.clear();

                //data bag
                for (Item item : player.inventory.itemsBag) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 11) {
                                opt.add(io.optionTemplate.id);
                                opt.add(player.congduc);
                            } else {
                                opt.add(io.optionTemplate.id);
                                opt.add(io.param);
                            }
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBag = dataArray.toJSONString();
                dataArray.clear();

                //data card
                //data box
                for (Item item : player.inventory.itemsBox) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 11) {
                                opt.add(io.optionTemplate.id);
                                opt.add(player.congduc);
                            } else {
                                opt.add(io.optionTemplate.id);
                                opt.add(io.param);
                            }
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBox = dataArray.toJSONString();
                dataArray.clear();

                //data box crack ball
                for (Item item : player.inventory.itemsBoxCrackBall) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBoxLuckyRound = dataArray.toJSONString();
                dataArray.clear();

                //data bạn bè
                JSONArray dataFE = new JSONArray();
                for (Friend f : player.friends) {
                    dataFE.add(f.id);
                    dataFE.add(f.name);
                    dataFE.add(f.head);
                    dataFE.add(f.body);
                    dataFE.add(f.leg);
                    dataFE.add(f.bag);
                    dataFE.add(f.power);
                    dataArray.add(dataFE.toJSONString());
                    dataFE.clear();
                }
                String friend = dataArray.toJSONString();
                dataArray.clear();

                //data kẻ thù
                for (Friend e : player.enemies) {
                    dataFE.add(e.id);
                    dataFE.add(e.name);
                    dataFE.add(e.head);
                    dataFE.add(e.body);
                    dataFE.add(e.leg);
                    dataFE.add(e.bag);
                    dataFE.add(e.power);
                    dataArray.add(dataFE.toJSONString());
                    dataFE.clear();
                }
                String enemy = dataArray.toJSONString();
                dataArray.clear();

                //data nội tại
                dataArray.add(player.playerIntrinsic.intrinsic.id);
                dataArray.add(player.playerIntrinsic.intrinsic.param1);
                dataArray.add(player.playerIntrinsic.intrinsic.param2);
                dataArray.add(player.playerIntrinsic.countOpen);
                String intrinsic = dataArray.toJSONString();
                dataArray.clear();

                //data item time
                dataArray.add((player.itemTime.isUseBoHuyet ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet)) : 0));
                dataArray.add((player.itemTime.isUseBoKhi ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi)) : 0));
                dataArray.add((player.itemTime.isUseGiapXen ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen)) : 0));
                dataArray.add((player.itemTime.isUseCuongNo ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo)) : 0));
                dataArray.add((player.itemTime.isUseAnDanh ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh)) : 0));
                dataArray.add((player.itemTime.isOpenPower ? (ItemTime.TIME_OPEN_POWER - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower)) : 0));
                dataArray.add((player.itemTime.isUseMayDo ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo)) : 0));
                dataArray.add((player.itemTime.isUseMayDo2 ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo2)) : 0));
                dataArray.add((player.itemTime.isEatMeal ? (ItemTime.TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal)) : 0));
                dataArray.add(player.itemTime.iconMeal);
                dataArray.add((player.itemTime.isUseTDLT ? ((player.itemTime.timeTDLT - (System.currentTimeMillis() - player.itemTime.lastTimeUseTDLT)) / 60 / 1000) : 0));
                String itemTime = dataArray.toJSONString();
                dataArray.clear();

                //data item time sc
                dataArray.add((player.itemTime.isUseBoHuyet2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet2)) : 0));
                dataArray.add((player.itemTime.isUseBoKhi2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi2)) : 0));
                dataArray.add((player.itemTime.isUseGiapXen2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen2)) : 0));
                dataArray.add((player.itemTime.isUseCuongNo2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo2)) : 0));
                dataArray.add((player.itemTime.isUseAnDanh2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh2)) : 0));
                String itemTimeSC = dataArray.toJSONString();
                dataArray.clear();

                //data item may dò
                dataArray.add((player.itemTime.isUseMaydoCua ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeMaydoCua)) : 0));
                dataArray.add((player.itemTime.isUseMaydoTom ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeMaydoTom)) : 0));
                dataArray.add((player.itemTime.isUseMaydoOc ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeMaydoOc)) : 0));
                dataArray.add((player.itemTime.isUseMaydoCa ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeMaydoCa)) : 0));
                String itemMayDo = dataArray.toJSONString();
                dataArray.clear();

                //data nhiệm vụ
                dataArray.add(player.playerTask.taskMain.id);
                dataArray.add(player.playerTask.taskMain.index);
                dataArray.add(player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count);
                String task = dataArray.toJSONString();
                dataArray.clear();

                //data nhiệm vụ hàng ngày
                dataArray.add(player.playerTask.sideTask.template != null ? player.playerTask.sideTask.template.id : -1);
                dataArray.add(player.playerTask.sideTask.receivedTime);
                dataArray.add(player.playerTask.sideTask.count);
                dataArray.add(player.playerTask.sideTask.maxCount);
                dataArray.add(player.playerTask.sideTask.leftTask);
                dataArray.add(player.playerTask.sideTask.level);
                String sideTask = dataArray.toJSONString();
                dataArray.clear();

                //data trứng bư
                if (player.mabuEgg != null) {
                    dataArray.add(player.mabuEgg.lastTimeCreate);
                    dataArray.add(player.mabuEgg.timeDone);
                }
                String mabuEgg = dataArray.toJSONString();
                dataArray.clear();

                //data trứng bill
                if (player.billEgg != null) {
                    dataArray.add(player.billEgg.lastTimeCreate);
                    dataArray.add(player.billEgg.timeDone);
                }
                String billEgg = dataArray.toJSONString();
                dataArray.clear();

                dataArray.add(player.isthoigianduhanh == true ? 1 : 0);
                dataArray.add(player.thoigianduhanh);
                String thoigianduhanh = dataArray.toJSONString();
                dataArray.clear();

                //data bùa
                dataArray.add(player.charms.tdTriTue);
                dataArray.add(player.charms.tdManhMe);
                dataArray.add(player.charms.tdDaTrau);
                dataArray.add(player.charms.tdOaiHung);
                dataArray.add(player.charms.tdBatTu);
                dataArray.add(player.charms.tdDeoDai);
                dataArray.add(player.charms.tdThuHut);
                dataArray.add(player.charms.tdDeTu);
                dataArray.add(player.charms.tdTriTue3);
                dataArray.add(player.charms.tdTriTue4);
                String charm = dataArray.toJSONString();
                dataArray.clear();

                // data skill
                JSONArray dataSkill = new JSONArray();
                for (Skill skill : player.playerSkill.skills) {
                    dataSkill.add(skill.template.id);
                    dataSkill.add(skill.point);
                    dataSkill.add(skill.lastTimeUseThisSkill);
                    dataSkill.add(skill.currLevel);
                    dataArray.add(dataSkill.toJSONString());
                    dataSkill.clear();
                }
                String skills = dataArray.toJSONString();
                dataArray.clear();
                dataArray.clear();

                // data skill shortcut
                for (int skillId : player.playerSkill.skillShortCut) {
                    dataArray.add(skillId);
                }
                String skillShortcut = dataArray.toJSONString();
                dataArray.clear();

                String pet = dataArray.toJSONString();
                String petInfo = dataArray.toJSONString();
                String petPoint = dataArray.toJSONString();
                String petBody = dataArray.toJSONString();
                String petSkill = dataArray.toJSONString();

                //data pet
                if (player.pet != null) {
                    dataArray.add(player.pet.typePet);
                    dataArray.add(player.pet.gender);
                    dataArray.add(player.pet.name);
                    dataArray.add(player.fusion.typeFusion);
                    int timeLeftFusion = (int) (Fusion.TIME_FUSION - (System.currentTimeMillis() - player.fusion.lastTimeFusion));
                    dataArray.add(timeLeftFusion < 0 ? 0 : timeLeftFusion);
                    dataArray.add(player.pet.status);
                    petInfo = dataArray.toJSONString();
                    dataArray.clear();

                    dataArray.add(player.pet.nPoint.limitPower);
                    dataArray.add(player.pet.nPoint.power);
                    dataArray.add(player.pet.nPoint.tiemNang);
                    dataArray.add(player.pet.nPoint.stamina);
                    dataArray.add(player.pet.nPoint.maxStamina);
                    dataArray.add(player.pet.nPoint.hpg);
                    dataArray.add(player.pet.nPoint.mpg);
                    dataArray.add(player.pet.nPoint.dameg);
                    dataArray.add(player.pet.nPoint.defg);
                    dataArray.add(player.pet.nPoint.critg);
                    dataArray.add(player.pet.nPoint.hp);
                    dataArray.add(player.pet.nPoint.mp);
                    petPoint = dataArray.toJSONString();
                    dataArray.clear();

                    JSONArray items = new JSONArray();
                    JSONArray options = new JSONArray();
                    JSONArray opt = new JSONArray();
                    for (Item item : player.pet.inventory.itemsBody) {
                        if (item.isNotNullItem()) {
                            dataItem.add(item.template.id);
                            dataItem.add(item.quantity);
                            for (Item.ItemOption io : item.itemOptions) {
                                opt.add(io.optionTemplate.id);
                                opt.add(io.param);
                                options.add(opt.toJSONString());
                                opt.clear();
                            }
                            dataItem.add(options.toJSONString());
                        } else {
                            dataItem.add(-1);
                            dataItem.add(0);
                            dataItem.add(options.toJSONString());
                        }

                        dataItem.add(item.createTime);

                        items.add(dataItem.toJSONString());
                        dataItem.clear();
                        options.clear();
                    }
                    petBody = items.toJSONString();

                    JSONArray petSkills = new JSONArray();
                    for (Skill s : player.pet.playerSkill.skills) {
                        JSONArray pskill = new JSONArray();
                        if (s.skillId != -1) {
                            pskill.add(s.template.id);
                            pskill.add(s.point);
                        } else {
                            pskill.add(-1);
                            pskill.add(0);
                        }
                        petSkills.add(pskill.toJSONString());
                    }
                    petSkill = petSkills.toJSONString();

                    dataArray.add(petInfo);
                    dataArray.add(petPoint);
                    dataArray.add(petBody);
                    dataArray.add(petSkill);
                    pet = dataArray.toJSONString();
                }
                dataArray.clear();

                //data thưởng ngọc rồng đen
                for (int i = 0; i < player.rewardBlackBall.timeOutOfDateReward.length; i++) {
                    JSONArray dataBlackBall = new JSONArray();
                    dataBlackBall.add(player.rewardBlackBall.timeOutOfDateReward[i]);
                    dataBlackBall.add(player.rewardBlackBall.lastTimeGetReward[i]);
                    dataBlackBall.add(player.rewardBlackBall.quantilyBlackBall[i]);
                    dataArray.add(dataBlackBall.toJSONString());
                    dataBlackBall.clear();
                }
                String dataBlackBall = dataArray.toJSONString();
                dataArray.clear();
                JSONObject achievementObject = new JSONObject();
                achievementObject.put("numPvpWin", player.achievement.numPvpWin);
                achievementObject.put("numSkillChuong", player.achievement.numSkillChuong);
                achievementObject.put("numFly", player.achievement.numFly);
                achievementObject.put("numKillMobFly", player.achievement.numKillMobFly);
                achievementObject.put("numKillNguoiRom", player.achievement.numKillNguoiRom);
                achievementObject.put("numHourOnline", player.achievement.numHourOnline);
                achievementObject.put("numGivePea", player.achievement.numGivePea);
                achievementObject.put("numSellItem", player.achievement.numSellItem);
                achievementObject.put("numPayMoney", player.achievement.numPayMoney);
                achievementObject.put("numKillSieuQuai", player.achievement.numKillSieuQuai);
                achievementObject.put("numHoiSinh", player.achievement.numHoiSinh);
                achievementObject.put("numSkillDacBiet", player.achievement.numSkillDacBiet);
                achievementObject.put("numPickGem", player.achievement.numPickGem);
                achievementObject.put("sm1", 0);
                achievementObject.put("sm2", 0);
                achievementObject.put("sm3", 0);
                achievementObject.put("sm4", 0);
                achievementObject.put("sm5", 0);
                achievementObject.put("sm6", 0);
                achievementObject.put("sm7", 0);
                achievementObject.put("sm8", 0);
                achievementObject.put("sm9", 0);
                achievementObject.put("sm10", 0);
                dataArray.addAll(player.achievement.listReceiveGem);
                achievementObject.put("listReceiveGem", dataArray);
                String info_achive = achievementObject.toJSONString();

                String info_phoban = "[" + player.bdkb_lastTimeJoin + "," + player.bdkb_countPerDay + "]";
                String query = " update player set info_achievement =?, data_may_do = ?, khi_gas =?, data_item_time_sieu_cap = ?,head = ?, have_tennis_space_ship = ?,"
                        + "clan_id_sv" + Manager.SERVER + " = ?, data_inventory = ?, data_location = ?, data_point = ?, data_magic_tree = ?,"
                        + "items_body = ?, items_bag = ?, items_box = ?, items_box_lucky_round = ?, friends = ?,"
                        + "enemies = ?, data_intrinsic = ?, data_item_time = ?, data_item_time_sieu_cap = ?, data_task = ?, data_mabu_egg = ?, pet = ?,"
                        + "data_black_ball = ?, data_side_task = ?, data_charm = ?, skills = ?,"
                        + " skills_shortcut = ?, violate=?, pointPvp=?, NguHanhSonPoint=?, pointCauCa=?, pointNroNamec=?,registeredVodai = ?, ischeckjoinvodai =?, ismonkey = ?, isbroly =?, xuatsu=?, damethanmeo=?, ptdame=?, diemtrungthu=?, diemnaubanhtrung=?, diemsontinh=?, diemthuytinh=?, diemsukientet=?, diemsukienhe=?, diembitich=?,data_card=?,bill_data =? ,info_phoban =?, congduc =?, data_offtrain =?, capboss=?, point_gapthu =?, point_cauca =?, thoigianduhanh = ?, thachdauwhis = ?, tongtien=?, flagMoc10k = ?, flagMoc20k = ?, flagMoc50k = ?, flagMoc100k = ?, flagMoc200k = ?, flagMoc500k = ?, flagMoc1cu = ?, diemhotong = ?, Month_Card = ? where id = ?";
                GirlkunDB.executeUpdate(query,
                        info_achive,
                        itemMayDo,
                        player.khigas,
                        itemTimeSC,
                        player.head,
                        player.haveTennisSpaceShip,
                        (player.clan != null ? player.clan.id : -1),
                        inventory,
                        location,
                        point,
                        magicTree,
                        itemsBody,
                        itemsBag,
                        itemsBox,
                        itemsBoxLuckyRound,
                        friend,
                        enemy,
                        intrinsic,
                        itemTime,
                        itemTimeSC,
                        task,
                        mabuEgg,
                        pet,
                        dataBlackBall,
                        sideTask,
                        charm,
                        skills,
                        skillShortcut,
                        player.diemdanh,
                        player.pointPvp,
                        player.NguHanhSonPoint,
                        player.pointCauCa,
                         player.pointNroNamec,
                        player.registeredVodai,
                     player.ischeckjoinvodai,
                        player.ismonkey,
                        player.isbroly,
                        player.xuatsu,
                        player.damethanmeo,
                        player.ptdame,
                        player.diemtrungthu,
                        player.diemnaubanhtrung,
                         player.diemsontinh,
                          player.diemthuytinh,
                           player.diemsukientet,
                            player.diemsukienhe,
                        player.diembitich,
                        JSONValue.toJSONString(player.Cards),
                        billEgg,
                        info_phoban,
                        player.congduc,
                        dataoff,
                        player.capboss,
                        player.point_gapthu,
                        player.point_cauca,
                        thoigianduhanh,
                        player.thachdauwhis,
                        player.tongtien,
                        player.flagMoc10k,
                        player.flagMoc20k,
                        player.flagMoc50k,
                        player.flagMoc100k,
                        player.flagMoc200k,
                        player.flagMoc500k,
                        player.flagMoc1cu,
                        player.diemhotong,
                        player.Month_Card,
                        player.id);
//                Logger.success("Total time save player " + player.name + " thành công! " + (System.currentTimeMillis() - st) + "\n");
            } catch (Exception e) {
                Logger.logException(PlayerDAO.class, e, "Lỗi save player " + player.name);
            }
        }
    }

    public static boolean subGoldBar(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set thoi_vang = (thoi_vang - ?), active = ? where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().actived ? 1 : 0);
            ps.setInt(3, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
            player.getSession().goldBar -= num;
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
            return false;
        } finally {
        }
        return false;
    }

    public static boolean subcoinBar(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set coin = (coin - ?), active = ? where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().actived ? 1 : 0);
            ps.setInt(3, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
            player.getSession().coinBar -= num;
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update Coin " + player.name);
            return false;
        } finally {
        }
        if (num > 1000) {
            insertHistoryGold(player, num);
        }
        return true;
    }

    public static boolean subquydoi(Player player, int num) {
        try {
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update account set vnd = (vnd - ?) where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            player.getSession().vnd -= num;
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            Logger.error("lỗi subvnd kêu admin fix nhé các member");
            return false;
        }

    }

    public static boolean subvnd(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set vnd = (vnd - ?), active = ? where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().actived ? 1 : 0);
            ps.setInt(3, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
            player.getSession().vnd -= num;
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update Coin " + player.name);
            return false;
        } finally {
        }
        if (num > 1000) {
            insertHistoryGold(player, num);
        }
        return true;
    }

    public static boolean setIs_gift_box(Player player) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set is_gift_box = 0 where id = ?");
            ps.setInt(1, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update new_reg " + player.name);
            return false;
        }
        return true;
    }

    public static void addHistoryReceiveGoldBar(Player player, int goldBefore, int goldAfter,
            int goldBagBefore, int goldBagAfter, int goldBoxBefore, int goldBoxAfter) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("insert into history_receive_goldbar(player_id,player_name,gold_before_receive,"
                    + "gold_after_receive,gold_bag_before,gold_bag_after,gold_box_before,gold_box_after) values (?,?,?,?,?,?,?,?)");
            ps.setInt(1, (int) player.id);
            ps.setString(2, player.name);
            ps.setInt(3, goldBefore);
            ps.setInt(4, goldAfter);
            ps.setInt(5, goldBagBefore);
            ps.setInt(6, goldBagAfter);
            ps.setInt(7, goldBoxBefore);
            ps.setInt(8, goldBoxAfter);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public static void updateItemReward(Player player) {
        String dataItemReward = "";
        for (Item item : player.getSession().itemsReward) {
            if (item.isNotNullItem()) {
                dataItemReward += "{" + item.template.id + ":" + item.quantity;
                if (!item.itemOptions.isEmpty()) {
                    dataItemReward += "|";
                    for (Item.ItemOption io : item.itemOptions) {
                        dataItemReward += "[" + io.optionTemplate.id + ":" + io.param + "],";
                    }
                    dataItemReward = dataItemReward.substring(0, dataItemReward.length() - 1) + "};";
                }
            }
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set reward = ? where id = ?");
            ps.setString(1, dataItemReward);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update phần thưởng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public static boolean insertHistoryGold(Player player, int quantily) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("insert into history_gold(name,gold) values (?,?)");
            ps.setString(1, player.name);
            ps.setInt(2, quantily);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi insert history_gold " + player.name);
            return false;
        }
        return true;
    }

    public static boolean checkLogout(Connection con, Player player) {
        long lastTimeLogout = 0;
        long lastTimeLogin = 0;
        try {
            PreparedStatement ps = con.prepareStatement("select * from account where id = ? limit 1");
            ps.setInt(1, player.getSession().userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                lastTimeLogin = rs.getTimestamp("last_time_login").getTime();

            }
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        } catch (Exception e) {
            return false;
        }
        return lastTimeLogout > lastTimeLogin;
    }

    public static void LogNapTIen(String uid, String menhgia, String seri, String code, String tranid) {
        String UPDATE_PASS = "INSERT INTO naptien(uid,sotien,seri,code,loaithe,time,noidung,tinhtrang,tranid,magioithieu) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try {
            Connection conn = GirlkunDB.getConnection();
            PreparedStatement ps = null;
            //UPDATE NRSD,
            ps = conn.prepareStatement(UPDATE_PASS);
            conn.setAutoCommit(false);
            //NGOC RONG SAO DEN
            ps.setString(1, uid);
            ps.setString(2, menhgia);
            ps.setString(3, seri);
            ps.setString(4, code);

            ps.setString(5, "VIETTEL");
            ps.setString(6, "123123123123");
            ps.setString(7, "dang nap the");
            ps.setString(8, "0");
            ps.setString(9, tranid);
            ps.setString(10, "0");
            if (ps.executeUpdate() == 1) {
            }

            conn.commit();
            //UPDATE NRSD
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
