package com.torabipour;

import com.torabipour.bcube.ui.BCubePanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;

public class HomePage extends WebPage {

    private static final long serialVersionUID = 1L;

    public HomePage(final PageParameters parameters) {
        super(parameters);

        add(new BCubePanel("bcube"));

        // TODO Add your page's components here
    }
}
