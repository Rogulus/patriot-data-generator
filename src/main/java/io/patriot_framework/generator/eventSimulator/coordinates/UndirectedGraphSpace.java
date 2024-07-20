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

package io.patriot_framework.generator.eventSimulator.coordinates;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class UndirectedGraphSpace {
    private Graph<String, DefaultEdge> graph;
    private HashMap<String, UndirectedGraphCoordinate> coordinates;

    private UndirectedGraphCoordinate convertToCoordinate(String name) {
        return new UndirectedGraphCoordinate(this, name);
    }

    private UndirectedGraphSpace(Graph<String , DefaultEdge> graph) {
        this.graph = graph;
        coordinates = graph.vertexSet().stream()
                .collect(toMap(Function.identity(), this::convertToCoordinate, (x, y) -> y, HashMap::new));  // lambda will never be called because vertex set is set
    }

    public UndirectedGraphCoordinate getCoordinate(String coordinateName) {
        if( ! coordinates.containsKey(coordinateName)) {
            throw new RuntimeException("TODO"); //todo
        }
        return coordinates.get(coordinateName);
    }

    public double distance(String coordinateName1, String coordinateName2) {
        DijkstraShortestPath<String, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(graph);
        GraphPath<String, DefaultEdge> path = dijkstraAlg.getPath(coordinateName1, coordinateName2);
        return path.getLength();
    }

    public HashSet<UndirectedGraphCoordinate> getNeighbors(String coordinateName) {
        return  Graphs.neighborListOf(graph, coordinateName)
                .stream()
                .map(coordinates::get)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public HashSet<UndirectedGraphCoordinate> getAll() {
        return  graph.vertexSet()
                .stream()
                .map(coordinates::get)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public static class UndirectedGraphSpaceBuilder {
        private Graph<String, DefaultEdge> graph;

        public UndirectedGraphSpaceBuilder() {
            this.graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
        }

        public UndirectedGraphSpaceBuilder addEdge(String coordinateName, String coordinateName2) {
            if(!graph.containsVertex(coordinateName)) {
                graph.addVertex(coordinateName);
            }
            if(!graph.containsVertex(coordinateName2)) {
                graph.addVertex(coordinateName2);
            }
            graph.addEdge(coordinateName, coordinateName2);
            return this;
        }

        public UndirectedGraphSpace build() {
            return new UndirectedGraphSpace(graph);
        }
    }
}