package polytech.RBNN.retrofit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitServiceFactory {

    private final Retrofit retrofit;

    RetrofitServiceFactory() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        this.retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8200/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build();
    }

    @Bean
    public VaultService vaultService() {
        return retrofit.create(VaultService.class);
    }

}
