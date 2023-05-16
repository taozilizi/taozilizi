package taozi.util;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import taozi.resp.AcceptorBasicInfoData;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***获常组装*/
public class ConstUtil {

    /***获取excel名称*/
   /* public static String getExcelName(){

        return "/票据详情_"
                + VarConstant.NAME_VARIABLE
                + ".xlsx";
    }*/


    /**
     * 取差集（取存在一个集合中，但不存在于另外一个集合中的元素）
     * @return 存在A集合，不存在B集合的数据
     */
    public static Set<String> differenceSet(Set<String> a, Set<String> b){
        Set<String> chaJi = new HashSet<>(a);
        chaJi.removeAll(b);
        return chaJi;
    }
    /**写入到txt文件
     * content
     * path
     * **/
    public static void createTxt(String path,String content){
        if(StringUtils.isBlank(path)){
            path = "/log"+DateUtil.getDateYmdHms()+".txt";
        }
        FileUtil.writeUtf8String(content,path);
    }

    /**
     * 字符串是否包含中文
     *
     * @param str 待校验字符串
     * @return true 包含中文字符  false 不包含中文字符
     * @throws
     */
    public static boolean isContainChinese(String str) throws  Exception {

        if (StringUtils.isEmpty(str)) {
            throw new Exception("sms context is empty!");
        }
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
    //字符串是否包含中文
    public static boolean containChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
    public static int getRandom(){
       /* SimpleDateFormat simpleDateFormat;
        simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        String date=simpleDateFormat.format(new Date());*/
        Random random=new Random();
        int rannum= (int)(random.nextDouble()*(99-10 + 1))+ 10;
        // System.out.println(rannum);
        //String rand=date+rannum;
        return  rannum ;
    }

    /**判断接口是否成功返回
     * **/
    public static Boolean isSuccess( JSONObject data ){
        if(data!=null&&200==Integer.parseInt(String.valueOf(data.get("code")))){
            return true;
        }
        return false;
    }


    public static AcceptorBasicInfoData isZw(String acceptor, List<AcceptorBasicInfoData> list)
    {   AcceptorBasicInfoData v = null;
        if(ConstUtil.containChinese(acceptor)){//是中文查找就要完全匹配下拉框数据
            Boolean isFind = false;
            for (AcceptorBasicInfoData vo : list) {
                if (StringUtils.isNoneBlank(vo.getEntName())) {
                    //int i = vo.getEntName().indexOf("(");
                    //String rs = vo.getEntName().substring(0, i);
                    if (acceptor.equals(vo.getEntName())) {
                        System.out.println(DateUtil.getDate() + "-----"+acceptor+"----- 选中下拉列表中的-----");
                        v=vo;
                        isFind =true;
                        break;
                    }
                }
            }
            System.out.println(acceptor+"---是中文名称---"+isFind);

        }else{
            System.out.println(acceptor+"---是信用代码---");
            v= list.get(0);
        }
        return v;
    }
    public static void main(String[] args) {
        Set<String> setA = new HashSet<>();
        setA.add("1");setA.add("2");setA.add("3");setA.add("4");setA.add("5");
        Set<String> setB = new HashSet<>();
        setB.add("1");setB.add("5");setB.add("3");setB.add("7");setB.add("9");
        Set<String> rs = differenceSet(setA,setB);
        System.out.println(rs);
    }
}
