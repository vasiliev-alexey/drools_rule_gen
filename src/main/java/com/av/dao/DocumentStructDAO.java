package com.av.dao;

import com.av.domain.DocumentStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by alexey on 26.10.15.
 */
@Repository
public class DocumentStructDAO {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private final static String FIND_BY_DOC_ID = "select struct_id ,    doc_id,    class_code,  " +
            "  node ,     level_code ,  collection_class , " +
            "PACKAGE_NAME, PATH from document_structure\n"+
            "where doc_id = :DOC_ID";

    public HashMap<String , DocumentStructure> getDocMetaData(long docID) {

        HashMap<String , Long> p = new HashMap<String, Long>();
        p.put("DOC_ID" , docID);
       return template.query(FIND_BY_DOC_ID , p , new DocumentStructureResultSetExtractor() );

    }


    private  class  DocumentStructureResultSetExtractor implements ResultSetExtractor<HashMap<String , DocumentStructure>>
    {
        public HashMap<String, DocumentStructure> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            HashMap<String , DocumentStructure> hds = new HashMap<String, DocumentStructure>();

            while (resultSet.next()) {

                DocumentStructure ds = new DocumentStructure();
                ds.setClassCode(resultSet.getString("class_code"));
                ds.setCollectionType(resultSet.getString("collection_class"));
                ds.setId(resultSet.getInt("struct_id"));
                ds.setLevelCode(resultSet.getInt("level_code"));
                ds.setNode(resultSet.getString("node"));
                ds.setPackageName(resultSet.getString("package_name"));
                ds.setPath(resultSet.getString("path"));

                hds.put(ds.getPath() , ds);

            }


            return hds;
        }
    }


}
