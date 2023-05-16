package taozi.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import taozi.resp.detail.EntListInfoVo;
import taozi.util.DateUtil;
import lombok.Data;

/**
 * excel导出
 *
 * 详情
 * 查询的信息
 *entListInfoVo总对象企业信息
 * batchSettleInfoPage分页详情
 * 保存详情信息到excel
 * **/
@Data
public class AcceptorDetailExlExpVo {
    @ExcelProperty("名称")
    private String acptName;
    @ExcelProperty("所属集团")
    private String groupName;
    @ExcelProperty("统一社会信用代码")
    private String soccode;//entListInfoVo
    @ExcelProperty("机构类别")
    private String acptOrgType;//entListInfoVo
    @ExcelProperty("注册日期")
    private String createTime;//entListInfoVo
    @ExcelProperty("披露信息时点日期")
    private String showDate;
    @ExcelProperty("开户机构名称")
    private String dimAcptBranchName;
    @ExcelProperty("累计发生额（元）")
    private String acptAmount;
    @ExcelProperty("余额（元）")
    private String acptOver;
    @ExcelProperty("累计逾期发生额（元）")
    private String totalOverdueAmount;
    @ExcelProperty("逾期余额（元）")
    private String overdueOver;
    @ExcelProperty("披露日期")
    private String relDate;
    @ExcelProperty("系统备注")
    private String remark;
    @ExcelProperty("是否披露")
    private String showStatus;
    @ExcelProperty("写入时间")
    private String createDate;

    public AcceptorDetailExlExpVo setBaseInfo(EntListInfoVo baseVo){
        this.setAcptName(baseVo.getEntName());
        this.setSoccode(baseVo.getSoccode());
        this.setCreateTime(baseVo.getCreateTime());
        this.setAcptOrgType(baseVo.getAcptOrgType());
        this.setCreateDate(DateUtil.getDate());
        return this;
    }
}
