package org.blogger.servlet;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.blogger.service.BloggerPostService;

@SuppressWarnings("serial")
@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
        @Property(name = "service.vendor", value = "The Apache Software Foundation"),
        @Property(name = "sling.servlet.paths", value = { "/servlet/blogger/posts" }),
        @Property(name = "sling.servlet.extensions", value = { "viewText",
                "viewImage", "viewQuote", "viewVideo", "viewAudio", "viewLink",
                "postText", "postImage", "postVideo", "postAudio", "postQuote",
                "postLink", "editText", "editQuote", "deleteNode" })

})
public class BloggerPostsServlet extends SlingAllMethodsServlet {

    @Reference
    private BloggerPostService service;

    @Override
    protected void doPost(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
        if (request.getRequestPathInfo().getExtension().equals("postText")) {

            if (request.getParameter("type").equals("Queued")) {
                service.postText(request.getParameter("bloggerId"),
                        request.getParameter("blogId"),
                        request.getParameter("text"),
                        request.getParameter("title"),
                        request.getParameter("postId"),
                        request.getParameter("tags"),
                        request.getParameter("postType"),
                        request.getParameter("accessType"),
                        request.getParameter("type"),
                        request.getParameter("queuedTime"),
                        request.getParameter("edit"),
                        request.getParameter("formerPostType"),
                        request.getParameter("reBlogFlag"), 
                        request.getParameter("sourceId"), 
                        request.getParameter("reBloggerId"));
            } else {
                service.postText(request.getParameter("bloggerId"),
                        request.getParameter("blogId"),
                        request.getParameter("text"),
                        request.getParameter("title"),
                        request.getParameter("postId"),
                        request.getParameter("tags"),
                        request.getParameter("postType"),
                        request.getParameter("accessType"),
                        request.getParameter("type"), "",
                        request.getParameter("edit"),
                        request.getParameter("formerPostType"),
                        request.getParameter("reBlogFlag"), 
                        request.getParameter("sourceId"), 
                        request.getParameter("reBloggerId"));
            }
        } else if (request.getRequestPathInfo().getExtension()
                .equals("postImage")) {

            if (request.getParameter("type").equals("Queued")) {
                service.postImage(request.getParameter("blogId"),
                        request.getParameter("type"),
                        request.getParameter("description"),
                        request.getParameter("tags"),
                        request.getParameter("accessType"),
                        request.getParameter("bloggerId"),
                        request.getParameter("postId"),
                        request.getParameter("queuedTime"), request,
                        request.getParameter("edit"),
                        request.getParameter("formerPostType"), 
                        request.getParameter("reBlogFlag"), 
                        request.getParameter("sourceId"), 
                        request.getParameter("reBloggerId"));
            } else {
                service.postImage(request.getParameter("blogId"),
                        request.getParameter("type"),
                        request.getParameter("description"),
                        request.getParameter("tags"),
                        request.getParameter("accessType"),
                        request.getParameter("bloggerId"),
                        request.getParameter("postId"),
                        request.getParameter("queuedTime"), request,
                        request.getParameter("edit"),
                        request.getParameter("formerPostType"),
                        request.getParameter("reBlogFlag"), 
                        request.getParameter("sourceId"), 
                        request.getParameter("reBloggerId"));
            }
        } else if (request.getRequestPathInfo().getExtension()
                .equals("postLink")) {

            if (request.getParameter("type").equals("Queued")) {
                service.postLink(request.getParameter("bloggerId"),
                        request.getParameter("blogId"),
                        request.getParameter("link"),
                        request.getParameter("title"),
                        request.getParameter("description"),
                        request.getParameter("postId"),
                        request.getParameter("tags"),
                        request.getParameter("accessType"),
                        request.getParameter("type"),
                        request.getParameter("queuedTime"),
                        request.getParameter("edit"),
                        request.getParameter("formerPostType"),
                        request.getParameter("reBlogFlag"), 
                        request.getParameter("sourceId"), 
                        request.getParameter("reBloggerId"));
            } else {
                service.postLink(request.getParameter("bloggerId"),
                        request.getParameter("blogId"),
                        request.getParameter("link"),
                        request.getParameter("title"),
                        request.getParameter("description"),
                        request.getParameter("postId"),
                        request.getParameter("tags"),
                        request.getParameter("accessType"),
                        request.getParameter("type"), "",
                        request.getParameter("edit"),
                        request.getParameter("formerPostType"),
                        request.getParameter("reBlogFlag"), 
                        request.getParameter("sourceId"), 
                        request.getParameter("reBloggerId"));
            }
        } else if (request.getRequestPathInfo().getExtension()
                .equals("postVideo")) {

            if (request.getParameter("type").equals("Queued")) {
                service.postVideo(request.getParameter("blogId"),
                        request.getParameter("type"),
                        request.getParameter("description"),
                        request.getParameter("tags"),
                        request.getParameter("accessType"),
                        request.getParameter("bloggerId"),
                        request.getParameter("postId"),
                        request.getParameter("queuedTime"), request,
                        request.getParameter("edit"),
                        request.getParameter("formerPostType"),
                        request.getParameter("videoLinkFlag"),
                        request.getParameter("videoLink"),
                        request.getParameter("videoIframe"),
                        request.getParameter("videoSource"),
                        request.getParameter("videoThumbnail"),
                        request.getParameter("reBlogFlag"), 
                        request.getParameter("sourceId"), 
                        request.getParameter("reBloggerId"));
            } else {
                service.postVideo(request.getParameter("blogId"),
                        request.getParameter("type"),
                        request.getParameter("description"),
                        request.getParameter("tags"),
                        request.getParameter("accessType"),
                        request.getParameter("bloggerId"),
                        request.getParameter("postId"), "", request,
                        request.getParameter("edit"),
                        request.getParameter("formerPostType"),
                        request.getParameter("videoLinkFlag"),
                        request.getParameter("videoLink"),
                        request.getParameter("videoIframe"),
                        request.getParameter("videoSource"),
                        request.getParameter("videoThumbnail"),
                        request.getParameter("reBlogFlag"), 
                        request.getParameter("sourceId"), 
                        request.getParameter("reBloggerId"));
            }
        } else if (request.getRequestPathInfo().getExtension()
                .equals("postAudio")) {

            if (request.getParameter("type").equals("Queued")) {
                service.postAudio(request.getParameter("blogId"),
                        request.getParameter("type"),
                        request.getParameter("description"),
                        request.getParameter("tags"),
                        request.getParameter("accessType"),
                        request.getParameter("bloggerId"),
                        request.getParameter("postId"),
                        request.getParameter("queuedTime"), request,
                        request.getParameter("edit"),
                        request.getParameter("formerPostType"),
                        request.getParameter("audioLinkFlag"),
                        request.getParameter("audioLink"),
                        request.getParameter("audioTrack"),
                        request.getParameter("audioArtist"),
                        request.getParameter("audioAlbum"),
                        request.getParameter("coverPage"),
                        request.getParameter("reBlogFlag"), 
                        request.getParameter("sourceId"), 
                        request.getParameter("reBloggerId"));
            } else {
                service.postAudio(request.getParameter("blogId"),
                        request.getParameter("type"),
                        request.getParameter("description"),
                        request.getParameter("tags"),
                        request.getParameter("accessType"),
                        request.getParameter("bloggerId"),
                        request.getParameter("postId"), "", request,
                        request.getParameter("edit"),
                        request.getParameter("formerPostType"),
                        request.getParameter("audioLinkFlag"),
                        request.getParameter("audioLink"),
                        request.getParameter("audioTrack"),
                        request.getParameter("audioArtist"),
                        request.getParameter("audioAlbum"),
                        request.getParameter("coverPage"),
                        request.getParameter("reBlogFlag"), 
                        request.getParameter("sourceId"), 
                        request.getParameter("reBloggerId"));
            }
        } else if (request.getRequestPathInfo().getExtension()
                .equals("deleteNode")) {
            response.getOutputStream().print(
                    service.deleteNode(request.getParameter("path")));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("getVideoData")) {
            response.getOutputStream().print(
                    service.extractVideoData(request.getParameter("type"),
                            request.getParameter("videoLink")));

        }
    }

    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
        if (request.getRequestPathInfo().getExtension().equals("viewText")) {
            request.setAttribute(
                    "blogID",
                    service.getUserBlog(request.getSession(true).getAttribute(
                            "bloggerId")
                            + ""));
            request.getRequestDispatcher("/content/blogger/*.viewText")
                    .forward(request, response);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("editText")) {
            request.setAttribute(
                    "blogID",
                    service.getUserBlog(request.getSession(true).getAttribute(
                            "bloggerId")
                            + ""));
            request.getRequestDispatcher(
                    "/content/blogger/blog/" + request.getParameter("blogId")
                            + "/" + request.getParameter("blogType") + "/"
                            + request.getParameter("postId") + ".viewText")
                    .forward(request, response);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("viewQuote")) {
            request.setAttribute(
                    "blogID",
                    service.getUserBlog(request.getSession(true).getAttribute(
                            "bloggerId")
                            + ""));
            request.getRequestDispatcher("/content/blogger/*.viewQuote")
                    .forward(request, response);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("editQuote")) {
            request.setAttribute(
                    "blogID",
                    service.getUserBlog(request.getSession(true).getAttribute(
                            "bloggerId")
                            + ""));
            request.getRequestDispatcher(
                    "/content/blogger/blog/" + request.getParameter("blogId")
                            + "/" + request.getParameter("blogType") + "/"
                            + request.getParameter("postId") + ".viewQuote")
                    .forward(request, response);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("viewImage")) {
            request.setAttribute(
                    "blogID",
                    service.getUserBlog(request.getSession(true).getAttribute(
                            "bloggerId")
                            + ""));
            request.getRequestDispatcher("/content/blogger/*.viewImage")
                    .forward(request, response);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("editImage")) {
            request.setAttribute(
                    "blogID",
                    service.getUserBlog(request.getSession(true).getAttribute(
                            "bloggerId")
                            + ""));
            request.getRequestDispatcher(
                    "/content/blogger/blog/" + request.getParameter("blogId")
                            + "/" + request.getParameter("blogType") + "/"
                            + request.getParameter("postId") + ".viewImage")
                    .forward(request, response);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("viewLink")) {
            request.setAttribute(
                    "blogID",
                    service.getUserBlog(request.getSession(true).getAttribute(
                            "bloggerId")
                            + ""));
            request.getRequestDispatcher("/content/blogger/*.viewLink")
                    .forward(request, response);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("editLink")) {
            request.setAttribute(
                    "blogID",
                    service.getUserBlog(request.getSession(true).getAttribute(
                            "bloggerId")
                            + ""));
            request.getRequestDispatcher(
                    "/content/blogger/blog/" + request.getParameter("blogId")
                            + "/" + request.getParameter("blogType") + "/"
                            + request.getParameter("postId") + ".viewLink")
                    .forward(request, response);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("viewVideo")) {
            request.setAttribute(
                    "blogID",
                    service.getUserBlog(request.getSession(true).getAttribute(
                            "bloggerId")
                            + ""));
            request.getRequestDispatcher("/content/blogger/*.viewVideo")
                    .forward(request, response);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("editVideo")) {
            request.setAttribute(
                    "blogID",
                    service.getUserBlog(request.getSession(true).getAttribute(
                            "bloggerId")
                            + ""));
            request.getRequestDispatcher(
                    "/content/blogger/blog/" + request.getParameter("blogId")
                            + "/" + request.getParameter("blogType") + "/"
                            + request.getParameter("postId") + ".viewVideo")
                    .forward(request, response);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("viewAudio")) {
            request.setAttribute(
                    "blogID",
                    service.getUserBlog(request.getSession(true).getAttribute(
                            "bloggerId")
                            + ""));
            request.getRequestDispatcher("/content/blogger/*.viewAudio")
                    .forward(request, response);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("editAudio")) {
            request.setAttribute(
                    "blogID",
                    service.getUserBlog(request.getSession(true).getAttribute(
                            "bloggerId")
                            + ""));
            request.getRequestDispatcher(
                    "/content/blogger/blog/" + request.getParameter("blogId")
                            + "/" + request.getParameter("blogType") + "/"
                            + request.getParameter("postId") + ".viewAudio")
                    .forward(request, response);
        }else if (request.getRequestPathInfo().getExtension()
                .equals("getUrlContent")) {
            response.getOutputStream().print(
                    service.getWebsiteContent(request.getParameter("paramUrl"))
                            + "");
        }
    }

}
