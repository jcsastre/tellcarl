package com.tellcarl.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.comprehend.model.*;
import com.amazonaws.services.comprehend.model.Entity;
import com.google.cloud.language.v1.*;
import com.google.cloud.language.v1.Document.Type;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class LanguageService
{
    LanguageServiceClient googleLanguageService;
    AmazonComprehend      comprehendClient;

    public List<KeyPhrase> getKeyPhrases( String dreamText )
    {
        DetectKeyPhrasesRequest detectKeyPhrasesRequest = new DetectKeyPhrasesRequest().withText(dreamText)
                                                                                       .withLanguageCode(LanguageCode.Es);
        DetectKeyPhrasesResult detectKeyPhrasesResult = comprehendClient.detectKeyPhrases(detectKeyPhrasesRequest);

        return detectKeyPhrasesResult.getKeyPhrases();
    }

    public List<Entity> getEntities( String dreamText )
    {

        final DetectEntitiesRequest detectEntitiesRequest = new DetectEntitiesRequest().withText(dreamText)
                                                                                       .withLanguageCode(LanguageCode.Es);

        final DetectEntitiesResult detectEntitiesResult = comprehendClient.detectEntities(detectEntitiesRequest);

        final List<Entity> entities = detectEntitiesResult.getEntities();

        return entities;
    }

    public AnalyzeEntitiesResponse analyzeEntities( String dreamText )
    {
        Document doc = Document.newBuilder().setContent(dreamText).setType(Type.PLAIN_TEXT).build();

        return googleLanguageService.analyzeEntities(doc);
    }

    public AnalyzeEntitySentimentResponse analyzeEntitySentiment( String dreamText )
    {
        Document doc = Document.newBuilder().setContent(dreamText).setType(Type.PLAIN_TEXT).build();

        return googleLanguageService.analyzeEntitySentiment(doc);
    }

    public AnalyzeSyntaxResponse analyzeSyntax( String dreamText )
    {
        Document doc = Document.newBuilder().setContent(dreamText).setType(Type.PLAIN_TEXT).build();

        return googleLanguageService.analyzeSyntax(doc);
    }

    public AnalyzeSentimentResponse analyzeSentiment( String dreamText )
    {
        Document doc = Document.newBuilder().setContent(dreamText).setType(Type.PLAIN_TEXT).build();

        return googleLanguageService.analyzeSentiment(doc);
    }

    @PostConstruct
    public void initialize() throws IOException
    {
        // Google Cloud Natural Language
        googleLanguageService = LanguageServiceClient.create();

        // AWS Comprehend
        AWSCredentialsProvider awsCreds = DefaultAWSCredentialsProviderChain.getInstance();

        comprehendClient = AmazonComprehendClientBuilder.standard()
                                                        .withCredentials(awsCreds)
                                                        .withRegion(Regions.US_EAST_1)
                                                        .build();

    }
}
