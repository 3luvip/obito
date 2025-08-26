package com.girlkun.utils;

import com.girlkun.jdbc.daos.GodGK;
import com.girlkun.models.Template;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import java.text.NumberFormat;
import java.util.*;

import com.girlkun.models.matches.TOP;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.npc.Npc;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.server.Client;
import com.girlkun.server.Manager;
import com.girlkun.services.ItemService;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang.ArrayUtils;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;
public class Util {

    private static final Random rand;
    private static final SimpleDateFormat dateFormat;
    private static final SimpleDateFormat dateFormatWeek;
    public static final SimpleDateFormat dateFormatDay;
    private static final Locale locale = new Locale("vi", "VN");
    private static final NumberFormat num = NumberFormat.getInstance(locale);

    static {
        rand = new Random();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatWeek = new SimpleDateFormat("yyyy-MM-ww");
        dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
    }

    public static String strSQL(final String str) {
        return str.replaceAll("['\"\\\\%]", "\\\\$0");
    }

    public static String mumberToLouis(long number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(number).replace(',', '.');
    }
    public static String formatNumber(int number) {
        NumberFormat nf = NumberFormat.getInstance();
        return nf.format(number);
    }
    public static String getFormatNumber(float hp) {
        // obito
        return Util.num.format(hp);
    }
    public static String tinhgio(long ms) {
        ms = ms - System.currentTimeMillis();
        if (ms < 0) {
            ms = 0;
        }
        long mm;
        long ss;
        long hh;
        ss = ms / 1000;
        mm = (long) (ss / 60);
        ss = ss % 60;
        hh = (long) (mm / 60);
        mm = mm % 60;
        String ssString = String.valueOf(ss);
        String mmString = String.valueOf(mm);
        String hhString = String.valueOf(hh);
        String time;
        if (hh != 0) {
            time = hhString + " giờ, " + mmString + " phút, " + ssString + " giây";
        } else if (mm != 0) {
            time = mmString + " phút, " + ssString + "giây";
        } else if (ss != 0) {
            time = ssString + " giây";
        } else {
            time = ssString + " giây";
        }
        return time;
    }

//    public static byte[] randomImg() {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte[] array = null;
//        try {
//            BufferedImage img = new BufferedImage(nextInt(80, 250), nextInt(80, 250), BufferedImage.TYPE_INT_ARGB);
//            IntStream.range(0, img.getWidth())
//                    .forEach(x -> IntStream.range(0, img.getHeight())
//                    .forEach(y -> {
//                        int a = ThreadLocalRandom.current().nextInt(256);
//                        int r = ThreadLocalRandom.current().nextInt(256);
//                        int g = ThreadLocalRandom.current().nextInt(256);
//                        int b = ThreadLocalRandom.current().nextInt(256);
//                        int p = (a << 24) | (r << 16) | (g << 8) | b;
//                        img.setRGB(x, y, p);
//                    }));
//            ImageIO.write(img, "png", baos);
//            array = baos.toByteArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return array;
//    }
    


    private static final String AES_ALGORITHM = "AES";
    private static final int AES_KEY_SIZE = 128;

    // Phương thức tạo hình ảnh ngẫu nhiên
    public static BufferedImage createRandomImage(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Đặt thuộc tính vẽ
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Vẽ hình ảnh ngẫu nhiên
        g2d.setColor(Util.getRandomColor());
        g2d.fillRect(0, 0, width, height);

        g2d.dispose();
        return img;
    }

    // Phương thức tạo màu ngẫu nhiên
    private static Color getRandomColor() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return new Color(red, green, blue);
    }
