package com.av.dao;

import com.av.domain.Document;
import com.av.domain.DocumentAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alexey on 17.10.15.
 */
@Repository
public class DocumentDAO {
    @Autowired
    NamedParameterJdbcTemplate template;


    private static final String FIND_ALL = "select doc.doc_id , doc.code , doc.name ,\n" +
            "docat.attribute_id , av.code attribute_code  , av.name as attribute_name  , docat.path, \n" +
            "docat.FIELD , docat.FIELD_CLASS\n" +
            "from document  doc\n" +
            " left outer   join document_attribute docat on doc.doc_id = docat.doc_id " +
            " left outer join abstract_value av on av.id = docat.attribute_id ";

    private static final String FIND_BY_ID = FIND_ALL + " where doc.doc_id=:DOC_ID";

    public List<Document> findAll() {

        return template.query(FIND_ALL, new DocumentExtractor());
    }

    public Document findById(Long id) {


        HashMap<String, Long> params = new HashMap<String, Long>();
        params.put("DOC_ID", id);
        SqlParameterSource param = new MapSqlParameterSource(params);
        //  System.out.println("DOC_ID=" + id);
        return template.query(FIND_BY_ID, param, new DocumentExtractor()).get(0);
    }

    public DocumentAttribute findAttrById(long id) {


        String SQL_STR = "select\n" +
                "docat.attribute_id , av.code attribute_code  , av.name as attribute_name , av.type , docat.path, \n" +
                "            docat.FIELD , docat.FIELD_CLASS \n" +
                "            from  document_attribute docat \n" +
                "            inner join abstract_value av on av.id = docat.attribute_id \n" +
                "where av.id = :ID";

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("ID", id);

        return template.query(SQL_STR, param, new DocumentAttributeExtractor());

    }

    ;


    private class DocumentAttributeExtractor implements ResultSetExtractor<DocumentAttribute> {
        public DocumentAttribute extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            while (resultSet.next()) {
                DocumentAttribute docAtr = new DocumentAttribute();

                docAtr.setId(resultSet.getLong("attribute_id"));
                docAtr.setCode(resultSet.getString("attribute_code"));
                docAtr.setName(resultSet.getString("attribute_name"));
                docAtr.setType(resultSet.getString("type"));
                docAtr.setPath(resultSet.getString("path"));
                docAtr.setFieldClass(resultSet.getString("FIELD_CLASS"));
                docAtr.setFieldCode(resultSet.getString("FIELD"));
                return docAtr;
            }
return null;
        }
    }


    private class DocumentExtractor implements ResultSetExtractor<List<Document>> {
        public List<Document> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            List<Document> listDoc = new ArrayList<Document>();
            HashMap<Long, Document> map = new HashMap<Long, Document>();
            Document doc;

            while (resultSet.next()) {

                if (map.containsKey(resultSet.getLong("DOC_ID"))) {
                    doc = map.get(resultSet.getLong("DOC_ID"));
                } else {
                    doc = new Document();
                    doc.setDocumentAttributeList(new ArrayList<DocumentAttribute>());
                    listDoc.add(doc);

                }
                doc.setId(resultSet.getLong("DOC_ID"));
                doc.setCode(resultSet.getString("CODE"));
                doc.setName(resultSet.getString("NAME"));


                map.put(doc.getId(), doc);


                if (resultSet.getLong("attribute_id") > 0) {
                   // System.out.println("ADD");
                    DocumentAttribute docAtr = new DocumentAttribute();
                    docAtr.setId(resultSet.getLong("attribute_id"));
                    docAtr.setCode(resultSet.getString("attribute_code"));
                    docAtr.setName(resultSet.getString("attribute_name"));
                    docAtr.setPath(resultSet.getString("path"));
                    docAtr.setFieldClass(resultSet.getString("FIELD_CLASS"));
                    docAtr.setFieldCode(resultSet.getString("FIELD"));
                    doc.getDocumentAttributeList().add(docAtr);
                }

            }


            return listDoc;
        }
    }

}
