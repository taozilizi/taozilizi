package taozi.vo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VerifyImgVo {
    /**验证码背景图*/
    private String bigImg;
    /**验证码移动缺口图*/
    private String tarImg;
    private String offset;
    private String sessionId;
}
