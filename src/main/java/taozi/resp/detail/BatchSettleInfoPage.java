package taozi.resp.detail;

import lombok.Data;

import java.util.List;

/**
 *(分页查询)
 *  *  *  *详情
 *  *  *  第2个接口返回值
 *  *  *  第 3 层
 * **/
@Data
public class BatchSettleInfoPage {
    private List<BillAcceptanceCreditInfoRecord> records;
    private int total;
    private int size;
    private int current;
    private Object orders;//[]数组
    private boolean searchCount;
    private int pages;
}
