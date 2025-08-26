package com.girlkun.models.player;

import BoMong.BoMong;
import com.girlkun.card.Card;
import com.girlkun.models.map.MapMaBu.MapMaBu;
import com.girlkun.models.skill.PlayerSkill;

import java.util.List;

import com.girlkun.models.clan.Clan;
import com.girlkun.models.intrinsic.IntrinsicPlayer;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.ItemTime;
import com.girlkun.models.npc.specialnpc.MagicTree;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.consts.ConstTask;
import com.girlkun.models.npc.specialnpc.MabuEgg;
import com.girlkun.models.mob.MobMe;
import com.girlkun.data.DataGame;
import com.girlkun.models.clan.ClanMember;
import com.girlkun.models.map.MapSatan.MapSatan;
import com.girlkun.models.map.MapVoDai.MapVodai;
import com.girlkun.models.map.Mapchichi.Map22h;
import com.girlkun.models.map.TrapMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.map.bando.BanDoKhoBauService;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.girlkun.models.map.doanhtrai.DoanhTraiService;
import com.girlkun.models.map.nguhanhson.nguhs;
import com.girlkun.models.map.gas.GasService;
import com.girlkun.models.matches.IPVP;
import com.girlkun.models.matches.TYPE_LOSE_PVP;
import com.girlkun.models.matches.TYPE_PVP;
import com.girlkun.models.matches.pvp.DaiHoiVoThuat;
import com.girlkun.models.npc.specialnpc.BillEgg;
import static com.girlkun.models.player.Inventory.LIMIT_GOLD;
import static com.girlkun.models.player.Pet.ATTACK;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Manager;
import com.girlkun.services.Service;
import com.girlkun.server.io.MySession;
import com.girlkun.models.task.TaskPlayer;
import com.girlkun.network.io.Message;
import com.girlkun.server.Client;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.FriendAndEnemyService;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.MapService;
import com.girlkun.services.NpcService;
import com.girlkun.services.PetService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.TaskService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.services.func.ChonAiDay;
import com.girlkun.services.func.CombineNew;
import com.girlkun.services.func.SummonDragon;
import com.girlkun.services.func.TopService;
import com.girlkun.services.func.minigame.ChonAiDay_Gem;
import com.girlkun.services.func.minigame.ChonAiDay_Ruby;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Player {
//    public PlayerEvent event;
    public int gemNormar;
    public int rubyVIP;
    public int rubyNormar;
    public int gemVIP;
    public byte countBDKB;
    public boolean firstJoinBDKB;
    public long lastimeJoinBDKB;
    public int goldChallenge;
    public boolean receivedWoodChest;
    public int BanhChung;
    public int BanhTet;
    public int levelWoodChest;
    private int vodaiBallNumber;
    public List<String> textRuongGo = new ArrayList<>();
    public boolean autodapdo = false;
    public byte vip;
    public List<Zone> mapSatan;
    public MySession session;
    public List<Zone> mapVodai;
    public boolean reloadtitle = true;
    public long sovangauto = 0;
    public boolean isBot = false;
    public boolean haveChangeFlagNamec;
    public long ltggtv4;
    public boolean autobv;
    public boolean autobvrun;
    public int goldTai;
    public long last_time_dd;
    public int goldXiu;
    public boolean beforeDispose;
    public byte role;
    public boolean isReferee;
    public boolean isReferee1;
    public boolean titleitem;
    public boolean haveDuongTang;
    public boolean resetdame = false;
    public long lastTimeDame;
    public double dametong = 0;
    public int account_id;
    public int typeInput = -1;
    public boolean titlett;
    public boolean isTitleUse1;
    public long lastTimeTitle1;
    public boolean isTitleUse2;
    public long lastTimeTitle2;
    public boolean isTitleUse3;
    public long lastTimeTitle3;
    public int point_gapthu;
    public int point_cauca;
    public long rankSieuHang;
    public long numKillSieuHang;
    // skill 10 11
    public int ismonkey;
    public int isbroly;
    public long lastTimetienhoa;

    public long thoigianduhanh;
    public boolean isthoigianduhanh;

    public boolean isPet;
    public boolean isNewPet;
    public boolean isPet3;
    public boolean isfake;
//    public boolean isNewPet1;
    public boolean isBoss;
    public int NguHanhSonPoint = 0;
    public int pointCauCa = 0;
    public int pointNroNamec = 0;
    public int registeredVodai;
    public int ischeckjoinvodai;
    public int xuatsu = 0;
    public long damethanmeo = 0;
    public int ptdame = 0;
    public int diemtrungthu = 0;
    public int diemsukientet = 0;
    public int diemsukienhe = 0;
    public int diemsontinh = 0;
    public int diemthuytinh = 0;
    public int diemnaubanhtrung = 0;
    public int diembitich = 0;
    public int capboss = 0;
    public int thachdauwhis = 0;
    public long diemdanh;
    public int tongtien = 0;
    public int flagMoc10k = 0;
    public int flagMoc20k = 0;
    public int flagMoc50k = 0;
    public int flagMoc100k = 0;
    public int flagMoc200k = 0;
    public int flagMoc500k = 0;
    public int flagMoc1cu = 0;
    public int diemhotong = 0;
    public int Month_Card = 0;
    public int timedanhboss;
    public int bossxuathien;
    public IPVP pvp;
    public int pointPvp;
    public int khigas = 0;
    public byte maxTime = 30;
    public byte type = 0;
    public long timeoff = 0;
    public long lastTimeCauCa;
    public int rateCauCa;
    public int mapIdBeforeLogout;
    public List<Zone> mapBlackBall;
    public List<Zone> mapMaBu;

    public Zone zone;
    public Zone mapBeforeCapsule;
    public List<Zone> mapCapsule;
    public Pet pet;
    public NewPet newpet;
    public Pet3 pet3;

    public MobMe mobMe;
    public Location location;
    public SetClothes setClothes;
    public EffectSkill effectSkill;
    public MabuEgg mabuEgg;
    public BillEgg billEgg;
    public TaskPlayer playerTask;
    public ItemTime itemTime;
    public Fusion fusion;
    public MagicTree magicTree;
    public IntrinsicPlayer playerIntrinsic;
    public Inventory inventory;
    public PlayerSkill playerSkill;
    public CombineNew combineNew;
    public IDMark iDMark;
    public Charms charms;
    public EffectSkin effectSkin;
    public Gift gift;
    public NPoint nPoint;
    public RewardBlackBall rewardBlackBall;
    public EffectFlagBag effectFlagBag;
    public FightMabu fightMabu;
    public SkillSpecial skillSpecial;

    public BoMong achievement;
    public Clan clan;
    public ClanMember clanMember;

    public List<Friend> friends;
    public List<Enemy> enemies;

    public long id;
    public String name;
    public byte gender;
    public boolean isNewMember;
    public short head;

    public boolean banv = false;
    public boolean isdem = false;
    public boolean muav = false;
    public long timeudbv = 0;
    public long timeudmv = 0;
    public long lasttimebanv;
    public long lasttimemuav;

    public byte typePk;

    public byte cFlag;
    public int congduc;

    public boolean haveTennisSpaceShip;

    public boolean justRevived;
    public long lastTimeRevived;

    public int violate;
    public byte totalPlayerViolate;
    public long timeChangeZone;
    public long lastTimeUseOption;

    public short idNRNM = -1;
    public short idGo = -1;
    public long lastTimePickNRNM;
    public int goldNormar;
    public int goldVIP;
    public long lastTimeWin;
    public boolean isWin;
    public List<Card> Cards = new ArrayList<>();
    public short idAura = -1;

    public byte countKG;
    public boolean firstJoinKG;
    public long lastimeJoinKG;

    public int bdkb_countPerDay;
    public long bdkb_lastTimeJoin;
    public boolean bdkb_isJoinBdkb;
    public boolean lockPK;
    public Timer timerDHVT;
    public Player _friendGiaoDich;

    public byte typetrain;
    public int expoff;
    public boolean istrain;
    public boolean istry;
    public boolean istry1;
    public boolean isfight;
    public boolean isfight1;
    public boolean seebossnotnpc;
    public long lastTimeHoiPhuc;
    public float DucNTdamethanmeo;

    public String Hppl = "\n";
    public String tt = "";

    public boolean isvongchan;
    public boolean isdh1;
    public boolean isdh2;
    public boolean isdh3;
    public boolean isdh4;
    public List<Integer> idEffChar = new ArrayList<>();

    public Player() {
        this.location = new Location();
        lastTimeUseOption = System.currentTimeMillis();
        location = new Location();
        nPoint = new NPoint(this);
        inventory = new Inventory();
        playerSkill = new PlayerSkill(this);
        setClothes = new SetClothes(this);
        effectSkill = new EffectSkill(this);
        fusion = new Fusion(this);
        playerIntrinsic = new IntrinsicPlayer();
        rewardBlackBall = new RewardBlackBall(this);
        effectFlagBag = new EffectFlagBag();
        fightMabu = new FightMabu(this);
        //----------------------------------------------------------------------
        iDMark = new IDMark();
        combineNew = new CombineNew();
        playerTask = new TaskPlayer();
        friends = new ArrayList<>();
        enemies = new ArrayList<>();
        itemTime = new ItemTime(this);
        charms = new Charms();
        gift = new Gift(this);
        effectSkin = new EffectSkin(this);
        skillSpecial = new SkillSpecial(this);
        achievement = new BoMong(this);
//        this.typePk = 5; trạng thái pk toàn server
    }

    //--------------------------------------------------------------------------
    public boolean isDie() {
        if (this.nPoint != null) {
            return this.nPoint.hp <= 0;
        }
        return true;
    }

    public void chat(String text) {
        Service.gI().chat(this, text);
    }

    //--------------------------------------------------------------------------
    public void setSession(MySession session) {
        this.session = session;
    }

    public void sendMessage(Message msg) {
        if (this.session != null) {
            session.sendMessage(msg);
        }
    }

    public MySession getSession() {
        return this.session;
    }

    public boolean isPl() {
        return !isPet && !isBoss && !isNewPet && !isPet3 && !isfake;
    }

    public void update() {
        final Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(11);

        if (!this.beforeDispose) {
            try {
                if (!iDMark.isBan()) {

                    if (nPoint != null) {
                        nPoint.update();

                    }

                    if (this.istrain && !MapService.gI().isMapTrainOff(this, this.zone.map.mapId) && this.timeoff >= 30) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, MapService.gI().getMapTrainOff(this), -1, 250);
                        congExpOff();
                        this.timeoff = 0;
                    }

                    if (fusion != null) {
                        fusion.update();
                    }
                    if (effectSkin != null) {
                        effectSkill.update();
                    }
//                    if (!isdem && (hour >= 18 || hour < 5)) {
//                        SummonDragon.gI().activeNight(this);
//                        isdem = true;
//                        Service.getInstance().sendThongBao(this, "Chúc bạn buổi tối vui vẻ");
//                    } 
//                    else if (isdem && (hour >= 5 && hour < 18)) {
//                        SummonDragon.gI().activeDay(this);
//                        isdem = false;
//                        Service.getInstance().sendThongBao(this, "Chúc bạn buổi sáng tốt lành");
//                    }

                    if (mobMe != null) {
                        mobMe.update();
                    }
                    if (this.isPl() && this.clan != null && this.clan.khiGas != null) {
                        GasService.gI().update(this);
                    }
                    if (effectSkin != null) {
                        effectSkin.update();
                    }
                    if (pet != null) {
                        pet.update();
                    }
                    if (newpet != null) {
                        newpet.update();
                    }
                    if (pet3 != null) {
                        pet3.update();
                    }
                    if (magicTree != null) {
                        magicTree.update();
                    }
                    if (itemTime != null) {
                        itemTime.update();
                    }
                    if (itemTime != null) {
                        itemTime.update();
                    }
                    long now = System.currentTimeMillis();
                    if (banv && this != null && Util.canDoWithTime(lasttimebanv, 1000) && (now >= timeudbv + 1000)) {
                        banv(this);
                        timeudbv = System.currentTimeMillis();
                    }
                    if (muav && this != null && Util.canDoWithTime(lasttimemuav, 2000) && (now >= timeudmv + 10000)) {
                        muav(this);
                        timeudmv = System.currentTimeMillis();
                    }
                    if (this.isPl() && this.setClothes.ocTieu == 5) {
                        Service.gI().sendTitle(this, 889);
                    }
                    if (this.isPl() && this.setClothes.picolo == 5) {
                        Service.gI().sendTitle(this, 889);
                    }
                    if (this.isPl() && this.setClothes.pikkoroDaimao == 5) {
                        Service.gI().sendTitle(this, 889);
                    }
                    if (this.isPl() && this.setClothes.nappa == 5) {
                        Service.gI().sendTitle(this, 889);
                    }
                    if (this.isPl() && this.setClothes.cadic == 5) {
                        Service.gI().sendTitle(this, 889);
                    }
                    if (this.isPl() && this.setClothes.kakarot == 5) {
                        Service.gI().sendTitle(this, 889);
                    }
                    if (this.isPl() && this.setClothes.songoku == 5) {
                        Service.gI().sendTitle(this, 889);
                    }
                    if (this.isPl() && this.setClothes.kirin == 5) {
                        Service.gI().sendTitle(this, 889);
                    }
                    if (this.isPl() && this.setClothes.thienXinHang == 5) {
                        Service.gI().sendTitle(this, 889);
                    }
                    if (isPl()) {
                    }
                    if (this.thoigianduhanh > 0 && Util.canDoWithTime(this.thoigianduhanh, 1000)) {
                        this.thoigianduhanh = 0;
                    }
                    nguhs.gI().update(this);
                    BlackBallWar.gI().update(this);
                    MapVodai.gI().update(this);
                    MapMaBu.gI().update(this);
                    MapSatan.gI().update(this);
                    Map22h.gI().update(this);
//                    DoanhTraiService.gI().updatePlayer(this);
                    updateEff(this);
                    if (!isBoss && this.iDMark.isGotoFuture() && Util.canDoWithTime(this.iDMark.getLastTimeGoToFuture(), 6000)) {
                        ChangeMapService.gI().changeMapInYard(this, 102, -1, Util.nextInt(60, 200));
                        this.iDMark.setGotoFuture(false);
                    }
                    if (!isBoss && this.iDMark.isGoToHome() && Util.canDoWithTime(this.iDMark.getLastTimeGoToHome(), 30000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, (this.gender + 21), -1, Util.nextInt(60, 200));
                        this.iDMark.setGoToHome(false);
                    }
                    if (!isBoss && this.iDMark.isGoToHomefromDT() && Util.canDoWithTime(this.iDMark.getLastTimeGoToHomefromDT(), 300000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, (this.gender + 21), -1, Util.nextInt(60, 200));
                        this.iDMark.setGoToHomefromDT(false);
                    }
                    if (this.iDMark.isGoToBDKB() && Util.canDoWithTime(this.iDMark.getLastTimeGoToBDKB(), 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 135, -1, 35);
                        this.iDMark.setGoToBDKB(false);
                    }
                    if (!isBoss && this.iDMark.isGoToCDRD() && Util.canDoWithTime(this.iDMark.getLastTimeGoToCDRD(), 6000)) {
                        ChangeMapService.gI().changeMapInYard(this, 143, -1, 1090);
                        this.iDMark.setGoToCDRD(false);
                    }
                    if (this.iDMark.isGoToGas() && Util.canDoWithTime(this.iDMark.getLastTimeGotoGas(), 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 149, -1, 163);
                        this.iDMark.setGoToGas(false);
                    }

                    if (this.zone != null) {
                        TrapMap trap = this.zone.isInTrap(this);
                        if (trap != null) {
                            trap.doPlayer(this);
                        }
                    }
                    if (this.isPl() && this.inventory.itemsBody.get(7) != null) {
                        Item it = this.inventory.itemsBody.get(7);
                        if (it != null && it.isNotNullItem() && this.newpet == null) {
                            PetService.Pet2(this, it.template.head, it.template.body, it.template.leg);
                            Service.getInstance().point(this);
                        }
                    } else if (this.isPl() && newpet != null && !this.inventory.itemsBody.get(7).isNotNullItem()) {
                        newpet.dispose();
                        newpet = null;
                    }
                    if (this.isPl() && isWin && this.zone.map.mapId == 51 && Util.canDoWithTime(lastTimeWin, 2000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 52, 0, -1);
                        isWin = false;
                    }
                } else {
                    if (Util.canDoWithTime(iDMark.getLastTimeBan(), 5000)) {
                        Client.gI().kickSession(session);
                    }
                }
                if (Util.canDoWithTime(this.lastTimeDame, 5000) && this.dametong != 0) {
                    this.dametong = 0;
                    this.resetdame = true;
                }
            } catch (Exception e) {
                e.getStackTrace();
                Logger.logException(Player.class, e, "Lỗi tại player: " + this.name);
            }
        }
    }

    
