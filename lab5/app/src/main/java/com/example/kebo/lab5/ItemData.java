package com.example.kebo.lab5;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
//商品列表listView中item的实现
public class ItemData {

    public ArrayList<Map<String, Object>> data; //所有商品
    public ArrayList<Integer> stared;   //已收藏商品
    public ArrayList<Integer> itemid;   //商品的id
    public ArrayList<Integer> imgid;    //商品图片的id
    String[] name = {"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis",
            "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers", "Lindt",
            "Borggreve"};
    String[] price = {"¥5.00", "¥59.00", "¥79.00", "¥2399.00", "¥179.00", "¥14.90",
            "¥132.59", "¥141.43", "¥139.43", "¥28.90"};
    String[] infotype = {"作者", "产地", "产地", "版本", "重量", "产地", "重量", "重量", "重量",
            "重量"};
    String[] infovalue = {"Johanna Basford", "德国", "澳大利亚", "8GB", "2Kg", "英国", "300g",
            "118g", "249g", "640g"};
    String[] firstletter = {"E", "A", "D", "K", "W", "M", "F", "M", "L", "B"};
    Integer[] resimg = {R.mipmap.enchated_forest, R.mipmap.arla, R.mipmap.devondale,
            R.mipmap.kindle, R.mipmap.waitrose, R.mipmap.mcvitie, R.mipmap.ferrero,
            R.mipmap.maltesers, R.mipmap.lindt, R.mipmap.borggreve};

    public ItemData(boolean type) { //ItemData构造函数
        data = new ArrayList<>();   //初始化
        stared = new ArrayList<>();
        itemid = new ArrayList<>();
        imgid = new ArrayList<>();
        if (type) { //根据参数确定生成ItemData的类型
            for (int i = 0; i < name.length; ++i) {
                stared.add(0);  //默认收藏列表全为False
                itemid.add(i);
                imgid.add(resimg[i]);
                Map<String, Object> tmp = new LinkedHashMap<>();    //一个中间Map变量temp
                tmp.put("name", name[i]);
                tmp.put("price", price[i]);
                tmp.put("infotype", infotype[i]);
                tmp.put("infovalue", infovalue[i]);
                tmp.put("firstletter", firstletter[i]);
                data.add(tmp);
            }
        } else {
            Map<String, Object> tmp = new LinkedHashMap<>();
            stared.add(-1);
            itemid.add(-1);
            imgid.add(-1);
            tmp.put("name", "购物车");
            tmp.put("price", "价格");
            tmp.put("infotype", "title");
            tmp.put("infovalue", "title");
            tmp.put("firstletter", "*");
            data.add(tmp);
        }
    }

    public ItemData(ItemData i) {   //拷贝构造函数
        this.data = new ArrayList<>(i.data);
        this.stared = new ArrayList<>(i.stared);
        this.itemid = new ArrayList<>(i.itemid);
        this.imgid = new ArrayList<>(i.imgid);
    }

    public void remove(int pos) {   //重载remove()函数
        data.remove(pos);
        stared.remove(pos);
        itemid.remove(pos);
        imgid.remove(pos);
    }
    //重载add()函数
    public void add(Map<String, Object> m, int st, int iid, int im_id) {
        data.add(m);
        stared.add(st);
        itemid.add(iid);
        imgid.add(im_id);
    }

    public int getIndex(int iid) {  //getID()函数
        return itemid.indexOf(iid);
    }

    public int getsize() { //getSize()函数，返回长度
        return data.size();
    }
}
