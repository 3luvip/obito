package com.girlkun.service;

public class Setting {

    // ------------------------- server setting -------------------------
    public static final int PORT = 14445;
    public static final int MAX_IP = 10;
    public static final int PLAYER_ID = 0;

    public static boolean IN_LOG = true;

    public static String LIST_SERVER = "KhanhDTK1:192.168.0.105:14445:0,0,0";
    public static String SERVER_NAME = "KhanhDTK2:192.168.0.105:14445:0,0,0"; // ch

    // -------------------------- sql setting ----------------------------
    public static final String DB_HOST = "127.0.0.1";
    public static final String DB_DATE = "nro_data";
    public static final String DB_NICK = "nro_acc";
    public static final String DB_USER = "root";
    public static final String DB_PASS = "";

    public static final int MAX_PLAYER = 50;

    // SOURCE
    public static final int VERSION_IMAGE_SOURCE = 5714091;
    public static byte vsData = 12;
    public static byte vsMap = 16;
    public static byte vsSkill = 6;
    public static byte vsItem = 11;

    // ------------------------- role -------------------------
    public static final byte ROLE_ADMIN_VIP = 100;
    public static final byte ROLE_ADMIN = 99;
    public static final byte ROLE_BAN_ACC = 1;
    public static final byte ROLE_BAN_CTG = 2;
    public static final byte ROLE_BAN_CHAT = 3;
    public static final byte ROLE_BAN_ALL_CHAT = 4;

    public static final int DONATE_EXP = 10; // exp sv hiện tại là x10( tnsm khi up ấy )
    public static final int GOLD_ROUND = 100000000;
    public static final int GOLD_DHVT23 = 50000;
    public static final int DAY_NEW = 30;

    public static final int MAX_STAMINA = 10000;
    public static final int MAX_STAMINA_FOR_PET = 1100;

    public static final int GOLD_OPEN_SPEACIAL = 10000000;
    public static final int GEM_OPEN_SPEACIAL = 100;

    public static final int DELAY_PLAYER = 1000;
    public static final int DELAY_PET = 500;

    public static final int DELAY_UPDATE_DATA_BASE = 600000;

    public static final int DELAY_UPDATE_MOB = 250;
    public static final int DELAY_MOB_ATTACK = 1000;

    // Player
    public static final int TOC_DO_CHAY = 9;
    public static final int SIZE_BODY_PLAYER = 11;
    public static final int SIZE_BODY_PET = 9;
    public static final int ITEM_START = 8;
    public static final int ITEM_LEVEL = 8;

    public static final int MAX_SMALL = 32767;
    public static final int GOLD_SELL_TV_1 = 500000000;
    public static final int GOLD_SELL_TV_2 = 50000000;
    public static final long GOLD_SELL_TV_3 = 2500000000L;
    // cong chi so
    public static final int HP_FROM_1000_TN = 20;
    public static final int MP_FROM_1000_TN = 20;
    public static final int SD_FROM_1000_TN = 1;
    public static final int EXP_FOR_ONE_ADD = 100;

    public static final int GOLD_OPEN_GHSM = 500000000;
    public static final long[] LIMIT_SUC_MANH = {
            17999999999L,
            19999999999L,
            24999999999L,
            29999999999L,
            34999999999L,
            39999999999L,
            50010000000L,
            60010000000L,
            80010000000L,
            120010000000L,
            200010000000L
    };

    // rong than trái đất
    public static final int TIME_RECALL_DRAGON = 120000;
    public static final int TIME_DRAGON_WAIT = 120000;
    public static final int COUNT_SUMMON_DRAGON = 50000;
    // rong than băng
    public static final int TIME_RECALL_DRAGON_BANG = 300000;
    public static final int TIME_DRAGON_WAIT_BANG = 300000;
    // Chat
    public static boolean LOG_CHAT_GLOBAL = false;
    // kí gửi
    public static int ITEM_ID = 1000;
    public static final int GOLD_MIN_SELL_KI_GUI = 1;
    public static final int GOLD_MAX_SELL_KI_GUI = 1000;
    public static final int NGOC_MIN_SELL_KI_GUI = 2;
    public static final int NGOC_MAX_SELL_KI_GUI = 20000;
    // Mini game
    public static final byte TIME_START_GAME = 8;
    public static final byte TIME_END_GAME = 22;
    public static final byte TIME_MINUTES_GAME = 1;
    // nrsd
    public static final byte TIME_START = 20;
    public static final byte TIME_END = 21;
    public static final byte TIME_PICK = 30;
    public static final int TIME_WIN_NRSD = 300000;
    // hirudegarn
    // public static final byte TIME_START_HIRU_1 = 22;
    // public static final byte TIME_START_HIRU_2 = 22;
    // osin mabu
    public static final byte TIME_START_OSIN_1 = 12;
    public static final byte TIME_START_OSIN_2 = 18;
    public static boolean START_OSIN = false;

    // open super tỉ lệ
    public static final byte TIME_START_SUPER = 17;
    // phụ bản
    public static final int ZONE_PHU_BAN = 10;
    // ho tro nv
    public static boolean HO_TRO_TDST = true;

    // Event
    public static boolean EVENT_SPEACIAL = false;
    public static boolean EVENT_HALLOWEEN = false;
    public static boolean EVENT_TRUNG_THU = false;
    public static boolean EVENT_GIANG_SINH = false;

    // sét true = chạy sự kiện == false ko chạy sự kiện
    // Event 2024
}
