package com.chen.hello;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Request;
import com.ning.http.client.Response;

//For test
@Path("/")
public class CityRequest {
    private static final Logger logger = LoggerFactory
            .getLogger(CityRequest.class);
    private static AsyncHttpClient httpClient = new AsyncHttpClient();
    private static JSONObject groupList=null;
    private static JSONArray photoArray=null;
    static {
        //String url= "http://api.flickr.com/services/rest/?method=flickr.groups.pools.getPhotos&api_key=b76401e294a3aad3c2e94bb992cc33f5&group_id=1463451%40N25&per_page=2000&format=json&nojsoncallback=1&auth_token=72157635086397405-989ce11905998d62&api_sig=39bc21443817701c9f68b1fb07ad2d08";
        String url = "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=b76401e294a3aad3c2e94bb992cc33f5&group_id=16906560%40N00&has_geo=true&per_page=2000&format=json&nojsoncallback=1&auth_token=72157635086397405-989ce11905998d62&api_sig=d7f2f84d6b5a90977c70d7bde1425402";
        Request request = httpClient.prepareGet(url).build();
        try{
            Response response = httpClient.executeRequest(request).get();
            String responseBody = response.getResponseBody();
            groupList = new JSONObject(responseBody);
            groupList=groupList.getJSONObject("photos");
            photoArray=groupList.getJSONArray("photo");
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("{city}")
    @Produces({ "application/json" })
    public javax.ws.rs.core.Response getStr(@PathParam("city") String city)
            throws InterruptedException, ExecutionException, IOException,
            JSONException {
        String url = "http://dbpedia.org/data/" + city + ".json";
        Request request = httpClient.prepareGet(url).build();
        Response response = httpClient.executeRequest(request).get();
        String responseBody = response.getResponseBody();
        JSONObject jsonReader = new JSONObject(responseBody);
        jsonReader = jsonReader.getJSONObject("http://dbpedia.org/resource/"
                + city);
        ResponseMetaData responseEntity = new ResponseMetaData();
        responseEntity.setAreaTotal(getAeraTotal(jsonReader));
        responseEntity.setPopulationDesity(getPopulationDensity(jsonReader));
        responseEntity.setPopulationTotal(getPopulationTotal(jsonReader));
        return javax.ws.rs.core.Response.ok().entity(responseEntity).build();
    }

    private String getPopulationTotal(JSONObject json) throws JSONException {
        String keyForPopulationTotal = "http://dbpedia.org/ontology/populationTotal";
        JSONObject populationTotal = json.getJSONArray(keyForPopulationTotal)
                .getJSONObject(0);
        return populationTotal.getString("value");
    }

    private String getAeraTotal(JSONObject json) throws JSONException {
        String keyForAeraTotal = "http://dbpedia.org/ontology/PopulatedPlace/areaTotal";
        JSONObject populationTotal = json.getJSONArray(keyForAeraTotal)
                .getJSONObject(0);
        return populationTotal.getString("value");
    }

    private String getPopulationDensity(JSONObject json) throws JSONException {
        String keyForPopulationDensity = "http://dbpedia.org/ontology/populationDensity";
        JSONObject populationTotal = json.getJSONArray(keyForPopulationDensity)
                .getJSONObject(0);
        return populationTotal.getString("value");
    }

    @GET
    @Path("flicker/{city}")
    @Produces({ "application/json" })
    public javax.ws.rs.core.Response getPhotoInfo(@PathParam("city") String city)
            throws InterruptedException, ExecutionException, IOException,
            JSONException {
        /*String url = "http://api.flickr.com/services/rest/?method=flickr.groups.pools.getPhotos&api_key=b76401e294a3aad3c2e94bb992cc33f5&group_id=16906560%40N00&per_page=2000&format=json&nojsoncallback=1&auth_token=72157635086397405-989ce11905998d62&api_sig=31afe7fde5856be7947f6efcf0918340";
        Request request = httpClient.prepareGet(url).build();
        Response response = httpClient.executeRequest(request).get();
        String responseBody = response.getResponseBody();*/
       /* JSONObject jsonReader = new JSONObject(responseBody);
        jsonReader = jsonReader.getJSONObject("http://dbpedia.org/resource/"
                + city);
        ResponseMetaData responseEntity = new ResponseMetaData();
        responseEntity.setAreaTotal(getAeraTotal(jsonReader));
        responseEntity.setPopulationDesity(getPopulationDensity(jsonReader));
        responseEntity.setPopulationTotal(getPopulationTotal(jsonReader));*/  //getURL(getMetaData(getPhotoID()))+getLAT(getMetaData(getPhotoID()))
        JSONObject metaData= getMetaData(getPhotoID());
        JSONObject rej= new JSONObject();
        rej.put("url", getIMG(getHTML(getURL(metaData))));
        rej.put("LAT", getLAT(metaData));
        rej.put("LONG", getLONG(metaData));
        rej.put("Name",getName(metaData));
        rej.put("flickerUrl",getURL(metaData));
      //  String rej="{\"url\":\""+getIMG(getHTML(getURL(metaData)))+"\",\"LAT\":\""+getLAT(metaData)+"\",\"LONG\":\""+getLONG(metaData)+"\",\"NAME\":\""+getName(metaData)+"\"}";
        return javax.ws.rs.core.Response.ok().entity(rej.toString()).build();
    }

    private String getPhotoID() throws JSONException{
        int minimum=0;
        int maximum=200;
        int randomNum = minimum + (int)(Math.random()*maximum);
        return photoArray.getJSONObject(randomNum).getString("id");
    }

    private JSONObject getMetaData(String id) throws InterruptedException, ExecutionException, IOException, JSONException{
        String url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20flickr.photos.info%20where%20photo_id%3D%27"+id+"%27%20and%20api_key%20%3D%20%223425119e7575aa57367b3a9b74725dc5%22&format=json&callback=";
        Request request = httpClient.prepareGet(url).build();
        Response response = httpClient.executeRequest(request).get();
        String responseBody = response.getResponseBody();
        return new JSONObject(responseBody);
    }

    private String getURL(JSONObject metaData) throws JSONException{
        try {
            return metaData.getJSONObject("query").getJSONObject("results").getJSONObject("photo").getJSONObject("urls").getJSONObject("url").getString("content");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }



    private String getLAT(JSONObject metaData) throws JSONException{
        try {
            return metaData.getJSONObject("query").getJSONObject("results").getJSONObject("photo").getJSONObject("location").getString("latitude");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private String getLONG(JSONObject metaData) throws JSONException{
        try {
            return metaData.getJSONObject("query").getJSONObject("results").getJSONObject("photo").getJSONObject("location").getString("longitude");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private String getName(JSONObject metaData) throws JSONException{
        try {
            return metaData.getJSONObject("query").getJSONObject("results").getJSONObject("photo").getJSONObject("owner").getString("location");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    private String getHTML(String url) throws JSONException, InterruptedException, ExecutionException, IOException{
        Request request = httpClient.prepareGet(url).build();
        Response response = httpClient.executeRequest(request).get();
        String responseBody = response.getResponseBody();
        return responseBody;
    }



    private String getIMG(String html){
        String start="property=\"og:image\" content=\"";
        int from=html.indexOf(start)+start.length();
        int to=html.indexOf("\"", from);
        return html.substring(from, to);
    }



    @GET
    @Path("flicker/{pid}/{username}/{score}")
    @Produces({ "application/json" })
    public javax.ws.rs.core.Response postStr(@PathParam("pid") String pid, @PathParam("username") String username, @PathParam("score") String score)
            throws InterruptedException, ExecutionException, IOException,
            JSONException {
        String url = "http://embarkdark.corp.gq1.yahoo.com/post_score.php";
        Request request = httpClient.preparePost(url).addParameter("PictureId", pid).addParameter("Username", username).addParameter("Score", score).build();
        Response response = httpClient.executeRequest(request).get();
        String responseBody = response.getResponseBody();
        return javax.ws.rs.core.Response.ok().entity(responseBody).build();
    }

    @GET
    @Path("flicker/pid/{pid}")
    @Produces({ "application/json" })
    public javax.ws.rs.core.Response postStr(@PathParam("pid") String pid)
            throws InterruptedException, ExecutionException, IOException,
            JSONException {
        String url = "http://embarkdark.corp.gq1.yahoo.com/lb_by_pic.php";
        Request request = httpClient.preparePost(url).addParameter("PictureId", pid).build();
        Response response = httpClient.executeRequest(request).get();
        String responseBody = response.getResponseBody();
        return javax.ws.rs.core.Response.ok().entity(responseBody).build();
    }






}