//       public void send_text_time_nhan_bua_mien_phi() {
//        if (Util.canDoWithTime(lastTimeSendTextTime, 60000)) {
//            if (this.event.luotNhanBuaMienPhi == 1) {
//                ItemTimeService.gI().sendTextTime(this, TEXT_NHAN_BUA_MIEN_PHI, "Nhận ngẫu nhiên bùa 1h mỗi ngày tại Bà Hạt Mít ở vách núi", 30);
//            }
//            lastTimeSendTextTime = System.currentTimeMillis();
//        }
//    }
    
    public void updateEff(Player player) {
        try {
            if (player.nPoint != null && player.inventory.itemsBody.size() >= 17) {
                for (int i = 12; i <= 16; i++) {
                    Item item = player.inventory.itemsBody.get(i);
                    if (item.isNotNullItem()) {
                        Service.gI().addEffectChar(player, (short) item.template.part, 1, -1, -1, 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------
    /*
     * {380, 381, 382}: ht lưỡng long nhất thể xayda trái đất
     * {383, 384, 385}: ht porata xayda trái đất
     * {391, 392, 393}: ht namếc
     * {870, 871, 872}: ht c2 trái đất
     * {873, 874, 875}: ht c2 namếc
     * {867, 878, 869}: ht c2 xayda
     * {2033,2034,2035}: ht c3 td
     * {2030,2031,2032}: ht c3 nm   
     * {2027,2028,2029}: ht c3 xd*/
    private static final short[][] idOutfitFusion = {
        {380, 381, 382}, {383, 384, 385}, {391, 392, 393},
        {870, 871, 872}, {873, 874, 875}, {867, 868, 869},
        {1276, 1277, 1278}, {1273, 1274, 1275}, {1270, 1271, 1272},
        {1847, 1848, 1849}, {1717, 1718, 1719}, {1850, 1851, 1852},};

    public short getHead() {
        if (nPoint.IsBiHoaDa) {
            return 454;
        }
        if (effectSkill != null && effectSkill.isMonkey) { // biến broly
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } 
        if (effectSkill != null && effectSkill.istienhoa) {
            return (short) ConstPlayer.HEADBIENHINH[this.gender][effectSkill.leveltienhoa - 1];
        
        }
//        else if (this.effectSkill != null && this.effectSkill.istienhoa && this.isbroly == 1 && this.gender == 1) {
//            return (short) 1600;
//        } else if (this.effectSkill != null && this.effectSkill.istienhoa && this.isbroly == 2 && this.gender == 1) {
//            return (short) 1601;
//        } else if (this.effectSkill != null && this.effectSkill.istienhoa && this.isbroly == 3 && this.gender == 1) {
//            return (short) 1602;
//        } else if (this.effectSkill != null && this.effectSkill.istienhoa && this.isbroly == 4 && this.gender == 1) {
//            return (short) 1603;
//        } else if (this.effectSkill != null && this.effectSkill.istienhoa && this.isbroly == 1 && this.gender == 2) {
//            return (short) 1557;
//        } else if (this.effectSkill != null && this.effectSkill.istienhoa && this.isbroly == 2 && this.gender == 2) {
//            return (short) 1558;
//        } else if (this.effectSkill != null && this.effectSkill.istienhoa && this.isbroly == 3 && this.gender == 2) {
//            return (short) 1559;
//        } else if (this.effectSkill != null && this.effectSkill.istienhoa && this.isbroly == 4 && this.gender == 2) {
//            return (short) 1560;
//        } else if (this.effectSkill != null && this.effectSkill.istienhoa && this.isbroly == 1 && this.gender == 0) {
//            return (short) 1550;
//        } else if (this.effectSkill != null && this.effectSkill.istienhoa && this.isbroly == 2 && this.gender == 0) {
//            return (short) 1551;
//        } else if (this.effectSkill != null && this.effectSkill.istienhoa && this.isbroly == 3 && this.gender == 0) {
//            return (short) 1552;
//        } else if (this.effectSkill != null && this.effectSkill.istienhoa && this.isbroly == 4 && this.gender == 0) {
//            return (short) 1553;
//        } else if (this.effectSkill != null && this.effectSkill.isBroly) {
//            switch (this.gender) {
//                case 0:
//                    return (short) ConstPlayer.HEADBROLY2[effectSkill.levelBroly - 1];
//                case 1:
//                    return (short) ConstPlayer.HEADBROLY1[effectSkill.levelBroly - 1];
//                case 2:
//                    return (short) ConstPlayer.HEADBROLY[effectSkill.levelBroly - 1];
//            }
//            return (short) ConstPlayer.HEADBROLY[effectSkill.levelBroly - 1];
//        }
        else if (effectSkill != null && effectSkill.isBinh) {
            return 1413;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 412;
        } else if (this.effectSkill != null && this.effectSkill.isHoaCarot) {
            return 406;
        } else if (effectSkill != null && this.effectSkill.isHoaDa) {
            return 454;
        } else if (this.effectSkill != null && this.effectSkill.isHoaSocola) {
            return 412;
        } else if (this.effectSkill != null && this.effectSkill.isHoaLanh) {
            return 412;
        } else {
            if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
                if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][0];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][0];
//                }
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][0];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][0];
//                }
                    return idOutfitFusion[3 + this.gender][0];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][0];
//                }
                    return idOutfitFusion[6 + this.gender][0];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][0];
//                }
                    return idOutfitFusion[9 + this.gender][0];
                }
            } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
                int head = inventory.itemsBody.get(5).template.head;
                if (head != -1) {
                    return (short) head;
                }
            }
        }
        return this.head;
    }

    public short getBody() {
        if (nPoint.IsBiHoaDa) {
            return 455;
        }
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        }
        if (effectSkill != null && effectSkill.istienhoa) {
            return (short) ConstPlayer.BODYBIENHINH[this.gender];
        
        } 
