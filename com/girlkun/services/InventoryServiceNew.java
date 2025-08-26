package com.girlkun.services;

import com.girlkun.MaQuaTang.MaQuaTang;
import com.girlkun.MaQuaTang.MaQuaTang;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.MapVoDai.MapVodai;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.girlkun.models.npc.specialnpc.BillEgg;
import com.girlkun.models.npc.specialnpc.MabuEgg;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.Pet;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InventoryServiceNew {

    private static InventoryServiceNew I;

    public static InventoryServiceNew gI() {
        if (InventoryServiceNew.I == null) {
            InventoryServiceNew.I = new InventoryServiceNew();
        }
        return InventoryServiceNew.I;
    }

    public void addItemGiftCodeToPlayer(Player p, MaQuaTang giftcode) {
        Set<Integer> keySet = giftcode.detail.keySet();
        String textGift = "Bạn vừa nhận được:\b";
        for (Integer key : keySet) {
            int idItem = key;
            int quantity = giftcode.detail.get(key);

            if (idItem == -1) {
                p.inventory.gold = Math.min(p.inventory.gold + (long) quantity, 2000000000L);
                textGift += quantity + " vàng\b";
            } else if (idItem == -2) {
                p.inventory.gem = Math.min(p.inventory.gem + quantity, 200000000);
                textGift += quantity + " ngọc\b";
            } else if (idItem == -3) {
                p.inventory.ruby = Math.min(p.inventory.ruby + quantity, 200000000);
                textGift += quantity + " ngọc khóa\b";
            } else {
                Item itemGiftTemplate = ItemService.gI().createNewItem((short) idItem);
                if (itemGiftTemplate != null) {
                    Item itemGift = new Item((short) idItem);

                    if (itemGift.template.type == 0 || itemGift.template.type == 1 || itemGift.template.type == 2 || itemGift.template.type == 3
                            || itemGift.template.type == 4 || itemGift.template.type == 5) {
                        if (itemGift.template.id == 457) {
                            itemGift.itemOptions.add(new ItemOption(30, 0));
                        } else {
                            itemGift.itemOptions = giftcode.option;
                            itemGift.quantity = quantity;
                            addItemBag(p, itemGift);
                        }
                    } else {
                        itemGift.quantity = quantity;
                        addItemBag(p, itemGift);
                    }
                    textGift += "x" + quantity + " " + itemGift.template.name + "\b";
                }
            }
        }
        sendItemBags(p);

        Service.gI().sendThongBaoOK(p, textGift);

    }

    private void __________________Tìm_kiếm_item_____________________________() {
        //**********************************************************************
    }

    public Item findItem(List<Item> list, int tempId) {
        try {
            for (Item item : list) {
                if (item.isNotNullItem() && item.template.id == tempId) {
                    return item;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
    public Item findItemBagByTemp(Player player, int tempId) {
        return findItem(player.inventory.itemsBag, tempId);
    }
    public List<Item> getListItem(Player player, int... items) {
        return player.inventory.itemsBag.stream().filter(i -> in(i, items)).collect(Collectors.toList());
    }
    private boolean in(Item item, int... items) {
        return IntStream.of(items).anyMatch(id -> (item.isNotNullItem() && item.template.id == id));
    }

    public Item findItemBody(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBody, tempId);
    }
    public Item findnlskgiotohungvuong (Player pl) {
        for (Item item : pl.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 1035)&&(item.quantity >=1000) ) {
                return item;
            }
        }
        return null;
    }
    public Item findduahaugioto (Player pl) {
        for (Item item : pl.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 569) ) {
                return item;
            }
        }
        return null;
    }

    public Item findItemBag(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBag, tempId);
    }

    public Item findItemBox(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBox, tempId);
    }

    public boolean isExistItem(List<Item> list, int tempId) {
        try {
            this.findItem(list, tempId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isExistItemBody(Player player, int tempId) {
        return this.isExistItem(player.inventory.itemsBody, tempId);
    }

    public boolean isExistItemBag(Player player, int tempId) {
        return this.isExistItem(player.inventory.itemsBag, tempId);
    }

    public boolean isExistItemBox(Player player, int tempId) {
        return this.isExistItem(player.inventory.itemsBox, tempId);
    }

    private void __________________Sao_chép_danh_sách_item__________________() {
        //**********************************************************************
    }

    public List<Item> copyList(List<Item> items) {
        List<Item> list = new ArrayList<>();
        for (Item item : items) {
            list.add(ItemService.gI().copyItem(item));
        }
        return list;
    }

    public List<Item> copyItemsBody(Player player) {
        return copyList(player.inventory.itemsBody);
    }

    public List<Item> copyItemsBag(Player player) {
        return copyList(player.inventory.itemsBag);
    }

    public List<Item> copyItemsBox(Player player) {
        return copyList(player.inventory.itemsBox);
    }

    private void __________________Vứt_bỏ_item______________________________() {
        //**********************************************************************
    }

    public void throwItem(Player player, int where, int index) {
        Item itemThrow = null;
        if (where == 0) {
            itemThrow = player.inventory.itemsBody.get(index);
            removeItemBody(player, index);
            sendItemBody(player);
            Service.gI().Send_Caitrang(player);
        } else if (where == 1) {
            itemThrow = player.inventory.itemsBag.get(index);
            if (itemThrow.template.id != 457) {
                removeItemBag(player, index);
                sortItems(player.inventory.itemsBag);
                sendItemBags(player);
                if ((itemThrow.template.type >= 0 && itemThrow.template.type <= 4) || (itemThrow.template.id >= 222 && itemThrow.template.id <= 226)) {
                    if (!itemThrow.isSKH() && !itemThrow.isDHD() && !itemThrow.isDTL() && !itemThrow.isDTS()) {
                        ItemMap item = new ItemMap(player.zone, itemThrow.template.id, itemThrow.quantity, Util.nextInt((player.location.x - 50), (player.location.x + 50)), player.location.y, player.id);
                        item.options = itemThrow.itemOptions;
                        Service.getInstance().dropItemMap(player.zone, item);
                    }
                }
            } else {
                Service.gI().sendThongBao(player, "Thưa ngài");
            }
        }
        if (itemThrow == null) {
            return;
        }
    }

    private void __________________Xoá_bỏ_item______________________________() {
        //**********************************************************************
    }

    public void removeItem(List<Item> items, int index) {
        Item item = ItemService.gI().createItemNull();
        items.set(index, item);
    }

    public void removeItem(List<Item> items, Item item) {
        if (item == null) {
            return;
        }
        Item it = ItemService.gI().createItemNull();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(item)) {
                items.set(i, it);
                item.dispose();
                break;
            }
        }
    }

    public void removeItemBag(Player player, int index) {
        this.removeItem(player.inventory.itemsBag, index);
    }

    public void removeItemBag(Player player, Item item) {
        this.removeItem(player.inventory.itemsBag, item);
    }

    public void removeItemBody(Player player, int index) {
        this.removeItem(player.inventory.itemsBody, index);
    }

    public void removeItemPetBody(Player player, int index) {
        this.removeItemBody(player.pet, index);
    }

    public void removeItemBox(Player player, int index) {
        this.removeItem(player.inventory.itemsBox, index);
    }

    private void __________________Giảm_số_lượng_item_______________________() {
        //**********************************************************************
    }

    public void subQuantityItemsBag(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBag, item, quantity);
    }

    public void subQuantityItemsBody(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBody, item, quantity);
    }

    public void subQuantityItemsBox(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBox, item, quantity);
    }

    public void subQuantityItem(List<Item> items, Item item, int quantity) {
        if (item != null) {
            for (Item it : items) {
                if (item.equals(it)) {
                    it.quantity -= quantity;
                    if (it.quantity <= 0) {
                        this.removeItem(items, item);
                    }
                    break;
                }
            }
        }
    }

    private void __________________Sắp_xếp_danh_sách_item___________________() {
        //**********************************************************************
    }

    public void sortItems(List<Item> list) {
        int first = -1;
        int last = -1;
        Item tempFirst = null;
        Item tempLast = null;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isNotNullItem()) {
                first = i;
                tempFirst = list.get(i);
                break;
            }
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).isNotNullItem()) {
                last = i;
                tempLast = list.get(i);
                break;
            }
        }
        if (first != -1 && last != -1 && first < last) {
            list.set(first, tempLast);
            list.set(last, tempFirst);
            sortItems(list);
        }
    }

    private void __________________Thao_tác_tháo_mặc_item___________________() {
        //**********************************************************************
    }

    private Item putItemBag(Player player, Item item) {
        for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
            if (!player.inventory.itemsBag.get(i).isNotNullItem()) {
                player.inventory.itemsBag.set(i, item);
                Item sItem = ItemService.gI().createItemNull();
                return sItem;
            }
        }
        return item;
    }

    private Item putItemBox(Player player, Item item) {
        for (int i = 0; i < player.inventory.itemsBox.size(); i++) {
            if (!player.inventory.itemsBox.get(i).isNotNullItem()) {
                player.inventory.itemsBox.set(i, item);
                Item sItem = ItemService.gI().createItemNull();
                return sItem;
            }
        }
        return item;
    }

    private Item putItemBody(Player player, Item item) {
        Item sItem = item;
        if (!item.isNotNullItem()) {
            return sItem;
        }
        switch (item.template.type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 32:
            case 23:
            case 24:
            case 11:
            case 75:
            case 72:
                case 39:
            case 21:
            case 27:
            case 74:
            case 77:
                break;
            default:
                Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Trang bị không phù hợp!");
                return sItem;
        }
        if (item.template.gender < 3 && item.template.gender != player.gender) {
            Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Trang bị không phù hợp!");
            return sItem;
        }
        long powerRequire = item.template.strRequire;
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 21) {
                powerRequire = io.param * 1000000000L;
                break;
            }
        }
        if (player.nPoint.power < powerRequire) {
            Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Sức mạnh không đủ yêu cầu!");
            return sItem;
        }
        int index = -1;
        switch (item.template.type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                index = item.template.type;
                break;
            case 32:
            case 77:
                index = 6;
                break;
            case 23:
            case 24:
                index = 9;
                break;
            case 11:
                index = 8;
                break;
            case 75:
            case 72:
                index = 10;
                if (player.isPet) {
                    index = 8;
                }
                break;
            case 27:
                index = 7;
                break;
            case 21:
                index = 7;
                break;
            case 74:
                index = 11;
                break;
//            case 77:
//                index = 12;
//                break;
                case 39:
                index = 12;
                break;
        }
        sItem = player.inventory.itemsBody.get(index);
        player.inventory.itemsBody.set(index, item);
        return sItem;
    }

    public void itemBagToBody(Player player, int index) {
        Item item = player.inventory.itemsBag.get(index);
        if (item.isNotNullItem()) {
            player.inventory.itemsBag.set(index, putItemBody(player, item));
            if (item.template.id > 1327 && item.template.id < 1337) {
                Service.gI().removeTitle(player);
                Service.gI().sendFoot(player, item.template.id);
            }
            if (item.template.id > 1670 && item.template.id < 1685) {
                Service.gI().removeTitle(player);
                Service.gI().sendTitle(player, item.template.id);
            }
            if (player.inventory.itemsBody.get(10).isNotNullItem()) {
                Service.getInstance().sendPetFollow(player, (short) (player.inventory.itemsBody.get(10).template.iconID - 1));
            }
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().player(player);
            player.zone.load_Me_To_Another(player);
            player.zone.load_Another_To_Me(player);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().sendFlagBag(player);
            Service.getInstance().point(player);
            if (item.template.type >= 101 && item.template.type <= 105) {
                Service.gI().removeTitle(player);
            }
        }
    }

    public void itemBodyToBag(Player player, int index) {
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            if (index == 10) {
                Service.gI().sendPetFollow(player, (short) 0);
            }
            if (index == 7) {
                if (player.newpet != null) {
                    ChangeMapService.gI().exitMap(player.newpet);
                    player.newpet.dispose();
                    player.newpet = null;
                }
            }
            player.inventory.itemsBody.set(index, putItemBag(player, item));
            if (item.isNotNullItem()) {
                Service.gI().removeTitle(player);
                Service.gI().sendFoot(player, item.template.id);
            }
            if (item.isNotNullItem()) {
                Service.gI().removeTitle(player);
                Service.gI().sendTitle(player, item.template.id);
            }
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().player(player);
            player.zone.load_Me_To_Another(player);
            player.zone.load_Another_To_Me(player);
            Service.getInstance().sendFlagBag(player);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().point(player);
            if (item.template.type >= 101 && item.template.type <= 105) {
                Service.gI().removeTitle(player);
            }
        }
    }

    public void itemBagToPetBody(Player player, int index) {
        if (player.pet != null && player.pet.nPoint.power >= 1500000) {
            Item item = player.inventory.itemsBag.get(index);
            if (item.isNotNullItem()) {
                Item itemSwap = putItemBody(player.pet, item);
                player.inventory.itemsBag.set(index, itemSwap);
                sendItemBags(player);
                sendItemBody(player);
                Service.gI().Send_Caitrang(player.pet);
                Service.gI().Send_Caitrang(player);
                if (!itemSwap.equals(item)) {
                    Service.gI().point(player);
                    Service.gI().showInfoPet(player);
                }
                if (item.template.type >= 101 && item.template.type <= 105) {
                    Service.gI().removeTitle(player);
                }
            }
        } else {
            Service.gI().sendThongBaoOK(player, "Đệ tử phải đạt 1tr5 sức mạnh mới có thể mặc");
        }
    }

    public void itemPetBodyToBag(Player player, int index) {
        Item item = player.pet.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            player.pet.inventory.itemsBody.set(index, putItemBag(player, item));
            sendItemBags(player);
            sendItemBody(player);
            Service.gI().Send_Caitrang(player.pet);
            Service.gI().Send_Caitrang(player);
            Service.gI().point(player);
            Service.gI().showInfoPet(player);
            if (item.template.type >= 101 && item.template.type <= 105) {
                Service.gI().removeTitle(player.pet);
            }
        }
    }

    public void itemBoxToBodyOrBag(Player player, int index) {
        Item item = player.inventory.itemsBox.get(index);
        if (item.isNotNullItem()) {
            boolean done = false;
            if (item.template.type >= 0 && item.template.type <= 5 || item.template.type == 32) {
                Item itemBody = player.inventory.itemsBody.get(item.template.type == 32 ? 6 : item.template.type);
                if (!itemBody.isNotNullItem()) {
                    if (item.template.gender == player.gender || item.template.gender == 3) {
                        long powerRequire = item.template.strRequire;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 21) {
                                powerRequire = io.param * 1000000000L;
                                break;
                            }
                        }
                        if (powerRequire <= player.nPoint.power) {
                            player.inventory.itemsBody.set(item.template.type == 32 ? 6 : item.template.type, item);
                            player.inventory.itemsBox.set(index, itemBody);
                            done = true;
                            sendItemBody(player);
                            Service.getInstance().player(player);
                            player.zone.load_Me_To_Another(player);
                            player.zone.load_Another_To_Me(player);
                            Service.getInstance().Send_Caitrang(player);
                            Service.getInstance().point(player);
                            Service.getInstance().sendFlagBag(player);
                        }
                    }
                }
            }
            if (!done) {
                if (addItemBag(player, item)) {
                    if (item.quantity == 0) {
                        Item sItem = ItemService.gI().createItemNull();
                        player.inventory.itemsBox.set(index, sItem);
                    }
                    sendItemBags(player);
                }
            }
            sendItemBox(player);
        }
    }

    public void itemBagToBox(Player player, int index) {
        Item item = player.inventory.itemsBag.get(index);
        if (item != null) {
            if (item.template.id == 457 || item.template.id == 674) {
                Service.gI().sendThongBao(player, "Không thể cất vàng vào rương");
                return;
            }
            if (addItemBox(player, item)) {
                if (item.quantity == 0) {
                    Item sItem = ItemService.gI().createItemNull();
                    player.inventory.itemsBag.set(index, sItem);
                }
                sortItems(player.inventory.itemsBag);
                sendItemBags(player);
                sendItemBox(player);
            }
        }
    }

    public void itemBodyToBox(Player player, int index) {
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
//            if (index == 10) {
//                Service.gI().sendPetFollow(player, (short) 0);
//            }
            if (index == 7) {
                if (player.newpet != null) {
                    ChangeMapService.gI().exitMap(player.newpet);
                    player.newpet.dispose();
                    player.newpet = null;
                }
            }
            player.inventory.itemsBody.set(index, putItemBox(player, item));
            sortItems(player.inventory.itemsBag);
            sendItemBody(player);
            sendItemBox(player);
            Service.gI().Send_Caitrang(player);
            sendItemBody(player);
            Service.gI().point(player);
            Service.gI().sendFlagBag(player);
            player.zone.load_Me_To_Another(player);
            player.zone.load_Another_To_Me(player);
            if (item.template.type >= 101 && item.template.type <= 105) {
                Service.gI().removeTitle(player);
            }
        }
    }

    private void __________________Gửi_danh_sách_item_cho_người_chơi________() {
        //**********************************************************************
    }

    public void sendItemBags(Player player) {
        sortItems(player.inventory.itemsBag);
        Message msg;
        try {
            msg = new Message(-36);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBag.size());
            for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
                Item item = player.inventory.itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    continue;
                }
                msg.writer().writeShort(item.template.id);
                msg.writer().writeInt(item.quantity);
                msg.writer().writeUTF(item.getInfo());
                msg.writer().writeUTF(item.getContent());
                msg.writer().writeByte(item.itemOptions.size()); //options
                for (int j = 0; j < item.itemOptions.size(); j++) {
                    msg.writer().writeByte(item.itemOptions.get(j).optionTemplate.id);
                    msg.writer().writeShort(item.itemOptions.get(j).param);
                }
            }

            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendItemBody(Player player) {
        Message msg;
        try {
            msg = new Message(-37);
            msg.writer().writeByte(0);
            msg.writer().writeShort(player.getHead());
            msg.writer().writeByte(player.inventory.itemsBody.size());
            for (Item item : player.inventory.itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<Item.ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (Item.ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
        Service.gI().Send_Caitrang(player);
        Service.gI().sendFlagBag(player);
    }

    public void sendItemBox(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBox.size());
            for (Item it : player.inventory.itemsBox) {
                msg.writer().writeShort(it.isNotNullItem() ? it.template.id : -1);
                if (it.isNotNullItem()) {
                    msg.writer().writeInt(it.quantity);
                    msg.writer().writeUTF(it.getInfo());
                    msg.writer().writeUTF(it.getContent());
                    msg.writer().writeByte(it.itemOptions.size());
                    for (Item.ItemOption io : it.itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
        this.openBox(player);
    }

    public void openBox(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void __________________Thêm_vật_phẩm_vào_danh_sách______________() {
        //**********************************************************************
    }

    private boolean addItemSpecial(Player player, Item item) {
        //bùa
        if (item.template.type == 13) {
            int min = 0;
            try {
                String tagShopBua = player.iDMark.getShopOpen().tagName;
                if (tagShopBua.equals("BUA_1H")) {
                    min = 60;
                } else if (tagShopBua.equals("BUA_8H")) {
                    min = 60 * 8;
                } else if (tagShopBua.equals("BUA_1M")) {
                    min = 60 * 24 * 30;
                }
            } catch (Exception e) {
            }
            player.charms.addTimeCharms(item.template.id, min);
            return true;
        }

        switch (item.template.id) {
            case 568: //quả trứng
                if (player.mabuEgg == null) {
                    MabuEgg.createMabuEgg(player);
                }
                return true;

            case 2027: //quả trứng
                if (player.billEgg == null) {
                    BillEgg.createBillEgg(player);
                }
                return true;
            case 453: //tàu tennis
                player.haveTennisSpaceShip = true;
                return true;
            case 74: //đùi gà nướng
                player.nPoint.setFullHpMp();
                PlayerService.gI().sendInfoHpMp(player);
                return true;
        }
        return false;
    }

    public boolean addItemList(List<Item> items, Item item, int maxQuantity) {
        //nếu item ko có option, add option rỗng vào
        if (item.itemOptions.isEmpty()) {
            item.itemOptions.add(new ItemOption(73, 0));
        }

        //item cộng thêm chỉ số param: tự động luyện tập
        int optionId = isItemIncrementalOptionn(item);
        if (optionId != -1) {
            int param = 0;
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == optionId) {
                    param = io.param;
                }
            }
            for (Item it : items) {
                if (it.isNotNullItem() && it.template.id == item.template.id) {
                    for (ItemOption io : it.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            io.param += param;
                        }
                    }
                    return true;
                }
            }
        }

        //item tăng số lượng
        if (isItemIncremental(item)) {
            for (Item it : items) {
                if (it.isNotNullItem() && it.template.id == item.template.id) {
                    if (item.template.id != 457 && item.template.id != 590
                            && item.template.id != 2013 && item.template.id != 2014 && item.template.id != 2015
                            && it.quantity + item.quantity > maxQuantity && maxQuantity != 0) {
                        if (maxQuantity == 999) {
                            int add = 999 - it.quantity;
                            it.quantity = 999;
                            item.quantity -= add;
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        it.quantity += item.quantity;
                        if (maxQuantity > 0) {
                            item.quantity = 0;
                        }
                        return true;
                    }
                }
            }
        }

        //add item vào ô mới
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).isNotNullItem()) {
                items.set(i, ItemService.gI().copyItem(item));
                item.quantity = 0;
                return true;
            }
        }
        return false;
    }

    private boolean isItemIncremental(Item item) { //item cộng dồn số lượng
        switch (item.template.type) {
            case 99: // vật phẩm hỗ trợ
            case 8: //vật phẩm nhiệm vụ
            case 12: //ngọc rồng
            case 30: //sao pha lê
            case 6: //đậu thần
            case 29: //item time, đồ ăn
            case 25: //rađa dò ngọc namếc
            case 27:
            case 33: //mảnh rada
            case 14: //đá nâng cấp
            case 50: //vé đổi đồ hủy diệt
                return true;
            default:
                return false;
        }
    }

    public boolean addItemBag(Player player, Item item) {
        //ngọc rồng đen
        if (ItemMapService.gI().isBlackBall(item.template.id)) {
            return BlackBallWar.gI().pickBlackBall(player, item);
        }
        else if (ItemMapService.gI().isVodaiBall(item.template.id)) {
            return MapVodai.gI().pickVodaiBall(player, item);
        }
        if (addItemSpecial(player, item)) {
            return true;
        }

        //gold, gem, ruby
        switch (item.template.type) {
            case 9:
                if (player.inventory.gold + item.quantity <= Inventory.LIMIT_GOLD) {
                    player.inventory.gold += item.quantity;
                    Service.gI().sendMoney(player);
                    return true;
                } else {
                    Service.gI().sendThongBao(player, "Vàng sau khi nhặt quá giới hạn cho phép");
                    return false;
                }
            case 10:
                player.inventory.gem += item.quantity;
                Service.gI().sendMoney(player);
                return true;
            case 34:
                player.inventory.ruby += item.quantity;
                Service.gI().sendMoney(player);
                return true;
        }

        //mở rộng hành trang - rương đồ
        if (item.template.id == 517) {
            if (player.inventory.itemsBag.size() < Inventory.MAX_ITEMS_BAG) {
                player.inventory.itemsBag.add(ItemService.gI().createItemNull());
                Service.gI().sendThongBaoOK(player, "Hành trang của bạn đã được mở rộng thêm 1 ô");
                return true;
            } else {
                Service.gI().sendThongBaoOK(player, "Hành trang của bạn đã đạt tối đa");
                return false;
            }
        } else if (item.template.id == 518) {
            if (player.inventory.itemsBox.size() < Inventory.MAX_ITEMS_BOX) {
                player.inventory.itemsBox.add(ItemService.gI().createItemNull());
                Service.gI().sendThongBaoOK(player, "Rương đồ của bạn đã được mở rộng thêm 1 ô");
                return true;
            } else {
                Service.gI().sendThongBaoOK(player, "Rương đồ của bạn đã đạt tối đa");
                return false;
            }
        }
        return addItemList(player.inventory.itemsBag, item);
    }
    
    public boolean addItemBag(Player player, Item item, int maxQuantity) {
        if (ItemMapService.gI().isBlackBall(item.template.id)) {
            return BlackBallWar.gI().pickBlackBall(player, item);
        }

        //quả trứng
        if (item.template.id == 568) {
            if (player.mabuEgg == null) {
                MabuEgg.createMabuEgg(player);
            }
            return true;
        }

        //tennis spaceship
        if (item.template.id == 453) {
            player.haveTennisSpaceShip = true;
            return true;
        }
        //đùi gà nướng
        if (item.template.id == 74) {
            player.nPoint.setFullHpMp();
            PlayerService.gI().sendInfoHpMp(player);
            return true;
        }

        //gold, gem, ruby
        switch (item.template.type) {
            case 10:
                player.inventory.gem += item.quantity;
                Service.getInstance().sendMoney(player);
                return true;
            case 34:
                player.inventory.ruby += item.quantity;
                Service.getInstance().sendMoney(player);
                return true;
        }

        //mở rộng hành trang - rương đồ
        if (item.template.id == 517) {
            if (player.inventory.itemsBag.size() < 60) {
                player.inventory.itemsBag.add(ItemService.gI().createItemNull());
                Service.getInstance().sendThongBaoOK(player, "Hành trang của bạn đã được mở rộng thêm 1 ô");
                return true;
            } else {
                Service.getInstance().sendThongBaoOK(player, "Hành trang của bạn đã đạt tối đa");
                return false;
            }
        } else if (item.template.id == 518) {
            if (player.inventory.itemsBox.size() < 40) {
                player.inventory.itemsBox.add(ItemService.gI().createItemNull());
                Service.getInstance().sendThongBaoOK(player, "Rương đồ của bạn đã được mở rộng thêm 1 ô");
                return true;
            } else {
                Service.getInstance().sendThongBaoOK(player, "Rương đồ của bạn đã đạt tối đa");
                return false;
            }
         
        }
        return addItemList(player.inventory.itemsBag, item, maxQuantity);
    }

    public boolean addItemBox(Player player, Item item) {
        return addItemList(player.inventory.itemsBox, item);
    }

    public boolean addItemList(List<Item> items, Item itemAdd) {
        //nếu item ko có option, add option rỗng vào
        if (itemAdd.itemOptions.isEmpty()) {
            if (itemAdd.template.type == 5) {
                itemAdd.itemOptions.add(new Item.ItemOption(228, 0));
            }
            itemAdd.itemOptions.add(new Item.ItemOption(73, 0));
        }

        //item cộng thêm chỉ số param: tự động luyện tập
        int[] idParam = isItemIncrementalOption(itemAdd);
        if (idParam[0] != -1) {
            for (Item it : items) {
                if (it.isNotNullItem() && it.template.id == itemAdd.template.id) {
                    for (Item.ItemOption io : it.itemOptions) {
                        if (io.optionTemplate.id == idParam[0]) {
                            io.param += idParam[1];
                        }
                    }
                    return true;
                }
            }
        }

        //item tăng số lượng
        //Gộp vật phẩm x99999
        if (itemAdd.template.isUpToUp) {
            for (Item it : items) {
                if (!it.isNotNullItem() || it.template.id != itemAdd.template.id) {
                    continue;
                }
                //457-thỏi vàng; 590-bí kiếp
                if (itemAdd.template.id == 457
                        || itemAdd.template.id == 590
                        || itemAdd.template.id == 1320
                        || itemAdd.template.id == 674
                        || itemAdd.template.id == 610
                        || itemAdd.template.id == 14
                        || itemAdd.template.id == 15
                        || itemAdd.template.id == 16
                        || itemAdd.template.id == 17
                        || itemAdd.template.id == 18
                        || itemAdd.template.id == 19
                        || itemAdd.template.id == 20
                        || itemAdd.template.id == 220
                        || itemAdd.template.id == 221
                        || itemAdd.template.id == 222
                        || itemAdd.template.id == 223
                        || itemAdd.template.id == 224
                        || itemAdd.template.id == 987
                        || itemAdd.template.id == 933
                        || itemAdd.template.id == 934
                        || itemAdd.template.id == 1394
                        || itemAdd.template.id == 1395
                        || itemAdd.template.id == 1396
                        || itemAdd.template.id == 1397
                        || itemAdd.template.id == 1398
                        || itemAdd.template.id == 1400
                        || itemAdd.template.id == 1401
                        || itemAdd.template.id == 1402
                        || itemAdd.template.id == 1828
                        || itemAdd.template.id == 1829
                        || itemAdd.template.id == 1405
                        || itemAdd.template.id == 1406
                        || itemAdd.template.id == 1407
                        || itemAdd.template.id == 1408
                        || itemAdd.template.id == 1409
                        || itemAdd.template.id == 1411
                        || itemAdd.template.id == 1412
                        || itemAdd.template.id == 1413
                        || itemAdd.template.id == 1836
                        || itemAdd.template.id == 1415
                        || itemAdd.template.type == 14
                        || itemAdd.template.type == 933
                        || itemAdd.template.type == 934) {
                    it.quantity += itemAdd.quantity;
                    itemAdd.quantity = 0;
                    return true;
                }

                if (it.quantity < 9999) {
                    int add = 9999 - it.quantity;
                    if (itemAdd.quantity <= add) {
                        it.quantity += itemAdd.quantity;
                        itemAdd.quantity = 0;
                        return true;
                    } else {
                        it.quantity = 9999;
                        itemAdd.quantity -= add;
                    }
                }
            }
        }

        //add item vào ô mới
        if (itemAdd.quantity > 0) {
            for (int i = 0; i < items.size(); i++) {
                if (!items.get(i).isNotNullItem()) {
                    items.set(i, ItemService.gI().copyItem(itemAdd));
                    itemAdd.quantity = 0;
                    return true;
                }
            }
        }
        return false;
    }

    private void __________________Kiểm_tra_điều_kiện_vật_phẩm______________() {
        //**********************************************************************
    }

    /**
     * Kiểm tra vật phẩm có phải là vật phẩm tăng chỉ số option hay không
     *
     * @param item
     * @return id option tăng chỉ số - param
     */
    private int[] isItemIncrementalOption(Item item) {
        for (Item.ItemOption io : item.itemOptions) {
            switch (io.optionTemplate.id) {
                case 1:
                    return new int[]{io.optionTemplate.id, io.param};
            }
        }
        return new int[]{-1, -1};
    }

    private byte isItemIncrementalOptionn(Item item) { //trả về id option template
        int temp = item.template.id;
        byte opp = -1;
        switch (temp) {
            case 521:
                opp = 1;
                break;
            default:
                break;

        }
        return opp;
    }

    private void __________________Kiểm_tra_danh_sách_còn_chỗ_trống_________() {
        //**********************************************************************
    }

    public byte getCountEmptyBag(Player player) {
        return getCountEmptyListItem(player.inventory.itemsBag);
    }

    public byte getCountEmptyListItem(List<Item> list) {
        byte count = 0;
        for (Item item : list) {
            if (!item.isNotNullItem()) {
                count++;
            }
        }
        return count;
    }

    public byte getIndexBag(Player pl, Item it) {
        for (byte i = 0; i < pl.inventory.itemsBag.size(); ++i) {
            Item item = pl.inventory.itemsBag.get(i);
            if (item != null && it.equals(item)) {
                return i;
            }
        }
        return -1;
    }

    public byte getIndexBag(Player pl, int iditem) {
        for (byte i = 0; i < pl.inventory.itemsBag.size(); ++i) {
            Item item = pl.inventory.itemsBag.get(i);
            if (item != null && item.template.id == iditem) {
                return i;
            }
        }
        return -1;
    }

    public boolean finditemWoodChest(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 570) {
                return false;
            }
        }
        for (Item item : player.inventory.itemsBox) {
            if (item.isNotNullItem() && item.template.id == 570) {
                return false;
            }
        }
        return true;
    }
}
