package taozi.vo;

import taozi.resp.detail.BatchSettleInfoPage;
import taozi.resp.detail.EntListInfoVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataHandleVo {

    public BatchSettleInfoPage listVo;
    public EntListInfoVo baseVo;

    public List<AcceptorDetailExlExpVo> data = new ArrayList<>();
    /**是否导入成功*/
    public Boolean impSuccess;

}
