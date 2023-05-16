package taozi.resp.detail;

import lombok.Data;
import lombok.ToString;

/**
 * 第2个接口返回值
 * 详情
 *  第2个接口返回值
 *  第 1 层
 * */
@Data
@ToString
public class BillAcceptanceCreditInfo {
    private int code;
    private boolean success;
    private String message;
    private BillAcceptanceCreditInfoData data;
    public int getTotal(){
        return data.getBatchSettleInfoPage().getTotal();
    }
}
