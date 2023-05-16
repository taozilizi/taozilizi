package taozi.resp.detail;

import lombok.Data;
import lombok.ToString;

/**
 *  *  第2个接口返回值
 *  *  第  2  层
 * */
@Data
@ToString
public class BillAcceptanceCreditInfoData {
    private BatchSettleInfoPage batchSettleInfoPage;
    private EntListInfoVo entListInfoVo;
}
