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
package com.torabipour.bcube;

import com.torabipour.graph.Graph;
import com.torabipour.vis.GraphGeneratorAbstraction;
import com.torabipour.vis.Point;
import com.torabipour.vis.VisGraph;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Mohammad TRB
 */
public class BCube {

    private int k;
    private int n;
    private int numOfServers;
    private int numOfSwitches;
    private int numOfLevels;
    private int numOfLinks;
    private int numOfBCubes;
    private int numOfNodes;
    private Graph<BCubeNode> graph;
    private int numOfSwitchesPerLevel;
    private List<BCubeNode> serverNodes;
    private Map<Integer, List<BCubeNode>> switchNodes;
    private GraphGeneratorAbstraction<BCubeNode> graphGenerator;
    private File file;

    public BCube(int k, int n) {
        this.k = k;
        this.n = n;
        this.numOfLevels = k + 1;
        this.numOfSwitchesPerLevel = (int) Math.pow(n, k);
        this.numOfServers = (int) Math.pow(n, k + 1);
        this.numOfBCubes = (int) Math.pow(n, k);
        this.numOfSwitches = (int) ((k + 1) * Math.pow(n, k));
        this.numOfLinks = (int) ((k + 1) * Math.pow(n, k + 1));
        this.numOfNodes = numOfServers + numOfSwitches;
    }

    public VisGraph generateVisGraph() {
        if (graph == null) {
            generateGraph();
        }
        if (graphGenerator == null) {
            graphGenerator = new GraphGeneratorAbstraction<BCubeNode>(graph, this::determinePosition);
        }

        return graphGenerator.generateGraph();
    }

    public File generateAdjacencyFile() throws IOException {
        return new BCubeAdjacencyFileGenerator(this).getFile();
    }

    @Deprecated
    public File generateAdjFile() throws IOException {
        if (file == null) {
            file = new File("adjacency.txt");
            FileWriter writer = new FileWriter("adjacency.txt");
            writer.write(this.getAdjacencyString());
            writer.close();
        }
        return file;
    }

    public Graph<BCubeNode> generateGraph() {
        if (graph == null) {
            graph = new Graph<>();
            serverNodes = generateServerNodes();
            switchNodes = generateSwitchNodes();
            addEdgesToGraph(serverNodes, switchNodes);
        }
        return graph;
    }

    private Point determinePosition(BCubeNode node) {
        int level = node.getData().getLevel();
        int x = node.getHorizontalIndex();
        if (node.getData().getType().equals(NodeType.Switch)) {
            level++;
            x = x * n + n / 2;
        }
        return new Point(x * 250, -level * 250);
    }

    private void addEdgesToGraph(List<BCubeNode> serverNodes, Map<Integer, List<BCubeNode>> switchNodes) {
        generateZeroBCubes(serverNodes, switchNodes.get(0));
        for (int level : switchNodes.keySet()) {
            if (level == 0) {
                continue;
            }
            int step = (int) Math.pow(n, level);
            for (int i = 0; i < switchNodes.get(level).size(); i++) {
                int startIndex = i;
                BCubeNode currentSwitch = switchNodes.get(level).get(i);
                for (int j = 0; j < n; j++) {
                    BCubeNode server = serverNodes.get(startIndex);
                    graph.addEdge(currentSwitch, server);
                    graph.addEdge(server, currentSwitch);
                    startIndex += step;
                }

            }
        }
    }

    private void generateZeroBCubes(List<BCubeNode> serverNodes, List<BCubeNode> levelZeroSwitches) {
        for (int i = 0; i < levelZeroSwitches.size(); i++) {
            int index = i * n;
            BCubeNode currentSwitch = levelZeroSwitches.get(i);
            for (int j = 0; j < n; j++) {
                BCubeNode server = serverNodes.get(index);
                graph.addEdge(currentSwitch, server);
                graph.addEdge(server, currentSwitch);
                index++;
            }
        }
    }

    private List<BCubeNode> generateServerNodes() {
        List<BCubeNode> result = new ArrayList<>();
        BCubeNodeData serverData = new BCubeNodeData(NodeType.PhysicalServer, 0);
        for (int i = 0; i < numOfServers; i++) {
            result.add(new BCubeNode(i, serverData, getNodeLabel(serverData, i), i));
        }
        return result;
    }

    private String getNodeLabel(BCubeNodeData data, Integer id) {
        StringBuilder sb = new StringBuilder();
        sb.append("Node Type: \n");
        sb.append(data.getType().getName());
        sb.append("\n");
        sb.append("Level: \n");
        sb.append(data.getLevel());
        sb.append("\n");
        sb.append("Index: \n");
        sb.append(id);
        return sb.toString();
    }

    private Map<Integer, List<BCubeNode>> generateSwitchNodes() {
        Map<Integer, List<BCubeNode>> switchNodes = new HashMap<>();
        int switchIndex = numOfServers;
        for (int i = 0; i <= k; i++) {
            List<BCubeNode> switches = new ArrayList();
            for (int j = 0; j < numOfSwitchesPerLevel; j++) {
                BCubeNodeData data = new BCubeNodeData(NodeType.Switch, i);
                switches.add(new BCubeNode(switchIndex, data, getNodeLabel(data, switchIndex), j));
                switchIndex++;
            }
            switchNodes.put(i, switches);
        }
        return switchNodes;
    }

    public int getK() {
        return k;
    }

    public int getN() {
        return n;
    }

    public int getNumOfServers() {
        return numOfServers;
    }

    public int getNumOfSwitches() {
        return numOfSwitches;
    }

    public int getNumOfLevels() {
        return numOfLevels;
    }

    public int getNumOfLinks() {
        return numOfLinks;
    }

    public int getNumOfBCubes() {
        return numOfBCubes;
    }

    public Graph<BCubeNode> getGraph() {
        return graph;
    }

    public int getNumOfSwitchesPerLevel() {
        return numOfSwitchesPerLevel;
    }

    public String getAdjacencyString() {
        StringBuilder sb = new StringBuilder();

        if (graph == null) {
            generateGraph();
        }
        HashSet<BCubeNode> allNodes = new HashSet<BCubeNode>();
        allNodes.addAll(serverNodes);
        allNodes.addAll(switchNodes.values().stream().flatMap(x -> x.stream()).collect(Collectors.toSet()));
        for (BCubeNode src : allNodes) {
            for (BCubeNode des : allNodes) {
                if (!des.getId().equals(src.getId())) {
                    Boolean areAdj = graph.areAdj(src, des);
                    int num = areAdj ? 1 : 9999;
                    sb.append(src.getId());
                    sb.append(" ");
                    sb.append(des.getId());
                    sb.append(" ");
                    sb.append(num);
                    sb.append("\n");
                }

            }
        }
        return sb.toString();
    }

    public int getNumOfNodes() {
        return numOfNodes;
    }

}
