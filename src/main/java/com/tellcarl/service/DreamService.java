package com.tellcarl.service;

import com.amazonaws.services.comprehend.model.Entity;
import com.amazonaws.services.comprehend.model.KeyPhrase;
import com.google.cloud.language.v1.*;
import com.google.cloud.language.v1.PartOfSpeech.Tag;
import com.tellcarl.domain.*;
import com.tellcarl.domain.DreamReport.Work;
import com.tellcarl.repository.SymbolRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DreamService
{
    private final SymbolRepository symbolRepository;
    private final LanguageService  languageService;

    public DreamService( SymbolRepository symbolRepository, LanguageService languageService )
    {
        this.symbolRepository = symbolRepository;
        this.languageService = languageService;
    }

    public DreamReport getDreamAnalysis( String dreamText )
    {
        final List<com.amazonaws.services.comprehend.model.Entity> awsEntities    = languageService.getEntities(dreamText);
        final List<com.google.cloud.language.v1.Entity>            googleEntities = languageService.analyzeEntities(dreamText).getEntitiesList();

        for ( com.google.cloud.language.v1.Entity entity : googleEntities )
        {
            entity.getName();
        }

        final String dreamTextWithMarkup = getAnnotatedEntitiesText(dreamText, awsEntities);

        final Work work = extractWork(dreamText);

        return new DreamReport(dreamText, dreamTextWithMarkup, awsEntities, work, false);
    }

    private String getTextWithMarkup( String dreamText )
    {
        StringBuffer sb = new StringBuffer(dreamText);

        final List<KeyPhrase> keyPhrases = languageService.getKeyPhrases(dreamText);

        int offsetAcc = 0;
        for ( KeyPhrase keyPhrase : keyPhrases )
        {
            sb.insert(offsetAcc + keyPhrase.getBeginOffset(), "*_");
            offsetAcc = offsetAcc + 2;
            sb.insert(offsetAcc + keyPhrase.getEndOffset(), "_*");
            offsetAcc = offsetAcc + 2;
        }

        return sb.toString();
    }

    private String getAnnotatedEntitiesText( String dreamText, List<Entity> entities )
    {
        StringBuffer sb = new StringBuffer(dreamText);

        int offsetAcc = 0;
        for ( Entity entity : entities )
        {
            sb.insert(offsetAcc + entity.getBeginOffset(), "*_");
            offsetAcc = offsetAcc + 2;
            sb.insert(offsetAcc + entity.getEndOffset(), "_*");
            offsetAcc = offsetAcc + 2;
        }

        return sb.toString();
    }

    private Set<DreamSymbol> extractDreamSymbols( String dreamText )
    {
        List<DreamSymbol> symbols = new ArrayList<>();

        final AnalyzeSyntaxResponse analyzeSyntaxResponse = languageService.analyzeSyntax(dreamText);

        final List<Token> tokens = analyzeSyntaxResponse.getTokensList();

        for ( Token token : tokens )
        {
            final PartOfSpeech partOfSpeech = token.getPartOfSpeech();

            final Tag tag = partOfSpeech.getTag();

            if ( tag == Tag.NOUN || tag == Tag.VERB )
            {
                final Optional<Symbol> optionalSymbol = symbolRepository.findByLemma(token.getLemma());

                if ( optionalSymbol.isPresent() )
                {

//                    symbols.add(new DreamSymbol(token.getText().getContent(), optionalSymbol.get(), null));
                }
                else
                {
                    if ( tag == Tag.NOUN )
                    {
//                        symbols.add(new DreamSymbol(token.getText().getContent(), new Symbol(token.getLemma(), SymbolType.of(tag), false, null, null), null));
                    }
                }

                final DependencyEdge dependencyEdge = token.getDependencyEdge();
            }
        }

        return Set.copyOf(symbols);
    }

    private Work extractWork( String dreamText )
    {
        final AnalyzeSentimentResponse analyzeSentimentResponse = languageService.analyzeSentiment(dreamText);

        final Set<DreamSentence> dreamsSentences =
                analyzeSentimentResponse.getSentencesList().stream()
                                        .map(s -> new DreamSentence(s.getText().getContent(), s.getSentiment().getMagnitude()))
                                        .collect(Collectors.toSet());

        Comparator<DreamSentence> comparator = Comparator.comparing(DreamSentence::getRelevance);
        final LinkedHashSet<DreamSentence> sentencesSortedByRelevance =
                dreamsSentences.stream().sorted(comparator.reversed()).collect(Collectors.toCollection(LinkedHashSet::new));

        final Iterator<DreamSentence> sentencesIt = sentencesSortedByRelevance.iterator();

        int                 sentencesCount = 0;
        List<DreamSentence> mainSentences  = new ArrayList<>();
        while ( sentencesIt.hasNext() && sentencesCount < 2 )
        {
            final DreamSentence sentence = sentencesIt.next();
            mainSentences.add(sentence);
            sentencesIt.remove();
            sentencesCount++;
        }

        return new Work(mainSentences);
    }
}

//    // Secondary Sentences (sentences containing main symbols -lemmas-)
//    SortedSet<DreamSentence> secondarySentences = new TreeSet<>();
//    int sentencesCount = 0;
//        while ( sentencesIt.hasNext() )
//                {
//final DreamSentence sentence = sentencesIt.next();
//
//        Set<String> sentenceWords = Arrays.stream(sentence.getText().split(" ")).collect(Collectors.toSet());
//
//final Set<String> intersection = sentenceWords.stream().filter(mainSymbolsLemmas::contains).collect(Collectors.toSet());
//
//        if ( intersection.size() > 0 )
//        {
//        secondarySentences.add(sentence);
//        sentencesIt.remove();
//        }
//        }
