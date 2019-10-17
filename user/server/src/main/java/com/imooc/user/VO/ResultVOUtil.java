package com.imooc.user.VO;

import com.imooc.user.enums.ResultEnum;

public class ResultVOUtil {

    public static ResultVO success(){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO error(ResultEnum resultEnum){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg(resultEnum.getMessage());
        resultVO.setData(resultEnum);
        return resultVO;
    }
}
