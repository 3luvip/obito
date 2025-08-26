package com.girlkun.services.func.minigame;

import java.lang.management.ManagementPermission;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;
import com.girlkun.server.Client;
import com.girlkun.server.Manager;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.func.MiniGame;
import com.girlkun.utils.Util;

/**
 *
 * @author louis
 */
public class ConSoMayMan {

    // SETTING GAME
    public long second = 50;
    public long currlast = System.currentTimeMillis();
    public long money = 450;
//    public long gold = 90_000_000; // vàng
    public long gold = 90; // thỏi vàng
    public long min = 0;
    public long max = 99;
    // KẾT QUẢ
    public long result = 0;
    public long result_next = Util.nextInt((int) min, (int) max);
    // GIA
    public long giaSo_Gem = 5;
//    public long giaSo_Gold = 1_000_000; // vàng
    public long giaSo_Gold = 1; // thỏi vàng
    // PLAYER NAME WIN
    public String result_name;

    public List<ConSoMayManData> players = new ArrayList<>();

    // chia player ra xem là player con số may mắn vàng hay ngọc //
    public List<Player> conSoMayManNgoc = new ArrayList<>();
    public List<Player> conSoMayManVang = new ArrayList<>();
    // end chia player //

    public void addPlayerVang(Player pl) { // thêm player chơi = vàng vô list
        if (!conSoMayManVang.contains(pl)) {
            conSoMayManVang.add(pl);
        }
    }

    public void addPlayerNgoc(Player pl) { // thêm player chơi = ngọc vô list
        if (!conSoMayManNgoc.contains(pl)) {
            conSoMayManNgoc.add(pl);
        }
    }

    public void removePlayeVang(Player pl) { // xóa player chơi = vàng vô list
        conSoMayManVang.removeIf(player -> player == pl);
    }

    public void removePlayerNgoc(Player pl) { // thêm player chơi = ngọc vô list
        conSoMayManNgoc.removeIf(player -> player == pl);
    }

    public List<Long> dataKQ_CSMM = new ArrayList<>();

    public Timer timer;
    public TimerTask task;
    protected boolean actived = false;

    public void close() {
        try {
            actived = false;
            task.cancel();
            timer.cancel();
            task = null;
            timer = null;
        } catch (Exception e) {
            task = null;
            timer = null;
        }
    }

