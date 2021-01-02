package com.tellcarl.service;

import com.amazonaws.services.comprehend.model.Entity;
import com.tellcarl.domain.*;
import com.tellcarl.domain.DreamReport.Work;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import rst.pdfbox.layout.elements.*;
import rst.pdfbox.layout.elements.render.VerticalLayoutHint;
import rst.pdfbox.layout.text.Alignment;
import rst.pdfbox.layout.text.BaseFont;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PdfService
{
    public void createPdf( DreamReport report ) throws Exception
    {
        Document doc = new Document(70, 40, 50, 50);

        addDreamText(report, doc);

        doc.add(VERTICAL_SPACER_20);

//        addSymbols(report, doc);
//
//        doc.add(VERTICAL_SPACER_20);

        addWork(report, doc);

        final OutputStream outputStream = new FileOutputStream("hellodoc.pdf");
        doc.save(outputStream);
    }

    private void addWork( DreamReport report, Document doc ) throws Exception
    {
        Paragraph p = new Paragraph();
        p.setAlignment(Alignment.Center);
        p.addText("TRABAJO", 16, PDType1Font.HELVETICA_BOLD);
        doc.add(p);
        doc.add(VERTICAL_SPACER_20);

        final String instructions = "A continuación aparecen una serie de ejercicios que te ayudarán a trabajar sobre tus sueños. Están " +
                "ordenados de mayor a menor importancia. Empieza por el primero y haz todos los que puedas.";
        p = new Paragraph();
        p.addText(instructions, 10, PDType1Font.HELVETICA);
        doc.add(p);
        doc.add(VERTICAL_SPACER_20);

        final List<DreamSentence> mainSentences = report.getWork().getMainSentences();
        if ( mainSentences != null && mainSentences.size() > 0 )
        {
            addWorkMainSentences(mainSentences, doc);
        }
    }

    private void addWorkMainSentences( List<DreamSentence> sentences, Document doc ) throws IOException, URISyntaxException
    {
        for ( DreamSentence dreamSentence : sentences )
        {
            Paragraph p = new Paragraph();
            p.addText("REFLEXIONA Y HAZ UN DIBUJO SOBRE ESTA PARTE DEL SUEÑO: ", 10, PDType1Font.HELVETICA_BOLD);
            doc.add(p);

            doc.add(VERTICAL_SPACER_05);

            p = new Paragraph();
            p.setAlignment(Alignment.Center);
            p.addText("\"" + dreamSentence.getText() + "\"", 12, PDType1Font.HELVETICA_OBLIQUE);
            doc.add(p);

            doc.add(VERTICAL_SPACER_10);

            addDrawSentenceRectangle(doc);

            doc.add(VERTICAL_SPACER_20);
        }
    }

    private void addDreamText( DreamReport report, Document doc ) throws IOException
    {
        Paragraph pHeader = new Paragraph();
        pHeader.setAlignment(Alignment.Center);
        pHeader.addText("SUEÑO", 16, PDType1Font.HELVETICA_BOLD);
        doc.add(pHeader);

        doc.add(VERTICAL_SPACER_10);

        Paragraph pDreamText = new Paragraph();
        pDreamText.addMarkup(report.getDreamTextWithMarkup(), 12, BaseFont.Helvetica);
        doc.add(pDreamText);
    }

//    private void addSymbols( DreamReport report, Document doc ) throws IOException, URISyntaxException
//    {
////        final int mainSymbolsCount  = report.getAwsEntities() != null ? report.getAwsEntities().size() : 0;
//
//        final List<Entity> awsEntities = report.getAwsEntities();
//        final List<com.google.cloud.language.v1.Entity> googleEntities = report.getGoogleEntities();
//
//        if ( (awsEntities != null && awsEntities.size() > 0) || (googleEntities != null && googleEntities.size() > 0) )
//        {
//            Paragraph pHeader = new Paragraph();
//            pHeader.setAlignment(Alignment.Center);
//            pHeader.addText("SÍMBOLOS", 16, PDType1Font.HELVETICA_BOLD);
//            doc.add(pHeader);
//
//            doc.add(VERTICAL_SPACER_10);
//
//            if ( mainSymbolsCount > 0 )
//            {
//                addMainSymbols(report, doc);
//
//                doc.add(VERTICAL_SPACER_20);
//            }
//
//            if ( otherSymbolsCount > 0 )
//            {
//                addOtherSymbols(report, doc);
//
//                doc.add(VERTICAL_SPACER_20);
//            }
//        }
//    }

//    private void addMainSymbols( DreamReport report, Document doc ) throws IOException, URISyntaxException
//    {
//        for ( DreamSymbol s : report.getAwsEntities() )
//        {
//            final String imgFilename = s.getSymbol().getImgFilename();
//            if ( imgFilename != null )
//            {
//                final URL          res          = getClass().getClassLoader().getResource("./img/pdf/symbols/" + imgFilename);
//                File               file         = Paths.get(res.toURI()).toFile();
//                String             absolutePath = file.getAbsolutePath();
//                final ImageElement imageElement = new ImageElement(absolutePath);
//                imageElement.setHeight(30);
//                imageElement.setWidth(30);
//                doc.add(imageElement, VerticalLayoutHint.CENTER);
//                doc.add(VERTICAL_SPACER_05);
//            }
//
//            final String lemma       = s.getSymbol().getLemma().toUpperCase(Locale.ROOT);
//            final String explanation = s.getSymbol().getExplanation();
//            Paragraph    pDreamText  = new Paragraph();
//            pDreamText.addText(lemma + ": " + explanation, 12, PDType1Font.HELVETICA);
//            doc.add(pDreamText);
//            doc.add(VERTICAL_SPACER_10);
//        }
//    }

//    private void addOtherSymbols( DreamReport report, Document doc ) throws IOException, URISyntaxException
//    {
//        final Set<String> lemmas = report.getOtherSymbols().stream().map(s -> s.getSymbol().getLemma().toUpperCase()).collect(Collectors.toSet());
//        final String      txt    = String.join(", ", lemmas);
//
//        Paragraph p = new Paragraph();
//        p.addText(txt, 12, PDType1Font.HELVETICA);
//        doc.add(p);
//    }

    private void addDrawSentenceRectangle( Document doc ) throws IOException, URISyntaxException
    {
        final URL          res          = getClass().getClassLoader().getResource("./img/pdf/DrawSentenceRectangleMaxCompression.png");
        File               file         = Paths.get(res.toURI()).toFile();
        String             absolutePath = file.getAbsolutePath();
        final ImageElement imageElement = new ImageElement(absolutePath);
        imageElement.setWidth(ImageElement.SCALE_TO_RESPECT_WIDTH);
        imageElement.setHeight(ImageElement.SCALE_TO_RESPECT_WIDTH);
        doc.add(imageElement, VerticalLayoutHint.CENTER);
        doc.add(VERTICAL_SPACER_05);
    }

    private static final VerticalSpacer VERTICAL_SPACER_05 = new VerticalSpacer(05);
    private static final VerticalSpacer VERTICAL_SPACER_10 = new VerticalSpacer(10);
    private static final VerticalSpacer VERTICAL_SPACER_20 = new VerticalSpacer(20);

//    public static void main( String[] args ) throws Exception
//    {
//        final String dreamText = "Jugando a *_tenis_* con *_Ramón_* y *_otro chico_*. Uso *_la mano_*. Como *_los puntos_* normalmente. Hay *_una niña_* que juega pero que va *_un poco_* de *_víctima_*.\n" +
//                "\n" +
//                "Estoy en *_una mesa_* con *_amigos_* del *_Pepe_*. Luego viendo *_el fútbol_*, *_la final_* de *_Champions_*, *_Barça_* contra *_el Bayern de Munich_*. *_El Barça_* remonta inesperadamente con *_un gol_* de *_mano_*. *_Messi_* está llorando desconsoladamente, parece que se ha lesionado y no podrá seguir jugando. Termina *_la primera parte_* del *_partido_*. Lo estoy viendo en *_la cocina_*, pero me voy al *_comedor_*. Sería *_la segunda vez_* que *_el Bayern_* pierde *_la final_* porque le remontan *_el partido_*.\n" +
//                "\n" +
//                "*_Pequeña montaña rusa_*, que tiene *_una parte_* donde vas por *_el aire_*. Me tiro repetidamente por *_la montaña rusa_* porque me gusta. Hay *_una chica morena_* muy guapa.\n" +
//                "\n" +
//                "Estoy esperando para recoger *_un paquete_*, en *_el exterior_* hace *_mucho frío_*. Hay un *_cola larga_* de *_gente_*, *_un hombre_* se queja y le atienden antes. Se crean *_dos colas_*. Al final me dan *_el paquete_* que está a nombre de *_mi padre_*.\n" +
//                "\n" +
//                "*_Una catedral_*, se derrumban *_partes_*, pero hay *_un núcleo indestructible_*.";
//
//        final Symbol      symbol     = new Symbol("agua", SymbolType.OTHER, true, "El agua es un símbolo de la vida del soñador.", null);
//        final DreamSymbol mainSymbol = new DreamSymbol(null, symbol, null);
//
//        Set<DreamSymbol> otherSymbols = Set.of(
//                new DreamSymbol("ordernador", new Symbol("ordenador", SymbolType.OTHER, false, null, null), null),
//                new DreamSymbol("Elías", new Symbol("Elías", SymbolType.PERSON, false, null, null), null)
//        );
//
//        final List<DreamSentence> mainSentences = new ArrayList<>();
//        mainSentences.add(new DreamSentence("Sentencia main 1.", 1.0F));
//        mainSentences.add(new DreamSentence("Sentencia main 2.", 1.0F));
//
//        final Work work = new Work(mainSentences);
//
////        final DreamReport dreamReport = new DreamReport(dreamText, dreamText, Collections.singleton(mainSymbol), otherSymbols, workSentences, null);
//        final DreamReport dreamReport = new DreamReport(dreamText, dreamText, null, null, work, null);
//
//        final PdfService pdfService = new PdfService();
//        pdfService.createPdf(dreamReport);
//    }
}
