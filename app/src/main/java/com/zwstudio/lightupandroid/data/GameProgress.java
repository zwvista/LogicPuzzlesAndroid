package com.zwstudio.lightupandroid.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by zwvista on 2016/09/29.
 */

@Entity(name = "GameProgress")
public class GameProgress implements java.io.Serializable {
    @Id
    @Column(name = "ID", nullable = false)
    public int id;
    @Column(name = "levelID")
    public String levelID;
    @Column(name = "markerOption")
    public Integer markerOption;
}