    public void activate(int delay) {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                ConSoMayMan.this.update();
            }
        };
        timer.schedule(task, delay, delay);
    }

    private boolean isWithinWorkingHours() {
        LocalTime currentTime = LocalTime.now();
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(21, 59);

        return (currentTime.isAfter(startTime) && currentTime.isBefore(endTime));
    }

    public void update() {
        try {
            if (second > 0) {
                second--;
                if (second <= 0) {
                    currlast = System.currentTimeMillis();
                }
            }
            if (second <= 0 && Util.canDoWithTime(currlast, 10000)) {
                ResetGame((int) result_next, (int) 50);
                result_next = Util.nextInt((int) min, (int) max);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newData(Player player, int point, int VangorNgoc) { // ngọc // vang = 0; ngoc = 1;

        Item thoiVang = InventoryServiceNew.gI().findItemBagByTemp(player, (short) 457);
        if (VangorNgoc == 0) {
            if (thoiVang == null) {
                Service.getInstance().sendThongBao(player, "Bạn có thỏi vàng lồn đâu????");
                return;
            } else if (thoiVang.quantity < this.giaSo_Gold) {
                Service.getInstance().sendThongBao(player, "Bạn có đủ thỏi vàng lồn đâu????");
                return;
            }
//            if (player.inventory.gold < 1_000_000) { // cái này là dùng để dùng vàng nhé thằng dev sau :)))
//                Service.getInstance().sendThongBao(player, "Bạn không đủ 1 triệu vàng để thực hiện");
//                return;
//            }
        } else {
            if (player.inventory.gem < 50) {
                Service.getInstance().sendThongBao(player, "Bạn không đủ 50 ngọc để thực hiện");
                return;
            }
        }
        if (players.stream().filter(d -> d.id == player.id).toList().size() >= 10) {
            Service.gI().sendThongBao(player, "Bạn đã chọn 10 số rồi không thể chọn thêm");
            return;
        }
        players.stream().filter(d -> d.id == player.id).forEach((game)
                -> {
            if (game.id == player.id && game.point == point) {
                Service.gI().sendThongBao(player, "Số này bạn đã chọn rồi vui lòng chọn số khác.");
            }
        });
        if (VangorNgoc == 0) {
            if (!players.stream().anyMatch(game -> game.id == player.id && game.point == point)) {
                ConSoMayManData data = new ConSoMayManData();
                data.id = (int) player.id;
                data.conSoMayManNgoc = 1;
                data.point = point;
                players.add(data);
                player.inventory.gem -= (giaSo_Gem);
                Service.gI().sendMoney(player);
                Service.gI().showYourNumber(player, strNumber((int) player.id), null, null, 0);
            }
        }
        if (VangorNgoc == 1) {
            if (!players.stream().anyMatch(game -> game.id == player.id && game.point == point)) {
                ConSoMayManData data = new ConSoMayManData();
                data.id = (int) player.id;
                data.conSoMayManVang = 1;
                data.point = point;
                players.add(data);
                InventoryServiceNew.gI().subQuantityItemsBag(player, thoiVang, (int) giaSo_Gold);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn bị trừ " + thoiVang.quantity + " thỏi vàng vì cờ bạc");
//                player.inventory.gold -= (giaSo_Gold); // đoạn này trừ vàng (Nếu con số may mắn dùng vàng)
//                Service.gI().sendMoney(player);
                Service.gI().showYourNumber(player, strNumber((int) player.id), null, null, 0);
            }
        }
    }

    public void ramdom1SoLe(Player player, int VangorNgoc) { // ngọc
        Random random = new Random();

        int point = random.nextInt(50) * 2 + 1;  // Lấy số lẻ từ 1 đến 99

        Item thoiVang = InventoryServiceNew.gI().findItemBagByTemp(player, (short) 457);
        if (VangorNgoc == 0) {
            if (thoiVang == null) {
                Service.getInstance().sendThongBao(player, "Bạn có thỏi vàng lồn đâu????");
                return;
            } else if (thoiVang.quantity < this.giaSo_Gold) {
                Service.getInstance().sendThongBao(player, "Bạn có đủ thỏi vàng lồn đâu????");
                return;
            }
//            if (player.inventory.gold < 1_000_000) { // cái này là dùng để dùng vàng nhé thằng dev sau :)))
//                Service.getInstance().sendThongBao(player, "Bạn không đủ 1 triệu vàng để thực hiện");
//                return;
//            }
        } else {
            if (player.inventory.gem < 50) {
                Service.getInstance().sendThongBao(player, "Bạn không đủ 50 ngọc để thực hiện");
                return;
            }
        }

        if (players.stream().filter(d -> d.id == player.id).toList().size() >= 10) {
            Service.gI().sendThongBao(player, "Bạn đã chọn 10 số rồi không thể chọn thêm");
            return;
        }

        players.stream().filter(d -> d.id == player.id).forEach((game) -> {
            if (game.id == player.id && game.point == point) {
                // Nếu số đã chọn trùng, thực hiện đệ quy để chọn số mới
                this.ramdom1SoLe(player, VangorNgoc);
            }
        });

        if (VangorNgoc == 0) {
            if (!players.stream().anyMatch(game -> game.id == player.id && game.point == point)) {
                // Nếu số mới không trùng, thêm vào danh sách và cập nhật ngọc của người chơi
                ConSoMayManData data = new ConSoMayManData();
                data.id = (int) player.id;
                data.conSoMayManNgoc = 1;
                data.point = point;
                players.add(data);
                player.inventory.gem -= (giaSo_Gem);
                Service.gI().sendMoney(player);
                Service.gI().showYourNumber(player, strNumber((int) player.id), null, null, 0);
            }
        }

        if (VangorNgoc == 1) {
            if (!players.stream().anyMatch(game -> game.id == player.id && game.point == point)) {
                // Nếu số mới không trùng, thêm vào danh sách và cập nhật vàng của người chơi
                ConSoMayManData data = new ConSoMayManData();
                data.id = (int) player.id;
                data.conSoMayManVang = 1;
                data.point = point;
                players.add(data);
                InventoryServiceNew.gI().subQuantityItemsBag(player, thoiVang, (int) giaSo_Gold);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn bị trừ " + thoiVang.quantity + " thỏi vàng vì cờ bạc");
//                player.inventory.gold -= (giaSo_Gold);
//                Service.gI().sendMoney(player);
                Service.gI().showYourNumber(player, strNumber((int) player.id), null, null, 0);
            }
        }
    }

    public void ramdom1SoChan(Player player, int VangorNgoc) { // ngọc
        Random random = new Random();

        int point = random.nextInt(50) * 2;  // Lấy số lẻ từ 1 đến 99

        Item thoiVang = InventoryServiceNew.gI().findItemBagByTemp(player, (short) 457);
        if (VangorNgoc == 0) {
            if (thoiVang == null) {
                Service.getInstance().sendThongBao(player, "Bạn có thỏi vàng lồn đâu????");
                return;
            } else if (thoiVang.quantity < this.giaSo_Gold) {
                Service.getInstance().sendThongBao(player, "Bạn có đủ thỏi vàng lồn đâu????");
                return;
            }
//            if (player.inventory.gold < 1_000_000) { // cái này là dùng để dùng vàng nhé thằng dev sau :)))
//                Service.getInstance().sendThongBao(player, "Bạn không đủ 1 triệu vàng để thực hiện");
//                return;
//            }
        } else {
            if (player.inventory.gem < 50) {
                Service.getInstance().sendThongBao(player, "Bạn không đủ 50 ngọc để thực hiện");
                return;
            }
        }

        if (players.stream().filter(d -> d.id == player.id).toList().size() >= 10) {
            Service.gI().sendThongBao(player, "Bạn đã chọn 10 số rồi không thể chọn thêm");
            return;
        }

        players.stream().filter(d -> d.id == player.id).forEach((game) -> {
            if (game.id == player.id && game.point == point) {
                // Nếu số đã chọn trùng, thực hiện đệ quy để chọn số mới
                this.ramdom1SoChan(player, VangorNgoc);
            }
        });

        if (VangorNgoc == 0) {
            if (!players.stream().anyMatch(game -> game.id == player.id && game.point == point)) {
                // Nếu số mới không trùng, thêm vào danh sách và cập nhật ngọc của người chơi
                ConSoMayManData data = new ConSoMayManData();
                data.id = (int) player.id;
                data.conSoMayManNgoc = 1;
                data.point = point;
                players.add(data);
                player.inventory.gem -= (giaSo_Gem);
                Service.gI().sendMoney(player);
                Service.gI().showYourNumber(player, strNumber((int) player.id), null, null, 0);
            }
        }

        if (VangorNgoc == 1) {
            if (!players.stream().anyMatch(game -> game.id == player.id && game.point == point)) {
                // Nếu số mới không trùng, thêm vào danh sách và cập nhật vàng của người chơi
                ConSoMayManData data = new ConSoMayManData();
                data.id = (int) player.id;
                data.conSoMayManVang = 1;
                data.point = point;
                players.add(data);
                InventoryServiceNew.gI().subQuantityItemsBag(player, thoiVang, (int) giaSo_Gold);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn bị trừ " + thoiVang.quantity + " thỏi vàng vì cờ bạc");
//                player.inventory.gold -= (giaSo_Gold);
//                Service.gI().sendMoney(player);
                Service.gI().showYourNumber(player, strNumber((int) player.id), null, null, 0);
            }
        }
    }

    public String strNumber(int id) {
        String number = "";
        List<ConSoMayManData> pl = players.stream().filter(d -> d.id == id).toList();
        for (int i = 0; i < pl.size(); i++) {
            ConSoMayManData d = pl.get(i);
            if (d.id == id) {
                number += d.point + (i >= pl.size() - 1 ? "" : ",");
            }
        }
        return number;
    }

    public String strFinish(int id) {
        String finish = "Con số chúng thưởng là " + result + " chúc bạn may mắn lần sau";
        for (ConSoMayManData g : players) {
            if (id == g.id && g.point == result && g.conSoMayManNgoc == 1) {
                Player player = Client.gI().getPlayer(g.id);
                finish = "Chúc mừng " + (player == null ? "NULL" : player.name) + " đã thắng " + MiniGame.gI().MiniGame_S1.money + " ngọc với con số may mắn " + result;
                // trả thưởng cho player trúng thưởng //
                player.inventory.ruby += MiniGame.gI().MiniGame_S1.money;
                Service.getInstance().sendMoney(player);
                // end trả thưởng //
                break;
            }
            if (id == g.id && g.point == result && g.conSoMayManVang == 1) {
                Player player = Client.gI().getPlayer(g.id);
                Item thoiVang = ItemService.gI().createNewItem((short) 457, (int) MiniGame.gI().MiniGame_S1.gold);
                finish = "Chúc mừng " + (player == null ? "NULL" : player.name) + " đã thắng " + Util.mumberToLouis(MiniGame.gI().MiniGame_S1.gold) + " thỏi vàng với con số may mắn " + result;
                // trả thưởng cho player trúng thưởng //
//                InventoryServiceNew.gI().addItemBag(player, thoiVang, 99999);
                InventoryServiceNew.gI().sendItemBags(player);
                // end trả thưởng //
                break;
            }
        }
        return finish;
    }

    public void ResetGame(int result, int second) {
        this.result = result;
        dataKQ_CSMM.add(this.result);
        this.second = second;
        this.result_name = "";
        players.stream().filter(g -> g.point == result).forEach((g) -> {
            Player player = Client.gI().getPlayer(g.id);
            result_name += (player != null ? player.name : "") + ",";
        });
        if (result_name.length() > 0) {
            result_name = result_name != null ? result_name.substring(0, result_name.length() - 1) : null;
        }
        players.forEach((g)
                -> {
            Player player = Client.gI().getPlayer(g.id);
            if (player != null) {
                Service.gI().showYourNumber(player, "", result + "", strFinish(g.id), 1);
            }
        });
        // Trả thưởng...
        money = 450;
        gold = 90;
        players.clear();
    }
}