// Phương thức tạo khóa AES
    public static SecretKey generateAESKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGen.init(128); // Kích thước khóa 128-bit
            return keyGen.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Phương thức mã hóa hình ảnh bằng AES
    public static byte[] encryptImage(String imagePath, SecretKey secretKey) {
        try {
            // Đọc hình ảnh từ đường dẫn
            BufferedImage originalImage = ImageIO.read(new File(imagePath));

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Chuyển đổi BufferedImage thành mảng byte
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);
            byte[] imageData = baos.toByteArray();

            // Mã hóa dữ liệu hình ảnh
            byte[] encryptedData = cipher.doFinal(imageData);

            return encryptedData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Phương thức giải mã hình ảnh bằng AES (nếu cần thiết)
    public static BufferedImage decryptImage(byte[] encryptedData, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Giải mã dữ liệu
            byte[] decryptedData = cipher.doFinal(encryptedData);

            // Chuyển đổi mảng byte thành BufferedImage
            ByteArrayInputStream bais = new ByteArrayInputStream(decryptedData);
            return ImageIO.read(bais);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    


    // Phương thức mã hóa hình ảnh bằng AES
    public static byte[] encryptImage(BufferedImage originalImage, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Chuyển đổi BufferedImage thành mảng byte
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);
            byte[] imageData = baos.toByteArray();

            // Mã hóa dữ liệu hình ảnh
            byte[] encryptedData = cipher.doFinal(imageData);

            return encryptedData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Phương thức giải mã hình ảnh bằng AES (nếu cần thiết)
//    public static BufferedImage decryptImage(byte[] encryptedData, SecretKey secretKey) {
//        try {
//            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
//            cipher.init(Cipher.DECRYPT_MODE, secretKey);
//
//            // Giải mã dữ liệu
//            byte[] decryptedData = cipher.doFinal(encryptedData);
//
//            // Chuyển đổi mảng byte thành BufferedImage
//            return ImageIO.read(new ByteArrayInputStream(decryptedData));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


    
    public static int createIdDuongTank(int idPlayer) {
        return -idPlayer - 100_000_000;
    }

    public static synchronized Date getDate(final String dateString) {
        if (dateString == null || dateString.equals("")) {
            return new Date();
        }
        try {
            return Util.dateFormat.parse(dateString);
        } catch (ParseException e) {
//            e.printStackTrace(threading.Server.LOG_FILE);
            return new Date();
        }
    }

    public static Item petrandom(int tempId) {
        Item gapthuong = ItemService.gI().createNewItem((short) tempId);
        if (Util.isTrue(90, 100)) {
            gapthuong.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 10)));
            gapthuong.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 10)));
            gapthuong.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
            if (Util.isTrue(30, 100)) {
                gapthuong.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 9)));
            }
        }
        return gapthuong;
    }

    public static Item petccrandom(int tempId) {
        Item gapcc = ItemService.gI().createNewItem((short) tempId);
        if (Util.isTrue(90, 100)) {
            gapcc.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 20)));
            gapcc.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 20)));
            gapcc.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 20)));
            if (Util.isTrue(30, 100)) {
                gapcc.itemOptions.add(new Item.ItemOption(93, Util.nextInt(5, 12)));
            }
        }
        return gapcc;
    }

    public static Item petviprandom(int tempId) {
        Item gapvip = ItemService.gI().createNewItem((short) tempId);
        if (Util.isTrue(90, 100)) {
            gapvip.itemOptions.add(new Item.ItemOption(50, Util.nextInt(15, 25)));
            gapvip.itemOptions.add(new Item.ItemOption(103, Util.nextInt(15, 25)));
            gapvip.itemOptions.add(new Item.ItemOption(77, Util.nextInt(15, 25)));
            if (Util.isTrue(30, 100)) {
                gapvip.itemOptions.add(new Item.ItemOption(93, Util.nextInt(7, 15)));
            }
        }
        return gapvip;
    }

    public static ItemMap bikip(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        return it;
    }

    public static ItemMap useeItem(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(729);
        if (tanjiro.contains(tempId)) {
            it.options.add(new Item.ItemOption(77, highlightsItem(it.itemTemplate.gender == 3, new Random().nextInt(40) + 1)));
            it.options.add(new Item.ItemOption(103, highlightsItem(it.itemTemplate.gender == 3, new Random().nextInt(40) + 1)));
            it.options.add(new Item.ItemOption(50, highlightsItem(it.itemTemplate.gender == 3, new Random().nextInt(40) + 1)));
            it.options.add(new Item.ItemOption(5, highlightsItem(it.itemTemplate.gender == 3, new Random().nextInt(10) + 1)));
        }
        it.options.add(new Item.ItemOption(209, 1)); // đồ rơi từ boss
        it.options.add(new Item.ItemOption(93, 1));
        it.options.add(new Item.ItemOption(30, 1));// ko thể gd

        return it;
    }

    public static ItemMap khongthegiaodich(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        it.options.add(new Item.ItemOption(30, 1));
        return it;
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (Exception e) {
        }
        return "";
    }

    public static int createIdBossLV(long idPlayer) {
        return (int) (-idPlayer - 500_000_000);
    }

    public static int createIdBossClone(int idPlayer) {
        return -idPlayer - 100_000_000;
    }

    public static boolean contains(String[] arr, String key) {
        return Arrays.toString(arr).contains(key);
    }

    public static String numberToMoney(long power) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat num = NumberFormat.getInstance(locale);
        num.setMaximumFractionDigits(1);
        if (power >= 1000000000) {
            return num.format((double) power / 1000000000) + " Tỷ";
        } else if (power >= 1000000) {
            return num.format((double) power / 1000000) + " Tr";
        } else if (power >= 1000) {
            return num.format((double) power / 1000) + " K";
        } else {
            return num.format(power);
        }
    }

    public static String powerToString(double power) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat num = NumberFormat.getInstance(locale);
        num.setMaximumFractionDigits(1);
        if (power >= 1000000000) {
            return num.format((double) power / 1000000000) + " Tỷ";
        } else if (power >= 1000000) {
            return num.format((double) power / 1000000) + " Tr";
        } else if (power >= 1000) {
            return num.format((double) power / 1000) + " K";
        } else {
            return num.format(power);
        }
    }

    public static long Tamkjllnext(double from, double to) {
        //Telegram: @Tamkjll
        return (long) (from + rand.nextInt((int) (to - from + 1)));
    }

    public static int TamkjllGH(double a) {
        //Telegram: @Tamkjll
        if (a > 2123456789) {
            a = 2123456789;
        }
        return (int) a;
    }

    public static String format(double power) {
        return num.format(power);
    }

    public static int getDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static int getDistance(Player pl1, Player pl2) {
        return getDistance(pl1.location.x, pl1.location.y, pl2.location.x, pl2.location.y);
    }

    public static int getDistance(Player pl, Npc npc) {
        return getDistance(pl.location.x, pl.location.y, npc.cx, npc.cy);
    }

    public static int getDistance(Player pl, Mob mob) {
        return getDistance(pl.location.x, pl.location.y, mob.location.x, mob.location.y);
    }

    public static int getDistance(Mob mob1, Mob mob2) {
        return getDistance(mob1.location.x, mob1.location.y, mob2.location.x, mob2.location.y);
    }

    public static int nextInt(int from, int to) {
        return from + rand.nextInt(to - from + 1);
    }

    public static int nextInt(int max) {
        return rand.nextInt(max);
    }

    public static int nextInt(int[] percen) {
        int next = nextInt(1000), i;
        for (i = 0; i < percen.length; i++) {
            if (next < percen[i]) {
                return i;
            }
            next -= percen[i];
        }
        return i;
    }

    public static int getOne(int n1, int n2) {
        return rand.nextInt() % 2 == 0 ? n1 : n2;
    }

    public static int currentTimeSec() {
        return (int) System.currentTimeMillis() / 1000;
    }

    public static String replace(String text, String regex, String replacement) {
        return text.replace(regex, replacement);
    }

    public static boolean isTrue(int ratio, int typeRatio) {
        int num = Util.nextInt(typeRatio);
        if (num < ratio) {
            return true;
        }
        return false;
    }

    public static boolean isTrue(float ratio, int typeRatio) {
        if (ratio < 1) {
            ratio *= 10;
            typeRatio *= 10;
        }
        int num = Util.nextInt(typeRatio);
        if (num < ratio) {
            return true;
        }
        return false;
    }

    public static boolean haveSpecialCharacter(String text) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        boolean b = m.find();
        return b || text.contains(" ");
    }

    public static boolean canDoWithTime(long lastTime, long miniTimeTarget) {
        return System.currentTimeMillis() - lastTime > miniTimeTarget;
    }

    private static final char[] SOURCE_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É',
        'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
        'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
        'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
        'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
        'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
        'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
        'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
        'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
        'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
        'ữ', 'Ự', 'ự',};

    private static final char[] DESTINATION_CHARACTERS = {'A', 'A', 'A', 'A', 'E',
        'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
        'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
        'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
        'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
        'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
        'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
        'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
        'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
        'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
        'U', 'u', 'U', 'u',};

    public static char removeAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    public static String removeAccent(String str) {
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }

    public static String generateRandomText(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                + "lmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static Object[] addArray(Object[]... arrays) {
        if (arrays == null || arrays.length == 0) {
            return null;
        }
        if (arrays.length == 1) {
            return arrays[0];
        }
        Object[] arr0 = arrays[0];
        for (int i = 1; i < arrays.length; i++) {
            arr0 = ArrayUtils.addAll(arr0, arrays[i]);
        }
        return arr0;
    }

    public static ItemMap manhTS(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        return new ItemMap(zone, tempId, quantity, x, y, playerId);
    }

    public static ItemMap ratiDTL(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(tempId)) {
            it.options.add(new Item.ItemOption(47, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(501) + 1300)));
        }
        if (quan.contains(tempId)) {
            it.options.add(new Item.ItemOption(22, highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(11) + 45)));
        }
        if (gang.contains(tempId)) {
            it.options.add(new Item.ItemOption(0, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(1001) + 3500)));
        }
        if (giay.contains(tempId)) {
            it.options.add(new Item.ItemOption(23, highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(11) + 35)));
        }
        if (ntl == tempId) {
            it.options.add(new Item.ItemOption(14, new Random().nextInt(2) + 15));
        }
        it.options.add(new Item.ItemOption(209, 1)); // đồ rơi từ boss
        it.options.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ
        it.options.add(new Item.ItemOption(30, 1)); // ko thể gd
        if (Util.isTrue(90, 100)) {// tỉ lệ ra spl
            it.options.add(new Item.ItemOption(107, new Random().nextInt(3) + 1));
        } else if (Util.isTrue(4, 100)) {
            it.options.add(new Item.ItemOption(107, new Random().nextInt(3) + 5));
        } else {
            it.options.add(new Item.ItemOption(107, new Random().nextInt(5) + 1));
        }
        return it;
    }

    public static ItemMap RaitiDoc12(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> ao = Arrays.asList(233, 237, 241);
        List<Integer> quan = Arrays.asList(245, 249, 253);
        List<Integer> gang = Arrays.asList(257, 261, 265);
        List<Integer> giay = Arrays.asList(269, 273, 277);
        int rd12 = 281;
        if (ao.contains(tempId)) {
            it.options.add(new Item.ItemOption(47, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(121) + 350)));//giáp 350-470
        }
        if (quan.contains(tempId)) {
            it.options.add(new Item.ItemOption(22, highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(5) + 20)));//hp 20-24k
        }
        if (gang.contains(tempId)) {
            it.options.add(new Item.ItemOption(0, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(51) + 2200)));//2200-2250
        }
        if (giay.contains(tempId)) {
            it.options.add(new Item.ItemOption(23, highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(4) + 20)));//20-23k ki
        }
        if (rd12 == tempId) {
            it.options.add(new Item.ItemOption(14, new Random().nextInt(3) + 10));//10-12cm
        }
        it.options.add(new Item.ItemOption(209, 1));//đồ rơi từ boss
        if (Util.isTrue(70, 100)) {// tỉ lệ ra spl 1-3 sao 70%
            it.options.add(new Item.ItemOption(107, new Random().nextInt(1) + 3));
        } else if (Util.isTrue(4, 100)) {// tỉ lệ ra spl 5-7 sao 4%
            it.options.add(new Item.ItemOption(107, new Random().nextInt(3) + 5));
        } else {// tỉ lệ ra spl 1-5 sao 6%
            it.options.add(new Item.ItemOption(107, new Random().nextInt(2) + 3));
        }
        return it;
    }

    public static Item ratiItemTL(int tempId) {
        Item it = ItemService.gI().createItemSetKichHoat(tempId, 1);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(47, highlightsItem(it.template.gender == 2, new Random().nextInt(501) + 1000)));
        }
        if (quan.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(22, highlightsItem(it.template.gender == 0, new Random().nextInt(11) + 45)));
        }
        if (gang.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(0, highlightsItem(it.template.gender == 2, new Random().nextInt(1001) + 3500)));
        }
        if (giay.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(23, highlightsItem(it.template.gender == 1, new Random().nextInt(11) + 35)));
        }
        if (ntl == tempId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(3) + 15));
        }
        it.itemOptions.add(new Item.ItemOption(21, 15));
        return it;
    }

    public static ItemMap useItem(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1087, 1088, 1091, 1090);
        if (tanjiro.contains(tempId)) {
            it.options.add(new Item.ItemOption(77, highlightsItem(it.itemTemplate.gender == 3, new Random().nextInt(30) + 1)));
            it.options.add(new Item.ItemOption(103, highlightsItem(it.itemTemplate.gender == 3, new Random().nextInt(30) + 1)));
            it.options.add(new Item.ItemOption(50, highlightsItem(it.itemTemplate.gender == 3, new Random().nextInt(30) + 1)));
        }
        it.options.add(new Item.ItemOption(209, 1)); // đồ rơi từ boss
        it.options.add(new Item.ItemOption(30, 1)); // ko thể gd

        return it;
    }

    public static ItemMap ratiItem(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(tempId)) {
            it.options.add(new Item.ItemOption(47, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(501) + 1000)));
        }
        if (quan.contains(tempId)) {
            it.options.add(new Item.ItemOption(22, highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(11) + 45)));
        }
        if (gang.contains(tempId)) {
            it.options.add(new Item.ItemOption(0, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(1001) + 3500)));
        }
        if (giay.contains(tempId)) {
            it.options.add(new Item.ItemOption(23, highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(11) + 35)));
        }
        if (ntl == tempId) {
            it.options.add(new Item.ItemOption(14, new Random().nextInt(3) + 15));
        }
        it.options.add(new Item.ItemOption(209, 1));
        it.options.add(new Item.ItemOption(21, 15));
        return it;
    }

    public static int highlightsItem(boolean highlights, int value) {
        double highlightsNumber = 1.1;
        return highlights ? (int) (value * highlightsNumber) : value;
    }
    public static boolean isTimeHiru(){
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        return (hour == Mob.TIME_START_HIRU);
    }

    public static Item sendDo(int itemId, int sql, List<Item.ItemOption> ios) {
//        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
//        InventoryServiceNew.gI().sendItemBags(player);
        Item item = ItemService.gI().createNewItem((short) itemId);
        item.itemOptions.addAll(ios);
        item.itemOptions.add(new Item.ItemOption(107, sql));
        return item;
    }

    public static boolean checkDo(Item.ItemOption itemOption) {
        switch (itemOption.optionTemplate.id) {
            case 0:// tấn công
                if (itemOption.param > 12000) {
                    return false;
                }
                break;
            case 14:// chí mạng
                if (itemOption.param > 30) {
                    return false;
                }
                break;
            case 107:// spl
            case 102:// spl
                if (itemOption.param > 8) {
                    return false;
                }
                break;
            case 77:
            case 103:
            case 95:
            case 96:
                if (itemOption.param > 41) {
                    return false;
                }
                break;
            case 50:// sd 3%
                if (itemOption.param > 24) {
                    return false;
                }
                break;
            case 6:// hp
            case 7:// ki
                if (itemOption.param > 120000) {
                    return false;
                }
                break;
            case 47:// giáp
                if (itemOption.param > 3500) {
                    return false;
                }
                break;
        }
        return true;
    }

    public static void useCheckDo(Player player, Item item, String position) {
        try {
            if (item.template != null) {
                if (item.template.id >= 381 && item.template.id <= 385) {
                    return;
                }
                if (item.template.id >= 66 && item.template.id <= 135) {
                    return;
                }
                if (item.template.id >= 474 && item.template.id <= 515) {
                    return;
                }
                item.itemOptions.forEach(itemOption -> {
                    if (!Util.checkDo(itemOption)) {
                        Logger.error(player.name + "-" + item.template.name + "-" + position + "\n");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String phanthuong(int i) {
        switch (i) {
            case 1:
                return "5tr";
            case 2:
                return "3tr";
            case 3:
                return "1tr";
            default:
                return "100k";
        }
    }

    public static int randomBossId() {
        int bossId = Util.nextInt(-10000, - 300);
        while (BossManager.gI().getBossById(bossId) != null) {
            bossId = Util.nextInt(-10000, - 300);
        }
        return bossId;
    }

    public static long tinhLuyThua(int coSo, int soMu) {
        long ketQua = 1;

        for (int i = 0; i < soMu; i++) {
            ketQua *= coSo;
        }
        return ketQua;
    }

    public static void checkPlayer(Player player) {
        new Thread(() -> {
            List<Player> list = Client.gI().getPlayers().stream().filter(p -> !p.isPet && !p.isPet3 && !p.isNewPet && p.getSession().userId == player.getSession().userId).collect(Collectors.toList());
            if (list.size() > 1) {
                list.forEach(pp -> Client.gI().kickSession(pp.getSession()));
                list.clear();
            }
        }).start();
    }

    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }

    public static int[] pickNRandInArr(int[] array, int n) {
        List<Integer> list = new ArrayList<Integer>(array.length);
        for (int i : array) {
            list.add(i);
        }
        Collections.shuffle(list);
        int[] answer = new int[n];
        for (int i = 0; i < n; i++) {
            answer[i] = list.get(i);
        }
        Arrays.sort(answer);
        return answer;
    }

    public static int nextIntDhvt(int from, int to) {
        return from + rand.nextInt(to - from);
    }

}
