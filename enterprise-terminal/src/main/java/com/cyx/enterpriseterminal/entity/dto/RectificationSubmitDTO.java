package com.cyx.enterpriseterminal.entity.dto;
import jakarta.validation.constraints.NotBlank; import lombok.Data;
/** 企业提交整改结果和 JSON 佐证地址请求。 */
@Data public class RectificationSubmitDTO { @NotBlank private String resultDescription; private String evidenceUrls; }
