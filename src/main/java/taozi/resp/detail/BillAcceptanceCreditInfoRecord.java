package taozi.resp.detail;

import lombok.Data;

/**
 *
 *   *  *
 *  *  *  *  第2个接口返回值
 *  *  *  *  第  4  层
 * **/
@Data
public class BillAcceptanceCreditInfoRecord {
    private String id;
    private String acptName;
    private String acptCode;
    private String dimAcptBranchName;
    private String dimAcptBranchId;
    private String acptAmount;
    private String acptOver;
    private String totalOverdueAmount;
    private String overdueOver;
    private String showDate;
    private String relDate;
    private String remark;
    private String entRemark;
    private String entDetailRemark;
    private String showStatus;
    private String billAttr;
}
