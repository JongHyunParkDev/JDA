package com.yj.yjbot.data;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Component
public class LiarGame {
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static List<LiarObj> liarGameDataList;

    private static final Logger logger = LoggerFactory.getLogger(LiarGame.class);
    public LiarGame(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;

        readLiarGameDataJson();
    }

    public void readLiarGameDataJson() {
        try {
            Resource resource = resourceLoader.getResource("classpath:LiarGameData.json");
            Data data = objectMapper.readValue(resource.getInputStream(), Data.class);
            liarGameDataList = data.list;
        }
        catch (IOException e) {
            logger.error("Json Load Error" ,e);
        }
    }

    public static LiarObj getLiarData() {
        int length = liarGameDataList.size();
        return liarGameDataList.get(new Random().nextInt(length));
    }

    public static class Data {
        private List<LiarObj> list;

        public List<LiarObj> getList() {
            return list;
        }

        public void setList(List<LiarObj> list) {
            this.list = list;
        }
    }

    public static class LiarObj {
        private String category;
        private String answer;

        public String getCategory() {
            return category;
        }

        public String getAnswer() {
            return answer;
        }
        public void setCategory(String category) {
            this.category = category;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}
