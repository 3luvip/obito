package com.girlkun.models.player;

import com.girlkun.models.mob.Mob;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.ItemTimeService;
import com.girlkun.utils.Util;
import com.girlkun.services.Service;

public class EffectSkill {

    private Player player;

    //thái dương hạ san
    public boolean isStun;
    public long lastTimeStartStun;
    public int timeStun;

    //khiên năng lượng
    public boolean isShielding;
    public long lastTimeShieldUp;
    public int timeShield;

    //biến khỉ
    public boolean isMonkey;
    public byte levelMonkey;
    public long lastTimeUpMonkey;
    public int timeMonkey;
    
    // biến broly
    public boolean isBroly;
    public byte levelBroly;
    public long lastTimeUpBroly;
    public int timeBroly;
    // Cải trang xin ba to
    public boolean isPhanTam;
    public long lastTimePhanTam;
    public int timePhanTam;
    //tiến hóa
    public boolean istienhoa;
    public int leveltienhoa = 0;
    public long lastTimeUptienhoa;
    public int timetienhoa;

    // Vô hiệu chưởng
    public boolean isVoHieuChuong;
    public long lastTimeVoHieuChuong;
    public int timeVoHieuChuong;
    
    //tái tạo năng lượng
    public boolean isCharging;
    public int countCharging;

    //huýt sáo
    public int tiLeHPHuytSao;
    public long lastTimeHuytSao;

    //thôi miên
    public boolean isThoiMien;
    public long lastTimeThoiMien;
    public int timeThoiMien;
    
    //biến lạnh
    public long lastTimeHoaLanh;

    //trói
    public boolean useTroi;
    public boolean anTroi;
    public long lastTimeTroi;
//    public long lastTimeAnTroi;
    public int timeTroi;
//    public int timeAnTroi;
    public Player plTroi;
    public Player plAnTroi;
    public Mob mobAnTroi;
    public boolean isAnThan;
    public long lastTimeAnThan;
    public int timeAnThan;
    //dịch chuyển tức thời
    public boolean isBlindDCTT;
    public long lastTimeBlindDCTT;
    public int timeBlindDCTT;
    //socola
    public boolean isHoaLanh;
    public boolean isSocola;
    public boolean isBinh;
    public long lastTimeSocola;
    public int timeSocola;
    public long lastTimeBinh;
    public int timeBinh;
    public int countPem1hp;
    public int timeHoaLanh;
    //soi
    public boolean isDanhHoi;
    public long lastTimeDanhHoi;
    public int timeDanhHoi;
     // cuồng chiến
    public boolean isCuongChien;
    public long lastTimeCuongChien;
    // Cải trang Thỏ Đại Ca
    public boolean isHoaCarot;
    public long lastTimeHoaCarot;
    public int timeHoaCarot;
    // Cải trang Buibui
    public boolean isLamCham;
    public long lastTimeLamCham;
    public int timeLamCham;
    // Cải trang Dracula hóa đá
    public boolean isHoaDa;
    public long lastTimeHoaDa;
    public int timeHoaDa;
    // Cải trang Sexy
    public boolean isHorny;
    public long lastTimeHorny;
    public int timeHorny;
    //
    public boolean isThieuDot;
    public long lastTimeThieuDot;
    public int timeThieuDot;
    //
    public boolean isHoaSocola;
    public long lastTimeHoaSocola;
    public int timeHoaSocola;
    // biến thành cái bình
    public boolean isMaPhongBa;
    public long lastTimeMaPhongBa;
    public int timeMaPhongBa;

    public EffectSkill(Player player) {
        this.player = player;
    }

    public void removeSkillEffectWhenDie() {
        if (isMonkey) {
            EffectSkillService.gI().monkeyDown(player);
        }
        if (isCuongChien) {
            EffectSkillService.gI().removeCuongChien(player);
        }
        if (isBroly) {
            EffectSkillService.gI().brolyDown(player);
        }
        if (istienhoa) {
            EffectSkillService.gI().brolyDown(player);
        }
        if (isVoHieuChuong) {
            EffectSkillService.gI().vohieuDown(player);
        }
        if (isShielding) {
            EffectSkillService.gI().removeShield(player);
            ItemTimeService.gI().removeItemTime(player, 3784);
        }
        if (useTroi) {
            EffectSkillService.gI().removeUseTroi(this.player);
        }
        if (isStun) {
            EffectSkillService.gI().removeStun(this.player);
        }
        if (isThoiMien) {
            EffectSkillService.gI().removeThoiMien(this.player);
        }
        if (isBlindDCTT) {
            EffectSkillService.gI().removeBlindDCTT(this.player);
        }
    }

