package com.sipc.hospitalalarmsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sipc.hospitalalarmsystem.model.po.Alarm.SqlGetAlarm;
import com.sipc.hospitalalarmsystem.model.po.Alarm.Alarm;
import com.sipc.hospitalalarmsystem.model.po.Alarm.AlarmCaseTypeTotal;
import com.sipc.hospitalalarmsystem.model.po.Alarm.AlarmTotal;
import com.sipc.hospitalalarmsystem.model.po.Alarm.TimePeriod;
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

//    @Select("<script>" +
//            "SELECT monitor.area,\n" +
//            "    case_type_info.case_type_name,\n" +
//            "    alarm_info.id,\n" +
//            "    alarm_info.clip_link,\n" +
//            "    alarm_info.warning_level,\n" +
//            "    alarm_info.create_time,\n" +
//            "    alarm_info.`status`,\n" +
//            "    alarm_info.processing_content,\n" +
//            "    monitor.`name` FROM alarm_info\n" +
//            "    INNER JOIN monitor ON alarm_info.monitor_id = monitor.id \n" +
//            "    INNER JOIN case_type_info ON alarm_info.case_type = case_type_info.id " +
//            "WHERE 1=1 " +
//            "<if test='caseType != null'>AND CASE_TYPE = #{caseType}</if>" +
//            "<if test='status != null'>AND STATUS = #{status}</if>" +
//            "<if test='warningLevel != null'>AND WARNING_LEVEL = #{warningLevel}</if>" +
//            "<if test='time1 != null and time2 != null'>AND CREATE_TIME BETWEEN #{time1} AND #{time2}</if>" +
//            "<if test='time1 != null and time2 == null'>AND CREATE_TIME >= #{time1}</if>" +
//            "<if test='time1 == null and time2 != null'>AND CREATE_TIME &lt;= #{time2}</if>" +
//            "ORDER BY alarm_info.warning_level DESC, alarm_info.create_time DESC, alarm_info.id DESC\n" +  // Here is the sorting clause
//            "LIMIT #{pageOffset}, #{pageSize}" +
//            "</script>")
@Select("<script>" +
        "SELECT monitor.area,\n" +
        "    case_type_info.case_type_name,\n" +
        "    alarm_info.id,\n" +
        "    alarm_info.clip_link,\n" +
        "    alarm_info.warning_level,\n" +
        "    alarm_info.create_time,\n" +
        "    alarm_info.`status`,\n" +
        "    alarm_info.processing_content,\n" +
        "    monitor.`name`,\n" +
        "    user_info.phone AS phone FROM alarm_info\n" + // Added leader's phone
        "    INNER JOIN monitor ON alarm_info.monitor_id = monitor.id \n" +
        "    INNER JOIN case_type_info ON alarm_info.case_type = case_type_info.id \n" +
        "    INNER JOIN user_info ON monitor.leader = user_info.user_name " + // Join user_info to get the phone based on the leader id
        "WHERE 1=1 " +
        "<if test='caseType != null'>AND CASE_TYPE = #{caseType}</if>" +
        "<if test='status != null'>AND STATUS = #{status}</if>" +
        "<if test='warningLevel != null'>AND WARNING_LEVEL = #{warningLevel}</if>" +
        "<if test='time1 != null and time2 != null'>AND CREATE_TIME BETWEEN #{time1} AND #{time2}</if>" +
        "<if test='time1 != null and time2 == null'>AND CREATE_TIME >= #{time1}</if>" +
        "<if test='time1 == null and time2 != null'>AND CREATE_TIME &lt;= #{time2}</if>" +
        "ORDER BY alarm_info.warning_level DESC, alarm_info.create_time DESC, alarm_info.id DESC\n" +  // Here is the sorting clause
        "LIMIT #{pageOffset}, #{pageSize}" +
        "</script>")
    List<SqlGetAlarm> selectByCondition(@Param("pageOffset") Integer pageOffset,
                                        @Param("pageSize") Integer pageSize,
                                        @Param("caseType") String caseType,
                                        @Param("status") String status,
                                        @Param("warningLevel") String warningLevel,
                                        @Param("time1") String time1,
                                        @Param("time2") String time2);


    @Select("""
            SELECT
              SUM(total) AS total,
              SUM(today_new) AS today_new,
              SUM(today_new) - SUM(yesterday_new) AS day_change
            FROM
            (
              SELECT\s
                COUNT(*) AS total,
                SUM(CASE WHEN DATE(create_time) = CURDATE() THEN 1 ELSE 0 END) AS today_new,
                SUM(CASE WHEN DATE(create_time) = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN 1 ELSE 0 END) AS yesterday_new
              FROM alarm_info
            ) t""")
    AlarmTotal SqlGetAlarmTotal();

    @Select("""
            SELECT
              c.case_type_name,
              SUM(CASE WHEN DATE(a.create_time) = CURDATE() THEN 1 ELSE 0 END) AS today_new,
              COUNT(a.case_type) AS total
            FROM case_type_info c\s
            LEFT JOIN alarm_info a ON c.id = a.case_type
            GROUP BY c.case_type_name
            """)
    List<AlarmCaseTypeTotal> SqlGetAlarmCaseTypeTotal();

    @Select("""
            SELECT
            \tmonitor.area,\s
            \tcase_type_info.case_type_name,\s
            \talarm_info.id,\s
            \talarm_info.clip_link,\s
            \talarm_info.warning_level,\s
            \talarm_info.create_time,\s
            \talarm_info.`status`,\s
            \talarm_info.processing_content,\s
            \tmonitor.`name`
            FROM
            \talarm_info
            \tINNER JOIN
            \tmonitor
            \tON\s
            \t\talarm_info.monitor_id = monitor.id
            \tINNER JOIN
            \tcase_type_info
            \tON\s
            \t\talarm_info.case_type = case_type_info.id
            WHERE
            \talarm_info.id = #{alarmId}
            """)
    SqlGetAlarm SqlGetAlarm(@Param("alarmId") Integer alarmId);

    @Select("SELECT CASE WHEN HOUR ( create_time ) >= 0 AND HOUR ( create_time ) < 3 THEN '03:00' WHEN HOUR ( create_time ) >= 3 AND HOUR ( create_time ) < 6 THEN '06:00' WHEN HOUR ( create_time ) >= 6 AND HOUR ( create_time ) < 9 THEN '09:00' WHEN HOUR ( create_time ) >= 9 AND HOUR ( create_time ) < 12 THEN '12:00' WHEN HOUR ( create_time ) >= 12 AND HOUR ( create_time ) < 15 THEN '15:00' WHEN HOUR ( create_time ) >= 15 AND HOUR ( create_time ) < 18 THEN '18:00' WHEN HOUR ( create_time ) >= 18 AND HOUR ( create_time ) < 21 THEN '21:00' WHEN HOUR ( create_time ) >= 21 AND HOUR ( create_time ) < 24 THEN '24:00' END AS period, COUNT(*) AS cnt FROM alarm_info WHERE DATE ( create_time ) = #{date} GROUP BY period ORDER BY MIN( HOUR ( create_time ));")
    List<TimePeriod> SqlGetDayHistoryCnt(@Param("date") String date);

    @Select("""
            SELECT\s
            \tDATE(create_time) AS period,
                COUNT(*) AS cnt
            FROM alarm_info \s
            WHERE DATE(create_time) BETWEEN DATE_SUB(#{date}, INTERVAL 2 DAY) AND #{date}
            GROUP BY DATE(create_time)
            ORDER BY MIN(period)""")
    List<TimePeriod> SqlGetThreeDaysHistoryCnt(@Param("date") String date);

    @Select("""
            SELECT\s
            \tDATE(create_time) AS period,
                COUNT(*) AS cnt
            FROM alarm_info \s
            WHERE DATE(create_time) BETWEEN DATE_SUB(#{date}, INTERVAL 6 DAY) AND #{date}
            GROUP BY DATE(create_time)
            ORDER BY MIN(period)""")
    List<TimePeriod> SqlGetWeekHistoryCnt(@Param("date") String date);

    @Select("""
            SELECT
                    m.area AS period,
                    COUNT(a.monitor_id) AS cnt
                FROM monitor m
                LEFT JOIN alarm_info a ON m.id = a.monitor_id AND DATE(a.create_time) = #{date}
                GROUP BY m.area""")
    List<TimePeriod> SqlGetAreasDayHistoryCnt(@Param("date") String date);

    @Select("""
    SELECT
        m.area AS period,
        COUNT(a.monitor_id) AS cnt
    FROM monitor m
    LEFT JOIN alarm_info a ON m.id = a.monitor_id AND DATE(a.create_time) BETWEEN DATE_SUB(#{date}, INTERVAL 2 DAY) AND #{date}
    GROUP BY m.area""")
    List<TimePeriod> SqlGetAreasThreeDaysHistoryCnt(@Param("date") String date);



    @Select("""
    SELECT
        m.area AS period,
        COUNT(a.monitor_id) AS cnt
    FROM monitor m
    LEFT JOIN alarm_info a ON m.id = a.monitor_id AND DATE(a.create_time) BETWEEN DATE_SUB(#{date}, INTERVAL 6 DAY) AND #{date}
    GROUP BY m.area""")
    List<TimePeriod> SqlGetAreasWeekHistoryCnt(@Param("date") String date);


    @Select("""
    SELECT
        case_type_info.case_type_name as period,
        COUNT(alarm_info.case_type) AS cnt
    FROM case_type_info
    LEFT JOIN alarm_info ON case_type_info.id = alarm_info.case_type AND DATE(alarm_info.create_time) = #{date}
    GROUP BY case_type_info.case_type_name""")
    List<TimePeriod> SqlGetCaseTypesDayHistoryCnt(@Param("date") String date);

    @Select("""
    SELECT
        case_type_info.case_type_name as period,
        COUNT(alarm_info.case_type) AS cnt
    FROM case_type_info
    LEFT JOIN alarm_info ON case_type_info.id = alarm_info.case_type AND DATE(alarm_info.create_time) BETWEEN DATE_SUB(#{date}, INTERVAL 2 DAY) AND #{date}
    GROUP BY case_type_info.case_type_name""")
    List<TimePeriod> SqlGetCaseTypesThreeDaysHistoryCnt(@Param("date") String date);


    @Select("""
    SELECT
        case_type_info.case_type_name as period,
        COUNT(alarm_info.case_type) AS cnt
    FROM case_type_info
    LEFT JOIN alarm_info ON case_type_info.id = alarm_info.case_type AND DATE(alarm_info.create_time) BETWEEN DATE_SUB(#{date}, INTERVAL 6 DAY) AND #{date}
    GROUP BY case_type_info.case_type_name""")
    List<TimePeriod> SqlGetCaseTypesWeekHistoryCnt(@Param("date") String date);

}
