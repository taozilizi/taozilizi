package taozi.util;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import taozi.constant.Cons;
import taozi.vo.AcceptorExlImpVo;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

//创建读取excel监听器
public class ExcelListener extends AnalysisEventListener<AcceptorExlImpVo> {

    //创建list集合封装最终的数据
    List<AcceptorExlImpVo> list = new ArrayList<>();

    //一行一行去读取excle内容
    @Override
    public void invoke(AcceptorExlImpVo user, AnalysisContext analysisContext) {
        if(StringUtils.isNoneBlank(user.getAcptName())){
            list.add(user);
        }
    }

    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息："+headMap);
    }

    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("一共导入了多少数据--- "+list.size());
        Cons.allImp = list.stream().map(v -> {return v;}).collect(Collectors.toSet());
        System.out.println("一共---多少数据--- "+ Cons.allImp.size());
        //循环查找所有数据
        handleRegisterList(list);
        //检查未成功数据
        Set<AcceptorExlImpVo> unSucess = findUnSuccess();

        if(!CollectionUtil.isEmpty(unSucess)){
            Set<AcceptorExlImpVo> rsSet = null;
            int time = 1;
            do{
                time++;
                rsSet = handleErr(unSucess,time);//第二次，重新开始查询
            }while (!CollectionUtil.isEmpty(rsSet));
        }

        System.out.println("所有导出------"+Cons.allExp.size());
        System.out.println("所有导入---"+Cons.allImp.size());
        System.out.println("所有注册"+Cons.allReg.size());
        System.out.println("所有注册导出成功"+Cons.allRegSuccess.size());
        System.out.println("所有未注册"+Cons.allUnReg.size());
    }
    /**操作浏览器查询已注册详情数据**/
    public void handleRegisterList(List<AcceptorExlImpVo> list) {

        for (AcceptorExlImpVo vo :list) {
            if (StringUtils.isNoneBlank(vo.getAcptName())) {
                try {
                    try {
                        Cons.current_vo=vo;
                        BrowserUtil.invoke(vo);
                    } catch (Exception e) {
                        System.err.println(e);
                        System.err.println(DateUtil.getDate()+"invoke1报错:"+vo.getAcptName());
                        //是已注册，但是已导出不包含
                        System.out.println("--已注册包含---"+Cons.allReg.contains(vo)+"--已注册导出包含---"+Cons.allRegSuccess.contains(vo));
                        if(Cons.allReg.contains(vo)&&!Cons.allRegSuccess.contains(vo)){
                            BrowserUtil.invoke(Cons.current_vo);
                        }else{
                            continue;
                        }
                    }
                    Thread.sleep(ConstUtil.getRandom());
                } catch (Exception e) {
                    System.err.println(DateUtil.getDate()+"invoke222报错"+Cons.current_vo);
                }
            }
        }
    }

    /**处理上次未成功查询的数据, 再次去查询**/
    public Set<AcceptorExlImpVo> handleErr(Set<AcceptorExlImpVo> errSet,int time){
        System.out.println("第"+time+"次需要查询的数据------->"+errSet.size());
        for (AcceptorExlImpVo s:errSet) {
            try {
                Cons.current_vo=s;
                BrowserUtil.invoke(s);
                Thread.sleep(ConstUtil.getRandom());
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        //检查未成功数据
        Set<AcceptorExlImpVo> unSucess = findUnSuccess();
        System.out.println("第"+time+"次 查询的结果------->"+unSucess.size());
        return unSucess;
    }
    public Set<AcceptorExlImpVo> findUnSuccess(){
        //排除成功查询详情的已注册，收集未成功查询的再去循环查询-->handleErr
        Set<AcceptorExlImpVo> unSucess = new HashSet<>();
        for (AcceptorExlImpVo v:Cons.allImp) {
            if(!Cons.allRegSuccess.contains(v)&&!Cons.allUnReg.contains(v)){
                unSucess.add(v);
            }
        }
        return unSucess;
    }
    /**导出未注册**/
    public void expUnReg(){
        /*if(StringUtils.isBlank(VarConstant.yearMonth)){
            VarConstant.yearMonth = DateUtil.getLastMonth();
        }
        String lastDay = DateUtil.getLastOfMonth(VarConstant.yearMonth);
        String fileName2 = "/"+VarConstant.yearMonth+"未注册_"+DateUtil.getDateYmdHms()+".xlsx";

        List<AcceptorDetailExlExpVo> unregList= Lists.newArrayList();
        for (AcceptorExlImpVo s: AcceptorBaseUtil.rightEmptySet) {
            AcceptorDetailExlExpVo vo = new AcceptorDetailExlExpVo();
            vo.setAcptName(s.getName());
            vo.setGroupName(s.getGroupName());
            vo.setShowStatus(AcceptorStatusInSE.UN_REGIST.getCode());
            vo.setShowDate(lastDay);
            unregList.add(vo);
        }
        AcceptorBaseUtil.exp(unregList,fileName2);*/
    }
}

