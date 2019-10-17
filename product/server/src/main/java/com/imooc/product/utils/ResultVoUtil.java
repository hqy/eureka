package com.imooc.product.utils;

import com.imooc.product.VO.ResultVO;

public class ResultVoUtil {
    public static ResultVO success(Object obj){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(obj);
        return resultVO;
    }
}
