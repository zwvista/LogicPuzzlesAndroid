package com.zwstudio.lightupandroid.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by zwvista on 2016/09/29.
 */

@Entity(name = "MoveProgress")
public class MoveProgress implements java.io.Serializable {
    @Id
    @Column(name = "ID", nullable = false)
    public int id;
    @Column(name = "levelID")
    public String levelID;
    @Column(name = "moveIndex")
    public Integer moveIndex;
    @Column(name = "row")
    public Integer row;
    @Column(name = "col")
    public Integer col;
    @Column(name = "objTypeAsString")
    public String objTypeAsString;
}
