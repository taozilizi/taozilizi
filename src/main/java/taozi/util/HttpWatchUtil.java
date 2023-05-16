package taozi.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import taozi.constant.Cons;
import taozi.vo.AcceptorDetailExlExpVo;
import taozi.vo.VerifyImgVo;
import com.github.wycm.biz.constant.AcceptorStatusInSE;
import taozi.resp.AcceptorBasicInfoData;
import taozi.resp.detail.BatchSettleInfoPage;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**处理监控chrome返回的url请求*/
public class HttpWatchUtil {

    /**
     * 处理查询基本信息
     * **/
    public static void findAccInfoListByAcptName(JSONObject data){
        if(ConstUtil.isSuccess(data)){
            JSONArray l2 = data.getJSONArray("data");
            List<AcceptorBasicInfoData> list = l2.toJavaList(AcceptorBasicInfoData.class);
            System.out.println(DateUtil.getDate()+"-------"+ Cons.current_vo+"-----<<<查询基本信息成功>>>>----"+JSON.toJSONString(list));


            if (!CollectionUtil.isEmpty(list)) {
                AcceptorBasicInfoData vv=ConstUtil.isZw(Cons.current_vo.getAcptName(),list);

                if(ObjectUtil.isEmpty(vv)){
                    if(!Cons.allUnReg.contains(Cons.current_vo)){
                        Cons.allUnReg.add(Cons.current_vo);
                        List<AcceptorDetailExlExpVo> l =  setUnReg();
                        AcceptorDetailUtil.exp(l);
                        Cons.allExp.addAll(l);
                    }
                }else{
                    Cons.allReg.add(Cons.current_vo);
                }
            } else{
                if(!Cons.allUnReg.contains(Cons.current_vo)){
                    Cons.allUnReg.add(Cons.current_vo);
                    List<AcceptorDetailExlExpVo> l =  setUnReg();
                    AcceptorDetailUtil.exp(l);
                    Cons.allExp.addAll(l);
                }
            }
        }
    }

    /**
     * 检查验证码结果
     * **/
    public static void sliderCheck(JSONObject data){
        if(ConstUtil.isSuccess(data)){
            System.out.println(DateUtil.getDate()+"----"+Cons.current_vo+"--------验证码校验成功------------ " +JSON.toJSONString(data));
        }
    }


    /**
     * 滑动验证码
     * **/
    public static void slider(JSONObject data){
        if(ConstUtil.isSuccess(data)){
            System.out.println(DateUtil.getDate()+"-----"+Cons.current_vo+"-------<<<<slider200>>>>------------ ");
            JSONObject data1 = data.getJSONObject("data");
            VerifyImgVo vo = data1.toJavaObject(VerifyImgVo.class);
            try {
                Cons.bigImg= SaveDataImgUtil.saveToLocal(vo.getBigImg(),"/b1.jpg");
                Cons.tarImg= SaveDataImgUtil.saveToLocal(vo.getTarImg(),"/t1.jpg");

            } catch (Exception e) {
                System.err.println("slider报错----- 报错 "+Cons.current_vo);
            }
            System.out.println(DateUtil.getDate()+"------"+Cons.current_vo+"------验证码背景图------------ " + Cons.bigImg);
            System.out.println(DateUtil.getDate()+"------"+ Cons.current_vo+"------验证码滑块图------------ " + Cons.tarImg);
        }else{
            try {
                System.out.println(DateUtil.getDate()+"-------"+Cons.current_vo+"-------url_slider--没获取到验证码----重试----->>>>");
                //BrowserUtil.slideBtn();
            } catch (Exception e) {
                System.err.println(DateUtil.getDate()+"-----"+Cons.current_vo+"----slider--重试---- 次数 ");
            }
        }
    }





    /**
     * 查询详情(多页)
     * **/
    public static void findSettlePageMulti(JSONObject data){
        if(ConstUtil.isSuccess(data)){
            BatchSettleInfoPage pageVo = AcceptorDetailUtil.handleDetailMulti(data);

            System.out.println("fenye------"+pageVo.getCurrent()+"--pages----"+pageVo.getPages());
            if(pageVo.getCurrent()==pageVo.getPages()||0==pageVo.getPages()){
                Cons.allRegSuccess.add(Cons.current_vo);
                System.out.println("------处理成功-----"+Cons.current_vo);
            }else{
                if(pageVo.getPages()>1&&pageVo.getCurrent()<pageVo.getPages()){
                    int nextPage = pageVo.getCurrent()+1;
                    BrowserUtil.clickNextPage(nextPage);
                }
            }
            System.out.println(DateUtil.getDate()+"---"+Cons.current_vo+"------总"+
                    pageVo.getPages()+"页中第"+pageVo.getCurrent()+"页---详情查询成功---------"+JSON.toJSONString(data));
        }
    }

    public static List<AcceptorDetailExlExpVo> setUnReg(){
        String lastDay = "";
        if(StringUtils.isNotBlank(Cons.yearMonth)){
            lastDay = DateUtil.getLastOfMonth(Cons.yearMonth);
        }else{
            DateUtil.getLastOfMonth(DateUtil.getLastMonth());
        }
        AcceptorDetailExlExpVo v = new AcceptorDetailExlExpVo();
        v.setAcptName(Cons.current_vo.getName());
        v.setSoccode(Cons.current_vo.getAcptName());
        v.setGroupName(Cons.current_vo.getGroupName());
        v.setShowStatus(AcceptorStatusInSE.UN_REGIST.getCode());
        v.setShowDate(lastDay);
        List<AcceptorDetailExlExpVo> l = Lists.newArrayList();
        l.add(v);
        return l;
    }
}
