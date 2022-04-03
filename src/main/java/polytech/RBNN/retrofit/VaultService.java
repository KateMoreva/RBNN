package polytech.RBNN.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface VaultService {

    @POST("/v1/transit/encrypt/orders")
    Call<String> encrypt(@Header("X-Vault-Token") String token,
                         @Body String plaintext);

    @POST("/v1/transit/decrypt/orders")
    Call<String> decrypt(@Header("X-Vault-Token") String token,
                         @Body String ciphertext);

}
