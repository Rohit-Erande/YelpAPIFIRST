import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yelp.v2.Business;
import com.yelp.v2.YelpSearchResult;

public class RunMe {

	public void start() {
		// Define your keys, tokens and secrets.  These are available from the Yelp website.  
		String CONSUMER_KEY = "F1IWn950Cua0xPRrC18jUg";
		String CONSUMER_SECRET = "mgE-UjUxeEFAO6nvNXFIpneHUnc";
		String TOKEN = "K4dtPAHehuTwqkaHbMYBjRlh2vN-OBLg";
		String TOKEN_SECRET = "1esUhMkTc7Y0wc-6I6R-4kkAhHk";
		
		// Some example values to pass into the Yelp search service.  
		String lat = "30.361471";
		String lng = "-87.164326";
		//String lat = "37.3041";
		//String lng = "121.53";
		String category = "restaurants";
		
		// Execute a signed call to the Yelp service.  
		OAuthService service = new ServiceBuilder().provider(YelpV2API.class).apiKey(CONSUMER_KEY).apiSecret(CONSUMER_SECRET).build();
		Token accessToken = new Token(TOKEN, TOKEN_SECRET);
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
		request.addQuerystringParameter("ll", lat + "," + lng);
		//request.addQuerystringParameter(key, value)
		request.addQuerystringParameter("category", category);
		service.signRequest(accessToken, request);
		System.out.println("this is URL"+request);
		Response response = request.send();
		String rawData = response.getBody();
		 
		// Sample of how to turn that text into Java objects.  
		try {
			YelpSearchResult places = new Gson().fromJson(rawData, YelpSearchResult.class);
			
			System.out.println("Your search found " + places.getTotal() + " results.");
			System.out.println("Yelp returned " + places.getBusinesses().size() + " businesses in this request.");
			System.out.println();
			
			for(Business biz : places.getBusinesses()) {
				System.out.println(biz.getName());
				for(String address : biz.getLocation().getAddress()) {					
					System.out.println("  " + address);
				}
				System.out.print("  " + biz.getLocation().getCity());
				System.out.println(biz.getUrl());
				System.out.println();
			}
			
			
		} catch(Exception e) {
			System.out.println("Error, could not parse returned data!");
			System.out.println(rawData);			
		}
			
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new RunMe().start();
	}

}
