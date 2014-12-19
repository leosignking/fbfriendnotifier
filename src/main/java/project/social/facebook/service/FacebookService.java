package project.social.facebook.service;

/**
 * 
 * @author sridhar
 *
 */
public interface FacebookService {
	void getFriendList(String accessToken);
	void getFriendListInApp(String accessToken);
}
