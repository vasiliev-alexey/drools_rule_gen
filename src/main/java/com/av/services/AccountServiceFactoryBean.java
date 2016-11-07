package com.av.services;

import com.av.utils.CustomSource;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Bean;

/**
 * Created by alexey on 31.10.15.
 */
public class AccountServiceFactoryBean extends AbstractFactoryBean<AccountServceIface> {
private   KieServices kieServices;
    private  KieFileSystem kfs;
    KieContainer kieContainer;

    public AccountServiceFactoryBean() {
         kieServices = KieServices.Factory.get();
         kfs = kieServices.newKieFileSystem();
        //  FileInputStream fis = new FileInputStream( "/home/alexey/IdeaProjects/mark2/src/main/resources/s1.drl" );
        kfs.write("src/main/resources/s1_1.drl",
                kieServices.getResources().newClassPathResource("s22.drl"));// .newInputStreamResource( fis ) ); //fis
        KieBuilder kieBuilder = kieServices.newKieBuilder( kfs ).buildAll();
        Results results = kieBuilder.getResults();
        if( results.hasMessages( Message.Level.ERROR ) ){
            System.out.println( results.getMessages() );
            throw new IllegalStateException( "### AccountServiceFactoryBean has errors ###" );
        }
         kieContainer =
                kieServices.newKieContainer( kieServices.getRepository().getDefaultReleaseId() );
        KieBase kieBase = kieContainer.getKieBase();


    }

    @Override
    public Class<?> getObjectType() {
        return AccountServceIface.class;
    }

    @Override
    public boolean isSingleton() {
       return false;
    }

    @Override
        protected AccountServceIface createInstance() throws Exception {
        KieSession ks = kieContainer.newKieSession();
        ks.setGlobal("customSource" , new CustomSource());
        AccountServiceImpl accountService = new AccountServiceImpl(ks);

        return accountService;
    }





}
