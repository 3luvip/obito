package com.girlkun.models.player;

import com.girlkun.models.item.Item;
import com.girlkun.models.mob.Mob;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.ItemService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.MapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.List;

public class EffectSkin {

    private static final String[] textOdo = new String[]{
        "Hôi quá", "Tránh ra đi thằng ở dơ", "Mùi gì kinh quá vậy?",
        "Kinh tởm quá", "Biến đi thằng ở dơ", "Kính ngài ở dơ"
    };
    private static final String[] textHoaDa = new String[]{
        "Chết rồi", "Tránh ra đi tên khốn!", "Bị hóa đá ròi",
        "Kinh tởm quá", "Thấy ảo chưa nè aaa!", "Kính ngài nước dãi"
    };
    private static final String[] textFrost = new String[]{ // Cải trang ThoDaiCa
        "Lạnh quá?", "Tên Frost bư tiệt", "Có tránh xa ta ra không?"
    };
    private static final String[] test = new String[]{
        "Người gì mà đẹp trai zậy", "Thì ra là Anh bar", "Đẹp trai vô cùng tận luôn đó?"

    };
    private static final String[] seggs = new String[]{
        "Luồng KI Này Thật Kinh Khủng", "Gì Đây Không Gian Đang Bị Bóp Méo",
        "Kinh Khủng Quá", "Hắn Đang Hấp Thụ Mọi Thứ"
    };
    private static final String[] textDrabura = new String[]{ // Cải trang Drabura
        "Năng quá", "Chết tiệt", "Hóa đá rồi", "Tránh xa ta ra"
    };
    private static final String[] textBuibui = new String[]{ // Cải trang Buibui
        "Nặng thế?", "Sao trọng lực xung quanh lại bị thay đổi vậy?", "Không thể di chuyển bình thường được"
    };
    private static final String[] textThoDaiCa = new String[]{ // Cải trang ThoDaiCa
        "Cái củ cà rốt gì vậy?", "Tên thỏ chết tiệt", "Có tránh xa ta ra không?"
    };
    private static final String[] textMabu = new String[]{ // Cải trang ThoDaiCa
        "Cái đéo gì vậy?", "Tên Ma bư tiệt", "Có tránh xa ta ra không?"
    };
    private static final String[] textChichi = new String[]{ // Cải trang Chichi
        "Dễ thương quá!", "Em gái ở đâu ăn gì mà xinh vậy!"
            , "Em ăn cơm chưa?","Đẹp gái vô cừng tận luôn đó!!"
    };
    private static final String[] textSexy = new String[]{ // Cải trang Chichi
        "Á đù gái xinh!", "Ngọt canh xương ống, đậm đà thịt thăn!", "Cho anh xin số người đẹp ơi!"
    };
    private static final String[] textXinBaTo = new String[]{ // Cải trang Chichi
        "Ồn quá!", "im đi cho anh!", "Đừng để anh nóng"
    };
    private Player player;

    public EffectSkin(Player player) {
        this.player = player;
        this.xHPKI = 1;
    }
    private long lastTimeDiaNguc;
    public long lastTimeAttack;
    private long lastTimeOdo;
    private long lastTimeTest;
    private long lastTimeXenHutHpKi;
    private long lastTimeHoaLanh;
    public long lastTimeAddTimeTrainArmor;
    public long lastTimeSubTimeTrainArmor;
    private long lastTimeHoaDa;
    private long lastTimeHoaCaRot;
    public boolean isVoHinh;
    private long lastTimeLamCham;
    public long lastTimePhanTam;
    public long lastTimeXHPKI;
    public int xHPKI;
    private long lastTimeHoaSocola;
    public long lastTimeUpdateCTHT;
    public long lastTimeChichi;
    public long lastTimeThucHienHoaDa;
    public long lastTimeBiHoaDa;
    public long lastTimeYacon;
    public long lastTimeSexy;
    public long lastTimeSoiDaiCa;
    private static final int TIME_HOA_DA = 10000;
    private static final int TIME_HOA_DA_PER_SECOND = 20000;

    public void update() {

        if (this.player != null && this.player.zone != null
                && !MapService.gI().isMapOffline(this.player.zone.map.mapId)) {
            updateOdo();
            updateXenHutXungQuanh();
            LuongKI();
            updateCTHaiTac();
            updateDrabura();
            updateThoDaiCa();
            updateMabu();
            updateFrost();
            updateVoHinh();
            updateBuiBui();
            updateChichi();
            updateSexy();
            updateYacon();
            updateXinBaTo();
            updateSoiDaiCa();
            updateDiaNguc();
        }

        if (!this.player.isBoss && !this.player.isPet && !player.isNewPet) {
            updateTrainArmor();
        }

        if (this.player != null && xHPKI != 1 && Util.canDoWithTime(lastTimeXHPKI, 1800000)) {
            xHPKI = 1;
            Service.gI().point(player);
        }

    }

