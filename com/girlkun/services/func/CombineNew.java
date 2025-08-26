package com.girlkun.services.func;

import com.girlkun.models.item.Item;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Stole By obito ZALO 0866468126 💖
 *
 */
public class CombineNew {

    public long lastTimeCombine;

    public List<Item> itemsCombine;
    public int typeCombine;
    public int ngusacCombine;
    public int dakham;
    public int goldCombine;
    public int gemCombine;
    public float ratioCombine;
    public float ratioCombine2;
    public float ratioCombine3;
    public float ratioCombine4;
    public float ratioCombine5;
    public int countDaNangCap;
    public short countDaBaoVe;
    public int rubyCombine;
    public short quantities = 1;

    public CombineNew() {
        this.itemsCombine = new ArrayList<>();
    }

    public void setTypeCombine(int type) {
        this.typeCombine = type;
    }

    public void clearItemCombine() {
        this.itemsCombine.clear();
    }

    public void clearParamCombine() {
        this.goldCombine = 0;
        this.gemCombine = 0;
        this.ratioCombine = 0;
        this.countDaNangCap = 0;
        this.countDaBaoVe = 0;

    }

    public void dispose() {
        this.itemsCombine = null;
    }
}
