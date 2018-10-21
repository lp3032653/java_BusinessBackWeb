package com.neuedu.service.impl;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.IcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements IcategoryService{

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse add_category(int parentId, String categoryName) {
        //非空判断
        if(categoryName==null||categoryName.equals("")){
            return ServerResponse.createByError("类别名称不能为空");
        }
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);
        int result = categoryMapper.insert(category);
        if(result>0){
            return ServerResponse.createBySuccess("添加类别成功");
        }else{
            return ServerResponse.createByError("添加类别失败");
        }
    }
}
