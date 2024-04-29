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

package io.patriot_framework.generator.hlaPoC.pocFederation.FireFederate;


import hla.rti1516e.CallbackModel;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.EncoderFactory;

import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.time.HLAfloat64TimeFactory;

import hla.rti1516e.encoding.HLAinteger16BE;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;

public class FireFederate implements Runnable{
    private String federateName;
    private RTIambassador rtiAmb;
    private FireAmbassador fireAmb;
    private EncoderFactory encoderFactory;

    private FireSim fireSim;

    private HLAfloat64TimeFactory timeFactory;
    public static final String READY_TO_RUN = "ReadyToRun";
    public InteractionClassHandle tempUpdateHandle;
    public InteractionClassHandle extinguishUpdateHandle;

    public FireFederate(String federateName) {
        this.federateName = federateName;
    }


    @Override
    public void run() {

        try {
            fireSim = new FireSim();
            rtiAmb = RtiFactoryFactory.getRtiFactory().getRtiAmbassador();
            fireAmb = new FireAmbassador(fireSim, this);
            encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();

            rtiAmb.connect(fireAmb, CallbackModel.HLA_IMMEDIATE);
        } catch (Exception e) {
            System.out.println("Chybicka");
        }

        try {
            URL[] modules = new URL[]{
                    (new File("src/main/java/io/patriot_framework/generator/hlaPoC/pocFederation/FirePoC.xml")).toURI().toURL(),
            };

            rtiAmb.createFederationExecution("PoCFederation", modules);
            System.out.println("Created Federation");
        } catch (FederationExecutionAlreadyExists exists) {
            System.out.println("Didn't create federation, it already existed");
        } catch (MalformedURLException urle) {
            System.out.println("Exception loading one of the FOM modules from disk: " + urle.getMessage());
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
                    "PoCFederation",     // name of federation
                    null);           // modules we want to add

            this.timeFactory = (HLAfloat64TimeFactory)rtiAmb.getTimeFactory();

            System.out.println(fireAmb.isAnnounced);
            rtiAmb.registerFederationSynchronizationPoint( READY_TO_RUN, null );
            while( fireAmb.isAnnounced == false )
            {
                rtiAmb.evokeMultipleCallbacks( 0.1, 0.2 );
                System.out.println( "Still waiting for announce" );
            }


            Thread.sleep(3000);

            System.out.println("HERE1");
            rtiAmb.synchronizationPointAchieved( READY_TO_RUN );
            System.out.println("HERE2");
            while( fireAmb.isReadyToRun == false )
            {
                rtiAmb.evokeMultipleCallbacks( 0.1, 0.2 );
            }



            // do the publication
            String iname = "HLAinteractionRoot.TemperatureUpdate";
            tempUpdateHandle = rtiAmb.getInteractionClassHandle( iname );

            rtiAmb.publishInteractionClass(tempUpdateHandle);
            rtiAmb.subscribeInteractionClass(tempUpdateHandle);


            String iname2 = "HLAinteractionRoot.FireExtinguish";
            extinguishUpdateHandle = rtiAmb.getInteractionClassHandle( iname2 );
            rtiAmb.subscribeInteractionClass(extinguishUpdateHandle);



            for(int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                System.out.println("SEND");
            ParameterHandleValueMap parameters = rtiAmb.getParameterHandleValueMapFactory().create(2);

            ParameterHandle tempHandle = rtiAmb.getParameterHandle( tempUpdateHandle, "Temperature" );
            ParameterHandle coordHandle = rtiAmb.getParameterHandle( tempUpdateHandle, "Coordinate" );

            HLAinteger16BE temperature = encoderFactory.createHLAinteger16BE( (short)69);
            parameters.put(tempHandle, temperature.toByteArray());
            HLAinteger16BE coords = encoderFactory.createHLAinteger16BE( (short)42);
            parameters.put(coordHandle, coords.toByteArray());


                rtiAmb.sendInteraction( tempUpdateHandle, parameters, generateTag());
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

//    private void enableTimePolicy() throws Exception
//    {
//        // NOTE: Unfortunately, the LogicalTime/LogicalTimeInterval create code is
//        //       Portico specific. You will have to alter this if you move to a
//        //       different RTI implementation. As such, we've isolated it into a
//        //       method so that any change only needs to happen in a couple of spots
//        HLAfloat64Interval lookahead = timeFactory.makeInterval( fireAmb.federateLookahead );
//
//        ////////////////////////////
//        // enable time regulation //
//        ////////////////////////////
//        this.rtiAmb.enableTimeRegulation( lookahead );
//
//        // tick until we get the callback
//        while( fireAmb.isRegulating == false )
//        {
//            rtiAmb.evokeMultipleCallbacks( 0.1, 0.2 );
//        }
//
//        /////////////////////////////
//        // enable time constrained //
//        /////////////////////////////
//        this.rtiAmb.enableTimeConstrained();
//
//        // tick until we get the callback
//        while( fireAmb.isConstrained == false )
//        {
//            rtiAmb.evokeMultipleCallbacks( 0.1, 0.2 );
//        }
//    }
}
