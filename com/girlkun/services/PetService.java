package com.girlkun.services;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.Map;
import com.girlkun.models.player.NewPet;
import com.girlkun.models.player.Pet;
import com.girlkun.models.player.Pet3;
import com.girlkun.models.player.Player;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;
import java.util.Random;

public class PetService {

    private static PetService i;

    public static PetService gI() {
        if (i == null) {
            i = new PetService();
        }
        return i;
    }

    public void createXenPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet1(player, false, true, false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Đệ tử sẽ hấp thụ hết bọn nó cho sư phụ...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createXenPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet1(player, false, true, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Đệ tử sẽ hấp thụ hết bọn nó cho sư phụ...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createFidePet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet1(player, false, false, true);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Nhất Dương Chỉ của đệ tử sẽ không làm sư phụ thất vọng...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createFidePet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet1(player, false, false, true, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Nhất Dương Chỉ của đệ tử sẽ không làm sư phụ thất vọng...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createNormalPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createNormalPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createMabuPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, true, false, false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createMabuPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, true, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createBerusPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, true, false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Thần hủy diệt hiện thân tất cả quỳ xuống...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createBerusPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, true, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Thần hủy diệt hiện thân tất cả quỳ xuống...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createPicPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, true);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Sư Phụ SooMe hiện thân tụi m quỳ xuống...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createPicPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, true, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Sư Phụ SooMe hiện thân tụi m quỳ xuống...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createThienTuPetVip(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Thiên Tử";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 12;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 12; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Đệ tử vip vãi nồi đây...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createBlackGokuPetVip(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Black Goku";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 13;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4500;
                pet.nPoint.mpg = 4500;
                pet.nPoint.dameg = 310;
                pet.nPoint.defg = 200;
                pet.nPoint.critg = 22;
                for (int i = 0; i < 12; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Đệ tử vip vãi nồi đây...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createKaidoPetVip(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Tứ Hoàng Kaido";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 15;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 5000;
                pet.nPoint.mpg = 5000;
                pet.nPoint.dameg = 320;
                pet.nPoint.defg = 250;
                pet.nPoint.critg = 25;
                for (int i = 0; i < 12; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Đệ tử vip vãi nồi đây...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createSuperbu(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Superbu";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 1;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 12; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Đệ tử vip vãi nồi đây...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createberus(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Berus";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 2;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 6000;
                pet.nPoint.mpg = 6000;
                pet.nPoint.dameg = 500;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 12; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Đệ tử vip vãi nồi đây...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createuub(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Uub";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 3;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 8000;
                pet.nPoint.mpg = 8000;
                pet.nPoint.dameg = 800;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 12; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Đệ tử vip vãi nồi đây...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createbumusic(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Bư music";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 4;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 10000;
                pet.nPoint.mpg = 10000;
                pet.nPoint.dameg = 1100;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 12; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Đệ tử vip vãi nồi đây...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createngokhong(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Ngộ không";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 5;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 20000;
                pet.nPoint.mpg = 20000;
                pet.nPoint.dameg = 2000;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 12; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Đệ tử vip vãi nồi đây...");
            } catch (Exception e) {
            }
        }).start();
    }
    public void createpazuzu(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Pazuzu";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 15000000;
                pet.typePet = 6;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 30000;
                pet.nPoint.mpg = 30000;
                pet.nPoint.dameg = 3000;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 12; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Đệ tử vip vãi nồi đây...");
            } catch (Exception e) {
            }
        }).start();
    }
    public void createkaie(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Kaie";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 150000000;
                pet.typePet = 7;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 50000;
                pet.nPoint.mpg = 50000;
                pet.nPoint.dameg = 5000;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 12; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Đệ tử vip vãi nồi đây...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void changeNormalPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createNormalPet(player, gender, limitPower);
    }

    public void changeNormalPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createNormalPet(player, limitPower);
    }

    public void changeMabuPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMabuPet(player, limitPower);
    }

    public void changeMabuPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMabuPet(player, gender, limitPower);
    }

