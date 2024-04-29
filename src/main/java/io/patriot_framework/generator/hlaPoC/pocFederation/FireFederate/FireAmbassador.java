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

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.FederateHandleSet;
import hla.rti1516e.OrderType;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.exceptions.FederateInternalError;

public class FireAmbassador extends NullFederateAmbassador {
    public Boolean isAnnounced = false;
    public Boolean isReadyToRun = false;
    private FireFederate federate;

    public FireAmbassador(FireSim fireSim, FireFederate federate) {
        this.federate = federate;
    }

    @Override
    public void announceSynchronizationPoint( String label, byte[] tag )
    {
        if( label.equals(FireFederate.READY_TO_RUN) )
            this.isAnnounced = true;
    }

    @Override
    public void federationSynchronized( String label, FederateHandleSet failed )
    {
        if( label.equals(FireFederate.READY_TO_RUN) )
            this.isReadyToRun = true;
    }


    @Override
    public void receiveInteraction( InteractionClassHandle interactionClass,
                                    ParameterHandleValueMap theParameters,
                                    byte[] tag,
                                    OrderType sentOrdering,
                                    TransportationTypeHandle theTransport,
                                    SupplementalReceiveInfo receiveInfo )
            throws FederateInternalError
    {
        System.out.println("HEERE");
        // just pass it on to the other method for printing purposes
        // passing null as the time will let the other method know it
        // it from us, not from the RTI
        this.receiveInteraction( interactionClass,
                theParameters,
                tag,
                sentOrdering,
                theTransport,
                null,
                sentOrdering,
                receiveInfo );
    }


    @Override
    public void receiveInteraction( InteractionClassHandle interactionClass,
                                    ParameterHandleValueMap theParameters,
                                    byte[] tag,
                                    OrderType sentOrdering,
                                    TransportationTypeHandle theTransport,
                                    LogicalTime time,
                                    OrderType receivedOrdering,
                                    SupplementalReceiveInfo receiveInfo )
            throws FederateInternalError
    {

        if( interactionClass.equals(federate.extinguishUpdateHandle) ) {
            System.out.println("HASIM");
            for( ParameterHandle parameter : theParameters.keySet() )
            {
                System.out.println("receive extinguish:");
                System.out.println(parameter);
                System.out.println(theParameters.get(parameter));
            }
        }

        System.out.println("COKOLI");
        for( ParameterHandle parameter : theParameters.keySet() )
        {
            System.out.println("receive extinguish:");
            System.out.println(parameter);
            System.out.println(theParameters.get(parameter));
        }
    }
}
