package com.giyeon.chatbotforforeignstudentsincbnu.service;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Vector {
        // 텍스트 벡터화 (간단한 단어 빈도로 벡터 생성)
        private static RealVector textToVector(String text, List<String> vocabulary) {
            Map<String, Integer> wordCounts = new HashMap<>();
            String[] words = text.split("\\s+");

            // 단어 개수 카운트
            for (String word : words) {
                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            }

            // 벡터 생성
            double[] vector = new double[vocabulary.size()];
            int i = 0;
            for (String vocab : vocabulary) {
                vector[i++] = wordCounts.getOrDefault(vocab, 0);
            }

            return new ArrayRealVector(vector);
        }

        // 코사인 유사도 계산
        private static double cosineSimilarity(RealVector vec1, RealVector vec2) {
            double dotProduct = vec1.dotProduct(vec2);
            double normA = vec1.getNorm();
            double normB = vec2.getNorm();
            return dotProduct / (normA * normB);
        }

        public static void main(String[] args) {
            List<String> vocabulary = Arrays.asList("hello", "world", "java", "code", "example", "vector", "similarity");

            // 기존 질문
            String question1 = "hello world example";
            String question2 = "java code vector";
            String question3 = "world java example";

            // 새로운 질문
            String newQuestion = "java vector code example";

            // 벡터화
            RealVector vecQuestion1 = textToVector(question1, vocabulary);
            RealVector vecQuestion2 = textToVector(question2, vocabulary);
            RealVector vecQuestion3 = textToVector(question3, vocabulary);
            RealVector vecNewQuestion = textToVector(newQuestion, vocabulary);

            // 코사인 유사도 계산
            double similarity1 = cosineSimilarity(vecNewQuestion, vecQuestion1);
            double similarity2 = cosineSimilarity(vecNewQuestion, vecQuestion2);
            double similarity3 = cosineSimilarity(vecNewQuestion, vecQuestion3);

            // 유사도가 0.7 이상인 질문 필터링
            List<String> similarQuestions = Arrays.asList(question1, question2, question3).stream()
                    .filter(q -> {
                        RealVector vecQ = textToVector(q, vocabulary);
                        return cosineSimilarity(vecNewQuestion, vecQ) >= 0.7;
                    })
                    .collect(Collectors.toList());

            // 결과 출력
            System.out.println("Similar Questions: " + similarQuestions);
        }
    }

