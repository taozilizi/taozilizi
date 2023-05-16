package taozi.resp;

import lombok.Data;
/**
 * 第1个接口返回值
 * **/
@Data
public class AcceptorBasicInfoData {
    private String acptName;
    private String dimAcptBranchName;
    private String dimAcptBranchId;
    private String acptCode;
    private String entName;
    private String entAccount;
    private String soccode;
}
