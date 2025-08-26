package com.girlkun.services;

import com.girlkun.consts.ConstMap;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.WayPoint;
import com.girlkun.models.map.Zone;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.girlkun.models.map.doanhtrai.DoanhTraiService;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Pet;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.network.io.Message;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapService {

    private static MapService i;

    public static MapService gI() {
        if (i == null) {
            i = new MapService();
        }
        return i;
    }

    public WayPoint getWaypointPlayerIn(Player player) {
        for (WayPoint wp : player.zone.map.wayPoints) {
            if (player.location.x >= wp.minX && player.location.x <= wp.maxX && player.location.y >= wp.minY && player.location.y <= wp.maxY) {
                if (player.pet3 != null && player.pet3.location != null && (player.location.x - player.pet3.location.x <= 250)) {
                    return wp;
                } else if (player.pet3 == null || player.pet3.isDie()) {
                    return wp;
                } else {
                    Service.getInstance().chat(player, "Hãy đứng gần sư phụ");
                }
            }
        }
        return null;
    }

    /**
     * @param tileTypeFocus tile type: top, bot, left, right...
     * @return [tileMapId][tileType]
     */
    public int[][] readTileIndexTileType(int tileTypeFocus) {
        int[][] tileIndexTileType = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("data/girlkun/map/tile_set_info"));
            int numTileMap = dis.readByte();
            tileIndexTileType = new int[numTileMap][];
            for (int i = 0; i < numTileMap; i++) {
                int numTileOfMap = dis.readByte();
                for (int j = 0; j < numTileOfMap; j++) {
                    int tileType = dis.readInt();
                    int numIndex = dis.readByte();
                    if (tileType == tileTypeFocus) {
                        tileIndexTileType[i] = new int[numIndex];
                    }
                    for (int k = 0; k < numIndex; k++) {
                        int typeIndex = dis.readByte();
                        if (tileType == tileTypeFocus) {
                            tileIndexTileType[i][k] = typeIndex;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
        return tileIndexTileType;
    }
    
    public void exitMap(Player playerExit, Zone map) {
        if (map != null) {
            Message msg = null;
            try {
                map.players.remove(playerExit);
                msg = new Message(-6);
                msg.writer().writeInt((int) playerExit.id);
                Service.gI().sendMessAnotherNotMeInMap(playerExit, msg);
            } catch (Exception e) {
//                e.printStackTrace();
            } finally {
                if (msg != null) {
                    msg.cleanup();
                    msg = null;
                }
            }
        }
    }

    //tilemap for paint
    public int[][] readTileMap(int mapId) {
        int[][] tileMap = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("data/girlkun/map/tile_map_data/" + mapId));
            dis.readByte();
            int w = dis.readByte();
            int h = dis.readByte();
            tileMap = new int[h][w];
            for (int i = 0; i < tileMap.length; i++) {
                for (int j = 0; j < tileMap[i].length; j++) {
                    tileMap[i][j] = dis.readByte();
                }
            }
            dis.close();
        } catch (Exception e) {
        }
        return tileMap;
    }

    public Zone getMapCanJoin(Player player, int mapId, int zoneId) {
        if (this.isMapOffline(mapId)) {
            return (Zone) this.getMapById(mapId).zones.get(0);
        } else {
            Iterator var4;
            Mob mob;
            Player boss;
            if (this.isMapDoanhTrai(mapId)) {
                if (player.clan != null && player.clan.doanhTrai != null) {
                    if (this.isMapDoanhTrai(player.zone.map.mapId)) {
                        var4 = player.zone.mobs.iterator();

                        while (var4.hasNext()) {
                            mob = (Mob) var4.next();
                            if (!mob.isDie()) {
                                return null;
                            }
                        }

                        var4 = player.zone.getBosses().iterator();

                        while (var4.hasNext()) {
                            boss = (Player) var4.next();
                            if (!boss.isDie()) {
                                return null;
                            }
                        }
                    }

                    return player.clan.doanhTrai.getMapById(mapId);
                } else {
                    return null;
                }
            } else if (this.isMapConDuongRanDoc(mapId)) {
                System.out.println("map rắn độc");
                if (player.clan != null && player.clan.ConDuongRanDoc != null) {
                    if (this.isMapConDuongRanDoc(player.zone.map.mapId)) {
                        Iterator<Mob> mobIterator = player.zone.mobs.iterator();
                        while (mobIterator.hasNext()) {
                            Mob mob2 = mobIterator.next();
                            if (!mob2.isDie()) {
                                System.out.println("Mob die");
                                return null;
                            }
                        }

                        Iterator<Player> bossIterator = player.zone.getBosses().iterator();
                        while (bossIterator.hasNext()) {
                            Player boss2 = bossIterator.next();
                            if (!boss2.isDie()) {
                                System.out.println("boss2 die");
                                return null;
                            }
                        }
                    }
                    return player.clan.ConDuongRanDoc.getMapById(mapId);
                } else {
                    return null;
                }

            } else if (this.isMapKhiGas(mapId)) {
                if (player.clan != null && player.clan.khiGas != null) {
                    if (this.isMapKhiGas(player.zone.map.mapId)) {
                        var4 = player.zone.mobs.iterator();

                        while (var4.hasNext()) {
                            mob = (Mob) var4.next();
                            if (!mob.isDie()) {
                                return null;
                            }
                        }

                        var4 = player.zone.getBosses().iterator();

                        while (var4.hasNext()) {
                            boss = (Player) var4.next();
                            if (!boss.isDie()) {
                                return null;
                            }
                        }
                    }

                    return player.clan.khiGas.getMapById(mapId);
                } else {
                    return null;
                }
            } else if (this.isMapGiaiCuuMiNuong(mapId)) {
                if (player.clan != null && player.clan.giaiCuuMiNuong != null) {
                    if (this.isMapGiaiCuuMiNuong(player.zone.map.mapId)) {
                        var4 = player.zone.mobs.iterator();

                        while (var4.hasNext()) {
                            mob = (Mob) var4.next();
                            if (!mob.isDie()) {
                                return null;
                            }
                        }

                        var4 = player.zone.getBosses().iterator();

                        while (var4.hasNext()) {
                            boss = (Player) var4.next();
                            if (!boss.isDie()) {
                                return null;
                            }
                        }
                    }

                    return player.clan.giaiCuuMiNuong.getMapById(mapId);
                } else {
                    return null;
                }
            } else if (!this.isMapBanDoKhoBau(mapId)) {
                if (this.isMapKhiGas(mapId) && player.clan == null) {
                    return null;
                } else {
                    return zoneId == -1 ? this.getZone(mapId) : this.getZoneByMapIDAndZoneID(mapId, zoneId);
                }
            } else if (player.clan != null && player.clan.banDoKhoBau != null) {
                if (this.isMapBanDoKhoBau(player.zone.map.mapId)) {
                    var4 = player.zone.mobs.iterator();

                    while (var4.hasNext()) {
                        mob = (Mob) var4.next();
                        if (!mob.isDie()) {
                            return null;
                        }
                    }

                    var4 = player.zone.getBosses().iterator();

                    while (var4.hasNext()) {
                        boss = (Player) var4.next();
                        if (!boss.isDie()) {
                            return null;
                        }
                    }
                }

                if (player.clan.banDoKhoBau.getListMap().indexOf(mapId) > player.clan.banDoKhoBau.getCurrentIndexMap()) {
                    player.clan.banDoKhoBau.setCurrentIndexMap(player.clan.banDoKhoBau.getListMap().indexOf(mapId));
                    player.clan.banDoKhoBau.init();
                }

                return player.clan.banDoKhoBau.getMapById(mapId);
            } else {
                return null;
            }
        }
    }

    public int GetZone(int mapId) {
        Map map = getMapById(mapId);
        int x = 20;
        int y = Util.nextInt(0, map.zones.size() - 1);
        for (int i = 0; i < map.zones.size(); i++) {
            if (map.zones.get(i).getNumOfPlayers() < x) {
                y = i;
                x = map.zones.get(i).getNumOfPlayers();
            }
        }
        return y;
    }

    public Zone getZone(int mapId) {
        Map map = getMapById(mapId);
        if (map == null) {
            return null;
        }
        int z = GetZone(mapId);
        while (map.zones.get(z).getNumOfPlayers() >= map.zones.get(z).maxPlayer) {
            z = Util.nextInt(0, map.zones.size() - 1);
        }
        return map.zones.get(z);
    }

    private Zone getZoneByMapIDAndZoneID(int mapId, int zoneId) {
        Zone zoneJoin = null;
        try {
            Map map = getMapById(mapId);
            if (map != null) {
                zoneJoin = map.zones.get(zoneId);
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
        return zoneJoin;
    }

    public Map getMapById(int mapId) {
        for (Map map : Manager.MAPS) {
            if (map.mapId == mapId) {
                return map;
            }
        }
        return null;
    }

    public Map getMapForCalich() {
        int mapId = Util.nextInt(27, 29);
        return MapService.gI().getMapById(mapId);
    }

    /**
     * Trả về 1 map random cho boss
     */
    public Zone getMapWithRandZone(int mapId) {
        Map map = MapService.gI().getMapById(mapId);
        Zone zone = null;
        try {
            if (map != null) {
                zone = map.zones.get(Util.nextInt(0, map.zones.size() - 1));
            }
        } catch (Exception e) {
        }
        return zone;
    }

    public String getPlanetName(byte planetId) {
        switch (planetId) {
            case 0:
                return "Trái đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "";
        }
    }

    public int getMapTrainOff(Player pl) {
        switch (pl.typetrain) {
            case 0:
                return 46;
            case 1:
            case 2:
                return 45;
            case 3:
            case 4:
                return 48;
            case 5:
                return 50;
            case 6:
            case 7:
                return 154;
            default:
                return 0;
        }
    }

    /**
     * lấy danh sách map cho capsule
     */
    public List<Zone> getMapCapsule(Player pl) {
        List<Zone> list = new ArrayList<>();
        if (pl.mapBeforeCapsule != null
                && pl.mapBeforeCapsule.map.mapId != 21
                && pl.mapBeforeCapsule.map.mapId != 22
                && pl.mapBeforeCapsule.map.mapId != 23
                && !isMapTuongLai(pl.mapBeforeCapsule.map.mapId)) {
            addListMapCapsule(pl, list, pl.mapBeforeCapsule);
        }
        addListMapCapsule(pl, list, getMapCanJoin(pl, 21 + pl.gender, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 47, 0));
//        addListMapCapsule(pl, list, getMapCanJoin(pl, 154, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 0, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 7, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 14, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 5, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 13, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 20, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 24 + pl.gender, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 27, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 19, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 79, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 84, 0));

        return list;
    }

    public List<Zone> getMapBlackBall() {
        List<Zone> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(getMapById(85 + i).zones.get(0));
        }
        return list;
    }

    public List<Zone> getMapMaBu() {
        List<Zone> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(getMapById(114 + i).zones.get(0));
        }
        return list;
    }
    public List<Zone> getMapSatan() {
        List<Zone> list = new ArrayList<>();

        list.add(getMapById(192).zones.get(0));

        return list;
    }
    public List<Zone> getMapVodai() {
        List<Zone> list = new ArrayList<>();

        list.add(getMapById(206).zones.get(0));

        return list;
    }

    private void addListMapCapsule(Player pl, List<Zone> list, Zone zone) {
        for (Zone z : list) {
            if (z != null && zone != null && z.map.mapId == zone.map.mapId) {
                return;
            }
        }
        if (zone != null && pl.zone.map.mapId != zone.map.mapId) {
            list.add(zone);
        }
    }

    public void sendPlayerMove(Player player) {
        Message msg;
        try {
            msg = new Message(-7);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.location.x);
            msg.writer().writeShort(player.location.y);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
    }

    public boolean isMapOffline(int mapId) {
        for (Map map : Manager.MAPS) {
            if (map.mapId == mapId) {
                return map.type == ConstMap.MAP_OFFLINE;
            }
        }
        return false;
    }

    public boolean isMapBlackBallWar(int mapId) {
        return mapId >= 85 && mapId <= 91;
    }

    public boolean isMapthapkarin(int mapId) {
        return mapId >= 45 && mapId <= 50;
    }

    public boolean isMapLeoThap(int mapId) {
        return mapId == 174;
    }

    public boolean isMapMaBu(int mapId) {
        return mapId >= 114 && mapId <= 120;
    }

    public boolean isYardart(int mapId) {
        return mapId >= 131 && mapId <= 133;
    }

    public boolean isMapPVP(int mapId) {
        return mapId == 112;
    }
    
    public boolean isMapSatan(int mapId) {
        return mapId == 192;
    }
    
    public boolean isMapVodai(int mapId) {
        return mapId == 206;
    }

//    public boolean isMapKhiGaHuyDiet(int mapId) {
//        return mapId == 149 || mapId == 150 || mapId == 151 || mapId == 152 || mapId == 153;
//    }
    public boolean isMap22h(int mapId) {
        return mapId == 192;
    }
    public boolean isMapDiaNguc(int mapId) {
        return mapId >= 197 && mapId <= 203;
    }

    public boolean isMapCold(Map map) {
        int mapId = map.mapId;
        return mapId >= 105 && mapId <= 110;
    }

    public boolean isMapKhiGas(int mapId) {
        return mapId == 149 || mapId == 148 || mapId == 147 || mapId == 151 || mapId == 152;
    }

    public boolean isMapConDuongRanDoc(int mapId) {
        return mapId == 141 || mapId == 142 || mapId == 143 || mapId == 144;
    }

    public boolean isMapNha(int mapId) {
        return mapId >= 21 && mapId <= 23;
    }

    public boolean isMapDoanhTrai(int mapId) {
        return mapId >= 53 && mapId <= 62;
    }

    public boolean isMapTrainOff(Player pl, int mapId) {
        return mapId == 46 || mapId == 47 || mapId == 45 || mapId == 48 || mapId == 50 || mapId == 154;
    }

    public boolean isMapHuyDiet(int mapId) {
        return mapId >= 146 && mapId <= 148;
    }

    public boolean isMapBanDoKhoBau(int mapId) {
        return mapId >= 135 && mapId <= 138;
    }
    
    public boolean is22h(int mapId) {
        return mapId == 192;
    }

    public boolean isnguhs(int mapId) {
        return mapId >= -1 && mapId <= -1;
    //     return mapId >= 122 && mapId <= 124;
    }

    public boolean isMapGiaiCuuMiNuong(int mapId) {
        return mapId >= 185 && mapId <= 188;
    }

    public boolean isMapTuongLai(int mapId) {
        return (mapId >= 92 && mapId <= 94)
                || (mapId >= 96 && mapId <= 100)
                || mapId == 102 || mapId == 103;
    }

    public void goToMap(Player player, Zone zoneJoin) {
        Zone oldZone = player.zone;
        if (oldZone != null) {
            ChangeMapService.gI().exitMap(player);
            if (player.mobMe != null) {
                player.mobMe.goToMap(zoneJoin);
            }
        }
        player.zone = zoneJoin;
        player.zone.addPlayer(player);
    }

    public boolean isMapSetKichHoat(int mapId) {
        return (mapId >= 1 && mapId <= 3)
                || (mapId == 8 || mapId == 9 || mapId == 11)
                || (mapId >= 15 && mapId <= 17);
    }

    public boolean isMapKhongCoSieuQuai(int mapId) {
        return !isMapSetKichHoat(mapId)
                && mapId != 4 && mapId != 27 && mapId != 28
                && mapId != 12 && mapId != 31 && mapId != 32
                && mapId != 18 && mapId != 35 && mapId != 36;
    }
}
