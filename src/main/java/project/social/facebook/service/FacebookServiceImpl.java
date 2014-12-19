package project.social.facebook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.Reference;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FacebookServiceImpl implements FacebookService {

	@Autowired
	private RestTemplate restTemplate;

	public void getFriendList(String accessToken) {
		Facebook facebook = new FacebookTemplate(accessToken);

		List<FacebookProfile> fProfiles = facebook.friendOperations()
				.getFriendProfiles();

		for (FacebookProfile profile : fProfiles) {
			System.out.println(profile.getId());
		}

	}

	public void getFriendListInApp(String accessToken) {
		Facebook facebook = new FacebookTemplate(accessToken);
		List<Reference> friends = facebook.friendOperations().getFriendLists();
		for(Reference r : friends) {
			System.out.println(r.getName());
		}
	}
}
