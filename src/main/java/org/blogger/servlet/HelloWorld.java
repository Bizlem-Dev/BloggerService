package org.blogger.servlet;

import org.apache.felix.scr.annotations.sling.SlingServlet;

@SuppressWarnings("serial")
/*@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
        @Property(name = "service.vendor", value = "The Apache Software Foundation"),
        @Property(name = "sling.servlet.paths", value = { "/servlet/blogger/posts" }),
        @Property(name = "sling.servlet.extensions", value = { "viewText",
                "viewImage", "viewQuote", "viewVideo", "viewAudio", "viewLink",
                "postText", "postImage", "postVideo", "postAudio", "postQuote",
                "postLink", "editText", "editQuote", "deleteNode" })

})*/

@SlingServlet(paths="/bin/mySearchServlet", methods = "POST")
public class HelloWorld extends org.apache.sling.api.servlets.SlingAllMethodsServlet {

	
}
