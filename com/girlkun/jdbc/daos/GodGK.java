package com.girlkun.jdbc.daos;

import com.girlkun.card.Card;
import com.girlkun.card.OptionCard;
import com.girlkun.consts.ConstNpc;
import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.data.DataGame;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.clan.ClanMember;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.ItemTime;
import com.girlkun.models.npc.specialnpc.MabuEgg;
import com.girlkun.models.npc.specialnpc.BillEgg;
import com.girlkun.models.npc.specialnpc.MagicTree;
import com.girlkun.models.player.Enemy;
import com.girlkun.models.player.Friend;
import com.girlkun.models.player.Fusion;
import com.girlkun.models.player.Pet;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.models.task.TaskMain;
import com.girlkun.network.server.GirlkunSessionManager;
import com.girlkun.network.session.ISession;
import com.girlkun.server.Client;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerManager;
import com.girlkun.server.io.MySession;
import com.girlkun.server.model.AntiLogin;
import com.girlkun.services.ClanService;
import com.girlkun.services.IntrinsicService;
import com.girlkun.services.ItemService;
import com.girlkun.services.MapService;
import com.girlkun.services.NpcService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.services.func.SummonDragon;
import com.girlkun.utils.Logger;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.TimeUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.girlkun.utils.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class GodGK {

    public static List<OptionCard> loadOptionCard(JSONArray json) {
        List<OptionCard> ops = new ArrayList<>();
        try {
            for (int i = 0; i < json.size(); i++) {
                JSONObject ob = (JSONObject) json.get(i);
                if (ob != null) {
                    ops.add(new OptionCard(Integer.parseInt(ob.get("id").toString()), Integer.parseInt(ob.get("param").toString()), Byte.parseByte(ob.get("active").toString())));
                }
            }
        } catch (Exception e) {
        }
        return ops;
    }
    public static Boolean baotri = false;

    public static synchronized Player login(MySession session, AntiLogin al) {
        Player player = null;
        GirlkunResultSet rs = null;
        try {
            rs = GirlkunDB.executeQuery("select * from account where username = ? and password = ?", session.uu, (session.pp));
            if (rs.first()) {
                session.userId = rs.getInt("account.id");
                session.isAdmin = rs.getBoolean("admin");
                session.lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                session.lastTimeOff = rs.getTimestamp("last_time_off").getTime();
                session.actived = rs.getBoolean("active");
                session.goldBar = rs.getInt("account.thoi_vang");
                session.bdPlayer = rs.getDouble("account.bd_player");
                session.vnd = rs.getInt("vnd");
                session.tongnap = rs.getInt("tongnap");
                session.coinBar = rs.getInt("account.coin");
                long lastTimeLogin = rs.getTimestamp("last_time_login").getTime();
                int secondsPass1 = (int) ((System.currentTimeMillis() - lastTimeLogin) / 1000);
                long lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                int secondsPass = (int) ((System.currentTimeMillis() - lastTimeLogout) / 1000);

//                if (!session.isAdmin) {
//                    Service.gI().sendThongBaoOK(session, "Chi danh cho admin");
//                }else
                if (rs.getBoolean("ban")) {
                    Service.gI().sendThongBaoOK(session, "Tài khoản đã bị khóa, do liên tục thực hiện hành vi xấu!");
                } else if (baotri && session.isAdmin) {
                    Service.gI().sendThongBaoOK(session, "Máy chủ đang bảo trì, vào con cặc!");
                } else if (secondsPass1 < Manager.SECOND_WAIT_LOGIN) {
                    if (secondsPass < secondsPass1) {
                        Service.gI().sendThongBaoOK(session, "Vui lòng chờ " + (Manager.SECOND_WAIT_LOGIN - secondsPass) + "s");
                        return null;
                    }
                    Service.gI().sendThongBaoOK(session, "Vui lòng chờ " + (Manager.SECOND_WAIT_LOGIN - secondsPass1) + "s");
                    return null;
                } else if (rs.getTimestamp("last_time_login").getTime() > session.lastTimeLogout) {
                    Player plInGame = Client.gI().getPlayerByUser(session.userId);
                    if (plInGame != null) {
                        Client.gI().kickSession(plInGame.getSession());
                        Service.gI().sendThongBaoOK(session, "Ai đó đã vô acc bạn :3");
                    } else {
                    }
//                    Service.gI().sendThongBaoOK(session, "Tài khoản đang được đăng nhập tại máy chủ khác");
                } else {
                    if (secondsPass < Manager.SECOND_WAIT_LOGIN) {
                        Service.gI().sendThongBaoOK(session, "Vui lòng chờ " + (Manager.SECOND_WAIT_LOGIN - secondsPass) + "s");
                    } else {//set time logout trước rồi đọc data player
                        rs = GirlkunDB.executeQuery("select * from player where account_id = ? limit 1", session.userId);
                        if (!rs.first()) {
                            Service.gI().switchToCreateChar(session);
                            DataGame.sendDataItemBG(session);
                            DataGame.sendVersionGame(session);
                            DataGame.sendTileSetInfo(session);
                            Service.gI().sendMessage(session, -93, "1630679752231_-93_r");
                            DataGame.updateData(session);
                        } else {
                            Player plInGame = Client.gI().getPlayerByUser(session.userId);
                            if (plInGame != null) {
                                Client.gI().kickSession(plInGame.getSession());
                            }
                            int plHp = 200000000;
                            int plMp = 200000000;
                            JSONValue jv = new JSONValue();
                            JSONArray dataArray = null;

                            player = new Player();

                            //base info
                            player.id = rs.getInt("id");
                            if (player.tongtien >= 500000 && player.tongtien < 1000000) {
                                player.name = "[VIP] " + rs.getString("name");
                            } else if (player.tongtien >= 1000000) {
                                player.name = "[SVIP] " + rs.getString("name");
                            } else {
                                player.name = rs.getString("name");
                            }
                            player.head = rs.getShort("head");
                            player.gender = rs.getByte("gender");
                            player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                            player.diemdanh = rs.getLong("violate");
                            player.pointPvp = rs.getInt("pointPvp");
                            player.NguHanhSonPoint = rs.getInt("NguHanhSonPoint");
                            player.pointCauCa = rs.getInt("pointCauCa");
                            player.pointNroNamec = rs.getInt("pointNroNamec");
                            player.registeredVodai = rs.getInt("registeredVodai");
                            player.ischeckjoinvodai = rs.getInt("ischeckjoinvodai");
                            player.ismonkey = rs.getInt("ismonkey");
                            player.isbroly = rs.getInt("isbroly");
                            player.xuatsu = rs.getInt("xuatsu");
                            player.damethanmeo = rs.getLong("damethanmeo");
                            player.ptdame = rs.getInt("ptdame");
                            player.diemtrungthu = rs.getInt("diemtrungthu");
                            player.diemnaubanhtrung = rs.getInt("diemnaubanhtrung");
                            player.diemsontinh = rs.getInt("diemsontinh");
                            player.diemthuytinh = rs.getInt("diemthuytinh");
                            player.diemsukientet = rs.getInt("diemsukientet");
                            player.diemsukienhe = rs.getInt("diemsukienhe");
                            player.diembitich = rs.getInt("diembitich");
                            player.khigas = rs.getInt("khi_gas");
                            player.congduc = rs.getInt("congduc");
                            player.capboss = rs.getInt("capboss");
                            player.point_gapthu = rs.getInt("point_gapthu");
                            player.point_cauca = rs.getInt("point_cauca");
                            player.thachdauwhis = rs.getInt("thachdauwhis");
                            player.tongtien = rs.getInt("tongtien");
                            player.flagMoc10k = rs.getInt("flagMoc10k");
                            player.flagMoc20k = rs.getInt("flagMoc20k");
                            player.flagMoc50k = rs.getInt("flagMoc50k");
                            player.flagMoc100k = rs.getInt("flagMoc100k");
                            player.flagMoc200k = rs.getInt("flagMoc200k");
                            player.flagMoc500k = rs.getInt("flagMoc500k");
                            player.flagMoc1cu = rs.getInt("flagMoc1cu");
                            player.diemhotong = rs.getInt("diemhotong");
                            player.Month_Card = rs.getInt("Month_Card");
                            long now = System.currentTimeMillis();
                            long thoiGianOffline = now - session.lastTimeOff;
                            player.timeoff = thoiGianOffline /= 60000;
                            player.totalPlayerViolate = 0;
                            int clanId = rs.getInt("clan_id_sv" + Manager.SERVER);
                            if (clanId != -1) {
                                Clan clan = ClanService.gI().getClanById(clanId);
                                for (ClanMember cm : clan.getMembers()) {
                                    if (cm.id == player.id) {
                                        clan.addMemberOnline(player);
                                        player.clan = clan;
                                        player.clanMember = cm;
                                        break;
                                    }
                                }
                            }

                            //data kim lượng
                            dataArray = (JSONArray) jv.parse(rs.getString("data_inventory"));
                            player.inventory.gold = Long.parseLong(String.valueOf(dataArray.get(0)));
                            player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            if (dataArray.size() >= 4) {
                                player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            } else {
                                player.inventory.coupon = 0;
                            }
                            if (dataArray.size() >= 5 && false) {
                                player.inventory.event = Integer.parseInt(String.valueOf(dataArray.get(4)));
                            } else {
                                player.inventory.event = 0;
                            }
                            // obito
                            if (dataArray.size() >= 6 && false) {
                                player.inventory.skien = Integer.parseInt(String.valueOf(dataArray.get(5)));   // điểm gấp thú
                            } else {
                                player.inventory.skien = 0;
                            }
                            if (dataArray.size() >= 7 && false) {
                                player.inventory.skien1 = Integer.parseInt(String.valueOf(dataArray.get(6)));
                            } else {
                                player.inventory.skien1 = 0;
                            }
                            if (dataArray.size() >= 8 && false) {
                                player.inventory.skien2 = Integer.parseInt(String.valueOf(dataArray.get(7)));
                            } else {
                                player.inventory.skien2 = 0;
                            }
                            if (dataArray.size() >= 9 && false) {
                                player.inventory.skien3 = Integer.parseInt(String.valueOf(dataArray.get(8)));
                            } else {
                                player.inventory.skien3 = 0;
                            }
                            if (dataArray.size() >= 10 && false) {
                                player.inventory.skien4 = Integer.parseInt(String.valueOf(dataArray.get(9)));
                            } else {
                                player.inventory.skien4 = 0;
                            }
                            if (dataArray.size() >= 11 && false) {
                                player.inventory.skien5 = Integer.parseInt(String.valueOf(dataArray.get(10)));
                            } else {
                                player.inventory.skien5 = 0;
                            }
                            dataArray.clear();

                            dataArray = (JSONArray) jv.parse(rs.getString("thoigianduhanh"));
                            player.isthoigianduhanh = Integer.parseInt(String.valueOf(dataArray.get(0))) == 1 ? true : false;
                            player.thoigianduhanh = Long.parseLong(String.valueOf(dataArray.get(1)));
                            dataArray.clear();

                            // data rada card
                            dataArray = (JSONArray) jv.parse(rs.getString("data_card"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                JSONObject obj = (JSONObject) dataArray.get(i);
                                player.Cards.add(new Card(Short.parseShort(obj.get("id").toString()), Byte.parseByte(obj.get("amount").toString()), Byte.parseByte(obj.get("max").toString()), Byte.parseByte(obj.get("level").toString()), loadOptionCard((JSONArray) JSONValue.parse(obj.get("option").toString())), Byte.parseByte(obj.get("used").toString())));
                            }
                            dataArray.clear();

                            //data tọa độ
                            try {
                                dataArray = (JSONArray) jv.parse(rs.getString("data_location"));
                                int mapId = Integer.parseInt(String.valueOf(dataArray.get(0)));
                                player.location.x = Integer.parseInt(String.valueOf(dataArray.get(1)));
                                player.location.y = Integer.parseInt(String.valueOf(dataArray.get(2)));
                                player.location.lastTimeplayerMove = System.currentTimeMillis();
                                if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                                        || MapService.gI().isMapBanDoKhoBau(mapId) || MapService.gI().isMapMaBu(mapId)
                                        || MapService.gI().isMapMaBu(mapId)
                                        || MapService.gI().isMapVodai(mapId)
                                        || MapService.gI().isMapConDuongRanDoc(mapId)
                                        || MapService.gI().isMapGiaiCuuMiNuong(mapId)
                                        || MapService.gI().isMapSatan(mapId)) {
                                    mapId = player.gender + 21;
                                    player.location.x = 300;
                                    player.location.y = 336;
                                }

                                player.zone = MapService.gI().getMapCanJoin(player, mapId, -1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dataArray.clear();

                            //data chỉ số
                            dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
                            player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                            player.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                            player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                            player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                            player.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                            player.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                            player.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                            player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                            player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                            dataArray.get(10); //** Năng động
                            plHp = Integer.parseInt(String.valueOf(dataArray.get(11)));
                            plMp = Integer.parseInt(String.valueOf(dataArray.get(12)));
                            dataArray.clear();

                            //data đậu thần
                            dataArray = (JSONArray) jv.parse(rs.getString("data_magic_tree"));
                            byte level = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            byte currPea = Byte.parseByte(String.valueOf(dataArray.get(1)));
                            boolean isUpgrade = Byte.parseByte(String.valueOf(dataArray.get(2))) == 1;
                            long lastTimeHarvest = Long.parseLong(String.valueOf(dataArray.get(3)));
                            long lastTimeUpgrade = Long.parseLong(String.valueOf(dataArray.get(4)));
                            player.magicTree = new MagicTree(player, level, currPea, lastTimeHarvest, isUpgrade, lastTimeUpgrade);
                            dataArray.clear();

                            //data phần thưởng sao đen
                            dataArray = (JSONArray) jv.parse(rs.getString("data_black_ball"));
                            JSONArray dataBlackBall = null;
                            for (int i = 0; i < dataArray.size(); i++) {
                                dataBlackBall = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                player.rewardBlackBall.timeOutOfDateReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(0)));
                                player.rewardBlackBall.lastTimeGetReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(1)));
                                try {
                                    player.rewardBlackBall.quantilyBlackBall[i] = dataBlackBall.get(2) != null ? Integer.parseInt(String.valueOf(dataBlackBall.get(2))) : 0;
                                } catch (Exception e) {
                                    player.rewardBlackBall.quantilyBlackBall[i] = player.rewardBlackBall.timeOutOfDateReward[i] != 0 ? 1 : 0;
                                }
                                dataBlackBall.clear();
                            }
                            dataArray.clear();

                            //data body
                            dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                        int id = Integer.parseInt(String.valueOf(opt.get(0)));
                                        int param = Integer.parseInt(String.valueOf(opt.get(1)));
                                        if (id == 11) {
                                            item.itemOptions.add(new Item.ItemOption(id,
                                                    player.congduc));
                                        } else {
                                            item.itemOptions.add(new Item.ItemOption(id,
                                                    param));
                                        }
                                    }
                                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                    if (ItemService.gI().isOutOfDateTime(item)) {
                                        item = ItemService.gI().createItemNull();
                                    }
                                } else {
                                    item = ItemService.gI().createItemNull();
                                }
                                player.inventory.itemsBody.add(item);
                            }
                            if (player.inventory.itemsBody.size() == 12) {
                                player.inventory.itemsBody.add(ItemService.gI().createItemNull());
                            }
                            dataArray.clear();

                            //data bag
                            dataArray = (JSONArray) jv.parse(rs.getString("items_bag"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                        int id = Integer.parseInt(String.valueOf(opt.get(0)));
                                        int param = Integer.parseInt(String.valueOf(opt.get(1)));
                                        if (id == 11) {
                                            item.itemOptions.add(new Item.ItemOption(id,
                                                    player.congduc));
                                        } else {
                                            item.itemOptions.add(new Item.ItemOption(id,
                                                    param));
                                        }
                                    }
                                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                    if (ItemService.gI().isOutOfDateTime(item)) {
                                        item = ItemService.gI().createItemNull();
                                    }
                                } else {
                                    item = ItemService.gI().createItemNull();
                                }
                                player.inventory.itemsBag.add(item);
                            }
                            dataArray.clear();

                            //data box
                            dataArray = (JSONArray) jv.parse(rs.getString("items_box"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                        int id = Integer.parseInt(String.valueOf(opt.get(0)));
                                        int param = Integer.parseInt(String.valueOf(opt.get(1)));
                                        if (id == 11) {
                                            item.itemOptions.add(new Item.ItemOption(id,
                                                    player.congduc));
                                        } else {
                                            item.itemOptions.add(new Item.ItemOption(id,
                                                    param));
                                        }
                                    }
                                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                    if (ItemService.gI().isOutOfDateTime(item)) {
                                        item = ItemService.gI().createItemNull();
                                    }
                                } else {
                                    item = ItemService.gI().createItemNull();
                                }
                                player.inventory.itemsBox.add(item);
                            }
                            dataArray.clear();

                            //data box lucky round
                            dataArray = (JSONArray) jv.parse(rs.getString("items_box_lucky_round"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                Integer.parseInt(String.valueOf(opt.get(1)))));
                                    }
                                    player.inventory.itemsBoxCrackBall.add(item);
                                }
                            }
                            dataArray.clear();

                            //data friends
                            dataArray = (JSONArray) jv.parse(rs.getString("friends"));
                            if (dataArray != null) {
                                for (int i = 0; i < dataArray.size(); i++) {
                                    JSONArray dataFE = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                    Friend friend = new Friend();
                                    friend.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                                    friend.name = String.valueOf(dataFE.get(1));
                                    friend.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                                    friend.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                                    friend.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                                    friend.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                                    friend.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                                    player.friends.add(friend);
                                    dataFE.clear();
                                }
                                dataArray.clear();
                            }
                            player.rankSieuHang = rs.getInt("rank_sieu_hang");
                            if (player.rankSieuHang == 999999) {
                                player.rankSieuHang = ServerManager.gI().getNumPlayer();
                            }

                            //data enemies
                            dataArray = (JSONArray) jv.parse(rs.getString("enemies"));
                            if (dataArray != null) {
                                for (int i = 0; i < dataArray.size(); i++) {
                                    JSONArray dataFE = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                    Enemy enemy = new Enemy();
                                    enemy.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                                    enemy.name = String.valueOf(dataFE.get(1));
                                    enemy.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                                    enemy.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                                    enemy.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                                    enemy.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                                    enemy.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                                    player.enemies.add(enemy);
                                    dataFE.clear();
                                }
                                dataArray.clear();
                            }

                            //data nội tại
                            dataArray = (JSONArray) jv.parse(rs.getString("data_intrinsic"));
                            byte intrinsicId = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(intrinsicId);
                            player.playerIntrinsic.intrinsic.param1 = Short.parseShort(String.valueOf(dataArray.get(1)));
                            player.playerIntrinsic.intrinsic.param2 = Short.parseShort(String.valueOf(dataArray.get(2)));
                            player.playerIntrinsic.countOpen = Byte.parseByte(String.valueOf(dataArray.get(3)));
                            dataArray.clear();

                            //data item time
                            dataArray = (JSONArray) jv.parse(rs.getString("data_item_time"));
                            int timeBoHuyet = Integer.parseInt(String.valueOf(dataArray.get(0)));

                            int timeBoKhi = Integer.parseInt(String.valueOf(dataArray.get(1)));

                            int timeGiapXen = Integer.parseInt(String.valueOf(dataArray.get(2)));

                            int timeCuongNo = Integer.parseInt(String.valueOf(dataArray.get(3)));

                            int timeAnDanh = Integer.parseInt(String.valueOf(dataArray.get(4)));

                            int timeOpenPower = Integer.parseInt(String.valueOf(dataArray.get(5)));
                            int timeMayDo = Integer.parseInt(String.valueOf(dataArray.get(6)));
                            int timeMayDo2 = Integer.parseInt(String.valueOf(dataArray.get(6)));

                            int timeMeal = Integer.parseInt(String.valueOf(dataArray.get(7)));
                            int iconMeal = Integer.parseInt(String.valueOf(dataArray.get(8)));

                            int timeUseTDLT = 0;
                            if (dataArray.size() == 10) {
                                timeUseTDLT = Integer.parseInt(String.valueOf(dataArray.get(9)));
                            }

                            player.itemTime.lastTimeBoHuyet = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet);
                            player.itemTime.lastTimeBoKhi = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi);
                            player.itemTime.lastTimeGiapXen = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen);
                            player.itemTime.lastTimeCuongNo = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo);
                            player.itemTime.lastTimeAnDanh = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanh);

                            player.itemTime.lastTimeOpenPower = System.currentTimeMillis() - (ItemTime.TIME_OPEN_POWER - timeOpenPower);
                            player.itemTime.lastTimeUseMayDo = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeMayDo);
                            player.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO2 - timeMayDo2);

                            player.itemTime.lastTimeEatMeal = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeMeal);
                            player.itemTime.timeTDLT = timeUseTDLT * 60 * 1000;
                            player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();

                            player.itemTime.iconMeal = iconMeal;
                            player.itemTime.isUseBoHuyet = timeBoHuyet != 0;
                            player.itemTime.isUseBoKhi = timeBoKhi != 0;
                            player.itemTime.isUseGiapXen = timeGiapXen != 0;
                            player.itemTime.isUseCuongNo = timeCuongNo != 0;
                            player.itemTime.isUseAnDanh = timeAnDanh != 0;
                            player.itemTime.isUseBoHuyet2 = timeBoHuyet != 0;
                            player.itemTime.isUseBoKhi2 = timeBoKhi != 0;
                            player.itemTime.isUseGiapXen2 = timeGiapXen != 0;
                            player.itemTime.isUseCuongNo2 = timeCuongNo != 0;
                            player.itemTime.isUseAnDanh2 = timeAnDanh != 0;
                            player.itemTime.isOpenPower = timeOpenPower != 0;
                            player.itemTime.isUseMayDo = timeMayDo != 0;
                            player.itemTime.isUseMayDo2 = timeMayDo2 != 0;

                            player.itemTime.isEatMeal = timeMeal != 0;
                            player.itemTime.isUseTDLT = timeUseTDLT != 0;
                            dataArray.clear();

                            //data item time
                            dataArray = (JSONArray) jv.parse(rs.getString("data_item_time_sieu_cap"));
                            int timeBoHuyet2 = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            int timeBoKhi2 = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            int timeGiapXen2 = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            int timeCuongNo2 = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            int timeAnDanh2 = Integer.parseInt(String.valueOf(dataArray.get(4)));

                            player.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet2);
                            player.itemTime.lastTimeBoKhi2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi2);
                            player.itemTime.lastTimeGiapXen2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen2);
                            player.itemTime.lastTimeCuongNo2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo2);
                            player.itemTime.lastTimeAnDanh2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanh2);

                            player.itemTime.isUseBoHuyet2 = timeBoHuyet2 != 0;
                            player.itemTime.isUseBoKhi2 = timeBoKhi2 != 0;
                            player.itemTime.isUseGiapXen2 = timeGiapXen2 != 0;
                            player.itemTime.isUseCuongNo2 = timeCuongNo2 != 0;
                            player.itemTime.isUseAnDanh2 = timeAnDanh2 != 0;

                            dataArray.clear();

                            //data ái tèm máy dò
                            dataArray = (JSONArray) jv.parse(rs.getString("data_may_do"));
                            int maydoCua = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            int maydoTom = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            int maydoOc = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            int maydoCa = Integer.parseInt(String.valueOf(dataArray.get(3)));

                            player.itemTime.lastTimeMaydoCua = System.currentTimeMillis() - (ItemTime.TIME_ITEM - maydoCua);
                            player.itemTime.lastTimeMaydoTom = System.currentTimeMillis() - (ItemTime.TIME_ITEM - maydoTom);
                            player.itemTime.lastTimeMaydoOc = System.currentTimeMillis() - (ItemTime.TIME_ITEM - maydoOc);
                            player.itemTime.lastTimeMaydoCa = System.currentTimeMillis() - (ItemTime.TIME_ITEM - maydoCa);

                            player.itemTime.isUseMaydoCua = maydoCua != 0;
                            player.itemTime.isUseMaydoTom = maydoTom != 0;
                            player.itemTime.isUseMaydoOc = maydoOc != 0;
                            player.itemTime.isUseMaydoCa = maydoCa != 0;
                            dataArray.clear();

                            //data nhiệm vụ
                            dataArray = (JSONArray) jv.parse(rs.getString("data_task"));
                            TaskMain taskMain = TaskService.gI().getTaskMainById(player, Byte.parseByte(String.valueOf(dataArray.get(0))));
                            taskMain.index = Byte.parseByte(String.valueOf(dataArray.get(1)));
                            taskMain.subTasks.get(taskMain.index).count = Short.parseShort(String.valueOf(dataArray.get(2)));
                            player.playerTask.taskMain = taskMain;
                            dataArray.clear();

                            //data nhiệm vụ hàng ngày
                            dataArray = (JSONArray) jv.parse(rs.getString("data_side_task"));
                            String format = "dd-MM-yyyy";
                            long receivedTime = Long.parseLong(String.valueOf(dataArray.get(1)));
                            Date date = new Date(receivedTime);
                            if (TimeUtil.formatTime(date, format).equals(TimeUtil.formatTime(new Date(), format))) {
                                player.playerTask.sideTask.template = TaskService.gI().getSideTaskTemplateById(Integer.parseInt(String.valueOf(dataArray.get(0))));
                                player.playerTask.sideTask.count = Integer.parseInt(String.valueOf(dataArray.get(2)));
                                player.playerTask.sideTask.maxCount = Integer.parseInt(String.valueOf(dataArray.get(3)));
                                player.playerTask.sideTask.leftTask = Integer.parseInt(String.valueOf(dataArray.get(4)));
                                player.playerTask.sideTask.level = Integer.parseInt(String.valueOf(dataArray.get(5)));
                                player.playerTask.sideTask.receivedTime = receivedTime;
                            }

                            //data luyện tập off
                            dataArray = (JSONArray) jv.parse(rs.getString("data_offtrain"));
                            player.typetrain = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            player.istrain = Byte.parseByte(String.valueOf(dataArray.get(1))) == 1;
                            dataArray.clear();

                            //data trứng bư
                            dataArray = (JSONArray) jv.parse(rs.getString("data_mabu_egg"));
                            if (dataArray.size() != 0) {
                                player.mabuEgg = new MabuEgg(player, Long.parseLong(String.valueOf(dataArray.get(0))),
                                        Long.parseLong(String.valueOf(dataArray.get(1))));
                            }
                            dataArray.clear();

                            //data trứng bill
                            dataArray = (JSONArray) jv.parse(rs.getString("bill_data"));
                            if (dataArray.size() != 0) {
                                player.billEgg = new BillEgg(player, Long.parseLong(String.valueOf(dataArray.get(0))),
                                        Long.parseLong(String.valueOf(dataArray.get(1))));
                            }
                            dataArray.clear();

                            //data bùa
                            dataArray = (JSONArray) jv.parse(rs.getString("data_charm"));
                            player.charms.tdTriTue = Long.parseLong(String.valueOf(dataArray.get(0)));
                            player.charms.tdManhMe = Long.parseLong(String.valueOf(dataArray.get(1)));
                            player.charms.tdDaTrau = Long.parseLong(String.valueOf(dataArray.get(2)));
                            player.charms.tdOaiHung = Long.parseLong(String.valueOf(dataArray.get(3)));
                            player.charms.tdBatTu = Long.parseLong(String.valueOf(dataArray.get(4)));
                            player.charms.tdDeoDai = Long.parseLong(String.valueOf(dataArray.get(5)));
                            player.charms.tdThuHut = Long.parseLong(String.valueOf(dataArray.get(6)));
                            player.charms.tdDeTu = Long.parseLong(String.valueOf(dataArray.get(7)));
                            player.charms.tdTriTue3 = Long.parseLong(String.valueOf(dataArray.get(8)));
                            player.charms.tdTriTue4 = Long.parseLong(String.valueOf(dataArray.get(9)));
                            dataArray.clear();

                            // data skill
                            dataArray = (JSONArray) jv.parse(rs.getString("skills"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                JSONArray dataSkill = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                int tempId = Integer.parseInt(String.valueOf(dataSkill.get(0)));
                                byte point = Byte.parseByte(String.valueOf(dataSkill.get(1)));
                                Skill skill = null;
                                if (point != 0) {
                                    skill = SkillUtil.createSkill(tempId, point);
                                } else {
                                    skill = SkillUtil.createSkillLevel0(tempId);
                                }
                                skill.lastTimeUseThisSkill = Long.parseLong(String.valueOf(dataSkill.get(2)));
                                player.playerSkill.skills.add(skill);
                            }
                            dataArray.clear();

                            // data skill shortcut
                            dataArray = (JSONArray) jv.parse(rs.getString("skills_shortcut"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                player.playerSkill.skillShortCut[i] = Byte.parseByte(String.valueOf(dataArray.get(i)));
                            }
                            for (int i : player.playerSkill.skillShortCut) {
                                if (player.playerSkill.getSkillbyId(i) != null
                                        && player.playerSkill.getSkillbyId(i).damage > 0) {
                                    player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(i);
                                    break;
                                }
                            }
                            if (player.playerSkill.skillSelect == null) {
                                player.playerSkill.skillSelect = player.playerSkill
                                        .getSkillbyId(player.gender == ConstPlayer.TRAI_DAT
                                                ? Skill.DRAGON
                                                : (player.gender == ConstPlayer.NAMEC ? Skill.DEMON : Skill.GALICK));
                            }
                            dataArray.clear();

                            //data pet
                            JSONArray petData = (JSONArray) jv.parse(rs.getString("pet"));
                            if (!petData.isEmpty()) {
                                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(0)));
                                Pet pet = new Pet(player);
                                pet.id = -player.id;
                                pet.typePet = Byte.parseByte(String.valueOf(dataArray.get(0)));
                                pet.gender = Byte.parseByte(String.valueOf(dataArray.get(1)));
                                pet.name = String.valueOf(dataArray.get(2));
                                player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataArray.get(3)));
                                player.fusion.lastTimeFusion = System.currentTimeMillis()
                                        - (Fusion.TIME_FUSION - Integer.parseInt(String.valueOf(dataArray.get(4))));
                                pet.status = Byte.parseByte(String.valueOf(dataArray.get(5)));
                                try {

                                } catch (Exception e) {
                                    //                    throw new RuntimeException(e);
                                }

                                //data chỉ số
                                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(1)));
                                pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                                pet.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                                pet.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                                pet.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                                pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                                pet.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                                pet.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                                pet.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                                pet.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                                pet.nPoint.critg = Integer.parseInt(String.valueOf(dataArray.get(9)));
                                int hp = Integer.parseInt(String.valueOf(dataArray.get(10)));
                                int mp = Integer.parseInt(String.valueOf(dataArray.get(11)));

                                //data body
                                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(2)));
                                for (int i = 0; i < dataArray.size(); i++) {
                                    Item item = null;
                                    JSONArray dataItem = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                    if (tempId != -1) {
                                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                        for (int j = 0; j < options.size(); j++) {
                                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                                        }
                                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                        if (ItemService.gI().isOutOfDateTime(item)) {
                                            item = ItemService.gI().createItemNull();
                                        }
                                    } else {
                                        item = ItemService.gI().createItemNull();
                                        ;
                                    }
                                    pet.inventory.itemsBody.add(item);
                                }

                                //data skills
                                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(3)));
                                for (int i = 0; i < dataArray.size(); i++) {
                                    JSONArray skillTemp = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                    int tempId = Integer.parseInt(String.valueOf(skillTemp.get(0)));
                                    byte point = Byte.parseByte(String.valueOf(skillTemp.get(1)));
                                    Skill skill = null;
                                    if (point != 0) {
                                        skill = SkillUtil.createSkill(tempId, point);
                                    } else {
                                        skill = SkillUtil.createSkillLevel0(tempId);
                                    }
                                    switch (skill.template.id) {
                                        case Skill.KAMEJOKO:
                                        case Skill.MASENKO:
                                        case Skill.ANTOMIC:
                                            skill.coolDown = 1000;
                                            break;
                                    }
                                    pet.playerSkill.skills.add(skill);
                                }
                                if (pet.playerSkill.skills.size() < 5) {
                                    pet.playerSkill.skills.add(4, SkillUtil.createSkillLevel0(-1));
                                }
                                pet.nPoint.hp = hp;
                                pet.nPoint.mp = mp;
                                player.pet = pet;
                            }
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("info_phoban"));
                            if (!dataArray.isEmpty()) {
                                player.bdkb_countPerDay = Integer.parseInt(String.valueOf(dataArray.get(1)));
                                player.bdkb_lastTimeJoin = Long.parseLong(String.valueOf(dataArray.get(0)));

                            }
                            dataArray.clear();

                            // nhiem vu bo mong 
                            JSONObject achievementObject = (JSONObject) JSONValue.parse(rs.getString("info_achievement"));
                            player.achievement.numPvpWin = Integer.parseInt(String.valueOf(achievementObject.get("numPvpWin")));
                            player.achievement.numSkillChuong = Integer.parseInt(String.valueOf(achievementObject.get("numSkillChuong")));
                            player.achievement.numFly = Integer.parseInt(String.valueOf(achievementObject.get("numFly")));
                            player.achievement.numKillMobFly = Integer.parseInt(String.valueOf(achievementObject.get("numKillMobFly")));
                            player.achievement.numKillNguoiRom = Integer.parseInt(String.valueOf(achievementObject.get("numKillNguoiRom")));
                            player.achievement.numHourOnline = Long.parseLong(String.valueOf(achievementObject.get("numHourOnline")));
                            player.achievement.numGivePea = Integer.parseInt(String.valueOf(achievementObject.get("numGivePea")));
                            player.achievement.numSellItem = Integer.parseInt(String.valueOf(achievementObject.get("numSellItem")));
                            player.achievement.numPayMoney = Integer.parseInt(String.valueOf(achievementObject.get("numPayMoney")));
                            player.achievement.numKillSieuQuai = Integer.parseInt(String.valueOf(achievementObject.get("numKillSieuQuai")));
                            player.achievement.numHoiSinh = Integer.parseInt(String.valueOf(achievementObject.get("numHoiSinh")));
                            player.achievement.numSkillDacBiet = Integer.parseInt(String.valueOf(achievementObject.get("numSkillDacBiet")));
                            player.achievement.numPickGem = Integer.parseInt(String.valueOf(achievementObject.get("numPickGem")));

                            dataArray = (JSONArray) JSONValue.parse(String.valueOf(achievementObject.get("listReceiveGem")));
                            for (Byte i = 0; i < dataArray.size(); i++) {
                                player.achievement.listReceiveGem.add(Boolean.valueOf(String.valueOf(dataArray.get(i))));
                            }
                            dataArray.clear();
                            player.bdkb_isJoinBdkb = false;
                            if ((new java.sql.Date(player.bdkb_lastTimeJoin)).getDay() != (new java.sql.Date(System.currentTimeMillis())).getDay()) {
                                player.bdkb_countPerDay = 0;
                            }
                            if ((new java.sql.Date(player.diemdanh)).getDay() != (new java.sql.Date(System.currentTimeMillis())).getDay()) {
                                player.diemdanh = 0;
                            }
                            player.nPoint.hp = plHp;
                            player.nPoint.mp = plMp;
                            player.iDMark.setLoadedAllDataPlayer(true);
                            GirlkunDB.executeUpdate("update account set last_time_login = '" + new Timestamp(System.currentTimeMillis()) + "', ip_address = '" + session.ipAddress + "' where id = " + session.userId);
                        }
                    }
                }
                al.reset();
            } else {
                Service.gI().sendThongBaoOK(session, "Thông tin tài khoản hoặc mật khẩu không chính xác");
                al.wrong();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(session.uu);
            player.dispose();
            player = null;
            Logger.logException(GodGK.class, e);
        } finally {
            if (rs != null) {
                rs.dispose();
            }
        }
        return player;
    }

    public static void checkDo() {
        long st = System.currentTimeMillis();
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        Player player;
        PreparedStatement ps = null;
        String name = "";
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection()) {
            ps = con.prepareStatement("select * from player");
            rs = ps.executeQuery();
            while (rs.next()) {
                int plHp = 200000000;
                int plMp = 200000000;
                player = new Player();
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                //data kim lượng
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_inventory"));
                player.inventory.gold = Long.parseLong(String.valueOf(dataArray.get(0)));
                player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                if (dataArray.size() == 4) {
                    player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                } else {
                    player.inventory.coupon = 0;
                }
                dataArray.clear();

                //data chỉ số
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_point"));
                player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                player.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                player.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                player.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                dataArray.get(10); //** Năng động
                plHp = Integer.parseInt(String.valueOf(dataArray.get(11)));
                plMp = Integer.parseInt(String.valueOf(dataArray.get(12)));
                dataArray.clear();

                //data body
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            int id = Integer.parseInt(String.valueOf(opt.get(0)));
                            int param = Integer.parseInt(String.valueOf(opt.get(1)));
                            if (id == 11) {
                                item.itemOptions.add(new Item.ItemOption(id,
                                        player.congduc));
                            } else {
                                item.itemOptions.add(new Item.ItemOption(id,
                                        param));
                            }

                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "body");
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();

                //data bag
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_bag"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            int id = Integer.parseInt(String.valueOf(opt.get(0)));
                            int param = Integer.parseInt(String.valueOf(opt.get(1)));
                            if (id == 11) {
                                item.itemOptions.add(new Item.ItemOption(id,
                                        player.congduc));
                            } else {
                                item.itemOptions.add(new Item.ItemOption(id,
                                        param));
                            }
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "bag");
                    player.inventory.itemsBag.add(item);
                }
                dataArray.clear();

                //data box
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            int id = Integer.parseInt(String.valueOf(opt.get(0)));
                            int param = Integer.parseInt(String.valueOf(opt.get(1)));
                            if (id == 11) {
                                item.itemOptions.add(new Item.ItemOption(id,
                                        player.congduc));
                            } else {
                                item.itemOptions.add(new Item.ItemOption(id,
                                        param));
                            }
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "box");
                    player.inventory.itemsBox.add(item);
                }
                dataArray.clear();

                //data box lucky round
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box_lucky_round"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        player.inventory.itemsBoxCrackBall.add(item);
                    }
                }
                dataArray.clear();

                //data luyện tập off
                dataArray = (JSONArray) jv.parse(rs.getString("data_offtrain"));
                player.typetrain = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.istrain = Byte.parseByte(String.valueOf(dataArray.get(1))) == 1;
                dataArray.clear();
                //data pet
                JSONArray petData = (JSONArray) jv.parse(rs.getString("pet"));
                if (!petData.isEmpty()) {
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(0)));
                    Pet pet = new Pet(player);
                    pet.id = -player.id;
                    pet.typePet = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    pet.gender = Byte.parseByte(String.valueOf(dataArray.get(1)));
                    pet.name = String.valueOf(dataArray.get(2));
                    player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataArray.get(3)));
                    player.fusion.lastTimeFusion = System.currentTimeMillis()
                            - (Fusion.TIME_FUSION - Integer.parseInt(String.valueOf(dataArray.get(4))));
                    pet.status = Byte.parseByte(String.valueOf(dataArray.get(5)));

                    //data chỉ số
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(1)));
                    pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    pet.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                    pet.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                    pet.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                    pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                    pet.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                    pet.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                    pet.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                    pet.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                    pet.nPoint.critg = Integer.parseInt(String.valueOf(dataArray.get(9)));
                    int hp = Integer.parseInt(String.valueOf(dataArray.get(10)));
                    int mp = Integer.parseInt(String.valueOf(dataArray.get(11)));

                    //data body
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(2)));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        JSONArray dataItem = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                            JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        Util.useCheckDo(player, item, "pet");
                        pet.inventory.itemsBody.add(item);
                    }

                }

            }
        } catch (Exception e) {
            System.out.println(name);
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi load database");
            System.exit(0);
        }
    }

    public static void checkVang(int x) {
        int thoi_vang = 0;
        long st = System.currentTimeMillis();
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        Player player;
        PreparedStatement ps = null;
        String name = "";
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection()) {
            ps = con.prepareStatement("select * from player");
            rs = ps.executeQuery();
            while (rs.next()) {
                int plHp = 200000000;
                int plMp = 200000000;
                player = new Player();
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                //data kim lượng
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_inventory"));
                player.inventory.gold = Integer.parseInt(String.valueOf(dataArray.get(0)));
                player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                
                if (dataArray.size() == 4) {
                    player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                } else {
                    player.inventory.coupon = 0;
                }
                dataArray.clear();

                //data luyện tập off
                dataArray = (JSONArray) jv.parse(rs.getString("data_offtrain"));
                player.typetrain = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.istrain = Byte.parseByte(String.valueOf(dataArray.get(1))) == 1;
                dataArray.clear();

                //data chỉ số
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_point"));
                player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                player.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                player.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                player.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                dataArray.get(10); //** Năng động
                plHp = Integer.parseInt(String.valueOf(dataArray.get(11)));
                plMp = Integer.parseInt(String.valueOf(dataArray.get(12)));
                dataArray.clear();

                //data body
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            int id = Integer.parseInt(String.valueOf(opt.get(0)));
                            int param = Integer.parseInt(String.valueOf(opt.get(1)));
                            if (id == 11) {
                                item.itemOptions.add(new Item.ItemOption(id,
                                        player.congduc));
                            } else {
                                item.itemOptions.add(new Item.ItemOption(id,
                                        param));
                            }

                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "body");
                    player.inventory.itemsBody.add(item);
                    if (item.template.id == 457) {
                        thoi_vang += item.quantity;
                    }
                }
                dataArray.clear();

                //data bag
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_bag"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            int id = Integer.parseInt(String.valueOf(opt.get(0)));
                            int param = Integer.parseInt(String.valueOf(opt.get(1)));
                            if (id == 11) {
                                item.itemOptions.add(new Item.ItemOption(id,
                                        player.congduc));
                            } else {
                                item.itemOptions.add(new Item.ItemOption(id,
                                        param));
                            }
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "bag");
                    if (item.template.id == 457) {
                        thoi_vang += item.quantity;
                    }
                    player.inventory.itemsBag.add(item);
                }
                dataArray.clear();

                //data box
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            int id = Integer.parseInt(String.valueOf(opt.get(0)));
                            int param = Integer.parseInt(String.valueOf(opt.get(1)));
                            if (id == 11) {
                                item.itemOptions.add(new Item.ItemOption(id,
                                        player.congduc));
                            } else {
                                item.itemOptions.add(new Item.ItemOption(id,
                                        param));
                            }
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "box");
                    if (item.template.id == 457) {
                        thoi_vang += item.quantity;
                    }
                    player.inventory.itemsBox.add(item);
                }
                dataArray.clear();
                if (thoi_vang > x) {
                    Logger.error("play:" + player.name);
                    Logger.error("thoi_vang:" + thoi_vang);
                }

                JSONObject achievementObject = (JSONObject) JSONValue.parse(rs.getString("info_achievement"));
                player.achievement.numPvpWin = Integer.parseInt(String.valueOf(achievementObject.get("numPvpWin")));
                player.achievement.numSkillChuong = Integer.parseInt(String.valueOf(achievementObject.get("numSkillChuong")));
                player.achievement.numFly = Integer.parseInt(String.valueOf(achievementObject.get("numFly")));
                player.achievement.numKillMobFly = Integer.parseInt(String.valueOf(achievementObject.get("numKillMobFly")));
                player.achievement.numKillNguoiRom = Integer.parseInt(String.valueOf(achievementObject.get("numKillNguoiRom")));
                player.achievement.numHourOnline = Long.parseLong(String.valueOf(achievementObject.get("numHourOnline")));
                player.achievement.numGivePea = Integer.parseInt(String.valueOf(achievementObject.get("numGivePea")));
                player.achievement.numSellItem = Integer.parseInt(String.valueOf(achievementObject.get("numSellItem")));
                player.achievement.numPayMoney = Integer.parseInt(String.valueOf(achievementObject.get("numPayMoney")));
                player.achievement.numKillSieuQuai = Integer.parseInt(String.valueOf(achievementObject.get("numKillSieuQuai")));
                player.achievement.numHoiSinh = Integer.parseInt(String.valueOf(achievementObject.get("numHoiSinh")));
                player.achievement.numSkillDacBiet = Integer.parseInt(String.valueOf(achievementObject.get("numSkillDacBiet")));
                player.achievement.numPickGem = Integer.parseInt(String.valueOf(achievementObject.get("numPickGem")));

                dataArray = (JSONArray) JSONValue.parse(String.valueOf(achievementObject.get("listReceiveGem")));
                for (Byte i = 0; i < dataArray.size(); i++) {
                    player.achievement.listReceiveGem.add((Boolean) dataArray.get(i));
                }

            }
        } catch (Exception e) {
            System.out.println(name);
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi load database");
        }
    }

    public static Player loadById(int id) {
        Player player = null;
        GirlkunResultSet rs = null;
        if (Client.gI().getPlayer(id) != null) {
            player = Client.gI().getPlayer(id);
            return player;
        }
        try {
            rs = GirlkunDB.executeQuery("select * from player where id = ? limit 1", id);
            if (rs.first()) {
                int plHp = 200000000;
                int plMp = 200000000;
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;

                player = new Player();

                //base info
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");

                int clanId = rs.getInt("clan_id_sv" + Manager.SERVER);
                if (clanId != -1) {
                    Clan clan = ClanService.gI().getClanById(clanId);
                    for (ClanMember cm : clan.getMembers()) {
                        if (cm.id == player.id) {
                            clan.addMemberOnline(player);
                            player.clan = clan;
                            player.clanMember = cm;
                            break;
                        }
                    }
                }

                //data kim lượng
                dataArray = (JSONArray) jv.parse(rs.getString("data_inventory"));
                player.inventory.gold = Long.parseLong(String.valueOf(dataArray.get(0)));
                player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                dataArray.clear();

                player.rankSieuHang = rs.getInt("rank_sieu_hang");
                if (player.rankSieuHang == 999999) {
                    player.rankSieuHang = ServerManager.gI().getNumPlayer();
                }

                //data tọa độ
                try {
                    dataArray = (JSONArray) jv.parse(rs.getString("data_location"));
                    int mapId = Integer.parseInt(String.valueOf(dataArray.get(0)));
                    player.location.x = Integer.parseInt(String.valueOf(dataArray.get(1)));
                    player.location.y = Integer.parseInt(String.valueOf(dataArray.get(2)));
                    if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                            || MapService.gI().isMapKhiGas(mapId) || MapService.gI().isMapBanDoKhoBau(mapId)
                            || MapService.gI().isMapMaBu(mapId)
                            || MapService.gI().isMapVodai(mapId)
                            || MapService.gI().isMapConDuongRanDoc(mapId)
                            || MapService.gI().isMapGiaiCuuMiNuong(mapId)) {
                        mapId = player.gender + 21;
                        player.location.x = 300;
                        player.location.y = 336;
                    }
                    player.zone = MapService.gI().getMapCanJoin(player, mapId, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dataArray.clear();

                //data luyện tập off
                dataArray = (JSONArray) jv.parse(rs.getString("data_offtrain"));
                player.typetrain = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.istrain = Byte.parseByte(String.valueOf(dataArray.get(1))) == 1;
                dataArray.clear();

                //data chỉ số
                dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
                player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                player.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                player.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                player.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                dataArray.get(10); //** Năng động
                plHp = Integer.parseInt(String.valueOf(dataArray.get(11)));
                plMp = Integer.parseInt(String.valueOf(dataArray.get(12)));
                dataArray.clear();

                //data đậu thần
                dataArray = (JSONArray) jv.parse(rs.getString("data_magic_tree"));
                byte level = Byte.parseByte(String.valueOf(dataArray.get(0)));
                byte currPea = Byte.parseByte(String.valueOf(dataArray.get(1)));
                boolean isUpgrade = Byte.parseByte(String.valueOf(dataArray.get(2))) == 1;
                long lastTimeHarvest = Long.parseLong(String.valueOf(dataArray.get(3)));
                long lastTimeUpgrade = Long.parseLong(String.valueOf(dataArray.get(4)));
                player.magicTree = new MagicTree(player, level, currPea, lastTimeHarvest, isUpgrade, lastTimeUpgrade);
                dataArray.clear();

                //data phần thưởng sao đen
                dataArray = (JSONArray) jv.parse(rs.getString("data_black_ball"));
                JSONArray dataBlackBall = null;
                for (int i = 0; i < dataArray.size(); i++) {
                    dataBlackBall = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                    player.rewardBlackBall.timeOutOfDateReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(0)));
                    player.rewardBlackBall.lastTimeGetReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(1)));
                    dataBlackBall.clear();
                }
                dataArray.clear();

                //data body
                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            int idop = Integer.parseInt(String.valueOf(opt.get(0)));
                            int param = Integer.parseInt(String.valueOf(opt.get(1)));
                            if (id == 11) {
                                item.itemOptions.add(new Item.ItemOption(idop,
                                        player.congduc));
                            } else {
                                item.itemOptions.add(new Item.ItemOption(idop,
                                        param));
                            }
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();

                //data bag
                dataArray = (JSONArray) jv.parse(rs.getString("items_bag"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBag.add(item);
                }
                dataArray.clear();

                //data box
                dataArray = (JSONArray) jv.parse(rs.getString("items_box"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            int idop = Integer.parseInt(String.valueOf(opt.get(0)));
                            int param = Integer.parseInt(String.valueOf(opt.get(1)));
                            if (id == 11) {
                                item.itemOptions.add(new Item.ItemOption(idop,
                                        player.congduc));
                            } else {
                                item.itemOptions.add(new Item.ItemOption(idop,
                                        param));
                            }
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBox.add(item);
                }
                dataArray.clear();

                //data box lucky round
                dataArray = (JSONArray) jv.parse(rs.getString("items_box_lucky_round"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        player.inventory.itemsBoxCrackBall.add(item);
                    }
                }
                dataArray.clear();

                //data friends
                dataArray = (JSONArray) jv.parse(rs.getString("friends"));
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONArray dataFE = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        Friend friend = new Friend();
                        friend.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                        friend.name = String.valueOf(dataFE.get(1));
                        friend.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                        friend.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                        friend.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                        friend.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                        friend.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                        player.friends.add(friend);
                        dataFE.clear();
                    }
                    dataArray.clear();
                }

                //data enemies
                dataArray = (JSONArray) jv.parse(rs.getString("enemies"));
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONArray dataFE = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        Enemy enemy = new Enemy();
                        enemy.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                        enemy.name = String.valueOf(dataFE.get(1));
                        enemy.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                        enemy.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                        enemy.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                        enemy.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                        enemy.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                        player.enemies.add(enemy);
                        dataFE.clear();
                    }
                    dataArray.clear();
                }

                //data nội tại
                dataArray = (JSONArray) jv.parse(rs.getString("data_intrinsic"));
                byte intrinsicId = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(intrinsicId);
                player.playerIntrinsic.intrinsic.param1 = Short.parseShort(String.valueOf(dataArray.get(1)));
                player.playerIntrinsic.intrinsic.param2 = Short.parseShort(String.valueOf(dataArray.get(2)));
                player.playerIntrinsic.countOpen = Byte.parseByte(String.valueOf(dataArray.get(3)));
                dataArray.clear();

                //data item time
                dataArray = (JSONArray) jv.parse(rs.getString("data_item_time"));
                int timeBoHuyet = Integer.parseInt(String.valueOf(dataArray.get(0)));
                int timeBoKhi = Integer.parseInt(String.valueOf(dataArray.get(1)));
                int timeGiapXen = Integer.parseInt(String.valueOf(dataArray.get(2)));
                int timeCuongNo = Integer.parseInt(String.valueOf(dataArray.get(3)));
                int timeAnDanh = Integer.parseInt(String.valueOf(dataArray.get(4)));
                int timeOpenPower = Integer.parseInt(String.valueOf(dataArray.get(5)));
                int timeMayDo = Integer.parseInt(String.valueOf(dataArray.get(6)));
                int timeMayDo2 = Integer.parseInt(String.valueOf(dataArray.get(7)));
                int timeMeal = Integer.parseInt(String.valueOf(dataArray.get(8)));
                int iconMeal = Integer.parseInt(String.valueOf(dataArray.get(9)));
                int timeUseTDLT = 0;
                if (dataArray.size() == 11) {
                    timeUseTDLT = Integer.parseInt(String.valueOf(dataArray.get(10)));
                }

                player.itemTime.lastTimeBoHuyet = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet);
                player.itemTime.lastTimeBoKhi = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi);
                player.itemTime.lastTimeGiapXen = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen);
                player.itemTime.lastTimeCuongNo = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo);
                player.itemTime.lastTimeAnDanh = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanh);
                player.itemTime.lastTimeOpenPower = System.currentTimeMillis() - (ItemTime.TIME_OPEN_POWER - timeOpenPower);
                player.itemTime.lastTimeUseMayDo = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeMayDo);
                player.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO2 - timeMayDo2);
                player.itemTime.lastTimeEatMeal = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeMeal);
                player.itemTime.timeTDLT = timeUseTDLT * 60 * 1000;
                player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();
                player.itemTime.iconMeal = iconMeal;

                player.itemTime.isUseBoHuyet = timeBoHuyet != 0;
                player.itemTime.isUseBoKhi = timeBoKhi != 0;
                player.itemTime.isUseGiapXen = timeGiapXen != 0;
                player.itemTime.isUseCuongNo = timeCuongNo != 0;
                player.itemTime.isUseAnDanh = timeAnDanh != 0;
                player.itemTime.isOpenPower = timeOpenPower != 0;
                player.itemTime.isUseMayDo = timeMayDo != 0;
                player.itemTime.isUseMayDo2 = timeMayDo2 != 0;
                player.itemTime.isEatMeal = timeMeal != 0;
                player.itemTime.isUseTDLT = timeUseTDLT != 0;
                dataArray.clear();

                //data item time
                dataArray = (JSONArray) jv.parse(rs.getString("data_item_time_sieu_cap"));
                int timeBoHuyet2 = Integer.parseInt(String.valueOf(dataArray.get(0)));
                int timeBoKhi2 = Integer.parseInt(String.valueOf(dataArray.get(1)));
                int timeGiapXen2 = Integer.parseInt(String.valueOf(dataArray.get(2)));
                int timeCuongNo2 = Integer.parseInt(String.valueOf(dataArray.get(3)));
                int timeAnDanh2 = Integer.parseInt(String.valueOf(dataArray.get(4)));

                player.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet2);
                player.itemTime.lastTimeBoKhi2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi2);
                player.itemTime.lastTimeGiapXen2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen2);
                player.itemTime.lastTimeCuongNo2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo2);
                player.itemTime.lastTimeAnDanh2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanh2);

                player.itemTime.isUseBoHuyet2 = timeBoHuyet2 != 0;
                player.itemTime.isUseBoKhi2 = timeBoKhi2 != 0;
                player.itemTime.isUseGiapXen2 = timeGiapXen2 != 0;
                player.itemTime.isUseCuongNo2 = timeCuongNo2 != 0;
                player.itemTime.isUseAnDanh2 = timeAnDanh2 != 0;
                dataArray.clear();

                //data nhiệm vụ
                dataArray = (JSONArray) jv.parse(rs.getString("data_task"));
                TaskMain taskMain = TaskService.gI().getTaskMainById(player, Byte.parseByte(String.valueOf(dataArray.get(0))));
                taskMain.index = Byte.parseByte(String.valueOf(dataArray.get(1)));
                taskMain.subTasks.get(taskMain.index).count = Short.parseShort(String.valueOf(dataArray.get(2)));
                player.playerTask.taskMain = taskMain;
                dataArray.clear();

                //data nhiệm vụ hàng ngày
                dataArray = (JSONArray) jv.parse(rs.getString("data_side_task"));
                String format = "dd-MM-yyyy";
                long receivedTime = Long.parseLong(String.valueOf(dataArray.get(1)));
                Date date = new Date(receivedTime);
                if (TimeUtil.formatTime(date, format).equals(TimeUtil.formatTime(new Date(), format))) {
                    player.playerTask.sideTask.template = TaskService.gI().getSideTaskTemplateById(Integer.parseInt(String.valueOf(dataArray.get(0))));
                    player.playerTask.sideTask.count = Integer.parseInt(String.valueOf(dataArray.get(2)));
                    player.playerTask.sideTask.maxCount = Integer.parseInt(String.valueOf(dataArray.get(3)));
                    player.playerTask.sideTask.leftTask = Integer.parseInt(String.valueOf(dataArray.get(4)));
                    player.playerTask.sideTask.level = Integer.parseInt(String.valueOf(dataArray.get(5)));
                    player.playerTask.sideTask.receivedTime = receivedTime;
                }

                //data trứng bư
                dataArray = (JSONArray) jv.parse(rs.getString("data_mabu_egg"));
                if (dataArray.size() != 0) {
                    player.mabuEgg = new MabuEgg(player, Long.parseLong(String.valueOf(dataArray.get(0))),
                            Long.parseLong(String.valueOf(dataArray.get(1))));
                }
                dataArray.clear();

                //data trứng Berus
                //data bùa
                dataArray = (JSONArray) jv.parse(rs.getString("data_charm"));
                player.charms.tdTriTue = Long.parseLong(String.valueOf(dataArray.get(0)));
                player.charms.tdManhMe = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.charms.tdDaTrau = Long.parseLong(String.valueOf(dataArray.get(2)));
                player.charms.tdOaiHung = Long.parseLong(String.valueOf(dataArray.get(3)));
                player.charms.tdBatTu = Long.parseLong(String.valueOf(dataArray.get(4)));
                player.charms.tdDeoDai = Long.parseLong(String.valueOf(dataArray.get(5)));
                player.charms.tdThuHut = Long.parseLong(String.valueOf(dataArray.get(6)));
                player.charms.tdDeTu = Long.parseLong(String.valueOf(dataArray.get(7)));
                player.charms.tdTriTue3 = Long.parseLong(String.valueOf(dataArray.get(8)));
                player.charms.tdTriTue4 = Long.parseLong(String.valueOf(dataArray.get(9)));
                dataArray.clear();

                //data skill
                dataArray = (JSONArray) jv.parse(rs.getString("skills"));
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONArray dataSkill = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                    int tempId = Integer.parseInt(String.valueOf(dataSkill.get(0)));
                    byte point = Byte.parseByte(String.valueOf(dataSkill.get(1)));
                    Skill skill = null;
                    if (point != 0) {
                        skill = SkillUtil.createSkill(tempId, point);
                    } else {
                        skill = SkillUtil.createSkillLevel0(tempId);
                    }
                    skill.lastTimeUseThisSkill = Long.parseLong(String.valueOf(dataSkill.get(2)));
                    player.playerSkill.skills.add(skill);
                }
                dataArray.clear();

                //data skill shortcut
                dataArray = (JSONArray) jv.parse(rs.getString("skills_shortcut"));
                for (int i = 0; i < dataArray.size(); i++) {
                    player.playerSkill.skillShortCut[i] = Byte.parseByte(String.valueOf(dataArray.get(i)));
                }
                for (int i : player.playerSkill.skillShortCut) {
                    if (player.playerSkill.getSkillbyId(i) != null && player.playerSkill.getSkillbyId(i).damage > 0) {
                        player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(i);
                        break;
                    }
                }
                if (player.playerSkill.skillSelect == null) {
                    player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(player.gender == ConstPlayer.TRAI_DAT
                            ? Skill.DRAGON : (player.gender == ConstPlayer.NAMEC ? Skill.DEMON : Skill.GALICK));
                }
                dataArray.clear();

                //data pet
                JSONArray petData = (JSONArray) jv.parse(rs.getString("pet"));
                if (!petData.isEmpty()) {
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(0)));
                    Pet pet = new Pet(player);
                    pet.id = -player.id;
                    pet.typePet = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    pet.gender = Byte.parseByte(String.valueOf(dataArray.get(1)));
                    pet.name = String.valueOf(dataArray.get(2));
                    player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataArray.get(3)));
                    player.fusion.lastTimeFusion = System.currentTimeMillis()
                            - (Fusion.TIME_FUSION - Integer.parseInt(String.valueOf(dataArray.get(4))));
                    pet.status = Byte.parseByte(String.valueOf(dataArray.get(5)));

                    //data chỉ số
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(1)));
                    pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    pet.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                    pet.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                    pet.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                    pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                    pet.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                    pet.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                    pet.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                    pet.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                    pet.nPoint.critg = Integer.parseInt(String.valueOf(dataArray.get(9)));
                    int hp = Integer.parseInt(String.valueOf(dataArray.get(10)));
                    int mp = Integer.parseInt(String.valueOf(dataArray.get(11)));

                    //data body
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(2)));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        JSONArray dataItem = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                            JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();;
                        }
                        pet.inventory.itemsBody.add(item);
                    }

                    //data skills
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(3)));
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONArray skillTemp = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        int tempId = Integer.parseInt(String.valueOf(skillTemp.get(0)));
                        byte point = Byte.parseByte(String.valueOf(skillTemp.get(1)));
                        Skill skill = null;
                        if (point != 0) {
                            skill = SkillUtil.createSkill(tempId, point);
                        } else {
                            skill = SkillUtil.createSkillLevel0(tempId);
                        }
                        switch (skill.template.id) {
                            case Skill.KAMEJOKO:
                            case Skill.MASENKO:
                            case Skill.ANTOMIC:
                                skill.coolDown = 1000;
                                break;
                        }
                        pet.playerSkill.skills.add(skill);
                    }
                    pet.nPoint.hp = hp;
                    pet.nPoint.mp = mp;
                    player.pet = pet;
                }
                JSONObject achievementObject = (JSONObject) JSONValue.parse(rs.getString("info_achievement"));
                player.achievement.numPvpWin = Integer.parseInt(String.valueOf(achievementObject.get("numPvpWin")));
                player.achievement.numSkillChuong = Integer.parseInt(String.valueOf(achievementObject.get("numSkillChuong")));
                player.achievement.numFly = Integer.parseInt(String.valueOf(achievementObject.get("numFly")));
                player.achievement.numKillMobFly = Integer.parseInt(String.valueOf(achievementObject.get("numKillMobFly")));
                player.achievement.numKillNguoiRom = Integer.parseInt(String.valueOf(achievementObject.get("numKillNguoiRom")));
                player.achievement.numHourOnline = Long.parseLong(String.valueOf(achievementObject.get("numHourOnline")));
                player.achievement.numGivePea = Integer.parseInt(String.valueOf(achievementObject.get("numGivePea")));
                player.achievement.numSellItem = Integer.parseInt(String.valueOf(achievementObject.get("numSellItem")));
                player.achievement.numPayMoney = Integer.parseInt(String.valueOf(achievementObject.get("numPayMoney")));
                player.achievement.numKillSieuQuai = Integer.parseInt(String.valueOf(achievementObject.get("numKillSieuQuai")));
                player.achievement.numHoiSinh = Integer.parseInt(String.valueOf(achievementObject.get("numHoiSinh")));
                player.achievement.numSkillDacBiet = Integer.parseInt(String.valueOf(achievementObject.get("numSkillDacBiet")));
                player.achievement.numPickGem = Integer.parseInt(String.valueOf(achievementObject.get("numPickGem")));

                dataArray = (JSONArray) JSONValue.parse(String.valueOf(achievementObject.get("listReceiveGem")));
                //               System.err.println("lỗi load bò mộng: " + dataArray.size());
                for (Byte i = 0; i < dataArray.size(); i++) {
                    player.achievement.listReceiveGem.add((Boolean) dataArray.get(i));
                }
                player.nPoint.hp = plHp;
                player.nPoint.mp = plMp;
                player.iDMark.setLoadedAllDataPlayer(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            player.dispose();
            player = null;
            Logger.logException(GodGK.class, e);
        } finally {
            if (rs != null) {
                rs.dispose();
            }
        }
        return player;
    }

    public static Player loadByIdSieuHang(int id) {
        Player player = null;
        GirlkunResultSet rs = null;
        if (Client.gI().getPlayer(id) != null) {
            player = Client.gI().getPlayer(id);
            return player;
        }
        try {
            rs = GirlkunDB.executeQuery("select * from player where id = ? limit 1", id);
            if (rs.first()) {
                long plHp = 2000000000;
                long plMp = 2000000000;
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;

                player = new Player();

                //base info
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                //data kim lượng
                player.rankSieuHang = rs.getInt("rank_sieu_hang");
                if (player.rankSieuHang == 999999) {
                    player.rankSieuHang = ServerManager.gI().getNumPlayer();
                }

                //data chỉ số
                dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
                player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                player.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                player.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                player.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                dataArray.get(10); //** Năng động
                plHp = Long.parseLong(String.valueOf(dataArray.get(11)));
                plMp = Long.parseLong(String.valueOf(dataArray.get(12)));
                dataArray.clear();

                //data body
                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();

                //data skill
                dataArray = (JSONArray) jv.parse(rs.getString("skills"));
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONArray dataSkill = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                    int tempId = Integer.parseInt(String.valueOf(dataSkill.get(0)));
                    byte point = Byte.parseByte(String.valueOf(dataSkill.get(1)));
                    Skill skill = null;
                    if (point != 0) {
                        skill = SkillUtil.createSkill(tempId, point);
                    } else {
                        skill = SkillUtil.createSkillLevel0(tempId);
                    }
                    skill.lastTimeUseThisSkill = Long.parseLong(String.valueOf(dataSkill.get(2)));
                    player.playerSkill.skills.add(skill);
                }
                dataArray.clear();

                //data skill shortcut
                dataArray = (JSONArray) jv.parse(rs.getString("skills_shortcut"));
                for (int i = 0; i < dataArray.size(); i++) {
                    player.playerSkill.skillShortCut[i] = Byte.parseByte(String.valueOf(dataArray.get(i)));
                }
                for (int i : player.playerSkill.skillShortCut) {
                    if (player.playerSkill.getSkillbyId(i) != null && player.playerSkill.getSkillbyId(i).damage > 0) {
                        player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(i);
                        break;
                    }
                }
                if (player.playerSkill.skillSelect == null) {
                    player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(player.gender == ConstPlayer.TRAI_DAT
                            ? Skill.DRAGON : (player.gender == ConstPlayer.NAMEC ? Skill.DEMON : Skill.GALICK));
                }
                dataArray.clear();

                //data pet
                player.nPoint.hp = (int) plHp;
                player.nPoint.mp = (int) plMp;
                player.iDMark.setLoadedAllDataPlayer(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            player.dispose();
            player = null;
            Logger.logException(GodGK.class, e);
        } finally {
            if (rs != null) {
                rs.dispose();
            }
        }
        return player;
    }
}
