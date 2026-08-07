package com.cyx.publicterminal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyx.publicterminal.entity.po.BizPublicFeedback;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公众反馈写入数据访问。
 */
@Mapper
public interface PublicFeedbackMapper extends BaseMapper<BizPublicFeedback> {
}
