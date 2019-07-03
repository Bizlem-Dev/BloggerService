package org.blogger.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.jcr.Node;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Interface BloggerPostService mainly used for adding,modifying and
 * removing (text,audio,video,quote,link,images) post,draft and queue Posts.
 */
public interface BloggerPostService {

	/**
	 * Post text. This method is used for adding,editing and reBlogging of text
	 * post.
	 * 
	 * @param bloggerId
	 *            here bloggerId is bloggerId of selected blog node.
	 * 
	 * @param blogId
	 *            here blogId is selected blogId in which blog you are going to
	 *            Post .
	 * @param text
	 *            here text is post text type content.
	 * @param title
	 *            here title is selected blogId
	 * @param postId
	 *            here postId is post node name.
	 * @param tags
	 *            here tags is post tags.
	 * @param postType
	 *            here postType is type of post weather it is
	 *            text.image,quote,audio,video,link.
	 * @param accessType
	 *            here access type is post access type weather it is public or
	 *            private.
	 * @param type
	 *            the type
	 * @param queuedTime
	 *            the queued time
	 * @param editFlag
	 *            the edit flag
	 * @param formerPostType
	 *            here editFlag and formerPostType used at the time of editing.
	 * @param reBlogFlag
	 *            here reBlogFlag is true or false,by default it is false.When
	 *            it is reBlogged it will change to true.
	 * @param sourceId
	 *            here sourceId is blogId which you reblogged.If it is your own
	 *            blog then it is null.
	 * @param reBloggerId
	 *            here reBloggerId is blogId which you reblogged. here
	 *            reBloggerId,reBlogFlag,sourceId is used at the of reblogging.
	 */
	void postText(String bloggerId, String blogId, String text, String title,
			String postId, String tags, String postType, String accessType,
			String type, String queuedTime, String editFlag,
			String formerPostType, String reBlogFlag, String sourceId,
			String reBloggerId);

	/**
	 * Post image. This method is used for adding,editing and reBlogging of
	 * image post.
	 * 
	 * @param blogId
	 *            here blogId is selected blogId in which blog you are going to
	 *            Post .
	 * @param type
	 *            the type
	 * @param description
	 *            here description is image type post description.
	 * @param tags
	 *            here tags is image post tags.
	 * @param accessType
	 *            here accessType is post access type weather it is public or
	 *            private.
	 * @param bloggerId
	 *            here bloggerId is bloggerId of selected blog node
	 * @param postId
	 *            here postId is image post node name.
	 * @param queuedTime
	 *            the queued time
	 * @param request
	 *            the request
	 * @param editFlag
	 *            the edit flag
	 * @param formerPostType
	 *            the former post type
	 * @param reBlogFlag
	 *            here reBlogFlag is reBlogFlag weather true or false,default it
	 *            is false.If you reblog ,then it will change to true.
	 * @param sourceId
	 *            here sourceId is blogId which you reblogged.If it is your own
	 *            blog then it is null.
	 * @param reBloggerId
	 *            here reBloggerId is blogId which you reblogged
	 * @return the string
	 * @throws ServletException
	 *             the servlet exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	String postImage(String blogId, String type, String description,
			String tags, String accessType, String bloggerId, String postId,
			String queuedTime, SlingHttpServletRequest request,
			String editFlag, String formerPostType, String reBlogFlag,
			String sourceId, String reBloggerId) throws ServletException,
			IOException;

	/**
	 * Schedular service. this method is n't used over here.
	 * 
	 * @param scheduledDate
	 *            the scheduled date
	 * @return the array list
	 */
	ArrayList<Node> schedularService(String scheduledDate);

	/**
	 * Gets the website content. This method is used for getting details of
	 * specific url enter by user.It will fetch website details from meta data
	 * of html page.It is using Jsoup web services for fetching of web content.
	 * 
	 * @param url
	 *            here url is url enter by the user.
	 * @return the website content
	 */
	JSONObject getWebsiteContent(String url);

	/**
	 * Generate thumbnail. This method is used for generating image Thumbnail.
	 * 
	 * @param filePath
	 *            here filePath is path of uploaded image path.
	 * @param filname
	 *            here filename is name of uploaded image.
	 * @param fileData
	 *            the file data
	 * @param width
	 *            here width is width of image.
	 * @param heightFlag
	 *            the height flag
	 * @param height
	 *            here height is height of image.
	 */
	void generateThumbnail(String filePath, String filname,
			InputStream fileData, int width, boolean heightFlag, int height);

	/**
	 * Generate random number. This method is used for generating 16 digits
	 * number.Generated number is used for giving node name.
	 * 
	 * @return the string
	 */
	String generateRandomNumber();

	/**
	 * Delete post. This method is used for removing of particular post of blog.
	 * 
	 * @param postType
	 *            here postType is type of post
	 *            weather(text,link,audio,video,quote)
	 * @param postId
	 *            here postId is post node name.
	 * @param blogId
	 *            here blogId is blogId in which you are going remove post.
	 */
	void deletePost(String postType, String postId, String blogId);

	/**
	 * Delete node. This method is used for removing of Node blogg.
	 * 
	 * @param path
	 *            here path is post node path.
	 * @return true, if successful
	 */
	boolean deleteNode(String path);

