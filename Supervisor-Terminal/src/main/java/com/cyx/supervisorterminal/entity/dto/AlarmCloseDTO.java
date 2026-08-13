package com.cyx.supervisorterminal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 监管人员关闭已解决告警时提交的说明。
 */
@Data
public class AlarmCloseDTO {
    @NotBlank(message = "关闭说明不能为空")
    @Size(max = 500, message = "关闭说明不能超过500个字符")
    private String closeRemark;
}
