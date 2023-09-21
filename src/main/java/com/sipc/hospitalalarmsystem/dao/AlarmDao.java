package com.sipc.hospitalalarmsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sipc.hospitalalarmsystem.controller.AlarmController;
import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.RealTimeAlarmRes;
import com.sipc.hospitalalarmsystem.model.dto.res.Alarm.SqlGetAlarmRes;
import com.sipc.hospitalalarmsystem.model.po.Alarm.Alarm;
import com.sipc.hospitalalarmsystem.model.po.Alarm.AlarmCaseTypeTotal;
import com.sipc.hospitalalarmsystem.model.po.Alarm.AlarmTotal;
import com.sipc.hospitalalarmsystem.model.po.Alarm.TimePeriod;
import icu.mhb.mybatisplus.plugln.base.mapper.JoinBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-11 19:29
 */
@Mapper
public interface AlarmDao extends BaseMapper<Alarm> {


    @Select("SELECT\n" +
            "  SUM(total) AS total,\n" +
            "  SUM(today_new) AS today_new,\n" +
            "  SUM(today_new) - SUM(yesterday_new) AS day_change\n" +
            "FROM\n" +
            "(\n" +
            "  SELECT \n" +
            "    COUNT(*) AS total,\n" +
            "    SUM(CASE WHEN DATE(create_time) = CURDATE() THEN 1 ELSE 0 END) AS today_new,\n" +
            "    SUM(CASE WHEN DATE(create_time) = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN 1 ELSE 0 END) AS yesterday_new\n" +
            "  FROM alarm_info\n" +
            ") t")
    AlarmTotal SqlGetAlarmTotal();

    @Select("SELECT\n" +
            "  c.case_type,\n" +
            "  SUM(CASE WHEN DATE(a.create_time) = CURDATE() THEN 1 ELSE 0 END) AS today_new,\n" +
            "  COUNT(*) AS total\n" +
            "FROM alarm_info a \n" +
            "JOIN case_type_info c ON a.case_type = c.id\n" +
            "GROUP BY c.case_type")
    List<AlarmCaseTypeTotal> SqlGetAlarmCaseTypeTotal();

    @Select("SELECT\n" +
            "\tmonitor.area, \n" +
            "\tcase_type_info.case_type, \n" +
            "\talarm_info.id, \n" +
            "\talarm_info.clip_link, \n" +
            "\talarm_info.warning_level, \n" +
            "\talarm_info.create_time, \n" +
            "\talarm_info.`status`, \n" +
            "\talarm_info.processing_content, \n" +
            "\tmonitor.`name`\n" +
            "FROM\n" +
            "\talarm_info\n" +
            "\tINNER JOIN\n" +
            "\tmonitor\n" +
            "\tON \n" +
            "\t\talarm_info.monitor_id = monitor.id\n" +
            "\tINNER JOIN\n" +
            "\tcase_type_info\n" +
            "\tON \n" +
            "\t\talarm_info.case_type = case_type_info.id\n" +
            "WHERE\n" +
            "\talarm_info.id = #{alarmId}\n")
    SqlGetAlarmRes SqlGetAlarm(@Param("alarmId") Integer alarmId);

    @Select("SELECT CASE WHEN HOUR ( create_time ) >= 0 AND HOUR ( create_time ) < 3 THEN '03:00' WHEN HOUR ( create_time ) >= 3 AND HOUR ( create_time ) < 6 THEN '06:00' WHEN HOUR ( create_time ) >= 6 AND HOUR ( create_time ) < 9 THEN '09:00' WHEN HOUR ( create_time ) >= 9 AND HOUR ( create_time ) < 12 THEN '12:00' WHEN HOUR ( create_time ) >= 12 AND HOUR ( create_time ) < 15 THEN '15:00' WHEN HOUR ( create_time ) >= 15 AND HOUR ( create_time ) < 18 THEN '18:00' WHEN HOUR ( create_time ) >= 18 AND HOUR ( create_time ) < 21 THEN '21:00' WHEN HOUR ( create_time ) >= 21 AND HOUR ( create_time ) < 24 THEN '24:00' END AS period, COUNT(*) AS cnt FROM alarm_info WHERE DATE ( create_time ) = #{date} GROUP BY period ORDER BY MIN( HOUR ( create_time ));")
    List<TimePeriod> SqlGetDayHistoryCnt(@Param("date") String date);

    @Select("SELECT \n" +
            "\tDATE(create_time) AS period,\n" +
            "    COUNT(*) AS cnt\n" +
            "FROM alarm_info  \n" +
            "WHERE DATE(create_time) BETWEEN DATE_SUB(#{date}, INTERVAL 2 DAY) AND #{date}\n" +
            "GROUP BY DATE(create_time)\n" +
            "ORDER BY MIN(period)")
    List<TimePeriod> SqlGetThreeDaysHistoryCnt(@Param("date") String date);

    @Select("SELECT \n" +
            "\tDATE(create_time) AS period,\n" +
            "    COUNT(*) AS cnt\n" +
            "FROM alarm_info  \n" +
            "WHERE DATE(create_time) BETWEEN DATE_SUB(#{date}, INTERVAL 6 DAY) AND #{date}\n" +
            "GROUP BY DATE(create_time)\n" +
            "ORDER BY MIN(period)")
    List<TimePeriod> SqlGetWeekHistoryCnt(@Param("date") String date);

    @Select("SELECT\n" +
            "  m.area AS period,\n" +
            "  COUNT(*) AS cnt \n" +
            "FROM alarm_info a\n" +
            "LEFT JOIN monitor m ON a.monitor_id = m.id\n" +
            "WHERE DATE(a.create_time) = #{date}\n" +
            "GROUP BY period")
    List<TimePeriod> SqlGetAreasDayHistoryCnt(@Param("date") String date);

    @Select("SELECT\n" +
            "  m.area AS period,\n" +
            "  COUNT(*) AS cnt \n" +
            "FROM alarm_info a\n" +
            "LEFT JOIN monitor m ON a.monitor_id = m.id\n" +
            "WHERE DATE(a.create_time) BETWEEN DATE_SUB(#{date}, INTERVAL 2 DAY) AND #{date}\n" +
            "GROUP BY period")
    List<TimePeriod> SqlGetAreasThreeDaysHistoryCnt(@Param("date") String date);


    @Select("SELECT\n" +
            "  m.area AS period,\n" +
            "  COUNT(*) AS cnt \n" +
            "FROM alarm_info a\n" +
            "LEFT JOIN monitor m ON a.monitor_id = m.id\n" +
            "WHERE DATE(a.create_time) BETWEEN DATE_SUB(#{date}, INTERVAL 6 DAY) AND #{date}\n" +
            "GROUP BY period")
    List<TimePeriod> SqlGetAreasWeekHistoryCnt(@Param("date") String date);
}
