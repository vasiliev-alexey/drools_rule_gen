package com.av.dao;

import com.av.domain.Condition;
import com.av.domain.Event;
import com.av.domain.EventSetting;
import com.av.domain.SegmentSetting;
import com.av.utils.ConvertAbstractValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by alexey on 17.10.15.
 */
@Repository
public class EventDAO {

    @Autowired
    private NamedParameterJdbcTemplate template;
    @Autowired
    private DocumentDAO documentDAO;
    @Autowired
    private ConditionDao conditionDao;
    @Autowired
    private AbstractValueDAO abstractValueDAO;

    private final String FIND_ALL = "select e.id , e.code , e.doc_id, e.name  , d.name as doc_name  , " +
            "enabled_flag from event e , document d" +
            " where d.doc_id = e.doc_id";

    private final String FIND_BY_ID = FIND_ALL + " and e.id =:ID";

    private final String FIND_ALL_ENABLE = FIND_ALL + " and e.enabled_flag = 'Y' ";


    private final String FIND_EVENT_SET_BY_ID = "select es.id , es.event_id , es.setting_code , es.enabled_flag , es.condition_id" +
            " from  event_setting es where es.event_id= :EVENT_ID ";

    private final String FIND_SEG_BY_EVENT_SEG_ID =
            "select ID , EVENT_SETTING_ID , ENTRY_TYPE , VALUE_ID , SEGMENT_NUM from EVENT_SEGMENT_SETTING " +
                    " where EVENT_SETTING_ID = :EVENT_SETTING_ID";

    public List<Event> findAll() {

        return template.query(FIND_ALL, new EventResultSetExtractor());

    }

    public Event findByID(long id) {

        HashMap <String, Long> p = new HashMap<String, Long>();
        p.put("ID" , id);
        return template.query(FIND_ALL, p , new EventResultSetExtractor()).get(0);

    }


    public List<Event> findEnable() {

        return template.query(FIND_ALL_ENABLE, new EventResultSetExtractor());

    }


    public List<EventSetting> findEventSetList(long event_id) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("EVENT_ID", event_id);

