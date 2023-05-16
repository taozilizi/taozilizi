package taozi.constant;

import taozi.vo.AcceptorDetailExlExpVo;
import taozi.vo.AcceptorExlImpVo;

import java.util.HashSet;
import java.util.Set;

public class Cons {
    /**所有导出数据获取完成在一次性导出*/
    public static Set<AcceptorDetailExlExpVo> allExp = new HashSet<>();

    /**所有导入的数据*/
    public static Set<AcceptorExlImpVo> allImp = new HashSet<>();

    /**所有导入的已注册数据----第一个接口查询出来的已注册*/
    public static Set<AcceptorExlImpVo> allReg = new HashSet<>();

    /**所有导入的已注册数据----第二个接口查询导出成功*/
    public static Set<AcceptorExlImpVo> allRegSuccess = new HashSet<>();

    /**所有导入的未 注册数据----第一个接口查询出来的已注册*/
    public static Set<AcceptorExlImpVo> allUnReg = new HashSet<>();



    /**当前查询的*/
    public static AcceptorExlImpVo current_vo=null;
    public static String yearMonth;
    /**验证码背景图*/
    public static String bigImg;
    /**验证码移动缺口图*/
    public static String tarImg;
}
