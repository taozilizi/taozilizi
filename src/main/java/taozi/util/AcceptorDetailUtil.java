package taozi.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import taozi.constant.Cons;
import taozi.vo.AcceptorDetailExlExpVo;
import taozi.vo.DataHandleVo;
import com.github.wycm.biz.constant.AcceptorStatusInSE;
import taozi.resp.detail.BatchSettleInfoPage;
import taozi.resp.detail.BillAcceptanceCreditInfoRecord;
import taozi.resp.detail.EntListInfoVo;
import taozi.vo.AcceptorExlImpVo;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * */
public class AcceptorDetailUtil {
    /**解析详情接口返回的json*/
    public static DataHandleVo dataHandle(JSONObject l1){
        System.out.println("------------------详情接口------------------");
        System.out.println("详情接口------"+ JSON.toJSONString(l1));
        System.out.println("------------------------详情接口------------");
        //第2层
        JSONObject l2 = l1.getJSONObject("data");

        //第3层
        JSONObject l31 = l2.getJSONObject("batchSettleInfoPage");//包含详情分页列表的对象
        BatchSettleInfoPage listVo = l31.toJavaObject(BatchSettleInfoPage.class);

        JSONObject l32 = l2.getJSONObject("entListInfoVo");//基本信息
        EntListInfoVo baseVo = l32.toJavaObject(EntListInfoVo.class);

        //第4层
        JSONArray l4 = l31.getJSONArray("records");//详情分页列表

        List<BillAcceptanceCreditInfoRecord> list = l4.toJavaList(BillAcceptanceCreditInfoRecord.class);

        String lastDay = "";
        if(StringUtils.isNotBlank(Cons.yearMonth)){
            lastDay = DateUtil.getLastOfMonth(Cons.yearMonth);
        }else{
            DateUtil.getLastOfMonth(DateUtil.getLastMonth());
        }

        List<AcceptorDetailExlExpVo> data = new ArrayList<>();
        if(CollectionUtil.isEmpty(list)){
            AcceptorDetailExlExpVo entity = new AcceptorDetailExlExpVo();
            entity.setBaseInfo(baseVo);
            entity.setGroupName(Cons.current_vo.getGroupName());
            entity.setShowStatus(AcceptorStatusInSE.UN_PUBLISH.getCode());
            entity.setShowDate(lastDay);
            data.add(entity);
        }else{
            for (BillAcceptanceCreditInfoRecord v:list) {
                AcceptorDetailExlExpVo entity = new AcceptorDetailExlExpVo();
                BeanUtil.copyProperties(v,entity);
                entity.setBaseInfo(baseVo);
                entity.setGroupName(Cons.current_vo.getGroupName());
                if(StringUtils.isBlank(v.getShowDate())){
                    entity.setShowDate(lastDay);
                }
                data.add(entity);
            }
        }
        System.out.println("----------------详情接口--处理完成----------");
        return DataHandleVo.builder().data(data).baseVo(baseVo).listVo(listVo).build();
    }


    /**把处理返回数据(多页)*/
    public static BatchSettleInfoPage handleDetailMulti(JSONObject l1){
        DataHandleVo vo = dataHandle(l1);
        Boolean rs = exp(vo.getData());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(rs){
            Cons.allExp.addAll(vo.getData());
        }
        System.out.println("handleDetailMulti------"+Cons.current_vo);
        return vo.getListVo();
    }

    /**把数据导出到excel*/
    public static  Boolean exp(List<AcceptorDetailExlExpVo> data){
       try {
           //追加方式写入excel
           File file = new File("/票据详情_"+ Cons.yearMonth + ".xlsx");
           File tempFile = new File("/temp.xlsx");
           if (file.exists()){
               // 第二次按照原有格式，不需要表头，追加写入
               EasyExcel.write(file,AcceptorDetailExlExpVo.class).needHead(false).
                       withTemplate(file).file(tempFile).sheet().doWrite(data);
           }else {
               // 第一次写入需要表头
               EasyExcel.write(file,AcceptorDetailExlExpVo.class).sheet().doWrite(data);
           }

           if (tempFile.exists()){
               file.delete();
               tempFile.renameTo(file);
           }
       }catch (Exception e){
           System.err.println("excel导出exp报错");
           return false;
       }
       System.out.println("excel导出完成------"+Cons.current_vo);
       return true;
    }

    /**导入到excel*/
    public static  void imp(String fileName)   {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, AcceptorExlImpVo.class, new ExcelListener()).sheet().doRead();

    }
}
