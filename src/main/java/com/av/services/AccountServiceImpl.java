package com.av.services;

import com.av.domain.Transaction;
import org.drools.core.ObjectFilter;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by alexey on 31.10.15.
 */
public class AccountServiceImpl implements AccountServceIface {

    private KieSession kieSession;

    public AccountServiceImpl(KieSession kieSession) {
this.kieSession = kieSession;
    }





    public List<Transaction> genTransaction(Object o, String eventCode) {

        kieSession.getAgenda().getAgendaGroup(eventCode).setFocus();
        kieSession.insert(o);
        kieSession.fireAllRules();



        Collection<Transaction> trList  = (Collection<Transaction>)kieSession.getObjects(new ObjectFilter() {
            public boolean accept(Object o) {
                return o instanceof Transaction;
            }
        });
        System.out.println("cnt=" +trList.size());
        return new ArrayList(trList);

    }
}
