package org.blogger.service.impl;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.servlet.ServletException;
import org.apache.commons.codec.binary.Base64;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.blogger.service.BloggerPostService;
import org.gallery.service.GalleryService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

// TODO: Auto-generated Javadoc
/**
 * The Class BloggerPostServiceImpl.
 */
@Component(configurationFactory = true)
@Service(BloggerPostService.class)
@Properties({ @Property(name = "BloggerPostService", value = "bloggingPost") })
public class BloggerPostServiceImpl implements BloggerPostService {

    /** The repos. */
    @Reference
    private SlingRepository repos;

    /** The gallery service. */
    @Reference
    private GalleryService galleryService;

    /** The session. */
    private Session session;
    
    /** The global date. */
    private Date globalDate = null;

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerPostService#getUserBlog(java.lang.String)
     */
    public ArrayList<String> getUserBlog(String bloggerId) {
        ArrayList<String> userList = new ArrayList<String>();
        Node bloggerNode;
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            bloggerNode = session.getNode("/content/blogger/user/" + bloggerId);
            userList.add(bloggerNode.getProperty("blogId").getString());
            if (bloggerNode.hasNode("blogId")
                    && bloggerNode.getNode("blogId").hasNodes()) {
                NodeIterator blogNodes = bloggerNode.getNode("blogId")
                        .getNodes();
                while (blogNodes.hasNext()) {
                    userList.add(blogNodes.nextNode().getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerPostService#postText(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void postText(String bloggerId, String blogId, String text,
            String title, String postId, String tags, String postType,
            String accessType, String type, String queuedTime, String editFlag,
            String formerPostType, String reBlogFlag, String sourceId,
            String reBloggerId) {
        if (editFlag.equals("true")) {
            if (!formerPostType.equals(type)) {
                deletePost(formerPostType, postId, blogId);
                postId = "0";
            }
        }
        Node bloggerNode, blogNode = null, tagNode, postNode, nodeType = null;
        globalDate = new Date();

        Calendar cal = Calendar.getInstance();
        String postDate = new SimpleDateFormat("EEE, dd MMM yyyy")
                .format(globalDate);
        String postTime = new SimpleDateFormat("hh:mm aa").format(globalDate);
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            bloggerNode = session.getNode("/content/blogger/blog");

            if (bloggerNode.hasNode(blogId)) {
                blogNode = bloggerNode.getNode(blogId);
            }

            if (blogNode.hasNode(type)) { // type : Post/Queued/Draft
                nodeType = blogNode.getNode(type);
            } else {
                nodeType = blogNode.addNode(type);
            }

            if (nodeType.hasProperty("postNumber")) {
                nodeType.setProperty("postNumber",
                        nodeType.getProperty("postNumber").getLong() + 1);
            } else {
                nodeType.setProperty("postNumber", 0);
            }

            if (postId.equals("0")) {
                postNode = nodeType.addNode("Post"
                        + nodeType.getProperty("postNumber").getLong());
                postNode.setProperty("postDate", postDate);
                postNode.setProperty("postTime", postTime);
                postNode.setProperty("postTimeStamp", cal);
            } else {
                postNode = nodeType.getNode(postId);
            }

            postNode.setProperty("title", title);
            postNode.setProperty("description", text);
            if( tags!=null && !tags.equals("")){
                if(postNode.hasNode("Tags")){
                    tagNode = postNode.getNode("Tags");  
                }else{
                    tagNode = postNode.addNode("Tags");
                }
                tagNode.setProperty("tags", tags.split(","));
            }
            /* postNode.setProperty("type", type); */// removed as a change
            postNode.setProperty("postType", postType);
            postNode.setProperty("accessType", accessType);
            postNode.setProperty("bloggerId", bloggerId);
            postNode.setProperty("reBlogFlag", reBlogFlag);
            postNode.setProperty("sourceId", sourceId);
            postNode.setProperty("reBloggerId", reBloggerId);

            if (type.equals("Queued")) {
                System.out.println(queuedTime);
                Date scheduledDate = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                        .parse(queuedTime);
                cal.setTime(scheduledDate);
                postNode.setProperty("scheduledDate", cal);
            }
            session.save();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.logout();
        }
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerPostService#deletePost(java.lang.String, java.lang.String, java.lang.String)
     */
    public void deletePost(String postType, String postId, String blogId) {
        Node blogNode, postNode = null, blogTypeNode = null;
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            blogNode = session.getNode("/content/blogger/blog/" + blogId);
            if (blogNode.hasNode(postType)) {
                blogTypeNode = blogNode.getNode(postType);
            }

            if (blogTypeNode.hasNode(postId)) {
                postNode = blogTypeNode.getNode(postId);
            }
            postNode.remove();
            session.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerPostService#deleteNode(java.lang.String)
     */
    public boolean deleteNode(String path) {
        Node node = null;
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            node = session.getNode(path);

            node.remove();
            session.save();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Move node.
     *
     * @param srcPath the src path
     * @param destPath the dest path
     * @return true, if successful
     */
    public boolean moveNode(String srcPath, String destPath) {
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            session.getWorkspace().move(srcPath, destPath);
            session.save();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.logout();
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerPostService#schedularService(java.lang.String)
     */
    public ArrayList<Node> schedularService(String scheduledDate) {
        System.out.println(">>" + scheduledDate);

        SimpleDateFormat dateFormat = (new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:00.000'+05:30'"));
        Date date = null;
        try {
            date = (new SimpleDateFormat("yyyy-MM-dd HH:mm"))
                    .parse(scheduledDate);
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String searchdate = dateFormat.format(date);
        System.out.println(">>" + searchdate);
        ArrayList<Node> list = new ArrayList<Node>();

        try {
            Session session = repos.login(new SimpleCredentials("admin",
                    "admin".toCharArray()));
            String jcrQuery = "select * from [nt:unstructured] where ISDESCENDANTNODE('/content/blogger/blog/') "
                    + "and (scheduledDate = CAST('"
                    + searchdate
                    + "' AS DATE))";
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
            System.out.println("-->" + list);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerPostService#getWebsiteContent(java.lang.String)
     */
    public JSONObject getWebsiteContent(String url) {
        JSONObject json = new JSONObject();
        JSONArray jarray = new JSONArray();
        String title = "";
        String description = "";
        try {
            Document doc = Jsoup.connect(url).get();
            title = doc.title();
            description = doc.select("meta[name=description]").get(0)
                    .attr("content");

        } catch (Exception e) {
        }
        try {
            json.accumulate("title", title);
            json.accumulate("description", description);
            jarray.put(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerPostService#postImage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.apache.sling.api.SlingHttpServletRequest, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("deprecation")
    public String postImage(String blogId, String type, String description,
            String tags, String accessType, String bloggerId, String postId,
            String queuedTime, SlingHttpServletRequest request,
            String editFlag, String formerPostType, String reBlogFlag,
            String sourceId, String reBloggerId) throws ServletException,
            IOException {
        String formerPostId = postId;
        if (editFlag.equals("true")) {
            if (!formerPostType.equals(type)) {
                // deletePost(formerPostType, postId, blogId);
                postId = "0";
            }
        }else if(editFlag.equals("reblog")){
            postId = "0";
        }
        Node blogNode, tagNode, nodeType, postNode = null, jcrNode, thumbnailJcrNode, pictureNode;
        globalDate = new Date();
        Calendar cal = Calendar.getInstance();
        String path = request.getSession().getServletContext().getRealPath("/")
                + "/temp/";
        String postDate = new SimpleDateFormat("EEE, dd MMM yyyy")
                .format(globalDate);
        String postTime = new SimpleDateFormat("hh:mm aa").format(globalDate);
        try {
            Session session = repos.login(new SimpleCredentials("admin",
                    "admin".toCharArray()));
            blogNode = session.getNode("/content/blogger/blog/" + blogId);
            if (blogNode.hasNode(type)) { // type : Post/Queued/Draft
                nodeType = blogNode.getNode(type);
            } else {
                nodeType = blogNode.addNode(type);
            }

            if (nodeType.hasProperty("postNumber")) {
                nodeType.setProperty("postNumber",
                        nodeType.getProperty("postNumber").getLong() + 1);
            } else {
                nodeType.setProperty("postNumber", 0);
            }

            if (postId.equals("0")) {

                if (editFlag.equals("true")) {
                    if (!formerPostType.equals(type)) {

                        session.getWorkspace().move(
                                "/content/blogger/blog/" + blogId + "/"
                                        + formerPostType + "/" + formerPostId,
                                "/content/blogger/blog/"
                                        + blogId
                                        + "/"
                                        + type
                                        + "/"
                                        + "Post"
                                        + nodeType.getProperty("postNumber")
                                                .getLong());
                        postNode = session.getNode("/content/blogger/blog/"
                                + blogId + "/" + type + "/" + "Post"
                                + nodeType.getProperty("postNumber").getLong());

                    }
                } else if (editFlag.equals("reblog")) {
                    session.getWorkspace().copy(
                            "/content/blogger/blog/" + reBloggerId + "/"
                                    + formerPostType + "/"
                                    + formerPostId,
                            "/content/blogger/blog/"
                                    + blogId
                                    + "/"
                                    + type
                                    + "/"
                                    + "Post"
                                    + nodeType
                                            .getProperty("postNumber")
                                            .getLong());
                    postNode = session.getNode("/content/blogger/blog/"
                            + blogId
                            + "/"
                            + type
                            + "/"
                            + "Post"
                            + nodeType.getProperty("postNumber")
                                    .getLong());
                } else {
                    postNode = nodeType.addNode("Post"
                            + nodeType.getProperty("postNumber").getLong());
                }

                postNode.setProperty("postDate", postDate);
                postNode.setProperty("postTime", postTime);
                postNode.setProperty("postTimeStamp", cal);
            } else {
                postNode = nodeType.getNode(postId);
            }

            postNode.setProperty("description", description);
            if( tags!=null && !tags.equals("")){
                if(postNode.hasNode("Tags")){
                    tagNode = postNode.getNode("Tags");  
                }else{
                    tagNode = postNode.addNode("Tags");
                }
                tagNode.setProperty("tags", tags.split(","));
            }
            postNode.setProperty("postType", "image");
            postNode.setProperty("accessType", accessType);
            postNode.setProperty("bloggerId", bloggerId);
            postNode.setProperty("reBlogFlag", reBlogFlag);
            postNode.setProperty("sourceId", sourceId);
            postNode.setProperty("reBloggerId", reBloggerId);
            if (type.equals("Queued")) {
                System.out.println(queuedTime);
                Date scheduledDate = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                        .parse(queuedTime);
                cal.setTime(scheduledDate);
                postNode.setProperty("scheduledDate", cal);
            }
            if (postNode.hasNode("Image")) {
                pictureNode = postNode.getNode("Image");
            } else {
                pictureNode = postNode.addNode("Image");
            }
            String mimeType = "";
            for (Entry<String, RequestParameter[]> e : request
                    .getRequestParameterMap().entrySet()) {
                for (RequestParameter p : e.getValue()) {
                    if (!p.isFormField()) {
                        mimeType = p.getContentType();
                        if (mimeType == null) {
                            mimeType = "application/octet-stream";
                        }
                        String randomNumber = "";

                        randomNumber = generateRandomNumber();
                        if (pictureNode.hasNode(randomNumber)) {
                            randomNumber = (Integer.parseInt(randomNumber) + 9018205)
                                    + "";
                        }
                        Node randomNode, imageNode, thumbnailNode = null;
                        randomNode = pictureNode.addNode(randomNumber);
                        imageNode = randomNode.addNode("xOp", "nt:file");
                        jcrNode = imageNode.addNode("jcr:content",
                                "nt:resource");
                        jcrNode.setProperty("jcr:data", p.getInputStream());
                        jcrNode.setProperty("jcr:lastModified",
                                Calendar.getInstance());
                        jcrNode.setProperty("jcr:mimeType", mimeType);
                        generateThumbnail(path, randomNumber,
                                p.getInputStream(), 320, false, 0);
                        File fileThumbnail = new File(path + randomNumber
                                + ".jpg");
                        InputStream thumbnailStream = new FileInputStream(
                                fileThumbnail);
                        thumbnailNode = randomNode.addNode("x320", "nt:file");
                        thumbnailJcrNode = thumbnailNode.addNode("jcr:content",
                                "nt:resource");

                        thumbnailJcrNode.setProperty("jcr:data",
                                thumbnailStream);
                        thumbnailJcrNode.setProperty("jcr:lastModified",
                                Calendar.getInstance());
                        thumbnailJcrNode.setProperty("jcr:mimeType", mimeType);
                        fileThumbnail.delete();
                    }
                }
            }
            session.save();
            // modifySession.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return postId;
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerPostService#postVideo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.apache.sling.api.SlingHttpServletRequest, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("deprecation")
    public String postVideo(String blogId, String type, String description,
            String tags, String accessType, String bloggerId, String postId,
            String queuedTime, SlingHttpServletRequest request,
            String editFlag, String formerPostType, String videoLinkFlag,
            String videoLink, String videoIframe, String videoSource,
            String videoThumbnail, String reBlogFlag, String sourceId,
            String reBloggerId) throws ServletException, IOException {
        String formerPostId = postId;
        String randomNumber = generateRandomNumber();
        if (editFlag.equals("true")) {
            if (!formerPostType.equals(type)) {
                // deletePost(formerPostType, postId, blogId);
                postId = "0";
            }
        }else if(editFlag.equals("reblog")){
            postId = "0";
        }
        Node blogNode, tagNode, nodeType, postNode = null, jcrNode, fileNode, videoNode;
        globalDate = new Date();
        Calendar cal = Calendar.getInstance();
        String path = request.getSession().getServletContext().getRealPath("/")
                + "/temp/";
        String postDate = new SimpleDateFormat("EEE, dd MMM yyyy")
                .format(globalDate);
        String postTime = new SimpleDateFormat("hh:mm aa").format(globalDate);
        Session session = null;
        String response = "";
        File files = new File(path);
        if (!files.exists()) {
            files.mkdirs();
        }
        try {
            byte[] data = request.getParameter("videoFile").getBytes(
                    "ISO-8859-1");
            if (data.length > 0) {
                File file = new File(path, randomNumber);
                FileOutputStream fout = new FileOutputStream(file);
                fout.write(data);
                fout.close();
                response = galleryService.convert(randomNumber, path);
            } else {
                response = "success";
            }
            if (response.equals("success")) {
                session = repos.login(new SimpleCredentials("admin", "admin"
                        .toCharArray()));
                blogNode = session.getNode("/content/blogger/blog/" + blogId);
                if (blogNode.hasNode(type)) { // type : Post/Queued/Draft
                    nodeType = blogNode.getNode(type);
                } else {
                    nodeType = blogNode.addNode(type);
                }

                if (nodeType.hasProperty("postNumber")) {
                    nodeType.setProperty("postNumber",
                            nodeType.getProperty("postNumber").getLong() + 1);
                } else {
                    nodeType.setProperty("postNumber", 0);
                }
                if (postId.equals("0")) {
                    if (editFlag.equals("true")) {
                        if (!formerPostType.equals(type)) {
                            session.getWorkspace().move(
                                    "/content/blogger/blog/" + blogId + "/"
                                            + formerPostType + "/"
                                            + formerPostId,
                                    "/content/blogger/blog/"
                                            + blogId
                                            + "/"
                                            + type
                                            + "/"
                                            + "Post"
                                            + nodeType
                                                    .getProperty("postNumber")
                                                    .getLong());
                            postNode = session.getNode("/content/blogger/blog/"
                                    + blogId
                                    + "/"
                                    + type
                                    + "/"
                                    + "Post"
                                    + nodeType.getProperty("postNumber")
                                            .getLong());

                        }
                    } else if (editFlag.equals("reblog")) {
                        session.getWorkspace().copy(
                                "/content/blogger/blog/" + reBloggerId + "/"
                                        + formerPostType + "/"
                                        + formerPostId,
                                "/content/blogger/blog/"
                                        + blogId
                                        + "/"
                                        + type
                                        + "/"
                                        + "Post"
                                        + nodeType
                                                .getProperty("postNumber")
                                                .getLong());
                        postNode = session.getNode("/content/blogger/blog/"
                                + blogId
                                + "/"
                                + type
                                + "/"
                                + "Post"
                                + nodeType.getProperty("postNumber")
                                        .getLong());
                    } else {
                        postNode = nodeType.addNode("Post"
                                + nodeType.getProperty("postNumber").getLong());
                    }

                    postNode.setProperty("postDate", postDate);
                    postNode.setProperty("postTime", postTime);
                    postNode.setProperty("postTimeStamp", cal);
                } else {
                    postNode = nodeType.getNode(postId);
                }
                postNode.setProperty("description", description);
                if( tags!=null && !tags.equals("")){
                    if(postNode.hasNode("Tags")){
                        tagNode = postNode.getNode("Tags");  
                    }else{
                        tagNode = postNode.addNode("Tags");
                    }
                    tagNode.setProperty("tags", tags.split(","));
                }
                postNode.setProperty("postType", "video");
                postNode.setProperty("accessType", accessType);
                postNode.setProperty("videoLinkFlag", videoLinkFlag);
                postNode.setProperty("videoLink", videoLink);
                postNode.setProperty("videoIframe", videoIframe);
                postNode.setProperty("videoSource", videoSource);
                postNode.setProperty("videoThumbnail", videoThumbnail);
                postNode.setProperty("reBlogFlag", reBlogFlag);
                postNode.setProperty("sourceId", sourceId);
                postNode.setProperty("reBloggerId", reBloggerId);
                if (type.equals("Queued")) {
                    System.out.println(queuedTime);
                    Date scheduledDate = new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm").parse(queuedTime);
                    cal.setTime(scheduledDate);
                    postNode.setProperty("scheduledDate", cal);
                }
                if(videoLinkFlag.equals("true")){
                    if(postNode.hasNode("Video")){
                        postNode.getNode("Video").remove();
                    }
                }
                if (data.length > 0 && videoLinkFlag.equals("false")) {
                    if(postNode.hasNode("Video")){
                        postNode.getNode("Video").remove();
                    }
                    videoNode = postNode.addNode("Video");
                    fileNode = videoNode.addNode(randomNumber + ".webm",
                            "nt:file");
                    jcrNode = fileNode.addNode("jcr:content", "nt:resource");
                    jcrNode.setProperty("jcr:data", new FileInputStream(path
                            + randomNumber + ".webm"));
                    jcrNode.setProperty("jcr:lastModified",
                            Calendar.getInstance());
                    jcrNode.setProperty("jcr:mimeType", "video/webm");
                }
                session.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        } finally {
            File file = new File(path + randomNumber + ".webm");
            file.delete();
            File file2 = new File(path + randomNumber + "");
            file2.delete();
        }

        return "success";
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerPostService#postAudio(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.apache.sling.api.SlingHttpServletRequest, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("deprecation")
    public String postAudio(String blogId, String type, String description,
            String tags, String accessType, String bloggerId, String postId,
            String queuedTime, SlingHttpServletRequest request,
            String editFlag, String formerPostType, String audioLinkFlag,
            String audioLink, String audioTrack, String audioArtist,
            String audioAlbum, String coverPage, String reBlogFlag, String sourceId,
            String reBloggerId) throws ServletException, IOException {
        String formerPostId = postId;
        String randomNumber = generateRandomNumber();
        if (editFlag.equals("true")) {
            if (!formerPostType.equals(type)) {
                // deletePost(formerPostType, postId, blogId);
                postId = "0";
            }
        }else if(editFlag.equals("reblog")){
            postId = "0";
        }
        Node blogNode, tagNode, nodeType, postNode = null, jcrNode, fileNode, audioNode;
        globalDate = new Date();
        Calendar cal = Calendar.getInstance();
        String path = request.getSession().getServletContext().getRealPath("/")
                + "/temp/";
        String postDate = new SimpleDateFormat("EEE, dd MMM yyyy")
                .format(globalDate);
        String postTime = new SimpleDateFormat("hh:mm aa").format(globalDate);
        Session session = null;
        File files = new File(path);
        if (!files.exists()) {
            files.mkdirs();
        }
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            blogNode = session.getNode("/content/blogger/blog/" + blogId);
            if (blogNode.hasNode(type)) { // type : Post/Queued/Draft
                nodeType = blogNode.getNode(type);
            } else {
                nodeType = blogNode.addNode(type);
            }

            if (nodeType.hasProperty("postNumber")) {
                nodeType.setProperty("postNumber",
                        nodeType.getProperty("postNumber").getLong() + 1);
            } else {
                nodeType.setProperty("postNumber", 0);
            }
            if (postId.equals("0")) {
                if (editFlag.equals("true")) {
                    if (!formerPostType.equals(type)) {
                        session.getWorkspace().move(
                                "/content/blogger/blog/" + blogId + "/"
                                        + formerPostType + "/"
                                        + formerPostId,
                                "/content/blogger/blog/"
                                        + blogId
                                        + "/"
                                        + type
                                        + "/"
                                        + "Post"
                                        + nodeType
                                                .getProperty("postNumber")
                                                .getLong());
                        postNode = session.getNode("/content/blogger/blog/"
                                + blogId
                                + "/"
                                + type
                                + "/"
                                + "Post"
                                + nodeType.getProperty("postNumber")
                                        .getLong());

                    }
                } else if (editFlag.equals("reblog")) {
                    session.getWorkspace().copy(
                            "/content/blogger/blog/" + reBloggerId + "/"
                                    + formerPostType + "/"
                                    + formerPostId,
                            "/content/blogger/blog/"
                                    + blogId
                                    + "/"
                                    + type
                                    + "/"
                                    + "Post"
                                    + nodeType
                                            .getProperty("postNumber")
                                            .getLong());
                    postNode = session.getNode("/content/blogger/blog/"
                            + blogId
                            + "/"
                            + type
                            + "/"
                            + "Post"
                            + nodeType.getProperty("postNumber")
                                    .getLong());
                } else {
                    postNode = nodeType.addNode("Post"
                            + nodeType.getProperty("postNumber").getLong());
                }

                postNode.setProperty("postDate", postDate);
                postNode.setProperty("postTime", postTime);
                postNode.setProperty("postTimeStamp", cal);
            } else {
                postNode = nodeType.getNode(postId);
            }
            postNode.setProperty("description", description);
            if( tags!=null && !tags.equals("")){
                if(postNode.hasNode("Tags")){
                    tagNode = postNode.getNode("Tags");  
                }else{
                    tagNode = postNode.addNode("Tags");
                }
                tagNode.setProperty("tags", tags.split(","));
            }
            postNode.setProperty("postType", "audio");
            postNode.setProperty("accessType", accessType);
            postNode.setProperty("audioLinkFlag", audioLinkFlag);
            postNode.setProperty("audioLink", audioLink);
            postNode.setProperty("audioTrack", audioTrack);
            postNode.setProperty("audioAlbum", audioAlbum);
            postNode.setProperty("audioArtist", audioArtist);
            postNode.setProperty("coverPage", coverPage);
            postNode.setProperty("reBlogFlag", reBlogFlag);
            postNode.setProperty("sourceId", sourceId);
            postNode.setProperty("reBloggerId", reBloggerId);
            if (type.equals("Queued")) {
                System.out.println(queuedTime);
                Date scheduledDate = new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm").parse(queuedTime);
                cal.setTime(scheduledDate);
                postNode.setProperty("scheduledDate", cal);
            }
            System.out.println(audioLinkFlag);
            if (audioLinkFlag.equals("false")) {
                String mimeType = "";
                for (Entry<String, RequestParameter[]> e : request
                        .getRequestParameterMap().entrySet()) {
                    for (RequestParameter p : e.getValue()) {
                        if (!p.isFormField()) {
                            mimeType = p.getContentType();
                            if (mimeType == null) {
                                mimeType = "application/octet-stream";
                            }
                            if(postNode.hasNode("Audio")){
                                postNode.getNode("Audio").remove();
                            }
                            audioNode = postNode.addNode("Audio");
                            fileNode = audioNode.addNode(randomNumber,
                                    "nt:file");
                            jcrNode = fileNode.addNode("jcr:content", "nt:resource");
                            System.out.println(p.getFileName());
                            jcrNode.setProperty("jcr:data", p.getInputStream());
                            jcrNode.setProperty("jcr:lastModified",
                                    Calendar.getInstance());
                            jcrNode.setProperty("jcr:mimeType", mimeType);
                        }
                    }
                }
            }else{
                    if(postNode.hasNode("Audio")){
                        postNode.getNode("Audio").remove();
                }
            }
            session.save();
            
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        } finally {
            File file = new File(path + randomNumber + ".webm");
            file.delete();
            File file2 = new File(path + randomNumber + "");
            file2.delete();
        }

        return "success";
    }
    
    /* (non-Javadoc)
     * @see org.blogger.service.BloggerPostService#generateRandomNumber()
     */
    public String generateRandomNumber() {
        Random rand = new Random();
        long accumulator = 1 + rand.nextInt(9); // ensures that the 16th digit
                                                // isn't 0
        for (int i = 0; i < 15; i++) {
            accumulator *= 10L;
            accumulator += rand.nextInt(10);
        }
        return accumulator + "";
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerPostService#generateThumbnail(java.lang.String, java.lang.String, java.io.InputStream, int, boolean, int)
     */
    public void generateThumbnail(String filePath, String filname,
            InputStream fileData, int width, boolean heightFlag, int height) {
        try {
            BufferedImage src = ImageIO.read(fileData);
            if (src == null) {
                final StringBuffer sb = new StringBuffer();
                for (String fmt : ImageIO.getReaderFormatNames()) {
                    sb.append(fmt);
                    sb.append(' ');
                }
                throw new IOException(
                        "Unable to read image, registered formats: " + sb);
            }

            final double scale = (double) width / src.getWidth();

            int destWidth = width;
            int destHeight = 0;
            if (heightFlag) {
                destHeight = height;
            } else {
                destHeight = new Double(src.getHeight() * scale).intValue();
            }

            BufferedImage dest = new BufferedImage(destWidth, destHeight,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = dest.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(
                    (double) destWidth / src.getWidth(), (double) destHeight
                            / src.getHeight());
            g.drawRenderedImage(src, at);
            File files = new File(filePath);
            if (!files.exists()) {
                files.mkdirs();
            }
            File fileThumbnail = new File(filePath + filname + ".jpg");
            ImageIO.write(dest, "jpg", fileThumbnail);
        } catch (Exception e) {

        }
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerPostService#postLink(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void postLink(String bloggerId, String blogId, String link,
            String title, String description, String postId, String tags,
            String accessType, String type, String queuedTime, String editFlag,
            String formerPostType, String reBlogFlag, String sourceId,
            String reBloggerId) {
        if (editFlag.equals("true")) {
            if (!formerPostType.equals(type)) {
                deletePost(formerPostType, postId, blogId);
                postId = "0";
            }
        }
        Node bloggerNode, blogNode = null, tagNode, postNode, nodeType = null;
        globalDate = new Date();

        Calendar cal = Calendar.getInstance();
        String postDate = new SimpleDateFormat("EEE, dd MMM yyyy")
                .format(globalDate);
        String postTime = new SimpleDateFormat("hh:mm aa").format(globalDate);
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            bloggerNode = session.getNode("/content/blogger/blog");

            if (bloggerNode.hasNode(blogId)) {
                blogNode = bloggerNode.getNode(blogId);
            }

            if (blogNode.hasNode(type)) { // type : Post/Queued/Draft
                nodeType = blogNode.getNode(type);
            } else {
                nodeType = blogNode.addNode(type);
            }

            if (nodeType.hasProperty("postNumber")) {
                nodeType.setProperty("postNumber",
                        nodeType.getProperty("postNumber").getLong() + 1);
            } else {
                nodeType.setProperty("postNumber", 0);
            }

            if (postId.equals("0")) {
                postNode = nodeType.addNode("Post"
                        + nodeType.getProperty("postNumber").getLong());
                postNode.setProperty("postDate", postDate);
                postNode.setProperty("postTime", postTime);
                postNode.setProperty("postTimeStamp", cal);
            } else {
                postNode = nodeType.getNode(postId);
            }

            postNode.setProperty("link", link);
            postNode.setProperty("title", title);
            postNode.setProperty("description", description);
            if( tags!=null && !tags.equals("")){
                if(postNode.hasNode("Tags")){
                    tagNode = postNode.getNode("Tags");  
                }else{
                    tagNode = postNode.addNode("Tags");
                }
                tagNode.setProperty("tags", tags.split(","));
            }
            postNode.setProperty("postType", "link");
            postNode.setProperty("accessType", accessType);
            postNode.setProperty("bloggerId", bloggerId);
            postNode.setProperty("reBlogFlag", reBlogFlag);
            postNode.setProperty("sourceId", sourceId);
            postNode.setProperty("reBloggerId", reBloggerId);
            if (type.equals("Queued")) {
                System.out.println(queuedTime);
                Date scheduledDate = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                        .parse(queuedTime);
                cal.setTime(scheduledDate);
                postNode.setProperty("scheduledDate", cal);
            }
            session.save();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.logout();
        }
    }

    /**
     * Gets the service.
     *
     * @param serviceUrl the service url
     * @return the service
     */
    public String getService(String serviceUrl) {
        URL url;
        StringBuilder sb = new StringBuilder();
        try {

            url = new URL(serviceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /* (non-Javadoc)
     * @see org.blogger.service.BloggerPostService#extractVideoData(java.lang.String, java.lang.String)
     */
    public String extractVideoData(String type, String videoLink) {
        JSONObject json = new JSONObject();
        String result = "";
        JSONObject jsonResult = null;
        try {
            if (type.equals("youtube")) {
                json.accumulate("service", type);
                json.accumulate("embedCode", "http://youtube.com/embed");

            } else if (type.equals("vimeo")) {
                result = getService("http://vimeo.com/api/oembed.json?url="
                        + videoLink);
                jsonResult = new JSONObject(result);
                json.accumulate("iframe", jsonResult.get("html").toString());
                json.accumulate("thumbnail", jsonResult.get("thumbnail_url")
                        .toString());
            } else if (type.equals("dailymotion")) {
                result = getService("http://www.dailymotion.com/services/oembed?url="
                        + videoLink);
                jsonResult = new JSONObject(result);
                json.accumulate("iframe", jsonResult.get("html").toString());
                json.accumulate("thumbnail", jsonResult.get("thumbnail_url")
                        .toString());
            } else if (type.equals("metacafe")) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json.toString();
    }

    /**
     * Decode image.
     *
     * @param source the source
     * @param filePath the file path
     * @param filename the filename
     * @return the string
     */
    private String decodeImage(String source, String filePath, String filename) {
        
        byte[] data;
        FileOutputStream fos = null;
        try {
            data = Base64.decodeBase64(source.getBytes());
            ByteArrayInputStream ins = new ByteArrayInputStream(data);
            /*generateThumbnail(path, randomNumber,
                    p.getInputStream(), 320, true, 150);
            generate(ins, 150, 150, filePath, filename);*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                }
        }
        return "done";
    }
    
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {

    }
}
