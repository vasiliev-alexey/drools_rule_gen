package test.formular;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexey on 26.10.15.
 */
public class ACR0531722 {

    private long docID;
    private List<TS1S1722ITEM> FS_S1_S1_722_LIST = new ArrayList<TS1S1722ITEM>();
    private String FUND_SOURCE_CODE;


    public List<TS1S1722ITEM> getFS_S1_S1_722_LIST() {
        return FS_S1_S1_722_LIST;
    }

    public void setFS_S1_S1_722_LIST(List<TS1S1722ITEM> FS_S1_S1_722_LIST) {
        this.FS_S1_S1_722_LIST = FS_S1_S1_722_LIST;
    }

    public String getFUND_SOURCE_CODE() {
        return FUND_SOURCE_CODE;
    }

    public void setFUND_SOURCE_CODE(String FUND_SOURCE_CODE) {
        this.FUND_SOURCE_CODE = FUND_SOURCE_CODE;
    }

    public long getDocID() {
        return docID;
    }

    public void setDocID(long docID) {
        this.docID = docID;
    }
}
