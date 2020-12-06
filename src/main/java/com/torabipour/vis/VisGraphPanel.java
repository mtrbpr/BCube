/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torabipour.vis;

import com.google.gson.Gson;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 *
 * @author Mohammad TRB
 */
public class VisGraphPanel extends Panel {

    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(VisGraphPanel.class, "vis.min.js")));
        response.render(CssHeaderItem.forReference(new CssResourceReference(VisGraphPanel.class, "vis.min.css")));
    }

    public VisGraphPanel(String id) {
        super(id);
        this.setOutputMarkupId(true);
    }


    public void renderGraph(AjaxRequestTarget target,VisGraph graph) {
        String data = new Gson().toJson(graph);
        
        target.appendJavaScript("var network = new vis.Network(container, " + data + ", options);");
    }
}