//        else if (effectSkill.isBroly) {  // biến broly
//            switch (this.gender) {
//                case 0:
//                    return 1554;
//                case 1:
//                    return 1605;
//                case 2:
//                    return 1561;
//            }
//        } else if (effectSkill.istienhoa) { // tiến hóa
//            switch (this.gender) {
//                case 0:
//                    return 1554;
//                case 1:
//                    return 1605;
//                case 2:
//                    return 1561;
//            }
//        } 
        else if (effectSkill != null && effectSkill.isBinh) {
            return 1414;
        } else if (this.effectSkill != null && this.effectSkill.isHoaLanh) {
            return 412;
        } else if (effectSkill != null && effectSkill.isHoaDa) {
            return 455;
        } else if (effectSkill != null && effectSkill.isHoaCarot) {
            return 407;
        } else if (effectSkill != null && effectSkill.isHoaSocola) {
            return 413;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 413;
        } else {
            if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
                if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][1];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][1];
//                }
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][1];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][1];
//                }
                    return idOutfitFusion[3 + this.gender][1];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][1];
//                }
                    return idOutfitFusion[6 + this.gender][1];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][1];
//                }
                    return idOutfitFusion[9 + this.gender][1];
                }
            } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
                int body = inventory.itemsBody.get(5).template.body;
                if (body != -1) {
                    return (short) body;
                }
            }
        }
        if (inventory != null && inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
    }

    public short getLeg() {
        if (nPoint.IsBiHoaDa) {
            return 456;
        }
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        } 
        if (effectSkill != null && effectSkill.istienhoa) {
            return (short) ConstPlayer.LEGBIENHINH[this.gender];
        
        }
