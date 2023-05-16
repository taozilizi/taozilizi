package taozi;

import taozi.constant.Cons;
import taozi.util.AcceptorDetailUtil;

public class Start {
    public static void main(String[] args) throws Exception {
        Cons.yearMonth = "2022-02";//不设置值默认查上个月
        AcceptorDetailUtil.imp("d:/1.xlsx");
    }
}
