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
package com.torabipour.bcube.ui;

import com.torabipour.bcube.BCube;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author Mohammad TRB
 */
public class BCubeInfoPanel extends Panel{
    
    public BCubeInfoPanel(String id, BCube bcube) {
        super(id);
        this.add(new ListView<BCube>("bcubeList", Stream.of(bcube).collect(Collectors.toList())) {
            @Override
            protected void populateItem(ListItem<BCube> li) {
                BCube item = li.getModelObject();
                li.add(new Label("n", item.getN()));
                li.add(new Label("k", item.getK()));
                li.add(new Label("numOfSwitches", item.getNumOfSwitches()));
                li.add(new Label("numOfSwitchesPerLevel", item.getNumOfSwitchesPerLevel()));
                li.add(new Label("numOfServers", item.getNumOfServers()));
                li.add(new Label("numOfLinks", item.getNumOfLinks()));
                li.add(new Label("numOfNodes", item.getNumOfNodes()));
                li.add(new Label("numOfLevels", item.getNumOfLevels()));
                li.add(new Label("numOfBcubes", item.getNumOfBCubes()));
            }
        });
    }
    
}