    public void changeBerusPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBerusPet(player, limitPower);
    }

    public void changeBerusPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBerusPet(player, gender, limitPower);
    }

    public void changePicPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createPicPet(player, limitPower);
    }

    public void changePicPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createPicPet(player, gender, limitPower);
    }

    public void changeXenPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createXenPet(player, limitPower);
    }

    public void changeXenPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createXenPet(player, gender, limitPower);
    }

    public void changeFide(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createFidePet(player, limitPower);
    }

    public void changeFide(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createFidePet(player, gender, limitPower);
    }

    public void changeNamePet(Player player, String name) {
        try {
            if (!InventoryServiceNew.gI().isExistItemBag(player, 400)) {
                Service.gI().sendThongBao(player, "Bạn cần thẻ đặt tên đệ tử, mua tại Santa");
                return;
            } else if (Util.haveSpecialCharacter(name)) {
                Service.gI().sendThongBao(player, "Tên không được chứa ký tự đặc biệt");
                return;
            } else if (name.length() > 10) {
                Service.gI().sendThongBao(player, "Tên quá dài");
                return;
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.name = "$" + name.toLowerCase().trim();
            InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItemBag(player, 400), 1);
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Service.gI().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã đặt cho con tên " + name);
                } catch (Exception e) {
                }
            }).start();
        } catch (Exception ex) {

        }
    }

    private int[] getDataPetNormal() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(20, 45); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetMabu() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetPic() {
        int[] hpmp = {1800, 1900, 2000, 2100, 2200, 2300};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 115) * 20; //hp
        petData[1] = Util.nextInt(40, 115) * 20; //mp
        petData[2] = Util.nextInt(70, 140); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetBerus() {
        int[] hpmp = {1800, 1900, 2000, 2100, 2200, 2300};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 115) * 20; //hp
        petData[1] = Util.nextInt(40, 115) * 20; //mp
        petData[2] = Util.nextInt(70, 140); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetKaido() {
        int[] hpmp = {2000, 2100, 2200, 2300, 2400, 2500};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 115) * 20; //hp
        petData[1] = Util.nextInt(40, 115) * 20; //mp
        petData[2] = Util.nextInt(70, 140); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetZeno() {
        int[] hpmp = {2000, 2100, 2200, 2300, 2400, 2500};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 115) * 20; //hp
        petData[1] = Util.nextInt(40, 115) * 20; //mp
        petData[2] = Util.nextInt(70, 140); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataFide() {
        int[] hpmp = {1800, 1900, 2000, 2100, 2200, 2300};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 115) * 20; //hp
        petData[1] = Util.nextInt(40, 115) * 20; //mp
        petData[2] = Util.nextInt(70, 140); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetXen() {
        int[] hpmp = {1800, 1900, 2000, 2100, 2200, 2300};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 115) * 20; //hp
        petData[1] = Util.nextInt(40, 115) * 20; //mp
        petData[2] = Util.nextInt(70, 140); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private void createNewPet(Player player, boolean isMabu, boolean isBerus, boolean isPic, byte... gender) {
        int[] data = isMabu ? isPic ? isBerus ? getDataPetMabu() : getDataPetPic() : getDataPetBerus() : getDataPetNormal();
        Pet pet = new Pet(player);
        pet.name = "$" + (isMabu ? "Mabư" : isBerus ? "Berus" : isPic ? "Pic" : "Đệ tử");
        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.nPoint.power = isMabu || isBerus || isPic ? 1500000 : 2000;
        pet.typePet = (byte) (isMabu ? 1 : isBerus ? 2 : isPic ? 3 : 0);
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 12; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 3; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.setFullHpMp();
        player.pet = pet;
    }

    private void createNewPet1(Player player, boolean isMabu, boolean isXen, boolean isFide, byte... gender) {
        int[] data = isMabu ? isXen ? isFide ? getDataPetMabu() : getDataPetXen() : getDataFide() : getDataPetNormal();
        Pet pet = new Pet(player);
        pet.name = "$" + (isMabu ? "Mabư" : isXen ? "Xên Bọ Hung" : isFide ? "Fide" : "Đệ tử");
        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.nPoint.power = isMabu || isXen || isFide ? 1500000 : 2000;
        pet.typePet = (byte) (isMabu ? 1 : isXen ? 5 : isFide ? 6 : 0);
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 8; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 3; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.setFullHpMp();
        player.pet = pet;
    }

    public static void Pet2(Player pl, int h, int b, int l) {
        if (pl.newpet != null) {
            pl.newpet.dispose();
        }
        pl.newpet = new NewPet(pl, (short) h, (short) b, (short) l);
        pl.newpet.name = "$";
        pl.newpet.gender = pl.gender;
        pl.newpet.nPoint.tiemNang = 1;
        pl.newpet.nPoint.power = 80000000000L;
        pl.newpet.nPoint.limitPower = 10;
        pl.newpet.nPoint.hpg = 5000;
        pl.newpet.nPoint.mpg = 5000;
        pl.newpet.nPoint.hp = 5000;
        pl.newpet.nPoint.mp = 5000;
        pl.newpet.nPoint.dameg = 1;
        pl.newpet.nPoint.defg = 1;
        pl.newpet.nPoint.critg = 1;
        pl.newpet.nPoint.stamina = 1;
        pl.newpet.nPoint.setBasePoint();
        pl.newpet.nPoint.setFullHpMp();
    }

    public static void Pet03(Player pl, int h, int b, int l) {

        pl.pet3 = new Pet3(pl, (short) h, (short) b, (short) l);
        pl.pet3.name = "Đường tăng";
        pl.pet3.gender = pl.gender;
        pl.pet3.nPoint.tiemNang = 1;
        pl.pet3.nPoint.power = 1;
        pl.pet3.location.x = pl.location.x;
        pl.pet3.location.y = pl.location.y;
        pl.pet3.nPoint.limitPower = 1;
        pl.pet3.nPoint.hpg = 200000;
        pl.pet3.nPoint.mpg = 5000000;
        pl.pet3.nPoint.hp = 50000;
        pl.pet3.nPoint.mp = 5000000;
        pl.pet3.nPoint.dameg = 1;
        pl.pet3.nPoint.defg = 1;
        pl.pet3.nPoint.critg = 1;
        pl.pet3.nPoint.stamina = 1;
        pl.pet3.nPoint.setBasePoint();
        pl.pet3.nPoint.setFullHpMp();

        Map maphotong = MapService.gI().getMapById(getzonedt());
        pl.pet3.maptg = maphotong;
        Service.gI().sendThongBaoOK(pl, "Map hộ tống: " + pl.pet3.maptg.mapName + "[" + pl.pet3.maptg.mapId + "]");
        PlayerService.gI().changeAndSendTypePK(pl.pet3, ConstPlayer.PK_ALL);
    }

    public static int getzonedt() {

        int[] maps = new int[]{70, 71, 72, 73, 74, 75};
        int randommap = new Random().nextInt(maps.length);
        int rdmap = maps[randommap];
        return rdmap;
    }

    //--------------------------------------------------------------------------
}
