package com.neuedu.controller.portal;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    IProductService productService;
    /**
     * 产品搜索
     *
     * */

    @RequestMapping(value = "/list.do")
    public ServerResponse list(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false)Integer categoryId,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNo,
                               @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                               @RequestParam(required = false) String orderBy){


        return productService.searchProduct(keyword,categoryId,pageNo,pageSize,orderBy);
    }
    @RequestMapping(value = "/detail.do")
    public  ServerResponse productDeatail(Integer productId){
        return productService.productDeatail(productId);
    }
}