package com.girlkun.models.boss;

import com.girlkun.models.boss.list_boss.doanh_trai_old.NinjaAoTim;
import com.girlkun.models.boss.list_boss.doanh_trai_old.RobotVeSi;
import com.girlkun.models.boss.list_boss.doanh_trai_old.TrungUyThep;
import com.girlkun.models.boss.list_boss.doanh_trai_old.TrungUyTrang;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.jdbc.daos.GodGK;
import com.girlkun.models.boss.list_boss.AnTrom;
import com.girlkun.models.boss.list_boss.BLACK.*;
import com.girlkun.models.boss.list_boss.Boss.MaBuCom;
import com.girlkun.models.boss.list_boss.Boss.MaBuDepTrai;
import com.girlkun.models.boss.list_boss.Boss.MaBuMap;
import com.girlkun.models.boss.list_boss.BossEvent.BossBangTinh;
import com.girlkun.models.boss.list_boss.BossEvent.BossHoaTinh;
import com.girlkun.models.boss.list_boss.Broly.BossKhiUltra;
import com.girlkun.models.boss.list_boss.Broly.BossThanMeo;
import com.girlkun.models.boss.list_boss.Broly.BossThoDaiKa;
import com.girlkun.models.boss.list_boss.Cooler.Cooler;
import com.girlkun.models.boss.list_boss.HuyDiet.Champa;
import com.girlkun.models.boss.list_boss.HuyDiet.ThanHuyDiet;
import com.girlkun.models.boss.list_boss.HuyDiet.ThienSuWhis;
import com.girlkun.models.boss.list_boss.HuyDiet.Vados;
import com.girlkun.models.boss.list_boss.NgucTu.CoolerGold;
import com.girlkun.models.boss.list_boss.Doraemon.Doraemon;
import com.girlkun.models.boss.list_boss.FideBack.Kingcold;
import com.girlkun.models.boss.list_boss.Mabu;
import com.girlkun.models.boss.list_boss.SuperXen;
import com.girlkun.models.boss.list_boss.NgucTu.Cumber;
import com.girlkun.models.boss.list_boss.cell.Xencon;
import com.girlkun.models.boss.list_boss.ginyu.TDST;
import com.girlkun.models.boss.list_boss.android.*;
import com.girlkun.models.boss.list_boss.cell.SieuBoHung;
import com.girlkun.models.boss.list_boss.cell.XenBoHung;
import com.girlkun.models.boss.list_boss.Broly.Broly;
import com.girlkun.models.boss.list_boss.Broly.MaVuongPicolo;
import com.girlkun.models.boss.list_boss.Doraemon.Nobita;
import com.girlkun.models.boss.list_boss.Doraemon.Xeko;
import com.girlkun.models.boss.list_boss.Doraemon.Xuka;
import com.girlkun.models.boss.list_boss.FideBack.FideRobot;
import com.girlkun.models.boss.list_boss.NgucTu.SongokuTaAc;
import com.girlkun.models.boss.list_boss.fide.Fide;
import com.girlkun.models.boss.list_boss.Doraemon.Chaien;
import com.girlkun.models.boss.list_boss.NRD.Rong1Sao;
import com.girlkun.models.boss.list_boss.NRD.Rong2Sao;
import com.girlkun.models.boss.list_boss.NRD.Rong3Sao;
import com.girlkun.models.boss.list_boss.NRD.Rong4Sao;
import com.girlkun.models.boss.list_boss.NRD.Rong5Sao;
import com.girlkun.models.boss.list_boss.NRD.Rong6Sao;
import com.girlkun.models.boss.list_boss.NRD.Rong7Sao;
import com.girlkun.models.boss.list_boss.Mabu12h.MabuBoss;
import com.girlkun.models.boss.list_boss.Mabu12h.BuiBui;
import com.girlkun.models.boss.list_boss.Mabu12h.BuiBui2;
import com.girlkun.models.boss.list_boss.Mabu12h.Drabura;
import com.girlkun.models.boss.list_boss.Mabu12h.Drabura2;
import com.girlkun.models.boss.list_boss.Mabu12h.Yacon;
import com.girlkun.models.boss.list_boss.Mai;
import com.girlkun.models.boss.list_boss.PiLap;
import com.girlkun.models.boss.list_boss.Su;
import com.girlkun.models.boss.list_boss.TrainOffline.Bill;
import com.girlkun.models.boss.list_boss.TrainOffline.Bubbles;
import com.girlkun.models.boss.list_boss.TrainOffline.MeoThan;
import com.girlkun.models.boss.list_boss.TrainOffline.Popo;
import com.girlkun.models.boss.list_boss.TrainOffline.ThanVuTru;
import com.girlkun.models.boss.list_boss.TrainOffline.Thuongde;
import com.girlkun.models.boss.list_boss.TrainOffline.ToSuKaio;
import com.girlkun.models.boss.list_boss.TrainOffline.Whis;
import com.girlkun.models.boss.list_boss.TrainOffline.Yajiro;
import com.girlkun.models.boss.list_boss.Yardrat.tapsu4;
import com.girlkun.models.boss.list_boss.gohanNN;
import com.girlkun.models.boss.list_boss.kami.cumberBlack;
import com.girlkun.models.boss.list_boss.kami.cumberYellow;
import com.girlkun.models.boss.list_boss.kami.ThanTai;
import com.girlkun.models.boss.list_boss.kami.kamiRin;
import com.girlkun.models.boss.list_boss.kami.kamiSooMe;
import com.girlkun.models.boss.list_boss.nappa.*;
import com.girlkun.models.boss.list_boss.bandokhobau.TrungUyXanhLoBdkb;
import com.girlkun.models.boss.list_boss.muoichin20.apk1920;
import com.girlkun.models.boss.list_boss.picpocking.picpocking;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerManager;
import com.girlkun.services.ItemMapService;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import com.girlkun.services.func.MiniGame;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BossManager implements Runnable {

    private static BossManager I;
    public static final byte ratioReward = 50;

    public static BossManager gI() {
        if (BossManager.I == null) {
            BossManager.I = new BossManager();
        }
        return BossManager.I;
    }

    private BossManager() {
        this.bosses = new ArrayList<>();
    }

    private boolean loadedBoss;
    private final List<Boss> bosses;

    public List<Boss> getBosses() {
        return this.bosses;
    }

    public void addBoss(Boss boss) {
        this.bosses.add(boss);
    }

    public void removeBoss(Boss boss) {
        this.bosses.remove(boss);
    }

    public void loadBoss() {
        if (this.loadedBoss) {
            return;
        }
        try {
            this.createBoss(BossID.SU);
            this.createBoss(BossID.MAI);
            this.createBoss(BossID.PI_LAP);
            this.createBoss(BossID.KUKUMDDRAMBO);
            this.createBoss(BossID.TDST);
            this.createBoss(BossID.FIDE);
            this.createBoss(BossID.APK1920);
            this.createBoss(BossID.PICPOCKING);
            this.createBoss(BossID.XEN_BO_HUNG);
            this.createBoss(BossID.XEN_CON);
            this.createBoss(BossID.XEN_CON);
            this.createBoss(BossID.SIEU_BO_HUNG);
            this.createBoss(BossID.BOSS_THAN_MEO);
//            this.createBoss(BossID.BOSS_KHI_ULTRA); 
//            this.createBoss(BossID.BOSS_THO_DAI_KA); 
//            this.createBoss(BossID.BOSS_BANG_TINH);
//            this.createBoss(BossID.BOSS_HOA_TINH);

            this.createBoss(BossID.BLACK);
            this.createBoss(BossID.SUPPER_BLACK_GOKU);
            this.createBoss(BossID.ZAMASZIN);
            this.createBoss(BossID.ZAMASZIN);
            this.createBoss(BossID.ZAMASZIN);
            this.createBoss(BossID.ZAMASMAX);
            this.createBoss(BossID.ZAMASMAX);
            this.createBoss(BossID.ZAMASMAX);
            this.createBoss(BossID.TAPSU4);
            this.createBoss(BossID.TAPSU4);
            this.createBoss(BossID.TAPSU4);
//            this.createBoss(BossID.BOSS_MA_VUONG_PICOLO);
            this.createBoss(BossID.BOSS_MA_BU_COM);
            this.createBoss(BossID.BOSS_MA_BU_MAP);
            this.createBoss(BossID.BOSS_MA_BU_DEP_TRAI);
            this.createBoss(BossID.KAMILOC);
            this.createBoss(BossID.BROLY);
            this.createBoss(BossID.BROLY);
            this.createBoss(BossID.BROLY);
            this.createBoss(BossID.BROLY);
            this.createBoss(BossID.BROLY);
            this.createBoss(BossID.XUKA);
            this.createBoss(BossID.CHAIEN);
            this.createBoss(BossID.DORAEMON);
            this.createBoss(BossID.NOBITA);
            this.createBoss(BossID.XEKO);
            this.createBoss(BossID.COOLER_GOLD);
            this.createBoss(BossID.CUMBER);
            this.createBoss(BossID.SONGOKU_TA_AC);
//             this.createBoss(BossID.AN_TROM);
//             this.createBoss(BossID.SUPER_XEN);
//             this.createBoss(BossID.BOSS_BANG_TINH);
//             this.createBoss(BossID.BOSS_HOA_TINH);
//             this.createBoss(BossID.BOSS_THO_DAI_KA);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.loadedBoss = true;
        new Thread(BossManager.I, "Update boss").start();
    }

    public Boss createBossDoanhTrai(int bossID, int dame, int hp, Zone zone) {
        System.out.println("create boss donh trai");
        try {
            switch (bossID) {
                case BossID.TRUNG_UY_TRANG:
                    return new TrungUyTrang(dame, hp, zone);
                case BossID.TRUNG_UY_XANH_LO:
                case BossID.TRUNG_UY_THEP:
                    return new TrungUyThep(dame, hp, zone);
                case BossID.NINJA_AO_TIM:
                    return new NinjaAoTim(dame, hp, zone);
                case BossID.ROBOT_VE_SI1:
                case BossID.ROBOT_VE_SI2:
                case BossID.ROBOT_VE_SI3:
                case BossID.ROBOT_VE_SI4:
                    return new RobotVeSi(bossID, dame, hp, zone);
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi create boss doanh trại");
            return null;
        }

    }

    public Boss createBossBdkb(int bossID, int dame, int hp, Zone zone) {
        try {
            switch (bossID) {
                case BossID.TRUNG_UY_XANH_LO:
                    return new TrungUyXanhLoBdkb(dame, hp, zone);
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Boss createBoss(int bossID) {
        try {
            switch (bossID) {
//                case BossID.AN_TROM:
//                    return new AnTrom();
//                case BossID.CUMBERYELLOW:
//                    return new cumberYellow();
//                case BossID.CUMBERBLACK:
//                    return new cumberBlack();
//                case BossID.KAMIRIN:
//                    return new kamiRin();
//                case BossID.KAMILOC:
//                    return new ThanTai();
//                case BossID.KAMI_SOOME:
//                    return new kamiSooMe();
                case BossID.KUKUMDDRAMBO:
                    return new kukumddrambo();
                case BossID.DRABURA:
                    return new Drabura();
                case BossID.DRABURA_2:
                    return new Drabura2();
                case BossID.BUI_BUI:
                    return new BuiBui();
                case BossID.BUI_BUI_2:
                    return new BuiBui2();
                case BossID.YA_CON:
                    return new Yacon();
                case BossID.MABU_12H:
                    return new MabuBoss();
                case BossID.Rong_1Sao:
                    return new Rong1Sao();
                case BossID.Rong_2Sao:
                    return new Rong2Sao();
                case BossID.Rong_3Sao:
                    return new Rong3Sao();
                case BossID.Rong_4Sao:
                    return new Rong4Sao();
                case BossID.Rong_5Sao:
                    return new Rong5Sao();
                case BossID.Rong_6Sao:
                    return new Rong6Sao();
                case BossID.Rong_7Sao:
                    return new Rong7Sao();
                case BossID.FIDE:
                    return new Fide();
                case BossID.APK1920:
                    return new apk1920();
                case BossID.ANDROID_13:
                    return new Android13();
                case BossID.ANDROID_14:
                    return new Android14();
                case BossID.ANDROID_15:
                    return new Android15();
                case BossID.SUPER_XEN:
                    return new SuperXen();
                case BossID.SU:
                    return new Su();
                case BossID.MAI:
                    return new Mai();
                case BossID.PI_LAP:
                    return new PiLap();
                case BossID.PICPOCKING:
                    return new picpocking();
                case BossID.XEN_BO_HUNG:
                    return new XenBoHung();
                case BossID.SIEU_BO_HUNG:
                    return new SieuBoHung();
                case BossID.XUKA:
                    return new Xuka();
                case BossID.NOBITA:
                    return new Nobita();
                case BossID.XEKO:
                    return new Xeko();
                case BossID.CHAIEN:
                    return new Chaien();
                case BossID.DORAEMON:
                    return new Doraemon();
                case BossID.VUA_COLD:
                    return new Kingcold();
                case BossID.FIDE_ROBOT:
                    return new FideRobot();
                case BossID.ZAMASMAX:
                    return new ZamasMax();
                case BossID.ZAMASZIN:
                    return new ZamasKaio();
                case BossID.BLACK2:
                    return new SuperBlack2();
                case BossID.BLACK1:
                    return new BlackGokuTl();
                case BossID.BLACK:
                    return new Black();
                case BossID.SUPPER_BLACK_GOKU:
                    return new SuperBlackGoku();
                case BossID.XEN_CON:
                    return new Xencon();
                case BossID.MABU:
                    return new Mabu();
                case BossID.TDST:
                    return new TDST();
                case BossID.COOLER_GOLD:
                    return new CoolerGold();
                case BossID.CUMBER:
                    return new Cumber();
                case BossID.THAN_HUY_DIET_CHAMPA:
                    return new Champa();
                case BossID.THIEN_SU_VADOS:
                    return new Vados();
                case BossID.THAN_HUY_DIET:
                    return new ThanHuyDiet();
                case BossID.THIEN_SU_WHIS:
                    return new ThienSuWhis();
                case BossID.SONGOKU_TA_AC:
                    return new SongokuTaAc();
                case BossID.BOSS_THAN_MEO:
                    return new BossThanMeo();
//                case BossID.BOSS_KHI_ULTRA:
//                    return new BossKhiUltra();
//                case BossID.BOSS_MA_VUONG_PICOLO:
//                    return new MaVuongPicolo();
//                case BossID.BOSS_MA_BU_COM:
//                    return new MaBuCom();
//                case BossID.BOSS_MA_BU_MAP:
//                    return new MaBuMap();
//                case BossID.BOSS_MA_BU_DEP_TRAI:
//                    return new MaBuDepTrai();
//                case BossID.BOSS_BANG_TINH:
//                    return new BossBangTinh();
//                case BossID.BOSS_HOA_TINH:
//                    return new BossHoaTinh();
//                case BossID.BOSS_THO_DAI_KA:
//                    return new BossThoDaiKa();
                case BossID.YARI:
                    return new Yajiro();
                case BossID.MR_POPO:
                    return new Popo();
                case BossID.BUBBLES:
                    return new Bubbles();
                case BossID.BROLY:
                    return new Broly();
                case BossID.TAPSU4:
                    return new tapsu4();
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existBossOnPlayer(Player player) {
        return player.zone.getBosses().size() > 0;
    }

    public void teleBoss(Player pl, Message _msg) {
        if (_msg != null) {
            try {
                int id = _msg.reader().readInt();
                Boss b = getBossById(id);
                if (b == null) {
                    Player player = GodGK.loadById(id);
                    if (player != null && player.zone != null) {
                        ChangeMapService.gI().changeMapYardrat(pl, player.zone, player.location.x, player.location.y);
                        return;
                    } else {
                        Service.gI().sendThongBao(pl, "Nó trốn rồi");
                        return;
                    }
                }
                if (b != null && b.zone != null) {
                    ChangeMapService.gI().changeMapYardrat(pl, b.zone, b.location.x, b.location.y);
                } else {
                    Service.gI().sendThongBao(pl, "Boss Hẹo Rồi");
                }
            } catch (IOException e) {
                System.out.println("Loi tele boss");
                e.printStackTrace();
            }
        }
    }

    public void showListBoss(Player player) {
        if (!player.isAdmin() && !player.isAdmin()) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Menu Boss");

            List<Boss> aliveBosses = bosses.stream()
                    .filter(boss -> boss.zone != null)
                    .filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0]) 
                            && !MapService.gI().isMapSatan(boss.data[0].getMapJoin()[0])
                            && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                            && !MapService.gI().isMapVodai(boss.data[0].getMapJoin()[0]))
                    .collect(Collectors.toList());

            msg.writer().writeByte(aliveBosses.size());
            for (int i = 0; i < aliveBosses.size(); i++) {
                Boss boss = aliveBosses.get(i);
                msg.writer().writeInt(i);
                msg.writer().writeInt(i);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);

                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());

                msg.writer().writeUTF(boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu " + boss.zone.zoneId + "");
                if (boss.zone != null) {
                    msg.writer().writeUTF(boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu " + boss.zone.zoneId + "");
                } else {
                    msg.writer().writeUTF("N/A");
                }
            }
//                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
//                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
//                msg.writer().writeUTF(boss.data[0].getName());
//                if (boss.zone != null) {
//                    msg.writer().writeUTF("Sống");
//                    msg.writer().writeUTF("Thông Tin Boss\n" + "|7|Map : " + boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") \nZone: " + boss.zone.zoneId + "\nHP: " + Util.powerToString(boss.nPoint.hp) + "\nDame: " + Util.powerToString(boss.nPoint.dame));
//                } else {
//                    msg.writer().writeUTF("Chết");
//                    msg.writer().writeUTF("Boss Respawn\n|7|Time to Reset : " + (boss.secondsRest <= 0 ? "BossAppear" : boss.secondsRest + " giây"));
//                }
//            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListBoss2(Player player) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss");
            msg.writer()
                    .writeByte(
                            (int) bosses.stream()
                            .filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapSatan(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapVodai(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0]))
                            .count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapSatan(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214 || player.getSession().version < 231) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF(
                            boss.zone.map.mapName + "(" + boss.zone.map.mapId + ")");
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Chết rồi");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi show list boss");
        }
    }

    public void showListBoss3(Player player) {
        if (player.getSession().actived) {
            Service.gI().sendThongBaoOK(player, "Vui Lòng Mở Thành Viên");
            return;
        }
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Menu Boss For Mem");
            msg.writer()
                    .writeByte(
                            (int) bosses.stream()
                                    .filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapSatan(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapConDuongRanDoc(boss.data[0].getMapJoin()[0])
//                                    && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapVodai(boss.data[0].getMapJoin()[0])
//                                    && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0]))
                                    .count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapSatan(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                        || !MapService.gI().isMapVodai(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapConDuongRanDoc(boss.data[0].getMapJoin()[0])){
//                        || MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF("(Dịch chuyển ngay đến Boss)");
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Time to Reset : " + boss.secondsRest-- + " Giây");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListBossNormal(Player player) {

        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss");
            msg.writer()
                    .writeByte(
                            (int) bosses.stream()
                                    .filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapSatan(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
//                                    && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapVodai(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0]))
                                    .count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapSatan(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                        || !MapService.gI().isMapVodai(boss.data[0].getMapJoin()[0]
//                        || MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0]
                        )) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214 || player.getSession().version < 231) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF(
                            "Tự đi tìm đi");
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Chết rồi");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi show list boss");
        }
    }

    public synchronized void callBoss(Player player, int mapId) {
        try {
            if (BossManager.gI().existBossOnPlayer(player)
                    || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                    || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                return;
            }
            Boss k = null;
            switch (mapId) {
                case 85:
                    k = BossManager.gI().createBoss(BossID.Rong_1Sao);
                    break;
                case 86:
                    k = BossManager.gI().createBoss(BossID.Rong_2Sao);
                    break;
                case 87:
                    k = BossManager.gI().createBoss(BossID.Rong_3Sao);
                    break;
                case 88:
                    k = BossManager.gI().createBoss(BossID.Rong_4Sao);
                    break;
                case 89:
                    k = BossManager.gI().createBoss(BossID.Rong_5Sao);
                    break;
                case 90:
                    k = BossManager.gI().createBoss(BossID.Rong_6Sao);
                    break;
                case 91:
                    k = BossManager.gI().createBoss(BossID.Rong_7Sao);
                    break;
            }
            if (k != null) {
                k.currentLevel = 0;
                k.joinMapByZone(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boss getBossById(int bossId) {
        return BossManager.gI().bosses.stream().filter(boss -> boss.id == bossId && !boss.isDie()).findFirst().orElse(null);
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                for (Boss boss : this.bosses) {
                    boss.update();
                }
                Thread.sleep(150 - (System.currentTimeMillis() - st));
            } catch (Exception ignored) {
            }

        }
    }
}