    private void updateDiaNguc() {
        try {
            if (player.nPoint.isThieuDot) {
                if (Util.canDoWithTime(lastTimeDiaNguc, 10000) && !this.player.isBoss && !this.player.isDie()) {

                    long subHp = this.player.nPoint.hpMax * 20 / 100;
                    if (subHp >= this.player.nPoint.hp) {
                        subHp = this.player.nPoint.hp - 1;
                    }
                    EffectSkillService.gI().setThieuDot(player, System.currentTimeMillis(), 9000);
                    this.player.injured(null, subHp, true, false);
                    this.player.chat("Nóng quá !!");
                    this.lastTimeDiaNguc = System.currentTimeMillis();
                    ItemTimeService.gI().sendItemTime(player, 27499, 9000 / 1000);
                    Service.gI().Send_Info_NV(this.player);
                    PlayerService.gI().sendInfoHpMpMoney(this.player);
                    Service.gI().point(this.player);
                    Service.gI().Send_Caitrang(this.player);
                    Service.getInstance().sendFlagBag(this.player);
                }
            }
        } catch (Exception e) {
            Logger.error("Lỗi địa ngục");
            e.printStackTrace();
        }
    }

    private void updateSoiDaiCa() { // sexy
        try {
            if (this.player.nPoint.isSoiDaiCa == true) {
                if (Util.canDoWithTime(lastTimeSoiDaiCa, 30000)) {

                    EffectSkillService.gI().setDanhHoi(player, System.currentTimeMillis(), 29000);
                    Service.gI().Send_Caitrang(player);
                    ItemTimeService.gI().sendItemTime(player, 4032, 29000 / 1000);
                    Service.gI().chat(player, "Sói cô độc ");
                    PlayerService.gI().sendInfoHpMpMoney(player);
                    Service.gI().Send_Info_NV(player);

                    this.lastTimeSoiDaiCa = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            // Logger.error("Lỗi sói đại ca");
        }
    }

    private void updateXinBaTo() { // Dracula Hóa Đá
        try {
            if (this.player.nPoint.isXinBaTo == true) {
                if (Util.canDoWithTime(lastTimePhanTam, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isPet && !pl.isNewPet && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 600) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        EffectSkillService.gI().setPhanTam(pl, System.currentTimeMillis(), 30000);

                        Service.gI().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 22039, 20000 / 1000);
                        Service.gI().chat(player, "Lạy ông đi qua, lạy bà đi lại, cho con xin ít nước !");
                        Service.gI().chat(pl, textXinBaTo[Util.nextInt(0, textXinBaTo.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                    }
                    this.lastTimePhanTam = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            // Logger.error("Lỗi xinbato");
        }
    }

    // tang hinh boss
    private void updateYacon() { // sexy
        try {
            if (this.player.nPoint.isYacon == true) {
                if (Util.canDoWithTime(lastTimeYacon, 30000)) {

                    EffectSkillService.gI().setAnThan(player, System.currentTimeMillis(), 29000);
                    Service.gI().Send_Caitrang(player);
                    ItemTimeService.gI().sendItemTime(player, 3744, 29000 / 1000);
                    Service.gI().chat(player, "Ẩn thân chi thuật");
                    PlayerService.gI().sendInfoHpMpMoney(player);
                    Service.gI().Send_Info_NV(player);

                    this.lastTimeYacon = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            // Logger.error("Lỗi Yacon");
        }
    }
    // bulma

    private void updateSexy() { // sexy
        try {

            if (this.player.nPoint.isSexy == true) {
                if (Util.canDoWithTime(lastTimeSexy, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!pl.isNewPet && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 600) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        EffectSkillService.gI().setHorny(pl, System.currentTimeMillis(), 30000);
                        Service.gI().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 19526, 20000 / 1000);
                        Service.gI().chat(player, "Chu <3");
                        Service.gI().chat(pl, textSexy[Util.nextInt(0, textSexy.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                    }
                    this.lastTimeSexy = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            // Logger.error("Lỗi Sexy");
        }
    }

    // chichi
    private void updateChichi() {
        try {
            int param = this.player.nPoint.mpHoiCute;
            if (param > 0) {
                if (!this.player.isDie() && Util.canDoWithTime(lastTimeChichi, 5000)) {
                    long mpHut = 0;
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Player pl : players) {
                        long subMp = pl.nPoint.mpMax * param / 100;
                        if (subMp >= pl.nPoint.mp) {
                            subMp = pl.nPoint.mp - 1;
                        }
                        mpHut = subMp;
                        pl.nPoint.addMp(mpHut);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                        Service.gI().chat(pl, textChichi[Util.nextInt(0, textChichi.length - 1)]);

                    }
                    long subMpPl = this.player.nPoint.mpMax * param / 100;
                    if (subMpPl >= this.player.nPoint.mp) {
                        subMpPl = this.player.nPoint.mp - 1;
                    }
                    this.player.nPoint.addMp(subMpPl);
                    PlayerService.gI().sendInfoHpMpMoney(this.player);
                    Service.gI().Send_Info_NV(this.player);
                    this.lastTimeChichi = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            // Logger.error("Lỗi chi chi");
        }
    }

    private void updateBuiBui() { // Dracula Hóa Đá
        try {
            if (this.player.nPoint.isBuiBui == true) {
                if (Util.canDoWithTime(lastTimeLamCham, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isPet && !pl.isNewPet && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 600) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        EffectSkillService.gI().setLamCham(pl, System.currentTimeMillis(), 30000);

                        Service.gI().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 2144, 20000 / 1000);
                        Service.gI().chat(player, "Um ba la");
                        Service.gI().chat(pl, textBuibui[Util.nextInt(0, textBuibui.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                    }
                    this.lastTimeLamCham = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            // Logger.error("Lỗi Buibui");
        }
    }

    private void updateDrabura() { // Dracula Hóa Đá
        try {
            if (this.player.nPoint.isDrabura == true) {
                if (Util.canDoWithTime(lastTimeHoaDa, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isPet && !pl.isNewPet && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 600) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        EffectSkillService.gI().setHoaDa(pl, System.currentTimeMillis(), 30000);
                        EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(), 10000);
                        EffectSkillService.gI().sendEffectPlayer(player, pl, EffectSkillService.TURN_ON_EFFECT,
                                EffectSkillService.BLIND_EFFECT);
                        Service.gI().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 4392, 10000 / 1000);
                        Service.gI().chat(player, "Chíu chíu");
                        Service.gI().chat(pl, textDrabura[Util.nextInt(0, textDrabura.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                    }
                    this.lastTimeHoaDa = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            // Logger.error("Lỗi Drabula");
        }
    }

    private void updateThoDaiCa() { // Thỏ Đại Ca
        try {
            if (this.player.nPoint.isThoDaiCa == true) {
                if (Util.canDoWithTime(lastTimeHoaCaRot, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isNewPet && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 600) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        EffectSkillService.gI().setHoaCarot(pl, System.currentTimeMillis(), 30000);
                        // EffectSkillService.gI().sendEffectPlayer(player, pl,
                        // EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);

                        Service.gI().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 4075, 20000 / 1000);
                        Service.gI().chat(player, "Ta sẽ biến các người thành carot");
                        Service.gI().chat(pl, textThoDaiCa[Util.nextInt(0, textThoDaiCa.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                    }
                    this.lastTimeHoaCaRot = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            // Logger.error("Lỗi thỏ");
        }
    }

    private void updateMabu() { // Mabu
        try {
            if (this.player.nPoint.isMabu == true) {
                if (Util.canDoWithTime(lastTimeHoaSocola, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isNewPet && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 600) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        EffectSkillService.gI().setHoaSocola(pl, System.currentTimeMillis(), 30000);

                        // EffectSkillService.gI().sendEffectPlayer(player, pl,
                        // EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                        Service.gI().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 4127, 20000 / 1000);
                        Service.gI().chat(player, "Ta sẽ biến các người thành Socola");
                        Service.gI().chat(pl, textMabu[Util.nextInt(0, textMabu.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                    }
                    this.lastTimeHoaSocola = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            // Logger.error("Lôi mabu");
        }
    }

    private void LuongKI() {
        try {
            int param = this.player.nPoint.tlHutHpMpXQ;
            if (param > 0) {
                if (!this.player.isDie() && Util.canDoWithTime(lastTimeXenHutHpKi, 5000)) {
                    int hpHut = 0;
                    int mpHut = 0;
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Mob mob : this.player.zone.mobs) {
                        if (mob.point.gethp() > 1) {
                            if (Util.getDistance(this.player, mob) <= 200) {
                                int subHp = mob.point.getHpFull() * param / 100;
                                if (subHp >= mob.point.gethp()) {
                                    subHp = mob.point.gethp() - 1;

                                }
                                hpHut += subHp;
                                mob.injured(null, subHp, false);
                            }
                        }
                    }
                    if (players.size() > 0) {

                        for (Player pl : players) {
                            long subHp = pl.nPoint.hpMax * param / 100;
                            long subMp = pl.nPoint.mpMax * param / 100;
                            if (subHp >= pl.nPoint.hp) {
                                subHp = pl.nPoint.hp - 1;
                                Service.gI().chat(pl, seggs[Util.nextInt(0, seggs.length - 1)]);
                                PlayerService.gI().sendInfoHpMpMoney(pl);
                                Service.gI().Send_Info_NV(pl);
                                pl.injured(null, subHp, true, false);
                            }
                            if (subMp >= pl.nPoint.mp) {
                                subMp = pl.nPoint.mp - 1;
                            }
                            hpHut += subHp;
                            mpHut += subMp;
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.gI().Send_Info_NV(pl);
                            pl.injured(null, subHp, true, false);
                        }
                    }
                    this.player.nPoint.addHp(hpHut);
                    this.player.nPoint.addMp(mpHut);
                    PlayerService.gI().sendInfoHpMpMoney(this.player);
                    Service.gI().Send_Info_NV(this.player);
                    this.lastTimeXenHutHpKi = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            // Logger.error("Lương ki");
        }
    }

    private void updateHoaDa() {
        try {
            boolean CoCaiTrangHoaDa = this.player.nPoint.CoCaiTrangHoaDa;
            if (CoCaiTrangHoaDa) {
                boolean Check = System.currentTimeMillis() - lastTimeThucHienHoaDa > TIME_HOA_DA_PER_SECOND;
                if (Check) { // thời gian hóa đá mỗi lần

                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200 && !pl.nPoint.IsBiHoaDa) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        pl.nPoint.IsBiHoaDa = true;
                        Service.gI().SendMsgUpdateHoaDa(pl, (byte) 1, (byte) 0, (byte) 42);
                        Service.gI().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 4392, (int) TIME_HOA_DA / 1000);
                        Service.gI().chat(pl, textHoaDa[Util.nextInt(0, textHoaDa.length - 1)]);
                        pl.effectSkin.lastTimeBiHoaDa = System.currentTimeMillis();
//                        System.err.println("HOA DA THANH CONG PLAYER: " + pl.name);
                    }
                    this.lastTimeThucHienHoaDa = System.currentTimeMillis();
                }
            }
            if (this.player.nPoint.IsBiHoaDa && (System.currentTimeMillis() - lastTimeBiHoaDa > TIME_HOA_DA)) {
                this.player.nPoint.IsBiHoaDa = false;
                Service.gI().SendMsgUpdateHoaDa(this.player, (byte) 0, (byte) 0, (byte) 42);
                Service.gI().Send_Caitrang(this.player);
                ItemTimeService.gI().removeItemTime(this.player, 4392);
                Service.gI().chat(this.player, "Phẹt Phẹt Phẹt...");
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }

    private void updateFrost() { // Dracula Hóa Đá
        try {
            if (this.player.nPoint.isFrost == true) {
                if (Util.canDoWithTime(lastTimeHoaLanh, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isPet && !pl.isNewPet && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 600) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        EffectSkillService.gI().setHoaLanh(pl, System.currentTimeMillis(), 30000);
                        // EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(), 10000);
                        // EffectSkillService.gI().sendEffectPlayer(player, pl,
                        // EffectSkillService.TURN_ON_EFFECT,
                        // EffectSkillService.BLIND_EFFECT);
                        Service.gI().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 741, 20000 / 1000);
                        Service.gI().chat(player, "Phà phà");
                        Service.gI().chat(pl, textFrost[Util.nextInt(0, textFrost.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                    }
                    this.lastTimeHoaLanh = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            // Logger.error("Lỗi frost");
        }
    }

    private void updateCTHaiTac() {
        if (this.player.setClothes.ctHaiTac != -1
                && this.player.zone != null
                && Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
            int count = 0;
            int[] cts = new int[9];
            cts[this.player.setClothes.ctHaiTac - 618] = this.player.setClothes.ctHaiTac;
            List<Player> players = new ArrayList<>();
            players.add(player);
            try {
                for (Player pl : player.zone.getNotBosses()) {
                    if (!player.equals(pl) && pl.setClothes.ctHaiTac != -1 && Util.getDistance(player, pl) <= 300) {
                        cts[pl.setClothes.ctHaiTac - 618] = pl.setClothes.ctHaiTac;
                        players.add(pl);
                    }
                }
            } catch (Exception e) {
            }
            for (int i = 0; i < cts.length; i++) {
                if (cts[i] != 0) {
                    count++;
                }
            }
            for (Player pl : players) {
                Item ct = pl.inventory.itemsBody.get(5);
                if (ct.isNotNullItem() && ct.template.id >= 618 && ct.template.id <= 626) {
                    for (Item.ItemOption io : ct.itemOptions) {
                        if (io.optionTemplate.id == 147
                                || io.optionTemplate.id == 77
                                || io.optionTemplate.id == 103) {
                            io.param = count * 3;
                        }
                    }
                }
                if (!pl.isPet && !pl.isNewPet && !pl.isPet3 && Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
                    InventoryServiceNew.gI().sendItemBody(pl);
                }
                pl.effectSkin.lastTimeUpdateCTHT = System.currentTimeMillis();
            }
        }
    }

    private void updateXenHutXungQuanh() {
        try {
            int param = this.player.nPoint.tlHutHpMpXQ;
            if (param > 0) {
                if (!this.player.isDie() && Util.canDoWithTime(lastTimeXenHutHpKi, 5000)) {
                    int hpHut = 0;
                    int mpHut = 0;
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Mob mob : this.player.zone.mobs) {
                        if (mob.point.gethp() > 1) {
                            if (Util.getDistance(this.player, mob) <= 200) {
                                int subHp = mob.point.getHpFull() * param / 100;
                                if (subHp >= mob.point.gethp()) {
                                    subHp = mob.point.gethp() - 1;
                                }
                                hpHut += subHp;
                                mob.injured(null, subHp, false);
                            }
                        }
                    }
                    if (players.size() > 0) {
                        for (Player pl : players) {
                            long subHp = pl.nPoint.hpMax * param / 100;
                            long subMp = pl.nPoint.mpMax * param / 100;
                            if (subHp >= pl.nPoint.hp) {
                                subHp = pl.nPoint.hp - 1;
                            }
                            if (subMp >= pl.nPoint.mp) {
                                subMp = pl.nPoint.mp - 1;
                            }
                            hpHut += subHp;
                            mpHut += subMp;
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.gI().Send_Info_NV(pl);
                            pl.injured(null, subHp, true, false);
                        }
                    }
                    this.player.nPoint.addHp(hpHut);
                    this.player.nPoint.addMp(mpHut);
                    PlayerService.gI().sendInfoHpMpMoney(this.player);
                    Service.gI().Send_Info_NV(this.player);
                    this.lastTimeXenHutHpKi = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            // Logger.error("Lỗi xên con");
        }
    }

    private void updateOdo() {
        try {
            int param = this.player.nPoint.tlHpGiamODo;
            if (param > 0) {
                if (Util.canDoWithTime(lastTimeOdo, 10000)) {
                    List<Player> players = new ArrayList<>();

                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Player pl : players) {
                        long subHp = pl.nPoint.hpMax * param / 100;
                        if (subHp >= pl.nPoint.hp) {
                            subHp = pl.nPoint.hp - 1;
                        }
                        Service.gI().chat(pl, textOdo[Util.nextInt(0, textOdo.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                        pl.injured(null, subHp, true, false);
                    }
                    this.lastTimeOdo = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            // Logger.error("Lỗi ở dơ");
        }
    }

    //giáp tập luyện
    private void updateTrainArmor() {
        if (Util.canDoWithTime(lastTimeAddTimeTrainArmor, 60000) && !Util.canDoWithTime(lastTimeAttack, 30000)) {
            if (this.player.nPoint.wearingTrainArmor) {
                for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                    if (io.optionTemplate.id == 9) {
                        if (io.param < 1000) {
                            io.param++;
                            InventoryServiceNew.gI().sendItemBody(player);
                        }
                        break;
                    }
                }
            }
            this.lastTimeAddTimeTrainArmor = System.currentTimeMillis();
        }
        if (Util.canDoWithTime(lastTimeSubTimeTrainArmor, 60000)) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem()) {
                    if (ItemService.gI().isTrainArmor(item)) {
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 9) {
                                if (io.param > 0) {
                                    io.param--;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            for (Item item : this.player.inventory.itemsBox) {
                if (item.isNotNullItem()) {
                    if (ItemService.gI().isTrainArmor(item)) {
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 9) {
                                if (io.param > 0) {
                                    io.param--;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            this.lastTimeSubTimeTrainArmor = System.currentTimeMillis();
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().point(this.player);
        }
    }

    private void updateVoHinh() {
//        if (this.player.nPoint.wearingVoHinh) {
        if (Util.canDoWithTime(lastTimeAttack, 10000) && this.player.nPoint.wearingVoHinh) {
            isVoHinh = true;
        } else {
            isVoHinh = false;
        }
//        }
    }

    public void dispose() {
        this.player = null;
    }
}
