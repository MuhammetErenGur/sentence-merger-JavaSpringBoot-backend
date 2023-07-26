package com.yazlab.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MergedWordsService {
    @Autowired
    private MergedWordsRepository mergedWordsRepository;

    public MergedWords saveMergedWords(MergedWords mw){
        mergedWordsRepository.insert(mw);
        return mw;
    }

    public MergedWords createMergedWords(List<String> words, String mergedWord, double time){
            MergedWords mergedWords=new MergedWords(words,mergedWord,time);
            return mergedWords;
    }


}
