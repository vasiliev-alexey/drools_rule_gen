package testDAO;

import com.av.domain.Transaction;
import com.av.utils.CustomSource;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.core.ObjectFilter;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.RuleContext;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import test.formular.ACR0531722;
import test.formular.TS1S1722ITEM;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by vasil on 05.10.2015.
 */
public class TransactionGeneratorTest {



    private ACR0531722 genDoc() {

        ACR0531722 rr = new ACR0531722();
        rr.setFUND_SOURCE_CODE("1");

        for (int i = 0; i <  9 ; i++) {
            TS1S1722ITEM  line = new TS1S1722ITEM();
            line.setBCC("10000000000000000021" + new Random().nextInt(9));
            if (i%3 == 0)   line.setSection("кан");

            if (i%3 == 1)   line.setSection("аю");
            if (i%3 == 2)   line.setSection("онтп");
            rr.getFS_S1_S1_722_LIST().add(line);
        }




        return  rr;
    }


    @Before
    public void init() {


    }








    @Test
    public  void t1() throws FileNotFoundException {

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
      //  FileInputStream fis = new FileInputStream( "/home/alexey/IdeaProjects/mark2/src/main/resources/s1.drl" );
        kfs.write("src/main/resources/s1_1.drl",
                kieServices.getResources().newClassPathResource("s22.drl"));// .newInputStreamResource( fis ) ); //fis
        KieBuilder kieBuilder = kieServices.newKieBuilder( kfs ).buildAll();
        Results results = kieBuilder.getResults();
        if( results.hasMessages( Message.Level.ERROR ) ){
            System.out.println( results.getMessages() );
            throw new IllegalStateException( "### errors ###" );
        }

        KieContainer kieContainer =
                kieServices.newKieContainer( kieServices.getRepository().getDefaultReleaseId() );
        KieBase kieBase = kieContainer.getKieBase();
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("customSource", new CustomSource());
        kieSession.insert(genDoc());

        kieSession.getAgenda().getAgendaGroup("CODE_1").setFocus();
        //kieSession.getAgenda().getAgendaGroup("CODE_1").setFocus();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy hh.mm.ss SSS");
        System.out.println(df.format(new Date()));

        kieSession.fireAllRules();
        System.out.println(df.format(new Date()));






        Collection<Transaction> trList  = (Collection<Transaction>)kieSession.getObjects(new ObjectFilter() {
            public boolean accept(Object o) {
                return o instanceof Transaction;
            }
        });

        System.out.println("-----------------------------");
        for (Transaction t: trList) {
            System.out.println(t);
        }


    }


    public static KnowledgeBase getBase () {

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        kbuilder.add( ResourceFactory.newFileResource("sq.drl"),
                ResourceType.DRL);
        if( kbuilder.hasErrors() ) {
            System.out.println( kbuilder.getErrors() );
            return null;
        }
      return   kbuilder.newKnowledgeBase();
    }


}