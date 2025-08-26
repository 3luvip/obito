//package com.girlkun.models.player;
//
//import java.util.ArrayList;
//import java.util.List;
//import com.girlkun.services.MapService;
//import com.girlkun.utils.Util;
//
//public class PlayerManger {
//
//    private static PlayerManger instance;
//
//    private final List<Player> players;
//
//    public PlayerManger() {
//        this.players = new ArrayList<>();
//    }
//
//    public static PlayerManger gI() {
//        if (instance == null) {
//            instance = new PlayerManger();
//        }
//        return instance;
//    }
//
//    public void kick(Player pl) {
//        players.remove(pl);
//        MapService.gI().exitMap(pl, pl.zone);
//        if (pl.pet != null) {
//            players.remove(pl.pet);
//            MapService.gI().exitMap(pl.pet, pl.pet.zone);
//        }
//        if (pl.newpet != null) {
//            players.remove(pl.newpet);
//            MapService.gI().exitMap(pl.newpet, pl.newpet.zone);
//        }
//    }
//
//    public void checkPlayer(int user_id) {
//        List<Player> numPlayer = new ArrayList<>();
//        for (Player player : players) {
//            if (player.isPl() && player.session.get_user_id() == user_id) {
//                numPlayer.add(player);
//            }
//        }
//        if(numPlayer.size() > 1){
//            for(Player player : numPlayer){
//                player.session.disconnect();
//            }
//        }
//    }
//    
//    public Player getPlayerByUserID(int _userID) {
//        for (Player player : players) {
//            if (player != null && !player.isPet && !player.isNewPet&& player.session.get_user_id() == _userID) {
//                return player;
//            }
//        }
//        return null;
//    }
//
//    public Player getPlayerByID(int playerId) {
//        for (int i = 0 ;i< players.size();i++) {
//            Player player = players.get(i);
//            if (player != null && player.id == playerId) {
//                return player;
//            }
//        }
//        return null;
//    }
//
//    public Player getPlayerByName(String name) {
//        for (Player player : players) {
//            if (player != null && !Util.isNullOrEmpty(name) && !Util.isNullOrEmpty(player.name) && player.name.equals(name)) {
//                return player;
//            }
//        }
//        return null;
//    }
//
//    public List<Player> getPlayers() {
//        return players;
//    }
//
//    public List<Player> getPlayers2() {
//        List<Player> pl = new ArrayList<>();
//        for (int i = 0; i < players.size(); i++) {
//            Player player = players.get(i);
//            if (player != null && player.isPl()) {
//                pl.add(player);
//            }
//        }
//        return pl;
//    }
//
//    public int size() {
//        return players.size();
//    }
//}