//        else if (effectSkill.isBroly) {
//            switch (this.gender) {
//                case 0:
//                    return 1555;
//                case 1:
//                    return 1606;
//                case 2:
//                    return 1562;
//            }
//        } else if (effectSkill.istienhoa) {
//            switch (this.gender) {
//                case 0:
//                    return 1555;
//                case 1:
//                    return 1606;
//                case 2:
//                    return 1562;
//            }
//        } 
        else if (effectSkill != null && effectSkill.isBinh) {
            return 1415;
        } else if (this.effectSkill != null && this.effectSkill.isHoaLanh) {
            return 412;
        } else if (effectSkill != null && effectSkill.isHoaDa) {
            return 456;
        } else if (effectSkill != null && effectSkill.isHoaCarot) {
            return 408;
        } else if (effectSkill != null && effectSkill.isHoaSocola) {
            return 414;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 414;
        } else {
            if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
                if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][2];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][2];
//                }
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][2];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][2];
//                }
                    return idOutfitFusion[3 + this.gender][2];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][2];
//                }
                    return idOutfitFusion[6 + this.gender][2];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][2];
//                }
                    return idOutfitFusion[9 + this.gender][2];
                }
            } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
                int leg = inventory.itemsBody.get(5).template.leg;
                if (leg != -1) {
                    return (short) leg;
                }
            }
        }
        if (inventory != null && inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }
        return (short) (gender == 1 ? 60 : 58);
    }

    public boolean checkPGG() {
        for (Item item : this.inventory.itemsBag) {
            if (item != null && item.template != null && item.template.id == 459  && item.quantity >= 1) {
                return true;
            }
        }
        return false;
    }
    public boolean check99ThucAnHuyDiet() {
        for (Item item : this.inventory.itemsBag) {
            if (item != null && item.template != null && item.template.id >= 663 && item.template.id <= 667 && item.quantity >= 99) {
                return true;
            }
        }
        return false;
    }
    
    public byte getAura() {
        if (this.isPl() && this.effectSkill != null && this.effectSkill.istienhoa) {
            return ConstPlayer.AURABIENHINH[this.gender][this.effectSkill.leveltienhoa - 1];
        }
    if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
        return -1;
    }
    Item item = this.inventory.itemsBody.get(5);
    if (!item.isNotNullItem()) {
        return 0;
    }

    int id = item.template.id;
    // Chuyển đổi template.id sang chỉ số mảng (index)
    Integer[] ids = {
        1274, 1290, 1557, 1715, 1716, 1717, 1718, 1719, 1720, 1721, 1722, 1723, 1724, 
        1725, 1726, 1727, 1728, 1729, 1730, 1731, 1732, 1570, 1781, 1782, 1783, 1784, 
        1785, 1786, 1787, 1788, 1789, 1790, 1791, 1792, 1793, 1794, 1795, 1796, 1797, 
        1798, 1799, 1800, 1801, 1804, 1805, 1806, 1807, 1808, 1809, 1810, 1823, 1843,
        1839, 1840, 1837, 1838, 1842
    };

    Byte[] values = {
        20, 22, 36, 71, 63, 72, 70, 63, 71, 63, 63, 72, 69, 66, 68, 66, 68, 
        66, 68, 66, 59, 56, 69, 71, 70, 70, 66, 68, 70, 64, 75, 71, 73, 63, 
        63, 66, 71, 72, 63, 78, 73, 67, 63, 72, 63, 66, 68, 84, 63, 64, 70,
        79, 84, 84, 71, 84, 84
    };

    for (int i = 0; i < ids.length; i++) {
        if (ids[i] == id) {
            return values[i];
        }
    }
    
    return -1;
}


    public int getVodaiBallNumber() {
        return this.vodaiBallNumber;
    }

    public void setVodaiBallNumber(int number) {
        this.vodaiBallNumber = number;
    }

    public byte getEffFront() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 20) {
            return 4;
        }
        Item item = this.inventory.itemsBody.get(5);
        if (!item.isNotNullItem()) {
            return 4;
        } else if (item.template.id == 1808) {
            return 12;
        } else if (item.template.id == 1809) {
            return 12;
        }
        int levelAo = 0;
        Item.ItemOption optionLevelAo = null;
        int levelQuan = 0;
        Item.ItemOption optionLevelQuan = null;
        int levelGang = 0;
        Item.ItemOption optionLevelGang = null;
        int levelGiay = 0;
        Item.ItemOption optionLevelGiay = null;
        int levelNhan = 0;
        Item.ItemOption optionLevelNhan = null;
        Item itemAo = this.inventory.itemsBody.get(0);
        Item itemQuan = this.inventory.itemsBody.get(1);
        Item itemGang = this.inventory.itemsBody.get(2);
        Item itemGiay = this.inventory.itemsBody.get(3);
        Item itemNhan = this.inventory.itemsBody.get(4);
        for (Item.ItemOption io : itemAo.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelAo = io.param;
                optionLevelAo = io;
                break;
            }
        }
        for (Item.ItemOption io : itemQuan.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelQuan = io.param;
                optionLevelQuan = io;
                break;
            }
        }
        for (Item.ItemOption io : itemGang.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelGang = io.param;
                optionLevelGang = io;
                break;
            }
        }
        for (Item.ItemOption io : itemGiay.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelGiay = io.param;
                optionLevelGiay = io;
                break;
            }
        }
        for (Item.ItemOption io : itemNhan.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelNhan = io.param;
                optionLevelNhan = io;
                break;
            }
        }
        if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 8 && levelQuan >= 8 && levelGang >= 8 && levelGiay >= 8 && levelNhan >= 8) {
            return 8;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 7 && levelQuan >= 7 && levelGang >= 7 && levelGiay >= 7 && levelNhan >= 7) {
            return 7;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 6 && levelQuan >= 6 && levelGang >= 6 && levelGiay >= 6 && levelNhan >= 6) {
            return 6;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 5 && levelQuan >= 5 && levelGang >= 5 && levelGiay >= 5 && levelNhan >= 5) {
            return 5;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 4 && levelQuan >= 4 && levelGang >= 4 && levelGiay >= 4 && levelNhan >= 4) {
            return 4;
        } else {
            return -1;
        }
    }

    public short getFlagBag() {
        if (this.nPoint.isThieuDot && this.nPoint.tlHpGiamDiaNguc > 0) {
            return 45;
        }
        if(this.itemTime.isCauCa){
            return 73;
        }
        if (this.iDMark.isHoldBlackBall()) {
            return 31;
        } else if (this.idNRNM >= 353 && this.idNRNM <= 359) {
            return 30;
        }
        if (this.iDMark.isHoldVodaiBall()) {
            return 30;
        }
        if (this.inventory.itemsBody.size() >= 11) {
            if (this.inventory.itemsBody.get(8).isNotNullItem()) {
                return this.inventory.itemsBody.get(8).template.part;
            }
        }
        if (TaskService.gI().getIdTask(this) == ConstTask.TASK_3_2) {
            return 28;
        }
        if (this.clan != null) {
            return (short) this.clan.imgId;
        }
        return -1;
    }

    public short getMount() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(9);
        if (!item.isNotNullItem()) {
            return -1;
        }
        if (item.template.type == 24) {
            if (item.template.gender == 3 || item.template.gender == this.gender) {
                return item.template.id;
            } else {
                return -1;
            }
        } else {
            if (item.template.id < 500) {
                return item.template.id;
            } else {
                return (short) DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
            }
        }

    }

    public Timer timer;
    public TimerTask task;
    protected boolean actived = false;

    public void active(int delay) {
        if (!actived) {
            actived = true;
            this.timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    Player.this.update();
                }
            };
            this.timer.schedule(task, delay, delay);
        }
    }

    public void hoiphuc(long hp, long mp) { //Hồi phục máu + mana
        if (nPoint.hp == 0 || isDie()) {
            return;
        }
        final long hpfull = nPoint.hpMax;
        if (nPoint.hp + hp >= hpfull || nPoint.hp + hp < 0) {
            nPoint.hp = nPoint.hp + hp < 0 ? 1 : hpfull;
        } else {
            nPoint.hp += hp;
        }
        final long mpfull = nPoint.mpMax;
        if (nPoint.mp + mp >= nPoint.mpMax || nPoint.mp + mp < 0) {
            nPoint.mp = nPoint.mp + mp < 0 ? 1 : mpfull;
        } else {
            nPoint.mp += mp;
        }
        try {
            Service.gI().Send_Info_NV(this);
            if (isPl()) {
                sendInfoHPMP();
            }
        } catch (Exception e) {

        }
    }

    public void sendInfoHPMP() {
        if (!isPl()) {
            return;
        }
        Message msg = null;
        try {
            msg = Service.gI().messageSubCommand((byte) 5);
            msg.writer().writeLong(this.nPoint.hp);
            this.sendMessage(msg);
            msg.cleanup();
            msg = Service.gI().messageSubCommand((byte) 6);
            msg.writer().writeLong(this.nPoint.mp);
            this.sendMessage(msg);
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }
    public void checkAnThan(Player plAtt) {
        if (plAtt != null && (plAtt.isPl() || plAtt.isPet) && plAtt.effectSkill.isAnThan) {
            EffectSkillService.gI().removeAnThan(plAtt);
        }
    }

    //--------------------------------------------------------------------------
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        this.checkAnThan(plAtt);
        if (!this.isDie()) {
            if (this.pet != null && this.pet.status == Pet.PROTECT
                    || this.pet != null && this.pet.status == Pet.ATTACK) {
                this.pet.playerAttack = plAtt;
            }
            boolean isSkillChuong = false;
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        if (this.nPoint.voHieuChuong > 0) {
                            com.girlkun.services.PlayerService.gI().hoiPhuc(this, 0, Util.TamkjllGH(damage * this.nPoint.voHieuChuong / 100));
                            return 0;
                        }
                        isSkillChuong = true;
                }
            }
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                    case Skill.MAKANKOSAPPO:
                    case Skill.QUA_CAU_KENH_KHI:
                    case Skill.LIEN_HOAN_CHUONG:
                        if (!piercing && this.effectSkill.isVoHieuChuong) {
                            int recoveredKi = (int) (damage * 0.15f); // Hồi phục 15% Ki
                            int recoveredHp = (int) (damage * 0.1f); // Hồi phục 10% HP
                            com.girlkun.services.PlayerService.gI().hoiPhuc(this, recoveredKi, recoveredHp);
                            return 0;
                        }
                        break;
                    default:
                        break;
                }
            }
            if (isMobAttack && this.charms.tdBatTu > System.currentTimeMillis() && damage >= this.nPoint.hp) {
                damage = this.nPoint.hp - 1;
            }
            if (plAtt != null) {
                if (isSkillChuong && plAtt.nPoint.multicationChuong > 0 && Util.canDoWithTime(plAtt.nPoint.lastTimeMultiChuong, PlayerSkill.TIME_MUTIL_CHUONG)) {
                    damage *= plAtt.nPoint.multicationChuong;
                    plAtt.nPoint.lastTimeMultiChuong = System.currentTimeMillis();
                }
            }

            this.nPoint.subHP(damage); // xoa tu day
            if (isDie()) {
                if (this.zone.map.mapId == 112) {

                } else {
                    if (MapService.gI().isMapMaBu(this.zone.map.mapId) && plAtt.isPl()) {
                        plAtt.fightMabu.changePoint(1);
                        Service.gI().sendThongBao(plAtt, "|7|Điểm của bạn là " + plAtt.fightMabu.pointMabu);
                    }
                }
                setDie(plAtt);
            }
            return damage; // xoa den day
        } else {
            return 0;
        }
    }

    protected void setDie(Player plAtt) {
        //xóa phù

        if (this.effectSkin.xHPKI > 1) {
            this.effectSkin.xHPKI = 1;
            Service.gI().point(this);
        }
        //xóa tụ skill đặc biệt
        this.playerSkill.prepareQCKK = false;
        this.playerSkill.prepareLaze = false;
        this.playerSkill.prepareTuSat = false;
        //xóa hiệu ứng skill
        this.effectSkill.removeSkillEffectWhenDie();
        //
        nPoint.setHp(0);
        nPoint.setMp(0);
        //xóa trứng
        if (this.mobMe != null) {
            this.mobMe.mobMeDie();
        }
        Service.gI().charDie(this);
        //add kẻ thù
        if (!this.isPet && !this.isNewPet && !this.isPet3 && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isPet3 && !plAtt.isNewPet && !plAtt.isBoss) {
            if (!plAtt.itemTime.isUseAnDanh) {
                FriendAndEnemyService.gI().addEnemy(this, plAtt);
            }
        }
        if (this.isPl() && plAtt != null && plAtt.isPl()) {
            plAtt.achievement.plusCount(3);
        }
        //kết thúc pk
        if (this.pvp != null) {
            this.pvp.lose(this, TYPE_LOSE_PVP.DEAD);
        }

//        PVPServcice.gI().finishPVP(this, PVP.TYPE_DIE);
        BlackBallWar.gI().dropBlackBall(this);
        MapVodai.gI().dropVodaiBall(this);
    }

    public void setDieLv(Player plAtt) {
        if (plAtt != null) {
            // xóa phù
            if (this.effectSkin.xHPKI > 1) {
                this.effectSkin.xHPKI = 1;
                Service.gI().point(this);
            }
            // xóa tụ skill đặc biệt
            this.playerSkill.prepareQCKK = false;
            this.playerSkill.prepareLaze = false;
            this.playerSkill.prepareTuSat = false;
            // xóa hiệu ứng skill
            this.effectSkill.removeSkillEffectWhenDie();
            //
            nPoint.setHp(0);
            nPoint.setMp(0);
            // xóa trứng
            if (this.mobMe != null) {
                this.mobMe.mobMeDie();
            }
            Service.gI().charDie(this);
            // add kẻ thù
            if (!this.isPet && !this.isNewPet && !this.isBoss && plAtt != null && !plAtt.isPet
                    && !plAtt.isNewPet && !plAtt.isBoss) {
                if (!plAtt.itemTime.isUseAnDanh) {
                    FriendAndEnemyService.gI().addEnemy(this, plAtt);
                }
            }
            // kết thúc pk
            if (this.pvp != null) {
                this.pvp.lose(this, TYPE_LOSE_PVP.DEAD);
            }
            // PVPServcice.gI().finishPVP(this, PVP.TYPE_DIE);
            BlackBallWar.gI().dropBlackBall(this);
            MapVodai.gI().dropVodaiBall(this);
        }
    }

    //--------------------------------------------------------------------------
    public void setClanMember() {
        if (this.clanMember != null) {
            this.clanMember.powerPoint = this.nPoint.power;
            this.clanMember.head = this.getHead();
            this.clanMember.body = this.getBody();
            this.clanMember.leg = this.getLeg();
            this.clanMember.leg = this.getAura();
        }
    }

    public boolean isAdmin() {
        return this.session.isAdmin;
    }

    public boolean isAdmin2() {
        return this.session.isAdmin2;
    }

    public void setJustRevivaled() {
        this.justRevived = true;
        this.lastTimeRevived = System.currentTimeMillis();
    }

    public void congExpOff() {

        long exp = this.nPoint.getexp() * this.timeoff;
        Service.gI().addSMTN(this, (byte) 2, exp, false);
        NpcService.gI().createTutorial(this, 536, "Bạn tăng được " + exp + " sức mạnh trong thời gian " + this.timeoff + " phút tập luyện Offline");

    }

    public void preparedToDispose() {

    }

    public void dispose() {
        if (pet != null) {
            pet.dispose();
            pet = null;
        }
        if (pet3 != null) {
            pet3.dispose();
            pet3 = null;
        }
        if (newpet != null) {
            newpet.dispose();
            newpet = null;
        }

        if (mapBlackBall != null) {
            mapBlackBall.clear();
            mapBlackBall = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapMaBu != null) {
            mapMaBu.clear();
            mapMaBu = null;
        }
        if (mapSatan != null) {
            mapSatan.clear();
            mapSatan = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapCapsule != null) {
            mapCapsule.clear();
            mapCapsule = null;
        }
        if (mobMe != null) {
            mobMe.dispose();
            mobMe = null;
        }
        location = null;
        if (setClothes != null) {
            setClothes.dispose();
            setClothes = null;
        }
        if (effectSkill != null) {
            effectSkill.dispose();
            effectSkill = null;
        }
        if (mabuEgg != null) {
            mabuEgg.dispose();
            mabuEgg = null;
        }
        if (billEgg != null) {
            billEgg.dispose();
            billEgg = null;
        }
        if (playerTask != null) {
            playerTask.dispose();
            playerTask = null;
        }
        if (itemTime != null) {
            itemTime.dispose();
            itemTime = null;
        }
        if (fusion != null) {
            fusion.dispose();
            fusion = null;
        }
        if (magicTree != null) {
            magicTree.dispose();
            magicTree = null;
        }
        if (playerIntrinsic != null) {
            playerIntrinsic.dispose();
            playerIntrinsic = null;
        }
        if (inventory != null) {
            inventory.dispose();
            inventory = null;
        }
        if (playerSkill != null) {
            playerSkill.dispose();
            playerSkill = null;
        }
        if (combineNew != null) {
            combineNew.dispose();
            combineNew = null;
        }
        if (iDMark != null) {
            iDMark.dispose();
            iDMark = null;
        }
        if (charms != null) {
            charms.dispose();
            charms = null;
        }
        if (effectSkin != null) {
            effectSkin.dispose();
            effectSkin = null;
        }
        if (gift != null) {
            gift.dispose();
            gift = null;
        }
        if (nPoint != null) {
            nPoint.dispose();
            nPoint = null;
        }
        if (rewardBlackBall != null) {
            rewardBlackBall.dispose();
            rewardBlackBall = null;
        }
        if (mapVodai != null) {
            mapVodai.clear();
            mapVodai = null;
        }
        if (effectFlagBag != null) {
            effectFlagBag.dispose();
            effectFlagBag = null;
        }
        if (pvp != null) {
            pvp.dispose();
            pvp = null;
        }
        effectFlagBag = null;
        clan = null;
        clanMember = null;
        friends = null;
        enemies = null;
        session = null;
        name = null;
    }

    public void banv(Player player) {
        try {
            if (this.banv && player.inventory.gold <= LIMIT_GOLD && player != null) {
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
                        lasttimebanv = System.currentTimeMillis();
                        PlayerService.gI().sendInfoHpMpMoney(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                    } else {
                        Service.getInstance().sendThongBao(player, "Đã đạt giới hạn vàng");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Hết thỏi vàng rồi, đã tắt lệnh bán vàng");
                    this.banv = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void muav(Player player) {
        try {
            if (this.muav && player != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                    if (player.inventory.gold >= 500000000) {
                        player.inventory.gold -= 500000000;
                        Item tv = ItemService.gI().createNewItem((short) 457);
                        InventoryServiceNew.gI().addItemBag(player, tv);
                        lasttimemuav = System.currentTimeMillis();
                        PlayerService.gI().sendInfoHpMpMoney(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                    }
                } else {
                    this.muav = false;
                    Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang, đã tắt tự mua tv");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setfight(byte typeFight, byte typeTatget) {

        try {
            if (typeFight == (byte) 0 && typeTatget == (byte) 0) {
                this.istry = true;
            }
            if (typeFight == (byte) 0 && typeTatget == (byte) 1) {
                this.istry1 = true;
            }
            if (typeFight == (byte) 1 && typeTatget == (byte) 0) {
                this.isfight = true;
            }
            if (typeFight == (byte) 1 && typeTatget == (byte) 1) {
                this.isfight1 = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean IsActiveMaster() {

        if (this.istry || this.isfight) {
            this.istry = true;
        }

        return false;
    }

    public void rsfight() {
        if (this.istry) {
            this.istry = false;
        }
        if (this.istry1) {
            this.istry1 = false;
        }
        if (this.isfight) {
            this.isfight = false;
        }
        if (this.isfight1) {
            this.isfight1 = false;
        }
    }

    public boolean IsTry0() {
        if (this.istry && this.isfight) {
            return true;
        }
        return false;
    }

    public boolean IsTry1() {
        if (this.istry && this.isfight1) {
            return true;
        }
        return false;
    }

    public boolean IsFigh0() {
        if (this.istry && this.isfight1) {
            return true;
        }
        return false;
    }

    public String percentGold(int type) {
        try {
            if (type == 0) {
                double percent = ((double) this.goldNormar / ChonAiDay.gI().goldNormar) * 100;
                return String.valueOf(Math.ceil(percent));
            } else if (type == 1) {
                double percent = ((double) this.goldVIP / ChonAiDay.gI().goldVip) * 100;
                return String.valueOf(Math.ceil(percent));
            }
        } catch (ArithmeticException e) {
            return "0";
        }
        return "0";
    }

    public String percentRuby(int type) {
        try {
            if (type == 0) {
                double denominator2 = ChonAiDay_Ruby.gI().rubyNormar;
                if (denominator2 != 0) {
                    double percent = ((double) this.rubyNormar / denominator2) * 100;
                    return String.valueOf(Math.ceil(percent));
                } else {
                    return "0";
                }
            } else if (type == 1) {
                double denominator2 = ChonAiDay_Ruby.gI().rubyVip;
                if (denominator2 != 0) {
                    double percent = ((double) this.rubyVIP / denominator2) * 100;
                    return String.valueOf(Math.ceil(percent));
                } else {
                    return "0";
                }
            }
        } catch (ArithmeticException e) {
            return "0";
        }
        return "0";
    }

    public String percentGem(int type) {
        try {
            if (type == 0) {
                double denominator3 = ChonAiDay_Gem.gI().gemNormar;
                if (denominator3 != 0) {
                    double percent = ((double) this.gemNormar / denominator3) * 100;
                    return String.valueOf(Math.ceil(percent));
                } else {
                    return "0";
                }
            } else if (type == 1) {
                double denominator3 = ChonAiDay_Gem.gI().gemVip;
                if (denominator3 != 0) {
                    double percent = ((double) this.gemVIP / denominator3) * 100;
                    return String.valueOf(Math.ceil(percent));
                } else {
                    return "0";
                }
            } else {
                return "0";
            }
        } catch (ArithmeticException | NullPointerException e) {
            // Xử lý nếu có lỗi
            return "0";
        }
    }
}
