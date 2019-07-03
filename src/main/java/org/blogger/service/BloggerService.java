package org.blogger.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.jcr.Node;

import org.apache.sling.api.SlingHttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Interface BloggerService is used for creation of blogger node in content
 * node .And creation of child nodes of blogger Node i.e user and blog.Node user
 * has child nodes same name with user bloggerId.Each bloggerId node at most
 * child nodes i.e like,blogId and followings node.Node blog has child nodes
 * same name with bloges created by user.Each blogId node at most child nodes
 * i.e Post,Queued,Draft,followers,avatar. And each Post node at most child
 * nodes i.e post0,post1....so on.Each child node at most child nodes Tags,Like.
 * 
 * 
 * Its mainly deals with creation,deletation,modifying,displaying user
 * posts,following,liking blog.
 */
public interface BloggerService {

    /**
     * This method is used for creation of new account in blogger,it will also
     * creates default blog same name with bloggerId,if it is exist,otherwise
     * with different name.Here userId is CAS loginId and bloggerId is enter by
     * user for default blogId.
     * 
     * @param bloggerId
     *            here bloggerId is enter by user to create new node in user of
     *            blogger Node.
     * @param userId
     *            here userId is CAS login Id.
     */
    void addUser(String bloggerId, String userId);
    public String addUserBlogOthers(String bloggerId, String userId);

    /**
     * This method to used to check weather blogger name enter by user already
     * exist or n't in user node.If it is already exist you have to create your
     * blogger node with different name which is n't exist.
     * 
     * @param bloggerId
     *            here bloggerId is enter by user to create new node in user
     *            node of blogger Node.
     * @param userId
     *            here userId is CAS login Id
     * 
     * 
     * @return true, if successful
     */
    boolean userCheck(String bloggerId, String userId);

    /**
     * Blog check. This method to used at the time of creation of new blog,to
     * check weather blog name enter by user already exist or n't.If it is
     * already exist you have to create your blog with different name which is
     * n't exist.
     * 
     * @param blogId
     *            here blogId is enter by user to create new blog node in blog
     *            Node of blogger Node.
     * 
     * @return true, if successful
     */
    boolean blogCheck(String blogId);

    /**
     * This method is used for creation of new blog in blogger,
     * 
     * 
     * @param blogId
     *            here blogId is new blogId Node in blog node of blogger node.
     * @param blogTitle
     *            here blogTitle is new blog Node blogTitle.
     * @param description
     *            here description is new blog Node description.
     * @param request
     *            here request is file,video upload object.
     * @param userBlogId
     *            here userBlogId is user default blog Node.
     * @param userBloggerId
     *            here userBloggerId is user bloggerId .
     * @param userId
     *            here userId is CAS loginId.
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void addBlog(String blogId, String blogTitle, String description,
            SlingHttpServletRequest request, String userBlogId,
            String userBloggerId, String userId) throws IOException;

    /**
     * This method is used for Edit of already exist blog in blogger,you can
     * modify your blog title,description,avatar.
     * 
     * @param blogNodeId
     *            the blog node id
     * @param blogId
     *            the blog id
     * @param title
     *            the title
     * @param description
     *            the description
     * @param blogType
     *            the blog type
     * @param bloggerId
     *            the blogger id
     * @param userId
     *            the user id
     * @param request
     *            the request
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void editBlog(String blogNodeId, String blogId, String title,
            String description, String blogType, String bloggerId,
            String userId, SlingHttpServletRequest request)
            throws FileNotFoundException, IOException;

    /**
     * Follow blog. This method is used for follow or unfollow any other blog
     * except your default blog.
     * 
     * @param blogId
     *            here blogId is which you are going follow or unfollow.
     * @param param
     *            here param is follow or unfollow.
     * @param userBlogId
     *            here blogId is user session blogId.
     * @param userBloggerId
     *            here bloggerId is user session blogId.
     * @param userId
     *            here userId is CAS loginId.
     * @return true, if successful
     */
    boolean followBlog(String blogId, String param, String userBlogId,
            String userBloggerId, String userId);

    /**
     * Search blog. This method is used for search blog.
     * 
     * @param parameter
     *            here parameter is string enter by user in search div.
     * @return the array list
     */
    ArrayList<Node> searchBlog(String parameter);

    /**
     * User content. This method is used for creation of session of particular
     * user,here userId is cache login id.
     * 
     * @param userId
     *            here userId is CAS loginId.
     * @return the map
     */
    Map<String, String> userContent(String userId);

    /**
     * All post nodes. This method is used for fetching Post Nodes of user all
     * post of default blog and user followed blog all post according to
     * timeStamp.Here we also limit,in one time it will fetch only 10 posts.For
     * further posts we again call same method ajax,methods calls only when
     * browser scroll reach at bottom.
     * 
     * 
     * @param blodId
     *            here blogId is user session blogId.
     * @param bloggerId
     *            here bloggerId is user session bloggerId.
     * @param offset
     *            here offset is query starting element.
     * @return the array list
     */
    ArrayList<Node> allPostNodes(String blodId, String bloggerId, int offset);

