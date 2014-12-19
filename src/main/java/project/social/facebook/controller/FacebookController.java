package project.social.facebook.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.social.facebook.service.FacebookService;

@Controller
@RequestMapping(value = "/facebook")
public class FacebookController {

	private static final String APP_SECRET = "aa2323bfe3b44e3de0986d12adb88";
	private static final String APP_ID = "240081369347003";
	
	@Autowired
	FacebookService facebookService;

	@RequestMapping(value = "/signin.do")
	public void loginWithFacebook(HttpServletResponse response,
			HttpServletRequest request) {

		System.out.println(request.getServerName());
		System.out.println(request.getScheme());
		System.out.println(request.getServerPort());
		String faceBookLoginUrl = "https://graph.facebook.com/oauth/authorize?client_id="
				+ APP_ID
				+ "&display=page&redirect_uri="
				+ request.getScheme()
				+ "://"
				+ request.getServerName()
				+ ":"
				+ request.getServerPort()+request.getContextPath() + "/facebook/handleAuth.do&scope=email,public_profile,user_friends,read_friendlists";
		//publish_actions,publish_stream,user_photos

		try {
			response.sendRedirect(faceBookLoginUrl);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/handleAuth.do")
	public void handleAuthResponse(HttpServletResponse response,
			HttpServletRequest request,
			@RequestParam(value="code",required=false) String code) throws MalformedURLException {
		
		 URL url = new URL(getAuthURL(code,request));
		 
         try {
             String result = readURL(url);
             String accessToken = null;
             Integer expires = null;
             String[] pairs = result.split("&");
             for (String pair : pairs) {
                 String[] kv = pair.split("=");
                 if (kv.length != 2) {
                     throw new RuntimeException("Unexpected auth response");
                 } else {
                     if (kv[0].equals("access_token")) {
                         accessToken = kv[1];
                     }
                     if (kv[0].equals("expires")) {
                         expires = Integer.valueOf(kv[1]);
                     }
                 }
             }
             if (accessToken != null && expires != null) {
              
                System.out.println("Token ----  " + accessToken+" Expires ----- " + expires);
                facebookService.getFriendList(accessToken);
             } else {
                 throw new RuntimeException("Access token and expires not found");
             }
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
		

	}

	
	
	
	public static String getAuthURL(String authCode,HttpServletRequest request) {
        return "https://graph.facebook.com/oauth/access_token?client_id=" + 
            APP_ID+"&redirect_uri="
            + request.getScheme()
			+ "://"
			+ request.getServerName()
			+ ":"
			+ request.getServerPort() +request.getContextPath()+ "/facebook/handleAuth.do"+"&client_secret="+APP_SECRET+"&code="+authCode;
    }
	
	
	
	
	private String readURL(URL url) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = url.openStream();
        int r;
        while ((r = is.read()) != -1) {
            baos.write(r);
        }
        return new String(baos.toByteArray());
    }
}
