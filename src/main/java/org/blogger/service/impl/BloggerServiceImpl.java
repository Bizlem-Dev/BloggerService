package org.blogger.service.impl;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.blogger.service.BloggerPostService;
import org.blogger.service.BloggerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.apache.sling.api.SlingHttpServletResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class BloggerServiceImpl.
 */
@Component(configurationFactory = true)
@Service(BloggerService.class)
@Properties({ @Property(name = "BloggerService", value = "blogging") })
public class BloggerServiceImpl implements BloggerService {

    /** The repos. */
    @Reference
    private SlingRepository repos;

    /** The post service. */
    @Reference
    private BloggerPostService postService;

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#addUser(java.lang.String, java.lang.String)
     */
    public void addUser(String bloggerId, String userId) {
        Session session;
        Node node, nodeBlog = null, userNode, blogNode, nodeblogger, userDes = null;

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            node = session.getNode("/content");
String name=node.getNode("user").getNode(bloggerId).getProperty("name").getString();
            if (node.hasNode("blogger")) {
                blogNode = node.getNode("blogger");

            } else {
                blogNode = node.addNode("blogger");
            }

            if (blogNode.hasNode("user")) {
                userNode = blogNode.getNode("user");

            } else {
                userNode = blogNode.addNode("user");
            }

            if (blogNode.hasNode("blog")) {
                nodeblogger = blogNode.getNode("blog");

            } else {
                nodeblogger = blogNode.addNode("blog");
            }

            userDes = userNode.addNode(bloggerId);
            userDes.setProperty("bloggerId", bloggerId);
            userDes.setProperty("userId", userId);

            if (nodeblogger.hasNode(bloggerId)) {
                if (nodeblogger.hasNode(bloggerId + "blog")) {
                    int i = 1;
                    while (i > 0) {
                        if (nodeblogger.hasNode(bloggerId + i)) {
                            i++;
                        } else {
                            nodeBlog = nodeblogger.addNode(bloggerId + i);
                            nodeBlog.setProperty("blogId", bloggerId + i);
                            break;
                        }

                    }
                } else {
                    nodeBlog = nodeblogger.addNode(bloggerId + "blog");
                    nodeBlog.setProperty("blogId", bloggerId + "blog");
                }
            } else {
                nodeBlog = nodeblogger.addNode(bloggerId);
                nodeBlog.setProperty("blogId", bloggerId);
            }
            nodeBlog.setProperty("blogType", "primary");
            nodeBlog.setProperty("bloggerId", bloggerId);
            nodeBlog.setProperty("userId", userId);
            nodeBlog.setProperty("blogTitle", "Untitled");
            nodeBlog.setProperty("blogDescription", "Description");
            nodeBlog.setProperty("themeCss", "blogView2.css");
            nodeBlog.setProperty("themeImage", "page-bg.jpg");
            nodeBlog.setProperty("blogerName",name);
            userDes.setProperty("blogId", nodeBlog.getName());
            
            session.save();
        } catch (LoginException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }

    }
    
    // create blog for otther than user
    public String addUserBlogOthers(String bloggerId, String userId) {
        Session session;
        Node node, nodeBlog = null, userNode, blogNode, nodeblogger, userDes = null;

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            node = session.getNode("/content");
//String name=node.getNode("user").getNode(bloggerId).getProperty("name").getString();
            if (node.hasNode("blogger")) {
                blogNode = node.getNode("blogger");

            } else {
                blogNode = node.addNode("blogger");
            }

            if (blogNode.hasNode("user")) {
                userNode = blogNode.getNode("user");

            } else {
                userNode = blogNode.addNode("user");
            }

            if (blogNode.hasNode("blog")) {
                nodeblogger = blogNode.getNode("blog");

            } else {
                nodeblogger = blogNode.addNode("blog");
            }

            userDes = userNode.addNode(bloggerId);
            userDes.setProperty("bloggerId", bloggerId);
            userDes.setProperty("userId", userId);

            if (nodeblogger.hasNode(bloggerId)) {
                if (nodeblogger.hasNode(bloggerId + "blog")) {
                    int i = 1;
                    while (i > 0) {
                        if (nodeblogger.hasNode(bloggerId + i)) {
                            i++;
                        } else {
                            nodeBlog = nodeblogger.addNode(bloggerId + i);
                            nodeBlog.setProperty("blogId", bloggerId + i);
                            break;
                        }

                    }
                } else {
                    nodeBlog = nodeblogger.addNode(bloggerId + "blog");
                    nodeBlog.setProperty("blogId", bloggerId + "blog");
                }
            } else {
                nodeBlog = nodeblogger.addNode(bloggerId);
                nodeBlog.setProperty("blogId", bloggerId);
            }
            nodeBlog.setProperty("blogType", "primary");
            nodeBlog.setProperty("bloggerId", bloggerId);
            nodeBlog.setProperty("userId", userId);
            nodeBlog.setProperty("blogTitle", "Untitled");
            nodeBlog.setProperty("blogDescription", "Description");
            nodeBlog.setProperty("themeCss", "blogView2.css");
            nodeBlog.setProperty("themeImage", "page-bg.jpg");
           // nodeBlog.setProperty("blogerName",name);
            nodeBlog.setProperty("blogerName",bloggerId);
            userDes.setProperty("blogId", nodeBlog.getName());
            
            session.save();
           // return userDes.toString();
           
        } catch (LoginException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }
        return userDes.toString();
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#userCheck(java.lang.String, java.lang.String)
     */
    public boolean userCheck(String bloggerId, String userId) {

        Session session = null;
        Node node, bloggerNode, blogNode, userNode = null;

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            node = session.getNode("/content");

            if (node.hasNode("blogger")) {
                blogNode = node.getNode("blogger");
                if (blogNode.hasNode("user")) {
                    userNode = blogNode.getNode("user");
                    Node blogIdNode = blogNode.getNode("blog");
                    if (userNode.hasNode(bloggerId)
                            || blogIdNode.hasNode(bloggerId)) {

                        return true;
                    }
                    NodeIterator userNodes = userNode.getNodes();
                    while (userNodes.hasNext()) {
                        bloggerNode = userNodes.nextNode();
                        if (bloggerNode.getProperty("userId").getString()
                                .equals(userId)) {
                            return true;
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        } finally {
            session.logout();
        }

        return false;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#blogCheck(java.lang.String)
     */
    public boolean blogCheck(String blogId) {

        Session session = null;
        Node node, bloggerNode, blogNode = null;

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            node = session.getNode("/content");

            if (node.hasNode("blogger")) {
                bloggerNode = node.getNode("blogger");
                if (bloggerNode.hasNode("blog")) {
                    blogNode = bloggerNode.getNode("blog");
                    if (blogNode.hasNode(blogId)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        } finally {
            session.logout();
        }

        return false;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#addBlog(java.lang.String, java.lang.String, java.lang.String, org.apache.sling.api.SlingHttpServletRequest, java.lang.String, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("deprecation")
    public void addBlog(String blogId, String blogTitle, String description,
            SlingHttpServletRequest request, String userBlogId,
            String userBloggerId, String userId) throws IOException {
        Session session = null;
        Node node, bloggerNode, blogNode, newBlog, fileNode, jcrNode, userNode, blogIdNode = null;
        String path = request.getSession().getServletContext().getRealPath("/")
                + "/temp/";
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            node = session.getNode("/content");

            if (node.hasNode("blogger")) {
                bloggerNode = node.getNode("blogger");
            } else {
                bloggerNode = node.addNode("blogger");
            }
            if (bloggerNode.hasNode("blog")) {
                blogNode = bloggerNode.getNode("blog");

            } else {
                blogNode = bloggerNode.addNode("blog");
            }

            if (blogNode.hasNode(blogId)) {
                newBlog = blogNode.getNode(blogId);

            } else {
                newBlog = blogNode.addNode(blogId);

            }

            if (bloggerNode.hasNode("user")) {
                userNode = bloggerNode.getNode("user/" + userBloggerId);
                if (userNode.hasNode("blogId")) {
                    blogIdNode = userNode.getNode("blogId");
                } else {
                    blogIdNode = userNode.addNode("blogId");
                }
                if (!blogIdNode.hasNode(blogId)) {
                    blogIdNode.addNode(blogId);
                }

            }

            newBlog.setProperty("blogId", blogId);
            newBlog.setProperty("blogType", "secondary");
            newBlog.setProperty("bloggerId", userBloggerId);
            newBlog.setProperty("userId", userId);
            newBlog.setProperty("blogTitle", blogTitle);
            newBlog.setProperty("blogDescription", description);
            newBlog.setProperty("themeCss", "blogView2.css");
            newBlog.setProperty("themeImage", "page-bg.jpg");
            if (request.getParameter("files").getBytes().length > 0) {
                fileNode = newBlog.addNode("avatar", "nt:file");
                jcrNode = fileNode.addNode("jcr:content", "nt:resource");
                for (Entry<String, RequestParameter[]> e : request
                        .getRequestParameterMap().entrySet()) {
                    for (RequestParameter p : e.getValue()) {
                        if (!p.isFormField()) {
                            //
                            postService
                                    .generateThumbnail(path, blogId + "avtar",
                                            p.getInputStream(), 70, true, 70);
                            File fileThumbnail = new File(path + blogId
                                    + "avtar.jpg");
                            InputStream thumbnailStream = new FileInputStream(
                                    fileThumbnail);
                            jcrNode.setProperty("jcr:data", thumbnailStream);
                            fileThumbnail.delete();
                        }
                    }
                }

                jcrNode.setProperty("jcr:lastModified", Calendar.getInstance());
                jcrNode.setProperty("jcr:mimeType", "image/jpg");
            }
            session.save();

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            session.logout();
        }
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#editBlog(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.apache.sling.api.SlingHttpServletRequest)
     */
    @SuppressWarnings("deprecation")
    public void editBlog(String blogNodeId, String blogId, String title,
            String description, String blogType, String bloggerId,
            String userId, SlingHttpServletRequest request) throws IOException {

        Session session = null;
        Node node, blogNode = null, fileNode, jcrNode, newBlog = null;
        String path = request.getSession().getServletContext().getRealPath("/")
                + "/temp/";
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            node = session.getNode("/content/blogger/blog");
            if (blogNodeId.equals(blogId)) {
                blogNode = node.getNode(blogNodeId);

            } else {
                if (node.hasNode(blogNodeId)) {
                    newBlog = node.getNode(blogNodeId);
                    newBlog.remove();
                    blogNode = node.addNode(blogId);

                }
            }
            blogNode.setProperty("blogId", blogId);
            blogNode.setProperty("blogType", blogType);
            blogNode.setProperty("bloggerId", bloggerId);
            blogNode.setProperty("userId", userId);
            blogNode.setProperty("blogTitle", title);
            blogNode.setProperty("blogDescription", description);

            if (request.getParameter("files").getBytes().length > 0) {
                if (blogNode.hasNode("avatar")) {
                    fileNode = blogNode.getNode("avatar");
                } else {
                    fileNode = blogNode.addNode("avatar", "nt:file");
                }
                if (fileNode.hasNode("jcr:content")) {
                    jcrNode = fileNode.getNode("jcr:content");
                } else {
                    jcrNode = fileNode.addNode("jcr:content", "nt:resource");
                }

                for (Entry<String, RequestParameter[]> e : request
                        .getRequestParameterMap().entrySet()) {
                    for (RequestParameter p : e.getValue()) {
                        if (!p.isFormField()) {
                            //
                            postService
                                    .generateThumbnail(path, blogId + "avtar",
                                            p.getInputStream(), 70, true, 70);
                            File fileThumbnail = new File(path + blogId
                                    + "avtar.jpg");
                            InputStream thumbnailStream = new FileInputStream(
                                    fileThumbnail);
                            jcrNode.setProperty("jcr:data", thumbnailStream);
                            fileThumbnail.delete();
                            jcrNode.setProperty("jcr:lastModified",
                                    Calendar.getInstance());
                            jcrNode.setProperty("jcr:mimeType", "image/jpg");
                        }
                    }
                }
            }

            session.save();
        } catch (LoginException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        } finally {
            session.logout();
        }
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#followBlog(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public boolean followBlog(String blogId, String param, String userBlogId,
            String userBloggerId, String userId) {
        Session session = null;
        Node node, blogNode, followNode, followerNode, addfollower, follower, addFollowing, following, removeNode, removeFollowing = null;

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            if (param.equals("follow")) {
                node = session.getNode("/content/blogger/blog");
                if (node.hasNode(blogId)) {
                    followerNode = node.getNode(blogId);
                    // followerNode.setProperty("flag", "1");
                    if (followerNode.hasNode("followers")) {
                        addfollower = followerNode.getNode("followers");
                    } else {
                        addfollower = followerNode.addNode("followers");
                    }

                    if (addfollower.hasNode(userBlogId)) {
                        follower = addfollower.getNode(userBlogId);
                    } else {
                        follower = addfollower.addNode(userBlogId);
                    }
                }
                blogNode = session.getNode("/content/blogger/user");
                if (blogNode.hasNode(userBloggerId)) {
                    followNode = blogNode.getNode(userBloggerId);
                    if (followNode.hasNode("followings")) {
                        addFollowing = followNode.getNode("followings");
                    } else {
                        addFollowing = followNode.addNode("followings");
                    }

                    if (addFollowing.hasNode(blogId)) {
                        following = addFollowing.getNode(blogId);
                    } else {
                        following = addFollowing.addNode(blogId);
                    }
                }
            } else {
                node = session.getNode("/content/blogger/blog");
                if (node.hasNode(blogId)) {
                    followerNode = node.getNode(blogId);
                    // followerNode.setProperty("flag", "0");
                    if (followerNode.hasNode("followers")) {
                        follower = followerNode.getNode("followers");
                        if (follower.hasNode(userBlogId)) {
                            removeNode = follower.getNode(userBlogId);
                            removeNode.remove();
                        }

                    }
                }
                blogNode = session.getNode("/content/blogger/user");
                if (blogNode.hasNode(userBloggerId)) {
                    followNode = blogNode.getNode(userBloggerId);
                    if (followNode.hasNode("followings")) {
                        following = followNode.getNode("followings");
                        if (following.hasNode(blogId)) {
                            removeFollowing = following.getNode(blogId);
                            removeFollowing.remove();
                        }

                    }
                }
            }
            session.save();
        } catch (LoginException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        } finally {
            session.logout();
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#searchBlog(java.lang.String)
     */
    public ArrayList<Node> searchBlog(String parameter) {
        Session session = null;
        ArrayList<Node> list = new ArrayList<Node>();

        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            String jcrQuery = "select * from [nt:unstructured] where ISDESCENDANTNODE('/content/blogger/blog/')"
                    + "and (contains('blogId','*" + parameter + "*'))";
            Workspace workspace = session.getWorkspace();
            Query query;
            query = workspace.getQueryManager().createQuery(jcrQuery,
                    Query.JCR_SQL2);
            QueryResult qr = query.execute();

            NodeIterator iterator = qr.getNodes();

            while (iterator.hasNext()) {
                Node node = iterator.nextNode();
                list.add(node);

            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return list;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#userContent(java.lang.String)
     */
    public Map<String, String> userContent(String userId) {
        Session session = null;
        Map<String, String> userDetails = new HashMap<String, String>();

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
//            String jcrQuery = "select * from [nt:unstructured] where ISDESCENDANTNODE('/content/blogger/user/')"
//                    + " and userId='" + userId + "'";
//            String jcrQuery = "select * from [nt:unstructured] where ISDESCENDANTNODE('/content/blogger/user/')"
//                    + " and bloggerId='" + userId + "'";
            userId = '"' + userId + '"';
            String jcrQuery = "select * from [nt:base] where (contains('bloggerId','*"+ userId+ "*'))  and ISDESCENDANTNODE('/content/blogger/user/')";
            Workspace workspace = session.getWorkspace();
            Query query;
            query = workspace.getQueryManager().createQuery(jcrQuery,
                    Query.JCR_SQL2);
            QueryResult qr = query.execute();
            NodeIterator iterator = qr.getNodes();
            userDetails.put("userExists", "false");
            userDetails.put("query", jcrQuery);
            while (iterator.hasNext()) {
                Node node = iterator.nextNode();
                userDetails.put("userId", node.getProperty("userId")
                        .getString());
                userDetails.put("blogId", node.getProperty("blogId")
                        .getString());
                userDetails.put("bloggerId", node.getProperty("bloggerId")
                        .getString());
                userDetails.put("userExists", "true");

            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            session.logout();
        }
        return userDetails;
    }

        /* (non-Javadoc)
         * @see org.blogger.service.BloggerService#allPostNodes(java.lang.String, java.lang.String, int)
         */
        public ArrayList<Node> allPostNodes(String blogId, String bloggerId,
            int offset) {
        ArrayList<Node> list = new ArrayList<Node>();
        ArrayList<String> followinglist = new ArrayList<String>();
        ArrayList<String> postList = new ArrayList<String>();
        Session session = null;
        Node node, bodeNode, post, userBlogNode, checkPostNode;

        String param = " ";
        String userParam = " ";
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            node = session.getNode("/content/blogger/user/" + bloggerId + "");

            if (node.hasNode("followings")) {
                bodeNode = node.getNode("followings");
                if (bodeNode.hasNodes()) {
                    NodeIterator followingNode = node.getNode("followings")
                            .getNodes();

                    while (followingNode.hasNext()) {
                        post = followingNode.nextNode();
                        followinglist.add(post.getName());
                    }
                    checkPostNode = session.getNode("/content/blogger/blog");
                    for (int i = 0; i < followinglist.size(); i++) {
                        if (checkPostNode.hasNode(followinglist.get(i))) {
                            Node checkNode = checkPostNode
                                    .getNode(followinglist.get(i));
                            if (checkNode.hasNode("Post")) {
                                Node postNode = checkNode.getNode("Post");
                                if (postNode.hasNodes()) {
                                    postList.add(followinglist.get(i));
                                }

                            }
                        }
                    }
                }
            }



            userBlogNode = session.getNode("/content/blogger/blog/" + blogId
                    + "");

            if (userBlogNode.hasNode("Post")) {
            	for (int j = 0; j < postList.size(); j++) {
            	if (j < postList.size() - 1) {

            		param += " ISCHILDNODE('/content/blogger/blog/"
            			+ postList.get(j) + "/Post')" + "OR" + " ";
            	} else {
            	// param += "ISCHILDNODE('/content/blogger/blog/"
            	// + postList.get(j) + "/Post')"
            	// + " ) and accessType='public' order by postTimeStamp desc";
            		param += "ISCHILDNODE('/content/blogger/blog/"
            			+ postList.get(j) + "/Post')"
            			+ " )";
            	}
            	}
            	Node userPostNode = userBlogNode.getNode("Post");
            	if (userPostNode.hasNodes()) {
            	if (param.equals(" ")) {
            	// userParam = "ISCHILDNODE('/content/blogger/blog/"
            	// + blogId + "/Post')"
            	// + " and accessType='public' order by postTimeStamp desc";
            		userParam = "ISCHILDNODE('/content/blogger/blog/"
            			+ blogId + "/Post')"
            			+ "";

            	} else {
            		userParam = "(ISCHILDNODE('/content/blogger/blog/"
            			+ blogId + "/Post')" + "OR" + " ";
            	}
            	}
            	}else{
            	for (int j = 0; j < postList.size(); j++) {
            	if (j < postList.size() - 1) {

            		param += "ISCHILDNODE('/content/blogger/blog/"
            			+ postList.get(j) + "/Post')" + "OR" + " ";
            	} else {
            		param += "ISCHILDNODE('/content/blogger/blog/"
            				+ postList.get(j) + "/Post')"
            				+ " order by postTimeStamp desc";
            	}
            	} 

            	}

            String jcrQuery = "SELECT * FROM [nt:unstructured] where" + " "
                    + "contains('accessType','public') and " +userParam + param + " order by postTimeStamp desc";
                        System.out.println("hhhhh"+jcrQuery);
//                        response.getOutputStream().print("hhhhh"+jcrQuery);
//                        response.getOutputStream().print("hhhhh"+postList.size());
            Workspace workspace = session.getWorkspace();
            Query query;
            query = workspace.getQueryManager().createQuery(jcrQuery,
                    Query.JCR_SQL2);
            query.setLimit(10);
            query.setOffset(offset);
            QueryResult qr = query.execute();

            NodeIterator iterator = qr.getNodes();

            while (iterator.hasNext()) {
                Node nodePost = iterator.nextNode();
                list.add(nodePost);
            }

        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#setPostLimit(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public String setPostLimit(String offset, String blogId, String bloggerId,
            String contextPath, String param, String keyword) {
        ArrayList<Node> list = new ArrayList<Node>();
        ArrayList<Node> imageList = new ArrayList<Node>();
        JSONObject json = new JSONObject();
        JSONArray jArr = new JSONArray();
        Session session;
        StringBuilder htmlString = new StringBuilder("");

        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            if (param.equals("home")) {
                list = allPostNodes(blogId, bloggerId, Integer.parseInt(offset));
            } else {
                list = searchTagBased(keyword, blogId, bloggerId,
                        Integer.parseInt(offset));
            }
            if (list.size() > 0) {

                for (int i = 0; i < list.size(); i++) {
                    htmlString = new StringBuilder("");
                    json = new JSONObject();
                    Node homePost = list.get(i);

                    String postType = homePost.getProperty("postType")
                            .getString();

                    htmlString.append("<li>");

                    htmlString.append("<div class='post'>");
                    htmlString.append("<div class='post-avtar'><a href='"
                            + contextPath
                            + "/servlet/blogger/view.blogView?param="
                            + homePost.getParent().getParent().getName()
                                    .toString() + "' class='avtar-pic'>");
                    Node avatarNode;
                    Node pictureNode = homePost.getParent().getParent();
                    if (pictureNode.hasNode("avatar")) {
                        avatarNode = pictureNode.getNode("avatar");
                        htmlString.append("<img src='" + contextPath + ""
                                + avatarNode.getPath() + "' />");
                    } else {
                        htmlString.append("<img src='" + contextPath
                                + "/apps/blogger/images/picture_user.jpg' />");
                    }
                    htmlString
                            .append("</a><a href='javascript:void(0);' class='avtar-link'></a></div>");
                    htmlString
                            .append("<div class='post-connector-arrow'></div>");
                    htmlString.append("  <div class='post-wrapper'>");
                    htmlString
                            .append(" <div class='post-header'> <span class='header-info'>"
                                    + "<a href='"
                                    + contextPath
                                    + "/servlet/blogger/view.blogView?param="
                                    + homePost.getParent().getParent()
                                        .getName()
                                    +"'>"
                                    + homePost.getParent().getParent()
                                            .getName() + "</a></span>");
                    Node node = session.getNode("/content/blogger/user/"
                            + bloggerId);
                    Node blogNode = session.getNode("/content/blogger/blog");
                    if (param.equals("home")) {
                        if (homePost.getProperty("reBlogFlag").getString()
                                .equals("true")) {

                            htmlString.append("<span>");
                            htmlString
                                .append("<a href='#' class='icon reblock'></a> <span>"
                                            + "<a href='"
                                            + contextPath
                                            + "/servlet/blogger/view.blogView?param="
                                            +homePost.getProperty(
                                                    "reBloggerId").getString()
                                                    +"'>"
                                        + homePost.getProperty(
                                                "reBloggerId").getString()
                                        + "</a>");

                            if ((node.hasNode("blogId") && node.getNode(
                                    "blogId").hasNode(
                                    homePost.getProperty("reBloggerId")
                                            .getString()))
                                    || (node.hasNode("followings") && node
                                            .getNode("followings").hasNode(
                                                    homePost.getProperty(
                                                            "reBloggerId")
                                                            .getString()))
                                    || homePost.getProperty("reBloggerId")
                                            .getString().equals(node.getName())) {

                            } else {

                                htmlString
                                        .append("<a href='javascript:void(0);' class='icon-plus-follow' onclick=\"followingBlog('"
                                                + homePost.getProperty(
                                                        "reBloggerId")
                                                        .getString()
                                                + "','follow',this);\">");
                                htmlString
                                        .append("<span class='plus'>+</span>");
                                htmlString
                                        .append("<span class='plus follow'>Follow</span>");
                                htmlString.append("</a>");
                            }
                            htmlString.append("</span>");

                        }
                        htmlString.append(" </span>");
                    } else {
                        if ((node.hasNode("followings")
                                && node.getNode("followings").hasNodes() && node
                                .getNode("followings").hasNode(
                                        homePost.getParent().getParent()
                                                .getName()))
                                || blogNode.hasNode(blogId)) {

                        } else {
                            htmlString.append("<span>");
                            htmlString
                                    .append("<a href='javascript:void(0);' class='icon-plus-follow' onclick=\"followingBlog('"
                                            + homePost.getParent().getParent()
                                                    .getName().toString()
                                            + "','follow',this);\">");
                            htmlString.append("<span class='plus'>+</span>");
                            htmlString
                                    .append("<span class='plus follow'>Follow</span>");
                            htmlString.append("</a>");
                            htmlString.append("</span>");
                        }
                    }
                    htmlString.append("</div>");
                    htmlString.append("<div class='post-content'>");
                    if (postType.equals("text")) {

                        htmlString.append(" <div class='quotes'>");
                        htmlString
                                .append(" <div class='title'><span class='quote-text'>"
                                        + homePost.getProperty("title")
                                                .getString() + "</span></div>");
                        htmlString.append(" <div class='description'>"
                                + homePost.getProperty("description")
                                        .getString() + "</div>");
                        htmlString.append("</div>");
                        htmlString.append("<div class='post-tags'>");
                        if (homePost.hasNode("Tags")
                                && homePost.getNode("Tags").hasProperty("tags")) {
                            for (int p = 0; p < homePost.getNode("Tags")
                                    .getProperty("tags").getValues().length; p++) {
                                htmlString
                                        .append("# <a href='javascript:void(0);'>"
                                                + homePost.getNode("Tags")
                                                        .getProperty("tags")
                                                        .getValues()[p]
                                                        .getString() + "</a>");
                            }
                        }
                        htmlString.append("</div>");
                    } else if (postType.equals("quote")) {

                        htmlString.append("<div class='quotes'>");
                        htmlString
                                .append("<div class='title'><span class='quote-mark'>&ldquo;</span><span class='quote-text'>"
                                        + homePost.getProperty("title")
                                                .getString()
                                        + "</span><span class='quote-mark'>&rdquo;</span></div>");
                        htmlString.append("<div class='description'>"
                                + homePost.getProperty("description")
                                        .getString() + "</div>");

                        htmlString.append("</div>");
                        htmlString.append("<div class='post-tags'>");
                        if (homePost.hasNode("Tags")
                                && homePost.getNode("Tags").hasProperty("tags")) {
                            for (int p = 0; p < homePost.getNode("Tags")
                                    .getProperty("tags").getValues().length; p++) {
                                htmlString
                                        .append("# <a href='javascript:void(0);'>"
                                                + homePost.getNode("Tags")
                                                        .getProperty("tags")
                                                        .getValues()[p]
                                                        .getString() + "</a>");
                            }
                        }
                        htmlString.append("</div>");
                    } else if (postType.equals("video")) {

                        htmlString
                                .append("<input type='hidden' id='video' value=\""
                                        + homePost.getProperty("videoIframe")
                                                .getString() + "\" />");
                        if (homePost.getProperty("videoLinkFlag").getString()
                                .equals("true")) {
                            htmlString
                                    .append("<a href='javascript:void(0);'><div class='video-upload-box'><span style='margin: 8.5em 20em;' id='button' onclick='showVideo(this);'></span><div id='image'>"
                                            + homePost.getProperty(
                                                    "videoThumbnail")
                                                    .getString()
                                            + "</div> <div></div></div></a>");
                        } else {
                            NodeIterator iterator = homePost.getNode("Video")
                                    .getNodes();
                            if (iterator.hasNext()) {
                                htmlString
                                        .append("<video width='575' height='300' controls><source src='"
                                                + contextPath
                                                + iterator.nextNode().getPath()
                                                + "' type='video/webm'>Your browser does not support the video tag.</video>");
                            }
                        }
                        htmlString.append(" <p>");
                        htmlString.append("<div class='quotes'>");

                        htmlString.append("<div class='description'>"
                                + homePost.getProperty("description")
                                        .getString() + "</div>");
                        htmlString.append(" </p>");
                        htmlString.append("</div>");
                        htmlString.append("<div class='post-tags'>");
                        if (homePost.hasNode("Tags")
                                && homePost.getNode("Tags").hasProperty("tags")) {
                            for (int p = 0; p < homePost.getNode("Tags")
                                    .getProperty("tags").getValues().length; p++) {
                                htmlString
                                        .append("# <a href='javascript:void(0);'>"
                                                + homePost.getNode("Tags")
                                                        .getProperty("tags")
                                                        .getValues()[p]
                                                        .getString() + "</a>");
                            }
                        }
                        htmlString.append("</div>");

                    }

                    else if (postType.equals("audio")) {

                        String audioLinkFlag = homePost.getProperty(
                                "audioLinkFlag").getString();

                        htmlString.append("<div class='audio-player-wrap'>");
                        htmlString
                                .append("<div class='audip-player-content clearfix'>");
                        htmlString.append("<div class='album-image'>");
                        if (homePost.getProperty("coverPage").getString() != "") {
                            htmlString.append("<img src="
                                    + homePost.getProperty("coverPage")
                                            .getString() + " alt=''/>");
                        }
                        htmlString.append("</div>");
                        htmlString.append("<div class='album-text'>");
                        htmlString.append("<span>"
                                + homePost.getProperty("audioAlbum")
                                        .getString() + "</span>");
                        htmlString.append("<span><i>"
                                + homePost.getProperty("audioTrack")
                                        .getString() + "</i></span>");
                        htmlString.append("<span><i>Artist name</i></span>");

                        htmlString.append("</div>");

                        htmlString.append("</div>");

                        if (audioLinkFlag.equals("true")) {

                            htmlString.append("<audio src='"
                                    + homePost.getProperty("audioLink")
                                            .getString()
                                    + "' preload='auto'></audio>");

                        } else {
                            NodeIterator audioNode = homePost.getNode("Audio")
                                    .getNodes();

                            htmlString.append("<audio src='" + contextPath + ""
                                    + audioNode.nextNode().getPath()
                                    + "' preload='auto'></audio>");

                        }

                        htmlString.append("<div class='album-description'>"
                                + homePost.getProperty("description")
                                        .getString());

                        htmlString.append("</div>");
                        htmlString.append("</div>");
                        htmlString.append("<div class='post-tags'>");
                        if (homePost.hasNode("Tags")
                                && homePost.getNode("Tags").hasProperty("tags")) {
                            for (int p = 0; p < homePost.getNode("Tags")
                                    .getProperty("tags").getValues().length; p++) {
                                htmlString
                                        .append("# <a href='javascript:void(0);'>"
                                                + homePost.getNode("Tags")
                                                        .getProperty("tags")
                                                        .getValues()[p]
                                                        .getString() + "</a>");
                            }
                        }
                        htmlString.append("</div>");
                    }

                    else if (postType.equals("link")) {

                        htmlString.append("<a href='javascript:void(0);'>");
                        htmlString.append(" <div class='link-box'>");
                        htmlString
                                .append(" <span>"
                                        + homePost.getProperty("title")
                                                .getString()
                                        + "<img  src='"
                                        + contextPath
                                        + "/apps/blogger/images/link-arrow.png' alt='' /></span>");
                        htmlString.append(" <span>"
                                + homePost.getProperty("link").getString()
                                + "</span>");

                        htmlString.append("</div>");
                        htmlString.append(" </a>");
                        htmlString.append(" <p>");
                        htmlString.append("<div class='quotes'>");
                        htmlString.append(" <div class='description'>"
                                + homePost.getProperty("description")
                                        .getString() + "</div>");
                        htmlString.append("</p>");
                        htmlString.append("</div>");
                        htmlString.append("<div class='post-tags'>");
                        if (homePost.hasNode("Tags")
                                && homePost.getNode("Tags").hasProperty("tags")) {
                            for (int p = 0; p < homePost.getNode("Tags")
                                    .getProperty("tags").getValues().length; p++) {
                                htmlString
                                        .append("# <a href='javascript:void(0);'>"
                                                + homePost.getNode("Tags")
                                                        .getProperty("tags")
                                                        .getValues()[p]
                                                        .getString() + "</a>");
                            }
                        }
                        htmlString.append("</div>");
                    } else if (postType.equals("image")) {
                        if (homePost.hasNode("Image")) {
                            Node Image = homePost.getNode("Image");
                            if (Image.hasNodes()) {
                                htmlString.append("<div class='image-post'>");
                                NodeIterator imageNode = Image.getNodes();
                                while (imageNode.hasNext()) {
                                    Node post = imageNode.nextNode();
                                    imageList.add(post);

                                }

                                for (int k = 0; k < imageList.size(); k++) {
                                    Node image = imageList.get(k);
                                    htmlString
                                            .append("<a onclick='imageFull(this)' href='javascript:void(0);'>");
                                    htmlString.append("<img src='"
                                            + contextPath + ""
                                            + image.getPath() + "/x320' />");
                                    htmlString.append("</a>");
                                }
                                htmlString.append("</div>");
                                htmlString.append("<div class='post-tags'>");
                                if (homePost.hasNode("Tags")
                                        && homePost.getNode("Tags")
                                                .hasProperty("tags")) {
                                    for (int p = 0; p < homePost
                                            .getNode("Tags")
                                            .getProperty("tags").getValues().length; p++) {
                                        htmlString
                                                .append("# <a href='javascript:void(0);'>"
                                                        + homePost
                                                                .getNode("Tags")
                                                                .getProperty(
                                                                        "tags")
                                                                .getValues()[p]
                                                                .getString()
                                                        + "</a>");
                                    }
                                }
                                htmlString.append("</div>");
                            }
                        }
                    }

                    htmlString.append("</div>");
                    htmlString.append("<div class='post-footer'>");

                  
                    SimpleDateFormat d = new SimpleDateFormat(
                            "yyyy-MM-dd hh:mm:ss");
                    String postDate = d.format(homePost
                            .getProperty("postTimeStamp").getDate().getTime());
                    htmlString
                            .append("<div class='note-count'><span class='note-text'>"
                                    + calculateAgo(postDate) + "</span></div>");

                    htmlString.append("<div class='post-action'>");
                    htmlString
                            .append("<a href='javascript:void(0);' class='icon reblock' onclick=\"reBlogPost('"
                                    + homePost.getName().toString()
                                    + "','Post','"
                                    + homePost.getParent().getParent()
                                            .getName().toString()
                                    + "','"
                                    + homePost.getProperty("postType")
                                            .getString() + "',this);\"></a>");

                    if (homePost.hasNode("Like")) {
                        Node likeNode = homePost.getNode("Like");

                        if (likeNode.hasNode(blogId)) {
                            htmlString
                                    .append("<a href='javascript:void(0);' class='icon like-hover' onclick=\"likePost('"
                                            + homePost.getPath()
                                            + "',this);\"></a>");
                        } else {
                            htmlString
                                    .append("<a href='javascript:void(0);' class='icon like' onclick=\"likePost('"
                                            + homePost.getPath()
                                            + "',this);\"></a>");
                        }
                    } else {
                        htmlString
                                .append("<a href='javascript:void(0);' class='icon like' onclick=\"likePost('"
                                        + homePost.getPath()
                                        + "',this);\"></a>");
                    }
                    if (param.equals("home")) {
                        if (homePost.getParent().getParent()
                                .getProperty("bloggerId").getString()
                                .equals(bloggerId)) {
                            htmlString
                                    .append("<a href='javascript:void(0)' class='icon more-action' onclick='settings(this);' ></a>");
                            htmlString.append("<div class='action-menu'>");
                            htmlString.append("<span class='nipple'></span>");
                            htmlString.append("<ul class='post-menu'>");

                            htmlString
                                    .append("<li class='post-menu-item'><a href='javascript:void(0)' onclick=\"editPost('"
                                            + homePost.getName().toString()
                                            + "','Post','"
                                            + homePost.getParent().getParent()
                                                    .getName().toString()
                                            + "','"
                                            + homePost.getProperty("postType")
                                                    .getString()
                                            + "',this);\">");
                            htmlString
                                    .append("<div class='post-menu-item-name'><span class='icon edit'></span>EDIT</div>");
                            htmlString.append("</a>");
                            htmlString.append("</li>");

                            htmlString
                                    .append("<li class='post-menu-item'><a href='javascript:void(0)' onclick=\"deletePost('"
                                            + homePost.getPath()
                                            + "',this);\">");
                            htmlString
                                    .append("<div class='post-menu-item-name'><span class='icon delete'></span>Delete</div>");
                            htmlString.append("</a>");
                            htmlString.append("</li>");
                            htmlString.append("</ul>");
                            htmlString.append("</div>");
                        }
                    }
                    htmlString.append("</div>");
                    htmlString.append("</div>");
                    htmlString.append("</div>");
                    htmlString
                            .append("<div class='post-wrapper view' style='display:none'></div>");
                    htmlString.append("</div>");
                    htmlString.append("</li>");

                    json.accumulate("posts", htmlString.toString());

                    jArr.put(json);

                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return jArr.toString();
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#likeBlog(java.lang.String, java.lang.String, java.lang.String)
     */
    public String likeBlog(String blogPath, String bloggerId, String blogId) {
        Session session = null;
        Node node, likeNode, likedNode, userNode, userLikeNode, addLikeNode = null;
        String var = "";
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            node = session.getNode(blogPath);
            if (node.hasNode("Like")) {
                likeNode = node.getNode("Like");
            } else {
                likeNode = node.addNode("Like");
            }

            if (likeNode.hasNode(bloggerId)) {
                likedNode = likeNode.getNode(bloggerId);
                likedNode.remove();
            } else {
                likedNode = likeNode.addNode(bloggerId);
            }

            userNode = session.getNode("/content/blogger/user/" + bloggerId
                    + "");

            if (userNode.hasNode("Like")) {
                userLikeNode = userNode.getNode("Like");
            } else {
                userLikeNode = userNode.addNode("Like");
            }
            String postId = node.getName();
            String blogID = node.getParent().getParent().getName();

            if (userLikeNode.hasNode(postId + "_" + blogID)) {

                addLikeNode = userLikeNode.getNode(postId + "_" + blogID);
                addLikeNode.remove();

                var = "remove";
            } else {
                addLikeNode = userLikeNode.addNode(postId + "_" + blogID);
                addLikeNode.setProperty("PostPath", blogPath);
                addLikeNode.setProperty("BlogId", blogID);
                var = "add";
            }

            session.save();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            session.logout();
        }

        return var;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#deleteBlog(java.lang.String, java.lang.String)
     */
    public String deleteBlog(String blogPath, String bloggerId) {
        Session session = null;
        Node node, userNode, userLikeNode, addLikeNode = null;
        String var = "delete";
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            node = session.getNode(blogPath);
            String postId = node.getName();
            String blogID = node.getParent().getParent().getName();

            userNode = session.getNode("/content/blogger/user/" + bloggerId
                    + "");

            if (userNode.hasNode("Like")) {
                userLikeNode = userNode.getNode("Like");
            } else {
                userLikeNode = userNode.addNode("Like");
            }

            if (userLikeNode.hasNode(postId + "_" + blogID)) {

                addLikeNode = userLikeNode.getNode(postId + "_" + blogID);
                addLikeNode.remove();
            }
            node.remove();

            session.save();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            session.logout();
        }

        return var;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#searchTagBased(java.lang.String, java.lang.String, java.lang.String, int)
     */
    @SuppressWarnings("deprecation")
    public ArrayList<Node> searchTagBased(String parameter, String blogId,
            String bloggerId, int offset) {

        Session session = null;
        ArrayList<Node> list = new ArrayList<Node>();
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            String jcrQuery = "select * from nt:unstructured where tags='"
                    + parameter
                    + "'  and jcr:path LIKE '/content/blogger/blog/%/Post/%/Tags'";
            Workspace workspace = session.getWorkspace();
            Query query;
            query = workspace.getQueryManager()
                    .createQuery(jcrQuery, Query.SQL);
            query.setLimit(5);
            query.setOffset(offset);
            QueryResult qr = query.execute();

            NodeIterator iterator = qr.getNodes();

            while (iterator.hasNext()) {
                Node node = iterator.nextNode();
                list.add(node.getParent());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#followerSearch(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public String followerSearch(String parameter, String bloggerId,
            String contextPath, String blogId) {
        Node followingNode, userNode;
        Session session = null;
        ArrayList<Node> list = new ArrayList<Node>();
        ArrayList<String> followingBlogList = new ArrayList<String>();
        StringBuilder htmlString = new StringBuilder("");

        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            String jcrQuery = "select * from [nt:unstructured] where ISDESCENDANTNODE('/content/blogger/blog/')"
                    + "and (contains('blogTitle','*"
                    + parameter
                    + "*') or contains('blogId','*" + parameter + "*'))";
            Workspace workspace = session.getWorkspace();
            Query query;
            query = workspace.getQueryManager().createQuery(jcrQuery,
                    Query.JCR_SQL2);
            QueryResult qr = query.execute();

            NodeIterator iterator = qr.getNodes();

            userNode = session.getNode("/content/blogger/user/" + bloggerId
                    + "");

            if (userNode.hasNode("followings")) {
                followingNode = userNode.getNode("followings");
                if (followingNode.hasNodes()) {
                    NodeIterator followingNodes = followingNode.getNodes();
                    while (followingNodes.hasNext()) {
                        Node bloggerNode = followingNodes.nextNode();
                        followingBlogList.add(bloggerNode.getName().toString());
                    }
                }
            }

            /*
             * if (userNode.hasNode("blogId")) { userblogIdNode =
             * userNode.getNode("blogId"); if (userblogIdNode.hasNodes()) {
             * NodeIterator blogNodes = userblogIdNode.getNodes(); while
             * (blogNodes.hasNext()) { Node blogIdNode = blogNodes.nextNode();
             * followingBlogList.add(blogIdNode.getName().toString()); } } }
             */

            followingBlogList.add(blogId);

            while (iterator.hasNext()) {
                Node node = iterator.nextNode();
                if (!followingBlogList.contains(node.getName().toString())) {
                    list.add(node);
                }

            }
            Node searchBlog;
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    searchBlog = list.get(i);

                    htmlString.append("<li>");
                    htmlString
                            .append("<a href='javascript:void(0);' class='follower-pic'>");
                    if (searchBlog.hasNode("avatar")) {
                        Node avatarNode = searchBlog.getNode("avatar");
                        htmlString.append("<img src='" + contextPath + ""
                                + avatarNode.getPath() + "' />");
                    } else {
                        htmlString
                                .append("<img src='"
                                        + contextPath
                                        + "/apps/blogger/images/picture_user.jpg' width='70' height='70' />");
                    }

                    htmlString.append("</a>");
                    htmlString
                            .append("<div class='follower-info'><div class='name'><a href='javascript:void(0);'>"
                                    + searchBlog.getProperty("blogId")
                                            .getString()
                                    + "</a></div><!--div class='detail'>Updated 18 hours ago</div--></div>");
                    htmlString
                            .append("<div class='action-button'><input name='follow' value='follow' type='button' class='follow-btn' onclick=\"followingBlog('"
                                    + searchBlog.getProperty("blogId")
                                            .getString()
                                    + "','follow',this);\"/></div>");
                    htmlString.append("</li>");

                }
            } else {
                htmlString.append("<li>No Result Found!</li>");
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            session.logout();
        }
        return htmlString.toString();
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#tagSearch(java.lang.String)
     */
    @SuppressWarnings("deprecation")
    public ArrayList<String> tagSearch(String parameter) {

        Session session = null;
        ArrayList<String> tagList = new ArrayList<String>();
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            String jcrQuery = "select excerpt(.) from nt:unstructured where contains(*, '"
                    + parameter
                    + "*') and jcr:path LIKE '/content/blogger/blog/%/Post/%/Tags'";
            Workspace workspace = session.getWorkspace();
            Query query;
            query = workspace.getQueryManager()
                    .createQuery(jcrQuery, Query.SQL);
            QueryResult qr = query.execute();
            RowIterator rit = qr.getRows();
            while (rit.hasNext()) {
                Row row = rit.nextRow();
                Value[] values = row.getValues();
                Document doc = Jsoup.parse(values[0].getString());
                Elements strongTag = doc.select("strong");
                for (int i = 0; i < strongTag.size(); i++) {
                    if (!tagList.contains(strongTag.get(i).text())) {
                        tagList.add(strongTag.get(i).text());
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            session.logout();
        }
        return tagList;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#searchBlogDiv(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public ArrayList<Map> searchBlogDiv(String key) {
        ArrayList<Node> blogList = searchBlog(key);
        ArrayList<Map> blogMapList = new ArrayList<Map>();
        Map<String, String> blogMap = null;
        for (int i = 0; i < blogList.size() && i < 5; i++) {
            blogMap = new HashMap<String, String>();
            try {
                blogMap.put("title", blogList.get(i).getProperty("blogTitle")
                        .getString());
                if (blogList.get(i).hasNode("avatar")) {
                    blogMap.put("picture", blogList.get(i).getNode("avatar")
                            .getPath());
                } else {
                    blogMap.put("picture",
                            "/apps/blogger/images/picture_user.jpg");
                }
                blogMap.put("bloggerId",
                        blogList.get(i).getProperty("bloggerId").getString());
                blogMap.put("blogId", blogList.get(i).getName());
                blogMapList.add(blogMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return blogMapList;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#deleteBlogId(java.lang.String, java.lang.String)
     */
    @SuppressWarnings("deprecation")
    public void deleteBlogId(String userBlogId, String bloggerId) {
        Session session = null;
        Node node, userNode;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            node = session.getNode("/content/blogger/blog/" + userBlogId + "");

            userNode = session.getNode("/content/blogger/user/" + bloggerId);

            Workspace workspace = session.getWorkspace();
            Query query;
            QueryResult qr;

            String queryForLike = "select * from nt:unstructured where BlogId='"
                    + userBlogId
                    + "' and jcr:path LIKE '/content/blogger/user/%/Like/%'";
            query = workspace.getQueryManager().createQuery(queryForLike,
                    Query.SQL);
            qr = query.execute();
            NodeIterator iterator = qr.getNodes();
            while (iterator.hasNext()) {
                Node likeNodes = iterator.nextNode();

                likeNodes.remove();
            }

            String queryForFollowing = "select * from nt:unstructured where  jcr:path LIKE '/content/blogger/user/%/followings/"
                    + userBlogId + "'";
            query = workspace.getQueryManager().createQuery(queryForFollowing,
                    Query.SQL);
            qr = query.execute();
            NodeIterator followingIterator = qr.getNodes();
            while (followingIterator.hasNext()) {
                Node followingNodes = followingIterator.nextNode();

                followingNodes.remove();
            }

            if (userNode.hasNode("blogId")) {
                Node userBlogNode = userNode.getNode("blogId");
                if (userBlogNode.hasNode(userBlogId)) {
                    Node userBlogIdNode = userBlogNode.getNode(userBlogId);
                    userBlogIdNode.remove();

                }
            }
            node.remove();
            session.save();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            session.logout();
        }

    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#deleteUserAccount(java.lang.String, java.lang.String)
     */
    @SuppressWarnings("deprecation")
    public void deleteUserAccount(String blogId, String bloggerId) {
        Session session = null;
        Node userNode;

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            userNode = session.getNode("/content/blogger/user/" + bloggerId);

            if (userNode.hasNode("blogId")) {
                Node userBlogIdNode = userNode.getNode("blogId");
                if (userBlogIdNode.hasNodes()) {

                    NodeIterator blogNodes = userBlogIdNode.getNodes();

                    while (blogNodes.hasNext()) {
                        Node blogIdNodes = blogNodes.nextNode();
                        deleteBlogId(blogIdNodes.getName(), bloggerId);

                    }

                }
            }
            deleteBlogId(blogId, bloggerId);

            Workspace workspace = session.getWorkspace();
            Query query;
            QueryResult qr;
            String queryforFollowersNodes = "select * from nt:unstructured where  jcr:path LIKE '/content/blogger/blog/%/followers/"
                    + bloggerId + "'";
            query = workspace.getQueryManager().createQuery(
                    queryforFollowersNodes, Query.SQL);
            qr = query.execute();
            NodeIterator iteratorFollowerNode = qr.getNodes();

            while (iteratorFollowerNode.hasNext()) {
                Node followerNodes = iteratorFollowerNode.nextNode();

                followerNodes.remove();
            }

            String queryforLikeNodes = "select * from nt:unstructured where jcr:path LIKE '/content/blogger/blog/%/Post/%/Like/"
                    + bloggerId + "'";
            query = workspace.getQueryManager().createQuery(queryforLikeNodes,
                    Query.SQL);
            qr = query.execute();
            NodeIterator iteratorLikeNodes = qr.getNodes();

            while (iteratorLikeNodes.hasNext()) {
                Node blogLikeNodes = iteratorLikeNodes.nextNode();

                blogLikeNodes.remove();
            }
            userNode = session.getNode("/content/blogger/user/" + bloggerId);

            userNode.remove();
            session.save();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            session.logout();
        }

    }

    /**
     * Check blog id.
     *
     * @param userBlogId the user blog id
     * @param blogId the blog id
     * @param bloggerId the blogger id
     * @return true, if successful
     */
    public boolean checkBlogId(String userBlogId, String blogId,
            String bloggerId) {
        Session session = null;
        Node userNode;

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            userNode = session.getNode("/content/blogger/user/" + bloggerId);
            if (userBlogId.equals(blogId)) {
                return true;
            } else if (userNode.hasNode("blogId")) {

                Node blogIdNode = userNode.getNode("blogId");
                if (blogIdNode.hasNode(userBlogId)) {

                    return true;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            session.logout();
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#queuedPost()
     */
    @SuppressWarnings({ "deprecation" })
    public String queuedPost() {
        Session session = null;
        Node nodeType;

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            SimpleDateFormat dateFormat = (new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:00.000'+05:30'"));
            Date date = new Date();
            String scheduleDate = dateFormat.format(date);

            Workspace workspace = session.getWorkspace();
            Query query;
            QueryResult qr;
            String queryforQueuedNodes = "select * from nt:base where scheduledDate = TIMESTAMP '"
                    + scheduleDate
                    + "' and jcr:path LIKE '/content/blogger/blog/%/Queued/%' ";
            System.out.println(queryforQueuedNodes);
            query = workspace.getQueryManager().createQuery(
                    queryforQueuedNodes, Query.SQL);
            qr = query.execute();
            NodeIterator iteratorQueuedNode = qr.getNodes();

            while (iteratorQueuedNode.hasNext()) {
                Node queuedNodes = iteratorQueuedNode.nextNode();
                Node blogNode = session.getNode("/content/blogger/blog/"
                        + queuedNodes.getParent().getParent().getName());

                if (blogNode.hasNode("Post")) {
                    nodeType = blogNode.getNode("Post");
                } else {
                    nodeType = blogNode.addNode("Post");
                }

                if (nodeType.hasProperty("postNumber")) {
                    nodeType.setProperty("postNumber",
                            nodeType.getProperty("postNumber").getLong() + 1);
                } else {
                    nodeType.setProperty("postNumber", 0);
                }

                session.getWorkspace().move(
                        "/content/blogger/blog/"
                                + queuedNodes.getParent().getParent().getName()
                                + "/" + "Queued" + "/" + queuedNodes.getName(),
                        "/content/blogger/blog/"
                                + queuedNodes.getParent().getParent().getName()
                                + "/" + "Post" + "/" + "Post"
                                + nodeType.getProperty("postNumber").getLong());

            }

            session.save();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            session.logout();
        }
        return "var";
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#tagList(java.lang.String)
     */
    @SuppressWarnings("deprecation")
    public ArrayList<String> tagList(String blogId) {

        Session session = null;

        ArrayList<String> tagList = new ArrayList<String>();
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            String jcrQuery = "select * from nt:base where  jcr:path LIKE '/content/blogger/blog/"
                    + blogId + "/%/Tags'";
            Workspace workspace = session.getWorkspace();
            Query query;
            query = workspace.getQueryManager()
                    .createQuery(jcrQuery, Query.SQL);

            QueryResult qr = query.execute();

            NodeIterator iterator = qr.getNodes();

            while (iterator.hasNext()) {
                Node node = iterator.nextNode();
                for (int p = 0; p < node.getProperty("tags").getValues().length; p++) {
                    if (!tagList
                            .contains(node.getProperty("tags").getValues()[p]
                                    .getString())) {
                        tagList.add(node.getProperty("tags").getValues()[p]
                                .getString());
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tagList;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#randomBlog(java.lang.String, java.lang.String)
     */
    public String randomBlog(String bloggerId, String blogId, String path) {
        Node followingNode, userNode, userblogIdNode, blogNode;
        Session session = null;
        ArrayList<Node> list = new ArrayList<Node>();

        ArrayList<String> followingBlogList = new ArrayList<String>();
        JSONObject json = new JSONObject();
        JSONArray jArr = new JSONArray();

        StringBuilder htmlString = new StringBuilder("");
        try {
            NodeIterator iterator = null;
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            blogNode = session.getNode("/content/blogger/blog");

            if (blogNode.hasNodes()) {
                iterator = blogNode.getNodes();
            }
            userNode = session.getNode("/content/blogger/user/" + bloggerId
                    + "");

            if (userNode.hasNode("followings")) {
                followingNode = userNode.getNode("followings");
                if (followingNode.hasNodes()) {
                    NodeIterator followingNodes = followingNode.getNodes();
                    while (followingNodes.hasNext()) {
                        Node bloggerNode = followingNodes.nextNode();
                        followingBlogList.add(bloggerNode.getName().toString());
                    }
                }
            }

            if (userNode.hasNode("blogId")) {
                userblogIdNode = userNode.getNode("blogId");
                if (userblogIdNode.hasNodes()) {
                    NodeIterator blogNodes = userblogIdNode.getNodes();
                    while (blogNodes.hasNext()) {
                        Node blogIdNode = blogNodes.nextNode();
                        followingBlogList.add(blogIdNode.getName().toString());
                    }
                }
            }

            followingBlogList.add(blogId);

            while (iterator.hasNext()) {
                Node node = iterator.nextNode();
                if (!followingBlogList.contains(node.getName().toString())) {
                    list.add(node);
                }

            }

            for (int i = 0; i < list.size(); i++) {
                json = new JSONObject();
                htmlString = new StringBuilder("");

                htmlString.append("<h3>Featured Blog</h3>");
                htmlString.append("<div class='featured-blog'>");
                if (list.get(i).hasNode("avatar")) {
                    htmlString
                            .append("<a href='"+path+"'/servlet/blogger/view.blogView?param="
                                    + list.get(i).getName()
                                    + "'><img src='"+path 
                                    + list.get(i).getNode("avatar").getPath()
                                    + "' align='left'/>");
                } else {
                    htmlString
                            .append("<a href='"+path+"'/servlet/blogger/view.blogView?param="
                                    + list.get(i).getName()
                                    + "'><img src='"+path+"'/apps/blogger/images/picture_user.jpg' align='left'/>");
                }
                htmlString.append("<div class='details'>");
                htmlString.append("<h3>" + list.get(i).getName() + "</h3>");
                htmlString.append("<span>"
                        + list.get(i).getProperty("blogDescription")
                                .getString() + "</span>");
                htmlString.append("</div>");
                htmlString.append("</a>");
                htmlString.append("</div>");

                json.accumulate("posts", htmlString.toString());

                jArr.put(json);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return jArr.toString();
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#changeTheme(java.lang.String, java.lang.String, java.lang.String, org.apache.sling.api.SlingHttpServletRequest)
     */
    @SuppressWarnings("deprecation")
    public void changeTheme(String blogId, String cssSelect,
            String imageSelect, SlingHttpServletRequest request) {

        Session session = null;
        Node blogNode, fileNode, jcrNode;

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            blogNode = session.getNode("/content/blogger/blog/" + blogId);

            blogNode.setProperty("themeCss", cssSelect);
            blogNode.setProperty("themeImage", imageSelect);
            if (request.getParameterMap().containsKey("files")) {
                if (blogNode.hasNode("themeImage")) {
                    fileNode = blogNode.getNode("themeImage");
                } else {
                    fileNode = blogNode.addNode("themeImage", "nt:file");
                }
                if (fileNode.hasNode("jcr:content")) {
                    jcrNode = fileNode.getNode("jcr:content");
                } else {
                    jcrNode = fileNode.addNode("jcr:content", "nt:resource");
                }

                for (Entry<String, RequestParameter[]> e : request
                        .getRequestParameterMap().entrySet()) {
                    for (RequestParameter p : e.getValue()) {
                        if (!p.isFormField()) {

                            jcrNode.setProperty("jcr:data", p.getInputStream());

                            jcrNode.setProperty("jcr:lastModified",
                                    Calendar.getInstance());
                            jcrNode.setProperty("jcr:mimeType", "image/jpg");
                        }
                    }
                }
            } else {
                System.out.println("pppp");
                if (blogNode.hasNode("themeImage")) {
                    fileNode = blogNode.getNode("themeImage");
                    fileNode.remove();
                }
            }

            session.save();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerService#reBlogPost(java.lang.String)
     */
    @SuppressWarnings("deprecation")
    public ArrayList<Node> reBlogPost(String postPath) {
        Session session = null;
        ArrayList<Node> list = new ArrayList<Node>();
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            String jcrQuery = "select * from nt:unstructured where  jcr:path LIKE '"
                    + postPath + "'";
            Workspace workspace = session.getWorkspace();
            Query query;
            query = workspace.getQueryManager()
                    .createQuery(jcrQuery, Query.SQL);

            QueryResult qr = query.execute();

            NodeIterator iterator = qr.getNodes();

            while (iterator.hasNext()) {
                Node node = iterator.nextNode();
                list.add(node);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

    /**
     * Calculate ago.
     *
     * @param dateStart the date start
     * @return the string
     */
    public static String calculateAgo(String dateStart) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        long diff = 0, diffSeconds, diffMinutes, diffHours = 0, diffDays, diffMonths;
        try {
            String dateStop = format.format(new Date());
            Date d1 = format.parse(dateStart);
            Date d2 = format.parse(dateStop);
            System.out.println(d2);

            diff = d2.getTime() - d1.getTime();
            if (diff > 0) {
                diffSeconds = diff / 1000 % 60;
                diffMinutes = diff / (60 * 1000) % 60;
                diffHours = diff / (60 * 60 * 1000) % 24;
                diffDays = diff / (24 * 60 * 60 * 1000);
                diffMonths = diffDays / 30;

                if (diffMonths > 0)
                    return diffMonths + " months ago";
                else if (diffDays > 0)
                    return diffDays + " days ago";
                else if (diffHours > 0)
                    return diffHours + " hr ago";
                else if (diffMinutes > 0)
                    return diffMinutes + " min ago";
                else
                    return diffSeconds + " sec ago";
            } else {
                return "not valid Date";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }
}
