package com.threeping.mudium.musical.service;

import com.threeping.mudium.musical.aggregate.Musical;
import com.threeping.mudium.musical.repository.MusicalRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class YoutubeService {

    private final MusicalRepository musicalRepository;

    public YoutubeService(MusicalRepository musicalRepository) {
        this.musicalRepository = musicalRepository;
    }

    @Transactional
    public void updateMusicalReviewVideos() throws Exception {
        List<Musical> musicals = musicalRepository.findAll();

        for (Musical musical : musicals) {
            String query = musical.getTitle() + " 뮤지컬 리뷰";  // 검색 쿼리 생성
            List<String> reviewVideos = YoutubeAPI.fetchYouTubeReviews(query, 10);  // YouTube API 호출

            if (reviewVideos == null) {
                System.out.println("YouTube API 요청 실패: " + query);
                continue;
            }

            if (!reviewVideos.isEmpty()) {
                String firstReviewVideo = reviewVideos.get(0);
                musical.setReviewVideo(firstReviewVideo);  // 리뷰 영상 업데이트
                musicalRepository.save(musical);  // DB 업데이트
            }
        }
    }


//    private final MusicalRepository musicalRepository;
//
//    public YoutubeService(MusicalRepository musicalRepository) {
//        this.musicalRepository = musicalRepository;
//    }
//
//    @Transactional
//    public void updateMusicalReviewVideos() throws Exception {
//        List<Musical> musicals = musicalRepository.findAll();
//
//        for (Musical musical : musicals) {
//            // 뮤지컬 제목과 "뮤지컬 공연 리뷰"를 명확하게 검색어로 추가
//            String query = musical.getTitle() + " 뮤지컬 공연 리뷰";
//            List<String> reviewVideos = YoutubeAPI.fetchYouTubeReviews(query, 10);  // YouTube API 호출
//
//            if (reviewVideos == null) {
//                System.out.println("YouTube API 요청 실패: " + query);
//                continue;
//            }
//
//            if (!reviewVideos.isEmpty()) {
//                String firstReviewVideo = reviewVideos.get(0);
//                musical.setReviewVideo(firstReviewVideo);  // 리뷰 영상 업데이트
//                musicalRepository.save(musical);  // DB 업데이트
//            }
//        }
//    }

//    @Scheduled(cron = "0 0 1 * * ?")
//    @Scheduled(initialDelay = 5000, fixedDelay = 300000000)
    public void scheduleMusicalReviewUpdate() {
        try {
            updateMusicalReviewVideos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
