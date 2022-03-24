import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            ObjectMapper mapper = new ObjectMapper();
            List<Post> posts = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<Post>>() {
            });
            posts = posts.stream()
                    .filter(value -> value.getUpvotes() != null && value.getUpvotes() > 0)
                    .collect(Collectors.toList());

            posts.forEach(System.out::println);
        }
    }
}
