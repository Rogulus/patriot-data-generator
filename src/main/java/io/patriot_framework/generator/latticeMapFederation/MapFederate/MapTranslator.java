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
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.IllegalName;
import hla.rti1516e.exceptions.NameSetWasEmpty;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotPublished;
import hla.rti1516e.exceptions.ObjectInstanceNameInUse;
import hla.rti1516e.exceptions.ObjectInstanceNameNotReserved;
import hla.rti1516e.exceptions.RTIexception;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;


public class MapTranslator {
    private LatticeMap map;
    public boolean nameReservationSucceededAll = false;
    public LatticeElem handleArray[][];
    private int cellNumber;
    private int namesReservedCount = 0;
    private RTIambassador rtiAmb;
    private EncoderFactory encoderFactory;


    private AttributeHandle valueHandle;
    private ObjectClassHandle mapElemClassHandle;
    private Dictionary<ObjectInstanceHandle, Pair> handleMapping = new Hashtable<>();

    private List<String> subscribedElems = new ArrayList<String>(10);


    public MapTranslator(LatticeMap map, RTIambassador rtiAmb) {
        this.map = map;
        this.rtiAmb = rtiAmb;
        handleArray = new LatticeElem[map.getWidth()][map.getHeight()];
        cellNumber = map.getHeight() * map.getWidth();
        for (int i = 0; i < map.getWidth(); i++) {
            for(int j = 0; j < map.getHeight(); j++){
                handleArray[i][j] = new LatticeElem();
            }
        }
        try {
            encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
        } catch (RTIinternalError e) {

        }
    }


    public void publishClassAttributes() throws Exception{
        String rtiClassName = "MapElement";

        mapElemClassHandle = rtiAmb.getObjectClassHandle(rtiClassName);
        AttributeHandle valueHandle = rtiAmb.getAttributeHandle( mapElemClassHandle, "CellValue" );
        setValueHandle(valueHandle);

        AttributeHandleSet attributes = rtiAmb.getAttributeHandleSetFactory().create();
        attributes.add( valueHandle );

        rtiAmb.publishObjectClassAttributes(mapElemClassHandle, attributes);
    }

    public void registerElementInstances() throws FederateNotExecutionMember, ObjectClassNotPublished, ObjectClassNotDefined, RestoreInProgress, NotConnected, RTIinternalError, SaveInProgress, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, IllegalName, CallNotAllowedFromWithinCallback, NameSetWasEmpty {
        for(int x = 0; x < map.getWidth(); x++) {
            for(int y = 0; y < map.getWidth(); y++) {
                String instanceName = x + "," + y;
                rtiAmb.reserveObjectInstanceName(instanceName);
            }
        }

//            rtiAmb.reserveMultipleObjectInstanceName(names);
        while(!nameReservationSucceededAll)
        {
            rtiAmb.evokeMultipleCallbacks( 0.1, 0.2 );
        }
    }


    public void setNameReservationSucceeded(int x, int y) {
        handleArray[x][y].nameReserved = true;
        namesReservedCount ++;
        if(namesReservedCount == cellNumber) {
            nameReservationSucceededAll = true;
        }
        String instanceName = x + "," + y;
        try {
            handleArray[x][y].instanceHandle = rtiAmb.registerObjectInstance(mapElemClassHandle, instanceName);
            System.out.println("reservation x:" + x + " y:" + y + " handle: " + handleArray[x][y].instanceHandle);
        } catch (Exception e) {
            System.out.println("setNameReservationSucceeded error:");
            e.printStackTrace();
        }
    }


    public void updateLatticeElem(int x, int y, int value) throws RTIexception
    {
        LatticeElem elem = handleArray[x][y];
        AttributeHandleValueMap attributes = rtiAmb.getAttributeHandleValueMapFactory().create(1);
        HLAinteger32BE cellValue = encoderFactory.createHLAinteger32BE( value );
        attributes.put( getValueHandle(), cellValue.toByteArray() );
        rtiAmb.updateAttributeValues( elem.instanceHandle, attributes, null );
        System.out.println("updating x:" + x + " y:" + y + " handle: " + elem.instanceHandle);
    }

    public AttributeHandle getValueHandle() {
        return valueHandle;
    }

    public void setValueHandle(AttributeHandle valueHandle) { //todo tohle by mohlo byt v konstruktoru
        this.valueHandle = valueHandle;
    }

    public void subscribeElem(int x, int y) {
        String rtiClassName = "MapElement";

        try {
            mapElemClassHandle = rtiAmb.getObjectClassHandle(rtiClassName);

            AttributeHandle valueHandle = rtiAmb.getAttributeHandle( mapElemClassHandle, "CellValue" );
            setValueHandle(valueHandle);

            AttributeHandleSet attributes = rtiAmb.getAttributeHandleSetFactory().create();
            attributes.add( valueHandle );
            rtiAmb.subscribeObjectClassAttributes(mapElemClassHandle, attributes);


            subscribedElems.add(x + "," + y);
        } catch (RTIexception e) {
            System.out.println(e);
        }
    }

    public void checkSubscription(ObjectInstanceHandle handle, String name){

        System.out.println("THREAD ID: " + Thread.currentThread().getId());
        if(! subscribedElems.contains(name)) {
            return;
        }
        String coords[] = name.split(",");
        Integer x = Integer.parseInt(coords[0]);
        Integer y = Integer.parseInt(coords[1]);
        handleArray[x][y].instanceHandle = handle;
        handleMapping.put(handle, new Pair(x, y));
        System.out.println(String.format("check subscription: X: %s, Y: %s ", x.toString(), y.toString()));

    }

    public ObjectClassHandle getClassHandle() {
        return mapElemClassHandle;
    }

    public void reflectAttributeValues(ObjectInstanceHandle instanceHandle, AttributeHandleValueMap attributes) {
        System.out.println("x");
        System.out.println(handleMapping.size());
        Pair coords = handleMapping.get(instanceHandle);
        System.out.println("y");
        System.out.println(instanceHandle);


        if (coords == null) {
            return;
        }

        System.out.println("MAP Translate reflect attribute ");
        StringBuilder builder = new StringBuilder( "Reflection for object:" );
        for( AttributeHandle attributeHandle : attributes.keySet() )
        {
            // print the attibute handle
            builder.append( "\tattributeHandle=" );

            HLAinteger32BE value = encoderFactory.createHLAinteger32BE();
            // decode
            try
            {
                value.decode( attributes.get(attributeHandle ));
            } catch (DecoderException e) {
                throw new RuntimeException(e);
            }

            map.setItem(value.getValue(), coords.first, coords.second);

            {
                builder.append( attributeHandle );
                builder.append( " (Temp)    " );
                builder.append( ", attributeValue=" );
                builder.append( value.getValue() );
            }


            builder.append( "\n" );
        }
        System.out.println(map);

        // todo updatovat hodnotu v mape
    }


    private static class LatticeElem {


        public ObjectClassHandle classHandle;
        public ObjectInstanceHandle instanceHandle;

        public boolean nameReserved = false;
        public int x;
        public int y;
    }

    private class Pair {
        public Pair(int x, int y) {
            first = x;
            second = y;
        }
        public int first;
        public int second;
    }
}