package com.peashoot.blog.crypto.advice;

import com.peashoot.blog.crypto.definition.Crypto;
import com.peashoot.blog.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class DecryptHttpInputMessage implements HttpInputMessage {
    private HttpInputMessage inputMessage;
    private String charset;
    private Crypto crypto;

    public DecryptHttpInputMessage(HttpInputMessage inputMessage, String charset, Crypto crypto) {
        this.inputMessage = inputMessage;
        this.charset = charset;
        this.crypto = crypto;
    }

    @Override
    public InputStream getBody() throws IOException {
        String content = IOUtils.read(inputMessage.getBody(), charset);
        String decryptBody = "";
        try {
            decryptBody = crypto.decrypt(content, charset);
        } catch (Exception ex) {
            log.error("An exception appeared when try to decrypt:", ex);
        }
        return new ByteArrayInputStream(decryptBody.getBytes(charset));
    }

    @Override
    public HttpHeaders getHeaders() {
        return inputMessage.getHeaders();
    }
}
