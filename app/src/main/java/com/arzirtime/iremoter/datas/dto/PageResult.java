package com.arzirtime.iremoter.datas.dto;

import com.arzirtime.iremoter.common.LogTag;
import com.arzirtime.support.util.ListUtils;
import com.arzirtime.support.util.LogUtil;

import java.util.List;

public class PageResult {
    public int totalCount;
    public List rows;

    public PageResult(){

    }

    public PageResult(List rows, int pageIndex, int pageSize, int totalCount){
        this.rows = rows;
        this.totalCount = totalCount;
    }
}
