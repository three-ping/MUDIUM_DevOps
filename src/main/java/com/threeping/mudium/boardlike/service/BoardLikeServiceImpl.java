package com.threeping.mudium.boardlike.service;

import com.threeping.mudium.boardlike.aggregate.entity.BoardLike;
import com.threeping.mudium.boardlike.aggregate.entity.BoardLikePK;
import com.threeping.mudium.boardlike.repository.BoardLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardLikeServiceImpl implements BoardLikeService{

    private final BoardLikeRepository boardLikeRepository;

    @Autowired
     BoardLikeServiceImpl(BoardLikeRepository boardLikeRepository){
        this.boardLikeRepository = boardLikeRepository;
    }

    @Override
    @Transactional
    public Boolean findBoardLike(Long boardId, Long userId) {
        BoardLike boardLike = boardLikeRepository.findById(new BoardLikePK(boardId,userId)).orElse(null);
        return boardLike != null;
    }

    @Override
    @Transactional
    public void createBoardLike(Long boardId, Long userId) {
        BoardLike boardLike = new BoardLike(boardId,userId);
        boardLikeRepository.save(boardLike);
    }

    @Override
    @Transactional
    public void deleteBoardLike(Long boardId, Long userId) {
        BoardLikePK boardLikePK = new BoardLikePK(boardId,userId);
        boardLikeRepository.deleteById(boardLikePK);
    }


}
