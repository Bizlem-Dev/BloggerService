package org.blogger.service.impl;

import javax.script.Bindings;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.scripting.api.BindingsValuesProvider;
import org.blogger.service.BloggerService;

@Component(immediate = true, metatype = false)
@Service
@Properties({
        @Property(name = "service.description", value = "Search For Blogs"),
        @Property(name = "service.vendor", value = "VisionInfoCon"),
        @Property(name = "javax.script.name", value = "any") })
public class BindingServiceImpl implements BindingsValuesProvider {

    @Reference
    private BloggerService bloggerService;

    public void addBindings(Bindings bindings) {

        SlingHttpServletRequest request = (SlingHttpServletRequest) bindings
                .get("request");
        if (request.getPathInfo().equals("/servlet/blogger/view.homeSearch")) {
            bindings.put("blogKey",
                    bloggerService.searchBlogDiv(request.getParameter("key")));
            bindings.put("tagKey",
                    bloggerService.tagSearch(request.getParameter("key"))); 
        }
       
    }

}