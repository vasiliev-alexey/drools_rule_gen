package com.av.services;

import com.av.domain.Transaction;

import java.util.List;

/**
 * Created by alexey on 31.10.15.
 */
public interface AccountServceIface {

    public List<Transaction> genTransaction(Object o , String eventCode);

}
