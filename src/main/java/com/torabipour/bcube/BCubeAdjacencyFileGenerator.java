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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Mohammad TRB
 */
public class BCubeAdjacencyFileGenerator {

    private BCube bcube;
    private File file;

    public BCubeAdjacencyFileGenerator(BCube bcube) {
        this.bcube = bcube;
    }

    public File getFile() throws IOException {
        if (file == null) {
            file = new File("adjacency.txt");
            FileWriter writer = new FileWriter("adjacency.txt");

            int numOfNodes = bcube.getNumOfNodes();
            for (int i = 0; i < numOfNodes; i++) {
                for (int j = 0; j < numOfNodes; j++) {
                    if (i == j) {
                        continue;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(i);
                    sb.append(" ");
                    sb.append(j);
                    sb.append(" ");
                    sb.append(areIndexesAdjacent(i, j) ? "1" : "9999");
                    sb.append("\n");
                    writer.write(sb.toString());
                }
            }
            writer.close();
        }
        return file;
    }

    private boolean areIndexesAdjacent(int i, int j) {
        if (areNodesSameType(i, j)) {
            return false;
        }
        int swich = isSwitch(i) ? i : j;
        int server = isServer(i) ? i : j;

        return areServerAndSwitchAdjacent(server, swich);
    }

    private boolean isSwitch(int i) {
        return i >= bcube.getNumOfServers();
    }

    private boolean isServer(int i) {
        return !isSwitch(i);
    }

    private boolean areNodesSameType(int i, int j) {
        return (isServer(i) && isServer(j)) || (isSwitch(i) && isSwitch(j));
    }

    private boolean areServerAndSwitchAdjacent(int server, int swich) {
        return getServersOfSwitch(swich).contains(server);
    }

    private int extractLevel(int swich) {
        return (swich - bcube.getNumOfServers()) / bcube.getNumOfSwitchesPerLevel();
    }

    private HashSet<Integer> getServersOfSwitch(int swich) {
        HashSet<Integer> result = new HashSet<>();
        int swichLevel = extractLevel(swich);
        if (swichLevel == 0) {
            int startServerIndex = (swich - bcube.getNumOfServers()) * bcube.getN();
            for(int j =0; j < bcube.getN() ; j++){
                result.add(startServerIndex);
                startServerIndex ++;
            }
            return result;
        } else {
            int startServerIndex = (swich - bcube.getNumOfServers()) % bcube.getNumOfSwitchesPerLevel();
            int step = (int)Math.pow(bcube.getN(), extractLevel(swich));
            for(int j =0; j < bcube.getN() ; j++){
                result.add(startServerIndex);
                startServerIndex += step;
            }
            return result;
        }
    }
}
