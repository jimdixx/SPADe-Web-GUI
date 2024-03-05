package cz.zcu.fav.kiv.antipatterndetectionapp.errorHandler;


import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class CustomResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = response.getStatusCode();

        if (statusCode == HttpStatus.CONFLICT) {
            throw new HttpClientErrorException(statusCode, "Conflict error.");
        } else if (statusCode == HttpStatus.BAD_REQUEST) {
            throw new HttpClientErrorException(statusCode, "Bad Request.");
        } else {
            throw new HttpClientErrorException(statusCode, "Unexpected error occurred.");
        }
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }
}