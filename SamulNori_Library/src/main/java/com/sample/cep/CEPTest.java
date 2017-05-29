package com.sample.cep;

import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CEPTest {
    public static final void main(String[] args) {
        try {
			StatefulKnowledgeSession kSession = loadDrlWithCEP();
			
			
			
			writeValue wv = new writeValue();
			wv.setWriteValue(3);
			kSession.insert(wv);
			EntryPoint ep1 = kSession.getEntryPoint("thermometer");		
			EntryPoint ep2 = kSession.getEntryPoint("gas");
			EntryPoint ep3 = kSession.getEntryPoint("fingerprint");
			EntryPoint ep4 = kSession.getEntryPoint("vibration");
			EntryPoint ep5 = kSession.getEntryPoint("dust");
			int i;
			
			// Rxtx
			Rxtx rx = new Rxtx();
			//rx.connect("COM3");

			for(i =0; i<1000; i++){
				DustSensorData ssd = new DustSensorData(102);
				ssd.setDustLimit(200);
				ep5.insert(ssd);
				ep5.insert(ssd);
				kSession.fireAllRules();
				rx.writeValue = wv.getWriteValue();
				System.out.println(rx.readValue);
				System.out.println(rx.writeValue + " / "+wv.getWriteValue());
			}
			kSession.halt();
			kSession.dispose();
			/*
			for(int i = 0; i < THERMO_DATA.length; i++) {
				System.out.println(i);
				Thread.sleep(1000);

				ThermoSensorData tsd = new ThermoSensorData(THERMO_DATA[i]);
				ep1.insert(tsd);
				System.out.println("  T: " + THERMO_DATA[i]);
				kSession.fireAllRules();
				Thread.sleep(100);
				
				SmokeSensorData ssd = new SmokeSensorData(SMOKE_DATA[i]);
				ep2.insert(ssd);
				System.out.println("  S: " + SMOKE_DATA[i]);
				kSession.fireAllRules();
			}
			
			kSession.halt();
			kSession.dispose();
			*/
			
        } 
        
        catch (Throwable t) {
            t.printStackTrace();
        }
        
    }
    static KieSession loadDrl() {
		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		KieFileSystem kfs = ks.newKieFileSystem();
		
		kfs.write(ResourceFactory.newClassPathResource("rules/cep.drl", CEPTest.class));
		
		KieBuilder kb = ks.newKieBuilder(kfs);
		
		kb.buildAll(); // kieModule is automatically deployed to KieRepository if successfully built.
		if (kb.getResults().hasMessages(Message.Level.ERROR)) {
		    throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
		}
		
		KieContainer kContainer = ks.newKieContainer(kr.getDefaultReleaseId());
		
		KieSession kSession = kContainer.newKieSession();

		return kSession;
		
    }
    
    static StatefulKnowledgeSession loadDrlWithCEP() {
		KieBaseConfiguration config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
		config.setOption(EventProcessingOption.STREAM);

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("rules/cep.drl", CEPTest.class), ResourceType.DRL);
		if (kbuilder.hasErrors()) {
		    throw new RuntimeException("Build Errors:\n" + kbuilder.getErrors().toString());
		}
		
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(config);
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());  
		
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		
		return ksession;
    }
}
