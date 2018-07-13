package Rest;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ViewModel.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jakob on 2017-12-22.
 */

public class UserRestClient {

    private final String TAG = "UserRestClient";
    private final String BASEURL = "http://localhost:7932/api/";


    public List<VUser> sendGetFriends(VUser user) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        UserRestClientApi client = retrofit.create(UserRestClientApi.class);
        Call<List<VUser>> call = client.getfriends(user);
        
        List<VUser> users = new ArrayList<>();
        
        try {
            Response<List<VUser>> response = call.execute();
            List<VUser> responseList = response.body();
            return responseList;
        } catch (IOException ex) {
            Logger.getLogger(UserRestClient.class.getName()).log(Level.SEVERE, null, ex);
            return users;
        }
        
    }

}
