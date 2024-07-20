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

package io.patriot_framework.generator.eventSimulator.eventGenerator.simulationAdapter;

import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.controll.client.CoapActuatorHandler;
import io.patriot_framework.generator.eventSimulator.eventGenerator.SimulationBase;
import io.patriot_framework.generator.eventSimulator.Time.Time;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;
import java.util.Deque;
import java.util.NoSuchElementException;

public abstract class ActuatorAdapterBase extends SimulationBase {
    Deque<String> stateHistory;
    private CoapActuatorHandler coapActuatorHandler;
    Time pollingInterval;


    public ActuatorAdapterBase(CoapActuatorHandler actuatorHandler, Time pollingInterval) {
        coapActuatorHandler = actuatorHandler;
        this.pollingInterval = pollingInterval;
    }

//    public ActuatorAdapter(String ip, int port, String actuatorLabel) throws ConnectorException, IOException {
//        this( new CoapControlClient(ip + ":" + port), actuatorLabel);
//    }


    protected String getNext() throws NoSuchElementException {
        return stateHistory.removeFirst();
    }

    protected String peekLast() throws NoSuchElementException{
        return stateHistory.getLast();
    }

    protected boolean hasChanged() {
        return !stateHistory.isEmpty();
    }

    protected int stateUpdatesCount() {
        return stateHistory.size();
    }


    // todo comment
    abstract protected void processUpdates();


    @Override
    public final void init () {
        registerRecurringAwake(pollingInterval);
    }

    @Override
    public final void awake () {

        try {
            stateHistory = coapActuatorHandler.getStateHistory();
            stateHistory.removeFirst(); // first element was the last observed state
//            Iterator<String> iter = stateHistory.iterator();
//            iter.next();
//            while (iter.hasNext()) {
//                publish(new Data(String.class, iter.next()), "actuatorUpdate");
//            }

        } catch (ConnectorException | IOException e) {
            throw new RuntimeException(e);
        }  //todo nejak poresit ty exceptions

        processUpdates();
    }

    @Override
    public final void receive (Data message, String topic){
    }
}
