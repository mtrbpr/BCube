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
import com.torabipour.vis.VisGraphPanel;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.extensions.ajax.AjaxDownloadBehavior;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.util.time.Duration;

/**
 *
 * @author Mohammad TRB
 */
public class BCubePanel extends Panel {

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptReferenceHeaderItem.forReference(new JavaScriptResourceReference(BCubePanel.class, "bootstrap.min.js")));
        response.render(CssReferenceHeaderItem.forReference(new CssResourceReference(BCubePanel.class, "bootstrap.min.css")));
    }

    private int k;
    private int n;
    private FeedbackPanel feedback;
    private Form form;
    private VisGraphPanel graphPanel;
    private File file;

    public BCubePanel(String id) {
        super(id);

        form = new Form("form");
        form.setOutputMarkupId(true);
        this.add(form);

        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);

        graphPanel = new VisGraphPanel("graph");
        graphPanel.setVisible(false);
        form.add(graphPanel);

        form.add(new EmptyPanel("bcubeInfo"));

        form.add(new TextField("kField", new IModel<String>() {
            @Override
            public void detach() {
            }

            @Override
            public void setObject(String object) {
                try {
                    k = Integer.parseInt(object);
                } catch (Exception ex) {
                }
            }

            @Override
            public String getObject() {
                return String.valueOf(k);
            }

        }).setOutputMarkupId(true));

        form.add(new TextField("nField", new IModel<String>() {
            @Override
            public void detach() {
            }

            @Override
            public void setObject(String object) {
                try {
                    n = Integer.parseInt(object);
                } catch (Exception ex) {
                }
            }

            @Override
            public String getObject() {
                return String.valueOf(k);
            }

        }).setOutputMarkupId(true));

        form.add(new AjaxButton("showGraph") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                try {
                    validateInput(target);
                } catch (IllegalArgumentException ex) {
                    return;
                }

                BCube bcube = new BCube(k, n);
                form.addOrReplace(new BCubeInfoPanel("bcubeInfo", bcube));
                graphPanel.setVisible(true);
                graphPanel.renderGraph(target, bcube.generateVisGraph());
                target.add(form);
            }
        }.setOutputMarkupId(true));

        FileResource fileResource = new FileResource() {
            @Override
            protected File getFile() {
                try {
                    return new BCube(k, n).generateAdjacencyFile();
                } catch (IOException ex) {
                    return null;
                }
            }
        };

        final AjaxDownloadBehavior download = new AjaxDownloadBehavior(fileResource) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onBeforeDownload(IPartialPageRequestHandler handler) {
            }

            @Override
            protected void onDownloadSuccess(AjaxRequestTarget target) {
            }

            @Override
            protected void onDownloadFailed(AjaxRequestTarget target) {
            }

            @Override
            protected void onDownloadCompleted(AjaxRequestTarget target) {
            }
        };
        download.setLocation(AjaxDownloadBehavior.Location.Blob);

        form.add(download);

        form.add(new AjaxButton("generateFile") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                try {
                    validateInput(target);
                    download.initiate(target);
                    form.addOrReplace(new BCubeInfoPanel("bcubeInfo", new BCube(k, n)));
                    success("File successfully created!");
                } catch (Exception ex) {
                    error("Unexpected error occurred");
                } finally {
                    target.add(form);
                }
            }
        }.setOutputMarkupId(true));

    }

    private void validateInput(AjaxRequestTarget target) {
        if (k <= 0) {
            feedback.error("parameter k is not set properly");
            target.add(form);
            throw new IllegalArgumentException();
        }
        if (n <= 0) {
            feedback.error("parameter n is not set properly");
            target.add(form);
            throw new IllegalArgumentException();
        }
    }

    private abstract class FileResource extends ResourceStreamResource {

        private static final long serialVersionUID = 1L;

        private int count = 0;

        public FileResource() {

            setFileName("File-from-IResource.txt");
            setCacheDuration(Duration.NONE);
        }

        @Override
        protected IResourceStream getResourceStream(Attributes attributes) {
            // simulate delay
            try {
                TimeUnit.MILLISECONDS.sleep(3000);
            } catch (InterruptedException e) {
            }

            count++;
            if (count == 3) {
                count = 0;
                throw new AbortWithHttpErrorCodeException(400);
            }

            return new FileResourceStream(getFile());
        }

        protected abstract File getFile();
    }
}
