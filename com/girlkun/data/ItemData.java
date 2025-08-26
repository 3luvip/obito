package com.girlkun.data;

import com.girlkun.models.Template.ArrHead2Frames;
import com.girlkun.models.Template.ItemOptionTemplate;
import com.girlkun.server.Manager;
import com.girlkun.network.io.Message;
import com.girlkun.server.io.MySession;
import java.util.Arrays;
import java.util.List;

public class ItemData {

    public static List<Integer> list_thuc_an = Arrays.asList(663, 664, 665, 666, 667);

    //------------------------------------------------------ start update client
    public static void updateItem(MySession session) {
        updateItemOptionItemplate(session);
//        int count = 500;
//        updateItemTemplate(session, count);
//        updateItemTemplate(session, count, Manager.ITEM_TEMPLATES.size());
        updateItemArrHead2F(session);
        updateItemTemplate(session, 750);
        updateItemTemplate(session, 750, Manager.ITEM_TEMPLATES.size());
//        updateItemTemplate(session, 1500, Manager.ITEM_TEMPLATES.size());
    }

    private static void updateItemOptionItemplate(MySession session) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);
            msg.writer().writeByte(DataGame.vsItem); //vcitem
            msg.writer().writeByte(0); //update option
            msg.writer().writeByte(Manager.ITEM_OPTION_TEMPLATES.size());
            for (ItemOptionTemplate io : Manager.ITEM_OPTION_TEMPLATES) {
                msg.writer().writeUTF(io.name);
                msg.writer().writeByte(io.type);
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    private static void updateItemTemplate(MySession session, int count) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);

            msg.writer().writeByte(DataGame.vsItem); //vcitem
            msg.writer().writeByte(1); //reload itemtemplate
            msg.writer().writeShort(count);
            for (int i = 0; i < count; i++) {
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).type);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).gender);
                msg.writer().writeUTF(Manager.ITEM_TEMPLATES.get(i).name);
                msg.writer().writeUTF(Manager.ITEM_TEMPLATES.get(i).description);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).level);
                msg.writer().writeInt(Manager.ITEM_TEMPLATES.get(i).strRequire);
                msg.writer().writeShort(Manager.ITEM_TEMPLATES.get(i).iconID);
                msg.writer().writeShort(Manager.ITEM_TEMPLATES.get(i).part);
                msg.writer().writeBoolean(Manager.ITEM_TEMPLATES.get(i).isUpToUp);
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    


    private static void updateItemArrHead2F(MySession session) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);
            msg.writer().writeByte(DataGame.vsItem); //vcitem
            msg.writer().writeByte(100); //update ArrHead2F
            msg.writer().writeShort(Manager.ARR_HEAD_2_FRAMES.size());
            for (ArrHead2Frames arr : Manager.ARR_HEAD_2_FRAMES) {
                msg.writeByte(arr.frames.size());
                for (int i : arr.frames) {
                    msg.writer().writeShort(i);
                }
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }


    private static void updateItemTemplate(MySession session, int start, int end) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);

            msg.writer().writeByte(DataGame.vsItem); //vcitem
            msg.writer().writeByte(2); //add itemtemplate
            msg.writer().writeShort(start);
            msg.writer().writeShort(end);
            for (int i = start; i < end; i++) {
//                System.out.println("start: " + start + " -> " + end + " id " + Manager.ITEM_TEMPLATES.get(i).id);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).type);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).gender);
                msg.writer().writeUTF(Manager.ITEM_TEMPLATES.get(i).name);
                msg.writer().writeUTF(Manager.ITEM_TEMPLATES.get(i).description);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).level);
                msg.writer().writeInt(Manager.ITEM_TEMPLATES.get(i).strRequire);
                msg.writer().writeShort(Manager.ITEM_TEMPLATES.get(i).iconID);
                msg.writer().writeShort(Manager.ITEM_TEMPLATES.get(i).part);
                msg.writer().writeBoolean(Manager.ITEM_TEMPLATES.get(i).isUpToUp);
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------- end update client
}
