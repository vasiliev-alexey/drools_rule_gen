package com.av.domain.drools;

import com.av.domain.Transaction;

/**
 * Created by vasil on 28.10.2015.
 */
public class ProcessGroupRule {


    private  String segment;
    private Transaction transaction;

   public   ProcessGroupRule (Transaction transaction , String seg) {
       this.segment = seg;
       this.transaction = transaction;
   }


}