	/**
	 * Post link. This method is used for adding,editing and reBlogging of Link
	 * post.
	 * 
	 * @param bloggerId
	 *            here bloggerId is bloggerId of selected blog node.
	 * @param blogId
	 *            here blogId is selected blogId in which blog you are going to
	 *            Post .
	 * @param link
	 *            here link is url of link type post.
	 * @param title
	 *            here title is link type post title.
	 * @param description
	 *            here description is link type post Description.
	 * @param postId
	 *            here postId is link post node name.
	 * @param tags
	 *            here tags is link type post tags.
	 * @param accessType
	 *            here access type is post access type weather it is public or
	 *            private.
	 * @param type
	 *            the type
	 * @param queuedTime
	 *            the queued time
	 * @param editFlag
	 *            the edit flag
	 * @param formerPostType
	 *            the former post type
	 * @param reBlogFlag
	 *            here reBlogFlag is reBlogFlag weather true or false,default it
	 *            is false.If you reblog ,then it will change to true.
	 * @param sourceId
	 *            here sourceId is blogId which you reblogged.If it is your own
	 *            blog then it is null.
	 * @param reBloggerId
	 *            here reBloggerId is blogId of blog which u reblogged.
	 */
	void postLink(String bloggerId, String blogId, String link, String title,
			String description, String postId, String tags, String accessType,
			String type, String queuedTime, String editFlag,
			String formerPostType, String reBlogFlag, String sourceId,
			String reBloggerId);

	/**
	 * Extract video data. This method is used for extracting Video data like
	 * video image etc.
	 * 
	 * @param type
	 *            the type
	 * @param videoLink
	 *            the video link
	 * @return the string
	 */
	String extractVideoData(String type, String videoLink);

	/**
	 * Post video. This method is used for adding,editing and reBlogging of
	 * Video post.
	 * 
	 * @param blogId
	 *            here blogId is selected blogId in which blog you are going to
	 *            Post .
	 * @param type
	 *            the type
	 * @param description
	 *            here description is video type post Description.
	 * @param tags
	 *            here tags is post tags.
	 * @param accessType
	 *            here access type is post access type weather it is public or
	 *            private.
	 * @param bloggerId
	 *            here bloggerId is bloggerId of selected blog node.
	 * @param postId
	 *            here postId is video post node name.
	 * @param queuedTime
	 *            the queued time
	 * @param request
	 *            the request
	 * @param editFlag
	 *            the edit flag
	 * @param formerPostType
	 *            the former post type
	 * @param videoLinkFlag
	 *            the video link flag
	 * @param videoLink
	 *            the video link
	 * @param videoIframe
	 *            here videoIframe is video embed code.
	 * @param videoSource
	 *            the video source
	 * @param videoThumbnail
	 *            here videoThumbnail is video image.
	 * @param reBlogFlag
	 *            here reBlogFlag is reBlogFlag weather true or false,default it
	 *            is false.If you reblog ,then it will change to true.
	 * @param sourceId
	 *            here sourceId is blogId which you reblogged.If it is your own
	 *            blog then it is null.
	 * @param reBloggerId
	 *            here reBloggerId is blogId which you reblogged.
	 * @return the string
	 * @throws ServletException
	 *             the servlet exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	String postVideo(String blogId, String type, String description,
			String tags, String accessType, String bloggerId, String postId,
			String queuedTime, SlingHttpServletRequest request,
			String editFlag, String formerPostType, String videoLinkFlag,
			String videoLink, String videoIframe, String videoSource,
			String videoThumbnail, String reBlogFlag, String sourceId,
			String reBloggerId) throws ServletException, IOException;

	/**
	 * Post audio. This method is used for adding,editing and reBlogging of
	 * Audio post.
	 * 
	 * @param blogId
	 *            here blogId is selected blogId in which blog you are going to
	 *            Post .
	 * @param type
	 *            the type
	 * @param description
	 *            here description is audio type post Description.
	 * @param tags
	 *            here tags is audio type post tags.
	 * @param accessType
	 *            here access type is post access type weather it is public or
	 *            private.
	 * @param bloggerId
	 *            here bloggerId is bloggerId of selected blog node.
	 * @param postId
	 *            here postId is audio post node name.
	 * @param queuedTime
	 *            the queued time
	 * @param request
	 *            the request
	 * @param editFlag
	 *            the edit flag
	 * @param formerPostType
	 *            the former post type
	 * @param audioLinkFlag
	 *            the audio link flag
	 * @param audioLink
	 *            here audioLink is source of audio type post.
	 * @param audioTrack
	 *            here audioTrack is audio type post name of audio Track.
	 * @param audioArtist
	 *             here audioArtist is audio type post name of audio Artist.
	 * @param audioAlbum
	 *             here audioAlbum is audio type post name of audio Album.
	 * @param coverPage
	 *            here coverPage is image of video track.
	 * @param reBlogFlag
	 *            here reBlogFlag is reBlogFlag weather true or false,default it
	 *            is false.If you reblog ,then it will change to true.
	 * @param sourceId
	 *            here sourceId is blogId which you reblogged.If it is your own
	 *            blog then it is null.
	 * @param reBloggerId
	 *            here reBloggerId is blogId which you reblogged.
	 * @return the string
	 * @throws ServletException
	 *             the servlet exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	String postAudio(String blogId, String type, String description,
			String tags, String accessType, String bloggerId, String postId,
			String queuedTime, SlingHttpServletRequest request,
			String editFlag, String formerPostType, String audioLinkFlag,
			String audioLink, String audioTrack, String audioArtist,
			String audioAlbum, String coverPage, String reBlogFlag,
			String sourceId, String reBloggerId) throws ServletException,
			IOException;

	/**
	 * Gets the user blog. This method is used for getting all blog nodes of
	 * corresponding to specfic user Node..
	 * 
	 * @param bloggerId
	 *            here bloggerId is coming from session.
	 * @return the user blog
	 */
	ArrayList<String> getUserBlog(String bloggerId);

}
