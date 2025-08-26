package com.girlkun.server;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.database.GirlkunDB;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.network.server.GirlkunSessionManager;
import com.girlkun.network.session.ISession;
import com.girlkun.server.io.MySession;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.services.func.SummonDragon;
import com.girlkun.services.func.TransactionService;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.NgocRongNamecService;
import com.girlkun.utils.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.girlkun.models.matches.pvp.DaiHoiVoThuat;
import com.girlkun.models.matches.pvp.DaiHoiVoThuatService;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.ItemService;
import com.girlkun.services.MapService;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;

public class Client implements Runnable {

    private static Client i;
    private int id = 1_000_000_000;
    private final Map<Long, Player> players_id = new HashMap<Long, Player>();
    private final Map<Integer, Player> players_userId = new HashMap<Integer, Player>();
    private final Map<String, Player> players_name = new HashMap<String, Player>();
    public final List<Player> players = new ArrayList<>();

    private boolean running = true;

    private Client() {
        new Thread(this).start();
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public static Client gI() {
        if (i == null) {
            i = new Client();
        }
        return i;
    }

    public void put(Player player) {
        if (!players_id.containsKey(player.id)) {
            this.players_id.put(player.id, player);
        }
        if (!players_name.containsValue(player)) {
            this.players_name.put(player.name, player);
        }
        if (!players_userId.containsValue(player)) {
            this.players_userId.put(player.getSession().userId, player);
        }
        if (!players.contains(player)) {
            this.players.add(player);
        }

    }
    
    public void createBot(MySession s){
        String[] name1 = {"kin","mai","xayki","mua","baki","trum","xayda","kamejoko","dumk","xox","xnxx","hentai","kion","kimochi","lazada","tiktok"};
        String[] name2 = {"1","2","3","4","5","6","7","8","9","0"};
        String[] name3 = {"1","2","3","4","5","6","7","8","9","0"};
        Player pl = new Player();
        Player temp = Client.gI().getPlayerByUser(1);//GodGK.loadById(2275);
        pl.setSession(s);
        s.userId = id;
        System.out.println("Tạo Boss:" +"[" + id +"]");
        pl.id = id; id++;
        pl.name = name1[Util.nextInt(name1.length)]+name2[Util.nextInt(name2.length)]+name3[Util.nextInt(name3.length)];
        pl.gender = (byte)Util.nextInt(0,2);
        pl.isBot = true;
        pl.isBoss = false;
        pl.isPet = false;
        pl.nPoint.power = Util.nextInt(20000000,20000000);
        pl.nPoint.power *= Util.nextInt(1,40);
        pl.nPoint.hpg = Util.nextInt(2000,50000);
        pl.nPoint.hpMax = Util.nextInt(2000,50000);
        pl.nPoint.hp = pl.nPoint.hpMax / 2;
        pl.nPoint.mpMax = Util.nextInt(2000,50000);
        pl.nPoint.dame = Util.nextInt(2000,5000);
        pl.nPoint.stamina = 32000;
        pl.itemTime.isUseTDLT = true;
        pl.typePk = ConstPlayer.NON_PK;
      
        //skill
        int[] skillsArr = pl.gender == 0 ? new int[]{0, 1, 6, 9, 20}
                    : pl.gender == 1 ? new int[]{2, 3, 17, 18}
                    : new int[]{4, 5, 13};
        for(int j = 0;j<skillsArr.length;j++){
            Skill skill = SkillUtil.createSkill(skillsArr[j], 7);
            pl.playerSkill.skills.add(skill);
        }
        pl.inventory = new Inventory();
        for (int i = 0; i <  12; i++){
            pl.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pl.inventory.gold = 2000000000;
        pl.inventory.itemsBody.set(5, Manager.CAITRANG.get(Util.nextInt(0,Manager.CAITRANG.size()-1)));
     //   pl.inventory.itemsBody.set(10, Manager.LINHTHU.get(Util.nextInt(1,Manager.LINHTHU.size()-1)));
    //    pl.inventory.itemsBody.set(8, Manager.DEOLUNG.get(Util.nextInt(0,Manager.DEOLUNG.size()-1)));
      
       pl.location.y = 50;
  Zone z = MapService.gI().getMapCanJoin(pl, Util.nextInt(150), -1);
while (z != null && (!z.mobs.isEmpty() || z.map.mapId == 21 || z.map.mapId == 22 || z.map.mapId == 23 || z.map.mapId == 6 || z.map.mapId >= 45 && z.map.mapId <= 62 || z.map.mapId >= 85 &&  z.map.mapId <= 91
        || z.map.mapId == 113 || z.map.mapId == 129|| z.map.mapId >= 135 && z.map.mapId <= 15 ||  z.map.mapId >= 164 &&  z.map.mapId <= 224 )) {
    z = MapService.gI().getMapCanJoin(pl, Util.nextInt(150), -1);
}

pl.zone = MapService.gI().getMapCanJoin(pl, Util.nextInt(150), -1);
if (pl.zone == null) return;
if (pl.zone.map == null || pl.zone.map.mapId == 21 || pl.zone.map.mapId == 22 || pl.zone.map.mapId == 21 || pl.zone.map.mapId ==6 || pl.zone.map.mapId >= 45 && pl.zone.map.mapId <= 62 || pl.zone.map.mapId >= 85 &&  pl.zone.map.mapId <= 91
        || pl.zone.map.mapId == 113 || pl.zone.map.mapId == 129|| pl.zone.map.mapId >= 135 && pl.zone.map.mapId <= 15 ||  pl.zone.map.mapId >= 164 &&  pl.zone.map.mapId <= 224  ) return;

pl.location.x = Util.nextInt(300, 1300);
pl.zone.addPlayer(pl);
pl.zone.load_Me_To_Another(pl);
Client.gI().put(pl);
    }

    private void remove(MySession session) {
        if (session.player != null) {
            this.remove(session.player);
            session.player.dispose();
        }
        if (session.joinedGame) {
            session.joinedGame = false;
            try {
                GirlkunDB.executeUpdate("update account set last_time_logout = ? where id = ?", new Timestamp(System.currentTimeMillis()), session.userId);
                GirlkunDB.executeUpdate("update account set last_time_off = ? where id = ?", new Timestamp(System.currentTimeMillis()), session.userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ServerManager.gI().disconnect(session);
    }

    private void remove(Player player) {
        this.players_id.remove(player.id);
        this.players_name.remove(player.name);
        this.players_userId.remove(player.getSession().userId);
        this.players.remove(player);
        if (!player.beforeDispose) {
            DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).removePlayerWait(player);
            DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).removePlayer(player);
            player.beforeDispose = true;
            player.mapIdBeforeLogout = player.zone.map.mapId;
            if (player.idNRNM != -1) {
                ItemMap itemMap = new ItemMap(player.zone, player.idNRNM, 1, player.location.x, player.location.y, -1);
                Service.gI().dropItemMap(player.zone, itemMap);
                NgocRongNamecService.gI().pNrNamec[player.idNRNM - 353] = "";
                NgocRongNamecService.gI().idpNrNamec[player.idNRNM - 353] = -1;
                player.idNRNM = -1;
            }
            ChangeMapService.gI().exitMap(player);
            TransactionService.gI().cancelTrade(player);
            if (player.clan != null) {
                player.clan.removeMemberOnline(null, player);
            }
            if (player.itemTime != null && player.itemTime.isUseTDLT) {
                Item tdlt = null;
                try {
                    tdlt = InventoryServiceNew.gI().findItemBag(player, 521);
                } catch (Exception ex) {
                }
                if (tdlt != null) {
                    ItemTimeService.gI().turnOffTDLT(player, tdlt);
                }
            }
            if (SummonDragon.gI().playerSummonShenron != null
                    && SummonDragon.gI().playerSummonShenron.id == player.id) {
                SummonDragon.gI().isPlayerDisconnect = true;
            }
            if (player.mobMe != null) {
                player.mobMe.mobMeDie();
            }
            if (player.pet != null) {
                if (player.pet.mobMe != null) {
                    player.pet.mobMe.mobMeDie();
                }
                ChangeMapService.gI().exitMap(player.pet);
            }
        }
        PlayerDAO.updatePlayer(player);
    }

    public void kickSession(MySession session) {
        if (session != null) {
            this.remove(session);
            session.disconnect();
        }
    }

    public Player getPlayer(long playerId) {
        return this.players_id.get(playerId);
    }

    public Player getPlayerByUser(int userId) {
        return this.players_userId.get(userId);
    }

    public Player getPlayer(String name) {
        return this.players_name.get(name);
    }

    public void close() {
        Logger.error("BEGIN KICK OUT SESSION.............................." + players.size() + "\n");
//        while(!GirlkunSessionManager.gI().getSessions().isEmpty()){
//            Logger.error("LEFT PLAYER: " + this.players.size() + ".........................\n");
//            this.kickSession((MySession) GirlkunSessionManager.gI().getSessions().remove(0));
//        }
        while (!players.isEmpty()) {
            this.kickSession((MySession) players.remove(0).getSession());
        }
        Logger.error("...........................................SUCCESSFUL\n");
    }

    public void cloneMySessionNotConnect() {
        Logger.error("BEGIN KICK OUT MySession Not Connect...............................\n");
        Logger.error("COUNT: " + GirlkunSessionManager.gI().getSessions().size());
        if (!GirlkunSessionManager.gI().getSessions().isEmpty()) {
            for (int j = 0; j < GirlkunSessionManager.gI().getSessions().size(); j++) {
                MySession m = (MySession) GirlkunSessionManager.gI().getSessions().get(j);
                if (m.player == null) {
                    this.kickSession((MySession) GirlkunSessionManager.gI().getSessions().remove(j));
                }
            }
        }
        Logger.error("..........................................................SUCCESSFUL\n");
    }

    private void update() {
        for (ISession s : GirlkunSessionManager.gI().getSessions()) {
            MySession session = (MySession) s;
            if (session.timeWait > 0) {
                session.timeWait--;
                if (session.timeWait == 0) {
                    kickSession(session);
                }
            }
        }
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                update();
                for (Player pl : Client.gI().players) {
                    if (pl != null) {
                        pl.zone.update();
                    }

                    for (Mob mob : pl.zone.mobs) {
                        mob.update();
                    }
                }
                Thread.sleep(800 - (System.currentTimeMillis() - st));
            } catch (Exception e) {
            }
        }
    }

    public void show(Player player) {
        String txt = "";
        txt += "sessions: " + GirlkunSessionManager.gI().getSessions().size() + "\n";
        txt += "players_id: " + players_id.size() + "\n";
        txt += "players_userId: " + players_userId.size() + "\n";
        txt += "players_name: " + players_name.size() + "\n";
        txt += "players: " + players.size() + "\n";
        Service.gI().sendThongBao(player, txt);
    }
}
