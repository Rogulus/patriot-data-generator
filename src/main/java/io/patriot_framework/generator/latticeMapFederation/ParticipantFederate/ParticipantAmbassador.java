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
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.FederateHandleSet;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.exceptions.FederateInternalError;
import io.patriot_framework.generator.hlaPoC.pocFederation.FireFederate.FireFederate;
import io.patriot_framework.generator.latticeMapFederation.MapFederate.MapTranslator;

public class ParticipantAmbassador extends NullFederateAmbassador {
    public Boolean isAnnounced = false;
    public Boolean isReadyToRun = false;
    private ParticipantFederate federate;
    private MapTranslator mapTranslator;

    public ParticipantAmbassador(ParticipantFederate federate, MapTranslator mapTranslator) {
        this.federate = federate;
        this.mapTranslator = mapTranslator;
    }

    @Override
    public void announceSynchronizationPoint( String label, byte[] tag )
    {
        if( label.equals(FireFederate.READY_TO_RUN) )
            this.isAnnounced = true;
        federate.log("ready to run announed");
    }


    @Override
    public void federationSynchronized( String label, FederateHandleSet failed )
    {
        if( label.equals(FireFederate.READY_TO_RUN) )
            this.isReadyToRun = true;
    }


    @Override
    public void objectInstanceNameReservationSucceeded(String name) {
        String coords[] = name.split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            mapTranslator.setNameReservationSucceeded(x, y);
    }

    @Override
    public void objectInstanceNameReservationFailed(String name) {
        federate.log("WHOOOOOA: multipleObjectInstanceNameReservationFailed");
        throw new RuntimeException ("ObjectInstanceNameReservationFailed");
    }


    @Override
    public void discoverObjectInstance( ObjectInstanceHandle theObject,
                                        ObjectClassHandle theObjectClass,
                                        String objectName )
            throws FederateInternalError
    {
        //todo zkontrolovat classy a poslat do patricnych mist
        mapTranslator.checkSubscription(theObject, objectName);
    }


    @Override
    public void reflectAttributeValues( ObjectInstanceHandle theObject,
                                        AttributeHandleValueMap theAttributes,
                                        byte[] tag,
                                        OrderType sentOrder,
                                        TransportationTypeHandle transport,
                                        SupplementalReflectInfo reflectInfo )
            throws FederateInternalError
    {
        System.out.println("reflectAttributes");
        mapTranslator.reflectAttributeValues(theObject, theAttributes);


//        StringBuilder builder = new StringBuilder( "Reflection for object:" );
//
//        // print the handle
//        builder.append( " handle=" + theObject );
//        // print the tag
//        builder.append( ", tag=" + new String(tag) );
//        // print the time (if we have it) we'll get null if we are just receiving
//        // a forwarded call from the other reflect callback above
//
//
//        // print the attribute information
//        builder.append( ", attributeCount=" + theAttributes.size() );
//        builder.append( "\n" );
//        for( AttributeHandle attributeHandle : theAttributes.keySet() )
//        {
//            // print the attibute handle
//            builder.append( "\tattributeHandle=" );
//
//            // if we're dealing with Flavor, decode into the appropriate enum value
//            if( attributeHandle.equals(federate.flavHandle) )
//            {
//                builder.append( attributeHandle );
//                builder.append( " (Flavor)    " );
//                builder.append( ", attributeValue=" );
//                builder.append( decodeFlavor(theAttributes.get(attributeHandle)) );
//            }
//            else
//            {
//                builder.append( attributeHandle );
//                builder.append( " (Unknown)   " );
//            }
//
//            builder.append( "\n" );
//        }
    }

}
