package com.threeping.mudium.musical.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class YoutubeAPI {

    private static final String API_KEY = "AIzaSyDLZRni6kwuP9IKIbO62SaCwLQ9ob2uIAU"; // 실제 API 키로 교체하세요
    private static final String YOUTUBE_API_URL = "https://www.googleapis.com/youtube/v3/search";

    public static List<String> fetchYouTubeReviews(String query, int maxResults) throws Exception {
        // 제목을 URL 인코딩 (특수 문자 처리)
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());

        // YouTube API 요청 URL 생성
        String urlStr = YOUTUBE_API_URL + "?part=snippet&q=" + encodedQuery +
                "&type=video&videoCategoryId=1&maxResults=" + maxResults +
                "&relevanceLanguage=ko&key=" + API_KEY;



        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return parseYouTubeResponse(response.toString());
        } else {
            System.out.println("GET 요청 실패: " + responseCode);
            return null;
        }
    }


//     JSON 응답을 파싱하는 메서드
    private static List<String> parseYouTubeResponse(String response) throws Exception {
        List<String> reviewUrls = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response);
        JsonNode items = jsonResponse.get("items");

        if (items.isArray()) {
            for (JsonNode item : items) {
                String videoId = item.get("id").get("videoId").asText();
                String videoUrl = "https://www.youtube.com/watch?v=" + videoId;
                reviewUrls.add(videoUrl);
            }
        }
        return reviewUrls;
    }
//


}
