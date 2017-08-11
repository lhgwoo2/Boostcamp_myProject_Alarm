package com.boostcamp.sentialarm.fragment;

import java.util.Date;

import io.realm.annotations.PrimaryKey;

/**
 * Created by 현기 on 2017-08-09.
 */

public class SongRecordDTO{

    @PrimaryKey
    private int id;

    private Date playDate;

}
