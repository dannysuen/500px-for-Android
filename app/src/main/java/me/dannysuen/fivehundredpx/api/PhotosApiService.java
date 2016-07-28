package me.dannysuen.fivehundredpx.api;

import me.dannysuen.fivehundredpx.model.PhotosResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by danny on 16-7-26.
 */
public interface PhotosApiService {

    // Example link: https://api.500px.com/v1/photos?consumer_key=ptfVekoqagi9wzs7ukGxXDcYOq65D3YPN6AIKwv5
    // &only=nature
    @GET("photos")
    Call<PhotosResponse> fetchPhotos(@Query("consumer_key") String consumerKey,
                                     @Query("only") String only,
                                     @Query("page") int page,
                                     @Query("image_size") int imageSizeId);


}
