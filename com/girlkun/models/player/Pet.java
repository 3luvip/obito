package com.girlkun.models.player;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.item.Item;
import com.girlkun.services.MapService;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.skill.Skill;
import com.girlkun.utils.SkillUtil;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import com.girlkun.network.io.Message;
import com.girlkun.server.Manager;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.SkillService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.TimeUtil;
import java.util.Calendar;

public class Pet extends Player {

    public byte thuctinh = 1;

    public String getNameThuctinh(byte thuctinh) {
        switch (thuctinh) {
            case 1:
                return "[Nâng Cấp +1]";
            case 2:
                return "[Nâng Cấp +2]";
            case 3:
                return "[Nâng Cấp +3]";
            case 4:
                return "[Nâng Cấp +4]";
            case 5:
                return "[Nâng Cấp +5]";
            case 6:
                return "[Nâng Cấp +6]";
            case 7:
                return "[Nâng Cấp +7]";
            case 8:
                return "[Nâng Cấp +8]";
            case 9:
                return "[Nâng Cấp +9]";
            case 10:
                return "[Tiến Hoá]";
            case 11:
                return "[Tiến Hoá +1]";
            case 12:
                return "[Tiến Hoá +2]";
            case 13:
                return "[Tiến Hoá +3]";
            case 14:
                return "[Tiến Hoá +4]";
            case 15:
                return "[Tiến Hoá +5]";
            case 16:
                return "[Tiến Hoá +6]";
            case 17:
                return "[Tiến Hoá +7]";
            case 18:
                return "[Tiến Hoá +8]";
            case 19:
                return "[Tiến Hoá +9]";
            case 20:
                return "[Thức Tỉnh]";
            case 21:
                return "[Thức Tỉnh +1]";
            case 22:
                return "[Thức Tỉnh +2]";
            case 23:
                return "[Thức Tỉnh +3]";
            case 24:
                return "[Thức Tỉnh +4]";
            case 25:
                return "[Thức Tỉnh +5]";
            case 26:
                return "[Thức Tỉnh +6]";
            case 27:
                return "[Thức Tỉnh +7]";
            case 28:
                return "[Thức Tỉnh +8]";
            case 29:
                return "[Thức Tỉnh +9]";
            case 30:
                return "[Thần Linh]";
            default:
                return "[Chưa tiến cấp]";
        }
    }

    private static final short ARANGE_CAN_ATTACK = 300;
    private static final short ARANGE_ATT_SKILL1 = 100;

    private static final short[][] PET_ID = {{285, 286, 287}, {288, 289, 290}, {282, 283, 284}, {304, 305, 303}};

    public static final byte FOLLOW = 0;
    public static final byte PROTECT = 1;
    public static final byte ATTACK = 2;
    public static final byte GOHOME = 3;
    public static final byte FUSION = 4;
    private static final String[] chatPr = {""};

    public Player master;
    public byte status = 0;

    public byte typePet;
    public boolean isTransform;

    public long lastTimeDie;

    private boolean goingHome;

    private Mob mobAttack;
    protected Player playerAttack;

    private static final int TIME_WAIT_AFTER_UNFUSION = 5000;
    private long lastTimeUnfusion;

    public byte getStatus() {
        return this.status;
    }

    public Pet(Player master) {
        this.master = master;
        this.isPet = true;
    }

