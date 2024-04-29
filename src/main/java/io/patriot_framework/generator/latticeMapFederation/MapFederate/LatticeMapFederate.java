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

package io.patriot_framework.generator.latticeMapFederation.MapFederate;


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
import hla.rti1516e.time.HLAfloat64TimeFactory;


import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import java.util.Arrays;

public class LatticeMapFederate implements Runnable{
    private String federateName;
    private String federationName;
    private RTIambassador rtiAmb;
    private LatticeMapAmbassador mapAmb;
    private EncoderFactory encoderFactory;

    private LatticeMap map;

    private MapTranslator mapTranslator;

    private HLAfloat64TimeFactory timeFactory;
    public static final String READY_TO_RUN = "ReadyToRun";
    public InteractionClassHandle tempUpdateHandle;
    public InteractionClassHandle extinguishUpdateHandle;




    public LatticeMapFederate(String federateName, String federationName) {
        this.federateName = federateName;
        this.federationName = federationName;
        map = new LatticeMap(5, 5);
        map.setItem(42,2, 3);

        try {
            rtiAmb = RtiFactoryFactory.getRtiFactory().getRtiAmbassador();
            mapTranslator = new MapTranslator(map, rtiAmb);
            mapAmb = new LatticeMapAmbassador( this, mapTranslator);
            encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();

        } catch (RTIexception e) {
            System.out.println("Chybicka");
        }
    }


    @Override
    public void run() {

        try {
            rtiAmb.connect(mapAmb, CallbackModel.HLA_IMMEDIATE);
        } catch (Exception e) {
            log("Chybicka connect");
        }

        try {
            URL[] modules = new URL[]{
                    (new File("src/main/java/io/patriot_framework/generator/latticeMapFederation/LatticeMap.xml")).toURI().toURL(),
            };

            rtiAmb.createFederationExecution(federationName, modules);
            log("Created Federation");
        } catch (FederationExecutionAlreadyExists exists) {
            log("Didn't create federation, it already existed");
        } catch (MalformedURLException urle) {
            log("Exception loading one of the FOM modules from disk: " + urle.getMessage());
            urle.printStackTrace();
            return;
        } catch (ErrorReadingFDD e) {
            throw new RuntimeException(e);
        } catch (InconsistentFDD e) {
            throw new RuntimeException(e);
        } catch (CouldNotOpenFDD e) {
            throw new RuntimeException(e);
        } catch (NotConnected e) {
            throw new RuntimeException(e);
        } catch (RTIinternalError e) {
            throw new RuntimeException(e);
        }


        try {


            rtiAmb.joinFederationExecution(federateName,            // name for the federate
                    "ExampleFederateType",   // federate type
                    federationName,     // name of federation
                    null);           // modules we want to add




            System.out.println(mapAmb.isAnnounced);
            rtiAmb.registerFederationSynchronizationPoint( READY_TO_RUN, null );
            while( mapAmb.isAnnounced == false )
            {
                rtiAmb.evokeMultipleCallbacks( 0.1, 0.2 );
                System.out.println( "Still waiting for announce" );
            }

            log("Federation created");
            Thread.sleep(5000);



            rtiAmb.synchronizationPointAchieved( READY_TO_RUN );

            while( mapAmb.isReadyToRun == false )
            {
                rtiAmb.evokeMultipleCallbacks( 0.1, 0.2 );
            }


            mapTranslator.publishClassAttributes();
            log("1");
            mapTranslator.registerElementInstances();
            log("2");
            mapTranslator.updateLatticeElem(1,2, 42);
            for(int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                int val = 42 + i;
                mapTranslator.updateLatticeElem(1,2, val);
                log("updated: x" + i + ", value: " + val);
            }


        } catch (Exception e) {
            System.out.println("AJAJAJ");
            System.out.println(e);
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
