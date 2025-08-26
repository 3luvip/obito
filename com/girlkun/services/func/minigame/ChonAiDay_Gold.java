package com.girlkun.services.func.minigame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;
import com.girlkun.services.ChatGlobalService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 *
 * @author louis
 */
public class ChonAiDay_Gold implements Runnable {

    public int goldNormar;
    public int goldVip;
    public long lastTimeEnd;
    public static final int TIME_CHONAIDAY = 300000;
    public List<Player> PlayersNormar = new ArrayList<>();
    public List<Player> PlayersVIP = new ArrayList<>();
    private static ChonAiDay_Gold instance;

    public static ChonAiDay_Gold gI() {
        if (instance == null) {
            instance = new ChonAiDay_Gold();
        }
        return instance;
    }

    public void addPlayerVIP(Player pl) {
        if (!PlayersVIP.contains(pl)) {
            PlayersVIP.add(pl);
        }
    }

    public void addPlayerNormar(Player pl) {
        if (!PlayersNormar.contains(pl)) {
            PlayersNormar.add(pl);
        }
    }

    public void removePlayerVIP(Player pl) {
        PlayersVIP.removeIf(player -> player == pl);
    }

    public void removePlayerNormar(Player pl) {
        PlayersNormar.removeIf(player -> player == pl);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if ((lastTimeEnd - System.currentTimeMillis()) / 1000 <= 0) {
                    List<Player> listN = new ArrayList<>();

                    PlayersNormar.stream().filter(p -> p != null && p.goldNormar != 0)
                            .sorted(Comparator.comparing(p -> Math.ceil(((double) p.goldNormar / goldNormar) * 100), Comparator.reverseOrder()))
                            .forEach(listN::add);

                    if (!listN.isEmpty()) {
                        int numWinners = Math.min(listN.size(), 5);
                        Player pl = listN.get(Util.nextInt(0, numWinners - 1));
                        if (pl != null) {
                            String chatMessage = pl.name + " đã chiến thắng Chọn ai đây giải thưởng";
                            int gold1 = goldNormar * 80 / 100;
                            Service.gI().sendThongBao(pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + Util.numberToMoney(gold1) + " vàng");
                            pl.inventory.gold += gold1;
                            Service.getInstance().sendMoney(pl);
                            ChatGlobalService.gI().chat(pl, chatMessage);
                        }
                    }

                    listN.clear();

                    PlayersVIP.stream().filter(p -> p != null && p.goldVIP != 0)
                            .sorted(Comparator.comparing(p -> Math.ceil(((double) p.goldVIP / goldVip) * 100), Comparator.reverseOrder()))
                            .forEach(listN::add);

                    if (!listN.isEmpty()) {
                        int numWinners = Math.min(listN.size(), 5);
                        Player pl = listN.get(Util.nextInt(0, numWinners - 1));
                        if (pl != null) {
                            String chatMessage = pl.name + " đã chiến thắng Chọn ai đây giải VIP";
                            int goldC = goldVip * 90 / 100;
                            Service.gI().sendThongBao(pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + Util.numberToMoney(goldC) + " vàng");
                            pl.inventory.gold += goldC;
                            Service.getInstance().sendMoney(pl);
                            ChatGlobalService.gI().chat(pl, chatMessage);
                        }
                    }

                    resetPlayers(PlayersNormar);
                    resetPlayers(PlayersVIP);
                    resetChonAiDay();
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void resetPlayers(List<Player> players) {
        players.forEach(player -> {
            if (player != null) {
                player.goldVIP = 0;
                player.goldNormar = 0;
            }
        });
        players.clear();
    }

    private void resetChonAiDay() {
        goldNormar = 0;
        goldVip = 0;
        lastTimeEnd = System.currentTimeMillis() + TIME_CHONAIDAY;
    }
}
