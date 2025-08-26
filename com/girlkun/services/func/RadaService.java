package com.girlkun.services.func;

import com.girlkun.card.Card;
import com.girlkun.consts.ConstNpc;
import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.services.Service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class RadaService {

    private static RadaService instance;

    private RadaService() {

    }

    public static RadaService getInstance() {
        if (instance == null) {
            instance = new RadaService();
        }
        return instance;
    }
    public void setIDAuraEff(Player player, byte aura) {
        try {
            Message mss = new Message(ConstNpc.RADA_CARD);
            DataOutputStream ds = mss.writer();
            ds.writeByte(4);
            ds.writeInt((int) player.id);
            ds.writeShort(aura);
            ds.flush();
            Service.getInstance().sendMessAllPlayerInMap(player, mss);
            mss.cleanup();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}