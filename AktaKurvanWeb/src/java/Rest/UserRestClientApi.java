/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;
import ViewModel.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 * @author Jakob
 */
import java.util.List;

/**
 * Created by Jakob on 2017-12-21.
 */

public interface UserRestClientApi {

   
    @POST("getfriends")
    Call<List<VUser>> getfriends(@Body VUser user);

}
