package com.example.swan.swanserver.service.impl;

import com.example.swan.swanserver.service.KaptchaService;
import com.example.swan.swanserver.util.CommonUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author entropyfeng
 * 默认验证码实现类
 */
@Primary
@Service
public class KaptchaServiceImpl implements KaptchaService {


    private static Logger logger = LoggerFactory.getLogger(KaptchaServiceImpl.class);

    @Autowired
    public KaptchaServiceImpl(DefaultKaptcha defaultKaptcha) {
        this.producer = defaultKaptcha;

    }



    private DefaultKaptcha producer;

    @Override
    public Map createKaptcha() {

        String kaptcha = producer.createText();

        Map<String, String> res = new HashMap<>();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage image = producer.createImage(kaptcha);

        try {
            ImageIO.write(image, "jpg", outputStream);
            BASE64Encoder encoder = new BASE64Encoder();
            res.put("img", encoder.encode(outputStream.toByteArray()));
            String kaptchaToken = CommonUtil.getKaptchaToken();
            res.put("kaptcha_token", kaptchaToken);
            res.put("kaptcha",kaptcha);
            logger.info("create captcha token {} value {}",kaptchaToken,kaptcha);
        } catch (IOException e) {
            logger.warn( "create Kaptcha fail", e);
            res = null;
        }

        return res;
    }
}
