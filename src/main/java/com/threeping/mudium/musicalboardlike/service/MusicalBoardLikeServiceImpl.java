package com.threeping.mudium.musicalboardlike.service;

import com.threeping.mudium.musicalboardlike.aggregate.MusicalBoardLike;
import com.threeping.mudium.musicalboardlike.aggregate.MusicalBoardLikePK;
import com.threeping.mudium.musicalboardlike.repository.MusicalBoardLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MusicalBoardLikeServiceImpl implements MusicalBoardLikeService {

    private final MusicalBoardLikeRepository musicalBoardLikeRepository;

    @Autowired
    public MusicalBoardLikeServiceImpl(MusicalBoardLikeRepository musicalBoardLikeRepository) {
        this.musicalBoardLikeRepository = musicalBoardLikeRepository;
    }

    @Override
    public Boolean findExistingLike(Long postId, Long userId) {
        MusicalBoardLike like = musicalBoardLikeRepository.findById(new MusicalBoardLikePK(postId, userId))
                .orElse(null);
        return like != null;
    }

    @Override
    public void createBoardLike(Long postId, Long userId) {
        MusicalBoardLike like = new MusicalBoardLike(postId, userId);
        musicalBoardLikeRepository.save(like);
    }

    @Override
    public void deleteBoardLike(Long postId, Long userId) {
        musicalBoardLikeRepository.deleteById(new MusicalBoardLikePK(postId, userId));
    }
}
