package com.example.kebo.project_search;

import java.util.List;

/**
 * Created by kebo on 2017/12/31.
 */

public class Song {
    private String name;
    private int id;
    private List<Artist> ar;
    private Album al;
    public String getName(){return name;}
    public int getId(){return id;}
    public List<Artist> getArs(){return ar;}
    public Album getAl(){return al;}
}
