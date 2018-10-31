package com.neuedu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.DateUtil;
import com.neuedu.utils.PropertiesUtil;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ICategoryService categoryService;
    @Override
    public ServerResponse saveOrUpdate(Product product) {
        if(product==null){
            return ServerResponse.createByError("参数错误");
        }
        // sub_images  1.jpg,2.jpg,3.jpg
        String subimages=product.getSubImages();
        if(subimages!=null&&!subimages.equals("")){
            String[] subimagesArr=  subimages.split(",");// ["1.jpg","2.jpg","3.jpg"]
            if(subimagesArr!=null&&subimagesArr.length>0){
                //为商品主图字段赋值
                product.setMainImage(subimagesArr[0]);
            }
        }
        //判断添加商品还是更新商品
        Integer productId= product.getId();
        if(productId==null){//添加商品
            int result=productMapper.insert(product);
            if(result>0){
                return ServerResponse.createBySuccess("商品添加成功");
            }else{
                return ServerResponse.createByError("商品添加失败");
            }
        }else{
            //更新商品
            int result=productMapper.updateByPrimaryKey(product);
            if(result>0){
                return ServerResponse.createBySuccess("商品更新成功");
            }else{
                return ServerResponse.createByError("商品更新失败");
            }
        }


    }

    @Override
    public ServerResponse set_sale_status(Integer productId, Integer status) {

        if(productId==null){
            return ServerResponse.createByError("商品id必须传递");
        }
        if(status==null){
            return ServerResponse.createByError("商品状态信息必须传递");
        }
        Product product=new Product();
        product.setId(productId);
        product.setStatus(status);
        int result=productMapper.updateByPrimaryKeySelective(product);
        if(result>0){
            return ServerResponse.createBySuccess("修改产品状态成功");
        }
        return ServerResponse.createByError("修改产品状态失败");
    }

    @Override
    public ServerResponse findProductByPage(Integer pageNo, Integer pageSize) {

        PageHelper.startPage(pageNo,pageSize);
        List<Product> productList= productMapper.selectAll();
        List<ProductListVO> productListVOList=new ArrayList<>();
        for(Product product:productList){
            ProductListVO productListVO=assembleProductListVO(product);
            productListVOList.add(productListVO);
        }

        PageInfo pageInfo=new PageInfo(productList);
        pageInfo.setList(productListVOList);


        return ServerResponse.createBySuccess(pageInfo,"成功");
    }

    /**
     * Product-->ProductListVO
     * */
    private  ProductListVO assembleProductListVO(Product product){

        ProductListVO productListVO=new ProductListVO();
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setId(product.getId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        return productListVO;
    }
    @Override
    public ServerResponse findProductDetail(Integer productId) {

        if(productId==null){
            return  ServerResponse.createByError("商品id必须传递");
        }
        Product product= productMapper.selectByPrimaryKey(productId);
        ProductDetailVO productDetailVO=assembleProductDetailVO(product);
        if(productDetailVO!=null){
            return  ServerResponse.createBySuccess(productDetailVO,"成功");
        }

        return ServerResponse.createByError("商品不存在");
    }

    private ProductDetailVO assembleProductDetailVO(Product product){
        if(product!=null){

            ProductDetailVO productDetailVO=new ProductDetailVO();
            productDetailVO.setCategoryId(product.getCategoryId());
            productDetailVO.setName(product.getName());
            productDetailVO.setId(product.getId());
            productDetailVO.setDetail(product.getDetail());
            productDetailVO.setMainImage(product.getMainImage());
            productDetailVO.setPrice(product.getPrice());
            productDetailVO.setStock(product.getStock());
            productDetailVO.setStatus(product.getStatus());
            productDetailVO.setSubImages(product.getSubImages());
            productDetailVO.setSubtitle(product.getSubtitle());

            Category category=categoryMapper.selectByPrimaryKey(product.getCategoryId());
            if(category==null){
                productDetailVO.setParentCategoryId(0);
            }else{
                productDetailVO.setParentCategoryId(category.getParentId());
            }

            //imagehost
            productDetailVO.setImageHost((String) PropertiesUtil.getProperty("imagehost"));
            productDetailVO.setCreateTime(DateUtil.dateToStr(product.getCreateTime()));
            productDetailVO.setUpdateTime(DateUtil.dateToStr(product.getUpdateTime()));
            return productDetailVO;
        }

        return null;
    }

    @Override
    public ServerResponse searchProductsByProductIdOrProductName(Integer productId, String productName, Integer pageNo, Integer pageSize) {

        if(productName!=null&&!productName.equals("")){ //  name like %productName%
            productName="%"+productName+"%";
        }
        PageHelper.startPage(pageNo,pageSize);
        List<Product> productList=  productMapper.searchProduct(productId,productName);
        PageInfo pageInfo=new PageInfo(productList);
        List<ProductListVO> productListVOList=new ArrayList<>();
        for(Product product:productList){
            ProductListVO productListVO=assembleProductListVO(product);
            productListVOList.add(productListVO);
        }
        pageInfo.setList(productListVOList);

        return ServerResponse.createBySuccess(pageInfo,"成功");
    }

    /**
     * 前台-商品搜索接口
     * */
    @Override
    public ServerResponse searchProduct(String keyword, Integer categoryId, Integer pageNo, Integer pageSize, String orderBy) {

        //非空判断
        if(keyword==null&& categoryId==null){
            return ServerResponse.createByError("参数错误");
        }
        Set<Category> categorySet=new HashSet<>();
        if(categoryId!=null){
            Category category= categoryMapper.selectByPrimaryKey(categoryId);
            if(category==null&&keyword==null){//没有商品
                PageHelper.startPage(pageNo,pageSize);
                List<ProductListVO> productListVOList=new ArrayList<>();
                PageInfo pageInfo=new PageInfo(productListVOList);
                return   ServerResponse.createBySuccess(pageInfo,"没有商品");
            }
            //递归后代节点

            categorySet= categoryService.findChildCategory(categorySet,categoryId);

        }

        if(keyword!=null&&!keyword.equals("")){
            keyword="%"+keyword+"%";
        }
        PageHelper.startPage(pageNo,pageSize);

        if(orderBy!=null&&!orderBy.equals("")){
            String[] orderByArr=orderBy.split("_");
            if(orderByArr!=null&&orderByArr.length>1){
                String  orderby_field=orderByArr[0];
                String sort=orderByArr[1];
                PageHelper.orderBy(orderby_field+" "+sort);
            }
        }
        List<Product> productList=  productMapper.findProductByCategoryIdsAndKeyWord(categorySet,keyword);
        PageInfo pageInfo=new PageInfo(productList);
        List<ProductListVO> productListVOList=new ArrayList<>();
        for(Product product:productList){
            productListVOList.add(assembleProductListVO(product));
        }
        pageInfo.setList(productListVOList);



        return ServerResponse.createBySuccess(pageInfo,"成功");
    }

    @Override
    public ServerResponse productDeatail(Integer productId) {

        //step1:非空判断
        if(productId==null){
            return ServerResponse.createByError("商品id必须传递");
        }
        //step2:根据productId查询商品信息
        Product product=productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.createByError("商品已经被下架或删除");
        }
        if(product.getStatus()!= Const.ProductStatusEnum.PRODUCT_ONLLINE.getStatus()){
            return ServerResponse.createByError("商品已经被下架或删除");
        }

        //product-productDetailVO
        ProductDetailVO productDetailVO=assembleProductDetailVO(product);

        return ServerResponse.createBySuccess(productDetailVO);
    }


}