/*
 * Copyright 2020 mohammad.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.torabipour.graph.algorithm;

import com.torabipour.graph.Graph;
import com.torabipour.graph.Node;
import com.torabipour.graph.TraverseDirection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 *
 * @author Mohammad TRB
 */
public class GraphBFSProvider<N extends Node>  {
    
    private BFS<N> bfs ;

    public GraphBFSProvider(Graph<N> graph,N root, Consumer<N> consumer, Predicate<N> checker, Predicate<N> stopCondition) {
        bfs = new BFS<N>(TraverseDirection.ANY, root, consumer, checker, stopCondition){
            @Override
            protected Map<TraverseDirection, Set<N>> adj(N node) {
                Map<TraverseDirection, Set<N>> result = new HashMap<>();
                result.put(TraverseDirection.ANY, graph.adj(node));
                return result;
            }
        };
    }

    public BFS<N> getBfs() {
        return bfs;
    }

}
