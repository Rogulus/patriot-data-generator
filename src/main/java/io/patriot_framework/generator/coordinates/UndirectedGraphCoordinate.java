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

package io.patriot_framework.generator.coordinates;


import io.patriot_framework.generator.Data;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.jgrapht.Graphs.neighborListOf;

// todo udelat vahovy graf a toto bude specialni pripad
public class UndirectedGraphCoordinate implements Coordinate<UndirectedGraphCoordinate>{
    private Graph<String, DefaultEdge> graph;
    private String vertex;


    private UndirectedGraphCoordinate(Graph<String, DefaultEdge> graph, String vertex) {
        this.graph = graph;
        this.vertex = vertex;
    }


    @Override
    public double distance(UndirectedGraphCoordinate other) {
        DijkstraShortestPath<String, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(graph);
        GraphPath<String, DefaultEdge> path = dijkstraAlg.getPath(vertex, other.vertex);
        return path.getLength();
    }


    public UndirectedGraphCoordinate getCoordinate(String vertex) {
        if( ! graph.containsVertex(vertex)) {
            throw new RuntimeException("TODO"); //todo
        }
        return new UndirectedGraphCoordinate(graph, vertex);
    }


    private UndirectedGraphCoordinate convertToCoordinate(String neighbor) {
        return new UndirectedGraphCoordinate(graph, neighbor);
    }


    public HashSet<UndirectedGraphCoordinate> getNeighbors() {
        return  Graphs.neighborListOf(graph, vertex)
                .stream()
                .map(this::convertToCoordinate)
                .collect(Collectors.toCollection(HashSet::new));
    }




//    public class UndirectedGraphSpace {
//        private Graph<String, DefaultEdge> graph;
//
//
//
//        private HashSet<UndirectedGraphCoordinate> dataMap;
//
//        Data data;
//        String result = data.
//
//
//    }


    public static class UndirectedGraphBuilder {
        private Graph<String, DefaultEdge> graph;

        public UndirectedGraphBuilder() {
            this.graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
        }

        public UndirectedGraphBuilder addEdge(String v1, String v2) {
            if(!graph.containsVertex(v1)) {
                graph.addVertex(v1);
            }
            if(!graph.containsVertex(v2)) {
                graph.addVertex(v2);
            }
            graph.addEdge(v1, v2);
            return this;
        }

        public UndirectedGraphCoordinate build(String vertex) {
            return new UndirectedGraphCoordinate(graph, vertex);
        }
    }
}
