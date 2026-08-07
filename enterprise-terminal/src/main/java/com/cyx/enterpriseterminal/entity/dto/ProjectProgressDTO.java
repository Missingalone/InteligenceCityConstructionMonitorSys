package com.cyx.enterpriseterminal.entity.dto;
import jakarta.validation.constraints.*; import lombok.Data; import java.math.BigDecimal; import java.time.LocalDate;
/** 企业上报项目进度请求。 */
@Data public class ProjectProgressDTO { @NotNull @DecimalMin("0.00") @DecimalMax("100.00") private BigDecimal progressPercent; private LocalDate actualStartDate; }
