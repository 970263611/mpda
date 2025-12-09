package com.dahuaboke.mpda.bot.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dahuaboke.mpda.bot.monitor.entity.MonitorEventEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

@Mapper
public interface BrMonitorMapper extends BaseMapper<MonitorEventEntity> {

    Cursor<MonitorEventEntity> streamMonitorEvent(@Param("begin") String begin, @Param("end") String end);
}
