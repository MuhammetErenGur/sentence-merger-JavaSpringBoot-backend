package com.yazlab.backend;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


@CrossOrigin(origins = "*",allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class MergedWordsController {
    private MergedWords mw;
    @Autowired
    private  MergedWordsService mergedWordsService;



    @PostMapping("/save_words")
    public  ResponseEntity<MergedWords> createMergedWords(){

        return new ResponseEntity<MergedWords>(mergedWordsService.saveMergedWords(mw),HttpStatus.CREATED);
    }
    @PostMapping("/merge_words")
    public ResponseEntity<MergedWords> mergeWords(@RequestBody Map<String,String[]> payload){
        ArrayList<String> list=new ArrayList<String>();
        Collections.addAll(list, payload.get("words"));
        long start = System.nanoTime();
        String mergedWord=MergedWords.mainFunc(list);
        long end = System.nanoTime();
        long elapsedTime = end - start;
        double elapsedTimeInSeconds=(double) elapsedTime / 1_000_000_000;
        mw=mergedWordsService.createMergedWords(list,mergedWord,elapsedTimeInSeconds);
        return new ResponseEntity<MergedWords>(mw,HttpStatus.OK);

    }
}
