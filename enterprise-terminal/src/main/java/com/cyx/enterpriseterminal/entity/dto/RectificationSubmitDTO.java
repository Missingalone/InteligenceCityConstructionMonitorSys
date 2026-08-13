package com.cyx.enterpriseterminal.entity.dto;
import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.Size; import lombok.Data;
/** 企业提交整改结果和 JSON 佐证地址请求。 */
@Data public class RectificationSubmitDTO {
    @NotBlank
    @Size(max = 4000)
    private String resultDescription;

    // 佐证地址由文件服务生成，本接口限制长度，避免超大请求直接进入数据库。
    @Size(max = 4000)
    private String evidenceUrls;
}
