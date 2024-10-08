package com.sparta.schedulemanager.repository;

import com.sparta.schedulemanager.entity.CustomEntity;
import com.sparta.schedulemanager.entity.Schedule;
import com.sparta.schedulemanager.utility.ProjectProtocol;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;


public class ScheduleRepository<T extends CustomEntity> extends BaseRepository<T> {
    // Schedule ID에 대한 정보를 조회하는 함수
    public Schedule getScheduleById(JdbcTemplate jdbcTemplate, Integer id) {
        return findScheduleById(jdbcTemplate, id);
    }

    // ID정보를 토대로 DataBase 조회하는 함수
    public Schedule findScheduleById(JdbcTemplate jdbcTemplate, Integer id) {
        String getQuery = queryUtility.getGetByColumnDataQuery(ProjectProtocol.TABLE_SCHEDULE, ProjectProtocol.TABLE_COLUMN_ID, String.valueOf(id));

        return jdbcTemplate.query(getQuery, resultSet-> {
            if(resultSet.next()) {
                return new Schedule(resultSet);
            } else {
                return null;
            }
        });
    }

    // 특정 일자를 기준으로 DataBase를 조회하는 함수
    public List<Schedule> getSchedulesByDate(JdbcTemplate jdbcTemplate, String date) {
        // 수정 일자를 기준으로 조회
        String getSchedulesQuery = queryUtility.getGetByColumnDateQuery(ProjectProtocol.TABLE_SCHEDULE, ProjectProtocol.TABLE_COLUMN_EDITTIME, date);

        return jdbcTemplate.query(getSchedulesQuery, (rs, rowNum) -> new Schedule(rs));
    }

    // ID에 해당하는 스케쥴을 수정하는 함수
    public Schedule editScheduleData(JdbcTemplate jdbcTemplate, Integer id, Schedule schedule) throws NoSuchFieldException, IllegalAccessException {
        // 갱신해야 하는 항목
        String[] updateItems = {"todo", "managerName", "editTime"};
        String editScheduleQuery = queryUtility.getUpdateQuery(ProjectProtocol.TABLE_SCHEDULE, updateItems, schedule, id);

        id = jdbcTemplate.update(editScheduleQuery);
        return findScheduleById(jdbcTemplate, id);
    }

}
