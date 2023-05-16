package taozi.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.ToString;

/**
 * excel导入
 *
 * 详情
 * 查询的信息
 *entListInfoVo总对象企业信息
 * batchSettleInfoPage分页票据信息详情
 * 保存详情信息到excel
 * **/
@ToString
public class AcceptorExlImpVo {
    @ExcelProperty(index = 0)
    private String name;//名称
    @ExcelProperty(index = 2)
    private String acptName;//社会信用代码或者名称
    @ExcelProperty(index = 3)
    private String groupName;//所属集团名称

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAcptName() {
        return acptName;
    }

    public void setAcptName(String acptName) {
        this.acptName = acptName;
    }
}
