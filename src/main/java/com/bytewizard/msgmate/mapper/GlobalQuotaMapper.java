package com.bytewizard.msgmate.mapper;

import com.bytewizard.msgmate.model.GlobalQuotaModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GlobalQuotaMapper {

    void save(@Param("globalQuotaModel") GlobalQuotaModel globalQuotaModel);

    GlobalQuotaModel getGlobalQuota(@Param("channel") int channel );
}
