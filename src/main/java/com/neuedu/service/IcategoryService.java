package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface IcategoryService {

    /**
     * 添加类别
     * */
    public ServerResponse add_category(int parentId,String categoryName);

}
