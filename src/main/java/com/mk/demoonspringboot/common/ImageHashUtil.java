package com.mk.demoonspringboot.common;

import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.AverageHash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;
import com.github.kilianB.matcher.exotic.SingleImageMatcher;

import java.io.File;
import java.io.IOException;

public class ImageHashUtil {

    public static void main(String[] args) throws IOException {
        //File img0 = new File("C:\\Users\\wudaye\\Downloads\\2.png");
        File img1 = new File("C:\\Users\\wudaye\\Pictures\\oa6vuhtsf2hp6t54xrilqznj.jpg");
        File img0 = new File("C:\\Users\\wudaye\\Pictures\\3p8a9yiakfi4814ivxfxfw1s4.jpg");

        HashingAlgorithm hasher = new PerceptiveHash(32);

        Hash hash0 = hasher.hash(img0);
        Hash hash1 = hasher.hash(img1);

        double similarityScore = hash0.normalizedHammingDistance(hash1);
        System.out.println("similarityScore: " + similarityScore);

        if(similarityScore < .2) {
            //Considered a duplicate in this particular case
        }

//Chaining multiple matcher for single image comparison

        SingleImageMatcher matcher = new SingleImageMatcher();
        matcher.addHashingAlgorithm(new AverageHash(64),.3);
        matcher.addHashingAlgorithm(new PerceptiveHash(32),.2);

        boolean match = matcher.checkSimilarity(img0, img1);
        if(match) {
            //Considered a duplicate in this particular case
        }
        System.out.println("match " + match);
    }
}
