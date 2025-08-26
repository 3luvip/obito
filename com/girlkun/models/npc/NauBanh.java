package com.girlkun.models.npc;

import com.girlkun.models.player.Player;
import com.girlkun.server.Client;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DEV By TeEt
 */
public class NauBanh implements Runnable {

    // sl defaut
    public int Count = 1;
    public int plBanhChung;
    public int plBanhTet;
    public float Nuoc;
    public int Cui;
    // đủ củi - nước > nấu
    public long ThoiGianCho;
    public long ThoiGianNau;
    // sau khi chở > nấu
    public boolean ChoXong = false;
    public boolean NauXong = false;
    // sau khi nấu chờ lấy > ko lấy qua turn
    public long ThoiGianChoLayBanh;
    // player add list
    public List<Player> ListPlNauBanh = new ArrayList<>();
    // bảo trì
    public boolean baotri = false;
    // get insrance
    private static NauBanh instance;

    public static NauBanh gI() {
        if (instance == null) {
            instance = new NauBanh();
        }
        return instance;
    }

    public void addListPlNauBanh(Player pl) {
        if (!ListPlNauBanh.equals(pl)) {
            ListPlNauBanh.add(pl);
        }
    }

    public void removeListPlNauBanh(Player pl) {
        ListPlNauBanh.remove(pl);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (ThoiGianCho != 0 && Util.canDoWithTime(ThoiGianCho, 1000)) {
                    ThoiGianCho = 0;
                }
                if (ThoiGianNau != 0 && Util.canDoWithTime(ThoiGianNau, 1000)) {
                    ThoiGianNau = 0;
                }
                if (ThoiGianChoLayBanh != 0 && Util.canDoWithTime(ThoiGianChoLayBanh, 1000)) {
                    ThoiGianChoLayBanh = 0;
                }
                if (Cui >= 1 && Nuoc >= 1) {
                    if (ChoXong == false && ((ThoiGianCho - System.currentTimeMillis()) / 1000) == 0) {
                        ChoXong = true;
                        ThoiGianNau = System.currentTimeMillis() + (1000 * 15);
                    } else if (NauXong == false && ChoXong == true && ((ThoiGianNau - System.currentTimeMillis()) / 1000) == 0) {
                        NauXong = true;
                        ThoiGianChoLayBanh = System.currentTimeMillis() + (1000 * 15);
                        if (!ListPlNauBanh.isEmpty()) {
                            for (int i = 0; i < ListPlNauBanh.size(); i++) {
                                Player pl = this.ListPlNauBanh.get(i);
                                if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                    pl.BanhChung += plBanhChung;
                                    pl.BanhTet += plBanhTet;
                                    Service.getInstance().sendThongBaoFromAdmin(pl, "|7|Bánh đã nấu xong rồi mời bạn đến nhận");
                                    ListPlNauBanh.clear();
                                    break;
                                }
                            }
                        }
                    } else if (NauXong == true && ((ThoiGianChoLayBanh - System.currentTimeMillis()) / 1000) == 0) {
                        Service.gI().sendThongBaoAllPlayer("Nồi nấu bánh đã nấu xong, tiếp tục nấu thôi nào");
                        plBanhChung = 0;
                        plBanhTet = 0;
                        ListPlNauBanh.clear();
                        Nuoc = 0;
                        Cui = 0;
                        NauXong = false;
                        ChoXong = false;
                        Count++;
                        ThoiGianChoLayBanh = -1;
                        ThoiGianCho = System.currentTimeMillis() + (1000 * 15);
                    }
                } else {
                    ThoiGianCho = System.currentTimeMillis() + (1000 * 15);
                    Thread.sleep(15000);
                }
            } catch (InterruptedException e) {
                System.out.print("Return Thread Fail");
            }

        }
    }
}
