/*
 * Copyright 2024 Patriot project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.patriot_framework.generator.latticeMapFederation.ParticipantFederate;


import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.exceptions.*;
import io.patriot_framework.generator.latticeMapFederation.MapFederate.LatticeMap;
import io.patriot_framework.generator.latticeMapFederation.MapFederate.LatticeMapAmbassador;
import io.patriot_framework.generator.latticeMapFederation.MapFederate.MapTranslator;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

public class ParticipantFederate implements Runnable{
    private String federateName;
    private String federationName;
    private RTIambassador rtiAmb;
    private ParticipantAmbassador parAmb;
    private EncoderFactory encoderFactory;

    private Participant participant;
    private ObjectClassHandle mapElemClassHandle;

    private MapTranslator mapTranslator;

    public static final String READY_TO_RUN = "ReadyToRun";
    public InteractionClassHandle extinguishUpdateHandle;

    public ParticipantFederate(String federateName, String federationName) {
        this.federateName = federateName;
        this.federationName = federationName;
        participant = new Participant();
        log("HERE1");
        try {
            rtiAmb = RtiFactoryFactory.getRtiFactory().getRtiAmbassador();
            mapTranslator = new MapTranslator(new LatticeMap(5, 5), rtiAmb); //todo tohle je podivne
            parAmb = new ParticipantAmbassador( this, mapTranslator);
            encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();

        } catch (RTIexception e) {
            System.out.println("Chybicka");
        }
    }


    @Override
    public void run() {
        try {
            log("uz tady");
                rtiAmb.connect(parAmb, CallbackModel.HLA_IMMEDIATE);
                log("sss");

                try {
                    URL[] modules = new URL[]{
                            (new File("src/main/java/io/patriot_framework/generator/latticeMapFederation/LatticeMap.xml")).toURI().toURL(),
                    };

                    rtiAmb.createFederationExecution(federationName, modules);
                    System.out.println("Created Federation");
                } catch (RTIexception e) {
                    log(e.toString());
                }


                rtiAmb.joinFederationExecution(federateName,            // name for the federate
                        "ExampleFederateType",   // federate type
                        federationName,     // name of federation
                        null);           // modules we want to add

                log("HERE");
                rtiAmb.registerFederationSynchronizationPoint( READY_TO_RUN, null );
                while( parAmb.isAnnounced == false )
                {
                    log("sd");
                    rtiAmb.evokeCallback(0.1);
                    rtiAmb.evokeMultipleCallbacks( 0.1, 0.2 );
                }
                log("there");
                rtiAmb.synchronizationPointAchieved( READY_TO_RUN );
                while( parAmb.isReadyToRun == false )
                {
                    rtiAmb.evokeMultipleCallbacks( 0.1, 0.2 );
                }


                // do the publication
                String iname = "HLAinteractionRoot.FireExtinguish";
                InteractionClassHandle fireExtinguishHandle = rtiAmb.getInteractionClassHandle( iname );
                rtiAmb.publishInteractionClass(fireExtinguishHandle);

                log("Int");

                mapTranslator.subscribeElem(1,2);


            while( true )
            {
                Thread.sleep(300);
                log("tn: " + Thread.currentThread().getId());
//                rtiAmb.evokeMultipleCallbacks( 0.1, 0.2 );
            }



        } catch (Exception e) {
            log("AJAJAJ");
            log(e.toString());
        }
    }

    private byte[] generateTag()
    {
        return ("(timestamp) "+System.currentTimeMillis()).getBytes();
    }




    public void log(String msg) {
        System.out.print("Federate: " + federateName + ", msg: ");
        System.out.println(msg);
    }

}