    /**
     * Sets the post limit. This methods calls only when browser scroll reach at
     * bottom. In method we call methods allPostNodes() is used for fetching
     * data in the form Post nodes then we render data in the form of html,then
     * we render data in the form of json array of all user post of default blog
     * and user followed blog all post according to timeStamp using ajax we send
     * data to home page,attend it with already exist post.
     * 
     * @param parameter
     *            the parameter
     * @param blogId
     *            here blogId is user session blogId.
     * @param bloggerId
     *            here bloggerId is user session bloggerId.
     * @param contextPath
     *            the context path
     * @param param
     *            the param
     * @param keyword
     *            the keyword
     * @return the string
     */
    String setPostLimit(String parameter, String blogId, String bloggerId,
            String contextPath, String param, String keyword);

    /**
     * Like blog. This method is used for like and dislike any post of any blog.
     * 
     * @param blogPath
     *            here blogPath is path of particular post of blog which you are
     *            going to like or unlike.
     * @param bloggerId
     *            here bloggerId is user session bloggerId.
     * @param blogId
     *            here blogId is user session blogId.
     * @return the string
     */
    String likeBlog(String blogPath, String bloggerId, String blogId);

    /**
     * Delete blog. This method is used for delete or remove any post of your
     * blog only.
     * 
     * @param blogPath
     *            here blogPath is path of particular post of blog which you are
     *            going to remove or delete.
     * @param bloggerId
     *            here bloggerId is user session bloggerId.
     * @return the string
     */
    String deleteBlog(String blogPath, String bloggerId);

    /**
     * Search tag based. This method is used for searching of posts based on
     * particular tag mentioned in post.
     * 
     * @param parameter
     *            the parameter
     * @param blogId
     *            here blogId is user session blogId.
     * @param bloggerId
     *            here bloggerId is user session bloggerId.
     * @param offset
     *            here offset is query starting element.
     * @return the array list
     */
    ArrayList<Node> searchTagBased(String parameter, String blogId,
            String bloggerId, int offset);

    /**
     * Follower search. This method is used for search blog other your default
     * blog and already followed blog.
     * 
     * @param parameter
     *            the parameter
     * @param bloggerId
     *            here bloggerId is user session bloggerId.
     * @param contextPath
     *            here contextPath is project path.
     * @param blogId
     *            here blogId is user session blogId.
     * @return the string
     */
    String followerSearch(String parameter, String bloggerId,
            String contextPath, String blogId);

    /**
     * Tag search.This method is used for tag search.
     * 
     * @param parameter
     *            here parameter is enter by user for searching tag.
     * @return the array list
     */
    ArrayList<String> tagSearch(String parameter);

    /**
     * Search blog div.This method is used for search blog.
     * 
     * @param key
     *            the key
     * @return the array list
     */
    ArrayList<Map> searchBlogDiv(String key);

    /**
     * Delete blog id. This method is used for permanent delete of particular
     * blog of user other than default blog.
     * 
     * @param userBlogId
     *            here blogId is user specific blog other than default blog.
     * @param bloggerId
     *            here bloggerId is user session bloggerId.
     */
    void deleteBlogId(String userBlogId, String bloggerId);

    /**
     * Delete user account. This method is used for delete user Account,if you
     * going delete your default blog it will also delete your all other blog
     * also.
     * 
     * @param blogId
     *            here blogId is user session blogId.
     * @param bloggerId
     *            here bloggerId is user session bloggerId.
     */
    void deleteUserAccount(String blogId, String bloggerId);

    /**
     * Queued post.This method is used for scheduler,for posting all the post
     * available in queue,scheduler runs after every 30 mins.It will move all
     * queue nodes into post node based on upon time.
     * 
     * @return the string
     */
    String queuedPost();

    /**
     * Tag list. his method is used for fetching all tags of particular
     * blog,used for tagCloud on the blog view page.
     * 
     * @param blogId
     *            here blogId is user specific blogId.
     * @return the array list
     */
    ArrayList<String> tagList(String blogId);

    /**
     * Random blog. This method is used for displaying random blog i.e other
     * then your blog on blog view page.
     * 
     * @param bloggerId
     *            here bloggerId is user session bloggerId.
     * @param blogId
     *            here blogId is user session blogId.
     * @param path
     *            path contains the context path of application.
     * @return the string
     */
    String randomBlog(String bloggerId, String blogId, String path);

    /**
     * Change theme. This method is used for customization of your specific blog
     * You can change your blog theme,blog background image.
     * 
     * @param blogId
     *            here blogId is user specific blogId.
     * @param cssSelect
     *            here cssSelect is blog cssSelect property.
     * @param imageSelect
     *            here imageSelect is blog imageSelect property.
     * @param request
     *            the request
     */
    void changeTheme(String blogId, String cssSelect, String imageSelect,
            SlingHttpServletRequest request);

    /**
     * Re blog post. This method is used for rePost of blog view page.
     * 
     * @param postPath
     *            here postPath is complete postPath which you are going to
     *            reBlog.
     * @return the array list
     */
    ArrayList<Node> reBlogPost(String postPath);

}