        return template.query(FIND_EVENT_SET_BY_ID, new MapSqlParameterSource(param), new EventSettingResultSetExtractor());


    }

    private class EventResultSetExtractor implements ResultSetExtractor<List<Event>> {
        public List<Event> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List<Event> eventList = new ArrayList<Event>();
            Event e;
            HashMap<Long, Event> map = new HashMap<Long, Event>();
            while (resultSet.next()) {

                e = map.get(resultSet.getLong("ID"));

                if (e == null) {
                    e = new Event();
                    eventList.add(e);
                }

                e.setId(resultSet.getInt("ID"));
                e.setCode(resultSet.getString("code"));
                e.setName(resultSet.getString("name"));
                e.setEnabled("Y".equals(resultSet.getString("enabled_flag")));

                if (resultSet.getInt("doc_id") > 0) {
                    e.setDoc(documentDAO.findById(resultSet.getLong("doc_id")));
                }
                map.put(e.getId(), e);


                List<EventSetting> list = findEventSetList(e.getId());
                e.setEventSettingList(list);


            }

            return eventList;
        }
    }

    @Transactional
    public boolean saveList(List<Event> list) {
        HashMap<String, Object> param = new HashMap<String, Object>();

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String[] columnNames = new String[]{"id"};

        for (Event e : list) {

            param.put("CODE", e.getCode());
            param.put("NAME", e.getName());
            if (e.isEnabled()) param.put("ENABLED_FLAG", "Y");
            if (!e.isEnabled()) param.put("ENABLED_FLAG", "N");
            param.put("DOC_ID", e.getDoc().getId());
            param.put("ID", e.getId());

            if (e.getId() > 0) {

                template.update(
                        "update event set code = :CODE , name= :NAME , enabled_flag = :ENABLED_FLAG, " +
                                " doc_id= :DOC_ID where id=:ID", param);
            } else {
                SqlParameterSource parameterSource = new MapSqlParameterSource(param);
                template.update("insert into event(  code , name , enabled_flag , doc_id) " +
                        " values (  :CODE , :NAME , :ENABLED_FLAG , :DOC_ID)", parameterSource, keyHolder, columnNames);
                e.setId(keyHolder.getKey().intValue());

            }


            for (EventSetting es : e.getEventSettingList()) {
                HashMap<String, Object> esparam = new HashMap<String, Object>();
                esparam.put("ID", es.getId());
                esparam.put("SETTING_CODE", es.getCode());
                esparam.put("EVENT_ID", e.getId());
                if (es.isEnabled()) esparam.put("ENABLED_FLAG", "Y");
                if (!es.isEnabled()) esparam.put("ENABLED_FLAG", "N");

                if (es.getId() > 0) {
                    template.update("update event_setting set " +
                            " setting_code = :SETTING_CODE , enabled_flag= :ENABLED_FLAG  " +
                            " where id= :ID", new MapSqlParameterSource(esparam));

                } else {

                    template.update("insert into event_setting (event_id , setting_code , enabled_flag) " +
                                    " values (:EVENT_ID , :SETTING_CODE , :ENABLED_FLAG)", new MapSqlParameterSource(esparam), keyHolder,
                            new String[]{"ID"});
                    es.setId(keyHolder.getKey().intValue());
                    esparam.put("ID", es.getId());

                }


                if (es.getCondition() != null) {

                    conditionDao.saveCondition(es.getCondition());
                    esparam.put("condition_id", es.getCondition().getId());
                    template.update("update event_setting set " +
                                    "condition_id = :condition_id  where id= :ID",
                            new MapSqlParameterSource(esparam));


                }


                // Сохраним сегменты
                template.update("delete from EVENT_SEGMENT_SETTING where event_setting_id = :ID", esparam);
                HashMap<String, Object> setParam = new HashMap<String, Object>();
                for (SegmentSetting s : es.getSegmentSettingList()) {
                    //System.out.println("save segment");
                    setParam.put("EVENT_SETTING_ID", es.getId());
                    setParam.put("ENTRY_TYPE", s.getTypeEntry());

                    if (s.getValue() != null && s.getValue() instanceof com.av.domain.ConstantValue &&
                            s.getValue().getId() == 0
                            ) {
                        abstractValueDAO.saveValue(s.getValue());
                    }

                    if (s.getValue() != null) {
                        setParam.put("VALUE_ID", s.getValue().getId());
                    } else {
                        setParam.put("VALUE_ID", null);
                    }

                    setParam.put("SEGMENT_NUM", s.getSegmentNum());

                    template.update("insert into EVENT_SEGMENT_SETTING (EVENT_SETTING_ID , ENTRY_TYPE , " +
                                    "VALUE_ID , SEGMENT_NUM)" +
                                    " VALUES (:EVENT_SETTING_ID , :ENTRY_TYPE , :VALUE_ID, :SEGMENT_NUM) "
                            , new MapSqlParameterSource(setParam),
                            keyHolder, new String[]{"ID"});
                    s.setId(keyHolder.getKey().intValue());


                }

            }


        }


        return true;
    }

    ;


    private List<SegmentSetting> loadListSegment(long EVENT_SET_ID) {
        HashMap<String, Object> p = new HashMap<String, Object>();
        p.put("EVENT_SETTING_ID", EVENT_SET_ID);


        return template.query(FIND_SEG_BY_EVENT_SEG_ID, p, new SegmentResultExtractor());

    }

    ;


    private class EventSettingResultSetExtractor implements ResultSetExtractor<List<EventSetting>> {
        public List<EventSetting> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            List<EventSetting> list = new ArrayList<EventSetting>();

            while (resultSet.next()) {
                EventSetting es = new EventSetting();
                es.setId(resultSet.getInt("ID"));
                es.setCode(resultSet.getString("SETTING_CODE"));

                if (resultSet.getInt("condition_id") > 0) {
                    Condition condition = conditionDao.findById(resultSet.getInt("condition_id"));
                    es.setCondition(condition);
                }

                List<SegmentSetting> segmentSettingList = loadListSegment(es.getId());
                es.setSegmentSettingList(segmentSettingList);
                es.setEnabled("Y".equals(resultSet.getString("ENABLED_FLAG")));
                list.add(es);
            }


            return list;
        }
    }


    private class SegmentResultExtractor implements ResultSetExtractor<List<SegmentSetting>> {
        public List<SegmentSetting> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            List<SegmentSetting> ssList = new ArrayList<SegmentSetting>();
            while (resultSet.next()) {
                SegmentSetting s = new SegmentSetting();
                s.setId(resultSet.getInt("ID"));
                s.setSegmentNum(resultSet.getInt("SEGMENT_NUM"));
                s.setTypeEntry(resultSet.getString("ENTRY_TYPE"));
                if (resultSet.getInt("VALUE_ID") > 0) {
                    s.setValue(abstractValueDAO.findByID(resultSet.getInt("VALUE_ID")));
                    s.setRuleType(ConvertAbstractValue.converetToCodeName(s.getValue().getClass()));
                }
                ssList.add(s);

            }

            return ssList;
        }
    }


}
