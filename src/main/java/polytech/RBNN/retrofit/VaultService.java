package polytech.RBNN.retrofit;

import polytech.RBNN.dto.CiphertextDto;
import polytech.RBNN.dto.PlaintextDto;
import polytech.RBNN.dto.VaultResponseDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface VaultService {

    @POST("/v1/transit/encrypt/orders")
    Call<VaultResponseDto<CiphertextDto>> encrypt(@Header("X-Vault-Token") String token, @Body PlaintextDto plaintext);

    @POST("/v1/transit/decrypt/orders")
    Call<VaultResponseDto<PlaintextDto>> decrypt(@Header("X-Vault-Token") String token, @Body CiphertextDto ciphertext);

}