    public void changeStatus(byte status) {
        if (goingHome || master.fusion.typeFusion != 0 || (this.isDie() && status == FUSION)) {
            Service.gI().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        Service.gI().chatJustForMe(master, this, getTextStatus(status));
        if (status == GOHOME) {
            goHome();
        } else if (status == FUSION) {
            fusion(false);
        }
        this.status = status;
    }

    public void joinMapMaster() {
        if (status != GOHOME && status != FUSION && !isDie()) {
            this.location.x = master.location.x + Util.nextInt(-10, 10);
            this.location.y = master.location.y;
            ChangeMapService.gI().goToMap(this, master.zone);
            this.zone.load_Me_To_Another(this);
        }
    }

    public void goHome() {
        if (this.status == GOHOME) {
            return;
        }
        goingHome = true;
//        new Thread(() -> {
        try {
            Pet.this.status = Pet.ATTACK;
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        ChangeMapService.gI().goToMap(this, MapService.gI().getMapCanJoin(this, master.gender + 21, -1));
        this.zone.load_Me_To_Another(this);
        Pet.this.status = Pet.GOHOME;
        goingHome = false;
//        }).start();
    }

    private String getTextStatus(byte status) {
        switch (status) {
            case FOLLOW:
                return "Ok con theo sư phụ";
            case PROTECT:
                return "Á..À!!Mày Dám Đánh Sư Phụ Tao À, con sẽ bảo vệ sư phụ";
            case ATTACK:
                return "Ok sư phụ, Thích Thì Đấm, Đụng Thì Chạm";
            case GOHOME:
                return "Ok con về, bibi sư phụ";
            default:
                return "";
        }
    }

    public void fusion(boolean porata) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusion2(boolean porata2) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata2) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA2;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusion3(boolean porata3) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata3) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA3;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusion4(boolean porata4) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata4) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA4;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void unFusion() {
        master.fusion.typeFusion = 0;
        this.status = PROTECT;
        Service.gI().point(master);
        joinMapMaster();
        fusionEffect(master.fusion.typeFusion);
        Service.gI().Send_Caitrang(master);
        Service.gI().point(master);
        this.lastTimeUnfusion = System.currentTimeMillis();
    }

    private void fusionEffect(int type) {
        Message msg;
        try {
            msg = new Message(125);
            msg.writer().writeByte(type);
            msg.writer().writeInt((int) master.id);
            Service.gI().sendMessAllPlayerInMap(master, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public long lastTimeMoveIdle;
    private int timeMoveIdle;
    public boolean idle;

    private void moveIdle() {
        if (status == GOHOME || status == FUSION) {
            return;
        }
        if (idle && Util.canDoWithTime(lastTimeMoveIdle, timeMoveIdle)) {
            int dir = this.location.x - master.location.x <= 0 ? -1 : 1;
            PlayerService.gI().playerMove(this, master.location.x
                    + Util.nextInt(dir == -1 ? 30 : -50, dir == -1 ? 50 : 30), master.location.y);
            lastTimeMoveIdle = System.currentTimeMillis();
            timeMoveIdle = Util.nextInt(5000, 8000);
        }
    }

    private long lastTimeMoveAtHome;
    private byte directAtHome = -1;

    @Override
    public void update() {
        try {
            super.update();
            increasePoint(); //cộng chỉ số
            updatePower(); //check mở skill...
            if (isDie()) {
                if (System.currentTimeMillis() - lastTimeDie > 50000) {
                    Service.gI().hsChar(this, nPoint.hpMax, nPoint.mpMax);
                } else {
                    return;
                }
            }

            if (justRevived && this.zone == master.zone) {
                Service.gI().chatJustForMe(master, this, "Sư phụ ơi, con đây nè!");
                justRevived = false;
            }

            if (this.zone == null || this.zone != master.zone) {
                joinMapMaster();
            }
            if (master.isDie() || this.isDie() || effectSkill.isHaveEffectSkill()) {
                return;
            }

            moveIdle();
            switch (status) {
                case FOLLOW:
                    followMaster(40);
                    break;
                case PROTECT:

                    if (playerAttack != null && playerAttack.zone.equals(this.zone) && !playerAttack.isDie()) {
                        if (Util.isTrue(1, 10)) {
                            Service.gI().chat(this, chatPr[Util.nextInt(chatPr.length)]);
                        }
                        int disToMob = Util.getDistance(this, playerAttack);
                        if (disToMob <= ARANGE_ATT_SKILL1) {
                            //đấm
                            this.playerSkill.skillSelect = getSkill(1);
                            if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    PlayerService.gI().playerMove(this, playerAttack.location.x + Util.nextInt(-20, 20), playerAttack.location.y);
                                    SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                } else {
                                    askPea();
                                }
                            }
                        } else {
                            //chưởng
                            this.playerSkill.skillSelect = getSkill(2);
                            if (this.playerSkill.skillSelect.skillId != -1) {
                                if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
                                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                    } else {
                                        askPea();
                                    }
                                }
                            }
                        }
                        return;
                    } else {
                        playerAttack = null;
                    }
                    if (useSkill3() || useSkill4() || useSkill5()) {
                        break;
                    }
                    mobAttack = findMobAttack();
                    if (mobAttack != null) {
                        int disToMob = Util.getDistance(this, mobAttack);
                        if (disToMob <= ARANGE_ATT_SKILL1) {
                            //đấm
                            this.playerSkill.skillSelect = getSkill(1);
                            if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                    SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                } else {
                                    askPea();
                                }
                            }
                        } else {
                            //chưởng
                            this.playerSkill.skillSelect = getSkill(2);
                            if (this.playerSkill.skillSelect.skillId != -1) {
                                if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
                                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                    } else {
                                        askPea();
                                    }
                                }
                            }
                        }

                    } else {
                        idle = true;
                    }

                    break;
                case ATTACK:
                    if (useSkill3() || useSkill4() || useSkill5()) {
                        break;
                    }
                    mobAttack = findMobAttack();
                    if (mobAttack != null) {
                        int disToMob = Util.getDistance(this, mobAttack);
                        if (disToMob <= ARANGE_ATT_SKILL1) {
                            this.playerSkill.skillSelect = getSkill(1);
                            if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                    SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                } else {
                                    askPea();
                                }
                            }
                        } else {
                            this.playerSkill.skillSelect = getSkill(2);
                            if (this.playerSkill.skillSelect.skillId != -1) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                }
                            } else {
                                this.playerSkill.skillSelect = getSkill(1);
                                if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
                                        PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                    } else {
                                        askPea();
                                    }
                                }
                            }
                        }

                    } else {
                        idle = true;
                    }
                    break;

                case GOHOME:
                    if (this.zone != null && (this.zone.map.mapId == 21 || this.zone.map.mapId == 22 || this.zone.map.mapId == 23)) {
                        if (System.currentTimeMillis() - lastTimeMoveAtHome <= 5000) {
                            return;
                        } else {
                            if (this.zone.map.mapId == 21) {
                                if (directAtHome == -1) {

                                    PlayerService.gI().playerMove(this, 250, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 200, 336);
                                    directAtHome = -1;
                                }
                            } else if (this.zone.map.mapId == 22) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 500, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 452, 336);
                                    directAtHome = -1;
                                }
                            } else if (this.zone.map.mapId == 22) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 250, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 200, 336);
                                    directAtHome = -1;
                                }
                            }
                            Service.gI().chatJustForMe(master, this, "H2O + C12H22O11 -> Uống ngọt lắm sư phụ ạ!");
                            lastTimeMoveAtHome = System.currentTimeMillis();
                        }
                    }
                    break;
            }
        } catch (Exception e) {
//            Logger.logException(Pet.class, e);
        }
    }

    private long lastTimeAskPea;

    public void askPea() {
        if (Util.canDoWithTime(lastTimeAskPea, 10000)) {
            Service.gI().chatJustForMe(master, this, "Sư phụ ơi cho con đậu thần đi, con đói sắp chết rồi !!");
            lastTimeAskPea = System.currentTimeMillis();
        }
    }

    private int countTTNL;
    final Calendar rightNow = Calendar.getInstance();
    int hour = rightNow.get(11);

    private boolean useSkill3() {
        try {
            playerSkill.skillSelect = getSkill(3);
            if (playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.THAI_DUONG_HA_SAN:
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        Service.gI().chatJustForMe(master, this, "Bất ngờ chưa ông già");
                        return true;
                    }
                    return false;
                case Skill.TAI_TAO_NANG_LUONG:
                    if (this.effectSkill.isCharging && this.countTTNL < Util.nextInt(3, 5)) {
                        this.countTTNL++;
                        return true;
                    }
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)
                            && (this.nPoint.getCurrPercentHP() <= 20 || this.nPoint.getCurrPercentMP() <= 20)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        this.countTTNL = 0;
                        return true;
                    }
                    return false;
                case Skill.KAIOKEN:
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        mobAttack = this.findMobAttack();
                        if (mobAttack == null) {
                            return false;
                        }
                        int dis = Util.getDistance(this, mobAttack);
                        if (dis > ARANGE_ATT_SKILL1) {
                            PlayerService.gI().playerMove(this, mobAttack.location.x, mobAttack.location.y);
                        } else {
                            if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                                PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                            }
                        }
                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                        getSkill(1).lastTimeUseThisSkill = System.currentTimeMillis();
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean useSkill4() {
        try {
            this.playerSkill.skillSelect = getSkill(4);
            if (this.playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.BIEN_KHI:
                    if (!this.effectSkill.isMonkey && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                        return true;
                    }
                    return false;
                case Skill.KHIEN_NANG_LUONG:
                    if (!this.effectSkill.isShielding && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                        return true;
                    }
                    return false;
                case Skill.DE_TRUNG:
                    if (this.mobMe == null && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

//========================BETA SKILL5=====================
    private boolean useSkill5() {
        try {
            this.playerSkill.skillSelect = getSkill(5);
            if (this.playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            mobAttack = this.findMobAttack();
            if (mobAttack == null) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.THOI_MIEN:
                    if (!this.effectSkill.isThoiMien && SkillService.gI().canUseSkillWithCooldown(this)
                            && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                        return true;
                    }
                    return false;
                case Skill.DICH_CHUYEN_TUC_THOI:
                    if (!this.effectSkill.isBlindDCTT && SkillService.gI().canUseSkillWithCooldown(this)
                            && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                        Service.gI().chatJustForMe(master, this, "Hãy xem ta đây! Dịch chuyển tức thời...");
                        return true;
                    }
                    return false;
                case Skill.SOCOLA:
                    if (this.effectSkill.isSocola && SkillService.gI().canUseSkillWithCooldown(this)
                            && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    //====================================================
    private long lastTimeIncreasePoint;

    private void increasePoint() {
        if (this.nPoint != null && Util.canDoWithTime(lastTimeIncreasePoint, 1)) {
            if (Util.isTrue(5, 100)) {
                this.nPoint.increasePoint((byte) 2, (short) 1);
            } else {
                byte type = (byte) Util.nextInt(0, 2);
                short point = (short) Util.nextInt(10);
                this.nPoint.increasePoint(type, point);
            }
            lastTimeIncreasePoint = System.currentTimeMillis();
        }
    }

    public void followMaster() {
        if (this.isDie() || effectSkill.isHaveEffectSkill()) {
            return;
        }
        switch (this.status) {
            case ATTACK:
                if ((mobAttack != null && Util.getDistance(this, master) <= 1500)) {
                    break;
                }
            case FOLLOW:
            case PROTECT:
                followMaster(40);
                break;
        }
    }
    
    public byte getAura() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 13) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(5);
        if (!item.isNotNullItem()) {
            return -1;
        }
        if (item.template.id == 1121) {
            return 10;
        } else if (item.template.id == 1128) {

            return 15;
        } else if (item.template.id == 1125 || item.template.id == 2058 || item.template.id == 2055) {
            return 14;
        } else if (item.template.id == 2045) {
            return 20;
        } else {
            return -1;
        }

    }

    // hieu ung theo set
    public byte getEffFront() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 13) {
            return -1;
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
        if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null
                && optionLevelNhan != null
                && levelAo >= 8 && levelQuan >= 8 && levelGang >= 8 && levelGiay >= 8 && levelNhan >= 8) {
            return 8;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null
                && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 7 && levelQuan >= 7 && levelGang >= 7 && levelGiay >= 7 && levelNhan >= 7) {
            return 7;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null
                && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 6 && levelQuan >= 6 && levelGang >= 6 && levelGiay >= 6 && levelNhan >= 6) {
            return 6;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null
                && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 5 && levelQuan >= 5 && levelGang >= 5 && levelGiay >= 5 && levelNhan >= 5) {
            return 5;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null
                && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 4 && levelQuan >= 4 && levelGang >= 4 && levelGiay >= 4 && levelNhan >= 4) {
            return 4;
        } else {
            return -1;
        }
    }

    private void followMaster(int dis) {
        int mX = master.location.x;
        int mY = master.location.y;
        int disX = this.location.x - mX;
        if (Math.sqrt(Math.pow(mX - this.location.x, 2) + Math.pow(mY - this.location.y, 2)) >= dis) {
            if (disX < 0) {
                this.location.x = mX - Util.nextInt(0, dis);
            } else {
                this.location.x = mX + Util.nextInt(0, dis);
            }
            this.location.y = mY;
            PlayerService.gI().playerMove(this, this.location.x, this.location.y);
        }
    }
    public short getAvatar() {
        if (this.typePet == 1) {
            return 439;
        } else if (this.typePet == 2) {
            return 1690;
        } else if (this.typePet == 3) {
            return 237;
        } else if (this.typePet == 4) {
            return 950;
        } else if (this.typePet == 5) {
            return 1568;
        } else if (this.typePet == 6) {
            return 1780;
        }else if (this.typePet == 7) {
            return 1777;
        } else if (this.typePet == 12) {
            return 1327;
        } else if (this.typePet == 13) {
            return 550;
        } else if (this.typePet == 14) {
            return 2072;
        } else if (this.typePet == 15) {
            return 1297;
        } else if (this.typePet == 16) {
            return 1904;
        } else if (this.typePet == 17) {
            return 1336;
        } else {
            return PET_ID[3][this.gender];
        }
    }

    @Override
    public short getHead() {
        if (this.effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else if (this.effectSkill.isSocola) {
            return 412;
        } else if (this.effectSkill.isHoaSocola) {
            return 412;
        } else if (this.effectSkill.isHoaDa) {
            return 454;
        } else if (this.effectSkill.isHoaCarot) {
            return 406;
        } else if (this.typePet == 1) {
            return 439;
        } else if (this.typePet == 2) {
            return 1690;
        } else if (this.typePet == 3) {
            return 946;
        } else if (this.typePet == 4) {
            return 950;
        } else if (this.typePet == 5) {
            return 1568;
        } else if (this.typePet == 6) {
            return 1780;
        }else if (this.typePet == 7) {
            return 1777;
        } else if (this.typePet == 12) {
            return 1327;
        } else if (this.typePet == 13) {
            return 550;
        } else if (this.typePet == 14) {
            return 2072;
        } else if (this.typePet == 15) {
            return 1297;
        } else if (this.typePet == 16) {
            return 1904;
        } else if (this.typePet == 17) {
            return 1336;
        } else if (inventory.itemsBody.get(5).isNotNullItem()) {
            int part = inventory.itemsBody.get(5).template.head;
            if (part != -1) {
                return (short) part;
            }
        }
        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][0];
        } else {
            return PET_ID[3][this.gender];
        }
    }

    @Override
    public short getBody() {
        if (effectSkill.isMonkey) {
            return 193;
        } else if (effectSkill.isSocola) {
            return 413;
        } else if (effectSkill.isHoaSocola) {
            return 413;
        } else if (effectSkill.isHoaDa) {
            return 455;
        } else if (effectSkill.isHoaCarot) {
            return 407;
        } else if (this.typePet == 1 && !this.isTransform) {
            return 440;
        } else if (this.typePet == 2 && !this.isTransform) {
            return 1691;
        } else if (this.typePet == 3 && !this.isTransform) {
            return 947;
        } else if (this.typePet == 4 && !this.isTransform) {
            return 951;
        } else if (this.typePet == 5 && !this.isTransform) {
            return 1569;
        } else if (this.typePet == 6 && !this.isTransform) {
            return 1781;
        }else if (this.typePet == 7 && !this.isTransform) {
            return 1778;
        } else if (this.typePet == 12 && !this.isTransform) {
            return 1328;
        } else if (this.typePet == 13 && !this.isTransform) {
            return 551;
        } else if (this.typePet == 14 && !this.isTransform) {
            return 2073;
        } else if (this.typePet == 15 && !this.isTransform) {
            return 1298;
        } else if (this.typePet == 16 && !this.isTransform) {
            return 1904;
        } else if (this.typePet == 17 && !this.isTransform) {
            return 1337;
        } else if (inventory.itemsBody.get(5).isNotNullItem()) {
            int body = inventory.itemsBody.get(5).template.body;
            if (body != -1) {
                return (short) body;
            }
        }
        if (inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][1];
        } else {
            return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
        }
    }

    @Override
    public short getLeg() {
        if (effectSkill.isMonkey) {
            return 194;
        } else if (effectSkill.isSocola) {
            return 414;
        } else if (effectSkill.isHoaSocola) {
            return 414;
        } else if (effectSkill.isHoaDa) {
            return 456;
        } else if (effectSkill.isHoaCarot) {
            return 408;
        } else if (this.typePet == 1 && !this.isTransform) {
            return 441;
        } else if (this.typePet == 2 && !this.isTransform) {
            return 1692;
        } else if (this.typePet == 3 && !this.isTransform) {
            return 948;
        } else if (this.typePet == 4 && !this.isTransform) {
            return 952;
        } else if (this.typePet == 5 && !this.isTransform) {
            return 1570;
        }else if (this.typePet == 6 && !this.isTransform) {
            return 1782;
        }else if (this.typePet == 7 && !this.isTransform) {
            return 1779;
        } else if (this.typePet == 2 && !this.isTransform) {
            return 236;
        } else if (this.typePet == 3 && !this.isTransform) {
            return 191;
        } else if (this.typePet == 12 && !this.isTransform) {
            return 1329;
        } else if (this.typePet == 13 && !this.isTransform) {
            return 552;
        } else if (this.typePet == 14 && !this.isTransform) {
            return 2074;
        } else if (this.typePet == 15 && !this.isTransform) {
            return 1299;
        } else if (this.typePet == 16 && !this.isTransform) {
            return 1904;
        } else if (this.typePet == 17 && !this.isTransform) {
            return 1338;
        } else if (inventory.itemsBody.get(5).isNotNullItem()) {
            int leg = inventory.itemsBody.get(5).template.leg;
            if (leg != -1) {
                return (short) leg;
            }
        }
        if (inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }

        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][2];
        } else {
            return (short) (gender == ConstPlayer.NAMEC ? 60 : 58);
        }
    }

    private Mob findMobAttack() {
        int dis = ARANGE_CAN_ATTACK;
        Mob mobAtt = null;
        for (Mob mob : zone.mobs) {
            if (mob.isDie()) {
                continue;
            }
            int d = Util.getDistance(this, mob);
            if (d <= dis) {
                dis = d;
                mobAtt = mob;
            }
        }
        return mobAtt;
    }

    //Sức mạnh mở skill đệ
    private void updatePower() {
        if (this.playerSkill != null) {
            switch (this.playerSkill.getSizeSkill()) {
                case 1:
                    if (this.nPoint.power >= 150000000) {
                        openSkill2();
                    }
                    break;
                case 2:
                    if (this.nPoint.power >= 1500000000) {
                        openSkill3();
                    }
                    break;
                case 3:
                    if (this.nPoint.power >= 20000000000L) {
                        openSkill4();
                    }
                    break;

                case 4:
                    if (this.nPoint.power >= 60000000000L) {
                        openSkill5();
                    }
                    break;
            }
        }
    }
    
    public void openSkill1() {
        Skill skill = null;
        int tiLeKame = 33;
        int tiLeMasenko = 33;
        int tiLeAntomic = 34;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeKame) {
            skill = SkillUtil.createSkill(Skill.LIEN_HOAN, 1);
        } else if (rd <= tiLeKame + tiLeMasenko) {
            skill = SkillUtil.createSkill(Skill.LIEN_HOAN, 1);
        } else if (rd <= tiLeKame + tiLeMasenko + tiLeAntomic) {
            skill = SkillUtil.createSkill(Skill.LIEN_HOAN, 1);
        }
        skill.coolDown = 1000;
        this.playerSkill.skills.set(0, skill);
    }

    public void openSkill2() {
        Skill skill = null;
        int tiLeKame = 10;
        int tiLeMasenko = 50;
        int tiLeAntomic = 40;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeKame) {
            skill = SkillUtil.createSkill(Skill.KAMEJOKO, 1);
        } else if (rd <= tiLeKame + tiLeMasenko) {
            skill = SkillUtil.createSkill(Skill.MASENKO, 1);
        } else if (rd <= tiLeKame + tiLeMasenko + tiLeAntomic) {
            skill = SkillUtil.createSkill(Skill.ANTOMIC, 1);
        }
        skill.coolDown = 1000;
        this.playerSkill.skills.set(1, skill);
    }

    public void openSkill3() {
        Skill skill = null;
        int tiLeTDHS = 15;
        int tiLeTTNL = 35;
        int tiLeKOK = 50;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeTDHS) {
            skill = SkillUtil.createSkill(Skill.THAI_DUONG_HA_SAN, 1);
        } else if (rd <= tiLeTDHS + tiLeTTNL) {
            skill = SkillUtil.createSkill(Skill.TAI_TAO_NANG_LUONG, 1);
        } else if (rd <= tiLeTDHS + tiLeTTNL + tiLeKOK) {
            skill = SkillUtil.createSkill(Skill.KAIOKEN, 1);
        }
        this.playerSkill.skills.set(2, skill);
    }

    public void openSkill4() {
        Skill skill = null;
        int tiLeBienKhi = 10;
        int tiLeDeTrung = 30;
        int tiLeKNL = 60;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeBienKhi) {
            skill = SkillUtil.createSkill(Skill.BIEN_KHI, 1);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung) {
            skill = SkillUtil.createSkill(Skill.DE_TRUNG, 1);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung + tiLeKNL) {
            skill = SkillUtil.createSkill(Skill.KHIEN_NANG_LUONG, 1);
        }
        this.playerSkill.skills.set(3, skill);
    }

    public void openSkill5() {
        Skill skill = null;
        int tiLeThoiMien = 25; //khi
        int tiLeSoCoLa = 50; //detrung
        int tiLeDCTT = 25;//khienNl
        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeThoiMien) {
            skill = SkillUtil.createSkill(Skill.SOCOLA, 7);
        } else if (rd <= tiLeThoiMien + tiLeSoCoLa) {
            skill = SkillUtil.createSkill(Skill.THOI_MIEN, 7);
        } else if (rd <= tiLeThoiMien + tiLeSoCoLa + tiLeDCTT) {
            skill = SkillUtil.createSkill(Skill.DICH_CHUYEN_TUC_THOI, 7);
        }
        this.playerSkill.skills.set(4, skill);
    }

//    ========================================================
    private Skill getSkill(int indexSkill) {
        return this.playerSkill.skills.get(indexSkill - 1);
    }

    public void transform() {
        if (this.typePet == 1) {
            this.isTransform = !this.isTransform;
            Service.gI().Send_Caitrang(this);
            Service.gI().chat(this, "Ai Am Bư !! Bư..Bư..Bư..Ma..Nhân..Bư....");
        }
        if (this.typePet == 2) {
            this.isTransform = !this.isTransform;
            Service.gI().Send_Caitrang(this);
            Service.gI().chat(this, "Ta sẽ hấp thụ các ngươi");
        }
        if (this.typePet == 3) {
            this.isTransform = !this.isTransform;
            Service.gI().Send_Caitrang(this);
            Service.gI().chat(this, "Nhất Dương Chỉ");
        }
    }

    @Override
    public void dispose() {
        this.mobAttack = null;
        this.master = null;
        super.dispose();
    }
}