    public void update() {
        if (isMonkey && (Util.canDoWithTime(lastTimeUpMonkey, timeMonkey))) {
            EffectSkillService.gI().monkeyDown(player);
        }
        if (isBroly && (Util.canDoWithTime(lastTimeUpBroly, timeBroly))) {
            EffectSkillService.gI().brolyDown(player);
        }
        if (istienhoa && (Util.canDoWithTime(lastTimeUptienhoa, timetienhoa))) {
            EffectSkillService.gI().tienhoaDown(player);
        }
        if (isVoHieuChuong && (Util.canDoWithTime(lastTimeVoHieuChuong, timeVoHieuChuong))) {
            EffectSkillService.gI().vohieuDown(player);
        }
        if (isCuongChien && (Util.canDoWithTime(lastTimeCuongChien, 5000))) {
            EffectSkillService.gI().removeCuongChien(player);
        }
        if (isShielding && (Util.canDoWithTime(lastTimeShieldUp, timeShield))) {
            EffectSkillService.gI().removeShield(player);
        }
        if (useTroi && Util.canDoWithTime(lastTimeTroi, timeTroi)
                || plAnTroi != null && plAnTroi.isDie()
                || useTroi && isHaveEffectSkill()) {
            EffectSkillService.gI().removeUseTroi(this.player);
        }
//        if (anTroi && (Util.canDoWithTime(lastTimeAnTroi, timeAnTroi) || player.isDie())) {
//            EffectSkillService.gI().removeAnTroi(this.player);
//        }
        if (isStun && Util.canDoWithTime(lastTimeStartStun, timeStun)) {
            EffectSkillService.gI().removeStun(this.player);
        }
        if (isThoiMien && (Util.canDoWithTime(lastTimeThoiMien, timeThoiMien))) {
            EffectSkillService.gI().removeThoiMien(this.player);
        }
        if (isBlindDCTT && (Util.canDoWithTime(lastTimeBlindDCTT, timeBlindDCTT))) {
            EffectSkillService.gI().removeBlindDCTT(this.player);
        }
        if (isHoaDa && (Util.canDoWithTime(lastTimeHoaDa, 10000))) {
            EffectSkillService.gI().removeHoaDa(this.player);
        }
        if (isLamCham && (Util.canDoWithTime(lastTimeLamCham, 20000))) {
            EffectSkillService.gI().removeLamCham(this.player);
        }
        if (isHoaCarot && (Util.canDoWithTime(lastTimeHoaCarot, 20000))) {
            EffectSkillService.gI().removeHoaCarot(this.player);
        }
        if (isHoaLanh && (Util.canDoWithTime(lastTimeHoaLanh, timeHoaLanh))) {
            EffectSkillService.gI().removeHoaLanh(this.player);
        }
        if (isHorny && (Util.canDoWithTime(lastTimeHorny, 20000))) {
            EffectSkillService.gI().removeHorny(this.player);
        }
        if (isThieuDot && (Util.canDoWithTime(lastTimeThieuDot, 10000))) {
            EffectSkillService.gI().removeThieuDot(this.player);
        }
        if (isHoaSocola && (Util.canDoWithTime(lastTimeHoaSocola, 20000))) {
            EffectSkillService.gI().removeHoaSocola(this.player);
        }
        if (isSocola && (Util.canDoWithTime(lastTimeSocola, timeSocola))) {
            EffectSkillService.gI().removeSocola(this.player);
        }
        if (tiLeHPHuytSao != 0 && Util.canDoWithTime(lastTimeHuytSao, 30000)) {
            EffectSkillService.gI().removeHuytSao(this.player);
        }
        if (isMaPhongBa && (Util.canDoWithTime(lastTimeMaPhongBa, timeMaPhongBa))) {
            EffectSkillService.gI().removeMaPhongBa(this.player);
        }
        if (isDanhHoi && (Util.canDoWithTime(lastTimeDanhHoi, 29000))) {
            EffectSkillService.gI().removeDanhHoi(this.player);
        }
        if (isPhanTam && (Util.canDoWithTime(lastTimePhanTam, 20000))) {
            EffectSkillService.gI().removePhanTam(this.player);
        }
    }

    public boolean isHaveEffectSkill() {
        return isStun || isBlindDCTT || anTroi || isThoiMien;
    }

    public void dispose() {
        this.player = null;
    }
}
