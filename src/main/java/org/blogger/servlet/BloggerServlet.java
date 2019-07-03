package org.blogger.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.blogger.service.BloggerService;

@SuppressWarnings("serial")
@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
        @Property(name = "service.vendor", value = "The Apache Software Foundation"),
        @Property(name = "sling.servlet.paths", value = { "/servlet/blogger/view" }),
        @Property(name = "sling.servlet.extensions", value = { "addAccount",
                "account", "success", "ajax", "newBlog", "ajaxBlog",
                "searchBlog", "search", "following", "follower", "userContent",
                "userPost", "userDraft", "userQueue", "home", "menu",
                "likeBlog", "deleteBlog", "tagSearch", "followerSearch",
                "edit", "viewBlog", "tagPosts", "blogSearch", "deleteBlogId",
                "deleteAccount", "confirmAccount", "confirmBlog" ,"randomBlog"})

})
public class BloggerServlet extends SlingAllMethodsServlet {

    @Reference
    BloggerService service;

    Session userSession = null;

    protected void doPost(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
        if (request.getRequestPathInfo().getExtension().equals("addAccount")) {
            if (!request.getRemoteUser().equals("anonymous")) {
                service.addUser(request.getRemoteUser(),
                        request.getRemoteUser());
                response.sendRedirect(request.getContextPath()
                        + "/servlet/blogger/view.home");
            } else {
                System.out.println("Blogger Id not created:>"
                        + request.getRemoteUser());
                response.sendRedirect(request.getContextPath()
                        + "/servlet/blogger/view.home");
            }

        } else if (request.getRequestPathInfo().getExtension().equals("ajax")) {

            boolean exist = service.userCheck(
                    request.getParameter("bloggerId"), request.getRemoteUser());
            if (exist == true) {
                response.getOutputStream().print(true);
            } else {
                response.getOutputStream().print(false);
            }

        } else if (request.getRequestPathInfo().getExtension()
                .equals("ajaxBlog")) {

            boolean exist = service.blogCheck(request.getParameter("blogId"));
            if (exist == true) {
                response.getOutputStream().print(true);
            } else {
                response.getOutputStream()
                        .print(request.getParameter("blogId"));
            }

        }

        else if (request.getRequestPathInfo().getExtension().equals("addblog")) {
            userSession = SecurityUtils.getSubject().getSession();
            service.addBlog(request.getParameter("blogId"), request
                    .getParameter("blogTitle"), request
                    .getParameter("description"), request, userSession
                    .getAttribute("blogId").toString(), userSession
                    .getAttribute("bloggerId").toString(), userSession
                    .getAttribute("userId").toString());

            response.sendRedirect(request.getContextPath()
                    + "/servlet/blogger/view.home?id="+userSession
                    .getAttribute("bloggerId").toString());

        } else if (request.getRequestPathInfo().getExtension()
                .equals("editblog")) {

            service.editBlog(request.getParameter("blogNodeId"),
                    request.getParameter("blogId"),
                    request.getParameter("blogTitle"),
                    request.getParameter("blogDescription"),
                    request.getParameter("blogType"),
                    request.getParameter("bloggerId"),
                    request.getParameter("userId"), request);

            response.sendRedirect(request.getContextPath()
                    + "/servlet/blogger/view.home?id="+request.getParameter("bloggerId"));

        }

        else if (request.getRequestPathInfo().getExtension()
                .equals("followBlog")) {
            userSession = SecurityUtils.getSubject().getSession();
            boolean exist = service.followBlog(request.getParameter("blogId"),
                    request.getParameter("param"),
                    userSession.getAttribute("blogId").toString(), userSession
                            .getAttribute("bloggerId").toString(), userSession
                            .getAttribute("userId").toString());
            if (exist == true) {
                response.getOutputStream().print(true);
            } else {
                response.getOutputStream().print(false);
            }

        } else if (request.getRequestPathInfo().getExtension()
                .equals("postLimit")) {
            userSession = SecurityUtils.getSubject().getSession();
            String postList = service.setPostLimit(request
                    .getParameter("number"), userSession.getAttribute("blogId")
                    .toString(), userSession.getAttribute("bloggerId")
                    .toString(), request.getContextPath(), request
                    .getParameter("param"), request.getParameter("key"));

            response.getOutputStream().print(postList + "");

        } else if (request.getRequestPathInfo().getExtension()
                .equals("likeBlog")) {
            userSession = SecurityUtils.getSubject().getSession();
            String data = service.likeBlog(request.getParameter("postPath"),
                    userSession.getAttribute("bloggerId").toString(),
                    userSession.getAttribute("blogId").toString());
            response.getOutputStream().print(data);

        } else if (request.getRequestPathInfo().getExtension()
                .equals("deleteBlog")) {
            userSession = SecurityUtils.getSubject().getSession();
            String data = service.deleteBlog(request.getParameter("postPath"),
                    userSession.getAttribute("bloggerId").toString());

            response.getOutputStream().print(data);

        } else if (request.getRequestPathInfo().getExtension()
                .equals("followerSearch")) {
            userSession = SecurityUtils.getSubject().getSession();
            String data = service.followerSearch(request.getParameter("param"),
                    userSession.getAttribute("bloggerId").toString(), request
                            .getContextPath(),
                    userSession.getAttribute("blogId").toString());
            response.getOutputStream().print(data);

        }
        else if (request.getRequestPathInfo().getExtension()
                .equals("randomBlog")) {
            userSession = SecurityUtils.getSubject().getSession();
            String data = service.randomBlog(
                    userSession.getAttribute("bloggerId").toString(),        
                    userSession.getAttribute("blogId").toString(),
                    request.getContextPath());
            response.getOutputStream().print(data);
        }
        
        else if (request.getRequestPathInfo().getExtension()
                .equals("changeTheme")) {
           
            service.changeTheme(request.getParameter("blogId"),request.getParameter("cssSelect"),request.getParameter("imageSelect") ,request);
                 
            response.getOutputStream().print("successfully");

        }

    }

    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
        if (request.getSession(true).getAttribute("bloggerId") == null) {
            Map<String, String> userDetails = new HashMap<String, String>();
            //userDetails = service.userContent(request.getRemoteUser());
            userDetails = service.userContent(request.getParameter("id"));
            if (userDetails.get("userExists").equals("true")) {
                userSession = SecurityUtils.getSubject().getSession();
                userSession.setAttribute("blogId", userDetails.get("blogId"));
                userSession.setAttribute("bloggerId",
                        userDetails.get("bloggerId"));
                userSession.setAttribute("userId", userDetails.get("userId"));
                response.sendRedirect(request.getContextPath()
                        + "/servlet/blogger/view.home?id="+userDetails.get("bloggerId")); 
//                if(request.getSession(true).getAttribute("redirectUri")==null){
//                    response.sendRedirect(request.getContextPath()
//                            + "/servlet/blogger/view.home"); 
//                }else{
//                    String redirectURI = request.getSession(true).getAttribute("redirectUri").toString();
//                    request.getSession(true).removeAttribute("redirectUri");
//                    response.sendRedirect(redirectURI);
//                }
            } else {
                if(request.getRemoteUser().equals("anonymous")){
                    request.getSession(true).setAttribute("redirectUri", request.getRequestURI()+"?"+request.getQueryString());
                    response.sendRedirect(request.getContextPath()
                            + "/servlet/blogger/view.home"); 
                }else if(request.getParameter("id").indexOf("_") != -1){
                	service.addUser(request.getParameter("id"),
                            request.getRemoteUser());
                	userDetails=service.userContent(request.getParameter("id"));
                	 if (userDetails.get("userExists").equals("true")) {
                	   userSession = SecurityUtils.getSubject().getSession();
                       userSession.setAttribute("blogId", userDetails.get("blogId"));
                       userSession.setAttribute("bloggerId",
                               userDetails.get("bloggerId"));
                       userSession.setAttribute("userId", userDetails.get("userId"));
                	
                    request.setAttribute("blogList", service.allPostNodes(
                            userSession.getAttribute("blogId").toString(),
                            userSession.getAttribute("bloggerId").toString(), 0));

                    request.getRequestDispatcher(
                            "/content/blogger/user/"
                                    + userSession.getAttribute("bloggerId")
                                            .toString() + ".userHome").forward(
                            request, response);
                    //request.getRequestDispatcher("/content/blogger/*.createAccount")
                    //.forward(request, response);
                	 }else{
                		 
                		response.getWriter().print("Not added"); 
                	 }
                }else{
                	userDetails=service.userContent(request.getParameter("id"));
                  	 if (userDetails.get("userExists").equals("true")) {
                  		userSession = SecurityUtils.getSubject().getSession();
                        userSession.setAttribute("blogId", userDetails.get("blogId"));
                        userSession.setAttribute("bloggerId",
                                userDetails.get("bloggerId"));
                        userSession.setAttribute("userId", userDetails.get("userId"));
                 	
                     request.setAttribute("blogList", service.allPostNodes(
                             userSession.getAttribute("blogId").toString(),
                             userSession.getAttribute("bloggerId").toString(), 0));

                     request.getRequestDispatcher(
                             "/content/blogger/user/"
                                     + userSession.getAttribute("bloggerId")
                                             .toString() + ".userHome").forward(
                             request, response);
                  	 }else{
                  		service.addUserBlogOthers(request.getParameter("id"),
                                request.getRemoteUser());
                  		userDetails=service.userContent(request.getParameter("id"));
                  		if (userDetails.get("userExists").equals("true")) {
                  			userSession = SecurityUtils.getSubject().getSession();
                            userSession.setAttribute("blogId", userDetails.get("blogId"));
                            userSession.setAttribute("bloggerId",
                                    userDetails.get("bloggerId"));
                            userSession.setAttribute("userId", userDetails.get("userId"));
                     	
                         request.setAttribute("blogList", service.allPostNodes(
                                 userSession.getAttribute("blogId").toString(),
                                 userSession.getAttribute("bloggerId").toString(), 0));

                         request.getRequestDispatcher(
                                 "/content/blogger/user/"
                                         + userSession.getAttribute("bloggerId")
                                                 .toString() + ".userHome").forward(
                                 request, response);
                  		}
                  	 }
                }
            }
        } else {
        	if (request.getRequestPathInfo().getExtension().equals("addAccountBlog")) {
        		//response.getWriter().println("comp_title: "+request.getParameter("comp_title"));
        		//response.getWriter().println("request.getRemoteUser(): "+request.getRemoteUser());
        		
             //   if (!request.getRemoteUser().equals("anonymous")) {
        		 Map<String, String> userDetails = new HashMap<String, String>();
        		userDetails=service.userContent(request.getParameter("id"));
           	 if (userDetails.get("userExists").equals("true")) {
                  
                   response.sendRedirect(request.getContextPath()+ "/servlet/social/mesg.blog?id="+request.getParameter("id"));
           	 }else{
           		 String res= service.addUserBlogOthers(request.getParameter("id"),
                         request.getRemoteUser());
           	   response.sendRedirect(request.getContextPath()+ "/servlet/social/mesg.blog?id="+request.getParameter("id"));
           	 }
                //    response.getWriter().println("request.res(): "+res);
                    //response.sendRedirect(request.getContextPath()+ "/servlet/social/mesg.blog?id="+request.getParameter("blog_title"));
            //    } else {
           //         System.out.println("Blogger Id not created:>"
            //                + request.getRemoteUser());
           //         response.sendRedirect(request.getContextPath()
           //                 + "/servlet/blogger/view.home");
            //    }
                

            } else if (request.getRequestPathInfo().getExtension().equals("print")) {

                request.getRequestDispatcher(
                        "/content/blogger/*.bloggerService").forward(request,
                        response);
            } else if (request.getRequestPathInfo().getExtension()
                    .equals("account")) {

                request.getRequestDispatcher("/content/blogger/*.createAccount")
                        .forward(request, response);
            } else if (request.getRequestPathInfo().getExtension()
                    .equals("success")) {

                request.getRequestDispatcher("/content/blogger/*.successfully")
                        .forward(request, response);
            } else if (request.getRequestPathInfo().getExtension()
                    .equals("newBlog")) {

                request.getRequestDispatcher("/content/blogger/*.addBlog")
                        .forward(request, response);
            } else if (request.getRequestPathInfo().getExtension()
                    .equals("search")) {
                request.getRequestDispatcher("/content/blogger/*.searchBlog")
                        .forward(request, response);

            } else if (request.getRequestPathInfo().getExtension()
                    .equals("following")) {
                // JSONArray jArr = new JSONArray();
                // request.setAttribute("blogLikeList",jArr);
                request.getRequestDispatcher(
                        "/content/blogger/user/"
                                + request.getParameter("param") + ".following")
                        .forward(request, response);

            } else if (request.getRequestPathInfo().getExtension()
                    .equals("follower")) {
                request.getRequestDispatcher(
                        "/content/blogger/blog/"
                                + request.getParameter("param") + ".follower")
                        .forward(request, response);

            } else if (request.getRequestPathInfo().getExtension()
                    .equals("getUser")) {
                userSession = SecurityUtils.getSubject().getSession();
                response.getOutputStream().print(
                        userSession.getAttribute("blogId").toString()
                                + "------"
                                + userSession.getAttribute("bloggerId")
                                        .toString());
            } else if (request.getRequestPathInfo().getExtension()
                    .equals("userPost")) {
                request.getRequestDispatcher(
                        "/content/blogger/blog/" + request.getParameter("user")
                                + ".userPosts").forward(request, response);

            } else if (request.getRequestPathInfo().getExtension()
                    .equals("userDraft")) {
                request.getRequestDispatcher(
                        "/content/blogger/blog/" + request.getParameter("user")
                                + ".userDrafts").forward(request, response);

            } else if (request.getRequestPathInfo().getExtension()
                    .equals("userQueue")) {
                request.getRequestDispatcher(
                        "/content/blogger/blog/" + request.getParameter("user")
                                + ".userQueues").forward(request, response);

            } else if (request.getRequestPathInfo().getExtension()
                    .equals("home")) {

                userSession = SecurityUtils.getSubject().getSession();
                
                Map<String, String> userDetails = new HashMap<String, String>();
            //    response.getWriter().print(request.getParameter("id"));
        		userDetails=service.userContent(request.getParameter("id"));
        	//	response.getWriter().print(userDetails.get("userExists"));
        	//	response.getWriter().print(userDetails.get("query"));
           	 if (userDetails.get("userExists").equals("true")) {
           		 userSession.setAttribute("blogId", userDetails.get("blogId"));
                 userSession.setAttribute("bloggerId",
                         userDetails.get("bloggerId"));
                 userSession.setAttribute("userId", userDetails.get("userId"));
                 request.setAttribute("blogList", service.allPostNodes(
                         userSession.getAttribute("blogId").toString(),
                         userSession.getAttribute("bloggerId").toString(), 0));

                 request.getRequestDispatcher(
                         "/content/blogger/user/"
                                 + userSession.getAttribute("bloggerId")
                                         .toString() + ".userHome").forward(
                         request, response);
           	 }
                

            }else if (request.getRequestPathInfo().getExtension()
                    .equals("blogpost")) {

                userSession = SecurityUtils.getSubject().getSession();
                request.setAttribute("blogList", service.allPostNodes(
                        userSession.getAttribute("blogId").toString(),
                        userSession.getAttribute("bloggerId").toString(), 0));

                request.getRequestDispatcher(
                        "/content/blogger/user/"
                                + userSession.getAttribute("bloggerId")
                                        .toString() + ".blogPost").forward(
                        request, response);

            } else if (request.getRequestPathInfo().getExtension()
                    .equals("menu")) {

                request.getRequestDispatcher("/content/blogger/*.rightMenu")
                        .forward(request, response);
            }

            else if (request.getRequestPathInfo().getExtension()
                    .equals("userLike")) {
                userSession = SecurityUtils.getSubject().getSession();
                request.getRequestDispatcher(
                        "/content/blogger/user/" + request.getParameter("user")
                                + ".userLike").forward(request, response);

            }

            else if (request.getRequestPathInfo().getExtension().equals("edit")) {
                request.getRequestDispatcher(
                        "/content/blogger/blog/"
                                + request.getParameter("param") + ".editBlog")
                        .forward(request, response);

            } else if (request.getRequestPathInfo().getExtension()
                    .equals("viewBlog")) {
                request.getRequestDispatcher(
                        "/content/blogger/blog/"
                                + request.getParameter("param") + ".viewBlog")
                        .forward(request, response);

            } 
            else if (request.getRequestPathInfo().getExtension()
                    .equals("blogView")) {
            	request.setAttribute("tagList", service.tagList(request.getParameter("param")));
                request.getRequestDispatcher(
                        "/content/blogger/blog/"
                                + request.getParameter("param") + ".blogView")
                        .forward(request, response);

            }
            
            else if (request.getRequestPathInfo().getExtension()
                    .equals("tagPosts")) {
                userSession = SecurityUtils.getSubject().getSession();
                request.setAttribute("blogList", service.searchTagBased(request
                        .getParameter("tagKey"),
                        userSession.getAttribute("blogId").toString(),
                        userSession.getAttribute("bloggerId").toString(), 0));

                request.getRequestDispatcher(
                        "/content/blogger/user/"
                                + userSession.getAttribute("bloggerId")
                                        .toString() + ".viewTagPost").forward(
                        request, response);

            } else if (request.getRequestPathInfo().getExtension()
                    .equals("blogSearch")) {
                userSession = SecurityUtils.getSubject().getSession();
                request.setAttribute("blogList",
                        service.searchBlog(request.getParameter("key")));
                request.getRequestDispatcher(
                        "/content/blogger/user/"
                                + userSession.getAttribute("bloggerId")
                                        .toString() + ".blogSearch").forward(
                        request, response);
            } else if (request.getRequestPathInfo().getExtension()
                    .equals("homeSearch")) {
                request.getRequestDispatcher("/content/blogger/*.searchDiv")
                        .forward(request, response);
            } else if (request.getRequestPathInfo().getExtension()
                    .equals("deleteBlogId")) {
                userSession = SecurityUtils.getSubject().getSession();
                service.deleteBlogId(request.getParameter("blogId"),
                        userSession.getAttribute("bloggerId").toString());
                response.sendRedirect(request.getContextPath()
                        + "/servlet/blogger/view.home");
            } else if (request.getRequestPathInfo().getExtension()
                    .equals("deleteAccount")) {
                userSession = SecurityUtils.getSubject().getSession();
                service.deleteUserAccount(userSession.getAttribute("blogId")
                        .toString(), userSession.getAttribute("bloggerId")
                        .toString());
                userSession.setAttribute("blogId", null);
                userSession.setAttribute("bloggerId",null);
                userSession.setAttribute("userId", null);
                response.sendRedirect(request.getContextPath()
                        + "/servlet/blogger/view.home");

            } else if (request.getRequestPathInfo().getExtension()
                    .equals("confirmAccount")) {
                request.setAttribute("confirm", "confirmAccount");
                request.getRequestDispatcher(
                        "/content/blogger/user/"
                                + userSession.getAttribute("bloggerId")
                                        .toString()
                                + ".deleteAccountConfirmation").forward(
                        request, response);

            } else if (request.getRequestPathInfo().getExtension()
                    .equals("confirmBlog")) {
                request.getRequestDispatcher(
                        "/content/blogger/*.deleteAccountConfirmation")
                        .forward(request, response);

            }
            else if (request.getRequestPathInfo().getExtension()
                    .equals("tagCloud")) {
            	request.setAttribute("tagList", service.tagList(request.getParameter("param")));
                       
                request.getRequestDispatcher(
                        "/content/blogger/blog/"
                                + request.getParameter("param") + ".tagCloud")
                        .forward(request, response);

            }
            
            else if (request.getRequestPathInfo().getExtension()
                    .equals("rePosts")) {
                userSession = SecurityUtils.getSubject().getSession();
                request.setAttribute("blogList", service.reBlogPost(request.getParameter("reblogPath")));
                //request.setAttribute("rePost", "true");
                request.getRequestDispatcher(
                        "/content/blogger/user/"
                                + userSession.getAttribute("bloggerId")
                                        .toString() + ".viewTagPost").forward(
                        request, response);

            }
            
            

        }

    }
}
